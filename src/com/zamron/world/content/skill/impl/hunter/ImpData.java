package com.zamron.world.content.skill.impl.hunter;

public enum ImpData {
	/**
	 * Baby Impling.
	 */
	BABY( "Baby Impling", 11238, 5577, 1, 6055 ),
	/**
	 * Young Impling.
	 */
	YOUNG( "Young Impling", 11240, 6355, 17, 6056 ),
	/**
	 * Gourmet Impling.
	 */
	GOURMET( "Gourmet Impling", 11244, 6980, 34, 6057),
	/**
	 * Earth Impling.
	 */
	EARTH( "Earth Impling", 11244, 6944, 34, 6058 ),
	/**
	 * Essence Impling.
	 */
	ESSENCE( "Essence Impling", 11246, 7211, 40, 6059 ),
	/**
	 * Electic Impling.
	 */
	ELECTIC( "Electic Impling", 11248, 7900, 50, 6060),
	/**
	 * Nature Impling.
	 */
	NATURE( "Nature Impling", 11250, 8352, 58,6061 ),
	/**
	 * Magpie Impling.
	 */
	MAGPIE( "Magpie Impling", 11252, 8876, 65, 6062 ),
	/**
	 * Ninja Impling.
	 */
	NINJA( "Ninja Impling", 11254, 9554, 74, 6063 ), 
	/**
	 * Dragon Impling.
	 */
	DRAGON( "Dragon Impling", 11256, 17888, 83, 6064 ), 
	/**
	 * Dragon Impling.
	 */
	KINGLY( "Kingly Impling", 15517, 54020, 91, 7903 ),
	
	DELUXE( "Deluxe Impling", 15517, 54020, 91, 7846 );
	
	/**
	 * Variables.
	 */
	public String name;
	public int impJar, XPReward, levelReq, npcId;

	/**
	 * Creating the Impling.
	 * @param name
	 * @param JarAdded
	 * @param XPAdded
	 * @param LevelNeed
	 * @param Npc
	 */
	ImpData(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
		this.name = name;
		this.impJar = JarAdded;
		this.XPReward = XPAdded;
		this.levelReq = LevelNeed;
		this.npcId = Npc;
	}
	
	public static ImpData forId(int npcId) {
		for(ImpData imps : ImpData.values()) {
			if(imps.npcId == npcId) {
				return imps;
			}
		}
		return null;
	}
}
