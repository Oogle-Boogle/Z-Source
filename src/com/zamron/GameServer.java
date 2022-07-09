package com.zamron;

import com.zamron.engine.task.impl.ServerTimeUpdateTask;
//import com.zamron.firebase.FirebaseInitalize;
//import com.zamron.firebase.FirebaseService;
import com.zamron.util.MACBanL;
import com.zamron.util.ShutdownHook;
import com.zamron.world.World;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.entity.impl.player.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The starting point of Zamron.
 * 
 * @author Gabriel
 * @author Samy
 */

public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Zamron");
	private static boolean updating;
	public static String serverHost;

	//FirebaseService firebaseService;

	static {
		try {
			serverHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	static {
		if (serverHost.contains("Leight") || serverHost.contains("DESKTOP-U45U8EF")) {
			GameSettings.DEVELOPERSERVER = true;
		}
	}

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			if (GameSettings.DEVELOPERSERVER) {
				logger.info("Launching the - DEVELOPER DEVELOPER DEVELOPER DEVELOPER - server!");
			} else {
				logger.info("Launching the - LIVE LIVE LIVE LIVE - server!");
				DiscordMessenger.sendGeneralChat("Zamron has been updated!", "Check out the update log for more information!");
				com.everythingrs.playersonline.PlayersOnline.service.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						int online = 0;
						for (Player player: World.getPlayers()) {
							if (player != null) {
								online += 1;
							}
						}
						com.everythingrs.playersonline.PlayersOnline.insert("fwvYT5w8ltI4iUiUiCDr2HPbIBdbyp17WLzAN6OCDtaCaHaD57GrV8xmcnnNMzgCb08fNSJl", online, false);
					}
				}, 0, 30, TimeUnit.SECONDS);
			}
			logger.info("Server Host Name is " + serverHost);
			loader.init();
			loader.finish();
			//logger.info("The loader has finished loading utility tasks.");
			ServerTimeUpdateTask.start_configuration_process();
			logger.info("Trying to connect to firebase database...");

			logger.info("Loaded up firebase connection: Success.");
			MACBanL.init();
			if (GameSettings.CONFIGURATION_TIME == 5) {
				logger.info("Configurations loaded.");
			}
			logger.info("Zamron is now online on IP: port " + GameSettings.GAME_PORT + "!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start Zamron Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}
}