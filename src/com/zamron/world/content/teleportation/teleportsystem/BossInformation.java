package com.zamron.world.content.teleportation.teleportsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zamron.model.Item;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NPCDrops.DropChance;
import com.zamron.model.definitions.NPCDrops.NpcDropItem;
import com.zamron.world.entity.impl.player.Player;

public class BossInformation {

	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
	public static void handleInformation(int buttonId, Player player) {
		player.getPacketSender().resetItemsOnInterface(36921, 80);
		for (int i = 0; i < 5; i++) {
			player.getPA().sendFrame126("", 36926 + i);
		}

		for (int i = 0; i < 8; i++) {
			player.getPA().sendFrame126("", 36931 + i);
		}
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedPosition(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 36808);
				Map<DropChance, List<Item>> dropTables = new HashMap<>();
				NPCDrops drops = NPCDrops.forId(bie.npcId);
				NpcDropItem[] items = drops.getDropList();
				
				for(NpcDropItem item : items) {
					
					DropChance chance = item.getChance();
					List<Item> table = dropTables.get(chance);
					

					
					if(table == null) {
						dropTables.put(chance, table = new ArrayList<Item>());
					}
					
					boolean found = false;
					
					for(Item drop : table) {
						if(drop != null && drop.getId() == item.getId()) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						
						int amount = arrayMax(item.getCount());
						
						if(amount >= 1 && amount <= Integer.MAX_VALUE) {
							table.add(new Item(item.getId(), amount));
						}
						
					}
					
				}
				for(Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {
					List<Item> dropTable = entry.getValue();
					
					if (dropTable.size() == 0) {
						continue;
					}
					if (bie.npcId == 7134) {
						List<Item> borkDrops = new ArrayList<>(); 
						int[] LOOT = { 13734, 987, 4717, 4719, 4721, 4723, 4715, 4713, 4711, 868, 12154, 1516, 1272,
								2358, 1457, 1459, 1514, 2358, 537, 13883, 13879, 220, 1290, 2504, 4132, 1334, 1705, 4100, 4094, 4114, 15271,
								6686, 20000, 20001, 20002, 15220, 15018, 15020, 15019, 6585, 4151, 11283, 10834,
								// Pvp armour
								13887, 13893, 13899, 13905, 13884, 13890, 13896, 13902, 13858, 13861, 13864, 13867, 13870, 13873, 13876,
								// Barrows
								11846, 11848, 11850, 11852, 11854, 11856, 16555, 16554, 12284, 18922, 19067, 10835 };
						for (int i = 0; i < LOOT.length - 1; i++) {
							borkDrops.add(new Item(LOOT[i]));
						}
						
						player.getPacketSender().sendItemsOnInterface(36921, 80, borkDrops, false);
					} else {
						player.getPacketSender().sendItemsOnInterface(36921, 80, dropTable, false);
					}					
				}
				
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 36926 + k);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param buttonId
	 * @param player
	 */
	public static void handleWildyInformation(int buttonId, Player player) {
		player.getPacketSender().resetItemsOnInterface(37921, 60);
		for (int i = 0; i < 5; i++) {
			player.getPA().sendFrame126("", 37926 + i);
		}

		for (int i = 0; i < 8; i++) {
			player.getPA().sendFrame126("", 37922 + i);
		}
		for (BossInformationEnum bie : BossInformationEnum.values()) {
			if (bie.getButtonId() == buttonId) {
				player.setSelectedPosition(bie.getPos());
				player.getPA().sendFrame126(bie.getBossName(), 37808);
				Map<DropChance, List<Item>> dropTables = new HashMap<>();
				NPCDrops drops = NPCDrops.forId(bie.npcId);
				NpcDropItem[] items = drops.getDropList();
				
				for(NpcDropItem item : items) {
					
					DropChance chance = item.getChance();
					List<Item> table = dropTables.get(chance);
					

					
					if(table == null) {
						dropTables.put(chance, table = new ArrayList<Item>());
					}
					
					boolean found = false;
					
					for(Item drop : table) {
						if(drop != null && drop.getId() == item.getId()) {
							found = true;
							break;
						}
					}
					
					if(!found) {
						
						int amount = arrayMax(item.getCount());
						
						if(amount >= 1 && amount <= Integer.MAX_VALUE) {
							table.add(new Item(item.getId(), amount));
						}
						
					}
					
				}
				
				for(Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {
					List<Item> dropTable = entry.getValue();
					
					if (dropTable.size() == 0) {
						continue;
					}
					
					player.getPacketSender().sendItemsOnInterface(37921, 60, dropTable, true);
					
				}
				for (int k = 0; k < bie.getInformation().length; k++) {
					player.getPA().sendFrame126(bie.getInformation()[k], 37926 + k);
				}
			}
		}
	}
	
	public static int arrayMax(int[] arr) {
		int max = Integer.MIN_VALUE;

	    for(int cur: arr)
	        max = Math.max(max, cur);

	    return max;
	}
}
