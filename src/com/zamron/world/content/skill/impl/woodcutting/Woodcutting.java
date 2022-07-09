package com.zamron.world.content.skill.impl.woodcutting;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.GameObject;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.EvilTrees;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.StarterTasks;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.content.StarterTasks.StarterTaskData;
import com.zamron.world.content.skill.impl.firemaking.Logdata;
import com.zamron.world.content.skill.impl.firemaking.Logdata.logData;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zamron.world.content.skill.impl.woodcutting.WoodcuttingData.Trees;
import com.zamron.world.entity.impl.player.Player;

public class Woodcutting {

	public static void cutWood(final Player player, final GameObject object, boolean restarting) {
		if (!restarting)
			player.getSkillManager().stopSkilling();
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You don't have enough free inventory space.");
			return;
		}
		player.setPositionToFace(object.getPosition());
		final int objId = object.getId();
		final Hatchet h = Hatchet.forId(WoodcuttingData.getHatchet(player));
		if (Misc.getRandom(25000) == 3) {
			player.getInventory().add(13322, 1);
			World.sendMessageNonDiscord("@blu@<img=12>[Skilling Pets] " + player.getUsername() + " has received the Beaver pet!");
			player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
		}
		if (h != null) {
			if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= h.getRequiredLevel()) {
				final WoodcuttingData.Trees t = WoodcuttingData.Trees.forId(objId);
				if (t != null) {
					player.setEntityInteraction(object);
					if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= t.getReq()) {
						player.performAnimation(new Animation(h.getAnim()));
						int delay = Misc.getRandom(t.getTicks() - WoodcuttingData.getChopTimer(player, h)) + 1;
						player.setCurrentTask(new Task(1, player, false) {
							int cycle = 0, reqCycle = delay >= 2 ? delay : Misc.getRandom(1) + 1;

							@Override
							public void execute() {
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									player.getPacketSender().sendMessage("You don't have enough free inventory space.");
									this.stop();
									return;
								}
								int chance = RandomUtility.random(100);
								if (chance >= 85 && chance <= 90) {
								ScavengeGain.WC(player);
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(h.getAnim()));
								} else if (cycle >= reqCycle) {
									int xp = t.getXp();
									if (lumberJack(player))
										xp *= 1.5;

									player.getSkillManager().addExperience(Skill.WOODCUTTING, xp);
									if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 6739) {
										if (Misc.getRandom(3) == 1) {
											xp *= 1.5;
											player.getSkillManager().addExperience(Skill.WOODCUTTING, xp);

										}
									}
									cycle = 0;
									//BirdNests.dropNest(player);
									this.stop();
									if (object.getId() == 11434) {
										if (EvilTrees.SPAWNED_TREE == null || EvilTrees.SPAWNED_TREE.getTreeObject()
												.getCutAmount() >= EvilTrees.MAX_CUT_AMOUNT) {
											player.getPacketSender().sendClientRightClickRemoval();
											player.getSkillManager().stopSkilling();
											return;
										} else {
											EvilTrees.SPAWNED_TREE.getTreeObject().incrementCutAmount();
										}
										// } else {
										// player.performAnimation(new Animation(65535));
									}
									if (!t.isMulti()) {
										// player.performAnimation(new Animation(65535));
										if (object.getId() == 11434) {
											return;
										}
										treeRespawn(player, object);
										player.getPacketSender().sendMessage("You've chopped the tree down.");
										StarterTasks.doProgress(player, StarterTaskData.CUT_1000_LOGS, 1);
									} else {
										cutWood(player, object, true);
										if (t == Trees.EVIL_TREE) {
											player.getPacketSender().sendMessage("You cut the Evil Tree...");
										} else {
											//StarterTasks.doProgress(player, StarterTaskData.CHOP_500_TREES, 100);
											player.getPacketSender().sendMessage("You get some logs..");
											StarterTasks.doProgress(player, StarterTaskData.CUT_1000_LOGS);
										}
									}
									Sounds.sendSound(player, Sound.WOODCUT);
									if (!(infernoAdze(player) && Misc.getRandom(5) <= 2)) {

										player.getInventory().add(t.getReward(), 1);
										if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 6739) {
											if (Misc.getRandom(3) == 1) {
												player.getInventory().add(t.getReward(), 1);

											}

										}
									} else if (Misc.getRandom(5) <= 2) {
										logData fmLog = Logdata.getLogData(player, t.getReward());
										if (fmLog != null) {
											player.getSkillManager().addExperience(Skill.FIREMAKING, fmLog.getXp());
											player.getPacketSender().sendMessage(
													"Your Inferno Adze burns the log, granting you Firemaking experience.");
											if (fmLog == Logdata.logData.OAK) {
												Achievements.finishAchievement(player, AchievementData.BURN_AN_OAK_LOG);
											} else if (fmLog == Logdata.logData.MAGIC) {
												Achievements.doProgress(player, AchievementData.BURN_100_MAGIC_LOGS);
												Achievements.doProgress(player, AchievementData.BURN_2500_MAGIC_LOGS);
											}
										}
									}
									int chance1 = RandomUtility.inclusiveRandom(10000);

									if (t == Trees.MAGIC || t == Trees.CUSTOM_TREE) {
									    if (chance1 >= 9990) {
									        player.getInventory().add(9436, 1);
									        World.sendMessageNonDiscord(
									                "<img=12>@blu@[WOODCUTTING]<img=12> @red@" + player.getUsername()
									                        + " @blu@Has just received Strange Fiber from woodcutting");
									    }
									} else if (chance1 >= 9997) {
									    World.sendMessageNonDiscord(
									            "<img=12>@blu@[WOODCUTTING]<img=12> @red@" + player.getUsername()
									                    + " @blu@Has just received Strange Fiber from woodcutting");
									    player.getInventory().add(9436, 1);
									}
									if (t == Trees.OAK) {
										Achievements.finishAchievement(player, AchievementData.CUT_AN_OAK_TREE);
									} else if (t == Trees.MAGIC) {
										Achievements.doProgress(player, AchievementData.CUT_100_MAGIC_LOGS);
										Achievements.doProgress(player, AchievementData.CUT_5000_MAGIC_LOGS);
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Woodcutting level of at least " + t.getReq() + " to cut this tree.");
					}
				}
			} else {
				player.getPacketSender()
						.sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
			}
		} else {
			player.getPacketSender().sendMessage("You do not have a hatchet that you can use.");
		}
	}

	public static boolean lumberJack(Player player) {
		return player.getEquipment().get(Equipment.HEAD_SLOT).getId() == 10941
				&& player.getEquipment().get(Equipment.BODY_SLOT).getId() == 10939
				&& player.getEquipment().get(Equipment.LEG_SLOT).getId() == 10940
				&& player.getEquipment().get(Equipment.FEET_SLOT).getId() == 10933;
	}

	public static boolean infernoAdze(Player player) {
		return player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13661;
	}

	public static void treeRespawn(final Player player, final GameObject oldTree) {
		if (oldTree == null || oldTree.getPickAmount() >= 1)
			return;
		oldTree.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getSkillManager().stopSkilling();
				players.getPacketSender().sendClientRightClickRemoval();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(1343, oldTree.getPosition().copy(), 10, 0), oldTree,
				20 + Misc.getRandom(10));
	}

}
