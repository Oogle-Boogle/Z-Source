package com.zamron;

import com.zamron.engine.task.impl.ServerTimeUpdateTask;
import com.zamron.util.MACBanL;
import com.zamron.util.ShutdownHook;
import com.zamron.util.flood.Flooder;
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
	private static volatile boolean updating;
	public static String serverHost;

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
						com.everythingrs.playersonline.PlayersOnline.insert("s10kavzuyiAJ2CIZ6dLY78lsehZihrk1ilAzzMgp9uP9vfLuDL968z5KSKoR1krXAGQr384P", online, false);
					}
				}, 0, 30, TimeUnit.SECONDS);
			}
			logger.info("Server Host Name is " + serverHost);
			loader.init();
			loader.finish();
			//logger.info("The loader has finished loading utility tasks.");
			ServerTimeUpdateTask.start_configuration_process();
			MACBanL.init();
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

	/**
	 * The flooder used to stress-test the server.
	 */
	private static final Flooder flooder = new Flooder();

	public static Flooder getFlooder() {
		return flooder;
	}

}