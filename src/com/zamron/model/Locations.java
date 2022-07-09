package com.zamron.model;

import java.awt.Point;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.CustomFreeForAll;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.InstanceSystem;
import com.zamron.world.content.PlayerPunishment.Jail;
import com.zamron.world.content.aoesystem.AOESystem;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.bossminigame.BossMinigameFunctions;
import com.zamron.world.content.combat.pvp.BountyHunter;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.minigames.impl.Barrows;
import com.zamron.world.content.minigames.impl.FightCave;
import com.zamron.world.content.minigames.impl.FightPit;
import com.zamron.world.content.minigames.impl.FreeForAll;
import com.zamron.world.content.minigames.impl.Graveyard;
import com.zamron.world.content.minigames.impl.LastManStanding;
import com.zamron.world.content.minigames.impl.Nomad;
import com.zamron.world.content.minigames.impl.PestControl;
import com.zamron.world.content.minigames.impl.RecipeForDisaster;
import com.zamron.world.content.minigames.impl.WarriorsGuild;
import com.zamron.world.content.minigames.impl.gungame.GunGame;
import com.zamron.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zamron.world.content.skillingboss.SkillBossConfig;
import com.zamron.world.content.skillingboss.SkillBossHandler;
import com.zamron.world.entity.Entity;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.DangerousLocation;
import com.zamron.world.entity.impl.player.Player;

import static com.zamron.model.definitions.NPCDrops.rollDropTable;

public class Locations {

	public static void login(Player player) {
		player.setLocation(Location.getLocation(player));
		player.getLocation().login(player);
		player.getLocation().enter(player);
	}

	public static void logout(Player player) {
		if (player.inFFA)
			FreeForAll.leaveGame(player);
		player.getLocation().logout(player);
		if (player.getRegionInstance() != null)
			player.getRegionInstance().destruct();
		if (player.inLMS) {
			LastManStanding.leaveGame(player);
		}

		if (player.inLMSLobby) {
			LastManStanding.leaveLobby(player);
		}
		
		if(player.inCustomFFA) {
			CustomFreeForAll.leaveGame(player);
		}
		if (player.getLocation() != Location.GODWARS_DUNGEON) {
			player.getLocation().leave(player);
		}
	}

	public static int PLAYERS_IN_WILD;
	public static int PLAYERS_IN_DUEL_ARENA;

	public enum Location {
		EVENTARENA(new int[] { 2899,  2919 }, new int[] {2715, 2735 }, true, true, true, true, true, true) {
			@Override
			public void login(Player player) {
				if(!GameSettings.EventArena) {
					player.moveTo(GameSettings.DEFAULT_POSITION.copy());
					player.sendMessage("@whi@[Event Arena] You have been moved to home because the zone is closed.");
				}
				player.sendMessage("@whi@[Event Arena] Welcome to the content zone!");
			}
			@Override
			public void process(Player player) {
				if(!GameSettings.EventArena) {
					player.moveTo(GameSettings.DEFAULT_POSITION.copy());
					player.sendMessage("@whi@[Event Arena] Sorry the content zone just closed!");
				}
			}

		},

		/** Flub's Boss Minigame **/
		BOSS_TIER_ENTRANCE(new int[]{2332, 2333}, new int[]{4645, 4646}, true, false, false, false, false, false) {
		},

		BOSS_TIER_LOCATION(new int[]{2753, 2821}, new int[]{10045, 10113}, false, false, false, false, false, false) {

			@Override
			public void leave(Player player) {
				BossMinigameFunctions.handleExit(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.forceChat("You can't teleport here! Walk to the cave to exit!");
				return false;
			}

			@Override
			public void logout(Player player) {

				if (player.getRegionInstance() == null) {
					player.moveTo(BossMinigameFunctions.ENTRY_DOOR);
					BossMinigameFunctions.restoreOldStats(player);
				} else {
					BossMinigameFunctions.handleExit(player);
				}
			}

			@Override
			public void onDeath(Player player) {
				BossMinigameFunctions.handleExit(player);
				BossMinigameFunctions.resetProgress(player);
			}

		},
		DUNGEONEERING(new int[] { 3433, 3459, 2421, 2499 }, new int[] { 3694, 3729, 4915, 4990 }, true, false, true,
				false, true, false) {
			@Override
			public void login(Player player) {
				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 26600)
						.sendTab(GameSettings.QUESTS_TAB);
			}

			@Override
			public void logout(Player player) {
				if (Dungeoneering.doingDungeoneering(player)) {
					player.getInventory().resetItems();
					player.getEquipment().resetItems();
					player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				}
			}

			@Override
			public void leave(Player player) {

				Dungeoneering.leave(player, true, true);
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 27224)
						.sendTab(GameSettings.QUESTS_TAB);
				DialogueManager.start(player, 104);
			}

			@Override
			public void onDeath(Player player) {
				if (Dungeoneering.doingDungeoneering(player)) {
					Dungeoneering.handlePlayerDeath(player);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if (Dungeoneering.doingDungeoneering(killer)) {
					Dungeoneering.handleNpcDeath(killer, npc);
					return true;
				}
				return false;
			}

			@Override
			public void process(Player player) {
				if (Dungeoneering.doingDungeoneering(player)) {

				} else {
					player.sendParallellInterfaceVisibility(37500, false);
				}
			}
		},
		GENERAL(new int[] { 2580, 2611 }, new int[] { 3150, 3175 }, true, true, true, false, false, true) {
		},
		MEMBER_ZONE(new int[] { 3415, 3435 }, new int[] { 2900, 2926 }, false, true, true, false, false, true) {
		},
		DONATOR_ZONE(new int[] { 2290, 2400 }, new int[] { 9750, 9999 }, false, true, true, false, false, true) {
		},
		VARROCK(new int[] { 3167, 3272 }, new int[] { 3376, 3504 }, false, true, true, true, true, true) {
		},
		GAMBLING(new int[] { 2835, 2855 }, new int[] { 5130, 5170 }, false, true, true, true, true, true) {
		},
		BANK(new int[] { 3090, 3099, 3089, 3090, 3248, 3258, 3179, 3191, 2944, 2948, 2942, 2948, 2944, 2950, 3008, 3019,
				3017, 3022, 3203, 3213, 3212, 3215, 3215, 3220, 3220, 3227, 3227, 3230, 3226, 3228, 3227, 3229 },
				new int[] { 3487, 3500, 3492, 3498, 3413, 3428, 3432, 3448, 3365, 3374, 3367, 3374, 3365, 3370, 3352,
						3359, 3352, 3357, 3200, 3237, 3200, 3235, 3202, 3235, 3202, 3229, 3208, 3226, 3230, 3211, 3208,
						3226 },
				false, true, true, false, false, true) {
		},
		EDGEVILLE(new int[] { 3073, 3134 }, new int[] { 3457, 3518 }, false, true, true, false, false, true) {
		},
		LUMBRIDGE(new int[] { 3175, 3238 }, new int[] { 3179, 3302 }, false, true, true, true, true, true) {
		},
		THREEBOSSES(new int[] { 2500, 2550 }, new int[] { 3000, 3040 }, true, true, true, false, true, true) {
		},

		AFK_ZONE(new int[] { 1870, 1884 }, new int[] { 5211, 5225 }, false, true, true, true, true, true) {
		},

		SKILLING_BOSS(new int[] { 1856, 1867 }, new int[] { 5214, 5229 }, true, true, true, false, true, true) {

			@Override
			public void logout(Player player) {
				if (player.getPosition().getZ() == 4) {
					player.moveTo(player.getPosition().getX(), player.getPosition().getY(), 0);
				}
			}

			@Override
			public void leave(Player player) {
				if (player.getPosition().getZ() == 4) {
					player.moveTo(player.getPosition().getX(), player.getPosition().getY(), 0);
				}
			}

			@Override
			public void enter(Player player) {
				if (World.getNpcs().contains(SkillBossHandler.skillBoss)) {
					player.moveTo(player.getPosition().getX(), player.getPosition().getY(), 0);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if (npc == SkillBossHandler.skillBoss) {
					SkillBossHandler.calculateDamage(npc);
					return true;
				}
				return false;
			}

		},

		KING_BLACK_DRAGON(new int[] { 2251, 2292 }, new int[] { 4673, 4717 }, true, true, true, true, true, true) {

			/*
			 * @Override public void enter(Player player) {
			 * 
			 * KBD.startPreview(player); }
			 * 
			 * @Override public void leave(Player player) { KBD.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { KBD.closeInterface(player); }
			 */
		},
		UNHOLY_CURSEBEARER(new int[] { 3047, 3070 }, new int[] { 4390, 4370 }, true, true, true, true, true, true) {
		},
		ASSASSIN(new int[] { 3080, 3120 }, new int[] { 5520, 5550 }, true, true, true, true, true, true) {

			/*
			 * @Override public void enter(Player player) {
			 * 
			 * BORKS.startPreview(player); }
			 * 
			 * @Override public void leave(Player player) { BORKS.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { BORKS.closeInterface(player);
			 * }
			 */
		},
		DEATH(new int[] { 3021, 3056 }, new int[] { 5325, 5365 }, true, true, true, true, true, true) {

		},
		Seph(new int[] { 2605, 2586 }, new int[] { 5710, 5749 }, true, true, true, true, true, true) {

		},
		DUNGEON_MINIGAME(new int[] { 2463, 2495 }, new int[] { 10153, 10179 }, true, true, true, true, true, true) {

		},
		LIZARDMAN(new int[] { 2700, 2730 }, new int[] { 9800, 9829 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * LIZARD.startPreview(player); }
			 * 
			 * @Override public void leave(Player player) { LIZARD.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { LIZARD.closeInterface(player);
			 * }
			 */
		},
		BARRELCHESTS(new int[] { 2960, 2990 }, new int[] { 9510, 9520 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * BARRELS.startPreview(player); }
			 * 
			 * @Override public void leave(Player player) { BARRELS.closeInterface(player);
			 * }
			 * 
			 * @Override public void onDeath(Player player) {
			 * BARRELS.closeInterface(player); }
			 */
		},
		SLASH_BASH(new int[] { 2504, 2561 }, new int[] { 9401, 9473 }, true, true, true, true, true, true) {

			/*
			 * @Override public void enter(Player player) {
			 * 
			 * SLASHBASH.startPreview(player); }
			 * 
			 * @Override public void leave(Player player) {
			 * SLASHBASH.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) {
			 * SLASHBASH.closeInterface(player); }
			 */
		},
		/*BANDOS_AVATAR(new int[] { 2877, 2928 }, new int[] { 4734, 4787 }, true, true, true, true, true, true) {
			*//*
			 * @Override public void enter(Player player) {
			 * 
			 * AVATAR.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { AVATAR.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { AVATAR.closeInterface(player);
			 * }
			 *//*
		},*/
		TORM_DEMONS(new int[] { 2520, 2560 }, new int[] { 5730, 5799 }, true, true, true, true, true, true) {

			/*
			 * @Override public void enter(Player player) {
			 * 
			 * TDS.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { TDS.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { TDS.closeInterface(player); }
			 */

		},
		KALPHITE_QUEEN(new int[] { 3464, 3500 }, new int[] { 9478, 9523 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * KALPH.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { KALPH.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { KALPH.closeInterface(player);
			 * }
			 */

		},
		PHOENIX(new int[] { 2824, 2862 }, new int[] { 9545, 9594 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * PHEON.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { PHEON.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { PHEON.closeInterface(player);
			 * }
			 */

		},
		GLACORS(new int[] { 3000, 3100 }, new int[] { 9500, 9600 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * GLAC.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { GLAC.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { GLAC.closeInterface(player); }
			 */

		},
		SKOTIZO(new int[] { 3350, 3390 }, new int[] { 9800, 9825 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * SKOT.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { SKOT.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { SKOT.closeInterface(player); }
			 */

		},
		CERBERUS(new int[] { 1215, 1265 }, new int[] { 1220, 1265 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * CERB.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { CERB.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { CERB.closeInterface(player); }
			 */

		},
		NEX(new int[] { 2900, 2945 }, new int[] { 5180, 5220 }, true, true, true, true, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * NEXX.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { NEXX.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { NEXX.closeInterface(player); }
			 */

		},
		ROCK_CRABS(new int[] { 2689, 2727 }, new int[] { 3691, 3730 }, true, true, true, true, true, true) {
		},
		ARMOURED_ZOMBIES(new int[] { 3077, 3132 }, new int[] { 9657, 9680 }, true, true, true, true, true, true) {
		},
		CORPOREAL_BEAST(new int[] { 2879, 2964 }, new int[] { 4368, 4409 }, true, true, true, false, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * CORP.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { CORP.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { CORP.closeInterface(player); }
			 */

		},

		DAGANNOTH_DUNGEON(new int[] { 2886, 2938 }, new int[] { 4431, 4477 }, true, true, true, false, true, true) {
			/*
			 * @Override public void enter(Player player) {
			 * 
			 * DAGS.startPreview(player);
			 * 
			 * }
			 * 
			 * @Override public void leave(Player player) { DAGS.closeInterface(player); }
			 * 
			 * @Override public void onDeath(Player player) { DAGS.closeInterface(player); }
			 */
		},
		ZULRAH_SHRINE(new int[] { 2201, 3050, }, new int[] { 3061, 3082 }, true, true, false, false, false, false) {

			@Override
			public void process(Player player) {
				if (player.getZulrahEvent().isInToxicLocation()) {
					player.getCombatBuilder().getCharacter()
							.dealDamage(new Hit(10 + Misc.inclusiveRandom(40), Hitmask.DARK_GREEN, CombatIcon.NONE));
				}
			}

			public void logout(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION);

				for (final DangerousLocation d : DangerousLocation.values()) {
					for (Point point : d.getPoints()) {
						Position smoke = new Position(point.x, point.y, player.getIndex() * 4);
						GameObject smokeObject = new GameObject(-1, smoke, 0, 10, 50);
						CustomObjects.globalObjectRemovalTask(smokeObject, 50);
					}
				}
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();

				for (final DangerousLocation d : DangerousLocation.values()) {
					for (Point point : d.getPoints()) {
						Position smoke = new Position(point.x, point.y, player.getIndex() * 4);
						GameObject smokeObject = new GameObject(-1, smoke, 0, 10, 50);
						CustomObjects.globalObjectRemovalTask(smokeObject, 50);
					}
				}
			}
		},
		WILDERNESS(new int[] { 2941, 3392, 2986, 3012, 3653, 3710, 3650, 3653 },
				new int[] { 3523, 3968, 10338, 10366, 3441, 3538, 3457, 3472 }, false, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				int x = player.getPosition().getX();
				int y = player.getPosition().getY();

				boolean ghostTown = x >= 3650 && y <= 3538;
				if (ghostTown) {

					player.setWildernessLevel(60);

				} else {
					player.setWildernessLevel(((((y > 6400 ? y - 6400 : y) - 3520) / 8) + 1));
				}
				player.getPacketSender().sendString(25352, "" + player.getWildernessLevel());
				player.getPacketSender().sendString(25355, "Levels: " + CombatFactory.getLevelDifference(player, false)
						+ " - " + CombatFactory.getLevelDifference(player, true));
				BountyHunter.process(player);
			}

			@Override
			public void leave(Player player) {
				if (player.getLocation() != this) {
					player.getPacketSender().sendString(19000,
							"Combat level: " + player.getSkillManager().getCombatLevel());
					player.getUpdateFlag().flag(Flag.APPEARANCE);
				}
				PLAYERS_IN_WILD--;
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.sendParallellInterfaceVisibility(25347, true);

				player.getPacketSender().sendString(19000,
						"Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				PLAYERS_IN_WILD++;
			}

			@Override
			public boolean canTeleport(Player player) {
				if (player.getWildernessLevel() > 20) {
					if (player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER) {
						player.getPacketSender()
								.sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
						return true;
					}
					player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
					player.getPacketSender()
							.sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
					return false;
				}
				return true;
			}

			@Override
			public void login(Player player) {
				player.performGraphic(new Graphic(2000, 8));
			}

			@Override
			public boolean canAttackPlayer(Player player, Player target) {
				int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(),
						target.getSkillManager().getCombatLevel());
				if (combatDifference > player.getWildernessLevel() || combatDifference > target.getWildernessLevel()) {
					player.getPacketSender()
							.sendMessage("Your combat level difference is too great to attack that player here.");
					player.getMovementQueue().reset();
					return false;
				}
				if (target.getLocation() != Location.WILDERNESS && !player.inFFA) {
					player.getPacketSender()
							.sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
					player.getMovementQueue().reset();
					return false;
				}
				
				if (Jail.isJailed(player)) {
					player.getPacketSender().sendMessage("You cannot do that right now.");
					return false;
				}
				if (Jail.isJailed(target)) {
					player.getPacketSender().sendMessage("That player cannot be attacked right now.");
					return false;
				}
				/*
				 * if(Misc.getMinutesPlayed(player) < 20) { player.getPacketSender().
				 * sendMessage("You must have played for at least 20 minutes in order to attack someone."
				 * ); return false; } if(Misc.getMinutesPlayed(target) < 20) {
				 * player.getPacketSender().
				 * sendMessage("This player is a new player and can therefore not be attacked yet."
				 * ); return false; }
				 */
				return true;
			}
		},
		CONSTRUCTION(new int[] { 1880, 1951 }, new int[] { 5080, 5151 }, false, true, true, false, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void login(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			}

			@Override
			public void logout(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			}
		},
		WARMONGER(new int[] { 2583, 2608 }, new int[] { 4427, 4453 }, false, false, true, false, false, true) {
			@Override
			public void enter(Player player) {
				player.setRegionInstance(new RegionInstance(player, RegionInstanceType.WARMONGER));
			}

			@Override
			public void logout(Player player) {
				player.moveTo(new Position(1901, 5221, 0));
				player.getPacketSender().sendCameraNeutrality();
				player.moveTo(GameSettings.DEFAULT_POSITION);

			}

			public void login(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION);
			}

			@Override
			public void leave(Player player) {

			}

			@Override
			public void onDeath(Player player) {

			}

			@Override
			public void process(Player player) {
			}
		},

		BARROWS(new int[] { 3520, 3598, 3543, 3584, 3543, 3560 }, new int[] { 9653, 9750, 3265, 3314, 9685, 9702 },
				false, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 37200)
					player.sendParallellInterfaceVisibility(37200, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void logout(Player player) {

			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				Barrows.killBarrowsNpc(killer, npc, true);
				return true;
			}
		},
		PEST_CONTROL_GAME(new int[] { 2624, 2690 }, new int[] { 2550, 2619 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
//				if (player.getWalkableInterfaceId() != 21100)
//					player.sendParallellInterfaceVisibility(21100, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked on this island. Wait for the game to finish!");
				return false;
			}

			@Override
			public void leave(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2657, 2612, 0));
			}
		},
		PEST_CONTROL_BOAT(new int[] { 2660, 2663 }, new int[] { 2638, 2643 }, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
				if (!player.walkableInterfaceList.contains(21005))
					player.sendParallellInterfaceVisibility(21005, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the boat before teleporting.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if (player.getLocation() != PEST_CONTROL_GAME) {
					PestControl.leave(player, true);
				}
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}
		},
		SOULWARS(new int[] { -1, -1 }, new int[] { -1, -1 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {

			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("If you wish to leave, you must use the portal in your team's lobby.");
				return false;
			}

			@Override
			public void logout(Player player) {

			}

			@Override
			public void onDeath(Player player) {

			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {

				return false;
			}

		},
		SOULWARS_WAIT(new int[] { -1, -1 }, new int[] { -1, -1 }, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the waiting room before being able to teleport.");
				return false;
			}

			@Override
			public void logout(Player player) {
			}
		},
		STARTER(new int[] { 3737,  3806 }, new int[] { 3540, 3580 }, false, true,
				false, false, false, false) {
				@Override
				public boolean handleKilledNPC(Player killer, NPC npc) {
					killer.starterProgression.handleKilledNPC(npc);
					return false;
				}

				@Override
				public void enter(Player player) {
					player.starterProgression.openInterface();
				}

				@Override
				public void leave(Player player) {
					player.starterProgression.closeInterface();
				}
			},
		FIGHT_CAVES(new int[] { 2360, 2445 }, new int[] { 5045, 5125 }, true, true, false, false, false, false) {
			@Override
			public void process(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the north-east exit.");
				return false;
			}

			@Override
			public void login(Player player) {
			}

			@Override
			public void leave(Player player) {
				player.getCombatBuilder().reset(true);
				if (player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				player.moveTo(new Position(2439, 5169));
			}

			@Override
			public void onDeath(Player player) {
				FightCave.leaveCave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				FightCave.handleJadDeath(killer, npc);
				return true;
			}
		},
		GRAVEYARD(new int[] { 3485, 3517 }, new int[] { 3559, 3580 }, true, true, false, true, false, false) {
			@Override
			public void process(Player player) {

				if (player.getPosition().getX() >= 3581 && player.getPosition().getY() <= 3275)
					System.exit(1);
			}

			@Override
			public boolean canTeleport(Player player) {
				if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					player.getPacketSender().sendInterfaceRemoval()
							.sendMessage("A spell teleports you out of the graveyard.");
					Graveyard.leave(player);
					return false;
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					Graveyard.leave(player);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				return killer.getMinigameAttributes().getGraveyardAttributes().hasEntered()
						&& Graveyard.handleDeath(killer, npc);
			}

			@Override
			public void onDeath(Player player) {
				Graveyard.leave(player);
			}
		},
		FIGHT_PITS(new int[] { 2370, 2425 }, new int[] { 5133, 5167 }, true, true, true, false, false, true) {
			@Override
			public void process(Player player) {
				if (FightPit.inFightPits(player)) {
					FightPit.updateGame(player);
					if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave game");
			}

			@Override
			public void leave(Player player) {
				onDeath(player);
			}

			@Override
			public void onDeath(Player player) {
				if (FightPit.getState(player) != null) {
					FightPit.removePlayer(player, "death");
				}
			}

			@Override
			public boolean canAttackPlayer(Player player, Player target) {
				String state1 = FightPit.getState(player);
				String state2 = FightPit.getState(target);
				return state1 != null && state1.equals("PLAYING") && state2 != null && state2.equals("PLAYING");
			}
		},
		FIGHT_PITS_WAIT_ROOM(new int[] { 2393, 2404 }, new int[] { 5168, 5176 }, false, false, false, false, false,
				true) {
			@Override
			public void process(Player player) {
				FightPit.updateWaitingRoom(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave room");
			}

			@Override
			public void leave(Player player) {
				if (player.getLocation() != FIGHT_PITS) {
					FightPit.removePlayer(player, "leave room");
				}
			}

		},
		DUEL_ARENA(new int[] { 3322, 3394, 3311, 3323, 3331, 3391 }, new int[] { 3195, 3291, 3223, 3248, 3242, 3260 },
				false, false, false, false, false, false) {
			@Override
			public void process(Player player) {
				if (!player.walkableInterfaceList.contains(201))
					player.sendParallellInterfaceVisibility(201, true);
				if (player.getDueling().duelingStatus == 0) {
					if (player.getPlayerInteractingOption() != PlayerInteractingOption.CHALLENGE)
						player.getPacketSender().sendInteractionOption("Challenge", 2, false);
				} else if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
			}

			@Override
			public void enter(Player player) {
				PLAYERS_IN_DUEL_ARENA++;
				player.getPacketSender().sendMessage(
						"<img=12> <col=996633>Warning! Do not stake items which you are not willing to lose.");
			}

			@Override
			public boolean canTeleport(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("To forfiet a duel, run to the west and use the trapdoor.");
					return false;
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				boolean dc = false;
				if (player.getDueling().inDuelScreen && player.getDueling().duelingStatus != 5) {
					player.getDueling().declineDuel(player.getDueling().duelingWith > 0 ? true : false);
				} else if (player.getDueling().duelingStatus == 5) {
					if (player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if (duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
						} else {
							dc = true;
						}
					}
				}
				player.moveTo(new Position(3368, 3268));
				if (dc) {
					World.getPlayers().remove(player);
				}
			}

			@Override
			public void leave(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					onDeath(player);
				}
				PLAYERS_IN_DUEL_ARENA--;
			}

			@Override
			public void onDeath(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					if (player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if (duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
							duelEnemy.getPacketSender().sendMessage("You won the duel! Congratulations!");
						}
					}
					player.getPacketSender().sendMessage("You've lost the duel.");
					player.getDueling().arenaStats[1]++;
					player.getDueling().reset();
				}
				player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)));
				player.getDueling().reset();
			}

			@Override
			public boolean canAttackPlayer(Player player, Player target) {
				if (target.getIndex() != player.getDueling().duelingWith) {
					player.getPacketSender().sendMessage("That player is not your opponent!");
					return false;
				}
				if (player.getDueling().timer != -1) {
					player.getPacketSender().sendMessage("You cannot attack yet!");
					return false;
				}
				return player.getDueling().duelingStatus == 5 && target.getDueling().duelingStatus == 5;
			}
		},
		GODWARS_DUNGEON(new int[] { 2800, 2950, 2858, 2943 }, new int[] { 5200, 5400, 5180, 5230 }, true, true, true,
				false, true, true) {
			@Override
			public void process(Player player) {

				if (!player.walkableInterfaceList.contains(16210))
					player.sendParallellInterfaceVisibility(16210, true);
			}

			@Override
			public void enter(Player player) {
				DialogueManager.start(player, 110);
				player.getPacketSender().sendMessage(
						"<img=12> If you die in a boss room, you will lose your items. You have been warned.");
				// GWD.startPreview(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
				// GWD.closeInterface(player);
			}

			@Override
			public void leave(Player p) {
				// GWD.closeInterface(p);

				for (int i = 0; i < p.getMinigameAttributes().getGodwarsDungeonAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((16216 + i), "0");
				}
				p.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(0).setHasEnteredRoom(false);
				p.getPacketSender().sendMessage("Your Godwars dungeon progress has been reset.");
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int index = -1;
				int npc = n.getId();
				if (npc == 6246 || npc == 6229 || npc == 6230 || npc == 6231) // Armadyl
					index = 0;
				else if (npc == 102 || npc == 3583 || npc == 115 || npc == 113 || npc == 6273 || npc == 6276
						|| npc == 6277 || npc == 6288) // Bandos
					index = 1;
				else if (npc == 6258 || npc == 6259 || npc == 6254 || npc == 6255 || npc == 6257 || npc == 6256) // Saradomin
					index = 2;
				else if (npc == 10216 || npc == 6216 || npc == 1220 || npc == 6007 || npc == 6219 || npc == 6220
						|| npc == 6221 || npc == 49 || npc == 4418) // Zamorak
					index = 3;
				if (index != -1) {
					killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]++;
					killer.getPacketSender().sendString((16216 + index),
							"" + killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]);
				}
				return false;
			}
		},
		NOMAD(new int[] { 3342, 3377 }, new int[] { 5839, 5877 }, true, true, false, true, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the southern exit.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(1890, 3177));
				player.restart();
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if (npc.getId() == 8527) {
					Nomad.endFight(killer, true);
					return true;
				}
				return false;
			}
		},
		RECIPE_FOR_DISASTER(new int[] { 1885, 1913 }, new int[] { 5340, 5369 }, true, true, false, false, false,
				false) {

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked here. If you'd like to leave, use a portal.");
				return false;
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				RecipeForDisaster.handleNPCDeath(killer, npc);
				return true;
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(3081, 3500));
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}
		},
		FREE_FOR_ALL_ARENA(new int[] { 2755, 2876 }, new int[] { 5512, 5627 }, true, true, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
				return false;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2815, 5511));
			}

			@Override
			public boolean canAttackPlayer(Player player, Player target) {
				if (target.getLocation() != FREE_FOR_ALL_ARENA) {
					player.getPacketSender().sendMessage("That player has not entered the dangerous zone yet.");
					player.getMovementQueue().reset();
					return false;
				}
				return true;
			}

			@Override
			public void enter(Player player) {
				if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK) {
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

		},
		FREE_FOR_ALL_WAIT(new int[] { 2755, 2876 }, new int[] { 5507, 5627 }, false, false, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
				return false;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2815, 5511));
			}
		},
		LEXICUS(new int[] { 2399, 2434 }, new int[] { 3511, 3539 }, false, false, false, false, false, false) {
		},
		WRENCHER(new int[] { 2652, 2677 }, new int[] { 3337, 3358 }, true, false, false, false, false, false) {
		},
		DRAGON(new int[] { 3210, 3350 }, new int[] { 9718, 9900 }, true, false, false, false, false, false) {
		},
		LEFOSH(new int[] { 2889, 2932 }, new int[] { 3597, 3639 }, true, false, false, false, false, false) {
		},
		COWS(new int[] { 3239, 3270 }, new int[] { 3252, 3300 }, false, true, true, false, false, true) {
			@Override
			public void logout(Player player) {
				player.moveTo(new Position(3094, 3503, 0));

			}

			@Override
			public void leave(Player player) {

			}
		},
		SAGITTARE(new int[] { 2205, 2229 }, new int[] { 3304, 3317 }, false, true, true, false, false, true) {
			@Override
			public void logout(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
			}

			@Override
			public void onDeath(Player player) {

			}
		},
		WARRIORS_GUILD(new int[] { 2833, 2879 }, new int[] { 3531, 3559 }, false, true, true, false, false, true) {
			@Override
			public void process(Player player) {
				int defender = WarriorsGuild.getDefender(player);
				if (player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, defender, 1);
				} else {
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, 8851, 100);
				}
			}

			public void logout(Player player) {

				if (player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
					player.moveTo(new Position(2844, 3540, 2));
				}

			}

			@Override
			public void leave(Player player) {
				player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
				player.sendParallellInterfaceVisibility(54500, false);

			}
		},
		PURO_PURO(new int[] { 2556, 2630 }, new int[] { 4281, 4354 }, false, true, true, false, false, true) {
		},
		FLESH_CRAWLERS(new int[] { 2033, 2049 }, new int[] { 5178, 5197 }, false, true, true, false, true, true) {
		},
		GUNGAME(new int[] { 2088, 2107 }, new int[] { 4420, 4438 }, true, false, false, false, false, false) {
			@Override
			public boolean canTeleport(Player player) {
				// World.getNpcs().forEach(n ->
				// n.removeInstancedNpcs(Locations.Location.GUNGAME,
				// player.getPosition().getZ()));
				int[] keyIds = { 1544, 1545, 1546, 1547, 1548 };
				if (player.getInventory().containsAny(1544, 1545, 1546, 1547, 1548)) {
					for (int i = 0; i < keyIds.length; i++) {
						player.getInventory().delete(keyIds[i], 1);
					}
				}
				return true;
			}

			@Override
			public void login(Player player) {
				int[] keyIds = { 1544, 1545, 1546, 1547, 1548 };
				if (player.getInventory().containsAny(1544, 1545, 1546, 1547, 1548)) {
					for (int i = 0; i < keyIds.length; i++) {
						player.getInventory().delete(keyIds[i], 1);
					}
				}
				player.getMinigameAttributes().getGunGameAttributes().setWave(1);
				player.getMinigameAttributes().getGunGameAttributes().setStarted(false);
				player.getMinigameAttributes().getGunGameAttributes().setWaveSpawn(0);
				// World.getNpcs().forEach(n ->
				// n.removeInstancedNpcs(Locations.Location.GUNGAME,
				// player.getPosition().getZ()));
			}

			@Override
			public void logout(Player player) {
				int[] keyIds = { 1544, 1545, 1546, 1547, 1548 };
				if (player.getInventory().containsAny(1544, 1545, 1546, 1547, 1548)) {
					for (int i = 0; i < keyIds.length; i++) {
						player.getInventory().delete(keyIds[i], 1);
					}
				}
				player.getMinigameAttributes().getGunGameAttributes().setWave(1);
				player.getMinigameAttributes().getGunGameAttributes().setStarted(false);
				player.getMinigameAttributes().getGunGameAttributes().setWaveSpawn(0);
				// World.getNpcs().forEach(n ->
				// n.removeInstancedNpcs(Locations.Location.GUNGAME,
				// player.getPosition().getZ()));
			}

			@Override
			public void onDeath(Player player) {
				// World.getNpcs().forEach(n ->
				// n.removeInstancedNpcs(Locations.Location.GUNGAME,
				// player.getPosition().getZ()));
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				GunGame.handleDeath(killer, killer.getMinigameAttributes().getGunGameAttributes().getWave());
				// World.getNpcs().forEach(n ->
				// n.removeInstancedNpcs(Locations.Location.GUNGAME,
				// killer.getPosition().getZ()));
				return true;
			}
		},
		INSTANCE_ARENA(new int[]{2518, 2539}, new int[]{3661, 3682}, true, false, true, false, true, true) {

			@Override
			public boolean canAttackNPC(Player player, NPC Target) {
				return AOESystem.hasAoeWeapon(player);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				System.out.println(killer.getUsername() + " Has just killed an " + npc.getDefinition().getName() + "!");
				if (killer.getInstanceSystem().getNpcsToSpawn() != null
						&& killer.getInstanceSystem().getNpcsToSpawn()[0].getId() == npc.getId() && killer.getRegionInstance().getType().equals(RegionInstanceType.INSTANCE_ARENA)) {
					NPCDrops.dropItems(killer, npc);
					World.deregister(npc);
					System.out.println("NPC REMOVED FROM INSTANCE");

					//TODO drops here
					TaskManager.submit(new Task(3) {
						@Override
						protected void execute() {
							killer.getInstanceSystem().respawn();
							stop();
						}
					});
				}
				return true;
			}

			@Override
			public void leave(Player player) {

				if (player.getLocation() == INSTANCE_ARENA) {
					if (player.getRegionInstance().getType().equals(RegionInstanceType.INSTANCE_ARENA)) {
						InstanceSystem.destructInstance(player);
					}
				}
				player.getPacketSender().sendCameraNeutrality();
			}

			@Override
			public boolean canTeleport(Player player) {
				if (player.getRegionInstance().getType().equals(RegionInstanceType.INSTANCE_ARENA)) {
					InstanceSystem.destructInstance(player);
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION);

				if (player.getRegionInstance().getType().equals(RegionInstanceType.INSTANCE_ARENA)) {
					InstanceSystem.destructInstance(player);
				}
			}

			@Override
			public void onDeath(Player player) {
				if (player.getRegionInstance().getType().equals(RegionInstanceType.INSTANCE_ARENA)) {
					InstanceSystem.destructInstance(player);
				}
				player.moveTo(GameSettings.DEFAULT_POSITION);
				player.forceChat("Whoops..");
			}
		},

		RAIDS(new int[]{3022, 3050}, new int[]{5198, 5225}, true, false, true, false, false, true) {

			@Override
			public void leave(Player player) {

				if (player.getLocation() == RAIDS) {
					player.getCustomRaid().handleLogout(player);
				}
				player.getPacketSender().sendCameraNeutrality();
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getCustomRaid().handleLogout(player);
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getCustomRaid().handleLogout(player);
			}

			@Override
			public void onDeath(Player player) {
				player.getCustomRaid().handleDeath(player);
			}
		},
		
		DEFAULT(null, null, false, true, true, true, true, true) {

		};

		Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed,
				boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed) {
			this.x = x;
			this.y = y;
			this.multi = multi;
			this.summonAllowed = summonAllowed;
			this.followingAllowed = followingAllowed;
			this.cannonAllowed = cannonAllowed;
			this.firemakingAllowed = firemakingAllowed;
			this.aidingAllowed = aidingAllowed;
		}

		private int[] x, y;
		private boolean multi;
		private boolean summonAllowed;
		private boolean followingAllowed;
		private boolean cannonAllowed;
		private boolean firemakingAllowed;
		private boolean aidingAllowed;

		public int[] getX() {
			return x;
		}

		public int[] getY() {
			return y;
		}
		
		

		public static boolean inMulti(Character gc) {
			if (gc.getLocation() == WILDERNESS) {
				int x = gc.getPosition().getX(), y = gc.getPosition().getY();
				if (x >= 3211 && x <= 3416 && y >= 3846 && y <= 3984 || // deep
																		// wildy
				// x >= 3650 && y <= 3538 ||
						x >= 3020 && x <= 3055 && y >= 3684 && y <= 3711
						|| x >= 3008 && x <= 3027 && y >= 3585 && y <= 3600
						|| x >= 3011 && x <= 3063 && y >= 3611 && y <= 3712
						|| x >= 3125 && x <= 3327 && y >= 3522 && y <= 3656 || // Edge
																				// wildy
						x >= 3192 && x <= 3327 && y >= 3656 && y <= 3794 || //
						x >= 3150 && x <= 3332 && y >= 3781 && y <= 3905 || // red
																			// drags
						x >= 3013 && x <= 3290 && y >= 3856 && y <= 3903 || // red
																			// drags
						x >= 2947 && x <= 2958 && y >= 3816 && y <= 3825 || // temple
						x >= 2992 && x <= 3005 && y >= 3914 && y <= 3930 || // wildy
																			// course
						x >= 3150 && x <= 3195 && y >= 2958 && y <= 3003)

					return true;
			}
			/*
			 * New in multi boolean coords for outside wilderness
			 */
			int x = gc.getPosition().getX(), y = gc.getPosition().getY();
			if (x >= 2700 && x <= 2730 && y >= 9800 && y <= 9829 || x >= 3080 && x <= 3120 && y >= 5520 && y <= 5550) {
				return true;
			}

			/// dzanth
			if (x >= 2355 && x <= 2376 && y >= 4945 && y <= 4978) {
				return true;
			}

			/// Skilling boss spawn
			if (x >= 2320 && x <= 2329 && y >= 3698 && y <= 3704) {
				return true;
			}

			//Dark ranger spawn
			if (x >= 2206 && x <= 2222 && y >= 5090 && y <= 5109) {
				return true;
			}

			/// delzone
			if (x >= 3207 && x <= 3237 && y >= 2768 && y <= 2790) {
				return true;
			}
			
			/// wyrms
			if (x >= 2302 && x <= 2355 && y >= 4568 && y <= 4610) {
				return true;
			}
			//lucid warriors
			if (x >= 2544 && x <= 2569 && y >= 4945 && y <= 4977) {
				return true;
			}
			
			//sephiroth
			if (x >= 2591 && x <= 2615 && y >= 5715 && y <= 5739) {
				return true;
			}
			//azazel
			if (x >= 2455 && x <= 2479 && y >= 3359 && y <= 3386) {
				return true;
			}
			
			
			//killer pumpkin
			if (x >= 2392 && x <= 2410 && y >= 3225 && y <= 3248) {
				return true;
			}
			
			if (x >= 2978 && x <= 3120 && y >= 5174 && y <= 5256) {
				return true;
			}
			
			if (x >= 2406 && x <= 2426 && y >= 2850 && y <= 2874) {
				return true;
			}

			/// raid2
			if (x >= 2363 && x <= 2443 && y >= 5058 && y <= 5123) {
				return true;
			}
			if (x >= 2560 && x <= 2620 && y >= 3390 && y <= 3450) {
				return true;
			}
			if (x >= 1930 && x <= 1980 && y >= 4740 && y <= 4780) {
				return true;
			}
			
			if (x >= 2320 && x <= 2360 && y >= 4740 && y <= 4780) {
				return true;
			}
			if (x >= 2000 && x <= 2040 && y >= 4420 && y <= 4450) {
				return true;
			}
			/// raid3
			if (x >= 2240 && x <= 2290 && y >= 3050 && y <= 3100) {
				return true;
			}
			
			if (x >= 2700 && x <= 2730 && y >= 9170 && y <= 9200) {
				return true;
			}
			
			if (x >= 2600 && x <= 3000 && y >= 9700 && y <= 10100) {
				return true;
			}

			if (x >= 2900 && x <= 3100 && y >= 9500 && y <= 9700) {
				return true;
			}

			if (x >= 2350 && x <= 2600 && y >= 4500 && y <= 4850) {
				return true;
			}
			if (x >= 2511 && x <= 2550 && y >= 4633 && y <= 4680) {
				return true;
			}
			if (x >= 2437 && x <= 2561 && y >= 10112 && y <= 10179) {
				return true;
			}
			if (x >= 2605 && x <= 7000 && y >= 2800 && y <= 9000) {
				return true;
			}
			if (x >= 3155 && x <= 3169 && y >= 2972 && y <= 2991) {
				return true;
			}
			if (x >= 2987 && x <= 3078 && y >= 9536 && y <= 9604) {
				return false;
			}
			return gc.getLocation().multi;
		}

		public boolean isSummoningAllowed() {
			return summonAllowed;
		}

		public boolean isFollowingAllowed() {
			return followingAllowed;
		}
		
		public boolean setMulti(boolean multi) {
			return this.multi = multi;
		}

		public boolean isCannonAllowed() {
			return cannonAllowed;
		}

		public boolean isFiremakingAllowed() {
			return firemakingAllowed;
		}

		public boolean isAidingAllowed() {
			return aidingAllowed;
		}

		public static Location getLocation(Entity gc) {
			for (Location location : Location.values()) {
				if (location != Location.DEFAULT)
					if (inLocation(gc, location))
						return location;
			}
			return Location.DEFAULT;
		}

		public static boolean inLocation(Entity gc, Location location) {
			if (location == Location.DEFAULT) {
				if (getLocation(gc) == Location.DEFAULT)
					return true;
				else
					return false;
			}
			/*
			 * if(gc instanceof Player) { Player p = (Player)gc; if(location ==
			 * Location.TRAWLER_GAME) { String state = FishingTrawler.getState(p); return
			 * (state != null && state.equals("PLAYING")); } else if(location ==
			 * FIGHT_PITS_WAIT_ROOM || location == FIGHT_PITS) { String state =
			 * FightPit.getState(p), needed = (location == FIGHT_PITS_WAIT_ROOM) ? "WAITING"
			 * : "PLAYING"; return (state != null && state.equals(needed)); } else
			 * if(location == Location.SOULWARS) { return (SoulWars.redTeam.contains(p) ||
			 * SoulWars.blueTeam.contains(p) && SoulWars.gameRunning); } else if(location ==
			 * Location.SOULWARS_WAIT) { return SoulWars.isWithin(SoulWars.BLUE_LOBBY, p) ||
			 * SoulWars.isWithin(SoulWars.RED_LOBBY, p); } }
			 */
			return inLocation(gc.getPosition().getX(), gc.getPosition().getY(), location);
		}

		public static boolean inLocation(int absX, int absY, Location location) {
			int checks = location.getX().length - 1;
			for (int i = 0; i <= checks; i += 2) {
				if (absX >= location.getX()[i] && absX <= location.getX()[i + 1]) {
					if (absY >= location.getY()[i] && absY <= location.getY()[i + 1]) {
						return true;
					}
				}
			}
			return false;
		}

		public void process(Player player) {

		}

		public boolean canTeleport(Player player) {
			return true;
		}

		public void login(Player player) {

		}

		public void enter(Player player) {

		}

		public void leave(Player player) {

		}

		public void logout(Player player) {

		}

		public void onDeath(Player player) {

		}

		public boolean handleKilledNPC(Player killer, NPC npc) {
			return false;
		}

		public boolean canAttackPlayer(Player player, Player target) {
			return false;
		}

		public boolean canAttackNPC(Player player, NPC target) {
			return false;
		}

		/**
		 * SHOULD AN ENTITY FOLLOW ANOTHER ENTITY NO MATTER THE DISTANCE BETWEEN THEM?
		 **/
		public static boolean ignoreFollowDistance(Character character) {
			Location location = character.getLocation();
			return location == Location.FIGHT_CAVES || location == Location.RECIPE_FOR_DISASTER
					|| location == Location.NOMAD;
		}

		public static boolean inDrStrange(Character gc) {
			final int x = gc.getPosition().getX(), y = gc.getPosition().getY(), z = gc.getPosition().getZ();
			return x >= 3015 && x <= 3028 && y >= 2921 && y <= 2928 && z >= 7 && z <= 9;
		}

		public static boolean inTotodile(Character gc) {
			final int x = gc.getPosition().getX(), y = gc.getPosition().getY(), z = gc.getPosition().getZ();
			return x >= 3014 && x <= 3028 && y >= 2896 && y <= 2905 && z >= 23 && z <= 25;
		}
	}

	public static void process(Character gc) {
		Location newLocation = Location.getLocation(gc);
		if (gc.getLocation() == newLocation) {
			if (gc.isPlayer()) {
				Player player = (Player) gc;
				gc.getLocation().process(player);
				if (Location.inMulti(player)) {
					if (player.getMultiIcon() != 1)
						player.getPacketSender().sendMultiIcon(1);
				} else if (player.getMultiIcon() == 1)
					player.getPacketSender().sendMultiIcon(0);
			}
		} else {
			Location prev = gc.getLocation();
			if (gc.isPlayer()) {
				Player player = (Player) gc;
				if (player.getMultiIcon() > 0)
					player.getPacketSender().sendMultiIcon(0);
				if (player.walkableInterfaceList.size() > 0)
					// player.getPacketSender().sendWalkableInterface(-1);
					player.resetInterfaces();
				if (player.getPlayerInteractingOption() != PlayerInteractingOption.NONE)
					player.getPacketSender().sendInteractionOption("null", 2, true);
			}
			gc.setLocation(newLocation);
			if (gc.isPlayer()) {
				prev.leave(((Player) gc));
				gc.getLocation().enter(((Player) gc));
			}
		}
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		if (playerX == objectX && playerY == objectY)
			return true;
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean goodDistance(Position pos1, Position pos2, int distanceReq) {
		if (pos1.getZ() != pos2.getZ())
			return false;
		return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distanceReq);
	}

	public static int distanceTo(Position position, Position destination, int size) {
		final int x = position.getX();
		final int y = position.getY();
		final int otherX = destination.getX();
		final int otherY = destination.getY();
		int distX, distY;
		if (x < otherX)
			distX = otherX - x;
		else if (x > otherX + size)
			distX = x - (otherX + size);
		else
			distX = 0;
		if (y < otherY)
			distY = otherY - y;
		else if (y > otherY + size)
			distY = y - (otherY + size);
		else
			distY = 0;
		if (distX == distY)
			return distX + 1;
		return distX > distY ? distX : distY;
	}
}
