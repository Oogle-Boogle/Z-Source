package com.zamron.world.content.skill.impl.fletching;

public enum StringingData {
	
		SHORT_BOW(50, 841, 5, 200, 6678),
	    LONG_BOW(48, 839, 10, 300, 6684),
    	
   		OAK_SHORT_BOW(54, 843, 20, 620, 6679),
    	OAK_LONG_BOW(56, 845, 25, 730, 6685),
    	
    	WILLOW_SHORT_BOW(60, 849, 35, 1150, 6680),
    	WILLOW_LONG_BOW(58, 847, 40, 1217, 6686),
    	
    	MAPLE_SHORT_BOW(64, 853, 50, 1720, 6681),
   		MAPLE_LONG_BOW(62, 851, 55, 1870, 6687),
   		
   		YEW_SHORT_BOW(68, 857, 65, 3110, 6682),
    	YEW_LONG_BOW(66, 855, 70, 3370, 6688),
    	
   		MAGIC_SHORT_BOW(72, 861, 80, 4220, 6683),
    	MAGIC_LONG_BOW(70, 859, 85, 4540, 6689);
		

	private int unstrung, strung, level, animation;
	private double xp;

	private StringingData(final int unstrung, final int strung, final int level, final double xp, final int animation) {
		this.unstrung = unstrung;
		this.strung= strung;
		this.level = level;
		this.xp = xp;
		this.animation = animation;
	}

	public int unStrung() {
		return unstrung;
	}
	
	public int Strung() {
		return strung;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getXP() {
		return xp;
	}
	
	public int getAnimation() {
		return animation;
	}
}
