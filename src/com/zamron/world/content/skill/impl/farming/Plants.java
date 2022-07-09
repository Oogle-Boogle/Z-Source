package com.zamron.world.content.skill.impl.farming;

public enum Plants {
	
	GUAM(5291, 199, 4, 173, 170, 9, 5, SeedType.HERB, 124, 3510, 5), 
	MARENTILL(5292, 201, 11, 173, 170, 14, 5, SeedType.HERB, 136, 4612, 5), 
	TARROMIN(5293, 203, 18, 173, 170, 19, 5, SeedType.HERB, 148, 6310, 5), 
	HARRALANDER(5294, 205, 25, 173, 170, 26, 5, SeedType.HERB, 163, 6730, 5), 
	RANARR(5295, 207, 32, 173, 170, 32, 5, SeedType.HERB, 189, 7098, 5),
	TOADFLAX(5296, 3050, 39, 173, 170, 36, 5, SeedType.HERB, 211, 7421, 5), 
	IRIT(5297, 209, 46, 173, 170, 44, 5, SeedType.HERB, 263, 8320, 5), 
	AVANTOE(5298, 211, 53, 173, 170, 50, 5,	SeedType.HERB, 312, 9157, 5), 
	WERGALI(14870, 14836, 60, 173, 170, 46, 5, SeedType.HERB, 328, 10677, 5),
	KWUARM(5299, 213, 68, 173, 170, 56, 5, SeedType.HERB, 480, 11632, 5), 
	SNAPDRAGON(5300, 3052, 75, 173, 170, 62, 5, SeedType.HERB, 612, 13467, 5), 
	CADANTINE(5301,	215, 82, 173, 170, 67, 5, SeedType.HERB, 811, 14322, 5), 
	LANTADYME(5302, 2486, 89, 173, 170, 73, 5,	SeedType.HERB, 877, 15100, 5), 
	DWARF_WEED(5303, 217, 96, 173, 170, 79, 5, SeedType.HERB, 932,	15721, 5), 
	TORSTOL(5304, 219, 103, 173, 170, 85, 5, SeedType.HERB, 1026, 16333, 5),

	POTATO(5318, 1942, 6, 0, 0, 1, 7, SeedType.ALLOTMENT, 21, 722, 4), 
	ONION(5319, 1957, 13, 0, 0, 5, 7,SeedType.ALLOTMENT,  32, 1234, 4), 
	CABBAGE(5324, 1967, 20, 0, 0, 7, 7, SeedType.ALLOTMENT, 56, 1780,4), 
	TOMATO(5322, 1982, 27, 0, 0, 12, 7, SeedType.ALLOTMENT, 78, 3023, 4), 
	SWEETCORN(5320, 7088, 34, 0,0, 20, 7, SeedType.ALLOTMENT, 139, 3987, 5), 
	STRAWBERRY(5323, 5504, 43, 0, 0, 31, 6, SeedType.ALLOTMENT, 233, 4122, 6), 
	WATERMELON(5321, 5982, 52, 0, 0, 47, 4, SeedType.ALLOTMENT, 300, 6222, 8),

	MARIGOLD(5096, 6010, 8, 0, 0, 2, 7, SeedType.FLOWER, 124, 1244, 4), 
	ROSEMARY(5097, 6014, 13, 0, 0, 11, 7, SeedType.FLOWER, 167, 2760, 4), 
	NASTURTIUM(5098, 6012, 18, 0, 0, 24, 7, SeedType.FLOWER, 210, 4011,4), 
	WOAD(5099, 5738, 23, 0, 0, 25, 7, SeedType.FLOWER, 233, 4122, 4), 
	LIMPWURT(5100, 225, 28, 0, 0,26, 7, SeedType.FLOWER, 277, 4377, 5), 
	WHITE_LILY(14589, 14583, 37, 0, 0, 52, 7, SeedType.FLOWER, 457, 6547, 4);

	public final int seed;
	public final int harvest;
	public final int healthy;
	public final int diseased;
	public final int dead;
	public final int level;
	public final int minutes;
	public final byte stages;
	public final double plantExperience;
	public final double harvestExperience;
	public final SeedType type;

	public static boolean isSeed(int id) {
		for (Plants i : values()) {
			if (i.seed == id) {
				return true;
			}
		}

		return false;
	}

	private Plants(int seed, int harvest, int config, int diseased, int dead, int level, int minutes, SeedType type,
			double plantExperience, double harvestExperience, int stages) {
		this.seed = seed;
		this.harvest = harvest;
		healthy = config;
		this.level = level;
		this.diseased = diseased;
		this.dead = dead;
		this.minutes = minutes;
		this.type = type;
		this.plantExperience = plantExperience;
		this.harvestExperience = harvestExperience;
		this.stages = ((byte) stages);
	}
}
