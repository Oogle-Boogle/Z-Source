package com.zamron.net.packet;

import com.zamron.world.content.roulette.Roulette;
import com.zamron.world.entity.impl.player.Player;

public class RouletteSpinEndPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int numberLanded = packet.readShort();
		if(numberLanded == Roulette.winningNumber) {
		player.getRoulette().getWinner(numberLanded); //TODO verify that the number is same that was sent from the server
		}
	}

}
