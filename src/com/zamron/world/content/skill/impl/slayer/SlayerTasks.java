package com.zamron.world.content.skill.impl.slayer;

import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.Broly;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Gabriel Hannason 
 */

public enum SlayerTasks {

	NO_TASK(null, -1, null, -1, null),

	/**
	 * Easy tasks
	 */
	BUGS_BUNNY(SlayerMaster.VANNAKA, 4455, "Bugs Bunny can be found in the Starter Zone", 1000, new Position(3795, 3543, 0)),
	DAFFY_DUCK(SlayerMaster.VANNAKA, 4457, "Daffy Duck can be found in the Starter Zone", 1000, new Position(3780, 3551, 0)),
	SYLVESTER(SlayerMaster.VANNAKA, 4459, "Sylvester can be found in the Starter Zone", 1000, new Position(3754, 3551)),
	COYOTE(SlayerMaster.VANNAKA, 4456, "Coyote can be found in the Starter Zone ", 1000, new Position(3745, 3564, 0)),
	YOSEMITE_SAM(SlayerMaster.VANNAKA, 4462, "Yosemite Sam can be found in the Starter Zone!", 1000, new Position(3760, 3572, 0)),
	MARTIAN(SlayerMaster.VANNAKA, 4409, "Martian can be found in the Starter Zone!", 1000, new Position(3785, 3572, 0)),
	BOWSER(SlayerMaster.VANNAKA, 728, "Bowser can be found in the Token Zone!", 1000, new Position(2527, 2851)),
	LUIGI(SlayerMaster.VANNAKA, 727, "Luigi can be found in the Token Zone!", 1000, new Position(2527, 2851)),
	
	/**
	 * Medium tasks
	 */
	HERCULES(SlayerMaster.DURADEL, 17, "Hercules can be found in the Starter Teleports!", 2500, new Position(2783, 4636, 0)),
	LUCARIO(SlayerMaster.DURADEL, 3263, "Lucario can be found in the Starer Teleports", 2500, new Position(2891, 4767)),
	DEFENDERS(SlayerMaster.DURADEL, 9994, "Defenders can be found in the Medium Teleports", 2500, new Position(2724, 9821)),
	GODZILLA(SlayerMaster.DURADEL, 9932, "Godzilla can be found in the Medium Teleports", 2500, new Position(2452, 10147)),
	CERBERUS(SlayerMaster.DURADEL, 1999, "Cerberus can be found in the Medium Teleports", 2500, new Position(1240, 1247)),
	VEGETA(SlayerMaster.DURADEL, 101, "Find them in the DBZ Teleport at home!", 2500, new Position(2142, 5537)),
	GOKU(SlayerMaster.DURADEL, 100, "Find them in the DBZ Teleport at home!", 2500, new Position(2142, 5537)),
	CHARIZARD(SlayerMaster.DURADEL, 2843, "Find them in the slayer tab!", 2500, new Position(0000, 0000)),
	CHARMANDER(SlayerMaster.DURADEL, 174, "Find them in the slayer tab!", 2500, new Position(0000, 0000)),
	BULBASAUR(SlayerMaster.DURADEL, 842, "Find them in the slayer tab!", 2500, new Position(2251, 3357)),
	PUMPKIN(SlayerMaster.DURADEL, 8548, "Find them in the slayer tab!", 3500, new Position(0000,0000)),

	/**
	 * Hard tasks
	 */
	ZEUS(SlayerMaster.KURADEL, 16, "Find Zeus in the Medium Teleports", 3850, new Position(0000, 0000)),
	INFARTICO(SlayerMaster.KURADEL, 9993, "Find Infartico in the Medium teleports", 3850, new Position(0000,0000)),
	LORDVALOR(SlayerMaster.KURADEL, 9277, "Find Lord Valors in the Medium Teleports", 3850, new Position(0000,0000)),
	STORMTROOPER(SlayerMaster.KURADEL, 1069, "Find Storm Troopers in the Starwars teleports at home!", 3850, new Position(0000, 0000)),
	VADER(SlayerMaster.KURADEL, 11, "Find Darth Vaders in the Starwars teleports at home!", 3850, new Position(0000, 0000)),
	CHARIZARD1(SlayerMaster.KURADEL, 2843, "Find them in the slayer tab!", 3850, new Position(0000, 0000)),
	MAGIC_SPIDER(SlayerMaster.KURADEL, 6309, "Find them in the slayer tab!", 3850, new Position(0000, 0000)),
	JOKER(SlayerMaster.KURADEL, 420, "Find them in the slayer tab!", 3850, new Position(0000, 0000)),
	PUMPKIN1(SlayerMaster.KURADEL, 8548, "Find them in the slayer tab!", 3850, new Position(0000,0000)),
	ZORBAK(SlayerMaster.KURADEL, 5922, "Find them in the slayer tab!", 3850, new Position(0000,0000)),



	/**
	 * Elite
	 */
	PUMPKIN2(SlayerMaster.SUMONA, 8548, "Find them in the slayer tab!", 5200, new Position(0000,0000)),
	JOKER1(SlayerMaster.SUMONA, 420, "Find them in the slayer tab!", 5200, new Position(0000, 0000)),
	AMONGUS_BLACK(SlayerMaster.SUMONA, 610, "Find Among us (Black) in Zones tele tab.", 5200, new Position(2922, 4083)),
	AMONGUS_CYAN(SlayerMaster.SUMONA, 609, "Find Among us (Cyan) in Zones tele tab.", 5200, new Position(2921, 4048)),
	BAL(SlayerMaster.SUMONA, 10140, "Find them in the slayer tab!", 5200, new Position(0000,0000)),
	KASH(SlayerMaster.SUMONA, 10038, "Find them in the slayer tab!", 5200, new Position(0000, 0000)),
	AMONGUS_GREEN(SlayerMaster.SUMONA, 607, "Find Among us (Green) in Zones tele tab.", 5200, new Position(2921, 4048)),
	STORMTROOPER1(SlayerMaster.SUMONA, 1069, "Find Storm Troopers in the Starwars teleports at home!", 5200, new Position(0000, 0000)),
	VADER2(SlayerMaster.SUMONA, 11, "Find Darth Vaders in the Starwars teleports at home!", 5200, new Position(0000, 0000)),
	MAGIC_SPIDER2(SlayerMaster.SUMONA, 6309, "Find them in the slayer tab!", 5200, new Position(0000, 0000)),
	TOAD(SlayerMaster.SUMONA, 812, "Find them in slayer tab!", 5200, new Position(0000, 0000)),
	MEWTWO(SlayerMaster.SUMONA, 6357, "Find them in the slayer tab!", 5200, new Position(0000,0000)),
	/**
	 * Extreme - Bravek
	 * Bosses
	 **/
	ANTMAN(SlayerMaster.BRAVEK, 9912, "Find Ant man in Hard raids!", 15000, new Position(3247, 3033)),
	ONSLAUGHT(SlayerMaster.BRAVEK, 422, "You can find Onslaught in hard raids!", 15000, new Position(0000,0000)),
	KINGKONG(SlayerMaster.BRAVEK, 111, "Find King Kong in Hard raids!", 15000, new Position(0000,0000)),
	ASSASSIN(SlayerMaster.BRAVEK, 9944, "Assassin global world boss...", 15000, new Position(0000, 0000)),
	DARKRANGER(SlayerMaster.BRAVEK, 299, "Dark Ranger global world boss...", 15000, new Position(0000, 0000));
	//BROLY(SlayerMaster.BRAVEK, 1059, "Find broly at ::broly spawn.", 10000, new Position(0000,0000));

	private SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;
	private Position taskPosition;

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public int getXP() {
		return this.XP;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}

	public static SlayerTasks forId(int id) {
		for (SlayerTasks tasks : SlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewTaskData(SlayerMaster master, Player player) {
		int slayerTaskId = 1, slayerTaskAmount = 30;
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0, extremeTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.VANNAKA)
				easyTasks++;
			else if (task.getTaskMaster() == SlayerMaster.DURADEL)
				mediumTasks++;
			else if (task.getTaskMaster() == SlayerMaster.KURADEL)
				hardTasks++;
			else if (task.getTaskMaster() == SlayerMaster.SUMONA)
				eliteTasks++;
			else if (task.getTaskMaster() == SlayerMaster.BRAVEK)
				extremeTasks++;
		}

		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 30 + Misc.getRandom(5);
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = 60 + Misc.getRandom(5);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = 100 + Misc.getRandom(5);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = 130 + Misc.getRandom(5);
		} else if (master == SlayerMaster.BRAVEK) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + eliteTasks + Misc.getRandom(extremeTasks - 1);
			if (player.getBravekDifficulty() == null) {
				slayerTaskAmount = 10 + Misc.getRandom(5);
			} else {
				switch (player.getBravekDifficulty()) {
				case "easy":
					slayerTaskAmount = 30 + Misc.getRandom(5);
					break;
				case "medium":
					slayerTaskAmount = 60 + Misc.getRandom(10);
					break;
				case "hard":
					slayerTaskAmount = 100 + Misc.getRandom(15);
					break;
				}
			}
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}
	
	@Override
	public String toString() {
		return Misc.ucFirst(name().toLowerCase().replaceAll("_", " "));
	}
}
