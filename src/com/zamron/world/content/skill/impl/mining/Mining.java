package com.zamron.world.content.skill.impl.mining;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.GameObject;
import com.zamron.model.Locations;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.ShootingStar;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.StarterTasks;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.content.StarterTasks.StarterTaskData;
import com.zamron.world.content.skill.impl.mining.MiningData.Ores;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.entity.impl.player.Player;

public class Mining {

	public static void startMining(final Player player, final GameObject oreObject) {
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if (!Locations.goodDistance(player.getPosition().copy(), oreObject.getPosition(), 1)
				&& oreObject.getId() != 24444 && oreObject.getId() != 24445 && oreObject.getId() != 38660)
			return;
		if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPacketSender().sendMessage("You do not have any free inventory space left.");
			return;
		}
		player.setInteractingObject(oreObject);
		player.setPositionToFace(oreObject.getPosition());
		final Ores o = MiningData.forRock(oreObject.getId());
		final boolean giveGem = o != Ores.Rune_essence && o != Ores.Pure_essence;
		final int reqCycle = o == Ores.Runite ? 6 + Misc.getRandom(2) : Misc.getRandom(o.getTicks() - 1);

		if (o != null) {
			final int pickaxe = MiningData.getPickaxe(player);
			final int miningLevel = player.getSkillManager().getCurrentLevel(Skill.MINING);
			if (pickaxe > 0) {
				if (miningLevel >= o.getLevelReq()) {
					final MiningData.Pickaxe p = MiningData.forPick(pickaxe);
					if (miningLevel >= p.getReq()) {
						player.performAnimation(new Animation(p.getAnim()));
						final int delay = o.getTicks() - MiningData.getReducedTimer(player, p);
						if (Misc.getRandom(15000) == 3) {
							player.getInventory().add(13321, 1);
							World.sendMessageNonDiscord("@blu@<img=12>[Skilling Pets] " + player.getUsername()
									+ " has received the Rock Golem pet!");
							player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
						}
						int chance = RandomUtility.random(100);
						if (chance >= 85 && chance <= 90) {
						ScavengeGain.Mining(player);
						}
						player.setCurrentTask(new Task(delay >= 2 ? delay : 1, player, false) {
							int cycle = 0;

							@Override
							public void execute() {
								if (player.getInteractingObject() == null
										|| player.getInteractingObject().getId() != oreObject.getId()) {
									player.getSkillManager().stopSkilling();
									player.performAnimation(new Animation(65535));
									stop();
									return;
								}
								if (player.getInventory().getFreeSlots() == 0) {
									player.performAnimation(new Animation(65535));
									stop();
									player.getPacketSender()
											.sendMessage("You do not have any free inventory space left.");
									return;
								}
								if (cycle != reqCycle) {
									cycle++;
									player.performAnimation(new Animation(p.getAnim()));
								}
								/*if (giveGem) {
									boolean onyx = (o == Ores.Runite || o == Ores.CRASHED_STAR)
											&& Misc.getRandom(o == Ores.CRASHED_STAR ? 20000 : 5000) == 1;
									if (onyx || Misc.getRandom(o == Ores.CRASHED_STAR ? 35 : 50) == 15) {
										int gemId = onyx ? 6571
												: MiningData.FuserIngredients[(int) (MiningData.FuserIngredients.length
														* Math.random())];
										player.getInventory().add(gemId, 1);
										player.getPacketSender().sendMessage("You have found a Blue Fusion Stone!");
										World.sendMessageNonDiscord("<img=12>@bla@ " + player.getUsername()
												+ " has just received a Blue Fusion Stone from mining" + "!");
										if (gemId == 6571) {
											String s = o == Ores.Runite ? "Runite ore" : "Crashed star";
											World.sendMessageNonDiscord("<img=12><col=009966> " + player.getUsername()
													+ " has just received an Uncut Onyx from mining a " + s + "!");
										}
									}
								}*/
								if (cycle == reqCycle) {
									if (o == Ores.Iron) {
										Achievements.finishAchievement(player, AchievementData.MINE_SOME_IRON);
									} else if (o == Ores.Runite) {
										Achievements.doProgress(player, AchievementData.MINE_25_RUNITE_ORES);
										Achievements.doProgress(player, AchievementData.MINE_2000_RUNITE_ORES);
										StarterTasks.doProgress(player, StarterTaskData.MINE_100_RUNE_ORES);
									}
									if (o.getItemId() != -1) {

										player.getInventory().add(o.getItemId(), 1);
										if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15259) {

											if (Misc.getRandom(3) == 1) {
												player.getInventory().add(o.getItemId(), 1);
											}
										}
									}
									if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15259) {
										if (Misc.getRandom(3) == 1) {
											player.getSkillManager().addExperience(Skill.MINING,
													(int) (o.getXpAmount() * 2.4));

										}
									}
									player.getSkillManager().addExperience(Skill.MINING, (int) (o.getXpAmount() * 1.4));

									if (o == Ores.CRASHED_STAR) {
										player.getPacketSender().sendMessage("You mine the crashed star..");
									} else {
										player.getPacketSender().sendMessage("You mine some ore.");
									}
									Sounds.sendSound(player, Sound.MINE_ITEM);
									cycle = 0;
									this.stop();
									if (o.getRespawn() > 0) {
										player.performAnimation(new Animation(65535));
										oreRespawn(player, oreObject, o);
									} else {
										if (oreObject.getId() == 38660) {
											/**if (ShootingStar.CRASHED_STAR == null
													|| ShootingStar.CRASHED_STAR.getStarObject()
															.getPickAmount() >= ShootingStar.MAXIMUM_MINING_AMOUNT) {**/
											if (ShootingStar.CRASHED_STAR == null) {
												player.getPacketSender().sendClientRightClickRemoval();
												player.getSkillManager().stopSkilling();
												return;
											} else {
												ShootingStar.CRASHED_STAR.getStarObject().incrementPickAmount();
											}
										} else {
											player.performAnimation(new Animation(65535));
										}
										startMining(player, oreObject);
									}
								}
							}
						});
						TaskManager.submit(player.getCurrentTask());
					} else {
						player.getPacketSender().sendMessage(
								"You need a Mining level of at least " + p.getReq() + " to use this pickaxe.");
					}
				} else {
					player.getPacketSender().sendMessage(
							"You need a Mining level of at least " + o.getLevelReq() + " to mine this rock.");
				}
			} else {
				player.getPacketSender().sendMessage("You don't have a pickaxe to mine this rock with.");
			}
		}
	}

	public static void oreRespawn(final Player player, final GameObject oldOre, Ores o) {
		if (oldOre == null || oldOre.getPickAmount() >= 1)
			return;
		oldOre.setPickAmount(1);
		for (Player players : player.getLocalPlayers()) {
			if (players == null)
				continue;
			if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
					.equals(player.getInteractingObject().getPosition().copy())) {
				players.getPacketSender().sendClientRightClickRemoval();
				players.getSkillManager().stopSkilling();
			}
		}
		player.getPacketSender().sendClientRightClickRemoval();
		player.getSkillManager().stopSkilling();
		CustomObjects.globalObjectRespawnTask(new GameObject(452, oldOre.getPosition().copy(), 10, 0), oldOre,
				o.getRespawn());
	}
}
