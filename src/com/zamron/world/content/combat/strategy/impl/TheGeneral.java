package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.model.Skill;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class TheGeneral implements CombatStrategy {

	/*
	 * 
	 * 
	 * private static final Animation attack_anim = new Animation(11665); private
	 * static final Animation attack_anim2 = new Animation(11665); private static
	 * final Graphic attack_graphic = new Graphic(1834);
	 * 
	 * 
	 */

	private static final Animation anim = new Animation(11665);
	private static final Animation anim2 = new Animation(11665);
	private static final Animation anim3 = new Animation(11665);

	private static final Graphic gfx1 = new Graphic(1935, 3, GraphicHeight.MIDDLE);
	private static final Graphic gfx2 = new Graphic(1834);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC cB = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (cB.isChargingAttack()) {
			return true;
		}
		Player target = (Player) victim;
		boolean stomp = false;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			if (Locations.goodDistance(t.getPosition(), cB.getPosition(), 1)) {
				stomp = true;
				cB.getCombatBuilder().setVictim(t);
				new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1, CombatType.MAGIC, true))
						.handleAttack();
			}
		}
		if (stomp) {
			cB.performAnimation(anim);
			victim.performGraphic(gfx1);
			cB.performGraphic(gfx2);

		}

		if (Locations.goodDistance(cB.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
			cB.performAnimation(anim);
			victim.performGraphic(gfx1);
			cB.getCombatBuilder().setContainer(new CombatContainer(cB, victim, 1, 2, CombatType.MELEE, true));
		} else if (Misc.getRandom(10) <= 7) {
			cB.performAnimation(anim2);
			cB.setChargingAttack(true);
			cB.getCombatBuilder().setContainer(new CombatContainer(cB, victim, 1, 2, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, cB, false) {
				@Override
				protected void execute() {
					if (cB.getConstitution() <= 0) {
						target.getPacketSender().sendMessage("The General was unsuccessful today.");

					}
					int skill = Misc.getRandom(6);
					Skill skillT = Skill.forId(skill);
					Player player = (Player) target;
					int lvl = player.getSkillManager().getCurrentLevel(skillT);
					lvl -= 5 + Misc.getRandom(6);
					player.getSkillManager().setCurrentLevel(skillT,
							player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ? 1 : lvl);
					target.getPacketSender()
							.sendMessage("Your " + skillT.getFormatName() + " has been slighly drained!");
					stop();
					new Projectile(cB, victim, 1901, 44, 3, 43, 31, 0).sendProjectile();
					cB.setChargingAttack(false).getCombatBuilder()
							.setAttackTimer(cB.getDefinition().getAttackSpeed() - 1);
					stop();
				}
			});
		} else {
			cB.performAnimation(anim3);
			victim.performGraphic(gfx2);
			cB.getCombatBuilder().setContainer(new CombatContainer(cB, victim, 1, 2, CombatType.MAGIC, true));
		}

		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 8;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

}

/*
 * 
 * private static final Animation attack_anim = new Animation(11665); private
 * static final Animation attack_anim2 = new Animation(11665); private static
 * final Graphic attack_graphic = new Graphic(1834);
 * 
 * @Override public boolean canAttack(Character entity, Character victim) {
 * return victim.isPlayer(); }
 * 
 * @Override public CombatContainer attack(Character entity, Character victim) {
 * return null; }
 * 
 * @Override public boolean customContainerAttack(Character entity, Character
 * victim) { NPC cB = (NPC) entity; if (cB.isChargingAttack() ||
 * cB.getConstitution() <= 0) { return true; } Player target = (Player) victim;
 * boolean stomp = false; for (Player t : Misc.getCombinedPlayerList(target)) {
 * if (t == null || t.getLocation() != Location.CORPOREAL_BEAST) continue; if
 * (Locations.goodDistance(t.getPosition(), cB.getPosition(), 1)) { stomp =
 * true; cB.getCombatBuilder().setVictim(t); new
 * CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1,
 * CombatType.MAGIC, true)) .handleAttack(); } } if (stomp) {
 * cB.performAnimation(attack_anim); cB.performGraphic(attack_graphic); }
 * 
 * int attackStyle = Misc.getRandom(4); if (attackStyle == 0 || attackStyle ==
 * 1) { // melee int distanceX = target.getPosition().getX() -
 * cB.getPosition().getX(); int distanceY = target.getPosition().getY() -
 * cB.getPosition().getY(); if (distanceX > 4 || distanceX < -1 || distanceY > 4
 * || distanceY < -1) attackStyle = 4; else {
 * 
 * cB.performAnimation(new Animation(attackStyle == 0 ? 10057 : 10058)); if
 * (target.getLocation() == Location.CORPOREAL_BEAST)
 * cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 1,
 * CombatType.MELEE, true)); return true; } } else if (attackStyle == 2) { //
 * powerfull mage spiky ball cB.performAnimation(attack_anim2);
 * cB.getCombatBuilder().setContainer(new CombatContainer(cB, target, 1, 2,
 * CombatType.MAGIC, true)); new Projectile(cB, target, 1825, 44, 3, 43, 43,
 * 0).sendProjectile(); } else if (attackStyle == 3) { // translucent ball of
 * energy cB.performAnimation(attack_anim2); if (target.getLocation() ==
 * Location.CORPOREAL_BEAST) cB.getCombatBuilder().setContainer(new
 * CombatContainer(cB, target, 1, 2, CombatType.MAGIC, true)); new
 * Projectile(cB, target, 1823, 44, 3, 43, 43, 0).sendProjectile();
 * TaskManager.submit(new Task(1, target, false) {
 * 
 * @Override public void execute() { int skill = Misc.getRandom(4); Skill skillT
 * = Skill.forId(skill); Player player = (Player) target; int lvl =
 * player.getSkillManager().getCurrentLevel(skillT); lvl -= 1 +
 * Misc.getRandom(4); player.getSkillManager().setCurrentLevel(skillT,
 * player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ? 1 : lvl);
 * target.getPacketSender() .sendMessage("Your " + skillT.getFormatName() +
 * " has been slighly drained!"); cB.setChargingAttack(false).getCombatBuilder()
 * .setAttackTimer(cB.getDefinition().getAttackSpeed() - 1);
 * 
 * stop(); } }); } if (attackStyle == 4) { cB.performAnimation(attack_anim2);
 * for (Player t : Misc.getCombinedPlayerList(target)) { if (t == null ||
 * t.getLocation() != Location.CORPOREAL_BEAST) continue; new Projectile(cB,
 * target, 1824, 44, 3, 43, 43, 0).sendProjectile();
 * cB.setChargingAttack(false).getCombatBuilder().setAttackTimer(cB.
 * getDefinition().getAttackSpeed() - 1);
 * 
 * } TaskManager.submit(new Task(1, target, false) {
 * 
 * @Override public void execute() { for (Player t :
 * Misc.getCombinedPlayerList(target)) { if (t == null || t.getLocation() !=
 * Location.CORPOREAL_BEAST) continue; cB.getCombatBuilder().setVictim(t); new
 * CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, t, 1,
 * CombatType.RANGED, true)) .handleAttack();
 * cB.setChargingAttack(false).getCombatBuilder()
 * .setAttackTimer(cB.getDefinition().getAttackSpeed() - 1); } stop(); } }); }
 * return true; }
 * 
 * @Override public int attackDelay(Character entity) { return
 * entity.getAttackSpeed(); }
 * 
 * @Override public int attackDistance(Character entity) { return 8; }
 * 
 * @Override public CombatType getCombatType() { return CombatType.MIXED; } }
 */