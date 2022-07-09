package com.zamron.world.content.skill.impl.summoning;

import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class BossPets {

	public enum BossPet {
		
		PET_CHAOS_ELEMENTAL(3200, 3033, 11995),
		PET_KING_BLACK_DRAGON(50, 3030, 11996),
		PET_GENERAL_GRAARDOR(6260, 3031, 11997),
		PET_TZTOK_JAD(2745, 3032, 11978),
		PET_CORPOREAL_BEAST(8133, 3034, 12001),
		PET_KREE_ARRA(6222, 3035, 12002),
		PET_KRIL_TSUTSAROTH(6203, 3036, 12003),
		PET_COMMANDER_ZILYANA(6247, 3037, 12004),
		PET_Suic_ZILYANA(6247, 3089, 12325),
		PET_DAGANNOTH_SUPREME(2881, 3038, 12005),
		PET_DAGANNOTH_PRIME(2882, 3039, 12006),
		PET_DAGANNOTH_REX(2883, 3040, 11990),
		PET_FROST_DRAGON(51, 3047, 11991),
		PET_TORMENTED_DEMON(8349, 3048, 11992),
		PET_KALPHITE_QUEEN(1158, 3050, 11993),
		PET_SLASH_BASH(2060, 3051, 11994),
		PET_PHOENIX(8549, 3052, 11989),
		PET_BANDOS_AVATAR(4540, 3053, 11988),
		PET_NEX(13447, 3054, 11987),
		PET_JUNGLE_STRYKEWYRM(9467, 3055, 11986),
		PET_DESERT_STRYKEWYRM(9465, 3056, 11985),
		PET_ICE_STRYKEWYRM(9463, 3057, 11984),
		PET_GREEN_DRAGON(941, 3058, 11983),
		PET_BABY_BLUE_DRAGON(52, 3059, 11982),
		PET_BLUE_DRAGON(55, 3060, 11981),
		PET_BLACK_DRAGON(54, 3061, 11979),
		PET_ABYZOU_HEARTWRENCHER(6313, 6315, 12177),
		PET_Suic(1999, 3544, 12178),
		PET_HELLHOUND(6311, 6312, 12315),
		PET_Suicencher(6313, 6318, 12319),
		PET_MINIWRENCHER(6313, 6328, 12445),
		PET_SUICSHOUND(6311, 6322, 12444),
		KID_BUU_PET(-1, 203, 16579),
		PERFECT_CELL_PET(-1, 204, 16580),
		FRIEZA_PET1(-1, 207, 16581),
		FRIEZA_PET2(-1, 218, 16584),
		FRIEZA_PET3(-1, 207, 16583),
		PET_SUICSPIDER(6307, 6308, 12446),
		PET_BALANCE_ELEMETANL(8281, 8282, 19666),
		PET_UMD_WANTED_WEIRD_LEGS_SO_HE_GOT_IT(8888, 8888, 3953),
		INFERNAL_BEAST_PET(9993, 9995, 3948),
		DEFENDERS_PET(9994, 9996, 3949),
		HULKS_PET(8493, 8497, 3957),
		CUSTOM_OLM_PET(8670, 8779, 5156),
		BANKET_PET(494, 8945, 19930),
		CVORAGO_PET(8507, 8512, 5155),
		HELLFIRE_DRAGON_PET(6593, 6594, 5151),
		BLUEFIRE_DRAGON_PET(6595, 6596, 5152),
		LAVA_DRAGON_PET(6599, 6600, 5153),
		COOL_DRAGON_PET(6603, 6604, 5154),
		BULBASAUR(842, 1008, 5157), 
		BLUEFIREDRAGON(6595, 6596, 11218),
		
		ABBADON(-1, 6304, 12542),
		RAINBOW_EEVEE(-1, 5960, 19753),
		
		

		
		GOKU(100, 806, 5127), 
		VEGETA(101, 808, 5128),
		FUZEDG(527, 809, 5135), 

		SKOTIZO(7286, 1946, 5148), 
		
		EEVEE(-1, 3137, 4742),
		JOLTEON(-1, 3138, 4743),
		FLAREON(-1, 642, 4744),
		VAPEREON(-1, 644, 4786),
		SYLVEON(-1, 722, 4787),


		PET_ASSASSIN(-1, 9945, 22204),
		ANTMAN(-1, 811, 1648), 
		MEWTWO(-1, 810, 1647), 
		ZORBAK(-1, 815, 1855), 
		STONEDTOAD(-1, 816, 2756), 
		DRAKKON(-1, 817, 2757),
		GALVEK(-1, 818, 2758),
		VASAV(-1, 819, 2759),
		DARKBEAST(-1, 820, 2762),
		ETERNAL(-1, 4971, 2763),
		KILLERCHUCKY(-1, 2322, 3647),
		LION(-1, 6249, 2764),
		ADVANCEDMBOX(-1, 225, 3638),
		EXTREMEMBOX(-1, 223, 3639),
		SUPREMEMBOX(-1, 227, 3640),
		BABYYODA(-1, 230, 6486),
		DONALDDUCK(-1, 229, 6484),
		CHARMANDER(174, 639, 5160), 
		JOKER(420, 640, 5161), 
		CHARIZARD(2843, 641, 5162), 
		GODZILLA(9932, 744, 12683), 
		LUCARIO(3263, 1739, 18377), 
		BROLY(-1, 1060, 5163),
		COOKIEMONSTER(-1, 755, 5166),
		BUGATTI(-1, 1785, 18400),
		SPRING_ELEMENTAL(-1, 5539, 18620),
		ROCK_GOLEM(-1, 6723, 13321),
		HERON(-1, 6724, 13320),
		BEAVER(-1, 6726, 13322),
		TANGLEROOT(-1, 6727, 13323),
		ROCKY(-1, 6728, 13324),
		SQUIRREL(-1, 6729, 13325),
		RIFT_GUARDIAN(-1, 6730, 13326),;

		@SuppressWarnings("unused")
		private final int npcId; 
		private final int spawnNpcId; 
		private final int itemId;
		
		BossPet(int npcId, int spawnNpcId, int itemId) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
		}
		
		public static BossPet forId(int itemId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getItemId() == itemId) {
					return p;
				}
			}
			return null;
		}
		
		public static BossPet forSpawnId(int spawnNpcId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getSpawnNpcId() == spawnNpcId) {
					return p;
				}
			}
			return null;
		}

		public int getSpawnNpcId() {
			return spawnNpcId;
		}

		public int getItemId() {
			return itemId;
		}
	}
	
	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if(pet != null) {
			if(player.getSummoning().getFamiliar() != null && player.getSummoning().getFamiliar().getSummonNpc() != null && player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
				if(player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.getSpawnNpcId() && player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
					player.getSummoning().unsummon(true, true);
					player.getPacketSender().sendMessage("You pick up your pet.");
					return true;
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			} else {
				player.getPacketSender().sendMessage("This is not your pet to pick up.");
			}
		}
		return false;
	}
	
}
