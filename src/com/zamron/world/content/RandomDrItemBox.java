package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class RandomDrItemBox {
	
	private final int[] drItems = new int[] {9503, 19335,19886, 19106,};
	
	private final int BOX_ID = 3989;
	
	public final void open(final Player player) {
		
		if (canOpen(player)) {
			player.getInventory().delete(BOX_ID);
			player.getInventory().add(drItems[Misc.getRandom(drItems.length - 1)], 1);
			player.sendMessage("@blu@You open the box and get a random dr item!");
		}
	}
	
	
	private final boolean canOpen(final Player player) {
		
		return player.getInventory().getFreeSlots() > 1 && player.getInventory().contains(BOX_ID);
		
	}
	
	
}
