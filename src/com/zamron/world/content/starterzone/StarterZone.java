package com.zamron.world.content.starterzone;

import com.zamron.model.definitions.NpcDefinition;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import java.util.HashMap;
import java.util.Map;

public class StarterZone
{
	private final Player player;
	private final Map<Integer, Integer> keyToKillCount = new HashMap<>();
	private final StarterZoneInterface zoneInterface;
	StarterZoneData currentData = StarterZoneData.BOMBY_MINION;

	public StarterZone(Player player) {
		this.player = player;
		zoneInterface = new StarterZoneInterface(player);
	}

	public void handleKilledNPC(NPC npc) {
		StarterZoneData data = StarterZoneData.getForNpc(npc.getId());
		if (data == null) {
			return;
		}
		if (data != currentData) {
			player.sendMessage("You are not killed the right npc! You should be killing " + NpcDefinition.forId(currentData.npcId).getName());
			return;
		}
		int newKC = keyToKillCount.merge(data.key, 1, Integer::sum);
		if (newKC == data.killsRequired) {
			player.getInventory().addItemSet(data.itemRewards);
			player.sendMessage("@red@You complete the Kill count requirement and gain your reward!");
			currentData = currentData.next();
		}
		zoneInterface.refresh();
	}

	public void loadData(Map<Integer, Integer> data) {
		keyToKillCount.putAll(data);
		refreshCurrentData();
	}

	private void refreshCurrentData()
	{
		for (StarterZoneData data : StarterZoneData.values) {
			if (keyToKillCount.getOrDefault(data.key, 0) < data.killsRequired) {
				currentData = data;
				return;
			}
		}
		currentData = StarterZoneData.values[StarterZoneData.values.length - 1];
	}

	public Map<Integer, Integer> getKeyToKillCountMap()
	{
		return keyToKillCount;
	}

	public void openInterface()
	{
		zoneInterface.open();
	}

	public void closeInterface()
	{
		zoneInterface.close();
	}
}
