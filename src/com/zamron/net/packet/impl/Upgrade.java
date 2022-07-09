package com.zamron.net.packet.impl;

import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class Upgrade {

	public static enum UpgItem {
		SHADOW_OBLIVION_MASK(22000, new Item[] { new Item(5022, 500000) }, 5, 22021),
		SHADOW_OBLIVION_PLATEBODY(22001, new Item[] { new Item(5022, 500000) }, 5, 22022),
		SHADOW_OBLIVION_PLATELEGS(22002, new Item[] { new Item(5022, 500000) }, 5, 22023),
		SHADOW_OBLIVION_WINGS(22003, new Item[] { new Item(5022, 250000) }, 5, 22024),
		VEGETAS_BODY(20920, new Item[] { new Item(5022, 250000) }, 5, 22019),
		VEGETAS_LEGS(20919, new Item[] { new Item(5022, 250000) }, 5, 22018),
		VEGETAS_HAIR(20918, new Item[] { new Item(5022, 250000) }, 5, 22017),
		VEGETAS_GLOVES(20917, new Item[] { new Item(5022, 250000) }, 5, 22016),
		VEGETAS_BOOTS(20916, new Item[] { new Item(5022, 250000) }, 5, 22015),
		TEDDYBEAR(20913, new Item[] { new Item(19640, 5000) }, 5, 20912),
		CHUCKY(20913, new Item[] { new Item(19640, 20000), }, 10, 22176),
		LEGENDSWORD(22127, new Item[] { new Item(19640, 100000),new Item(19838, 1), }, 3, 3086),
		RED_DARKLORD_FULLHELM(1153, new Item[] { new Item(11316, 100) }, 3, 21050),
		RED_DARKLORD_PLATEBODY(1115, new Item[] { new Item(11316, 100) }, 3, 21051),
		RED_DARKLORD1_PLATELEGS(1067, new Item[] { new Item(11316, 100) }, 3, 21052),
		RED_DARKLORD_GLOVES(21011, new Item[] { new Item(11316, 100) }, 3, 21053),
		RED_DARKLORD_BOOTS(21012, new Item[] { new Item(11316, 100) }, 3, 21054),
		WATER_LASER_SWORD(21031, new Item[] { new Item(11316, 100) }, 3, 21033),
		FIRE_LASER_SWORD(21002, new Item[] { new Item(11316, 100) }, 3, 21003),
		GRASS_LASER_SWORD(21030, new Item[] { new Item(11316, 100) }, 3, 21032),
		DARK_PREDATOR_HELM(20086, new Item[] { new Item(11316, 200) }, 5, 20081),
		DARK_PREDATOR_BODY(20087, new Item[] { new Item(11316, 200) }, 5, 20082),
		DARK_PREDATOR_LEGS(20088, new Item[] { new Item(11316, 200) }, 5, 20083),
		DARK_PREDATOR_BOOST(21021, new Item[] { new Item(11316, 200) }, 5, 20084),
		DARK_PREDATOR_GLOVES(21020, new Item[] { new Item(11316, 200) }, 5, 20085),
		DARK_PREDATOR_SCYTHE(21013, new Item[] { new Item(11316, 200) }, 5, 20079),
		DEADLY_ASSASIN_HELM(20104, new Item[] { new Item(11316, 400) }, 3, 20012),
		DEADLY_ASSASIN_BODY(20105, new Item[] { new Item(11316, 400) }, 3, 20010),
		DEADLY_ASSASIN_LEGS(20103, new Item[] { new Item(11316, 400) }, 3, 20011),
		DEADLY_ASSASIN_GLOVES(20106, new Item[] { new Item(11316, 400) }, 3, 20020),
		DEADLY_ASSASIN_BOOTS(20107, new Item[] { new Item(11316, 400) }, 3, 20019),
		DEADLY_ASSASIN_BLADE(20102, new Item[] { new Item(11316, 400) }, 3, 20607),
		NATURE_TORVA_DEFENDER(13199, new Item[] { new Item(11316, 600) }, 4, 21064),
		NATURE_TORVA_HELM(13196, new Item[] { new Item(11316, 600) }, 4, 14008),
		NATURE_TORVA_BODY(13197, new Item[] { new Item(11316, 600) }, 4, 14009),
		NATURE_TORVA_LEGS(13198, new Item[] { new Item(11316, 600) }, 4, 14010),
		NATURE_TORVA_SWORD(21060, new Item[] { new Item(11316, 600) }, 4, 21063),
		NATURE_TORVA_GLOVES(13207, new Item[] { new Item(11316, 600) }, 4, 21066),
		NATURE_TORVA_BOOTS(13206, new Item[] { new Item(11316, 600) }, 4, 21065),
		ROW(2572, new Item[] { new Item(11425, 1) }, 4, 11527),
		ROW1(11527, new Item[] { new Item(11425, 2) }, 4, 11529),
		ROW2(11529, new Item[] { new Item(11425, 3) }, 4, 11531),
		ROW3(11531, new Item[] { new Item(11425, 4) }, 4, 11533),
		VOID_MELLEE_HELM(11665, new Item[] { new Item(11425, 5) }, 4, 21056),
		VOID_RANGE_HELM(11664, new Item[] { new Item(11425, 5) }, 4, 21057),
		VOID_MAGE_HELM(11663, new Item[] { new Item(11425, 5) }, 4, 21058),
		VOID_BODY_HELM(8839, new Item[] { new Item(11425, 5) }, 4, 19787),
		VOID_LEGS_HELM(8840, new Item[] { new Item(11425, 5) }, 4, 19788),
		VOID_GLOVES_HELM(8842, new Item[] { new Item(11425, 5) }, 4, 21059),
		SLAYER1(11550, new Item[] { new Item(11316, 5000) }, 4, 11549),
		SLAYER2(11549, new Item[] { new Item(11316, 5000) }, 4, 11546),
		SLAYER3(11546, new Item[] { new Item(11316, 5000) }, 4, 11547),
		SLAYER4(11547, new Item[] { new Item(11316, 5000) }, 4, 11548),
		REGDONOR(12421, new Item[] { new Item(5022, 50000), new Item(11425, 5) }, 4, 12422),
		SUPERDONOR(12422, new Item[] { new Item(5022, 100000), new Item(11425, 5) }, 6, 12423),
		LEGENDARYDONOR(12423, new Item[] { new Item(5022, 250000), new Item(11425, 10) }, 8, 12424),
		TRIO_SWORD(20101, new Item[] { new Item(11425, 5) }, 6, 17849),
		STAFF_OF_WATER(20604, new Item[] { new Item(11425, 5) }, 5, 20602),
		DARK_LORD_BOW(3961, new Item[] { new Item(11425, 10) }, 5, 18965),
		PET_NUTELLA(2759, new Item[] { new Item(11425, 10), new Item(5022, 200000) }, 4, 2103),
		PET_HOBO(2762, new Item[] { new Item(11425, 10), new Item(5022, 200000) }, 11, 3622),
		UBERDONOR(12424, new Item[] { new Item(5022, 500000), new Item(11425, 20) }, 7, 12425),
		COLLECTOR_AMULET(15454, new Item[] { new Item(5022, 750000), new Item(11425, 10) }, 5, 11423),
		COLLECTORIMBUED(11423, new Item[] { new Item(5022, 1250000), new Item(11425, 15) }, 4, 11424),
		GALAXY_MG(21082, new Item[] { new Item(5022, 500000), new Item(11425, 8) }, 4, 901),
		/*
		 * DONOR1(12421, new Item[] {new Item(5022,50000),new Item(11425,1)}, 5, 12422),
		 * DONOR2(12422, new Item[] {new Item(5022,75000),new Item(11425,1)}, 10,
		 * 12423), DONOR3(12423, new Item[] {new Item(5022,100000),new Item(11425,1)},
		 * 20, 12424), DONOR4(12424, new Item[] {new Item(11316,1000),new
		 * Item(11425,1)}, 40, 12425),
		 */
		SOE(11316, new Item[] { new Item(11316, 500), new Item(5022, 1000) }, 3, 11425),
		RUBYGEM(1603, new Item[] { new Item(5023, 2) }, 1, 22074), // ATTACKGEM
		EMERALDGEM(1605, new Item[] { new Item(5023, 2) }, 1, 22075), // RANGEDGEM
		SAPPHIREGEM(1607, new Item[] { new Item(5023, 2) }, 1, 22076), // MAGICGEM
		OPALGEM(1609, new Item[] { new Item(5023, 2) }, 1, 22077), // STRENGTH
		RUBYGEM1(22074, new Item[] { new Item(5023, 5) }, 2, 22078), // ATTACKGEM
		EMERALDGEM1(22075, new Item[] { new Item(5023, 5) }, 2, 22079), // RANGEDGEM
		SAPPHIREGEM1(22076, new Item[] { new Item(5023, 5) }, 2, 22080), // MAGICGEM
		OPALGEM1(22077, new Item[] { new Item(5023, 5) }, 2, 22081), // STRENGTH
		RUBYGEM2(22078, new Item[] { new Item(5023, 12) }, 3, 22082), // ATTACKGEM
		EMERALDGEM2(22079, new Item[] { new Item(5023, 12) }, 3, 22083), // RANGEDGEM
		SAPPHIREGEM2(22080, new Item[] { new Item(5023, 12) }, 3, 22084), // MAGICGEM
		OPALGEM2(22081, new Item[] { new Item(5023, 12) }, 3, 22085), // STRENGTH
		RUBYGEM3(22082, new Item[] { new Item(5023, 25) }, 4, 22086), // ATTACKGEM
		EMERALDGEM3(22083, new Item[] { new Item(5023, 25) }, 4, 22087), // RANGEDGEM
		SAPPHIREGEM3(22084, new Item[] { new Item(5023, 25) }, 4, 22088), // MAGICGEM
		OPALGEM3(22085, new Item[] { new Item(5023, 25) }, 4, 22089), // STRENGTH
		RUBYGEM4(22086, new Item[] { new Item(5023, 50) }, 5, 22090), // ATTACKGEM
		EMERALDGEM4(22087, new Item[] { new Item(5023, 50) }, 5, 22091), // RANGEDGEM
		SAPPHIREGEM4(22088, new Item[] { new Item(5023, 50) }, 5, 22092), // MAGICGEM
		OPALGEM4(22089, new Item[] { new Item(5023, 50) }, 5, 22093), // STRENGTH
		DRAGON_MG_MG(901, new Item[] { new Item(5022, 1000000), new Item(11425, 10) }, 4, 902),
		TRIOCAPE2(21029, new Item[] { new Item(21027, 1),new Item(21028, 1), }, 3, 2543),
		TRIOCAPE1(21028, new Item[] {new Item(21027, 1),new Item(21029, 1), }, 3, 2543),
		//DBZ_TELEPORT(15105, new Item[] {new Item(12657, 750),new Item(19640, 5000), }, 3, 15106),
		TRIOCAPE(21027, new Item[] { new Item(21028, 1),new Item(21029, 1), }, 3, 2543);
		
		private Item[] reqItem;
		private int startItem;
		private int chance;
		private int EndItem;

		UpgItem(int startItem, Item[] reqItem, int chance, int EndItem) {
			this.startItem = startItem;
			this.reqItem = reqItem;
			this.chance = chance;
			this.EndItem = EndItem;
		}

		public int getStartItem() {
			return this.startItem;// should work ye okay also the boolean?
		}

		public Item[] getReqItem() {
			return this.reqItem;
		}

		public int getChance() {
			return this.chance;
		}

		public int getEndItem() {
			return this.EndItem;
		}

		public static boolean checkReq(Player p, int itemId) { // test it and let me see
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					for (Item requiredItem : item.reqItem) {
						if (!p.getInventory().contains(requiredItem.getId())
								|| p.getInventory().getAmount(requiredItem.getId()) < requiredItem.getAmount()) {
							p.sendMessage("You are missing " + requiredItem.getAmount() + "x "
									+ requiredItem.getDefinition().getName() + " to upgrade this item.");
							return false;
						}
					}
				}
			}
			// we can delete items if they made it this far
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					for (Item reqItem : item.reqItem) {
						p.getInventory().delete(reqItem.getId(), reqItem.getAmount());
					}
				}
			}
			return true;
		}

		public static Item[] getItemsReq(int itemId) {
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					return item.reqItem;
				}
			}
			return new Item[-1];
		}

		public static int getEndItem(int itemId) {
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					return item.EndItem;
				}
			}
			return -1;
		}

		public static void getReqs(Player p, int itemId) { // test it and let me see
			/*
			 * for (UpgItem item : UpgItem.values()) { if (item.startItem == itemId) { for
			 * (int i = 0; i < item.getReqItem().length; i++) {
			 * //p.sendMessage("<shad=0>you require "+item.reqItem[i].getAmount()+" x "
			 * +ItemDefinition.forId(item.reqItem[i].getId()).getName()
			 * +" To upgrade this item!"); return; } } }
			 */
		}

		public static int getRandom(int itemId) {
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					return item.chance;
				}
			}
			return 0;
		}

		public static int addEndItem(int itemId) {
			for (UpgItem item : UpgItem.values()) {
				if (item.startItem == itemId) {
					return item.EndItem;
				}
			}
			return 0;
		}

		public static int checkItem(Player p, int itemId) {
			for (UpgItem item : UpgItem.values()) {

				if (item.startItem == itemId) {
					return itemId;
				}
			}
			p.getPacketSender().sendMessage("This item can't be upgraded!");
			return 0;
		}

		public static boolean canUpgrade(Player p, int itemId) {
			for (UpgItem item : UpgItem.values()) {

				if (item.startItem == itemId) {
					return true;
				}
			}
			p.getPacketSender().sendMessage("This item can't be upgraded!");
			return false;
		}
	}

	/*
	 * commented out discord code because it's causing errors atm - jhin
	 */
	public static void init(Player p, int itemId) {
		if (p.getClickDelay().elapsed(5000)) {
			p.getClickDelay().reset(5000);
		} else {
			p.sendMessage("You have to wait a few seconds to click again.");
			return;
		}
		if (UpgItem.checkReq(p, itemId)) {
			p.getInventory().delete(itemId, 1);
			int random = Misc.inclusiveRandom(1, UpgItem.getRandom(itemId));
			int tries = 1;
			if (random == 1) {
				p.getPacketSender().sendMessage("" + random);
				if (!ItemDefinition.forId(itemId).getName().toLowerCase().contains("monster")) {
					World.sendMessageNonDiscord("<shad=0>@bla@[@gr3@Upgrade@bla@]@gr3@ " + p.getUsername()
							+ " @bla@ has upgraded his @gr2@" + ItemDefinition.forId(itemId).getName() + "@bla@ !");
				}
				p.getInventory().add(UpgItem.addEndItem(itemId), 1);
			} else {
				p.getPacketSender().sendMessage("@red@[Failed] @bla@You have failed to Upgrade your @red@"
						+ ItemDefinition.forId(itemId).getName());
				tries++;
				String text = "st";

				}
			}
			UpgItem.getReqs(p, itemId);
		}


	public static boolean getItems(Player p, int id) {
		return UpgItem.checkItem(p, id) != 0;
	}
}