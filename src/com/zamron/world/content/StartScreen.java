package com.zamron.world.content;

import com.zamron.model.Flag;
import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.world.World;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.entity.impl.player.Player;


/**
 * Start screen functionality.
 *
 * @author Joey wijers
 */

//item 5092 ironman cape
public class StartScreen {
    public enum GameModes {
        //NORMAL("Normal", 52761,-12780, 1, 0, new Item[] { new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10), new Item(392, 200), new Item(10835, 1), (new Item (18928, 1)), (new Item (896, 1)), (new Item (15243, 100)), (new Item (15373, 1)),(new Item (927, 1)), (new Item (928, 1)),(new Item (1639, 1)), (new Item (15037, 1)), (new Item (15118, 1)),   (new Item (13867, 1)), (new Item (929, 1)), (new Item (4767, 1)), (new Item (11142, 1)), (new Item (7806, 1))}, "As a normal player you will be able to", "play the game without any restrictions.", "", "", "", "", ""),
        //IRONMAN("  Ironman", 52762, -12779, 1, 1, new Item[] { new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10), new Item(392, 200), new Item(10835, 1), (new Item (18928, 1)), (new Item (896, 1)), (new Item (15243, 100)), (new Item (15373, 1)),(new Item (927, 1)), (new Item (928, 1)),(new Item (1639, 1)),(new Item (15037, 1)), (new Item (15118, 1)),    (new Item (13867, 1)), (new Item (929, 1)), (new Item (4767, 1)), (new Item (11142, 1)),  (new Item (7806, 1))}, "Play Platinum as an Iron man.", "You will be restricted from trading, staking and looting items from killed players.", "You will not get a npc drop if another player has done more damage.", "You will have to rely on your starter, skilling, pvming, and shops.", "This game mode is for players that love a challenge.", "", ""),
        //ULTIMATE_IRON("  Ultimate Iron", 52763, -12778, 1, 2, new Item[] { new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10), new Item(392, 200), new Item(10835, 1), (new Item (18928, 1)), (new Item (896, 1)), (new Item (15243, 100)), (new Item (15373, 1)),(new Item (927, 1)), (new Item (928, 1)),(new Item (1639, 1)),   (new Item (15037, 1)),  (new Item (15118, 1)),   (new Item (13867, 1)), (new Item (929, 1)), (new Item (4767, 1)), (new Item (11142, 1)),  (new Item (7806, 1))}, "Play Platinum as a Ultimate Ironman.", "In addiction to the iron man rules you cannot use banks.", "This gamemode is for the players that love the impossible.", "", "", "", "");


        NORMAL("Normal", 52761, -12780, 1, 0, new Item[]{
                new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10),
                new Item(392, 200), new Item(10835, 1), new Item(18928, 1), new Item(896, 1),
                new Item(15243, 100), new Item(15373, 1), new Item(927, 1), new Item(928, 1),
                new Item(1639, 1), new Item(15037, 1), new Item(15118, 1), new Item(13867, 1),
                new Item(929, 1), new Item(4767, 1), new Item(7806, 1)    //
        }, "As a normal player you will be", "able to play the game without", "any restrictions.", "", "", "", "", "",
                "x25", "x150"),
        IRONMAN("  Ironman", 52762, -12779, 1, 1, new Item[]{
                new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10),
                new Item(392, 200), new Item(10835, 1), new Item(896, 1),
                new Item(15243, 100), new Item(15373, 1), new Item(927, 1), new Item(928, 1),
                new Item(1639, 1), new Item(15037, 1), new Item(15118, 1), new Item(13867, 1),
                new Item(929, 1), new Item(4767, 1), new Item(7806, 1), new Item(5092, 1)
        },
                "Play Zamron as an Iron man.", "You will be restricted from trading,", " staking and looting items from killed players.", "You will not get a npc drop,", " if another player has done more damage.", "You will have to rely on your starter, skilling, pvming, and shops.", "This game mode is for players that love a challenge.", "", "", ""),

        ULTIMATE_IRON("  Ultimate Iron", 52763, -12778, 1, 2, new Item[]{
                new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10),
                new Item(392, 200), new Item(10835, 1), new Item(896, 1),
                new Item(15243, 100), new Item(15373, 1), new Item(927, 1), new Item(928, 1),
                new Item(1639, 1), new Item(15037, 1), new Item(15118, 1), new Item(13867, 1),
                new Item(929, 1), new Item(4767, 1), new Item(7806, 1), new Item(5092, 1)}//
                , "Play Zamron as a Ultimate Ironman.", "In addiction to the iron man rules,", " you cannot use banks.", "This gamemode is for the players,", " that love the impossible.", "", "", "", "", ""),

        GROUP_IRONMAN("  Group ironman", 50773, -14763, 1, 3, new Item[]{
                new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(3025, 10),
                new Item(392, 200), new Item(10835, 1), new Item(896, 1),
                new Item(15243, 100), new Item(15373, 1), new Item(927, 1), new Item(928, 1),
                new Item(1639, 1), new Item(15037, 1), new Item(15118, 1), new Item(13867, 1),
                new Item(929, 1), new Item(4767, 1), new Item(7806, 1), new Item(5092, 1)//
        },

                "Play Zamron as an Group Iron man.", "You will be restricted from trading,", " staking and looting items from killed players.", "You will not get a npc drop,", " if another player has done more damage.", "You will have to rely on your starter, skilling, pvming, and shops.", "This game mode is for players that love a challenge.", "", "", "");


        private String name;
        private int stringId;
        private int checkClick;
        private int textClick;
        private int configId;
        private Item[] starterPackItems;
        private String line1;
        private String line2;
        private String line3;
        private String line4;
        private String line5;
        private String line6;
        private String line7;
        private String line8;

        GameModes(String name, int stringId, int checkClick, int textClick, int configId,
                Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5,
                String line6, String line7, String line8, String skillExp, String cmbExp) {
            //private GameModes(String name, int stringId, int checkClick, int textClick, int configId, Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5, String line6, String line7) {
            this.name = name;
            this.stringId = stringId;
            this.checkClick = checkClick;
            this.textClick = textClick;
            this.configId = configId;
            this.starterPackItems = starterPackItems;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.line4 = line4;
            this.line5 = line5;
            this.line6 = line6;
            this.line7 = line7;
            this.line8 = line8;
        }
    }

    public static void open(Player player) {
        sendNames(player);
        ClanChatManager.join(player, "Help");
        player.getPacketSender().sendInterface(52750);
        player.selectedGameMode = GameModes.NORMAL;
        check(player, GameModes.NORMAL);
        sendStartPackItems(player, GameModes.NORMAL);
        sendDescription(player, GameModes.NORMAL);
    }

    public static void sendDescription(Player player, GameModes mode) {
        int s = 52764;
        player.getPacketSender().sendString(s, mode.line1);
        player.getPacketSender().sendString(s + 1, mode.line2);
        player.getPacketSender().sendString(s + 2, mode.line3);
        player.getPacketSender().sendString(s + 3, mode.line4);
        player.getPacketSender().sendString(s + 4, mode.line5);
        player.getPacketSender().sendString(s + 5, mode.line6);
        player.getPacketSender().sendString(s + 6, mode.line7);
    }

    private static void sendStartPackItems(Player player, GameModes mode) {
        final int START_ITEM_INTERFACE = 29494;
        for (int i = 0; i < 28; i++) {
            if (i < mode.starterPackItems.length) {
                int id = mode.starterPackItems[i].getId();
                int amount = mode.starterPackItems[i].getAmount();
                player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
            } else {
                player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, -1, -1);
            }

        }
    }

    public static boolean handleButton(Player player, int buttonId) {
        final int CONFIRM = -29767;
        if (buttonId == CONFIRM) {
            if (player.didReceiveStarter()) {
                return true;
            }
            if (!PlayerPunishment.hasRecieved1stStarter(player.getHostAddress())) {
                player.getPacketSender().sendInterfaceRemoval();
                player.setReceivedStarter(true);
                handleConfirm(player);
                addStarterToInv(player);
                ClanChatManager.join(player, "help");
                player.setPlayerLocked(false);
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);

                player.setNewPlayer(false);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                PlayerPunishment.addIpToStarterList1(player.getHostAddress());
                PlayerPunishment.addIpToStarter1(player.getHostAddress());
                World.sendMessageNonDiscord("@blu@<img=12>[NEW PLAYER]@bla@: " + player.getUsername() + " has logged into Zamron for the first time!");
                World.sendMessageNonDiscord(
                        "<img=12>@blu@Please give them a warm welcome <3");
                
            } else if (PlayerPunishment.hasRecieved1stStarter(player.getHostAddress()) && !PlayerPunishment
                    .hasRecieved2ndStarter(player.getHostAddress())) {
                player.getPacketSender().sendInterfaceRemoval();
                player.setReceivedStarter(true);
                handleConfirm(player);
                addStarterToInv(player);
                ClanChatManager.join(player, "help");
                player.setPlayerLocked(false);
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);
                player.setNewPlayer(false);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                PlayerPunishment.addIpToStarterList2(player.getHostAddress());
                PlayerPunishment.addIpToStarter2(player.getHostAddress());
                World.sendMessageNonDiscord("@blu@<img=12>[NEW PLAYER]@bla@: " + player.getUsername() + " has logged into Zamron for the first time!");
                World.sendMessageNonDiscord(
                        "<img=12>@blu@Please give them a warm welcome <3");
                
            } else if (PlayerPunishment.hasRecieved1stStarter(player.getHostAddress()) && PlayerPunishment
                    .hasRecieved2ndStarter(player.getHostAddress())) {
                //player.getPacketSender().sendInterfaceRemoval();
                ClanChatManager.join(player, "help");
                player.setPlayerLocked(false);
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);
                player.setNewPlayer(false);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.getPacketSender().sendMessage("You've recieved to many starters.");
                World.sendMessageNonDiscord("@blu@<img=12>[NEW PLAYER]@bla@: " + player.getUsername() + " has logged into Zamron for the first time!");
                World.sendMessageNonDiscord(
                        "<img=12>@blu@Please give them a warm welcome <3");
                
            } else if (PlayerPunishment.hasRecieved1stStarter(player.getHostAddress()) && !PlayerPunishment
                    .hasRecieved2ndStarter(player.getHostAddress())) {
                player.getPacketSender().sendInterfaceRemoval();
                player.setReceivedStarter(true);
                handleConfirm(player);
                addStarterToInv(player);
                ClanChatManager.join(player, "help");
                player.setPlayerLocked(false);
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);
                player.setNewPlayer(false);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                PlayerPunishment.addIpToStarterList2(player.getHostAddress());
                PlayerPunishment.addIpToStarter2(player.getHostAddress());
                World.sendMessageNonDiscord("@blu@<img=12>[NEW PLAYER]@bla@: " + player.getUsername() + " has logged into Zamron for the first time!");
                World.sendMessageNonDiscord(
                        "<img=12>@blu@Please give them a warm welcome <3");
                
            }
            player.getPacketSender().sendRights();
            player.getPacketSender().sendIronmanMode(player.getGameMode().ordinal());

            //DialogueManager.start(player, 81);
            //return true;
        }
        //System.out.println("ID: " + buttonId);

        for (GameModes mode : GameModes.values()) {
            //System.out.println(mode.checkClick + " | " + mode.textClick + " | " + buttonId);
            if (mode.checkClick == buttonId || mode.textClick == buttonId) {
                selectMode(player, mode);
                return true;
            }
        }
        return false;

    }

    public static void handleConfirm(Player player) {

        //System.out.println("Game mode: " + player.selectedGameMode);

        if (player.selectedGameMode == GameModes.IRONMAN) {
            GameMode.set(player, GameMode.IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.ULTIMATE_IRON) {
            GameMode.set(player, GameMode.HARDCORE_IRONMAN, false);
        } else if (player.selectedGameMode == GameModes.GROUP_IRONMAN) {
            GameMode.set(player, GameMode.GROUP_IRONMAN, false);
            player.setGim(true);
        } else {
            GameMode.set(player, GameMode.NORMAL, false);
        }
    }

    public static void addStarterToInv(Player player) {
        for (Item item : player.selectedGameMode.starterPackItems) {
            player.getInventory().add(item);
        }
    }

    public static void selectMode(Player player, GameModes mode) {
        player.selectedGameMode = mode;
        check(player, mode);
        sendStartPackItems(player, mode);
        sendDescription(player, mode);
    }

    public static void check(Player player, GameModes mode) {
        for (GameModes gameMode : GameModes.values()) {

            if (player.selectedGameMode == gameMode) {
                player.getPacketSender().sendConfig(gameMode.configId, 1);
                continue;
            }
            player.getPacketSender().sendConfig(gameMode.configId, 0);
        }
    }

    public static void sendNames(Player player) {
        for (GameModes mode : GameModes.values()) {
            player.getPacketSender().sendString(mode.stringId, mode.name);
        }
    }
}
