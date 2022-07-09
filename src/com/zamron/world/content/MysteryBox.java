package com.zamron.world.content;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class MysteryBox {

	/**
	 * The player object that will be triggering this event
	 */
	private final Player plr;
	private final int BOX = 6199;
	private final int INTERFACE_ID = 47000;
	private final int ITEM_FRAME = 47101;
	private int spinNum = 0;
	private boolean canMysteryBox = true;
	private int mysteryPrize;
	private int mysteryPrizeTier;

	public int getSpinNum() { return spinNum; }
	public boolean canMysteryBox() { return canMysteryBox; }
	public int getMysteryPrize() { return mysteryPrize; }
	public int getMysteryPrizeTier() { return mysteryPrizeTier; }

	/**
	 * Constructs a new mystery box to handle item receiving for this player alone
	 * @param plr the player
	 */
	public MysteryBox(Player plr) {
		this.plr = plr;
	}

	public void spin() {
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(BOX)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(BOX, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		process();
	}

	@SuppressWarnings("unused")
	public void process() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{19721,19722,19723,19734,19736,19468,18363,15398,15418}, //Common, 0
				{9006,3941,3974,18392,4799,4800,4801}, //Uncommon, 1
				{15012,1499,3951,5079,5187,5186,3316,3931,14559,6583} };

		int[] common = { 19721,19722,19723,19734,19736,19468,18363,15398,15418};
		int[] uncommon = { 9006,3941,3974,18392,4799,4800,4801 };
		int[] veryUncommon = { 15012,1499,3951,5079,5187,5186,3316,3931,14559,6583 };

		int[][] all = {common, uncommon, veryUncommon};
		int[] tier = all[Misc.random(2)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(2)];
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(2)];
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}

	public void spinRUBox() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15373)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15373, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processRUBox();
	}
	
	@SuppressWarnings("unused")
	public void processRUBox() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;
		int rewards2[][] = {
				{2577, 902, 903, 904, 905,896}, //Common, 0
				{20016,20017,20018,20021,20022,17911,19892,3956,3928,17908,17909,3932,4775}, //Uncommon, 1
				{19137, 19138, 19139, 18940,18942, 18941} };
		
		int[] common = { 2577, 902, 903, 904, 905,3082 };
		int[] uncommon = {2577, 902, 903, 904, 905,3082 };
		int[] veryUncommon = { 20016,20017,20018,20021,20022,17911,19892,3956,3928,17908,17909,3932,4775 };
		int[] rare = { 20016,20017,20018,20021,20022,17911,19892,3956,3928,17908,17909,3932,4775 };
		int[] veryRare = {19137, 19138, 19139, 18940,18942, 18941};
		int[] extremelyRare = { 19137, 19138, 19139, 18940,18942, 18941 };
		int[] legendary = {19137, 19138, 19139, 18940,18942, 18941};
		 
		int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
		int[] tier = all[Misc.random(6)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(6)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(6)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	//15369 = common - 15370 = uncommon - 15371 = rare - 15372 = Extreme ( rewards on ruboxes )
	public void spinMbox1() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15369)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15369, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processMbox1();
		ScavengeGain.MysteryBox(plr);
	}
	
	public void spinMbox3() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15371)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15371, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processMbox3();
		ScavengeGain.MysteryBox(plr);
	}
	
	public void spinMbox4() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15372)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15372, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processMbox4();
		ScavengeGain.MysteryBox(plr);
	}
	
	@SuppressWarnings("unused")
	public void processMbox4() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{3749,1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024, 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204, 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544, 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common, 0
				{15501, 11133, 15126, 10828, 6199, 3751, 3753, 11884, 10589, 18782, 6739, 6739, 2577, 2581, 18782, 15332, 15332, 15332, 11732, 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751, 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728, 4730, 4151, 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140, 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739, 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848,  2577, 2581, 2572, 15501, 18782, 6920, 6922, 11335, 15241, 15243, 6585, 4151, 11696, 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730, 11283, 18349, 18351, 18353, 18355, 18357, 18359, 11484, 2527, 12601, 15486, 15018, 15019, 15020, 15220} };
		
		

		 int [] common = {15507, 15259, 19066, 19021, 17291, 12601, 15501, 19317, 19320, 4151, 6585, 2572, 6199, 4151, 6585, 2572, 6199, 9470, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 19317, 19320, 19308, 19314, 10836, 10837, 10838, 10839, 19747};
		 int [] uncommon = {15507, 15259, 19066, 19021, 17291, 12601, 15501, 19317, 19320, 4151, 6585, 2572, 6199, 4151, 6585, 2572, 6199, 9470, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 19317, 19320, 19308, 19314, 10836, 10837, 10838, 10839, 19747};
		 
		 
		int[][] all = {common, uncommon};
		int[] tier = all[Misc.random(1)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	
	@SuppressWarnings("unused")
	public void processMbox3() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024, 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204, 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544, 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common, 0
				{15501, 11133, 15126, 10828, 6199, 3751, 3753, 11884, 10589, 18782, 6739, 6739, 2577, 2581, 18782, 15332, 15332, 15332, 11732, 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751, 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728, 4730, 4151, 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140, 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739, 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848,  2577, 2581, 2572, 15501, 18782, 6920, 6922, 11335, 15241, 15243, 6585, 4151, 11696, 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730, 11283, 18349, 18351, 18353, 18355, 18357, 18359, 11484, 2527, 12601, 15486, 15018, 15019, 15020, 15220} };
		
		

		 int [] common = {9813, 6666, 14008, 14009, 14010, 14012, 14013,13740, 13742, 13744, 13738, 15501, 7671, 7673, 19311, 19308, 19314,4151, 6585, 2572, 6199, 9470, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839};
		 int [] uncommon = {9813, 6666, 14008, 14009, 14010, 14012, 14013,13740, 13742, 13744, 13738, 15501, 7671, 7673, 19311, 19308, 19314,4151, 6585, 2572, 6199, 9470, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839};
		 
		 
		int[][] all = {common, uncommon};
		int[] tier = all[Misc.random(1)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	
	
	@SuppressWarnings("unused")
	public void processMbox1() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024, 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204, 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544, 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common, 0
				{15501, 11133, 15126, 10828, 6199, 3751, 3753, 11884, 10589, 18782, 6739, 6739, 2577, 2581, 18782, 15332, 15332, 15332, 11732, 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751, 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728, 4730, 4151, 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140, 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739, 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848,  2577, 2581, 2572, 15501, 18782, 6920, 6922, 11335, 15241, 15243, 6585, 4151, 11696, 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730, 11283, 18349, 18351, 18353, 18355, 18357, 18359, 11484, 2527, 12601, 15486, 15018, 15019, 15020, 15220} };
		
		

		 int [] common = {4151, 9813, 15019, 14004, 15018, 15020, 15501, 18782, 18782, 14004, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] uncommon = {14484, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] veryUncommon = {14484, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] rare = {4151, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] veryRare = {4151, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] extremelyRare  = {4151, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] legendary  = {4151, 9813, 15019, 15220, 15018, 15020, 15501, 18782, 18782, 20000, 20001, 20002, 13263, 18337, 4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 
		 
		int[][] all = {common, uncommon, veryUncommon, rare, veryRare, extremelyRare, legendary};
		int[] tier = all[Misc.random(6)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(6)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(6)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	
	@SuppressWarnings("unused")
	public void processMbox2() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127, 1093, 15332, 3024, 15272, 6685, 1434, 2, 536, 534, 1149, 1305, 1377, 1434, 1615, 3000, 3204, 2941, 2947, 2503, 15272, 2503, 10499, 6199, 6326, 861, 1163, 1201, 6111, 544, 6199, 542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 2572, 11118}, //Common, 0
				{15501, 11133, 15126, 10828, 6199, 3751, 3753, 11884, 10589, 18782, 6739, 6739, 2577, 2581, 18782, 15332, 15332, 15332, 11732, 6199, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889, 4716, 4718, 4720, 4722, 4745, 4747, 4749, 4751, 4708, 4710, 4712, 4714, 4732, 4734, 4736, 4738, 4724, 4726, 4728, 4730, 4151, 11732, 6585, 2577, 2581, 6737, 11235, 4675, 4087, 1187, 6914, 6889, 3140, 6916, 6918, 6920, 6922, 6924, 6731, 6735, 6733}, //Uncommon, 1
				{3749, 3753, 10828, 1215, 4587, 4091, 4093, 4095, 4097, 1079, 1127,6739, 15259, 15332, 15126, 11856, 11854, 11852, 11846, 11850, 11732, 11848,  2577, 2581, 2572, 15501, 18782, 6920, 6922, 11335, 15241, 15243, 6585, 4151, 11696, 11724, 11726, 11728, 11694, 11718, 11720, 11722, 11700, 11716, 11698, 11730, 11283, 18349, 18351, 18353, 18355, 18357, 18359, 11484, 2527, 12601, 15486, 15018, 15019, 15020, 15220} };
		
		

		 int [] common = {18893, 19040, 19041, 19042, 19043, 7462, 11718, 11720, 11722, 11724, 11726, 20012, 20010, 20011, 20019, 20020, 20016, 20017, 20018, 20021, 20022, 15501,4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 int [] uncommon = {18893, 19040, 19041, 19042, 19043, 19022, 11718, 11720, 11722, 11724, 11726, 20012, 20010, 20011, 20019, 20020, 20016, 20017, 20018, 20021, 20022, 15501,4151, 6585, 2572, 6199, 14000, 14001, 14002, 14003, 14004, 14005, 14006, 14007, 10836, 10837, 10838, 10839, 18744, 18745, 18746};
		 
		 
		int[][] all = {common, uncommon};
		int[] tier = all[Misc.random(1)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	
	@SuppressWarnings("unused")
	public void processMbox5() {
		// Reset prize
		mysteryPrize = -1;
		// Can't spin when already in progress
		canMysteryBox = false;

		int rewards2[][] = {
				{13275, 13271, 13272, 13273, 13274}, //Common, 0
				{13275, 13271, 13272, 13273, 13274}, //Uncommon, 1
				{13275, 13271, 13272, 13273, 13274} };
		
		

		 int [] common = {13275, 13271, 13272, 13273, 13274};
		 int [] uncommon = {13275, 13271 , 13272, 13273, 13274};
		 
		 
		int[][] all = {common, uncommon};
		int[] tier = all[Misc.random(1)];
		int PRIZE_ID = tier[Misc.random(tier.length -1)];

		//final int PRIZE_ID = 1040; // TODO: Add box prize logic here, use mysteryPrizeTier too (0-6) for different coloured reward text
		mysteryPrize = PRIZE_ID; // For rewards handling

		// Send items to interface
		// Move non-prize items client side if you would like to reduce server load
		if (spinNum == 0) {
			for (int i=0; i<66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, 55, PRIZE_ID, NOT_PRIZE_ID);
			}
		} else {
			for (int i=spinNum*50 + 16; i<spinNum*50 + 66; i++){
				tier = all[Misc.random(1)];			
				final int NOT_PRIZE_ID = tier[Misc.random(tier.length -1)];
				sendItem(i, (spinNum+1)*50 + 5, PRIZE_ID, NOT_PRIZE_ID);
			}
		}
		spinNum++;
	}
	
	public void spinMbox2() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15370)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15370, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processMbox2();
		ScavengeGain.MysteryBox(plr);
	}
	
	public void spinMbox5() {
		// Server side checks for spin
		if (!canMysteryBox) {
			plr.sendMessage("Please finish your current spin.");
			return;
		}
		if (!plr.getInventory().contains(15501)) {
			plr.sendMessage("You require a mystery box to do this.");
			return;
		}

		// Delete box
		plr.getInventory().delete(15501, 1);
		// Initiate spin
		plr.sendMessage(":spin");
		processMbox5();
		ScavengeGain.MysteryBox(plr);
	}
	
	public void sendItem(int i, int prizeSlot, int PRIZE_ID, int NOT_PRIZE_ID) {
		if (i == prizeSlot) {
			plr.getPA().mysteryBoxItemOnInterface(PRIZE_ID, 1, ITEM_FRAME, i);
		}
		else {
			plr.getPA().mysteryBoxItemOnInterface(NOT_PRIZE_ID, 1, ITEM_FRAME, i);
		}
	}

	public void reward() {
		if (mysteryPrize == -1) {
			return;
		}
		
		plr.getInventory().add(mysteryPrize, 1);
		
		// Reward text colour
		String tier = "";
		switch (mysteryPrizeTier) {
			case 0: tier = "<col=336600>"; break; // Common
			case 1: tier = "<col=005eff>"; break; // Uncommon
			case 2: tier = "<col=ff3000>"; break; // Very Uncommon
			case 3: tier = "<col=B80000>"; break; // Rare
			case 4: tier = "<col=ff00ff>"; break; // Very Rare
			case 5: tier = "<col=ffffff>"; break; // Extremely Rare
			case 6: tier = "<col=FFA500>"; break; // Legendary
		}
		
		// Reward message
		String name = ItemDefinition.forId(mysteryPrize).getName();
		if (mysteryPrize == 19886 || mysteryPrize == 3666 || mysteryPrize == 3664 || mysteryPrize == 18896 || mysteryPrize == 3662 || mysteryPrize == 17776 || mysteryPrize == 5130 || mysteryPrize == 3072 || mysteryPrize == 18998 || mysteryPrize == 3074 || mysteryPrize == 18954 || mysteryPrize == 18949 || mysteryPrize == 18953 || mysteryPrize == 18962 || mysteryPrize == 18955 || mysteryPrize == 18956) {
			World.sendMessageNonDiscord("[<img=12>][@red@Mega Or Ultra Mystery Box@bla@] @red@"+plr.getUsername()+"@bla@ has won a "+tier +name+"@bla@!");
		}
		
		if (name.substring(name.length() - 1).equals("s")) {
			plr.sendMessage("Congratulations, you have won " + tier + name + "@bla@!");
		}
		else {
			plr.sendMessage("Congratulations, you have won a " + tier + name + "@bla@!");
		}
		
		// Can now spin again
		canMysteryBox = true;
	}

	public void openInterface() {
		// Reset interface
		plr.sendMessage(":resetBox");
		spinNum = 0;
		// Open
		plr.getPA().sendInterface(INTERFACE_ID);
	}
}