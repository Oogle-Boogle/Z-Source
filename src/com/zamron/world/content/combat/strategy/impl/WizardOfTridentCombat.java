package com.zamron.world.content.combat.strategy.impl;

import com.zamron.util.Misc;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class WizardOfTridentCombat implements CombatStrategy {
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
		NPC td = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(td.isChargingAttack()) {
			return true;
		}
		if(Locations.goodDistance(td.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MAGIC, true));
			//td.performAnimation(new Animation(1979));
			new Projectile(td, victim, 1851, 44, 2, 5, 6, 0).sendProjectile();
		} else if(Misc.getRandom(10) <= 9) {
			td.setChargingAttack(true);
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, td, false) {
				@Override
				protected void execute() {
					//td.performAnimation(new Animation(1979));
					new Projectile(td, victim, 1851, 44, 2, 5, 6, 0).sendProjectile();
					td.setChargingAttack(false).getCombatBuilder().setAttackTimer(td.getDefinition().getAttackSpeed() - 1);
					stop();
				}
			});
		} else {
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MAGIC, true));
			//td.performAnimation(new Animation(1979));
			new Projectile(td, victim, 1851, 44, 2, 5, 6, 0).sendProjectile();
		}
		return true;
	}


	@Override
	public int attackDelay(Character entity) {
		
		return 2;
	}

	@Override
	public int attackDistance(Character entity) {
		
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		
		return null;
	}
	
}