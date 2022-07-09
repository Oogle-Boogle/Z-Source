package com.zamron.world.content;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class InfernalBox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = { 3459, 15398,18782};
    public static final int[] goodRewards = { 15357, 15361,15362 };
    public static final int[] bestRewards = { 15358, 15359, 15363, 19890,3317,4780, 19935, 19936,19140};
	
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
			//Use this method in future =) ~Love oogle <3
			int reward = (bestRewards[Misc.getRandom(bestRewards.length - 1)]);
			player.getInventory().add(reward, 1);
			player.sendMessage("You got a rare reward");
			World.sendMessageNonDiscord("<img=12>@blu@[INFERNAL BOX] @red@"+player.getUsername() + " @blu@Has just received a "+ ItemDefinition.forId(reward).getName());
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(4635, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
