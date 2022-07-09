package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class VIPChest {

	// Item ids that will be dropped
	public static int pvmKey = 7678;

	// useless, just needed to write down object id
	public static int chest = 4114;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 7682,7683,7684,7685,7686,7687,7688,4794,4795,4796,4797,19127,19128,19129,13991,13992,13993,13994,13995,14447,14448 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 7759,7761,7762,7763,7760,7764,7765,9496,9497,9498,9499,10905,19155,19741,19742,19743,19744,13997,15374 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = { 5561 };

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(7678)) {
			player.getInventory().delete(7678, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("Opening VIP Chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {

			player.getPacketSender().sendMessage("@red@You require a VIP Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 150) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 5561) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 200));
			} else if (commonDrop == 5561) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 5561) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(100, 5000));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 3));
			}
		} else if (random >= 181 && random <= 194) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 5561) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(1000, 2500));
			} else if (rareDrop == 5561) {
				player.getInventory().add(rareDrop, 4);
			} else {
				if(ItemDefinition.forId(rareDrop).getName().toLowerCase().contains("gem"))
					player.getInventory().add(rareDrop, Misc.inclusiveRandom(3, 5));
				else
					player.getInventory().add(rareDrop, 1);
			}
		} else if (random >= 195 && random <= 200) {
			int ultraDrops = getRandomItem(ultraLoots);
			if (ultraDrops == 5561) {
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(1500, 5000));
			} else if (ultraDrops == 5561) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessageNonDiscord("@blu@[VIP CHEST]: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the VIP chest!");
			}
		}
	}
}
