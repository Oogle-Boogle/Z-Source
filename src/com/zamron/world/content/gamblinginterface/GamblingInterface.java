package com.zamron.world.content.gamblinginterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Direction;
import com.zamron.model.GameMode;
import com.zamron.model.GameObject;
import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.Locations;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.Locations.Location;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.movement.MovementQueue;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.util.StringUtils;
import com.zamron.world.World;
import com.zamron.world.content.BankPin;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.Gambling.FlowersData;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.player.Player;

/**
 * 
 * @author Emerald for all the games/interface, etc.
 *
 */

public class GamblingInterface {

	private Player player;

	public GamblingInterface(Player p) {
		this.player = p;
	}

	public void requestGamble(Player player2) {
		if (player == null || player2 == null || player.getConstitution() <= 0 || player2.getConstitution() <= 0
				|| player.isTeleporting() || player2.isTeleporting())
			return;

		if (player.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender().sendMessage("Ironman-players are not allowed to gamble.");
			return;
		}
		if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("Hardcore-ironman-players are not allowed to gamble.");
			return;
		}
		if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
			player.getPacketSender().sendMessage("Group-ironman-players are not allowed to gamble.");
			return;
		}
		if (player2.getGameMode() == GameMode.IRONMAN) {
			player.getPacketSender()
					.sendMessage("That player is a Hardcore-ironman-player and can therefore not gamble.");
			return;
		}
		if (player2.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("That player is an Ironman player and can therefore not gamble.");
			return;
		}
		if (player2.getGameMode() == GameMode.GROUP_IRONMAN) {
			player.getPacketSender().sendMessage("Group-ironman-players are not allowed to gamble.");
			return;
		}
		if (player.getLocation() == Location.DUNGEONEERING) {
			player.getPacketSender().sendMessage("You are far too busy to gamble at the moment!");
			return;
		}
		if (player2.getLocation() == Location.DUNGEONEERING) {
			player.getPacketSender().sendMessage("You are far too busy to gamble at the moment!");
			return;
		}

		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			BankPin.init(player, false);
			return;
		}
		
		if (System.currentTimeMillis() - lastGambleSent < 5000 && !inGamble()) {
			player.getPacketSender().sendMessage("You're sending gamble requests too frequently. Please slow down.");
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You are far too busy to gamble at the moment!");
			return;
		}
		if (inGamble()) {
			declineGamble(true);
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
		if (player2.busy() || player2.getInterfaceId() > 0 || player2.getGambling().inGamble() || player2.isBanking()
				|| player2.isShopping()/* || player2.getDueling().inDuelScreen || FightPit.inFightPits(player2) */) {
			player.getPacketSender().sendMessage("The other player is currently busy.");
			return;
		}
		if (player.getInterfaceId() > 0 || inGamble() || player.isBanking()
				|| player.isShopping()/* || player.getDueling().inDuelScreen || FightPit.inFightPits(player) */) {
			player.getPacketSender().sendMessage("You are currently unable to gamble another player.");
			if (player.getInterfaceId() > 0)
				player.getPacketSender()
						.sendMessage("Please close all open interfaces before requesting to open another one.");
			return;
		}
		gambleWith = player2.getIndex();
		if (getGambleWith() == player.getIndex())
			return;
		if (!Locations.goodDistance(player.getPosition().getX(), player.getPosition().getY(),
				player2.getPosition().getX(), player2.getPosition().getY(), 2)) {
			player.getPacketSender().sendMessage("Please get closer to request a gamble.");
			return;
		}
		if (!inGamble() && player2.getGambling().gambleRequested()
				&& player2.getGambling().getGambleWith() == player.getIndex()) {
			openGamble();
			player2.getGambling().openGamble();
		} else if (!inGamble()) {
			setGambleRequested(true);
			player.getPacketSender().sendMessage("You've sent a gamble request to " + player2.getUsername() + ".");
			player2.getPacketSender().sendMessage(player.getUsername() + ":gamblereq:");
		}
		lastGambleSent = System.currentTimeMillis();
	}

	public void openGamble() {
		player.getPacketSender().sendClientRightClickRemoval();
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player == null || player2 == null || getGambleWith() == player.getIndex() || player.isBanking())
			return;
		resetData();
		player.getPacketSender().sendString(57195, "Blackjack (Playername hosts)");
		player2.getPacketSender().sendString(57195, "Blackjack (Playername hosts)");
		player.getPacketSender().sendString(57197, "55x2 (Playername hosts)");
		player2.getPacketSender().sendString(57197, "55x2 (Playername hosts)");
		setGambling(true);
		setGambleRequested(false);
		setCanOffer(true);
		setGambleStatus(1);
		player.getPacketSender().sendInterfaceItems(57171, offeredItems);
		player2.getPacketSender().sendInterfaceItems(57171, player2.getGambling().offeredItems);
		sendText(player2);
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(57171, offeredItems);
		player.getPacketSender().sendInterfaceItems(57181, player2.getGambling().offeredItems);
		player.getMovementQueue().reset();
		inGambleWith = player2.getIndex();
	}

	public void declineGamble(boolean tellOther) {
		Player player2 = getGambleWith() >= 0 && !(getGambleWith() > World.getPlayers().capacity())
				? World.getPlayers().get(getGambleWith())
				: null;
		for (Item item : offeredItems) {
			if (item.getAmount() < 1)
				continue;
			player.getInventory().add(item);
		}
		offeredItems.clear();
		if (tellOther && getGambleWith() > -1) {
			if (player2 == null)
				return;//
			player2.getGambling().declineGamble(false);
			player2.getPacketSender().sendMessage("Other player declined the gamble.");
		}
		resetGamble();
	}

	public void endGamble() {
		offeredItems.clear();
		resetGamble();
	}

	public void sendText(Player player2) {
		if (player2 == null)
			return;
		// player2.getPacketSender().sendString(3451, "" +
		// Misc.formatPlayerName(player.getUsername()) + "");
		player2.getPacketSender().sendString(57154,
				"Gambling with: " + Misc.formatPlayerName(player.getUsername()) + "");
		// player.getPacketSender().sendString(3451, "" +
		// Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(57154,
				"Gambling with: " + Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(57175, "");
		player.getPacketSender().sendString(57175, "");
		player.getPacketSender().sendInterfaceSet(57150, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
	}

	public void gambleItem(int itemId, int amount, int slot) {
		if (slot < 0)
			return;
		if (!getCanOffer())
			return;
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player2 == null || player == null)
			return;

		/*
		 * if(player.getNewPlayerDelay() > 0 && player.getRights().ordinal() == 0) {
		 * player.getPacketSender().sendMessage("You must wait another "+player.
		 * getNewPlayerDelay() / 60+" minutes before being able to trade items.");
		 * return; }
		 */
		if (player.getRights() != PlayerRights.DEVELOPER && player2.getRights() != PlayerRights.DEVELOPER
				&& !(itemId == 1419 && player.getRights().isSeniorStaff())) {
			if (!new Item(itemId).tradeable()) {
				player.getPacketSender().sendMessage("This item is currently ungambleable and cannot be gambled.");
				return;
			}
		}
		falseGambleConfirm();
		player.getPacketSender().sendClientRightClickRemoval();
		if (!inGamble() || !canOffer) {
			declineGamble(true);
			return;
		}
		if (!player.getInventory().contains(itemId))
			return;
		if (slot >= player.getInventory().capacity() || player.getInventory().getItems()[slot].getId() != itemId
				|| player.getInventory().getItems()[slot].getAmount() <= 0)
			return;
		Item itemToGamble = player.getInventory().getItems()[slot];
		if (itemToGamble.getId() != itemId)
			return;
		if (player.getInventory().getAmount(itemId) < amount) {
			amount = player.getInventory().getAmount(itemId);
			if (amount == 0 || player.getInventory().getAmount(itemId) < amount) {
				return;
			}
		}
		if (!itemToGamble.getDefinition().isStackable()) {
			for (int a = 0; a < amount && a < 28; a++) {
				if (player.getInventory().getAmount(itemId) >= 1) {
					offeredItems.add(new Item(itemId, 1));
					player.getInventory().delete(itemId, 1);
				}
			}
		} else if (itemToGamble.getDefinition().isStackable()) {
			boolean iteminGamble = false;
			for (Item item : offeredItems) {
				if (item.getId() == itemId) {
					iteminGamble = true;
					item.setAmount(item.getAmount() + amount);
					player.getInventory().delete(itemId, amount);
					break;
				}
			}
			if (!iteminGamble) {
				offeredItems.add(new Item(itemId, amount));
				player.getInventory().delete(itemId, amount);
			}
		}
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(57181, player2.getGambling().offeredItems);
		player.getPacketSender().sendInterfaceItems(57171, offeredItems);
		player.getPacketSender().sendString(57175, "");
		acceptedGamble = false;
		gambleConfirmed = false;
		gambleConfirmed2 = false;
		player2.getPacketSender().sendInterfaceItems(57181, offeredItems);
		player2.getPacketSender().sendString(57175, "");
		player2.getGambling().acceptedGamble = false;
		player2.getGambling().gambleConfirmed = false;
		player2.getGambling().gambleConfirmed2 = false;
		sendText(player2);
	}

	public void removeGambledItem(int itemId, int amount) {
		if (!getCanOffer())
			return;
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player2 == null)
			return;
		if (!inGamble() || !canOffer) {
			declineGamble(false);
			return;
		}
		falseGambleConfirm();
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
		falseGambleConfirm();
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(57181, player2.getGambling().offeredItems);
		player.getPacketSender().sendInterfaceItems(57171, offeredItems);
		player2.getPacketSender().sendInterfaceItems(57181, offeredItems);
		sendText(player2);
		player.getPacketSender().sendString(57175, "");
		player2.getPacketSender().sendString(57175, "");
		player.getPacketSender().sendClientRightClickRemoval();
	}

	public void acceptGamble(int stage) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (getGambleWith() < 0) {
			declineGamble(false);
			return;
		}
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player == null || player2 == null) {
			declineGamble(false);
			return;
		}
		if (!twoGamblers(player, player2)) {
			player.getPacketSender().sendMessage("An error has occured. Please try re-gambling the player.");
			return;
		}

		if (stage == 1) {
			//System.out.println(
					//"Player username: " + player.getUsername() + " player2 username: " + player2.getUsername());
			player2.getGambling().goodGamble = true;
			player2.getPacketSender().sendString(57175, "Other player has accepted.");
			goodGamble = true;
			player.getPacketSender().sendString(57175, "Waiting for other player...");
			gambleConfirmed = true;
			player.getPacketSender().sendString(57195, "Blackjack (" + player.getUsername() + " hosts)");
			player2.getPacketSender().sendString(57195, "Blackjack (" + player.getUsername() + " hosts)");
			player.getPacketSender().sendString(57197, "55x2 (" + player.getUsername() + " hosts)");
			player2.getPacketSender().sendString(57197, "55x2 (" + player.getUsername() + " hosts)");
			if (inGamble() && player2.getGambling().gambleConfirmed && player2.getGambling().goodGamble && goodGamble) {
				startGame(getGamblingMode());
				//System.out.println("Confirmed");
			}
		}
		player.getClickDelay().reset();
	}

	private void giveItems() {
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player2 == null)
			return;
		if (!inGamble() || !player2.getGambling().inGamble())
			return;

		for (Item item : player.getGambling().offeredItems) {
			player.getInventory().add(item);
		}

		for (Item item : player2.getGambling().offeredItems) {
			player.getInventory().add(item);
		}
	}

	private int ddScore = 0;

	private void get55x2Winner(int roll) {

		Player player2 = World.getPlayers().get(getGambleWith());

		if (roll > 54) {
			player.forceChat("I won!");
			this.giveItems();
		} else {
			player2.forceChat("I won!");
			player2.getGambling().giveItems();
		}

		gambleFinished = true;
		player2.getGambling().gambleFinished = true;
		gamblingStatus = 0;
		player2.getGambling().gamblingStatus = 0;// TODO FIX COUNTING
		endGamble();
		player2.getGambling().endGamble();
	}

	/*private void start55x2() {

		Player player2 = World.getPlayers().get(getGambleWith());

		if (player2 == null)
			return;

		gambleFinished = false;
		player2.getGambling().gambleFinished = false;

		gamblingStatus = 3;
		player2.getGambling().gamblingStatus = 3;

		player.getPacketSender().removeInterface();
		player2.getPacketSender().removeInterface();

		player.moveTo(new Position(2738, 3471));
		player2.moveTo(new Position(2738, 3472));
		
		player.setDirection(Direction.EAST);
		player2.setDirection(Direction.WEST);

		TaskManager.submit(new Task(3) {
			@Override
			protected void execute() {
				// player.sendMessage("Player name: " + player.getUsername() + " Player2 name: "
				// + player2.getUsername());
				int roll = RandomUtility.inclusiveRandom(1, 100);
				player2.forceChat("I rolled a " + roll);
				get55x2Winner(roll);
				stop();
				return;
			}
		});

	}*/
	

	private void startDiceDuel() {
		Player player2 = World.getPlayers().get(getGambleWith());
		if (player2 == null)
			return;

		gambleFinished = false;
		player2.getGambling().gambleFinished = false;
		gamblingStatus = 3;
		player2.getGambling().gamblingStatus = 3;

		player.getPacketSender().removeInterface();
		player2.getPacketSender().removeInterface();
		
		playerPosition = player.getPosition();
		player2Position = player2.getPosition();

		player.moveTo(new Position(2738, 3471));
		player2.moveTo(new Position(2738, 3472));
		player.setDirection(Direction.EAST);
		player2.setDirection(Direction.WEST);
		player.setPlayerLocked(true);
		player2.setPlayerLocked(true);
		

		TaskManager.submit(new Task(4) {
			@Override
			protected void execute() {

				getDDWinner();

				if (gambleFinished || player2.getGambling().gambleFinished) {
					player.moveTo(playerPosition);
					player2.moveTo(player2Position);
					player2Position = player2.getPosition();
					stop();
					return;
				}

				
				
				
				
				int randomRoll = RandomUtility.inclusiveRandom(1, 100);
				int randomRoll1 = RandomUtility.inclusiveRandom(1, 100);

				if (randomRoll == randomRoll1) {
					return;
				}

				if (randomRoll > randomRoll1) {
					ddScore++;
				} else {
					player2.getGambling().ddScore++;
				}
				
				player.performAnimation(new Animation(11900));
				player.performGraphic(new Graphic(2075));
				player2.performAnimation(new Animation(11900));
				player2.performGraphic(new Graphic(2075));

				player.forceChat("I rolled: " + randomRoll + " - Score: " + ddScore);
				player2.forceChat("I rolled: " + randomRoll1 + " - Score: " + player2.getGambling().ddScore);

			}
		});

	}

	private boolean gambleFinished = true;
	public int gamblingStatus = 0;

	private void resetDDScore() {
		Player player2 = World.getPlayers().get(getGambleWith());
		ddScore = 0;
		player2.getGambling().ddScore = 0;
	}

	private void getDDWinner() {
		Player player2 = World.getPlayers().get(getGambleWith());
		if (ddScore == 3) {
			player.forceChat("GG i won the gamble " + ddScore + "-" + player2.getGambling().ddScore);
			gambleFinished = true;
			resetDDScore();
			gamblingStatus = 0;
			giveItems();
			endGamble();
			player2.getGambling().endGamble();
			player.setPlayerLocked(false);
			player2.setPlayerLocked(false);
			return;
		} else if (player2.getGambling().ddScore == 3) {
			player2.forceChat("GG i won the gamble " + player2.getGambling().ddScore + "-" + ddScore);
			player2.getGambling().gambleFinished = true;
			resetDDScore();
			player2.getGambling().gamblingStatus = 0;
			player2.getGambling().giveItems();
			endGamble();
			player2.getGambling().endGamble();
			player.setPlayerLocked(false);
			player2.setPlayerLocked(false);
			return;
		}

	}

	public enum GamblingMode {
		FLOWER_POKER, DICE_DUEL, BLACKJACK; /*FIFTY_FIVE_X2*;*/

	}

	private GamblingMode gamblingMode;

	public GamblingMode getGamblingMode() {
		return gamblingMode;
	}

	public void setGamblingMode(GamblingMode mode) {
		this.gamblingMode = mode;
	}

	private void sendDescriptionForMode(GamblingMode mode) {

		if (mode.equals(GamblingMode.DICE_DUEL)) {
			player.getPacketSender().sendString(57157, "@whi@This game is also very simple.")
					.sendString(57158, "@whi@The higher roll gets the point")
					.sendString(57159, "@whi@First to 3 points wins.");
		} else if (mode.equals(GamblingMode.FLOWER_POKER)) {
			player.getPacketSender().sendString(57157, "@whi@This game is kinda simple")
					.sendString(57158, "@whi@Whoever gets more pairs wins")
					.sendString(57159, "@whi@Flower poker guide can be found on forums.");
		} else if (mode.equals(GamblingMode.BLACKJACK)) {
			player.getPacketSender().sendString(57157, "@whi@Ye this one is simple")
					.sendString(57158, "@whi@Ill explain later").sendString(57159, "@whi@Ight.");
	/*	} else if (mode.equals(GamblingMode.FIFTY_FIVE_X2)) {
			player.getPacketSender().sendString(57157, "@whi@Ye this one is simple")
					.sendString(57158, "@whi@If host rolls over 55, the player wins")
					.sendString(57159, "@whi@Otherwise the host wins.");*/
		}

	}
	
	private Position playerPosition, player2Position;

	private void startGame(GamblingMode mode) {
		Player player2 = World.getPlayers().get(getGambleWith());
		player.setPlayerLocked(true);
		player2.setPlayerLocked(true);
		if (mode.equals(GamblingMode.FLOWER_POKER)) {
			startFlowerPoker();
		} else if (mode.equals(GamblingMode.DICE_DUEL)) {
			startDiceDuel();
		} else if (mode.equals(GamblingMode.BLACKJACK)) {
			startBlackjack();
			//System.out.println("Starting blackjack");
		/*} else if (mode.equals(GamblingMode.FIFTY_FIVE_X2)) {
			start55x2();
			//System.out.println("Starting 55x2");*/
		}
	}

	private List<Integer> flowerList = new ArrayList<>();

	private void startFlowerPoker() {

		
		Player player2 = World.getPlayers().get(getGambleWith());

		if (player2 == null)
			return;

		flowerList.clear();
		player2.getGambling().flowerList.clear();

		gambleFinished = false;
		player2.getGambling().gambleFinished = false;
		gamblingStatus = 3;
		player2.getGambling().gamblingStatus = 3;

		player.getPacketSender().removeInterface();
		player2.getPacketSender().removeInterface();

		player.moveTo(new Position(2738, 3471));
		player2.moveTo(new Position(2738, 3472));

		TaskManager.submit(new Task(6) {
			@Override
			protected void execute() {

				plantFlower();
				//System.out.println("Flower count: " + flowerCount);
				if (flowerCount >= 5) {
					getFPWinner();
					stop();
					return;
				}

			}
		});
	}

	int flowerCount = 0;

	int pairCount = 0;

	private void plantFlower() {
		FlowersData flowers = FlowersData.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setInteractingObject(flower);
		MovementQueue.stepAway(player);
		//CustomObjects.globalObjectRemovalTask(flower, 90);
		CustomObjects.spawnGlobalObject(flower);
		player.setPositionToFace(flower.getPosition());
		flowerList.add(flowers.objectId);

		Player player2 = World.getPlayers().get(getGambleWith());
		FlowersData flowers1 = FlowersData.generate();
		final GameObject flower1 = new GameObject(flowers1.objectId, player2.getPosition().copy());
		player2.getMovementQueue().reset();
		player2.performAnimation(new Animation(827));
		player2.getPacketSender().sendMessage("You plant the seed..");
		player2.getMovementQueue().reset();
		player2.setInteractingObject(flower1);
		MovementQueue.stepAway(player2);
		//CustomObjects.globalObjectRemovalTask(flower1, 90);
		CustomObjects.spawnGlobalObject(flower1);
		player2.setPositionToFace(flower1.getPosition());
		player2.getGambling().flowerList.add(flowers1.objectId);
		flowerCount++;
		
		plants.add(flower);
		player2.getGambling().plants.add(flower1);
	}

	public int bjScore = 0;

	private void startBlackjack() {

		Player player2 = World.getPlayers().get(getGambleWith());

		if (player2 == null)
			return;

		bjScore = 0;
		bjTurn = 1;
		player2.getGambling().bjTurn = 1;

		gambleFinished = false;
		player2.getGambling().gambleFinished = false;
		gamblingStatus = 3;
		player2.getGambling().gamblingStatus = 3;

		player.moveTo(new Position(2738, 3471));
		player2.moveTo(new Position(2738, 3472));
		
		player.setDirection(Direction.EAST);
		player2.setDirection(Direction.WEST);

		player.getPacketSender().removeInterface();
		player2.getPacketSender().removeInterface();

		//System.out.println("Starting for: " + player.getUsername() + " and " + player2.getUsername());

		TaskManager.submit(new Task(5) {
			@Override
			protected void execute() {
				player.setDialogueActionId(920);
				DialogueManager.start(player, 185);
				//System.out.println("Starting for: " + player.getUsername());
				stop();
				return;

			}
		});
	}

	public int bjTurn = 0;

	public void setHostTurn() {
		Player player2 = World.getPlayers().get(getGambleWith());
		player2.setDialogueActionId(920);
		DialogueManager.start(player2, 185);
		//System.out.println("Starting for: " + player2.getUsername());
		player2.getGambling().bjTurn = 2;
		bjTurn = 2;
	}

	public void getBlackjackWinner() {
		Player player2 = World.getPlayers().get(getGambleWith());
		int hostScore = this.bjScore;
		int playerScore = player2.getGambling().bjScore;

		//System.out.println("Scores are: " + hostScore + " and " + playerScore);

		if (hostScore > 100 && playerScore <= 100) {
			player2.forceChat("I won!");
			player2.getGambling().giveItems();
		} else if (hostScore <= 100 && playerScore > 100) {
			player.forceChat("I won!");
			this.giveItems();
		} else if (hostScore > playerScore) {
			this.giveItems();
			player.forceChat("I won!");
		} else if (playerScore > hostScore) {
			player2.forceChat("I won!");
			player2.getGambling().giveItems();
		}

		gambleFinished = true;
		player2.getGambling().gambleFinished = true;
		gamblingStatus = 0;
		player2.getGambling().gamblingStatus = 0;// TODO FIX COUNTING
		bjScore = 0;
		player2.getGambling().bjScore = 0;
		bjTurn = 0;
		player2.getGambling().bjTurn = 0;
		endGamble();
		player2.getGambling().endGamble();
	}

	private String getResult(List<Integer> ints) {
		String result = "none";

		Map<Integer, Integer> frequencies = new HashMap<>();

		for (Integer anInt : ints) {
			frequencies.merge(anInt, 1, Integer::sum);
		}

		int pairs = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 2).count();
		int triple = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 3).count();
		int quad = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 4).count();
		int five = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 5).count();

		if (five != 0)
			result = "Five of a kind!";
		else if (quad != 0)
			result = "Four of a kind!";
		else if (pairs == 1 && triple != 0)
			result = "Full house!";
		else if (pairs != 2 && triple != 0)
			result = "Three of a kind!";
		else if (pairs == 2)
			result = "Two pairs!";
		else if (pairs == 1)
			result = "One pair!";
		else
			result = "No pair";
		//System.out.println("List elements: " + ints);
		//System.out.println("List result(String): " + result);

		return result;
	}

	private int convertPairToInt() {
		String pair = getResult(flowerList);

		switch (pair) {
		case "Five of a kind!":
			return 6;
		case "Four of a kind!":
			return 5;
		case "Full house!":
			return 4;
		case "Three of a kind!":
			return 3;
		case "Two pairs!":
			return 2;
		case "One pair!":
			return 1;
		case "No pair":
			return 0;

		}

		return -1;

	}
	
	private List<GameObject> plants = new ArrayList<>();

	private void getFPWinner() {
		Player player2 = World.getPlayers().get(getGambleWith());
		int result1 = convertPairToInt();
		int result2 = player2.getGambling().convertPairToInt();
		//System.out.println(player.getUsername() + " result1(int): " + result1);
		//System.out.println(player2.getUsername() + " result2(int): " + result2);

		if (result1 == result2) {
			//System.out.println("TIE!");
			TaskManager.submit(new Task(2) {
				@Override
				protected void execute() {
					player.forceChat("TIE - Replant starting in 10seconds!");
					player2.forceChat("TIE - Replant starting in 10seconds!");
					stop();
					return;
				}
			});
			flowerCount = 0;
			TaskManager.submit(new Task(17) {
				@Override
				protected void execute() {
					
					for(GameObject object : plants) {
						//System.out.println("-executed-");
						CustomObjects.globalObjectRemovalTask(object, 1);
					}
					
					for(GameObject obj : player2.getGambling().plants) {
						CustomObjects.globalObjectRemovalTask(obj, 1);
					}
					startFlowerPoker();
					stop();
					return;
				}
			});
		} else if (result1 > result2) {
			//System.out.println(player.getUsername() + " wins");
			TaskManager.submit(new Task(4) {
				@Override
				protected void execute() {
					player.forceChat("I win with " + result1 + " pairs!");
					giveItems();
					flowerCount = 0;
					for(GameObject object : plants) {
						//System.out.println("-executed-");
						CustomObjects.globalObjectRemovalTask(object, 4);
					}
					
					for(GameObject obj : player2.getGambling().plants) {
						CustomObjects.globalObjectRemovalTask(obj, 4);
					}

					gambleFinished = true;
					player2.getGambling().gambleFinished = true;
					gamblingStatus = 0;
					player2.getGambling().gamblingStatus = 0;// TODO FIX COUNTING
					endGamble();
					player2.getGambling().endGamble();
					
					stop();
					return;
				}
			});
		} else {
			TaskManager.submit(new Task(4) {
				@Override
				protected void execute() {
					player2.forceChat("I win with " + result2 + " pairs!");
					for(GameObject object : plants) {
						//System.out.println("-executed-");
						CustomObjects.globalObjectRemovalTask(object, 1);
					}
					
					for(GameObject obj : player2.getGambling().plants) {
						CustomObjects.globalObjectRemovalTask(obj, 1);
					}
					//System.out.println(player2.getUsername() + " wins");
					player2.getGambling().giveItems();
					flowerCount = 0;

					gambleFinished = true;
					player2.getGambling().gambleFinished = true;
					gamblingStatus = 0;
					player2.getGambling().gamblingStatus = 0;// TODO FIX COUNTING
					endGamble();
					player2.getGambling().endGamble();
					stop();
					return;
				}
			});
		}

	}

	private void resetData() {
		Player player2 = World.getPlayers().get(getGambleWith());
		player.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 0).sendConfig(1702, 0).sendConfig(1703, 0);
		player2.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 0).sendConfig(1702, 0).sendConfig(1703, 0);
		this.setGamblingMode(null);
		player2.getGambling().setGamblingMode(null);
		final String rules = " \"Game Name\" ";
		player.getPacketSender().sendString(57156, "Rules for" + rules);
		player.getPacketSender().sendString(57157, "Description will be listed here").sendString(57158, "It'll update after you choose a gamemode").sendString(57159, "This is the 3rd line of description :D");
	}

	public void handleChoice(int id) {

		if (id != -8346 && id != -8345 && id != -8342 && id != -8340)
			return;

		Player player2 = World.getPlayers().get(getGambleWith());

		switch (id) {

		case -8346:
			player.getPacketSender().sendConfig(1700, 1).sendConfig(1701, 0).sendConfig(1702, 0).sendConfig(1703, 0);
			player2.getPacketSender().sendConfig(1700, 1).sendConfig(1701, 0).sendConfig(1702, 0).sendConfig(1703, 0);
			setGamblingMode(GamblingMode.FLOWER_POKER);
			player2.getGambling().setGamblingMode(GamblingMode.FLOWER_POKER);
			sendDescriptionForMode(getGamblingMode());
			player2.getGambling().sendDescriptionForMode(player2.getGambling().getGamblingMode());
			break;
		case -8345:
			player.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 1).sendConfig(1702, 0).sendConfig(1703, 0);
			player2.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 1).sendConfig(1702, 0).sendConfig(1703, 0);
			setGamblingMode(GamblingMode.DICE_DUEL);
			player2.getGambling().setGamblingMode(GamblingMode.DICE_DUEL);
			sendDescriptionForMode(getGamblingMode());
			player2.getGambling().sendDescriptionForMode(player2.getGambling().getGamblingMode());
			break;
		case -8342:
			player.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 0).sendConfig(1702, 1).sendConfig(1703, 0);
			player2.getPacketSender().sendConfig(1700, 0).sendConfig(1701, 0).sendConfig(1702, 1).sendConfig(1703, 0);
			setGamblingMode(GamblingMode.BLACKJACK);
			player2.getGambling().setGamblingMode(GamblingMode.BLACKJACK);
			sendDescriptionForMode(getGamblingMode());
			player2.getGambling().sendDescriptionForMode(player2.getGambling().getGamblingMode());
			break;

		/*case -8340:
			player.getPacketSender().sendConfig(1703, 1).sendConfig(1702, 0).sendConfig(1701, 0).sendConfig(1700, 0);
			player2.getPacketSender().sendConfig(1703, 1).sendConfig(1702, 0).sendConfig(1701, 0).sendConfig(1700, 0);
			setGamblingMode(GamblingMode.FIFTY_FIVE_X2);
			player2.getGambling().setGamblingMode(GamblingMode.FIFTY_FIVE_X2);
			sendDescriptionForMode(getGamblingMode());
			player2.getGambling().sendDescriptionForMode(player2.getGambling().getGamblingMode());
			break;*/

		}
		player.getPacketSender().sendString(57156,
				"Rules for \"" + formatGamemode(this.getGamblingMode().toString()) + "\"");
		player2.getPacketSender().sendString(57156, "Rules for \""
				+ player2.getGambling().formatGamemode(player2.getGambling().getGamblingMode().toString()) + "\"");
		
		player.getPacketSender().sendString(57175, "");
		acceptedGamble = false;
		gambleConfirmed = false;
		gambleConfirmed2 = false;
		player2.getPacketSender().sendString(57175, "");
		player2.getGambling().acceptedGamble = false;
		player2.getGambling().gambleConfirmed = false;
		player2.getGambling().gambleConfirmed2 = false;
		sendText(player2);

	}

	public void resetGamble() {
		inGambleWith = -1;
		offeredItems.clear();
		setCanOffer(true);
		setGambling(false);
		setGambleWith(-1);
		setGambleStatus(0);
		lastGambleSent = 0;
		acceptedGamble = false;
		gambleConfirmed = false;
		gambleConfirmed2 = false;
		gambleRequested = false;
		canOffer = true;
		goodGamble = false;
		gamblingStatus = 0;
		player.getPacketSender().sendString(57175, "Are you sure you want to make this gamble?");
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().sendInterfaceRemoval();
		player.setPlayerLocked(false);
	}

	private boolean falseGambleConfirm() {
		Player player2 = World.getPlayers().get(getGambleWith());
		return gambleConfirmed = player2.getGambling().gambleConfirmed = false;
	}

	public CopyOnWriteArrayList<Item> offeredItems = new CopyOnWriteArrayList<Item>();
	private boolean inGamble = false;
	private boolean gambleRequested = false;
	private int gambleWith = -1;
	private int gambleStatus;
	public long lastGambleSent, lastAction;
	private boolean canOffer = true;
	public boolean gambleConfirmed = false;
	public boolean gambleConfirmed2 = false;
	public boolean acceptedGamble = false;
	public boolean goodGamble = false;

	public void setGambling(boolean gambling) {
		this.inGamble = gambling;
	}

	public boolean inGamble() {
		return this.inGamble;
	}

	public void setGambleRequested(boolean gambleRequested) {
		this.gambleRequested = gambleRequested;
	}

	public boolean gambleRequested() {
		return this.gambleRequested;
	}

	public void setGambleWith(int gambleWith) {
		this.gambleWith = gambleWith;
	}

	public int getGambleWith() {
		return this.gambleWith;
	}

	public void setGambleStatus(int status) {
		this.gambleStatus = status;
	}

	public int getGambleStatus() {
		return this.gambleStatus;
	}

	public void setCanOffer(boolean canOffer) {
		this.canOffer = canOffer;
	}

	public boolean getCanOffer() {
		return canOffer && player.getInterfaceId() == 57150 && !player.isBanking()
				&& !player.getPriceChecker().isOpen();
	}

	public int inGambleWith = -1;

	/**
	 * Checks if two players are the only ones in a gamble.
	 * 
	 * @param p1 Player1 to check if he's 1/2 player in gamble.
	 * @param p2 Player2 to check if he's 2/2 player in gamble.
	 * @return true if only two people are in the gamble.
	 */
	public static boolean twoGamblers(Player p1, Player p2) {
		int count = 0;
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getGambling().inGambleWith == p1.getIndex()
					|| player.getGambling().inGambleWith == p2.getIndex()) {
				count++;
			}
		}
		return count == 2;
	}
	
	private String formatGamemode(String gm) {
		return StringUtils.capitalizeFirst(gm).replace("Fifty_five_x2", "55x2").replaceAll("_", " ");
	}

}
