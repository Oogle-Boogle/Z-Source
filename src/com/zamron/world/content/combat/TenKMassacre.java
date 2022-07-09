package com.zamron.world.content.combat;

import com.zamron.model.Item;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
/@Author Flub
*/
public class TenKMassacre {

    public static List<String> POSSIBLE_WINNERS = new ArrayList<>();

    public static boolean winnerReceivedReward = false;

    public static String lastWinnerName;

    public static final Item[] REWARDS = {new Item(10835, 5000), //TODO CHANGE THE REWARDS
                                        new Item(10835, 5000), new Item(10835, 5000), new Item(10835, 5000), new Item(10835, 5000), new Item(10835, 5000),new Item(10835, 5000),

            //Better items
            new Item(11179, 5), new Item(20260, 1000), new Item(12848, 1), new Item(19081, 1), new Item(19103, 1), new Item(12845, 10), new Item(12846, 10),
            new Item(12847, 10)};

    public static int CURRENT_SERVER_KILLS = 0; //Used to keep track of current server kills (obviously)
    public static int REQUIRED_SERVER_KILLS = 10000; //How many kills are required to start prize draw?

    public static void incrementServerKills(Player possibleWinner, int numberOfKills) {
        //System.out.println("Player "+possibleWinner.getUsername() + " Added to 10kMassacre list after "+numberOfKills+" kills");
        if (CURRENT_SERVER_KILLS <= REQUIRED_SERVER_KILLS) {
            CURRENT_SERVER_KILLS += numberOfKills;
            System.out.println("CURRENT SERVER KILLS = "+CURRENT_SERVER_KILLS);
            POSSIBLE_WINNERS.add(possibleWinner.getUsername());
            updateQuestTab();
        } else {
            pickWinner();
            restartGame();
        }
    }

    public static void updateQuestTab() {
        //TODO Update quest tab for all players with current KC
    }

    public static void pickWinner() {
        String winner = POSSIBLE_WINNERS.get(Misc.getRandom(POSSIBLE_WINNERS.size() - 1));//Pick winner from list
        try {
            Player winningPlayer = World.getPlayerByName(winner);//Set the
            rewardPlayer(winningPlayer);//Starts Reward Process below
        } catch (Exception e) {
            //System.out.println("Picking winner - player offline");
        }
        lastWinnerName = winner;//Saving last players username
        //System.out.println(lastWinnerName+"won but was offline");
    }

    public static void rewardPlayer(Player winningPlayer) {
        Item reward = REWARDS[Misc.getRandom(REWARDS.length - 1)];
        boolean stackable = reward.getDefinition().isStackable();
        try {
            if (winningPlayer == null) {// If Player is offline
                winnerReceivedReward = false;
                //System.out.println("User "+lastWinnerName+" is Offline");
            }
            if (winningPlayer != null && !winnerReceivedReward) {
                String rewardMessage = ("<img=12><col=bababa>[<col=0999ad><shad=200>TenKMassacre<col=bababa>] " + winningPlayer.getUsername() + " has won " + reward.getAmount() + " x " + reward.getDefinition().getName() + " from TenKMassacre.");
                //IF THE PLAYER IS ONLINE...
                    winningPlayer.getBank().add(reward);
                    World.sendMessageNonDiscord(rewardMessage);
                    winnerReceivedReward = true;
                    restartGame();
                }
        } catch (Exception e) {
            //System.out.println("Winner of 10k Massacre was offline.");
        }
    }

    public static void checkOnLogin(Player player) {
        if (!winnerReceivedReward && player.getUsername().equalsIgnoreCase(lastWinnerName)) {
                rewardPlayer(player);
                winnerReceivedReward = true;
        }
    }

    public static void restartGame() {
        POSSIBLE_WINNERS.clear();
        CURRENT_SERVER_KILLS = 0;
    }


}