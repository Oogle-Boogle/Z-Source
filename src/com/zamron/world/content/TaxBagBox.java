package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Emerald
 *
 */
public class TaxBagBox {
	
	
	
	
	
	/*
	* Array of all the available rewards
	*/

	public static final int[] badRewards = {10835};
	
    /*
     * Chances for the 3 array fields
     */
	public static void boxInfo(Player player) {
			int amount = Misc.getRandom(1000);
			player.getInventory().add(badRewards[Misc.getRandom(badRewards.length - 1)], amount);
			player.sendMessage("The box contains: @red@" + amount + " 1b coins");
		}
	
	
	/*
	 * Handles the opening
	 */
	
	public static void openBox(Player player) {
		if (player.getInventory().getFreeSlots() >=3) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
			player.getInventory().delete(3912, 1);
			boxInfo(player);
		} else {
			player.sendMessage("@red@You need atleast 3 free spaces in order to open this box"); // if not sends player a msg.
		}
	}

}
