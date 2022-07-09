package com.zamron.model;

import com.google.common.collect.ImmutableSet;
import com.zamron.util.Misc;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.player.Player;

public class DifficultyHandler {

    private static final int easyBtn = -17034;
    private static final int mediumBtn = -17033;
    private static final int hardBtn = -17032;
    private static final int insaneBtn = -17031;
    private static final int zezimaBtn = -17030;

    public static final ImmutableSet<Integer> BUTTONS = ImmutableSet.of(easyBtn, mediumBtn, hardBtn, insaneBtn, zezimaBtn);

    public static void openInterface(Player player) {
        player.getPacketSender().sendInterface(48500);
        int interfaceStart = 48513;

        for (int i = 1; i < Difficulty.values().length; i++) {
            player.getPacketSender().sendString(interfaceStart, Difficulty.values()[i].interfaceString);
            interfaceStart++;
        }
    }

    public static void handleDifficulty(Player player, int btnID) {

        Difficulty current = player.getDifficulty();
        Difficulty selected = Difficulty.Default;

        switch (btnID) {
            case easyBtn:
                selected = Difficulty.Easy;
                break;
            case mediumBtn:
                selected = Difficulty.Medium;
                break;
            case hardBtn:
                selected = Difficulty.Hard;
                break;
            case insaneBtn:
                selected = Difficulty.Insane;
                break;
            case zezimaBtn:
                selected = Difficulty.Zezima;
                break;
        }

        player.setSelectedDifficulty(selected);

        String lineOne = "Select " + Misc.formatText(selected.name()) + " difficulty?";
        String lineTwo = "You can change difficulty in the future!";
        String xpRate = "You'll receive an XP Rate of " + selected.getDialogueString();
        String drRate = "You'll receive @blu@" + selected.getDrBoost() + "@bla@ drop rate boost.";

        if (current.ordinal() > selected.ordinal()
                || player.getRights().equals(PlayerRights.UBER_DONATOR)
                || player.getRights().equals(PlayerRights.DELUXE_DONATOR)
                || player.getRights().equals(PlayerRights.VIP_DONATOR)
                || player.getRights().isSeniorStaff()
                || player.getDifficulty().equals(Difficulty.Default)) {
            DialogueManager.start(player, 700);
            player.setDialogueActionId(655);
            player.getPacketSender().sendString(981, lineOne);
            player.getPacketSender().sendString(982, lineTwo);
            player.getPacketSender().sendString(983, xpRate);
            player.getPacketSender().sendString(984, drRate);
        } else {
            DialogueManager.start(player, 702);
            player.setDialogueActionId(656);
        }
        PlayerPanel.refreshPanel(player);
    }
}
