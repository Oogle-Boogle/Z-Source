package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class RaidsEasyChest {

	// Item ids that will be dropped
	public static int pvmKey = 13591;

	// useless, just needed to write down object id
	public static int chest = 54587;

	// We roll for random on scale of 1 - 200
	
	// if random is 121 - 185, they get this table
	public static int rareLoots[] = { 1499, 3973, 4800, 4801, 4802, 5079, 15012, 3951, 3316, 3931, 3958, 3959, 3960, 5186, 5187, 6584, 14559, 18750, 18751, 5131, 4770, 4771, 4772, 5209, 923, 3994, 3995, 3996 };

	//if random is 186 to 200, they get this table
	public static int ultraLoots[] = { 5132, 12605, 19720, 3910, 3909, 3908, 3907, 19886,
			3980, 3999, 4000, 4001, 5167, 15649, 15650, 15651, 15652, 15653, 15654, 15655, 3905, 4761, 4762, 4763, 4764, 4765, 5089, 18894, 926, 5210, 931, 5211, 930, 15045, 12001, 5173, 3821, 3822, 3820, 19945,
			20054, 4781, 4782, 4783, 15032, 4785, 5195, 3914, 15656, 5082, 5083, 5084, 5085, 17151, 19619, 19470, 19471, 19472, 19473, 19474, 5129, 5130,3075, 3076, 3078, 3242, 3244, 5198, 5199, 5200 };

	// not using this one
	//public static int amazingLoots[] = { 5022 };

	// if roll is 1 - 120, they get this table 
	public static int commonLoots[] = {10835};

	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	public static void openChest(Player player) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (player.getInventory().contains(13591)) {
			player.getInventory().delete(13591, 1);

			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {

					player.getPacketSender().sendMessage("@red@<shad=355>Opening Raids Chest...");
					giveReward(player);
					this.stop();

				}
			});
		} else {
			player.getPacketSender().sendMessage("@red@<shad=355>You require a Raids Key to open this chest!");
			return;
		}

	}

	// Main method for determining roll
	public static void giveReward(Player player) {
		int random = Misc.inclusiveRandom(1, 200);
		if (random >= 1 && random <= 150) {
			int commonDrop = getRandomItem(commonLoots);
			if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 20));
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, 2);
			} else if (commonDrop == 10835) {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(10, 50));
			} else {
				player.getInventory().add(commonDrop, Misc.inclusiveRandom(1, 1));
			}
		} else if (random >= 151 && random <= 194) {
			int rareDrop = getRandomItem(rareLoots);
			if (rareDrop == 10835) {
				player.getInventory().add(rareDrop, Misc.inclusiveRandom(5000, 7500));
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
				player.getInventory().add(ultraDrops, Misc.inclusiveRandom(7500, 10000));
			} else if (ultraDrops == 10835) {
				player.getInventory().add(ultraDrops, 4);
			} else {
				player.getInventory().add(ultraDrops, 1);
				World.sendMessageNonDiscord("@blu@<shad=255>[@red@<shad=355>RAID CHEST@blu@<shad=255>]@blu@<shad=200>: " + player.getUsername() + " has received " + ItemDefinition.forId(ultraDrops).getName() + " from the Raids chest!");

			}
		}
	}
}
