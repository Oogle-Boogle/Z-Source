package com.zamron.world.content;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class VoteMbox {
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {15373,18950};
    public static final int[] goodRewards = {18380,18392,18381,18382,18383,18384,18385,3937,3938,3939,3944,3945,3946,3947,3948};
    public static final int[] bestRewards = {20054,19140,5185,5163,19886};
	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
		int chance = RandomUtility.exclusiveRandom(100);
		if (chance >= 0 && chance <= 70) {
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], 1);
		} else if(chance >=71 && chance <=95) {
			player.getInventory().add(goodRewards[Misc.getRandom(goodRewards.length - 1)], 1);
			player.sendMessage("You got a uncommon reward");
		} else if(chance >=96) {
			int reward = (bestRewards[Misc.getRandom(bestRewards.length -1)]);
			player.getInventory().add(reward,1);
			player.sendMessage("You got a rare reward");
			World.sendMessageNonDiscord("<img=12>@blu@[VOTE BOX]<img=12> @red@"+player.getUsername() + " @blu@Has just received a "+ ItemDefinition.forId(reward).getName()+ "!!");
		}
	}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(3824, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
