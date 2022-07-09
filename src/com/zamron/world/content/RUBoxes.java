package com.zamron.world.content;

import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class RUBoxes {


	public static final int [] commonRewards = {10835};
	public static final int [] uncommonRewards = {10835};
	public static final int [] rareRewards = {10835};
	public static final int [] extremeRewards = {10835};
	public static final int [] legendaryRewards = {10835};

	public static void openCommonBox (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15369, 1);
		commonRReward(player);
	}

	public static void openUncommonBox (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15370, 1);
		uncommonRReward(player);
	}

	public static void openRareBox (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15371, 1);
		rareRReward(player);
	}

	public static void openExtremeBox (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15372, 1);
		extremeRReward(player);
	}

	public static void openLegendaryBox (Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.getPacketSender().sendMessage("You need at least 3 inventory spaces");
			return;
		}
		player.getInventory().delete(15373, 1);
		legendaryRReward(player);
	}


	public static void commonRReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(4) == 1) {
			player.getPacketSender().sendMessage("Your box has disappeared! Sorry!");
		} else {
			player.getInventory().add(commonRewards[Misc.getRandom(commonRewards.length - 1)], 1);
			player.getInventory().add(995, 100000 + RandomUtility.RANDOM.nextInt(500000));
		}
	}

	public static void uncommonRReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(5) == 1) {
			player.getPacketSender().sendMessage("Your box has disappeared! Sorry!");
		} else {
			player.getInventory().add(uncommonRewards[Misc.getRandom(uncommonRewards.length - 1)], 1);
			player.getInventory().add(995, 250000 + RandomUtility.RANDOM.nextInt(785000));
		}
	}

	public static void rareRReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(6) == 1) {
			player.getPacketSender().sendMessage("Your box has disappeared! Sorry!");
		} else {
			player.getInventory().add(rareRewards[Misc.getRandom(rareRewards.length - 1)], 1);
			player.getInventory().add(995, 500000 + RandomUtility.RANDOM.nextInt(1000000));
		}
	}

	public static void extremeRReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(8) == 1) {
			player.getPacketSender().sendMessage("Your box has disappeared! Sorry!");
		} else {
			player.getInventory().add(extremeRewards[Misc.getRandom(extremeRewards.length - 1)], 1);
			player.getInventory().add(995, 1250000 + RandomUtility.RANDOM.nextInt(2500000));
		}
	}

	public static void legendaryRReward(Player player) {
		if (RandomUtility.RANDOM.nextInt(10) == 1) {
			player.getPacketSender().sendMessage("Your box has disappeared! Sorry!");
		} else {
			player.getInventory().add(legendaryRewards[Misc.getRandom(legendaryRewards.length - 1)], 1);
			player.getInventory().add(995, 2500000 + RandomUtility.RANDOM.nextInt(2750000));
		}
	}


}
