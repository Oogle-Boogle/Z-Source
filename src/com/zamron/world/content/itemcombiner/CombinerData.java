package com.zamron.world.content.itemcombiner;

import com.zamron.model.Item;

public enum CombinerData {
	
	ITEM_1(new Item(14484, 1), new Item[] {new Item(1042, 1), new Item(1044, 3), new Item(1046, 1), new Item(1048, 10)}),
	ITEM_2(new Item(4565, 1), new Item[] {new Item(19475, 1), new Item(1044, 3), new Item(1046, 1), new Item(1048, 10)}),
	ITEM_3(new Item(1053, 1), new Item[] {new Item(4151, 3), new Item(1044, 3), new Item(1046, 1), new Item(1048, 10)}),
	ITEM_4(new Item(1055, 1), new Item[] {new Item(4565, 5), new Item(1044, 3), new Item(1046, 1), new Item(1048, 10)});
	
	CombinerData(Item craftedItem, Item[] requirements) {
		this.craftedItem = craftedItem;
		this.requirements = requirements;
	}
	
	public Item craftedItem;
	public Item[] requirements;
	
}
