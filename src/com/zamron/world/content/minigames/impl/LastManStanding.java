package com.zamron.world.content.minigames.impl;

import java.util.HashMap;
import java.util.Map;

import com.zamron.GameSettings;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.skill.SkillManager.Skills;
import com.zamron.world.entity.impl.player.Player;

public class LastManStanding {

	public static int totalPlayers = 0;
	public static int playersInLobby = 0;
	public static boolean eventRunning = false;
	public static boolean gameRunning = false;
	public static Map<Player, String> playerMap = new HashMap<Player, String>();
	public static Map<Player, String> playersInGame = new HashMap<Player, String>();
	private static int waitTimer = 500;
	public static final String PLAYING = "PLAYING";
	public static final String WAITING = "WAITING";
	public static Position lmsRespawn = new Position(2775, 9338, 0);
	public static Position spawn1 = new Position(2778, 9337, 0);
	public static Position spawn2 = new Position(2792, 9320, 0);
	public static Position spawn3 = new Position(2807, 9338, 0);
	public static Position spawn4 = new Position(2807, 9305, 0);
	public static Position spawn5 = new Position(2786, 9305, 0);
	public static Position spawn6 = new Position(2766, 9297, 0);

	public static String getState(Player player) {
		return playerMap.get(player);
	}

	public static void sequence() {
		if (gameRunning) {
			
			if(checkEndGame()) {
				endGame();
				return;
			}
			return;
		}
			
	
		if (!eventRunning)
			return;

		if(waitTimer > 0) { 
			waitTimer--;
			if (waitTimer % 100 == 0 && waitTimer > 0)
				World.sendMessageNonDiscord("@blu@[LAST MAN STANDING] "+waitTimer / 100 +" minutes until Last Man Standing starts!" +" Join now @ ::lms");
		}
		if(waitTimer <= 0 && playersInLobby >= 2) {
			if(!gameRunning)
				startGame();
		}
		if(waitTimer <= 0 && playersInLobby < 2 && !gameRunning ) {
			World.sendMessageNonDiscord("@blu@[LAST MAN STANDING]@red@ Not enough players, so the timer has been reset.");
			World.sendMessageNonDiscord("@blu@[LAST MAN STANDING]@red@ ::lms to join the lobby.");
			waitTimer = 300;
		}
	}

	public static void enterLobby(Player player) {
		if (!eventRunning) {
			player.sendMessage("There is no game available right now!");
			return;
		}
		if (getState(player) == null) {
			if (checkItems(player)) {
				player.getPA().closeAllWindows();
				playerMap.put(player, WAITING);
				totalPlayers++;
				playersInLobby++;
				player.inLMSLobby = true;
				player.moveTo(new Position(2223, 3799, 0));
				player.sendMessage("Welcome to Last Man Standing!");
			} else {
				player.sendMessage("Bank all your items to play Last Man Standing!");
			}
		}

	}

	public static boolean checkEndGame() {
		if (gameRunning) {

			if (playerMap.size() <= 1) {
				return true;
			}
		}
		return false;
	}

	public static void removePlayer(Player player) {
		playerMap.remove(player);
	}

	public static void startEvent() {
		if (!eventRunning) {
			eventRunning = true;
			World.sendMessageNonDiscord("@red@[LAST MAN STANDING]@blu@ Event has started, ::lms to join!");
		}
	}
	
	public static void leaveLobby(Player player) {
		playerMap.remove(player, WAITING);
		totalPlayers--;
		playersInLobby--;
		player.inLMSLobby = false;
		player.moveTo(GameSettings.DEFAULT_POSITION);
	}

	public static void startGame() {
		for (Player player : playerMap.keySet()) {
			Skills currentSkills = player.getSkillManager().getSkills();
			player.oldSkillLevels = currentSkills.level;
			player.oldSkillXP = currentSkills.experience;
			player.oldSkillMaxLevels = currentSkills.maxLevel;
			eventRunning = false;
			gameRunning = true;
			player.getPA().closeAllWindows();
			player.setExperienceLocked(true);
			moveToArena(player);
			
			player.inLMSLobby = false;
			player.inLMS = true;
			player.getInventory().add(19104, 10);
			player.getInventory().add(15272, 10);
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
			player.setLMSLifes(5);

		}
	}

	public static boolean checkItems(Player player) {
		if (player.getInventory().getFreeSlots() != 28) {
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().get(i).getId() > 0)
				return false;
		}
		return true;
	}

	public static void moveToArena(Player player) {
		int chance = RandomUtility.exclusiveRandom(5);
		switch(chance) {
		case 0:
			player.moveTo(spawn1);
			break;
		case 1:
			player.moveTo(spawn2);
			break;
		case 2:
			player.moveTo(spawn3);
			break;
		case 3:
			player.moveTo(spawn4);
			break;
		case 4:
			player.moveTo(spawn5);
			break;
		case 5:
			player.moveTo(spawn6);
			break;
		}
		playersInLobby--;
	}

	public static void leaveGame(Player player) {
		player.getInventory().deleteAll();
		player.getEquipment().deleteAll();
		playerMap.remove(player);
		player.inLMS = false;
		player.moveTo(GameSettings.DEFAULT_POSITION);
		player.getPacketSender().sendInteractionOption("null", 2, true);
		player.setExperienceLocked(false);
		player.sendMessage("@blu@Thanks for participating in the Last Man Standing minigame");

	}

	public static void endGame() {
		eventRunning = false;
		gameRunning = false;
		for (Player player : playerMap.keySet()) {
			World.sendMessageNonDiscord(
					"@blu@[LAST MAN STANDING] @blu@" + Misc.formatPlayerName(player.getUsername()) + "@red@ has won the Last Man Standing game!");
			leaveGame(player);
			player.sendMessage("Gj you were the winner!");
			player.incrementLmsPoints(1);
			player.sendMessage("@blu@You now have: @red@" + player.getLmsPoints() + " @blu@Last Man Standing Points");
			
		}
		playerMap.clear();
	}

}
