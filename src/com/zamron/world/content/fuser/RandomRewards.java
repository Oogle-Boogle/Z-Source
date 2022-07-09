package com.zamron.world.content.fuser;

import com.zamron.model.Locations;
import com.zamron.model.PlayerRights;
import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

/**
 *
 * @Author Oogleboogle
 *
 */

public class RandomRewards {

    public static final int blueStone = 12845;
    public static final int redStone = 12846;
    public static final int blackStone = 12847;


    /**
     * okay first off we need to find a way to filter the rewards
     * based on the type of skill that the player is doing
     * black= fish red =wc blue=mining
     */
    public static void giveReward(Skill skill, Player player) {

        if (player.getLocation().equals(Locations.Location.AFK_ZONE)) {
            return;
        }

        int chance = Misc.random(75);
        int stoneID;
        int amountToGive = 1;
        int randomMultiplier = Misc.random(1); // Maximum they can receive
        boolean doubleReward = false;

        switch (skill) {
            case FISHING:
                stoneID = blueStone;
                break;
            case WOODCUTTING:
                stoneID = redStone;
                break;
            case MINING:
                stoneID = blackStone;
                break;
            default:
                return;
        }

        amountToGive *= randomMultiplier;

        if (amountToGive == 0) { // If they are unlucky enough to have rolled 0 / 2 on the randomMultiplier
            return;
        }

        PlayerRights rights = player.getRights();

        switch (rights) {
            case DONATOR:
                chance += 1; // Extra 1% Chance to win
                if (Misc.random(75) == 1) {// 1 in 100 chance
                    doubleReward = true;
                }
                break;
            case SUPER_DONATOR:
                chance += 2; // Extra 2% Chance to win
                if (Misc.random(75) == 2) {// 1 in 100 chance
                    doubleReward = true;
                }
                break;
            case EXTREME_DONATOR:
                chance += 3; // Extra 3% Chance to win
                if (Misc.random(75) == 3) {// 1 in 100 chance
                    doubleReward = true;
                }
                break;
            case LEGENDARY_DONATOR:
                chance += 4; // Extra 4% Chance to win
                if (Misc.random(75) == 3) {// 1 in 30 chance
                    doubleReward = true;
                }
                break;
            case UBER_DONATOR:
                chance += 5; // Extra 5% Chance to win
                if (Misc.random(75) == 4) {// 1 in 20 chance
                    doubleReward = true;
                }
                break;
            case DELUXE_DONATOR:
                chance += 6; // Extra 6% Chance to win
                if (Misc.random(75) == 5) {// 1 in 10 chance
                    doubleReward = true;
                }
                break;
            case VIP_DONATOR:
                chance +=7;
                if (Misc.random(75) == 6) {
                    doubleReward = true;
                }
        }
        if (chance <= 70) { // 1% Chance to win
            return; // If the player doesn't win, stop the whole thing
        }
        if (doubleReward) {
            amountToGive *= 2;
        }
        if (player.getInteractingObject().getId() == 49522) { //Stops players from getting infusion stones at AFK
            System.out.println("Stopped a player from getting a " +stoneID); //Debug
            return;
        }
        if (player.getInteractingObject().getId() == 11434) { //Stops players from getting infusion stones at AFK
            System.out.println("Stopped a player from getting a " +stoneID); //Debug
            return;
        }

        String plural = amountToGive > 1 ? "'s" : "";

        // Checking if the stoneID has been set & player has a free slot
        if (player.getInventory().getFreeSlots() == 0 && amountToGive >= 1) {
                player.getBank(player.getCurrentBankTab()).add(stoneID, amountToGive);
                player.sendMessage("As an "
                        + rights
                        + " benefit, we sent "
                        + amountToGive
                        + " x "
                        + ItemDefinition.forId(stoneID).getName()
                        + plural
                        + " to your bank.");
            }

        player.getInventory().add(stoneID, amountToGive);
        if (doubleReward) {
            player.sendMessage("Congratulations! Your reward was doubled as an " + rights);
            World.sendMessageNonDiscord("<img=12><shad=20><col=b96900>"+player.getUsername() + " Has just received double " +amountToGive / 2+ " " + ItemDefinition.forId(stoneID).getName()+plural);
        }

        player.sendMessage("You have received @blu@"
                + amountToGive
                + " @bla@x @blu@"
                + ItemDefinition.forId(stoneID).getName()
                + plural
                + "@bla@!");
           // World.sendMessageNonDiscord("<img=12><shad=20><col=b96900>"+player.getUsername() + " Has just received " +amountToGive+ " " + ItemDefinition.forId(stoneID).getName()+plural);
    }
}
