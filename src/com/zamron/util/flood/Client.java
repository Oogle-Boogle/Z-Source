package com.zamron.util.flood;

import com.zamron.GameServer;
import com.zamron.GameSettings;
import com.zamron.net.login.LoginResponses;
import com.zamron.net.security.IsaacRandom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * Represents a client which will attempt
 * to connect to the server.
 * <p>
 * This can be used to stresstest the server.
 * <p>
 * Note: Code was copy+pasted from client.
 * I've barely touched it.
 *
 * @author Professor Oak
 */
public class Client {

    private final Flooder flooder;

    public Client(Flooder flooder, String username, String password) {
        this.flooder = flooder;
        this.username = username;
        this.password = password;
    }

    private final String username;
    private final String password;
    private Buffer incoming, login;
    private ByteBuffer outgoing;
    private BufferedConnection socketStream;
    private long serverSeed;
    private IsaacRandom encryption;
    public boolean loggedIn;

    public void attemptLogin() throws Exception {
        login = Buffer.create();
        incoming = Buffer.create();
        outgoing = ByteBuffer.create(5000, false, null);
        socketStream = new BufferedConnection(openSocket(GameSettings.GAME_PORT));

        outgoing.putByte(14); //REQUEST
        outgoing.putByte(0);
        socketStream.queueBytes(2, outgoing.getBuffer());

        System.out.println("opened " + username);

        socketStream.flushInputStream(incoming.payload, 17);
        incoming.currentPosition = 0;

        incoming.readLong();
        int response = incoming.readUnsignedByte();

        //Our encryption for outgoing messages for this player's session
        IsaacRandom cipher = null;

        if (response == 0) {
            serverSeed = incoming.readLong(); // aka server session key
            System.out.println("server seed " + serverSeed + " for " + username);
            int[] seed = new int[4];
            seed[0] = (int) (Math.random() * 99999999D);
            seed[1] = (int) (Math.random() * 99999999D);
            seed[2] = (int) (serverSeed >> 32);
            seed[3] = (int) serverSeed;
            outgoing.resetPosition();
            outgoing.putByte(10);
            outgoing.putInt(seed[0]);
            outgoing.putInt(seed[1]);
            outgoing.putInt(seed[2]);
            outgoing.putInt(seed[3]);
            outgoing.putInt((350 >> 2240));
            outgoing.putString(username);
            outgoing.putString(password);
            outgoing.putString(UUID.randomUUID().toString());
            outgoing.putString("sd-as-as-ss");
            outgoing.putShort(222);
            outgoing.putByte(0);
            outgoing.encryptRSAContent();
            final int blockLength = outgoing.getPosition();

            login.currentPosition = 0;
            login.writeByte(16); //18 if reconnecting, we aren't though
            login.writeByte(1 + 2 + 1 + (9 * 4) + blockLength); // size of the login block
            login.writeByte(255);
            login.writeShort(GameSettings.GAME_VERSION); //Client version
            login.writeByte(0); // low mem
            for (int i = 0; i < 9; i++) // crcs
                login.writeInt(0);
            login.writeBytes(outgoing.getBuffer(), blockLength, 0);
            cipher = new IsaacRandom(seed);
            for (int index = 0; index < 4; index++)
                seed[index] += 50;

            encryption = new IsaacRandom(seed);
            socketStream.queueBytes(login.currentPosition, login.payload);

            response = socketStream.read();
        }

        if (response != LoginResponses.LOGIN_SUCCESSFUL) {
            System.err.println("login response " + response + " for " + username);
            return;
        }

        int rights = socketStream.read();
        int dunno = socketStream.read();

        loggedIn = true;
        outgoing = ByteBuffer.create(5000, false, cipher);
        incoming.currentPosition = 0;

        if (!flooder.clients.add(this)) throw new Exception("already contains " + username);

        System.out.println("logged in " + username + " (rights=" + rights + ")");
    }

    int pingCounter = 0;

    public void process() throws Exception {
        if (loggedIn) {
			/*for(int i = 0; i < 5; i++) {
				if(!readPacket())
					break;
			}*/
            if (pingCounter++ >= 25) {
                outgoing.resetPosition();
                //Basic packet ping to keep connection alive
                outgoing.putOpcode(0);
                if (socketStream != null) {
                    socketStream.queueBytes(outgoing.bufferLength(), outgoing.getBuffer());
                }
                pingCounter = 0;
            }
        }
    }

    private boolean readPacket() throws Exception {
        if (socketStream == null) {
            return false;
        }

        int available = socketStream.available();
        if (available < 2) {
            return false;
        }

        int opcode = -1;
        int packetSize = -1;

        //First we read opcode...
        if (opcode == -1) {

            socketStream.flushInputStream(incoming.payload, 1);

            opcode = incoming.payload[0] & 0xff;

            if (encryption != null) {
                opcode = opcode - encryption.nextInt() & 0xff;
            }

            //Now attempt to read packet size..
            socketStream.flushInputStream(incoming.payload, 2);
            packetSize = ((incoming.payload[0] & 0xff) << 8)
                    + (incoming.payload[1] & 0xff);

        }

        if (!(opcode >= 0 && opcode < 256)) {
            opcode = -1;
            return false;
        }

        incoming.currentPosition = 0;
        socketStream.flushInputStream(incoming.payload, packetSize);

        switch (opcode) {

        }
        return false;
    }

    private Socket openSocket(int port) throws IOException {
        return new Socket(InetAddress.getByName("localhost"), port);
    }
}