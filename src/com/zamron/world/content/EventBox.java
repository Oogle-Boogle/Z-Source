package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class EventBox {
	
	/*
	 * Rewards
	 */
	public static final int [] shitRewards = {5082,5083,5084,15656,15045,926,931,5211,930,5210,19886,9492,9493,9494,9495,19159,19160,19161,19163,19164,19165,19166,19691,19692,19693,19694,19695,19696,19618};
	public static final int [] goodRewards = {19886,9492,9493,9494,9495,19159,19160,19161,19163,19164,19165,19166,19691,19692,19693,19694,19695,19696,19618,19727,19728,19729,19730,19731,19732,5171,19620,19821};
	public static final int [] bestRewards = {19727,19728,19729,19730,19731,19732,5171,19620,13202,13203,13204,13206,13207,11143,11144,11145,11147,4794,4795,4796,4797,19127,19128,19129,13991,13992,13993,13994,13995,14447,14448,10905,19155,9496,9497,9498,934,3952,3950,3949,};
	
	
	public static void example(Player player) {
		int chance = RandomUtility.random(40);

		if (chance >= 0 && chance <= 25) {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		} else if (chance >= 26 && chance <= 35) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1); //
		} else if (chance >= 34 && chance <= 40) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
		}


	}

	int boxId = 13997;

	public static void openAll(Player player, int boxId) {
		int amount = player.getInventory().getAmount(boxId);
		Map<Integer, Integer> rewards = new HashMap<>();
		for (int i = 0; i < amount; i++) {
			int reward = -1;
			int chance = RandomUtility.inclusiveRandom(0, 40);

			if (chance >= 34 && chance <= 40) {
				reward = bestRewards[RandomUtility.exclusiveRandom(0, bestRewards.length)];
				/**World.sendMessageNonDiscord("<shad=bf0000>[Rare Reward]</shad>@bla@: "+player.getUsername().toString() +
				 " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(reward).getName() +
				 " </shad>@bla@from the @red@" + ItemDefinition.forId(boxId).getName() );**/
			} else if (chance >= 26 && chance <= 33) {
				reward = goodRewards[RandomUtility.exclusiveRandom(0, goodRewards.length)];
			} else if (chance >= 0 && chance <= 25) {
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
	
	/*
	 * Handles opening obv
	 */
	public static void open (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
			// Opens the box, gives the reward, deletes the box from the inventory, and sends a message to the player.
		player.getInventory().delete(13997, 1);
		example(player);
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
			
		} else {
			player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);

		}
	}
		public static void givebestReward(Player player) {
			if (RandomUtility.RANDOM.nextInt(4) == 2) {
				
			} else {
				player.getInventory().add(shitRewards[Misc.getRandom(shitRewards.length - 1)], 1);
		}
		}
		
		// just do it like this its much easier sec ill add a new method for u
		/*
		 * S
		 * M
		 * D
		 */

		public void process() {
			// TODO Auto-generated method stub
			
		}

		public void reward() {
			// TODO Auto-generated method stub
			
		}
	}
