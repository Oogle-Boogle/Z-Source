package com.zamron.world.content.skill.impl.fletching;

public enum BowData {
	//Log Id, Unstrung Bow Id, Xp, Level Req.
	SHORTBOW(1511, 50, 100, 5),
	LONGBOW(1511, 48, 150, 10),

	OAK_SHORTBOW(1521, 54, 270, 20),
	OAK_LONGBOW(1521, 56, 350, 25),

	WILLOW_SHORTBOW(1519, 60, 590, 35),
	WILLOW_LONGBOW(1519, 58, 620, 40),

	MAPLE_SHORTBOW(1517, 64,910, 50),
	MAPLE_LONGBOW(1517, 62, 980, 55),

	YEW_SHORTBOW(1515, 68, 1480, 65),
	YEW_LONGBOW(1515, 66, 1650, 70),

	MAGIC_SHORTBOW(1513, 72, 2130, 80),
	MAGIC_LONGBOW(1513, 70, 2280, 85);

	public int logID, unstrungBow, xp, levelReq;

	private BowData(int logID, int unstrungBow, int xp, int levelReq) {
		this.logID = logID;
		this.unstrungBow = unstrungBow;
		this.xp = xp;
		this.levelReq = levelReq;
	}

	public int getLogID() {
		return logID;
	}

	public int getBowID() {
		return unstrungBow;
	}

	public int getXp() {
		return xp;
	}

	public int getLevelReq() {
		return levelReq;
	}
	

	public static BowData forBow(int id) {
		for (BowData fl : BowData.values()) {
			if (fl.getBowID() == id) {
				return fl;
			}
		}
		return null;
	}
	
	public static BowData forLog(int log) {
		for (BowData fl : BowData.values()) {
			if (fl.getLogID() == log) {
				return fl;
			}
		}
		return null;
	}
	
	public static BowData forLog(int log, boolean shortbow) {
		for (BowData fl : BowData.values()) {
			if (fl.getLogID() == log) {
				if(shortbow && fl.toString().toLowerCase().contains("shortbow") || !shortbow && fl.toString().toLowerCase().contains("longbow")) {
					return fl;
				}
			}
		}
		return null;
	}
	
	public static BowData forId(int id) {
		for (BowData fl : BowData.values()) {
			if (fl.ordinal() == id) {
				return fl;
			}
		}
		return null;
	}
}
