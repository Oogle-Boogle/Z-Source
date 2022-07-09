package com.zamron.world.content.minigames.impl;

import com.zamron.GameSettings;
import com.zamron.model.Flag;
import com.zamron.model.Item;
import com.zamron.model.MagicSpellbook;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.net.packet.impl.EquipPacketListener;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.content.skill.SkillManager.Skills;
import com.zamron.world.entity.impl.player.Player;
import java.util.HashMap;
import java.util.Map;

public class FreeForAll {
	public static int TOTAL_PLAYERS = 0;
	private static int PLAYERS_IN_LOBBY = 0;

	/**
	 * @author main
	 * 
	 * @edits Mvcrcus
	 * 
	 * @note Stores player and State
	 */

	
	public static void startEvent(String type) {
		if(!eventRunning) {
		World.sendMessageNonDiscord("@or2@[FFA] A "+type+" FFA event has been started! Type ::ffa to join!");
		if (type == "pure") {
			pure = true;
		} else if (type == "brid") {
		brid = true;
		} else {
			dharok = true;
		}
		eventRunning = true;
		}
	}
	
	public static Map<Player, String> playerMap = new HashMap<Player, String>();
	public static Map<Player, String> playersInGame = new HashMap<Player, String>();
	public static final String PLAYING = "PLAYING";
	public static final String WAITING = "WAITING";
	public String type = "";
	public static boolean pure = false;
	public int[][] pureInv = new int[][] { { Equipment.HEAD_SLOT, 1153 }, { Equipment.CAPE_SLOT, 10499 },
		{ Equipment.AMULET_SLOT, 1725 }, { Equipment.WEAPON_SLOT, 4587 }, { Equipment.BODY_SLOT, 1129 },
		{ Equipment.SHIELD_SLOT, 1540 }, { Equipment.LEG_SLOT, 2497 }, { Equipment.HANDS_SLOT, 7459 },
		{ Equipment.FEET_SLOT, 3105 }, { Equipment.RING_SLOT, 2550 }, { Equipment.AMMUNITION_SLOT, 9244 } };
	public static boolean brid = false;
	public static boolean dharok = false;
	private static boolean gameRunning = false;
	private static boolean eventRunning = false;
	private static int waitTimer = 50;
	public static int[][] coordinates = { {2265,4684,4}, {2261,4699,4}, {2282,4706,4}, {2282,4689,4}};
	public static String getState(Player player) {
		return playerMap.get(player);
	}
	public static void saveOldStats(Player player) {
		Skills currentSkills = player.getSkillManager().getSkills();
		player.oldSkillLevels = currentSkills.level;
		player.oldSkillXP = currentSkills.experience;
		player.oldSkillMaxLevels = currentSkills.maxLevel;
	}

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
				player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1169,1));
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(892,1000));
				player.getEquipment().set(Equipment.CAPE_SLOT, new Item(15345,1));
				player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1725,1));
				player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(861,1));
				player.getEquipment().set(Equipment.BODY_SLOT, new Item(1129,1));
				player.getEquipment().set(Equipment.LEG_SLOT, new Item(1099,1));
				player.getEquipment().set(Equipment.HANDS_SLOT, new Item(1065,1));
				player.getEquipment().set(Equipment.FEET_SLOT, new Item(1061,1));
				player.getEquipment().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				EquipPacketListener.resetWeapon(player);
				BonusManager.update(player);
			} else if (brid) {
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
				.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99).setMaxLevel(Skill.PRAYER, 990)
				.setMaxLevel(Skill.CONSTITUTION, 990);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
					.setExperience(skill,
							SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
		}
		player.getInventory().add(4151, 1);
		player.getInventory().add(4722, 1);
		player.getInventory().add(19111, 1);
		player.getInventory().add(14484, 1);
		player.getInventory().add(10551, 1);
		player.getInventory().add(11732, 1);
		player.getInventory().add(13262, 1);
		player.getInventory().add(6685, 1);
		player.getInventory().add(2436, 1);
		player.getInventory().add(2440, 1);
		player.getInventory().add(3024, 2);
		player.getInventory().add(385, 13);
		player.getInventory().add(560, 20000);
		player.getInventory().add(565, 20000);
		player.getInventory().add(555, 20000);
		player.getEquipment().set(Equipment.HEAD_SLOT, new Item(10828,1));
		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(10636,1));
		player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585,1));
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(15486,1));
		player.getEquipment().set(Equipment.BODY_SLOT, new Item(4712,1));
		player.getEquipment().set(Equipment.LEG_SLOT, new Item(4714,1));
		player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(6889,1));
		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462,1));
		player.getEquipment().set(Equipment.FEET_SLOT, new Item(6920,1));
		player.getEquipment().set(Equipment.RING_SLOT, new Item(2550,1));
		player.setSpellbook(MagicSpellbook.ANCIENT);
		player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
		player.getEquipment().refreshItems();
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		EquipPacketListener.resetWeapon(player);
		BonusManager.update(player);
			} else if (dharok) {
				player.getSkillManager().setMaxLevel(Skill.ATTACK, 99).setMaxLevel(Skill.STRENGTH, 99)
				.setMaxLevel(Skill.RANGED, 99).setMaxLevel(Skill.MAGIC, 99).setMaxLevel(Skill.DEFENCE, 99).setMaxLevel(Skill.PRAYER, 990)
				.setMaxLevel(Skill.CONSTITUTION, 990);
		for (Skill skill : Skill.values()) {
			player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
					.setExperience(skill,
							SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
		}
		player.getInventory().add(14484, 1);
		player.getInventory().add(2436, 1);
		player.getInventory().add(2440, 1);
		player.getInventory().add(6685, 1);
		player.getInventory().add(4718, 1);
		player.getInventory().add(3024, 2);
		player.getInventory().add(6685, 1);
		player.getInventory().add(385, 17);
		player.getInventory().add(560, 20000);
		player.getInventory().add(9075, 20000);
		player.getInventory().add(557, 20000);
		player.getEquipment().set(Equipment.HEAD_SLOT, new Item(4716,1));
		player.getEquipment().set(Equipment.CAPE_SLOT, new Item(19111,1));
		player.getEquipment().set(Equipment.AMULET_SLOT, new Item(6585,1));
		player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(4151,1));
		player.getEquipment().set(Equipment.BODY_SLOT, new Item(4720,1));
		player.getEquipment().set(Equipment.LEG_SLOT, new Item(4722,1));
		player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(13262,1));
		player.getEquipment().set(Equipment.HANDS_SLOT, new Item(7462,1));
		player.getEquipment().set(Equipment.FEET_SLOT, new Item(11732,1));
		player.getEquipment().set(Equipment.RING_SLOT, new Item(6737,1));
		player.setSpellbook(MagicSpellbook.LUNAR);
		player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
		player.getEquipment().refreshItems();
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		EquipPacketListener.resetWeapon(player);
		BonusManager.update(player);
			}
			movePlayerToArena(player);
			player.inFFALobby = false;
			player.inFFA = true;
			player.getPacketSender().sendInteractionOption("Attack", 2, true);
			waitTimer = 200;
		}
	}

	
	public static void removePlayer(Player c) {
		playerMap.remove(c);
	}
	public static boolean checkEndGame() {
		if (gameRunning) {
			
			if (playerMap.size() <= 1) {
				return true;
			}
		}
		return false;
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
				World.sendMessageNonDiscord("@or2@[FFA] "+waitTimer+" seconds until FFA starts!" +" Join now @ ::ffa");
		}
		updateGameInterface();
		if(waitTimer <= 0) {
			if(!gameRunning)
				startGame();
		}
	}
	
	private static void updateGameInterface() {
		for (Player p : playerMap.keySet()) {
			if(p == null)
				continue;
			
			String state = getState(p);
			if(state != null && state.equals(WAITING)) {
				p.getPacketSender().sendString(21006, "Time till start: "+waitTimer+"");
				p.getPacketSender().sendString(21007, "Players Ready: "+PLAYERS_IN_LOBBY+"");
				p.getPacketSender().sendString(21009, "");
			}
		}
	}
	public static boolean checkItems(Player c) {
		if (c.getInventory().getFreeSlots() != 28) {
			return false;
		}
		for (int i = 0; i < 14; i++) {
			if(c.getEquipment().get(i).getId() > 0)
				return false;
		}
		return true; 
	}
	
	private static void movePlayerToArena(Player p) {
		int random = Misc.random(3);
		p.moveTo(new Position(coordinates[random][0], coordinates[random][1], coordinates[random][2]));
		PLAYERS_IN_LOBBY--;
	}
	public static void enterLobby(Player c) {
		if (!eventRunning) {
			c.sendMessage("There is no game available right now!");
			return;			
		}		
		if(getState(c) == null) {
			if (checkItems(c)) {
			c.getPA().closeAllWindows();
			playerMap.put(c, WAITING);
			TOTAL_PLAYERS++;
			PLAYERS_IN_LOBBY++;
			c.inFFALobby = true;
			c.moveTo(new Position(2223,3799,0));
			c.sendMessage("Welcome to FFA!");
			} else {
				c.sendMessage("Bank all your items to play FFA!");
			}
		}	
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
public static final int[] ffaRewards = { 3932, 19090, 13240, 3980, 14484, 11694, 10835, 6570, 6585};
	public static void endGame() {
		eventRunning = false;
		gameRunning = false;
		for (Player p : playerMap.keySet()) {
			World.sendMessageNonDiscord("@or2@[FFA] @blu@"+Misc.formatPlayerName(p.getUsername()) + "@or2@ has won the FFA game!");
			p.sendMessage("You have won the game! PM a admin or owner for your reward!");
			leaveGame(p);
			p.sendMessage("Gj you were the winner!");
			p.setExperienceLocked(true);
		}
		playerMap.clear();
	}
	public static void leaveGame(Player c) {
		c.getInventory().deleteAll();
		c.getEquipment().deleteAll();
		playerMap.remove(c);
		c.getSkillManager().getSkills().level = c.oldSkillLevels;
		c.getSkillManager().getSkills().experience = c.oldSkillXP;
		c.getSkillManager().getSkills().maxLevel = c.oldSkillMaxLevels;
		updateSkills(c);
		c.moveTo(new Position(3208,3426,0));
		c.inFFA = false;
		c.getPacketSender().sendInteractionOption("null", 2, true);
		c.sendMessage("Thank you for participating in FFA!");
		c.setExperienceLocked(true);
	}
}
