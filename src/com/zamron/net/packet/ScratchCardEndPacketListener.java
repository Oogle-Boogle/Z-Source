package com.zamron.net.packet;

import com.zamron.world.entity.impl.player.Player;

public class ScratchCardEndPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		if(packet.readByte() != 1)
			return;
		
		player.getScratchCard().scratching = false;
		
		player.getScratchCard().getWinnings();

	}

}
