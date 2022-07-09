package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class LMSRuneBox {
	
		public static void boxRewards(Player player) {
			
				player.getInventory().add(560, Misc.getRandom(100));
				player.getInventory().add(9075, Misc.getRandom(100));
				player.getInventory().add(557, Misc.getRandom(100));
				player.getInventory().add(565, Misc.getRandom(100));
				player.getInventory().add(562, Misc.getRandom(100));
				player.getInventory().add(555, Misc.getRandom(100));
				player.getInventory().add(556, Misc.getRandom(100));
				player.getInventory().add(4675, 1);
				player.sendMessage("@blu@You open the box and get some runes + a staff.");
		}
			
			public static void openBox(Player player) {
				
				if(player.getInventory().getFreeSlots() < 5) {
					player.sendMessage("@blu@You need atleast 5 free inventory slots to open this box.");
					return;
				}
				player.getInventory().delete(3902, 1);
				boxRewards(player);
		}
	}
