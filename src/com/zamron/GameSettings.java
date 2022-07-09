package com.zamron;

import com.zamron.model.Position;
import com.zamron.net.security.ConnectionHandler;

import java.math.BigInteger;

public class GameSettings {

	/**
	 * The game version
	 */
	public static final int GAME_VERSION = 1;

	/**
	 *
	 * The default position
	 */
	//public static final Position DEFAULT_POSITION = new Position(2336, 3675);
	public static final Position DEFAULT_POSITION = new Position(2605, 3093);

	/**
	 * Configuration
	 */

	public static final Position RAIDS_FINISHED_POSITION = new Position(3220, 3223);

	public static boolean PLAYERS_ONLINE;

	public static boolean LIVE_GAME;

	public static boolean VOTING_CONNECTIONS;

	public static boolean STORE_CONNECTIONS;

	public static boolean YELL_STATUS;

	public static boolean BONUS_EXP;

	public static boolean DOUBLE_DROPS;
	
	
	public static boolean TRIPLE_EXP;
	
	public static boolean DOUBLE_EXP;

	public static boolean DOUBLE_POINTS;

	public static boolean DOUBLE_VOTE_TOKENS;
	public static boolean DOUBLE_BOSSPOINTS;

	public static boolean POS_ENABLED;
	
	public static boolean IS_GIVEAWAY;

	public static int PASSWORD_CHANGE;

	public static boolean PLAYER_LOGGING;

	public static boolean DEBUG_MODE;

	public static boolean SHOW_DEBUG_MESSAGES;

	public static boolean DEVELOPER_MODE;

	public static final boolean LOCALHOST = false;

	/**
	 * Dev server? Updated by a hostname check in GameServer
	 */
	public static boolean DEVELOPERSERVER = false;


	public static String getFileLocationData() {
        return "data/guides/";
    }


	/**
	 * Dzone activation
	 */
	public static boolean DZONEON = true;
	
	/**
	 * The game port
	 */
	public static final int GAME_PORT = 43595;



	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_THRESHOLD = 25;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_THRESHOLD = 50;
	
	/**
	 * The maximum amount of players who can receive rewards on a single game
	 * sequence.
	 */
	public static final int VOTE_REWARDING_THRESHOLD = 15;

	/**
	 * The maximum amount of connections that can be active at a time, or in
	 * other words how many clients can be logged in at once per connection.
	 * (0 is counted too)
	 */
	public static final int CONNECTION_AMOUNT = 3;

	/**
	 * The throttle interval for incoming connections accepted by the
	 * {@link ConnectionHandler}.
	 */
	public static final long CONNECTION_INTERVAL = 1000;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;
	
	/**
	 * The keys used for encryption on login
	 */
	public static final BigInteger RSA_MODULUS = new BigInteger("95531195088543003381926207549911578750927752559838646344102279851533093885936895701459642803359971619483361758568970235649765386985918101306821488063596658678610181711168036463209415228181305847847832082379183933879259945414168303442586791961472722947400452307538633351649526569969102829109536314017378433569");
	public static final BigInteger RSA_EXPONENT = new BigInteger("77801577836274078192577161020018929829745302121379189007280087352117542045989225493533530295657950855521384111293776299763043437523633866962941393470961547998589124962954684403059450970774414818924241519088254372331525610301075771380767119378544956435347898881962441752754084721185838685873107210335231264473");

	/**
	 * The maximum amount of messages that can be decoded in one sequence.
	 */
	public static final int DECODE_LIMIT = 30;
	
	/** GAME **/

	/**
	 * Processing the engine
	 */
	public static final int ENGINE_PROCESSING_CYCLE_RATE = 600;
	public static final int GAME_PROCESSING_CYCLE_RATE = 600;

	public static boolean EventArena = false;

	public static boolean PARTY_ROOM_ENABLED = true;

	public static boolean DOUBLE_DAMAGE_EVENT = false;
	public static boolean TRIPLE_DAMAGE_EVENT = false;
	/**
	 * Are the MYSQL services enabled?
	 */
	public static boolean MYSQL_ENABLED = false;

	
	public static final int MAX_STARTERS_PER_IP = 2;
	
	/**
	 * Untradeable items
	 * Items which cannot be traded or staked
	 */
	public static final int[] UNTRADEABLE_ITEMS = 
		{
		13727, 19634, 4651, 19635, 19642, 19643, 19644, 19711, 19712, 19785, 19786, 19787, 19788, 19789, 19790, 19803, 19804, 5092,
		6570, 14019, 20747, 19785, 18983, 19786, 19787, 19788, 19789, 19790, 19803, 19804, 8839, 8840, 8841, 8842, 19711, 19712, 14022, 14019, 15753, 16207, 16273, 15936, 16339, 16262, 16383, 16024, 16405, 16174, 16647, 16196, 16669, 15925, 16691, 15914, 16713, 16080, 16889, 16127, 16935, 16035, 17019, 16116, 17239, 16013, 17341, 15808, 15755, 16208, 16275, 15937, 16341, 16263, 16385, 16025, 16407, 16175, 16649, 16197, 16671, 15926, 16693, 15915, 16715, 16081, 16891, 16128, 16937, 16036, 17021, 16117, 17241, 16014, 17343, 15809, 15757, 16209, 16277, 15938, 16343, 16264, 16387, 16026, 16651, 16198, 16673, 15927, 16695, 15916, 16717, 16082, 16893, 16129, 16939, 16037, 17023, 16118 , 17243, 16015, 17345, 15810, 15759, 16210, 16279, 15939, 16345, 16265, 16389, 16027, 16653, 16199, 16675, 15928, 16697, 15917, 16719, 16083, 16895, 16130, 16941, 16038, 17025, 16119, 17245, 16016, 17347, 15811, 15761, 16211, 16281, 15940, 16347, 16266, 16391, 16028, 16413, 16178, 16655, 16200, 16677, 15929, 16699, 15918, 16721, 16084, 16897, 16131, 16943, 16039, 17027, 16120, 17247, 16017, 17349, 15812, 15763, 16212, 16283, 15941, 16349, 16267, 16393, 16029, 16415, 16179, 16201, 16679, 15930, 16701, 15919, 16723, 16085, 16899, 16132, 16945, 16040, 17029, 16121, 17249, 16018, 17351, 15813, 15765, 16213, 16285, 15942, 16351, 16268, 16395, 16030, 16417, 16180, 16659, 16202, 16681, 15931, 16703, 15920, 16725, 16086, 16901, 16133, 16947, 16041, 17031, 16122, 17251, 16019, 17353, 15814, 15767, 16214, 16287, 15943, 16353, 16269, 16397, 16031, 16419, 16181, 16661, 16203, 16683, 15932, 16705, 15921, 16727, 16087, 16903, 16134, 16949, 16042, 17033, 16123, 17253, 16020, 17355, 15815, 15769, 16215, 16289, 15944, 16355, 16270, 16399, 16032, 16421, 16182, 16663, 16204, 16685, 15933, 16707, 15922, 16729, 16088, 16905, 16135, 16951, 16043, 17035, 16124, 17255, 16021, 17357, 15816, 15771, 16216, 16291, 15945, 16357, 16271, 16401, 16033, 16423, 16183, 16665, 16205, 16687, 15934, 16709, 15923, 16731, 16089, 16907, 16136, 16953, 16044, 17037, 16125, 17257, 16022, 17359, 15817, 19009,
		};

	/**
	 * Unsellable items
	 * Items which cannot be sold to shops
	 */
	public static int UNSELLABLE_ITEMS[] = new int[] {
			5092,13727, 995, 4651, 13262, 19634, 19635, 19642, 19643, 19644, 19711, 19712, 19785, 19786, 19787, 19788, 19789, 19790, 19803, 19804, 6570, 14019, 20747, 19785, 19786, 19787, 19788, 19789, 19790, 19803, 19804, 8839, 8840, 8841, 8842, 19711, 19712, 14022, 14019, 15753, 16207, 16273, 15936, 16339, 16262, 16383, 16024, 16405, 16174, 16647, 16196, 16669, 15925, 16691, 15914, 16713, 16080, 16889, 16127, 16935, 16035, 17019, 16116, 17239, 16013, 17341, 15808, 15755, 16208, 16275, 15937, 16341, 16263, 16385, 16025, 16407, 16175, 16649, 16197, 16671, 15926, 16693, 15915, 16715, 16081, 16891, 16128, 16937, 16036, 17021, 16117, 17241, 16014, 17343, 15809, 15757, 16209, 16277, 15938, 16343, 16264, 16387, 16026, 16651, 16198, 16673, 15927, 16695, 15916, 16717, 16082, 16893, 16129, 16939, 16037, 17023, 16118 , 17243, 16015, 17345, 15810, 15759, 16210, 16279, 15939, 16345, 16265, 16389, 16027, 16653, 16199, 16675, 15928, 16697, 15917, 16719, 16083, 16895, 16130, 16941, 16038, 17025, 16119, 17245, 16016, 17347, 15811, 15761, 16211, 16281, 15940, 16347, 16266, 16391, 16028, 16413, 16178, 16655, 16200, 16677, 15929, 16699, 15918, 16721, 16084, 16897, 16131, 16943, 16039, 17027, 16120, 17247, 16017, 17349, 15812, 15763, 16212, 16283, 15941, 16349, 16267, 16393, 16029, 16415, 16179, 16201, 16679, 15930, 16701, 15919, 16723, 16085, 16899, 16132, 16945, 16040, 17029, 16121, 17249, 16018, 17351, 15813, 15765, 16213, 16285, 15942, 16351, 16268, 16395, 16030, 16417, 16180, 16659, 16202, 16681, 15931, 16703, 15920, 16725, 16086, 16901, 16133, 16947, 16041, 17031, 16122, 17251, 16019, 17353, 15814, 15767, 16214, 16287, 15943, 16353, 16269, 16397, 16031, 16419, 16181, 16661, 16203, 16683, 15932, 16705, 15921, 16727, 16087, 16903, 16134, 16949, 16042, 17033, 16123, 17253, 16020, 17355, 15815, 15769, 16215, 16289, 15944, 16355, 16270, 16399, 16032, 16421, 16182, 16663, 16204, 16685, 15933, 16707, 15922, 16729, 16088, 16905, 16135, 16951, 16043, 17035, 16124, 17255, 16021, 17357, 15816, 15771, 16216, 16291, 15945, 16357, 16271, 16401, 16033, 16423, 16183, 16665, 16205, 16687, 15934, 16709, 15923, 16731, 16089, 16907, 16136, 16953, 16044, 17037, 16125, 17257, 16022, 17359, 15817,
	};

	public static final String[] INVALID_NAMES = { "mod", "moderator", "admin", "administrator", "owner", "developer", "support",
			"supporter", "dev", "developer", "nigga", "0wn3r", "4dm1n", "m0d", "adm1n", "a d m i n", "m o d",
			"o w n e r" };

	public static final int 
	ATTACK_TAB = 0, 
	SKILLS_TAB = 1, 
	QUESTS_TAB = 2, 
	ACHIEVEMENT_TAB = 14,
	INVENTORY_TAB = 3, 
	EQUIPMENT_TAB = 4, 
	PRAYER_TAB = 5, 
	MAGIC_TAB = 6,

	SUMMONING_TAB = 13, 
	FRIEND_TAB = 8, 
	IGNORE_TAB = 9, 
	CLAN_CHAT_TAB = 7,
	LOGOUT = 10,
	OPTIONS_TAB = 11,
	EMOTES_TAB = 12;

	public static int CONFIGURATION_TIME = 0;
	
	public static final int REGIONAL_DISTANCE = 64;
	
}
