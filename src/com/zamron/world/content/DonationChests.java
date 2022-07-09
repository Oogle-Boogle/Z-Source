package com.zamron.world.content;

import com.zamron.model.Animation;
import com.zamron.model.container.impl.Inventory;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.net.packet.PacketSender;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class DonationChests {

    public static final int lowTierChestID = 172;
    public static final int mediumTierChestID = 6420;
    public static final int highTierChestID = 6421;

    public static final int lowTierKeyID = 3459;
    public static final int mediumTierKeyID = 3455;
    public static final int highTierKeyID = 3458;

    public static final int[] lowTierItems = {902, 903, 904, 905, 3082, 2577, 2749, 2750, 2752, 2751, 2753, 2754, 2755, 19721, 19722, 19723, 19724, 19734, 19736, 19468, 19137, 19935};
    public static final int[] mediumTierItems = {1499, 3973, 4800, 4801, 4802, 5079, 15012, 3951, 3316, 6584, 14559, 18750, 18751, 5131, 4770, 4771, 4772, 5209, 923, 3994, 3995, 3996, 5132, 12605, 19720, 3910, 3909, 3908, 3907, 19886,
    3980, 3999, 4000, 4001, 5167, 15649, 15650, 15651, 15652, 15653, 15654, 15655, 3905, 4761, 4762, 4763, 4764, 4765, 5089, 18894, 926, 5210, 931, 5211, 930, 15045, 12001, 5173, 3821, 3822, 3820, 19945,
    20054, 4781, 4782, 4783, 15032, 4785, 5195, 3914, 15656, 5082, 5083, 5084, 5085, 17151, 19619, 19470, 19471, 19472, 19473, 19474, 5129, 5130,3075, 3076, 3078, 3242, 3244, 5198, 5199, 5200, 19935, 19936, 19937 };
    public static final int[] highTierItems = {3064, 19618, 19691, 19692, 19693, 19694, 19696, 19695, 19159, 19160, 19161, 19163, 19164, 19165, 19166,
    9492, 9493, 9494, 9495, 9104, 19935, 14494, 14492, 14490, 2760, 19727, 19730, 19731, 19732, 19728, 6485, 19729, 19106, 13206, 13202, 13203, 13204, 13205, 13207, 11143, 11144, 11145, 11146, 11147,
    4797, 4794, 4795, 19127, 19128, 19129, 8664, 4796, 18931, 15374, 13992, 13994, 13993, 13995, 13991, 14448, 14447, 9496, 9497, 9498, 19155, 10905, 19741, 19742, 19743, 19744, 19154, 20427, 19936, 19937, 6927, 6928, 6929, 8656, 19935, 19936, 19937, 19938};

    /** Used to check all items are valid **/
    public static void checkItems() {
        for (int lowTierItem : lowTierItems) {
            System.out.println("[LOW] Item ID: " + lowTierItem + " Name: " + ItemDefinition.forId(lowTierItem).getName());
        }
        for (int mediumTierItem : mediumTierItems) {
            System.out.println("[MED] Item ID: " + mediumTierItem + " Name: " + ItemDefinition.forId(mediumTierItem).getName());
        }
        for (int highTierItem : highTierItems) {
            System.out.println("[HIGH] Item ID: " + highTierItem + " Name: " + ItemDefinition.forId(highTierItem).getName());
        }
    }
    /** Handles clicking each chest **/
    public static void handleChestClick(int ButtonID, Player player) {

        if(!player.getClickDelay().elapsed(3000))
            return;

        switch (ButtonID) {
            case lowTierChestID:
                lowTierReward(player);
                break;
            case mediumTierChestID:
                mediumTierReward(player);
                break;
            case highTierChestID:
                highTierReward(player);
                break;
        }
    }

    private static boolean shouldSaveKey(Player player) {
        int rolled = Misc.random(100);
        int chance = 0;

        switch (player.getRights()) {
            case DONATOR:
                chance = 10;
                break;
            case SUPER_DONATOR:
                chance = 20;
                break;
            case EXTREME_DONATOR:
                chance = 30;
                break;
            case LEGENDARY_DONATOR:
                chance = 40;
                break;
            case UBER_DONATOR:
            case DELUXE_DONATOR:
                chance = 50;
                break;
            case VIP_DONATOR:
                chance = 80;
                break;
            default:
                break;
        }
        switch (player.getSecondaryPlayerRights()) {
            case DONATOR:
                chance = 10;
                break;
            case SUPER_DONATOR:
                chance = 20;
                break;
            case EXTREME_DONATOR:
                chance = 30;
                break;
            case LEGENDARY_DONATOR:
                chance = 40;
                break;
            case UBER_DONATOR:
                chance = 50;
                break;
            case DELUXE_DONATOR:
                chance = 50;
                break;
            default:
                break;
        }
        return rolled <= chance;
    }
    /** Low tier 1-3 rewards **/
    private static void lowTierReward(Player player) {

        Inventory invent = player.getInventory();
        PacketSender pck = player.getPacketSender();

        if (invent.getFreeSlots() < 1) {
            pck.sendMessage("You need at least 1 slot free to open the chest.");
            return;
        }

        if (!invent.contains(lowTierKeyID)) {
            pck.sendMessage("You don't have the right key to open this chest!");
            return;
        }

        player.performAnimation(new Animation(536));
        player.getPacketSender().sendMessage("You open the chest..");
        if (shouldSaveKey(player)) {
            player.getPacketSender().sendMessage("Your key was saved due to your donor rank!");
        } else {
            invent.delete(lowTierKeyID, 1);
        }
        int reward = (lowTierItems[Misc.getRandom(lowTierItems.length - 1)]);
        invent.add(reward, 1);
       /** World.sendMessageNonDiscord("[T1-T3 Chest] "+player.getUsername()
                + " has just received 1 x "
                + ItemDefinition.forId(reward).getName()
                + "!");**/
    }

    /** Medium tier 4-7 rewards **/
    private static void mediumTierReward(Player player) {

        Inventory invent = player.getInventory();
        PacketSender pck = player.getPacketSender();

        if (invent.getFreeSlots() < 1) {
            pck.sendMessage("You need at least 1 slot free to open the chest.");
            return;
        }

        if (!invent.contains(mediumTierKeyID)) {
            pck.sendMessage("You don't have the right key to open this chest!");
            return;
        }

        player.performAnimation(new Animation(536));
        player.getPacketSender().sendMessage("You open the chest..");
        if (shouldSaveKey(player)) {
            player.getPacketSender().sendMessage("Your key was saved due to your donor rank!");
        } else {
            invent.delete(mediumTierKeyID, 1);
        }
        int reward = (mediumTierItems[Misc.getRandom(mediumTierItems.length - 1)]);
        invent.add(reward, 1);
        World.sendMessageDiscord("[T4-T7 Chest] "+player.getUsername()
                + " has just received 1x"
                + ItemDefinition.forId(reward).getName()
                + "!");
    }

    /** Top tier 8-10 rewards **/
    private static void highTierReward(Player player) {

        Inventory invent = player.getInventory();
        PacketSender pck = player.getPacketSender();

        if (invent.getFreeSlots() < 1) {
            pck.sendMessage("You need at least 1 slot free to open the chest.");
            return;
        }

        if (!invent.contains(highTierKeyID)) {
            pck.sendMessage("You don't have the right key to open this chest!");
            return;
        }

        player.performAnimation(new Animation(536));
        player.getPacketSender().sendMessage("You open the chest..");
        if (shouldSaveKey(player)) {
            player.getPacketSender().sendMessage("Your key was saved due to your donor rank!");
        } else {
            invent.delete(highTierKeyID, 1);
        }
        int reward = (highTierItems[Misc.getRandom(highTierItems.length - 1)]);
        invent.add(reward, 1);
        World.sendMessageDiscord("[T8-T10 Chest] @blu@"+player.getUsername()
                + " has just received 1x "
                + ItemDefinition.forId(reward).getName()
                + "!");
    }
}
