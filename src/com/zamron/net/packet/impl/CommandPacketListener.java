package com.zamron.net.packet.impl;

import com.everythingrs.vote.Vote;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zamron.GameServer;
import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Item;
import com.zamron.model.*;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Bank;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.container.impl.Shop.ShopManager;
import com.zamron.model.definitions.*;
import com.zamron.model.input.impl.EnterReferral;
import com.zamron.model.input.impl.EnterYellTitle;
import com.zamron.model.input.impl.SetPinPacketListener;
import com.zamron.net.login.IPVerification;
import com.zamron.net.packet.InterfaceInputPacketListener;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.net.security.ConnectionHandler;
import com.zamron.tools.Encryptor;
import com.zamron.util.MACBanL;
import com.zamron.util.Misc;
import com.zamron.util.NameUtils;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.*;
import com.zamron.world.content.PlayerPunishment.Jail;
import com.zamron.world.content.StarterTasks.StarterTaskData;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.DailyNPCTask;
import com.zamron.world.content.combat.DesolaceFormulas;
import com.zamron.world.content.combat.bossminigame.BossMinigameFunctions;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.combat.strategy.CombatStrategies;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.content.dropchecker.NPCDropTableChecker;
import com.zamron.world.content.droppreview.SLASHBASH;
import com.zamron.world.content.event.SpecialEvents;
import com.zamron.world.content.fuser.CombineEnum;
import com.zamron.world.content.fuser.CombineHandler;
import com.zamron.world.content.grandexchange.GrandExchangeOffers;
import com.zamron.world.content.guidesInterface.GuideBook;
import com.zamron.world.content.mapteleportinterface.MapTeleportInterface;
import com.zamron.world.content.minigames.impl.FreeForAll;
import com.zamron.world.content.minimes.MiniMeData;
import com.zamron.world.content.minimes.MiniMeFunctions;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.content.skill.impl.herblore.Decanting;
import com.zamron.world.content.skill.impl.slayer.SlayerMaster;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.zamron.world.content.skill.impl.summoning.Familiar;
import com.zamron.world.content.skillingboss.SkillBossHandler;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.content.transportation.TeleportType;
import com.zamron.world.content.trickortreat.TrickOrTreat;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerHandler;
import com.zamron.world.entity.impl.player.PlayerSaving;
import com.zamron.world.teleportinterface.TeleportInterface;

import java.io.File;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.Arrays;

//import com.platinum.world.content.dialogue.impl.Arianwyn;

/**
 * This packet listener manages commands a player uses by using the command
 * console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

    public static int config;

    /*public static String findcachedir() {
        String cacheLoc = System.getProperty("user.home") + "/.Zamron317/"; // "./cache/";
        return cacheLoc;
    }*/

    @Override
    public void handleMessage(Player player, Packet packet) {
        String command = Misc.readString(packet.getBuffer());
        String[] parts = command.toLowerCase().split(" ");
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        try {
            switch (player.getRights()) {
                case PLAYER:
                    playerCommands(player, parts, command);
                    break;
                case MODERATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    helperCommands(player, parts, command);
                    moderatorCommands(player, parts, command);
                    handlePunishmentCommands(player, parts, command, true);
                    break;
                case ADMINISTRATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    helperCommands(player, parts, command);
                    moderatorCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    handlePunishmentCommands(player, parts, command, true);
                    break;
                case DEVELOPER:
                case OWNER:
                    playerCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    memberCommands(player, parts, command);
                    helperCommands(player, parts, command);
                    moderatorCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    ownerCommands(player, parts, command);
                    developerCommands(player, parts, command);
                    handlePunishmentCommands(player, parts, command, true);
                    deluxeDonator(player, parts, command);
                    break;
                case COMMUNITY_MANAGER:
                    playerCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    memberCommands(player, parts, command);
                    helperCommands(player, parts, command);
                    moderatorCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    developerCommands(player, parts, command);
                    handlePunishmentCommands(player, parts, command, true);
                    deluxeDonator(player, parts, command);
                    break;
                case SUPPORT:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    helperCommands(player, parts, command);
                    handlePunishmentCommands(player, parts, command, false);
                    break;
                case YOUTUBER:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    break;
                case DONATOR:
                    playerCommands(player, parts, command);
                    donator(player, parts, command);
                    memberCommands(player, parts, command);
                    break;
                case SUPER_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    break;
                case EXTREME_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    break;
                case LEGENDARY_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    break;
                case UBER_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    break;
                case DELUXE_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    break;
                case VIP_DONATOR:
                    playerCommands(player, parts, command);
                    memberCommands(player, parts, command);
                    donator(player, parts, command);
                    superDonator(player, parts, command);
                    extremeDonator(player, parts, command);
                    legendaryDonator(player, parts, command);
                    uberDonator(player, parts, command);
                    deluxeDonator(player, parts, command);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();

            if (player.getRights() == PlayerRights.DEVELOPER) {
                player.getPacketSender().sendConsoleMessage("Error executing that command.");
            } else {
                player.getPacketSender().sendMessage("Error executing that command.");
            }

        }
    }

    public static final int[][] DRITEMS = {
            /**
             * ItemID, DR bonus (as int), 1 or 0 for 200% cap 1 for it DOES work past cap, 0
             * for it DOES NOT
             **/
            {18950, 50, 1}, // paper sack
            {16141, 50, 1}, // Diamond Ammy
            {16139, 100, 0}, // emerald Ammy
            {16140, 100, 1}, // Ruby Ammy
            {18961, 50, 1}, // One Cape
            {19893, 100, 1}, // Spirit Cape
            {4777, 125, 1}, // Darklord Cape
            {19754, 50, 1}, // Karamaja Gloves 4
            {17157, 100, 1}, // RoseBlood Gloves
            {13675, 100, 1}, // Ringmaster Boots
            {2572, 10, 0}, // Ring of Wealth
            {20054, 100, 0}, // Ring of Devotion
            {19896, 50, 1}, // kingship ring
            {14019, 5, 1}, // max cape
            {14022, 25, 1}, // comp cape
            {19886, 15, 1}, // col neck
            {3822, 100, 1}, // col neckkmhgjkmjhgkhgjkhj
            {3821, 100, 1}, // col neck
            {3820, 50, 1}, // col neck
            {3317, 25, 1}, {19821, 50, 1}, {19106, 100, 1},};

    // returns an array in the order of uncapped bonus, capped bonus, total bonus.
    // it returns in a manner that you can just throw a % sign at the end
    public static double[] drBonus(Player player) {
        int[] playerEquipment = new int[11];
        double cappedBonus = 0;
        double totalBonus = 0;

        // gets all of the players equipment and puts it into an array
        playerEquipment[0] = player.getEquipment().get(0).getId();
        playerEquipment[1] = player.getEquipment().get(1).getId();
        playerEquipment[2] = player.getEquipment().get(2).getId();
        playerEquipment[3] = player.getEquipment().get(3).getId();
        playerEquipment[4] = player.getEquipment().get(4).getId();
        playerEquipment[5] = player.getEquipment().get(5).getId();
        playerEquipment[6] = player.getEquipment().get(7).getId();
        playerEquipment[7] = player.getEquipment().get(9).getId();
        playerEquipment[8] = player.getEquipment().get(10).getId();
        playerEquipment[9] = player.getEquipment().get(12).getId();
        playerEquipment[10] = player.getEquipment().get(13).getId();

        // goes through all the players equipment and sees if it's a dr item
        // if it's a dr item, then it adds it to it's respective bonus (capped/uncapped)
        for (int equipID : playerEquipment) {
            if (equipID != 0) {
                for (int[] item : DRITEMS) {
                    if (equipID == item[0]) {
                        if (item[2] == 1)
                            totalBonus += item[1];
                        else if (item[2] == 0)
                            cappedBonus += item[1];
                    }
                }
            }
        }

        // adds the bonus from player's rank
        switch (player.getRights()) {
            case DONATOR:
                totalBonus += 2.5;
                break;
            case SUPER_DONATOR:
                totalBonus += 2.5;
                break;
            case EXTREME_DONATOR:
                totalBonus += 5;
                break;
            case LEGENDARY_DONATOR:
                totalBonus += 7.5;
                break;
            case UBER_DONATOR:
                totalBonus += 10;
                break;
            case DELUXE_DONATOR:
                totalBonus += 20;
                break;

            case SUPPORT:
                totalBonus += 20;
                break;

            case MODERATOR:
                totalBonus += 20;
                break;
            case ADMINISTRATOR:
                totalBonus += 20;
                break;
            case OWNER:
                totalBonus += 100;
                break;
            case DEVELOPER:
                totalBonus += 100;
                break;
            default:
                break;
        }

        // gets the player's familiar if they have one, and sees if it gives a dr bonus

		/* switch(player.getSummoning().getFamiliar().getSummonNpc().getId()) {
		 case 9945:
		 cappedBonus += 20;
		 break; case 676: emerald Spider
		 cappedBonus += 15;
		 break;
		 case 6315:
			 HeartWrencher Pet
			 cappedBonus += 20;
		  break; }*/

        // enforces the cap
        if (cappedBonus > 200)
            cappedBonus = 200;

        // does stuff for the results
        double[] results = {totalBonus, cappedBonus, totalBonus + cappedBonus};
        return results;
    }


    private static void playerCommands(final Player player, String[] command, String wholeCommand) {

        if (command[0].equalsIgnoreCase("difficulty")) {
            DifficultyHandler.openInterface(player);
        }

        if (command[0].equals("tot") && TrickOrTreat.currentLocation != null) {
            TeleportHandler.teleportPlayer(player, TrickOrTreat.currentLocation.teleportPos, player.getSpellbook().getTeleportType());
            player.getPacketSender().sendEntityHint(TrickOrTreat.currentLocation.npc);
        }

        if (command[0].equals("whatdrops")) {
            String item = wholeCommand.substring(command[0].length() + 1);
            player.getPacketSender().sendMessage("Searching for " + item);
            ItemDefinition.whatDrops(player, item);
        }
        if (command[0].equals(("resettask"))) {
            if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
                player.getPacketSender().sendMessage("You do not currently have a slayer task");
                return;
            }
            int cost = 10;
            if (player.getPointsHandler().getSlayerPoints() >= 10) {
                player.getPointsHandler()
                        .setSlayerPoints(player.getPointsHandler().getSlayerPoints() - cost, false);
                player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK);
                player.getPacketSender().sendMessage("You've paid 10 slayer points to reset your slayer task.");
            } else {
                player.getPacketSender().sendMessage("To reset your slayer task you must have 10 or more slayer points.");
                return;
            }
        }

        if (command[0].equalsIgnoreCase("eventarena")) {
            if (!GameSettings.EventArena) {
                player.getPA().sendMessage("@blu@Event Arena is disabled now, try again later.");
                return;
            }
            TeleportHandler.teleportPlayer(player, new Position(2909, 2709, 0), TeleportType.NORMAL);
        }
        if (command[0].equalsIgnoreCase("partyroom")) {
            TeleportHandler.teleportPlayer(player, new Position(3354, 3342), player.getSpellbook().getTeleportType());
            player.getPA().sendMessage("You have been teleported to Party Room!");
        }
        if (command[0].equals("google")) {
            String query = (wholeCommand.substring(command[0].length() + 1)).replace(" ", "%20");
            player.getPacketSender().openURL("https://www.google.com/search?q=" + query);
        }

        if (command[0].equals("youtube")) {
            String query = (wholeCommand.substring(command[0].length() + 1)).replace(" ", "%20");
            player.getPacketSender().openURL("https://www.youtube.com/results?search_query=" + query);
        }


        if (command[0].contains("bug")) {
            String bugReport = (wholeCommand.substring(command[0].length() + 1));
            System.out.println(" Bug : " + bugReport);
            DiscordMessenger.sendBug(bugReport, player);
        }

        if (command[0].equalsIgnoreCase("serverperks")) {
            GlobalPerks.getInstance().open(player);
        }
        if (command[0].equalsIgnoreCase("progress")) {
            player.getProgressionManager().open();
        }
        if (command[0].equalsIgnoreCase("Bis")) {
            player.getBestItems().open();
        }

        if (command[0].equalsIgnoreCase("accept")) {
            if (player.getGroupIronmanGroupInvitation() == null) {
                player.sendMessage("You have no pending group ironman invitations");
                return;
            }
            player.getGroupIronmanGroupInvitation().handleInvitation(player, true);
        }

        if (command[0].equalsIgnoreCase("gim")) {
            if (player.getGameMode() == GameMode.IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.NORMAL) {
                player.getPacketSender()
                        .sendMessage("Normal players are not allowed to use Group Ironman.");
                return;
            }
            player.getGroupIronman().open();
        }

        /**if (command[0].equalsIgnoreCase("gimbank")) {
            if (player.getGameMode() == GameMode.IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.NORMAL) {
                player.getPacketSender()
                        .sendMessage("Normal players are not allowed to use Group Ironman.");
                return;
            }
            player.getGroupIronmanGroup().openBank(player);
        }**/

        if (command[0].equalsIgnoreCase("gimleaderboard")) {
            if (player.getGroupIronmanGroup() == null) {
                player.sendMessage("You are not in a ironman group");
                return;
            }
            if (player.getGameMode() == GameMode.IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to use Group Ironman.");
                return;
            }
            if (player.getGameMode() == GameMode.NORMAL) {
                player.getPacketSender()
                        .sendMessage("Normal players are not allowed to use Group Ironman.");
                return;
            }
            player.getGroupIronman().openLeaderboard();
        }

        switch (command[0]) {

            case "donationdeals":
                player.sendMessage("You've donated a total amount of: @yel@$"+ player.getAmountDonatedToday()+ "@bla@ today!");
                player.getPacketSender().sendInterface(57265);
                player.getDonationDeals().displayTime();
                int slot = 0;
                for(int[] item : DonationDeals.items) {
                    player.getPacketSender().sendItemOnInterface(57276, item[0], slot, item[1]);
                    slot++;
                }
                break;

            case "dropchecker":
                player.getPacketSender().sendInterface(37600);
                NPCDropTableChecker.getSingleton().getActionIdForName(player, -1);
                player.getMovementQueue().reset();
                break;
            case "ge":
                player.getPacketSender().sendInterface(24500);
                player.getMovementQueue().reset();
                break;

            case "raids":
                if (player.isInRaid()) {
                    player.sendMessage("@red@You donkey... You're already in a raid.");
                    return;
                }
                if (Misc.getMinutesPlayed(player) <= 120) {
                    player.sendMessage("@red@You need 2 hours of gameplay to participate in raids.");
                    player.sendMessage("@red@You have " + Misc.getMinutesPlayed(player) + " minutes played.");
                    return;
                }
                player.getCustomRaid().open(player);
                break;


            case "claimextra":
                DonationDeals.handleRewards();
                player.sendMessage("You've donated a total amount of: $"+ player.getAmountDonatedToday()+ " today!");
                break;

            case "claim":
            case "claimdonation":
            case "donated": {
                new java.lang.Thread() {
                    public void run() {
                        try {
                            com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("fwvYT5w8ltI4iUiUiCDr2HPbIBdbyp17WLzAN6OCDtaCaHaD57GrV8xmcnnNMzgCb08fNSJl",
                                    player.getUsername());
                            if (donations.length == 0) {
                                player.getPacketSender().sendMessage("You currently don't have any items waiting. You must donate first!");
                                return;
                            }
                            if (donations[0].message != null) {
                                player.getPacketSender().sendMessage(donations[0].message);
                                return;
                            }
                            if (player.getInventory().getFreeSlots() <= 3) {
                                player.getPacketSender().sendMessage("Please try again when you have 3 slots free");
                                return;
                            }
                            for (com.everythingrs.donate.Donation donate : donations) {
                                player.getInventory().add(new Item(donate.product_id, donate.product_amount));
                                if (World.DOUBLE_DONATIONS) {
                                    player.getInventory().add(new Item(donate.product_id, donate.product_amount));
                                }
                                if (donate.product_id == 19935) {
                                    player.incrementAmountDonatedToday(+5);
                                }
                                if (donate.product_id == 19936) {
                                    player.incrementAmountDonatedToday(+10);
                                }
                                if (donate.product_id == 19937) {
                                    player.incrementAmountDonatedToday(+25);
                                }
                                if (donate.product_id == 19938) {
                                    player.incrementAmountDonatedToday(+50);
                                    System.out.println("Reached??");
                                }
                                if (donate.product_id == 19886) {
                                    player.incrementAmountDonatedToday(+10);
                                }
                            }
                            player.getPacketSender().sendMessage("Thank you for donating!");
                            World.sendMessageNonDiscord("<shad=2><col=0352fc><img=14>[DONATION]</shad>@bla@ " + player.getUsername()
                                    + ":@bla@ has donated!</col>");
                        } catch (Exception e) {
                            player.getPacketSender().sendMessage("Api Services are currently offline. Please check back shortly");
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            break;

            case "npckills":
                player.getPacketSender();
                player.sendMessage("<img=12>Your Overall KC on Zamron is <shad=10>@blu@" + player.getNpcKills() + "");
                return;
        }

        if (command[0].equalsIgnoreCase("endcustominstance")) {
            InstanceSystem.destructInstance(player);
            player.sendMessage("Instance reset.");
        }

        if (wholeCommand.equals("afk")) {
            TeleportHandler.teleportPlayer(player, new Position(3199, 3166), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("titlecolor")) {
            player.getPacketSender().sendInterface(64850);
            player.sendMessage("For this to work you must have a title active to change the color.");
        }

        if (command[0].equalsIgnoreCase("home")) {
            TeleportHandler.teleportPlayer(player, new Position(2605, 3093), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("stuck")) {
            InstanceSystem.destructInstance(player);
            player.heal(10);
            player.moveTo(2605, 3093, 0);
            player.getPacketSender().removeInterface();
            player.sendMessage("@red@[SERVER]: Please logout and back in for the effects to work.");

        }

        if (command[0].equalsIgnoreCase("may")) {
            TeleportHandler.teleportPlayer(player, new Position(2848, 3022, 0), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("sb")) {
            TeleportHandler.teleportPlayer(player, new Position(2606, 3125, 0), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("death")) {
            TeleportHandler.teleportPlayer(player, new Position(3038, 5340, 0), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("broly")) {
            TeleportHandler.teleportPlayer(player, new Position(2580, 3122, 0), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("shops")) {
            TeleportHandler.teleportPlayer(player, new Position(2574, 3098), player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("fuser")) {
            CombineHandler.openInterface(CombineEnum.AURA, player);
        }

        if (command[0].equalsIgnoreCase("ds")) {
            player.getDropSimulator().open();
        }

        if (command[0].equalsIgnoreCase("mydr")) {
            int bonus = DropUtils.drBonus(player);
            player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
        }
        if (command[0].equalsIgnoreCase("dr")) {
            int bonus = DropUtils.drBonus(player);
            player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
        }

/*        if (command[0].equalsIgnoreCase("juggernaut")) {
            TeleportHandler.teleportPlayer(player, new Position(2419, 4655, 0),
                    player.getSpellbook().getTeleportType());
        }*/

/*        if (command[0].equalsIgnoreCase("onslaught")) {
            TeleportHandler.teleportPlayer(player, new Position(2414, 2856, 0),
                    player.getSpellbook().getTeleportType());
        }*/


        /**if (command[0].equalsIgnoreCase("hween")) {
            TeleportHandler.teleportPlayer(player, new Position(2398, 3241, 0),
                    player.getSpellbook().getTeleportType());
        }**/

        if (command[0].equalsIgnoreCase("seph")) {
            TeleportHandler.teleportPlayer(player, new Position(2590, 5727, 0),
                    player.getSpellbook().getTeleportType());
        }
        if (command[0].equalsIgnoreCase("assassin")) {
            TeleportHandler.teleportPlayer(player, new Position(3111, 5544, 0),
                    player.getSpellbook().getTeleportType());
            player.getPacketSender().sendMessage("You have entered the Assassins Den!");

        }


        if (command[0].equalsIgnoreCase("entergiveaway")) {
            if (!GameSettings.IS_GIVEAWAY) {
                player.sendMessage("@blu@There is no giveaways going on at the moment.");
                return;
            }
            if (!player.hasEntered) {
                player.hasEntered = true;
                entries++;
                player.sendMessage("You have entered the giveaway");
                World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> @red@" + player.getUsername()
                        + " @blu@ Has entered the giveaway");
                World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> @red@ There is now a total of: " + entries
                        + " people entered in the giveaway.");
            }
        }

        if (command[0].equalsIgnoreCase("totalbosskills")) {
            player.sendMessage("@blu@Total Boss kills:@red@ " + player.getTotalBossKills());
        }

        if (command[0].equalsIgnoreCase("seteasy")) {
            player.setBravekDifficulty("easy");
            player.sendMessage("Your Bravek difficulty has been set to easy");
        }
        if (command[0].equalsIgnoreCase("setmedium")) {
            player.setBravekDifficulty("medium");
            player.sendMessage("Your Bravek difficulty has been set to medium");
        }
        if (command[0].equalsIgnoreCase("sethard")) {
            player.setBravekDifficulty("hard");
            player.sendMessage("Your Bravek difficulty has been set to hard");
        }

        if (command[0].equalsIgnoreCase("setloginpin")) {
            player.setInputHandling(new SetPinPacketListener());
            player.getPacketSender().sendEnterInputPrompt("Enter the pin that you want to set$pin");
        }

        if (command[0].equalsIgnoreCase("setpin")) {
            player.setInputHandling(new SetPinPacketListener());
            player.getPacketSender().sendEnterInputPrompt("Enter the pin that you want to set$pin");
        }

        if (command[0].equalsIgnoreCase("handlerewards")) {
            player.getDonationDeals().handleRewards();
        }

        if (command[0].equalsIgnoreCase("donatetoday")) {
            player.sendMessage("@blu@Your total donated amount for today is: @red@" + player.getAmountDonatedToday());
        }

        if (command[0].equalsIgnoreCase("refer")) {
            if (!player.hasReferral) {
                player.getPacketSender()
                        .sendEnterInputPrompt("Hello " +player.getUsername() + ". How did you hear about Zamron?");
                player.setInputHandling(new EnterReferral());
            } else {
                player.sendMessage("@red@You already used a code before :(");
            }
        }

        if (command[0].equalsIgnoreCase("droptable")) {
            String npcName = command[1];
            NPCDropTableChecker.getSingleton().searchForNPC(player, npcName);
        }
        if (command[0].equals("mypos")) {
            player.getPacketSender().sendMessage(player.getPosition().toString());
        }
		
		/*if (command[0].equalsIgnoreCase("lms")) {
			LastManStanding.enterLobby(player);
		}

		if (command[0].equalsIgnoreCase("startlms")) {
			if (!LastManStanding.eventRunning) {
				LastManStanding.startEvent();
			} else {
				player.sendMessage("Event is already running - ::lms to join");
			}
		}

		if (command[0].equalsIgnoreCase("openlms")) {
			if (player.getInventory().contains(19104)) {

				int[] lmsRewards = { 14415, 5148, 11694, 4151, 13896, 13887, 13893, 14009, 14010, 14008, 15272, 15272,
						15272, 15272, 13899, 3903, 10499, 11848, 11850, 11854, 14484, 15300, 18349, 18353, 18351, 3886,
						3902 };
				player.getInventory().add(lmsRewards[Misc.getRandom(lmsRewards.length - 1)], 1);
				player.getInventory().delete(19104, 1);
			} else {
				player.sendMessage("You need a LMS key to use this command.");
				return;
			}
		}*/
/*        if (command[0].equalsIgnoreCase("chests")) {
            TeleportHandler.teleportPlayer(player, new Position(2331, 3682, 0),
                    player.getSpellbook().getTeleportType());
        }*/

        if (command[0].equalsIgnoreCase("wb")) {
            TeleportHandler.teleportPlayer(player, new Position(2410, 4679, 0),
                    player.getSpellbook().getTeleportType());
        }
        if (command[0].equalsIgnoreCase("darkranger")) {
            TeleportHandler.teleportPlayer(player, new Position(2222, 5099, 0),
                    player.getSpellbook().getTeleportType());
        }
        if (command[0].equalsIgnoreCase("rick")) {
            TeleportHandler.teleportPlayer(player, new Position(3043, 3409, 0),
                    player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("worldboss")) {
            TeleportHandler.teleportPlayer(player, new Position(2410, 4679, 0),
                    player.getSpellbook().getTeleportType());
        }

        if (command[0].equalsIgnoreCase("drinfo")) {
            int bonus = DropUtils.drBonus(player);
            player.sendMessage("@blu@Your @red@Total @blu@ drop rate bonus is currently: " + bonus + "%");
        }

        if (command[0].equalsIgnoreCase("checkdr")) {
            Player target = World.getPlayerByName(command[1]);

            if (target == null) {
                player.sendMessage(command[1] + " player is currently offline");
                return;
            }

            player.sendMessage(target.getUsername() + " Dr bonus: @red@ " + DropUtils.drBonus(target) + "%");
        }

        if (command[0].equalsIgnoreCase("clearblockedlist")) {
            player.getBlockedCollectorsList().clear();
        }

		/*if (command[0].equalsIgnoreCase("bossinfo")) {
			player.getPacketSender().sendInterface(36800);
		}*/

        if (command[0].equalsIgnoreCase("drop")) {
            NPCDropTableChecker.getSingleton().open(player);
        }

		/*if (command[0].equalsIgnoreCase("drops")) {
			NPCDropTableChecker.getSingleton().open(player);
		}*/
		
		if (command[0].equalsIgnoreCase("npctasks")) {
			NpcTasks.updateInterface(player);
			player.getPacketSender().sendInterfaceReset();
			player.getPacketSender().sendInterface(65400);
		}

       /** if (command[0].equalsIgnoreCase("rankicons")) {
            player.getPacketSender().sendInterface(61500);
        }**/

/*        if (command[0].equalsIgnoreCase("war")) {
            if (player.getLastZulrah().elapsed(600000)) {
                TeleportHandler.teleportPlayer(player, new Position(2589, 4440, player.getIndex() * 4),
                        player.getSpellbook().getTeleportType());
                Warmonger.start(player);
                player.setWarmonger(false);
                player.getLastZulrah().reset();
                player.setRegionInstance(new RegionInstance(player, RegionInstanceType.WARMONGER));
            } else {
                player.getPacketSender().sendMessage("You can only teleport here every 10 minutes!");
            }
        }*/
        Familiar pet = player.getSummoning().getFamiliar();

        if (command[0].startsWith("voted")) {
            if (command.length == 1) {
                player.getPacketSender()
                        .sendMessage("Please use [::voted 1 all].");
                return;
            }
            final String playerName = player.getUsername();
            final String id = command[1];
            final String amount = command.length == 3 ? command[2] : "1";

            Vote.service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Vote[] reward = Vote.reward(
                                "fwvYT5w8ltI4iUiUiCDr2HPbIBdbyp17WLzAN6OCDtaCaHaD57GrV8xmcnnNMzgCb08fNSJl",
                                playerName, id, amount);
                        if (reward[0].message != null) {
                            player.getPacketSender().sendMessage(reward[0].message);
                            return;
                        }
                        int votePts = SpecialEvents.getDay() == SpecialEvents.MONDAY ? 2 : 1;
                        int petCheck = 1060;
                        votePts *= player.getRights().getVotePtModifier();
                        /**player.getPointsHandler().setVotingPoints(player.getPointsHandler().getVotingPoints() + (votePts * reward[0].give_amount));**/
                        player.getInventory().add(reward[0].reward_id, (votePts * reward[0].give_amount));
                        player.getPacketSender().sendMessage("Thank you for voting! You currently have " + player.getPointsHandler().getVotingPoints() + " vote points.");
                            World.sendMessageNonDiscord("<shad=2><col=5b5e63><img=22>[VOTED]</shad>@bla@ " + player.getUsername()
                                    + ":@bla@ has voted and received a Vote Scroll!</col>");
                        if (pet != null && pet.getSummonNpc().getId() == petCheck) {
                            player.getInventory().add(reward[0].reward_id, (reward[0].give_amount));
                            player.getPacketSender().sendMessage("You received an extra bonus for using broly pet.");
                            System.out.println("Gave extra reward for brolypet");
                        }
                    } catch (Exception e) {
                        player.getPacketSender()
                                .sendMessage("Api Services are currently offline. Please check back shortly");
                        e.printStackTrace();
                    }
                }
            });
        }

        if (command[0].equalsIgnoreCase("pos")) {
            if (player.getGameMode() == GameMode.IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Ironman-players are not allowed to buy items from the general-store.");
                return;
            }
            if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Hardcore-ironman-players are not allowed to buy items from the pos.");
                return;
            }
            if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
                player.getPacketSender()
                        .sendMessage("Hardcore-ironman-players are not allowed to buy items from the pos.");
                return;
            }
            if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.DUEL_ARENA) {
                player.getPacketSender().sendMessage("You can't open the player shops right now!");
            } else {
                player.getPlayerOwnedShopManager().options();
                return;
            }
        }
		if (command[0].equalsIgnoreCase("commands")) {
			for (int i = 8145; i <= 8195; i++) {
				player.getPacketSender().sendString(i, "");
			}
			player.getPacketSender().sendString(8144, "@dre@Zamron Commands");
			player.getPacketSender().sendString(8146, "@dre@::help - contacts staff for help");
			player.getPacketSender().sendString(8147, "@dre@::home - teleports you to home area");
			player.getPacketSender().sendString(8148, "@dre@::ds - opens up drop simulator for NPC's");
			player.getPacketSender().sendString(8149, "@dre@::vote - opens vote page");
			player.getPacketSender().sendString(8150, "@dre@::donate - opens donate page");
			player.getPacketSender().sendString(8151, "@dre@::donated - claims a donation");
			player.getPacketSender().sendString(8152, "@dre@::train - teleports you starter zone");
			player.getPacketSender().sendString(8153, "@dre@::maxhit - shows your max hits");
			player.getPacketSender().sendString(8154, "@dre@::empty - empties your entire inventory");
			player.getPacketSender().sendString(8155, "@dre@::answer (answer) - answers the trivia");
			player.getPacketSender().sendString(8156, "@dre@::reward 1 all - Rewards you for voting");
			player.getPacketSender().sendString(8157, "@dre@::drop (npc name) - opens drop list of npc");
            player.getPacketSender().sendString(8158, "@dre@::pos - opens up the player shops");
            player.getPacketSender().sendString(8159, "@dre@::drinfo - gives you your drop rate %");
            player.getPacketSender().sendString(8160, "@dre@::drcheck (username) - checks someones droprate %");
            player.getPacketSender().sendString(8160, "@dre@::changebravek - changes slayer master to bravek");
            player.getPacketSender().sendString(8161, "@dre@::gaz - teleports you to giveaway location.");
            player.getPacketSender().sendString(8162, "@dre@::resettask - resets your slayer task.");
            player.getPacketSender().sendString(8163, "@dre@::whatdrops (item name) - shows you what drops the item.");
            player.getPacketSender().sendString(8164, "@dre@::serverperks - contribute towards serverperks (1B COINS)");
            player.getPacketSender().sendString(8165, "@dre@::fuser - Allows you to fuse items together upgrade items");
            player.getPacketSender().sendString(8166, "@dre@::raids - Allows you to enter raids after 1k KC.");
            player.getPacketSender().sendString(8167, "@dre@::afk - teleports you to afk zone and get afk tokens.");
            player.getPacketSender().sendString(8168, "@dre@::npckills - tells you how many NPC kills you have all together");
            player.getPacketSender().sendString(8169, "@dre@::decant - decants all your potions in inventory.");
            player.getPacketSender().sendString(8170, "@dre@::setemail - sets an email to your account for recovery");
            player.getPacketSender().sendString(8171, "@dre@::setpin - sets a pin to your account for recovery");
            player.getPacketSender().sendString(8172, "@dre@::changepassword (password) - changes your password.");

			player.getPacketSender().sendInterface(8134);
		}


/*        if (command[0].equalsIgnoreCase("trinity")) {
            TeleportHandler.teleportPlayer(player, new Position(2517, 4645, 0),
                    player.getSpellbook().getTeleportType());
            player.getPacketSender().sendMessage("Welcome to trinity or w.e the npc is called");
        }*/
        if (command[0].equalsIgnoreCase("checkpoints")) {
            player.sendMessage("@blu@You currently have: @red@" + player.getPointsHandler().getminiGamePoints1()
                    + " Minigame1 Points");
            player.sendMessage("@blu@You currently have: @red@" + player.getPointsHandler().getminiGamePoints2()
                    + " Minigame2 Points");
            player.sendMessage("@blu@You currently have: @red@" + player.getPointsHandler().getminiGamePoints3()
                    + " Minigame3 Points");
            player.sendMessage("@blu@You currently have: @red@" + player.getPointsHandler().getminiGamePoints4()
                    + " Minigame4 Points");
            player.sendMessage("@blu@You currently have: @red@" + player.getPointsHandler().getminiGamePoints5()
                    + " Minigame5 Points");
        }


        if (command[0].equals("changebravek")) {
                SlayerMaster.changeSlayerMaster(player, SlayerMaster.BRAVEK);
        }

        if (command[0].equals("location")) {
            player.getPacketSender().sendMessage("current location: " + player.getLocation());
        }

		

		
		
/*		if (command[0].equalsIgnoreCase("uzone333")) {
			if (player.getAmountDonated() >= 200) {
				TeleportHandler.teleportPlayer(player, new Position(2334, 3640, 0),
						player.getSpellbook().getTeleportType());
				player.getPacketSender().sendMessage("Welcome to UBER ZONE.");
			} else {
				player.sendMessage("@red@nah fam, try again when ya got 200 donated");
				return;
			}
		}*/
        if (command[0].equalsIgnoreCase("gaz")) {
            if (player.getAmountDonated() >= 0) {
                TeleportHandler.teleportPlayer(player, new Position(3355, 2830, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Giveaway Zone.");
            } else {
                player.sendMessage("@red@Good Luck");
                return;
            }
        }
       if (command[0].equalsIgnoreCase("imboss")) {
            TeleportHandler.teleportPlayer(player, new Position(2148, 5089, 0),
                    player.getSpellbook().getTeleportType());
            player.getPacketSender().sendMessage("Welcome to the IronMan boss!");
        }
        /**if (command[0].equalsIgnoreCase("edge")) {
            TeleportHandler.teleportPlayer(player, new Position(3087, 3495, 0),
                    player.getSpellbook().getTeleportType());
            player.getPacketSender().sendMessage("Welcome to Edgeville");
        }
**/

        if (command[0].equalsIgnoreCase("depositbags")) {
            int amount = Integer.parseInt(command[1]);
            if (player.getInventory().getAmount(10835) < amount) {
                player.sendMessage("You don't have that many bags in your inventory.");
                return;
            }

            player.getInventory().delete(10835, amount);
            player.setMoneyInPouch(player.getMoneyInPouch() + (long) amount * 1);
            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
            player.sendMessage("You have deposited " + amount + " 1b coins for "
                    + Misc.formatNumber((long) amount * 1L));
        }

        if (command[0].equalsIgnoreCase("withdrawbags")) {
            int amount = Integer.parseInt(command[1]);
            if (player.getMoneyInPouch() < (long) amount * 1L) {
                player.sendMessage("You don't enough coins in your pouch to withdraw that many bags :/");
                player.sendMessage(
                        "Maximum amount of bags you can withdraw is: @red@" + player.getMoneyInPouch() / 1L);
                return;
            }
            player.setMoneyInPouch(player.getMoneyInPouch() - (long) amount * 1L);
            player.getInventory().add(10835, amount);
            player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
            player.sendMessage("You have withdrawed " + amount + " bags");
        }
	/*	if (command[0].equalsIgnoreCase("event")) {
			TeleportHandler.teleportPlayer(player, new Position(2731, 3475, 0),
					player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("These nubs drop Ecto-Tokens useable on Santa's store");
		}*/
        if (command[0].equalsIgnoreCase("starterzone")) {
            TeleportHandler.teleportPlayer(player, new Position(3298, 2799, 0),
                    player.getSpellbook().getTeleportType());
            player.getPacketSender().sendMessage(
                    "Welcome to starterzone, Jokers drop coins, and alot of things u need as a starter.");
        }

        if (command[0].equalsIgnoreCase("ffa")) {
            if (player.getSummoning().getFamiliar() != null) {
                player.sendMessage("@red@You cannot enter FFA with a pet on");
                return;
            }
            FreeForAll.enterLobby(player);
        }
        if (command[0].equals("players")) {
            int players = World.getPlayers().size() + 0;
            player.getPacketSender().sendMessage("There are currently " + players + " players online!");
            PlayersOnlineInterface.showInterface(player);
        }
/*        if (command[0].equals("platwarriors") || command[0].equals("platwarrior")) {
            TeleportHandler.teleportPlayer(player, new Position(2766, 2799), player.getSpellbook().getTeleportType());
        }
        if (command[0].equalsIgnoreCase("vorago")) {
            TeleportHandler.teleportPlayer(player, new Position(3104, 5537), player.getSpellbook().getTeleportType());
        }
        if (command[0].equalsIgnoreCase("ge")) {
            GrandExchange.open(player);
        }*/
/*        if (command[0].equals("tree")) {
            player.getPacketSender().sendMessage("<img=4><shad=1><col=FF9933> The Evil Tree has sprouted at: "
                    + EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
        }*/
        if (wholeCommand.startsWith("profile")) {
            final String[] s = wholeCommand.split(" ");
            if (s.length < 2) {
                ProfileViewing.view(player, player);
                return;
            }
            final String name = wholeCommand.substring(8);
            final Player other = World.getPlayerByName(name);
            if (other == null) {
                player.sendMessage("Player not online: " + name);
                return;
            }
            ProfileViewing.view(player, other);
        }

        if (command[0].equals("decant")) {
            // PotionDecanting.decantPotions(player);
            Decanting.startDecanting(player);
        }
/*        if (command[0].equals("skull")) {
            if (player.getSkullTimer() > 0) {
                player.setSkullTimer(0);
                player.setSkullIcon(0);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            } else {
                CombatFactory.skullPlayer(player);
            }
        }*/

        if (wholeCommand.startsWith("droplog")) {
            final String[] s = wholeCommand.split(" ");
            if (s.length < 2) {
                PlayerDropLog.sendDropLog(player, player);
                return;
            }
            final String name = wholeCommand.substring(8);
            final Player other = World.getPlayerByName(name);
            if (other == null) {
                player.sendMessage("Player not found: " + name);
                return;
            }
            PlayerDropLog.sendDropLog(player, other);
        } else if (wholeCommand.startsWith("drojjjjjjp") && !wholeCommand.startsWith("droplog")) {
            final String[] s = wholeCommand.split(" ");
            if (s.length < 2) {
                player.sendMessage("Enter npc name!");
                return;
            }
            final String name = wholeCommand.substring(5).toLowerCase();

            if (NpcDefinition.forName(name) != null) {
                final int id = NpcDefinition.forName(name).getId();
                if (id == -1) {
                    player.sendMessage("Npc not found: " + name);
                    return;
                }
                MonsterDrops.sendNpcDrop(player, id, name);
            }
        }
        if (command[0].equalsIgnoreCase("answer")) {
            String triviaAnswer = wholeCommand.substring(7);
            if (TriviaBot.acceptingQuestion())
                TriviaBot.attemptAnswer(player, triviaAnswer);
        }

/*        if (command[0].equalsIgnoreCase("zulrah")) {
            player.getZulrahEvent().initialize();
        }*/

		/*if (command[0].equalsIgnoreCase("keyroom4444")) {
			if (player.getKeyRoom() != null) {
				player.sendMessage("You already have an active Key Room!");
				return;
			}
			if (player.getWildernessLevel() > 20) {
				player.sendMessage("You cannot tele out because your in deep wildy.");
				return;
			}
			player.setKeyRoom(new KeyRoom(player, 1));
			return;
		}*/

/*        if (command[0].equalsIgnoreCase("sagittare")) {
            player.getSagittareEvent().initialize();
        }*/



        if (wholeCommand.equalsIgnoreCase("prayer")) {
            if (player.lastPrayerSwitch > System.currentTimeMillis()) {
                player.sendMessage("You can only use this command once every 15minutes!");
                player.sendMessage("You still need to wait another " + player.getTimeRemaining(player.lastPrayerSwitch));
                return;
            }
            if (player.getPrayerbook() == Prayerbook.NORMAL) {
                player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
                player.setPrayerbook(Prayerbook.CURSES);
                player.lastPrayerSwitch = System.currentTimeMillis() + 900000;// 15mins
            } else {
                player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
                player.setPrayerbook(Prayerbook.NORMAL);
                player.lastPrayerSwitch = System.currentTimeMillis() + 900000;// 15mins
            }
            player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
            PrayerHandler.deactivateAll(player);
            CurseHandler.deactivateAll(player);
        }
/*
        if (command[0].equals("zombie") || command[0].equals("zombies")) {
            TeleportHandler.teleportPlayer(player, new Position(3503, 3563), player.getSpellbook().getTeleportType());
        }
*/


        if (command[0].equalsIgnoreCase("train")) {
            // an example of how to display it(since i don't know where/when u want to display it)
            TeleportHandler.teleportPlayer(player, new Position(3795, 3543, 0),
                    player.getSpellbook().getTeleportType());
            //player.getStarterProgression().open();
            player.getStarterProgression().openInterface();

        }

        if (command[0].equalsIgnoreCase("setemail")) {
            String email = wholeCommand.substring(9);
            player.setEmailAddress(email);
            player.getPacketSender().sendMessage("You set your account's email adress to: [" + email + "] ");
            PlayerPanel.refreshPanel(player);
        }

        if (command[0].equalsIgnoreCase("changepassword")) {
            String syntax = wholeCommand.substring(15);
            if (syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
                player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
                return;
            }
            if (syntax.contains("_")) {
                player.getPacketSender().sendMessage("Your password can not contain underscores.");
                return;
            }
            player.setPassword(syntax);
            player.getPacketSender().sendMessage("Your new password is: [" + syntax + "] Write it down!");

        }

        if (command[0].equalsIgnoreCase("duel")) {
            if (player.getSummoning().getFamiliar() != null) {
                player.getPacketSender().sendMessage("You must dismiss your familiar before teleporting to the arena!");
            } else {
                TeleportHandler.teleportPlayer(player, new Position(3364, 3267),
                        player.getSpellbook().getTeleportType());
            }
        }
        if (command[0].equalsIgnoreCase("store")) {
            player.getPacketSender().openURL("https://zamron.us/store");
        }
        if (command[0].equalsIgnoreCase("vote")) {
            player.getPacketSender().openURL("https://zamron.us/vote");
        }

        if (command[0].equalsIgnoreCase("discord")) {
            player.getPacketSender().openURL("https://discord.gg/CHTjMe3p59");
        }

        if (command[0].equalsIgnoreCase("maxhit")) {
            int attack = DesolaceFormulas.getMeleeAttack(player) / 10;
            int range = DesolaceFormulas.getRangedAttack(player) / 10;
            int magic = DesolaceFormulas.getMagicAttack(player) / 10;
            player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
                    + range + "@bla@, magic attack: @or2@" + magic);
        }
        if (command[0].equals("save")) {
            player.save();
            player.getPacketSender().sendMessage("Your progress has been saved.");
        }

        if (command[0].equals("help")) {
            if (player.getLastYell().elapsed(30000)) {
                World.sendStaffMessage("[TICKET SYSTEM] " + player.getUsername()
                        + " has requested help. Please help them!");
                player.getLastYell().reset();
                player.getPacketSender()
                        .sendMessage("<col=663300>Your help request has been received. Please be patient.");
            } else {
                player.getPacketSender().sendMessage("")
                        .sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage(
                                "<col=663300>If it's an emergency, please private message a staff member directly instead.");
            }
        }
        if (command[0].equals("empty")) {
            player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
            player.getSkillManager().stopSkilling();
            player.getInventory().resetItems().refreshItems();
        }

/*        if (command[0].equalsIgnoreCase("[cn]")) {
            if (player.getInterfaceId() == 40172) {
                ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
            }
        }*/

        if (wholeCommand.toLowerCase().startsWith("yell")) {
            String primaryRights = player.getRights().ordinal() > 0 ? "<img=" + player.getRights().ordinal() + ">" : "";
            String secondaryRights = player.getSecondaryPlayerRights().ordinal() > 0 ? "<zmg=" + player.getSecondaryPlayerRights().ordinal() + ">" : "";
            String irn;
            switch (player.getGameMode()) {
                default:
                case NORMAL:
                    irn = "";
                    break;
                case IRONMAN:
                    irn = "<irn=1193>";
                    break;
                case HARDCORE_IRONMAN:
                    irn = "<irn=1192>";
                    break;
                case GROUP_IRONMAN:
                    irn = "<irn=1036>";
                    break;
            }
            // If the player is not an ironman, prefix is primary + secondary
            // If the player IS an ironman, prefix is ironman + secondary
            String rankIcons = irn.isEmpty() ? primaryRights + secondaryRights : irn + secondaryRights;
            //System.out.println("RANK ICONS FOR CC " + rankIcons);

            if (!GameSettings.YELL_STATUS) {
                player.getPacketSender().sendMessage("Yell is currently unavailable");
                return;
            }
            if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
                player.getPacketSender().sendMessage("You are muted and cannot yell.");
                return;
            }
            int delay = player.getRights().getYellDelay();
            if (!player.getLastYell().elapsed((delay * 1000))) {
                player.getPacketSender().sendMessage(
                        "You must wait at least " + delay + " seconds between every yell-message you send.");
                return;
            }
            String yellMessage = wholeCommand.substring(4, wholeCommand.length());

            player.getLastYell().reset();
            if (player.getRights() == PlayerRights.VIP_DONATOR) {
                World.sendMessageNonDiscord("<shad=2><col=03FFEA>" + rankIcons + "[VIP Donator]</shad>@bla@" + player.getUsername()
                        + ":@bla@" + yellMessage + "</col>");
                return;
            }

            /**if (player.getRights() == PlayerRights.OWNER) {
                World.sendMessageNonDiscord(player.getRights().getYellPrefix() + rankIcons + "<col=ff0000>" + player.getRights().getCustomYellPrefix(true) + "</col> @bla@"
                        + player.getUsername() + ":" + yellMessage);
                return;
            }

            if (player.getRights() == PlayerRights.DEVELOPER) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "@red@ Developer @bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.COMMUNITY_MANAGER) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "@cya@ Manager @bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }**/
            if (player.getRights() == PlayerRights.SUPPORT) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "@blu@[Support]@bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }

            if (player.getRights() == PlayerRights.MODERATOR) {
                World.sendMessageNonDiscord("<shad=2><col=f5faf6>" + rankIcons + "[Moderator]</shad>@bla@" + player.getUsername()
                        + ":@bla@" + yellMessage + "</col>");
                return;
            }
            if (player.getRights() == PlayerRights.ADMINISTRATOR) {
                World.sendMessageNonDiscord(player.getRights().getYellPrefix() + rankIcons + "@or2@ Administrator @bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }

            if (player.getRights().isStaff()) {
                World.sendMessageNonDiscord("<col=#ffffff><shad=555>" + player.getRights().getYellPrefix() + rankIcons + "</shad>[" + player.getRights().toString().toUpperCase() + "]" + player.getUsername() + ":" + player.getRights().getYellHexColor() + yellMessage);
                return;
            }

            if (player.getRights() == PlayerRights.DELUXE_DONATOR) {
                World.sendMessageNonDiscord(player.getRights().getYellPrefix() + rankIcons + " <col=8600CC>"
                        + player.getRights().getCustomYellPrefix(false) + "<col=0D75A5><shad=1>[Deluxe]</shad></col> @bla@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.UBER_DONATOR) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "<col=cdd406><shad=1> [Uber]</shad></col> @bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.LEGENDARY_DONATOR) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "<col=E0AF09><shad=1> [Legendary]</shad></col> @bla@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.EXTREME_DONATOR) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "<col=851CF7><shad=1> [Extreme]</shad></col> @bla@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.SUPER_DONATOR) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "<col=23F112><shad=1> [Super]</shad></col> @bla@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.DONATOR) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "<col=CE1B1C><shad=1> [Donator]</shad></col> @bla@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.PLAYER) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix()
                        + rankIcons
                        + "<col=FF7F00><shad=1> [PLAYER]</shad></col> @whi@" + player.getUsername() + ":"
                        + yellMessage);
                return;
            }
            if (player.getRights() == PlayerRights.YOUTUBER) {
                World.sendMessageNonDiscord("" + player.getRights().getYellPrefix() + rankIcons + "@red@ [Youtuber] @bla@" + player.getUsername() + ":" + yellMessage);
                return;
            }
            // TO-DO
        }
    }

    private static void donator(final Player player, String[] command, String wholeCommand) {

/*		if (command[0].equals("dzone333")) {
			if (player.getRights().isSeniorStaff() || player.getRights().isHighDonator()
					|| player.getRights() == PlayerRights.LEGENDARY_DONATOR
					|| player.getRights() == PlayerRights.EXTREME_DONATOR
					|| player.getRights() == PlayerRights.SUPER_DONATOR || player.getRights() == PlayerRights.DONATOR)
				TeleportHandler.teleportPlayer(player, new Position(3363, 9638),
						player.getSpellbook().getTeleportType());
		}*/
    }

    private static void superDonator(final Player player, String[] command, String wholeCommand) {

        if (command[0].equals("bank") && player.getLocation() != Location.WILDERNESS && player.getLocation() != Location.BOSS_TIER_LOCATION) {
            try {
                player.getBank(player.getCurrentBankTab()).open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void deluxeDonator(final Player player, String[] command, String wholeCommand) {
        if (!player.getRights().isDeluxeDonator(player))
            if (!player.getRights().isVipDonator(player))
                return;

        switch (command[0]) {

            case "setyelltitle": {
                player.setInputHandling(new EnterYellTitle());
                player.getPacketSender().sendEnterInputPrompt("Enter a new yell title:");
                return;
            }

            case "hp": {
                if (player.lastHpRestore > System.currentTimeMillis()) {
                    player.sendMessage("You can only use this command once every 15minutes!");
                    player.sendMessage("You still need to wait another " + player.getTimeRemaining(player.lastHpRestore));
                    return;
                }
                if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= player.getSkillManager()
                        .getMaxLevel(Skill.CONSTITUTION)) {
                    player.sendMessage("You already have full hitpoints.");
                    return;
                }
                player.sendMessage("You have restored your HP back to full.");
                player.lastHpRestore = System.currentTimeMillis() + 900000;// 15mins
                player.heal(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
                return;
            }

            case "pray": {
                if (player.lastPrayerRestore > System.currentTimeMillis()) {
                    player.sendMessage("You can only use this command once every 15minutes!");
                    player.sendMessage(
                            "You still need to wait another " + player.getTimeRemaining(player.lastPrayerRestore));
                    return;
                }
                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= player.getSkillManager()
                        .getMaxLevel(Skill.PRAYER)) {
                    player.sendMessage("You already have full prayer points.");
                    return;
                }
                player.sendMessage("You have restored your Prayer Points back to full.");
                player.lastPrayerRestore = System.currentTimeMillis() + 900000;// 15mins
                player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER));
                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
                        .getMaxLevel(Skill.PRAYER))
                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                return;
            }


        }
    }

    private static void uberDonator(final Player player, String[] command, String wholeCommand) {
        {
				/*if (command[0].equals("delzone333")) {
					if (player.getRights().isSeniorStaff()
							|| player.getRights() == PlayerRights.DELUXE_DONATOR
							|| player.getRights() == PlayerRights.VIP_DONATOR
					)
						TeleportHandler.teleportPlayer(player, new Position(3221, 2780),
								player.getSpellbook().getTeleportType());
				}*/
        }
        if (command[0].equalsIgnoreCase("customtitle")) {
            String title = wholeCommand.substring(12);
            if (title.length() < 1 || title.length() > 21) {
                player.sendMessage("@red@Error - Try another title");
                return;
            }
            String color = command[1];
            switch (color) {
                case "orange":
                    player.setTitle("@or2@" + title.replace(color, "").replace(" ", ""));
                    break;
                case "yellow":
                    player.setTitle("@yel@" + title.replace(color, "").replace(" ", ""));
                    break;
                case "red":
                    player.setTitle("@red@" + title.replace(color, "").replace(" ", ""));
                    break;
                case "green":
                    player.setTitle("@gre@" + title.replace(color, "").replace(" ", ""));
                    break;
                case "blue":
                    player.setTitle("@blu@" + title.replace(color, "").replace(" ", ""));
                    break;
            }
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }

    private static void legendaryDonator(final Player player, String[] command, String wholeCommand) {


    }


    private static void extremeDonator(final Player player, String[] command, String wholeCommand) {
        if (command[0].equalsIgnoreCase("title")) {
            String title = wholeCommand.substring(6);

            if (title == null || title.length() <= 1 || title.length() > 12 || !NameUtils.isValidName(title)) {
                player.getPacketSender().sendMessage("You can not set your title to that!");
                return;
            }

            // overriden permmited strings
            switch (player.getRights()) {
                case ADMINISTRATOR:
                    for (String s : GameSettings.INVALID_NAMES) {
                        if (Arrays.asList(admin).contains(s.toLowerCase())) {
                            continue;
                        }
                        if (title.toLowerCase().contains(s.toLowerCase())) {
                            player.getPacketSender().sendMessage("Your title contains an invalid tag.");
                            return;
                        }
                    }
                    break;
                case MODERATOR:
                    for (String s : GameSettings.INVALID_NAMES) {
                        if (Arrays.asList(mod).contains(s.toLowerCase())) {
                            continue;
                        }
                        if (title.toLowerCase().contains(s.toLowerCase())) {
                            player.getPacketSender().sendMessage("Your title contains an invalid tag.");
                            return;
                        }
                    }
                    break;


                // permitted to use whatever they'd like
                case OWNER:
                case DEVELOPER:
                    break;
                default:
                    for (String s : GameSettings.INVALID_NAMES) {
                        if (title.toLowerCase().contains(s.toLowerCase())) {
                            player.getPacketSender().sendMessage("Your title contains an invalid tag.");
                            return;
                        }
                    }
                    break;
            }
            player.setTitle("@red@" + title);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
		/*if (command[0].equals("ezone")) {
			if (player.getRights().isStaff() || player.getRights() == PlayerRights.UBER_DONATOR
					|| player.getRights() == PlayerRights.LEGENDARY_DONATOR
					|| player.getRights() == PlayerRights.EXTREME_DONATOR
					|| player.getRights() == PlayerRights.DELUXE_DONATOR

			)

				TeleportHandler.teleportPlayer(player, new Position(3429, 2919),
						player.getSpellbook().getTeleportType());
		}*/
    }


    private static final String[] admin = {"admin", "administrator", "a d m i n"};
    private static final String[] mod = {"mod", "moderator", "m o d"};

    private static void memberCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equals("dzone")) {
			TeleportHandler.teleportPlayer(player, new Position(3363, 9638), player.getSpellbook().getTeleportType());
		}

        if (command[0].equalsIgnoreCase("gambleint")) {
            // Player player2 = World.getPlayerByName("emeraldnew");
            // player.getGambling().openGamble(player, player2);
        }

        if (command[0].equalsIgnoreCase("requestgamble")) {
            Player playerName = World.getPlayerByName(command[1]);
            player.getGambling().requestGamble(playerName);
        }

    }

    private static void helperCommands(final Player player, String[] command, String wholeCommand) {

        if (wholeCommand.toLowerCase().startsWith("staff")) {
            String staffMessage = wholeCommand.substring(5);
            String crown = player.getRights().ordinal() > 0 ? "<img=" + player.getRights().ordinal() + ">" : "";
            World.sendStaffYell("<img=12><shad=2><col=03FFEA>[STAFF YELL] : " + crown + player.getUsername() +" " + staffMessage + "</col>");
        }


        if (command[0].equalsIgnoreCase("permban") || command[0].equalsIgnoreCase("permaban")) {
            try {
                Player player2 = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Target does not exist. Unable to permban.");
                    return;
                }


                String uuid = player2.getUuid();
                String mac = player2.getMac();
                String name = player2.getUsername();
                String bannedIP = player2.getHostAddress();

                World.sendStaffMessage("Perm banned " + name + " (" + bannedIP + "/" + mac + "/" + uuid + ")");
                PlayerLogs.log(player.getUsername(), "Has perm banned: " + name + "!");
                PlayerLogs.log(name, player + " perm banned: " + name + ".");

                PlayerPunishment.addBannedIP(bannedIP);
                ConnectionHandler.banUUID(name, uuid);
                ConnectionHandler.banMac(name, mac);
                PlayerPunishment.ban(name);

                if (player2 != null) {
                    World.deregister(player2);
                }

                for (Player playersToBan : World.getPlayers()) {
                    if (playersToBan == null)
                        continue;
                    if (playersToBan.getHostAddress() == bannedIP || playersToBan.getUuid().equals(uuid) || playersToBan.getMac().equals(mac)) {
                        PlayerLogs.log(player.getUsername(), player.getUsername() + " just caught " + playersToBan.getUsername() + " with permban!");
                        PlayerLogs.log(name, player + " perm banned: " + name + ", we were crossfire.");
                        World.sendStaffMessage(playersToBan.getUsername() + " was banned as well.");
                        PlayerPunishment.ban(playersToBan.getUsername());
                        World.deregister(playersToBan);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (command[0].equalsIgnoreCase("taskreset")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online!");
            }
            if (target.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
                player.getPacketSender().sendMessage("This player does not currently have a slayer task");
            } else {
                target.getSlayer().resetSlayerTask();
                target.getPacketSender().sendMessage(player.getRights() + " " + player.getUsername() + " Has reset your slayer task!");
                player.getPacketSender().sendMessage("Reset " + target.getUsername() + "'s Slayer task.");
                DiscordMessenger.sendStaffMessage("[TASK RESET] " + player.getUsername() + " Has reset " + target.getUsername()
                        + " 's slayer task!");
            }

        }

        if (command[0].equals("flub")) {
            TrickOrTreat.pickNextLocation();

        }

        if (command[0].equals("flub2")) {
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 1000; i++) {
                int randomInt = random.nextInt(2000);
                System.out.println(randomInt);
            }
        }

        if (command[0].equalsIgnoreCase("findnpc")) {
            String name = wholeCommand.substring(command[0].length() + 1);
            player.getPacketSender().sendMessage("Finding item id for item - " + name);
            boolean found = false;
            for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
                if (NpcDefinition.forId(i) == null || NpcDefinition.forId(i).getName() == null) {
                    continue;
                }
                if (NpcDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.getPacketSender().sendMessage("Found NPC with name [" + NpcDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                    found = true;
                }
            }
            if (!found) {
                player.getPacketSender().sendMessage("No NPC with name [" + name + "] has been found!");
            }
        }

        if (command[0].equals("checkip")) {
            try {
                Player target = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
                assert target != null;
                IPVerification.manualIPCheck(player, target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (command[0].equals("bank") && player.getLocation() != Location.BOSS_TIER_LOCATION) {
            try {
                player.getBank(player.getCurrentBankTab()).open();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (command[0].equalsIgnoreCase("dismiss")) {
            if (player.getSummoning().getFamiliar() != null) {
                player.getSummoning().unsummon(true, true);
                player.getPacketSender().sendMessage("You've dismissed your familiar.");
            }
        }

        if (command[0].equals("reminddonate")) {
            World.sendMessageNonDiscord("[DONATION DEALS]: (Expires in: @yel@" +DonationDeals.timeLeft+")");
        }

        if (command[0].equals("remindvote")) {
            World.sendMessageNonDiscord(
                    "<img=12> <col=008FB2>Remember to collect rewards by using the ::vote command every 12 hours!");
        }
        if (command[0].equals("remindvoting")) {
            World.sendMessageNonDiscord(
                    "<img=12> <col=008FB2>Did you know that The voting is currently tripled? Vote for 9 points now");
        }

        if (command[0].equals("staffzone")) {
            if (command.length > 1 && command[1].equals("all")) {
                for (Player players : World.getPlayers()) {
                    if (players != null) {
                        if (players.getRights().isSeniorStaff()) {
                            TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
                        }
                    }
                }
            } else {
                TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);
            }
        }
        if (command[0].equalsIgnoreCase("saveall")) {
            World.savePlayers();
            player.getPacketSender().sendMessage("Saved players!");
        }
        if (command[0].equalsIgnoreCase("teleto")) {
            String playerToTele = wholeCommand.substring(7);
            Player player2 = World.getPlayerByName(playerToTele);
            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            } else {
                boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
                        && player.getRegionInstance() == null && player2.getRegionInstance() == null;
                if (player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR) {
                    if (player2.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        player.sendMessage("you can't teleport to someone who is in dungeonnering");
                        return;
                    }
                }

                if (canTele) {
                    TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
                    player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
                } else {
                    player.getPacketSender()
                            .sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
                }
            }
        }
        if (command[0].equalsIgnoreCase("sendhome")) {
            String player2 = command[1];
            player2 = Misc.formatText(player2.replaceAll("_", " "));
            if (command.length >= 3 && command[2] != null) {
                player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
            }
            Player playerToMove = World.getPlayerByName(player2);
            if (playerToMove != null) {
                playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
                playerToMove.heal(990);
                playerToMove.getPacketSender()
                        .sendMessage("You've been teleported home by " + player.getUsername() + ".");
                player.getPacketSender()
                        .sendConsoleMessage("Successfully moved " + playerToMove.getUsername() + " to home.");
            }
        }

    }

    private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {

        if (command[0].equalsIgnoreCase("toggleinvis")) {
            player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        /*if (command[0].equalsIgnoreCase("raidlobby")) {
            player.getCustomRaid().open(player);
        }
        if (command[0].equalsIgnoreCase("raids")) {
            TeleportHandler.teleportPlayer(player, new Position(3044, 5233), player.getSpellbook().getTeleportType());
            player.sendMessage("to create a raid party do ::raidlobby");
        }*/
        if (command[0].equalsIgnoreCase("teletome")) {
            String playerToTele = wholeCommand.substring(command[0].length() + 1);
            Player player2 = World.getPlayerByName(playerToTele);
            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            } else {
                boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
                        && player.getRegionInstance() == null && player2.getRegionInstance() == null;
                if (canTele) {
                    TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
                    player.getPacketSender()
                            .sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
                    player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
                } else {
                    player.getPacketSender().sendConsoleMessage(
                            "You can not teleport that player at the moment. Maybe you or they are in a minigame?");
                }
            }
        }

    }

    private static void administratorCommands(final Player player, String[] command, String wholeCommand) {

        if (command[0].equals("tele")) {
            int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
            int z = player.getPosition().getZ();
            if (command.length > 3) {
                z = Integer.valueOf(command[3]);
            }
            Position position = new Position(x, y, z);
            player.moveTo(position);
            player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
        }
        if (command[0].equalsIgnoreCase("togglezone")) {
            GameSettings.EventArena = !GameSettings.EventArena;
            World.sendMessageNonDiscord("@blu@[Event Arena] @red@" + player.getUsername() + " @blu@has @red@" + (GameSettings.EventArena ? "activated" : "disabled") + " @blu@the zone!");
        }
        if (command[0].equals("checkequip")) {
            Player player2 = World.getPlayerByName(wholeCommand.substring(11));
            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            }
            player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            BonusManager.update(player);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("checkinv")) {
            Player player2 = World.getPlayerByName(wholeCommand.substring(9));
            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            }
            player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
        }
        if (command[0].equalsIgnoreCase("title")) {
            String title = wholeCommand.substring(6);
            if (title == null || title.length() <= 1 || title.length() > 12 || !NameUtils.isValidName(title)) {
                player.getPacketSender().sendMessage("You can not set your title to that!");
                return;
            }
            player.setTitle("@red@" + title);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equalsIgnoreCase("givespecial")) {
            int amount = Integer.parseInt(command[1]);
            String rss = command[2];
            if (command.length > 3) {
                rss += " " + command[3];
            }
            if (command.length > 4) {
                rss += " " + command[4];
            }
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendMessage("Player must be online to give them Special attack");
            } else {
                target.setSpecialPercentage(amount);
                CombatSpecial.updateBar(player);
                target.sendMessage("@red@Your Special Attack Bar has been incremented by " + amount);
                player.sendMessage("@red@Gave " + target + " " + amount + " Special attack");
            }
        }
        if (command[0].equals("find")) {
            String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
            player.getPacketSender().sendMessage("Finding item id for item - " + name);
            boolean found = false;
            for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.getPacketSender().sendMessage("Found item with name ["
                            + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                    found = true;
                }
            }
            if (!found) {
                player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
            }
        } else if (command[0].equals("id")) {
            String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
            player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
            boolean found = false;
            for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
                if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                    player.getPacketSender().sendConsoleMessage("Found item with name ["
                            + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                    found = true;
                }
            }
            if (!found) {
                player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
            }
        }
    }

    private static int entries = 0;

    private static void ownerCommands(final Player player, String[] command, String wholeCommand) {

        if (command[0].equals("item")) {
            int id = Integer.parseInt(command[1]);
            int amount = (command.length == 2 ? 1
                    : Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
                    .replaceAll("b", "000000000")));
            if (amount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE;
            }
            Item item = new Item(id, amount);
            player.getInventory().add(item, true);

            player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
        }

        if (command[0].equalsIgnoreCase("givemod")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.MODERATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " moderator.");
            }
        }
        if (command[0].equalsIgnoreCase("giveadmin")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.ADMINISTRATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " admin.");
            }
        }
        if (command[0].equalsIgnoreCase("giveha")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.VIP_DONATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " HAdmin.");
            }
        }
        if (command[0].equalsIgnoreCase("givecm")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.COMMUNITY_MANAGER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " CM.");
            }
        }
        if (command[0].equalsIgnoreCase("giveyt")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.YOUTUBER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " Youtuber.");
            }
        }

        if (command[0].equalsIgnoreCase("givess")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.SUPPORT);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " support.");
            }
        }

        if (command[0].equalsIgnoreCase("givedonator")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givesuper")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.SUPER_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " super Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("giveextreme")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.EXTREME_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " extreme Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givelegendary")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.LEGENDARY_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " legendary Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("giveuber")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.UBER_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " uber Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givedeluxe")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.DELUXE_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " deluxe Donator Rank.");
            }
        }

        if (command[0].equalsIgnoreCase("spawnsb")) {
            SkillBossHandler.spawnSkillBoss();
        }

        if (command[0].equalsIgnoreCase("spawndark")) {
            DarkRanger.spawn();
        }

        if (command[0].equals("master")) {
            for (Skill skill : Skill.values()) {
                int level = SkillManager.getMaxAchievingLevel(skill);
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                        SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
            }
            player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        if (command[0].equalsIgnoreCase("cpuban")) {
            Player player2 =
                    PlayerHandler.getPlayerForName(wholeCommand.substring(10));
            if (player2 !=
                    null && player2.getSerialNumber() != null) { //
                ConnectionHandler.banComputer(player2.getUsername(),
                        player2.getSerialNumber());
                player.getPacketSender().sendConsoleMessage(player2.getUsername() +
                        " has been CPU-banned.");
            } else
                player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player."
                );
        }
        //works !

        if (command[0].contains("host")) {
            String plr = wholeCommand.substring(command[0].length() + 1);
            Player playr2 = World.getPlayerByName(plr);
            if (playr2 != null) {
                player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
                        + playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
            } else {
                player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
            }
        }

        switch (command[0]) {

            case "macban":
                String name = wholeCommand.substring(6);

                Player target = World.getPlayerByName(name);

                if (target != null) {

                    if (target.getMacAddress().toLowerCase().contains("no mac")) {
                        return;
                    }
                    if (target.getMacAddress() != null)
                        MACBanL.ban(target, target != null);
                } else
                    player.getPacketSender().sendMessage("Unable to find player")
                            ;
                return;
        }

        if (command[0].equalsIgnoreCase("donamount")) {
            String name = wholeCommand.substring(10);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {

                player.getPacketSender().sendMessage("Player has donated: " + target.getAmountDonated() + " Dollars.");
            }
        }

        if (command[0].equals("reset")) {
            for (Skill skill : Skill.values()) {
                int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                        SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
            }
            player.getPacketSender().sendConsoleMessage("Your skill levels have now been reset.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        /** Mini me commands **/

        if (wholeCommand.equalsIgnoreCase("minime")) {
            if (player.getMinime() != null) {
                player.getMinime().flagBotRemoval();
            } else {
                MiniMeFunctions.create(player);
                player.getPacketSender().sendMessage("You Summon Your Minime!");
            }
        }

        if (command[0].equalsIgnoreCase("minime") && wholeCommand.length() > command[0].length()) {
            // Handle other commands
            switch (command[1]) {
                case "follow":
                    MiniMeFunctions.startFollowing(player);
                    break;
                case "unfollow":
                case "stopfollow":
                    MiniMeFunctions.stopFollowing(player);
                    break;
                case "save":
                    MiniMeData.save(player.getMinime());
                    break;
            }

        }

        if (command[0].equals("getpass")) {
            String targetName = wholeCommand.substring(command[0].length() + 1);
            DiscordMessenger.sendStaffMessage("**"
                    + player.getUsername()
                    + " just requested "
                    + targetName
                    + "'s password!**");
            File playerFile = new File("data/saves/characters/" + targetName + ".json");

            if (!playerFile.exists()) {
                player.sendMessage("Player file not found!");
                return;
            }

            try (FileReader fileReader = new FileReader(playerFile)) {
                JsonParser fileParser = new JsonParser();
                JsonObject reader = (JsonObject) fileParser.parse(fileReader);

                if (reader.has("password")) {
                    String password = reader.get("password").getAsString();
                    byte[] passBytes = password.getBytes();
                    if (passBytes.length >= 16) { //This is included so that it can encrypt passwords that are not currently encrypted.
                        password = Encryptor.decrypt(password, Encryptor.globalKey);
                    }

                    player.sendMessage(targetName + "'s pass is: " + password);
                }
            } catch (Exception e) {
                System.out.println("Error getting pass " + e);
            }
        }

        if (command[0].equals("fly")) {
            if (player.getCharacterAnimations().getStandingAnimation() != 1501) {
                player.performAnimation(new Animation(1500));
                player.getCharacterAnimations().setStandingAnimation(1501);
                player.getCharacterAnimations().setWalkingAnimation(1851);
                player.getCharacterAnimations().setRunningAnimation(1851);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.sendMessage("You turn Fly mode on.");
            } else {
                player.getCharacterAnimations().setStandingAnimation(0x328);
                player.getCharacterAnimations().setWalkingAnimation(0x333);
                player.getCharacterAnimations().setRunningAnimation(0x328);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                player.sendMessage("Flymode has been deactivated.");
            }
        }
        if (command[0].equals("checktieritems")) {
            DonationChests.checkItems();
        }


        if (command[0].equals("raidinstance")) {
            System.out.println("Raid instance " + player.getCustomRaid().toString());
        }

        if (command[0].equals("maxskills")) {
            System.out.println("Max skills " + Skill.values().length);
        }

        if (command[0].equals("discordtest")) {
            DiscordMessenger.sendRareDrop("Oogle", (" 1 x Dragon Platelegs from the KBD worth " + Misc.currency(200000000)));
            DiscordMessenger.sendNewPlayer("[NEW PLAYER] Oogle's Test Account has just joined Zamron!");
            DiscordMessenger.sendInGameMessage("A new world boss has just spawned in the wilderness!");
            DiscordMessenger.sendStaffMessage("Oogle has just been caught trying to dupe! Damn SLUT!");
        }


        if (wholeCommand.equals("afk")) {
            World.sendMessageNonDiscord("<img=12> <col=FF0000><shad=0>" + player.getUsername()
                    + ": I am now away, please don't message me; I won't reply.");
        }

        if (command[0].equalsIgnoreCase("destructinstance")) {
            BossMinigameFunctions.despawnNpcs(player);
            player.getPacketSender().sendMessage("Instances destructed");

        }

        /** End test commands **/

        if (command[0].equalsIgnoreCase("setdailynpc")) {
            int NPC_ID = Integer.parseInt(command[1]);
            DailyNPCTask.CHOSEN_NPC_ID = NPC_ID;
            World.sendMessageNonDiscord("@red@Today's Daily NPC task is now:"
                    + DailyNPCTask.KILLS_REQUIRED
                    + " x "
                    + NpcDefinition.forId(DailyNPCTask.CHOSEN_NPC_ID));
        }

        if (command[0].equalsIgnoreCase("customdailytask")) {
            int killsRequired = Integer.parseInt(command[1]);
            int NPCID = Integer.parseInt(command[2]);
            DailyNPCTask.KILLS_REQUIRED = killsRequired;
            DailyNPCTask.CHOSEN_NPC_ID = NPCID;
            World.sendMessageNonDiscord("@red@Today's Daily NPC task is now:"
                    + DailyNPCTask.KILLS_REQUIRED
                    + " x "
                    + NpcDefinition.forId(DailyNPCTask.CHOSEN_NPC_ID).getName());

        }

        if (command[0].equalsIgnoreCase("resetdailytask")) {
            DailyNPCTask.resetDailyNPCGame(player);
        }

        if (command[0].equalsIgnoreCase("removeAllEntries")) {
            entries = 0;
            World.getPlayers().forEach(players -> {
                players.hasEntered = false;
            });
        }

        if (command[0].equalsIgnoreCase("alltome")) {
            Position myPosition = player.getPosition();
            World.getPlayers().forEach(x -> {
                TeleportHandler.teleportPlayer(x, myPosition, x.getSpellbook().getTeleportType());
            });
        }

        if (command[0].equalsIgnoreCase("alert")) {
            String msg = "";
            for (int i = 1; i < command.length; i++) {
                msg += command[i] + " ";
            }
            World.sendMessageNonDiscord("Alert##Notification##" + msg + "##By: " + player.getUsername());
        }
        if (command[0].equals("emptyitem")) {
            if (player.getInterfaceId() > 0
                    || player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
                player.getPacketSender().sendMessage("You cannot do this at the moment.");
                return;
            }
            int item = Integer.parseInt(command[1]);
            int itemAmount = player.getInventory().getAmount(item);
            Item itemToDelete = new Item(item, itemAmount);
            player.getInventory().delete(itemToDelete).refreshItems();
        }
        if (command[0].equals("totalgold")) {
            Player p = World.getPlayerByName(wholeCommand.substring(10));
            if (p != null) {
                long gold = 0;
                for (Item item : p.getInventory().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable()) {
                        gold += item.getDefinition().getValue();
                    }
                }
                for (Item item : p.getEquipment().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable()) {
                        gold += item.getDefinition().getValue();
                    }
                }
                for (int i = 0; i < 9; i++) {
                    for (Item item : p.getBank(i).getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable()) {
                            gold += item.getDefinition().getValue();
                        }
                    }
                }
                gold += p.getMoneyInPouch();
                player.getPacketSender().sendMessage(
                        p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
            } else {
                player.getPacketSender().sendMessage("Can not find player online.");
            }
        }

        if (command[0].equals("cashineco")) {
            int gold = 0, plrLoops = 0;
            for (Player p : World.getPlayers()) {
                if (p != null) {
                    for (Item item : p.getInventory().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable()) {
                            gold += item.getDefinition().getValue();
                        }
                    }
                    for (Item item : p.getEquipment().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable()) {
                            gold += item.getDefinition().getValue();
                        }
                    }
                    for (int i = 0; i < 9; i++) {
                        for (Item item : player.getBank(i).getItems()) {
                            if (item != null && item.getId() > 0 && item.tradeable()) {
                                gold += item.getDefinition().getValue();
                            }
                        }
                    }
                    gold += p.getMoneyInPouch();
                    plrLoops++;
                }
            }
            player.getPacketSender().sendMessage(
                    "Total bag in economy right now: " + gold + ", went through " + plrLoops + " players items.");
        }

        if (command[0].equalsIgnoreCase("emptypouch")) {
            String name = wholeCommand.substring(11);
            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is offline");
            } else {
                target.setMoneyInPouch(0);
            }

        }
        if (command[0].equals("setlev")) {
            String name = wholeCommand.substring(command[0].length() + 1);
            Player target = World.getPlayerByName(name);
            int skillId = Integer.parseInt(command[1]);
            int level = Integer.parseInt(command[2]);
            if (level > 15000) {
                player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
                return;
            }
            Skill skill = Skill.forId(skillId);
            target.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                    SkillManager.getExperienceForLevel(level));
            player.getPacketSender().sendConsoleMessage("You have set his " + skill.getName() + " level to " + level);
        }
        if (command[0].equalsIgnoreCase("claimreward")) {
            StarterTasks.claimReward(player);
        }

        if (command[0].equalsIgnoreCase("fillbank")) {
            int amount = Integer.parseInt(command[1]);
            for (int i = 0; i < amount; i++) {
                int items = Misc.getRandom(15000);
                player.getBank(player.getCurrentBankTab()).add(items, 1);
            }
        }

        if (command[0].equalsIgnoreCase("completevote")) {
            StarterTasks.finishTask(player, StarterTaskData.REDEEM_A_VOTE_SCROLL);
        }

        /*
         * if(command[0].equalsIgnoreCase("great")) {
         * StarterTasks.updateInterface(player); int[] ids = {14484, 1050, 1048, 1046,
         * 1044, 1042, 1040, 11694, 6570, 3083, 5141, 5140, 5139, 5165, 5164, 4769 };
         * for(int i = 0; i < ids.length; i++) {
         * player.getPacketSender().sendItemOnInterface(53205, ids[i], i, 1); }
         * player.getPacketSender().sendInterfaceReset();
         * player.getPacketSender().sendInterface(53200); }
         */
        if (command[0].equals("takeitem")) {
            int item = Integer.parseInt(command[1]);
            int amount = Integer.parseInt(command[2]);
            String rss = command[3];
            if (command.length > 4) {
                rss += " " + command[4];
            }
            if (command.length > 5) {
                rss += " " + command[5];
            }
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendConsoleMessage("Player must be online to take items from them!");
            } else {
                player.getPacketSender().sendConsoleMessage("Item's removed..");
                target.getInventory().delete(item, amount, true);
                target.getBank(0).delete(item, amount, true);
            }
        }
        if (command[0].equals("forcefollow")) {
            Player target = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
            target.getMovementQueue().setFollowCharacter(player);
            target.forceChat("I will follow you until I die!");
        }
        if (command[0].equalsIgnoreCase("god")) {
            player.setSpecialPercentage(15000);
            CombatSpecial.updateBar(player);
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, 150000);
            player.getSkillManager().setCurrentLevel(Skill.ATTACK, 15000);
            player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 15000);
            player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 15000);
            player.getSkillManager().setCurrentLevel(Skill.RANGED, 15000);
            player.getSkillManager().setCurrentLevel(Skill.MAGIC, 15000);
            player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 150000);
            player.getSkillManager().setCurrentLevel(Skill.SUMMONING, 15000);
            player.setHasVengeance(true);
            player.performAnimation(new Animation(725));
            player.performGraphic(new Graphic(1555));
            player.getPacketSender().sendMessage("You're a god, and everyone knows it.");
        }
        if (command[0].equalsIgnoreCase("ungod")) {
            player.setSpecialPercentage(100);
            CombatSpecial.updateBar(player);
            player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER));
            player.getSkillManager().setCurrentLevel(Skill.ATTACK, player.getSkillManager().getMaxLevel(Skill.ATTACK));
            player.getSkillManager().setCurrentLevel(Skill.STRENGTH, player.getSkillManager().getMaxLevel(Skill.STRENGTH));
            player.getSkillManager().setCurrentLevel(Skill.DEFENCE, player.getSkillManager().getMaxLevel(Skill.DEFENCE));
            player.getSkillManager().setCurrentLevel(Skill.RANGED, player.getSkillManager().getMaxLevel(Skill.RANGED));
            player.getSkillManager().setCurrentLevel(Skill.MAGIC, player.getSkillManager().getMaxLevel(Skill.MAGIC));
            player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
            player.getSkillManager().setCurrentLevel(Skill.SUMMONING, player.getSkillManager().getMaxLevel(Skill.SUMMONING));
            player.setSpecialPercentage(100);
            player.setHasVengeance(false);
            player.performAnimation(new Animation(860));
            player.getPacketSender().sendMessage("You cool down, and forfeit god mode.");
        }
        if (command[0].equalsIgnoreCase("buff")) {
            String playertarget = wholeCommand.substring(command[0].length() + 1);
            Player player2 = World.getPlayerByName(playertarget);
            if (player2 != null) {
                player2.getSkillManager().setCurrentLevel(Skill.ATTACK, 1000);
                player2.getSkillManager().setCurrentLevel(Skill.DEFENCE, 1000);
                player2.getSkillManager().setCurrentLevel(Skill.STRENGTH, 1000);
                player2.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 149000);
                player.getPacketSender().sendMessage("We've buffed " + player2.getUsername() + "'s attack, def, and str to 1000.");
                World.sendMessageNonDiscord("@red@<img=3><img=4> [OWN/DEV]<col=6600FF> " + player.getUsername() + " just buffed " + player2.getUsername() + "'s stats.");
            } else {
                player.getPacketSender().sendMessage("Invalid player... We could not find \"" + playertarget + "\"...");
            }
        }
        if (command[0].equalsIgnoreCase("freeze")) {
            String playerToFreeze = wholeCommand.substring(command[0].length() + 1).toLowerCase().replaceAll("_", " ");
            Player player2 = World.getPlayerByName(playerToFreeze);

            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            }

            player2.setFreezeDelay(Integer.MAX_VALUE);
            player2.getPacketSender().sendMessage(player.getUsername() + " has frozen me!");
            player2.setResetMovementQueue(true);
        }

        if (command[0].equalsIgnoreCase("unnull")) {
            String playerToUnfreeze = wholeCommand.substring(command[0].length() + 1).toLowerCase().replaceAll("_", " ");
            Player player2 = World.getPlayerByName(playerToUnfreeze);

            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            }

            player2.setFreezeDelay(-1);
            player2.heal(990);
            player2.getPacketSender().sendMessage(player.getUsername() + " has unfrozen me!");
            player2.setResetMovementQueue(true);
        }
        if (command[0].contains("pure")) {
            int[][] data = new int[][]{{Equipment.HEAD_SLOT, 1153}, {Equipment.CAPE_SLOT, 10499},
                    {Equipment.AMULET_SLOT, 1725}, {Equipment.WEAPON_SLOT, 4587}, {Equipment.BODY_SLOT, 1129},
                    {Equipment.SHIELD_SLOT, 1540}, {Equipment.LEG_SLOT, 2497}, {Equipment.HANDS_SLOT, 7459},
                    {Equipment.FEET_SLOT, 3105}, {Equipment.RING_SLOT, 2550}, {Equipment.AMMUNITION_SLOT, 9244}};
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().setItem(slot, new Item(id, id == 9244 ? 500 : 1));
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
            player.getEquipment().refreshItems();
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getInventory().resetItems();
            player.getInventory().add(1216, 1000).add(9186, 1000).add(862, 1000).add(892, 10000).add(4154, 5000)
                    .add(2437, 1000).add(2441, 1000).add(2445, 1000).add(386, 1000).add(2435, 1000);
            player.getSkillManager().newSkillManager();
            player.getSkillManager().setMaxLevel(Skill.ATTACK, 60).setMaxLevel(Skill.STRENGTH, 85)
                    .setMaxLevel(Skill.RANGED, 85).setMaxLevel(Skill.PRAYER, 520).setMaxLevel(Skill.MAGIC, 70)
                    .setMaxLevel(Skill.CONSTITUTION, 850);
            for (Skill skill : Skill.values()) {
                player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill))
                        .setExperience(skill,
                                SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
            }
        }
        if (command[0].equalsIgnoreCase("demote")) {
            String name = wholeCommand.substring(6);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.PLAYER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + "player.");
            }
        }

        if (command[0].equalsIgnoreCase("clicktele")) {
            player.clickToTeleport = !player.clickToTeleport;
            player.getPacketSender().sendMessage("Click teleporting is now: " +(player.clickToTeleport ? "Disabled" : "Enabled"));
        }

        if (command[0].equals("mypos")) {
            player.getPacketSender().sendConsoleMessage(player.getPosition().toString());
        }
        if (wholeCommand.equals("sfs")) {
            Lottery.restartLottery();
        }
        if (command[0].equals("giveitem")) {
            int item = Integer.parseInt(command[1]);
            int amount = Integer.parseInt(command[2]);
            String rss = command[3];
            if (command.length > 4) {
                rss += " " + command[4];
            }
            if (command.length > 5) {
                rss += " " + command[5];
            }
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
            } else {
                player.getPacketSender().sendConsoleMessage("Gave player gold.");
                target.getInventory().add(item, amount);
            }
        }
        if (command[0].equals("update")) {
            DiscordMessenger.sendGeneralChat("Zamron is updating!", "We'll be back shortly!");
            int time = Integer.parseInt(command[1]);
            if (time > 0) {
                GameServer.setUpdating(true);
                for (Player players : World.getPlayers()) {
                    if (players == null) {
                        continue;
                    }
                    players.getPacketSender().sendSystemUpdate(time);
                }
                TaskManager.submit(new Task(time) {
                    @Override
                    protected void execute() {
                        World.savePlayers();
                        for (Player player : World.getPlayers()) {
                            if (player != null) {
                                World.deregister(player);
                            }
                        }
                        WellOfGoodwill.save();
                        WellOfWealth.save();
                        GrandExchangeOffers.save();
                        ClanChatManager.save();
                        GlobalPerks.getInstance().save();
                        GameServer.getLogger().info("Update task finished!");
                        stop();
                    }
                });
            }
        }
        if (command[0].contains("host")) {
            String plr = wholeCommand.substring(command[0].length() + 1);
            Player playr2 = World.getPlayerByName(plr);
            if (playr2 != null) {
                player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
                        + playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
            } else {
                player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
            }
        }
    }

    private static void developerCommands(Player player, String command[], String wholeCommand) {

        if (command[0].equalsIgnoreCase("givemod")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.MODERATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " moderator.");
            }
        }
        if (command[0].equalsIgnoreCase("giveadmin")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.ADMINISTRATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " admin.");
            }
        }
        if (command[0].equalsIgnoreCase("giveha")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.VIP_DONATOR);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " HAdmin.");
            }
        }
        if (command[0].equalsIgnoreCase("givecm")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.COMMUNITY_MANAGER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " CM.");
            }
        }
        if (command[0].equalsIgnoreCase("giveyt")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.YOUTUBER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " Youtuber.");
            }
        }

        if (command[0].equalsIgnoreCase("givess")) {
            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.SUPPORT);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " support.");
            }
        }

        if (command[0].equalsIgnoreCase("givedonator")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givesuper")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.SUPER_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " super Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("giveextreme")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.EXTREME_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " extreme Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givelegendary")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.LEGENDARY_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " legendary Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("giveuber")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.UBER_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " uber Donator Rank.");
            }
        }
        if (command[0].equalsIgnoreCase("givedeluxe")) {

            String name = wholeCommand.substring(command[0].length() + 1);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.DELUXE_DONATOR);
                target.getPacketSender().sendRights();
                target.incrementAmountDonated(10);
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + " deluxe Donator Rank.");
            }
        }

        if (command[0].equalsIgnoreCase("spawnsb")) {
            SkillBossHandler.spawnSkillBoss();
        }

        if (command[0].equalsIgnoreCase("spawndark")) {
            DarkRanger.spawn();
        }

        if (command[0].equals("master")) {
            for (Skill skill : Skill.values()) {
                int level = SkillManager.getMaxAchievingLevel(skill);
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                        SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
            }
            player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        if (command[0].equalsIgnoreCase("cpuban")) {
            Player player2 =
                    PlayerHandler.getPlayerForName(wholeCommand.substring(10));
            if (player2 !=
                    null && player2.getSerialNumber() != null) { //
                ConnectionHandler.banComputer(player2.getUsername(),
                        player2.getSerialNumber());
                player.getPacketSender().sendConsoleMessage(player2.getUsername() +
                        " has been CPU-banned.");
            } else
                player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player."
                );
        }
        //works !

        if (command[0].contains("host")) {
            String plr = wholeCommand.substring(command[0].length() + 1);
            Player playr2 = World.getPlayerByName(plr);
            if (playr2 != null) {
                player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
                        + playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
            } else {
                player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
            }
        }

        switch (command[0]) {

            case "macban":
                String name = wholeCommand.substring(6);

                Player target = World.getPlayerByName(name);

                if (target != null) {

                    if (target.getMacAddress().toLowerCase().contains("no mac")) {
                        return;
                    }
                    if (target.getMacAddress() != null)
                        MACBanL.ban(target, target != null);
                } else
                    player.getPacketSender().sendMessage("Unable to find player")
                            ;
                return;
        }

        if (command[0].equalsIgnoreCase("donamount")) {
            String name = wholeCommand.substring(10);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {

                player.getPacketSender().sendMessage("Player has donated: " + target.getAmountDonated() + " Dollars.");
            }
        }

        if (command[0].equals("reset")) {
            for (Skill skill : Skill.values()) {
                int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
                player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                        SkillManager.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
            }
            player.getPacketSender().sendConsoleMessage("Your skill levels have now been reset.");
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        if (command[0].equalsIgnoreCase("raidlobby")) {
            player.getCustomRaid().open(player);
        }

        switch (command[0]) {

            case "debug":
                GameSettings.SHOW_DEBUG_MESSAGES = (!GameSettings.SHOW_DEBUG_MESSAGES);
                break;
            case "testingscav":
                player.getSkillManager().addExperience(Skill.SCAVENGING, 15000);
                player.sendMessage("done");
                break;
            case "rankid":
                player.sendMessage("Send my ID: " + player.getRights().ordinal());

                break;
            case "dumpicons": {
                player.sendMessage("Sending icons...");
                for (int i = 0; i < 355; i++)
                    player.sendMessage("Icon Id=" + i + " Image=<img=" + i + ">");
                return;
            }
            case "toggledonations": {
                World.DOUBLE_DONATIONS = !World.DOUBLE_DONATIONS;
                String toggle = "";
                if (World.DOUBLE_DONATIONS) {
                    toggle = "Enabled";
                } else {
                    toggle = "Disabled";
                }
                World.sendMessageNonDiscord("<img=12>@blu@[DOUBLE DONATIONS] "+player.getUsername() + " has "+toggle+" double donations!");
                DiscordMessenger.sendDonationDeals("[DOUBLE DONATIONS] " +player.getUsername()+ " has " +toggle+ " double donations!");
                return;
            }
            case "doublexp": {
                GameSettings.DOUBLE_EXP = !GameSettings.DOUBLE_EXP;
                String toggle = "";
                if (GameSettings.DOUBLE_EXP) {
                    toggle = "Enabled";
                } else {
                    toggle = "Disabled";
                }
                World.sendMessageDiscord(player.getUsername() + " Has "+toggle+" double XP!");
                return;
            }
            case "teleto": {
                String playerToTele = wholeCommand.substring(7);
                Player player2 = World.getPlayerByName(playerToTele);
                try {
                    TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
                    player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
                } catch (Exception e) {
                    player.getPacketSender().sendConsoleMessage("Player appears to be Offline. (If bugged report to 24th)");
                }
                return;
            }

            case "xteletome": {
                String playerToTele = wholeCommand.substring(10);
                Player player2 = World.getPlayerByName(playerToTele);
                try {
                    TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
                    player.getPacketSender().sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
                    player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
                } catch (Exception e) {
                    player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                }
                return;
            }

        }


        if (command[0].equalsIgnoreCase("checkip")) {

            Player target = World.getPlayerByName(command[1]);
            player.sendMessage("@blu@" + target.getUsername() + "'s ip address: @red@" + target.getHostAddress());
            World.getPlayers().forEach(p -> {
                if (p.getHostAddress().equals(target.getHostAddress())
                        && !p.getUsername().equalsIgnoreCase(target.getUsername())) {
                    player.sendMessage("@blu@User: " + p.getUsername() + " has the same ip as " + target.getUsername()
                            + "@red@ ( " + target.getHostAddress() + " )");
                }
            });

        }

        if (command[0].equalsIgnoreCase("slot")) {
            player.getPacketSender().sendInterface(57380);
            int[] items = new int[]{14484, 11694, 1050, 1048, 1046, 1044, 1042, 1040, 4565, 4151, 1, 2, 3, 4, 5, 6, 7,
                    8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
                    33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
                    58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,
                    83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 41, 42, 43, 44, 45, 46, 47,
                    48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
                    73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97,
                    98, 99, 100, 88, 89, 90, 91, 92, 93, 94, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
                    60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
                    85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 41, 42, 43, 44, 45, 46, 47, 48, 49,
                    50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
                    75, 76, 77, 78, 79, 80,};
            //System.out.println("Items length: " + items.length);
            for (int i = 0; i < items.length; i++) {
                player.getPacketSender().sendSlotmachineItems(57391, items[i], i, 1);
                // player.getPacketSender().sendItemOnInterface(57391, items[i], i, 1);
            }
        }

        if (command[0].equalsIgnoreCase("giveironman")) {

            int which = Integer.parseInt(command[1]);

            if (which == 0) {
                player.setGameMode(GameMode.IRONMAN);
            } else {
                player.setGameMode(GameMode.HARDCORE_IRONMAN);
            }

            player.sendMessage("Enjoy ur: " + player.getGameMode().toString());

            player.getPacketSender().sendIronmanMode(player.getGameMode().ordinal());

        }

        if (command[0].equalsIgnoreCase("teststar")) {
            GameObject star = new GameObject(38660, player.getPosition());
            CustomObjects.spawnGlobalObject(star);
        }
        if (command[0].equalsIgnoreCase("givetotaldonated")) {
            int amount = Integer.parseInt(command[1]);
            String rss = command[2];
            if (command.length > 3) {
                rss += " " + command[3];
            }
            if (command.length > 4) {
                rss += " " + command[4];
            }
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendMessage("Player must be online to give them total donated");
            } else {
                target.incrementAmountDonated(amount);
                PlayerPanel.refreshPanel(target);
                target.sendMessage("@red@Your total donated has been incremented by " + amount);
            }
        }

        if (command[0].equals("antibot")) {
            AntiBotting.sendPrompt(player);
        }

        if (command[0].equals("checkinv")) {
            Player player2 = World.getPlayerByName(wholeCommand.substring(9));
            if (player2 == null) {
                player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
                return;
            }
            player.getInventory().setItems(player2.getInventory().getCopiedItems()).refreshItems();
        }
        if (command[0].equalsIgnoreCase("demote")) {
            String name = wholeCommand.substring(7);

            Player target = World.getPlayerByName(name);
            if (target == null) {
                player.getPacketSender().sendMessage("Player is not online");
            } else {
                target.setRights(PlayerRights.PLAYER);
                target.getPacketSender().sendRights();
                target.getPacketSender().sendMessage("Your player rights have been changed.");
                player.getPacketSender().sendMessage("Gave " + target + "player.");
            }
        }
        if (command[0].equals("sendstring")) {
            int child = Integer.parseInt(command[1]);
            String string = command[2];
            player.getPacketSender().sendString(child, string);
        }
        if (command[0].equalsIgnoreCase("kbd")) {
            SLASHBASH.startPreview(player);

        }
        if (command[0].equalsIgnoreCase("spec")) {
            player.setSpecialPercentage(1000);
            CombatSpecial.updateBar(player);
        }
        if (command[0].equals("givedpoints")) {
            int amount = Integer.parseInt(command[1]);
            String rss = command[2];
            if (command.length > 3) {
                rss += " " + command[3];
            }
            if (command.length > 4) {
                rss += " " + command[4];
            }
            Player target = World.getPlayerByName(rss);
            if (target == null) {
                player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
            } else {
                target.getPointsHandler().incrementDonationPoints(amount);
                target.getPointsHandler().refreshPanel();

                // player.refreshPanel(target);
            }
        }

        if (command[0].equals("event")) {
            Wildywyrm.spawn();
        }
        if (command[0].equals("ban")) {

            String playerToBan = wholeCommand.substring(4);
            if (!PlayerSaving.playerExists(playerToBan)) {
                player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
                return;
            } else {
                if (PlayerPunishment.banned(playerToBan)) {
                    player.getPacketSender()
                            .sendConsoleMessage("Player " + playerToBan + " already has an active ban.");
                    return;
                }
                PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just banned " + playerToBan + "!");
                PlayerPunishment.ban(playerToBan);
                player.getPacketSender().sendConsoleMessage(
                        "Player " + playerToBan + " was successfully banned. Command logs written.");
                Player toBan = World.getPlayerByName(playerToBan);
                World.deregister(player);
                if (toBan != null) {
                    World.deregister(toBan);
                }
            }
            return;
        }


        if (command[0].equals("bank")) {
            try {
                player.getBank(player.getCurrentBankTab()).open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (command[0].equalsIgnoreCase("getmsg")) {
            new InterfaceInputPacketListener();
        }

        if (command[0].equals("hp")) {
            player.setConstitution(150000);
        }
        if (command[0].equals("dzoneon")) {
            if (GameSettings.DZONEON = false) {
                GameSettings.DZONEON = true;
                World.sendMessageNonDiscord(
                        "@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
            }
            GameSettings.DZONEON = false;
            World.sendMessageNonDiscord(
                    "@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
        }

        if (command[0].equals("tasks")) {
            player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
        }

        if (command[0].equals("" + "cpubans")) {
            ConnectionHandler.reloadUUIDBans();
            player.getPacketSender().sendConsoleMessage("UUID bans reloaded!");
        }

        if (command[0].equals("reloadnpcs")) {
            NpcDefinition.parseNpcs().load();
            DiscordMessenger.sendStaffMessage("@red@NPC Definitions Reloaded.");
        }

        if (command[0].equals("reloadcombat")) {
        CombatStrategies.init();
        //World.sendFilteredMessage("@red@Combat Strategies have been reloaded");
        DiscordMessenger.sendStaffMessage("@red@Combat Strategies have been reloaded.");
        }


        if (command[0].equalsIgnoreCase("npcreset")) {
        NPC.init();
        }

        if (command[0].equals("reloadipbans")) {
            PlayerPunishment.reloadIPBans();
            player.getPacketSender().sendConsoleMessage("IP bans reloaded!");
        }
        if (command[0].equals("reloadipmutes")) {
            PlayerPunishment.reloadIPMutes();
            player.getPacketSender().sendConsoleMessage("IP mutes reloaded!");
        }
        if (command[0].equals("reloadbans")) {
            PlayerPunishment.reloadBans();
            player.getPacketSender().sendConsoleMessage("Banned accounts reloaded!");
        }
        if (command[0].equals("reloadguide")) {
            GuideBook.loadGuideDataFile();
            player.getPacketSender().sendMessage("Guide refreshed");
        }

        if (command[0].equalsIgnoreCase("ipban")) {
            String ip = wholeCommand.substring(7);
            PlayerPunishment.addBannedIP(ip);
            player.getPacketSender().sendConsoleMessage("" + ip + " IP was successfully banned. Command logs written.");
        }

        if (command[0].equals("memory")) {
            // ManagementFactory.getMemoryMXBean().gc();
            /*
             * MemoryUsage heapMemoryUsage =
             * ManagementFactory.getMemoryMXBean().getHeapMemoryUsage(); long mb =
             * (heapMemoryUsage.getUsed() / 1000);
             */
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            player.getPacketSender()
                    .sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
        }
        if (command[0].equals("star")) {
            ShootingStar.despawn(true);
            player.getPacketSender().sendConsoleMessage("star method called.");
        }
        if (command[0].equals("stree")) {
            EvilTrees.despawn(true);
            player.getPacketSender().sendConsoleMessage("tree method called.");
        }
        if (command[0].equals("save")) {
            player.save();
        }
        if (command[0].equals("saveall")) {
            World.savePlayers();
        }
//        if (command[0].equals("reloadshops")) {
//            ShopManager.parseShops().load();//
//            player.getInventory().refreshItems();
//            World.sendMessageNonDiscord("@red@Shops have been reloaded");
//        }

        if (command[0].equalsIgnoreCase("frame")) {
            int frame = Integer.parseInt(command[1]);
            String text = command[2];
            player.getPacketSender().sendString(frame, text);
        }
        if (command[0].equals("position")) {
            player.getPacketSender().sendMessage(player.getPosition().toString());
        }
        if (command[0].equals("npc")) {
            int id = Integer.parseInt(command[1]);
            NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
                    player.getPosition().getZ()));
            World.register(npc);
            // npc.setConstitution(20000);
            player.getPacketSender().sendEntityHint(npc);
            /*
             * TaskManager.submit(new Task(5) {
             *
             * @Override protected void execute() { npc.moveTo(new
             * Position(npc.getPosition().getX() + 2, npc.getPosition().getY() + 2));
             * player.getPacketSender().sendEntityHintRemoval(false); stop(); }
             *
             * });
             */
            // npc.getMovementCoordinator().setCoordinator(new
            // Coordinator().setCoordinate(true).setRadius(5));
        }
        if (command[0].equals("skull")) {
            if (player.getSkullTimer() > 0) {
                player.setSkullTimer(0);
                player.setSkullIcon(0);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            } else {
                CombatFactory.skullPlayer(player);
            }
        }
        if (command[0].equals("fillinv")) {
            for (int i = 0; i < 28; i++) {
                int it = RandomUtility.getRandom(10000);
                player.getInventory().add(it, 1);
            }
        }
        if (command[0].equals("pnpc")) {
            player.setNpcTransformationId(Integer.parseInt(command[1]));
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        } else if (command[0].equals("playobject")) {
            player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
                    new Animation(751));
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        if (command[0].equals("interface")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendInterface(id);
        }

        if (command[0].equals("swi")) {
            int id = Integer.parseInt(command[1]);
            boolean vis = Boolean.parseBoolean(command[2]);
            player.sendParallellInterfaceVisibility(id, vis);
            player.getPacketSender().sendMessage("Done.");
        }
        if (command[0].equals("walkableinterface")) {
            int id = Integer.parseInt(command[1]);
            player.sendParallellInterfaceVisibility(id, true);
        }
        if (command[0].equals("anim")) {
            int id = Integer.parseInt(command[1]);
            player.performAnimation(new Animation(id));
            player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
        }
        if (command[0].equals("gfx")) {
            int id = Integer.parseInt(command[1]);
            player.performGraphic(new Graphic(id));
            player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
        }
        if (command[0].equals("object")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
            player.getPacketSender().sendConsoleMessage("Sending object: " + id);
        }
        if (command[0].equals("config")) {
            int id = Integer.parseInt(command[1]);
            int state = Integer.parseInt(command[2]);
            player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
        }


        if (command[0].equals("doublebosspoints")) {
            if (GameSettings.DOUBLE_BOSSPOINTS) {
                GameSettings.DOUBLE_BOSSPOINTS = false;
                World.sendMessageNonDiscord("@blu@Double Boss points has been disabled!");
            } else {
                GameSettings.DOUBLE_BOSSPOINTS = true;
                World.sendMessageNonDiscord("@blu@Double Boss points has been enabled!");
            }
        }
    }

    private static void handlePunishmentCommands(Player player, String command[], String wholeCommand,
                                                 boolean modCommand) {

        switch (command[0]) {

            case "jail": {
                Player target = World.getPlayerByName(wholeCommand.substring(5));
                if (target != null) {
                    if (Jail.isJailed(target)) {
                        player.getPacketSender().sendConsoleMessage("That player is already jailed!");
                        return;
                    }

                    if (target.getRights().isAdmin() && !player.getRights().hasBanRights()) {
                        player.sendMessage("You cannot jail administrators.");
                        return;
                    }

                    if (Jail.jailPlayer(target)) {
                        target.getSkillManager().stopSkilling();
                        PlayerLogs.log(player.getUsername(),
                                "" + player.getUsername() + " just jailed " + target.getUsername() + "!");
                        player.getPacketSender().sendMessage("Jailed player: " + target.getUsername() + "");
                        target.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
                    } else {
                        player.getPacketSender().sendConsoleMessage("Jail is currently full.");
                    }
                } else {
                    player.getPacketSender().sendConsoleMessage("Could not find that player online.");
                }
                return;
            }

            case "unjail": {
                Player target = World.getPlayerByName(wholeCommand.substring(7));
                if (target != null) {
                    Jail.unjail(target);
                    PlayerLogs.log(player.getUsername(),
                            "" + player.getUsername() + " just unjailed " + target.getUsername() + "!");
                    player.getPacketSender().sendMessage("Unjailed player: " + target.getUsername() + "");
                    target.getPacketSender().sendMessage("You have been unjailed by " + player.getUsername() + ".");
                } else {
                    player.getPacketSender().sendConsoleMessage("Could not find that player online.");
                }
                return;
            }

            case "mute": {
                String name = Misc.formatText(wholeCommand.substring(5));
                if (!PlayerSaving.playerExists(name)) {
                    player.getPacketSender().sendConsoleMessage("Player " + name + " does not exist.");
                    return;
                } else {
                    if (PlayerPunishment.muted(name)) {
                        player.getPacketSender().sendConsoleMessage("Player " + name + " already has an active mute.");
                        return;
                    }

                    Player plr = World.getPlayerByName(name);

                    if (plr != null) {
                        if (plr.getRights().isAdmin() && !player.getRights().hasBanRights()) {
                            player.sendMessage("You cannot mute administrators.");
                            return;
                        }
                        plr.getPacketSender().sendMessage("You have been muted by " + player.getUsername() + ".");
                    }

                    PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just muted " + name + "!");
                    PlayerPunishment.mute(name);
                    player.getPacketSender()
                            .sendConsoleMessage("Player " + name + " was successfully muted. Command logs written.");

                }
                return;
            }

            case "unmute": {
                String name = wholeCommand.substring(7);
                if (!PlayerSaving.playerExists(name)) {
                    player.getPacketSender().sendConsoleMessage("Player " + name + " does not exist.");
                    return;
                } else {
                    if (!PlayerPunishment.muted(name)) {
                        player.getPacketSender().sendConsoleMessage("Player " + name + " is not muted!");
                        return;
                    }
                    PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unmuted " + name + "!");
                    PlayerPunishment.unmute(name);
                    player.getPacketSender()
                            .sendConsoleMessage("Player " + name + " was successfully unmuted. Command logs written.");
                    Player plr = World.getPlayerByName(name);
                    if (plr != null) {
                        plr.getPacketSender().sendMessage("You have been unmuted by " + player.getUsername() + ".");
                    }
                }
                return;
            }


            case "kick": {

                Player target = World.getPlayerByName(wholeCommand.substring(5));

                try {
                    if (target.getLocation() != Location.WILDERNESS) {
                        World.deregister(target);
                        PlayerHandler.handleLogout(target, false);
                        player.getPacketSender().sendConsoleMessage("Kicked " + target.getUsername()
                                + ".");
                        PlayerLogs.log(player.getUsername(), "" + player.getUsername() +
                                " just kicked " + target.getUsername() + "!");
                        return;
                    }
                    player.sendMessage("Player is in the wilderness so they cannot be kicked!");

                } catch (Exception e) {
                    player.getPacketSender()
                            .sendConsoleMessage("Player " + target.getUsername() +
                                    " couldn't be found on Zamron.");
                }
                return;
            }


        }

        if (!modCommand)
            return;

        switch (command[0]) {


            case "unban": {
                String playerToBan = wholeCommand.substring(6);
                if (!PlayerSaving.playerExists(playerToBan)) {
                    player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " does not exist.");
                    return;
                } else {
                    if (!PlayerPunishment.banned(playerToBan)) {
                        player.getPacketSender().sendConsoleMessage("Player " + playerToBan + " is not banned!");
                        return;
                    }
                    PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unbanned " + playerToBan + "!");
                    PlayerPunishment.unban(playerToBan);
                    player.getPacketSender().sendConsoleMessage(
                            "Player " + playerToBan + " was successfully unbanned. Command logs written.");
                }
                return;
            }

        }
    }

}