package com.zamron.world.content;

import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SupremeMysteryBox {

	/*
	 * Rewards
	 */
	public static final int[] shitRewards = {5187, 5186, 5131, 15398, 16137,};
	public static final int[] mediumRewards = {3317, 3810, 3811, 5118, 5119};
	public static final int[] bestRewards = {15045, 926, 931, 930, 5210, 5211, 5171, 19620, 5082, 5083, 5084, 15656, 3812, 3814, 3813, 5120, 19886};


	/*
	 * Handles opening obv
	 */
	public static void open(Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15374, 1);
		openSupremeBox(player);
		player.getPacketSender().sendMessage("Congratulations on your reward!");
	}

	public static void openSupremeBox(Player player) {  // <- na mate. so a trick for you now.. if something has a shit name
		int chance = Misc.getRandom(100);
		if (chance >= 0 && chance <= 50) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
			player.sendMessage("You got a common reward");
		} else if (chance >= 51 && chance <= 89) {
			player.getInventory().add(mediumRewards[Misc.getRandom(mediumRewards.length - 1)], 1);
			player.sendMessage("You got a uncommon reward");
		} else if (chance >= 90) {
			int reward = bestRewards[Misc.getRandom(bestRewards.length - 1)];
			player.getInventory().add(reward, 1);
			World.sendMessageDiscord("<img=12>@blu@[Supreme Mystery Box] @red@" + player.getUsername() + " @blu@Has just received a " + reward + " !!!!");
		}
	}

	public void process() {
		// TODO Auto-generated method stub

	}

	public void reward() {
		// TODO Auto-generated method stub

	}

	public static void openAll(Player player, int boxId) {
		int amount = player.getInventory().getAmount(boxId);
		Map<Integer, Integer> rewards = new HashMap<>();
		for (int i = 0; i < amount; i++) {
			int reward = -1;
			int chance = RandomUtility.inclusiveRandom(0, 100);

			if (chance >= 90) {
				reward = bestRewards[RandomUtility.exclusiveRandom(0, bestRewards.length)];
			} else if (chance >= 51 && chance <= 89) {
				reward = mediumRewards[RandomUtility.exclusiveRandom(0, mediumRewards.length)];
			} else if (chance >= 0 && chance <= 50) {
				reward = shitRewards[RandomUtility.exclusiveRandom(0, shitRewards.length)]; // ye its correct.
			}

			rewards.merge(reward, 1, Integer::sum);

		}
		player.getInventory().delete(boxId, amount);
		boolean bank = amount <= player.getInventory().getFreeSlots();
		rewards.entrySet().forEach(r -> {
			if (bank) {
				player.getInventory().add(r.getKey(), r.getValue());
			} else {
				player.getBank(0).add(r.getKey(), r.getValue());
			}
		});
	}
}
