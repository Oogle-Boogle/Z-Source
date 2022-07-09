package com.zamron.world.content.partyroom;

import java.util.ArrayList;
import java.util.Objects;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.GameObject;
import com.zamron.model.Graphic;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class PartyRoomManager {

    public PartyRoomManager(Player player) {
        setDeposit(new PartyRoomDepositItemContainer(player));
    }

    private static final PartyRoomLeverDialogue DIALOGUE = new PartyRoomLeverDialogue();

    private static final PartyRoomConfirmDepositDialogue CONFIRM = new PartyRoomConfirmDepositDialogue();

    private static PartyRoomChestItemContainer chest = new PartyRoomChestItemContainer();

    private PartyRoomDepositItemContainer deposit;

    private static final Animation PULL_LEVER = new Animation(2140);

    private static final Animation POP_BALLOON = new Animation(794);

    private static final Graphic BALLOON_POP = new Graphic(524);

    private static final PartyRoomBoundary[] BALLOON_BOUNDARIES = {
        new PartyRoomBoundary(new Position(3348, 3332, 0), new Position(3367, 3348, 0)),
    };

    private static final String[] WHITE_KNIGHT_SHOUT = {
        "We're knights of the party room",
        "We dance round and round like a loon", "Quite often we like to sing", "Unfortunately we make a din",
        "We're knights of the party room", "Do you like our helmet plumes?", "Everyone's happy now we can move",
        "Like a party animal in the groove"
    };

    private static ArrayList<Position> balloons = new ArrayList<>();

    private static final int CHANCE = 7;

    private static final int BALLOON_INTERVAL = 10;

    public static final int CHEST = 26193;

    public static final int LEVER = 26194;

    public static final int WHITE_KNIGHT = 660;

    private static final int BALLOON_ID = 115;

    private static final int BALLOON_RANGE = 7;

    public static final int CHEST_INVENTORY = 2273;

    public static final int DEPOSIT_INVENTORY = 2274;

    public static final int INVENTORY_ITEMS = 2006;

    public static final int MAIN_INVENTORY = 2005;

    public static final int MAIN_INTERFACE = 2156;

    public static final int DIALOGUE_ACTION_ID = 1_900;

    private static boolean whiteKnights;

    private static long lastBalloons;

    public static void open(Player player) {
        player.getPacketSender().sendItemContainer(chest, CHEST_INVENTORY);
        player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
        player.getPartyRoom().getDeposit().refreshItems();
        //player.getPacketSender().sendString(2275, "                        Total Chest Wealth: @or1@"
          //  + NumberFormat.getInstance().format(chest.getWealth()) + " gp");
        player.getPacketSender().sendInterfaceSet(MAIN_INTERFACE, MAIN_INVENTORY);
    }

    public static void sendLever(Player player) {
        if (player.getRights().isSeniorStaff()) {
            player.setDialogueActionId(DIALOGUE_ACTION_ID);
            DialogueManager.start(player, DIALOGUE);
        } else {
            player.getPacketSender().sendMessage("Only staff members can pull the lever!");
        }
    }

    public static void sendConfirmation(Player player) {
        if (player.getPartyRoom().getDeposit().isEmpty()) {
            player.getPacketSender().sendMessage("There are no items to accept.");
            return;
        }
        DialogueManager.start(player, CONFIRM);
        player.setDialogueActionId(DIALOGUE_ACTION_ID + 1);
    }

    public static void deposit(Player player, Item item, int slot) {
        if (!player.getRights().isSeniorStaff()) {
            player.sendMessage("Only staff members can add items to the chest!");
            return;
        }
        if (!GameSettings.PARTY_ROOM_ENABLED) {
            player.sendMessage("Party room is disabled at the moment.");
            return;
        }
        if (item.getAmount() < 1 || !player.getInventory().slotContainsItem(slot, item.getId())) {
            return;
        }
        if (chest.getFreeSlots() == 0) {
            player.getPacketSender().sendMessage("The chest is full");
            return;
        }
        if (player.getPartyRoom().getDeposit().getFreeSlots() == 0) {
            player.getPacketSender().sendMessage("You can't add anymore items. Either accept or remove items.");
            return;
        }
        for (int i : GameSettings.UNTRADEABLE_ITEMS) {
            if (i == item.getId()) {
                player.sendMessage("You can't trade this item.");
                return;
            }
        }
        int amount = item.getAmount();

        if (item.getDefinition().isStackable()) {
            if (amount > player.getInventory().getAmountForSlot(slot)) {
                amount = player.getInventory().getAmountForSlot(slot);
            }
            if (!player.getPartyRoom().getDeposit().contains(item.getId())
                && player.getPartyRoom().getDeposit().getFreeSlots() == 0) {
                return;
            }
        } else {
            if (amount > player.getInventory().getAmount(item.getId())) {
                amount = player.getInventory().getAmount(item.getId());
            }
            if (amount > chest.getFreeSlots()) {
                amount = chest.getFreeSlots();
            }
            if (amount > player.getPartyRoom().getDeposit().getFreeSlots()) {
                amount = player.getPartyRoom().getDeposit().getFreeSlots();
            }
        }
        long existing = chest.getAmount(item.getId());

        if (existing + amount > Integer.MAX_VALUE) {
            amount = (int) (Integer.MAX_VALUE - existing);
        }

        if (amount == 0) {
            return;
        }

        Item deposit = new Item(item.getId(), amount);

        player.getInventory().delete(deposit);

        player.getPartyRoom().getDeposit().add(deposit).refreshItems();
        player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
    }

    public static void withdraw(Player player, Item item, int slot) {
        if (!player.getPartyRoom().getDeposit().slotContainsItem(slot, item.getId())) {
            return;
        }
        int amount = item.getAmount();

        if (item.getDefinition().isStackable()) {
            if (amount > player.getPartyRoom().getDeposit().getAmountForSlot(slot)) {
                amount = player.getPartyRoom().getDeposit().getAmountForSlot(slot);
            }
            if (!player.getInventory().contains(item.getId()) && player.getInventory().getFreeSlots() == 0) {
                return;
            }
        } else {
            if (amount > player.getPartyRoom().getDeposit().getAmount(item.getId())) {
                amount = player.getPartyRoom().getDeposit().getAmount(item.getId());
            }
            if (amount > chest.getFreeSlots()) {
                amount = chest.getFreeSlots();
            }
            if (amount > player.getInventory().getFreeSlots()) {
                amount = player.getInventory().getFreeSlots();
            }
        }
        if (amount == 0) {
            return;
        }
        Item withdraw = new Item(item.getId(), amount);
        player.getPartyRoom().getDeposit().delete(withdraw);
        player.getInventory().add(withdraw);
        player.getPartyRoom().getDeposit().refreshItems();
        player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_ITEMS);
    }

    public static void accept(Player player) {
        if (player.getPartyRoom().getDeposit().isEmpty()) {
            player.getPacketSender().sendMessage("There are no items to accept.");
            return;
        }
        player.getPartyRoom().getDeposit().getValidItems().stream().filter(Objects::nonNull).forEach(item -> {
            chest.add(item);
        });
        player.getPartyRoom().getDeposit().resetItems().refreshItems();
        player.getPacketSender().sendItemContainer(chest, CHEST_INVENTORY);
        updateLocalPlayers(player);
    }

    public static void sendBalloons(Player player) {
        if (!player.getRights().isSeniorStaff()) {
            if (System.currentTimeMillis() - lastBalloons < BALLOON_INTERVAL * 60_000) {
                player.getPacketSender().sendMessage("The balloons cannot be pulled right now.");
                return;
            }
        }
        if (chest.isEmpty()) {
            player.getPacketSender().sendMessage("You need to add some items to the chest before pulling it.");
            return;
        }
        player.performAnimation(PULL_LEVER);
        lastBalloons = System.currentTimeMillis();
        World.sendMessageNonDiscord("@blu@[ Party Room ]: @bla@[" + player.getUsername() + "] has pulled the lever! "
            + Misc.getTotalAmount(chest.getWealth()) + " GP Drop Party starts now!");
        sendBalloons();
    }

    public static void sendWhiteKnights(Player player) {
        if (whiteKnights) {
            player.getPacketSender().sendMessage("The White Knights are already dancing.");
            return;
        }

        for (int y = 4778; y < 4785; y++) {
            NPC knight = NPC.of(WHITE_KNIGHT, new Position(2542, y));
            World.register(knight);
            TaskManager.submit(new Task(5) {

                int amount = 0;

                @Override
                protected void execute() {

                    if (amount == WHITE_KNIGHT_SHOUT.length - 1) {
                        stop();
                        whiteKnights = false;
                        World.deregister(knight);
                        return;
                    }
                    knight.forceChat(WHITE_KNIGHT_SHOUT[amount]);
                    amount++;
                }
            });
        }
    }

    public static void burstBalloon(Player player, GameObject object) {
        if (player == null || player.getConstitution() <= 0 || player.isTeleporting())
            return;
        if (Misc.getMinutesPlayed(player) <= 15) {
            player.getPacketSender().sendMessage("You can't do this under new player protection!");
            return;
        }

        player.performAnimation(POP_BALLOON);

        boolean test = false;

        TaskManager.submit(new Task(1) {

            @Override
            protected void execute() {
                player.performGraphic(BALLOON_POP);
                CustomObjects.deleteGlobalObject(object);
                stop();
            }

        });
        if (chest.isEmpty()) {
            return;
        }
        if (balloons.contains(object.getPosition())) {
            if (Misc.random(CHANCE) == 1 || test) {
                Item reward = chest.getRandom();
                chest.delete(reward);
                updateLocalPlayers(player);
                GroundItemManager.spawnGroundItem(player, new GroundItem(reward, object.getPosition().copy(),
                    player.getUsername(), false, 100, true, 100));
                player.getPacketSender()
                    .sendMessage("You find: " + reward.getDefinition().getName() + " x" + reward.getAmount());
                if (test) {
                    player.getPacketSender()
                        .sendMessage("Balloons: " + balloons.size() + ". items: " + chest.getValidItems().size());
                }
            }
        }
        balloons.remove(object.getPosition());

        if (balloons.size() < 15 && !chest.isEmpty()) {
            sendBalloons();
        }
    }

    private static void sendBalloons() {
        for (Position pos : balloons) {
            GameObject object = CustomObjects.getGameObject(pos);
            if (object != null) {
                CustomObjects.deleteGlobalObject(object);
            }
        }
        balloons.clear();
        for (PartyRoomBoundary boundary : BALLOON_BOUNDARIES) {
            for (Position pos : boundary.getAllPositionsWithinBoundary()) {
                if (Misc.random(10) == 1) {
                    continue;
                }
                GameObject balloon = new GameObject(BALLOON_ID + Misc.random(BALLOON_RANGE), pos);
                CustomObjects.spawnGlobalObject(balloon);
                balloons.add(pos);
            }
        }
    }

    public static void close(Player player) {
        if (player.getPartyRoom().getDeposit().getValidItems().size() > 0) {
            player.getInventory().addItemSet(player.getPartyRoom().getDeposit().getValidItems());
        }
        player.getPartyRoom().getDeposit().resetItems();
    }

    private static void updateLocalPlayers(Player player) {
        for (Player local : player.getLocalPlayers()) {
            if (local == null) {
                continue;
            }
            if (local.getInterfaceId() == MAIN_INTERFACE) {
                open(local);
            }
        }
        if (player.getInterfaceId() == MAIN_INTERFACE) {
            open(player);
        }
    }

    public PartyRoomDepositItemContainer getDeposit() {
        return deposit;
    }

    public void setDeposit(PartyRoomDepositItemContainer deposit) {
        this.deposit = deposit;
    }
}