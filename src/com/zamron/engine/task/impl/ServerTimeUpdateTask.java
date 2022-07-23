package com.zamron.engine.task.impl;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Locations;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.minigames.impl.PestControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

    public ServerTimeUpdateTask() {
        super(40);
    }

    private int tick = 0;

    public static void start_configuration_process() {
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                final long start = System.nanoTime();
                String[] args;
                String line;

//                try (BufferedReader reader = new BufferedReader(new FileReader("Configuration.txt"))) {
//                    while ((line = reader.readLine()) != null) {
//                        if (line.contains("players_online")) {
//                            args = line.split(": ");
//                            GameSettings.PLAYERS_ONLINE = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("voting_connections")) {
//                            args = line.split(": ");
//                            GameSettings.VOTING_CONNECTIONS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("store_connections")) {
//                            args = line.split(": ");
//                            GameSettings.STORE_CONNECTIONS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("yell_status")) {
//                            args = line.split(": ");
//                            GameSettings.YELL_STATUS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("double_vote_tokens")) {
//                            args = line.split(": ");
//                            GameSettings.DOUBLE_VOTE_TOKENS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("double_exp")) {
//                            args = line.split(": ");
//                            GameSettings.BONUS_EXP = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("double_points")) {
//                            args = line.split(": ");
//                            GameSettings.DOUBLE_POINTS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("double_drops")) {
//                            args = line.split(": ");
//                            GameSettings.DOUBLE_DROPS = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("pos_enabled")) {
//                            args = line.split(": ");
//                            GameSettings.POS_ENABLED = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("is_giveaway")) {
//                            args = line.split(": ");
//                            GameSettings.IS_GIVEAWAY = args[1].equalsIgnoreCase("true");
//                        } else if (line.contains("password_change")) {
//                            args = line.split(": ");
//                            GameSettings.PASSWORD_CHANGE = Integer.parseInt(args[1]);
//                        } else if (line.contains("player_logging")) {
//                            args = line.split(": ");
//                            GameSettings.PLAYER_LOGGING = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("debug_mode")) {
//                            args = line.split(": ");
//                            GameSettings.DEBUG_MODE = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("developer_mode")) {
//                            args = line.split(": ");
//                            GameSettings.DEVELOPER_MODE = args[1].equalsIgnoreCase("on");
//                        } else if (line.contains("live_game")) {
//                            args = line.split(": ");
//                            GameSettings.LIVE_GAME = args[1].equalsIgnoreCase("on");
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                final long end = System.nanoTime();
                final long elapsed = start - end;
                final long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsed);
                final long sleepMillis = 3000 - elapsedMillis;
                if (sleepMillis > 0) {
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }, "Server Time Update Thread");
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void execute() {
//        World.updateServerTime();
        if (tick >= 6 && (Locations.PLAYERS_IN_WILD >= 1 || Locations.PLAYERS_IN_DUEL_ARENA >= 1 || PestControl.totalPlayers >= 1)) {
            if (Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.totalPlayers || RandomUtility.getRandom(1) == 1 && Locations.PLAYERS_IN_WILD >= 1) {
                World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently " + Locations.PLAYERS_IN_WILD + " players roaming the Wilderness!");
            } else if (Locations.PLAYERS_IN_DUEL_ARENA > Locations.PLAYERS_IN_WILD && Locations.PLAYERS_IN_DUEL_ARENA > PestControl.totalPlayers) {
                World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently " + Locations.PLAYERS_IN_DUEL_ARENA + " players at the Duel Arena!");
            } else if (PestControl.totalPlayers > Locations.PLAYERS_IN_WILD && PestControl.totalPlayers > Locations.PLAYERS_IN_DUEL_ARENA) {
                World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently " + PestControl.totalPlayers + " players at Pest Control!");
            }
            tick = 0;
        }

        tick++;
    }
}