package com.zamron.net.packet;

import com.zamron.model.Item;
import com.zamron.model.container.ItemContainer;
import com.zamron.world.entity.impl.player.Player;

public class ShiftBankListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int itemId = packet.readShort();
		int slot = packet.readByte();
		int amount = player.getInventory().getAmountForSlot(slot);

		if (slot < 0 || slot >= player.getInventory().capacity()) {
			return;
		}

		Item clicked_item = player.getInventory().get(slot);

		if (clicked_item == null || clicked_item.getId() != itemId) { // what the fuck u looping for
			return;
		}

		//System.out.println("clicked_item= " + clicked_item.getDefinition().getName() + " amount= " + amount);
		
		ItemContainer container = player.getBank(player.getCurrentBankTab()).add(clicked_item);
		
		if (container != null) {
			//System.out.println("clicked item amount: " + clicked_item.getAmount());
			player.getInventory().delete(clicked_item);
		}

	}

}
