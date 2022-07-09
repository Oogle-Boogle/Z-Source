package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class LMSFoodBox {
	
		public static void boxRewards(Player player) {
				player.getInventory().add(15272, Misc.getRandom(20));
				player.sendMessage("@blu@You open the box and get some runes + a staff.");
		}
			
			public static void openBox(Player player) {
				
				if(player.getInventory().getFreeSlots() < 20) {
					player.sendMessage("@blu@You need atleast 20 free inventory slots to open this box.");
					return;
				}
				player.getInventory().delete(3886, 1);
				boxRewards(player);
		}
	}
