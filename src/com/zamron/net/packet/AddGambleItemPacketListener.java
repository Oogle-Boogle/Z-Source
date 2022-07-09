package com.zamron.net.packet;

import com.zamron.world.entity.impl.player.Player;

public class AddGambleItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int itemId = packet.readShort();
		
		//System.out.println("Item id ffs: " + itemId);

	}

}
