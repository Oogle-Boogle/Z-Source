package com.zamron.world.content;

import com.zamron.model.Item;
import com.zamron.util.Misc;
import com.zamron.util.StringUtils;
import com.zamron.world.entity.impl.player.Player;

/**
 *
 * @author Emerald
 */

public class NpcTasks {

	public enum NpcTaskData {

		KILL_50_JOKER("Kill 50 Jokers", 65607, "Some Gear", new int[] { 0, 50 }, "Kill 50 jokers @ ::starterzone", "", "",
				"", new Item[] { new Item(15373, 1),new Item(19864, 20) }), // 14484 is itemid, 1 is amount

		KILL_100_FROST_DRAGONS("Kill 100 Frost Dragons", 65608, "Some Gear", new int[] { 1, 100 }, "Kill 100 Frost dragons",
				"Find them in starter teleports", "", "",
				new Item[] { new Item(15373, 1), new Item(989, 3), new Item(10835, 50) }),
		KILL_150_SIRENIC_BEASTS("Kill 150 Sirenic Beasts", 65609, "Good Gear", new int[] { 2, 150 },
				"Kill 150 Sirenic Beasts", "Find them in Starter teleports", "", "",
				new Item[] { new Item(10835, 100), new Item(15373, 1), new Item(6199, 1) }),
		KILL_250_HADES("Kill 250 Hades", 65610, "Decent Gear", new int[] { 3, 250 }, "Kill 250 Hades",
				"Find them in Medium teleports", "", "",
				new Item[] { new Item(6199, 1), new Item(10835, 100) }),
		KILL_300_DEFENDERS("Kill 300 Defenders", 65611, "Decent Gear", new int[] { 4, 300 },
				"Kill 50 300 Defenders", "Find them in medium teleports", "Mass Boss", "",
				new Item[] { new Item(6199, 2),new Item(10835, 150) }),
		KILL_250_DEMONIC_OLMS("Kill 250 Demonic Olms", 65612, "Decent Gear", new int[] { 5, 250 }, "Kill 250 Demonic Olms",
				"Find them in the medium teleports", "Mass Boss", "",
				new Item[] { new Item(6199, 2), new Item(10835, 250), new Item(19864, 50)}),
		KILL_50_CEREBRUS("Kill 50 Cerebrus", 65613, "Desent Gear", new int[] { 6, 50 },
				"Kill 50 Cerebrus", "Find them in medium teleports", "Mass Boss", "",
				new Item[] { new Item(10835, 100), new Item(19864, 100), new Item(6199, 1) }),
		KILL_500_ZEUS("Kill 500 Zeus", 65614, "Desent Gear", new int[] { 7, 500 }, "Kill 500 Zeus", "Find them at medium teleports",
				"Mass Boss", "",
				new Item[] { new Item(10835, 200), new Item(6199, 2), new Item(19864, 75) }),
		KILL_50_INFARTICO("Kill 50 Infartico", 65615, "Decent Gear", new int[] { 8, 50 },
				"Kill 50 Infarticos", "Find them in hardened teleports", "Mass Boss", "",
				new Item[] { new Item(10835, 100), new Item(19864, 30) }),
		KILL_500_LORD_VALOR("Kill 500 Lord Valors", 65616, "Decent Gear", new int[] { 9, 500 }, "Kill 500 Lord Valor",
				"Find them at medium teleports", "Mass Boss", "",
				new Item[] { new Item(10835, 100), new Item(19864, 10) }),
		KILL_5000_LORD_VALOR("Kill 5000 lord Valors", 65617, "Desent Gear", new int[] { 10, 5000 },
				"Kill 5000 Lord Valor", "Find them at hardened teleports", "Mass Boss", "",
				new Item[] { new Item(10835, 500), new Item(19864, 100)}),
		KILL_500_DZANTH("Kill 500 Dzanth wizards", 65620, "Decent Gear", new int[] { 13, 500 },
				"Kill 500 Dzanth wizards", "Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 100), new Item(19864, 15) }),
		KILL_3000_DZANTH("Kill 3000 Dzanth wizards", 65621, "Decent Gear", new int[] { 14, 3000 }, "Kill 3000 Dzanth wizards",
				"Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 1000), new Item(19864, 150),new Item(3988, 1) }),
		KILL_50_KINGKONG("Kill 50 King Kong", 65622, "Some Gear", new int[] { 15, 50 }, "Kill 50 King Kong",
				"Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 1), new Item(19864, 15),new Item(3988, 1) }),
		KILL_500_KINGKONG("Kill 500 King Kong", 65623, "Some Gear", new int[] { 16, 500 }, "Kill 500 King Kong",
				"Find them in hardened teleport", "", "",
				new Item[] { new Item(10835, 200), new Item(19864, 15),new Item(3988, 1) }),
		KILL_50_CORP_BEAST("Kill 50 Corp beasts", 65624, "Some Gear", new int[] { 17, 50 }, "Kill 50 Corp beasts",
				"Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 100), new Item(19864, 35) }),
		KILL_500_CORP_BEAST("kill 500 Corp beasts", 65625, "Decent Gear", new int[] { 18, 500 }, "Kill 500 Corp beasts",
				"Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 350), new Item(19864, 50),new Item(3988, 1) }),
		KILL_2500_CORP_BEAST("kill 2500 Corp Beasts", 65626, "Decent Gear", new int[] { 19, 2500 }, "Kill 2500 Corp beasts",
				"Find them at hardened teleports", "", "",
				new Item[] {  new Item(19864, 150),new Item(3988, 1) }),
		KILL_500_LUCID_WARRIORS("kill 500 Lucid Warriors", 65627, "Decent Gear", new int[] { 20, 500 },
				"Kill 500 Lucid warriors", "Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 170), new Item(19864, 75) }),
		KILL_2500_LUCID_WARRIORS("kill 2500 Lucid Warriors", 65628, "Decent Gear", new int[] { 21, 2500 },
				"Kill 2500 Lucid warriors", "Find them at hardened teleports", "", "",
				new Item[] { new Item(10835, 350), new Item(19864, 55), new Item(3988, 1) }),
		KILL_10000_LUCID_WARRIORS1("kill 10000 Lucid warriors", 65629, "Decent Gear", new int[] { 22, 10000 },
				"Kill 10000 Lucid warriors", "Find them at hardened teleports", "Mass Boss", "",
				new Item[] {new Item(3988, 1), new Item(10835, 1000),
						 }),
		KILL_500_HULK("Kill 500 Hulk", 65630, "Decent Gear", new int[] { 23, 500 },
				"Kill 500 Hulk", "Find them at hardened teleports", "Mass Boss", "",
				new Item[] { new Item(5155, 1), new Item(14808, 1),new Item(10835, 1),
						new Item(10835, 1) }),
		KILL_10000_HULK("Kill 10000 Hulk", 65631, "Decent Gear", new int[] { 24, 10000 }, "Kill 10000 Hulk",
				"Find them at ::hulk", "", "",
				new Item[] { new Item(19092, 1),new Item(3988, 1), new Item(10835, 5000) }),
		KILL_500_DARKBLUE_WIZARDS("kill 500 Darkblue wizards", 65632, "Decent Gear", new int[] { 25, 500 }, "Kill 500 Darkblue wizards",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 100), new Item(19864, 50),new Item(3988, 1) }),
		KILL_2500_DARKBLUE_WIZARDS("Kill 2500 Darkblue Wizards", 65633, "Decent Gear", new int[] { 26, 2500 },
				"Kill 2500 Darkblue Wizards", "Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 200), new Item(19864, 100),new Item(3988, 1) }),
		KILL_10000_HEATED_PYRO("Kill 10000 Heated pyro", 65634, "Decent Gear", new int[] { 27, 10000 }, "Kill 10000 Heated pyros",
				"Find them at Expert teleports", "", "",
				new Item[] {  new Item(10835, 500),new Item(3988, 1) }),
		KILL_2500_WYRM("kill 10000 Lucid Warriors", 65635, "Decent Gear", new int[] { 28, 2500 },
				"Kill 2500 Darkfire wyrm", "Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 500) }),
		KILL_5000_WYRM("kill 5000 Darkfire wyrm", 65636, "Decent Gear", new int[] { 29, 5000 },
				"Kill 5000 Darkfire wyrm", "Find them at Expert teleports", "", "",
				new Item[] {  new Item(10835, 100),new Item(3988, 1) }),
		KILL_2500_TRINITY("kill 2500 Trinity", 65637, "Decent Gear", new int[] { 30, 2500 }, "Kill 2500 Trinity",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 100)
						 }),
		KILL_5000_TRINITY("Kill 5000 Trinity", 65638, "Decent Gear", new int[] { 31, 5000 }, "Kill 5000 Trinity",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(19864, 100),new Item(3988, 1),
						 new Item(10835, 500) }),
		KILL_2500_CLOUD("Kill 2500 Cloud", 65639, "Decent Gear", new int[] { 32, 2500 },
				"Kill Kill 2500 Cloud", "Find them at Expert teleports", "", "",
				new Item[] { new Item(19864, 100), new Item(10835, 100) }),
		KILL_5000_CLOUD("kill 5000 Cloud", 65640, "Decent Gear", new int[] { 33, 5000 },
				"Kill 5000 Cloud", "Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 200), new Item(19864, 100),new Item(3988, 1) }),
		KILL_5000_HERBAL_ROGUE("Kill 5000 Herbal Rogue", 65641, "Some Gear", new int[] { 34, 5000 },
				"Kill 5000 Herbal Rogues", "Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 5000), new Item(19864, 100) }),
		KILL_10000_HERBAL_ROGUE("Kill 10000 Herbal Rogues", 65642, "Some Gear", new int[] { 35, 10000 }, "Kill 10000 Herbal Rouge",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 1000),new Item(3988, 1)
						 }),
		KILL_1000_EXODEN("Kill 1000 Exoden", 65643, "Some Gear", new int[] { 36, 1000 }, "Kill 1000 Exoden",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 1), new Item(10835, 500) }),
		KILL_5000_EXODEN("Kill 5000 Exoden", 65644, "Some Gear", new int[] { 37, 5000 }, "Kill 5000 Exoden",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(10835, 1250),new Item(3988, 1)}),
		KILL_10000_EXODEN("Kill 10000 Exoden", 65644, "Some Gear", new int[] { 38, 10000 }, "Kill 10000 Exoden",
				"Find them at Expert teleports", "", "",
				new Item[] {  new Item(10835, 1000),new Item(3988, 2) }),
		
		KILL_1000_SUPREME_NEX("Kill 1000 Supreme Nex", 65645, "Some Gear", new int[] { 39, 1000 }, "Kill 1000 Supreme Nex",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(20054, 3), new Item(10835, 1), new Item(10835, 2500) }),
		KILL_5000_SUPREME_NEX("Kill 5000 Supreme Nex", 65646, "Some Gear", new int[] { 40, 5000 }, "Kill 5000 Supreme Nex",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(20054, 3), new Item(10835, 1), new Item(10835, 5000),new Item(3988, 2),new Item(15374, 1) }),
		KILL_10000_SUPREME_NEX("Kill 10000 Supreme Nex", 65647, "Some Gear", new int[] { 41, 10000 }, "Kill 10000 Supreme Nex",
				"Find them at Expert teleports", "", "",
				new Item[] { new Item(5130, 10), new Item(10835, 5000),new Item(3988, 2) ,new Item(15374, 3)});
		
		NpcTaskData(String taskName, int interFaceId, String requirements, int[] progressData, String taskDescription,
				String taskDescription1, String taskDescription2, String taskDescription3, Item[] rewards) {
			this.taskName = taskName;
			this.interFaceId = interFaceId;
			this.requirements = requirements;
			this.progressData = progressData;
			this.taskDescription = taskDescription;
			this.taskDescription1 = taskDescription1;
			this.taskDescription2 = taskDescription2;
			this.taskDescription3 = taskDescription3;
			this.rewards = rewards;
		}

		private String taskName;
		private int interFaceId;
		private String requirements;
		private int[] progressData;
		private String taskDescription;
		private String taskDescription1;
		private String taskDescription2;
		private String taskDescription3;
		private Item[] rewards;
	}

	static int[] rewards = { 12434, 12435, 12436, 5130, 4646, 4645, 4644, 3286, 6570, 6183 };

	public static boolean claimReward(Player player) {
		for (NpcTaskData tasks : NpcTaskData.values()) {
			if (!player.getNpcTaskAttributes().completed[tasks.ordinal()]) {
				player.sendMessage("Complete all the tasks first before claiming the reward.");
				return false;
			}
		}
		if (!player.npcTaskClaimed) {
			for (int i = 0; i < rewards.length; i++) {
				player.getInventory().add(rewards[i], 1);
			}
		}
		player.sendMessage("Enjoy your reward");
		player.npcTaskClaimed = true;
		return true;
	}

	public static boolean handleButton(Player player, int buttonID) {
		if (!(buttonID >= 71 && buttonID <= 120)) {
			return false;
		}
		int index = -1;

		if (buttonID >= 71) {
			index = -71 + buttonID;
		}

		if (index >= 0 && index < NpcTaskData.values().length) {
			NpcTaskData tasks = NpcTaskData.values()[index];
			getRewardsForTask(player, tasks);
			openInterface(player, tasks);
		}
		return true;
	}

	public static void updateInterface(Player player) {

		for (NpcTaskData tasks : NpcTaskData.values()) {
			boolean completed = player.getNpcTaskAttributes().getCompletion()[tasks.ordinal()];
			boolean progress = tasks.progressData != null
					&& player.getNpcTaskAttributes().getProgress()[tasks.progressData[0]] > 0;
			player.getPacketSender().sendString(tasks.interFaceId,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + tasks.taskName);
		}
	}

	public static void doProgress(Player player, NpcTaskData tasks) {
		doProgress(player, tasks, 1);
	}

	public static void doProgress(Player player, NpcTaskData taskData, int amount) {
		if (player.getNpcTaskAttributes().getCompletion()[taskData.ordinal()])
			return;
		if (taskData.progressData != null) {
			int progressIndex = taskData.progressData[0];
			int amountNeeded = taskData.progressData[1];
			int previousDone = player.getNpcTaskAttributes().getProgress()[progressIndex];
			if ((previousDone + amount) < amountNeeded) {
				player.getNpcTaskAttributes().getProgress()[progressIndex] = previousDone + amount;
				if (previousDone == 0)
					player.getPacketSender().sendString(taskData.interFaceId, "@yel@" + taskData.taskName);
			} else {
				finishTask(player, taskData);
			}
		}
	}

	public static void finishTask(Player player, NpcTaskData tasks) {
		if (player.getNpcTaskAttributes().getCompletion()[tasks.ordinal()])
			return;
		player.getNpcTaskAttributes().getCompletion()[tasks.ordinal()] = true;
		String taskName = StringUtils.capitalizeFirst(StringUtils.usToSpace(tasks.name()));
		player.sendMessage("You have completed the task: " + taskName);
		player.getInventory().addItemSet(tasks.rewards);
		rewardsToText(player, tasks.rewards);
	}

	public static void rewardsToText(Player player, Item[] rewards) {

		for (int i = 0; i < rewards.length; i++) {
			int amount = rewards[i].getAmount();
			if (amount == 1) {
				player.sendMessage(
						"A" + " " + rewards[i].getDefinition().getName() + " Has been added to your inventory");
			} else {
				player.sendMessage(amount + " " + rewards[i].getDefinition().getName() + "s"
						+ " Has been added to your inventory");
			}
		}
	}

	public static void openInterface(Player player, NpcTaskData task) {
		player.getPacketSender().sendString(65220, "" + task.taskDescription);
		player.getPacketSender().sendString(65221, "" + task.taskDescription1);
		player.getPacketSender().sendString(65222, "" + task.taskDescription2);
		player.getPacketSender().sendString(65223, "" + task.taskDescription3);

		if (!task.requirements.equalsIgnoreCase("")) {
			player.getPacketSender().sendString(65226, "Task Requirements:@yel@ " + task.requirements);
		} else {
			player.getPacketSender().sendString(65226, "Task Requirements: @yel@None");
		}

		if (player.getNpcTaskAttributes().getCompletion()[task.ordinal()]) {
			player.getPacketSender().sendString(65225, "Progress: @gre@100% (1/1)");
		} else if (task.progressData == null) {
			player.getPacketSender().sendString(65225, "Progress: @gre@0% (0/0)");

		} else {
			int currentProgress = player.getNpcTaskAttributes().getProgress()[task.progressData[0]];
			int totalProgress = task.progressData[1];
			boolean completed = player.getNpcTaskAttributes().getCompletion()[task.ordinal()];
			double percent = ((double) currentProgress * 100) / (double) totalProgress;
			if (currentProgress == 0) {
				player.getPacketSender().sendString(65225,
						"Progress: @gre@0 (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			} else if (currentProgress != totalProgress) {
				player.getPacketSender().sendString(65225,
						"Progress: @yel@" + percent + "% (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			} else if (completed) {
				player.getPacketSender().sendString(65225,
						"Progress: @gre@" + percent + " (" + Misc.insertCommasToNumber("" + currentProgress) + "/"
								+ Misc.insertCommasToNumber("" + totalProgress) + ")");
			}
		}
		player.getPacketSender().sendInterface(65400);
	}

	/**
	 * 
	 * @param player
	 * @param task
	 *            Emerald
	 */
	public static void getRewardsForTask(Player player, NpcTaskData task) {

		player.getPacketSender().resetItemsOnInterface(65205, 20);

		for (int i = 0; i < task.rewards.length; i++) {
			player.getPacketSender().sendItemOnInterface(65205, task.rewards[i].getId(), i,
					task.rewards[i].getAmount());
		}

	}

	public static class NpcTaskAttributes {

		public NpcTaskAttributes() {

		}

		/** Tasks **/
		private boolean[] completed = new boolean[NpcTaskData.values().length];
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
