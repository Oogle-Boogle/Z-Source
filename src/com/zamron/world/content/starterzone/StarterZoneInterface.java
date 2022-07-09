package com.zamron.world.content.starterzone;

import com.zamron.model.definitions.NpcDefinition;
import com.zamron.net.packet.PacketSender;
import com.zamron.world.entity.impl.player.Player;

class StarterZoneInterface
{
	private static final int INTERFACE_ID = 23400;
	private static final int NPC_NAME = 23403;
	private static final int PROGRESS_TEXT = 23406;
	private static final int PROGRESS_BAR = 23396;
	private static final int ITEM_CONTAINER = 23407;

	private final Player player;

	public void open() {
		player.getPacketSender().sendWalkableInterface(INTERFACE_ID, true);
		refresh();
	}

	StarterZoneInterface(Player player)
	{
		this.player = player;
	}

	public void refresh()
	{
		PacketSender out = player.getPacketSender();
		StarterZoneData current = player.starterZone.currentData;
		out.sendString(NPC_NAME, NpcDefinition.forId(player.starterZone.currentData.npcId).getName());
		int kc = player.starterZone.getKeyToKillCountMap().getOrDefault(current.key, 0);
		int percent = 100 * kc / current.killsRequired;
		out.sendInterfaceComponentMoval(percent - 100, 0, PROGRESS_BAR);
		out.sendString(PROGRESS_TEXT, kc + "/" + current.killsRequired + " | " + percent + "%");
		out.sendInterfaceItems(ITEM_CONTAINER, current.itemRewards);
	}

	public void close()
	{
		player.getPacketSender().sendWalkableInterface(INTERFACE_ID, false);
	}
}
