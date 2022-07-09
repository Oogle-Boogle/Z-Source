package com.zamron.world.content.combat;

import com.zamron.model.Item;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Misc;
import com.zamron.util.Stopwatch;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class DailyNPCTask {

    private static final int TIME = 86400000;

    private static Stopwatch timer = new Stopwatch().reset();

    public static int CHOSEN_NPC_ID = 0; //Leave this as 0, it's just a default initialiser

    public static int KILLS_REQUIRED = 100; //Change this if you want to update the task amount

    public static int[] NPC_IDs = {4455, 4456, 4462, 17, 3253, 15, 9932, 1999, 219, 100, 3054, 5959, 6311, 499, 191, 197, 192}; //Add your NPC ID's here

    public static Player winner = null;

    public static String lastWinner;
    /** Stores the reward items **/
    public static final Item[] REWARDS = {
            new Item(10835, 15000),
            new Item(10835, 10000),
            new Item(10835, 25000),
            new Item(10835, 35000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(10835, 5000),
            new Item(5136, 5),
            new Item(22203, 1),
            new Item(3961, 1),
            new Item(5185, 1),
            new Item(19886, 1),
            new Item(18392, 1),
            new Item(18768, 3),
            new Item(15398, 1),
            new Item(19696, 1),
            new Item(16555, 1),
            new Item(3947, 1),
            new Item(4062, 1),
            new Item(4061, 1),
            new Item(4060, 1),
            new Item(5170, 1),
            new Item(3063, 1),
            new Item(4082, 1),
            new Item(14559, 1),
    };

    /** Picks a random NPC for the next task **/
    public static void pickDailyNPC() {
        CHOSEN_NPC_ID = NPC_IDs[Misc.random(NPC_IDs.length - 1)];
        //System.out.println("Today's Chosen NPC is "+ NpcDefinition.forId(CHOSEN_NPC_ID).getName());
        World.sendMessageDiscord("@red@Daily NPC Task Reset! New task: Kill "
                + KILLS_REQUIRED
                + NpcDefinition.forId(CHOSEN_NPC_ID).getName()
                + "!");
        timer.reset(); // <- This was missing.
    }

    /** Tracks a players progress **/
    public static void countPlayerKill(Player player) {
        int lastKnownNPC = player.getCurrentDailyNPC();
        if (lastKnownNPC != CHOSEN_NPC_ID || lastKnownNPC == 0) { //If the last task wasn't finished and we're onto a new NPC, we need to reset the KC
            player.setCurrentDailyNPCKills(1); //Resetting KC and adding one for the current kill
            player.setCurrentDailyNPC(CHOSEN_NPC_ID); //Setting the last known NPC Task to the current one
            player.setCurrentDailyNPCKills(1); //Resetting KC and adding one for the current kill
        }
        if (lastKnownNPC == CHOSEN_NPC_ID) { //Players last killed NPC task matches the current one
            if (player.getCurrentDailyNPCKills() < KILLS_REQUIRED) { //Player hasn't won yet
                player.setCurrentDailyNPCKills(player.getCurrentDailyNPCKills() + 1);
                player.getPacketSender().sendMessage((KILLS_REQUIRED - player.getCurrentDailyNPCKills()
                        + " "
                        + NpcDefinition.forId(CHOSEN_NPC_ID).getName()
                        + " kills remaining.")); //Tells the player their current KC every time they get a kill
            } else if (player.getCurrentDailyNPCKills() == KILLS_REQUIRED) {
                if (winner == player) {
                    //player.sendMessage("You've already received a reward for completing this daily task.");
                    return;
                } else {
                    winner = player; //Save the winner
                    rewardWinner(winner); //Reward the winner
                    player.setCurrentDailyNPCKills(101);
                }
            }
        }
    }

    public static void rewardWinner(Player player) {

        Item reward = REWARDS[Misc.getRandom(REWARDS.length - 1)];//Pick a random reward from the list above
        boolean stackable = reward.getDefinition().isStackable(); //Doing this just to make it clean
        int rewardQuantity = reward.getAmount(); //Int saved to make it clean
        int freeSlots = player.getInventory().getFreeSlots(); //Int saved to make it clean

        if (stackable) { //If the item is stackable
            if (freeSlots >= 1) //If stackable we only need 1 slot
                player.getInventory().add(reward); //Add to inventory
            else if (freeSlots == 0) //If player has no slots
                player.getBank(0).add(reward, true); //Add reward to bank
            player.getPacketSender().sendMessage("@red@Congratulations on winning the daily NPC task! Reward sent to your bank!");
        }
        if (!stackable) { //Non stackable reward
            if (freeSlots >= rewardQuantity) //Check if the player has enough slots
                player.getInventory().add(reward);//Add to inventory

            if (freeSlots < rewardQuantity) //If player doesn't have enough slots
                player.getBank(0).add(reward, true); //Add reward to bank
            player.getPacketSender().sendMessage("@red@Congratulations on winning the daily NPC task! Reward sent to your bank!");
        }
        World.sendMessageDiscord("@red@" + winner.getUsername() + " has completed today's NPC task and was rewarded "
                + reward.getAmount() + " x "
                + reward.getDefinition().getName()
                + "!");
        winner = player;
    }

    /** Simply restarts the game **/
    public static void resetDailyNPCGame(Player player) {
        lastWinner = winner.getUsername(); // Saves last winner name as a string to be used when player is offline.
        winner = null; // Wipes the current winner value
        pickDailyNPC(); // Picks new NPC Task
        //Thread.sleep(60);
//        player.setCurrentDailyNPCKills(1); //TODO ???
    }

    public static void dailyResetTask() {
        if (CHOSEN_NPC_ID == 0) {
            pickDailyNPC();
        }
        if (timer.elapsed(TIME)) {
            pickDailyNPC();
        }
    }

    public static void sequence(Player player) {
        if (timer.elapsed(TIME)) {
            // resetDailyNPCGame();
        }
    }
}
