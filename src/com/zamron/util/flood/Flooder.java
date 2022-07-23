package com.zamron.util.flood;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import com.zamron.util.Misc;

/**
 * An implementation of {@link Runnable} which creates
 * new clients and tries to connect them with the server.
 *
 * @author Professor Oak
 */
public final class Flooder implements Runnable {

    /**
     * The clients that are currently active.
     * We can use this map to distinguish fake-clients
     * from real ones.
     */
    public final Set<Client> clients = new CopyOnWriteArraySet<>();

    /**
     * Is this flooder currently running?
     */
    private volatile boolean running;

    /**
     * Starts this flooder if it hasn't
     * been started already.
     */
    public void start() {
        if (!running) {
            running = true;
            Thread thread = new Thread(this, "Flooder Thread");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    /**
     * Stops this flooder.
     * <p>
     * Any logged in clients will eventually be disconnected
     * from the server automatically for being idle.
     */
    public void stop() {
        running = false;
    }

    /**
     * Attempts to login the amount of given clients.
     *
     * @param amount
     */
    public void login(int amount) {
        //Make sure we have started before logging in clients.
        start();
        //Attempt to login the amount of bots..
        for (int i = 0; i < amount; i++) {
            try {
                String username = "bot " + i;
                String password = "bot";
                new Client(this, Misc.formatText(username), password).attemptLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted() && running) {
            long start = System.nanoTime();
            try {
                Iterator<Client> it = clients.iterator();
                while (it.hasNext()) {
                    Client client = it.next();
                    try {
                        client.process();
                    } catch (Exception e) {
                        e.printStackTrace();
                        it.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long end = System.nanoTime();
            long elapsed = end - start;
            long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsed);

            long sleepMillis = 300 - elapsedMillis;
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Flooder().login(args.length > 0 ? Integer.parseInt(args[0]) : 300);
    }
}
