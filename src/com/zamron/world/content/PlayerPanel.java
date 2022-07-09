package com.zamron.world.content;

import com.zamron.model.definitions.DropUtils;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.DailyNPCTask;
import com.zamron.world.content.combat.TenKMassacre;
import com.zamron.world.content.event.SpecialEvents;
import com.zamron.world.entity.impl.player.Player;

public class PlayerPanel {
	
	
	//#1
		public static final String LINE_START = "            ";
		public static final String LINE5_START = " ";
		public static final String LINE1_START = "                ";
		public static final String LINE3_START = "              ";
		public static final String LINE2_START = " ";
		//#2
		public static final String LINE6_START = " ";
		public static final String MIDDLE_START = " ";
		public static final String MIDDLE2_START = " ";
		//#3
		public static final String LINE7_START = "          ";
		public static final String LINE9_START = " ";
		public static final String MIDDLE3_START = "           ";
		public static final String MIDDLE8_START = " ";
		public static final String MIDDLE7_START = "              ";
		public static final String MIDDLE6_START = " ";
		//#4
		public static final String MIDDLE9_START = " ";
		public static final String MIDDLE10_START = "            ";
		public static final String MIDDLE11_START = " ";
		public static final String MIDDLE12_START = "          ";
		//#5
		public static final String MIDDLE13_START = " ";
		public static final String MIDDLE14_START = " ";
		
	//#quest tab1
		public static final String Quest_START = "               ";
		public static final String Quest2_START = "                ";
		public static final String Quest3_START = "                 ";
		public static final String Quest4_START = "                     ";
		public static final String Quest5_START = "                   ";
		public static final String Quest6_START = "                  ";
		//#quest tab 1 under writting
		//#quest tab1
			public static final String QuestU_START = "                    ";
			public static final String QuestU2_START = "                      ";
			public static final String QuestU3_START = "-                    ";
			public static final String QuestU4_START = "                    ";
			public static final String QuestU5_START = "                       ";
			public static final String QuestU6_START = "                  ";

	private static int FIRST_STRING = 39159;
	private static int LAST_STRING = 39210;


	public static void handleSwitch(Player player, int index, boolean fromCurrent) {
		if (!fromCurrent) {
			resetStrings(player);
		}
		player.currentPlayerPanelIndex = index;
		switch (index) {
		case 1:
			refreshPanel(player); // first tab, cba rename just yet.
			break;

		case 2:
			sendSecondTab(player);
			break;
		case 3:
			sendThirdTab(player);
			break;
		case 4:
			sendForthTab(player);
			break;
		}
	}

	public static void refreshCurrentTab(Player player) {
		handleSwitch(player, player.currentPlayerPanelIndex, true);
	}

	public static void refreshPanel(Player player) {

		if (player.currentPlayerPanelIndex != 1) { // now it would update the other tab, if this is not the current tab
			refreshCurrentTab(player);
			return;
		}
		String[] Messages = new String[] { "  ", "<img=35>@blu@ Player Information", "",
				"@whi@-@gre@Server Time:", "@bla@*@whi@"+Misc.getCurrentServerTime(),
				"@whi@-@gre@Time Played:", "@bla@*@whi@"+Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())),
				"@whi@-@gre@Drop Rate:", "@bla@*@whi@" +DropUtils.drBonus(player) + "%",
				"@whi@-@gre@Cleansing Time:", "@bla@*@whi@" + player.getCleansingTime()+ " ms",
	 			"@whi@-@gre@Total Level: ", "@bla@*@whi@" +player.getSkillManager().getTotalLevel(),
	 			"@whi@-@gre@Username:", "@bla@*@whi@"+player.getUsername(),
				"@whi@-@gre@Difficulty:", "@bla@*@whi@"+player.getDifficulty().toString(),
	 	 		"@whi@-@gre@Rank: ", "@bla@*@whi@"+player.getRights().toString().replace("_", " "),
	 	 		"@whi@-@gre@Donator Rank: ", "@bla@*@whi@"+player.getSecondaryPlayerRights().toString().replace("_", " "),
	 			"@whi@-@gre@Donated:", "@bla@*@whi@"+player.getAmountDonated(),
				"@whi@-@gre@Exp Lock: ", "@bla@*@whi@"+(player.experienceLocked() ? "Locked" : "Unlocked"),


	
		};
		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				/*System.out.println("PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");*/
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendSecondTab(Player player) {

		String[] Messages = new String[] { "  ", "<img=35>@red@ World And Events", "",
				"@whi@-@gre@Evil Tree:",
			 "@bla@*@whi@"	+(EvilTrees.getLocation() != null ? EvilTrees.getLocation().playerPanelFrame : "N/A"),
			 
			 "@whi@-@gre@Well of Goodwill:",
			 "@bla@*@whi@"	+(WellOfGoodwill.isActive() ? WellOfGoodwill.getMinutesRemaining() + " mins" : "N/A"),
			 
			 "@whi@-@gre@Crashed Star:",
		 "@bla@*@whi@"	+(ShootingStar.getLocation() != null ?ShootingStar.getLocation().playerPanelFrame : "N/A"),
		 
		 "@whi@-@gre@Bonus:",
			 "@bla@*@whi@"	+ SpecialEvents.getSpecialDay(),

				"@whi@-@gre@NPC Deaths Until Prize Draw:",
				"@bla@*@whi@"	+ TenKMassacre.CURRENT_SERVER_KILLS+"/"+TenKMassacre.REQUIRED_SERVER_KILLS,

				"@whi@-@gre@Daily NPC Task:",
				"@bla@*@whi@" + DailyNPCTask.KILLS_REQUIRED + " x " + NpcDefinition.forId(DailyNPCTask.CHOSEN_NPC_ID).getName(),
				"@bla@*@whi@Progress: "	+ player.getCurrentDailyNPCKills()+"/"+DailyNPCTask.KILLS_REQUIRED,


		 
		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				//System.out.println("PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						//+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendThirdTab(Player player) {

		String[] Messages = new String[] { "  ", "<img=35>@whi@ Points & Statistics", "",
				
				"@whi@-@gre@Loyalty Points: ",
			 "@bla@*@whi@"+player.getPointsHandler().getLoyaltyPoints(),

			 "@whi@-@gre@Custom Well Donations: ",
	 "@bla@*@whi@"		+player.getCustomDonations(),

	 "@whi@-@gre@Prestige Points: ",
	 "@bla@*@whi@"	+player.getPointsHandler().getPrestigePoints(),

	 "@whi@-@gre@Trivia Points: ",
 "@bla@*@whi@"	+player.getPointsHandler().getTriviaPoints(),

 "@whi@-@gre@Voting Points: ",
 "@bla@*@whi@"	+player.getPointsHandler().getVotingPoints(),

 "@whi@-@gre@Donation Points: ",
 "@bla@*@whi@"	+player.getPointsHandler().getDonationPoints(),

 "@whi@-@gre@Dungeon Points: ",
 "@bla@*@whi@"	+player.getDungeonPoints(),

 "@whi@-@gre@Raid Points: ",
 "@bla@*@whi@"	+player.getPointsHandler().getRaidPoints(),

 "@whi@-@gre@Pest Control points: ",
 "@bla@*@whi@"	+player.getPointsHandler().getCustompestcontrolpoints(),

 "@whi@-@gre@Dung. Tokens: ",
"@bla@*@whi@"	+player.getPointsHandler().getDungeoneeringTokens(),

"@whi@-@gre@Boss Points: ",
"@bla@*@whi@"	+player.getBossPoints(),

"@whi@-@gre@Custom Boss Points: ",
	 "@bla@*@whi@"	+player.getCustomPoints(),

	 "@whi@-@gre@Slayer Points: ",
	  "@bla@*@whi@" +player.getPointsHandler().getSlayerPoints(),
	 	 
	  "@whi@-@gre@Bravek Tasks Completed: ",
	 "@bla@*@whi@" +player.getBravekTasksCompleted(),

	 "@whi@-@gre@Minigame Points1: ",
	 "@bla@*@whi@" +player.getPointsHandler().getminiGamePoints1(),

	 "@whi@-@gre@Pk Points: ",
	 "@bla@*@whi@" +player.getPointsHandler().getPkPoints(),

	 "@whi@-@gre@Wilderness Killstreak: ",
"@bla@*@whi@"+player.getPlayerKillingAttributes().getPlayerKillStreak(),

"@whi@-@gre@Wilderness Kills: ",
"@bla@*@whi@" +player.getPlayerKillingAttributes().getPlayerKills(),

"@whi@-@gre@Wilderness Deaths: ",
"@bla@*@whi@" +player.getPlayerKillingAttributes().getPlayerDeaths(),

		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				//System.out.println("PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						//+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendForthTab(Player player) {

		String[] Messages = new String[] { " ", "<img=35>@gre@ Slayer Information", "",

				"@whi@-@gre@Master: ",
			 "@bla@*@whi@"	 +player.getSlayer().getSlayerMaster(),
		

			 "@whi@-@gre@Duo Partner: ",
			"@bla@*@whi@"	+ player.getSlayer().getDuoPartner(),
			
			"@whi@-@gre@Task: ",
		 "@bla@*@whi@"		+player.getSlayer().getSlayerTask(),
				
		 "@whi@-@gre@Task Amount: ",
		 "@bla@*@whi@"	+player.getSlayer().getAmountToSlay(),
		 
		 "@whi@-@gre@Task Streak: ",
 "@bla@*@whi@"	+player.getSlayer().getTaskStreak(),

		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				//System.out.println("PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						//+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void resetStrings(Player player) {
		for (int i = FIRST_STRING; i < LAST_STRING; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

}