package com.zamron.net.packet.impl;
import com.zamron.world.content.*;
import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.WalkToTask;
import com.zamron.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zamron.model.Animation;
import com.zamron.model.Direction;
import com.zamron.model.DwarfCannon;
import com.zamron.model.Flag;
import com.zamron.model.GameObject;
import com.zamron.model.Graphic;
import com.zamron.model.MagicSpellbook;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.Prayerbook;
import com.zamron.model.Skill;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.GameObjectDefinition;
import com.zamron.model.input.impl.DonateToWell;
import com.zamron.model.input.impl.EnterAmountOfLogsToAdd;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.clip.region.RegionClipping;
//import com.platinum.world.content.CrystalChest;
import com.zamron.world.content.combat.bossminigame.BossMinigameFunctions;
import com.zamron.world.content.combat.bossminigame.BossRewardChest;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.combat.range.DwarfMultiCannon;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.grandexchange.GrandExchange;
import com.zamron.world.content.minigames.impl.*;
import com.zamron.world.content.minigames.impl.Dueling.DuelRule;
import com.zamron.world.content.minigames.impl.gungame.GunGame;
import com.zamron.world.content.partyroom.PartyRoomManager;
import com.zamron.world.content.raids.Raid3;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.raids.addons.RaidChest;
import com.zamron.world.content.skill.impl.AfkSkilling;
import com.zamron.world.content.skill.impl.agility.Agility;
import com.zamron.world.content.skill.impl.construction.Construction;
import com.zamron.world.content.skill.impl.crafting.Flax;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.content.skill.impl.fishing.Fishing;
import com.zamron.world.content.skill.impl.fishing.Fishing.Spot;
import com.zamron.world.content.skill.impl.hunter.Hunter;
import com.zamron.world.content.skill.impl.hunter.PuroPuro;
import com.zamron.world.content.skill.impl.mining.Mining;
import com.zamron.world.content.skill.impl.mining.MiningData;
import com.zamron.world.content.skill.impl.mining.Prospecting;
import com.zamron.world.content.skill.impl.runecrafting.Runecrafting;
import com.zamron.world.content.skill.impl.runecrafting.RunecraftingData;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.content.skill.impl.smithing.EquipmentMaking;
import com.zamron.world.content.skill.impl.smithing.Smelting;
import com.zamron.world.content.skill.impl.thieving.Stalls;
import com.zamron.world.content.skill.impl.woodcutting.Woodcutting;
import com.zamron.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.zamron.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.content.transportation.TeleportType;
import com.zamron.world.content.trickortreat.TrickOrTreat;
import com.zamron.world.content.trickortreat.TrickOrTreatData;
import com.zamron.world.entity.impl.npc.minigame.KeyRoom;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.teleportinterface.TeleportInterface;
import com.zamron.world.teleportinterface.TeleportInterface.Slayers;

/**
 * This packet listener is called when a player clicked
 * on a game object.
 * 
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

	/**
	 * The PacketListener logger to debug information and print out errors.
	 */
	//private final static Logger logger = Logger.getLogger(ObjectActionPacketListener.class);

	private static void firstClick(final Player player, Packet packet) {
		final int x = packet.readLEShortA();
		final int id = packet.readUnsignedShort();
		final int y = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(player.getLocation() != Location.CONSTRUCTION) {
			if(id > 0 && id != 6 && !RegionClipping.objectExists(gameObject) && id != 9294) {
				//	player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX() : GameObjectDefinition.forId(id).getSizeY();
		if (size <= 0)
			size = 1;
		gameObject.setSize(size);
		
		if (CustomObjects.getRaidChest(position) != null) {
			player.sendMessage("Opening raid chest...");
			RaidChest raidChest = (RaidChest) CustomObjects.getRaidChest(position);
			raidChest.setSize(size);
			player.setWalkToTask(new WalkToTask(player, position, raidChest.getSize(), new FinalizedMovementTask() {
				@Override
				public void execute() {
					/*
					 * Player controller
					 */
					if (raidChest.getRaid() instanceof Raid3) {
						raidChest.claimRewardWithPercent(player);
					} else {
						raidChest.claimReward(player);
					}
				}
			}));

			return;
		}
		
		if(player.getMovementQueue().isLockMovement())
			return;

		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("First click object id; [id, position] : [" + id + ", " + position.toString() + "]");

		player.setInteractingObject(gameObject).setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				player.setPositionToFace(gameObject.getPosition());

				if(player.getRegionInstance() != null) {
					Construction.handleFifthObjectClick(x, y, id, player);
					
				}
				if(WoodcuttingData.Trees.forId(id) != null) {
					Woodcutting.cutWood(player, gameObject, false);
					return;
				}
				if(MiningData.forRock(gameObject.getId()) != null) {
					Mining.startMining(player, gameObject);
					return;
				}
				if (player.getFarming().click(player, x, y, 1))
					return;
				if(Runecrafting.runecraftingAltar(player, gameObject.getId())) {
					RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
					if(rune == null)
						return;
					Runecrafting.craftRunes(player, rune);
					return;
				}
				if(Agility.handleObject(player, gameObject)) {
					return;
				}
				if (id == DonationChests.lowTierChestID
						|| id == DonationChests.mediumTierChestID
						|| id == DonationChests.highTierChestID) {
					DonationChests.handleChestClick(id, player);
				}
				if(Barrows.handleObject(player, gameObject)) {
					return;
				}
				if (id == BossMinigameFunctions.STAIRS_ENTRY_ID) {
					BossMinigameFunctions.handleDoor(player);
				}
				if (id == BossMinigameFunctions.EXIT_CAVE_ID) {
					BossMinigameFunctions.handleExit(player);
				}
				/** Here we handle opening the reward chest at ::boss **/
				if (id == BossRewardChest.rewardChestID) {
					BossRewardChest.clickChest(player);
				}
				if(player.getLocation() == Location.WILDERNESS && WildernessObelisks.handleObelisk(gameObject.getId())) {
					return;
				}
				if (TrickOrTreat.currentLocation != null && id == TrickOrTreat.currentLocation.doorID) {
						TrickOrTreat.knockDoor(player, id);
				}
				switch(id) {


					/**
					 * H'ween teleport
					 */
					case TrickOrTreatData.portalID:
						TeleportHandler.startTeleportCity(player);
						break;

					/** AFK Objects **/
					case 28750:
						player.getPacketSender().sendRichPresenceState("AFK Hunting");
						AfkSkilling.afkSkilling(player, 250, Skill.HUNTER, 827);
						break;
					case 49522:
						player.getPacketSender().sendRichPresenceState("AFK Fishing");
						AfkSkilling.afkSkilling(player, 250, Skill.FISHING, 623);
						break;
					case 8381:
						player.getPacketSender().sendRichPresenceState("AFK Woodcutting");
						AfkSkilling.afkSkilling(player, 250, Skill.WOODCUTTING, 10227);
						break;
					case 15931:
						player.getPacketSender().sendRichPresenceState("AFK Fletching");
						AfkSkilling.afkSkilling(player, 250, Skill.FLETCHING, 1248);
						break;
					case 40951:
						player.getPacketSender().sendRichPresenceState("AFK Thieving");
						AfkSkilling.afkSkilling(player, 250, Skill.THIEVING, 881);
						break;
					case 36959:
						player.getPacketSender().sendRichPresenceState("AFK Combat");
						AfkSkilling.afkCombat(player, 250);
						break;
					case 619:
						player.getPacketSender().sendRichPresenceState("AFK Herblore");
						AfkSkilling.afkSkilling(player, 250, Skill.HERBLORE, 363);
						break;
					case 61334:
						player.getPacketSender().sendRichPresenceState("AFK Crafting");
						AfkSkilling.afkSkilling(player, 250, Skill.CRAFTING, 896);
						break;
					case 565:
						player.getPacketSender().sendRichPresenceState("AFK Praying");
						AfkSkilling.afkSkilling(player, 250, Skill.PRAYER, 645);
						break;
					case 50051:
						player.getPacketSender().sendRichPresenceState("AFK Farming");
						AfkSkilling.afkSkilling(player, 250, Skill.FARMING, 2275);
						break;
					case 9682:
						player.getPacketSender().sendRichPresenceState("AFK Cooking");
						AfkSkilling.afkSkilling(player, 250, Skill.COOKING, 896);
						break;



					case 115:
					case 116:
					case 117:
					case 118:
					case 119:
					case 120:
					case 121:
					case 122:
						PartyRoomManager.burstBalloon(player, gameObject);
						break;
					case PartyRoomManager.LEVER:
						PartyRoomManager.sendLever(player);
						break;
					case PartyRoomManager.CHEST:
						PartyRoomManager.open(player);
						break;


				case 2466:
				case 2467:
				case 12120:
					KeyRoom.handleObjectClick(player, gameObject, 1);
					return;
					case 9391:
						player.getPacketSender().sendInterface(23500);
						break;



					case 10817:
						GunGame.start(player);
						break;
				case 38660:
					if(ShootingStar.CRASHED_STAR != null) {

					}
					break;
				case 2468:
					player.setDialogueActionId(776);
					DialogueManager.start(player, 180);
					break;
				case 42611: // KH raid portal
					player.sendMessage("Disabled - Currently being worked on.");
					break;
				case 2469:
					//TeleportHandler.teleportPlayer(player, new Position(2582, 4609), player.getSpellbook().getTeleportType());
					//player.sendMessage("@red@Get the key from the NPCS to advance to room 2");
					player.getPacketSender().sendMessage("@red@Disabled.");
					break;
					
				case 11231:
					if(player.getInventory().contains(19104)) {
					
					int[] lmsRewards = {14415, 5148, 11694, 4151, 13896, 13887, 13893, 14009, 14010, 14008, 15272, 15272, 15272, 15272, 13899, 3903, 10499, 11848, 11850, 11854, 14484, 15300, 18349, 18353, 18351, 3886, 3902};
					player.getInventory().delete(19104, 1);
					player.getInventory().add(lmsRewards[Misc.getRandom(lmsRewards.length - 1)], 1);
					} else {
						player.sendMessage("You need a LMS key to open this chest.");
						return;
					}
					break;
					
				case 2470:
					LastManStanding.enterLobby(player);
					break;
					
				case 52:
				case 53:
					if (player.getInventory().contains(19095) 
							|| player.getRights() == PlayerRights.VIP_DONATOR 
							||player.getRights() == PlayerRights.DELUXE_DONATOR || player.getRights().isSeniorStaff()) {
						player.getInventory().delete(19095, 1);
						TeleportHandler.teleportPlayer(player, new Position(2582, 4622, 0),
								player.getSpellbook().getTeleportType());
						player.sendMessage("@red@You successfully open the gate with the key [Or without if your Deluxe Donator]");
					} else {
						player.sendMessage(
								"@red@You don't have the Room 1 key, which is needed to open this gate.");
						return;
					}
					break;
				case 11434:
					if (EvilTrees.SPAWNED_TREE != null) {

					}
					break;
					
				case 27330:
					player.performAnimation(new Animation(645));
					if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
							.getMaxLevel(Skill.PRAYER)) {
						player.getSkillManager().setCurrentLevel(Skill.PRAYER,
								player.getSkillManager().getMaxLevel(Skill.PRAYER), true);

						if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player.getSkillManager()
								.getMaxLevel(Skill.CONSTITUTION)) {
							player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
									player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
							player.sendMessage("@red@You feel Rejuvinated!");

						}
					}
					else;
					TeleportInterface.sendSlayerData(player, TeleportInterface.Slayers.BOWSER);
					TeleportInterface.sendBossTab(player);
					break;
					
				case 2079:
					TrioBosses.openChest(player);
					break;
				case 2477:
					if (player.getInventory().contains(19951)) {
						player.getInventory().delete(19951, 1);
						TeleportHandler.teleportPlayer(player, new Position(2511, 3802, 0),
								player.getSpellbook().getTeleportType());
						player.sendMessage("@red@You successfully teleport to the next zone");
					} else {
						player.sendMessage(
								"@red@You don't have the Stage 1 key.");
						return;
					}
					break;
					case 2476:
						if (player.getInventory().contains(19952)) {
							player.getInventory().delete(19952, 1);
							TeleportHandler.teleportPlayer(player, new Position(2511, 3785, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 2 key.");
							return;
						}
						break;
					case 2473:
						if (player.getInventory().contains(19953)) {
							player.getInventory().delete(19953, 1);
							TeleportHandler.teleportPlayer(player, new Position(2529, 3820, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 3 key.");
							return;
						}
						break;
					case 2503:
						if (player.getInventory().contains(19954)) {
							player.getInventory().delete(19954, 1);
							TeleportHandler.teleportPlayer(player, new Position(2529, 3802, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 4 key.");
							return;
						}
						break;
					case 2475:
						if (player.getInventory().contains(19955)) {
							player.getInventory().delete(19955, 1);
							TeleportHandler.teleportPlayer(player, new Position(2547, 3819, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 5 key.");
							return;
						}
						break;
					case 2504:
						if (player.getInventory().contains(19956)) {
							player.getInventory().delete(19956, 1);
							TeleportHandler.teleportPlayer(player, new Position(2547, 3802, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 6 key.");
							return;
						}
						break;
					case 2474:
						if (player.getInventory().contains(19961)) {
							player.getInventory().delete(19961, 1);
							TeleportHandler.teleportPlayer(player, new Position(2547, 3785, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 7 key.");
							return;
						}
						break;
					case 2472:
						if (player.getInventory().contains(19958)) {
							player.getInventory().delete(19958, 1);
							TeleportHandler.teleportPlayer(player, new Position(2218, 3292, 0),
									player.getSpellbook().getTeleportType());
							player.sendMessage("@red@You successfully teleport to the next zone");
						} else {
							player.sendMessage(
									"@red@You don't have the Stage 8 key.");
							return;
						}
						break;
				case 2465:
					player.inFFALobby = false;
					player.moveTo(new Position(3208,3426,0));
					FreeForAll.removePlayer(player);
					return;
					
				case 5259:
					if(player.getPosition().getX() >= 3653) {
						player.moveTo(new Position(3652, player.getPosition().getY()));
					} else {
						player.setDialogueActionId(73);
						DialogueManager.start(player, 115);
					}
					break;
				case 359:
					if(!player.getClickDelay().elapsed(30000)) {
						player.sendMessage("You can only scavenge this box every 30 seconds!");
						return;
					}
					ScavengeGain.EasyBox(player);
					player.getClickDelay().reset();
					break;
				case 360:
					if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 55) {
						player.sendMessage("You can only scavenge this box with a Scavenging level of 55+");
						return;
					}
					if(!player.getClickDelay().elapsed(30000)) {
						player.sendMessage("You can only scavenge this box every 30 seconds!");
						return;
					}
					ScavengeGain.MediumBox(player);
					player.getClickDelay().reset();
					break;
				case 361:
					if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 80) {
						player.sendMessage("You can only scavenge this box with a Scavenging level of 80+");
						return;
					}
					if(!player.getClickDelay().elapsed(30000)) {
						player.sendMessage("You can only scavenge this box every 30 seconds!");
						return;
					}
					ScavengeGain.HardBox(player);
					player.getClickDelay().reset();
					break;
				case 13405:
					if(!TeleportHandler.checkReqs(player, null)) {
						return;
					}
					if(!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement()) {
						return;
					}
					if(player.getLocation() == Location.CONSTRUCTION) {
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
						return;
					}
					Construction.newHouse(player);
					Construction.enterHouse(player, player, true, true);
					player.getPacketSender().sendMessage("@red@If your construction area map bugs out, teleport home and back in!");
					break;
				case 8799:
			        GrandExchange.open(player);
			        break;
				case 21505:
				case 21507:
					player.moveTo(new Position(2329, 3804));
					break;
				case 38700:
					player.moveTo(new Position(3092, 3502));
					break;
				case 45803:
				case 1767:
					DialogueManager.start(player, 114);
					player.setDialogueActionId(72);
					break;
				case 7352:
					if(Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
						player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getGatestonePosition());
						player.setEntityInteraction(null);
						player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
						player.performGraphic(new Graphic(1310));
					} else
						player.getPacketSender().sendMessage("Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
					break;
				case 7353:
					player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
					break;
				case 7321:
					player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
					break;
				case 7322:
					player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
					break;
				case 7315:
					player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
					break;
				case 7316:
					player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
					break;
				case 7318:
					player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
					break;
				case 7319:
					player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
					break;
				case 7324:
					player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
					break;
				case 47180:
					
					player.getPacketSender().sendMessage("You activate the device..");
					player.moveTo(new Position(2586, 3912));
					break;
				case 10091:
				case 8702:
					
					Fishing.setupFishing(player, Spot.ROCKTAIL);
					break;
				case 9319:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61){
						player.getPacketSender().sendMessage("You need an Agility level of at least 61 or higher to climb this");
						return;
					}
					if(player.getPosition().getZ() == 0)
						player.moveTo(new Position(3422, 3549, 1));
					else if(player.getPosition().getZ() == 1) {
						if(gameObject.getPosition().getX() == 3447) 
							player.moveTo(new Position(3447, 3575, 2));
						else
							player.moveTo(new Position(3447, 3575, 0));
					}
					break;
					
					


				case 9320:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 61 or higher to climb this");
						return;
					}
					if(player.getPosition().getZ() == 1)
						player.moveTo(new Position(3422, 3549, 0));
					else if(player.getPosition().getZ() == 0)
						player.moveTo(new Position(3447, 3575, 1));
					else if(player.getPosition().getZ() == 2)
						player.moveTo(new Position(3447, 3575, 1));
					player.performAnimation(new Animation(828));
					break;
				case 2274:
					if(gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
						player.moveTo(new Position(2914, 5300, 1));
					} else if(gameObject.getPosition().getX() == 2914 && gameObject.getPosition().getY() == 5300) {
						player.moveTo(new Position(2912, 5300, 2));
					} else if(gameObject.getPosition().getX() == 2919 && gameObject.getPosition().getY() == 5276) {
						player.moveTo(new Position(2918, 5274));
					} else if(gameObject.getPosition().getX() == 2918 && gameObject.getPosition().getY() == 5274) {
						player.moveTo(new Position(2919, 5276, 1));
					} else if(gameObject.getPosition().getX() == 3001 && gameObject.getPosition().getY() == 3931 || gameObject.getPosition().getX() == 3652 && gameObject.getPosition().getY() == 3488) {
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
						player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
					}
					break;
				case 7836:
				case 7808:
					int amt = player.getInventory().getAmount(6055);
					if(amt > 0) {
						player.getInventory().delete(6055, amt);
						player.getPacketSender().sendMessage("You put the weed in the compost bin.");
						player.getSkillManager().addExperience(Skill.FARMING, 20*amt);
					} else {
						player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
					}
					break;
				case 5960: //Levers
				case 5959:
					player.setDirection(Direction.WEST);
					TeleportHandler.teleportPlayer(player, new Position(3090, 3475), TeleportType.LEVER);
					break;
				case 5096:
					if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
						player.moveTo(new Position(2649, 9591));
					break;

				case 5094:
					if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
						player.moveTo(new Position(2643, 9594, 2));
					break;

				case 5098:
					if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
						player.moveTo(new Position(2637, 9517));
					break;

				case 5097:
					if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
						player.moveTo(new Position(2636, 9510, 2));
					break;
				case 26428:
				case 26426:
				case 26425:
				case 26427:
					String bossRoom = "Armadyl";
					boolean leaveRoom = player.getPosition().getY() > 5295;
					int index = 0;
					Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
					if(id == 26425) {
						bossRoom = "Bandos";
						leaveRoom = player.getPosition().getX() > 2863;
						index = 1;
						movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
					} else if(id == 26427) {
						bossRoom = "Saradomin";
						leaveRoom = player.getPosition().getX() < 2908;
						index = 2;
						movePos = new Position(leaveRoom ? 2908 : 2907, 5265);
					} else if(id == 26428) {
						bossRoom = "Zamorak";
						leaveRoom = player.getPosition().getY() <= 5331;
						index = 3;
						movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
					}
					if(!leaveRoom && (player.getRights() != PlayerRights.ADMINISTRATOR && player.getRights() != PlayerRights.OWNER && player.getRights() != PlayerRights.UBER_DONATOR && player.getRights() != PlayerRights.LEGENDARY_DONATOR && player.getRights() != PlayerRights.EXTREME_DONATOR && player.getRights() != PlayerRights.SUPER_DONATOR && player.getRights() != PlayerRights.MODERATOR && player.getRights() != PlayerRights.DELUXE_DONATOR && player.getRights() != PlayerRights.SUPPORT && player.getRights() != PlayerRights.DEVELOPER && player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] < 20)) {
						player.getPacketSender().sendMessage("You need "+Misc.anOrA(bossRoom)+" "+bossRoom+" killcount of at least 20 to enter this room.");
						return;
					}
					player.moveTo(movePos);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(leaveRoom ? false : true);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
					player.getPacketSender().sendString(16216+index, "0");
					break;
				case 26289:
				case 26286:
				case 26288:
				case 26287:
					if(System.currentTimeMillis() - player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
						player.getPacketSender().sendMessage("");
						player.getPacketSender().sendMessage("You can only pray at a God's altar once every 10 minutes.");
						player.getPacketSender().sendMessage("You must wait another "+(int)((600 - (System.currentTimeMillis() - player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))+" seconds before being able to do this again.");
						return;
					}
					int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false) : id == 26286 ? Equipment.getItemCount(player, "Zamorak", false) : id == 26288 ? Equipment.getItemCount(player, "Armadyl", false) : id == 26287 ? Equipment.getItemCount(player, "Saradomin", false) : 0;
					int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
						player.getPacketSender().sendMessage("You do not need to recharge your Prayer points at the moment.");
						return;
					}
					player.performAnimation(new Animation(645));
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(System.currentTimeMillis());
					break;
				case 23093:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 70 to go through this portal.");
						return;
					}
					if(!player.getClickDelay().elapsed(2000)) 
						return;
					int plrHeight = player.getPosition().getZ();
					if(plrHeight == 2)
						player.moveTo(new Position(2914, 5300, 1));
					else if(plrHeight == 1) {
						int x = gameObject.getPosition().getX();
						int y = gameObject.getPosition().getY();
						if(x == 2914 && y == 5300)
							player.moveTo(new Position(2912, 5299, 2));
						else if(x == 2920 && y == 5276)
							player.moveTo(new Position(2920, 5274, 0));
					} else if(plrHeight == 0)
						player.moveTo(new Position(2920, 5276, 1));
					player.getClickDelay().reset();
					break;
				case 26439:
					if(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
						player.getPacketSender().sendMessage("You need a Constitution level of at least 70 to swim across.");
						return;
					}
					if(!player.getClickDelay().elapsed(1000))
						return;
					if(player.isCrossingObstacle())
						return;
					final String startMessage = "You jump into the icy cold water..";
					final String endMessage = "You climb out of the water safely.";
					final int jumpGFX = 68;
					final int jumpAnimation = 772;
					player.setSkillAnimation(773);
					player.setCrossingObstacle(true);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.performAnimation(new Animation(3067));
					final boolean goBack2 = player.getPosition().getY() >= 5344;
					player.getPacketSender().sendMessage(startMessage);  
					player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
					player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
					player.performGraphic(new Graphic (jumpGFX));
					player.performAnimation(new Animation(jumpAnimation));
					TaskManager.submit(new Task(1, player, false) {
						int ticks = 0;
						@Override
						public void execute() {
							ticks++;
							player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
							if(ticks >= 10)
								stop();
						}
						@Override
						public void stop() {
							player.setSkillAnimation(-1);
							player.setCrossingObstacle(false);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
							player.getPacketSender().sendMessage(endMessage);
							player.moveTo(new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
							setEventRunning(false);
						}
					});
					player.getClickDelay().reset((System.currentTimeMillis() + 9000));
					break;
				case 26384:
					if(player.isCrossingObstacle())
						return;
					if(!player.getInventory().contains(2347)) {
						player.getPacketSender().sendMessage("You need to have a hammer to bang on the door with.");
						return;
					}
					player.setCrossingObstacle(true);
					final boolean goBack = player.getPosition().getX() <= 2850;
					player.performAnimation(new Animation(377));
					TaskManager.submit(new Task(2, player, false) {
						@Override
						public void execute() {
							player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
							player.setCrossingObstacle(false);
							stop();
						}
					});
					break;
				case 26303:
					if(!player.getClickDelay().elapsed(1200))
						return;
					if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
						player.getPacketSender().sendMessage("You need a Ranged level of at least 70 to swing across here.");
					else if (!player.getInventory().contains(9418)) {
						player.getPacketSender().sendMessage("You need a Mithril grapple to swing across here. Explorer Jack might have one.");
						return;
					} else {
						player.performAnimation(new Animation(789));
						TaskManager.submit(new Task(2, player, false) {
							@Override
							public void execute() {
								player.getPacketSender().sendMessage("You throw your Mithril grapple over the pillar and move across.");
								player.moveTo(new Position(2871, player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
								stop();
							}
						});
						player.getClickDelay().reset();
					}
					break;
				case 4493:
					if(player.getPosition().getX() >= 3432) {
						player.moveTo(new Position(3433, 3538, 1));
					}
					break;
					
				case 11356:
					if (player.getRights() == PlayerRights.UBER_DONATOR || player.getRights().isSeniorStaff()) {
						player.getPacketSender()
						.sendMessage("@red@Thank you for supporting Zamron!");
						player.moveTo(new Position(2335, 3612, 0));}
					
						else;
					{if (player.getRights() == PlayerRights.DONATOR || player.getRights() == PlayerRights.SUPER_DONATOR) 
					
						player.getPacketSender()
					.sendMessage("@red@You Need to be UBER Donator to access this Zone!");
					break;}
					
					
					
				case 16082:
					if (player.getAmountDonated() >= 1000) {
						TeleportHandler.teleportPlayer(player, new Position(2335, 3692, 0),
								player.getSpellbook().getTeleportType());
						player.getPacketSender().sendMessage("Welcome to the Deluxe Zone");
					} else {
						player.sendMessage("@red@nah fam, you need Deluxe Donator rank to enter");
						return;
					}
					break;
					
				case 10804:
					if (player.getInventory().contains(8851, 1000) && player.getInventory().contains(4691, 5000) && player.getSkillManager().getCurrentLevel(Skill.PVM) >= 92) {
						player.getInventory().delete(8851, 1000);
						player.getInventory().delete(4691, 5000);
						player.performAnimation(new Animation(828));
						player.moveTo(new Position(3309, 9808, 8));
						player.sendMessage("@red@You Found a secret dungeon");
					} else {
						player.sendMessage("@red@To enter this dungeon you need 1k Frankensteins tokens and 5k Colorful tokens!");
						player.sendMessage("@red@To be able to attack the monsters you also need 92 PVM to enter.");
					}
					break;
					
				case 26301:
					player.performAnimation(new Animation(828));
					player.moveTo(new Position(2605, 3093, 0));
					player.sendMessage("@red@You manage to escape the dungeon");
				break;
				
				case 16050:
					if (player.getAmountDonated() >= 500) {
						TeleportHandler.teleportPlayer(player, new Position(2335, 3692, 0),
								player.getSpellbook().getTeleportType());
						player.getPacketSender().sendMessage("Welcome to the Deluxe Zone");
					} else {
						player.sendMessage("@red@nah fam, you need Deluxe Donator rank to enter");
						return;
					}
					break;
					
				case 13619:
					if (player.getInventory().getAmount(17923) < 1000) {
					player.getPacketSender()
					.sendMessage("@blu@You need at least 1000 @red@Hellfire"
					+ " orbs @blu@to enter this area.");
					return;
					}
				player.getPacketSender()
				.sendMessage("@red@Goodluck in Room 2!");
				player.getInventory().delete(17923, 1000);
				player.moveTo(new Position(2459, 10135));
					break;
					
				case 23095:
					if (player.getInventory().getAmount(17924) < 1000) {
						player.getPacketSender()
								.sendMessage("@blu@You need at least 1000 @red@Nomads tickets"
						+ " @blu@to enter this area.");
						return;
					}
				player.getPacketSender()
				.sendMessage("@red@Goodluck in the Final Room!");
				player.getInventory().delete(17924, 1000);
				player.moveTo(new Position(2458, 10164));
					break;
				
				case 4494:
					player.moveTo(new Position(3438, 3538, 0));
					break;
				case 4495:
					player.moveTo(new Position(3417, 3541, 2));
					break;
				case 4496:
					player.moveTo(new Position(3412, 3541, 1));
					break;
				case 2491:
					player.setDialogueActionId(48);
					DialogueManager.start(player, 87);
					break;
				case 25339:
				case 25340:
					player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
					break;
				case 10229:
				case 10230:
					boolean up = id == 10229;
					player.performAnimation(new Animation(up ? 828 : 827));
					player.getPacketSender().sendMessage("You climb "+(up ? "up" : "down")+" the ladder..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
							stop();
						}
					});
					break;
				case 1568:
					player.moveTo(new Position(3097, 9868));
					break;
				case 5103: //Brimhaven vines
				case 5104:
				case 5105:
				case 5106:
				case 5107:
					if(!player.getClickDelay().elapsed(4000))
						return;
					if(player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
						player.getPacketSender().sendMessage("You need a Woodcutting level of at least 30 to do this.");
						return;
					}
					if(WoodcuttingData.getHatchet(player) < 0) {
						player.getPacketSender().sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
						return;
					}
					final Hatchet axe = Hatchet.forId(WoodcuttingData.getHatchet(player));
					player.performAnimation(new Animation(axe.getAnim()));
					gameObject.setFace(-1);
					TaskManager.submit(new Task(3 + RandomUtility.getRandom(4), player, false) {
						@Override
						protected void execute() {
							if(player.getMovementQueue().isMoving()) {
								stop();
								return;
							}
							int x = 0;
							int y = 0;
							if(player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2691 && player.getPosition().getY() == 9564) {
								x = -2;
								y = 0;
							} else if(player.getPosition().getX() == 2683 && player.getPosition().getY() == 9568) {
								x = 0;
								y = 2;
							} else if(player.getPosition().getX() == 2683 && player.getPosition().getY() == 9570) {
								x = 0;
								y = -2;
							} else if(player.getPosition().getX() == 2674 && player.getPosition().getY() == 9479) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2676 && player.getPosition().getY() == 9479) {
								x = -2;
								y = 0;
							} else if(player.getPosition().getX() == 2693 && player.getPosition().getY() == 9482) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2672 && player.getPosition().getY() == 9499) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2674 && player.getPosition().getY() == 9499) {
								x = -2;
								y = 0;
							}
							CustomObjects.objectRespawnTask(player, new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
							player.getPacketSender().sendMessage("You chop down the vines..");
							player.getSkillManager().addExperience(Skill.WOODCUTTING, 45);
							player.performAnimation(new Animation(65535));
							player.getMovementQueue().walkStep(x, y);
							stop();
						}
					});
					player.getClickDelay().reset();
					break;
				case 29942:
					if(player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager().getMaxLevel(Skill.SUMMONING)) {
						player.getPacketSender().sendMessage("You do not need to recharge your Summoning points right now.");
						return;
					}
					player.performGraphic(new Graphic(1517));
					player.getSkillManager().setCurrentLevel(Skill.SUMMONING, player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
					player.getPacketSender().sendString(18045, " "+player.getSkillManager().getCurrentLevel(Skill.SUMMONING)+"/"+player.getSkillManager().getMaxLevel(Skill.SUMMONING));
					player.getPacketSender().sendMessage("You recharge your Summoning points.");
					break;
				case 57225:
					if(!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
						player.setDialogueActionId(44);
						DialogueManager.start(player, 79);
					} else {
						player.moveTo(new Position(2906, 5204));
						player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
					}
					break;
				case 884:
					player.setDialogueActionId(41);
					DialogueManager.start(player, 75);
					break;
				case 9294:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 80 to use this shortcut.");
						return;
					}
					player.performAnimation(new Animation(769));
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));	
							stop();
						}
					});
					break;
				case 9293:
					boolean back = player.getPosition().getX() > 2888;
					player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
					break;
				case 2320:
					back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
					player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
					break;
				case 1755:
					player.performAnimation(new Animation(828));
					player.getPacketSender().sendMessage("You climb the stairs..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							if(gameObject.getPosition().getX() == 2547 && gameObject.getPosition().getY() == 9951) {
								player.moveTo(new Position(2548, 3551));
							} else if(gameObject.getPosition().getX() == 3005 && gameObject.getPosition().getY() == 10363) { 
								player.moveTo(new Position(3005, 3962));
							} else if(gameObject.getPosition().getX() == 3084 && gameObject.getPosition().getY() == 9672) {
								player.moveTo(new Position(3117, 3244));
							} else if(gameObject.getPosition().getX() == 3097 && gameObject.getPosition().getY() == 9867) {
								player.moveTo(new Position(3096, 3468));
							}
							stop();
						}
					});
					break;
				case 5110:
					player.moveTo(new Position(2647, 9557));
					player.getPacketSender().sendMessage("You pass the stones..");
					break;
				case 5111:
					player.moveTo(new Position(2649, 9562));
					player.getPacketSender().sendMessage("You pass the stones..");
					break;
				case 6434:
					player.performAnimation(new Animation(827));
					player.getPacketSender().sendMessage("You enter the trapdoor..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(new Position(3085, 9672));
							stop();
						}
					});
					break;
				case 19187:
				case 19175:
					Hunter.dismantle(player, gameObject);
					break;
				case 25029:
					PuroPuro.goThroughWheat(player, gameObject);
					break;
				case 47976:
					Nomad.endFight(player, false);
					break;
				case 2182:
					if(!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
						player.getPacketSender().sendMessage("You have no business with this chest. Talk to the Gypsy first!");
						return;
					}
					RecipeForDisaster.openRFDShop(player);
					break;
				case 12356:
					if(!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
						player.getPacketSender().sendMessage("You have no business with this portal. Talk to the Gypsy first!");
						return;
					}
					if(player.getPosition().getZ() > 0) {
						RecipeForDisaster.leave(player);
					} else {
						player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1, true);
						RecipeForDisaster.enter(player);
					}
					break;
				case 9369:
					if (player.getPosition().getY() > 5175) {
						FightPit.addPlayer(player);
					} else {
						FightPit.removePlayer(player, "leave room");
					}
					break;
				case 9368:
					if (player.getPosition().getY() < 5169) {
						FightPit.removePlayer(player, "leave game");
					}
					break;
				case 357:
					
					break;
				case 1:
					
					break;
				case 9357:
					FightCave.leaveCave(player, false);
					break;
				case 9356:
					FightCave.enterCave(player);
					break;
				case 6704:
					player.moveTo(new Position(3577, 3282, 0));
					break;
				case 6706:
					player.moveTo(new Position(3554, 3283, 0));
					break;
				case 6705:
					player.moveTo(new Position(3566, 3275, 0));
					break;
				case 6702:
					player.moveTo(new Position(3564, 3289, 0));
					break;
				case 6703:
					player.moveTo(new Position(3574, 3298, 0));
					break;
				case 6707:
					player.moveTo(new Position(3556, 3298, 0));
					break;
				case 3203:
					if(player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
						if(Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
							player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");			
							return;
						}
						player.getCombatBuilder().reset(true);
						if(player.getDueling().duelingWith > -1) {
							Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
							if(duelEnemy == null)
								return;
							duelEnemy.getCombatBuilder().reset(true);
							duelEnemy.getMovementQueue().reset();
							duelEnemy.getDueling().duelVictory();
						}
						player.moveTo(new Position(3368 + RandomUtility.getRandom(5), 3267+ RandomUtility.getRandom(3), 0));
						player.getDueling().reset();
						player.getCombatBuilder().reset(true);
						player.restart();
					}
					break;
				case 14315:
					PestControl.boardBoat(player);
					//player.sendMessage("Currently Pest Control is Disabled, we're fixing some stuff");
					break;
					//PestControl.boardBoat(player);
				case 14314:
					if(player.getLocation() == Location.PEST_CONTROL_BOAT) {
						player.getLocation().leave(player);
					}
					break;
				case 1738:
					if(player.getLocation() == Location.WARRIORS_GUILD) {
						player.moveTo(new Position(2840, 3539, 2));
					} else {
						player.getPacketSender().sendMessage("Nothing interesting happens.");
					}
					break;
				case 15638:
					//player.moveTo(new Position(1901, 5221, 0)); old home
					player.moveTo(GameSettings.DEFAULT_POSITION);
					break;
				case 15644:
				case 15641:
					switch(player.getPosition().getZ()) {
					case 0:
						player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
						break;
					case 2:
						if(player.getPosition().getX() == 2846) {
							if(player.getInventory().getAmount(8851) < 70) {
								player.getPacketSender().sendMessage("You need at least 70 tokens to enter this area.");
								return;
							}
							DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
							player.moveTo(new Position(2847, player.getPosition().getY(), 2));
							WarriorsGuild.handleTokenRemoval(player);
						} else if(player.getPosition().getX() == 2847) {
							WarriorsGuild.resetCyclopsCombat(player);
							player.moveTo(new Position(2846, player.getPosition().getY(), 2));
							player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
						}
						break;
					}
					break;
				case 1746:
					player.performAnimation(new Animation(827));
					player.delayedMoveTo(new Position(2209, 5348), 2);
					break;
				case 19191:
				case 19189:
				case 19180:
				case 19184:
				case 19182:
				case 19178:
					Hunter.lootTrap(player, gameObject);
					break;
				case 13493:
					double c = Math.random()*1;
					int reward = c >= 1 ? 1 : c >= 1 ? 1 : c >= 1 ? 1 : c >= 1 ? 1 : c >= 1 ? 1 : c >= 1 ? 1 : c >= 1 ? 1 : 1;
					Stalls.stealFromStall(player, 95, 24800, reward, "You've stolen some money!");
					break;
				case 3192:
					player.setDialogueActionId(11);
					DialogueManager.start(player, 20);
					break;
				case 28716:
					if(!player.busy()) {
						player.getSkillManager().updateSkill(Skill.SUMMONING);
						player.getPacketSender().sendInterface(63471);
					} else
						player.getPacketSender().sendMessage("Please finish what you're doing before opening this.");
					break;

				case 6:
					DwarfCannon cannon = player.getCannon();
					if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
						player.getPacketSender().sendMessage("This is not your cannon!");
					} else {
						DwarfMultiCannon.startFiringCannon(player, cannon);
					}
					break;
				case 2:
					player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
					player.getPacketSender().sendMessage("You walk through the entrance..");
					break;
				case 2026:
				case 2028:
				case 2029:
				case 2030:
				case 2031:
					player.setEntityInteraction(gameObject);
					Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
					return;
				case 12692:
				case 2783:
				case 4306:
					player.setInteractingObject(gameObject);
					EquipmentMaking.handleAnvil(player);
					//player.getPacketSender().sendMessage("Temporarily Disabled until fixed.");

					break;
				case 2732:
					EnterAmountOfLogsToAdd.openInterface(player);
					break;
				case 409:
				case 27661:
				case 2640:
				case 36972:
					player.performAnimation(new Animation(645));
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
						player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
						player.getPacketSender().sendMessage("You recharge your Prayer points.");
					}
					break;
				case 8749:
			
						player.setSpecialPercentage(100);
						CombatSpecial.updateBar(player);
						player.getPacketSender().sendMessage("Your special attack energy has been restored.");	
					player.performGraphic(new Graphic(1302));
					break;
				case 4859:
					player.performAnimation(new Animation(645));
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
						player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
						player.getPacketSender().sendMessage("You recharge your Prayer points.");
					}
					break;
				case 411:
					case 61:
					player.performAnimation(new Animation(645));
					if(player.getPrayerbook() == Prayerbook.NORMAL) {
						player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
						player.setPrayerbook(Prayerbook.CURSES);
					} else {
						player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
						player.setPrayerbook(Prayerbook.NORMAL);
					}
					player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
					PrayerHandler.deactivateAll(player);
					CurseHandler.deactivateAll(player);
					break;
				case 6552:
					player.performAnimation(new Animation(645));
					player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL : MagicSpellbook.ANCIENT);
					player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
					Autocasting.resetAutocast(player, true);
					break;
				case 13179:
					player.performAnimation(new Animation(645));
					player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL : MagicSpellbook.LUNAR);
					player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");;
					Autocasting.resetAutocast(player, true);
					break;
				case 4090:
					if (player.lastSpecialClaim > System.currentTimeMillis()) {
						player.sendMessage("You can only use this command once every 30 mins!");
						player.sendMessage("You still need to wait another <col=8C33FF>"
								+ player.getLongDurationTimer(player.lastSpecialClaim));
						return;
					}
					player.performAnimation(new Animation(645));
					player.setSpecialPercentage(100);
					CombatSpecial.updateBar(player);
					player.sendMessage("@red@Your special bar is now at 100%");
					player.lastSpecialClaim = System.currentTimeMillis() + 1800000;
					break;

				case 2403:
					SephirothChest.openChest(player);
					break;
				case 2183:
					DungeonMinigameChest.openChest(player);
					break;
					
				case 54587:
					RaidsEasyChest.openChest(player);
					break;
					
				case 2995:
					FantasyChest.openChest(player);
					break;
				case 4114:
					VIPChest.openChest(player);
					break;
					
				case 49347:
					DeluxeDonatorChest.openChest(player);
					break;
					
				case 19042:
					DungeonMinigameChest.openChest(player);
					break;
					
				case 6910:
				case 4483:
				case 3193:
				case 2213:
				case 11402:
				case 11758:
				case 14367:
				case 42192:
				case 75:
				case 9075:
					case 3045:
					case 49018:
					player.getBank(player.getCurrentBankTab()).open();
					break;
				}
			}
		}));
	}

	private static void secondClick(final Player player, Packet packet) {
		final int id = packet.readLEShortA();
		final int y = packet.readLEShort();
		final int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
			//player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
			return;
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("Second click object id; [id, position] : [" + id + ", " + position.toString() + "]");
		player.setInteractingObject(gameObject).setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			public void execute() {
				if(MiningData.forRock(gameObject.getId()) != null) {
					Prospecting.prospectOre(player, id);
					return;
				}
				if (player.getFarming().click(player, x, y, 1))
					return;
				switch(gameObject.getId()) {
				
				case 2466:
				case 2467:
					KeyRoom.handleObjectClick(player, gameObject, 2);
					return;
					
				case 884:
					player.setDialogueActionId(41);
					player.setInputHandling(new DonateToWell());
					player.getPacketSender().sendInterfaceRemoval().sendEnterAmountPrompt("How much money would you like to contribute with?");
					break;

					case 6421:
						int[] itemList2 = { 3064, 19618, 19691, 19692, 19693, 19694, 19696, 19695, 19159, 19160, 19161, 19163, 19164, 19165, 19166,
								9492, 9493, 9494, 9495, 9104, 19935, 14494, 14492, 14490, 2760, 19727, 19730, 19731, 19732, 19728, 6485, 19729, 19106, 13206, 13202, 13203, 13204, 13205, 13207, 11143, 11144, 11145, 11146, 11147,
								4797, 4794, 4795, 19127, 19128, 19129, 8664, 4796, 18931, 15374, 13992, 13994, 13993, 13995, 13991, 14448, 14447, 9496, 9497, 9498, 19155, 10905, 19741, 19742, 19743, 19744, 19154, 20427, 6927, 6928, 6929, 8656, 19935, 19936, 19937, 19938, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,};
						player.getPacketSender().sendInterface(65000);
						for (int i = 0; i < itemList2.length; i++)
							player.getPacketSender().sendItemOnInterface(65002, itemList2[i], i, 1);
						break;

					case 6420:
						int[] itemList1 = { 1499, 3973, 4800, 4801, 4802, 5079, 15012, 3951, 3316, 6584, 14559, 18750, 18751, 5131, 4770, 4771, 4772, 5209, 923, 3994, 3995, 3996, 5132, 12605, 19720, 3910, 3909, 3908, 3907, 19886,
								3980, 3999, 4000, 4001, 5167, 15649, 15650, 15651, 15652, 15653, 15654, 15655, 3905, 4761, 4762, 4763, 4764, 4765, 5089, 18894, 926, 5210, 931, 5211, 930, 15045, 12001, 5173, 3821, 3822, 3820, 19945,
								20054, 4781, 4782, 4783, 15032, 4785, 5195, 3914, 15656, 5082, 5083, 5084, 5085, 17151, 19619, 19470, 19471, 19472, 19473, 19474, 5129, 5130,3075, 3076, 3078, 3242, 3244, 5198, 5199, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
						player.getPacketSender().sendInterface(65000);
						for (int i = 0; i < itemList1.length; i++)
							player.getPacketSender().sendItemOnInterface(65002, itemList1[i], i, 1);
						break;
					case 172:
					int[] itemList8 = { 902, 903, 904, 905, 3082, 2577, 2749, 2750, 2752, 2751, 2753, 2754, 2755, 19721, 19722, 19723, 19724, 19734, 19736, 19468, 19137, 19935, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
					player.getPacketSender().sendInterface(65000);
					for (int i = 0; i < itemList8.length; i++)
						player.getPacketSender().sendItemOnInterface(65002, itemList8[i], i, 1);
					break;
				case 3479:
					InstanceSystem.open(player);
					break;
				case 2995:
					int[] itemList10 = { 4671,4672,4673,4670,10835, 923,5209,5167,15649,15650,15653, 4765,5089,
							930, 15045, 5210, 926, 931, 5211, 4781, 4782, 4783, 4785,5195, 15032, 3321, 16429,4780,932,12426, 6450,6451, 6452, 6480, 6481, 18950, 3988, 2547, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
					player.getPacketSender().sendInterface(65000);
					for (int i = 0; i < itemList10.length; i++)
						player.getPacketSender().sendItemOnInterface(65002, itemList10[i], i, 1);
					break;
				case 21505:
				case 21507:
					player.moveTo(new Position(2328, 3804));
					break;
				case 2646:
				case 312:
					if(!player.getClickDelay().elapsed(1200))
						return;
					if(player.getInventory().isFull()) {
						player.getPacketSender().sendMessage("You don't have enough free inventory space.");
						return;
					}
					String type = gameObject.getId() == 312 ? "Potato" : "Flax";
					player.performAnimation(new Animation(827));
					player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);
					player.getPacketSender().sendMessage("You pick some "+type+ "");
					gameObject.setPickAmount(gameObject.getPickAmount() + 1);
					if(RandomUtility.getRandom(3) == 1 && gameObject.getPickAmount() >= 1 || gameObject.getPickAmount() >= 6) {
						player.getPacketSender().sendClientRightClickRemoval();
						gameObject.setPickAmount(0);
						CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()), gameObject, 10);
					}
					player.getClickDelay().reset();
					break;
				case 2644:
					Flax.showSpinInterface(player);
					break;
				case 6:
					DwarfCannon cannon = player.getCannon();
					if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
						player.getPacketSender().sendMessage("This is not your cannon!");
					} else {
						DwarfMultiCannon.pickupCannon(player, cannon, false);
					}
					break;
				case 4875:
					Stalls.stealFromStall(player, 1, 5100, 18199, "You steal a banana.");
					break;
				case 4874:
					Stalls.stealFromStall(player, 30, 6130, 15009, "You steal a golden ring.");
					break;
				case 4876:
					Stalls.stealFromStall(player, 60, 7370, 17401, "You steal a damaged hammer.");
					break;
				case 4877:
					Stalls.stealFromStall(player, 65, 7990, 1389, "You steal a staff.");
					break;
				case 4878:
					Stalls.stealFromStall(player, 80, 9230, 11998, "You steal a scimitar.");
					break;
					
					
				case 6189:
				case 26814:
				case 11666:
					Smelting.openInterface(player);
					break;
				case 2152:
					player.performAnimation(new Animation(8502));
					player.performGraphic(new Graphic(1308));
					player.getSkillManager().setCurrentLevel(Skill.SUMMONING, player.getSkillManager().getMaxLevel(Skill.SUMMONING));
					player.getPacketSender().sendMessage("You renew your Summoning points.");
					break;
				}
			}
		}));
	}

	private static void fifthClick(final Player player, Packet packet) {
		final int id = packet.readUnsignedShortA();
		final int y = packet.readUnsignedShortA();
		final int x = packet.readShort();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(!Construction.buildingHouse(player)) {
			if(id > 0 && !RegionClipping.objectExists(gameObject)) {
				//player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		player.setInteractingObject(gameObject);
		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER)
			player.getPacketSender().sendMessage("Fifth click object id; [id, position] : [" + id + ", " + position.toString() + "]");
		player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch(id) {
				}
				Construction.handleFifthObjectClick(x, y, id, player);
			}
		}));
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			firstClick(player, packet);
			break;
		case SECOND_CLICK:
			secondClick(player, packet);
			break;
		case THIRD_CLICK:
			//thirdClick(player, packet);
			break;
		case FOURTH_CLICK:
			//fourthClick(player, packet);
			break;
		case FIFTH_CLICK:
			fifthClick(player, packet);
			break;
		}
	}

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234, FIFTH_CLICK = 228;
}
