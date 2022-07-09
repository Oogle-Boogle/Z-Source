package com.zamron.world.content.skill.impl.summoning;

import com.zamron.model.CombatIcon;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.entity.impl.player.Player;

/**
 * Charming imp
 * @author Kova+
 * Redone by Gabbe
 */
public class CharmingImp {

	public static final int GREEN_CHARM = 12159;
	public static final int GOLD_CHARM = 12158;
	public static final int CRIM_CHARM = 12160;
	public static final int BLUE_CHARM = 12163;

	public static void changeConfig(Player player, int index, int config) {
		player.getSummoning().setCharmImpConfig(index, config);
		player.getPacketSender().sendInterfaceRemoval().sendMessage("<img=12> <col=996633>Your configuration for "+ItemDefinition.forId(getCharmForIndex(index)).getName()+"s has been saved.");
	}

	public static boolean handleCharmDrop(Player player, int itemId, int amount) {
		int index = getIndexForCharm(itemId);
		if(index == -1) {
			return false;
		}
		switch (player.getSummoning().getCharmImpConfig(index)) {
		case 0:
			sendToInvo(player, itemId, amount);
			return true;
		case 1:
			turnIntoXp(player, itemId, amount);
			return true;
		}
		return false;
	}

	
	private static boolean sendToInvo(Player player, int itemId, int amount) {
		if (!player.getInventory().contains(itemId) && player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("Your inventory is full, the Looting Bag is unable to pick up any charms!");
			return false;
		}
		sendMessage(player, 0, itemId, amount);
		player.getInventory().add(itemId, amount);
		return true;
	}

	private static void turnIntoXp(Player player, int itemId, int amount) {
		switch (itemId) {
		case GOLD_CHARM:
			player.getSkillManager().addExperience(Skill.SUMMONING, 17544 * amount);
			break;
		case GREEN_CHARM:
			player.getSkillManager().addExperience(Skill.SUMMONING, 21444 * amount);
			break;
		case CRIM_CHARM:
			player.getSkillManager().addExperience(Skill.SUMMONING, 28877 * amount);
			break;
		case BLUE_CHARM:
			player.getSkillManager().addExperience(Skill.SUMMONING, 36544 * amount);
			break;
		}
		sendMessage(player, 1, itemId, amount);
	}

	private static void sendMessage(Player player, int config, int itemId, int amount) {
		String itemName = ItemDefinition.forId(itemId).getName();
		if(amount > 1) {
			itemName += "s";
		}
		switch (config) {
		case 0:
			player.getPacketSender().sendMessage("Your Looting Bag has looted <col=ff0000>" + amount + "</col> " + itemName + " and placed it in your inventory.");
			break;
		case 1:
			player.getPacketSender().sendMessage("Your Looting Bag looted <col=ff0000>" + amount + "</col> " + itemName + " and turned it into Summoning exp.");
			break;
		}
	}
	
	public static void sendConfig(Player player) {
		for(int i = 0; i < 4; i++) {
			int state = player.getSummoning().getCharmImpConfig(i) == 1 ? 0 : 1;
			player.getSummoning().setCharmImpConfig(i, state);
			int charm = getCharmForIndex(i);
			switch(state) {
			case 0:
				player.getPacketSender().sendMessage("<img=12> <col=996633>Your Looting Bag is placing all "+ItemDefinition.forId(charm).getName()+"s it finds in your inventory.");
				break;
			case 1:
				player.getPacketSender().sendMessage("<img=12> <col=996633>Your Looting Bag is turning all "+ItemDefinition.forId(charm).getName()+"s it finds into Summoning exp.");
				break;
			}
		}
	}

	private static int getIndexForCharm(int charm) {
		switch(charm) {
		case GOLD_CHARM:
			return 0;
		case GREEN_CHARM:
			return 1;
		case CRIM_CHARM:
			return 2;
		case BLUE_CHARM:
			return 3;
		}
		return -1;
	}
	
	private static int getCharmForIndex(int index) {
		switch(index) {
		case 0:
			return GOLD_CHARM;
		case 1:
			return GREEN_CHARM;
		case 2:
			return CRIM_CHARM;
		case 3:
			return BLUE_CHARM;
		}
		return -1;
	}

}
