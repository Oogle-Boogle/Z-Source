package com.zamron.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.entity.impl.player.Player;

public class BestItems {
	public BestItems(Player player) {
		this.player = player;
	}
	public List<ItemDefinition> definitions = new ArrayList<>();
	@SuppressWarnings("unchecked")
	public List<Integer> itemsToIgnore = new ArrayList() {
		{
			add(7995);
			add(22185);
		}
	};
	private int definitionIndex;
	public void fillDefinitions() {
		Collections.addAll(definitions, ItemDefinition.getDefinitions());
		definitions.removeIf(def -> def == null || itemsToIgnore.contains(def.getId()));
		
		player.getPA().sendString(56010, "Check Stab");
		player.getPA().sendString(56011, "Check Slash");
		player.getPA().sendString(56012, "Check Crush");
		player.getPA().sendString(56013, "Check Magic");
		player.getPA().sendString(56014, "Check Range");
		
		player.getPA().sendString(56015, "Check Stab");
		player.getPA().sendString(56016, "Check Slash");
		player.getPA().sendString(56017, "Check Crush");
		player.getPA().sendString(56018, "Check Magic");
		player.getPA().sendString(56019, "Check Range");
		player.getPA().sendString(56020, "");
		
		player.getPA().sendString(56022, "Check Strength");
		player.getPA().sendString(56023, "Check Ranged Str");
		player.getPA().sendString(56024, "Check Magic Damage");
	}
	public void open() {
		definitionIndex = 0;
		definitions.sort(new sortDefinitions(definitionIndex).reversed());
		//System.out.println(definitions.get(0).getName());
		displayBonuses();
		player.getPA().sendInterface(56000);
	}
	private void sortDefinitions(int index) {
		definitionIndex = index;
		definitions.sort(new sortDefinitions(definitionIndex).reversed());
		}
	
	private void displayBonuses() {
		int bonusRank = 56102;
		int name = 56103;
		int itemModel = 56104;
		for(int i = 0; i < 100; i++) {
			player.getPA().sendString(bonusRank, "" + definitions.get(i).getBonus()[definitionIndex]);
			player.getPA().sendString(name, definitions.get(i).getName());
			player.getPA().sendItemOnInterface(itemModel, definitions.get(i).getId(), 1);
			bonusRank += 4;
			name += 4;
			itemModel += 4;
		}
	}
	public boolean handleButton(int id) {
		if(id >= -9526 && id <= -9512) {
			int index = id + 9526;
			sortDefinitions(index);
			displayBonuses();
		}
		return false;
	}
	
	private final Player player;
}
 class sortDefinitions implements Comparator<ItemDefinition> {
	 private final int bonusIndex;
	 public sortDefinitions(int bonusIndex) {
		 this.bonusIndex = bonusIndex;
	 }
	@Override
	public int compare(ItemDefinition def, ItemDefinition other) {
		
		return (int) (def.getBonus()[bonusIndex] - other.getBonus()[bonusIndex]);
	}
	
}
