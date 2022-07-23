package com.zamron.net;

import com.zamron.GameSettings;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketBuilder;
import com.zamron.net.packet.PacketConstants;
import com.zamron.net.packet.PacketListener;
import com.zamron.net.packet.codec.PacketDecoder;
import com.zamron.net.packet.codec.PacketEncoder;
import com.zamron.net.packet.impl.ExamineNpcPacketListener;
import com.zamron.net.packet.impl.PlayerRelationPacketListener;
import com.zamron.net.security.IsaacRandom;
import com.zamron.world.content.minimes.MiniMeData;
import com.zamron.world.content.minimes.MiniMeFunctions;
import com.zamron.world.entity.impl.player.Player;
import org.jboss.netty.channel.Channel;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscArrayQueue;

import static com.zamron.net.login.LoginResponses.LOGIN_SUCCESSFUL;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class PlayerSession {

    /**
     * The queue of messages that will be handled on the next sequence.
     */
    private final MessagePassingQueue<Packet> messages = new MpscArrayQueue<>(GameSettings.DECODE_LIMIT);

    /**
     * The channel that will manage the connection for this player.
     */
    private final Channel channel;

    /**
     * The player I/O operations will be executed for.
     */
    private Player player;

    /**
     * The current state of this I/O session.
     */
    private volatile SessionState state = SessionState.CONNECTED;

    private final IsaacRandom encodingCipher;
    private final IsaacRandom decodingCipher;

    private final MessagePassingQueue.Consumer<Packet> messageConsumer = message -> {
        try {
            int op = message.getOpcode();

            PacketListener listener = PacketConstants.PACKETS[op];
            if (op != 11 && op != 60 && op != 5 && op != 12 && op != 103 && op != 230 && op != 4 && op != 98 && op != 164 && op != 248 && !(listener instanceof PlayerRelationPacketListener)) {
                if (message.getLength() != PacketConstants.MESSAGE_SIZES[op]) {
                    System.out.println("Player " + player.getUsername() + " ignored packet opcode: " + op + ", size: " + message.getLength() + ", actual size: " + PacketConstants.MESSAGE_SIZES[op]);
                    return;
                }
            }

            listener.handleMessage(player, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * Creates a new {@link PlayerSession}.
     *
     * @param key      the selection key registered to the selector.
     * @param response the current login response for this session.
     */
    public PlayerSession(Channel channel, IsaacRandom encodingRandom, IsaacRandom decodingRandom) {
        this.channel = channel;
        this.encodingCipher = encodingRandom;
        this.decodingCipher = decodingRandom;
    }

    public void setLoggedIn() {
        channel.write(new PacketBuilder()
                .put((byte) LOGIN_SUCCESSFUL)
                .put((byte) player.getRights().ordinal())
                .put((byte) 0).toPacket());

        channel.getPipeline().replace("encoder", "encoder", new PacketEncoder(encodingCipher));
        channel.getPipeline().replace("decoder", "decoder", new PacketDecoder(decodingCipher));

        setState(SessionState.LOGGED_IN);
    }

    /**
     * Queues the {@code msg} for this session to be encoded and sent to the
     * client.
     *
     * @param msg the message to queue.
     */
    public void queueMessage(PacketBuilder msg) {
        try {
            if (!channel.isOpen())
                return;
            channel.write(msg.toPacket());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Processes all of the queued messages from the {@link PacketDecoder} by
     * polling the internal queue, and then handling them via the handleInputMessage.
     */
    public int handleQueuedMessages() {
        return messages.drain(messageConsumer, GameSettings.DECODE_LIMIT);
    }

    /**
     * Uses state-machine to handle upstream messages from Netty.
     *
     * @param msg the message to handle.
     */
    public boolean handleIncomingMessage(Packet msg) {
        return messages.offer(msg);
    }

    public void clearMessages() {
        messages.clear();
    }

    /**
     * Gets the player I/O operations will be executed for.
     *
     * @return the player I/O operations.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the current state of this I/O session.
     *
     * @return the current state.
     */
    public SessionState getState() {
        return state;
    }

    /**
     * Sets the value for {@link PlayerSession#state}.
     *
     * @param state the new value to set.
     */
    public void setState(SessionState state) {
        this.state = state;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
