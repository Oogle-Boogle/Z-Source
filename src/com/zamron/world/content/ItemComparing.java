package com.zamron.world.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.input.impl.ItemSearch;
import com.zamron.world.entity.impl.player.Player;

/**
 * 
 * @author Emerald
 * TODO: Improve code
 */

public class ItemComparing {

	public Map<Integer, String> itemData = new HashMap<>();

	private static ItemComparing SINGLETON = null;

	public int index = -1;

	public List<Integer> tabData = new ArrayList<>();

	private final String[] bonuses = { "Stab: ", "Slash: ", "Crush: ", "Magic: ", "Range: ", "Stab: ", "Slash: ", "Crush: ",
			"Magic: ", "Range: ", "Summoning: ", "Str: ", "Pray: " };

	/**
	 * 
	 * @param player
	 * @param buttonID
	 * 
	 *            Handles button or smthing
	 */

	private int firstTabIndex = 0;
	private int secondTabIndex = 0;
	public int clickNumber = 0;
	private int countItemClicks = 0;

	private List<Integer> firstTabBonuses = new ArrayList<>();
	public List<Integer> secondTabBonuses = new ArrayList<>();

	public boolean handleButton(Player player, int buttonID) { //TODO: Clean up this method(alot)
		if (!(buttonID >= -12026 && buttonID <= -11615)) {
			return false;
		}

		if (buttonID >= -12026) {
			index = 12026 + buttonID;
		}
		if (index >= 0 && index < itemData.size()) {
			if (countItemClicks == 2) {
				player.sendMessage("@red@You are already comparing 2 items");
				player.sendMessage("@red@Use the 'Reset Item' button if u'd like to compare different items");
				return false;
			}
			if (countItemClicks == 0) {
				//System.out.println("Got executed!!!");
				player.getPacketSender().sendString(56000, itemData.values().toArray(new String[200])[index]);
				countItemClicks = 1;
			} else if (countItemClicks == 1) {
				player.getPacketSender().sendString(56001, itemData.values().toArray(new String[200])[index]);
				countItemClicks = 2;
			}

			if (tabData.size() > 1) {
				player.sendMessage("@red@You are already comparing 2 items");
				player.sendMessage("@red@Click the 'Reset Items' button to reset current items in the combinator");
				return false;
			}
			if (clickNumber < 2) {
				for (int i = 0; i < 13; i++) {
					tabData.add(getItemStats(itemData.keySet().toArray(new Integer[1000])[index], firstTabIndex));
					if (firstTabIndex == 10) {
						firstTabIndex += 4;
					} else if (firstTabIndex == 12) {
						// do nothing because it's the last index
					} else {
						firstTabIndex++;
					}
					firstTabBonuses.addAll(tabData);
					tabData.clear();
				}
				clickNumber = 2;
			} else if (clickNumber == 2) {
				for (int i = 0; i < 13; i++) {
					tabData.add(getItemStats(itemData.keySet().toArray(new Integer[1000])[index], secondTabIndex));
					if (secondTabIndex == 10) {
						secondTabIndex += 4;
					} else if (secondTabIndex == 12) {

					} else {
						secondTabIndex++;
					}
					secondTabBonuses.addAll(tabData);
					tabData.clear();
				}
			}

			//System.out.println("Size atm: " + tabData.size());

		}
		return true;
	}

	public void searchItem(Player player, String searchQuery) {
		int id = 53510;
		searchQuery = (String) ItemSearch.getFixedSyntax(searchQuery)[0];
		for (String name : itemData.values()) {
			if (name.toLowerCase().contains(searchQuery.toLowerCase())) {
				player.getPacketSender().sendString(id++, name);

			}
		}
	}
	
	public void search(Player player, String itemName) {
		List<String> foundItems = new ArrayList<>();
		for(int i = 53510; i < 53985; i++) {
			player.getPacketSender().sendString(i, "");
		}
		
		for(String name : itemData.values()) {
			if (name.toLowerCase().contains(itemName.toLowerCase())) {
				foundItems.add(name);
			}
		}
		
		itemData.values().clear();
		
		for(int index = 0; index < foundItems.size(); index++) {
			int id = ItemDefinition.getItemId(foundItems.get(index));
			itemData.put(id, foundItems.get(index));
			//System.out.println("Index: " + index + " for item name: " + foundItems.get(index));
		}
		
		//System.out.println("Found items size: " + foundItems.size());
		for(int x = 0; x < foundItems.size(); x++) {
			player.getPacketSender().sendString(53510 + x, foundItems.get(x));
		}
	}

	private int getItemStats(int index, int arrayIndex) {
		ItemDefinition def = ItemDefinition.forId(index);
		int currentBonus = (int) def.getBonus()[arrayIndex];
		return currentBonus;
	}

	/**
	 * 
	 * @param player
	 * @param index
	 *            Sends / Compares item stats
	 */

	public void sendItemStats(Player player, int index) {
		int id = 56004;
		int id1 = 56019;
		for (int i = 0; i < 13; i++) {
			boolean colorToDefine = firstTabBonuses.get(i) > secondTabBonuses.get(i);
			boolean equalBonuses = firstTabBonuses.get(i) == secondTabBonuses.get(i);
			String color = equalBonuses ? "@yel@" : colorToDefine ? "@gre@" : "@red@";
			String color1 = equalBonuses ? "@yel@" : colorToDefine ? "@red@" : "@gre@";
			player.getPacketSender().sendString(id++, bonuses[i] + color + firstTabBonuses.get(i));
			player.getPacketSender().sendString(id1++, bonuses[i] + color1 + secondTabBonuses.get(i));

			if (i == 4 || i == 10) {
				id++;
				id1++;
			}
		}
	}

	/**
	 * @param player
	 *            read the method name
	 */

	public void reset(Player player) {
		int id = 56004;
		int id1 = 56019;
		player.getPacketSender().sendString(56000, "1st Item Name");
		player.getPacketSender().sendString(56001, "2nd Item Name");
		tabData.clear();
		firstTabIndex = 0;
		secondTabIndex = 0;
		clickNumber = 0;
		countItemClicks = 0;
		firstTabBonuses.clear();
		secondTabBonuses.clear();
		for (int i = 0; i < 13; i++) {
			player.getPacketSender().sendString(id++, bonuses[i]);
			player.getPacketSender().sendString(id1++, bonuses[i]);

			if (i == 4 || i == 10) {
				id++;
				id1++;
			}
		}
		player.sendMessage("@blu@Items in the combinator have been reset");
	}

	/**
	 * @param player
	 *            read the method name
	 */

	public void sendItemNames(Player player) {
		player.getPacketSender().sendInterface(53500);
		int startID = 53510;
		for (String names : itemData.values()) {
			player.getPacketSender().sendString(startID++, names);
		}
	}

	/**
	 * Just pure fun i love singletons
	 */

	public static ItemComparing getSingleton() {
		if (SINGLETON == null) {
			SINGLETON = new ItemComparing();
		}
		return SINGLETON;
	}

	/**
	 * 
	 * *not* pure fun
	 * 
	 */

	private Map<Integer, String> sortedMap(Map<Integer, String> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * read the method name
	 */

	public void loadItems() {
		Map<Integer, String> unsorted = new HashMap<>();
		for (int i = 0; i < ItemDefinition.getDefinitions().length; i++) {
			ItemDefinition def = ItemDefinition.forId(i);
			if (BonusManager.hasStats(i) && !def.getName().equalsIgnoreCase("null")
					&& !def.getDescription().equalsIgnoreCase("null") && !def.getName().contains("(b)")
					&& !def.getName().contains("class") && !def.getName().contains("deg")
					&& !def.getName().contains("javelin") && !def.getName().contains("broken")
					&& !def.getName().contains("Crystal bow") && !def.getName().contains("Dharok")
					&& !def.getName().contains("p++") && !def.getName().contains("sighted")
					&& !def.getName().startsWith("Corrupt") && !def.getName().equalsIgnoreCase("Dark bow")
					&& !def.getName().contains("Gorgonite") && !def.getName().contains("Katagon")
					&& !def.getName().contains("Primal") && !def.getName().contains("Promethium")
					&& !def.getName().contains("Zamorak godsword") && !def.getName().equals("'perfect' ring")
					&& !def.getName().equalsIgnoreCase("Abyssal whip") && !def.getName().contains("Argonite")
					&& !def.getName().contains("Guthan") && !def.getName().contains("Karil")) {
				unsorted.put(def.getId(), def.getName());
			}
		}

		itemData = sortedMap(unsorted);
		////System.out.println("Map size: " + itemData.size());

	}

	private ItemComparing() {
		initialize();
	}

	private void initialize() {

	}

}
