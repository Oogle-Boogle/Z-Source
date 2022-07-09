package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

/**
 *
 * @author Emerald
 */

public class StarterTasks {

	public enum StarterTaskData {

		READ_THE_GUIDE_BOOK("Read The Guide Book", 53207, "", null, "Read the book", "That you received",
				"At the start", ""),
		KILL_10_JADS("Kill Jad 10 times", 53208, "Idk lol :S ", new int[] { 0, 10 }, "Teleport to fight caves",
				"And Enter the jad dungeon", "To fight jad", ""),
		KILL_10_GLODS("Kill Glod 10 times", 53209, "", new int[] { 1, 10 }, "Kill glod 10 times", "", "", ""),
		CUT_1000_LOGS("Cut 1000 Logs", 53210, "Any axe", new int[] { 2, 1000 }, "Click the woodcutting skill",
				"In your skill tab", "", ""),
		REDEEM_A_VOTE_SCROLL("Redeem a Vote Scroll", 53211, "", null, "::vote to vote", "After you vote",
				"Redeem the scroll", "And claim it"),
		REACH_1000_TOTAL("Reach 1000 Total Lvl", 53212, "", null, "Reach a total level of 1000", "", "", ""),
		KILL_WARMONGER("Defeat Warmonger", 53213, "", null, "Defeat Warmonger @ ::war", "", "", ""),
		MINE_100_RUNE_ORES("Mine 100 Rune Ores", 53214, "Mining level of 85", new int[] { 3, 100 },
				"Mine 100 Rune ores", "", "", ""),
		BURY_50_FROST_DRAGON_BONES("Bury 50 FD Bones", 53215, "Frostdragon Bones", new int[] { 4, 50 },
				"Bury 50 Frost dragon bones", "", "", ""),
		COMPLETE_A_CLUE_SCROLL("Complete a Cluescroll", 53216, "", null, "Complete atleast 1", "Clue scroll", "", ""),
		FISH_250_SHARKS("Fish 250 Sharks", 53217, "76 Fishing", new int[] { 5, 250 }, "Fish 250 Sharks", "", "", ""),
		BURN_1000_LOGS("Burn 1000 Logs", 53218, "Tinderbox", new int[] { 6, 1000 }, "Burn 1000 Logs", "", "", ""),
		COOK_250_SHARKS("Cook 250 Sharks", 53219, "80 Cooking", new int[] { 7, 250 }, "Cook 250 Sharks", "", "", "");

		StarterTaskData(String taskName, int interFaceId, String requirements, int[] progressData,
				String taskDescription, String taskDescription1, String taskDescription2, String taskDescription3) {
			this.taskName = taskName;
			this.interFaceId = interFaceId;
			this.requirements = requirements;
			this.progressData = progressData;
			this.taskDescription = taskDescription;
			this.taskDescription1 = taskDescription1;
			this.taskDescription2 = taskDescription2;
			this.taskDescription3 = taskDescription3;
		}

		private String taskName;
		private int interFaceId;
		private String requirements;
		private int[] progressData;
		private String taskDescription;
		private String taskDescription1;
		private String taskDescription2;
		private String taskDescription3;
	}

	static int[] rewards = { 12434, 12435, 12436, 5130, 4646, 4645, 4644, 3286, 10835, 6570, 6183 };

	public static boolean claimReward(Player player) {
		for (StarterTaskData tasks : StarterTaskData.values()) {
			if (!player.getStarterTaskAttributes().completed[tasks.ordinal()]) {
				player.sendMessage("Complete all the tasks first before claiming the reward.");
				return false;
			}
		}
		if (!player.starterClaimed) {
			for (int i = 0; i < rewards.length; i++) {
				player.getInventory().add(rewards[i], 1);
			}
		}
		player.sendMessage("Enjoy your reward");
		player.starterClaimed = true;
		return true;
	}

	public static boolean handleButton(Player player, int buttonID) {
		if (!(buttonID >= -12329 && buttonID <= -12317)) {
			return false;
		}
		int index = -1;

		if (buttonID >= -12329) {
			index = 12329 + buttonID;
		}

		if (index >= 0 && index < StarterTaskData.values().length) {
			StarterTaskData tasks = StarterTaskData.values()[index];
			openInterface(player, tasks);
		}
		return true;
	}

	public static void updateInterface(Player player) {

		for (StarterTaskData tasks : StarterTaskData.values()) {
			boolean completed = player.getStarterTaskAttributes().getCompletion()[tasks.ordinal()];
			boolean progress = tasks.progressData != null
					&& player.getStarterTaskAttributes().getProgress()[tasks.progressData[0]] > 0;
			player.getPacketSender().sendString(tasks.interFaceId,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + tasks.taskName);
		}
	}

	public static void doProgress(Player player, StarterTaskData tasks) {
		doProgress(player, tasks, 1);
	}

	public static void doProgress(Player player, StarterTaskData taskData, int amount) {
		if (player.getStarterTaskAttributes().getCompletion()[taskData.ordinal()])
			return;
		if (taskData.progressData != null) {
			int progressIndex = taskData.progressData[0];
			int amountNeeded = taskData.progressData[1];
			int previousDone = player.getStarterTaskAttributes().getProgress()[progressIndex];
			if ((previousDone + amount) < amountNeeded) {
				player.getStarterTaskAttributes().getProgress()[progressIndex] = previousDone + amount;
				if (previousDone == 0)
					player.getPacketSender().sendString(taskData.interFaceId, "@yel@" + taskData.taskName);
			} else {
				finishTask(player, taskData);
			}
		}
	}

	public static void finishTask(Player player, StarterTaskData tasks) {
		if (player.getStarterTaskAttributes().getCompletion()[tasks.ordinal()])
			return;
		player.getStarterTaskAttributes().getCompletion()[tasks.ordinal()] = true;
	}

	public static void openInterface(Player player, StarterTaskData task) {
		player.getPacketSender().sendString(53220, "" + task.taskDescription);
		player.getPacketSender().sendString(53221, "" + task.taskDescription1);
		player.getPacketSender().sendString(53222, "" + task.taskDescription2);
		player.getPacketSender().sendString(53223, "" + task.taskDescription3);

		if (!task.requirements.equalsIgnoreCase("")) {
			player.getPacketSender().sendString(53226, "Task Requirements:@yel@ " + task.requirements);
		} else {
			player.getPacketSender().sendString(53226, "Task Requirements: @yel@None");
		}

		if (player.getStarterTaskAttributes().getCompletion()[task.ordinal()]) {
			player.getPacketSender().sendString(53225, "Progress: @gre@100% (1/1)");
		} else if (task.progressData == null) {
			player.getPacketSender().sendString(53225, "Progress: @gre@0% (0/0)");

		} else {
			int currentProgress = player.getStarterTaskAttributes().getProgress()[task.progressData[0]];
			int totalProgress = task.progressData[1];
			boolean completed = player.getStarterTaskAttributes().getCompletion()[task.ordinal()];
			double percent = ((double) currentProgress * 100) / (double) totalProgress;
			if (currentProgress == 0) {
				player.getPacketSender().sendString(53225,
						"Progress: @gre@0 (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			} else if (currentProgress != totalProgress) {
				player.getPacketSender().sendString(53225,
						"Progress: @yel@" + percent + "% (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			} else if (completed) {
				player.getPacketSender().sendString(53225,
						"Progress: @gre@" + percent + " (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			}
		}

		player.getPacketSender().sendInterface(53200);
	}

	public static class StarterTaskAttributes {

		public StarterTaskAttributes() {

		}

		/** Tasks **/
		private boolean[] completed = new boolean[StarterTaskData.values().length];
		private int[] progress = new int[55];

		public boolean[] getCompletion() {
			return completed;
		}

		public void setCompletion(int index, boolean value) {
			this.completed[index] = value;
		}

		public void setCompletion(boolean[] completed) {
			this.completed = completed;
		}

		public int[] getProgress() {
			return progress;
		}

		public void setProgress(int index, int value) {
			this.progress[index] = value;
		}

		public void setProgress(int[] progress) {
			this.progress = progress;
		}

	}
}
