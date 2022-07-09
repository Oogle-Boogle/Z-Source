package com.zamron.world.content.combat.range;

import com.zamron.model.container.impl.Equipment;
import com.zamron.world.entity.impl.player.Player;

/**
 * A table of constants that hold data for all ranged ammo.
 * 
 * @author lare96
 */
public class CombatRangedAmmo {

	
	//TODO: Add poisonous ammo
	public enum RangedWeaponData {

		LONGBOW(new int[] {839}, new AmmunitionData[] {AmmunitionData.BATHEUS_ARROW}, RangedWeaponType.LONGBOW),
		SHORTBOW(new int[] {841}, new AmmunitionData[] {AmmunitionData.BATHEUS_ARROW}, RangedWeaponType.SHORTBOW),
		OAK_LONGBOW(new int[] {845}, new AmmunitionData[] {AmmunitionData.MARMAROS_ARROW}, RangedWeaponType.LONGBOW),
		OAK_SHORTBOW(new int[] {843}, new AmmunitionData[] {AmmunitionData.MARMAROS_ARROW}, RangedWeaponType.SHORTBOW),
		WILLOW_LONGBOW(new int[] {847}, new AmmunitionData[] {AmmunitionData.KRATONITE_ARROW}, RangedWeaponType.LONGBOW),
		WILLOW_SHORTBOW(new int[] {849}, new AmmunitionData[] {AmmunitionData.KRATONITE_ARROW}, RangedWeaponType.SHORTBOW),
		MAPLE_LONGBOW(new int[] {851}, new AmmunitionData[] {AmmunitionData.FRACTITIE_ARROW}, RangedWeaponType.LONGBOW),
		MAPLE_SHORTBOW(new int[] {853}, new AmmunitionData[] {AmmunitionData.FRACTITIE_ARROW}, RangedWeaponType.SHORTBOW),
		YEW_LONGBOW(new int[] {855}, new AmmunitionData[] {AmmunitionData.FRACTITIE_ARROW}, RangedWeaponType.LONGBOW),
		YEW_SHORTBOW(new int[] {857}, new AmmunitionData[] {AmmunitionData.FRACTITIE_ARROW}, RangedWeaponType.SHORTBOW),
		MAGIC_LONGBOW(new int[] {859}, new AmmunitionData[] {AmmunitionData.DRAGON_ARROW, AmmunitionData.KRATONITE_ARROW}, RangedWeaponType.LONGBOW),
		MAGIC_SHORTBOW(new int[] {4312, 3666, 3072, 3069, 3663, 3074, 18965, 861, 6724}, new AmmunitionData[] {AmmunitionData.DRAGON_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.FRACTITIE_ARROW}, RangedWeaponType.SHORTBOW),
		GODBOW(new int[] {19143, 19149, 19146}, new AmmunitionData[] {AmmunitionData.DRAGON_ARROW}, RangedWeaponType.SHORTBOW),
		
		TOXIC_BLOWPIPE(new int[] {12926}, new AmmunitionData[] {AmmunitionData.BLOWPIPE}, RangedWeaponType.BLOWPIPE),
		MAGMA_BLOWPIPE(new int[] {12927}, new AmmunitionData[] {AmmunitionData.BLOWPIPE}, RangedWeaponType.BLOWPIPE),
		DRAGON_BLOWPIPE(new int[] {20557}, new AmmunitionData[] {AmmunitionData.BLOWPIPE}, RangedWeaponType.BLOWPIPE),
		ARMADYLIAN_MACHINE(new int[] {19467}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT,
				AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.DRAGON_BOLT, AmmunitionData.ARMADYLIAN_ARROW}, RangedWeaponType.CROSSBOW),

				
		DARK_BOW(new int[] {3077, 3084, 3083, 11235, 13405, 18971, 15701, 15702, 15703, 15704, 19957}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.DARK_BOW),
		
		ZARYTE_BOW(new int[] {4706, 18971}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.DARK_BOW),


		BRONZE_CROSSBOW(new int[] {9174}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT}, RangedWeaponType.CROSSBOW),
		IRON_CROSSBOW(new int[] {9177}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT}, RangedWeaponType.CROSSBOW),
		STEEL_CROSSBOW(new int[] {9179}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT}, RangedWeaponType.CROSSBOW),
		MITHRIL_CROSSBOW(new int[] {9181}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT}, RangedWeaponType.CROSSBOW),
		ADAMANT_CROSSBOW(new int[] {9183}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.RUBY_BOLT}, RangedWeaponType.CROSSBOW),
		/* Crossbows who accept all ammo */HIGHEST_CROSSBOWS(new int[] {9185, 5172, 18931, 12433, 4453, 18357, 13051, 14684, 18889}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.JADE_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.DRAGON_BOLT}, RangedWeaponType.CROSSBOW),
		BRONZE_DART(new int[] {806}, new AmmunitionData[] {AmmunitionData.BRONZE_DART}, RangedWeaponType.THROW),
		IRON_DART(new int[] {807}, new AmmunitionData[] {AmmunitionData.IRON_DART}, RangedWeaponType.THROW),
		STEEL_DART(new int[] {808}, new AmmunitionData[] {AmmunitionData.STEEL_DART}, RangedWeaponType.THROW),
		MITHRIL_DART(new int[] {809}, new AmmunitionData[] {AmmunitionData.MITHRIL_DART}, RangedWeaponType.THROW),
		ADAMANT_DART(new int[] {810}, new AmmunitionData[] {AmmunitionData.ADAMANT_DART}, RangedWeaponType.THROW),
		RUNE_DART(new int[] {811}, new AmmunitionData[] {AmmunitionData.RUNE_DART}, RangedWeaponType.THROW),
		DRAGON_DART(new int[] {11230}, new AmmunitionData[] {AmmunitionData.DRAGON_DART}, RangedWeaponType.THROW),

		BRONZE_KNIFE(new int[] {864, 870, 5654}, new AmmunitionData[] {AmmunitionData.BRONZE_KNIFE}, RangedWeaponType.THROW),
		IRON_KNIFE(new int[] {863, 871, 5655}, new AmmunitionData[] {AmmunitionData.IRON_KNIFE}, RangedWeaponType.THROW),
		STEEL_KNIFE(new int[] {865, 872, 5656}, new AmmunitionData[] {AmmunitionData.STEEL_KNIFE}, RangedWeaponType.THROW),
		BLACK_KNIFE(new int[] {869, 874, 5658}, new AmmunitionData[] {AmmunitionData.BLACK_KNIFE}, RangedWeaponType.THROW),
		MITHRIL_KNIFE(new int[] {866, 873, 5657}, new AmmunitionData[] {AmmunitionData.MITHRIL_KNIFE}, RangedWeaponType.THROW),
		ADAMANT_KNIFE(new int[] {867, 875, 5659}, new AmmunitionData[] {AmmunitionData.ADAMANT_KNIFE}, RangedWeaponType.THROW),
		RUNE_KNIFE(new int[] {868, 876, 5660, 5667}, new AmmunitionData[] {AmmunitionData.RUNE_KNIFE}, RangedWeaponType.THROW),

		BRONZE_THROWNAXE(new int[] {800}, new AmmunitionData[] {AmmunitionData.BRONZE_THROWNAXE}, RangedWeaponType.THROW),
		IRON_THROWNAXE(new int[] {801}, new AmmunitionData[] {AmmunitionData.IRON_THROWNAXE}, RangedWeaponType.THROW),
		STEEL_THROWNAXE(new int[] {802}, new AmmunitionData[] {AmmunitionData.STEEL_THROWNAXE}, RangedWeaponType.THROW),
		MITHRIL_THROWNAXE(new int[] {803}, new AmmunitionData[] {AmmunitionData.MITHRIL_THROWNAXE}, RangedWeaponType.THROW),
		ADAMANT_THROWNAXE(new int[] {804}, new AmmunitionData[] {AmmunitionData.ADAMANT_THROWNAXE}, RangedWeaponType.THROW),
		RUNE_THROWNAXE(new int[] {805}, new AmmunitionData[] {AmmunitionData.RUNE_THROWNAXE}, RangedWeaponType.THROW),
		MORRIGANS_THROWNAXE(new int[] {13883, 13957}, new AmmunitionData[] {AmmunitionData.MORRIGANS_THROWNAXE}, RangedWeaponType.THROW),
		DRAGON_THROWNAXE(new int[] {4451}, new AmmunitionData[] {AmmunitionData.DRAGON_THROWNAXE}, RangedWeaponType.THROW),

		
		TOKTZ_XIL_UL(new int[] {6522}, new AmmunitionData[] {AmmunitionData.TOKTZ_XIL_UL}, RangedWeaponType.THROW),
		
		
		
		BRONZE_JAVELIN(new int[] {825}, new AmmunitionData[] {AmmunitionData.BRONZE_JAVELIN}, RangedWeaponType.THROW),
		IRON_JAVELIN(new int[] {826}, new AmmunitionData[] {AmmunitionData.IRON_JAVELIN}, RangedWeaponType.THROW),
		STEEL_JAVELIN(new int[] {827}, new AmmunitionData[] {AmmunitionData.STEEL_JAVELIN}, RangedWeaponType.THROW),
		MITHRIL_JAVELIN(new int[] {828}, new AmmunitionData[] {AmmunitionData.MITHRIL_JAVELIN}, RangedWeaponType.THROW),
		ADAMANT_JAVELIN(new int[] {829}, new AmmunitionData[] {AmmunitionData.ADAMANT_JAVELIN}, RangedWeaponType.THROW),
		RUNE_JAVELIN(new int[] {830}, new AmmunitionData[] {AmmunitionData.RUNE_JAVELIN}, RangedWeaponType.THROW),
		MORRIGANS_JAVELIN(new int[] {13879, 13953}, new AmmunitionData[] {AmmunitionData.MORRIGANS_JAVELIN}, RangedWeaponType.THROW),

		CHINCHOMPA(new int[] {10033}, new AmmunitionData[] {AmmunitionData.CHINCHOMPA}, RangedWeaponType.THROW),
		RED_CHINCHOMPA(new int[] {10034}, new AmmunitionData[] {AmmunitionData.RED_CHINCHOMPA}, RangedWeaponType.THROW),
		
		UMD_CANNON(new int[] {3904}, new AmmunitionData[] {AmmunitionData.UMD_SHOT}, RangedWeaponType.HAND_CANNON),
		KARILS_CROSSBOW(new int[]{4734}, new AmmunitionData[] {AmmunitionData.BOLT_RACK}, RangedWeaponType.CROSSBOW),
		
		SHINESMINIGUN(new int[] {5125}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		GOLDENMINIGUN(new int[] {5130}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		RAYGUN(new int[] {3275}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		BFG900(new int[] {20427}, new AmmunitionData[] {AmmunitionData.BFG_AMMO}, RangedWeaponType.MINIGUN),
		DETRIMENTALMINIGUN(new int[] {10905}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		HULKMINIGUN(new int[] {5195}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		DRAGONMINIGUN(new int[] {5131}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		JOKERSBOW(new int[] {3082}, new AmmunitionData[] {AmmunitionData.BATHEUS_ARROW}, RangedWeaponType.SHORTBOW),
		HERBALBOW(new int[] {9492}, new AmmunitionData[] {AmmunitionData.HERBALAMMO}, RangedWeaponType.SHORTBOW),
		ICY_CROSSBOW(new int[]{4734, 18931, 8001}, new AmmunitionData[] {AmmunitionData.KATAGON_ARROW}, RangedWeaponType.CROSSBOW),
		
		HANDCANNON(new int[] {15241}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		MINIGUN(new int[] {896}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		SKYROCKETMINIGUN(new int[] {3990}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		CHAOSWORLDMINIGUN(new int[] {3991}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		KO4USBIRTHDAYGIFT(new int[] {3987}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		VPRSLAVAGUN(new int[] {5158}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.SHORTBOW),
		BFG9002(new int[] {80}, new AmmunitionData[] {AmmunitionData.BFG_AMMO, AmmunitionData.CUSTOMARROW1}, RangedWeaponType.HAND_CANNON),
		VIPSTONERGUN(new int[] {7764}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT, AmmunitionData.CUSTOMARROW1}, RangedWeaponType.HAND_CANNON),
		FROSTMINIGUN(new int[] {5134}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		PLATINUMMINIGUN(new int[] {5132}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		RAINBOWMINIGUN(new int[] {5081}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),
		PURPLEMINIGUN(new int[] {5133}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.SHORTBOW),
		IMBUEDBOW(new int[] {8655, 5115}, new AmmunitionData[] {AmmunitionData.KATAGON_ARROW, AmmunitionData.FRACTITIE_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.SHORTBOW),
		
		SUPREMEHERBALBOW(new int[] {13201, 5171}, new AmmunitionData[] {AmmunitionData.HERBALAMMO, AmmunitionData.CUSTOMARROW1, AmmunitionData.CUSTOMARROW2}, RangedWeaponType.SHORTBOW);

		
		
		RangedWeaponData(int[] weaponIds, AmmunitionData[] ammunitionData, RangedWeaponType type) {
			this.weaponIds = weaponIds;
			this.ammunitionData = ammunitionData;
			this.type = type;
		}

		private int[] weaponIds;
		private AmmunitionData[] ammunitionData;
		private RangedWeaponType type;

		public int[] getWeaponIds() {
			return weaponIds;
		}

		public AmmunitionData[] getAmmunitionData() {
			return ammunitionData;
		}

		public RangedWeaponType getType() {
			return type;
		}

		public static RangedWeaponData getData(Player p) {
			int weapon = p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
			for(RangedWeaponData data : RangedWeaponData.values()) {
				for(int i : data.getWeaponIds()) {
					if(i == weapon)
						return data;
				}
			}
			return null;
		}

		public static AmmunitionData getAmmunitionData(Player p) {
			RangedWeaponData data = p.getRangedWeaponData();
			if(data != null) {
				int ammunition = p.getEquipment().getItems()[data.getType() == RangedWeaponType.THROW || data.getType() == RangedWeaponType.BLOWPIPE ? Equipment.WEAPON_SLOT : Equipment.AMMUNITION_SLOT].getId();
				for(AmmunitionData ammoData : AmmunitionData.values()) {
					for(int i : ammoData.getItemIds()) {
						if(i == ammunition)
							return ammoData;
					}
				}
			}
			return AmmunitionData.BATHEUS_ARROW;
		}
	}

	public enum AmmunitionData {
		
		BLOWPIPE(new int[] {12926}, -1, -1, 1123, 10, 20, 85, 23, 25),
		DRAGONBLOWPIPE(new int[] {20557}, -1, -1, 1123, 10, 20, 85, 23, 25),

		
		BATHEUS_ARROW(new int[] {16432}, 19, 2107, 10, 3, 44, 20, 43, 31),
		MARMAROS_ARROW(new int[] {16437}, 18, -1, 9, 3, 44, 50, 43, 31),
		KRATONITE_ARROW(new int[] {16342}, 20, -1, 11, 3, 44, 70, 43, 31),
		FRACTITIE_ARROW(new int[] {16447}, 21, -1, 12, 3, 44, 90, 43, 31),
		ZEPHYRIUM_ARROW(new int[] {16452}, 22, -1, 13, 3, 44, 110, 43, 31),
		ARGONITE_ARROW(new int[] {16458}, 24, -1, 15, 3, 44, 130, 43, 31),
		KATAGON_ARROW(new int[]{16462}, 25, -1, 16, 3, 44, 150, 34, 31),
		BROAD_ARROW(new int[] {4160}, 20, -1, 11, 3, 44, 170, 43, 31),
		DRAGON_ARROW(new int[] {11212}, 1111, -1, 1120, 3, 44, 190, 43, 31),

		BRONZE_BOLT(new int[] {877}, -1, -1, 27, 3, 44, 13, 43, 31),
		OPAL_BOLT(new int [] {879, 9236}, -1, -1, 27, 3, 44, 20, 43, 31),
		IRON_BOLT(new int[] {9140}, -1, -1, 27, 3, 44, 28, 43, 31),
		JADE_BOLT(new int[] {9335, 9237}, -1, -1, 27, 3, 44, 31, 43, 31),
		STEEL_BOLT(new int[] {9141}, -1, -1, 27, 3, 44, 35, 43, 31),
		PEARL_BOLT(new int[] {880, 9238}, -1, -1, 27, 3, 44, 38, 43, 31),
		MITHRIL_BOLT(new int[] {9142}, -1, -1, 27, 3, 44, 40, 43, 31),
		TOPAZ_BOLT(new int[] {9336, 9239}, -1, -1, 27, 3, 44, 50, 43, 31),
		ADAMANT_BOLT(new int[] {9143}, -1, -1, 27, 3, 44, 60, 43, 31),
		SAPPHIRE_BOLT(new int[] {9337, 9240}, -1, -1, 27, 3, 44, 65, 43, 31),
		EMERALD_BOLT(new int[] {9338, 9241}, -1, -1, 27, 3, 44, 70, 43, 31),
		RUBY_BOLT(new int[] {9339, 9242}, -1, -1, 27, 3, 44, 75, 43, 31),
		RUNITE_BOLT(new int[] {9144}, -1, -1, 27, 3, 44, 84, 43, 31),
		BROAD_BOLT(new int[] {13280}, -1, -1, 27, 3, 44, 88, 43, 31),
		DIAMOND_BOLT(new int[] {9340, 9243}, -1, -1, 27, 3, 44, 88, 43, 31),
		ONYX_BOLT(new int[] {9342, 9245}, -1, -1, 27, 3, 44, 90, 43, 31),
		DRAGON_BOLT(new int[] {9341, 9244}, -1, -1, 27, 3, 44, 95, 43, 31),

		BRONZE_DART(new int[] {806}, 1234, -1, 226, 3, 33, 2, 45, 37),
		IRON_DART(new int[] {807}, 1235, -1, 227, 3, 33, 5, 45, 37),
		STEEL_DART(new int[] {808}, 1236, -1, 228, 3, 33, 8, 45, 37),
		MITHRIL_DART(new int[] {809}, 1238, -1, 229, 3, 33, 10, 45, 37),
		ADAMANT_DART(new int[] {810}, 1239, -1, 230, 3, 33, 15, 45, 37),
		RUNE_DART(new int[] {811}, 1240, -1, 231, 3, 33, 20, 45, 37),
		DRAGON_DART(new int[] {11230}, 1123, -1, 226, 3, 33, 25, 45, 37),

		BRONZE_KNIFE(new int[] {864, 870, 5654}, 219, -1, 212, 3, 33, 8, 45, 37),
		IRON_KNIFE(new int[] {863, 871, 5655}, 220, -1, 213, 3, 33, 12, 45, 37),
		STEEL_KNIFE(new int[] {865, 872, 5656}, 221, -1, 214, 3, 33, 15, 45, 37),
		BLACK_KNIFE(new int[] {869, 874, 5658}, 222, -1, 215, 3, 33, 17, 45, 37),
		MITHRIL_KNIFE(new int[] {866, 873, 5657}, 223, -1, 215, 3, 33, 19, 45, 37),
		ADAMANT_KNIFE(new int[] {867, 875, 5659}, 224, -1, 217, 3, 33, 24, 45, 37),
		RUNE_KNIFE(new int[] {868, 876, 5660, 5667}, 225, -1, 218, 3, 33, 30, 45, 37),
		
		BRONZE_THROWNAXE(new int[] {800}, 43, -1, 36, 3, 44, 7, 43, 31),
		IRON_THROWNAXE(new int[] {801}, 42, -1, 35, 3, 44, 9, 43, 31),
		STEEL_THROWNAXE(new int[] {802}, 44, -1, 37, 3, 44, 11, 43, 31),
		MITHRIL_THROWNAXE(new int[] {803}, 45, -1, 38, 3, 44, 13, 43, 31),
		ADAMANT_THROWNAXE(new int[] {804}, 46, -1, 39, 3, 44, 15, 43, 31),
		RUNE_THROWNAXE(new int[] {805}, 48, -1, 41, 3, 44, 17, 43, 31),
		MORRIGANS_THROWNAXE(new int[] {13883, 13957}, 1856, -1, -1, 3, 44, 100, 43, 31),
		DRAGON_THROWNAXE(new int[] {4451}, 48, -1, 41, 3, 44, 17, 43, 31),

		BRONZE_JAVELIN(new int[] {825}, 206, -1, 200, 2, 40, 7, 45, 37),
		IRON_JAVELIN(new int[] {826}, 207, -1, 201, 2, 40, 9, 45, 37),
		STEEL_JAVELIN(new int[] {827}, 208, -1, 202, 2, 40, 11, 45, 37),
		MITHRIL_JAVELIN(new int[] {828}, 209, -1, 203, 2, 40, 13, 45, 37),
		ADAMANT_JAVELIN(new int[] {829}, 210, -1, 204, 2, 40, 15, 45, 37),
		RUNE_JAVELIN(new int[] {830}, 211, -1, 205, 2, 40, 17, 45, 37),
		MORRIGANS_JAVELIN(new int[] {13879, 13953}, 1855, -1, -1, 2, 40, 100, 45, 37),

		TOKTZ_XIL_UL(new int[]{6522}, -1, -1, 442, 2, 40, 58, 45, 37),
		CHINCHOMPA(new int[] {10033}, -1, -1, -1, 17, 8, 50, 45, 37),
		RED_CHINCHOMPA(new int[] {10034}, -1, -1, -1, 17, 8, 80, 45, 37),
		UMD_SHOT(new int[] {8857}, 2138, -1, 2989, 3, 8, 115, 43, 31),
		SARADOMIN_ARROW(new int[] {19152}, 472, -1, 473, 3, 44, 66, 43, 31),
		ARMADYLIAN_ARROW(new int[] {9706}, -1, -1, 374, 3, 44, 103, 43, 31),
		NO_AMMO(new int[] {-1}, 206, -1, 200, 2, 40, 7, 45, 37),
		BOLT_RACK(new int[] {4740}, -1, -1, 27, 3, 33, 70, 43, 31),

		HAND_CANNON_SHOT(new int[] {15243}, 2138, -1, 2143, 3, 8, 125, 43, 31),
		CUSTOMARROW1(new int[]{22194}, 472, -1, 473, 3, 44, 1000, 43, 31),
		CUSTOMARROW2(new int[]{22195}, 472, -1, 473, 3, 44, 1500, 43, 31),
		HERBALAMMO(new int[]{13091}, 472, -1, 473, 3, 44, 2250, 43, 31),
		BFG_AMMO(new int[] {20260}, 2138, -1, 2143, 3, 8, 10000, 43, 31),
		
		
		
		;
		
		


		AmmunitionData(int[] itemIds, int startGfxId,int startAnim, int projectileId, int projectileSpeed, int projectileDelay, int strength, int startHeight, int endHeight) {
			this.itemIds = itemIds;
			this.startGfxId = startGfxId;
			this.startAnim = startAnim;
			this.projectileId = projectileId;
			this.projectileSpeed = projectileSpeed;
			this.projectileDelay = projectileDelay;
			this.strength = strength;
			this.startHeight = startHeight;
			this.endHeight = endHeight;		
		}

		private int[] itemIds;
		private int startGfxId;
		private int startAnim;
		private int projectileId;
		private int projectileSpeed;
		private int projectileDelay;
		private int strength;
		private int startHeight;
		private int endHeight;
		
		public int[] getItemIds() {
			return itemIds;
		}

		public boolean hasSpecialEffect() {
			return getItemIds().length >= 2;
		}

		public int getStartGfxId() {
			return startGfxId;
		}
		public int getStartAnimation() {
			return startAnim;
		}

		public int getProjectileId() {
			return projectileId;
		}

		public int getProjectileSpeed() {
			return projectileSpeed;
		}

		public int getProjectileDelay() {
			return projectileDelay;
		}

		public int getStrength() {
			return strength;
		}
		
		public int getStartHeight() {
			return startHeight;
		}
		
		public int getEndHeight() {
			return endHeight;
		}
	}

	public enum RangedWeaponType {

		LONGBOW(5, 5),
		SHORTBOW(5, 4),
		CROSSBOW(5, 5),
		THROW(4, 3),
		DARK_BOW(5, 5),
		HAND_CANNON(5, 4),
		BLOWPIPE(5, 3),
		MINIGUN(5, 5);

		RangedWeaponType(int distanceRequired, int attackDelay) {
			this.distanceRequired = distanceRequired;
			this.attackDelay = attackDelay;
		}

		private int distanceRequired;
		private int attackDelay;

		public int getDistanceRequired() {
			return distanceRequired;
		}

		public int getAttackDelay() {
			return attackDelay;
		}
	}
}
