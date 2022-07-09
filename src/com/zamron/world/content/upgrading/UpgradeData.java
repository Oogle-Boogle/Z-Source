package com.zamron.world.content.upgrading;

import com.zamron.model.Item;

/**
 *
 * Created by Oogleboogle - Rune-Server
 * 14/04/21
 *
 */

public enum UpgradeData {

	VOIDTOP(new Item(8839, 1), new Item(19785, 1), 50, 1),
	VOIDLEG(new Item(8840, 1), new Item(19786, 1), 50, 1),

	RINGOFOVL(new Item(19140, 1), new Item(19141, 1), 40, 50000),
	RINGOFOVLX(new Item(19141, 1), new Item(19142, 1), 25, 100000),

	CUSTOMSLAYHELM(new Item(19101, 1), new Item(936, 1), 40, 85000),

	SOLACEGLAIVE(new Item(3641, 1), new Item(17931, 1), 40, 200000),
	SIRESGLAIVE(new Item(17931, 1), new Item(12426, 1), 25, 200000),

	//Magic
	DEFENDERSHELM(new Item(4799, 1), new Item(3994, 1), 45, 25000),
	DEFENDERSBODY(new Item(4800, 1), new Item(3996, 1), 45, 25000),
	DEFENDERSLEGS(new Item(4801, 1), new Item(3995, 1), 45, 25000),
	DEFENDERCAPE(new Item(3973, 1), new Item(3974, 1), 45, 25000),
	DEFENDERSSTAFF(new Item(3951, 1), new Item(5129, 1), 25, 85000),


	//Melee
	FIGHTING_BOOTS(new Item(9006, 1), new Item(5079, 1), 35, 5500),
	REXHLEM(new Item(3908, 1), new Item(13206, 1), 40, 30000),
	REXBODY(new Item(3909, 1), new Item(13202, 1), 40, 30000),
	REXLEGS(new Item(3910, 1), new Item(13203, 1), 40, 30000),
	REXGLOVES(new Item(5187, 1), new Item(13204, 1), 40, 30000),
	REXBOOTS(new Item(5186, 1), new Item(13205, 1), 40, 30000),


	//Range
	CRIMSONHELM(new Item(4772, 1), new Item(3076, 1), 40, 25000),
	CRIMSONBODY(new Item(4771, 1), new Item(3242, 1), 40, 25000),
	CRIMSONLEGS(new Item(4770, 1), new Item(3244, 1), 40, 25000),
	PEGBOOTS(new Item(12708, 1), new Item(3075, 1), 40, 25000),
	DRAGONMINIGUN(new Item(5131, 1), new Item(5130, 1), 40, 25000),
	RANGERGLOVES(new Item(18347, 1), new Item(3078, 1), 40, 25000),
	PEGBOOTSELITE(new Item(3075, 1), new Item(3914, 1), 35, 50000),


	//Extras
	INFERNAL_SCYTHE(new Item(3928, 1), new Item(3941, 1), 40, 55000),
	BLESSED_AMULET(new Item(15418, 1), new Item(9503, 1), 30, 65000),
	ROW(new Item(2572, 1), new Item(20054, 1), 35, 85000),
	DR_SCROLL(new Item(18392, 1), new Item(18401, 1), 50, 55000),
	COLLECTORS_AMULET(new Item(19886, 1), new Item(19106, 1), 33, 10000),
	INFINITEOVERLOAD(new Item(5185, 1), new Item(3961, 1), 50, 10000),
	SABER(new Item(3276, 1), new Item(3274, 1), 33, 50000),
	SABER2(new Item(3274, 1), new Item(4059, 1), 20, 200000),
	SLAYER_HELM(new Item(936, 1), new Item(3949, 1), 20, 100000),
	T1T3(new Item(3459, 1), new Item(3455, 1), 40, 50000),
	T4T7(new Item(3455, 1), new Item(3458, 1), 15, 100000),
	PRODEF(new Item(20699, 1), new Item(20700, 1), 20, 100000),

	;
	
	
	private Item required, reward;
	private int chance, bagsRequired;
	
	UpgradeData(Item required, Item reward, int chance, int bagsRequired) {
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
