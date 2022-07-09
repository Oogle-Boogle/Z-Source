package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Projectile;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.HitQueue.CombatHit;
import com.zamron.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Jonathan Sirens
 */

public class Fear implements CombatStrategy {

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
		NPC fear = (NPC) entity;
		if (fear.isChargingAttack() || fear.getConstitution() <= 0) {
			return true;
		}
		fear.performAnimation(new Animation(426));
		fear.setChargingAttack(true);
		Player target = (Player) victim;
		TaskManager.submit(new Task(2, target, false) {
			@Override
			public void execute() {
				fear.getCombatBuilder().setVictim(target);
				AmmunitionData ammo = AmmunitionData.DRAGON_ARROW;
				new Projectile(fear, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed() - 3, ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				new CombatHit(fear.getCombatBuilder(), new CombatContainer(fear, target, 1, CombatType.RANGED, true))
						.handleAttack();
				fear.setChargingAttack(false);
				stop();
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
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
