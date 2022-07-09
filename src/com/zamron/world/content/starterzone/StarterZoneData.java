package com.zamron.world.content.starterzone;

import com.zamron.model.Item;

public enum StarterZoneData
{
	BOMBY_MINION(0, 1265, 10, new Item(995), new Item(773)),
	DONUT_HOMER(1, 2437, 25, new Item(7104), new Item(21013, 1), new Item(11559, 5)),
	ALIEN(2, 1677, 50, new Item(5022, 10000), new Item(7118, 2), new Item(21013,1)),
	BOWSER(3, 1880, 75, new Item(20102), new Item(7120), new Item(455, 2)),
	MEWTWO(4, 1459, 100, new Item(5157), new Item(7118, 5), new Item(7114)),
	BANDICOOT(5, 4392, 150, new Item(20695), new Item(20900, 5), new Item(2717, 5));

	final int key;
	final int npcId;
	final int killsRequired;
	final Item[] itemRewards;

	StarterZoneData(int key, int npcId, int killsRequired, Item... itemRewards) {
		this.key = key;
		this.npcId = npcId;
		this.killsRequired = killsRequired;
		this.itemRewards = itemRewards;
	}

	public static StarterZoneData[] values = StarterZoneData.values();

	public static StarterZoneData getForNpc(int npcId)
	{
//		if (npcId == 8146) { // knuckles
//			return SONIC;
//		}
		for (StarterZoneData data : values) {
			if (data.npcId == npcId) {
				return data;
			}
		}
		return null;
	}

	public StarterZoneData next()
	{
		return values[Math.min(ordinal()+1, values.length - 1)];
	}
}
