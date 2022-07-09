
package com.zamron.net.packet.impl;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.CleansingTask;
import com.zamron.engine.task.impl.HPRegainPotionTask;
import com.zamron.engine.task.impl.PraiseTask;
import com.zamron.engine.task.impl.PrayerRegainPotionTask;
import com.zamron.engine.task.impl.SpecialRegainPotionTask;
import com.zamron.model.Animation;
import com.zamron.model.GameMode;
import com.zamron.model.GameObject;
import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Shop.ShopManager;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.input.impl.ChangePassword;
import com.zamron.model.input.impl.ChangeUsername;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.util.QuickUtils;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.*;
import com.zamron.world.content.DonorBonds;
import com.zamron.world.content.combat.range.DwarfMultiCannon;
import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.guidesInterface.GuideBook;
import com.zamron.world.content.minimes.MiniMeData;
import com.zamron.world.content.minimes.MiniMeFunctions;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.scratchcards.ScratchCard;
import com.zamron.world.content.skill.impl.construction.Construction;
import com.zamron.world.content.skill.impl.dungeoneering.ItemBinding;
import com.zamron.world.content.skill.impl.herblore.Herblore;
import com.zamron.world.content.skill.impl.herblore.IngridientsBook;
import com.zamron.world.content.skill.impl.hunter.BoxTrap;
import com.zamron.world.content.skill.impl.hunter.Hunter;
import com.zamron.world.content.skill.impl.hunter.JarData;
import com.zamron.world.content.skill.impl.hunter.PuroPuro;
import com.zamron.world.content.skill.impl.hunter.SnareTrap;
import com.zamron.world.content.skill.impl.hunter.Trap.TrapState;
import com.zamron.world.content.skill.impl.prayer.Prayer;
import com.zamron.world.content.skill.impl.runecrafting.Runecrafting;
import com.zamron.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.zamron.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.content.skill.impl.slayer.SlayerDialogues;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.zamron.world.content.skill.impl.summoning.CharmingImp;
import com.zamron.world.content.skill.impl.summoning.SummoningData;
import com.zamron.world.content.skill.impl.woodcutting.BirdNests;
import com.zamron.world.content.transportation.JewelryTeleporting;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.content.transportation.TeleportType;
import com.zamron.world.content.treasuretrails.ClueScroll;
import com.zamron.world.content.treasuretrails.CoordinateScrolls;
import com.zamron.world.content.treasuretrails.DiggingScrolls;
import com.zamron.world.content.treasuretrails.MapScrolls;
import com.zamron.world.content.treasuretrails.Puzzle;
import com.zamron.world.content.treasuretrails.SearchScrolls;
import com.zamron.world.entity.impl.player.Player;

public class ItemActionPacketListener implements PacketListener {

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}

	public static boolean checkReqs(Player player, Location targetLocation) {
		if (player.getConstitution() <= 0)
			return false;
		if (player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if (player.getLocation() != null && !player.getLocation().canTeleport(player))
			return false;
		if (player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	private static void firstAction(final Player player, Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();

		Location targetLocation = player.getLocation();

		if (interfaceId == 38274) {
			Construction.handleItemClick(itemId, player);
			return;
		}

		if (slot < 0 || slot > player.getInventory().capacity())
			return;

		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;

		player.setInteractingItem(player.getInventory().getItems()[slot]);

		if (itemId == OldRaidParty.MAGICAL_ORB && player.getClickDelay().elapsed(2000)) {
			int counter = 0;
			if (player.getOldRaidParty() == null) {
				player.sendMessage("You are not in an party so you can't check for players to join your party");
				return;
			}
			player.sendMessage("Checking for players that want to join your raid group..");
			for (Player p : World.getPlayers()) {
				if (p != null) {
					if (p.getOldRaidParty() == null) {
						if (p.getRaidPartyInvites().contains(player.getOldRaidParty())) {
							player.sendMessage("@blu@" + p.getUsername() + " is waiting to join your raid group.");
							counter++;
						}
					}
				}
			}
			player.sendMessage("Total: " + counter);
			return;
		}
		
		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		if (Consumables.isFood(player, itemId, slot))
			return;
		if (Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		if (BirdNests.isNest(itemId)) {
			BirdNests.searchNest(player, itemId);
			return;
		}
		if (Herblore.cleanHerb(player, itemId))
			return;
		if (DonorBonds.handleBond(player, itemId))
			return;
		if (ClueScroll.handleCasket(player, itemId) || SearchScrolls.loadClueInterface(player, itemId)
				|| MapScrolls.loadClueInterface(player, itemId) || Puzzle.loadClueInterface(player, itemId)
				|| CoordinateScrolls.loadClueInterface(player, itemId)
				|| DiggingScrolls.loadClueInterface(player, itemId))
			return;
		if (Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if (ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}

		int drinkingAnimation = 829;
		switch (itemId) {

			case 18343:
				//player.setInputHandling(new ChangeUsername());
				//player.getPacketSender().sendEnterInputPrompt("What would you like to change your username to?");
				player.getPacketSender().sendMessage("Currently disabled. Hand this scroll into Oogle for username change.");
				break;

			case 15357:
				player.getInventory().delete(15357, 1);
				Tztok.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned World Boss!");
				break;
			case 15358:
				player.getInventory().delete(15358, 1);
				Assassin.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned Assassin!");
				break;
			case 15359:
				player.getInventory().delete(15359, 1);
				DarkRanger.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned Dark Ranger!");
				break;
			case 15363:
				player.getInventory().delete(15363, 1);
				TheSeph.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned Sephiroph!");
				break;
			case 15361:
				player.getInventory().delete(15361, 1);
				TheRick.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned Rick!");
				break;
			case 15362:
				player.getInventory().delete(15362, 1);
				TheMay.spawn();
				World.sendMessageNonDiscord(player.getUsername() + " has just spawned May!");
				break;

			case 19479:
				if (player.getInventory().contains(itemId)) {
					if (player.getPointsHandler().getSlayerPoints() > 20000) {
						player.getInventory().delete(itemId);
						player.getPointsHandler().decrementSlayerPoints(20000);
						player.getInventory().add(19477, 1);
					} else {
						player.sendMessage("You do not have the required items/points to make the lumberjack scroll :/");
						player.sendMessage("20k Slayer points are needed.");
					}
				}
				break;

			case 10600:
				new com.zamron.world.content.scratchcard.ScratchCard(player).display();
				break;


			case 14415:
				if (!PrayerRegainPotionTask.effectStatus) {
					player.performAnimation(new Animation(drinkingAnimation));
					TaskManager.submit(new PrayerRegainPotionTask(player));
					player.getInventory().delete(14415, 1);
					player.sendMessage("This will increase your prayer 2 times (1 now, 1 afterwards)");
					PrayerRegainPotionTask.effectStatus = true;
				} else {
					player.sendMessage("@blu@You already have the effect.");
				}
				break;

			case 15648:
				player.getPointsHandler().incrementTriviaPoints(player, 10);
				player.getInventory().delete(15648, 1);
				player.sendMessage("10 Trivia points have been added to your account");
				break;

			case 4767:
				player.getPacketSender().openURL("https://zamron.net/gearguide.html");
				player.getPacketSender().sendMessage("Attempting to open: Zamron Gear guide");
				break;

			case 3988:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					new StoreMBox().getReward(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 3989:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					new RandomDrItemBox().open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 5185:
				if (player.getClickDelay().elapsed(60000)) {
					player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 155);
					player.getSkillManager().setCurrentLevel(Skill.ATTACK, 155);
					player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 155);
					player.getSkillManager().setCurrentLevel(Skill.RANGED, 155);
					player.getSkillManager().setCurrentLevel(Skill.MAGIC, 155);
					player.performAnimation(new Animation(829));
					player.sendMessage("@blu@The Potion Makes you feel even stronger");
					player.getClickDelay().reset();
				} else {
					return;
				}
				break;

			case 3961:
				if (player.getClickDelay().elapsed(60000)) {
					player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 199);
					player.getSkillManager().setCurrentLevel(Skill.ATTACK, 199);
					player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 199);
					player.getSkillManager().setCurrentLevel(Skill.RANGED, 199);
					player.getSkillManager().setCurrentLevel(Skill.MAGIC, 199);
					player.getClickDelay().reset();
					player.performAnimation(new Animation(829));
					player.sendMessage("@blu@The Potion Makes you feel even stronger");
				} else {
					return;
				}
				break;

			case 5120:
				StarterTasks.updateInterface(player);
				int[] ids = {12434, 12435, 12436, 5130, 4646, 4645, 4644, 3286, 6570, 6183};
				for (int i = 0; i < ids.length; i++) {
					player.getPacketSender().sendItemOnInterface(53205, ids[i], i, 1);
				}
				player.getPacketSender().sendInterfaceReset();
				player.getPacketSender().sendInterface(53200);
				break;

			case 5168:
				TeleportHandler.teleportPlayer(player, new Position(2524, 4774, 4), player.getSpellbook().getTeleportType());
				break;

			case 5121:
				if (player.getClickDelay().elapsed(3000)) {
					ItemComparing.getSingleton().reset(player);
					ItemComparing.getSingleton().loadItems();
					ItemComparing.getSingleton().sendItemNames(player);
					player.getClickDelay().reset();
				} else {
					player.sendMessage("@red@Wait a few secs before opening this interface again");
					return;
				}
				break;

			case 5122:
				player.getPacketSender().sendInterface(64530);
				break;

			case 5148:
				if (!HPRegainPotionTask.effectStatus) {
					player.performAnimation(new Animation(drinkingAnimation));
					TaskManager.submit(new HPRegainPotionTask(player));
					player.getInventory().delete(5148, 1);
					player.sendMessage("This will heal you 2 times (1 now, 1 afterwards)");
					HPRegainPotionTask.effectStatus = true;
				} else {
					player.sendMessage("@blu@You already have the effect.");
				}
				break;

			case 3903:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					LMSRangeBox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 3902:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					LMSRuneBox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 3886:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					LMSFoodBox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 19890:
				if (player.isDoubleDropsActive()) {
					player.sendMessage("You already have double drops active");
					player.sendMessage(
							"Your double drops effect will end in: " + (int) QuickUtils.tickToMin(player.getCleansingTime())
									+ QuickUtils.getCleansingPrefix(player));
				} else {
					player.getInventory().delete(itemId, 1);
					player.sendMessage("You have been granted 2 hours of double drops.");
					player.incrementCleansingTime(12000);
					TaskManager.submit(new CleansingTask(player));
					player.setDoubleDropsActive(true);
				}
				break;
			case 12848:
				player.getInventory().delete(itemId, 1);
				player.sendMessage("You have been granted 3 hours of double drops.");
				player.incrementCleansingTime(18000);
				TaskManager.submit(new CleansingTask(player));
				player.setDoubleRateActive(true);
				break;


			case 19864:
				try {
					ShopManager.getShops().get(58).open(player);
					player.getPacketSender().sendMessage("This store is pretty cool");
					player.sendMessage("If you have any suggestions on what should be added");
					player.sendMessage("Make sure to make a suggestion and i will consider it.");
				} catch (Exception i) {
					player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
				}
				break;

			case 732:
				try {
					ShopManager.getShops().get(51).open(player);
					player.getPacketSender().sendMessage("This store is pretty cool");
					player.sendMessage("If you have any suggestions on what should be added");
					player.sendMessage("Make sure to make a suggestion and i will consider it.");
				} catch (Exception i) {
					player.sendMessage("Failed to open store.. shop doesn't exist in the list!");
				}
				break;


			case 14808:
				if (player.isDoubleRateActive()) {
					player.sendMessage("You already have double drop rate active");
					player.sendMessage("Your double drop rate effect will end in: "
							+ (int) QuickUtils.tickToMin(player.getPraiseTime()) + QuickUtils.getPraisePrefix(player));
				} else {
					player.getInventory().delete(itemId, 1);
					player.sendMessage("You have been granted 2 hours of double drop rates.");
					player.incrementPraiseTime(12000);
					TaskManager.submit(new PraiseTask(player));
					player.setDoubleRateActive(true);
				}
				break;

			case 5733:
				GuideBook.displayGuideInterface(player);
				break;

			case 7630:
				if (player.getInventory().getFreeSlots() < 7) {
					player.getPacketSender().sendMessage("You need atleast 7 free slots to open this supply crate.");
				} else {
					SupplyCrate.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 6199:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setLegendary(false);// that will do it
					player.setMbox1(false);
					player.setMbox2(false);
					player.setMbox3(false);
					player.setMbox4(false);
					player.setMbox5(false);
					player.getInventory().delete(6199, 1);
					player.getMysteryBox().process();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 3912:
				TaxBagBox.openBox(player);
				break;

			case 15369:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setMbox1(true);
					player.setMbox2(false);
					player.setMbox3(false);
					player.setMbox4(false);
					player.setMbox5(false);
					player.setLegendary(false);
					player.getInventory().delete(15369, 1);
					player.getMysteryBox().processMbox1();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15370:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setMbox1(false);
					player.setMbox2(true);
					player.setMbox3(false);
					player.setMbox4(false);
					player.setMbox5(false);
					player.setLegendary(false);
					player.getInventory().delete(15370, 1);
					player.getMysteryBox().processMbox2();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15371:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setMbox1(false);
					player.setMbox2(false);
					player.setMbox3(true);
					player.setMbox4(false);
					player.setMbox5(false);
					player.setLegendary(false);
					player.getInventory().delete(15371, 1);
					player.getMysteryBox().processMbox3();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15372:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setMbox1(false);
					player.setMbox2(false);
					player.setMbox3(false);
					player.setMbox4(true);
					player.setMbox5(false);
					player.setLegendary(false);
					player.getInventory().delete(15372, 1);
					player.getMysteryBox().processMbox4();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15373:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					player.setLegendary(true);
					player.setMbox1(false);
					player.setMbox2(false);
					player.setMbox3(false);
					player.setMbox4(false);
					player.setMbox5(false);
					player.getInventory().delete(15373, 1);
					player.getMysteryBox().processRUBox();
					player.getMysteryBox().reward();
					ScavengeGain.MysteryBox(player);
				}
				break;


			case 13663:
				if (player.getInterfaceId() > 0) {
					player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
					return;
				}
				player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
				player.getPacketSender().sendString(38006, "Choose stat to reset!")
						.sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
						.sendString(38090, "Which skill would you like to reset?");
				player.getPacketSender().sendInterface(38000);
				break;
			case 19670:
				if (player.getInventory().contains(19670) && player.getClickDelay().elapsed(700)) {
					int amt = player.getInventory().getAmount(19670);
					int pointAmount = 1;
					player.getInventory().delete(19670);
					player.getPointsHandler().incrementVotingPoints(pointAmount);
					player.getPointsHandler().refreshPanel();
					player.getPacketSender().sendMessage("You claim the " + (amt > 1 ? "scrolls" : "scroll")
							+ " and receive your reward including " + pointAmount + " Vote Points");
					StarterTasks.finishTask(player, StarterTasks.StarterTaskData.REDEEM_A_VOTE_SCROLL);
					int chance = RandomUtility.exclusiveRandom(1000);
					if (chance >= 750) {
						player.getInventory().add(3824, 1);
						World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> " + player.getUsername().toString()
								+ " @red@Has just received a Vote Mystery Box!");
					}
					if (chance >= 980) {
						player.getInventory().add(19936, 1);
						World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> " + player.getUsername().toString()
								+ " @red@Has just received a $10 Bond from voting!");
					}
					player.getClickDelay().reset();
				}
				break;
			case 8007: // Varrock
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8007, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(3210, 3424, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 19475: // Custom zone
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8007, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2478, 10129, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 18702:
				for (int i = 8145; i <= 8195; i++) {
					player.getPacketSender().sendString(i, "");
				}
				player.getPacketSender().sendString(8144, "@dre@Zamron's Starter Guide");
				player.getPacketSender().sendString(8145, "@dre@Make sure to check out all the forum guides");
				player.getPacketSender().sendString(8149, "@dre@Start by going to ::starterzone");
				player.getPacketSender().sendString(8153, "@dre@Make sure to vote and sell the vote book");
				player.getPacketSender().sendString(8155, "@dre@Ask new players on what to do next");
				player.getPacketSender().sendInterface(8134);
				break;
			case 19476: // Custom zone 2 ( 30.09.2018 )
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8007, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2524, 4776, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;

			case 5206: // DUNGEON FLOOR 2 TP
				TeleportHandler.teleportPlayer(player, new Position(2845, 3540, 0), TeleportType.NORMAL);
				break;

			case 17922: // DUNGEON FLOOR 2 TP
				TeleportHandler.teleportPlayer(player, new Position(2478, 10129, 0), TeleportType.NORMAL);
				player.getInventory().delete(17922, 1);
				break;

			case 5136: // DUNGEON FLOOR 2 TP
				TeleportHandler.teleportPlayer(player, new Position(2518, 4645, 8), TeleportType.NORMAL);
				player.getInventory().delete(5136, 1);
				break;

			case 6749:
				player.getInventory().delete(6749, 1);
				//player.getSkillManager().setCurrentLevel(Skill.MAGIC, player.getSkillManager().getMaxLevel(Skill.MAGIC) + 7);
				Consumables.overloadIncrease(player, Skill.MAGIC, 2);
				Sounds.sendSound(player, Sounds.Sound.ACTIVATE_PRAYER_OR_CURSE);
				break;

			case 8008: // Lumbridge
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8008, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(3222, 3218, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 8009: // Falador
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8009, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2964, 3378, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 8010: // Camelot
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8010, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2757, 3477, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 8011: // Ardy
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8011, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2662, 3305, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 8012: // Watchtower Tele
				if (player.inFFALobby) {
					player.sendMessage("Use the portal to leave the ffa lobby!");
					return;
				}
				if (player.inFFA) {
					player.sendMessage("You can not teleport out of FFA!");
					return;
				}
				if (!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
					return;
				}
				if (player.getLocation() == Location.CONSTRUCTION) {
					player.getPacketSender().sendMessage("Please use the portal to exit your house");
					return;
				}

				if (!checkReqs(player, targetLocation)) {
					return;
				}
				player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
				cancelCurrentActions(player);
				player.performAnimation(new Animation(4731));
				player.performGraphic(new Graphic(678));
				player.getInventory().delete(8012, 1);
				player.getClickDelay().reset();

				TaskManager.submit(new Task(2, player, false) {
					@Override
					public void execute() {
						Position position = new Position(2728, 3349, 0);
						player.moveTo(position);
						player.getMovementQueue().setLockMovement(false).reset();
						this.stop();
					}
				});

				break;
			case 8013: // Home Tele
				TeleportHandler.teleportPlayer(player, new Position(3087, 3491), TeleportType.NORMAL);
				break;
			case 19121:
				if (player.getInventory().contains(19121, 1)) {
					player.getInventory().delete(19121, 1);
					player.getInventory().add(896, 1);
					player.getInventory().add(2577, 1);
					player.getInventory().add(10835, 1);
					player.getInventory().add(15243, 125);
					player.getInventory().add(5161, 1);
				} else {
					return;
				}
				break;

			case 19477: // Home Tele
				TeleportHandler.teleportPlayer(player, new Position(2392, 9907), TeleportType.NORMAL);
				player.sendMessage("@red@Welcome to the new zone, enjoy the drops :D");
				break;
			case 19478: // Home Tele
				TeleportHandler.teleportPlayer(player, new Position(2899, 9912), TeleportType.NORMAL);
				player.sendMessage("@red@Welcome to the Ant man zone!");
				break;
			case 13598: // Runecrafting Tele
				TeleportHandler.teleportPlayer(player, new Position(2595, 4772), TeleportType.NORMAL);
				break;
			case 13599: // Air Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2845, 4832), TeleportType.NORMAL);
				break;
			case 3062:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					int chance = RandomUtility.random(100);
					if (chance >= 50) {
						player.getInventory().delete(3062, 1);
						player.getPointsHandler().incrementboxPoints(1);
						player.sendMessage("You were lucky and got 1 Box Point, you now have @red@"
								+ player.getPointsHandler().getboxPoints() + " Box Points");
						ScavengeGain.MysteryBox(player);
					} else {
						player.getInventory().delete(3062, 1);
						player.sendMessage("You were unlucky n got nothing");
					}
				}
				break;
			case 13600: // Mind Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2796, 4818), TeleportType.NORMAL);
				break;
			case 13601: // Water Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2713, 4836), TeleportType.NORMAL);
				break;
			case 13602: // Earth Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2660, 4839), TeleportType.NORMAL);
				break;
			case 13603: // Fire Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2584, 4836), TeleportType.NORMAL);
				break;
			case 13604: // Body Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2527, 4833), TeleportType.NORMAL);
				break;
			case 13605: // Cosmic Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2162, 4833), TeleportType.NORMAL);
				break;
			case 13606: // Chaos Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2269, 4843), TeleportType.NORMAL);
				break;
			case 13607: // Nature Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2398, 4841), TeleportType.NORMAL);
				break;
			case 13608: // Law Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2464, 4834), TeleportType.NORMAL);
				break;
			case 13609: // Death Altar Tele
				TeleportHandler.teleportPlayer(player, new Position(2207, 4836), TeleportType.NORMAL);
				break;
			case 18809: // Rimmington Tele
				TeleportHandler.teleportPlayer(player, new Position(2957, 3214), TeleportType.NORMAL);
				break;
			case 18811: // Pollnivneach Tele
				TeleportHandler.teleportPlayer(player, new Position(3359, 2910), TeleportType.NORMAL);
				break;
			case 18812: // Rellekka Tele
				TeleportHandler.teleportPlayer(player, new Position(2659, 3661), TeleportType.NORMAL);
				break;
			case 18814: // Yanielle Tele
				TeleportHandler.teleportPlayer(player, new Position(2606, 3093), TeleportType.NORMAL);
				break;
			case 6542:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					ChristmasPresent.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 10025:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					CharmBox.open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 1959:
				TrioBosses.eatPumpkin(player);
				break;
			case 2677:
			case 2678:
			case 2679:
			case 2680:
			case 2681:
			case 2682:
			case 2683:
			case 2684:
			case 2685:
				ClueScrolls.giveHint(player, itemId);
				break;

			case 7956:
				if (player.getRights() == PlayerRights.DONATOR) {
					if (Misc.getRandom(25) == 5) {
						player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
					} else {
						player.getInventory().delete(7956, 1);
					}
				}
				if (player.getRights() == PlayerRights.SUPER_DONATOR || player.getRights() == PlayerRights.SUPPORT) {
					if (Misc.getRandom(20) == 5) {
						player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
					} else {
						player.getInventory().delete(7956, 1);
					}
				}
				if (player.getRights() == PlayerRights.EXTREME_DONATOR || player.getRights() == PlayerRights.MODERATOR) {
					if (Misc.getRandom(15) == 5) {
						player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
					} else {
						player.getInventory().delete(7956, 1);
					}
				}
				if (player.getRights() == PlayerRights.LEGENDARY_DONATOR
						|| player.getRights() == PlayerRights.ADMINISTRATOR) {
					if (Misc.getRandom(10) == 5) {
						player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
					} else {
						player.getInventory().delete(7956, 1);
					}
				}
				if (player.getRights() == PlayerRights.UBER_DONATOR || player.getRights() == PlayerRights.DEVELOPER
						|| player.getRights() == PlayerRights.DELUXE_DONATOR || player.getRights() == PlayerRights.VIP_DONATOR) {
					if (Misc.getRandom(5) == 2) {
						player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
					} else {
						player.getInventory().delete(7956, 1);
					}
				}


				if (player.getRights() == PlayerRights.PLAYER || player.getRights() == PlayerRights.YOUTUBER) {
					player.getInventory().delete(7956, 1);
				}


				int[] rewards = {10835, 10835, 10835, 10835, 10835, 10835, 10835, 10835, 10835, 15373, 15012, 536, 536, 536, //14
						19864, 19864, 19864, 2572, 11133, 10835, 10835, 10835, 10835, 10835, 10835, 10835, 10835, 10835};
				int[] rewardsAmount = {15, 16, 14, 14, 22, 17, 34, 12, 21, 1, 1, 15, 10, 20, 10, 25, //16
						50, 1, 1, 3, 3, 3, 2, 2, 2, 1, 1, 1};
				int rewardPos = Misc.getRandom(rewards.length - 1);
				player.getInventory().add(rewards[rewardPos],
						(int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
				break;

			// Donation Box
			case 6183:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					DonationBox.open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 18768:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					UltraDonationBox.open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 19901:
				int[] bravekCasketRewards = {3980, 3912, 15454, 15459, 15464, 15449, 15374, 18768, 3069, 3071, 3981, 5133,
						12434, 12435, 12436, 14012, 14013, 15373, 15372, 15371, 15370, 15369, 15449, 15454, 18782,
						18782, 18782, 18782, 18782, 18782, 18782, 18989, 14808, 14044, 1050, 1419,
						15426, 1053, 1055, 1057, 1046, 1048, 1044, 1040, 5130, 5130, 19040, 19040, 20000, 20000, 20001,
						20001, 20002, 20002, 6500, 6500, 6500, 6500, 6500, 13239, 13239, 13239, 13239, 13293};
				if (player.getInventory().contains(19901)) {
					player.getInventory().delete(19901, 1);
					player.getInventory().add(bravekCasketRewards[Misc.getRandom(bravekCasketRewards.length - 1)], 1);
					player.sendMessage("@blu@You open the casket, and get a reward.");
					ScavengeGain.MysteryBox(player);
				}

				break;
			case 3824:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					VoteMbox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;

			case 19122:
				if (player.getInventory().getFreeSlots() < 5) {
					player.sendMessage("@red@You need atleast 5 free inventory slots to open this scroll");
					return;
				}
				player.getInventory().add(5130, 1);
				player.getInventory().add(14012, 1);
				player.getInventory().add(14013, 1);
				player.getInventory().add(15373, 1);
				player.getInventory().add(15243, 1000);
				player.getInventory().delete(19122, 1);
				player.sendMessage("@blu@Thanks for redeeming Merk Stream Scroll, Enjoy the items!");
				break;

			case 10835:
				int valueOfTaxBag = 1000000000;
				if (player.getInventory().getAmount(995) >= Integer.MAX_VALUE - valueOfTaxBag) {
					player.getPacketSender().sendMessage("You can't have more than 2147m in your inventory.");
					return;
				}
				player.getInventory().delete(10835, 1);
				player.getInventory().add(995, valueOfTaxBag);
				break;

			case 4805:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					ChristmasMysteryBox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 14207:
				player.getInventory().delete(14207, 1);
				player.performAnimation(new Animation(829));
				player.performGraphic(new Graphic(1265));
				TaskManager.submit(new SpecialRegainPotionTask(player));
				player.sendMessage("@red@Enjoy the effect");
				break;
			case 4635:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					InfernalBox.openBox(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15374:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					SupremeMysteryBox.open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 13997:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
					EventBox.open(player);
					ScavengeGain.MysteryBox(player);
				}
				break;
			case 15375:
				if (player.getInventory().getFreeSlots() < 3) {
					player.sendMessage("You need atleast 3 free inventory slots to open this box.");
				} else {
				ChanceBox.open(player);
				ScavengeGain.MysteryBox(player);
		}
			break;
		case 14691:
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need atleast 3 free inventory slots to open this box.");
			} else {
				PetMysteryBox.open(player);
				ScavengeGain.MysteryBox(player);
			}
			break;
		// Clue Scroll
		case 2714:
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need atleast 3 free inventory slots to open this box.");
			} else {
				ClueScrolls.addClueRewards(player);
				ScavengeGain.MysteryBox(player);
			}
			break;

		case 15387:
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need atleast 3 free inventory slots to open this box.");
			} else {
				player.getInventory().delete(15387, 1);
				rewards = new int[]{1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
						347, 247};
				player.getInventory().add(rewards[RandomUtility.getRandom(rewards.length - 1)], 1);
				ScavengeGain.MysteryBox(player);
			}
			break;
		case 407:
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need atleast 3 free inventory slots to open this box.");
			} else {
				player.getInventory().delete(407, 1);
				if (RandomUtility.getRandom(3) < 3) {
					player.getInventory().add(409, 1);
					ScavengeGain.MysteryBox(player);
				} else if (RandomUtility.getRandom(4) < 4) {
					player.getInventory().add(411, 1);
					ScavengeGain.MysteryBox(player);
				} else
					player.getInventory().add(413, 1);
				ScavengeGain.MysteryBox(player);
			}
			break;
		case 405:
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need atleast 3 free inventory slots to open this box.");
			} else {
				player.getInventory().delete(405, 1);
				if (RandomUtility.getRandom(1) < 1) {
					int coins = RandomUtility.getRandom(100);
					player.getInventory().add(10835, coins);
					ScavengeGain.MysteryBox(player);
					player.getPacketSender().sendMessage("The casket contained " + coins + " coins!");
				} else
					player.getPacketSender().sendMessage("The casket was empty.");
			}
			break;
			
		case 15084:
			if(player.getTotalPlayTime() >= 36000000 * 3) {
			Gambling.rollDice(player);
			} else {
				player.sendMessage("@red@You need to play for atleast 30 hours before u can gamble");
				return;
			}
			break;
		case 299:
			if(player.getTotalPlayTime() >= 36000000 * 3) {
			Gambling.plantSeed(player);
			} else {
				player.sendMessage("@red@You need to play for atleast 50 hours before u can gamble");
			}
			break;
		case 4155:
			if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				return;
			}
			DialogueManager.start(player, SlayerDialogues.dialogue(player));
			break;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
		case 11842:
		case 11844:
			if (!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if (player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] { 10350, 10348, 10346, 10352 }
					: itemId == 11860 ? new int[] { 10334, 10330, 10332, 10336 }
							: itemId == 11862 ? new int[] { 10342, 10338, 10340, 10344 }
									: itemId == 11848 ? new int[] { 4716, 4720, 4722, 4718 }
											: itemId == 11856 ? new int[] { 4753, 4757, 4759, 4755 }
													: itemId == 11850 ? new int[] { 4724, 4728, 4730, 4726 }
															: itemId == 11854 ? new int[] { 4745, 4749, 4751, 4747 }
																	: itemId == 11852
																			? new int[] { 4732, 4734, 4736, 4738 }
																			: itemId == 11846
																					? new int[] { 4708, 4712, 4714,
																							4710 }
																					: itemId == 11842
																							? new int[] { 4087, 3140,
																									11335 }
																							: itemId == 11844
																									? new int[] { 4087,
																											14479,
																											11335 }
																									: new int[] {
																											itemId };

			if (player.getInventory().getFreeSlots() < items.length) {
				player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
				return;
			}
			player.getInventory().delete(itemId, 1);
			for (int i : items) {
				player.getInventory().add(i, 1);
			}
			player.getPacketSender().sendMessage("You open the set and find items inside.");
			player.getClickDelay().reset();
			break;
		case 952:
			Digging.dig(player);
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;

		case 11882:
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 6831:
			player.getInventory().delete(6831, 1);
			player.getInventory().add(14008, 1).refreshItems();
			player.getInventory().add(14009, 1).refreshItems();
			player.getInventory().add(14010, 1).refreshItems();
			break;
		case 6833:
			player.getInventory().delete(6833, 1);
			player.getInventory().add(14012, 1).refreshItems();
			player.getInventory().add(14013, 1).refreshItems();
			break;
		case 6830:
			player.getInventory().delete(6829, 1);
			player.getInventory().add(19727, 1).refreshItems();
			player.getInventory().add(19728, 1).refreshItems();
			player.getInventory().add(19729, 1).refreshItems();
			player.getInventory().add(19730, 1).refreshItems();
			player.getInventory().add(19731, 1).refreshItems();
			player.getInventory().add(19732, 1).refreshItems();
			break;
			
			
		case 19626:
			player.getInventory().delete(19626, 1);
			player.getInventory().add(7956, 2).refreshItems();
			player.getInventory().add(10835, 5).refreshItems();
			break;
			
			
			

		case 11884:
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11906:
			player.getInventory().delete(11906, 1);
			player.getInventory().add(7394, 1).refreshItems();
			player.getInventory().add(7390, 1).refreshItems();
			player.getInventory().add(7386, 1).refreshItems();
			break;
		case 15262:
			if (!player.getClickDelay().elapsed(1000))
				return;
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		if (itemId == OldRaidParty.MAGICAL_ORB && player.getClickDelay().elapsed(2000)) {
			int counter = 0;
			if (player.getOldRaidParty() != null) {
				player.sendMessage("You are already in a party");
				return;
			}
			player.setOldRaidParty(new OldRaidParty(player));
			player.sendMessage("We made an party for you!");
			return;
		}
		switch (itemId) {

		case 995:
			MoneyPouch.depositTaxBag(player, player.getInventory().getAmount(995), false, 995);
		break;

			/**case 6199:
			case 15373:
			case 15375:
			case 15374:
			case 13997:
			case 4635:
			case 3824:
				//TODO open all
				if (player.getInventory().getFreeSlots() < 4) {
					int amount = player.getInventory().getAmount(itemId);
					player.getInventory().delete(player.getInventory().getById(itemId));
					player.getPacketSender().sendMessage("You've opened " + amount + " of box's.");
					player.getMysteryBox().openInterface();
					return;
				} else {
				//canny open box
					player.sendMessage("You don't have enough inventory space to open those.");
				}
				break;**/

		case 6500:
			if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
				player.getPacketSender().sendMessage("You cannot configure this right now.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 101);
			player.setDialogueActionId(60);
			break;
		case 12926:
			player.getBlowpipeLoading().handleUnloadBlowpipe();
			break;
		case 11724:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 250);
				player.getInventory().add(897, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
			}
			break;
		case 11726:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 250);
				player.getInventory().add(894, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Tassets@la@ to tier 1!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 250 Upgrade Tokens To Do This");
			}
			break;
		case 894:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 500);
				player.getInventory().add(895, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
			}
			break;
		case 895:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 1000);
				player.getInventory().add(896, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Tassets@la@ to tier 3!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
			}
			break;
		case 897:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 500);
				player.getInventory().add(898, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 2!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 500 Upgrade Tokens To Do This");
			}
			break;
		case 898:
			if (player.getInventory().contains(6530)) {
				player.getInventory().delete(6530, 1000);
				player.getInventory().add(899, 1);
				World.sendMessageNonDiscord("<img=12> <col=008FB2>[Upgrade]" + player.getUsername()
						+ " has just upgraded his @red@Bandos Chestplate@la@ to tier 3!");
				player.getPacketSender().sendMessage("Your Bandos Items Has been Upgraded");
			} else {
				player.getPacketSender().sendMessage("@red@You Need 1000 Upgrade Tokens To Do This");
			}
			break;
			
			

		case 1712:
		case 1710:
		case 1708:
		case 1706:
		case 11118:
		case 11120:
		case 11122:
		case 11124:
			JewelryTeleporting.rub(player, itemId);
			break;
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getSlayer().handleSlayerRingTP(itemId);
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;

		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		@SuppressWarnings("unused")
		int interfaceId = packet.readLEShortA();

		if (player.getRights().equals(PlayerRights.DEVELOPER) && GameSettings.DEBUG_MODE) {
			player.getPacketSender().sendMessage("[3RD ACTINO CLICK] ItemID: " + itemId + " Slot: " + slot + " InterfaceID: " + interfaceId);
		}
		if (slot < 0 || slot > player.getInventory().capacity())
			return;
		if (player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if (ItemBinding.isBindable(itemId)) {
			ItemBinding.bindItem(player, itemId);
			return;
		}
		switch (itemId) {
			case 18391: //iPhone minime
				MiniMeFunctions.create(player);
				break;
		//case 14019:
		case 14022:
			player.setCurrentCape(itemId);
			int[] colors = itemId == 14019 ? player.getMaxCapeColors() : player.getCompCapeColors();
			String[] join = new String[colors.length];
			for (int i = 0; i < colors.length; i++) {
				join[i] = Integer.toString(colors[i]);
			}
			player.getPacketSender().sendString(60000, "[CUSTOMIZATION]" + itemId + "," + String.join(",", join));
			player.getPacketSender().sendInterface(60000);
			break;

		/*
		 * case 1050: player.setCurrentHat(itemId); int[] santaColors =
		 * player.getSantaColors(); String[] santaJoin = new String[santaColors.length];
		 * for(int i = 0; i < santaColors.length; i++) { santaJoin[i] =
		 * Integer.toString(santaColors[i]); }
		 * 
		 * player.getPacketSender().sendInterface(58000);
		 * //System.out.println("Current santa colors: " +
		 * Arrays.toString(player.getSantaColors())); break;
		 */

		case 10835:
			MoneyPouch.depositTaxBag(player, player.getInventory().getAmount(10835), false, 10835);
			break;

		case 12926:
			player.getBlowpipeLoading().handleCheckBlowpipe();
			break;
			/**
			 *
			 * Open all - Mystery Box's
			 * @Author oogle
			 *
			 */
			case 6199:
				int[] common = { 19721,19722,19723,19734,19736,19468,18363,15398,15418};
				int[] uncommon = { 9006,3941,3974,18392,4799,4800,4801 };
				int[] veryUncommon = { 15012,1499,3951,5079,5187,5186,3316,3931,14559,6583 };
				player.getMysteryBoxOpener().setOpenBox(6199);
				player.getMysteryBoxOpener().display(6199, "Advanced Box", common, uncommon, veryUncommon);
				player.getMysteryBoxOpener().openAll(player.getMysteryBoxOpener().getOpenBox());
				ScavengeGain.MysteryBox(player);
				break;
		case 15373:
			int[] common2 = { 2577, 902, 903, 904, 905,3082 };
			int[] uncommon2 = {2577, 902, 903, 904, 905,3082, 19892,3956,3928,17908,17909,3932,4775 };
			int[] veryUncommon2 = { 20016,20017,20018,20021,20022,17911,19892,3956,3928,17908,17909,3932,4775, 19137, 19138, 19139, 18940,18942, 18941 };
			player.getMysteryBoxOpener().setOpenBox(15373);
			player.getMysteryBoxOpener().display(15373, "Starter Box", common2, uncommon2, veryUncommon2);
			player.getMysteryBoxOpener().openAll(player.getMysteryBoxOpener().getOpenBox());
			ScavengeGain.MysteryBox(player);
			break;

			case 4635:
				int[] badRewards = { 3459, 15398,18782};
				int[] goodRewards = { 15357, 15361,15362 };
				int[] bestRewards = { 15358, 15359, 15363, 19890,3317,4780, 19935, 19936,19140};
				player.getMysteryBoxOpener().setOpenBox(4635);
				player.getMysteryBoxOpener().display(4635, "Infernal Box", badRewards, goodRewards, bestRewards);
				player.getMysteryBoxOpener().openAll(player.getMysteryBoxOpener().getOpenBox());
				ScavengeGain.MysteryBox(player);
				break;

			case 3824:
				int[] badRewards2 = {15373,18950};
				int[] goodRewards2 = {18380,18392,18381,18382,18383,18384,18385,3937,3938,3939,3944,3945,3946,3947,3948};
				int[] bestRewards2 = {20054,19140,5185,5163,19886};
				player.getMysteryBoxOpener().setOpenBox(3824);
				player.getMysteryBoxOpener().display(3824, "Vote Box", badRewards2, goodRewards2, bestRewards2);
				player.getMysteryBoxOpener().openAll(player.getMysteryBoxOpener().getOpenBox());
				ScavengeGain.MysteryBox(player);
				break;

			case 15375:
				ChanceBox.openAll(player, 15375);
				ScavengeGain.MysteryBox(player);
				break;

			case 13997:
				EventBox.openAll(player, 13997);
				ScavengeGain.MysteryBox(player);
				break;

			case 15374:
				SupremeMysteryBox.openAll(player, 15374);
				ScavengeGain.MysteryBox(player);
				break;

			case 19670:
			if (player.getInventory().contains(19670) && player.getClickDelay().elapsed(700)) {
				int amt = player.getInventory().getAmount(19670);
				int pointAmount = 1;
				player.getInventory().delete(19670, amt);
				player.getPointsHandler().incrementVotingPoints(amt * pointAmount);
				player.getPointsHandler().refreshPanel();
				player.getPacketSender().sendMessage("You claim the " + (amt > 1 ? "scrolls" : "scroll")
						+ " and receive your reward including " + pointAmount * amt + " Vote Points");
				StarterTasks.finishTask(player, StarterTasks.StarterTaskData.REDEEM_A_VOTE_SCROLL);
				int chance = RandomUtility.exclusiveRandom(1000);
				if (chance >= 750) {
					player.getInventory().add(3824, 1);
					World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> " + player.getUsername().toString()
							+ " @red@Has just received a Vote Mystery Box!");
				}
				if (chance >= 980) {
					player.getInventory().add(19936, 1);
					World.sendMessageNonDiscord("<img=12>@blu@[WORLD]<img=12> " + player.getUsername().toString()
							+ " @red@Has just received a $10 Bond from voting!");
				}
				player.getClickDelay().reset();
			}
			break;
		case 9952:
			CharmingImp.sendConfig(player);
			break;
		case 4155:
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 103);
			player.setDialogueActionId(65);
			break;

		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
					? ("You do not have a Slayer task.")
					: ("Your current task is to kill another " + (player.getSlayer().getAmountToSlay()) + " "
							+ Misc.formatText(
									player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))
							+ "s."));
			break;
		case 6570:
			if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
				player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
		case 8655:
			if (player.getInventory().contains(10835) && player.getInventory().getAmount(10835) >= 500) {
				player.getInventory().delete(8655, 1).delete(10835, 500).add(8656, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 500 1b coins to Operate the Bow into a Staff");
			}
			break;
		case 8656:
			if (player.getInventory().contains(10835) && player.getInventory().getAmount(10835) >= 500) {
				player.getInventory().delete(8656, 1).delete(10835, 500).add(8654, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 500 1b coins to Operate the Staff into a Sword");
			}
			break;
		case 8654:
			if (player.getInventory().contains(10835) && player.getInventory().getAmount(10835) >= 500) {
				player.getInventory().delete(8654, 1).delete(10835, 100000).add(8655, 1);
				player.getPacketSender().sendMessage("You have Successfully Switched your Imbued Weapon");
			} else {
				player.getPacketSender().sendMessage(
						"You need at least 100k 1b coins to Operate the Sword into a Bow");
			}
			break;
			
			//IMBUED LEGS
			
		case 4063:
			if (player.getInventory().contains(4063) && player.getInventory().getAmount(4063) >= 1) {
				player.getInventory().delete(4063, 1).add(4060, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Legs");

			}
			break;
			
		case 4060:
			if (player.getInventory().contains(4060) && player.getInventory().getAmount(4060) >= 1) {
				player.getInventory().delete(4060, 1).add(4063, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
			
			//END IMBUED LEGS
			//START IMBUED BODY
		case 4061:
			if (player.getInventory().contains(4061) && player.getInventory().getAmount(4061) >= 1) {
				player.getInventory().delete(4061, 1).add(4064, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
		case 4064:
			if (player.getInventory().contains(4064) && player.getInventory().getAmount(4064) >= 1) {
				player.getInventory().delete(4064, 1).add(4085, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
		case 4085:
			if (player.getInventory().contains(4085) && player.getInventory().getAmount(4085) >= 1) {
				player.getInventory().delete(4085, 1).add(4061, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Body");

			}
			break;
			//END IMBUED BODY
			//START IMBUED BOOTS
		case 4062:
			if (player.getInventory().contains(4062) && player.getInventory().getAmount(4062) >= 1) {
				player.getInventory().delete(4062, 1).add(4065, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
			
		case 4065:
			if (player.getInventory().contains(4065) && player.getInventory().getAmount(4065) >= 1) {
				player.getInventory().delete(4065, 1).add(4555, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
			
		case 4555:
			if (player.getInventory().contains(4555) && player.getInventory().getAmount(4555) >= 1) {
				player.getInventory().delete(4555, 1).add(4062, 1);
				player.getPacketSender().sendMessage("You have Switched your Imbued Boots");

			}
			break;
		case 6828:
			player.getInventory().delete(6828, 1);
			player.getInventory().add(8654, 1).refreshItems();
			player.getInventory().add(4060, 1).refreshItems();
			player.getInventory().add(4061, 1).refreshItems();
			player.getInventory().add(4062, 1).refreshItems();
			break;
			
		case 12162:
			player.incrementNPCKills(1000);
			player.getInventory().delete(12162, 1);
			player.sendMessage("1K Total NPC Killcount has been Added to your account");
			break;
			
		case 12164:
			player.incrementNPCKills(5000);
			player.getInventory().delete(12164, 1);
			player.sendMessage("5K Total NPC Killcount has been Added to your account");
			break;

			
			//END IMBUED BOOTS
			
			
			
			
			
		case 15262:
			if (!player.getClickDelay().elapsed(1300))
				return;
			int amt = player.getInventory().getAmount(15262);
			if (amt > 0)
				player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
		case 11283: // DFS
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
			break;
		case 11613: // dkite
			player.getPacketSender()
					.sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}