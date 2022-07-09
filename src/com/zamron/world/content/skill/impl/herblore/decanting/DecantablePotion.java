package com.zamron.world.content.skill.impl.herblore.decanting;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Represents a decantable potion and it's respective data.
 *
 * @author Andys1814.
 */
public enum DecantablePotion {

	STRENGTH(113, 115, 117, 119), ATTACK(2428, 121, 123, 125), RESTORE(2430, 127, 129, 131), DEFENCE(2432, 133, 135,
			137), PRAYER(2434, 139, 141, 143), FISHING(2438, 151, 153, 155), RANGING(2444, 169, 171,
					173), ANTIFIRE(2452, 2454, 2456, 2458), ENERGY(3008, 3010, 3012, 3014), AGILITY(3032, 3034, 3036,
							3038), MAGIC(3040, 3042, 3044, 3046), COMBAT(9739, 9741, 9743, 9745), SUMMONING(12140,
									12142, 12144, 12146), SUPER_ATTACK(2436, 145, 147, 149), SUPER_STRENGTH(2440, 157,
											159, 161), POTION_FLASK(14207, 157,
													159, 161), SUPER_DEFENCE(2442, 163, 165, 167), SUPER_ENERGY(3016, 3018,
													3020, 3022), SUPER_RESTORE(3024, 3026, 3028, 3030);

	private final int[] itemIds;

	DecantablePotion(int... itemIds) {
		this.itemIds = itemIds;
	}

	public static DecantablePotion forId(int itemId) {
		return Arrays.stream(DecantablePotion.values()).filter(potion -> ArrayUtils.contains(potion.getIds(), itemId))
				.findAny().get();
	}

	public int doseForId(int itemId) {
		if (itemId == this.getIds()[0]) {
			return 4;
		}
		if (itemId == this.getIds()[1]) {
			return 3;
		}
		if (itemId == this.getIds()[2]) {
			return 2;
		}
		if (itemId == this.getIds()[3]) {
			return 1;
		}
		return -1;
	}

	public static void main(String[] args) {
		//System.out.println(forId(113));
		FISHING.getIds();
	}

	public int[] getIds() {
		return itemIds;
	}

}
