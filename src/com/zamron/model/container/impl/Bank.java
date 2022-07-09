package com.zamron.model.container.impl;

import com.zamron.model.Flag;
import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.ItemContainer;
import com.zamron.model.container.StackType;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.model.definitions.WeaponInterfaces;
import com.zamron.model.input.impl.ItemSearch;
import com.zamron.util.Misc;
import com.zamron.world.content.BankPin;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.entity.impl.player.Player;

/**
 * 100% safe Bank System
 * 
 * @author Gabriel Hannason
 */

public class Bank extends ItemContainer {

	private Bank[] bankTabs; // open the shift click packet 4 me

	public Bank(Player player) {
		super(player);
	}
	public void add(Item[] items) {
		for (Item item : items) {
			if (item == null) {
				continue;
			}
			add(item);
		}

	}
	
	
	
	public Bank open() {
		getPlayer().getPacketSender().sendConfig(324, getPlayer().isPlaceholders() ? 1 : 0);
		return open(getPlayer(), true);
	}

	public Bank open(Player player, boolean reset) {
		if(player.isGim() && player.getGroupIronmanGroup() != null) {
			player.getGroupIronmanGroup().openBank(player);
			return this;
		}
		player.getPacketSender().sendClientRightClickRemoval();
		if (Dungeoneering.doingDungeoneering(player)) {
			return this;
		}
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			BankPin.init(player, true);
			return this;
		}
		if (player.getLocation() != Location.DUNGEONEERING) {
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {

				player.getPacketSender().sendInterfaceRemoval()
						.sendMessage("Hardcore-ironman-players cannot use banks.");
				return this;
			}
			if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
				player.getPacketSender().sendInterfaceRemoval()
						.sendMessage("You haven't created a GIM group.");
				player.sendMessage("To create a group use the command ::gim and you can invite players. Max 4.");
				return this;
			}
		}
		getPlayer().getPacketSender().sendRichPresenceState("Banking..");
		getPlayer().getPacketSender().sendSmallImageKey("bank");
		int rand1 = Misc.random(1000, 9999);
		getPlayer().getPacketSender().sendRichPresenceSmallPictureText("PIN: "+rand1);
		sortItems().refreshItems();
		player.setBanking(true).setInputHandling(null);
		player.getPacketSender().sendConfig(115, player.withdrawAsNote() ? 1 : 0)
				.sendConfig(304, player.swapMode() ? 1 : 0)
				.sendConfig(117,(player.getBankSearchingAttribtues().isSearchingBank() && player.getBankSearchingAttribtues().getSearchedBank() != null) ? 1 : 0)
				.sendConfig(111, player.isPlaceholders() ? 1 : 0)
				
				.sendInterfaceSet(5292, 5063);
		return this;
	}

	@Override
	public Bank switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		if (!getPlayer().isBanking() || getPlayer().getInterfaceId() != 5292 || to instanceof Inventory
				&& !(getPlayer().getBank(getPlayer().getCurrentBankTab()).contains(item.getId())
						|| getPlayer().getBankSearchingAttribtues().getSearchedBank() != null
								&& getPlayer().getBankSearchingAttribtues().getSearchedBank().contains(item.getId()))) {
			getPlayer().getPacketSender().sendClientRightClickRemoval();
			return this;
		}
		ItemDefinition def = ItemDefinition.forId(item.getId() + 1);
		if (to.getFreeSlots() <= 0 && (!(to.contains(item.getId()) && item.getDefinition().isStackable()))
				&& !(getPlayer().withdrawAsNote() && def != null && def.isNoted() && to.contains(def.getId()))) {
			to.full();
			return this;
		}
		if (item.getAmount() > to.getFreeSlots() && !item.getDefinition().isStackable()) {
			if (to instanceof Inventory) {
				if (getPlayer().withdrawAsNote()) {
					if (def == null || !def.isNoted())
						item.setAmount(to.getFreeSlots());
				} else
					item.setAmount(to.getFreeSlots());
			}
		}
		
		if (getPlayer().getBankSearchingAttribtues().isSearchingBank()
				&& getPlayer().getBankSearchingAttribtues().getSearchedBank() != null) {
			int tab = Bank.getTabForItem(getPlayer(), item.getId());
			if (!getPlayer().getBank(tab).contains(item.getId())
					|| !getPlayer().getBankSearchingAttribtues().getSearchedBank().contains(item.getId()))
				return this;
			if (item.getAmount() > getPlayer().getBank(tab).getAmount(item.getId())) {
				item.setAmount(getPlayer().getBank(tab).getAmount(item.getId()));
			}
			if (item.getAmount() <= 0) {
				int from = getPlayer().getBank(tab).getSlot(item.getId());
				if (from != -1) {
					if (getPlayer().getBank(tab).getItems()[from].getId() == item.getId()) {
						getPlayer().getBank(tab).getItems()[from].setId(-1);
						getPlayer().getBankSearchingAttribtues().getSearchedBank().delete(item);
						getPlayer().getBankSearchingAttribtues().getSearchedBank().open(getPlayer(), false);
					}
				}
				return this;
			}
			getPlayer().getBank(tab).delete(item);
			getPlayer().getBankSearchingAttribtues().getSearchedBank().delete(item);
			getPlayer().getBankSearchingAttribtues().getSearchedBank().open(getPlayer(), false);
		} else {
			if (getItems()[slot].getId() != item.getId() || !contains(item.getId()))
				return this;
			if (item.getAmount() > getAmount(item.getId())) {
				item.setAmount(getAmount(item.getId()));
			}
			
			if (item.getAmount() <= 0) {
				// Placeholder
				getItems()[slot].setId(-1);
				refreshItems();
				return this;
			}

			if (to instanceof Inventory) {
				boolean withdrawAsNote = getPlayer().withdrawAsNote() && def != null && def.isNoted()
						&& item.getDefinition() != null
						&& def.getName().equalsIgnoreCase(item.getDefinition().getName())
						&& !def.getName().contains("Torva") && !def.getName().contains("Virtus")
						&& !def.getName().contains("Pernix") && !def.getName().contains("Torva");
				int checkId = withdrawAsNote ? item.getId() + 1 : item.getId();
				if (to.getAmount(checkId) + item.getAmount() > Integer.MAX_VALUE
						|| to.getAmount(checkId) + item.getAmount() <= 0) {
					getPlayer().getPacketSender()
							.sendMessage("You cannot withdraw that entire amount into your inventory.");
					return this;
				}
			}

			delete(item, slot, refresh, to);
		}
		if (getPlayer().withdrawAsNote()) {
			if (def != null && def.isNoted() && item.getDefinition() != null
					&& def.getName().equalsIgnoreCase(item.getDefinition().getName())
					&& !def.getName().contains("Torva") && !def.getName().contains("Virtus")
					&& !def.getName().contains("Pernix") && !def.getName().contains("Torva"))
				item.setId(item.getId() + 1);
			else
				getPlayer().getPacketSender().sendMessage("This item cannot be withdrawn as a note.");
		}
		to.add(item, refresh);

		if (sort)
			sortItems();
		if (refresh) {
			refreshItems();
			to.refreshItems();
		}
		return this;
	}

	@Override
	public int capacity() {
		return 500; //t

	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Bank refreshItems() {
		Bank bank = getPlayer().getBankSearchingAttribtues().isSearchingBank()
				&& getPlayer().getBankSearchingAttribtues().getSearchedBank() != null
						? getPlayer().getBankSearchingAttribtues().getSearchedBank() : this;
		getPlayer().getPacketSender().sendString(22033, "" + bank.getValidItems().size());
		getPlayer().getPacketSender().sendString(22034, "" + bank.capacity());
		getPlayer().getPacketSender().sendItemContainer(bank, INTERFACE_ID);
		getPlayer().getPacketSender().sendItemContainer(getPlayer().getInventory(), INVENTORY_INTERFACE_ID);
		sendTabs(getPlayer(), getBankTabs());
		if (!getPlayer().isBanking() || getPlayer().getInterfaceId() != 5292)
			getPlayer().getPacketSender().sendClientRightClickRemoval();
		return this;
	}

	@Override
	public Bank full() {
		getPlayer().getPacketSender().sendMessage("Not enough space in bank.");
		return this;
	}

	public static void sendTabs(Player player, Bank[] bankTabs) {
		if (bankTabs == null) {
			bankTabs = player.getBanks();
		}
		boolean moveRest = false;
		if (isEmpty(bankTabs[1])) { // tab 1 empty
			player.setBank(1, bankTabs[2]);
			player.setBank(2, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[2]) || moveRest) {
			player.setBank(2, bankTabs[3]);
			player.setBank(3, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[3]) || moveRest) {
			player.setBank(3, bankTabs[4]);
			player.setBank(4, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[4]) || moveRest) {
			player.setBank(4, bankTabs[5]);
			player.setBank(5, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[5]) || moveRest) {
			player.setBank(5, bankTabs[6]);
			player.setBank(6, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[6]) || moveRest) {
			player.setBank(6, bankTabs[7]);
			player.setBank(7, new Bank(player));
			moveRest = true;
		}
		if (isEmpty(bankTabs[7]) || moveRest) {
			player.setBank(7, bankTabs[8]);
			player.setBank(8, new Bank(player));
		}
		/*
		 * boolean moveRest = false; for(int i = 1; i <= 7; i++) {
		 * if(isEmpty(bankTabs[i)) || moveRest) { int j = i+2 > 8 ? 8 : i+2;
		 * player.setBank(i, bankTabs[j)); player.setBank(j, new Bank(player));
		 * moveRest = true; } }
		 */
		int tabs = getTabCount(player, bankTabs);
		if (player.getCurrentBankTab() > tabs)
			player.setCurrentBankTab(tabs);
		player.getPacketSender().sendString(27001, Integer.toString(tabs)).sendString(27002,
				Integer.toString(player.getCurrentBankTab()));
		int l = 1;
		for (int i = 22035; i < 22043; i++) {
			player.getPacketSender().sendItemOnInterface(i, getInterfaceModel(bankTabs[l]), 1);
			l++;
		}
		player.getPacketSender().sendString(27000, "1");
	}

	public static void depositItems(Player p, ItemContainer from, boolean ignoreReqs) {
		if (!ignoreReqs)
			if (!p.isBanking() || p.getInterfaceId() != 5292)
				return;

		if (p.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			if (Dungeoneering.doingDungeoneering(p)) {
				p.getPacketSender().sendMessage("Your items have been banked.");
			} else {
				p.getPacketSender().sendMessage("You can only withdraw items here, not deposit");
				return;
			}

		}

		for (Item it : from.getValidItems()) {
			if (p.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				if (p.getBank(p.getCurrentBankTab()).getFreeSlots() <= 322
						&& !(p.getBank(p.getCurrentBankTab()).contains(it.getId())
								&& it.getDefinition().isStackable())) {
					p.getPacketSender()
							.sendMessage("@red@Bank full. You only have 30 slots as a hc iron man for dungeoneering.");
					/*
					 * Item toDrop = new
					 * Item(ItemDefinition.forId(it.getId()).isNoted() ?
					 * (it.getId() - 1) : it.getId(), it.getAmount()); int drop
					 * = toDrop.getId(); GroundItemManager.spawnGroundItem(p,
					 * new GroundItem(new Item(drop), p.getPosition().copy(),
					 * p.getUsername(), false, 110, true, 100));
					 */
					return;
				}
			}
			if (p.getBank(p.getCurrentBankTab()).getFreeSlots() <= 0
					&& !(p.getBank(p.getCurrentBankTab()).contains(it.getId()) && it.getDefinition().isStackable())) {
				p.getPacketSender().sendMessage("Bank full.");
				return;
			}

			Item toBank = new Item(ItemDefinition.forId(it.getId()).isNoted() ? (it.getId() - 1) : it.getId(),
					it.getAmount());
			int tab = getTabForItem(p, toBank.getId());
			p.setCurrentBankTab(tab);
			int bankAmt = p.getBank(tab).getAmount(toBank.getId());
			if (bankAmt + toBank.getAmount() >= Integer.MAX_VALUE || bankAmt + toBank.getAmount() <= 0) {
				p.getPacketSender().sendMessage("Your bank cannot hold that amount of that item.");
				continue;
			}
			p.getBank(tab).add(toBank.copy(), false);
			if (p.getBankSearchingAttribtues().isSearchingBank()
					&& p.getBankSearchingAttribtues().getSearchedBank() != null)
				BankSearchAttributes.addItemToBankSearch(p, toBank);
			from.delete(it.getId(), it.getAmount(), false);
		}
		from.refreshItems();
		p.getBank(p.getCurrentBankTab()).sortItems().refreshItems();
		if (from instanceof Equipment) {
			Item weapon = p.getEquipment().get(Equipment.WEAPON_SLOT);

			Autocasting.resetAutocast(p, true);
			//p.getPacketSender().sendMessage("Autocast spell cleared.");
			WeaponInterfaces.assign(p, p.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(p);
			p.setStaffOfLightEffect(-1);
			p.getUpdateFlag().flag(Flag.APPEARANCE);
			WeaponAnimations.assign(p, weapon);

		}
	}

	public static boolean isEmpty(Bank bank) {
		return bank.sortItems().getValidItems().size() <= 0;
	}

	public static int getTabCount(Player player, Bank[] bankTabs) {
		int tabs = 0;
		for (int i = 1; i < 9; i++) {
			if (!isEmpty(bankTabs[i])) {
				tabs++;
			} else
				break;
		}
		return tabs;
	}

	public static int getTabForItem(Player player, int itemID) {
		if (ItemDefinition.forId(itemID).isNoted()) {
			itemID = Item.getUnNoted(itemID);
		}
		for (int k = 0; k < 9; k++) {
			Bank bank = player.getBank(k);
			for (int i = 0; i < bank.getValidItems().size(); i++) {
				if (bank.getItems()[i].getId() == itemID) {
					return k;
				}
			}
		}
		return player.getCurrentBankTab();
	}

	public static int getInterfaceModel(Bank bank) {
		if (bank.getItems()[0] == null)
			return -1;
		int model = bank.getItems()[0].getId();
		int amount = bank.getItems()[0].getAmount();
		if (model == 995) {
			if (amount > 9999) {
				model = 1004;
			} else if (amount > 999) {
				model = 1003;
			} else if (amount > 249) {
				model = 1002;
			} else if (amount > 99) {
				model = 1001;
			} else if (amount > 24) {
				model = 1000;
			} else if (amount > 4) {
				model = 999;
			} else if (amount > 3) {
				model = 998;
			} else if (amount > 2) {
				model = 997;
			} else if (amount > 1) {
				model = 996;
			}
		}
		return model;
	}

	/**
	 * The bank interface id.
	 */
	public static final int INTERFACE_ID = 5382;

	/**
	 * The bank inventory interface id.
	 */
	public static final int INVENTORY_INTERFACE_ID = 5064;

	/**
	 * The bank tab interfaces
	 */
	public static final int[][] BANK_TAB_INTERFACES = { { 5, 0 }, { 13, 1 }, { 26, 2 }, { 39, 3 }, { 52, 4 }, { 65, 5 },
			{ 78, 6 }, { 91, 7 }, { 104, 8 } };

	/**
	 * The item id of the selected item in the 'bank X' option
	 */

	public static class BankSearchAttributes {

		private boolean searchingBank;
		private String searchSyntax;
		private Bank searchedBank;

		public boolean isSearchingBank() {
			return searchingBank;
		}

		public BankSearchAttributes setSearchingBank(boolean searchingBank) {
			this.searchingBank = searchingBank;
			return this;
		}

		public String getSearchSyntax() {
			return searchSyntax;
		}

		public BankSearchAttributes setSearchSyntax(String searchSyntax) {
			this.searchSyntax = searchSyntax;
			return this;
		}

		public Bank getSearchedBank() {
			return searchedBank;
		}

		public BankSearchAttributes setSearchedBank(Bank searchedBank) {
			this.searchedBank = searchedBank;
			return this;
		}

		public static void beginSearch(Player player, String searchSyntax) {
			player.getPacketSender().sendClientRightClickRemoval();
			searchSyntax = (String) ItemSearch.getFixedSyntax(searchSyntax)[0];
			player.getPacketSender().sendString(5383, "Searching for: " + searchSyntax + "");
			player.getBankSearchingAttribtues().setSearchingBank(true).setSearchSyntax(searchSyntax);
			player.setCurrentBankTab(0).setNoteWithdrawal(false);
			player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab())).sendString(27000,
					"1");
			player.getBankSearchingAttribtues().setSearchedBank(new Bank(player));
			for (Bank bank : player.getBanks()) {
				bank.sortItems();
				for (Item bankedItem : bank.getValidItems())
					addItemToBankSearch(player, bankedItem);
			}
			player.getBankSearchingAttribtues().getSearchedBank().open();
			player.getPacketSender().sendString(5383, "Showing results found for: " + searchSyntax + "");
		}

		public static void addItemToBankSearch(Player player, Item item) {
			if (player.getBankSearchingAttribtues().getSearchSyntax() != null) {
				if (item.getDefinition().getName().toLowerCase()
						.contains(player.getBankSearchingAttribtues().getSearchSyntax())) {
					if (player.getBankSearchingAttribtues().getSearchedBank().getFreeSlots() == 0)
						return;
					player.getBankSearchingAttribtues().getSearchedBank().add(item, true);
				}
			}
		}

		public static void stopSearch(Player player, boolean openBank) {
			player.getPacketSender().sendClientRightClickRemoval();
			player.getBankSearchingAttribtues().setSearchedBank(null).setSearchingBank(false).setSearchSyntax(null);
			player.setCurrentBankTab(0).setNoteWithdrawal(false);
			player.getPacketSender().sendString(27002, Integer.toString(0)).sendString(27000, "1").sendConfig(117, 0)
					.sendString(5383, "        The Bank of Zamron");
			if (openBank)
				player.getBank(0).open(player, false);
			player.setInputHandling(null);
		}

		public static void withdrawFromSearch(Player player, Item item) {
			if (player.isBanking() && player.getBankSearchingAttribtues().isSearchingBank()) {
				int tab = Bank.getTabForItem(player, item.getId());
				if (tab == player.getCurrentBankTab() && !player.getBank(tab).contains(item.getId()))
					return;
			}
		}
	}

	/**
	 * The items in this container.
	 */
	private Item[] items;

	public Item[] array() {
		return items.clone();
	}

	public Bank[] getBankTabs() {
		return bankTabs;
	}

	public void setBankTabs(Bank[] bankTabs) {
		this.bankTabs = bankTabs;
	}
	public ItemContainer getBank(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}