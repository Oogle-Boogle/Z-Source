package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class LMSRangeBox {
	
	public static void boxRewards(Player player) {
		
		int roll = RandomUtility.exclusiveRandom(100);
		
		if(roll >= 0 && roll <= 80) {
			player.getInventory().add(11235, 1);
			player.getInventory().add(11212, Misc.getRandom(100));
			player.sendMessage("@blu@You were unlucky and got the worst items in the box :(");
		} else if(roll >= 81 && roll <= 98) {
			player.getInventory().add(12926, 1);
			player.getInventory().add(11230, Misc.getRandom(250));
			player.sendMessage("@blu@You very lucky and got the 2nd best items in the box!");
		} else {
			player.getInventory().add(896, 1);
			player.getInventory().add(15243, Misc.getRandom(50));
			player.sendMessage("@blu@You were insanely lucky and got the best items available from this box!");
		}
	}
		
		public static void openBox(Player player) {
			
			if(player.getInventory().getFreeSlots() < 5) {
				player.sendMessage("@blu@You need atleast 5 free inventory slots to open this box.");
				return;
			}
			player.getInventory().delete(3903, 1);
			boxRewards(player);
	}
}
