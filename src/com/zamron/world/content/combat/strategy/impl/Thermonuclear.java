package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class Thermonuclear implements CombatStrategy {

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
		NPC thermo = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(thermo.isChargingAttack()) {
			return true;
		}
		thermo.setChargingAttack(true);
		final CombatType attkType = Misc.getRandom(5) <= 2 ? CombatType.RANGED : CombatType.MAGIC;
		thermo.getCombatBuilder().setContainer(new CombatContainer(thermo, victim, 1, 4, attkType, Misc.getRandom(5) <= 1 ? false : true));
		TaskManager.submit(new Task(1, thermo, false) {
			int tick = 0;
			@Override
			public void execute() {
				if(tick == 2) {
					new Projectile(thermo, victim, (attkType == CombatType.RANGED ? 605 : 971), 44, 3, 43, 43, 0).sendProjectile();
					thermo.setChargingAttack(false);
					stop();
				}
				tick++;
			}
		});
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 5;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
