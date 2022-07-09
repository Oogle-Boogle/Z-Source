package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Locations;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Warmonger implements CombatStrategy {

	private static final Animation anim = new Animation(14373);
	private static final Animation anim2 = new Animation(14375);
	private static final Animation anim3 = new Animation(14376);

	private static final Graphic gfx1 = new Graphic(1198);
	private static final Graphic gfx2 = new Graphic(1198);

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
		NPC war = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (war.isChargingAttack()) {
			return true;
		}
		//System.out.println("hp : " + war.getConstitution());
		Player target = (Player) victim;

		if (war.getConstitution() <= 15000 && !target.getWarmonger()) {
			target.setWarmonger(true);
			war.performAnimation(new Animation(14385));
			war.forceChat("Zamorak, give me strength!");
			war.heal(5000);
			return true;
		}
		if (Locations.goodDistance(target.getPosition(), war.getPosition(), 2)) {
			war.performAnimation(anim);
			victim.performGraphic(gfx1);
			war.getCombatBuilder().setVictim(target);
			victim.dealDamage(new Hit(100 + RandomUtility.getRandom(100), Hitmask.RED, CombatIcon.BLOCK));
		}
		int random = Misc.getRandom(10);

		if (Locations.goodDistance(war.getPosition().copy(), victim.getPosition().copy(), 2) && random <= 4) {
			war.performAnimation(anim2);
			war.getCombatBuilder().setContainer(new CombatContainer(war, victim, 1, 2, CombatType.MELEE, true));
		} else if (random >= 6) {
			war.performAnimation(anim3);
			war.setChargingAttack(true);
			victim.performGraphic(gfx1);
			war.getCombatBuilder().setContainer(new CombatContainer(war, victim, 2, 2, CombatType.MAGIC, true));
			war.setChargingAttack(false).getCombatBuilder().setAttackTimer(war.getDefinition().getAttackSpeed() - 1);
		} else {
			war.performAnimation(anim3);
			victim.performGraphic(gfx2);
			war.getCombatBuilder().setContainer(new CombatContainer(war, victim, 1, 2, CombatType.MAGIC, true));
		}

		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 10;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}

}