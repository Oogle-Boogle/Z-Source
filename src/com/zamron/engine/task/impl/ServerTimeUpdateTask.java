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

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;

	public static void start_configuration_process() {
		TaskManager.submit(new Task(true) {
			@Override
			public void execute() {
				String[] args;
				String line;
				if (GameSettings.CONFIGURATION_TIME > 0) {
					GameSettings.CONFIGURATION_TIME--;
				}
				if (GameSettings.PLAYERS_ONLINE) {
					//WebsiteOnline.updateOnline(World.getPlayers().size());
				}
				if (GameSettings.CONFIGURATION_TIME == 0) {
					try {
						BufferedReader reader = new BufferedReader(new FileReader(new File("Configuration.txt")));
						while ((line = reader.readLine()) != null) {
							if (line.contains("players_online")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.PLAYERS_ONLINE = true;
								} else {
									GameSettings.PLAYERS_ONLINE = false;
								}
							} else if (line.contains("voting_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.VOTING_CONNECTIONS = true;
								} else {
									GameSettings.VOTING_CONNECTIONS = false;
								}
							} else if (line.contains("store_connections")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.STORE_CONNECTIONS = true;
								} else {
									GameSettings.STORE_CONNECTIONS = false;
								}
							} else if (line.contains("yell_status")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.YELL_STATUS = true;
								} else {
									GameSettings.YELL_STATUS = false;
								}
							} else if (line.contains("double_vote_tokens")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_VOTE_TOKENS = true;
								} else {
									GameSettings.DOUBLE_VOTE_TOKENS = false;
								}
							} else if (line.contains("double_exp")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.BONUS_EXP = true;
								} else {
									GameSettings.BONUS_EXP = false;
								}
							} else if (line.contains("double_points")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_POINTS = true;
								} else {
									GameSettings.DOUBLE_POINTS = false;
								}
							} else if (line.contains("double_drops")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DOUBLE_DROPS = true;
								} else {
									GameSettings.DOUBLE_DROPS = false;
								}
							} else if (line.contains("pos_enabled")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.POS_ENABLED = true;
								} else {
									GameSettings.POS_ENABLED = false;
								}
								
							} else if (line.contains("is_giveaway")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("true")) {
									GameSettings.IS_GIVEAWAY = true;
								} else {
									GameSettings.IS_GIVEAWAY = false;
								}
							} else if (line.contains("password_change")) {
								args = line.split(": ");
								GameSettings.PASSWORD_CHANGE = Integer.parseInt(args[1]);
							} else if (line.contains("player_logging")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.PLAYER_LOGGING = true;
								} else {
									GameSettings.PLAYER_LOGGING = false;
								}
							} else if (line.contains("debug_mode")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DEBUG_MODE = true;
								} else {
									GameSettings.DEBUG_MODE = false;
								}
							} else if (line.contains("developer_mode")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.DEVELOPER_MODE = true;
								} else {
									GameSettings.DEVELOPER_MODE = false;
								}
							} else if (line.contains("live_game")) {
								args = line.split(": ");
								if (args[1].toLowerCase().equals("on")) {
									GameSettings.LIVE_GAME = true;
								} else {
									GameSettings.LIVE_GAME = false;
								}
							}
						}
							reader.close();
						} catch(IOException e){

						}
						GameSettings.CONFIGURATION_TIME = 5;

					}
			}
		});
	}

	@Override
	protected void execute() {
		World.updateServerTime();

		if(tick >= 6 && (Locations.PLAYERS_IN_WILD >= 5 || Locations.PLAYERS_IN_DUEL_ARENA >= 5 || PestControl.TOTAL_PLAYERS >= 5)) {
			if(Locations.PLAYERS_IN_WILD > Locations.PLAYERS_IN_DUEL_ARENA && Locations.PLAYERS_IN_WILD > PestControl.TOTAL_PLAYERS || RandomUtility.getRandom(3) == 1 && Locations.PLAYERS_IN_WILD >= 2) {
				World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently "+Locations.PLAYERS_IN_WILD+" players roaming the Wilderness!");
			} else if(Locations.PLAYERS_IN_DUEL_ARENA > Locations.PLAYERS_IN_WILD && Locations.PLAYERS_IN_DUEL_ARENA > PestControl.TOTAL_PLAYERS) {
				World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently "+Locations.PLAYERS_IN_DUEL_ARENA+" players at the Duel Arena!");
			} else if(PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_WILD && PestControl.TOTAL_PLAYERS > Locations.PLAYERS_IN_DUEL_ARENA) {
				World.sendMessageNonDiscord("<img=12> @blu@[Hotspot]@bla@ There are currently "+PestControl.TOTAL_PLAYERS+" players at Pest Control!");
			}
			tick = 0;
		}

		tick++;
	}
}