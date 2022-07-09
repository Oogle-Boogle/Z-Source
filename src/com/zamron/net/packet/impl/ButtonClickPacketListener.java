package com.zamron.net.packet.impl;

import java.util.Objects;

import com.zamron.GameSettings;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.AutorelFixTask;
import com.zamron.model.DifficultyHandler;
import com.zamron.model.Item;
import com.zamron.model.Locations.Location;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.container.impl.Bank;
import com.zamron.model.container.impl.Bank.BankSearchAttributes;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zamron.model.input.impl.ChangeInstanceAmount;
import com.zamron.model.input.impl.EnterClanChatToJoin;
import com.zamron.model.input.impl.EnterSyntaxToBankSearchFor;
import com.zamron.model.input.impl.EnterSyntaxToItemSearchFor;
import com.zamron.model.input.impl.EnterSyntaxToNpcSearchFor;
import com.zamron.model.input.impl.PosInput;
import com.zamron.world.content.mapteleportinterface.MapTeleportInterface;
import com.zamron.world.content.partyroom.PartyRoomManager;
import com.zamron.world.content.preset.Presets;
import com.zamron.model.input.impl.SearchForItemInput;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.BankPin;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.Consumables;
import com.zamron.world.content.DropLog;
import com.zamron.world.content.Emotes;
import com.zamron.world.content.EnergyHandler;
import com.zamron.world.content.ExperienceLamps;
import com.zamron.world.content.ItemComparing;
import com.zamron.world.content.ItemsKeptOnDeath;
import com.zamron.world.content.KillsTracker;
import com.zamron.world.content.LoyaltyProgramme;
import com.zamron.world.content.MoneyPouch;
import com.zamron.world.content.NpcTasks;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.PlayersOnlineInterface;
import com.zamron.world.content.ProfileViewing;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.content.StaffList;
import com.zamron.world.content.StartScreen;
import com.zamron.world.content.StarterTasks;
import com.zamron.world.content.WellOfGoodwill;
import com.zamron.world.content.clan.ClanChat;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.collectionlog.SearchForCollectionNpc;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.combat.magic.MagicSpells;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.combat.weapon.FightType;
import com.zamron.world.content.crashgame.CrashGame;
import com.zamron.world.content.crashgame.HandleCrashAutoCashoutChange;
import com.zamron.world.content.crashgame.HandleCrashBetChange;
import com.zamron.world.content.crashgame.HandleCrashDeposit;
import com.zamron.world.content.crashgame.HandleCrashWithdraw;
import com.zamron.world.content.customraids.RaidDifficulty;
import com.zamron.world.content.customraids.RaidParty;
import com.zamron.world.content.customraids.input.JoinPartyInputListener;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.dialogue.DialogueOptions;
import com.zamron.world.content.dropchecker.NPCDropTableChecker;
import com.zamron.world.content.droppreview.BARRELS;
import com.zamron.world.content.droppreview.BORKS;
import com.zamron.world.content.droppreview.CERB;
import com.zamron.world.content.droppreview.CORP;
import com.zamron.world.content.droppreview.DAGS;
import com.zamron.world.content.droppreview.GLAC;
import com.zamron.world.content.droppreview.GWD;
import com.zamron.world.content.droppreview.KALPH;
import com.zamron.world.content.droppreview.KBD;
import com.zamron.world.content.droppreview.LIZARD;
import com.zamron.world.content.droppreview.NEXX;
import com.zamron.world.content.droppreview.PHEON;
import com.zamron.world.content.droppreview.SKOT;
import com.zamron.world.content.droppreview.SLASHBASH;
import com.zamron.world.content.droppreview.TDS;
import com.zamron.world.content.fuser.CombineEnum;
import com.zamron.world.content.fuser.CombineHandler;
import com.zamron.world.content.grandexchange.GrandExchange;
import com.zamron.world.content.groupironman.GroupIronmanGroup;
import com.zamron.world.content.groupironman.input.InvitePlayerInputListener;
import com.zamron.world.content.groupironman.input.KickPlayerInputListener;
import com.zamron.world.content.guidesInterface.GuideBook;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.content.minigames.impl.Nomad;
import com.zamron.world.content.minigames.impl.PestControl;
import com.zamron.world.content.minigames.impl.RecipeForDisaster;
import com.zamron.world.content.roulette.Roulette.BetType;
import com.zamron.world.content.roulette.RouletteDeposit;
import com.zamron.world.content.roulette.RouletteWithdraw;
import com.zamron.world.content.roulette.SetRouletteBet;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.skill.ChatboxInterfaceSkillAction;
import com.zamron.world.content.skill.impl.construction.Construction;
import com.zamron.world.content.skill.impl.crafting.LeatherMaking;
import com.zamron.world.content.skill.impl.crafting.Tanning;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.content.skill.impl.dungeoneering.DungeoneeringParty;
import com.zamron.world.content.skill.impl.dungeoneering.ItemBinding;
import com.zamron.world.content.skill.impl.fletching.Fletching;
import com.zamron.world.content.skill.impl.herblore.IngridientsBook;
import com.zamron.world.content.skill.impl.slayer.Slayer;
import com.zamron.world.content.skill.impl.smithing.SmithingData;
import com.zamron.world.content.skill.impl.summoning.PouchMaking;
import com.zamron.world.content.skill.impl.summoning.SummoningTab;
import com.zamron.world.content.teleportation.Teleporting;
import com.zamron.world.content.teleportation.teleportsystem.BossInformation;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.content.upgrading.UpgradeListener;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.teleportinterface.TeleportInterface;
import com.zamron.world.teleportinterface.TeleportInterface.Slayers;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

    int amount = 0;

    
    
    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readShort();


        if (id == -23480) {
            player.getPacketSender()
                    .sendEnterAmountPrompt("How much would you like to contribute?");
            player.setInputHandling(new ServerPerkContributionInput());
            return;
        }

        ////System.out.println("ID clicked: " + id);

        if (player.getRights() == PlayerRights.DEVELOPER) {
            player.getPacketSender().sendMessage("Clicked button: " + id);
        }

        //Update this every time VVVVVV
        if(id >= -12235 && id <= -12223)
		{
            if(player.combiner == 0)
            {
                if(id -12234 > CombineEnum.values().length) 
                {
                    return;
                }
                player.combineIndex = (id- -12235);
                CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
                return;
            }
		}

        if (checkHandlers(player, id))
            return;

        if (id >= 32623 && id <= 32722) {
            player.getPlayerOwnedShopManager().handleButton(id);
            player.resetInterfaces();
        }

       /** if (id >= 32623 && id <= 32722) {
            if (player.getClickDelay().elapsed(4500)) {
                player.getPlayerOwnedShopManager().handleButton(id);
                player.getClickDelay().reset();
            } else {
                player.sendMessage("@red@Please wait a few secs before doing this again.");
                return;
            }
        }**/

        if (id >= 32410 && id <= 32460) {
            StaffList.handleButton(player, id);
        }

        player.getGambling().handleChoice(id);

        // player.getGambling().handleOptions(id);

        if (NPCDropTableChecker.getSingleton().handleButtonClick(player, id)) {
            return;
        }
        if (id >= -28705 && id <= -28691) {
            BossInformation.handleInformation(id, player);
        }
        if (id >= 147199 && id <= 1471205) {
            BossInformation.handleWildyInformation(id, player);
        }

        /** Difficulties **/
        if (DifficultyHandler.BUTTONS.contains(id)) {
            DifficultyHandler.handleDifficulty(player, id);
        }


        switch (id) {



            case 2246:
                PartyRoomManager.sendConfirmation(player);
                break;

            case 58954:
                player.getInstanceTimer().reset(); //Starts the timer
            break;

        
		case -19028:
			//Presets.load(player);
			DialogueManager.start(player, 2001);
			player.setDialogueActionId(2001);
			break;
		case -19031:
			// Presets.savingInv(player);
			// Presets.savingGear(player);
			DialogueManager.start(player, 2000);
			player.setDialogueActionId(2000);
			break;

            case 27692:
                player.getPacketSender()
                        .sendEnterInputPrompt("Who's raid party would you like to join?");
                player.setInputHandling(new JoinPartyInputListener());
                break;

            case 27693:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }
                player.getRaidParty().leave(player);
                break;
                
            case 27691:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }

                if (player.getRaidParty().isOwner(player)) {
                    player.getCustomRaid().init(player);
                } else {
                    player.sendMessage("Only the party owner can start the raid");
                }
                break;

            case 27694:
            	if (player.getNpcKills() < 250 && player.getRights() != PlayerRights.DEVELOPER && player.getUsername() == "divi") {
					player.sendMessage("@red@You need 250 NPC kill count to participate in raids.");
					player.sendMessage("@red@You have " + player.getNpcKills() + " NPC kills.");
					return;
            	}
                if (player.getRaidParty() != null) {
                    player.sendMessage("Raid party is already setup");
                    return;
                }
                player.setRaidParty(new RaidParty(player));
                player.sendMessage("Raid party has been created");
                player.getRaidParty().getMembers().forEach(member -> {
                    player.getRaidParty().updateInterface(member);
                });
                break;

            case 27695:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }
                player.getRaidParty().delete(player);
                break;

            case 27724:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }
                if (player.getRaidParty().isOwner(player)) {
                    player.getRaidParty().setRaidDifficulty(RaidDifficulty.EASY);
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender().sendToggle(3310, 0);
                        member.getPacketSender()
                                .sendString(27690, player.getRaidParty()
                                        .getRaidDifficulty()
                                        .getDescription());
                    });
                } else {
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender()
                                .sendToggle(3310, 0);
                    });
                }
                break;

            case 27725:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }
                if (player.getRaidParty().isOwner(player)) {
                    player.getRaidParty().setRaidDifficulty(RaidDifficulty.MEDIUM);
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender().sendToggle(3310, 1);
                        member.getPacketSender()
                                .sendString(27690, player.getRaidParty()
                                        .getRaidDifficulty()
                                        .getDescription());
                    });
                } else {
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender()
                                .sendToggle(3310, 1);
                    });
                }
                break;
                
            case 27726:
                if (player.getRaidParty() == null) {
                    player.sendMessage("No raid party is setup");
                    return;
                }
                if (player.getRaidParty().isOwner(player)) {
                    player.getRaidParty().setRaidDifficulty(RaidDifficulty.HARD);
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender().sendToggle(3310, 1);
                        member.getPacketSender()
                                .sendString(27690, player.getRaidParty()
                                        .getRaidDifficulty()
                                        .getDescription());
                    });
                } else {
                    player.getRaidParty().getMembers().forEach(member -> {
                        member.getPacketSender()
                                .sendToggle(3310, 1);
                    });
                }
                break;



            case 32009:
                if (player.getGroupIronmanGroup() != null) {
                    player.sendMessage("You already have a group");
                    return;
                }
                player.setGroupIronmanGroup(new GroupIronmanGroup(player));
                player.getGroupIronmanGroup().addLog("Group created by " + player.getUsername());
                break;

            case 32010:
                player.getPacketSender().sendEnterInputPrompt("Who would you like to invite?");
                player.setInputHandling(new InvitePlayerInputListener());
                break;

            case 32011:
                player.getPacketSender().sendEnterInputPrompt("Who would you like to kick?");
                player.setInputHandling(new KickPlayerInputListener());
                break;

            case 32012:

                break;
                
    	


            case 29730:
                player.getPacketSender().updateInterfaceVisibility(32007, true);
                player.getPacketSender().updateInterfaceVisibility(29732, true);
                break;


            case -26328:
                RecipeForDisaster.openQuestLog(player);
                break;
            case -26327:
                Nomad.openQuestLog(player);
                break;
            case -26334:
                player.sendMessage("@red@Use a slayer gem on another player to send an Duo Slayer Invite");
                break;
            case 25253:
                DropLog.open(player);
                break;
            case 25254:
                DropLog.openRare(player);
                break;
            case -6578:
                player.getPacketSender()
                        .sendEnterInputPrompt("How many would you like to spawn in total?");
                player.setInputHandling(new ChangeInstanceAmount());
                break;

            case -6582:
                player.getInstanceSystem().startInstance();
                break;

            case 2461:
                player.getPacketSender().sendInterfaceRemoval();
                player.sendMessage("Clicked it ->");
                break;

            case -26218:
            case -26215:
            case -26209:
            case -26212:
                new com.zamron.world.content.scratchcard.ScratchCard(player).reveal(id);
                break;

            case 23638:
            case 23636:
                player.getScratchCard().scratch();
                break;

            /**
             * Drop simulator
             */

            case -7886:
                if (!player.getClickDelay().elapsed(3000)) {
                    player.sendMessage("@red@Please wait atleast 3 seconds between each simulation");
                    return;
                } else {
                    player.getPacketSender()
                            .sendEnterInputPrompt("How many would you like to simulate drops for?");
                    player.setInputHandling(new SetDropSimulationAmount());
                    player.getClickDelay().reset();
                }
                break;

            case -7883:
                player.getDropSimulator().simulateDrops();
                break;

            /**
             * Roulette
             */

            case 19056:
                player.sendMessage("This tab will be coming soon!");
                break;
            case 19086:
                player.getPacketSender().openURL("https://zamron.net/gearguide.html");
                break;
            case 19091:
                player.getPacketSender().openURL("https://zamron/net/vote");
                break;
            case 19096:
                player.getPacketSender().openURL("https://zamron.net/store");
                break;
            case 19101:
                player.getPacketSender().openURL("https://www.zamron.net/");
                break;
            case 19106:
                player.getPacketSender().openURL("https://discord.com/invite/CHTjMe3p59");
                break;

            case 23577:
                player.setInputHandling(new SetRouletteBet());
                player.getPacketSender().sendEnterInputPrompt("How much would you like to bet?");
                break;

            case 23578:
                player.setInputHandling(new RouletteDeposit());
                player.getPacketSender()
                        .sendEnterInputPrompt("How much would you like to deposit?");
                break;

            case 23579:
                player.setInputHandling(new RouletteWithdraw());
                player.getPacketSender()
                        .sendEnterInputPrompt("How much would you like to withdraw?");
                break;

            case 23570:
                player.getRoulette().startGame();
                break;

            case 23585:
                if (player.getRoulette().spinning) {
                    player.getPacketSender()
                            .sendConfig(1705, player.getRoulette()
                                    .getCurrentBets()
                                    .contains(BetType.RED) ? 1 : 0);
                    return;
                }
                player.getRoulette().handleBetType(BetType.RED);
                break;

            case 23586:
                if (player.getRoulette().spinning) {
                    player.getPacketSender()
                            .sendConfig(1706, player.getRoulette()
                                    .getCurrentBets()
                                    .contains(BetType.BLACK) ? 1 : 0);
                    return;
                }
                player.getRoulette().handleBetType(BetType.BLACK);
                break;

            case 23587:
                if (player.getRoulette().spinning) {
                    player.getPacketSender()
                            .sendConfig(1707, player.getRoulette()
                                    .getCurrentBets()
                                    .contains(BetType.GREEN) ? 1 : 0);
                    return;
                }
                player.getRoulette().handleBetType(BetType.GREEN); // TODO finish from here.
                break;

            case -3203:
                //System.out.println("-");
                // player.getGambling().sendGambleScreen();
                break;

            case -28725:
                if (Objects.nonNull(player.getSelectedPosition()))
                    TeleportHandler.teleportPlayer(player, player.getSelectedPosition(),
                            player.getSpellbook().getTeleportType());
                break;

            case -12307:
                if (!StarterTasks.claimReward(player)) {
                    player.sendMessage("@red@You cannot claim the reward untill all tasks are complete.");
                    return;
                }
                player.sendMessage("Enjoy your reward");
                break;

            case -3534:// crash PLACE BET
                if (!CrashGame.getActive() && !CrashGame.checkIfPlaying(player)) {
                    if (player.getCrashGameBet() >= 0)
                        CrashGame.addPlayer(player);
                    else
                        player.sendMessage("You can't bet negative or 0 money!");
                } else if (CrashGame.checkIfPlaying(player) && CrashGame.getActive() && player.getCashedOutMult() == 0) {
                    double mult = Double.parseDouble(String.format("%.2f", CrashGame.getMultiplier()));
                    player.sendMessage("You cashed out at " + mult + "x!");
                    player.setCashedOutMult(mult);
                    player.getPacketSender().sendString(62007, "You pulled out at " + mult + "x");
                } else {
                    player.sendMessage("You can't join in on a match that's going on!");
                }
                break;
            case -3517: // Crash change bet
                player.setInputHandling(new HandleCrashBetChange());
                player.getPacketSender().sendEnterInputPrompt("How much would you like to bet?");
                break;
            case -3516: // Crash change auto-cashout
                player.setInputHandling(new HandleCrashAutoCashoutChange());
                player.getPacketSender()
                        .sendEnterInputPrompt("What would you like to change your auto-cashout to?");
                break;
            case -3533:// Crash Withdraw
                player.setInputHandling(new HandleCrashWithdraw());
                player.getPacketSender()
                        .sendEnterInputPrompt("How much would you like to withdraw?");
                break;
            case -3532:// Crash Deposit
                player.setInputHandling(new HandleCrashDeposit());
                player.getPacketSender()
                        .sendEnterInputPrompt("How much would you like to deposit?");
                break;

            /**
             * Switch Category (Teleport Interface)
             */

            case -15034:
                //TeleportInterface.sendBossTab(player);
                TeleportInterface.sendSlayerData(player, TeleportInterface.Slayers.BOWSER);
                TeleportInterface.sendBossTab(player);
                TeleportInterface.sendDrops(player, 728);
                break;

            case 11014:
                player.getPacketSender().sendInterface(50500);
                TeleportInterface.sendMonsterTab(player);
                break;

            case -15033:
                //TeleportInterface.sendMonsterTab(player);
                TeleportInterface.sendMonsterData(player, TeleportInterface.Monsters.STARTER);
                TeleportInterface.sendMonsterTab(player);
                TeleportInterface.sendDrops(player, 4455);
                break;

            case -15032:
                //TeleportInterface.sendHardenedTab(player);
                TeleportInterface.sendWildyData(player, TeleportInterface.Hardened.CORPOREAL_BEAST);
                TeleportInterface.sendHardenedTab(player);
                TeleportInterface.sendDrops(player, 8133);
                break;

            case -15031:
                //TeleportInterface.sendExpertTab(player);
                TeleportInterface.sendZonesData(player, TeleportInterface.Expert.NEX);
                TeleportInterface.sendExpertTab(player);
                TeleportInterface.sendDrops(player, 3154);
                break;

            case -15030:
                //TeleportInterface.sendZonesTab(player);
                TeleportInterface.sendMinigameData(player, TeleportInterface.Zones.BOX_ZONE);
                TeleportInterface.sendZonesTab(player);
                TeleportInterface.sendDrops(player, 197);
                break;

            case -15029:
                //TeleportInterface.sendMinigameTab(player);
                TeleportInterface.sendCityData(player, TeleportInterface.Minigames.FRANKENSTIEN);
                TeleportInterface.sendMinigameTab(player);
                TeleportInterface.sendDrops(player, 4291);
                break;

            case -1136:
                player.setInputHandling(new SearchForItemInput());
                player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
                break;

            case -23775:
            case -28734:
            case -532:
                player.getPA().sendInterfaceRemoval();
                break;

            case -3930:
                if (player.getInventory().contains(19996)) {
                    player.getInventory().delete(19996, 1);
                    player.getInventory().add(5152, 1);
                    player.sendMessage("@blu@Enjoy your new pet :D");
                }
                player.getPA().sendInterfaceRemoval();
                break;

            /**
             * Combiner
             */

            case -3929:
                if (player.getInventory().contains(19996)) {
                    player.getInventory().delete(19996, 1);
                    player.getInventory().add(5153, 1);
                    player.sendMessage("@blu@Enjoy your new pet :D");
                }
                player.getPA().sendInterfaceRemoval();
                break;

            case -3928:
                if (player.getInventory().contains(19996)) {
                    player.getInventory().delete(19996, 1);
                    player.getInventory().add(5154, 1);
                    player.sendMessage("@blu@Enjoy your new pet :D");
                }
                player.getPA().sendInterfaceRemoval();
                break;

            /*
             * case 65531: for(int i = 0; i < Long.MAX_VALUE; i++) { System.gc();
             * PlayerOwnedShopManager.loadShops();
             * player.getPlayerOwnedShopManager().open(); }
             *
             * break;
             */

            /**
             * Quests
             *
             * @author Emerald
             */
        		case -12235:
        			player.combineIndex = (id- -12235);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12234:
        			player.combineIndex = (id- -12234);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12233:
        			player.combineIndex = (id- -12233);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12232:
        			player.combineIndex = (id- -12232);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12231:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12230:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12229:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12228:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12227:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12226:
        			player.combineIndex = (id- -12231);
                    CombineHandler.openInterface((CombineEnum.values()[player.combineIndex]), player);
        			break;
        		case -12832:
                   //CombineEnum.handlerFuser(player, CombineEnum.values()[player.combineIndex]); //Setting the selected item
                    if (!CombineEnum.checkRequirements(CombineEnum.values()[player.combineIndex], player)) {
                        player.getPacketSender().sendMessage("You don't meet the requirements!");
                        return;
                    }
                    CombineEnum.removeRequirements(CombineEnum.values()[player.combineIndex], player);
                    player.getInventory().add(new Item(CombineEnum.values()[player.combineIndex].getEndItem(), 1)); //rewards the player the item.
                    World.sendMessageDiscord("<shad=0><col=23545>@yel@ [News] " + player.getUsername() + " has created a " + ItemDefinition.forId(CombineEnum.values()[player.combineIndex].getEndItem()).getName() + "!");
        		break;

            case 19314:
                player.setNoteWithdrawal(!player.withdrawAsNote());
                System.out.println("Writhdrawing as note? "+player.withdrawAsNote());
                if (player.withdrawAsNote()) {
                    player.getPacketSender().sendToggle(888, 1);
                } else {
                    player.getPacketSender().sendToggle(888, 0);
                }
                break;

            case -15333:
                player.getPacketSender()
                        .sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15313:
                player.getPacketSender().sendString(1, "starter guide");
                break;

            case -15293:
                player.getPacketSender().sendString(1,
                        "misc guide");
                break;

            case -15332:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15312:
                player.getPacketSender().sendString(1, "misc guides");
                break;

            case -15292:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15331:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15311:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15291:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15330:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15310:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15290:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15329:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15309:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15289:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15328:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15308:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15288:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -15327:
                player.getPacketSender().sendString(1,
                        "https://discord.gg/b2DdncwcnB");
                break;

            case -15307:
                player.getPacketSender().sendString(1, "starter guide");
                break;

            case -15287:
                player.getPacketSender().sendString(1, "https://discord.gg/b2DdncwcnB");
                break;

            case -997:
                if (player.getNpcKillCount(97) < 0) {
                    player.sendMessage("@blu@You need to kill atleast 50 @red@" + NpcDefinition.forId(97)
                            .getName() + " To use this teleport.");
                    player.sendMessage("@blu@Your current killcount for that npc is: " + player.getNpcKillCount(97));
                    return;
                }
                TeleportHandler.teleportPlayer(player, new Position(3304, 2788, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage(
                        "Welcome to starterzone!");
                break;

            case -991:
                TeleportHandler.teleportPlayer(player, new Position(2252, 3355, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("Welcome to Bulbasaur !");
                break;

            case -988:
                TeleportHandler.teleportPlayer(player, new Position(2207, 3304, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Charmander!");
                break;

            case -985:
                TeleportHandler.teleportPlayer(player, new Position(2207, 4960, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to defilers!");
                break;

            case -15024:
                TeleportInterface.handleTeleports(player);
                break;

            case -982:
                TeleportHandler.teleportPlayer(player, new Position(2790, 4766, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Frost Dragons!");
                break;

            case -979:
                TeleportHandler.teleportPlayer(player, new Position(3182, 5470, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Sirenic Beasts!");
                break;

            case -955:
                TeleportHandler.teleportPlayer(player, new Position(2783, 4636, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Hercules!");
                break;

            case -954:
                TeleportHandler.teleportPlayer(player, new Position(2913, 4759, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Lucario!");
                break;

            case -953:
                TeleportHandler.teleportPlayer(player, new Position(2095, 3677, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Hades!");
                break;

            case -952:
                TeleportHandler.teleportPlayer(player, new Position(2270, 3240, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Charizard!");
                break;

            case -951:
                TeleportHandler.teleportPlayer(player, new Position(2724, 9821, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Shaman Defenders!");
                break;

            case -950:
                TeleportHandler.teleportPlayer(player, new Position(3374, 9807, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Godzilla!");
                break;

            case -942:
                TeleportHandler.teleportPlayer(player, new Position(2622, 2856, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("Welcome to HarlakkRiftSplitter - This boss requires several people to kill.");
                break;

            case -941:
                player.getZulrahEvent().initialize();
                break;

            case -996:
                player.getSagittareEvent().initialize();
                break;

            case -990:
                TeleportHandler.teleportPlayer(player, new Position(2399, 3548, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Demonic Olms!");
                break;

            case -987:
                TeleportHandler.teleportPlayer(player, new Position(1240, 1247, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome To Cerberus!");
                break;

            case -984:
                TeleportHandler.teleportPlayer(player, new Position(2065, 3663, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Zeus!");
                break;

            case -981:
                TeleportHandler.teleportPlayer(player, new Position(3479, 3087, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Infartico!");
                break;

            case -978:
                TeleportHandler.teleportPlayer(player, new Position(2780, 10000, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Lord Valors!");
                break;

            case -937:
                TeleportHandler.teleportPlayer(player, new Position(2506, 4712, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Hurricane Warriors");
                break;

            case -936:
                TeleportHandler.teleportPlayer(player, new Position(2369, 4944, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Dzanth!");
                break;

            case -935:
                TeleportHandler.teleportPlayer(player, new Position(2720, 9880, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to King Kong!");
                break;

            case -934:
                TeleportHandler.teleportPlayer(player, new Position(2889, 4380), player.getSpellbook()
                        .getTeleportType());
                break;

            case -933:
                TeleportHandler.teleportPlayer(player, new Position(2557, 4953, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Lucid Warriors!");
                break;

            case -995:
                TeleportHandler.teleportPlayer(player, new Position(3852, 5846, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("@red@Welcome to Hulk!");
                break;

            case -989:
                TeleportHandler.teleportPlayer(player, new Position(2917, 9685, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("Welcome to Darkblue Wizards!");
                break;

            case -986:
                TeleportHandler.teleportPlayer(player, new Position(3040, 4838, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("@blu@Welcome to Heated pyros!");
                break;

            case -983:
                TeleportHandler.teleportPlayer(player, new Position(2524, 10143, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to the Dark purplefire Wyrm!");
                break;

            case -980:
                TeleportHandler.teleportPlayer(player, new Position(2517, 4645), player.getSpellbook()
                        .getTeleportType());
                break;

            case -977:
                TeleportHandler.teleportPlayer(player, new Position(2539, 5774, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("Welcome to Cloud");
                break;

            case -927:
                TeleportHandler.teleportPlayer(player, new Position(2767, 4703, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Herbal Rogue");
                break;

            case -926:
                TeleportHandler.teleportPlayer(player, new Position(2807, 4704, 0),
                        player.getSpellbook().getTeleportType());
                player.sendMessage("@red@Goodluck !");
                break;

            case -925:
                TeleportHandler.teleportPlayer(player, new Position(2785, 4698, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("@blu@Welcome to purple Wyrm");
                break;

            case -924:
                TeleportHandler.teleportPlayer(player, new Position(2519, 4644, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("Welcome to Trinity!");
                break;

            case -915:
                TeleportHandler.teleportPlayer(player, new Position(2807, 4704, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender()
                        .sendMessage("@red@Best Drops: Custom Infernal particle cape, Dark Purplefire Particle sled etc.");
                break;

            case -923:
                TeleportHandler.teleportPlayer(player, new Position(2539, 5774, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Welcome to Cloud!");
                break;

            case -922:
                TeleportHandler.teleportPlayer(player, new Position(2766, 4700, 0),
                        player.getSpellbook().getTeleportType());
                break;

            case 19076: // Opens custom NPC teleports interface
                player.getPacketSender().sendInterface(50500);
                break;

            // End here
            case -3334:
                if (!player.getClickDelay().elapsed(3000)) {
                    player.sendMessage("@red@Please wait a few secounds before trying to upgrade again.");
                    return;
                }
                new UpgradeListener(player).upgrade();
                player.getClickDelay().reset();
                break;

            case -2334:
                if (!player.getClickDelay().elapsed(3000)) {
                    player.sendMessage("@red@Please wait a few secounds before trying to dissolve again.");
                    return;
                }
                //new DissolveListener(player).dissolve();
                //player.getClickDelay().reset();
                break;
            case -23771:
                Achievements.openInterface(player, AchievementData.FILL_WELL_OF_GOODWILL_1M);
                break;
            case -23770:
                Achievements.openInterface(player, AchievementData.FILL_WELL_OF_GOODWILL_50M);
                break;
            case -23769:
                Achievements.openInterface(player, AchievementData.FILL_WELL_OF_GOODWILL_250M);
                break;
            case -23768:
                Achievements.openInterface(player, AchievementData.COMPLETE_ALL_HARD_TASKS);
                break;
            case 26113:
                player.dropLogOrder = !player.dropLogOrder;
                if (player.dropLogOrder) {
                    player.getPA().sendFrame126(26113, "Oldest to Newest");
                } else {
                    player.getPA().sendFrame126(26113, "Newest to Oldest");
                }
                break;
            case -29031:
                ProfileViewing.rate(player, true);
                break;
            case -26345:
                displayInstructions(player);
                break;
            case 19081:
                player.getCollectionLog().open();
                break;

            case -29028:
                ProfileViewing.rate(player, false);
                break;
            case -27454:
            case -27534:
            case 5384:
            case 12729:
            case -736:
            case -19076:
            case -14492:
            case -13233:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case -18132:
                if (player.isLegendary()) {
                    player.getMysteryBox().spinRUBox();
                } else if (player.isMbox1())
                    player.getMysteryBox().spinMbox1();
                else if (player.isMbox2())
                    player.getMysteryBox().spinMbox2();
                else if (player.isMbox3())
                    player.getMysteryBox().spinMbox3();
                else if (player.isMbox4())
                    player.getMysteryBox().spinMbox4();
                else if (player.isMbox5())
                    player.getMysteryBox().spinMbox5();
                else
                    player.getMysteryBox().spin();
                break;
            case -17631:
                KBD.closeInterface(player);
                break;

            case -31906:
                player.getPlayerOwnedShopManager().open();
                player.resetInterfaces();
                break;

            case -11438:
                if (player.getClickDelay().elapsed(4500)) {
                    player.getPlayerOwnedShopManager().openEditor();
                    player.resetInterfaces();
                }
                /**    player.getPlayerOwnedShopManager().openEditor();
                    player.getClickDelay().reset();
                } else {
                    player.sendMessage("Please wait a few secs before doing this again.");
                    return;
                }
                break;**/

            case -17629:
                if (player.getLocation() == Location.KING_BLACK_DRAGON) {
                    KBD.nextItem(player);
                }
                if (player.getLocation() == Location.SLASH_BASH) {
                    SLASHBASH.nextItem(player);
                }
                if (player.getLocation() == Location.TORM_DEMONS) {
                    TDS.nextItem(player);
                }
                if (player.getLocation() == Location.CORPOREAL_BEAST) {
                    CORP.nextItem(player);
                }
                if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
                    DAGS.nextItem(player);
                }
/*                if (player.getLocation() == Location.BANDOS_AVATAR) {
                    AVATAR.nextItem(player);
                }*/
                if (player.getLocation() == Location.KALPHITE_QUEEN) {
                    KALPH.nextItem(player);
                }
                if (player.getLocation() == Location.PHOENIX) {
                    PHEON.nextItem(player);
                }
                if (player.getLocation() == Location.GLACORS) {
                    GLAC.nextItem(player);
                }
                if (player.getLocation() == Location.SKOTIZO) {
                    SKOT.nextItem(player);
                }
                if (player.getLocation() == Location.CERBERUS) {
                    CERB.nextItem(player);
                }
                if (player.getLocation() == Location.NEX) {
                    NEXX.nextItem(player);
                }
                if (player.getLocation() == Location.GODWARS_DUNGEON) {
                    GWD.nextItem(player);
                }
                if (player.getLocation() == Location.ASSASSIN) {
                    BORKS.nextItem(player);
                }
                if (player.getLocation() == Location.LIZARDMAN) {
                    LIZARD.nextItem(player);
                }
                if (player.getLocation() == Location.BARRELCHESTS) {
                    BARRELS.nextItem(player);
                }
                break;
            case 12859:
                player.sendMessage("@red@This feature is currently disabled.");
                /*
                 * if (!player.isBanking() || player.getInterfaceId() != 5292) return;
                 * player.setPlaceholders(!player.isPlaceholders());
                 * player.getPacketSender().sendConfig(111, player.isPlaceholders() ? 1 : 0);
                 */
                break;

            case -17630:
                if (player.getLocation() == Location.KING_BLACK_DRAGON) {
                    KBD.previousItem(player);
                }
                if (player.getLocation() == Location.SLASH_BASH) {
                    SLASHBASH.previousItem(player);
                }
                if (player.getLocation() == Location.TORM_DEMONS) {
                    TDS.previousItem(player);
                }
                if (player.getLocation() == Location.CORPOREAL_BEAST) {
                    CORP.previousItem(player);
                }
                if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
                    DAGS.previousItem(player);
                }
/*                if (player.getLocation() == Location.BANDOS_AVATAR) {
                    AVATAR.previousItem(player);
                }*/
                if (player.getLocation() == Location.KALPHITE_QUEEN) {
                    KALPH.previousItem(player);
                }
                if (player.getLocation() == Location.PHOENIX) {
                    PHEON.previousItem(player);
                }
                if (player.getLocation() == Location.GLACORS) {
                    GLAC.previousItem(player);
                }
                if (player.getLocation() == Location.SKOTIZO) {
                    SKOT.previousItem(player);
                }
                if (player.getLocation() == Location.CERBERUS) {
                    CERB.previousItem(player);
                }
                if (player.getLocation() == Location.NEX) {
                    NEXX.previousItem(player);
                }
                if (player.getLocation() == Location.GODWARS_DUNGEON) {
                    GWD.previousItem(player);
                }
                if (player.getLocation() == Location.ASSASSIN) {
                    BORKS.previousItem(player);
                }
                if (player.getLocation() == Location.LIZARDMAN) {
                    LIZARD.previousItem(player);
                }
                if (player.getLocation() == Location.BARRELCHESTS) {
                    BARRELS.previousItem(player);
                }
                break;

            // case -26373:
            // DropLog.open(player);
            // break;
            case 1036:
                EnergyHandler.rest(player);
                break;
            // case -26376:
            // PlayersOnlineInterface.showInterface(player);
            // break;


            //new quest tab
            case -31194:
                PlayerPanel.handleSwitch(player, 1, false);
                break;
            case -31192:
                PlayerPanel.handleSwitch(player, 2, false);
                break;
            case -31190:
                PlayerPanel.handleSwitch(player, 3, false);
                break;
            case -31188:
                PlayerPanel.handleSwitch(player, 4, false);
                break;
            case -31186:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 47500); // 26600
                break;
            case -18034:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 38333); // 26600
                break;
            case 32422:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 38333); // 26600
                break;
            case -31196:
                //player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 45000); // 26600
                NpcTasks.updateInterface(player);
                player.getPacketSender().sendInterfaceReset();
                player.getPacketSender().sendInterface(65400);
                break;
            case 19066:
                PlayerPanel.refreshPanel(player);
                KillsTracker.open(player);
                break;
            case -26373:
                PlayerPanel.refreshPanel(player);
                DropLog.open(player);
                break;

            case -30281:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 19071:
                PlayerPanel.refreshPanel(player);
                DropLog.open(player);
                break;
            //End
            case 19051:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 46343);
                StaffList.updateInterface(player);
                PlayerPanel.refreshPanel(player);
                break;
            case -26367:
                PlayerPanel.refreshPanel(player);
                break;

            case -26368:
                PlayerPanel.refreshPanel(player);
                break;
            case 32388:
                PlayerPanel.refreshPanel(player);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 47500); // 26600
                break;
            case -26359:
                PlayerPanel.refreshPanel(player);
                player.setExperienceLocked(!player.experienceLocked());
                player.sendMessage("Your experience is now: " + (player.experienceLocked() ? "locked" : "unlocked"));
                break;
            case -26360:
                PlayerPanel.refreshPanel(player);
                player.sendMessage("You have donated a total of: $" + player.getAmountDonated() + "!");
                break;
            case 27229:
                DungeoneeringParty.create(player);
                break;
            case 3229:
                player.sendMessage("Common Costs 50 Zamron Points.");
                break;
            case 3218:
                player.sendMessage("Uncommon Package Costs 100 Zamron Points.");
                break;
            case 3215:
                player.sendMessage("Extreme Package Costs 200 Zamron Points.");
                break;
            case 3221:
                player.sendMessage("Rare Package Costs 150 Zamron Points.");
                break;
            case 3235:
                player.sendMessage("Legendary Package Costs 250 Zamron Points.");
                break;
            case 3204:
                if (player.getRuneUnityPoints() >= 150) {
                    player.getInventory().add(15371, 1);
                    player.incrementRuneUnityPoints(150);
                    PlayerPanel.refreshPanel(player);
                }
                break;
            case 3206:
                if (player.getRuneUnityPoints() >= 200) {
                    player.getInventory().add(15372, 1);
                    player.incrementRuneUnityPoints(200);
                    PlayerPanel.refreshPanel(player);
                }
                break;
            case 3260:
                player.getPacketSender().sendString(1, "");
                player.getPacketSender()
                        .sendMessage("Attempting to open discord...");
                break;
            case 3208:
                if (player.getRuneUnityPoints() >= 100) {
                    player.getInventory().add(15370, 1);
                    player.incrementRuneUnityPoints(100);
                    PlayerPanel.refreshPanel(player);
                }
                break;
            case 3225:
                if (player.getRuneUnityPoints() >= 50) {
                    player.getInventory().add(15369, 1);
                    player.incrementRuneUnityPoints(50);
                    PlayerPanel.refreshPanel(player);
                }
                break;
            case 3240:
                if (player.getRuneUnityPoints() >= 250) {
                    player.getInventory().add(15373, 1);
                    player.incrementRuneUnityPoints(250);
                    PlayerPanel.refreshPanel(player);
                }
                break;
            case 26226:
            case 26229:
                if (Dungeoneering.doingDungeoneering(player)) {
                    DialogueManager.start(player, 114);
                    player.setDialogueActionId(71);
                } else {
                    Dungeoneering.leave(player, false, true);
                }
                break;
            case 26244:
            case 26247:
                if (player.getMinigameAttributes()
                        .getDungeoneeringAttributes()
                        .getParty() != null) {
                    if (player.getMinigameAttributes()
                            .getDungeoneeringAttributes()
                            .getParty()
                            .getOwner()
                            .getUsername()
                            .equals(player.getUsername())) {
                        DialogueManager.start(player, id == 26247 ? 106 : 105);
                        player.setDialogueActionId(id == 26247 ? 68 : 67);
                    } else {
                        player.getPacketSender()
                                .sendMessage("Only the party owner can change this setting.");
                    }
                }
                break;
            case 28180:
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getSummoning().getFamiliar() != null) {
                    player.getPacketSender()
                            .sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
                    player.getPacketSender()
                            .sendMessage("You must dismiss your familiar before joining the dungeon");
                    return;
                }

                TeleportHandler.teleportPlayer(player, new Position(3450, 3715), player.getSpellbook()
                        .getTeleportType());
                break;
            case 14176:
                player.setUntradeableDropItem(null);
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 14175:
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getUntradeableDropItem() != null
                        && player.getInventory()
                        .contains(player.getUntradeableDropItem().getId())) {
                    ItemBinding.unbindItem(player, player.getUntradeableDropItem().getId());
                    player.getInventory().delete(player.getUntradeableDropItem());
                    player.getPacketSender()
                            .sendMessage("Your item vanishes as it hits the floor.");
                    Sounds.sendSound(player, Sound.DROP_ITEM);
                }
                player.setUntradeableDropItem(null);
                break;
            case 1013:
                player.getSkillManager().setTotalGainedExp(0);
                break;
            case -26369:
                if (WellOfGoodwill.isActive()) {
                    player.getPacketSender().sendMessage(
                            "<img=12> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another "
                                    + WellOfGoodwill.getMinutesRemaining() + " minutes.");
                } else {
                    player.getPacketSender()
                            .sendMessage("<img=12> <col=008FB2>The Well of Goodwill needs another "
                                    + Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount())
                                    + " coins before becoming full.");
                }
                break;


            case -10531:
                if (player.isKillsTrackerOpen()) {
                    player.setKillsTrackerOpen(false);
                    player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
                    PlayerPanel.refreshPanel(player);
                }
                break;

            case 28177:
                if (!TeleportHandler.checkReqs(player, null)) {
                    player.getSkillManager().stopSkilling();
                    return;
                }
                if (!player.getClickDelay().elapsed(4500) || player.getMovementQueue()
                        .isLockMovement()) {
                    player.getSkillManager().stopSkilling();
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    player.getSkillManager().stopSkilling();
                    return;
                }
                Construction.newHouse(player);
                TaskManager.submit(new AutorelFixTask(player));
                Construction.enterHouse(player, player, true, true);
                player.getSkillManager().stopSkilling();
                break;
            case -30282:
                KillsTracker.openBoss(player);
                break;
            case -10283:
                KillsTracker.open(player);
                break;

            case 10004:
                player.getPacketSender().sendInterface(23500);
                break;

            case 19061:
               // ProfileViewing.view(player, player);
            	player.getPacketSender().sendConfig(4600, 0);
    			Presets.start(player);
                
                break;
            case 350:
                player.getPacketSender()
                        .sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
                        .sendTab(GameSettings.MAGIC_TAB)
                        .sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
                break;
            case 12162:
                DialogueManager.start(player, 61);
                player.setDialogueActionId(28);
                break;
            case 29335:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                DialogueManager.start(player, 60);
                player.setDialogueActionId(27);
                break;
            case 29455:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                ClanChatManager.toggleLootShare(player);
                break;
            case 8658:
                DialogueManager.start(player, 55);
                player.setDialogueActionId(26);
                break;
            case 11001:
                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(), player.getSpellbook().getTeleportType());
                break;
            case 8667:
                TeleportHandler.teleportPlayer(player, new Position(3208, 3159), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8672:
                TeleportHandler.teleportPlayer(player, new Position(2595, 4772), player.getSpellbook()
                        .getTeleportType());
                player.getPacketSender().sendMessage(
                        "<img=12> To get started with Runecrafting, buy a talisman and use the locate option on it.");
                break;
            case 8861:
                TeleportHandler.teleportPlayer(player, new Position(3208, 3160), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8656:
                player.setDialogueActionId(47);
                DialogueManager.start(player, 86);
                break;
            case 8659:
                TeleportHandler.teleportPlayer(player, new Position(3024, 9741), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8664:
                TeleportHandler.teleportPlayer(player, new Position(3191, 3161), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8666:
                TeleportHandler.teleportPlayer(player, new Position(2334, 3656), player.getSpellbook()
                        .getTeleportType());
                break;

            /*
             * Teleporting Called Below
             */

            case -4914:
            case -4911:
            case -4908:
            case -4905:
            case -4899:
            case -4896:
            case -4893:
            case -4890:
            case -4845:
            case -4839:
            case -4842:
                Teleporting.teleport(player, id);
                break;

            case -4902:
                if (player.getSummoning().getFamiliar() != null) {
                    player.getPacketSender()
                            .sendMessage("You must dismiss your familiar before teleporting to the arena!");
                } else {
                    Teleporting.teleport(player, id);
                }
                break;

            case 10003: //Change this to monster
                //TeleportInterface.sendSlayerData(player, TeleportInterface.Slayers.BOWSER);
                //TeleportInterface.sendBossTab(player);
                TeleportInterface.sendSlayerData(player, TeleportInterface.Slayers.BOWSER);
                TeleportInterface.sendBossTab(player);
                TeleportInterface.sendDrops(player, 728);
                break;

            case -4934:
                Teleporting.openTab(player, -4934);
                break;
            case -4931:
                Teleporting.openTab(player, -4931);
                break;
            case -4928:
                Teleporting.openTab(player, -4928);
                break;
            case -4925:
                Teleporting.openTab(player, -4925);
                break;
            case -4922:
                Teleporting.openTab(player, -4922);
                break;
            case -4919:
                Teleporting.openTab(player, -4919);
                break;

            /*
             * End Teleporting
             */

            case 8671:
                player.setDialogueActionId(56);
                DialogueManager.start(player, 89);
                break;
            case 8670:
                TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8668:
                TeleportHandler.teleportPlayer(player, new Position(2709, 3437), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8665:
                TeleportHandler.teleportPlayer(player, new Position(2340, 3706), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8662:
                TeleportHandler.teleportPlayer(player, new Position(2341, 3700), player.getSpellbook()
                        .getTeleportType());
                break;
            case 13928:
                TeleportHandler.teleportPlayer(player, new Position(3052, 3304), player.getSpellbook()
                        .getTeleportType());
                break;
            case 28179:
                TeleportHandler.teleportPlayer(player, new Position(2209, 5348), player.getSpellbook()
                        .getTeleportType());
                break;
            case 28178:
                DialogueManager.start(player, 54);
                player.setDialogueActionId(25);
                break;
            case 1159: // Bones to Bananas
            case 15877:// Bones to peaches
            case 30306:
                MagicSpells.handleMagicSpells(player, id);
                break;
            case 10001:
                if (player.getInterfaceId() == -1) {
                    Consumables.handleHealAction(player);
                } else {
                    player.getPacketSender().sendMessage("You cannot heal yourself right now.");
                }
                break;
            case 18025:
                if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
                }
                break;
            case 18018:
                if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
                }
                break;
            case 10000:
            case 950:
                if (player.getInterfaceId() < 0)
                    player.getPacketSender().sendInterface(40030);
                else
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before doing this.");
                break;
            case 3546:
            case 3420:
                if (System.currentTimeMillis() - player.getTrading().lastAction <= 300)
                    return;
                player.getTrading().lastAction = System.currentTimeMillis();
                if (player.getTrading().inTrade()) {
                    player.getTrading().acceptTrade(id == 3546 ? 2 : 1);
                } else {
                    player.getPacketSender().sendInterfaceRemoval();
                }
                break;
            // gambling
            case -8384:
                if (System.currentTimeMillis() - player.getGambling().lastAction <= 300)
                    return;
                player.getGambling().lastAction = System.currentTimeMillis();
                if (player.getGambling().inGamble() && player.getGambling()
                        .getGamblingMode() != null) {
                    player.getGambling().acceptGamble(1);
                } else {
                    player.sendMessage("@red@Game mode not set, set 1 to play");
                }
                //System.out.println("In gamble: " + player.getGambling().inGamble());
                break;

            case -8383:
                if (player.getGambling().inGamble())
                    player.getGambling().declineGamble(true);
                break;

            case 10162:
            case -18269:
            case 11729:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 841:
                IngridientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
                break;
            case 839:
                IngridientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
                break;
            case 14922:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                break;
            case 14921:
                player.getPacketSender()
                        .sendMessage("Please visit the forums and ask for help in the support section.");
                break;
            case 5294:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
                DialogueManager.start(player,
                        DialogueManager.getDialogues()
                                .get(player.getBankPinAttributes().hasBankPin() ? 12 : 9));
                break;
            case 27653:
                if (!player.busy() && !player.getCombatBuilder().isBeingAttacked()
                        && !Dungeoneering.doingDungeoneering(player)) {
                    player.getSkillManager().stopSkilling();
                    player.getPriceChecker().open();
                } else {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                }
                break;

            case -12034:
                if (ItemComparing.getSingleton().clickNumber == 2
                        && ItemComparing.getSingleton().secondTabBonuses.size() != 0) {
                    ItemComparing.getSingleton()
                            .sendItemStats(player, ItemComparing.getSingleton().itemData.keySet()
                                    .toArray(new Integer[1000])[ItemComparing.getSingleton().index]);
                } else {
                    player.sendMessage("@red@2 items isn't chosen");
                    return;
                }
                break;

            case 2735:
            case 1511:
                if (player.getSummoning().getBeastOfBurden() != null) {
                    player.getSummoning().toInventory();
                    player.getPacketSender().sendInterfaceRemoval();
                } else {
                    player.getPacketSender()
                            .sendMessage("You do not have a familiar who can hold items.");
                }
                break;
            case -11501:
            case -11504:
            case -11498:
            case -11507:
            case 1020:
            case 1021:
            case 1019:
            case 1018:
                if (id == 1020 || id == -11504)
                    SummoningTab.renewFamiliar(player);
                else if (id == 1019 || id == -11501)
                    SummoningTab.callFollower(player);
                else if (id == 1021 || id == -11498)
                    SummoningTab.handleDismiss(player, false);
                else if (id == -11507)
                    player.getSummoning().store();
                else if (id == 1018)
                    player.getSummoning().toInventory();
                break;
            case 11004:
                TeleportHandler.teleportPlayer(player, new Position(2605, 3093), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8654:
            case 8657:
            case 8655:
            case 8663:
            case 8669:
            case 8660:
            case 11008:
            	TeleportInterface.sendSlayerData(player, Slayers.BOWSER);
                TeleportInterface.sendBossTab(player);
                break;
            case 11017:
            	TeleportInterface.sendSlayerData(player, Slayers.BOWSER);
                TeleportInterface.sendBossTab(player);
                break;
            case 11011:
            	TeleportInterface.sendSlayerData(player, TeleportInterface.Slayers.BOWSER);
                TeleportInterface.sendBossTab(player);
                break;

            case 11020:
            	TeleportInterface.sendSlayerData(player, Slayers.BOWSER);
                TeleportInterface.sendHardenedTab(player);
                break;

            case 2799:
            case 2798:
            case 1747:
            case 1748:
            case 8890:
            case 8886:
            case 8875:
            case 8871:
            case 8894:
                ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
                break;
            case 14873:
            case 14874:
            case 14875:
            case 14876:
            case 14877:
            case 14878:
            case 14879:
            case 14880:
            case 14881:
            case 14882:
                BankPin.clickedButton(player, id);
                break;
            case 27005:
            case 22012:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
                break;
            case 27023:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                if (player.getSummoning().getBeastOfBurden() == null) {
                    player.getPacketSender()
                            .sendMessage("You do not have a familiar which can hold items.");
                    return;
                }
                Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
                break;
            case 22008:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setNoteWithdrawal(!player.withdrawAsNote());
                break;
        	case 27026:
    			if(!player.isBanking() || player.getInterfaceId() != 5292)
    				return;
    			player.setPlaceholders(!player.isPlaceholders());
    			break;
            case 21000:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setSwapMode(false);
                player.getPacketSender()
                        .sendConfig(304, 0)
                        .sendMessage("This feature is coming soon!");
                // player.setSwapMode(!player.swapMode());
                break;
            case 27009:
                MoneyPouch.toBank(player);
                break;
            case 27014:
            case 27015:
            case 27016:
            case 27017:
            case 27018:
            case 27019:
            case 27020:
            case 27021:
            case 27022:
                if (!player.isBanking())
                    return;
                if (player.getBankSearchingAttribtues().isSearchingBank())
                    BankSearchAttributes.stopSearch(player, true);
                int bankId = id - 27014;
                boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
                if (!empty || bankId == 0) {
                    player.setCurrentBankTab(bankId);
                    player.getPacketSender().sendString(5385, "scrollreset");
                    player.getPacketSender()
                            .sendString(27002, Integer.toString(player.getCurrentBankTab()));
                    player.getPacketSender().sendString(27000, "1");
                    player.getBank(bankId).open();
                } else
                    player.getPacketSender()
                            .sendMessage("To create a new tab, please drag an item here.");
                break;
            case 22004:
                if (!player.isBanking())
                    return;
                if (!player.getBankSearchingAttribtues().isSearchingBank()) {
                    player.getBankSearchingAttribtues().setSearchingBank(true);
                    player.setInputHandling(new EnterSyntaxToBankSearchFor());
                    player.getPacketSender()
                            .sendEnterInputPrompt("What would you like to search for?");
                } else {
                    BankSearchAttributes.stopSearch(player, true);
                }
                break;

            case -2203:
                player.setInputHandling(new EnterSyntaxToNpcSearchFor());
                player.getPacketSender()
                        .sendEnterInputPrompt("Enter the NPC Name you'd like to search for.");
                break;

            case -12028:
                ItemComparing.getSingleton().loadItems();
                player.setInputHandling(new EnterSyntaxToItemSearchFor());
                if (amount > 0) {
                    ItemComparing.getSingleton().sendItemNames(player);
                    amount = 0;
                }
                player.getPacketSender()
                        .sendEnterInputPrompt("Enter the Item name you'd like to search for.");
                amount++;
                break;

            case 32602:
                player.setInputHandling(new PosInput());
                player.getPacketSender()
                        .sendEnterInputPrompt("What/Who would you like to search for?");
                break;

            case -15086:
            case -12286:
            case -12046:
            case -3306:
                player.getPacketSender().sendInterfaceRemoval();
                break;

            case 22845:
            case 24115:
            case 24010:
            case 24041:
            case 150:
                player.setAutoRetaliate(!player.isAutoRetaliate());
                break;
            case 29332:
                ClanChat clan = player.getCurrentClanChat();
                if (clan == null) {
                    player.getPacketSender().sendMessage("You are not in a clanchat channel.");
                    return;
                }
                ClanChatManager.leave(player, false);
                player.setClanChatName(null);
                break;
            case 29329:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.setInputHandling(new EnterClanChatToJoin());
                player.getPacketSender()
                        .sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
                break;
            case 19158:
            case 152:
                if (player.getRunEnergy() <= 1) {
                    player.getPacketSender()
                            .sendMessage("You do not have enough energy to do this.");
                    player.setRunning(false);
                } else
                    player.setRunning(!player.isRunning());
                player.getPacketSender().sendRunStatus();
                break;

            case -282:
                DropLog.openRare(player);
                break;

            case 27658:
                player.setExperienceLocked(!player.experienceLocked());
                String type = player.experienceLocked() ? "locked" : "unlocked";
                player.getPacketSender().sendMessage("Your experience is now " + type + ".");
                PlayerPanel.refreshPanel(player);
                break;

            case -11534:
                ItemComparing.getSingleton().reset(player);
                break;

            case 27651:
            case 21341:
                if (player.getInterfaceId() == -1) {
                    player.getSkillManager().stopSkilling();
                    BonusManager.update(player);
                    player.getPacketSender().sendInterface(21172);
                } else
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before doing this.");
                break;
            case 27654:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.getSkillManager().stopSkilling();
                ItemsKeptOnDeath.sendInterface(player);
                break;
            case 2458: // Logout
                if (player.isInRaid()) {
                    player.getPacketSender().sendMessage("You can't logout while in raids!");
                    return;
                }
                if (player.logout()) {
                    World.getPlayers().remove(player);
                }
                break;
            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
            case 7473:
            case 7562:
            case 7487:
            case 7788:
            case 8481:
            case 7612:
            case 7587:
            case 7662:
            case 7462:
            case 7548:
            case 7687:
            case 7537:
            case 12322:
            case 7637:
            case 12311:
            case -24530:
                CombatSpecial.activate(player);
                break;
            case 1772: // shortbow & longbow
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_ACCURATE);
                }
                break;
            case 1771:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_RAPID);
                }
                break;
            case 1770:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_LONGRANGE);
                }
                break;
            case 2282: // dagger & sword
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_STAB);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_STAB);
                }
                break;
            case 2285:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_LUNGE);
                }
                break;
            case 2284:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_SLASH);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_SLASH);
                }
                break;
            case 2283:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_BLOCK);
                }
                break;
            case 2429: // scimitar & longsword
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_CHOP);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_CHOP);
                }
                break;
            case 2432:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_SLASH);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_SLASH);
                }
                break;
            case 2431:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_LUNGE);
                }
                break;
            case 2430:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_BLOCK);
                }
                break;
            case 3802: // mace
                player.setFightType(FightType.MACE_POUND);
                break;
            case 3805:
                player.setFightType(FightType.MACE_PUMMEL);
                break;
            case 3804:
                player.setFightType(FightType.MACE_SPIKE);
                break;
            case 3803:
                player.setFightType(FightType.MACE_BLOCK);
                break;
            case 4454: // knife, thrownaxe, dart & javelin
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_ACCURATE);
                }
                break;
            case 4453:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_RAPID);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_RAPID);
                }
                break;
            case 4452:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_LONGRANGE);
                }
                break;
            case 4685: // spear
                player.setFightType(FightType.SPEAR_LUNGE);
                break;
            case 4688:
                player.setFightType(FightType.SPEAR_SWIPE);
                break;
            case 4687:
                player.setFightType(FightType.SPEAR_POUND);
                break;
            case 4686:
                player.setFightType(FightType.SPEAR_BLOCK);
                break;
            case 4711: // 2h sword
                player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
                break;
            case 4714:
                player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
                break;
            case 4713:
                player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
                break;
            case 4712:
                player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
                break;
            case 5576: // pickaxe
                player.setFightType(FightType.PICKAXE_SPIKE);
                break;
            case 5579:
                player.setFightType(FightType.PICKAXE_IMPALE);
                break;
            case 5578:
                player.setFightType(FightType.PICKAXE_SMASH);
                break;
            case 5577:
                player.setFightType(FightType.PICKAXE_BLOCK);
                break;
            case 7768: // claws
                player.setFightType(FightType.CLAWS_CHOP);
                break;
            case 7771:
                player.setFightType(FightType.CLAWS_SLASH);
                break;
            case 7770:
                player.setFightType(FightType.CLAWS_LUNGE);
                break;
            case 7769:
                player.setFightType(FightType.CLAWS_BLOCK);
                break;
            case 8466: // halberd
                player.setFightType(FightType.HALBERD_JAB);
                break;
            case 8468:
                player.setFightType(FightType.HALBERD_SWIPE);
                break;
            case 8467:
                player.setFightType(FightType.HALBERD_FEND);
                break;
            case 5862: // unarmed
                player.setFightType(FightType.UNARMED_PUNCH);
                break;
            case 5861:
                player.setFightType(FightType.UNARMED_KICK);
                break;
            case 5860:
                player.setFightType(FightType.UNARMED_BLOCK);
                break;
            case 12298: // whip
                player.setFightType(FightType.WHIP_FLICK);
                break;
            case 12297:
                player.setFightType(FightType.WHIP_LASH);
                break;
            case 12296:
                player.setFightType(FightType.WHIP_DEFLECT);
                break;
            case 336: // staff
                player.setFightType(FightType.STAFF_BASH);
                break;
            case 335:
                player.setFightType(FightType.STAFF_POUND);
                break;
            case 334:
                player.setFightType(FightType.STAFF_FOCUS);
                break;
            case 433: // warhammer
                player.setFightType(FightType.WARHAMMER_POUND);
                break;

            case 30362:
                player.setInputHandling(new SearchForCollectionNpc());
                player.getPacketSender()
                        .sendEnterInputPrompt("Type the NPC name that you would like to search for");
                break;
            case 432:
                player.setFightType(FightType.WARHAMMER_PUMMEL);
                break;
            case 431:
                player.setFightType(FightType.WARHAMMER_BLOCK);
                break;
            case 782: // scythe
                player.setFightType(FightType.SCYTHE_REAP);
                break;
            case 784:
                player.setFightType(FightType.SCYTHE_CHOP);
                break;
            case 785:
                player.setFightType(FightType.SCYTHE_JAB);
                break;
            case 783:
                player.setFightType(FightType.SCYTHE_BLOCK);
                break;
            case 1704: // battle axe
                player.setFightType(FightType.BATTLEAXE_CHOP);
                break;
            case 1707:
                player.setFightType(FightType.BATTLEAXE_HACK);
                break;
            case 1706:
                player.setFightType(FightType.BATTLEAXE_SMASH);
                break;
            case 1705:
                player.setFightType(FightType.BATTLEAXE_BLOCK);
                break;
            case -8305:
                player.getPacketSender().sendInterfaceRemoval();
                break;
        }

    }


    private boolean checkHandlers(Player player, int id) {

        if (MapTeleportInterface.processButton(player, id)) {
            return true;
        }

        if (GlobalPerks.getInstance().handleButton(player, id)) {
            return true;
        }
        if (StartScreen.handleButton(player, id)) {
            return true;
        }
        
		if(player.getBestItems().handleButton(id))
			return true;
		

        if (player.getCollectionLog().handleButton(id))
            return true;
        if (Construction.handleButtonClick(id, player)) {
            return true;
        }
        switch (id) {

            /** Game Mode Selector **/
            case -11042: // Website Button
                player.getPacketSender().openURL("https://zamron.net");
                break;
            case -12042: // Vote Button
                player.getPacketSender().openURL("https://zamron.everythingrs.com/services/vote");
                break;

            case 2494:
            case 2495:
            case 2496:
            case 2497:
            case 2498:
            case 2471:
            case 2472:
            case 2473:
            case 2461:
            case 2462:
            case 2482:
            case 2483:
            case 2484:
            case 2485:
                DialogueOptions.handle(player, id);
                return true;

        }
        if (player.getTeleportInterface().handleButton(id)) {
            return true;
        }

        if (player.getDropSimulator().handleButton(id)) {
            return true;
        }

        if (player.getInstanceSystem().handleClick(id)) {
            return true;
        }

        if (player.getCollectionLog().handleButton(id)) {
            return true;
        }
        if (player.getTeleportInterface().handleButton(id)) {
            return true;
        }


        if (player.isPlayerLocked() && id != 2458 && id != -12780 && id != -12779 && id != -12778 && id != -29767) {
            return true;
        }

        if (Achievements.handleButton(player, id)) {
            return true;
        }

        if (StarterTasks.handleButton(player, id)) {
            return true;
        }

        if (NpcTasks.handleButton(player, id)) {
            return true;
        }

        if (ItemComparing.getSingleton().handleButton(player, id)) {
            return true;
        }

        if (GuideBook.isGuideInterfaceButton(player, id)) {
            return true;
        }

        if (Sounds.handleButton(player, id)) {
            return true;
        }
        if (PrayerHandler.isButton(id)) {
            PrayerHandler.togglePrayerWithActionButton(player, id);
            return true;
        }
        if (CurseHandler.isButton(player, id)) {
            return true;
        }

        if (Autocasting.handleAutocast(player, id)) {
            return true;
        }
        if (SmithingData.handleButtons(player, id)) {
            return true;
        }
        if (PouchMaking.pouchInterface(player, id)) {
            return true;
        }
        if (TeleportInterface.handleButton(player, id)) {
            return true;
        }
        if (LoyaltyProgramme.handleButton(player, id)) {
            return true;
        }
        if (Fletching.fletchingButton(player, id)) {
            return true;
        }
        if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
            return true;
        }
        if (Emotes.doEmote(player, id)) {
            return true;
        }
        if (PestControl.handleInterface(player, id)) {
            return true;
        }
        if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
            return true;
        }
        if (Slayer.handleRewardsInterface(player, id)) {
            return true;
        }
        if (ExperienceLamps.handleButton(player, id)) {
            return true;
        }
        if (PlayersOnlineInterface.handleButton(player, id)) {
            return true;
        }
        if (GrandExchange.handleButton(player, id)) {
            return true;
        }
        if (ClanChatManager.handleClanChatSetupButton(player, id)) {
            return true;
        }
        return false;
    }

    private static void displayInstructions(Player player) {
        for (int i = 8145; i <= 8195; i++)
            player.getPacketSender().sendString(i, "");

        player.getPacketSender().sendString(8144, "@dre@Minigame Points Guide");
        player.getPacketSender().sendString(8147, "@blu@Skeletal horror gives");
        player.getPacketSender().sendString(8148, "@blu@Minigame1 points (Access at ::horror)");
        player.getPacketSender().sendString(8150, "@blu@Bravek Slayer master gives a");
        player.getPacketSender()
                .sendString(8152, "@blu@Minigamepoint2 if lucky (10% chance every time)");
        player.getPacketSender()
                .sendString(8154, "@blu@Phoenix gives Minigamepoint3 if lucky (1/1000)");
        player.getPacketSender()
                .sendString(8156, "@blu@but its easy to kill, access at boss teleports");
        player.getPacketSender().sendString(8158, "@blu@Darklord's give Minigamepoint4 if");
        player.getPacketSender()
                .sendString(8160, "@blu@lucky (1/1000) again. hard to kill tho (high hp)");
        player.getPacketSender()
                .sendString(8162, "@blu@Slash bash gives Minigamepoint5 if super lucky (1/10000)");
        player.getPacketSender().sendString(8164, "@blu@However very easy to kill and it also");
        player.getPacketSender()
                .sendString(8166, "@blu@gives boss points / custom boss points sometimes");
        player.getPacketSender().sendString(8168, "@blu@REWARDS COMING SOON!!!");
        player.getPacketSender()
                .sendString(8170, "@red@You can check all your points with ::checkpoints");
        player.getPacketSender().sendInterface(8134);
    }

    public static final int OPCODE = 185;
}
