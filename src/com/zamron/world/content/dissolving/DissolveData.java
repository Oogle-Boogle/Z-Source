package com.zamron.world.content.dissolving;



import com.zamron.model.Item;

public enum DissolveData {


	//CRYSTAL_KEY(new Item(989, 1), new Item(1543, 1), 15, 200),
	STARTER_BOX(new Item(15373, 1), new Item(6199, 1), 25, 250),
	BLESSED_AMULET(new Item(15418, 1), new Item(9503, 1), 40, 300),
	ROW(new Item(2572, 1), new Item(20054, 1), 33, 750),
	FIGHTING_BOOTS(new Item(9006, 1), new Item(5079, 1), 33, 1500),
	DEFENDER_CAPE(new Item(3973, 1), new Item(18748, 1), 33, 2500),
	COLLECTORS_AMULET(new Item(19886, 1), new Item(19106, 1), 33, 10000),
	INFERNAL_SCYTHE(new Item(3928, 1), new Item(3941, 1), 33, 3500),
	REX_WHIP(new Item(18865, 1), new Item(14559, 1), 20, 7500),
	HURRICANE_WHIP(new Item(18957, 1), new Item(19618, 1), 15, 15000),
	FROST_MG(new Item(5130, 1), new Item(5134, 1), 33, 5000),
	DRAGON_MG(new Item(5134, 1), new Item(5131, 1), 33, 7500),
	HULK_MG(new Item(5131, 1), new Item(5195, 1), 25, 15000),
	INFERNAL_BATTLESTAFF(new Item(19468, 1), new Item(3951, 1), 33, 2500),
	STAFF_OF_VALOR(new Item(3951, 1), new Item(19720, 1), 20, 7500),
	HEATED_BATTLESTAFF(new Item(19720, 1), new Item(5129, 1), 20, 15000),
	DR_SCROLL(new Item(18392, 1), new Item(18401, 1), 25, 2000),
	REX_HELMET(new Item(3908, 1), new Item(5200, 1), 33, 4000),
	REX_PLATEBODY(new Item(3910, 1), new Item(5198, 1), 33, 4000),
	REX_PLATELEGS(new Item(3909, 1), new Item(5199, 1), 33, 4000),
	ZEUS_HELMET(new Item(6194, 1), new Item(11148, 1), 30, 4000),
	ZEUS_PLATEBODY(new Item(6195, 1), new Item(11149, 1), 30, 4000),
	ZEUS_PLATELEGS(new Item(6196, 1), new Item(11150, 1), 30, 4000),
	ETERNAL_POTION(new Item(5185, 1), new Item(3961, 1), 50, 10000),
	
	EXOTIC_STAFF(new Item(19727, 1), new Item(8664, 1), 20, 25000),
	EXOTIC_BOOTS(new Item(19728, 1), new Item(8666, 1), 25, 20000),
	EXOTIC_GLOVES(new Item(19729, 1), new Item(8667, 1), 25, 20000),
	EXOTIC_HELMET(new Item(19730, 1), new Item(8668, 1), 25, 20000),
	EXOTIC_PLATELEGS(new Item(19731, 1), new Item(8669, 1), 25, 20000),
	EXOTIC_PLATEBODY(new Item(19732, 1), new Item(8670, 1), 25, 20000),
	EXOTIC_WINGS(new Item(6485, 1), new Item(8665, 1), 25, 20000),
	
;
	
	
	private Item required, reward;
	private int chance, bagsRequired;
	
	/*UpgradeData(Item required, Item reward, int chance) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
	}*/
	
	DissolveData(Item required, Item reward, int chance, int bagsRequired) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
		this.bagsRequired = bagsRequired;
	}
	
	public Item getRequired() {
		return required;
	}

	public Item getReward() {
		return reward;
	}

	public int getChance() {
		return chance;
	}
	
	
	public int getBagsRequired() {
		return bagsRequired;
	}
	

}
