package com.zamron.world.content.skill.impl.prayer;

public enum BonesData {
	 BONES(526, 1355),
	 BAT_BONES(530, 1455),
	 WOLF_BONES(2859, 1555),
	 BIG_BONES(532, 2680),
	 FEMUR_BONES(15182, 3501),
	 BABYDRAGON_BONES(534, 4865),
	 JOGRE_BONE(3125, 5130),
	 ZOGRE_BONES(4812, 5344),
	 LONG_BONES(10976, 5551),
	 CURVED_BONE(10977, 5651),
	 SHAIKAHAN_BONES(3123, 5857),
	 DRAGON_BONES(536, 8750),
	 FAYRG_BONES(4830, 9981),
	 RAURG_BONES(4832, 9957),
	 DAGANNOTH_BONES(6729, 10890),
	 OURG_BONES(14793, 10985),
	 FROSTDRAGON_BONES(18830, 14870),
	 INFERNAL_BONES(19080, 100000),
	 PUMPKIN(18834, 100000);
	
	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private int buryXP;
	
	public int getBoneID() {
		return this.boneId;
	}
	
	public int getBuryingXP() {
		return this.buryXP;
	}
	
	public static BonesData forId(int bone) {
		for(BonesData prayerData : BonesData.values()) {
			if(prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}
	
}
