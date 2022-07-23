package com.zamron.net.login;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.zamron.GameSettings;
import com.zamron.net.PlayerSession;
import com.zamron.net.packet.PacketBuilder;
import com.zamron.net.packet.codec.PacketDecoder;
import com.zamron.net.packet.codec.PacketEncoder;
import com.zamron.net.security.IsaacRandom;
import com.zamron.util.Misc;
import com.zamron.util.NameUtils;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

import static com.zamron.net.login.LoginResponses.*;

/**
 * A {@link org.niobe.net.StatefulFrameDecoder} which decodes
 * the login requests.
 *
 * @author Gabriel Hannason
 */
public final class LoginDecoder extends FrameDecoder {

    private enum State {
        CONNECTED, LOGGING_IN
    }

    private State state = State.CONNECTED;
    private long seed;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (!channel.isConnected()) {
            return null;
        }
        buffer.markReaderIndex();
        //System.err.println("decode " + channel.getRemoteAddress() + " state=" + state);
        switch (state) {
            case CONNECTED:
                if (buffer.readableBytes() < 2) {
                    buffer.resetReaderIndex();
                    return null;
                }
                int request = buffer.readUnsignedByte();
                if (request != 14) {
                    //System.err.println("Invalid login request: " + request);
                    channel.close();
                    return null;
                }
                buffer.readUnsignedByte();
                seed = new SecureRandom().nextLong();
                channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(seed).toPacket());
                state = State.LOGGING_IN;
                return null;
            case LOGGING_IN:
                if (buffer.readableBytes() < 2) {
                    buffer.resetReaderIndex();
                    return null;
                }
                int loginType = buffer.readByte();
                if (loginType != 16 && loginType != 18) {
                    //System.err.println("Invalid login type: " + loginType);
                    channel.close();
                    return null;
                }
                int blockLength = buffer.readByte() & 0xff;
                if (buffer.readableBytes() < blockLength) {
                    buffer.resetReaderIndex();
                    return null;
                }
                int magicId = buffer.readUnsignedByte();
                if (magicId != 0xFF) {
                    //System.err.println("Invalid magic id");
                    channel.close();
                    return null;
                }
                int clientVersion = buffer.readShort();
                //System.err.println("Client version = " + clientVersion);
                int memory = buffer.readByte();
                if (memory != 0 && memory != 1) {
                    //System.err.println("Unhandled memory byte value");
                    channel.close();
                    return null;
                }
                int[] archiveCrcs = new int[9];
                for (int i = 0; i < 9; i++) {
                    archiveCrcs[i] = buffer.readInt();
                }
                //System.err.println("crcs " + Arrays.toString(archiveCrcs));
                int length = buffer.readUnsignedByte();
                //System.err.println("length " + length + " vs " + buffer.readableBytes());
                /**
                 * Our RSA components.
                 */
                ChannelBuffer rsaBuffer = buffer.readBytes(length);
                BigInteger bigInteger = new BigInteger(rsaBuffer.array());
                bigInteger = bigInteger.modPow(GameSettings.RSA_EXPONENT, GameSettings.RSA_MODULUS);
                rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
                int securityId = rsaBuffer.readByte();
                if (securityId != 10) {
                    //System.err.println("securityId id != 10.");
                    channel.close();
                    return null;
                }
                long clientSeed = rsaBuffer.readLong();
                long seedReceived = rsaBuffer.readLong();
                if (seedReceived != seed) {
                    //System.err.println("Unhandled seed read: [seed, seedReceived] : [" + seed + ", " + seedReceived + "]");
                    channel.close();
                    return null;
                }
                int[] seed = new int[4];
                seed[0] = (int) (clientSeed >> 32);
                seed[1] = (int) clientSeed;
                seed[2] = (int) (this.seed >> 32);
                seed[3] = (int) this.seed;
                IsaacRandom decodingCipher = new IsaacRandom(seed);
                for (int i = 0; i < seed.length; i++) {
                    seed[i] += 50;
                }
                IsaacRandom encodingCipher = new IsaacRandom(seed);
                //System.err.println("RSA : " + rsaBuffer);
                int uid = rsaBuffer.readInt();
                //System.err.println("uid = " + uid);
                //String MAC = Misc.readString(rsaBuffer);
                String username = Misc.readString(rsaBuffer);
                String password = Misc.readString(rsaBuffer);
                //String serial = Misc.readString(rsaBuffer);
                String uuid = Misc.readString(rsaBuffer);
                String mac = Misc.readString(rsaBuffer);
                //System.err.println("Login Decoder mac " + mac + " uuid " + uuid);

                if (username.length() > 12 || password.length() > 20) {
                    //System.err.println("Username or password length too long");
                    channel.write(new PacketBuilder().put((byte) INVALID_CREDENTIALS).toPacket())
                            .addListener(ChannelFutureListener.CLOSE);
                    return null;
                }
                username = Misc.formatText(username.toLowerCase());
                return login(channel, encodingCipher, decodingCipher,
                        new LoginDetailsMessage(username, password, uuid, mac,
                                ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress(),
                                clientVersion, uid));
        }
        return null;
    }

    public Player login(Channel channel,
                        IsaacRandom encodingCipher, IsaacRandom decodingCipher,
                        LoginDetailsMessage msg) throws IOException {

        PlayerSession session = new PlayerSession(channel, encodingCipher, decodingCipher);

        Player player = new Player(session)
                .setUsername(msg.getUsername())
                .setLongUsername(NameUtils.stringToLong(msg.getUsername()))
                .setPassword(msg.getPassword())
                .setHostAddress(msg.getHost());

        player.setMac(msg.getMac());
        player.setUuid(msg.getUuid());

        session.setPlayer(player);

        int response = LoginResponses.getResponse(player, msg);

        final boolean newAccount = response == NEW_ACCOUNT;
        if (newAccount) {
            player.setNewPlayer(true);
            response = LOGIN_SUCCESSFUL;
        }

        if (response == LOGIN_SUCCESSFUL) {
            if (!World.queueLogin(player)) {
                sendReturnCode(channel, LOGIN_COULD_NOT_COMPLETE);
                return null;
            }
            return player;
        } else {
            sendReturnCode(channel, response);
            return null;
        }
    }

    public static void sendReturnCode(final Channel channel, final int code) {
        channel.write(new PacketBuilder()
                        .put((byte) code)
                        .toPacket())
                .addListener(ChannelFutureListener.CLOSE);
    }

}
