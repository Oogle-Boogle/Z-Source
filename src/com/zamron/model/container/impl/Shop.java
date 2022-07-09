package com.zamron.model.container.impl;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.ShopRestockTask;
import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.model.Skill;
import com.zamron.model.container.ItemContainer;
import com.zamron.model.container.StackType;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.input.impl.EnterAmountToBuyFromShop;
import com.zamron.model.input.impl.EnterAmountToSellToShop;
import com.zamron.util.JsonLoader;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.minigames.impl.RecipeForDisaster;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.binding.ObjectExpression;

/**
 * Messy but perfect Shop System
 *
 * @author Gabriel Hannason
 */

public class Shop extends ItemContainer {

	/*
	 * The shop constructor
	 */
	
	private static final String PREFIX = "1b coins";
	
	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 102)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 100 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	private final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public static String formatNumber(int number) {
		return NumberFormat.getInstance().format(number);
	}

	public Item getCurrency() {
		return currency;
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	private boolean restockingItems;

	public boolean isRestockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	/**
	 * Opens a shop for a player
	 *
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		setPlayer(player);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
		}
	}

	/**
	 * Checks a value of an item in a shop
	 *
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 10835)
			return;
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}
		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
					if (id == TOKKUL_EXCHANGE_STORE
							|| id == STARDUST_STORE
							|| id == RAIDSTORE
							|| id == AGILITY_TICKET_STORE
							|| id == TOKEN_STORE
							|| id == GENERAL_STORE
							|| id == GRAVEYARD_STORE
							|| id == DBZ_TOKEN_SHOP
							|| id == SANTAS_STORE
							|| id == STARTER_STORE
							|| id == LOYALTYPOINT_STORE
							|| id == DARKLORD_TOKEN_SHOP
							|| id == WOODCUTTING
							|| id == FIREMAKING_SHOP
							|| id == RAIDS_FISHING_STORE
							|| id == GAMBLING_STORE
							|| id == HALLOWEEN_SHOP
							|| id == AFK_SHOP
					)
					{
						Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + formatNumber((int) finalValue) + " " + s + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}
		
		if (id == CONSUMABLES) {
			player.sendMessage("You cannot sell items to this store.");
			return;
		}
		if (id == WOODCUTTING) {
			player.sendMessage("You cannot sell items to this store.");
			return;
		}
		if (id == FIREMAKING_SHOP) {
			player.sendMessage("You cannot sell items to this store.");
			return;
		}
		if (id == WOODCUTTING || id == RAIDS_FISHING_STORE || id == RAIDSTORE|| id == GENERAL_STORE|| id == HALLOWEEN_SHOP) {
			player.sendMessage("You cannot sell items to this store.");
			return;
		}

		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if (amountToSell == 0)
			return;
		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "+s+
		 * " at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (getCurrency().getId() > 0 && id != 119) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}

		if (itemValue <= 0)
			return;
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}

		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
					} else {
						// Return points here
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					} else {
						// Return points here
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
		}
		if (customShop) {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't do this whilst Dungeoneering");
				return this;
			}
			if (player.getGameMode() == GameMode.IRONMAN) {
				player.getPacketSender()
						.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
			if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
				player.getPacketSender()
						.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("Hardcore-ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
		}
		if (!shopSellsItem(item))
			return this;

		if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {

			player.getPacketSender()
					.sendMessage("The shop can't be 1 items and needs to regenerate some items first..");

		}

		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (id == 21) { // farming cheapfix
			if (getItems()[slot].getAmount() - amountBuying <= 1) {
				amountBuying = getItems()[slot].getAmount() - 1;
				while (getItems()[slot].getAmount() - amountBuying <= 1) {
					if (getItems()[slot].getAmount() - amountBuying == 1)
						break;
					amountBuying--;
				}
			}
		}
		if (getItems()[slot].getAmount() < amountBuying) {
			amountBuying = getItems()[slot].getAmount() - 101;
		}
		if (amountBuying == 0)
			return this;

		if (amountBuying > 25000) {
			player.getPacketSender().sendMessage(
					"You can only buy 25000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			return this;
		}
		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		long playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();

			if (currency.getId() == 995) {
				if (player.getInventory().contains(995, value)) {
					player.getInventory().delete(995, value);
				} else {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					int amountRequired = 0;
					if (value <= 1000000000) {
						amountRequired = 1;
					} else if (value <= 2000000000) {
						amountRequired = 2;
					} else {
						amountRequired = 3;
					}

					if (playerCurrencyAmount >= amountRequired) {
						player.setMoneyInPouch(player.getMoneyInPouch() - amountRequired);
						player.getPacketSender().sendString(8135, String.valueOf(player.getMoneyInPouch())); //Update the money pouch
						int coinsToAdd = (amountRequired * 1000000000) - value;
						player.getInventory().add(995, coinsToAdd);
						player.getPacketSender().sendMessage("Your change is " + coinsToAdd);
					} else {
						player.getPacketSender().sendMessage("You cannot afford this item.");
					}

				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == TOKKUL_EXCHANGE_STORE
						|| id == RAIDSTORE
						|| id == AGILITY_TICKET_STORE
						|| id == TOKEN_STORE
						|| id == GRAVEYARD_STORE
						|| id == DBZ_TOKEN_SHOP
						|| id == SANTAS_STORE
						|| id == STARTER_STORE
						|| id == LOYALTYPOINT_STORE
						|| id == DARKLORD_TOKEN_SHOP
						|| id == AMONG_REWARDS_STORE
						|| id == FIREMAKING_SHOP
						|| id == WOODCUTTING
						|| id == RAIDS_FISHING_STORE
						|| id == GAMBLING_STORE
						|| id == AFK_SHOP
				) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == DUNGEON_POINTS_STORE) {
				playerCurrencyAmount = player.getDungeonPoints();
			} else if (id == LOYALTYPOINT_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getLoyaltyPoints();
			} else if (id == VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
			} else if (id == AMONG_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getAmongPoints();
			} else if (id == DUNGEONEERING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
			} else if (id == DONATOR_STORE_1) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == VOID_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getCustompestcontrolpoints();
			} else if (id == SKILLING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSkillPoints();
			} else if (id == TRIVIA_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getTriviaPoints();
			} else if (id == BOSS_POINT_STORE) {
				playerCurrencyAmount = player.getBossPoints();
			} else if (id == DONATOR_PET_SHOP) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == PRESTIGE_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getPrestigePoints();
			} else if (id == SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			} else if (id == CUSTOMSLAYER_POINT_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			}
			else if (id == RAIDSTORE) {
			playerCurrencyAmount = player.getPointsHandler().getRaidPoints();
			}
			
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender()
					.sendMessage("You do not have enough "
							+ ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s")))
							+ " to purchase this item.");
			return this;
		}
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		} else if (id == GAMBLING_STORE) {
			if (item.getId() == 15084 || item.getId() == 299) {
				if (player.getRights() == PlayerRights.PLAYER) {
					player.getPacketSender().sendMessage("You need to be a member to use these items.");
					return this;
				}
			}
		}

		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() < amountBuying) {
				amountBuying = getItems()[slot].getAmount() - 101;

			}

			if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {

				player.getPacketSender()
						.sendMessage("The shop can't be below 1 items and needs to regenerate some items first...");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(currency.getId(), value, false);
						}
					} else {
						if (id == DUNGEON_POINTS_STORE) {
							player.setDungeonPoints(player.getDungeonPoints() - value);
						} else if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value, true);
						} else if (id == LOYALTYPOINT_STORE) {
							player.getPointsHandler().setLoyaltyPoints(-value, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value, true);
						} else if (id == AMONG_REWARDS_STORE) {
							player.getPointsHandler().setAmongPoints(-value, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == SKILLING_STORE) {
							player.getPointsHandler().setSkillPoints(-value, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - value);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value, true);
						} else if (id == VOID_STORE) {
							player.getPointsHandler().setCustompestcontrolpoints(-value, true);
						} else if (id == DONATOR_PET_SHOP) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						} else if (id == RAIDSTORE) {
							player.getPointsHandler().setRaidPoints(-value, true);
						} else if (id == CUSTOMSLAYER_POINT_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						}
					}
					

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					int canBeBought = (int) playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(currency.getId(), value * canBeBought, false);
						}
					} else {
						if (id == DUNGEON_POINTS_STORE) {
							player.setDungeonPoints(player.getDungeonPoints() -value * canBeBought);
						} else if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value * canBeBought, true);
						} else if (id == LOYALTYPOINT_STORE) {
							player.getPointsHandler().setLoyaltyPoints(-value * canBeBought, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
						} else if (id == AMONG_REWARDS_STORE) {
							player.getPointsHandler().setAmongPoints(-value * canBeBought, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == SKILLING_STORE) {
							player.getPointsHandler().setSkillPoints(-value * canBeBought, true);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value * canBeBought, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - (value * canBeBought));
						} else if (id == DONATOR_PET_SHOP) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == VOID_STORE) {
							player.getPointsHandler().setCustompestcontrolpoints(-value * canBeBought, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value * canBeBought, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						} else if (id == CUSTOMSLAYER_POINT_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						} else if (id == RAIDSTORE) {
							player.getPointsHandler().setRaidPoints(-value * canBeBought, true);
						}
					}
					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					playerCurrencyAmount -= value;
					break;
				} else {
					break;
				}
			}
			amountBuying--;
		}
		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
				// the
				// money
				// pouch
			}
		} else {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 *
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
			publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 100;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public void restockShop() {
		for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
			int currentStockAmount = getItems()[shopItemIndex].getAmount();
			add(getItems()[shopItemIndex].getId(), getOriginalStock()[shopItemIndex].getAmount() - currentStockAmount);
			// publicRefresh();
			refreshItems();
		}

	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE || shopId == SELL_FOR_TAXBAGS_SHOP)
			return true;
		if (shopId == DUNGEONEERING_STORE
				|| shopId == BOSS_POINT_STORE
				|| shopId == SANTAS_STORE
				|| shopId == RAIDSTORE
				|| shopId == SKILLING_STORE
				|| shopId == STARTER_STORE
				|| shopId == TRIVIA_STORE
				|| shopId == DONATOR_STORE_1
				|| shopId == DONATOR_PET_SHOP
				|| shopId == VOID_STORE
				|| shopId == PKING_REWARDS_STORE
				|| shopId == VOTING_REWARDS_STORE
				|| shopId == DUNGEON_POINTS_STORE
				|| shopId == RECIPE_FOR_DISASTER_STORE
				|| shopId == DBZ_TOKEN_SHOP
				|| shopId == AGILITY_TICKET_STORE
				|| shopId == TOKEN_STORE
				|| shopId == GRAVEYARD_STORE
				|| shopId == TOKKUL_EXCHANGE_STORE
				|| shopId == PRESTIGE_STORE
				|| shopId == STARDUST_STORE
				|| shopId == AMONG_REWARDS_STORE
				|| shopId == SLAYER_STORE
				|| shopId == GAMBLING_STORE
				|| shopId == CUSTOMSLAYER_POINT_STORE
				|| shopId == CONSUMABLES
				|| shopId == FIREMAKING_SHOP
				|| shopId == WOODCUTTING
				|| shopId == DARKLORD_TOKEN_SHOP
				|| shopId == RAIDSTORE
				|| shopId == RAIDS_FISHING_STORE
				|| shopId == FIREMAKING_SHOP
				|| shopId == WOODCUTTING
				|| shopId == TAX_BAG_SHOP)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static int getSantasStore() {
		return SANTAS_STORE;
	}

	public static int getSkillingStore() {
		return SKILLING_STORE;
	}

	public static int getStarterStore() {
		return STARTER_STORE;
	}

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Map<Integer, Shop> getShops() {
			return shops;
		}
		
		public static JsonLoader parseShops() {
			return new JsonLoader() {
				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					if (id != 0)
						////System.out.println("Id=" + id + " " + name + " " + currency + " " + "");
					shops.put(id, new Shop(null, id, name, currency, items));
				}

				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}
			};
		}

		public static Object[] getCustomShopData(int shop, int item) {

			if (shop == DUNGEON_POINTS_STORE) {
				switch (item) {
					case 9999: //ITEM BS
						return new Object[] { 5, "Dungeon Points" };
					case 14808: //scroll of praise
						return new Object[] { 50, "Dungeon Points" };
				}
				return new Object[] { 100, "Dungeon Points" }; //Default if you forget to set a value
			} else if (shop == VOTING_REWARDS_STORE) {
				switch (item) {
				case 11179: //AOE instance
                    return new Object[] { 2, "Voting Points" };
					case 12845:
					case 12846:
					case 12847:
						return new Object[] {3, "Voting Points"};
					case 9078: //Agg book
						return new Object[] {100, "Voting Points"};
					case 15374: //Supreme box
						return new Object[] {5, "Voting Points"};
					case 19935: //5 bond
						return new Object[] {12, "Voting Points"};
					case 7759:
					case 7760:
					case 7761:
					case 7762:
					case 7763:
					case 7764:
					case 7765:
						return new Object[] {25, "Voting Points"};
				case 19890: //2h double drops scroll
				case 14808: //scroll of praise 
							return new Object[] {8, "Voting Points" };
				}
				return new Object[] { 100, "Voting Points" };
				
			} else if (shop == SELL_FOR_TAXBAGS_SHOP) {
				switch (item) {
					case 952:
						return new Object[] {100, "1b coins"};
				//Thiev items
					case 18199:
						return new Object[] {100, "1b coins"};
					case 15009:
						return new Object[] {200, "1b coins"};
					case 17401:
						return new Object[] {300, "1b coins"};
					case 1389:
						return new Object[] {500, "1b coins"};
					case 11998:
						return new Object[] {1000, "1b coins"};

						//Tier 1
					case 15373:
					case 2577:
					case 902:
					case 903:
					case 904:
					case 905:
					case 3082:
					case 5161:
						return new Object[] { 500, "1b coins"};

					case 20016:
					case 20017:
					case 20018:
					case 20021:
					case 20022:
					case 18910:
					case 10720:
					case 14006:
					case 5160:
					case 5157:
						return new Object[] {850, "1b coins"};

					case 17911:
					case 17908:
					case 17909:
					case 11732:
					case 3928:
					case 3932:
					case 4775:
					case 19892:
						return new Object[] {1250, "1b coins"};

						//tier 2
					case 2749:
					case 2750:
					case 2751:
					case 2752:
					case 2753:
					case 2754:
					case 2755:
						return new Object[] {2000, "1b coins"};
					//tier 3
					case 19721:
					case 19722:
					case 19723:
					case 19724:
					case 19725:
					case 19734:
					case 19736:
					case 15418:
					case 18892:
					case 19468:
					case 15398:
					case 18363:
					case 18377:
					case 2572:
					case 16137:
					case 11076:
					case 11077:
						return new Object[] { 2850, "1b coins"};
				}
			} else if (shop == GAMBLING_STORE) {
				switch (item) {
				case 15084:
					return new Object[] { 100000, "1b coins" };
				}
				return new Object[] { 70000, "Blood Money" };
			} else if (shop == RAIDS_FISHING_STORE) {
				switch(item) {

					case 4777:
						return new Object[]{25, "Fish tokens"};
					case 19479:
						return new Object[]{20, "Fish tokens"};
					case 18950:
						return new Object[]{13, "Fish tokens"};
					case 19886:
						return new Object[]{18, "Fish tokens"};
					case 5185:
						return new Object[]{12, "Fish tokens"};
				}
			} else if (shop == SKILLING_STORE) {
				switch (item) {
					case 19706:// Common mbox
					case 19707:// Uncommon box
					case 19708:
						return new Object[]{1500, "Skilling Points"};
					case 18782:
						return new Object[]{2000, "Skilling Points"};
					case 15373:// Korasi
						return new Object[]{1000, "Skilling Points"};
					case 6199:// Blood Necklace
						return new Object[]{5000, "Skilling Points"};
				}
			} else if (shop == DBZ_TOKEN_SHOP) {
				switch (item) {
				case 18337:
					return new Object[] { 350, "DBZ Tokens" };
				case 5127:
				case 5128:
				case 5136:
					return new Object[] { 7500, "DBZ Tokens" };
					
				case 9481:
				case 9482:
				case 9483:
					
				case 9484:
				case 9485:
				case 9486:
					return new Object[] { 5000, "DBZ Tokens" };
				case 9490:
				case 9491:
					return new Object[] { 7000, "DBZ Tokens" };
					
				case 9487:
				case 9488:
				case 9489:
					return new Object[] { 10000, "DBZ Tokens" };
					
				case 13271:
				case 13272:
				case 13273:
				case 13274:
				case 13275:
				case 13594:
				case 13595:
				case 13596:
				case 13597:
				case 13664:
					return new Object[] { 1500, "DBZ Tokens" };
				}
				return new Object[] { 10000, "DBZ Tokens" };
			} else if (shop == STARTER_STORE) {
				switch (item) {
				case 902:
				case 903:
				case 904:
				case 905:
				case 3082:
					return new Object[] { 50, "Starter Tickets" };
				case 20016:
				case 20017:
				case 20018:
				case 20021:
				case 20022:
				case 18910:
				case 10720:
				case 14006:
					return new Object[] { 100, "Starter Tickets" };
				case 15243:
					return new Object[] { 1, "Starter Tickets" };
				case 1543:
				case 5161:
					return new Object[] { 200, "Starter Tickets" };
				case 5157:
					return new Object[] { 500, "Starter Tickets" };
				case 5160:
					return new Object[] { 750, "Starter Tickets" };
				case 17911:
				case 17908:
				case 17909:
				case 11732:
					case 3928:
					return new Object[] { 300, "Starter Tickets" };
					case 18830:
						return new Object[] { 10, "Starter Tickets" };
					case 15373:
						return new Object[] { 200, "Starter Tickets" };
				}
			} else if (shop == RAIDSTORE) {
				switch (item) {
				case 6927:
				case 6928:
				case 6929:
				case 6930:
				case 6931:
					return new Object[] { 10000, "Raid Points" };
				case 1648:
				case 1647:
				case 1855:
				case 2756:
				case 2757:
				case 2758:
				case 2759:
				case 2762:
				case 2763:
				case 2764:
					return new Object[] { 5000, "Raid Points" };
				}
				return new Object[] { 10_000, "Raid Points" };
				
			} else if (shop == BOSS_POINT_STORE) {
				switch (item) {
					case 6:
					case 8:
					case 10:
					case 12:
						return new Object[] {2250, "Boss Points"};
					case 19935:
					case 12848:
						return new Object[]{7500, "Boss Points"};
					case 3810:
					case 3811:
					case 3812:
					case 3813:
					case 3814:
					case 19886:
						return new Object[]{850, "Boss Points"};
					case 7759:
					case 7760:
					case 7761:
					case 7762:
					case 7763:
					case 7764:
					case 7765:
							return new Object[]{3000, "Boss Points"};
					case 4056:
					case 4057:
					case 4058:
					case 4059:
						return new Object[]{150000, "Boss Points"};
					case 15357:
					case 15361:
					case 15362:
						return new Object[]{200000, "Boss Points"};
					case 15358:
					case 15359:
					case 15363:
						return new Object[] {285000, "Boss Points"};
				}
			} else if (shop == LOYALTYPOINT_STORE) {
				switch (item) {
				case 18980: // Red Phat
				case 18981: //Yellow Phat
				case 18982: // blue Phat
				case 18979: //green Phat
				case 19103: //purple Phat
									return new Object[] { 20000, "Loyalty Points" };
				case 13101: //Infernal phat
									return new Object[] { 50000, "Loyalty Points" };
				case 19747: //5% demonic olm phat
									return new Object[] { 10000, "Loyalty Points" };
				}
				return new Object[] { 100, "Loyalty Points" };
			} else if (shop == DONATOR_STORE_1) {
				switch (item) {
					case 3459: //Dragonkin lamp
					case 18782: //Low key
						return new Object[] { 1, "Donation Points" };
					case 3455: // Med Key
					case 5185: //inf overload potion
					case 14808: //Scroll of praise
					case 19890: // 2h double drop
						return new Object[] { 25, "Donation Points" };
					case 3458: // Top Key
						return new Object[] { 50, "Donation Points" };
					case 13201:
						return new Object[] { 15, "Donation Points"};
					case 5080: // infernal pickaxe
				return new Object[] { 20, "Donation Points" };
				case 5197: // 10% perm dr scroll
						return new Object[] { 100, "Donation Points" };
				case 5170: //inf prayer scroll
					return new Object[] { 45, "Donation Points" };
				}
				return new Object[] { 150, "Donation Points" };
			} else if (shop == VOID_STORE) {
				switch (item) {
					case 9083:
						return new Object[] {200, "Pest Control Points"};
				case 14044:// Black Partyhat
				case 14050:// Black Santa Hat
				case 11288:// Black hween
					return new Object[] { 100, "Pest Control Points" };
					case 8839:
					case 8840:
					case 8841:
						return new Object[] {250, "Pest Control Points"};
					case 11664:
					case 11665:
					case 11663:
						return new Object[] {200, "Pest Control Points"};
					case 8842:
						return new Object[] {150, "Pest Control Points"};
					case 18889:
					case 18891:
						return new Object[] {400, "Pest Control Points"};
					case 18890:
					case 19712:
						return new Object[] {300, "Pest Control Points"};
				}
				return new Object[] { 100, "Pest Control Points" };
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
				case 14936:
				case 14938:
					return new Object[] { 60, "agility tickets" };
				case 10941:
				case 10939:
				case 10940:
				case 10933:
					return new Object[] { 20, "agility tickets" };
				case 13661:
					return new Object[] { 100, "agility tickets" };
				}
			} else if (shop == TOKEN_STORE) {
				switch (item) {
					case 3912:
						return new Object[] {300, "Colorful Tokens"};

					case 3459:
						return new Object[] {2000, "Colorful Tokens"};

					case 19140:
						return new Object[] {85000, "Colorful Tokens"};

				case 3810:
				case 3811:
				case 3812:
				case 3813:
				case 3814:
				case 3815:
					case 5118:
					case 5119:
					case 5120:
						return new Object[] { 125000, "Colorful Tokens" };

				}
			} else if (shop == GRAVEYARD_STORE) {
				switch (item) {
				case 18337:
					return new Object[] { 350, "zombie fragments" };
				case 20010:
				case 20011:
				case 20012:
				case 20009:
				case 20020:
				case 10551:
					return new Object[] { 500, "zombie fragments" };
				case 10548:
				case 10549:
				case 10550:
				case 11846:
				case 11848:
				case 11850:
				case 11852:
				case 11854:
				case 11856:
					return new Object[] { 200, "zombie fragments" };
				case 11842:
				case 11844:
				case 7592:
				case 7593:
				case 7594:
				case 7595:
				case 7596:
					return new Object[] { 150, "zombie fragments" };
				case 15241:
					return new Object[] { 1250, "zombie fragments" };
				case 18889:
				case 18890:
				case 18891:

				case 16137:
				case 13045:
				case 13047:
				case 16403:
				case 16425:
				case 16955:
					return new Object[] { 2500, "zombie fragments" };
				case 1:
				case 15243:
					return new Object[] { 2, "zombie fragments" };
				}
				return new Object[] { 10000, "zombie fragments" };
			} else if (shop == DUNGEONEERING_STORE) {
				switch (item) {
				case 18351:
				case 18349:
				case 18353:
				case 18357:
				case 18355:
				case 18359:
				case 18361:
				case 18363:
					return new Object[] { 150000, "Dungeoneering tokens" };
				case 16955:
				case 16425:
				case 16403:
					return new Object[] { 300000, "Dungeoneering tokens" };
				case 18335:
				case 18509:
					return new Object[] { 75000, "Dungeoneering tokens" };
				case 19709:
					return new Object[] { 500000, "Dungeoneering tokens" };
				}
			} else if (shop == TRIVIA_STORE) {
				switch (item) {
				case 19121:
					case 15648:
					return new Object[] { 10, "Trivia Points" };
				case 6199:
					return new Object[] { 50, "Trivia Points" };
				case 15374:
					return new Object[] { 300, "Trivia Points" };
									
				}
			}else if (shop == AFK_SHOP) {
				switch (item) {
					case 19890:
					case 18768:
					case 4635:
						return new Object[] {100000, "AFK tokens"};
					case 14691:
						return new Object[] {50000, "AFK Tokens"};
					case 15648:
						return new Object[] {15000, "AFK Tokens"};
						//TODO ADD SCROLLS TO SPAWN WORLDBOSSES
					case 15357: //Jad
					case 15361:
					case 15362:
						return new Object[] {150000, "AFK Tokens"};
					case 15363:
					case 15359:
					case 15358:
						return new Object[] {185000, "AFK Tokens"};
				}

			} else if (shop == PRESTIGE_STORE) {
				switch (item) {
				case 11587:
				case 11588:
				case 11589:
				case 11592:
					return new Object[] { 75, "Prestige points" };
				}
			} else if (shop == HALLOWEEN_SHOP) {
				switch(item) {
					case 10723: // Jack latern mask
					case 11789:	// reaper hood
					case 11814: // death wings
					case 13094:	// hween scythe
					case 13111:	// bat mask
					case 14078:	// witch
					case 14079:	// witch
					case 14080:	// witch
					case 1506:	// gas mask
					case 15352:	// web cloak
					case 15660:	// chuck doll
					case 18921:	// goblin mask
					case 19327:	// bat staff
					case 3647:	// pet chucky
					case 3874:	// icy skeleton costume
					case 3875:	// icy skeleton costume
					case 3876:	// icy skeleton costume
					case 4250:	// amulet
					case 7592:	// zombie
					case 7593:	// zombie
					case 7594:	// zombie
					case 7595:	// zombie
					case 7596:	// zombie
					case 934:	// mask of the dead
					case 9906:	// Ghost buster 500
					case 9921:	// skeleton costume
					case 9922:	// skeleton costume
					case 9923:	// skeleton costume
					case 9924:	// skeleton costume
					case 9925:  // skeleton costume
						return new Object[] { 500, "Sweets" };
				}
			} else if (shop == SLAYER_STORE) {
				switch (item) {

				case 15374:
					return new Object[] { 300, "Slayer points" };
				case 19101:
					return new Object[] { 750, "Slayer points" };
				case 19886:
					return new Object[] { 500, "Slayer points" };
					case 936:
					case 937:
					case 938:
					case 939:
						return new Object[] {2500, "Slayer Points"};
					case 3312:
						return new Object[] {3250, "Slayer Points"};
					case 3648:
					case 3649:
					case 3650:
					case 3651:
					case 3652:
					case 3659:
						return new Object[] {800, "Slayer Points"};
					case 5112:
					case 5113:
					case 5114:
					case 5115:
						return new Object[] {1250, "Slayer Points"};
					case 3641:
						return new Object[] {500, "Slayer Points"};
				}

			}
			return null;
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/*
	 * Declared shops
	 */
	private static final int DUNGEON_POINTS_STORE = 999; //TODO CHANGE ME TO NEW ID


	public static final int DONATOR_STORE_1 = 48;
	public static final int DONATOR_PET_SHOP = 49;

	public static final int TRIVIA_STORE = 50;

	public static final int GENERAL_STORE = 23;
	public static final int RECIPE_FOR_DISASTER_STORE = 36;
	private static final int CONSUMABLES = 6;
	private static final int VOTING_REWARDS_STORE = 27;
	private static final int AMONG_REWARDS_STORE = 33;
	private static final int PKING_REWARDS_STORE = 26;
	private static final int ENERGY_FRAGMENT_STORE = 33;
	private static final int AGILITY_TICKET_STORE = 39;
	private static final int GRAVEYARD_STORE = 42;
	private static final int TOKKUL_EXCHANGE_STORE = 43;
	private static final int DBZ_TOKEN_SHOP = 51;
	private static final int FIREMAKING_SHOP = 15;
	private static final int SANTAS_STORE = 57;
	private static final int SKILLING_STORE = 59;
	private static final int STARTER_STORE = 58;
	private static final int SKILLCAPE_STORE_1 = 8;
	private static final int SKILLCAPE_STORE_2 = 9;
	private static final int SKILLCAPE_STORE_3 = 10;
	private static final int GAMBLING_STORE = 41;
	private static final int DUNGEONEERING_STORE = 44;
	private static final int PRESTIGE_STORE = 46;
	public static final int BOSS_POINT_STORE = 92;
	public static final int RAIDSTORE = 124;
	private static final int SLAYER_STORE = 47;
	public static final int STARDUST_STORE = 55;
	private static final int LOYALTYPOINT_STORE = 205;

	private static final int BLOOD_MONEY_STORE = 100;
	private static final int BLOOD_MONEY_STORE2 = 101;
	private static final int VOID_STORE = 115;
	private static final int TOKEN_STORE = 116;
	private static final int CUSTOMSLAYER_POINT_STORE = 117;
	private static final int SELL_FOR_TAXBAGS_SHOP = 119;
	private static final int DARKLORD_TOKEN_SHOP = 120;
	private static final int TAX_BAG_SHOP = 121;
	private static final int HALLOWEEN_SHOP = 666;
	private static final int AFK_SHOP = 10;
	
	private static final int WOODCUTTING = 122;
	private static final int RAIDS_FISHING_STORE = 123;
}