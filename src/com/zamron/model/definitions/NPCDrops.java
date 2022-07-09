package com.zamron.model.definitions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zamron.GameSettings;
import com.zamron.engine.task.impl.CleansingTask;
import com.zamron.model.*;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Bank;
import com.zamron.util.JsonLoader;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.*;
import com.zamron.world.content.DropLog.DropLogEntry;
import com.zamron.world.content.aoesystem.AOESystem;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.collectionlog.CollectionEntry;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.content.minigames.impl.WarriorsGuild;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.skill.impl.prayer.BonesData;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.content.skill.impl.summoning.CharmingImp;
import com.zamron.world.content.skill.impl.summoning.Familiar;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import java.security.SecureRandom;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controls the npc drops
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 *         Samy
 *
 */
public class NPCDrops { //LOL

	/**
	 * The map containing all the npc drops.
	 */
	private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

	public static JsonLoader parseDrops() {
		// //System.out.println("CALLED");
		ItemDropAnnouncer.init();

		return new JsonLoader() {

			@Override
			public void load(JsonObject reader, Gson builder) {
				int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
				NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);

				NPCDrops d = new NPCDrops();
				d.npcIds = npcIds;
				d.drops = drops;
				for (int id : npcIds) {
					dropControllers.put(id, d);
					// //System.out.println("Drops put: " + id + " . " + d);
					// //System.out.println("put: "+id+" "+d);
				}

			}

			@Override
			public String filePath() {
				return "./data/def/json/drops.json";
			}
		};
	}

	/**
	 * The id's of the NPC's that "owns" this class.
	 */
	private int[] npcIds;

	/**
	 * All the drops that belongs to this class.
	 */
	private NpcDropItem[] drops;


	/**
	 * Gets the NPC drop controller by an id.
	 *
	 * @return The NPC drops associated with this id.
	 */
	public static NPCDrops forId(int id) {
		return dropControllers.get(id);
	}

	public static Map<Integer, NPCDrops> getDrops() {
		return dropControllers;
	}

	/**
	 * Gets the drop list
	 *
	 * @return the list
	 */
	public NpcDropItem[] getDropList() {
		return drops;
	}

	/**
	 * Gets the npcIds
	 *
	 * @return the npcIds
	 */
	public int[] getNpcIds() {
		return npcIds;
	}

	/**
	 * Represents a npc drop item
	 */
	public static class NpcDropItem {

		/**
		 * The id.
		 */
		private final int id;

		/**
		 * Array holding all the amounts of this item.
		 */
		private final int[] count;

		/**
		 * The chance of getting this item.
		 */
		private final int chance;

		/**
		 * New npc drop item
		 *
		 * @param id     the item
		 * @param count  the count
		 * @param chance the chance
		 */
		public NpcDropItem(int id, int[] count, int chance) {
			this.id = id;
			this.count = count;
			this.chance = chance;
		}

		/**
		 * Gets the item id.
		 *
		 * @return The item id.
		 */
		public int getId() {
			return id;
		}

		public final int PLAT_TOKEN = 10835;

		/**
		 * Gets the chance.
		 *
		 * @return The chance.
		 */
		public int[] getCount() {
			return count;
		}


		static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

			List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

			Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {

				@Override
				public int compare(Entry<K, V> e1, Entry<K, V> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}

			});

			return sortedEntries;

		}

		/**
		 * Gets the chance.
		 *
		 * @return The chance.
		 */
		public DropChance getChance() {
			switch (chance) {
				case 1:
					return DropChance.ALMOST_ALWAYS; // 50% <-> 1/2
				case 2:
					return DropChance.VERY_COMMON; // 20% <-> 1/5
				case 3:
					return DropChance.COMMON; // 5% <-> 1/20
				case 4:
					return DropChance.UNCOMMON; // 2% <-> 1/50
				case 5:
					return DropChance.RARE; // 0.5% <-> 1/200
				case 6:
					return DropChance.LEGENDARY; // 0.2% <-> 1/500
				case 7:
					return DropChance.LEGENDARY_2;
				case 8:
					return DropChance.LEGENDARY_3;
				case 9:
					return DropChance.LEGENDARY_4;
				case 10:
					return DropChance.LEGENDARY_5;
				case 11:
					return DropChance.LEGENDARY_6;
				case 12:
					return DropChance.LEGENDARY_7;
				default:
					return DropChance.ALWAYS; // 100% <-> 1/1
			}
		}

		public WellChance getWellChance() {
			switch (chance) {
				case 1:
					return WellChance.ALMOST_ALWAYS; // 50% <-> 1/2
				case 2:
					return WellChance.VERY_COMMON; // 20% <-> 1/5
				case 3:
					return WellChance.COMMON; // 5% <-> 1/20
				case 4:
					return WellChance.UNCOMMON; // 2% <-> 1/50
				case 5:
					return WellChance.RARE; // 0.5% <-> 1/200
				case 6:
					return WellChance.LEGENDARY; // 0.2% <-> 1/500
				case 7:
					return WellChance.LEGENDARY_2;
				case 8:
					return WellChance.LEGENDARY_3;
				case 9:
					return WellChance.LEGENDARY_4;
				case 10:
					return WellChance.LEGENDARY_5;
				default:
					return WellChance.ALWAYS; // 100% <-> 1/1
			}
		}

		/**
		 * Gets the item
		 *
		 * @return the item
		 */
		public Item getItem() {
			int amount = 0;
			for (int i = 0; i < count.length; i++)
				amount += count[i];
			if (amount > count[0])
				amount = count[0] + RandomUtility.getRandom(count[1]);
			return new Item(id, amount);
		}
	}

	public enum DropChance {
		ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(5), COMMON(15), UNCOMMON(40), NOTTHATRARE(75), RARE(125),
		LEGENDARY(150), LEGENDARY_2(900), LEGENDARY_3(1500), LEGENDARY_4(2000), LEGENDARY_5(3500), LEGENDARY_6(5000), LEGENDARY_7(7000);

		DropChance(int randomModifier) {
			this.random = randomModifier;
		}

		private int random;

		public int getRandom() {
			return this.random;
		}
	}

	public enum WellChance {
		ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(3), COMMON(8), UNCOMMON(20), NOTTHATRARE(50), RARE(76), LEGENDARY(160),
		LEGENDARY_2(205), LEGENDARY_3(425), LEGENDARY_4(340), LEGENDARY_5(450);

		WellChance(int randomModifier) {
			this.random = randomModifier;
		}

		private int random;

		public int getRandom() {
			return this.random;
		}
	}

	/**
	 * Drops items for a player after killing an npc. A player can max receive one
	 * item per drop chance.
	 *
	 * @param p   Player to receive drop.
	 * @param npc NPC to receive drop FROM.
	 */
	public static void dropItems(Player p, NPC npc) {
		if (npc.getLocation() == Location.WARRIORS_GUILD)
			WarriorsGuild.handleDrop(p, npc);
		NPCDrops drops = NPCDrops.forId(npc.getId());
		if (drops == null)
			return;
		if (npc.getLocation() == Location.RAIDS)
			return;


		SecureRandom random = new SecureRandom();
		int randomInt = random.nextInt(5000);
		int randomPts = Misc.random(1, 10);
		if (randomInt == 1 && !p.getLocation().equals(Location.BOSS_TIER_LOCATION)) {
			p.getPointsHandler().incrementDonationPoints(randomPts);
			p.sendMessage("@red@You've received " + randomPts + " donation points!");
			World.sendMessageDiscord(p.getUsername() + " has just received " + randomPts + " donator points from " + npc.getDefinition().getName() + "!");
		}

		final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
		final Position npcPos = npc.getPosition().copy();
		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {

			casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
		}
		if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4) {
			clueDrop(p, npc.getDefinition().getCombatLevel(), npcPos);

		}

		if (npc.getDefaultConstitution() > 10000) {
			dropScratchcard(p, p.getPosition());
		}

		rollDropTable(false, p, drops.getDropList().clone(), npc, npcPos, goGlobal);

		if (WellOfWealth.isActive())
			rollDropTable(true, p, drops.getDropList(), npc, npcPos, goGlobal);

	}

	public static Collection<Integer> getNpcList(String name) {
		List<Integer> items = new ArrayList<>();
		for (int index = 0; index < ItemDefinition.getDefinitions().length; index++) {
			ItemDefinition def = ItemDefinition.forId(index);
			if (def == null || !def.getName().toLowerCase().equals(name)) {
				continue;
			}
			items.add(def.getId());
			break;
		}
		if (items.isEmpty()) {
			//System.out.println("No such item.");
			return new ArrayList<>();
		}
		Collection<Integer> npcs = new ArrayList<>();
		for (NPCDrops npc_drop : dropControllers.values()) {
			for (NpcDropItem dropped_item : npc_drop.drops) {
				for (Integer cached_item : items) {
					if (dropped_item.getId() == cached_item) {
						for (int npc_id : npc_drop.getNpcIds()) {
							if (npcs.contains(npc_id)) {
								continue;
							}
							npcs.add(npc_id);
						}
						continue;
					}
				}
			}
		}
		return npcs;
	}

	public static void rollDropTable(boolean isWell, Player player, NpcDropItem[] drops, NPC npc, Position npcPos,
									 boolean goGlobal) {
		boolean hasRecievedDrop = false;
		int playerDr = DropUtils.drBonus(player);

		//System.out.println("Player dr: " + playerDr);

		if (npc.getId() == HourlyBoss.currentHourlyBoss) {
			playerDr += 20;
		}

		//System.out.println("Player dr after: " + playerDr);

		Arrays.sort(drops, (a, b) -> b.getChance().compareTo(a.getChance()));

		for (int i = 0; i < drops.length; i++) {
			int chance = isWell ? drops[i].getWellChance().getRandom() : drops[i].getChance().getRandom();
			int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player) / 100.0) + 1 : 1))
					+ (playerDr > 0 ? 1 : 0);

			// //System.out.println("Item: " + drops[i].getItem().getDefinition().getName() +
			// "x" + drops[i].getItem().getAmount() + " chance: " + chance + " adj chance: "
			// + adjustedDr);

			if (drops[i].getChance() == DropChance.ALWAYS || adjustedDr == 1) {
				Item drop = drops[i].getItem();
				if (GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.DOUBLE_DROPS || player.isDoubleDropsActive()) {
					drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
					drop(player, drop, npc, npcPos, goGlobal);
					System.out.println("Double Drops Perk / Double drop scroll <3: " + drop.getId() + ", " + drop.getAmount());
				}
			} else if (RandomUtility.getRandom(adjustedDr) == 1 && !hasRecievedDrop) {
				Item drop = drops[i].getItem();
				if (player.isDoubleDropsActive())
					drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
				drop(player, drop, npc, npcPos, goGlobal);
				System.out.println("Normal Drop: " + drop.getId() + ", " + drop.getAmount());
				hasRecievedDrop = true;
			}

		}

	}

	public static int getDropAmntMult(Player player) {

		int multiplier = 1;

		int random = Misc.random(100);
		int chance = 0;

		switch (player.getRights()) {
			case DONATOR:
				chance = 1;
				break;
			case SUPER_DONATOR:
				chance = 3;
				break;
			case EXTREME_DONATOR:
				chance = 5;
				break;
			case LEGENDARY_DONATOR:
				chance = 8;
				break;
			case UBER_DONATOR:
				chance = 10;
				break;
			case DELUXE_DONATOR:
				chance = 15;
				break;
			case VIP_DONATOR:
				chance = 20;
				break;
		}
		switch (player.getSecondaryPlayerRights()) {
			case DONATOR:
				chance = 1;
				break;
			case SUPER_DONATOR:
				chance = 3;
				break;
			case EXTREME_DONATOR:
				chance = 5;
				break;
			case LEGENDARY_DONATOR:
				chance = 8;
				break;
			case UBER_DONATOR:
				chance = 10;
				break;
			case DELUXE_DONATOR:
				chance = 15;
				break;
		}
		if (random <= chance) {
			multiplier++;
		}
		return multiplier;
	}

	public static double getDroprate(Player p, boolean display) {
		double drBoost = 0;

		if (p.getGameMode() == GameMode.IRONMAN) {
			drBoost += 5;
		}
		if (p.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			drBoost += 15;
		}
		if (p.getGameMode() == GameMode.GROUP_IRONMAN) {
			drBoost += 10;
		}
		return drBoost;
	}

	public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {

		if (npc.getId() == 227 || npc.getId() == 2043 || npc.getId() == 2044) {
			pos = player.getPosition();
		}
		if (player.getInventory().contains(18337)
				&& BonesData.forId(item.getId()) != null) {
			player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
			player.getSkillManager().addExperience(Skill.PRAYER,
					BonesData.forId(item.getId()).getBuryingXP());
			return;
		}

		player.handleCollectedItem(npc.getId(), item);

		boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);
		boolean isWearingCollectorUpgraded = DropUtils.hasUpgradedCOLL(player);
		boolean dropPerks = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.DOUBLE_DROPS;
		boolean scrollDrop = player.getCleansingTime() > 100;

		Familiar pet = player.getSummoning().getFamiliar();
		if (pet != null && PetPerkData.hasLootEffect(pet.getSummonNpc().getId())) {
			if (dropPerks || scrollDrop) {
				player.getBank(0).add(item.getId(), item.getAmount() * 2);
				if (player.getGameMode() == GameMode.GROUP_IRONMAN)
					player.getGroupIronmanGroup().addItem(player, item.getId(), item.getAmount() * 2);
				player.sendMessage("Added " + item.getDefinition().getName() + " to your bank because of pet perk.");
				return;
			}
			player.getBank(0).add(item.getId(), item.getAmount());
			if (player.getGameMode() == GameMode.GROUP_IRONMAN)
				player.getGroupIronmanGroup().addItem(player, item.getId(), item.getAmount());
			System.out.println("TRIED TO CATCH ?");
			player.sendMessage("Added " + item.getDefinition().getName() + " to your bank because of pet perk.");
			return;
		}
		if (isWearingCollectorUpgraded && !player.getBlockedCollectorsList().contains(item.getId())) {
			if (dropPerks || scrollDrop) {
				player.getBank(0).add(item.getId(), item.getAmount() * 2);
				if (player.getGameMode() == GameMode.GROUP_IRONMAN)
					player.getGroupIronmanGroup().addItem(player, item.getId(), item.getAmount() * 2);

				player.sendMessage("@red@Your Collector has picked up @blu@" + item.getAmount() * 2 + "x "
						+ item.getDefinition().getName() + " @red@and added them to your bank!");
				return;
			}
			player.getBank(0).add(item.getId(), item.getAmount());
			if (player.getGameMode() == GameMode.GROUP_IRONMAN)
				player.getGroupIronmanGroup().addItem(player, item.getId(), item.getAmount());

			player.sendMessage("@red@Your Collector has picked up @blu@" + item.getAmount() + "x "
					+ item.getDefinition().getName() + " @red@and added them to your bank!");
			return;
		}
		if (isWearingCollector && !player.getBlockedCollectorsList().contains(item.getId())) {
			if (dropPerks || scrollDrop) {
				player.getInventory().add(item.getId(), item.getAmount() * 2);
				player.sendMessage("@red@Your Collector has picked up @blu@" + item.getAmount() * 2+ "x "
						+ item.getDefinition().getName() + " @red@and added them to your inventory!");
				return;
			}
			player.getInventory().add(item.getId(), item.getAmount());
			player.sendMessage("@red@Your Collector has picked up @blu@" + item.getAmount() + "x "
					+ item.getDefinition().getName() + " @red@and added them to your inventory!");
			return;
		}
		if (dropPerks || scrollDrop) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(item, pos, player.getUsername(), false, 150, goGlobal, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(item, pos, player.getUsername(), false, 150, goGlobal, 200));
			return;
		}
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(item, pos, player.getUsername(), false, 150, goGlobal, 200));

		if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
			player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
			player.getSkillManager().addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP());
			return;
		}
		int itemId = item.getId();
		int amount = item.getAmount();


		if (player.getInventory().contains(6821)) {
			int value = item.getDefinition().getValue();
			int formula = value * amount;

			player.getPacketSender().sendMessage("@or2@Your drop has been converted to coins!");
			player.getInventory().add(995, formula);
		}



		if ( itemId == 6914 || itemId == 7158 || itemId == 6889 || itemId == 15019
				|| itemId == 11235 || itemId == 15020 || itemId == 15018 || itemId == 15220 || itemId == 6735|| itemId == 4770 || itemId == 4772 || itemId == 4771 || itemId == 3988
				|| itemId == 6737 || itemId == 6585 || itemId == 4151 || itemId == 4087
				|| itemId == 2581 ||itemId == 6193|| itemId ==  6194|| itemId ==  6195||  itemId == 6196|| itemId == 6197|| itemId ==  6198|| itemId ==  6199
				|| itemId == 6193|| itemId ==  6194|| itemId ==  6195||  itemId == 6196|| itemId == 6197|| itemId ==  6198|| itemId ==  6199
				||itemId == 5209|| itemId ==  923||  itemId == 3994||  itemId == 3995||  itemId == 3996|| itemId == 3974|| itemId == 5131
				||itemId == 12605||  itemId == 3908|| itemId == 3910||  itemId == 3909|| itemId == 3907|| itemId == 19720
				|| itemId == 4001||  itemId == 3999||  itemId == 4000||  itemId == 3980|| itemId == 18955|| itemId == 18956|| itemId == 18957
				|| itemId == 5209|| itemId ==  923||  itemId == 3994||  itemId == 3995||  itemId == 3996|| itemId == 3974|| itemId == 5131
				||itemId == 12605||  itemId == 19886||  itemId == 3908|| itemId == 3910|| itemId == 3907|| itemId == 19720
				||itemId == 4001 ||  itemId == 3999||  itemId == 4000||  itemId == 3980|| itemId == 18955|| itemId == 18956|| itemId == 18957
				||itemId == 15649||  itemId == 15650||  itemId == 15651||  itemId == 15654||  itemId == 15655||  itemId == 5167|| itemId == 15652|| itemId == 15653||// Dzanth
				itemId == 4761|| itemId == 4762||  itemId == 4763|| itemId ==  4764|| itemId == 4765|| itemId == 3905|| itemId == 5089|| itemId == 18894|| // King Kong
				itemId == 15045|| itemId == 926|| itemId == 5210|| itemId == 931|| itemId == 5211|| itemId == 930|| itemId == 12001||
				itemId == 3821|| itemId == 3820||  itemId == 3822|| itemId ==  19945||  itemId == 20054|| itemId == 5173||  itemId == 5206||// Lucid Warriors
				itemId == 4781|| itemId ==  4782|| itemId == 4783|| itemId ==  15032|| itemId ==  4785|| itemId ==  5195|| itemId ==  3914|| itemId == 3957|| itemId == 19890|| // Hulk
				itemId == 15656|| itemId ==  5082|| itemId == 5083|| itemId == 5084|| itemId == 3985||
				itemId == 19619|| itemId == 19470|| itemId ==  19471|| itemId ==  19472||  itemId == 19473||  itemId == 19474||  itemId == 5129||//Pyros
				itemId == 4643|| itemId == 4641|| itemId == 4642|| itemId == 3983|| itemId == 3064|| // purple wyrms
				itemId == 19618|| itemId ==  19691|| itemId ==  19692||  itemId == 19693|| itemId == 19694|| itemId == 19695|| itemId == 19696|| // trinity
				itemId == 19159||  itemId == 19160|| itemId ==  19161||  itemId == 19163||  itemId == 19164|| itemId ==  19165|| itemId ==  19166||
				itemId == 9492|| itemId ==  9493||  itemId == 9494||  itemId == 9495||// herbal rouge
				itemId == 19727|| itemId == 19728|| itemId == 19729|| itemId == 19730|| itemId == 19731|| itemId == 19732|| itemId == 6485||//supreme
				itemId == 13202|| itemId == 13203|| itemId == 13204|| itemId == 13205|| itemId == 13206|| itemId == 13207|| itemId == 11143|| itemId == 11144|| itemId == 11145|| itemId == 11146|| itemId == 11147||//storm breaker and apollo
				itemId == 14494|| itemId ==  14492|| itemId ==  14490||  itemId == 2760||
				itemId == 4794|| itemId ==  4795|| itemId ==  4796||  itemId == 4797||
				itemId == 19127|| itemId ==  19128|| itemId ==  19129|| itemId ==  19154||  itemId == 19741||
				itemId == 19742|| itemId ==  19743|| itemId ==  19744
		) {
			String itemName = item.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			String npcName = Misc.formatText(npc.getDefinition().getName());
			String worth = item.getDefinition().getValue() > 0 ? " worth " + Misc.currency(item.getDefinition().getValue()) : "";
			String discordmessage = itemMessage + " from " + npcName + worth + "!";
			NpcGain.RareDropXP(player);
			Familiar pet2 = player.getSummoning().getFamiliar();
			if (pet2 != null && PetPerkData.hasLootEffect(pet.getSummonNpc().getId())) {
				DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemName + " from the Assassin!");
			}
			World.sendMessageNonDiscord("<col=089915><shad=1>" + player.getUsername()
					+ "  has just received <img=12><col=991608> " + itemMessage + " <img=12><col=089915> from <col=bb1313> " + npcName + "!");
			DiscordMessenger.sendRareDrop(Misc.formatPlayerName(player.getUsername()), discordmessage);
		}




		if (itemId == CharmingImp.GOLD_CHARM || itemId == CharmingImp.GREEN_CHARM || itemId == CharmingImp.CRIM_CHARM
				|| itemId == CharmingImp.BLUE_CHARM) {
			if (player.getInventory().contains(6500) && CharmingImp.handleCharmDrop(player, itemId, amount)) {
				return;
			}
		}

		Player toGive = player;

		boolean ccAnnounce = false;
		if (Location.inMulti(player)) {
			if (player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
				CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
				for (Player member : player.getCurrentClanChat().getMembers()) {
					if (member != null) {
						if (member.getPosition().isWithinDistance(player.getPosition())) {
							playerList.add(member);
						}
					}
				}
				if (playerList.size() > 0) {
					toGive = playerList.get(RandomUtility.getRandom(playerList.size() - 1));
					if (toGive == null || toGive.getCurrentClanChat() == null
							|| toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
						toGive = player;
					}
					ccAnnounce = true;
				}
			}
		}

		if (itemId == 18778) { // Effigy, don't drop one if player already has one
			if (toGive.getInventory().contains(18778) || toGive.getInventory().contains(18779)
					|| toGive.getInventory().contains(18780) || toGive.getInventory().contains(18781)) {
				return;
			}
			for (Bank bank : toGive.getBanks()) {
				if (bank == null) {
					continue;
				}
				if (bank.contains(18778) || bank.contains(18779) || bank.contains(18780) || bank.contains(18781)) {
					return;
				}
			}
		}



		if (ItemDropAnnouncer.announce(itemId)) {
			String itemName = item.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			String npcName = Misc.formatText(npc.getDefinition().getName());

			if (player.getRights() == PlayerRights.DEVELOPER) {
				GroundItemManager.spawnGroundItem(toGive,
						new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
			}

			new CollectionEntry(npc.getId(), item.getId(), item.getAmount()).submit(player);


			switch (itemId) {
				case 14484:
					itemMessage = "a pair of Dragon Claws";
					break;
				case 20000:
				case 20001:
				case 20002:
					itemMessage = itemName;
					break;
			}
			switch (npc.getId()) {
				case 81:
					npcName = "a Cow";
					break;
				case 50:
				case 3200:
				case 8133:
				case 4540:
				case 1160:
				case 8549:
					npcName = "The " + npcName + "";
					break;
				case 51:
				case 54:
				case 5363:
				case 8349:
				case 1592:
				case 1591:
				case 1590:
				case 1615:
				case 9463:
				case 9465:
				case 9467:
				case 1382:
				case 13659:
				case 11235:
					npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
					break;
			}
			ItemDefinition drop = ItemDefinition.forId(itemId);
			NpcDefinition npcDrop = NpcDefinition.forId(npc.getId());

			int npcId = npc.getId();
			int playerKills = player.getNpcKillCount(npcId);

			String message = "<img=12><col=eaeaea>[<col=FF0000>RARE DROP<col=eaeaea>]<img=12><col=eaeaea> " + toGive.getUsername() + " has just received <img=12><col=07b481>" + itemMessage
					+ "<img=12><col=eaeaea> from <col=FF0000>" + npcName + "!";
			String worth = item.getDefinition().getValue() > 0 ? " worth " + Misc.currency(item.getDefinition().getValue()) : "";
			String discordmessage = itemMessage + " from " + npcName + worth + "!";
			if (pet != null && PetPerkData.hasLootEffect(pet.getSummonNpc().getId())) {
				DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemName + " from the Assassin!");
			}
			World.sendMessageNonDiscord(message);
			DiscordMessenger.sendRareDrop(Misc.formatPlayerName(toGive.getUsername()), discordmessage);



			if (ccAnnounce) {
				ClanChatManager.sendMessage(player.getCurrentClanChat(),
						"<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> " + toGive.getUsername()
								+ "  has just received <img=12><col=07b481> " + itemMessage + "<img=12><col=eaeaea> from <col=FF0000>" + npcName + "!");
			}

			PlayerLogs.log(toGive.getUsername(),
					"" + toGive.getUsername() + " received " + itemMessage + " from " + npcName + "");
		}
		DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
		new CollectionEntry(npc.getId(), item.getId(), item.getAmount()).submit(player);

	}


	public static void casketDrop(Player player, int combat, Position pos) {
		if (Misc.inclusiveRandom(1, 20) == 10|| Misc.inclusiveRandom(1, 20) == -10) {
			player.getInventory().add(7956, 1);

		}
	}

	public static void clueDrop(Player player, int combat, Position pos) {
		if (Misc.inclusiveRandom(1, 40) == 15|| Misc.inclusiveRandom(1, 40) == -15) {
			player.getInventory().add(19626, 1);
			player.getPacketSender().sendMessage("@or2@<img=12><shad=1>You have recieved a Mysterious scroll!");
		}
	}

	private static void dropScratchcard(Player player, Position pos) {
		int chance = RandomUtility.inclusiveRandom(0, 1000);

		if (chance <= 999) {
			return;
		}

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(3912, 1), pos, player.getUsername(), false, 150, true, 200));

		player.sendMessage("@or2@<img=12><shad=1>You Earned a Event box, Congratulations ");
	}

	public static class ItemDropAnnouncer {

		private static List<Integer> ITEM_LIST;

		private static final int[] TO_ANNOUNCE = new int[] {
				//5131, 4772, 4771, 4770, 12708, 13235, 13239,18347, //Cerb
				//6193, 6194, 6195, 6196, 6197, 6198, 6199, // Zeus
				//5209, 923, 3994, 3995, 3996,3974,5131,// Infartico
				//12605, 19886, 3908, 3910, 3909,3907,19720, // Valor
				//4001, 3999, 4000, 3980, 18955, 18956,18957, // Hurricane Warriors
				//15649, 15650, 15651, 15654, 15655, 5167,15652,15653, // Dzanth
				//4761,4762, 4763, 4764,4765,3905,5089,18894, // King Kong
				//15045, 926, 5210,931,5211,930,12001, // Corp
				//3821, 3820, 3822, 19945, 20054, 5173, 5206,// Lucid Warriors
				//4781, 4782, 4783, 15032, 4785, 5195, 3914,3957,19890, // Hulk
				//15656, 5082, 5083,5084,3985, // darkblue wizards
				//19619, 19470, 19471, 19472, 19473, 19474, 5129,//Pyros
				//4643,4641,4642,3983,3064, // purple wyrms
				//19618, 19691, 19692, 19693,19694,19695,19696, // trinity
				//19159, 19160, 19161, 19163, 19164, 19165, 19166, // cloud
				//9492, 9493, 9494, 9495,// herbal rouge
				//19935,19936,19937,19938, //bonds
				//14494, 14492, 14490, 2760,// Exoden
				19999//Supreme Nex

		};

		private static void init() {
			ITEM_LIST = new ArrayList<Integer>();
			for (int i : TO_ANNOUNCE) {
				ITEM_LIST.add(i);
			}
		}

		public static boolean announce(int item) {
			return ITEM_LIST.contains(item);
		}
	}
}