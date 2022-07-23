package com.zamron.net.packet.impl;

import com.zamron.model.PlayerRights;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.world.World;
import com.zamron.world.content.MonsterDrops;
import com.zamron.world.content.dropchecker.NPCDropTableChecker;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import static com.zamron.net.packet.impl.NPCOptionPacketListener.npcConfigEditing;


public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if(npc <= 0) {
			return;
		}


		int index = packet.readLEShortA();
		if (index < 0 || index > World.getNpcs().capacity())
			return;
		final NPC npc2 = World.getNpcs().get(index);
		player.setEntityInteraction(npc2);
		final int npcId = npc2.getId();
		NpcDefinition defs = NpcDefinition.forId(npcId);

		if (defs != null && defs.getCombatLevel() > 0 && !npcConfigEditing) {
			player.getPacketSender().sendInterface(37600);
			NPCDropTableChecker.getSingleton().getActionIdForName(player, npc);
			player.getMovementQueue().reset();
			return;
		}




		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if(npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
		}
	}

}
