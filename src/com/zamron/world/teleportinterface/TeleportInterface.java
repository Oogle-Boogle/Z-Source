package com.zamron.world.teleportinterface;

import com.zamron.model.Position;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.world.content.combat.bossminigame.BossMinigameFunctions;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Emerald
 * @since 8th July 2019
 */

public class TeleportInterface {

	private final static int CATEGORY_NAME_ID = 50508;

	public enum Slayers {
		// this is 1st field etc,

		BOWSER(50601, "Bowser", "", "Drops Colorful Tokens", "", "SLAYER NPC", "@red@HP:@gre@ 100k", 728,
				new int[] { 2528, 2850, 0 }, 3000),

		LUIGI(50602, "Luigi", "", "Drops Colorful Tokens", "", "SLAYER NPC", "@red@HP:@gre@ 100k", 727,
				new int[] { 2528, 2850, 0 }, 3000),

		BULBASAUR(50603, "Bulbasaur", "", "", "", "SLAYER NPC", "@red@HP:@gre@ 100k", 842,
				new int[] { 2254, 3356, 0 }, 3000),

		CHARMANDER(50604, "Charmander", "", "Drops Frankenstein Tokens", "", "SLAYER NPC", "@red@HP:@gre@ 100k", 174,
				new int[] { 2338, 4059, 0 }, 3000),

		CHARIZARD(50605, "Charizard", "", "Drops Frankenstein Tokens", "Scratch cards bonus", "SLAYER NPC", "@red@HP:@gre@ 500k", 2843,
				new int[] { 2207, 3302, 0 }, 3000),

		MAGICSPIDER(50606, "Magic Spider", "", "A magical poisonous spider", "", "SLAYER NPC", "@gre@HP:@red@ 650k", 6309,
				new int[] { 2917, 3625, 0 }, 3000),

		PUMPKIN(50607, "Killer Pumpkin", "Underestimate the pumpkin?", "Drops H'Ween cosmetics", "Is that a collector aura?", "SLAYER NPC", "@gre@HP:@red@ 1M", 8548,
				new int[] { 2960, 9779, 0 }, 3000),

		JOKER(50608, "Joker", "", "", "", "SLAYER NPC", "@gre@HP:@red@ 1M", 420,
				new int[] { 3310, 2788, 0 }, 3000),

		TOAD(50609, "Stoned Toad", "", "Toadly stoned", "", "SLAYER NPC", "@gre@HP:@red@ 1.6M", 812,
				new int[] { 2197, 4945, 0 }, 3000),

		ZORBAK(50610, "Zorbak", "", "", "", "SLAYER NPC", "@gre@HP:@red@ 1.8M", 5922,
				new int[] { 2214, 4944, 0 }, 3000),

		MEWTWO(50611, "Mewtwo", "Magical Mewtwo", "", "", "SLAYER NPC", "@gre@HP:@red@ 2.5M", 6357,
				new int[] { 2206, 4957, 0 }, 3000),

		TOKASH(50612, "To'Kash", "", "", "", "SLAYER NPC", "@gre@HP:@red@ 2.5M", 10038,
				new int[] { 3354, 9402, 0 }, 3000),

		BALLAK(50613, "Bal'lak", "", "", "", "SLAYER NPC", "@gre@HP:@red@ 2.5M", 10140,
				new int[] { 3354, 9402, 0 }, 3000),

		OOGLE(50614, "Baby Oogle", "", "", "", "SLAYER NPC", "@gre@HP:@red@ 10M", 4541,
				new int[] { 2605, 3093, 0 }, 3000),

		AMONGUS(50615, "Among Us", "Slayer NPC's", "Juicy...", "", "Whos the TRAITOR!?", "", 610, new int[] {2914, 4064, 0});



		Slayers(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Slayers(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Monsters {
		STARTER(50601, "Starter Zone", "Bugs Bunny@gre@(T1)", "Starter Tasks", "@red@HP:@gre@ 2.5k", "", "", 4455,
				new int[] { 3795, 3543, 0 }),

		HERCULES(50602, "Hercules", "Hercules@gre@(T2)", "Drops Hercules set ", "@red@HP:@gre@ 85k", "", "", 17,
				new int[] { 2783, 4636, 0 }),

		LUCARIO(50603, "Lucario", "Lucario@yel@(T3)", "Drops the blessed set", "@red@KC REQ: 50 Hercules", "@red@HP:@gre@ 185k", "", 3263,
				new int[] { 2913, 4759, 0 },2000),

		HADES(50604, "Hades", "Hades@yel@(T3)", "Drops Misc items", "@red@KC REQ: 50 Lucario", "@red@HP:@gre@ 300k", "", 15,
				new int[] { 2095, 3677, 0 }, 3000),

		DEFENDERS(50605, "Defenders", "Defenders@yel@(T4)", "Protects Itself", "from Range!", "@red@KC REQ: 100 Hades", "@red@HP:@gre@ 400k", 9994,
				new int[] { 2724, 9821, 0 }, 2000),


		GODZILLA(50606, "Godzilla", "Godzilla@yel@(T4)", "This NPC drops", "the Rex set", "@red@KC REQ: 150 Defenders", "@red@HP:@gre@ 425k", 9932,
				new int[] { 3374, 9807, 0 }),

		DEMONOLM(50607, "Demonic Olm", "Demonic Olm@yel@(T4)", "This NPC drops", "Misc Items", "@red@KC REQ: 175 Godzilla", "@red@HP:@gre@ 450k", 224,
				new int[] { 2399, 3548, 0 }),

		CERBERUS(50608, "Cerberus", "Cerberus@yel@(T4)", "Drops Misc Gear", "@red@KC REQ: 200 Demonic Olm", "@red@HP:@gre@ 500k", "", 1999,
				new int[] { 1240, 1247, 0 }, 3000),

		ZEUS(50609, "Zeus", "Zeus@yel@(T5)", "Drops Zeus set", "@red@KC REQ: 225 Cerberus", "@red@HP:@gre@ 500k", "", 16,
				new int[] { 2065, 3663, 0 }, 3000),

		INFERNAL_BEAST(50610, "Infartico", "Infartico@red@(T5)", "@red@KC REQ: 260 Zeus", "@red@HP:@gre@ 525k", "", "", 9993,
				new int[] { 3479, 3087, 0 }, 3000),

		VALOR(50611, "Lord Valor", "Lord Valor@red@(T5)", "Hybrid NPC", "@red@KC REQ: 350 Infartico", "@red@HP:@gre@ 550k", "", 9277,
				new int[] { 2780, 10000, 0 }),

		STORMBREAKER(50612, "Storm Breaker", "Storm Breaker@bla@(T5)", "This drops Stormbreaker", "@red@KC REQ: 450 Lord Valor", "@red@HP:@gre@ 600k", "", 33,
				new int[] { 3226, 2844, 0 },2000),

		TRIDENT(50613, "Dzanth", "Dzanth@red@(T6)", "@red@KC REQ: 550 Storm Breakers", "@red@HP:@gre@ 625k", "", "", 9273,
				new int[] { 2369, 4944, 0 }),

		HARAMBE(50614, "King Kong", "King Kong@red@(T6)", "Multi MASS BOSS", "@red@KC REQ: 750 Dzanth", "@red@HP:@gre@ 650k)", "", 9903,
				new int[] { 2720, 9880, 0 });

		Monsters(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Monsters(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Hardened {




		CORPOREAL_BEAST(50601, "Corp Beast", "Corp Beast@red@(T6)", "@red@KC REQ: 750 Kong", "@red@HP:@gre@ 650k", "", "", 8133,
				new int[] { 2886, 4376, 0 }, 4000),

		LUCID(50602, "Lucid Warriors", "Lucid Warriors@red@(T7)", "@red@KC REQ: 750 Corp Beasts", "@red@HP:@gre@ 650k", "", "", 9247,
				new int[] { 2557, 4953, 0 }),

		HULK(50603, "Hulk", "Hulk@red@(T7)", "@red@KC REQ: 750 Lucid Warriors", "@red@HP:@gre@ 675k", "", "", 8493,
				new int[] { 3852, 5846, 0 }),
		WIZARDS(50604, "Darkblue Wizards", "Darkblue Wizards@bla@(T7)", "@red@KC REQ: 750 Hulk", "@red@HP:@gre@ 700k", "", "", 9203,
				new int[] { 2920, 9687, 0 }),

		PYROS(50605, "Heated Pyros", "Heated Pyros@bla@(T8)", "@red@KC REQ: 750 Darkblue wizards", "@red@HP:@gre@ 700k", "", "", 172,
				new int[] { 3040, 4838, 0 }),

		PURPLEFIRE_WYRM(50606, "Dark Purple Wyrm", "Dark Purple Wyrm@bla@(T8)", "Its a massboss", "@red@KC REQ: 1000 Pyros", "@red@HP:@gre@ 700k", "", 9935,
				new int[] { 2325, 4586, 0 }, 2000),

		TRINITY(50607, "Trinity", "Trinity@bla@(T8)", "ITS a Massboss", "@red@KC REQ: 1000 Purple Wyrms", "@red@HP:@gre@ 725k", "", 170,
				new int[] { 2517, 4645, 0 }, 3000),

		CLOUD(50608, "Cloud", "Cloud@bla@(T9)", "@red@KC REQ: 1000 Trinity", "@red@HP:@gre@ 750k", "", "", 169,
				new int[] { 2539, 5774, 0 }),

		HERBAL(50609, "Herbal Rogue", "Herbal Rogue@bla@(T9)", "@red@KC REQ: 1000 Cloud", "@red@HP:@gre@ 750k", "", "", 219, new int[] { 2737, 5087, 0 },
				4000),

		EXODEN(50610, "Exoden", "Exoden@bla@(T9)", "@red@KC REQ: 1000 Herbal Rogue", "@red@HP:@gre@ 800k", "", "", 12239, new int[] { 2540, 10162, 0 },
				4000);

		Hardened(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Hardened(int textId, String name, String description1, String description2, String description3,
				 String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;

	}

	public enum Expert {




		NEX(50601, "Supreme nex", "Supreme nex@bla@(T9)", "This npc drops Supreme set", "@red@KC REQ: 1000 Exoden", "@red@HP:@gre@ 825k", "", 3154,
				new int[] { 2599, 4699, 0 },2000),
		APOLLO(50602, "Apollo Ranger", "Apollo Ranger@bla@(T10)", "This drops Apollo Set", "@red@KC REQ: 550 Supreme Nex'", "@red@HP:@gre@ 3m", "", 1684,
				new int[] { 3178, 4237, 2 },2000),
		TROLL(50603, "Noxious Troll", "Noxious Troll@bla@(T10)", "This drops Noxious Set", "@red@KC REQ: 550 Apollo Rangers", "@red@HP:@gre@ 3m", "", 5957,
				new int[] { 3232, 3043, 0 },3000),
		AZAZEL(50604, "Azazel Beast", "Azazel Beast@bla@(T10)", "This drops Azazel Set", "@red@KC REQ: 550 Noxious Trolls", "@red@HP:@gre@ 3m", "", 5958,
				new int[] { 2468, 3372, 0 },3000),
		RAVANA(50605, "Ravana", "Ravana@bla@(T10)", "This drops Detrimental Set", "@red@KC REQ: 550 Azazel Beasts", "@red@HP:@gre@ 3m", "", 5959,
				new int[] { 3595, 3492, 0 },3000),
		LUMINITIOS(50606, "Luminitous Warriors", "Warriors@bla@(T10)", "This drops Luminitous Set", "@red@KC REQ: 550 Ravanas", "@red@HP:@gre@ 3m", "", 185,
				new int[] { 2525, 4776, 0 },3000),
		HELLHOUND(50607, "Custom Hellhounds", "Hell Hounds@bla@(T10)", "This drops BFG set", "@red@KC REQ: 550 Luminitous warriors", "@red@HP:@gre@ 3m", "", 6311,
				new int[] { 3176, 3029, 0 },3000);

		Expert(int textId, String name, String description1, String description2, String description3,
			   String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Expert(int textId, String name, String description1, String description2, String description3,
			   String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Zones {

		BOX_ZONE(50601, "Box Zone", "Box Zone", "Box Zone For Starters!", "", "", "", 197,
				new int[] { 3379, 3424, 0 }),

		TOKEN_ZONE(50602, "Token Zone", "Earn Tokens", "to be used at", "the Token shop","", "", 729,
				new int[] { 2526, 2851, 0 }),

		STARWARS(50603, "StarWars Zone", "", "", " ", "", "", 11,
				new int[] { 2575, 3104, 0 }),

		DBZ(50604, "DBZ", "Great for starter", "items...", "Collect the", "tokens!", "", 100,
				new int[] { 2575, 3104, 0 }),

		EVENTBOSS(50605, "World boss Event", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 2745,
				new int[] { 2409, 4679, 0 }),

		RICK(50606, "Rick", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 421,
				new int[] { 3043, 3409, 0 }),

		SEPH(50607, "Seph (HARD)", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 25,
				new int[] { 2590, 5727, 0 }),

		ASSASSIN(50608, "Assassin (HARD)", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 9944,
				new int[] { 3111, 5544, 0 }),

		DARKRANGER(50609, "Dark Ranger (HARD)", "THIS NPC Spawns", "Every Hour", "Multiple People ", "May be Required!", "", 299,
				new int[] { 2222, 5099, 0 }),

		SKILLINGBOSS(50610, "Skilling Boss", "This NPC gives", "EXP in the", "selected skill!", "KILL ME!", "", 6306,
				new int[] { 2604, 3123, 0 });



		Zones(int textId, String name, String description1, String description2, String description3,
			  String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Zones(int textId, String name, String description1, String description2, String description3,
			  String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public enum Minigames {
		FRANKENSTIEN(50601, "Frankenstiens Castle", "Frankenstiens Castle", "This Minigame drops", "Tier 1-7 Defenders", "", "", 4291, new int[] { 2845, 3540, 2 }),
		PESTCONTROL(50602, "Pest control", "Pest Control", "Earn points to use", "at the pest control shop", "", "", 3789, new int[] { 2657, 2648, 0 }),
		BARROWSMINIGAME(50603, "Barrows", "Barrows", "Dig your way for some", " fancy diamonds!", "", "", 2025, new int[] { 3564, 3289, 0 }),
		DUNGEON(50604, "Dungeons Minigame", "Dungeons Minigame", "Do you have what it", " takes to survive?", "", "", 499, new int[] { 2606, 3103, 0 }),
		CHALLENGER(50605, "Challenging Minigame", "Challenging Minigame", "only for the toughest", "are you tough?", "", "", 50, new int[] {BossMinigameFunctions.DOOR_X, BossMinigameFunctions.DOOR_Y, 0});
		//HALLOWEEN(50602, "Trios Minigame", "Earn all 3 Keys", "to be used at", "the Trio Chest!","", "", 75, 
		//	new int[] { 3107, 3427, 0 }, 0);
		Minigames(int textId, String name, String description1, String description2, String description3,
				  String description4, String description5, int npcId, int[] teleportCords) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;

		}

		Minigames(int textId, String name, String description1, String description2, String description3,
				  String description4, String description5, int npcId, int[] teleportCords, int adjustedZoom) {
			this.textId = textId;
			this.name = name;
			this.description1 = description1;
			this.description2 = description2;
			this.description3 = description3;
			this.description4 = description4;
			this.description5 = description5;
			this.npcId = npcId;
			this.teleportCords = teleportCords;
			this.adjustedZoom = adjustedZoom;

		}

		private int textId;
		private String name;
		private String description1, description2, description3, description4, description5;
		private int npcId;
		private int[] teleportCords;
		private int adjustedZoom;
	}

	public static void resetOldData() {
		currentTab = 0;
		currentClickIndex = 0;
	}

	public static void handleTeleports(Player player) {
		switch (currentTab) {
			case 0:
				Slayers bossData = Slayers.values()[currentClickIndex];
				handleBossTeleport(player, bossData);
				break;
			case 1:
				Monsters monsterData = Monsters.values()[currentClickIndex];
				handleMonsterTeleport(player, monsterData);
				break;
			case 2:
				Hardened wildyData = Hardened.values()[currentClickIndex];
				handleWildyTeleport(player, wildyData);
				break;
			case 3:
				Expert expertData = Expert.values()[currentClickIndex];
				handleZonesTeleport(player, expertData);
				break;
			case 4:
				Zones minigameData = Zones.values()[currentClickIndex];
				handleMinigameTeleport(player, minigameData);
				break;
			case 5:
				Minigames cityData = Minigames.values()[currentClickIndex];
				handleCityTeleport(player, cityData);
				break;
		}
	}

	public static void handleBossTeleport(Player player, Slayers bossData) {

		TeleportHandler.teleportPlayer(player,
				new Position(bossData.teleportCords[0], bossData.teleportCords[1], bossData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleMonsterTeleport(Player player, Monsters monsterData) {

		TeleportHandler.teleportPlayer(player,
				new Position(monsterData.teleportCords[0], monsterData.teleportCords[1], monsterData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleWildyTeleport(Player player, Hardened wildyData) {

		TeleportHandler.teleportPlayer(player,
				new Position(wildyData.teleportCords[0], wildyData.teleportCords[1], wildyData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleZonesTeleport(Player player, Expert skillData) {

		TeleportHandler.teleportPlayer(player,
				new Position(skillData.teleportCords[0], skillData.teleportCords[1], skillData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	public static void handleMinigameTeleport(Player player, Zones minigameData) {

		TeleportHandler.teleportPlayer(player, new Position(minigameData.teleportCords[0],
				minigameData.teleportCords[1], minigameData.teleportCords[2]), player.getSpellbook().getTeleportType());
	}

	public static void handleCityTeleport(Player player, Minigames cityData) {

		TeleportHandler.teleportPlayer(player,
				new Position(cityData.teleportCords[0], cityData.teleportCords[1], cityData.teleportCords[2]),
				player.getSpellbook().getTeleportType());
	}

	private static void clearData(Player player) {
		for (int i = 50601; i < 50700; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

	private static int currentTab = 0; // 0 = Boss, 1 = Monsters, 2 = Wildy, 3 = Zones, 4 = Minigame, 5 = Cities

	public static boolean handleButton(Player player, int buttonID) {
		if (!(buttonID >= -14935 && buttonID <= -14836)) {
			return false;
		}
		int index = -1;

		if (buttonID >= -14935) {
			index = 14935 + buttonID;
		}
		if (currentTab == 0) {
			if (index >= 0 && index < Slayers.values().length) {
				//System.out.println("Handled boss data [As index was 0]");
				Slayers bossData = Slayers.values()[index];
				currentClickIndex = index;
				sendSlayerData(player, bossData);
				sendDrops(player, bossData.npcId);
			}
		}
		if (currentTab == 1) {
			if (index >= 0 && index < Monsters.values().length) {
				//System.out.println("Handled monster data [As index was 1]");
				Monsters monsterData = Monsters.values()[index];
				currentClickIndex = index;
				sendMonsterData(player, monsterData);
				sendDrops(player, monsterData.npcId);
			}
		}
		if (currentTab == 2) {
			if (index >= 0 && index < Hardened.values().length) {
				//System.out.println("Handled monster data [As index was 1]");
				Hardened wildyData = Hardened.values()[index];
				currentClickIndex = index;
				sendWildyData(player, wildyData);
				sendDrops(player, wildyData.npcId);
			}
		}
		if (currentTab == 3) {
			if (index >= 0 && index < Expert.values().length) {
				//System.out.println("Handled monster data [As index was 1]");
				Expert expertData = Expert.values()[index];
				currentClickIndex = index;
				sendZonesData(player, expertData);
				sendDrops(player, expertData.npcId);
			}
		}
		if (currentTab == 4) {
			if (index >= 0 && index < Zones.values().length) {
				//System.out.println("Handled monster data [As index was 1]");
				Zones zonesData = Zones.values()[index];
				currentClickIndex = index;
				sendMinigameData(player, zonesData);
				sendDrops(player, zonesData.npcId);
			}
		}
		if (currentTab == 5) {
			if (index >= 0 && index < Minigames.values().length) {
				//System.out.println("Handled monster data [As index was 1]");
				Minigames cityData = Minigames.values()[index];
				currentClickIndex = index;
				sendCityData(player, cityData);
				sendDrops(player, cityData.npcId);
			}
		}
		return true;

	}

	public static int currentClickIndex = 0;

	public static void sendSlayerData(Player player, Slayers data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendMonsterData(Player player, Monsters data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendWildyData(Player player, Hardened data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendZonesData(Player player, Expert data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendMinigameData(Player player, Zones data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendCityData(Player player, Minigames data) {
		player.getPacketSender().sendString(51200, data.description1);
		player.getPacketSender().sendString(51201, data.description2);
		player.getPacketSender().sendString(51202, data.description3);
		player.getPacketSender().sendString(51203, data.description4);
		player.getPacketSender().sendString(51204, data.description5);
		player.getPacketSender().sendNpcOnInterface(50514, data.npcId, data.adjustedZoom != 0 ? data.adjustedZoom : 0);
	}

	public static void sendBossTab(Player player) {
		player.getPacketSender().sendInterface(50500);
		currentTab = 0;
		clearData(player);
		resetOldData();
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Slayer NPC's");
		for (Slayers data : Slayers.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendMonsterTab(Player player) {
		currentTab = 1;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Beginner NPC's");
		for (Monsters data : Monsters.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendHardenedTab(Player player) {
		currentTab = 2;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Medium NPC's");
		for (Hardened data : Hardened.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendExpertTab(Player player) {
		currentTab = 3;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Hard NPC's");
		for (Expert data : Expert.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendZonesTab(Player player) {
		currentTab = 4;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Zones & WBosses");
		for (Zones data : Zones.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendMinigameTab(Player player) {
		currentTab = 5;
		clearData(player);
		player.getPacketSender().sendString(CATEGORY_NAME_ID, "Minigame tele");
		for (Minigames data : Minigames.values()) {
			player.getPacketSender().sendString(data.textId, data.name);
		}
		return;
	}

	public static void sendDrops(Player player, int npcId) {
		player.getPacketSender().resetItemsOnInterface(51251, 100);
		try {
			NPCDrops drops = NPCDrops.forId(npcId);
			if(drops == null) {
				//System.out.println("Was null");
				return;
			}
			for (int i = 0; i < drops.getDropList().length; i++) {
				player.getPacketSender().sendItemOnInterface(51251, drops.getDropList()[i].getId(), i,
						drops.getDropList()[i].getItem().getAmount());
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}