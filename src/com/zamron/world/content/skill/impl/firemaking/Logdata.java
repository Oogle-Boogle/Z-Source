package com.zamron.world.content.skill.impl.firemaking;

import com.zamron.world.entity.impl.player.Player;


public class Logdata {

	public static enum logData {
		
		LOG(1511, 1, 150, 30),
		ACHEY(2862, 1, 250, 30),
		OAK(1521, 15, 275, 40),
		WILLOW(1519, 30, 300, 45),
		TEAK(6333, 35, 350, 45),
		ARCTIC_PINE(10810, 42, 400, 45),
		MAPLE(1517, 45, 450, 45),
		MAHOGANY(6332, 50, 500, 45),
		EUCALYPTUS(12581, 58, 550, 45),
		YEW(1515, 60, 600, 50),
		MAGIC(1513, 75, 650, 50),
		HELLFIRE(19079, 50, 700, 50);
		
		private int logId, level, burnTime;
		private int xp;
		
		private logData(int logId, int level, int xp, int burnTime) {
			this.logId = logId;
			this.level = level;
			this.xp = xp;
			this.burnTime = burnTime;
		}
		
		public int getLogId() {
			return logId;
		}
		
		public int getLevel() {
			return level;
		}
		
		public int getXp() {
			return xp;
		}		
		
		public int getBurnTime() {
			return this.burnTime;
		}
	}

	public static logData getLogData(Player p, int log) {
		for (final Logdata.logData l : Logdata.logData.values()) {
			if(log == l.getLogId() || log == -1 && p.getInventory().contains(l.getLogId())) {
				return l;
			}
		}
		return null;
	}

}