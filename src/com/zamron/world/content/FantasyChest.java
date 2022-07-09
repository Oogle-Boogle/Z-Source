package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class FantasyChest {

	// Item ids that will be dropped
	public static int pvmKey = 6083;

	// useless, just needed to write down object id
	public static int chest = 2995;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 4671,4672,4673,4670,10835,923,5209,5167,15649,15650,15653,4761,4762,4763,4764,4765,5089 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 930,15045,5210,926,931,5211,4781,4782,4783,4785,5195,15032,3321,16429,4780,10835,932,12426,6450,6451,6452,6480,6481,18950,3988,2547,2546,2545,15652,7617,19886,15026, };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 10835 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(6083)) {
			player.getInventory().delete(6083, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening UBER Chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a UBER Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 150) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 200));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 3));
			}
		} else if (random >= 151 && random <= 194) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(1000, 2500));
			} else if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 195 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(1500, 5000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessageNonDiscord("@blu@[UBER CHEST]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the Uber chest!");
			}
		}
	}
}
