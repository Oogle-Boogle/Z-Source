package com.zamron.net.packet;

import com.zamron.model.Flag;
import com.zamron.world.entity.impl.player.Player;

public class TransformToNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		int npcId = packet.readShort();
		
		//System.out.println("Received " + npcId + " from client, and now transforming");
		
		player.setNpcTransformationId(npcId);
		player.getUpdateFlag().flag(Flag.APPEARANCE);

	}

}
