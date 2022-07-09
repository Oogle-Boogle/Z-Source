package com.zamron.world.content;

import com.zamron.model.Item;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class Achievements {

	public enum AchievementData {

		FILL_WELL_OF_GOODWILL_1M(AchievementDifficulty.EASY, "Pour 1m Into The Well", 45006, null, "", "", "", "", "", "", new Item(10835, 1000), new Item(8851, 100), new Item(6199, 1)),
		CUT_AN_OAK_TREE(AchievementDifficulty.EASY, "Cut An Oak Tree", 45007, null, "", "", "", "", "", "",  new Item(10835, 1000), new Item(8851, 100), new Item(6199, 1)),
		BURN_AN_OAK_LOG(AchievementDifficulty.EASY, "Burn An Oak Log", 45008, null, "", "", "", "", "", "",  new Item(10835, 1000), new Item(8851, 100), new Item(6199, 1)),
		FISH_A_SALMON(AchievementDifficulty.EASY, "Fish A Salmon", 45009, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		COOK_A_SALMON(AchievementDifficulty.EASY, "Cook A Salmon", 45010, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		EAT_A_SALMON(AchievementDifficulty.EASY, "Eat A Salmon", 45011, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		MINE_SOME_IRON(AchievementDifficulty.EASY, "Mine Some Iron", 45012, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		SMELT_AN_IRON_BAR(AchievementDifficulty.EASY, "Smelt An Iron Bar", 45013, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		HARVEST_A_CROP(AchievementDifficulty.EASY, "Harvest A Crop", 45014, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		INFUSE_A_DREADFOWL_POUCH(AchievementDifficulty.EASY, "Infuse A Dreadfowl Pouch", 45015, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		CATCH_A_YOUNG_IMPLING(AchievementDifficulty.EASY, "Catch A Young Impling", 45016, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		CRAFT_A_PAIR_OF_LEATHER_BOOTS(AchievementDifficulty.EASY, "Craft A Pair of Leather Boots", 45017, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		CLIMB_AN_AGILITY_OBSTACLE(AchievementDifficulty.EASY, "Climb An Agility Obstacle", 45018, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(8851, 100), new Item(6199, 1)),
		FLETCH_SOME_ARROWS(AchievementDifficulty.EASY, "Fletch Some Arrows", 45019, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		STEAL_A_RING(AchievementDifficulty.EASY, "Steal A Ring", 45020, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		MIX_A_POTION(AchievementDifficulty.EASY, "Mix A Potion", 45021, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		RUNECRAFT_SOME_RUNES(AchievementDifficulty.EASY, "Runecraft Some Runes", 45022, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		BURY_A_BIG_BONE(AchievementDifficulty.EASY, "Bury A Big Bone", 45023, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		COMPLETE_A_SLAYER_TASK(AchievementDifficulty.EASY, "Complete A Slayer Task", 45024, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		SET_UP_A_CANNON(AchievementDifficulty.EASY, "Set Up A Cannon", 45025, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		KILL_A_MONSTER_USING_MELEE(AchievementDifficulty.EASY, "Kill a Monster Using Melee", 45026, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		KILL_A_MONSTER_USING_RANGED(AchievementDifficulty.EASY, "Kill a Monster Using Ranged", 45027, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		KILL_A_MONSTER_USING_MAGIC(AchievementDifficulty.EASY, "Kill a Monster Using Magic", 45028, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		DEAL_EASY_DAMAGE_USING_MELEE(AchievementDifficulty.EASY, "Deal 1000 Melee Damage", 45029, new int[]{0, 1000}, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		DEAL_EASY_DAMAGE_USING_RANGED(AchievementDifficulty.EASY, "Deal 1000 Ranged Damage", 45030, new int[]{1, 1000}, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		DEAL_EASY_DAMAGE_USING_MAGIC(AchievementDifficulty.EASY, "Deal 1000 Magic Damage", 45031, new int[]{2, 1000}, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		PERFORM_A_SPECIAL_ATTACK(AchievementDifficulty.EASY, "Perform a Special Attack", 45032, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		FIGHT_ANOTHER_PLAYER(AchievementDifficulty.EASY, "Fight Another Player", 45033, null, "", "", "", "", "", "",  new Item(10835, 10000), new Item(4691, 100), new Item(6199, 1)),
		//OPEN_UP_THE_FORUMS(Difficulty.EASY, "Set An Email Address", 45034, null, "", "", "", "", "", "", new Item(10835, 10000), new Item(10835, 10000), new Item(10835, 10000)),

		FILL_WELL_OF_GOODWILL_50M(AchievementDifficulty.MEDIUM, "Pour 50M Into The Well", 45037, new int[]{4, 1}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		CUT_100_MAGIC_LOGS(AchievementDifficulty.MEDIUM, "Cut 100 Magic Logs", 45038, new int[]{5, 100}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		BURN_100_MAGIC_LOGS(AchievementDifficulty.MEDIUM, "Burn 100 Magic Logs", 45039, new int[]{6, 100}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		FISH_25_ROCKTAILS(AchievementDifficulty.MEDIUM, "Fish 25 Rocktails", 45040, new int[]{7, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		COOK_25_ROCKTAILS(AchievementDifficulty.MEDIUM, "Cook 25 Rocktails", 45041, new int[]{8, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		MINE_25_RUNITE_ORES(AchievementDifficulty.MEDIUM, "Mine 25 Runite Ores", 45042, new int[]{9, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		SMELT_25_RUNE_BARS(AchievementDifficulty.MEDIUM, "Smelt 25 Rune Bars", 45043, new int[]{10, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		HARVEST_10_TORSTOLS(AchievementDifficulty.MEDIUM, "Harvest 10 Torstols", 45044, new int[]{11, 10}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		INFUSE_25_TITAN_POUCHES(AchievementDifficulty.MEDIUM, "Infuse 25 Steel Titans", 45045, new int[]{12, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		CATCH_5_KINGLY_IMPLINGS(AchievementDifficulty.MEDIUM, "Catch 5 Kingly Implings", 45046, new int[]{13, 5}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		COMPLETE_A_HARD_SLAYER_TASK(AchievementDifficulty.MEDIUM, "Complete A Hard Slayer Task", 45047, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		CRAFT_20_BLACK_DHIDE_BODIES(AchievementDifficulty.MEDIUM, "Craft 20 Black D'hide Bodies", 45048, new int[]{14, 20}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		FLETCH_450_RUNE_ARROWS(AchievementDifficulty.MEDIUM, "Fletch 450 Rune Arrows", 45049, new int[]{15, 450}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		STEAL_140_SCIMITARS(AchievementDifficulty.MEDIUM, "Steal 140 Scimitars", 45050, new int[]{16, 140}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		MIX_AN_OVERLOAD_POTION(AchievementDifficulty.MEDIUM, "Mix An Overload Potion", 45051, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		ASSEMBLE_A_GODSWORD(AchievementDifficulty.MEDIUM, "Assemble A Godsword", 45052, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		CLIMB_50_AGILITY_OBSTACLES(AchievementDifficulty.MEDIUM, "Climb 50 Agility Obstacles", 45053, new int[]{17, 50}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		RUNECRAFT_500_BLOOD_RUNES(AchievementDifficulty.MEDIUM, "Runecraft 500 Blood Runes", 45054, new int[]{18, 500}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		BURY_25_FROST_DRAGON_BONES(AchievementDifficulty.MEDIUM, "Bury 25 Frost Dragon Bones", 45055, new int[]{19, 25}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		FIRE_500_CANNON_BALLS(AchievementDifficulty.MEDIUM, "Fire 500 Cannon Balls", 45056, new int[]{20, 500}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEAL_MEDIUM_DAMAGE_USING_MELEE(AchievementDifficulty.MEDIUM, "Deal 100K Melee Damage", 45057, new int[]{21, 100000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEAL_MEDIUM_DAMAGE_USING_RANGED(AchievementDifficulty.MEDIUM, "Deal 100K Ranged Damage", 45058, new int[]{22, 100000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEAL_MEDIUM_DAMAGE_USING_MAGIC(AchievementDifficulty.MEDIUM, "Deal 100K Magic Damage", 45059, new int[]{23, 100000}, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_THE_KING_BLACK_DRAGON(AchievementDifficulty.MEDIUM, "Defeat The King Black Dragon", 45060, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_THE_CHAOS_ELEMENTAL(AchievementDifficulty.MEDIUM, "Defeat The Chaos Elemental", 45061, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_A_TORMENTED_DEMON(AchievementDifficulty.MEDIUM, "Defeat A Tormented Demon", 45062, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_THE_CULINAROMANCER(AchievementDifficulty.MEDIUM, "Defeat The Culinaromancer", 45063, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_NOMAD(AchievementDifficulty.MEDIUM, "Defeat Nomad", 45064, null, "", "", "", "", "", "", new Item(10835, 3), new Item(15373, 1), new Item(19864, 25)),

		FILL_WELL_OF_GOODWILL_250M(AchievementDifficulty.HARD, "Pour 250M Into The Well", 45070, new int[]{25, 5}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		CUT_5000_MAGIC_LOGS(AchievementDifficulty.HARD, "Cut 5000 Magic Logs", 45071, new int[]{26, 5000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		BURN_2500_MAGIC_LOGS(AchievementDifficulty.HARD, "Burn 2500 Magic Logs", 45072, new int[]{27, 2500}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		FISH_2000_ROCKTAILS(AchievementDifficulty.HARD, "Fish 2000 Rocktails", 45073, new int[]{28, 2000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		COOK_1000_ROCKTAILS(AchievementDifficulty.HARD, "Cook 1000 Rocktails", 45074, new int[]{29, 1000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		MINE_2000_RUNITE_ORES(AchievementDifficulty.HARD, "Mine 2000 Runite Ores", 45075, new int[]{30, 2000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		SMELT_1000_RUNE_BARS(AchievementDifficulty.HARD, "Smelt 1000 Rune Bars", 45076, new int[]{31, 1000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		HARVEST_1000_TORSTOLS(AchievementDifficulty.HARD, "Harvest 1000 Torstols", 45077, new int[]{32, 1000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		INFUSE_500_STEEL_TITAN_POUCHES(AchievementDifficulty.HARD, "Infuse 500 Steel Titans", 45078, new int[]{33, 500}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		CRAFT_1000_DIAMOND_GEMS(AchievementDifficulty.HARD, "Craft 1000 Diamond Gems", 45079, new int[]{34, 1000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		CATCH_100_KINGLY_IMPLINGS(AchievementDifficulty.HARD, "Catch 100 Kingly Imps", 45080, new int[]{35, 100}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		FLETCH_5000_RUNE_ARROWS(AchievementDifficulty.HARD, "Fletch 5000 Rune Arrows", 45081, new int[]{36, 5000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		STEAL_5000_SCIMITARS(AchievementDifficulty.HARD, "Steal 5000 Scimitars", 45082, new int[]{45, 5000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		RUNECRAFT_8000_BLOOD_RUNES(AchievementDifficulty.HARD, "Runecraft 8000 Blood Runes", 45083, new int[]{38, 8000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		BURY_500_FROST_DRAGON_BONES(AchievementDifficulty.HARD, "Bury 500 Frost Dragon Bones", 45084, new int[]{39, 500}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		FIRE_5000_CANNON_BALLS(AchievementDifficulty.HARD, "Fire 5000 Cannon Balls", 45085, new int[]{40, 5000}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		MIX_100_OVERLOAD_POTIONS(AchievementDifficulty.HARD, "Mix 100 Overload Potions", 45086, new int[]{41, 100}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		COMPLETE_AN_ELITE_SLAYER_TASK(AchievementDifficulty.HARD, "Complete An Elite Slayer Task", 45087, null, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 25)),
		ASSEMBLE_5_GODSWORDS(AchievementDifficulty.HARD, "Assemble 5 Godswords", 45088, new int[]{42, 5}, "", "", "", "", "", "", new Item(10835, 5), new Item(15373, 2), new Item(19864, 250)),
		DEAL_HARD_DAMAGE_USING_MELEE(AchievementDifficulty.HARD, "Deal 5M Melee Damage", 45089, new int[]{43, 5000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(19864, 25)),
		DEAL_HARD_DAMAGE_USING_RANGED(AchievementDifficulty.HARD, "Deal 5M Ranged Damage", 45090, new int[]{44, 5000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(19864, 25)),
		DEAL_HARD_DAMAGE_USING_MAGIC(AchievementDifficulty.HARD, "Deal 5M Magic Damage", 45091, new int[]{45, 5000000}, "", "", "", "", "", "", new Item(10835, 25), new Item(15373, 1), new Item(19864, 25)),
		DEFEAT_JAD(AchievementDifficulty.HARD, "Defeat Jad", 45092, null, "", "", "", "", "", "", new Item(10835, 50), new Item(6199, 1), new Item(19864, 25)),
		DEFEAT_BANDOS_AVATAR(AchievementDifficulty.HARD, "Defeat Bandos Avatar", 45093, null, "", "", "", "", "", "", new Item(10835, 5), new Item(6199, 2), new Item(19864, 25)),
		DEFEAT_GENERAL_GRAARDOR(AchievementDifficulty.HARD, "Defeat General Graardor", 45094, null, "", "", "", "", "", "", new Item(10835, 5), new Item(6199, 2), new Item(19864, 25)),
		DEFEAT_KREE_ARRA(AchievementDifficulty.HARD, "Defeat Kree'Arra", 45095, null, "", "", "", "", "", "", new Item(10835, 5), new Item(6199, 2), new Item(19864, 25)),
		DEFEAT_COMMANDER_ZILYANA(AchievementDifficulty.HARD, "Defeat Commander Zilyana", 45096, null, "", "", "", "", "", "", new Item(10835, 5), new Item(6199, 2), new Item(19864, 25)),
		DEFEAT_KRIL_TSUTSAROTH(AchievementDifficulty.HARD, "Defeat K'ril Tsutsaroth", 45097, null, "", "", "", "", "", "", new Item(10835, 5), new Item(6199, 2), new Item(19864, 25)),
		DEFEAT_THE_CORPOREAL_BEAST(AchievementDifficulty.HARD, "Defeat The Corporeal Beast", 45098, null, "", "", "", "", "", "", new Item(10835, 50), new Item(6199, 1), new Item(19864, 25)),
		DEFEAT_NEX(AchievementDifficulty.HARD, "Defeat Nex", 45099, null, "", "", "", "", "", "", new Item(10835, 5000), new Item(12846, 2), new Item(19864, 100)),

		COMPLETE_ALL_HARD_TASKS(AchievementDifficulty.ELITE, "Complete All Hard Tasks", 45104, new int[]{47, 32}, "", "", "", "", "", "", new Item(10835, 2000), new Item(15373, 5), new Item(12845, 2)),
		CUT_AN_ONYX_STONE(AchievementDifficulty.ELITE, "Cut An Onyx Stone", 45105, null, "", "", "", "", "", "", new Item(10835, 2), new Item(6199, 1), new Item(8851, 15)),
		REACH_MAX_EXP_IN_A_SKILL(AchievementDifficulty.ELITE, "Reach Max Exp In A Skill", 45106, null, "", "", "", "", "", "", new Item(10835, 25), new Item(6199, 2), new Item(15373, 1)),
		DEFEAT_10000_MONSTERS(AchievementDifficulty.ELITE, "Defeat 10,000 Monsters", 45108, new int[]{49, 10000}, "", "", "", "", "", "", new Item(10835, 5000), new Item(15373, 4), new Item(12847, 2)),
		DEFEAT_500_BOSSES(AchievementDifficulty.ELITE, "Defeat 500 Boss Monsters", 45109, new int[]{50, 500}, "", "", "", "", "", "", new Item(10835, 1000), new Item(6199, 5), new Item(19864, 25)),
		;
		
		AchievementData(AchievementDifficulty achievementDifficulty, String interfaceLine, int interfaceFrame, int[] progressData,
						String desc1, String desc2, String desc3, String desc4, String desc5, String desc6, Item Rewards,
						Item Rewards1, Item Rewards2) {
			this.achievementDifficulty = achievementDifficulty;
			this.interfaceLine = interfaceLine;
			this.interfaceFrame = interfaceFrame;
			this.progressData = progressData;
			this.desc1 = desc1;
			this.desc2 = desc2;
			this.desc3 = desc3;
			this.desc4 = desc4;
			this.desc5 = desc5;
			this.desc6 = desc6;
			this.Rewards = Rewards;
			this.Rewards1 = Rewards1;
			this.Rewards2 = Rewards2;
		}

		private AchievementDifficulty achievementDifficulty;
		private String interfaceLine;
		private int interfaceFrame;
		private int[] progressData;
		private String desc1;
		private String desc2;
		private String desc3;
		private String desc4;
		private String desc5;
		private String desc6;
		private Item Rewards;
		private Item Rewards1;
		private Item Rewards2;

		public AchievementDifficulty getDifficulty() {
			return achievementDifficulty;
		}
	}

	public enum AchievementDifficulty {
		BEGINNER, EASY, MEDIUM, HARD, ELITE;
	}

	public static boolean handleButton(Player player, int button) {
		if (!(button >= -23731 && button <= -20416)) {
			return false;
		}
		int index = -1;
		if (button >= -20530 && button <= -20503) {
			index = 20530 + button;
		} else if (button >= -20499 && button <= -20469) {
			index = 28 + 20499 + button;
		} else if (button >= -20466 && button <= -20437) {
			index = 56 + 20466 + button;
		} else if (button >= -20432 && button <= -20430) {
			index = 86 + 20432 + button;
		}  else if (button >= -20428 && button <= -20427) {
			index = 85 + 20432 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 87 + 20430 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 88 + 20428 + button;
		}  else if (button >= -20424 && button <= -20423) {
			index = 89 + 20426 + button;
		}  else if (button >= -20426 && button <= -20425) {
			index = 90 + 20424 + button;
		} else if (button >= -23731 && button <= -23672) {
			if (player.achievementDifficulty == AchievementDifficulty.EASY) {
				index = 23731 + button;
			} else if (player.achievementDifficulty == AchievementDifficulty.MEDIUM) {
				index = 28 + 23731 + button;
			} else if (player.achievementDifficulty == AchievementDifficulty.HARD) {
				index = 56 + 23731 + button;
			} else if (player.achievementDifficulty == AchievementDifficulty.ELITE) {
				index = 86 + 23731 + button;
			}
		}

		if (index >= 0 && index < AchievementData.values().length) {
			AchievementData achievement = AchievementData.values()[index];
			openInterface(player, achievement);
		}
		return true;
	}

	public static void updateInterface(Player player) {
		for (AchievementData achievement : AchievementData.values()) {
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			player.getPacketSender().sendString(achievement.interfaceFrame,
					(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
		}
		player.getPacketSender().sendString(45001, "Achievements: " + player.getPointsHandler().getAchievementPoints()
				+ "/" + AchievementData.values().length);
	}

	public static void openInterface(Player player, AchievementData task) {
		int id = 41805;
		int id1 = 41805;
		if (task.achievementDifficulty == AchievementDifficulty.MEDIUM) {
			id -= 28;
		}
		if (task.achievementDifficulty == AchievementDifficulty.HARD) {
			id -= 56;
		}
		if (task.achievementDifficulty == AchievementDifficulty.ELITE) {
			id -= 86;
		}

		player.achievementDifficulty = task.achievementDifficulty;
		for (AchievementData achievement : AchievementData.values()) {
			player.getPacketSender().sendString(id1++, "");
			boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
			boolean progress = achievement.progressData != null
					&& player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
			if (achievement.achievementDifficulty == task.achievementDifficulty) {
				player.getPacketSender().sendString(id,
						(completed ? "@gre@" : progress ? "@yel@" : "@red@") + achievement.interfaceLine);
			}
			id++;
		}
		player.getPacketSender().sendString(41764, "Achievements");

		player.getPacketSender().sendString(41769, "Easy");
		player.getPacketSender().sendString(41770, "Medium");
		player.getPacketSender().sendString(41771, "Hard");
		player.getPacketSender().sendString(41772, "Elite");

		player.getPacketSender().sendString(41773, "" + task.achievementDifficulty);

		player.getPacketSender().sendString(41774, "" + task.interfaceLine);

		int points = 0;
		if (task.achievementDifficulty == AchievementDifficulty.EASY) {
			points = 3;
		}else if (task.achievementDifficulty == AchievementDifficulty.MEDIUM) {
			points = 10;
		}else if (task.achievementDifficulty == AchievementDifficulty.HARD) {
			points = 15;
		}else if (task.achievementDifficulty == AchievementDifficulty.ELITE) {
			points = 150;
		}
		player.getPacketSender().sendString(41881, points + " Zamron points");

		player.getPacketSender().sendString(41776, "" + task.desc1);
		player.getPacketSender().sendString(41777, "" + task.desc2);
		player.getPacketSender().sendString(41778, "" + task.desc3);
		player.getPacketSender().sendString(41779, "" + task.desc4);
		player.getPacketSender().sendString(41780, "" + task.desc5);
		player.getPacketSender().sendString(41781, "" + task.desc6);

		player.getPA().sendItemOnInterface(41901, task.Rewards.getId(), task.Rewards.getAmount());
		player.getPA().sendItemOnInterface(41902, task.Rewards1.getId(), task.Rewards1.getAmount());
		player.getPA().sendItemOnInterface(41903, task.Rewards2.getId(), task.Rewards2.getAmount());

		if (player.getAchievementAttributes().getCompletion()[task.ordinal()]) {
			player.getPacketSender().sendString(41775, "Progress: @gre@100% (1/1)");
		} else if (task.progressData == null) {
			player.getPacketSender().sendString(41775, "Progress: @gre@0% (0/0)");

		} else {
			int progressTask = player.getAchievementAttributes().getProgress()[task.progressData[0]];
			int progressTotal = task.progressData[1];
			long percent = (progressTask / progressTotal) * 100;
			if (progressTask == 0) {
				player.getPacketSender().sendString(41775,
						"Progress: @gre@0 (" + Misc.insertCommasToNumber("" + progressTask) + "/"
								+ Misc.insertCommasToNumber("" + progressTotal) + ")");
			} else if (progressTask != progressTotal) {
				player.getPacketSender().sendString(41775,
						"Progress: @gre@" + percent + " (" + Misc.insertCommasToNumber("" + progressTask) + "/"
								+ Misc.insertCommasToNumber("" + progressTotal) + ")");
			}
		}

		player.getPA().sendInterface(41750);
	}

	public static void setPoints(Player player) {
		int points = 0;
		for (AchievementData achievement : AchievementData.values()) {
			if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
				points++;
			}
		}
		player.getPointsHandler().setAchievementPoints(points, false);
	}

	public static void doProgress(Player player, AchievementData achievement) {
		doProgress(player, achievement, 1);
	}

	public static void doProgress(Player player, AchievementData achievement, int amt) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		if (achievement.progressData != null) {
			int progressIndex = achievement.progressData[0];
			int amountNeeded = achievement.progressData[1];
			int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
			if ((previousDone + amt) < amountNeeded) {
				player.getAchievementAttributes().getProgress()[progressIndex] = previousDone + amt;
				if (previousDone == 0)
					player.getPacketSender().sendString(achievement.interfaceFrame,
							"@yel@" + achievement.interfaceLine);
			} else {
				finishAchievement(player, achievement);
			}
		}
	}

	public static void finishAchievement(Player player, AchievementData achievement) {
		if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
			return;
		player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
		player.getPacketSender().sendString(achievement.interfaceFrame, ("@gre@") + achievement.interfaceLine)
				.sendMessage("<img=12> <col=339900>You have completed the achievement "
						+ Misc.formatText(achievement.toString().toLowerCase() + "."))
				.sendString(45001, "Achievements: " + player.getPointsHandler().getAchievementPoints() + "/"
						+ AchievementData.values().length);
		player.getInventory().add(achievement.Rewards);
		player.getInventory().add(achievement.Rewards1);
		player.getInventory().add(achievement.Rewards2);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards1);
		//player.getBank(player.getCurrentBankTab()).add(achievement.Rewards2);
		player.getPointsHandler().setAchievementPoints(1, true);
		
		int points = 0;
		if (achievement.achievementDifficulty == AchievementDifficulty.EASY) {
			points = 3;
		}else if (achievement.achievementDifficulty == AchievementDifficulty.MEDIUM) {
			points = 10;
		}else if (achievement.achievementDifficulty == AchievementDifficulty.HARD) {
			points = 15;
		}else if (achievement.achievementDifficulty == AchievementDifficulty.ELITE) {
			points = 150;
		}
		player.setRuneUnityPoints(player.getRuneUnityPoints() + points);
	}

	public static class AchievementAttributes {

		public AchievementAttributes() {
			
		}

		/** ACHIEVEMENTS **/
		private boolean[] completed = new boolean[AchievementData.values().length];
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

		/** MISC **/
		private int coinsGambled;
		private double totalLoyaltyPointsEarned;
		private boolean[] godsKilled = new boolean[5];

		public int getCoinsGambled() {
			return coinsGambled;
		}

		public void setCoinsGambled(int coinsGambled) {
			this.coinsGambled = coinsGambled;
		}

		public double getTotalLoyaltyPointsEarned() {
			return totalLoyaltyPointsEarned;
		}

		public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
			this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
		}

		public boolean[] getGodsKilled() {
			return godsKilled;
		}

		public void setGodKilled(int index, boolean godKilled) {
			this.godsKilled[index] = godKilled;
		}

		public void setGodsKilled(boolean[] b) {
			this.godsKilled = b;
		}
	}
}
