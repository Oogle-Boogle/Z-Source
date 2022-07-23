package com.zamron.world.entity.impl.player;


import com.zamron.world.content.DPSOverlay;
import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.PlayerDeathTask;
import com.zamron.engine.task.impl.WalkToTask;
import com.zamron.model.*;
import com.zamron.model.container.impl.*;
import com.zamron.model.container.impl.Bank.BankSearchAttributes;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.model.definitions.WeaponInterfaces;
import com.zamron.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zamron.model.input.Input;
import com.zamron.net.PlayerSession;
import com.zamron.net.SessionState;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketBuilder;
import com.zamron.net.packet.PacketSender;
import com.zamron.net.packet.ValueType;
import com.zamron.util.FrameUpdater;
import com.zamron.util.Misc;
import com.zamron.util.Stopwatch;
import com.zamron.world.World;
import com.zamron.world.content.Achievements.AchievementAttributes;
import com.zamron.world.content.BankPin.BankPinAttributes;
import com.zamron.world.content.*;
import com.zamron.world.content.DropLog.DropLogEntry;
import com.zamron.world.content.KillsTracker.KillsEntry;
import com.zamron.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.zamron.world.content.NpcTasks.NpcTaskAttributes;
import com.zamron.world.content.StartScreen.GameModes;
import com.zamron.world.content.StarterTasks.StarterTaskAttributes;
import com.zamron.world.content.clan.ClanChat;
import com.zamron.world.content.collectionlog.CollectionEntry;
import com.zamron.world.content.collectionlog.CollectionLogInterface;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.zamron.world.content.combat.magic.CombatSpell;
import com.zamron.world.content.combat.magic.CustomMagicStaff;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.combat.pvp.PlayerKillingAttributes;
import com.zamron.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zamron.world.content.combat.strategy.CombatStrategies;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.combat.weapon.FightType;
import com.zamron.world.content.customraids.CustomRaid;
import com.zamron.world.content.customraids.RaidParty;
import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.gamblinginterface.GamblingInterface;
import com.zamron.world.content.grandexchange.GrandExchangeSlot;
import com.zamron.world.content.groupironman.GroupIronman;
import com.zamron.world.content.groupironman.GroupIronmanGroup;
import com.zamron.world.content.invansionminigame.InvasionGame;
import com.zamron.world.content.kcsystem.KCSystem;
import com.zamron.world.content.minigames.Minigame;
import com.zamron.world.content.minigames.MinigameAttributes;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.content.partyroom.PartyRoomManager;
import com.zamron.world.content.pos.PlayerOwnedShopManager;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.roulette.Roulette;
import com.zamron.world.content.scratchcards.ScratchCard;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.zamron.world.content.skill.impl.construction.ConstructionData.HouseTheme;
import com.zamron.world.content.skill.impl.construction.HouseFurniture;
import com.zamron.world.content.skill.impl.construction.Portal;
import com.zamron.world.content.skill.impl.construction.Room;
import com.zamron.world.content.skill.impl.farming.Farming;
import com.zamron.world.content.skill.impl.slayer.Slayer;
import com.zamron.world.content.skill.impl.summoning.BossPets;
import com.zamron.world.content.skill.impl.summoning.Pouch;
import com.zamron.world.content.skill.impl.summoning.Summoning;
import com.zamron.world.content.starterprogression.StarterProgression;
import com.zamron.world.content.starterzone.StarterZone;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.Bosses.CustomBossInstance;
import com.zamron.world.entity.impl.npc.Bosses.Sagittare;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.minigame.KeyRoom;
import lombok.Getter;
import lombok.Setter;
import com.zamron.world.content.teleport.TeleportInterface;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player extends Character {


	private final TeleportInterface teleportInterface = new TeleportInterface(this);


	public TeleportInterface getTeleportInterface() {
		return teleportInterface;
	}


	private int perkIndex = 0;

	public boolean clickToTeleport;

	public int getPerkIndex() {
		return perkIndex;
	}

	public void setPerkIndex(int perkIndex) {
		this.perkIndex = perkIndex;
	}

	public int combineIndex = 0;
    public int combiner = 0;

    public Stopwatch getClickDelay() {
        return clickDelay;
    }
    public Stopwatch getCombayDelay() {
        return combatDelay;
    }

	private DPSOverlay dpsOverlay = new DPSOverlay(this);

	public DPSOverlay getDpsOverlay() {
		return dpsOverlay;
	}


	/**
	 * Raids
	 */

	private RaidParty raidParty = null;

	public RaidParty getRaidParty() {
		return this.raidParty;
	}

	public void setRaidParty(RaidParty raidParty) {
		this.raidParty = raidParty;
	}

	private Set<String> bannedRaidMembers = new HashSet<>();

	public Set<String> getBannedRaidMembers() {
		return this.bannedRaidMembers;
	}

	public void setBannedRaidMembers(Set<String> bannedRaidMembers) {
		this.bannedRaidMembers = bannedRaidMembers;
	}

	private boolean inRaid = false;

	private CustomRaid customRaid = new CustomRaid(this);

	public CustomRaid getCustomRaid() {

		return customRaid;
	}

	public void setCustomRaid(CustomRaid customRaid) {
		this.customRaid = customRaid;
	}

	public boolean isInRaid() {
		return inRaid;
	}

	public void setInRaid(boolean inRaid) {
		this.inRaid = inRaid;
	}


	// Presets
	public int[] presetItems = new int[56];
	public int[] presetGear = new int[30];
	private int PrayerBook;
	private int MageBook;
	private List<Integer> lootList;
	public int getPrayerBook() {
		return PrayerBook;
	}

	public void setPrayerBook(int amount) {
		this.PrayerBook = amount;
	}

	public int getMageBook() {
		return MageBook;
	}

	public void setMageBook(int amount) {
		this.MageBook = amount;
	}

	public int[] presetItems2 = new int[56];
	public int[] presetGear2 = new int[30];
	private int PrayerBook2;
	private int MageBook2;

	public int getPrayerBook2() {
		return PrayerBook2;
	}

	public void setPrayerBook2(int amount) {
		this.PrayerBook2 = amount;
	}

	public int getMageBook2() {
		return MageBook2;
	}

	public void setMageBook2(int amount) {
		this.MageBook2 = amount;
	}

	public int[] presetItems3 = new int[56];
	public int[] presetGear3 = new int[30];
	private int PrayerBook3;
	private int MageBook3;

	public int getPrayerBook3() {
		return PrayerBook3;
	}

	public void setPrayerBook3(int amount) {
		this.PrayerBook3 = amount;
	}

	public int getMageBook3() {
		return MageBook3;
	}

	public void setMageBook3(int amount) {
		this.MageBook3 = amount;
	}

	public int[] presetItems4 = new int[56];
	public int[] presetGear4 = new int[30];
	private int PrayerBook4;
	private int MageBook4;

	public int getPrayerBook4() {
		return PrayerBook4;
	}

	public void setPrayerBook4(int amount) {
		this.PrayerBook4 = amount;
	}

	public int getMageBook4() {
		return MageBook4;
	}

	public void setMageBook4(int amount) {
		this.MageBook4 = amount;
	}

	public int[] presetItems5 = new int[56];
	public int[] presetGear5 = new int[30];
	private int PrayerBook5;
	private int MageBook5;

	public int getPrayerBook5() {
		return PrayerBook5;
	}

	public void setPrayerBook5(int amount) {
		this.PrayerBook5 = amount;
	}

	public int getMageBook5() {
		return MageBook5;
	}

	public void setMageBook5(int amount) {
		this.MageBook5 = amount;
	}

	public static boolean preset = false;
	public static boolean preset2 = false;
	public static boolean preset3 = false;
	public static boolean preset4 = false;
	public static boolean preset5 = false;

	public static boolean bankPreset = false;

	/** Mini Me's **/

	@Getter
	@Setter
	public Player minime, minimeOwner;

	public boolean isMiniMe = false;

	public boolean pendingBotRemoval = false;

	public void flagBotRemoval() {
		pendingBotRemoval = true;
	}

	public boolean checkPendingBotRemoval() {
		return pendingBotRemoval;
	}


	 /**
     * Group ironman
     */

    private GroupIronmanGroup groupIronmanGroup;

    public GroupIronmanGroup getGroupIronmanGroup() {
        return groupIronmanGroup;
    }





    public void setGroupIronmanGroup(GroupIronmanGroup groupIronmanGroup) {
        this.groupIronmanGroup = groupIronmanGroup;
    }

    private BestItems bestItems = new BestItems(this);

    private GroupIronmanGroup groupIronmanGroupInvitation = null;

    public GroupIronmanGroup getGroupIronmanGroupInvitation() {
        return groupIronmanGroupInvitation;
    }

    public void setGroupIronmanGroupInvitation(GroupIronmanGroup groupIronmanGroupInvitation) {
        this.groupIronmanGroupInvitation = groupIronmanGroupInvitation;
    }

    private String groupOwnerName = null;

    public String getGroupOwnerName() {
        return groupOwnerName;
    }

    public void setGroupOwnerName(String groupOwnerName) {
        this.groupOwnerName = groupOwnerName;
    }

    private GroupIronman groupIronman = new GroupIronman(this);

    public GroupIronman getGroupIronman() {
        return groupIronman;
    }

    private boolean gim;

    public boolean isGim() {
        return gim;
    }



    public void setGim(boolean state) {
        gim = state;
    }

	public final StarterZone starterZone = new StarterZone(this);

	public final StarterProgression starterProgression = new StarterProgression(this);

	public StarterProgression getStarterProgression() {
		return starterProgression;
	}

	private boolean[] starterProgressionCompletions = new boolean[6];

	public boolean[] getStarterProgressionCompletions() {
		return starterProgressionCompletions;
	}

	public void setStarterProgressionCompletions(boolean[] starterProgressionCompletions) {
		this.starterProgressionCompletions = starterProgressionCompletions;
	}

	public void setStarterProgressionCompleted() {
		for (int i = 0; i < starterProgressionCompletions.length; i++) {
			if (!starterProgressionCompletions[i]) {
				starterProgressionCompletions[i] = true;
				break;
			}
		}
	}

	public int getCurrentStarterProgression() {
		for (int i = 0; i < starterProgressionCompletions.length; i++) {
			if (!starterProgressionCompletions[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Drop Simulator
	 */

	DropSimulator ds = new DropSimulator(this);

	/**
	 * The last text clicked interface id.
	 */
	public int textClickedInterfaceId;

	public DropSimulator getDropSimulator() {
		return ds;
	}

	/**
	 * Defenders minigame
	 */
	public int summoned = -1;

	public int getSummoned() {
		return summoned;
	}

	public void setSummoned(int sum) {
		this.summoned = sum;
	}

	DefendersMinigame defendersMg = new DefendersMinigame(this);

	public DefendersMinigame getDefendersMg() {
		return defendersMg;
	}

	public NPC activePet = null;

	public void addItemUnderAnyCircumstances(Item item) {
		if (getInventory().getFreeSlots() < 1) {
			getBank(0).add(item);
			sendMessage("Your " + item.getDefinition().getName() + " has been placed in your bank.");
		} else {
			getInventory().add(item);
		}
	}

	public static void setFixedScrollMax(Player player, int interfaceId, int scrollMax) {
		player.getPA().sendMessage(":packet:scrollmax " + interfaceId + " " + scrollMax);
	}

	public static void setFixedScrollMax(Player player, int interfaceId, double entries, int multiplier,
			int minimumScrollLength) {
		int scrollLength = (int) Misc.getDoubleRoundedUp(entries * multiplier);
		if (scrollLength < minimumScrollLength) {
			scrollLength = minimumScrollLength;
		}
		Player.setFixedScrollMax(player, interfaceId, scrollLength);
	}

	/**
	 * Will clear these frames, starting from firstId to lastId
	 */
	public static void clearFrames(Player player, int firstId, int lastId) {
		for (int index = firstId; index <= lastId; index++) {
			if (player.getPA().interfaceText.containsKey(index)) {
				player.getPA().interfaceText.remove(index);
			}
		}
		player.getPA().sendMessage(":packet:clearframes " + firstId + " " + lastId);

	}

	@Setter
	@Getter
	public boolean knockedDoor = false;

	/**
	 * Invasion minigame
	 */

	InvasionGame invasionGame = new InvasionGame(this);

	public InvasionGame getInvasionGame() {
		return invasionGame;
	}

	private int invasionKc;

	public int getInvasionKc() {
		return invasionKc;
	}

	public int getNPCKILLCount() {
		return npckillcount;
	}

	private int npckillcount;

	public void setInvasionKc(int amount) {
		this.invasionKc = amount;
	}

	private CollectionLogInterface collectionLog = new CollectionLogInterface(this);
	@Setter
	private List<CollectionEntry> collectionLogData = new ArrayList<>();

	private Map<Integer, Map<Integer, Integer>> collectedItems = new HashMap<>(); // wtf

	public Map<Integer, Map<Integer, Integer>> getCollectedItems() {
		return collectedItems;
	}

	public void setCollectedItems(Map<Integer, Map<Integer, Integer>> collectedItems) {
		this.collectedItems = collectedItems;
	}

	public void handleCollectedItem(int npcId, Item item) {
		int id = item.getId();
		int amount = item.getAmount();
		if (collectedItems.get(npcId) == null) {
			Map<Integer, Integer> itemData = new HashMap<>();
			itemData.put(id, amount);
			collectedItems.put(npcId, itemData);
		} else {
			collectedItems.get(npcId).merge(id, amount, Integer::sum);
		}
	}

	public int currentPlayerPanelIndex = 1;

	public void incrementInvasionKc(int amount) {
		this.invasionKc += amount;
	}

	public void decrementInvasionKc(int amount) {
		this.invasionKc -= amount;
	}

	/**
	 * Roulette
	 */

	private Roulette roulette = new Roulette(this);

	public Roulette getRoulette() {
		return roulette;
	}

	private long rouletteBalance;

	public long getRouletteBalance() {
		return rouletteBalance;
	}

	public void setRouletteBalance(long amount) {
		this.rouletteBalance = amount;
	}

	public void incrementRouletteBalance(long amount) {
		this.rouletteBalance += amount;
	}

	public void decrementRouletteBalance(long amount) {
		this.rouletteBalance -= amount;
	}

	private long rouletteBet;

	public long getRouletteBet() {
		return rouletteBet;
	}

	public void setRouletteBet(long amount) {
		this.rouletteBet = amount;
	}



	public void resetRichPresence() {
		this.getPacketSender().sendRichPresenceState("Exploring Zamron..");
		this.getPacketSender().sendRichPresenceSmallPictureText("Combat Lvl: " + this.getSkillManager().getCombatLevel());
		this.getPacketSender().sendSmallImageKey("logo");
	}

	/**
	 * Instance manager interface
	 */

	private InstanceSystem instanceSystem = new InstanceSystem(this);

	public InstanceSystem getInstanceSystem() {
		return instanceSystem;
	}

	private int amountDonatedToday;

	public int getAmountDonatedToday() {
		return amountDonatedToday;
	}

	public void setAmountDonatedToday(int amount) {
		this.amountDonatedToday = amount;
	}

	public void incrementAmountDonatedToday(int amount) {
		this.amountDonatedToday += amount;
	}

	public boolean claimedFirst;
	public boolean claimedSecond;
	public boolean claimedThird;

	public long lastDonation;
	public long lastTimeReset;

	private boolean rewardTimerActive1;

	public boolean hasRewardTimerActive1() {
		return rewardTimerActive1;
	}

	public void setRewardTimerActive1(boolean active) {
		this.rewardTimerActive1 = active;
	}

	private boolean rewardTimerActive2;

	public boolean hasRewardTimerActive2() {
		return rewardTimerActive2;
	}

	public void setRewardTimerActive2(boolean active) {
		this.rewardTimerActive2 = active;
	}

	private boolean rewardTimerActive3;

	public boolean hasRewardTimerActive3() {
		return rewardTimerActive3;
	}

	public void setRewardTimerActive3(boolean active) {
		this.rewardTimerActive3 = active;
	}

	private boolean rewardTimerActive4;

	public boolean hasRewardTimerActive4() {
		return rewardTimerActive4;
	}

	public void setRewardTimerActive4(boolean active) {
		this.rewardTimerActive4 = active;
	}

	private int rewardTimer1;
	private int rewardTimer2;
	private int rewardTimer3;
	private int rewardTimer4;

	private final ScratchCard scratchCard = new ScratchCard(this);

	public ScratchCard getScratchCard() {
		return scratchCard;
	}

	public Map<Integer, Integer> getNpcKillCount() {
		return npcKillCountMap;
	}

	public void setNpcKillCount(Map<Integer, Integer> dataMap) {
		this.npcKillCountMap = dataMap;
	}

	public void addNpcKillCount(int npcId) {
		int amount = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_NPC_KILLS ? 2 : 1;
		npcKillCountMap.merge(npcId, amount, Integer::sum);
	}

	private Map<Integer, Integer> npcKillCountMap = new HashMap<>();

	public int getNpcKillCount(int npcId) {
		return npcKillCountMap.get(npcId) == null ? 0 : npcKillCountMap.get(npcId);
	}

	public int getKcSum() {
		return npcKillCountMap.values().stream().mapToInt(Integer::intValue).sum();
	}

	private final KCSystem kcSystem = new KCSystem(this);

	public KCSystem getKcSystem() {
		return kcSystem;
	}

	public int getRewardTimer1() {
		return rewardTimer1;
	}

	public int getRewardTimer2() {
		return rewardTimer2;
	}

	public int getRewardTimer3() {
		return rewardTimer3;
	}

	public int getRewardTimer4() {
		return rewardTimer4;
	}

	public void setRewardTimer1(int amount) {
		this.rewardTimer1 = amount;
	}

	public void setRewardTimer2(int amount) {
		this.rewardTimer2 = amount;
	}

	public void setRewardTimer3(int amount) {
		this.rewardTimer3 = amount;
	}

	public void setRewardTimer4(int amount) {
		this.rewardTimer4 = amount;
	}

	public void decrementRewardTimer1(int amount) {
		this.rewardTimer1 -= amount;
	}

	public void decrementRewardTimer2(int amount) {
		this.rewardTimer2 -= amount;
	}

	public void decrementRewardTimer3(int amount) {
		this.rewardTimer3 -= amount;
	}

	public void decrementRewardTimer4(int amount) {
		this.rewardTimer4 -= amount;
	}

	private DonationDeals donationDeals = new DonationDeals(this);

	public DonationDeals getDonationDeals() {
		return donationDeals;
	}

	private long crashGameBalance = 0;
	private long crashGameBet = 0;
	private double crashAutoCashout = 0.0;
	private double cashedOutMult = 0.0;

	public double getCashedOutMult() {
		return cashedOutMult;
	}

	public void setCashedOutMult(double cashedOutMult) {
		this.cashedOutMult = cashedOutMult;
	}

	public double getCrashAutoCashout() {
		return crashAutoCashout;
	}

	public void setCrashAutoCashout(double crashAutoCashout) {
		this.crashAutoCashout = crashAutoCashout;
	}

	public long getCrashGameBet() {
		return crashGameBet;
	}

	public void setCrashGameBet(long crashGameBet) {
		this.crashGameBet = crashGameBet;
	}

	public void addToCrashBalance(long l) {
		crashGameBalance += l;
	}

	public void removeFromCrashBalance(long amount) {
		crashGameBalance -= amount;
	}

	public long getCrashGameBalance() {
		return crashGameBalance;
	}

	/**
	 * Crash game variables
	 */

	private int betAmount;

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}

	private double cashoutMultiplier;

	public double getCashoutMultiplier() {
		return cashoutMultiplier;
	}

	public void setCashoutMultiplier(double cashoutMultiplier) {
		this.cashoutMultiplier = cashoutMultiplier;
	}

	private int depositedAmount;

	public int getDepositedAmount() {
		return depositedAmount;
	}

	public void setDepositedAmount(int depositedAmount) {
		this.depositedAmount = depositedAmount;
	}

	public void incrementDepositedAmount(int amount) {
		this.depositedAmount += amount;
	}

	public void decrementDepositedAmount(int amount) {
		this.depositedAmount -= amount;
	}

	public boolean inLMS;
	public boolean inLMSLobby;
	public int lmsLifes;
	public int lmsKillStreak;
	public int lmsPoints;
	public boolean starterClaimed;
	private List<Integer> blockedCollectorsList = new ArrayList<>();

	private final NpcTaskAttributes npcTaskAttributes = new NpcTaskAttributes();

	public NpcTaskAttributes getNpcTaskAttributes() {
		return npcTaskAttributes;
	}

	public boolean npcTaskClaimed;

	private int praiseTime;

	public int getPraiseTime() {
		return praiseTime;
	}

	public void setPraiseTime(int praiseTime) {
		this.praiseTime = praiseTime;
	}

	public boolean inDragon = false;

	public boolean attackable = true;

	public void incrementPraiseTime(int praiseTime) {
		this.praiseTime += praiseTime;
	}

	public void decrementPraiseTime(int praiseTime) {
		this.praiseTime -= praiseTime;
	}

	public boolean hasEntered = false;

	private boolean giveaway;

	public boolean isGiveaway() {
		return giveaway;
	}

	public void setGiveaway(boolean giveaway) {
		this.giveaway = giveaway;
	}

	private int cleansingTime;

	public int getCleansingTime() {
		return cleansingTime;
	}

	public void setCleansingTime(int cleansingTime) {
		this.cleansingTime = cleansingTime;
	}

	public void incrementCleansingTime(int cleansingTime) {
		this.cleansingTime += cleansingTime;
	}

	public void decrementCleansingTime(int cleansingTime) {
		this.cleansingTime -= cleansingTime;
	}

	public List<Integer> getBlockedCollectorsList() {
		return blockedCollectorsList;
	}

	public boolean getStarterClaimed() {
		return starterClaimed;
	}

	public void setStarterClaimed(boolean starterClaimed) {
		this.starterClaimed = starterClaimed;
	}

	public boolean starterTaskCompleted;

	public boolean isStarterTaskCompleted() {
		return starterTaskCompleted;
	}

	public void setStarterTaskCompleted() {
		starterTaskCompleted = true;
	}

	public int getLmsPoints() {
		return lmsPoints;
	}

	public void setLmsPoints(int lmsPoints) {
		this.lmsPoints = lmsPoints;
	}

	public void incrementLmsPoints(int amount) {
		this.lmsPoints += amount;
	}

	public void decrementLmsPoints(int amount) {
		this.lmsPoints -= amount;
	}

	public int getLMSLifes() {
		return lmsLifes;
	}

	public void setLMSLifes(int lmsLifes) {
		this.lmsLifes = lmsLifes;
	}

	public void incrementLMSLifes(int amount) {
		this.lmsLifes += amount;
	}

	public void decrementLMSLifes(int amount) {
		this.lmsLifes -= amount;
	}

	public int getLMSKillStreak() {
		return lmsKillStreak;
	}

	public void incrementLMSKillStreak(int amount) {
		this.lmsKillStreak += amount;
	}

	public void resetLMSKillStreak() {
		lmsKillStreak = 0;
	}

	public Item upgradeSelection;

	public Item getUpgradeSelection() {
		return upgradeSelection;
	}

	public void setUpgradeSelection(Item upgradeSelection) {
		this.upgradeSelection = upgradeSelection;
	}

	public Item dissolveSelection;

	public Item getDissolveSelection() {
		return dissolveSelection;
	}

	public void setDissolveSelection(Item dissolveSelection) {
		this.dissolveSelection = dissolveSelection;
	}

	private int minionsKC;

	public int getMinionsKC() {
		return minionsKC;
	}

	public void incrementMinionsKC(int amount) {
		this.minionsKC += amount;
	}

	public void setMinionsKC(int minionsKC) {
		this.minionsKC = minionsKC;
	}

	private int herculesKC;

	public int getHerculesKC() {
		return herculesKC;
	}

	public void incrementHerculesKC(int amount) {
		this.herculesKC += amount;
	}

	public void setHerculesKC(int herculesKC) {
		this.herculesKC = herculesKC;
	}

	private int hadesKC;

	public int getHadesKC() {
		return hadesKC;
	}

	public void incrementHadesKC(int amount) {
		this.hadesKC += amount;
	}

	public void setHadesKC(int hadesKC) {
		this.hadesKC = hadesKC;
	}

	private int lucarioKC;

	public int getLucarioKC() {
		return lucarioKC;
	}

	public void incrementLucarioKC(int amount) {
		this.lucarioKC += amount;
	}

	public void setLucarioKC(int lucarioKC) {
		this.lucarioKC = lucarioKC;
	}

	private int charizardKC;

	public int getCharizardKC() {
		return charizardKC;
	}

	public void incrementCharizardKC(int amount) {
		this.charizardKC += amount;
	}

	public void setCharizardKC(int charizardKC) {
		this.charizardKC = charizardKC;
	}

	private int defendersKC;

	public int getDefendersKC() {
		return defendersKC;
	}

	public void incrementDefendersKC(int amount) {
		this.defendersKC += amount;
	}

	public void setDefendersKC(int defendersKC) {
		this.defendersKC = defendersKC;
	}

	private int godzillaKC;

	public int getGodzillaKC() {
		return godzillaKC;
	}

	public void incrementGodzillaKC(int amount) {
		this.godzillaKC += amount;
	}

	public void setGodzillaKC(int GodzillaKC) {
		this.godzillaKC = GodzillaKC;
	}

	private int demonolmKC;

	public int getDemonolmKC() {
		return demonolmKC;
	}

	public void incrementDemonolmKC(int amount) {
		this.demonolmKC += amount;
	}

	public void setDemonolmKC(int DemonolmKC) {
		this.demonolmKC = DemonolmKC;
	}

	private int cerbKC;

	public int getCerbKC() {
		return cerbKC;
	}

	public void incrementCerbKC(int amount) {
		this.cerbKC += amount;
	}

	public void setCerbKC(int CerbKC) {
		this.cerbKC = CerbKC;
	}

	private int zeusKC;

	public int getZeusKC() {
		return zeusKC;
	}

	public void incrementZeusKC(int amount) {
		this.zeusKC += amount;
	}

	public void setZeusKC(int ZeusKC) {
		this.zeusKC = ZeusKC;
	}

	private int infarticoKC;

	public int getInfarticoKC() {
		return infarticoKC;
	}

	public void incrementInfarticoKC(int amount) {
		this.infarticoKC += amount;
	}

	public void setInfarticoKC(int InfarticoKC) {
		this.infarticoKC = InfarticoKC;
	}

	private int valorKC;

	public int getValorKC() {
		return valorKC;
	}

	public void incrementValorKC(int amount) {
		this.valorKC += amount;
	}

	public void setValorKC(int ValorKC) {
		this.valorKC = ValorKC;
	}

	private int hwKC;

	public int getHwKC() {
		return hwKC;
	}

	public void incrementHwKC(int amount) {
		this.hwKC += amount;
	}

	public void setHwKC(int HwKC) {
		this.hwKC = HwKC;
	}

	private int dzanthKC;

	public int getDzanthKC() {
		return dzanthKC;
	}

	public void incrementDzanthKC(int amount) {
		this.dzanthKC += amount;
	}

	public void setDzanthKC(int DzanthKC) {
		this.dzanthKC = DzanthKC;
	}

	private int kongKC;

	public int getKongKC() {
		return kongKC;
	}

	public void incrementKongKC(int amount) {
		this.kongKC += amount;
	}

	public void setKongKC(int KongKC) {
		this.kongKC = KongKC;
	}

	private int corpKC;

	public int getCorpKC() {
		return corpKC;
	}

	public void incrementCorpKC(int amount) {
		this.corpKC += amount;
	}

	public void setCorpKC(int CorpKC) {
		this.corpKC = CorpKC;
	}

	private int lucidKC;

	public int getLucidKC() {
		return lucidKC;
	}

	public void incrementLucidKC(int amount) {
		this.lucidKC += amount;
	}

	public void setLucidKC(int LucidKC) {
		this.lucidKC = LucidKC;
	}

	private int hulkKC;

	public int getHulkKC() {
		return hulkKC;
	}

	public void incrementHulkKC(int amount) {
		this.hulkKC += amount;
	}

	public void setHulkKC(int HulkKC) {
		this.hulkKC = HulkKC;
	}

	private int darkblueKC;

	public int getDarkblueKC() {
		return darkblueKC;
	}

	public void incrementDarkblueKC(int amount) {
		this.darkblueKC += amount;
	}

	public void setDarkblueKC(int DarkblueKC) {
		this.darkblueKC = DarkblueKC;
	}

	private int pyroKC;

	public int getPyroKC() {
		return pyroKC;
	}

	public void incrementPyroKC(int amount) {
		this.pyroKC += amount;
	}

	public void setPyroKC(int PyroKC) {
		this.pyroKC = PyroKC;
	}

	private int wyrmKC;

	public int getWyrmKC() {
		return wyrmKC;
	}

	public void incrementWyrmKC(int amount) {
		this.wyrmKC += amount;
	}

	public void setWyrmKC(int WyrmKC) {
		this.wyrmKC = WyrmKC;
	}

	private int exodenKC;

	public int getExodenKC() {
		return exodenKC;
	}

	public void incrementExodenKC(int amount) {
		this.exodenKC += amount;
	}

	public void setExodenKC(int ExodenKC) {
		this.exodenKC = ExodenKC;
	}

	private int trinityKC;

	public int getTrinityKC() {
		return trinityKC;
	}

	public void incrementTrinityKC(int amount) {
		this.trinityKC += amount;
	}

	public void setTrinityKC(int TrinityKC) {
		this.trinityKC = TrinityKC;
	}

	private int cloudKC;

	public int getCloudKC() {
		return cloudKC;
	}

	public void incrementCloudKC(int amount) {
		this.cloudKC += amount;
	}

	public void setCloudKC(int CloudKC) {
		this.cloudKC = CloudKC;
	}

	private int herbalKC;

	public int getHerbalKC() {
		return herbalKC;
	}

	public void incrementHerbalKC(int amount) {
		this.herbalKC += amount;
	}

	public void setHerbalKC(int HerbalKC) {
		this.herbalKC = HerbalKC;
	}

	private int supremeKC;

	public int getSupremeKC() {
		return supremeKC;
	}

	public void incrementSupremeKC(int amount) {
		this.supremeKC += amount;
	}

	public void setSupremeKC(int SupremeKC) {
		this.supremeKC = SupremeKC;
	}

	private int breakerKC;

	public int getBreakerKC() {
		return breakerKC;
	}

	public void incrementBreakerKC(int amount) {
		this.breakerKC += amount;
	}

	public void setBreakerKC(int BreakerKC) {
		this.breakerKC = BreakerKC;
	}

	private int apolloKC;

	public int getApolloKC() {
		return apolloKC;
	}

	public void incrementApolloKC(int amount) {
		this.apolloKC += amount;
	}

	public void setApolloKC(int ApolloKC) {
		this.apolloKC = ApolloKC;
	}

	private int noxKC;

	public int getNoxKC() {
		return noxKC;
	}

	public void incrementNoxKC(int amount) {
		this.noxKC += amount;
	}

	public void setNoxKC(int NoxKC) {
		this.noxKC = NoxKC;
	}

	private int azazelKC;

	public int getAzazelKC() {
		return azazelKC;
	}

	public void incrementAzazelKC(int amount) {
		this.azazelKC += amount;
	}

	public void setAzazelKC(int AzazelKC) {
		this.azazelKC = AzazelKC;
	}

	private int ravanaKC;

	public int getRavanaKC() {
		return ravanaKC;
	}

	public void incrementRavanaKC(int amount) {
		this.ravanaKC += amount;
	}

	public void setRavanaKC(int RavanaKC) {
		this.ravanaKC = RavanaKC;
	}

	private int luminKC;

	public int getLuminKC() {
		return luminKC;
	}

	public void incrementLuminKC(int amount) {
		this.luminKC += amount;
	}

	public void setLuminKC(int LuminKC) {
		this.luminKC = LuminKC;
	}

	private int customhKC;

	public int getCustomhKC() {
		return customhKC;
	}

	public void incrementCustomhKC(int amount) {
		this.customhKC += amount;
	}

	public void setCustomhKC(int CustomhKC) {
		this.customhKC = CustomhKC;
	}

	private int customOlmKC;

	private String bravekDifficulty;

	public String getBravekDifficulty() {
		return bravekDifficulty;
	}

	public void setBravekDifficulty(String bravekDifficulty) {
		this.bravekDifficulty = bravekDifficulty;
	}

	public int getcustomOlmKC() {
		return customOlmKC;
	}

	public void incrementCustomOlmKC(int amount) {
		this.customOlmKC += amount;
	}

	public void setCustomOlmKC(int amount) {
		this.customOlmKC = amount;
	}

	int totalBossKills;

	public void incrementTotalBossKills(int amount) {
		this.totalBossKills += amount;
	}

	public int getTotalBossKills() {
		return totalBossKills;
	}

	public void setTotalBossKills(int totalBossKills) {
		this.totalBossKills = totalBossKills;
	}

	public Achievements.AchievementDifficulty achievementDifficulty;

	private int WarmongerHealth = 0;

	private int sellToShopPoints;

	public int getSellToPoints() {
		return sellToShopPoints;
	}

	public void setShopPoints(int points) {
		this.sellToShopPoints += points;
	}

	public boolean hasReferral;

	public int getWarmongerHealth() {
		return WarmongerHealth;
	}

	public void setWarmongerHealth(int WarmongerHealth) {
		this.WarmongerHealth = WarmongerHealth;
	}

	private boolean DoubleDropsActive = false;

	public boolean isDoubleDropsActive() {
		return DoubleDropsActive;
	}

	public void setDoubleDropsActive(boolean doubleDropsActive) {
		DoubleDropsActive = doubleDropsActive;
	}

	public boolean doubleRateActive = false;

	public boolean isDoubleRateActive() {
		return doubleRateActive;
	}

	private String savedPin;
	private String savedIp;
	private boolean hasPin = false;

	public String getSavedPin() {
		return savedPin;
	}

	public void setSavedPin(String savedPin) {
		this.savedPin = savedPin;
	}

	public String getSavedIp() {
		return savedIp;
	}

	public void setSavedIp(String savedIp) {
		this.savedIp = savedIp;
	}

	public boolean getHasPin() {
		return hasPin;
	}

	public void setHasPin(boolean hasPin) {
		this.hasPin = hasPin;
	}

	public void setDoubleRateActive(boolean doubleRateActive) {
		this.doubleRateActive = doubleRateActive;
	}

	private final Stopwatch doubleRateTimer = new Stopwatch();

	public Stopwatch getDoubleRateTimer() {
		return doubleRateTimer;
	}

	private boolean Warmonger = false;

	public boolean getWarmonger() {
		return Warmonger;
	}

	public void setWarmonger(boolean Warmonger) {
		this.Warmonger = Warmonger;
	}

	private int[] maxCapeColors = { 65214, 65200, 65186, 62995 };
	private int[] compCapeColors = { 65214, 65200, 65186, 62995 };
	private int currentCape;
	private int currentHat;

	// private int[] santaColors = { 10351, 933 };

	public int[] getCompCapeColors() {
		return compCapeColors;
	}

	public void setCompCapeColors(int[] compCapeColors) {
		this.compCapeColors = compCapeColors;
	}

	public int getCurrentCape() {
		return currentCape;
	}

	public int getCurrentHat() {
		return currentHat;
	}

	public void setCurrentHat(int currentHat) {
		this.currentHat = currentHat;
	}

	public void setCurrentCape(int currentCape) {
		this.currentCape = currentCape;
	}

	public int[] getMaxCapeColors() {
		return maxCapeColors;
	}

	@Getter
	@Setter
	private String mac;
	@Getter
	@Setter
	private String uuid;


	public void setMaxCapeColors(int[] maxCapeColors) {
		this.maxCapeColors = maxCapeColors;
	}

	private String title = "";

	private final PlayerOwnedShopManager playerOwnedShopManager = new PlayerOwnedShopManager(this);

	private boolean active;

	private boolean shopUpdated;

	public PlayerOwnedShopManager getPlayerOwnedShopManager() {
		return playerOwnedShopManager;
	}

	public Player(PlayerSession playerIO) {
		super(GameSettings.DEFAULT_POSITION.copy());
		this.session = playerIO;
	}

	private final Map<String, Object> attributes = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) attributes.get(key);
	}

	private Minigame minigame = null;

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, T fail) {
		Object object = attributes.get(key);
		return object == null ? fail : (T) object;
	}

	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	private int hardwareNumber;

	public int getHardwareNumber() {
		return hardwareNumber;
	}

	public Player setHardwareNumber(int hardwareNumber) {
		this.hardwareNumber = hardwareNumber;
		return this;
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public void appendDeath() {
		if (!isDying) {
			isDying = true;
			TaskManager.submit(new PlayerDeathTask(this));
		}
	}

	private int runeunityPoints;

	public int getRuneUnityPoints() {
		return runeunityPoints;
	}

	public void setRuneUnityPoints(int runeunityPoints) {
		this.runeunityPoints = runeunityPoints;
	}

	public void incrementRuneUnityPoints(double amount) {
		this.runeunityPoints -= amount;
	}

	private int amongPoints;

	public int getAmongPoints() {
		return amongPoints;
	}

	public void setAmongPoints(int amongPoints) {
		this.amongPoints = amongPoints;
	}

	public void incrementAmongPoints(double amount) {
		this.amongPoints -= amount;
	}

	private int bossPoints;
	private int customPoints;


	@Getter
	@Setter
	private int dungeonPoints;

	public int getBossPoints() {
		return bossPoints;
	}

	public int getCustomPoints() {
		return customPoints;
	}

	public void decrementCustomPoints(double amount) {
		this.customPoints -= amount;
	}

	public void incrementCustomPoints(double amount) {
		this.customPoints += amount;
	}

	public void setBossPoints(int bossPoints) {
		this.bossPoints = bossPoints;
	}

	public void setCustomPoints(int customPoints) {
		this.customPoints = customPoints;
	}

	private int loyaltyPoints;

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	/*
	 * Variables for DropTable & Player Profiling
	 *
	 * @author Levi Patton
	 *
	 * @www.rune-server.org/members/auguryps
	 */
	public Player dropLogPlayer;
	public boolean dropLogOrder;
	private PlayerDropLog playerDropLog = new PlayerDropLog();
	private ProfileViewing profile = new ProfileViewing();

	@Getter
	@Setter
	private Difficulty difficulty = Difficulty.Default;

	@Getter
	@Setter
	private Difficulty selectedDifficulty = Difficulty.Default;

	/*
	 * Variables for the DropLog
	 *
	 * @author Levi Patton
	 */
	public PacketSender getPA() {
		return getPacketSender();
	}

	public PlayerDropLog getPlayerDropLog() {
		return playerDropLog;
	}

	public ProfileViewing getProfile() {
		return profile;
	}

	public void setProfile(ProfileViewing profile) {
		this.profile = profile;
	}

	public void setBravekTasksCompleted(int bravekTasksCompleted, boolean add) {
		if (add)
			this.bravekTasksCompleted += bravekTasksCompleted;
		else
			this.bravekTasksCompleted = bravekTasksCompleted;
	}

	public void setCustomWellDonated(int customWellTimesDonated, boolean add) {
		if (add)
			this.customWellTimesDonated += customWellTimesDonated;
		else
			this.customWellTimesDonated = customWellTimesDonated;
	}

	public int getBravekTasksCompleted() {
		return bravekTasksCompleted;
	}

	public int getCustomDonations() {
		return customWellTimesDonated;
	}

	public void incrementCustomWellDonated(int amount) {
		this.customWellTimesDonated += amount;
	}

	public void incrementBravekTasksCompleted(int amount) {
		this.bravekTasksCompleted += amount;
	}

	public void decrementBravekTasksCompleted(int amount) {
		this.bravekTasksCompleted -= amount;
	}

	public void setPlayerDropLog(PlayerDropLog playerDropLog) {
		this.playerDropLog = playerDropLog;
	}

	@Override
	public int getConstitution() {
		return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
	}

	@Override
	public Character setConstitution(int constitution) {
		if (isDying) {
			System.out.println("????");
			return this;
		}
		skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
		packetSender.sendSkill(Skill.CONSTITUTION);
		if (getConstitution() <= 0 && !isDying) {
			appendDeath();
		}
		return this;
	}

	@Override
	public void heal(int amount) {
		int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
		if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
			setConstitution(level);
		} else {
			setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
		}
	}

	@Override
	public int getBaseAttack(CombatType type) {
		if (type == CombatType.RANGED) {
			return skillManager.getCurrentLevel(Skill.RANGED);
		} else if (type == CombatType.MAGIC) {
			return skillManager.getCurrentLevel(Skill.MAGIC);
		}
		return skillManager.getCurrentLevel(Skill.ATTACK);
	}

	@Override
	public int getBaseDefence(CombatType type) {
		if (type == CombatType.MAGIC) {
			return skillManager.getCurrentLevel(Skill.MAGIC);
		}
		return skillManager.getCurrentLevel(Skill.DEFENCE);
	}
	public static void updateAppearance(Player player, PacketBuilder out, Player target) {
		for(Player p : World.getPlayers()) {

		}
		Appearance appearance = target.getAppearance();
		Equipment equipment = target.getEquipment();
		//EquipmentWings equipment2 = target.getEquipmentWings();
		PacketBuilder properties = new PacketBuilder();
		properties.put(appearance.getGender().ordinal());
		properties.put(appearance.getHeadHint());
		properties.put(target.getLocation() == Locations.Location.WILDERNESS ? appearance.getBountyHunterSkull() : -1);
		properties.putShort(target.getSkullIcon());

		int test = 0;



		if (target.getNpcTransformationId() > 1) {
			properties.putShort(-1);
			properties.putShort(target.getNpcTransformationId());
		}else {
			int[] equip = new int[equipment.capacity()];
			for (int i = 0; i < 14; i++) {
				equip[i] = equipment.getItems()[i].getId();
			}
			if (equip[Equipment.HEAD_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.HEAD_SLOT]);
			} else {
				properties.put(0);
			}
			if (equip[Equipment.CAPE_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.CAPE_SLOT]);
				if (equip[Equipment.CAPE_SLOT] == 14019) {
					/*
					 * int[] modelColors = new int[] { 65214, 65200, 65186, 62995 };
					 * if(target.getUsername().equalsIgnoreCase("apache ah64")) { modelColors[0] =
					 * 926;//cape modelColors[1] = 350770;//cape modelColors[2] = 928;//outline
					 * modelColors[3] = 130770;//cape } else
					 * if(target.getUsername().equalsIgnoreCase("apache ah66")) { modelColors[0] =
					 * 926;//cape modelColors[1] = 302770;//cape modelColors[2] = 928;//outline
					 * modelColors[3] = 302770;//cape }
					 */
					int[] modelColors = target.getMaxCapeColors();
					// System.out.println("Updating: "+Arrays.toString(modelColors));
					if (modelColors != null) {
						properties.put(modelColors.length);
						for (int i = 0; i < modelColors.length; i++) {
							properties.putInt(modelColors[i]);
						}
					} else {
						properties.put(0);
					}
				} else {
					properties.put(0);
				}
			} else {
				properties.put(0);
			}
			if (equip[Equipment.AMULET_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.AMULET_SLOT]);
			} else {
				properties.put(0);
			}
			if (equip[Equipment.WEAPON_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.WEAPON_SLOT]);
			} else {
				properties.put(0);
			}
			if (equip[Equipment.BODY_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.BODY_SLOT]);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.CHEST]);
			}
			if (equip[Equipment.SHIELD_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.SHIELD_SLOT]);
			} else {
				properties.put(0);
			}

			if (ItemDefinition.forId(equip[Equipment.BODY_SLOT]).isFullBody()) {
				properties.put(0);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.ARMS]);
			}

			if (equip[Equipment.LEG_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.LEG_SLOT]);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.LEGS]);
			}

			if (ItemDefinition.forId(equip[Equipment.HEAD_SLOT]).isFullHelm()) {
				properties.put(0);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.HEAD]);
			}
			if (equip[Equipment.HANDS_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.HANDS_SLOT]);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.HANDS]);
			}
			if (equip[Equipment.FEET_SLOT] > -1) {
				properties.putShort(0x200 + equip[Equipment.FEET_SLOT]);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.FEET]);
			}
			if (appearance.getLook()[Appearance.BEARD] <= 0 || appearance.getGender().equals(Gender.FEMALE)) {
				properties.put(0);
			} else {
				properties.putShort(0x100 + appearance.getLook()[Appearance.BEARD]);
			}
		}
		properties.put(appearance.getLook()[Appearance.HAIR_COLOUR]);
		properties.put(appearance.getLook()[Appearance.TORSO_COLOUR]);
		properties.put(appearance.getLook()[Appearance.LEG_COLOUR]);
		properties.put(appearance.getLook()[Appearance.FEET_COLOUR]);
		properties.put(appearance.getLook()[Appearance.SKIN_COLOUR]);

		int skillAnim = target.getSkillAnimation();
		if(skillAnim > 0) {
			for(int i = 0; i < 7; i++)
				properties.putShort(skillAnim);
		} else {
			properties.putShort(target.getCharacterAnimations().getStandingAnimation());
			properties.putShort(0x337);
			properties.putShort(target.getCharacterAnimations().getWalkingAnimation());
			properties.putShort(0x334);
			properties.putShort(0x335);
			properties.putShort(0x336);
			properties.putShort(target.getCharacterAnimations().getRunningAnimation());
		}

		properties.putLong(target.getLongUsername());
		properties.put(target.getSkillManager().getCombatLevel());
		properties.putShort(target.getRights().ordinal());
		properties.putString(target.getTitle());
		////System.out.println("test - "+target.getTitle());
		//properties.putShort(target.getLoyaltyTitle().ordinal());

		out.put(properties.buffer().writerIndex(), ValueType.C);
		out.putBytes(properties.buffer());
	}


	@Override
	public int getAttackSpeed() {
		int speed = weapon.getSpeed();
		int weaponId = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getId();

		if (weaponId == 19618
				|| weaponId == 9492
				|| weaponId == 2760
				|| weaponId == 19727
				|| weaponId == 10905
				|| weaponId == 19154
				|| weaponId == 8656
				|| weaponId == 20427
				|| weaponId == 6320
				|| weaponId == 1413
				|| weaponId == 3920
				|| weaponId == 22196
				|| weaponId == 13265
				|| weaponId == 8655
				|| weaponId == 8654
				|| weaponId == 3077

				|| weaponId == 3072
				|| weaponId == 5089
				|| weaponId == 3991
				|| weaponId == 18919
				|| weaponId == 3651
				|| weaponId == 3274
				|| weaponId == 19123
				|| weaponId == 3282
				|| weaponId == 19620
				|| weaponId == 18398
				|| weaponId == 16429
				|| weaponId == 925
				|| weaponId == 12426   //ICY GLAIVE
				|| weaponId == 17931   //Sires glaive
				|| weaponId == 18398
				|| weaponId == 12428
				|| weaponId == 4059
		) {
			return 1;
		}
		if (weaponId == 5131
				|| weaponId == 19004
				|| weaponId == 5130
				|| weaponId == 19720
				|| weaponId == 5132
				|| weaponId == 15653
				|| weaponId == 5195
				|| weaponId == 15656
				|| weaponId == 7764
				|| weaponId == 13201
				|| weaponId == 16550
				|| weaponId == 3275

				|| weaponId == 3951
				|| weaponId == 14559
				|| weaponId == 3941
				|| weaponId == 3990
				|| weaponId == 5081
				|| weaponId == 5125
				|| weaponId == 5133
				|| weaponId == 5134
				|| weaponId == 5129
				|| weaponId == 19163
				|| weaponId == 8664
				|| weaponId == 4796
				|| weaponId == 18931
				|| weaponId == 13995
				|| weaponId == 6483
				|| weaponId == 13094
				|| weaponId == 18865
				|| weaponId == 2543
				|| weaponId == 18683
				|| weaponId == 5115
				|| weaponId == 3276
				|| weaponId == 6930
				|| weaponId == 8001
				|| weaponId == 3279
				|| weaponId == 3250
				|| weaponId == 7682
				|| weaponId == 7688
				|| weaponId == 18385
				|| weaponId == 16429
		) {
			return 2;
		}
		if (weaponId == 3082
				|| weaponId == 14006
				|| weaponId == 3928
				|| weaponId == 5173
				|| weaponId == 19468
				|| weaponId == 16137
				|| weaponId == 7806
				|| weaponId == 6197
				|| weaponId == 923
				|| weaponId == 13207
				|| weaponId == 896
				|| weaponId == 1667
				|| weaponId == 3641  //Solace glaive
		) {
			return 3;
		}

		return speed;
		// return DesolaceFormulas.getAttackDelay(this);
	}

	public boolean sendElementalMessage = true;
	public int clue1Amount;
	public int clue2Amount;
	public int clue3Amount;
	public int clueLevel;
	public Item[] puzzleStoredItems;
	public int sextantGlobalPiece;
	public double sextantBarDegree;
	public int rotationFactor;
	public int sextantLandScapeCoords;
	public int sextantSunCoords;

	// private Channel channel;

	// public Player write(Packet packet) {
	// if (channel.isConnected()) {
	// channel.write(packet);
	// }
	// return this;
	// }

	/// public Channel getChannel() {
	// return channel;
	// }

	private Bank bank = new Bank(this);

	public Bank getBank() {
		return bank;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Player)) {
			return false;
		}
		Player p = (Player) o;
		int index = getIndex();
		return (index >= 0 && index == p.getIndex()) || username.equals(p.getUsername());
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public void poisonVictim(Character victim, CombatType type) {
		if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
				|| weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
			CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
		} else if (type == CombatType.RANGED) {
			CombatFactory.poisonEntity(victim,
					CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
		}
	}

	@Override
	public CombatStrategy determineStrategy() {
		if (specialActivated && castSpell == null) {

			if (combatSpecial.getCombatType() == CombatType.MELEE) {
				return CombatStrategies.getDefaultMeleeStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.RANGED) {
				setRangedWeaponData(RangedWeaponData.getData(this));
				return CombatStrategies.getDefaultRangedStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
				return CombatStrategies.getDefaultMagicStrategy();
			}
		}
		if (CustomMagicStaff.checkCustomStaff(this)) {
			CustomMagicStaff.handleCustomStaff(this);
			this.setCastSpell(CustomMagicStaff.CustomStaff
					.getSpellForWeapon(this.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()));
			return CombatStrategies.getDefaultMagicStrategy();
		}

		if (castSpell != null || autocastSpell != null
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 11605
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 6483
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 11609
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3951
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5129
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13995
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 15653
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19727
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 6483
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8664
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 18891
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19720
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3282
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5179
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3911) {
			return CombatStrategies.getDefaultMagicStrategy();
		}

		if (castSpell != null || autocastSpell != null) {
			return CombatStrategies.getDefaultMagicStrategy();
		}

		RangedWeaponData data = RangedWeaponData.getData(this);
		if (data != null) {
			setRangedWeaponData(data);
			return CombatStrategies.getDefaultRangedStrategy();
		}

		return CombatStrategies.getDefaultMeleeStrategy();
	}

	public void process() {
		process.sequence();
	}

	public void dispose() {
		save();
		packetSender.sendLogout();
	}

	public void save() {
		if (isMiniMe)
			return;
		if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
			return;
		}
		PlayerSaving.save(this);
	}

	public boolean logout() {

		boolean debugMessage = false;

		if (this.getSummoning().getFamiliar() != null) {
			BossPets.pickup(this, this.getSummoning().getFamiliar().getSummonNpc());
		}
		int[] playerXP = new int[Skill.values.length];
		for (int i = 0; i < Skill.values.length; i++) {
			playerXP[i] = this.getSkillManager().getExperience(Skill.forId(i));
		}

		//Nicely format the gamemode
		String gameMode = Misc.formatText(this.getGameMode().name());

		if (!this.getGameMode().equals(GameMode.NORMAL) && !this.getRights().equals(PlayerRights.OWNER) && !this.getRights().equals(PlayerRights.DEVELOPER) && !this.getRights().equals(PlayerRights.ADMINISTRATOR)){
			com.everythingrs.hiscores.Hiscores.update("s10kavzuyiAJ2CIZ6dLY78lsehZihrk1ilAzzMgp9uP9vfLuDL968z5KSKoR1krXAGQr384P", gameMode, getUsername(), this.getRights().ordinal(), playerXP, debugMessage);
		}

		// Only submit a hiscore if the player isn't an Owner, Admin or Dev
		if (!this.getRights().equals(PlayerRights.OWNER) && !this.getRights().equals(PlayerRights.DEVELOPER) && !this.getRights().equals(PlayerRights.ADMINISTRATOR)){
			com.everythingrs.hiscores.Hiscores.update("s10kavzuyiAJ2CIZ6dLY78lsehZihrk1ilAzzMgp9uP9vfLuDL968z5KSKoR1krXAGQr384P", "Normal Mode", getUsername(), this.getRights().ordinal(), playerXP, debugMessage);
		}

		if (getCombatBuilder().isBeingAttacked()) {
			getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return false;
		}
		if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
			getPacketSender().sendMessage("You cannot log out at the moment.");
			return false;
		}
		return true;
	}

	public void restart() {
		setFreezeDelay(0);
		setOverloadPotionTimer(0);
		setPrayerRenewalPotionTimer(0);
		setSpecialPercentage(100);
		setSpecialActivated(false);
		CombatSpecial.updateBar(this);
		setHasVengeance(false);
		setSkullTimer(0);
		setSkullIcon(0);
		setTeleblockTimer(0);
		setPoisonDamage(0);
		setStaffOfLightEffect(0);
		performAnimation(new Animation(65535));
		WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		PrayerHandler.deactivateAll(this);
		CurseHandler.deactivateAll(this);
		getEquipment().refreshItems();
		getInventory().refreshItems();
		for (Skill skill : Skill.values) {
			getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
		}
		setRunEnergy(100);
		setDying(false);
		getMovementQueue().setLockMovement(false).reset();
		getUpdateFlag().flag(Flag.APPEARANCE);

	}

	public boolean busy() {
		return interfaceId > 0 || isBanking || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
	}

	/*
	 * Fields
	 */
	/**
	 * * STRINGS **
	 */
	private String username;
	private String password;
	private String serial_number;
	private String emailAddress;
	private String hostAddress;
	private String clanChatName;

	private HouseLocation houseLocation;

	private HouseTheme houseTheme;

	/**
	 * * LONGS *
	 */
	private long longUsername;
	private long moneyInPouch;
	private long totalPlayTime;
	// Timers (Stopwatches)
	private final Stopwatch sqlTimer = new Stopwatch();
	private final Stopwatch foodTimer = new Stopwatch();
	private final Stopwatch potionTimer = new Stopwatch();
	private final Stopwatch lastRunRecovery = new Stopwatch();
	private final Stopwatch clickDelay = new Stopwatch();
	private final Stopwatch lastItemPickup = new Stopwatch();
	private final Stopwatch lastYell = new Stopwatch();
    private final Stopwatch combatDelay = new Stopwatch();
	private final Stopwatch lastZulrah = new Stopwatch();
	private final Stopwatch lastSql = new Stopwatch();

	private final Stopwatch lastVengeance = new Stopwatch();
	private final Stopwatch emoteDelay = new Stopwatch();
	private final Stopwatch specialRestoreTimer = new Stopwatch();
	private final Stopwatch lastSummon = new Stopwatch();
	private final Stopwatch recordedLogin = new Stopwatch();
	@SuppressWarnings("unused")
	private final Stopwatch creationDate = new Stopwatch();
	private final Stopwatch tolerance = new Stopwatch();
	private final Stopwatch lougoutTimer = new Stopwatch();

	private final Stopwatch doubleDrops = new Stopwatch();

	/**
	 * * INSTANCES **
	 */
	private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
	private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
	private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
	private ArrayList<Portal> housePortals = new ArrayList<>();
	private final List<Player> localPlayers = new LinkedList<Player>();
	private final List<NPC> localNpcs = new LinkedList<NPC>();

	private PlayerSession session;
	private final PlayerProcess process = new PlayerProcess(this);
	private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
	private final MinigameAttributes minigameAttributes = new MinigameAttributes();
	private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
	private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
	private final AchievementAttributes achievementAttributes = new AchievementAttributes();
	private final StarterTaskAttributes starterTaskAttributes = new StarterTaskAttributes();
	private CharacterAnimations characterAnimations = new CharacterAnimations();
	private final BonusManager bonusManager = new BonusManager();
	private final PointsHandler pointsHandler = new PointsHandler(this);

	private final PacketSender packetSender = new PacketSender(this);
	private final Appearance appearance = new Appearance(this);
	private final FrameUpdater frameUpdater = new FrameUpdater();
	private PlayerRights rights = PlayerRights.PLAYER;

	@Setter
	private SecondaryPlayerRights secondaryPlayerRights = SecondaryPlayerRights.DEFAULT;

	private SkillManager skillManager = new SkillManager(this);
	private PlayerRelations relations = new PlayerRelations(this);
	private ChatMessage chatMessages = new ChatMessage();
	private Inventory inventory = new Inventory(this);
	private Equipment equipment = new Equipment(this);
	//private EquipmentWings equipmentWings = new EquipmentWings(this);
	private PriceChecker priceChecker = new PriceChecker(this);
	private Trading trading = new Trading(this);
	private GamblingInterface gambling = new GamblingInterface(this);

	public GamblingInterface getGambling() {
		return gambling;
	}

	private Dueling dueling = new Dueling(this);
	private Slayer slayer = new Slayer(this);

	private Farming farming = new Farming(this);
	private Summoning summoning = new Summoning(this);
	private Bank[] bankTabs = new Bank[9];
	private Room[][][] houseRooms = new Room[5][13][13];
	private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
	private GameMode gameMode = GameMode.NORMAL;
	private CombatType lastCombatType = CombatType.MELEE;
	private FightType fightType = FightType.UNARMED_PUNCH;
	private Prayerbook prayerbook = Prayerbook.NORMAL;
	private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
	private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;

	private ClanChat currentClanChat;
	private Input inputHandling;
	private WalkToTask walkToTask;
	private Shop shop;
	private GameObject interactingObject;
	private Item interactingItem;
	private Dialogue dialogue;
	private DwarfCannon cannon;
	private CombatSpell autocastSpell, castSpell, previousCastSpell;
	private RangedWeaponData rangedWeaponData;
	private CombatSpecial combatSpecial;
	private WeaponInterface weapon;
	private Item untradeableDropItem;
	private Object[] usableObject;
	private GrandExchangeSlot[] grandExchangeSlots = new GrandExchangeSlot[6];
	private Task currentTask;
	private Position resetPosition;
	private Pouch selectedPouch;
	private BlowpipeLoading blowpipeLoading = new BlowpipeLoading(this);

	/**
	 * * INTS **
	 */
	public int destination = 0;
	public int lastClickedTab = 0;

	private int[] brawlerCharges = new int[9];
	private int[] forceMovement = new int[7];
	private int[] leechedBonuses = new int[7];
	private int[] ores = new int[2];
	private int[] constructionCoords;
	private int recoilCharges;
	private int[] voidTopCharges = new int[1];
	private int[] voidLegCharges = new int[1];
	private int runEnergy = 100;
	private int currentBankTab;
	private int interfaceId, walkableInterfaceId, multiIcon;
	private int dialogueActionId;
	private int overloadPotionTimer, prayerRenewalPotionTimer;
	private int fireImmunity, fireDamageModifier;
	private int amountDonated;
	private int wildernessLevel;
	private int bravekTasksCompleted;
	private int fireAmmo;
	private int customWellTimesDonated;
	private int specialPercentage = 100;
	private int skullIcon = -1, skullTimer;
	private int teleblockTimer;
	private int dragonFireImmunity;
	private int poisonImmunity;
	private int shadowState;
	private int effigy;
	private int dfsCharges;
	private int playerViewingIndex;
	private int staffOfLightEffect;
	private int minutesBonusExp = -1;
	private int selectedGeSlot = -1;
	private int selectedGeItem = -1;
	private int geQuantity;
	private int gePricePerItem;
	private int selectedSkillingItem;
	private int currentBookPage;
	private int storedRuneEssence, storedPureEssence;
	private int trapsLaid;
	private int skillAnimation;
	private int houseServant;
	private int houseServantCharges;
	private int servantItemFetch;
	private int portalSelected;
	private int constructionInterface;
	private int buildFurnitureId;
	private int buildFurnitureX;
	private int buildFurnitureY;
	private int combatRingType;

	/**
	 * * BOOLEANS **
	 */
	private boolean unlockedLoyaltyTitles[] = new boolean[12];
	private boolean[] crossedObstacles = new boolean[7];
	private boolean processFarming;
	private boolean crossingObstacle;
	private boolean targeted;
	private boolean isBanking, noteWithdrawal, swapMode;
	private boolean regionChange, allowRegionChangePacket;
	private boolean isDying;
	private boolean isLegendary;
	private boolean isMbox1;
	private boolean isMbox2;
	private boolean isMbox3;
	private boolean isMbox4;
	private boolean isMbox5;
	private boolean isRunning = true, isResting;
	private boolean experienceLocked;
	private boolean clientExitTaskActive;
	private boolean drainingPrayer;
	private boolean shopping;
	private boolean settingUpCannon;
	private boolean hasVengeance;
	private boolean killsTrackerOpen;
	private boolean acceptingAid;
	private boolean autoRetaliate;
	private boolean autocast;
	private boolean specialActivated;
	private boolean isCoughing;
	private boolean playerLocked;
	private boolean recoveringSpecialAttack;
	private boolean soundsActive, musicActive;
	private boolean newPlayer;
	private boolean openBank;
	private boolean inActive;
	public int timeOnline;
	private boolean inConstructionDungeon;
	private boolean isBuildingMode;
	private boolean voteMessageSent;
	private boolean receivedStarter;

	private Position previousPosition;

	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	private MysteryBox mysteryBox = new MysteryBox(this);

	public MysteryBox getMysteryBox() {
		return mysteryBox;
	}

	/*
	 * Getters & Setters
	 */
	public PlayerSession getSession() {
		return session;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public PriceChecker getPriceChecker() {
		return priceChecker;
	}

	/*
	 * Getters and setters
	 */
	public String getUsername() {
		return username;
	}

	public Player setUsername(String username) {
		this.username = username;
		return this;
	}

	public long getLongUsername() {
		return longUsername;
	}

	public Player setLongUsername(long longUsername) {
		this.longUsername = longUsername;
		return this;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String address) {
		this.emailAddress = address;
	}

	/** Passwords are being encrypted now ~ Flub **/

	public String getPassword() { // Need to Decrypt
		return password;
	}

	public Player setPassword(String password) { // Need to encrypt
		this.password = password;
		return this;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public Player setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
		return this;
	}

	public String getSerialNumber() {
		return serial_number;
	}

	public Player setSerialNumber(String serial_number) {
		this.serial_number = serial_number;
		return this;
	}

	public FrameUpdater getFrameUpdater() {
		return this.frameUpdater;
	}

	public PlayerRights getRights() {
		return rights;
	}
	public SecondaryPlayerRights getSecondaryPlayerRights() {
		return secondaryPlayerRights;
	}

	public Player setRights(PlayerRights rights) {
		this.rights = rights;
		return this;
	}

    public boolean isDonor() {
        return getRights() == PlayerRights.DONATOR || getRights() == PlayerRights.SUPER_DONATOR || getRights() == PlayerRights.EXTREME_DONATOR || getRights() == PlayerRights.DELUXE_DONATOR || getRights() == PlayerRights.UBER_DONATOR
				|| getRights() == PlayerRights.LEGENDARY_DONATOR || getRights() == PlayerRights.VIP_DONATOR;
    }

    public boolean isAdmin() {
        return getRights() == PlayerRights.ADMINISTRATOR || getRights() == PlayerRights.OWNER;
    }

	public ChatMessage getChatMessages() {
		return chatMessages;
	}

	public PacketSender getPacketSender() {
		return packetSender;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public PlayerRelations getRelations() {
		return relations;
	}

	public PlayerKillingAttributes getPlayerKillingAttributes() {
		return playerKillingAttributes;
	}

	public PointsHandler getPointsHandler() {
		return pointsHandler;
	}

	public boolean isImmuneToDragonFire() {
		return dragonFireImmunity > 0;
	}

	public int getDragonFireImmunity() {
		return dragonFireImmunity;
	}

	public void setDragonFireImmunity(int dragonFireImmunity) {
		this.dragonFireImmunity = dragonFireImmunity;
	}

	public void incrementDragonFireImmunity(int amount) {
		dragonFireImmunity += amount;
	}

	public void decrementDragonFireImmunity(int amount) {
		dragonFireImmunity -= amount;
	}

	public int getPoisonImmunity() {
		return poisonImmunity;
	}

	public void setPoisonImmunity(int poisonImmunity) {
		this.poisonImmunity = poisonImmunity;
	}

	public void incrementPoisonImmunity(int amount) {
		poisonImmunity += amount;
	}

	public void decrementPoisonImmunity(int amount) {
		poisonImmunity -= amount;
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	/**
	 * @return the castSpell
	 */
	public CombatSpell getCastSpell() {
		return castSpell;
	}

	/**
	 * @param castSpell the castSpell to set
	 */
	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}

	public CombatSpell getPreviousCastSpell() {
		return previousCastSpell;
	}

	public void setPreviousCastSpell(CombatSpell previousCastSpell) {
		this.previousCastSpell = previousCastSpell;
	}

	/**
	 * @return the autocast
	 */
	public boolean isAutocast() {
		return autocast;
	}

	/**
	 * @param autocast the autocast to set
	 */
	public void setAutocast(boolean autocast) {
		this.autocast = autocast;
	}

	/**
	 * @return the skullTimer
	 */
	public int getSkullTimer() {
		return skullTimer;
	}

	/**
	 * @param skullTimer the skullTimer to set
	 */
	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public void decrementSkullTimer() {
		skullTimer -= 50;
	}

	/**
	 * @return the skullIcon
	 */
	public int getSkullIcon() {
		return skullIcon;
	}

	/**
	 * @param skullIcon the skullIcon to set
	 */
	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}

	/**
	 * @return the teleblockTimer
	 */
	public int getTeleblockTimer() {
		return teleblockTimer;
	}

	/**
	 * @param teleblockTimer the teleblockTimer to set
	 */
	public void setTeleblockTimer(int teleblockTimer) {
		this.teleblockTimer = teleblockTimer;
	}

	public void decrementTeleblockTimer() {
		teleblockTimer--;
	}

	/**
	 * @return the autocastSpell
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}

	/**
	 * @param autocastSpell the autocastSpell to set
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
	}

	/**
	 * @return the specialPercentage
	 */
	public int getSpecialPercentage() {
		return specialPercentage;
	}

	/**
	 * @param specialPercentage the specialPercentage to set
	 */
	public void setSpecialPercentage(int specialPercentage) {
		this.specialPercentage = specialPercentage;
	}

	/**
	 * @return the fireAmmo
	 */
	public int getFireAmmo() {
		return fireAmmo;
	}

	/**
	 * @param fireAmmo the fireAmmo to set
	 */
	public void setFireAmmo(int fireAmmo) {
		this.fireAmmo = fireAmmo;
	}

	public int getWildernessLevel() {
		return wildernessLevel;
	}

	public void setWildernessLevel(int wildernessLevel) {
		this.wildernessLevel = wildernessLevel;
	}

	/**
	 * @return the combatSpecial
	 */
	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}

	/**
	 * @param combatSpecial the combatSpecial to set
	 */
	public void setCombatSpecial(CombatSpecial combatSpecial) {
		this.combatSpecial = combatSpecial;
	}

	/**
	 * @return the specialActivated
	 */
	public boolean isSpecialActivated() {
		return specialActivated;
	}

	/**
	 * @param specialActivated the specialActivated to set
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}


	/** Flub adding DailyNPCTask Values **/
	@Getter
	@Setter
	public int currentDailyNPC = 0;//Used to save the last task the player participated in. If it doesn't match the current server task, reset KC.
	@Getter
	@Setter
	public int currentDailyNPCKills = 0;//Counts the current Kills.

	/** Flub adding Boss Minigame Values **/
	public int[] bossGameLevels = new int[Skill.values.length];
	public int[] bossGameSkillXP = new int[Skill.values.length];
	public int[] bossGameMaxLevels = new int[Skill.values.length];

	@Setter
	public int currentBossWave = 0;
	@Setter
	public int barrowsKC = 0;
	@Setter
	boolean playedNewBarrows = false;
	@Setter
	boolean usedBossTeleport = false;
	@Setter
	private boolean shouldGiveBossReward;
	/** End Boss Minigame Values **/


	public void decrementSpecialPercentage(int drainAmount) {
		this.specialPercentage -= drainAmount;

		if (specialPercentage < 0) {
			specialPercentage = 0;
		}
	}

	public void incrementSpecialPercentage(int gainAmount) {
		this.specialPercentage += gainAmount;
		if (specialPercentage > 100) {
			specialPercentage = 100;
		}
	}

	/**
	 * @return the rangedAmmo
	 */
	public RangedWeaponData getRangedWeaponData() {
		return rangedWeaponData;
	}

	/**
	 * @param rangedAmmo the rangedAmmo to set
	 */
	public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
		this.rangedWeaponData = rangedWeaponData;
	}

	/**
	 * @return the weapon.
	 */
	public WeaponInterface getWeapon() {
		return weapon;
	}

	public ArrayList<Integer> walkableInterfaceList = new ArrayList<>();
	public long lastHelpRequest;
	public long lastAuthClaimed;
	public GameModes selectedGameMode;
	private boolean areCloudsSpawned;

	public boolean inFFA;
	public boolean inFFALobby;
	public boolean inCustomFFA;
	public boolean inCustomFFALobby;
	public int[] oldSkillLevels = new int[25];
	public int[] oldSkillXP = new int[25];
	public int[] oldSkillMaxLevels = new int[25];



	public void resetInterfaces() {
		walkableInterfaceList.stream().filter((i) -> !(i == 41005 || i == 41000)).forEach((i) -> {
			getPacketSender().sendWalkableInterface(i, false);
		});

		walkableInterfaceList.clear();
	}

	public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
		if (this != null && this.getPacketSender() != null) {
			if (visible) {
				if (walkableInterfaceList.contains(interfaceId)) {
					return;
				} else {
					walkableInterfaceList.add(interfaceId);
				}
			} else {
				if (!walkableInterfaceList.contains(interfaceId)) {
					return;
				} else {
					walkableInterfaceList.remove((Object) interfaceId);
				}
			}

			getPacketSender().sendWalkableInterface(interfaceId, visible);
		}
	}

	/**
	 * @param weapon the weapon to set.
	 */
	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the fightType
	 */
	public FightType getFightType() {
		return fightType;
	}

	/**
	 * @param fightType the fightType to set
	 */
	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public Bank[] getBanks() {
		return bankTabs;
	}

	public Bank getBank(int index) {
		return bankTabs[index];
	}

	public Player setBank(int index, Bank bank) {
		this.bankTabs[index] = bank;
		return this;
	}

	public boolean isAcceptAid() {
		return acceptingAid;
	}

	public void setAcceptAid(boolean acceptingAid) {
		this.acceptingAid = acceptingAid;
	}

	public Trading getTrading() {
		return trading;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
		return killsTracker;
	}

	public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
		return dropLog;
	}

	public void setWalkToTask(WalkToTask walkToTask) {
		this.walkToTask = walkToTask;
	}

	public WalkToTask getWalkToTask() {
		return walkToTask;
	}

	public Player setSpellbook(MagicSpellbook spellbook) {
		this.spellbook = spellbook;
		return this;
	}

	public MagicSpellbook getSpellbook() {
		return spellbook;
	}

	public Player setPrayerbook(Prayerbook prayerbook) {
		this.prayerbook = prayerbook;
		return this;
	}

	public Prayerbook getPrayerbook() {
		return prayerbook;
	}

	/**
	 * The player's local players list.
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * The player's local npcs list getter
	 */
	public List<NPC> getLocalNpcs() {
		return localNpcs;
	}

	public int[] skillPoints = new int[60];

	public Player setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}

	public int getInterfaceId() {
		return this.interfaceId;
	}

	public boolean isDying() {
		return isDying;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}

	public boolean isLegendary() {
		return isLegendary;
	}

	public void setLegendary(boolean isLegendary) {
		this.isLegendary = isLegendary;
	}

	public boolean isMbox1() {
		return isMbox1;
	}

	public void setMbox1(boolean isMbox1) {
		this.isMbox1 = isMbox1;
	}

	public boolean isMbox2() {
		return isMbox2;
	}

	public void setMbox2(boolean isMbox2) {
		this.isMbox2 = isMbox2;
	}

	public boolean isMbox3() {
		return isMbox3;
	}

	public void setMbox3(boolean isMbox3) {
		this.isMbox3 = isMbox3;
	}

	public boolean isMbox4() {
		return isMbox4;
	}

	public void setMbox4(boolean isMbox4) {
		this.isMbox4 = isMbox4;
	}

	public boolean isMbox5() {
		return isMbox5;
	}

	public void setMbox5(boolean isMbox5) {
		this.isMbox5 = isMbox5;
	}

	public int[] getForceMovement() {
		return forceMovement;
	}

	public Player setForceMovement(int[] forceMovement) {
		this.forceMovement = forceMovement;
		return this;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public CharacterAnimations getCharacterAnimations() {
		return characterAnimations;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
		this.characterAnimations = equipmentAnimation.clone();
	}

	public LoyaltyTitles getLoyaltyTitle() {
		return loyaltyTitle;
	}

	public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
		this.loyaltyTitle = loyaltyTitle;
	}

	public void setWalkableInterfaceId(int interfaceId2) {
		this.walkableInterfaceId = interfaceId2;
	}

	public PlayerInteractingOption getPlayerInteractingOption() {
		return playerInteractingOption;
	}

	public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
		this.playerInteractingOption = playerInteractingOption;
		return this;
	}

	public int getMultiIcon() {
		return multiIcon;
	}

	public Player setMultiIcon(int multiIcon) {
		this.multiIcon = multiIcon;
		return this;
	}

	@Setter
	@Getter
	public long fuseCombinationTimer;

	@Setter
	@Getter
	public int fuseItemSelected = 0;

	@Setter
	@Getter
	public boolean claimedFuseItem = true;

	@Setter
	@Getter
	public boolean fuseInProgress = false;

	@Setter
	@Getter
	private Stopwatch instanceTimer = new Stopwatch();

	public int getWalkableInterfaceId() {
		return walkableInterfaceId;
	}

	public boolean soundsActive() {
		return soundsActive;
	}

	public void setSoundsActive(boolean soundsActive) {
		this.soundsActive = soundsActive;
	}

	public boolean musicActive() {
		return musicActive;
	}

	public void setMusicActive(boolean musicActive) {
		this.musicActive = musicActive;
	}

	public BonusManager getBonusManager() {
		return bonusManager;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public Player setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
		return this;
	}

	public Stopwatch getLastRunRecovery() {
		return lastRunRecovery;
	}

	public Player setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public Player setResting(boolean isResting) {
		this.isResting = isResting;
		return this;
	}

	public boolean isResting() {
		return isResting;
	}

	public void setMoneyInPouch(long moneyInPouch) {
		this.moneyInPouch = moneyInPouch;
	}

	public long getMoneyInPouch() {
		return moneyInPouch;
	}

	public int getMoneyInPouchAsInt() {
		return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
	}

	public boolean experienceLocked() {
		return experienceLocked;
	}

	public void setExperienceLocked(boolean experienceLocked) {
		this.experienceLocked = experienceLocked;
	}

	public void setClientExitTaskActive(boolean clientExitTaskActive) {
		this.clientExitTaskActive = clientExitTaskActive;
	}

	public boolean isClientExitTaskActive() {
		return clientExitTaskActive;
	}

	public Player setCurrentClanChat(ClanChat clanChat) {
		this.currentClanChat = clanChat;
		return this;
	}

	public ClanChat getCurrentClanChat() {
		return currentClanChat;
	}

	public String getClanChatName() {
		return clanChatName;
	}

	public Player setClanChatName(String clanChatName) {
		this.clanChatName = clanChatName;
		return this;
	}

	public void setInputHandling(Input inputHandling) {
		this.inputHandling = inputHandling;
	}

	public Input getInputHandling() {
		return inputHandling;
	}

	public boolean isDrainingPrayer() {
		return drainingPrayer;
	}

	public void setDrainingPrayer(boolean drainingPrayer) {
		this.drainingPrayer = drainingPrayer;
	}

	   public int secondsOver = 0;
		public int damage = 0;

	public Stopwatch getDoubleDrops() {
		return doubleDrops;
	}

	public int[] getLeechedBonuses() {
		return leechedBonuses;
	}

	public Stopwatch getLastItemPickup() {
		return lastItemPickup;
	}

	public Stopwatch getLastSummon() {
		return lastSummon;
	}

	public BankSearchAttributes getBankSearchingAttribtues() {
		return bankSearchAttributes;
	}

	public AchievementAttributes getAchievementAttributes() {
		return achievementAttributes;
	}

	// public ArrayList<Integer> completedStarterTaskAttributes = new
	// ArrayList<Integer>();

	// public ArrayList<Integer> getCompletedStarterList() {
	// return completedStarterTaskAttributes;
	// }

	// public void setCompletedStarterList(ArrayList<Integer>
	// completedStarterTaskAttributes) {
	// this.completedStarterTaskAttributes = completedStarterTaskAttributes;
	// }

	public StarterTaskAttributes getStarterTaskAttributes() {
		return starterTaskAttributes;
	}

	public BankPinAttributes getBankPinAttributes() {
		return bankPinAttributes;
	}

	public int getCurrentBankTab() {
		return currentBankTab;
	}

	public Player setCurrentBankTab(int tab) {
		this.currentBankTab = tab;
		return this;
	}

	public boolean isBanking() {
		return isBanking;
	}

	public Player setBanking(boolean isBanking) {
		this.isBanking = isBanking;
		return this;
	}

	public void setNoteWithdrawal(boolean noteWithdrawal) {
		this.noteWithdrawal = noteWithdrawal;
	}

	public boolean withdrawAsNote() {
		return noteWithdrawal;
	}

	public void setSwapMode(boolean swapMode) {
		this.swapMode = swapMode;
	}

	public boolean swapMode() {
		return swapMode;
	}

	public boolean isShopping() {
		return shopping;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public Shop getShop() {
		return shop;
	}

	public Player setShop(Shop shop) {
		this.shop = shop;
		return this;
	}

	public GameObject getInteractingObject() {
		return interactingObject;
	}

	public Player setInteractingObject(GameObject interactingObject) {
		this.interactingObject = interactingObject;
		return this;
	}

	public Item getInteractingItem() {
		return interactingItem;
	}

	public void setInteractingItem(Item interactingItem) {
		this.interactingItem = interactingItem;
	}

	public Dialogue getDialogue() {
		return this.dialogue;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public int getDialogueActionId() {
		return dialogueActionId;
	}

	public void setDialogueActionId(int dialogueActionId) {
		this.dialogueActionId = dialogueActionId;
	}

	public void setSettingUpCannon(boolean settingUpCannon) {
		this.settingUpCannon = settingUpCannon;
	}

	public boolean isSettingUpCannon() {
		return settingUpCannon;
	}

	public Player setCannon(DwarfCannon cannon) {
		this.cannon = cannon;
		return this;
	}

	public DwarfCannon getCannon() {
		return cannon;
	}

	public int getOverloadPotionTimer() {
		return overloadPotionTimer;
	}

	public void setOverloadPotionTimer(int overloadPotionTimer) {
		this.overloadPotionTimer = overloadPotionTimer;
	}

	public int getPrayerRenewalPotionTimer() {
		return prayerRenewalPotionTimer;
	}

	public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
		this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
	}

	public Stopwatch getSpecialRestoreTimer() {
		return specialRestoreTimer;
	}

	public boolean[] getUnlockedLoyaltyTitles() {
		return unlockedLoyaltyTitles;
	}

	public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
		this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
	}

	public void setUnlockedLoyaltyTitle(int index) {
		unlockedLoyaltyTitles[index] = true;
	}

	public Stopwatch getEmoteDelay() {
		return emoteDelay;
	}

	public MinigameAttributes getMinigameAttributes() {
		return minigameAttributes;
	}

	public Minigame getMinigame() {
		return minigame;
	}

	public void setMinigame(Minigame minigame) {
		this.minigame = minigame;
	}

	public int getFireImmunity() {
		return fireImmunity;
	}

	public Player setFireImmunity(int fireImmunity) {
		this.fireImmunity = fireImmunity;
		return this;
	}

	public int getFireDamageModifier() {
		return fireDamageModifier;
	}

	public Player setFireDamageModifier(int fireDamageModifier) {
		this.fireDamageModifier = fireDamageModifier;
		return this;
	}

	public boolean hasVengeance() {
		return hasVengeance;
	}

	public void setHasVengeance(boolean hasVengeance) {
		this.hasVengeance = hasVengeance;
	}

	public Stopwatch getLastVengeance() {
		return lastVengeance;
	}

	public void setHouseRooms(Room[][][] houseRooms) {
		this.houseRooms = houseRooms;
	}

	public void setHousePortals(ArrayList<Portal> housePortals) {
		this.housePortals = housePortals;
	}

	/*
	 * Construction instancing Arlania
	 */

	private boolean hideBot = false;

	public void hideBot() {
		hideBot = (!hideBot);
	}


	public boolean isVisible() {
		if (isMiniMe && hideBot) {
			return false;
		}
		if (getLocation() == Locations.Location.CONSTRUCTION) {
			return false;
		}
		return true;
	}

	public void setHouseFurtinture(ArrayList<HouseFurniture> houseFurniture) {
		this.houseFurniture = houseFurniture;
	}

	public Stopwatch getTolerance() {
		return tolerance;
	}

	public boolean isTargeted() {
		return targeted;
	}

	public void setTargeted(boolean targeted) {
		this.targeted = targeted;
	}

	public Stopwatch getLastYell() {
		return lastYell;
	}

	public Stopwatch getLastZulrah() {
		return lastZulrah;
	}

	public Stopwatch getLastSql() {
		return lastSql;
	}

	public int getAmountDonated() {
		return amountDonated;
	}

	public void incrementAmountDonated(int amountDonated) {
		this.amountDonated += amountDonated;
	}

	public long getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(long amount) {
		this.totalPlayTime = amount;
	}

	public Stopwatch getRecordedLogin() {
		return recordedLogin;
	}

	public Player setRegionChange(boolean regionChange) {
		this.regionChange = regionChange;
		return this;
	}

	public boolean isChangingRegion() {
		return this.regionChange;
	}

	public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
		this.allowRegionChangePacket = allowRegionChangePacket;
	}

	public boolean isAllowRegionChangePacket() {
		return allowRegionChangePacket;
	}

	public boolean isKillsTrackerOpen() {
		return killsTrackerOpen;
	}

	public void setKillsTrackerOpen(boolean killsTrackerOpen) {
		this.killsTrackerOpen = killsTrackerOpen;
	}

	public boolean isCoughing() {
		return isCoughing;
	}

	public void setCoughing(boolean isCoughing) {
		this.isCoughing = isCoughing;
	}

	public int getShadowState() {
		return shadowState;
	}

	public void setShadowState(int shadow) {
		this.shadowState = shadow;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public boolean isPlayerLocked() {
		return playerLocked;
	}

	public Player setPlayerLocked(boolean playerLocked) {
		this.playerLocked = playerLocked;
		return this;
	}

	private MysteryBoxOpener mysteryBoxOpener = new MysteryBoxOpener(this);

	public MysteryBoxOpener getMysteryBoxOpener() {
		return mysteryBoxOpener;
	}

	public Stopwatch getSqlTimer() {
		return sqlTimer;
	}

	public Stopwatch getFoodTimer() {
		return foodTimer;
	}

	public Stopwatch getPotionTimer() {
		return potionTimer;
	}

	public Item getUntradeableDropItem() {
		return untradeableDropItem;
	}

	public void setUntradeableDropItem(Item untradeableDropItem) {
		this.untradeableDropItem = untradeableDropItem;
	}

	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
	}

	public CombatType getLastCombatType() {
		return lastCombatType;
	}

	public void setLastCombatType(CombatType lastCombatType) {
		this.lastCombatType = lastCombatType;
	}

	public int getEffigy() {
		return this.effigy;
	}

	public void setEffigy(int effigy) {
		this.effigy = effigy;
	}

	public int getDfsCharges() {
		return dfsCharges;
	}

	public void incrementDfsCharges(int amount) {
		this.dfsCharges += amount;
	}

	public void setNewPlayer(boolean newPlayer) {
		this.newPlayer = newPlayer;
	}

	public boolean newPlayer() {
		return newPlayer;
	}

	public Stopwatch getLogoutTimer() {
		return lougoutTimer;
	}

	public Player setUsableObject(Object[] usableObject) {
		this.usableObject = usableObject;
		return this;
	}

	public Player setUsableObject(int index, Object usableObject) {
		this.usableObject[index] = usableObject;
		return this;
	}

	public Object[] getUsableObject() {
		return usableObject;
	}

	public int getPlayerViewingIndex() {
		return playerViewingIndex;
	}

	public void setPlayerViewingIndex(int playerViewingIndex) {
		this.playerViewingIndex = playerViewingIndex;
	}

	public boolean hasStaffOfLightEffect() {
		return staffOfLightEffect > 0;
	}

	public int getStaffOfLightEffect() {
		return staffOfLightEffect;
	}

	public void setStaffOfLightEffect(int staffOfLightEffect) {
		this.staffOfLightEffect = staffOfLightEffect;
	}

	public void decrementStaffOfLightEffect() {
		this.staffOfLightEffect--;
	}

	public boolean openBank() {
		return openBank;
	}

	public void setOpenBank(boolean openBank) {
		this.openBank = openBank;
	}

	public int getMinutesBonusExp() {
		return minutesBonusExp;
	}

	public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
		this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
	}

	public void setInactive(boolean inActive) {
		this.inActive = inActive;
	}

	public boolean isInActive() {
		return inActive;
	}

	public int getSelectedGeItem() {
		return selectedGeItem;
	}

	public void setSelectedGeItem(int selectedGeItem) {
		this.selectedGeItem = selectedGeItem;
	}

	public int getGeQuantity() {
		return geQuantity;
	}

	public void setGeQuantity(int geQuantity) {
		this.geQuantity = geQuantity;
	}

	public int getGePricePerItem() {
		return gePricePerItem;
	}

	public void setGePricePerItem(int gePricePerItem) {
		this.gePricePerItem = gePricePerItem;
	}

	public GrandExchangeSlot[] getGrandExchangeSlots() {
		return grandExchangeSlots;
	}

	public void setGrandExchangeSlots(GrandExchangeSlot[] GrandExchangeSlots) {
		this.grandExchangeSlots = GrandExchangeSlots;
	}

	public void setGrandExchangeSlot(int index, GrandExchangeSlot state) {
		this.grandExchangeSlots[index] = state;
	}

	public void setSelectedGeSlot(int slot) {
		this.selectedGeSlot = slot;
	}

	public int getSelectedGeSlot() {
		return selectedGeSlot;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	@Getter
	@Setter
	public boolean afkSkilling;

	public int getSelectedSkillingItem() {
		return selectedSkillingItem;
	}

	public void setSelectedSkillingItem(int selectedItem) {
		this.selectedSkillingItem = selectedItem;
	}

	public boolean shouldProcessFarming() {
		return processFarming;
	}

	public void setProcessFarming(boolean processFarming) {
		this.processFarming = processFarming;
	}

	public Pouch getSelectedPouch() {
		return selectedPouch;
	}

	public void setSelectedPouch(Pouch selectedPouch) {
		this.selectedPouch = selectedPouch;
	}

	public int getCurrentBookPage() {
		return currentBookPage;
	}

	public void setCurrentBookPage(int currentBookPage) {
		this.currentBookPage = currentBookPage;
	}

	public int getStoredRuneEssence() {
		return storedRuneEssence;
	}

	public void setStoredRuneEssence(int storedRuneEssence) {
		this.storedRuneEssence = storedRuneEssence;
	}

	public int getStoredPureEssence() {
		return storedPureEssence;
	}

	public void setStoredPureEssence(int storedPureEssence) {
		this.storedPureEssence = storedPureEssence;
	}

	public int getTrapsLaid() {
		return trapsLaid;
	}

	public void setTrapsLaid(int trapsLaid) {
		this.trapsLaid = trapsLaid;
	}

	public boolean isCrossingObstacle() {
		return crossingObstacle;
	}

	public Player setCrossingObstacle(boolean crossingObstacle) {
		this.crossingObstacle = crossingObstacle;
		return this;
	}

	public boolean[] getCrossedObstacles() {
		return crossedObstacles;
	}

	public boolean getCrossedObstacle(int i) {
		return crossedObstacles[i];
	}

	public Player setCrossedObstacle(int i, boolean completed) {
		crossedObstacles[i] = completed;
		return this;
	}

	public void setCrossedObstacles(boolean[] crossedObstacles) {
		this.crossedObstacles = crossedObstacles;
	}

	public int getSkillAnimation() {
		return skillAnimation;
	}

	public Player setSkillAnimation(int animation) {
		this.skillAnimation = animation;
		return this;
	}

	public int[] getOres() {
		return ores;
	}

	public void setOres(int[] ores) {
		this.ores = ores;
	}

	public void setResetPosition(Position resetPosition) {
		this.resetPosition = resetPosition;
	}

	public Position getResetPosition() {
		return resetPosition;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	public Farming getFarming() {
		return farming;
	}
	public boolean isSendElementalMessage() {
		return sendElementalMessage;
	}
	public void setSendElementalMessage(boolean elemental) {
		this.sendElementalMessage = elemental;
	}

	public boolean inConstructionDungeon() {
		return inConstructionDungeon;
	}

	public void setInConstructionDungeon(boolean inConstructionDungeon) {
		this.inConstructionDungeon = inConstructionDungeon;
	}

	public int getHouseServant() {
		return houseServant;
	}

	public HouseLocation getHouseLocation() {
		return houseLocation;
	}

	public HouseTheme getHouseTheme() {
		return houseTheme;
	}

	public void setHouseTheme(HouseTheme houseTheme) {
		this.houseTheme = houseTheme;
	}

	public void setHouseLocation(HouseLocation houseLocation) {
		this.houseLocation = houseLocation;
	}

	public void setHouseServant(int houseServant) {
		this.houseServant = houseServant;
	}

	public int getHouseServantCharges() {
		return this.houseServantCharges;
	}

	public void setHouseServantCharges(int houseServantCharges) {
		this.houseServantCharges = houseServantCharges;
	}

	public void incrementHouseServantCharges() {
		this.houseServantCharges++;
	}

	public int getServantItemFetch() {
		return servantItemFetch;
	}

	public void setServantItemFetch(int servantItemFetch) {
		this.servantItemFetch = servantItemFetch;
	}

	public int getPortalSelected() {
		return portalSelected;
	}

	public void setPortalSelected(int portalSelected) {
		this.portalSelected = portalSelected;
	}

	public boolean isBuildingMode() {
		return this.isBuildingMode;
	}

	public void setIsBuildingMode(boolean isBuildingMode) {
		this.isBuildingMode = isBuildingMode;
	}

	public int[] getConstructionCoords() {
		return constructionCoords;
	}

	public void setConstructionCoords(int[] constructionCoords) {
		this.constructionCoords = constructionCoords;
	}

	public int getBuildFurnitureId() {
		return this.buildFurnitureId;
	}

	public void setBuildFuritureId(int buildFuritureId) {
		this.buildFurnitureId = buildFuritureId;
	}

	public int getBuildFurnitureX() {
		return this.buildFurnitureX;
	}

	public void setBuildFurnitureX(int buildFurnitureX) {
		this.buildFurnitureX = buildFurnitureX;
	}

	public int getBuildFurnitureY() {
		return this.buildFurnitureY;
	}

	public void setBuildFurnitureY(int buildFurnitureY) {
		this.buildFurnitureY = buildFurnitureY;
	}

	public int getCombatRingType() {
		return this.combatRingType;
	}

	public void setCombatRingType(int combatRingType) {
		this.combatRingType = combatRingType;
	}

	public Room[][][] getHouseRooms() {
		return houseRooms;
	}

	public ArrayList<Portal> getHousePortals() {
		return housePortals;
	}

	public ArrayList<HouseFurniture> getHouseFurniture() {
		return houseFurniture;
	}

	public int getConstructionInterface() {
		return this.constructionInterface;
	}

	public void setConstructionInterface(int constructionInterface) {
		this.constructionInterface = constructionInterface;
	}

	public int getRecoilCharges() {
		return this.recoilCharges;
	}

	public int setRecoilCharges(int recoilCharges) {
		return this.recoilCharges = recoilCharges;
	}

	public int[] getBrawlerChargers() {
		return this.brawlerCharges;
	}

	public void setBrawlerCharges(int[] brawlerCharges) {
		this.brawlerCharges = brawlerCharges;
	}

	public int[] getVoidTopChargers() {
		return this.voidTopCharges;
	}

	public void setVoidTopCharges(int[] voidTopCharges) {
		this.voidTopCharges = voidTopCharges;
	}

	public int[] getVoidLegChargers() {
		return this.voidLegCharges;
	}

	public void setVoidLegCharges(int[] voidLegCharges) {
		this.voidLegCharges = voidLegCharges;
	}

	public boolean voteMessageSent() {
		return this.voteMessageSent;
	}

	public void setVoteMessageSent(boolean voteMessageSent) {
		this.voteMessageSent = voteMessageSent;
	}

	public boolean didReceiveStarter() {
		return receivedStarter;
	}

	public void sendMessage(String string) {
		packetSender.sendMessage(string);
	}

	public void setReceivedStarter(boolean receivedStarter) {
		this.receivedStarter = receivedStarter;
	}

	public BlowpipeLoading getBlowpipeLoading() {
		return blowpipeLoading;
	}

	public boolean cloudsSpawned() {
		return areCloudsSpawned;
	}

	public void setCloudsSpawned(boolean cloudsSpawned) {
		this.areCloudsSpawned = cloudsSpawned;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isShopUpdated() {
		return shopUpdated;
	}

	public void setShopUpdated(boolean shopUpdated) {
		this.shopUpdated = shopUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void write(Packet packet) {
		// TODO Auto-generated method stub

	}

	public void datarsps(Player player, String username2) {
		// TODO Auto-generated method stub

	}

	public String macAddress;

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getMacAddress() {
		// TODO Auto-generated method stub
		return macAddress;
	}

	public Zulrah zulrah = new Zulrah(this);
	public Sagittare sagittare = new Sagittare(this);
	public int ZULRAH_CLICKS = 0;
	private long bestZulrahTime;

	public CustomBossInstance customBossInstance;

	public CustomBossInstance getCustomBoss() {
		return customBossInstance;
	}

	public void setCustomBoss(CustomBossInstance customBossInstance) {
		this.customBossInstance = customBossInstance;
	}

	public Zulrah getZulrahEvent() {
		return zulrah;
	}

	public Sagittare getSagittareEvent() {
		return sagittare;
	}

	public long setBestZulrahTime(long bestZulrahTime) {
		return this.bestZulrahTime = bestZulrahTime;
	}

	public long getBestZulrahTime() {
		return bestZulrahTime;
	}

	private boolean placeholders = false;

	public boolean isPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(boolean placeholders) {
		this.placeholders = placeholders;
	}

	public Position selectedPos = null;

	public Position getSelectedPosition() {
		return selectedPos;
	}

	public void setSelectedPosition(Position selectedPos) {
		this.selectedPos = selectedPos;
	}

	public BestItems getBestItems() {
		return this.bestItems;
	}

	public CollectionLogInterface getCollectionLog() {
		return this.collectionLog;
	}

	public List<CollectionEntry> getCollectionLogData() {
		return this.collectionLogData;
	}

	public int getCurrentBossWave() {
		return this.currentBossWave;
	}

	public int getBarrowsKC() {
		return this.barrowsKC;
	}

	public boolean isPlayedNewBarrows() {
		return this.playedNewBarrows;
	}

	public boolean isUsedBossTeleport() {
		return this.usedBossTeleport;
	}

	public boolean isShouldGiveBossReward() {
		return this.shouldGiveBossReward;
	}

    private enum KeyTypes {

		ROOM_1("Beginner key", 1279, 2832, 100), ROOM_2("Decent key", 1290, 2834, 100),
		ROOM_3("Best Key", 742, 2836, 100);

		public String itemName;

		public int npcId, keyId;

		public double dropChance;

		KeyTypes(String itemName, int npcId, int keyId, double dropChance) {
			this.itemName = itemName;
			this.npcId = npcId;
			this.keyId = keyId;
			this.dropChance = dropChance;
		}

	}

	public static KeyTypes get(int npcId) {
		for (KeyTypes i : KeyTypes.values()) {
			if (i.npcId == npcId)
				return i;
		}
		return null;
	}

	public int keyCount1, keyCount2, keyCount3;

	public int savedKeyCount1 = 0, savedKeyCount2 = 0, savedKeyCount3 = 0;

	public KeyRoom keyRoom;

	public long lastOpPotion;

	public long lastDonationClaim;

	public long lastHpRestore;

	public long lastPrayerSwitch;

	public long lastPrayerRestore;

	public long lastCashClaim;

	public long lastSpecialClaim;

	public long lastSpecialRestoreClaim;

	public void handleKeyRates(Player killer, NPC npc) {

		if (npc == null || killer == null)
			return;

		KeyTypes keyData = get(npc.getId());

		if (keyData == null)
			return;

		if (npc.getId() == 1279)
			this.keyCount1++;
		else if (npc.getId() == 1290)
			this.keyCount2++;
		else if (npc.getId() == 742)
			this.keyCount3++;

		if (this.getKeyRoom() != null)
			this.getKeyRoom().count--;

		double percent = Math.random() * 100;

		if (percent <= keyData.dropChance) {
			killer.sendMessage("A " + keyData.itemName + " has been added to your inventory.");

			int necklaceId = killer.getEquipment().get(Equipment.AMULET_SLOT).getId();
			int freeSpaces = killer.getInventory().getFreeSlots();

			if (necklaceId == 19886 && freeSpaces > 0) {

				killer.getInventory().add(keyData.keyId, 1);
			} else {

				killer.sendMessage("A " + keyData.itemName + " has been dropped below the monster.");
				GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(keyData.keyId, 1), npc.getPosition(),
						killer.getUsername(), false, 150, true, 200));
			}
		}
	}

	/**
	 * @param keyRoom2
	 */
	public void setKeyRoom(KeyRoom keyRoom) {
		this.keyRoom = keyRoom;
	}

	public KeyRoom getKeyRoom() {
		return keyRoom;
	}

	/**
	 *
	 */
	public void endKeyRoom(boolean fromUpdate) {
		if (this.getKeyRoom() != null) {
			this.getKeyRoom().finishRoom(fromUpdate);
		}
	}

	public void endCustomBossRoom() {
		if (this.getCustomBoss() != null) {
			this.getCustomBoss().finishRoom();
		}
	}

	public String getLongDurationTimer(long ms) {
		ms = ms - System.currentTimeMillis();
		long minutes = (int) Math.ceil((ms / 1000) / 60);
		if (minutes >= 60) {
			long hours = minutes / 60;
			long remainder = minutes % 60;
			if (remainder == 0)
				return hours + " Hours";
			return hours + " Hours " + remainder + " Minutes";
		}
		return minutes + " Minutes";
	}

	public String getTimeRemaining(long totalTime) {
		/***
		 * Only used for under 1hrs
		 */
		return "(<col=258324>" + Misc.convertMsToTime(totalTime - System.currentTimeMillis()) + "</col>" + ")";
	}

	public void giveItem(int itemId, int itemAmount) {

		final ItemDefinition definition = ItemDefinition.forId(itemId);

		if (definition == null) {
			sendMessage("@red@[Error]: Could not find definition (" + itemId + "-" + itemAmount + ")");
			sendMessage("@red@Please take a screenshot and post it on the forums.");
			return;
		}

		final int occupiedSlots = definition.isNoted() || definition.isStackable() ? 1 : itemAmount;

		if (inventory.getFreeSlots() >= occupiedSlots) {
			inventory.add(itemId, itemAmount).refreshItems();
		} else if (bank.getFreeSlots() >= occupiedSlots) {
			boolean added = false;
			for (Bank bank : getBanks()) {
				if (!added && !Bank.isEmpty(bank)) {
					bank.add(itemId, itemAmount).refreshItems();
					added = true;
				}
			}
		} else {
			sendMessage("@red@[Error]: Could not give (" + itemId + "-" + itemAmount + ")");
			sendMessage("@red@Please take a screenshot and post it on the forums.");
			World.sendStaffMessage("@red@[Error]: Could not give (" + itemId + "-" + itemAmount + ") to " + username);
		}
	}

	public boolean busy1() {
		return interfaceId > 0 || isBanking || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
	}

	private ArrayList<OldRaidParty> pendingOldRaidPartyInvites = new ArrayList<OldRaidParty>();

	public ArrayList<OldRaidParty> getRaidPartyInvites() {
		return pendingOldRaidPartyInvites;
	}

	public void addPendingRaidParty(OldRaidParty party) {
		if (pendingOldRaidPartyInvites.contains(party))
			return;
		pendingOldRaidPartyInvites.add(party);
	}

	public void removeRaidPartyInvite(OldRaidParty party) {
		if (pendingOldRaidPartyInvites.contains(party)) {
			pendingOldRaidPartyInvites.remove(party);
		}
	}

	private OldRaidParty oldRaidParty;

	public void setOldRaidParty(OldRaidParty oldRaidParty) {
		this.oldRaidParty = oldRaidParty;
	}

	public OldRaidParty getOldRaidParty() {
		return oldRaidParty;
	}

	public Object getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateMoneyPouch() {
		getPacketSender().sendString(8135, "" + getMoneyInPouch() + "");
	}

	public void addMoneyToPouch(long l) {
		moneyInPouch += l;
		this.updateMoneyPouch();
	}

	public boolean getGodPotionStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setGodPotionStatus(int i) {
		// TODO Auto-generated method stub

	}

	public void setGodPotionStatus(boolean b) {
		// TODO Auto-generated method stub

	}

	public void setBankHolder(Object object) {
		// TODO Auto-generated method stub

	}


	public void setNpcKills(int npcKills) {
		this.npcKills = npcKills;
	}

	private int npcKills;

	public int getNpcKills() {
		return npcKills;
	}

	private int itemToUpgrade;

	public int getItemToUpgrade() {
		return itemToUpgrade;
	}

	public void setItemToUpgrade(int itemId) {
		this.itemToUpgrade = itemId;
	}

	private int itemToDissolve;

	public int getItemToDissolve() {
		return itemToDissolve;
	}

	public void setItemToDissolve(int itemId) {
		this.itemToDissolve = itemId;
	}

	public void incrementNPCKills(int amount) {
		this.npcKills += amount;
	}

	public void setNPCKills(int NPCKills) {
		this.npcKills = NPCKills;
	}

	private final PartyRoomManager partyRoom = new PartyRoomManager(this);

	public PartyRoomManager getPartyRoom() {
		return partyRoom;
	}

	/**
	 * Progression Manager
	 */

	private ProgressionManager progressionManager = new ProgressionManager(this);

	public boolean dropMessageToggle = true;


	public ProgressionManager getProgressionManager() {
		return progressionManager;
	}

	public void setForceLogout(boolean b) {
		World.deregister(null);

		// TODO Auto-generated method stub

	}

}
