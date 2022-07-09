package com.zamron.world.content.combat.effect;

import com.zamron.model.container.impl.Equipment;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.player.Player;

public class EquipmentBonus {

	public static boolean wearingVoid(Player player, CombatType attackType) {
		int correctEquipment = 0;
		int helmet = attackType == CombatType.MAGIC ? MAGE_VOID_HELM :
						attackType == CombatType.RANGED ? RANGED_VOID_HELM : MELEE_VOID_HELM;
		for (int armour[] : VOID_ARMOUR) {
			if (player.getEquipment().getItems()[armour[0]].getId() == armour[1]) {
				correctEquipment++;
			}
		}
		if (player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == VOID_KNIGHT_DEFLECTOR) {
			correctEquipment++;
		}
		return correctEquipment >= 3 && player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == helmet;
	}

	public static boolean wearingEliteVoid(Player player, CombatType attackType) {
		int correctEquipment = 0;
		int helmet = attackType == CombatType.MAGIC ? MAGE_VOID_HELM :
				attackType == CombatType.RANGED ? RANGED_VOID_HELM : MELEE_VOID_HELM;
		for (int armour[] : ELITE_VOID_ARMOUR) {
			if (player.getEquipment().getItems()[armour[0]].getId() == armour[1]) {
				correctEquipment++;
			}
		}
		if (player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == VOID_KNIGHT_DEFLECTOR) {
			correctEquipment++;
		}
		return correctEquipment >= 3 && player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == helmet;
	}
		
	private static final int MAGE_VOID_HELM = 11663;
	
	private static final int RANGED_VOID_HELM = 11664;
	
	private static final int MELEE_VOID_HELM = 11665;
	
	private static final int VOID_KNIGHT_DEFLECTOR = 19712;
	
	public static final int[][] VOID_ARMOUR = {
		{Equipment.BODY_SLOT, 8839},
		{Equipment.LEG_SLOT, 8840},
		{Equipment.HANDS_SLOT, 8842}
	};

	public static final int[][] ELITE_VOID_ARMOUR = {
			{Equipment.BODY_SLOT, 19785},
			{Equipment.LEG_SLOT, 19786},
			{Equipment.HANDS_SLOT, 8842}
	};
}
