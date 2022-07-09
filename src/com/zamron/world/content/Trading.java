package com.zamron.world.content;

import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.model.Locations;
import com.zamron.model.Locations.Location;
import com.zamron.model.PlayerRights;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: @Gabbe Warning: This crap is so messy and ugly. Will redo it once I
 *          get some time over. Should be dupe-free.
 */

public class Trading {

	private Player player;

	public Trading(Player p) {
		this.player = p;
	}

	public void requestTrade(Player player2) {
		if (player == null || player2 == null || player.getConstitution() <= 0 || player2.getConstitution() <= 0
				|| player.isTeleporting() || player2.isTeleporting())
			return;

		// player.getSummoning().getFamiliar().getSummonNpc()
		// NPC npc = player.getSummoning().getFamiliar().getSummonNpc();
		// if (npc != null) {
		// player.pickUp(player, npc);

		if (player.getGameMode() == GameMode.GROUP_IRONMAN && !player.isGim()) {
			player.getPacketSender().sendMessage("Group_Ironman players are not allowed to trade.");
			return;
		}
		if (player.getRights() == PlayerRights.YOUTUBER) {
			player.getPacketSender().sendMessage("Youtubers are not allowed to trade.");
			return;
		}

		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("Hardcore-ironman-players are not allowed to trade.");
			return;
		}
		if (player.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender().sendMessage("ironman-players are not allowed to trade.");
			return;
		}
		if (player2.getGameMode() == GameMode.GROUP_IRONMAN && !player.isGim()) {
			player.getPacketSender().sendMessage("That player is a Group_ironman player and can therefore not trade.");
			return;
		}
		if (player2.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender().sendMessage("That player is a ironman player and can therefore not trade.");
			return;
		}
		if (player2.getRights() == PlayerRights.YOUTUBER) {
			player.getPacketSender().sendMessage("This player is a Youtuber and can therefore not trade.");
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		if (player2.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("That player is an Ironman player and can therefore not stake.");
			return;
		}

		if (player.isGim()) {
			boolean hasGroup = player.getGroupIronmanGroup() != null;
			if (hasGroup) {
				if (!player.getGroupIronmanGroup().getMembers().contains(player2.getUsername())) {
					player.sendMessage("You can only trade your own group members");
					return;
				} else {
					player.sendMessage("You can not trade players outside your group");
					return;
				}

			}
		}

		/*
		 * if(Misc.getMinutesPlayed(player) < 15) { player.getPacketSender().
		 * sendMessage("You must have played for at least 15 minutes in order to trade someone."
		 * ); return; }
		 */
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			BankPin.init(player, false);
			return;
		}
		/*
		 * if(player.getHostAdress().equals(player2.getHostAdress()) &&
		 * player.getRights() != PlayerRights.OWNER && player.getRights() !=
		 * PlayerRights.DEVELOPER) { player.getPacketSender().
		 * sendMessage("Same IP-adress found. You cannot trade yourself from the same IP."
		 * ); return; }
		 */
		if (System.currentTimeMillis() - lastTradeSent < 5000 && !inTrade()) {
			player.getPacketSender().sendMessage("You're sending trade requests too frequently. Please slow down.");
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You are far too busy to trade at the moment!");
			return;
		}
		if (inTrade()) {
			declineTrade(true);
			return;
		}
		if (player.getLocation() == Location.GODWARS_DUNGEON
				&& player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()
				&& !player2.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
			player.getPacketSender().sendMessage("You cannot reach that.");
			return;
		}
		if (player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (player.busy()) {
			return;
		}
		if (player2.busy() || player2.getInterfaceId() > 0 || player2.getTrading().inTrade() || player2.isBanking()
				|| player2.isShopping()/* || player2.getDueling().inDuelScreen || FightPit.inFightPits(player2) */) {
			player.getPacketSender().sendMessage("The other player is currently busy.");
			return;
		}
		if (player.getInterfaceId() > 0 || inTrade() || player.isBanking()
				|| player.isShopping()/* || player.getDueling().inDuelScreen || FightPit.inFightPits(player) */) {
			player.getPacketSender().sendMessage("You are currently unable to trade another player.");
			if (player.getInterfaceId() > 0)
				player.getPacketSender()
						.sendMessage("Please close all open interfaces before requesting to open another one.");
			return;
		}
		tradeWith = player2.getIndex();
		if (getTradeWith() == player.getIndex())
			return;
		if (!Locations.goodDistance(player.getPosition().getX(), player.getPosition().getY(),
				player2.getPosition().getX(), player2.getPosition().getY(), 2)) {
			player.getPacketSender().sendMessage("Please get closer to request a trade.");
			return;
		}
		if (!inTrade() && player2.getTrading().tradeRequested()
				&& player2.getTrading().getTradeWith() == player.getIndex()) {
			openTrade();
			player2.getTrading().openTrade();
		} else if (!inTrade()) {
			setTradeRequested(true);
			player.getPacketSender().sendMessage("You've sent a trade request to " + player2.getUsername() + ".");
			player2.getPacketSender().sendMessage(player.getUsername() + ":tradereq:");
		}
		lastTradeSent = System.currentTimeMillis();
	}

	public void openTrade() {
		player.getPacketSender().sendClientRightClickRemoval();
		Player player2 = World.getPlayers().get(getTradeWith());
		// player.getSummoning().getFamiliar().getSummonNpc()
		// NPC npc = player.getSummoning().getFamiliar().getSummonNpc();
		// if (npc != null) {
		// player.pickUp(player, npc);

		if (player == null || player2 == null || getTradeWith() == player.getIndex() || player.isBanking())
			return;
		setTrading(true);
		setTradeRequested(false);
		setCanOffer(true);
		setTradeStatus(1);
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player2.getPacketSender().sendInterfaceItems(3415, player2.getTrading().offeredItems);
		sendText(player2);
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player.getPacketSender().sendInterfaceItems(3416, player2.getTrading().offeredItems);
		player.getMovementQueue().reset();
		inTradeWith = player2.getIndex();

		player.getPacketSender().sendRichPresenceState("Trading: " + player2.getUsername());
		player.getPacketSender().sendRichPresenceSmallPictureText("CB LVL: " + player.getSkillManager().getCombatLevel());
		player.getPacketSender().sendSmallImageKey("trade");

		player2.getPacketSender().sendRichPresenceState("Trading: " + player.getUsername());
		player2.getPacketSender().sendRichPresenceSmallPictureText("CB LVL: " + player2.getSkillManager().getCombatLevel());
		player2.getPacketSender().sendSmallImageKey("trade");

	}

	public void declineTrade(boolean tellOther) {
		Player player2 = getTradeWith() >= 0 && !(getTradeWith() > World.getPlayers().capacity())
				? World.getPlayers().get(getTradeWith())
				: null;
		for (Item item : offeredItems) {
			if (item.getAmount() < 1)
				continue;
			player.getInventory().add(item);
		}
		offeredItems.clear();
		if (tellOther && getTradeWith() > -1) {
			if (player2 == null)
				return;
			player2.getTrading().declineTrade(false);
			player2.getPacketSender().sendMessage("Other player declined the trade.");
		}
		resetTrade();
	}

	public void sendText(Player player2) {
		if (player2 == null)
			return;
		player2.getPacketSender().sendString(3451, "" + Misc.formatPlayerName(player.getUsername()) + "");
		player2.getPacketSender().sendString(3417, "Trading with: " + Misc.formatPlayerName(player.getUsername()) + "");
		player.getPacketSender().sendString(3451, "" + Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(3417, "Trading with: " + Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(3431, "");
		player.getPacketSender().sendString(3535, "Are you sure you want to make this trade?");
		player.getPacketSender().sendInterfaceSet(3323, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
	}

	private void sendMessageTradeFailedBecauseInventoryFull(Player player2) {
		player.sendMessage(
				"Your trade could not be processed because " + player2.getUsername() + " has no free inventory space");
		player2.sendMessage("Your trade could not be processed because you have no free inventory space");
		player.getPacketSender().closeAllWindows();
		player2.getPacketSender().closeAllWindows();
	}

	public void tradeItem(int itemId, int amount, int slot) {
		if (slot < 0)
			return;
		if (!getCanOffer())
			return;
		Player player2 = World.getPlayers().get(getTradeWith());
		if (player2 == null || player == null)
			return;

		/*
		 * if(player.getNewPlayerDelay() > 0 && player.getRights().ordinal() == 0) {
		 * player.getPacketSender().sendMessage("You must wait another "+player.
		 * getNewPlayerDelay() / 60+" minutes before being able to trade items.");
		 * return; }
		 */
		falseTradeConfirm();
		player.getPacketSender().sendClientRightClickRemoval();
		if (!inTrade() || !canOffer) {
			declineTrade(true);
			return;
		}
		if (!player.getInventory().contains(itemId))
			return;
		if (slot >= player.getInventory().capacity() || player.getInventory().getItems()[slot].getId() != itemId
				|| player.getInventory().getItems()[slot].getAmount() <= 0)
			return;
		Item itemToTrade = player.getInventory().getItems()[slot];
		if (itemToTrade.getId() != itemId)
			return;
		if (player.getInventory().getAmount(itemId) < amount) {
			amount = player.getInventory().getAmount(itemId);
			if (amount == 0 || player.getInventory().getAmount(itemId) < amount) {
				return;
			}
		}
		if (!itemToTrade.getDefinition().isStackable()) {
			if (player2HasFullInventory(player2)) {
				sendMessageTradeFailedBecauseInventoryFull(player2);
				return;
			}
			for (int a = 0; a < amount && a < 28; a++) {
				if (player.getInventory().getAmount(itemId) >= 1) {
					offeredItems.add(new Item(itemId, 1));
					player.getInventory().delete(itemId, 1);
				}
			}
		} else if (itemToTrade.getDefinition().isStackable()) {
			if (player2HasFullInventoryWithoutItemToTrade(player2, itemToTrade)) {
				sendMessageTradeFailedBecauseInventoryFull(player2);
				return;
			}
			boolean itemInTrade = false;
			for (Item item : offeredItems) {
				if (item.getId() == itemId) {
					itemInTrade = true;
					item.setAmount(item.getAmount() + amount);
					player.getInventory().delete(itemId, amount);
					break;
				}
			}
			if (!itemInTrade) {
				offeredItems.add(new Item(itemId, amount));
				player.getInventory().delete(itemId, amount);
			}
		}
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3416, player2.getTrading().offeredItems);
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player.getPacketSender().sendString(3431, "");
		acceptedTrade = false;
		tradeConfirmed = false;
		tradeConfirmed2 = false;
		player2.getPacketSender().sendInterfaceItems(3416, offeredItems);
		player2.getPacketSender().sendString(3431, "");
		player2.getTrading().acceptedTrade = false;
		player2.getTrading().tradeConfirmed = false;
		player2.getTrading().tradeConfirmed2 = false;
		sendText(player2);
	}

	public void removeTradedItem(int itemId, int amount) {
		if (!getCanOffer())
			return;
		Player player2 = World.getPlayers().get(getTradeWith());
		if (player2 == null)
			return;
		if (!inTrade() || !canOffer) {
			declineTrade(false);
			return;
		}
		falseTradeConfirm();
		ItemDefinition def = ItemDefinition.forId(itemId);
		if (!def.isStackable()) {
			if (amount > 28)
				amount = 28;
			for (int a = 0; a < amount; a++) {
				for (Item item : offeredItems) {
					if (item.getId() == itemId) {
						if (!item.getDefinition().isStackable()) {
							offeredItems.remove(item);
							player.getInventory().add(itemId, 1);
						}
						break;
					}
				}
			}
		} else
			for (Item item : offeredItems) {
				if (item.getId() == itemId) {
					if (item.getDefinition().isStackable()) {
						if (item.getAmount() > amount) {
							item.setAmount(item.getAmount() - amount);
							player.getInventory().add(itemId, amount);
						} else {
							amount = item.getAmount();
							offeredItems.remove(item);
							player.getInventory().add(itemId, amount);
						}
					}
					break;
				}
			}
		falseTradeConfirm();
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3416, player2.getTrading().offeredItems);
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player2.getPacketSender().sendInterfaceItems(3416, offeredItems);
		sendText(player2);
		player.getPacketSender().sendString(3431, "");
		player2.getPacketSender().sendString(3431, "");
		player.getPacketSender().sendClientRightClickRemoval();
	}

	private boolean player2HasFullInventory(Player player2) {
		Item[] itemsPlayer2 = player2.getInventory().getItems();
		return Arrays.stream(itemsPlayer2).noneMatch(x -> x.getId() == -1);
	}

	private boolean player2HasFullInventoryWithoutItemToTrade(Player player2, Item itemToTrade) {
		Item[] itemsPlayer2 = player2.getInventory().getItems();
		if (player2HasFullInventory(player2)) {
			return Arrays.stream(itemsPlayer2).noneMatch(x -> x.getId() == itemToTrade.getId());
		}
		return false;
	}

	public void acceptTrade(int stage) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (getTradeWith() < 0) {
			declineTrade(false);
			return;
		}
		Player player2 = World.getPlayers().get(getTradeWith());
		if (player == null || player2 == null) {
			declineTrade(false);
			return;
		}
		if (!twoTraders(player, player2)) {
			player.getPacketSender().sendMessage("An error has occured. Please try re-trading the player.");
			return;
		}
		if (stage == 2) {
			if (!inTrade() || !player2.getTrading().inTrade() || !player2.getTrading().tradeConfirmed) {
				declineTrade(true);
				return;
			}
			if (!tradeConfirmed)
				return;
			acceptedTrade = true;
			tradeConfirmed2 = true;
			player2.getPacketSender().sendString(3535, "Other player has accepted.");
			player.getPacketSender().sendString(3535, "Waiting for other player...");
			if (inTrade() && player2.getTrading().tradeConfirmed2) {
				acceptedTrade = true;
				giveItems();
				player.getPacketSender().sendMessage("Trade accepted.");
				player2.getTrading().acceptedTrade = true;
				player2.getTrading().giveItems();
				player2.getPacketSender().sendMessage("Trade accepted.");
				resetTrade();
				player2.getTrading().resetTrade();
			}
		} else if (stage == 1) {
			player2.getTrading().goodTrade = true;
			player2.getPacketSender().sendString(3431, "Other player has accepted.");
			goodTrade = true;
			player.getPacketSender().sendString(3431, "Waiting for other player...");

			tradeConfirmed = true;
			if (inTrade() && player2.getTrading().tradeConfirmed && player2.getTrading().goodTrade && goodTrade) {
				confirmScreen();
				player2.getTrading().confirmScreen();
			}
		}
		player.getClickDelay().reset();
	}

	public void confirmScreen() {
		Player player2 = World.getPlayers().get(getTradeWith());
		if (player2 == null)
			return;
		setCanOffer(false);
		player.getInventory().refreshItems();
		String SendTrade = "Absolutely nothing!";
		String SendAmount;
		int Count = 0;
		for (Item item : offeredItems) {
			if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
				SendAmount = "@cya@" + (item.getAmount() / 1000) + "K @whi@(" + Misc.format(item.getAmount()) + ")";
			} else if (item.getAmount() >= 1000000) {
				SendAmount = "@gre@" + (item.getAmount() / 1000000) + " million @whi@(" + Misc.format(item.getAmount())
						+ ")";
			} else {
				SendAmount = "" + Misc.format(item.getAmount());
			}
			if (Count == 0) {
				SendTrade = item.getDefinition().getName().replaceAll("_", " ");
			} else
				SendTrade = SendTrade + "\\n" + item.getDefinition().getName().replaceAll("_", " ");
			if (item.getDefinition().isStackable())
				SendTrade = SendTrade + " x " + SendAmount;
			Count++;
		}

		player.getPacketSender().sendString(3557, SendTrade);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;
		for (Item item : player2.getTrading().offeredItems) {
			if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
				SendAmount = "@cya@" + (item.getAmount() / 1000) + "K @whi@(" + Misc.format(item.getAmount()) + ")";
			} else if (item.getAmount() >= 1000000) {
				SendAmount = "@gre@" + (item.getAmount() / 1000000) + " million @whi@(" + Misc.format(item.getAmount())
						+ ")";
			} else {
				SendAmount = "" + Misc.format(item.getAmount());
			}
			if (Count == 0) {
				SendTrade = item.getDefinition().getName().replaceAll("_", " ");
			} else
				SendTrade = SendTrade + "\\n" + item.getDefinition().getName().replaceAll("_", " ");
			if (item.getDefinition().isStackable())
				SendTrade = SendTrade + " x " + SendAmount;
			Count++;
		}
		player.getPacketSender().sendString(3558, SendTrade);
		player.getPacketSender().sendInterfaceSet(3443, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
		/*
		 * Remove all tabs!
		 */
		// player.getPacketSender().sendInterfaceSet(3443, Inventory.INTERFACE_ID);
		// player.getPacketSender().sendItemContainer(player.getInventory(),
		// Inventory.INTERFACE_ID);
	}

	public void giveItems() {
		Player player2 = World.getPlayers().get(getTradeWith());
		if (player2 == null)
			return;
		if (!inTrade() || !player2.getTrading().inTrade())
			return;
		try {
			for (Item item : player2.getTrading().offeredItems) {
				player.getInventory().add(item);
			}
			for (Item item : player.getTrading().offeredItems) {
				if (item.getId() == 5520) {
					if (item.getAmount() >= 1000000) {
						World.sendMessageNonDiscord("@red@[ADMIN]@red@ " + player.getUsername() + " Gave " + player2.getUsername()
								+ ". Id: " + item.getId() + "(cash), amount: "
								+ Misc.insertCommasToNumber(String.valueOf(item.getAmount())));
					}
				}
			}

			// logs
			for (Item item : player.getTrading().offeredItems) {
				PlayerLogs.log(player.getUsername(), "Gave item in trade to " + player2.getUsername() + ". Id: "
						+ item.getId() + ", amount: " + item.getAmount());
			}
			for (Item item : player2.getTrading().offeredItems) {
				PlayerLogs.log(player.getUsername(),
						"Receiving item from trade. Id: " + item.getId() + ", amount: " + item.getAmount());
			}
		} catch (Exception ignored) {
		}
	}

	public void resetTrade() {
		inTradeWith = -1;
		offeredItems.clear();
		setCanOffer(true);
		setTrading(false);
		setTradeWith(-1);
		setTradeStatus(0);
		lastTradeSent = 0;
		acceptedTrade = false;
		tradeConfirmed = false;
		tradeConfirmed2 = false;
		tradeRequested = false;
		canOffer = true;
		goodTrade = false;
		player.getPacketSender().sendString(3535, "Are you sure you want to make this trade?");
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().sendInterfaceRemoval();
	}

	private boolean falseTradeConfirm() {
		Player player2 = World.getPlayers().get(getTradeWith());
		return tradeConfirmed = player2.getTrading().tradeConfirmed = false;
	}

	public CopyOnWriteArrayList<Item> offeredItems = new CopyOnWriteArrayList<Item>();
	private boolean inTrade = false;
	private boolean tradeRequested = false;
	private int tradeWith = -1;
	private int tradeStatus;
	public long lastTradeSent, lastAction;
	private boolean canOffer = true;
	public boolean tradeConfirmed = false;
	public boolean tradeConfirmed2 = false;
	public boolean acceptedTrade = false;
	public boolean goodTrade = false;

	public void setTrading(boolean trading) {
		this.inTrade = trading;
	}

	public boolean inTrade() {
		return this.inTrade;
	}

	public void setTradeRequested(boolean tradeRequested) {
		this.tradeRequested = tradeRequested;
	}

	public boolean tradeRequested() {
		return this.tradeRequested;
	}

	public void setTradeWith(int tradeWith) {
		this.tradeWith = tradeWith;
	}

	public int getTradeWith() {
		return this.tradeWith;
	}

	public void setTradeStatus(int status) {
		this.tradeStatus = status;
	}

	public int getTradeStatus() {
		return this.tradeStatus;
	}

	public void setCanOffer(boolean canOffer) {
		this.canOffer = canOffer;
	}

	public boolean getCanOffer() {
		return canOffer && player.getInterfaceId() == 3323 && !player.isBanking() && !player.getPriceChecker().isOpen();
	}

	public int inTradeWith = -1;

	/**
	 * Checks if two players are the only ones in a trade.
	 *
	 * @param p1 Player1 to check if he's 1/2 player in trade.
	 * @param p2 Player2 to check if he's 2/2 player in trade.
	 * @return true if only two people are in the trade.
	 */
	public static boolean twoTraders(Player p1, Player p2) {
		int count = 0;
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getTrading().inTradeWith == p1.getIndex() || player.getTrading().inTradeWith == p2.getIndex()) {
				count++;
			}
		}
		return count == 2;
	}

	/**
	 * The trade interface id.
	 */
	public static final int INTERFACE_ID = 3322;

	/**
	 * The trade interface id for removing items.
	 */
	public static final int INTERFACE_REMOVAL_ID = 3415;

}
