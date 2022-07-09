package com.zamron.world.content;

import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.entity.impl.player.Player;

public class PetMysteryBox {

	/*
	 * Rewards
	 */
	public static final int[] shitRewards = {5148, 11991, 11992, 11993, 11994, 11989, 11988, 11987, 11986, 11985, 11984, 11995, 11996, 11997, 11997, 11978, 12001, 12002, 12003, 12004, 12005, 12006, 11990, 11983, 11982, 12177, 12319, 12315, 12325, 12444, 12445,};
	public static final int[] goodRewards = {5154, 5153, 12001, 5127, 5135, 5128, 3638, 3639, 3640, 3647, 16579, 6484, 16580, 16584, 16583, 16581, 5157, 2757, 2758, 2759, 2763, 5162};
	public static final int[] bestRewards = {12683, 12542, 6486, 4742, 19753, 4743, 4744, 4786, 4787, 1648, 1647, 22204, 1855, 2756, 5163, 5166, 18400};


	public static void example(Player player) {
		int chance = RandomUtility.random(100);

		if (chance >= 0 && chance <= 30) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		} else if (chance >= 31 && chance <= 96) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else if (chance >= 97 && chance <= 100) {
			int reward = (bestRewards[Misc.getRandom(bestRewards.length - 1)]);
			player.getInventory().add(reward, 1);
			World.sendMessageNonDiscord("[PETS] " + player.getUsername() + " has just received a rare " +ItemDefinition.forId(reward));
			
			
			//TODO Announce rare pet name
		}


	}

	/*
	 * Handles opening obv
	 */
	public static void open(Player player) {
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You need at least 1 inventory space");
			return;
		}
		// Opens the box, gives the reward, deletes the box from the inventory, and sends a message to the player.
		player.getInventory().delete(14691, 1);
		//example(player);
		example(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}
}
