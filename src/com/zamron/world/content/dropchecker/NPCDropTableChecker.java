package com.zamron.world.content.dropchecker;

import java.util.*;
import java.util.Map.Entry;

import com.zamron.model.Item;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.model.input.impl.ItemSearch;
import com.zamron.model.definitions.NPCDrops.DropChance;
import com.zamron.model.definitions.NPCDrops.NpcDropItem;
import com.zamron.world.entity.impl.player.Player;

/**
 * NPCDropTableChecker Handles the NPC drop table checker interface actions.
 */
public class NPCDropTableChecker {

	/**
	 * The single instance of the NPCDropTableChecker class.
	 */
	private static final NPCDropTableChecker SINGLETON = new NPCDropTableChecker();

	/**
	 * A {@link List} of {@link Integer}s holding all the NPC identifiers in
	 * alphabetical order of their respected NPC name.
	 */
	private final List<Integer> dropTableNpcIds = new ArrayList<>();


	/**
	 * A {@link List} of {@link String}s holding all the NPC names in alphabetical
	 * order.
	 */
	public final List<String> dropTableNpcNames = new ArrayList<>();

	/**
	 * Creates a new NPCDropTableChecker instance.
	 */
	private NPCDropTableChecker() {
		initialize();
	}

	/**
	 * Initializes this class by grabbing all the required data from the
	 * {@link NPCDropManager} class.
	 */
	private void initialize() {
		for (int npcId : NPCDrops.getDrops().keySet()) {
			if (npcId < 0) {
				continue;
			}
			boolean addToList = true;
			String name = getNPCName(npcId);
			for (int i : dropTableNpcIds) {
				if (getNPCName(i).equalsIgnoreCase(name) || name.equals("null") || npcId == 1234) {
					addToList = false;
					break;
				}
			}
			if (addToList) {
				dropTableNpcIds.add(npcId);
			}
		}
		Collections.sort(dropTableNpcIds, (Integer s1, Integer s2) -> getNPCName(s1).compareTo(getNPCName(s2)));
		for (int npcId : dropTableNpcIds) {

			dropTableNpcNames.add(getNPCName(npcId));
			savedNames.add(getNPCName(npcId));
		}
	}

	public void searchForItem(Player player, String item_name) {
		Collection<Integer> npc_list = NPCDrops.getNpcList(item_name);
		if (npc_list.isEmpty())
			return;
		showNPCDropTable1(player, npc_list);
	}

	public void searchForNPC(Player player, String npcName) {
		npcName = (String) ItemSearch.getFixedNPCSyntax(npcName)[0];
		for (int i = 0; i < dropTableNpcNames.size(); i++) {
			if (npcName.toLowerCase().contains(dropTableNpcNames.get(i).toLowerCase())) {
				refreshDropTableChilds(player);
				showNPCDropTable(player, dropTableNpcIds.get(i));
				player.getPacketSender().sendFrame126(
						"@or1@Viewing @whi@" + dropTableNpcNames.get(i) + "@or1@ drop table.", 37602);
				player.sendMessage("@blu@Found: @red@ " + npcName.substring(0, 1).toUpperCase() + npcName.substring(1));
				return;
			}
		}
	}

	public void getActionIdForName(Player player, int npcId) {
		try {
			int[] excludedIds = {150};
			List<int[]> excludedNPCList = new ArrayList<int[]>() {
			};
			excludedNPCList.add(excludedIds);
			boolean shouldShowDropTable = true;

			if (excludedNPCList.contains(excludedIds)) {
				shouldShowDropTable = true;
			}

			NpcDefinition defs = NpcDefinition.forId(npcId);
			String name = defs.getName();

			for (int i = 0; i < dropTableNpcNames.size(); i++) {
				if (name.equalsIgnoreCase(dropTableNpcNames.get(i)) && shouldShowDropTable) {
					player.getPacketSender().sendInterface(37600);
					showNPCDropTable(player, dropTableNpcIds.get(i));
					player.getPacketSender().sendFrame126(
							"@or1@Viewing @whi@" + dropTableNpcNames.get(i) + "@or1@ drop table.",
							37602);
					player.sendMessage("Now viewing drops for Name: " + name + " Combat Lvl: " + defs.getCombatLevel());
					return;
				}
			}
		} catch (Exception e) {
			
		}
	}

	/**
	 * Refreshed the drop table names on the specified {@link Player}s drop table
	 * checker interface.
	 * 
	 * @param player
	 *            The player performing this action.
	 */

	List<String> savedNames = new ArrayList<>();

	public void refreshDropTableChilds(final Player player) {
		int childId = 37651;
		for (String npcName : dropTableNpcNames) {
			player.getPacketSender().sendFrame126(npcName, childId++);
		}
		for (int child = childId; child < 37821; child++) {
			player.getPacketSender().sendFrame126("", child);
		}
	}

	public void reinitialize(Player player) {
		int childId = 37651;
		for (String npcName : savedNames) {
			player.getPacketSender().sendFrame126(npcName, childId++);
		}
		for (int child = childId; child < 37821; child++) {
			player.getPacketSender().sendFrame126("", child);
		}
	}

	/**
	 * Handles the action buttons clicked in the npc drop table checker.
	 * 
	 * @param player
	 *            The player performing this actions.
	 * @param actionId
	 *            The actionId of the button that has been clicked.
	 * @return If the actionId is one of the action buttons in the npc drop table
	 *         checker interface. If true, it will stop further actions in the
	 *         action button method.
	 */
	public boolean handleButtonClick(final Player player, final int actionId) {
		if (actionId >= -27885 && actionId <= -27716) {
			int index = actionId + 27885;
			if (index >= dropTableNpcIds.size() || dropTableNpcIds.get(index) == null)
				return true;

			showNPCDropTable(player, dropTableNpcIds.get(index));
			player.getPacketSender().sendFrame126(
					"@or1@Viewing @whi@" + dropTableNpcNames.get(index) + "@or1@ drop table.", 37602);
			return true;
		}
		return false;
	}

	public static int arrayMax(int[] arr) {
		int max = Integer.MIN_VALUE;

		for (int cur : arr)
			max = Math.max(max, cur);

		return max;
	}

	public void showNPCDropTable(final Player player, List<Integer> npcs) {

	}
	
	public void open(Player player) {
		refreshDropTableChilds(player);
		player.getPacketSender().sendInterface(37600);
	}

	/**
	 * Opens the drop table checker interface and shows the contents of the
	 * specified npc.
	 * 
	 * @param player
	 *            The player performing this action.
	 * @param npcId
	 *            The npcId of which the drop table is viewed.
	 */
	public void showNPCDropTable1(final Player player, Collection<Integer> npcs) {
		List<Integer> copied_ids = new ArrayList<>();
		List<String> copied_names = new ArrayList<>();
		copied_ids.addAll(dropTableNpcIds);
		copied_names.addAll(dropTableNpcNames);
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().resetItemsOnInterface(37915, 60);
		player.getPacketSender().resetItemsOnInterface(37916, 60);
		player.getPacketSender().resetItemsOnInterface(37917, 60);
		player.getPacketSender().resetItemsOnInterface(37918, 60);
		player.getPacketSender().resetItemsOnInterface(37919, 60);
		player.getPacketSender().resetItemsOnInterface(37920, 60);
		player.getPacketSender().resetItemsOnInterface(37921, 60);
		player.getPacketSender().resetItemsOnInterface(37922, 60); // these are interface ID's
		dropTableNpcIds.clear();
		dropTableNpcNames.clear();
		dropTableNpcIds.addAll(npcs);
		Collections.sort(dropTableNpcIds, (Integer s1, Integer s2) -> getNPCName(s1).compareTo(getNPCName(s2)));
		for (int npcId : dropTableNpcIds) {
			dropTableNpcNames.add(getNPCName(npcId));
		}
		refreshDropTableChilds(player);
		player.getPacketSender().sendInterface(37600);
		dropTableNpcIds.addAll(copied_ids);
		dropTableNpcNames.addAll(copied_names);
	}

	/**
	 * Opens the drop table checker interface and shows the contents of the
	 * specified npc.
	 * 
	 * @param player
	 *            The player performing this action.
	 * @param npcId
	 *            The npcId of which the drop table is viewed.
	 */
	public void showNPCDropTable(final Player player, final int npcId) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().resetItemsOnInterface(37915, 60);
		player.getPacketSender().resetItemsOnInterface(37916, 60);
		player.getPacketSender().resetItemsOnInterface(37917, 60);
		player.getPacketSender().resetItemsOnInterface(37918, 60);
		player.getPacketSender().resetItemsOnInterface(37919, 60);
		player.getPacketSender().resetItemsOnInterface(37920, 60);
		player.getPacketSender().resetItemsOnInterface(37921, 60);
		player.getPacketSender().resetItemsOnInterface(37922, 60);

		Map<DropChance, List<Item>> dropTables = new HashMap<>();

		NPCDrops drops = NPCDrops.forId(npcId);
		NpcDropItem[] items = drops.getDropList();

		for (NpcDropItem item : items) {
			DropChance chance = item.getChance();
			List<Item> table = dropTables.get(chance);

			if (table == null) {
				dropTables.put(chance, table = new ArrayList<Item>());
			}

			boolean found = false;

			for (Item drop : table) {
				if (drop != null && drop.getId() == item.getId()) {
					found = true;
					break;
				}
			}

			if (!found) {

				int amount = arrayMax(item.getCount());

				if (amount >= 1 && amount <= Integer.MAX_VALUE) {
					table.add(new Item(item.getId(), amount));
				}

			}

		}

		for (Entry<DropChance, List<Item>> entry : dropTables.entrySet()) {

			DropChance chance = entry.getKey();
			List<Item> dropTable = entry.getValue();

			if (dropTable.size() == 0) {
				continue;
			}

			if (chance == DropChance.ALMOST_ALWAYS) {
				player.getPacketSender().sendItemsOnInterface(37915, 60, dropTable, false);
			} else if (chance == DropChance.COMMON) {
				player.getPacketSender().sendItemsOnInterface(37916, 60, dropTable, false);
			} else if (chance == DropChance.UNCOMMON) {
				player.getPacketSender().sendItemsOnInterface(37917, 60, dropTable, false);
			} else if (chance == DropChance.RARE) {
				player.getPacketSender().sendItemsOnInterface(37918, 60, dropTable, false);
			} else if (chance == DropChance.LEGENDARY) {
				player.getPacketSender().sendItemsOnInterface(37919, 60, dropTable, false);
			} else if (chance == DropChance.LEGENDARY_2) {
				player.getPacketSender().sendItemsOnInterface(37920, 60, dropTable, false);
			} else if (chance == DropChance.LEGENDARY_4) {
				player.getPacketSender().sendItemsOnInterface(37921, 60, dropTable, false);
			} else if (chance == DropChance.LEGENDARY_5) {
				player.getPacketSender().sendItemsOnInterface(37922, 60, dropTable, false);
			}

		}
		player.getPacketSender().sendInterface(37600);
	}

	/**
	 * Grabs the NPC name from the NPCDefinitions.
	 * 
	 * @param npcId
	 *            The npcId that you want to get the name from.
	 * @return The NPC name of the specified npcId.
	 */
	private String getNPCName(int npcId) {
		NpcDefinition def = NpcDefinition.forId(npcId);
		if (def == null) {
			return "null";
		}
		return def.getName();
	}

	/**
	 * Gets the single instance of this class.
	 * 
	 * @return The single instance of this class.
	 */
	public static NPCDropTableChecker getSingleton() {
		return SINGLETON;
	}

}