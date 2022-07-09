package com.zamron.net.packet.impl;

import com.zamron.model.Flag;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.*;
import com.zamron.model.container.impl.Shop.ShopManager;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.model.definitions.WeaponInterfaces;
import com.zamron.model.input.EnterAmount;
import com.zamron.model.input.Input;
import com.zamron.model.input.impl.EnterAmountToBank;
import com.zamron.model.input.impl.EnterAmountToBuyFromShop;
import com.zamron.model.input.impl.EnterAmountToPriceCheck;
import com.zamron.model.input.impl.EnterAmountToRemoveFromBank;
import com.zamron.model.input.impl.EnterAmountToRemoveFromBob;
import com.zamron.model.input.impl.EnterAmountToRemoveFromPriceCheck;
import com.zamron.model.input.impl.EnterAmountToRemoveFromStake;
import com.zamron.model.input.impl.EnterAmountToRemoveFromTrade;
import com.zamron.model.input.impl.EnterAmountToSellToShop;
import com.zamron.model.input.impl.EnterAmountToStake;
import com.zamron.model.input.impl.EnterAmountToStore;
import com.zamron.model.input.impl.EnterAmountToTrade;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.Trading;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.dissolving.DissolveData;
import com.zamron.world.content.grandexchange.GrandExchange;
import com.zamron.world.content.grandexchange.GrandExchangeOffer;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.content.minigames.impl.Dueling.DuelRule;
import com.zamron.world.content.partyroom.PartyRoomManager;
import com.zamron.world.content.skill.impl.smithing.EquipmentMaking;
import com.zamron.world.content.skill.impl.smithing.SmithingData;
import com.zamron.world.content.transportation.JewelryTeleporting;
import com.zamron.world.content.upgrading.UpgradeData;
import com.zamron.world.entity.impl.player.Player;

import java.util.Arrays;

public class ItemContainerActionPacketListener implements PacketListener {

	/**
	 * Manages an item's first action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void firstAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		Item item = new Item(id);

		if (player.getRights().equals(PlayerRights.OWNER)) {
			player.getPacketSender().sendMessage("FIRST ACTION Interface: "+interfaceId + " ID: " + id + " Slot " + slot + " Item: " + item.getDefinition().getName());
		}

		/*
		 * if(player.getRights() == PlayerRights.DEVELOPER) {
		 * player.sendMessage("@red@Button clicked: @blu@" + interfaceId);
		 * player.sendMessage("@red@Slot id: @blu@" + slot);
		 * player.sendMessage("@red@id clicked: @blu@" + id); }
		 */



		if (player.getInterfaceId() == 37600) {
			if (!player.getBlockedCollectorsList().contains(item.getId())) {
				player.getBlockedCollectorsList().add(item.getId());
				player.sendMessage("@red@Blocked item:@blu@ " + item.getDefinition().getName());
			} else {
				player.getBlockedCollectorsList().remove(item.getId());
				player.sendMessage("@blu@That item was already blocked so it has been unblocked");
				return;
			}
			//System.out.println("List: " + player.getBlockedCollectorsList());
		}
		//System.out.println("Interface id: " + interfaceId);

		switch (interfaceId) {



		case -334:
			//System.out.println("Item was: " + item.getId());
			Object[] obj = ShopManager.getCustomShopData(119, item.getId());
			if (obj == null) {
				//System.out.println("This item has no value");
				return;
			}
			int value = (int) ((int) obj[0] * 0.85);
			String name = item.getDefinition().getName();
			player.sendMessage("@blu@The shop buys @red@" + name + " for " + value + " 1b coins");

			break;

		case -3327:
			for (UpgradeData data : UpgradeData.values()) {
				if (data.getRequired().getId() == item.getId()) {
					player.setUpgradeSelection(item);
					player.getPacketSender().sendItemOnInterface(62210, data.getReward().getId(),
							data.getReward().getAmount());
					player.getPacketSender().sendString(62250, "Upgrade Chance: @gre@" + data.getChance() + "%");
					int bagsRequired = data.getBagsRequired() == 0 ? 0 : data.getBagsRequired();
					player.getPacketSender().sendString(62203, "1b Coins required: @gre@" + bagsRequired);
				}
			}
			break;
		case -2327:
			for (DissolveData data : DissolveData.values()) {
				if (data.getRequired().getId() == item.getId()) {
					player.setDissolveSelection(item);
					player.getPacketSender().sendItemOnInterface(63210, data.getReward().getId(),
							data.getReward().getAmount());
					player.getPacketSender().sendString(63250, "Upgrade Chance: @gre@" + data.getChance() + "%");
					int bagsRequired = data.getBagsRequired() == 0 ? 0 : data.getBagsRequired();
					player.getPacketSender().sendString(63203, "1b coins required: @gre@" + bagsRequired);
				}
			}
			break;
		case -23927:
			player.getMysteryBox().reward();
			break;
			case PartyRoomManager.INVENTORY_ITEMS:
				PartyRoomManager.deposit(player, new Item(id), slot);
				break;
			case PartyRoomManager.DEPOSIT_INVENTORY:
				PartyRoomManager.withdraw(player, new Item(id), slot);
				break;

		case -12026:
			//System.out.println("ItemContainer one!!!");
			break;
		case 32621:
			player.getPlayerOwnedShopManager().handleBuy(slot, id, -1);
			break;
		case -31915:
			player.getPlayerOwnedShopManager().handleWithdraw(slot, id, -1);
			break;
		case -28482:
			player.getPlayerOwnedShopManager().handleStore(slot, id, 1);
			break;
		case GrandExchange.COLLECT_ITEM_PURCHASE_INTERFACE:
			GrandExchange.collectItem(player, id, slot, GrandExchangeOffer.OfferType.BUYING);
			break;
		case GrandExchange.COLLECT_ITEM_SALE_INTERFACE:
			GrandExchange.collectItem(player, id, slot, GrandExchangeOffer.OfferType.SELLING);
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 1, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 1, slot);
			}
			if (player.getGambling().inGamble()) {
				player.getGambling().gambleItem(id, 1, slot);
			}

			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().addItem(player, item.getId(), 1);
			}
			// player.getGambling().gambleItem(id, 1, slot);
			break;

		case -8365:
			if (player.getGambling().inGamble()) {
				player.getGambling().removeGambledItem(id, 1);
			}
			break;

		case 57150:
			//System.out.println("xd ok k");
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 1);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 1);
				return;
			}

			break;

		case 19313:
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().removeItem(player, id, 1);
			}
			break;
		case Equipment.INVENTORY_INTERFACE_ID:
			item = slot < 0 ? null : player.getEquipment().getItems()[slot];
			if (item == null || item.getId() != id)
				return;
			if (player.getLocation() == Location.DUEL_ARENA) {
				if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
					if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT
							|| item.getDefinition().isTwoHanded()) {
						player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
						return;
					}
				}
			}
			boolean stackItem1 = item.getDefinition().isStackable() && player.getInventory().getAmount(item.getId()) > 0;
			int inventorySlot1 = player.getInventory().getEmptySlot();
			if (inventorySlot1 != -1) {
				Item itemReplacement = new Item(-1, 0);
				player.getEquipment().setItem(slot, itemReplacement);
				if (!stackItem1)
					player.getInventory().setItem(inventorySlot1, item);
				else
					player.getInventory().add(item.getId(), item.getAmount());
				BonusManager.update(player);
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
					if (player.getAutocastSpell() != null || player.isAutocast()) {
						Autocasting.resetAutocast(player, true);
						player.getPacketSender().sendMessage("Autocast spell cleared.");
					}
					player.setSpecialActivated(false);
					player.getPacketSender().sendSpriteChange(41006, 945);
					CombatSpecial.updateBar(player);
					if (player.hasStaffOfLightEffect()) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender()
								.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
				}
				player.getEquipment().refreshItems();
				player.getInventory().refreshItems();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				player.getInventory().full();
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				break;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), item, slot, true, true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			if (!player.isBanking() || !player.getInventory().contains(item.getId()) || player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() != null)
				player.getShop().checkValue(player, slot, false);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.getShop() != null)
				player.getShop().checkValue(player, slot, true);
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), item, slot, false, true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), item, slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 1;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item.getId(), SmithingData.getItemAmount(item)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), item,
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 1),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's second action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int id = packet.readLEShortA();
		int slot = packet.readLEShort();
		Item item = new Item(id);

		if (player.getRights().equals(PlayerRights.OWNER)) {
			player.getPacketSender().sendMessage("Interface: "+interfaceId + " ID: " + id + " Slot " + slot + " Item: " + item.getDefinition().getName());
		}

		switch (interfaceId) {
		case -31915:
			player.setInputHandling(new Input() {

				@Override
				public void handleAmount(Player player, int value) {
					player.getPlayerOwnedShopManager().setCustomPrice(slot, id, value);
				}

			});
			player.getPacketSender().sendEnterAmountPrompt("Enter the price for this item:");
			break;
		case -28482:
			player.getPlayerOwnedShopManager().handleStore(slot, id, 5);
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 5, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 5, slot);
			}
			if (player.getGambling().inGamble()) {
				player.getGambling().gambleItem(id, 5, slot);
			}
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().addItem(player, item.getId(), 5);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 5);
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 5);
				return;
			}
			break;
			case PartyRoomManager.INVENTORY_ITEMS:
				PartyRoomManager.deposit(player, new Item(id, 5), slot);
				break;
			case PartyRoomManager.DEPOSIT_INVENTORY:
				PartyRoomManager.withdraw(player, new Item(id, 5), slot);
				break;
		case 19313:
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().removeItem(player, id, 5);
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || item.getId() != id || player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 5), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			item = player.getInventory().forSlot(slot).copy().setAmount(5).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() == null)
				return;
			item = player.getShop().forSlot(slot).copy().setAmount(1).copy();
			player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 1);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				if (item.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), new Item(id, 5), slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 5), slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 5;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item.getId(), SmithingData.getItemAmount(item)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 5),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 5),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's third action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void thirdAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShort();
		int id = packet.readShortA();
		int slot = packet.readShortA();
		Item item1 = new Item(id);
		switch (interfaceId) {
			case PartyRoomManager.INVENTORY_ITEMS:
				PartyRoomManager.deposit(player, new Item(id, 10), slot);
				break;
			case PartyRoomManager.DEPOSIT_INVENTORY:
				PartyRoomManager.withdraw(player, new Item(id, 10), slot);
				break;
			case 32621:
			player.getPlayerOwnedShopManager().handleBuy(slot, id, 1);
			break;
		case -31915:
			player.getPlayerOwnedShopManager().handleWithdraw(slot, id, 1);
			break;
		case -28482:
			player.getPlayerOwnedShopManager().handleStore(slot, id, 10);
			break;
		case Equipment.INVENTORY_INTERFACE_ID:
			if (!player.getEquipment().contains(id))
				return;
			switch (id) {
			case 1712:
			case 1710:
			case 1708:
			case 1706:
			case 11118:
			case 11120:
			case 11122:
			case 11124:
				JewelryTeleporting.rub(player, id);
				break;
			case 1704:
				player.getPacketSender().sendMessage("Your amulet has run out of charges.");
				break;
			case 11126:
				player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
				break;
				case 19785:
					int[] charges1 = player.getVoidTopChargers();
					player.getPacketSender().sendMessage("You Elite void top has " + Arrays.toString(charges1)+ "/1500 left before it crumbles.");
					break;
				case 19786:
					int[] charges2 = player.getVoidLegChargers();
					player.getPacketSender().sendMessage("You Elite void legs has " + Arrays.toString(charges2)+ "/1500 left before it crumbles.");
					break;
			case 11283:
			case 11613:
				int charges = player.getDfsCharges();
				if (charges >= 20 || player.getRights() == PlayerRights.DEVELOPER) {
					if (player.getCombatBuilder().isAttacking())
						CombatFactory.handleDragonFireShield(player, player.getCombatBuilder().getVictim());
					else
						player.getPacketSender().sendMessage("You can only use this in combat.");
				} else
					player.getPacketSender().sendMessage("Your shield doesn't have enough power yet. It has "
							+ player.getDfsCharges() + "/20 dragon-fire charges.");
				break;
			}
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, 10, slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, 10, slot);
			}
			if (player.getGambling().inGamble()) {
				player.getGambling().gambleItem(id, 10, slot);
			}
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().addItem(player, item1.getId(), 10);
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade())
				player.getTrading().removeTradedItem(id, 10);
			break;

		case 19313:
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().removeItem(player, id, 10);
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().removeStakedItem(id, 10);
				return;
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), new Item(id, 10), slot, true,
					true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item item = player.getInventory().forSlot(slot).copy().setAmount(10).copy();
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() == null)
				return;
			item = player.getShop().forSlot(slot).copy().setAmount(5).copy();
			player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 5);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 10), slot, false, true);
			}
			break;
		case 1119: // smithing interface row 1
		case 1120: // row 2
		case 1121: // row 3
		case 1122: // row 4
		case 1123: // row 5
			int barsRequired = SmithingData.getBarAmount(item1);
			Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
			int x = 10;
			if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
				x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
			EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
					new Item(item1.getId(), SmithingData.getItemAmount(item1)), x);
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 10),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 10),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fourth action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fourthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShort();
		int id = packet.readShortA();
		switch (interfaceId) {
			case PartyRoomManager.INVENTORY_ITEMS:
				PartyRoomManager.deposit(player, new Item(id, player.getInventory().getAmount(id)), slot);
				break;
			case PartyRoomManager.DEPOSIT_INVENTORY:
				PartyRoomManager.withdraw(player, new Item(id, player.getPartyRoom().getDeposit().getAmount(id)), slot);
				break;
			case 32621:
			player.setInputHandling(new Input() {

				@Override
				public void handleAmount(Player player, int value) {
					player.getPlayerOwnedShopManager().handleBuy(slot, id, value);
				}

			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?:");
			break;
		case -28482:
			player.getPlayerOwnedShopManager().handleStore(slot, id, Integer.MAX_VALUE);
			break;
		case -31915:
			player.setInputHandling(new Input() {

				@Override
				public void handleAmount(Player player, int value) {
					player.getPlayerOwnedShopManager().handleWithdraw(slot, id, value);
				}

			});
			player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?:");
			break;
		case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.getTrading().tradeItem(id, player.getInventory().getAmount(id), slot);
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.getDueling().stakeItem(id, player.getInventory().getAmount(id), slot);
			}
			if (player.getGambling().inGamble()) {
				player.getGambling().gambleItem(id, player.getInventory().getAmount(id), slot);
			}
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().addItem(player, id, player.getInventory().getAmount(id));
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				for (Item item : player.getTrading().offeredItems) {
					if (item != null && item.getId() == id) {
						player.getTrading().removeTradedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
		case 19313:
			if (player.getInterfaceId() == 19307 && player.getGroupIronmanGroup() != null) {
				player.getGroupIronmanGroup().removeItem(player, id);
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				for (Item item : player.getDueling().stakedItems) {
					if (item != null && item.getId() == id) {
						player.getDueling().removeStakedItem(id, item.getAmount());
						if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
							break;
					}
				}
			}
			break;
		case Bank.INTERFACE_ID:
			if (!player.isBanking() || player.getBank(Bank.getTabForItem(player, id)).getAmount(id) <= 0
					|| player.getInterfaceId() != 5292)
				return;
			player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
					new Item(id, player.getBank(Bank.getTabForItem(player, id)).getAmount(id)), slot, true, true);
			player.getBank(player.getCurrentBankTab()).open();
			break;
		case Bank.INVENTORY_INTERFACE_ID:
			Item item = player.getInventory().forSlot(slot).copy().setAmount(player.getInventory().getAmount(id));
			if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
					|| player.getInterfaceId() != 5292)
				return;
			player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
			player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.getShop() == null)
				return;
			item = player.getShop().forSlot(slot).copy().setAmount(10).copy();
			player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, true, true);
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, 10);
				return;
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 29);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
						true);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.getInventory().switchItem(player.getPriceChecker(),
						new Item(id, player.getInventory().getAmount(id)), slot, false, true);
			}
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 29),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(),
						new Item(id, player.getPriceChecker().getAmount(id)),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fifth action.
	 * 
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fifthAction(Player player, Packet packet) {
		int slot = packet.readLEShort();
		int interfaceId = packet.readShortA();
		int id = packet.readLEShort();
		//System.out.println("Data id: " + id);
		//System.out.println("Data int id: " + interfaceId);
		switch (interfaceId) {
			case PartyRoomManager.INVENTORY_ITEMS:
				player.setInputHandling(new EnterAmount() {

					@Override
					public void handleAmount(Player player, int amount) {
						PartyRoomManager.deposit(player, new Item(id, amount), slot);
					}
				});
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
				break;
			case PartyRoomManager.DEPOSIT_INVENTORY:
				player.setInputHandling(new EnterAmount() {

					@Override
					public void handleAmount(Player player, int amount) {
						PartyRoomManager.withdraw(player, new Item(id, amount), slot);
					}
				});
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
				break;
			case Trading.INTERFACE_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToTrade(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to trade?");
			} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToStake(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to stake?");
			}
			break;
		case Trading.INTERFACE_REMOVAL_ID:
			if (player.getTrading().inTrade()) {
				player.setInputHandling(new EnterAmountToRemoveFromTrade(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Dueling.INTERFACE_REMOVAL_ID:
			if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
				player.setInputHandling(new EnterAmountToRemoveFromStake(id));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
			break;
		case Bank.INVENTORY_INTERFACE_ID: // BANK X
			if (player.isBanking()) {
				player.setInputHandling(new EnterAmountToBank(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to bank?");
			}
			break;
		case Bank.INTERFACE_ID:
		case 11:
			if (player.isBanking()) {
				if (interfaceId == 11) {
					player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
				} else {
					if (player.getBank(Bank.getTabForItem(player, id)).getAmount(id) == 1) {
						player.getPacketSender()
								.sendMessage("You only have 1 " + ItemDefinition.forId(id).getName() + "!");
					} else {
						player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(),
								new Item(id, player.getBank(Bank.getTabForItem(player, id)).getAmount(id) - 1), slot,
								true, true);
					}
					player.getBank(player.getCurrentBankTab()).open(player, false);
				}
			}
			break;
		case Shop.ITEM_CHILD_ID:
			if (player.isBanking())
				return;
			if (player.isShopping()) {
				player.setInputHandling(new EnterAmountToBuyFromShop(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
				player.getShop().setPlayer(player);
			}
			break;
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isBanking())
				return;
			if (player.isShopping()) {
				player.setInputHandling(new EnterAmountToSellToShop(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
				player.getShop().setPlayer(player);
			}
			break;
		case PriceChecker.INTERFACE_PC_ID:
			if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to pricecheck?");
			}
			break;
		case BeastOfBurden.INTERFACE_ID:
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				Item storeItem = new Item(id, 10);
				if (storeItem.getDefinition().isStackable()) {
					player.getPacketSender().sendMessage("You cannot store stackable items.");
					return;
				}
				player.setInputHandling(new EnterAmountToStore(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to store?");
			}
			break;
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.setInputHandling(new EnterAmountToRemoveFromBob(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToRemoveFromPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		}
	}

	private static void sixthAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		switch (interfaceId) {
		case Shop.INVENTORY_INTERFACE_ID:
			if (player.isShopping()) {
				player.getShop().sellItem(player, slot, player.getInventory().getAmount(id));
				return;
			}
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdAction(player, packet);
			break;
		case FOURTH_ITEM_ACTION_OPCODE:
			fourthAction(player, packet);
			break;
		case FIFTH_ITEM_ACTION_OPCODE:
			fifthAction(player, packet);
			break;
		case SIXTH_ITEM_ACTION_OPCODE:
			sixthAction(player, packet);
			break;
		}
	}

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	public static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	public static final int SIXTH_ITEM_ACTION_OPCODE = 138;
}
