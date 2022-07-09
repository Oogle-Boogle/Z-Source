package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.*;
import com.zamron.model.Locations.Location;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.*;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.NpcTasks.NpcTaskData;
import com.zamron.world.content.StarterTasks.StarterTaskData;
import com.zamron.world.content.combat.DailyNPCTask;
import com.zamron.world.content.combat.TenKMassacre;
import com.zamron.world.content.combat.bossminigame.BossMinigameFunctions;
import com.zamron.world.content.combat.strategy.impl.Death;
import com.zamron.world.content.combat.strategy.impl.HarLakkRiftsplitter;
import com.zamron.world.content.combat.strategy.impl.KalphiteQueen;
import com.zamron.world.content.combat.strategy.impl.Nex;
import com.zamron.world.content.combat.strategy.impl.SuicsBoss;
import com.zamron.world.content.multi.MultiBoss;
import com.zamron.world.content.multi.MultiBossHandler;
import com.zamron.world.content.multi7.MultiBoss7;
import com.zamron.world.content.multi7.MultiBoss7Handler;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.raids.RaidNpc;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.content.skill.impl.slayer.Slayer;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.zamron.world.content.skillingboss.SkillBossConfig;
import com.zamron.world.content.skillingboss.SkillBossHandler;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerSaving;
import java.util.*;
import static com.zamron.world.content.skill.impl.pvm.NpcGain.BossesXP;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 *
 * @author relex lawl
 *
 * @Edited @Fixed Oogleboogle
 */

public class NPCDeathTask extends Task {

	private Set<Integer> Tier1to3 = new HashSet<>(
			Arrays.asList(420, 842, 174, 3767, 51, 2783, 17, 422, 3263, 15, 1982));
	private Set<Integer> Tier4to6 = new HashSet<>(
			Arrays.asList(9994, 9932, 224, 1999, 16, 9993, 9277, 9944, 9273, 9903, 8133));
	private Set<Integer> Tier7to9 = new HashSet<>(
			Arrays.asList(9247, 8493, 9203, 172, 9935, 170, 169, 219, 12239, 3154));
	private Set<Integer> Tier10 = new HashSet<>(Arrays.asList(33, 1684, 5957, 5958, 5959, 185, 6311));

	int[] justiciarIds = new int[] { 9903, 8133 };

	private Set<Integer>BOSS3P = new HashSet<>(Arrays.asList(9932, 224, 1999)); //3Boss points

	private Set<Integer>BOSS5P = new HashSet<>(Arrays.asList(168, 9993, 9277));//5Boss points

	private Set<Integer>BOSS7P = new HashSet<>(Arrays.asList(33, 9273, 9903, 111));//7Boss points

	private Set<Integer>BOSS9P = new HashSet<>(Arrays.asList(8133, 9247, 8493));//9Boss points

	private Set<Integer>BOSS11P = new HashSet<>(Arrays.asList(9203, 172, 9935, 110));//11Boss points

	private Set<Integer>BOSS13P = new HashSet<>(Arrays.asList(170, 219, 12239, 12240));//13Boss points

	private Set<Integer>BOSS15P = new HashSet<>(Arrays.asList(3054, 1684, 1685, 5957, 5958, 109, 5959, 185, 6311, 169, 171, 3054));//15Boss points

	//private Set<Integer> BOSSES = new HashSet<>(Arrays.asList(NPC IDS HERE));

	/**
	 * The NPCDeathTask constructor.
	 *
	 * @param npc The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;

	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute() {
		try {
			if (killer != null && !killer.isMiniMe) {
				killer.resetRichPresence();
			}
			npc.setEntityInteraction(null);
			switch (ticks) {
				case 2:
					npc.getMovementQueue().setLockMovement(false).reset();
					killer = npc.getCombatBuilder().getKiller(
							!(npc instanceof MultiBoss)
									&& !(npc instanceof MultiBoss7)
									&& npc.getId() != 8507 && npc.getId() != 2745 && npc.getId() != 25 && npc.getId() != 299
									&& npc.getId() != 6309 && npc.getId() != 8548 && npc.getId() != 8949 && npc.getId() != 6593
									&& npc.getId() != 9993 && npc.getId() != 9903 && npc.getId() != 2005 && npc.getId() != 421
									&& npc.getId() != 6313 && npc.getId() != 9913 && npc.getId() != 422 && npc.getId() != 7286
									&& npc.getId() != 1059 && npc.getId() != 2862
									&& npc.getId() != 9944 && npc.getId() != SkillBossConfig.npcID);

					if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
						npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

					/** CUSTOM NPC DEATHS **/
					if (npc.getId() == 13447) {
						Nex.handleDeath();
					}
					/**if (npc.getId() == 2862) {
						Death.handleDeath();
						TheDeath.handleDrop(npc);
					}**/

					break;
				case 0:
					if (killer != null) {
						if (killer.isMiniMe) {
							killer = killer.getMinimeOwner();
							if (!World.getPlayers().contains(killer)) {
								stop();
								return;
							}
						}
					}
					if (killer != null) {
						killer.setNpcKills(killer.getNpcKills() + 1);
						PlayerSaving.save(killer);
						if (npc instanceof RaidNpc) {
							OldRaidParty party = killer.getOldRaidParty();
							RaidNpc raidNpc = (RaidNpc) npc;
							if (party != null) {

								if (party.getCurrentRaid().getStage() == raidNpc.getStageRequiredToAttack()) {
									party.getCurrentRaid().nextLevel();
								}
								stop();
								return;
							}
						}

						killer.handleKeyRates(killer, npc);

						if (BOSS3P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 6 : 3;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 3 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS5P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 10 : 5;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 5 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS7P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 14 : 7;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 7 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS9P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 18 : 9;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 9 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS11P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 22 : 11;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 11 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS13P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 26 : 13;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 13 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}
						if (BOSS15P.contains(npc.getId())) {
							int points = GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_BOSS_POINTS ? 30 : 15;
							points += killer.getRights().getBonusBossPts();
							killer.setBossPoints(killer.getBossPoints() + points);
							killer.getSkillManager().addExperience(Skill.PVM, BossesXP);
							String pnts = killer.getBossPoints() == 15 ? "Point" : "Points";
							killer.sendMessage("<img=0>You now have @red@" + killer.getBossPoints() + " Boss " + pnts + "!");
						}

						killer.addNpcKillCount(npc.getId());
						killer.getStarterProgression().handleKill(npc.getId());
						killer.sendMessage("@blu@You now have @blu@" + killer.getNpcKillCount(npc.getId()) + "@red@ "
								+ npc.getDefinition().getName() + "@red@ KC!");


						if (Tier1to3.contains(npc.getId())) {
							killer.getInventory().addItem(19864, 1);
							//killer.sendMessage("@blu@You received 1x Starter ticket for killing an T1 - T3 NPC");
							// taxbag
							int chance = RandomUtility.inclusiveRandom(0, 100);
							if (chance >= 97 && chance <= 100) {
								//World.sendStaffMessage("Testing for the chance: " + chance);
								//killer.sendMessage("@red@You received 10x Tax Bag for killing this NPC");
								killer.getInventory().add(10835, 10);
							} else if (chance >= 0 && chance <= 97) {
								killer.getInventory().add(10835, 1);
								//killer.sendMessage("@red@You received 1x Tax Bag for killing this NPC");
							}

							// expcustomskill
							if (killer.getOldRaidParty() == null && !(npc instanceof RaidNpc)) {
								NpcGain.GainBossesXP(killer);
							} else if (killer.getOldRaidParty() != null && npc instanceof RaidNpc) {
								NpcGain.RaidNPCBossXP(killer, npc);
							}

							killer.incrementTotalBossKills(1);
						}

						//PLACE BOSS POINTS HERE

						final int CASH = 10835;

						// 1b coin drops
						if (npc.getId() == 170 || npc.getId() == 169 || npc.getId() == 184 || npc.getId() == 12239 || npc.getId() == 3154 || npc.getId() == 33 || npc.getId() == 292 || npc.getId() == 5957 || npc.getId() == 5958
								|| npc.getId() == 5959 || npc.getId() == 185 || npc.getId() == 6311) {

							if (Misc.random(1000) <= 25) { // 47 / 1000
								killer.sendMessage("@blu@[RARE DROP]: You get some Tokens for Frankenstiens Minigame");
								killer.giveItem(8851, 100);
							}

							int easyAmount = Misc.random(30);
							if ((killer.getInventory().contains(CASH) || killer.getInventory().getFreeSlots() > 0)) {
								killer.getInventory().addItem(CASH, easyAmount);
								killer.sendMessage("@bla@[CASH]: You get " + easyAmount + " 1b coins.");
							} else {
								killer.sendMessage("You do not have room to collect your cash. Please free up atleast 1 space.");
							}
						}

						if (npc.getId() == 17 || npc.getId() == 422 || npc.getId() == 3253 || npc.getId() == 15 || npc.getId() == 9994 || npc.getId() == 9932 || npc.getId() == 224 || npc.getId() == 1999
								|| npc.getId() == 16 || npc.getId() == 9993 || npc.getId() == 9277 || npc.getId() == 9935 || npc.getId() == 9273 || npc.getId() == 9903 || npc.getId() == 3034
								|| npc.getId() == 9247 || npc.getId() == 8493 || npc.getId() == 9203 || npc.getId() == 172 || npc.getId() == 184) {

							if (Misc.random(1000) <= 50) { // 48 / 1000
								killer.sendMessage("@blu@[RARE DROP]: You get some Tokens for Frankenstiens Minigame");
								killer.giveItem(8851, 70);
							}

							int easyAmount = Misc.random(30);
							if ((killer.getInventory().contains(CASH) || killer.getInventory().getFreeSlots() > 0)) {
								killer.getInventory().addItem(CASH, easyAmount);
								killer.sendMessage("@bla@[CASH]: You get " + easyAmount + " 1b Coins.");
							} else {
								killer.sendMessage("You do not have room to collect your cash. Please free up atleast 1 space.");
							}
						}

						if (npc.getId() == 4455 || npc.getId() == 4457 || npc.getId() == 4459 || npc.getId() == 4456 || npc.getId() == 4462 || npc.getId() == 4409) {

							if (Misc.random(1000) <= 50) { // 50 / 1000 {
								killer.sendMessage("You get some Tokens for Frankenstiens Minigame");
								killer.giveItem(8851, 10);
							}

							int easyAmount = Misc.random(10);
							if ((killer.getInventory().contains(CASH) || killer.getInventory().getFreeSlots() > 0)) {
								killer.getInventory().addItem(CASH, easyAmount);
								killer.sendMessage("@bla@[CASH]: You get " + easyAmount + " 1b Coins.");
							} else {
								killer.sendMessage("You do not have room to collect your cash. Please free up atleast 1 space.");
							}
						}


						if (npc.getId() == 9280) {
							if (killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
								killer.incrementMinionsKC(2);
								killer.sendMessage("@red@You now have: @blu@" + killer.getMinionsKC()
										+ " Custom Rex Minions Kill-count");
							} else {
								killer.incrementMinionsKC(1);
								killer.sendMessage("@red@You now have: @blu@" + killer.getMinionsKC()
										+ " Custom Rex Minions Kill-count");
							}

						}
						if (npc.getId() == 9855 && killer.getEquipment().contains(5131)) {
							killer.getPointsHandler().incrementMiniGamePoints1(1);
							killer.sendMessage("@red@Since you're wearing DMG you recieve an extra Minigame1 point");
							killer.sendMessage("@blu@You Now Have " + killer.getPointsHandler().getminiGamePoints1()
									+ " Minigame1 Points");
						}
						if (npc.getId() == 9176) {
							int chance = RandomUtility.random(100);
							if (chance >= 95) {
								killer.getPointsHandler().incrementMiniGamePoints1(1);
								killer.sendMessage("You have received a MiniGamePoint1 you now have "
										+ killer.getPointsHandler().getminiGamePoints1() + " Minigame1 Points");
							}
						}
						if (npc.getId() == 8549) {
							int chance = RandomUtility.random(1000);
							if (chance >= 1000) {
								killer.getPointsHandler().incrementMiniGamePoints3(1);
								killer.sendMessage("Gj you got a Minigamepoint3!! You now have: "
										+ killer.getPointsHandler().getminiGamePoints3() + " Minigame3 Points");
							}
						}

						if (killer.isInRaid()) {
							killer.getRaidParty().getOwner().getCustomRaid().handleKill();
						}

						if (npc.getId() == 8572) {
							killer.getDefendersMg().handleKill();
						}

						if (npc.getId() == 8573) {

							int random = RandomUtility.inclusiveRandom(0, 100);

							if (random > 95) {
								killer.getInventory().add(5206, 10);
								killer.sendMessage("[LUCKY DROP] You got 10 tokens from this kill");
							} else if (random > 60) {
								killer.getInventory().add(5206, 2);
								killer.sendMessage("You got 2 tokens from this kill");

								killer.getInventory().add(5206, 1);
								// killer.sendMessage("You got 1 token from this kill.");
							}
						}
						if (npc.getId() == 420) { // Joker
							NpcTasks.doProgress(killer, NpcTaskData.KILL_50_JOKER);
						}
						if (npc.getId() == 51) { // Frost dragons
							NpcTasks.doProgress(killer, NpcTaskData.KILL_100_FROST_DRAGONS);
						}
						if (npc.getId() == 2783) { // Sirenic beasts
							NpcTasks.doProgress(killer, NpcTaskData.KILL_150_SIRENIC_BEASTS);
						}
						if (npc.getId() == 15) { // Hades
							NpcTasks.doProgress(killer, NpcTaskData.KILL_250_HADES);
						}
						if (npc.getId() == 9994) { // Shaman Defenders
							NpcTasks.doProgress(killer, NpcTaskData.KILL_300_DEFENDERS);
						}
						if (npc.getId() == 224) { // Demonic Olm
							NpcTasks.doProgress(killer, NpcTaskData.KILL_250_DEMONIC_OLMS);
						}
						if (npc.getId() == 1999) { // Cerb
							NpcTasks.doProgress(killer, NpcTaskData.KILL_50_CEREBRUS);
						}
						if (npc.getId() == 16) { // Abbadon
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_ZEUS);
						}
						if (npc.getId() == 9993) { // Custom infartico
							NpcTasks.doProgress(killer, NpcTaskData.KILL_50_INFARTICO);
						}
						if (npc.getId() == 9277) { // Lord Valor
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_LORD_VALOR);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_LORD_VALOR);
						}
						if (npc.getId() == 9273) { // Dzanth
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_DZANTH);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_3000_DZANTH);
						}
						if (npc.getId() == 9903) { // King Kong
							NpcTasks.doProgress(killer, NpcTaskData.KILL_50_KINGKONG);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_KINGKONG);
						}
						if (npc.getId() == 8133) { // CORP
							NpcTasks.doProgress(killer, NpcTaskData.KILL_50_CORP_BEAST);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_CORP_BEAST);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_CORP_BEAST);
						}
						if (npc.getId() == 9247) { // Lucid Warriors
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_LUCID_WARRIORS);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_LUCID_WARRIORS);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_LUCID_WARRIORS1);
						}
						if (npc.getId() == 8493) { // HULK
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_HULK);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HULK);
						}
						if (npc.getId() == 9203) { // DARKBLUE WIZARD Dragon
							NpcTasks.doProgress(killer, NpcTaskData.KILL_500_DARKBLUE_WIZARDS);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_DARKBLUE_WIZARDS);
						}
						if (npc.getId() == 172) { // Ice Warrior
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HEATED_PYRO);
						}
						if (npc.getId() == 9935) { // WYRM
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_WYRM);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_WYRM);
						}
						if (npc.getId() == 170) { // TRINITY
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_TRINITY);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_TRINITY);
						}
						if (npc.getId() == 169) { // CLOUD
							NpcTasks.doProgress(killer, NpcTaskData.KILL_2500_CLOUD);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_CLOUD);
						}

						if (npc.getId() == 219) { // ROUGE
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_HERBAL_ROGUE);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_HERBAL_ROGUE);
						}
						if (npc.getId() == 12239) { // Exoden
							NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_EXODEN);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_EXODEN);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_EXODEN);
						}
						if (npc.getId() == 3154) { // Nex
							NpcTasks.doProgress(killer, NpcTaskData.KILL_1000_SUPREME_NEX);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_5000_SUPREME_NEX);
							NpcTasks.doProgress(killer, NpcTaskData.KILL_10000_SUPREME_NEX);
						}

						if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
								&& killer.getSummoning().getFamiliar().getSummonNpc().getId() == 3032) {
							//killer.incrementNPCKills(1);
							killer.incrementNPCKills(+1);
							System.out.println("Added extra kill for jad pet");
						}

						if (npc.getId() == 6306) {
							int chance = RandomUtility.random(1000);
							if (chance >= 1000) {
								killer.getPointsHandler().incrementMiniGamePoints4(1);
								killer.sendMessage("You have received a Minigamepoint4!! You now have: "
										+ killer.getPointsHandler().getminiGamePoints4() + " Minigame4 Points");
							}
						}
						if (npc.getId() == 8281) {
							killer.forceChat("Aaahh, that hurts!!");
							killer.dealDamage(new Hit(Misc.random(100, 800), Hitmask.DARK_PURPLE, CombatIcon.DEFLECT));
						}
						if (npc.getId() == 2060) {
							int chance = RandomUtility.random(10000);
							if (chance >= 10000) {
								killer.getPointsHandler().incrementMiniGamePoints5(1);
								killer.sendMessage("LUCKY! You have received a Minigamepoint5, You now have "
										+ killer.getPointsHandler().getminiGamePoints5() + " Minigame5 points");
							}
						}

						if (npc.getId() == 6593) {
							SuicsBoss.resetAll();
						}

						Achievements.doProgress(killer, AchievementData.DEFEAT_10000_MONSTERS);
						if (npc.getId() == 50) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_KING_BLACK_DRAGON);
						} else if (npc.getId() == 3200) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CHAOS_ELEMENTAL);
						} else if (npc.getId() == 8349) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_A_TORMENTED_DEMON);
						} else if (npc.getId() == 3491) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CULINAROMANCER);
						} else if (npc.getId() == 12840 || npc.getId() == 12841) {
							StarterTasks.finishTask(killer, StarterTaskData.KILL_WARMONGER);
						} else if (npc.getId() == 8528) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_NOMAD);
						} else if (npc.getId() == 2745) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_JAD);
							StarterTasks.doProgress(killer, StarterTaskData.KILL_10_JADS);
						} else if (npc.getId() == 5996) {
							StarterTasks.doProgress(killer, StarterTaskData.KILL_10_GLODS);
						} else if (npc.getId() == 4540) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_BANDOS_AVATAR);
						} else if (npc.getId() == 6260) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_GENERAL_GRAARDOR);
							killer.getAchievementAttributes().setGodKilled(0, true);
						} else if (npc.getId() == 6222) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_KREE_ARRA);
							killer.getAchievementAttributes().setGodKilled(1, true);
						} else if (npc.getId() == 6247) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_COMMANDER_ZILYANA);
							killer.getAchievementAttributes().setGodKilled(2, true);
						} else if (npc.getId() == 6203) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_KRIL_TSUTSAROTH);
							killer.getAchievementAttributes().setGodKilled(3, true);
						} else if (npc.getId() == 8133) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_THE_CORPOREAL_BEAST);
						} else if (npc.getId() == 3154) {
							Achievements.finishAchievement(killer, AchievementData.DEFEAT_NEX);
							killer.getAchievementAttributes().setGodKilled(4, true);
						}
						/** ACHIEVEMENTS **/
						switch (killer.getLastCombatType()) {
							case MAGIC:
								Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MAGIC);
								break;
							case MELEE:
								Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_MELEE);
								break;
							case RANGED:
								Achievements.finishAchievement(killer, AchievementData.KILL_A_MONSTER_USING_RANGED);
								break;
						}


						killer.getDpsOverlay().resetDamageDone(); // will work now
						/** LOCATION KILLS **/
						if (npc.getLocation().handleKilledNPC(killer, npc)) { //todo check
							stop();
							return;
						}

						if(npc instanceof MultiBoss)
							MultiBossHandler.onDeath((MultiBoss)npc);
						if(npc instanceof MultiBoss7)
							MultiBoss7Handler.onDeath((MultiBoss7)npc);
						/*
						 * Halloween event dropping
						 */

						if (npc.getId() == 1973) {
							TrioBosses.handleSkeleton(killer, npc.getPosition());
						}
						if (npc.getId() == 75) {
							TrioBosses.handleZombie(killer, npc.getPosition());
						}
						if (npc.getId() == 103) {
							TrioBosses.handleGhost(killer, npc.getPosition());
						}
						if (npc.getId() == 3334) {
							Wildywyrm.giveLoot(killer, npc, npc.getPosition());
						}

						/*
						 * End Halloween event dropping
						 */

						/**
						 * Keys Event Monsters
						 **/
						if (npc.getId() == 7134) {
							KeysEvent.handleSkotizo(killer, npc.getPosition());
						}
						if (npc.getId() == 8549) {
							KeysEvent.handlePhoenix(killer, npc.getPosition());
						}
						if (npc.getId() == 499) {
							KeysEvent.handleThermo(killer, npc.getPosition());
						}
						if (npc.getId() == 2060) {
							KeysEvent.handleSlashBash(killer, npc.getPosition());
						}
						if (npc.getId() == 2642) {
							KeysEvent.handleKBD(killer, npc.getPosition());
						}
						if (npc.getId() == 1999) {
							KeysEvent.handleCerb(killer, npc.getPosition());
						}
						if (npc.getId() == 7134) {
							KeysEvent.handleBork(killer, npc.getPosition());
						}
						if (npc.getId() == 1382) {
							KeysEvent.handleGlacor(killer, npc.getPosition());
						}
						if (npc.getId() == 6766) {
							KeysEvent.handleShaman(killer, npc.getPosition());
						}
						if (npc.getId() == 941) {
							KeysEvent.handleGreenDragon(killer, npc.getPosition());
						}
						if (npc.getId() == 55) {
							KeysEvent.handleBlueDragon(killer, npc.getPosition());
						}
						if (npc.getId() == 1615) {
							KeysEvent.handleAbbyDemon(killer, npc.getPosition());
						}

						/** PARSE DROPS **/
						if (npc.getLocation() != Location.BOSS_TIER_LOCATION) {
							if (npc.getId() == 9911) {
								HarLakkRiftsplitter.handleDrop(npc);
							}

							if (npc.getId() == 25) {
								TheSeph.handleDrop(npc);
							}
							if (npc.getId() == 8949) {
								Juggernaut.handleDrop(npc);
							}
							if (npc.getId() == 25) {
								TheSeph.handleDrop(npc);
							}
							if (npc.getId() == 1059) {
								Broly.handleDrop(npc);
							}
							if (npc.getId() == 2005) {
								TheMay.handleDrop(npc);
							}
							if (npc.getId() == 421) {
								TheRick.handleDrop(npc);
							}
							if (npc.getId() == 422) {
								Onslaught.handleDrop(npc);
							}
							if (npc.getId() == 2745) {
								Tztok.handleDrop(npc);
							}
							if (npc.getId() == 6306) {
								SkillBossHandler.calculateDamage(npc);
							}
							if (npc.getId() == 299) {
								DarkRanger.handleDrop(npc);
							}
							if (npc.getId() == 9944) {
								Assassin.handleDrop(npc);
							} else {
								NPCDrops.dropItems(killer, npc); //todo check
							}
						}
						/** SLAYER **/
						if (killer.getSlayer().getSlayerTask() == SlayerTasks.MAGIC_SPIDER && npc.getId() == 2000) {
							killer.getSlayer().killedNpc(npc);
							killer.getSlayer().handleSlayerTaskDeath(true);
							//Slayer.handleSlayerTaskDeath(true); //DO NOT MAKE STATIC
							System.out.println("Killed venenatis");
							stop();
							break;
						}
						killer.getSlayer().killedNpc(npc);
						System.out.println("Counted");
					}
					stop();
					break;
			}
			ticks--;
		} catch (Exception e) {
			System.out.println("ERROR IN NPCDEATHTASK .. "+e.getMessage());
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		setEventRunning(false);
		npc.setDying(false);


		if (npc.getLocation() == Location.BOSS_TIER_LOCATION) {
			if (killer.currentBossWave <= 4 && !killer.isShouldGiveBossReward()) {
				killer.currentBossWave++;
				killer.setShouldGiveBossReward(true);
				killer.forceChat("I should leave now!");
				BossMinigameFunctions.despawnNpcs(killer);
			}

			if (killer.currentBossWave <= 4) {
				World.sendMessageNonDiscord("@bla@[@blu@" + killer.getUsername() + "@bla@]@red@ has just completed wave " + (killer.getCurrentBossWave()) + " at OS Battle Arena!");
			}
			if (killer.currentBossWave == 5) {
				World.sendMessageNonDiscord("@bla@[@blu@" + killer.getUsername() + "@bla@]@red@ has just killed completed the final wave at OS Battle Arena!");
			}

			TaskManager.submit(new Task(2, killer, false) {
				@Override
				public void execute() {
					killer.moveTo(BossMinigameFunctions.ARENA_ENTRANCE);
					stop();
				}
			});
		}

		TenKMassacre.incrementServerKills(killer,1); // Add killer to list and increment server kills by 1

		if (npc.getId() == DailyNPCTask.CHOSEN_NPC_ID) {
			DailyNPCTask.countPlayerKill(killer);
		}

		// respawn
		if (npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD
				&& npc.getLocation() != Location.DUNGEONEERING && npc.getLocation() != Location.BOSS_TIER_LOCATION
				&& npc.getLocation() != Location.INSTANCE_ARENA && !killer.isInRaid() && !(npc instanceof RaidNpc)) {

			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime(), killer));
			System.out.println("setting respawn task for npc: " + npc.getId() + " OBJECT: " + npc.getDefinition().getName());
		} else {
			System.out.println("Not setting respawn task for npc: " + npc.getId() + " OBJECT: " + npc.getDefinition().getName());
		}
		World.deregister(npc);

		if (npc.getId() == 1158 || npc.getId() == 1160) {
			KalphiteQueen.death(npc.getId(), npc.getPosition());
		}
		if (Nex.nexMob(npc.getId())) {
			Nex.death(npc.getId());
		}
	}
}
