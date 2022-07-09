package com.zamron.net.packet;

import com.zamron.model.Direction;
import com.zamron.model.Position;
import com.zamron.net.packet.impl.NPCOptionPacketListener;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;

public class NpcConfigActionPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		
		int npcId = packet.readShort();
		int dir = packet.readByte();
		int xPos = packet.readShort();
		int yPos = packet.readShort();
		int zPos = packet.readByte();
		boolean coordinate = packet.readByte() == 1 ? true : false;
		int radius = packet.readByte();
		
		if(zPos == 5) { // kinda a cheaphax.
			NPCOptionPacketListener.npcConfigEditing = false;
			return;
		}
		
		if(npcId == 1) {
			NPCOptionPacketListener.npcConfigEditing = true;
			return;
		}
		Direction direction = Direction.numberToDirection(dir);
		Position position = new Position(xPos, yPos, zPos);
		Coordinator cn = new Coordinator(coordinate, radius);
		NPC.writeNpcData(npcId, direction, position, cn, true);
	}

}
