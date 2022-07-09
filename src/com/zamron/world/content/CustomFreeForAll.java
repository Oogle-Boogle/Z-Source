package com.zamron.world.content;

import com.zamron.model.Flag;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.net.packet.impl.EquipPacketListener;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.content.skill.SkillManager.Skills;
import com.zamron.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFreeForAll {

	/**
	 * @author Emerald
	 * @since 9th July 2019 Based off the old FFA [Also kept old FFA]
	 */

	public static List<String> redTeam = new ArrayList<>();
	public static List<String> blueTeam = new ArrayList<>();

	private static List<String> initialRedTeam = new ArrayList<>();
	private static List<String> initialBlueTeam = new ArrayList<>();

	public static Map<Player, String> playerMap = new HashMap<>();
	public static Map<Player, String> playersInGame = new HashMap<Player, String>();

	public static final String PLAYING = "PLAYING";
	public static final String WAITING = "WAITING";

	private static boolean gameRunning = false;
	private static boolean eventRunning = false;

	private static int waitTimer = 50;
	public static int[][] coordinates = { { 2265, 4684, 4 }, { 2261, 4699, 4 }, { 2282, 4706, 4 }, { 2282, 4689, 4 } };

	public static boolean isInSameTeam(Player player1, Player player2) {
		return redTeam.contains(player1.getUsername()) && redTeam.contains(player2.getUsername())
				|| blueTeam.contains(player1.getUsername()) && blueTeam.contains(player2.getUsername());
	}

	public static String getState(Player player) {
		return playerMap.get(player);
	}

	public static void saveOldStats(Player player) {
		Skills currentSkills = player.getSkillManager().getSkills();
		player.oldSkillLevels = currentSkills.level;
		player.oldSkillXP = currentSkills.experience;
		player.oldSkillMaxLevels = currentSkills.maxLevel;
	}

	public static boolean checkItems(Player c) {
		if (c.getInventory().getFreeSlots() != 28) {
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if (c.getEquipment().get(i).getId() > 0)
				return false;
		}
		return true;
	}

	private static void movePlayersToArena() {

		blueTeam.forEach(x -> {
			Player player = World.getPlayerByName(x);
			player.moveTo(new Position(coordinates[0][0], coordinates[0][1], coordinates[0][2]));
		});

		redTeam.forEach(x -> {
			Player player = World.getPlayerByName(x);
			player.moveTo(new Position(coordinates[1][0], coordinates[1][1], coordinates[1][2]));
		});
		PLAYERS_IN_LOBBY = 0;
		// PLAYERS_IN_LOBBY--;
	}

	public static void updateSkills(Player player) {
		player.getSkillManager().updateSkill(Skill.ATTACK);
		player.getSkillManager().updateSkill(Skill.AGILITY);
		player.getSkillManager().updateSkill(Skill.CONSTITUTION);
		player.getSkillManager().updateSkill(Skill.CONSTRUCTION);
		player.getSkillManager().updateSkill(Skill.COOKING);
		player.getSkillManager().updateSkill(Skill.CRAFTING);
		player.getSkillManager().updateSkill(Skill.DEFENCE);
		player.getSkillManager().updateSkill(Skill.DUNGEONEERING);
		player.getSkillManager().updateSkill(Skill.FARMING);
		player.getSkillManager().updateSkill(Skill.FIREMAKING);
		player.getSkillManager().updateSkill(Skill.FISHING);
		player.getSkillManager().updateSkill(Skill.FLETCHING);
		player.getSkillManager().updateSkill(Skill.HERBLORE);
		player.getSkillManager().updateSkill(Skill.HUNTER);
		player.getSkillManager().updateSkill(Skill.MAGIC);
		player.getSkillManager().updateSkill(Skill.MINING);
		player.getSkillManager().updateSkill(Skill.PRAYER);
		player.getSkillManager().updateSkill(Skill.RANGED);
		player.getSkillManager().updateSkill(Skill.RUNECRAFTING);
		player.getSkillManager().updateSkill(Skill.SLAYER);
		player.getSkillManager().updateSkill(Skill.SMITHING);
		player.getSkillManager().updateSkill(Skill.STRENGTH);
		player.getSkillManager().updateSkill(Skill.SUMMONING);
		player.getSkillManager().updateSkill(Skill.THIEVING);
		player.getSkillManager().updateSkill(Skill.WOODCUTTING);
	}

	public static int TOTAL_PLAYERS = 0;
	static int PLAYERS_IN_LOBBY = 0;

	/**
	 * @author Emerald Similiar to FFA, additional R/B teams
	 */

	public static void startEvent(String type) {
		if (!eventRunning) {
			World.sendMessageNonDiscord(
					"@or2@[Custom FFA] A " + type + " Custom FFA event has been started! Type ::customffa to join!");
			if (type == "pure") {
				pure = true;
			}
			eventRunning = true;
		}
	}

	private static boolean pure = false;

	public int[][] pureInv = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
			{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
			{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
			{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };

	public static void startGame() {
		for (Player player : playerMap.keySet()) {
			eventRunning = false;
			gameRunning = true;
			player.getPA().closeAllWindows();
			saveOldStats(player);
			player.getSkillManager().newSkillManager();
			updateSkills(player);
			player.setExperienceLocked(true);
			if (pure) {
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 40).setMaxLevel(Skill.STRENGTH, 99)
						.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99)
						.setMaxLevel(Skill.CONSTITUTION, 990);
				for (Skill skill : Skill.values()) {
					player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
							.setExperience(skill,
									SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
				}
				player.getInventory().add(1319, 1);
				player.getInventory().add(2440, 1);
				player.getInventory().add(2444, 1);
				player.getInventory().add(379, 25);
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1169, 1));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(892, 1000));
				blueTeam.forEach(x -> {
					Player playerToGive = World.getPlayerByName(x);
					playerToGive.getEquipment().set(Equipment.CAPE_SLOT, new Item(15345, 1));
					playerToGive.getEquipment().set(Equipment.CAPE_SLOT, new Item(15071, 1));
				});
				
				redTeam.forEach(x -> {
					Player playerToGive = World.getPlayerByName(x);
					playerToGive.getEquipment().set(Equipment.CAPE_SLOT, new Item(15345, 1));
					playerToGive.getEquipment().set(Equipment.CAPE_SLOT, new Item(15071, 1));
				});
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(15345, 1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725, 1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(861, 1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(1129, 1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(1099, 1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(1065, 1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(1061, 1));
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			}
			movePlayersToArena();
			player.inCustomFFALobby = false;
			player.inCustomFFA = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
			waitTimer = 50;
			// initialRedTeam = redTeam;
			// initialBlueTeam = blueTeam;
			//System.out.println("This method was called");
		}
	}

	public static void removePlayer(Player c) {
		playerMap.remove(c);
	}

	public static boolean checkEndGame() {
		if (gameRunning) {

			if (redTeam.size() == 0 || blueTeam.size() == 0) {
				return true;
			}

			// if (playerMap.size() <= 1) {
			// return true;
			// }
		}
		return false;
	}

	public static void sequence() {
		if (gameRunning) {

			if (checkEndGame()) {
				endGame();
				return;
			}
			return;
		}

		if (!eventRunning)
			return;

		if (waitTimer > 0) {
			waitTimer--;
			if (waitTimer % 100 == 0 && waitTimer > 0)
				World.sendMessageNonDiscord(
						"@or2@[Custom FFA] " + waitTimer + " seconds until FFA starts!" + " Join now @ ::customffa");
		}
		if (waitTimer <= 0 && PLAYERS_IN_LOBBY > 1) {
			if (!gameRunning)
				startGame();
		}
	}

	private static int reward = 7118;

	private static Position waitingRoom = new Position(2223, 3799, 0);

	public static void enterLobby(Player c) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (!eventRunning) {
			c.sendMessage("There is no game available right now!");
			return;
		}
		if (getState(c) == null) {
			if (checkItems(c)) {
				c.getPA().closeAllWindows();
				playerMap.put(c, WAITING);
				TOTAL_PLAYERS++;
				if (redTeam.size() > blueTeam.size()) {
					blueTeam.add(c.getUsername());
					initialBlueTeam.add(c.getUsername());
					c.sendMessage("@blu@You have been assigned to the blue team");
					waitingRoom = new Position(2223, 3799, 0);
				} else if (blueTeam.size() > redTeam.size()) {
					redTeam.add(c.getUsername());
					initialRedTeam.add(c.getUsername());
					c.sendMessage("@red@You have been assigned to the red team");
					waitingRoom = new Position(2223, 3799, 4);
				} else {
					if (chance > 50) {
						redTeam.add(c.getUsername());
						initialRedTeam.add(c.getUsername());
						c.sendMessage("@red@You have been assigned to the red team");
						waitingRoom = new Position(2223, 3799, 4);
					} else {
						c.sendMessage("@blu@You have been assigned to the blue team");
						waitingRoom = new Position(2223, 3799, 0);
						blueTeam.add(c.getUsername());
						initialBlueTeam.add(c.getUsername());
					}
				}
				c.inCustomFFALobby = true;
				c.moveTo(waitingRoom);
				PLAYERS_IN_LOBBY++;
				// c.moveTo(new Position(2223, 3799, 0));
			} else {
				c.sendMessage("Bank all your items to play Custom FFA!");
			}
		}
	}

	public static void endGame() {

		eventRunning = false;
		gameRunning = false;
		String winner = blueTeam.size() == 0 ? "@red@Red Team" : "@blu@Blue Team";
		for (Player p : playerMap.keySet()) {
			leaveGame(p);
			p.sendMessage("Gj you were the winner!");
			p.setExperienceLocked(false);
		}
		World.sendMessageNonDiscord("@or2@[Custom FFA] " + winner + " Has won the game");
		if (winner.equalsIgnoreCase("@red@Red Team")) {
			initialRedTeam.forEach(x -> {
				//System.out.println("Initial red team player name: " + x);
				Player player = World.getPlayerByName(x);
				player.getInventory().add(reward, 1).refreshItems();
			});
		}

		if (winner.equalsIgnoreCase("@blu@Blue Team")) { // this better work
			initialBlueTeam.forEach(x -> {
				//System.out.println("Initial blue team player name: " + x);
				Player player = World.getPlayerByName(x);
				player.getInventory().add(reward, 1).refreshItems();
			});
		}
		playerMap.clear();
		redTeam.clear();
		blueTeam.clear();
		initialBlueTeam.clear();
		initialRedTeam.clear();
	}

	public static void leaveOnDeath(Player player) {

	}

	public static void leaveGame(Player c) {
		if(c.inCustomFFA) {
		c.getInventory().deleteAll();
		c.getEquipment().deleteAll();
		c.getSkillManager().getSkills().level = c.oldSkillLevels;
		c.getSkillManager().getSkills().experience = c.oldSkillXP;
		c.getSkillManager().getSkills().maxLevel = c.oldSkillMaxLevels;
		updateSkills(c);
		c.moveTo(new Position(3208, 3426, 0));
		c.inCustomFFA = false;
		c.getPacketSender().sendInteractionOption("null", 2, true);
		c.sendMessage("Thank you for participating in Custom FFA!");
		c.setExperienceLocked(false);
	}
	}
}
