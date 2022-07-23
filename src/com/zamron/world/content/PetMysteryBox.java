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
	public static Item[] bestRewards = { new Item(12683, 1), new Item(12542, 1), new Item(6486, 1), new Item(4742, 1), new Item(19753, 1), new Item(4743, 1), new Item(4744, 1), new Item(4786, 1),
	new Item(4787, 1), new Item(1648, 1), new Item(22204, 1), new Item(1855, 1), new Item(2756, 1), new Item(5163, 1),
	new Item(5166, 1), new Item(18400, 1)};


	public static void example(Player player) {

		Item reward = bestRewards[Misc.getRandom(bestRewards.length - 1)];
		int chance = RandomUtility.random(100);

		if (chance >= 0 && chance <= 30) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		} else if (chance >= 31 && chance <= 90) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else if (chance >= 90 && chance <= 100) {

			String itemName = (reward.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;

			player.getInventory().add(reward);
			World.sendMessageNonDiscord("<img=12><col=FF0000>[PETS] " + player.getUsername() + " has just received a rare " + itemMessage);

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
