/*package com.platinum.model.container.impl;

import com.platinum.model.Item;
import com.platinum.model.container.ItemContainer;
import com.platinum.model.container.StackType;
import com.platinum.model.definitions.ItemDefinition;
import com.platinum.model.definitions.NPCDrops;
import com.platinum.world.entity.impl.player.Player;

/**
 * Represents a player's equipment item container.
 * 
 * @author relex lawl
 */

/*public class EquipmentWings extends ItemContainer {

	/**
	 * The Equipment constructor.
	 * @param player	The player who's equipment is being represented.
	 */
	/*public EquipmentWings(Player player) {
		super(player);
	}

	@Override
	public int capacity() {
		return 17;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, INVENTORY_INTERFACE_ID);
		//getPlayer().getPacketSender().sendString("@gre@"+ NPCDrops.getDroprate(getPlayer(),true)+"%",37469);
		//getPlayer().getPacketSender().sendString("@gre@"+ NPCDrops.getDoubleDr(getPlayer(),true)+"%",37470);

		return this;
	}

	@Override
	public EquipmentWings full() {
		return this;
	}

	/**
	 * The equipment inventory interface id.
	 */
	/*public static final int INVENTORY_INTERFACE_ID = -16162;


	/**
	 * The arrows slot.
	 */
	/*public static final int WING = 0;

	/**
	 * The arrows slot.
	 */
	/*public static final int AURA = 1;

	public boolean properEquipmentForWilderness() {
		int count = 0;
		for(Item item : getValidItems()) {
			if(item != null && item.tradeable())
				count++;
		}
		return count >= 3;
	}

	/**
	 * Gets the amount of item of a type a player has, for example, gets how many Zamorak items a player is wearing for GWD
	 * @param p		The player
	 * @param s		The item type to search for
	 * @return		The amount of item with the type that was found
	 */
	/*public static int getItemCount(Player p, String s, boolean inventory) {
		int count = 0;
		for(Item t : p.getEquipment().getItems()) {
			if(t == null || t.getId() < 1 || t.getAmount() < 1)
				continue;
			if(t.getDefinition().getName().toLowerCase().contains(s.toLowerCase()))
				count++;
		}
		if(inventory)
			for(Item t : p.getInventory().getItems()) {
				if(t == null || t.getId() < 1 || t.getAmount() < 1)
					continue;
				if(t.getDefinition().getName().toLowerCase().contains(s.toLowerCase()))
					count++;
			}
		return count;
	}
}*/
