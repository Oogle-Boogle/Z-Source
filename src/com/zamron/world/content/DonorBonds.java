package com.zamron.world.content;

import com.zamron.model.PlayerRights;
import com.zamron.model.SecondaryPlayerRights;
import com.zamron.util.Misc;
import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.dialogue.DialogueExpression;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.dialogue.DialogueType;
import com.zamron.world.entity.impl.player.Player;

public class DonorBonds {

    public static void checkForRankUpdate(Player player) {
        if (player.getRights().isStaff() || player.getRights().equals(PlayerRights.YOUTUBER)) {
            SecondaryPlayerRights secondRights = null;
            if (player.getAmountDonated() >= 10)
                secondRights = SecondaryPlayerRights.DONATOR;
            if (player.getAmountDonated() >= 25)
                secondRights = SecondaryPlayerRights.SUPER_DONATOR;
            if (player.getAmountDonated() >= 50)
                secondRights = SecondaryPlayerRights.EXTREME_DONATOR;
            if (player.getAmountDonated() >= 125)
                secondRights = SecondaryPlayerRights.LEGENDARY_DONATOR;
            if (player.getAmountDonated() >= 200)
                secondRights = SecondaryPlayerRights.UBER_DONATOR;
            if (player.getAmountDonated() >= 500)
                secondRights = SecondaryPlayerRights.DELUXE_DONATOR;
            if (player.getAmountDonated() >= 1000)
                secondRights = SecondaryPlayerRights.VIP_DONATOR;

            if (secondRights != null && secondRights != player.getSecondaryPlayerRights()) {
                player.getPacketSender().sendMessage("You're now an " + Misc.formatText(secondRights.toString().toLowerCase()) + "! Congratulations!");
                player.setSecondaryPlayerRights(secondRights);
                player.getPacketSender().sendRights();
            }
            return;
        }

        PlayerRights rights = null;
        if (player.getAmountDonated() >= 10)
            rights = PlayerRights.DONATOR;
        if (player.getAmountDonated() >= 25)
            rights = PlayerRights.SUPER_DONATOR;
        if (player.getAmountDonated() >= 50)
            rights = PlayerRights.EXTREME_DONATOR;
        if (player.getAmountDonated() >= 125)
            rights = PlayerRights.LEGENDARY_DONATOR;
        if (player.getAmountDonated() >= 200)
            rights = PlayerRights.UBER_DONATOR;
        if (player.getAmountDonated() >= 500)
            rights = PlayerRights.DELUXE_DONATOR;
        if (player.getAmountDonated() >= 1000)
            rights = PlayerRights.VIP_DONATOR;

        if (rights != null && rights != player.getRights() && !player.getRights().isSeniorStaff() && !player.getRights().isMods()) {
            player.getPacketSender().sendMessage("You're now an " + Misc.formatText(rights.toString().toLowerCase()) + "! Congratulations!");
            player.setRights(rights);
            player.getPacketSender().sendRights();
        }
    }

    public static boolean handleBond(Player player, int item) {
        switch (item) {
            case 19935:
            case 19936:
            case 19937:
            case 19938:
                int funds;
                if (item == 19935)
                    funds = 5;
                else if (item == 19936)
                    funds = 10;
                else if (item == 19937)
                    funds = 25;
                else
                    funds = 50;
                player.getInventory().delete(item, 1);
                player.incrementAmountDonated(funds);
                player.getPointsHandler().incrementDonationPoints(funds);
                player.getPacketSender().sendMessage("Your account has added $" + funds + " to your rank. Your total is now $" + player.getAmountDonated() + "!")
                        .sendMessage("@red@Thank you for your support <3");
                checkForRankUpdate(player);
                PlayerPanel.refreshPanel(player);
                break;
        }
        return false;
    }

    public static Dialogue getTotalFunds(final Player player) {
        return new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.NPC_STATEMENT;
            }

            @Override
            public DialogueExpression animation() {
                return DialogueExpression.NORMAL;
            }

            @Override
            public int npcId() {
                return 4657;
            }

            @Override
            public String[] dialogue() {
                return player.getAmountDonated() > 0 ? new String[]{"You have donated $" + player.getAmountDonated() + " in total.", "Thank you for supporting us!"} : new String[]{"Your account has donated $" + player.getAmountDonated() + " in total."};
            }

            @Override
            public Dialogue nextDialogue() {
                return DialogueManager.getDialogues().get(5);
            }
        };
    }
}
