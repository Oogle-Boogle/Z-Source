package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class Skotizo implements CombatStrategy {

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
		NPC skotizo = (NPC)entity;
		if(skotizo.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Misc.getRandom(15) <= 2){
			int hitAmount = 1;
			skotizo.performGraphic(new Graphic(605));
			skotizo.setConstitution(skotizo.getConstitution() + hitAmount);
			//((Player)victim).getPacketSender().sendMessage(MessageType.NPC_ALERT, "skotizo absorbs his next attack, healing himself a bit.");
		}
		if(Locations.goodDistance(skotizo.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) <= 3) {
			skotizo.performAnimation(new Animation(skotizo.getDefinition().getAttackAnimation()));
			skotizo.getCombatBuilder().setContainer(new CombatContainer(skotizo, victim, 1, 1, CombatType.MELEE, true));
			if(Misc.getRandom(10) <= 2) {
				new Projectile(skotizo, victim, 971, 44, 3, 43, 43, 0).sendProjectile();
				//victim.moveTo(new Position(3156 + Misc.getRandom(3), 3804 + Misc.getRandom(3)));
				//victim.performAnimation(new Animation(534));
			}
		} else {
			skotizo.setChargingAttack(true);
			skotizo.performAnimation(new Animation(64));
			skotizo.getCombatBuilder().setContainer(new CombatContainer(skotizo, victim, 1, 8, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, skotizo, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0) {
						new Projectile(skotizo, victim, 971, 44, 3, 41, 43, 0).sendProjectile();
					} else if(tick == 1) {
						skotizo.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
