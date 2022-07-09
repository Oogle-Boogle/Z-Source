package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class ChristmasMysteryBox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = { 6199, 6199, 15370, 9470, 19018, 1050, 7671, 19335, 15332, 2572};
    public static final int[] goodRewards = { 14691, 15373, 19067, 15426, 14044, 2639, 18782, 14051, 5607, 14050, 14049};
    public static final int[] bestRewards = { 14603, 14595, 14602, 14605, 898, 3090, 6183, 4084, 18979};
	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 0 && chance <= 70) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
			player.sendMessage("You got a common reward");
		} else if(chance >=71 && chance <=95) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			player.sendMessage("You got a uncommon reward");
		} else if(chance >=96) {
			player.getInventory().add(bestRewards[Misc.getRandom(bestRewards.length - 1)], 1);
			player.sendMessage("You got a rare reward");
			World.sendMessageNonDiscord("<img=12>@blu@[CHRISTMAS BOX]<img=12> @red@"+player.getUsername().toString() + " @blu@Has just received a rare reward.");
		}
	}
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(4805, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
