package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class DonationBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {20250, 12434, 20251, 20252, 20253, 14557, 14558 };
	public static final int [] goodRewards = {19067, 18896, 3286, 11605,
			3662, 17776, 3664, 3073, 20000, 20001, 20002};


	public static void getDonorBoxItems() {
		for (int shitReward : shitRewards) {
			//System.out.println("Shit Donor Box Reward Name: " + ItemDefinition.forId(shitRewards[shitReward]) + " ID: " + shitRewards[shitReward]);
		}

		for (int goodReward : goodRewards) {
			//System.out.println("Good Donor Box Reward Name: " + ItemDefinition.forId(goodRewards[goodReward]) + " ID: " + goodRewards[goodReward]);
		}
	}
	
	/*
	 * Handles the opening of the donation box
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
			//fk
		player.getInventory().delete(6183, 1);
		giveReward(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}
	
	/*
	 * Gives the reward base on misc Random chance
	 */
	public static void giveReward(Player player) {
		/*
		 * 1/3 Chance for a good reward
		 */
		if (RandomUtility.RANDOM.nextInt(3) == 2) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
		/*
		 * Adds 5m + a random amount up to 100m every box
		 * Max cash reward = 105m
		 */
		player.getInventory().add(10835, 1 + RandomUtility.RANDOM.nextInt(100));
	}

}
