package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Locations;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class RainbowTextureNPC implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int[] graphics = {2969, 2970, 2971, 2972, 2973, 2977, 2977, 2975, 2974, 2975};
		NPC npc = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(npc.isChargingAttack()) {
			return true;
		}
		if(Locations.goodDistance(npc.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 2) {
			npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 1, 2, CombatType.MAGIC, true));
			npc.performAnimation(new Animation(8525));
			victim.performGraphic(new Graphic(graphics[Misc.getRandom(graphics.length - 1)]));
			victim.dealDamage(new Hit(Misc.getRandom(300), Hitmask.RED, CombatIcon.MAGIC));
		} else if(Misc.getRandom(20) >=18) {
			npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 1, 2, CombatType.MAGIC, true));
			npc.performAnimation(new Animation(8525));
			
			victim.performGraphic(new Graphic(2979));
			victim.getMovementQueue().freeze(15);
			npc.forceChat("Oopsie!");
			victim.dealDamage(new Hit(Misc.getRandom(700), Hitmask.RED, CombatIcon.MAGIC));
		} else if(Misc.getRandom(10) >= 8) {
			npc.setChargingAttack(true);
			npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 1, 2, CombatType.MAGIC, true));
			npc.performAnimation(new Animation(8525));
			victim.performGraphic(new Graphic(graphics[Misc.getRandom(graphics.length - 1)]));
			victim.dealDamage(new Hit(Misc.getRandom(300), Hitmask.RED, CombatIcon.MAGIC));
			TaskManager.submit(new Task(1, npc, false) {
				@Override
				protected void execute() {
					npc.performAnimation(new Animation(8525));
					victim.performGraphic(new Graphic(graphics[Misc.getRandom(graphics.length - 1)]));
					victim.dealDamage(new Hit(Misc.getRandom(400), Hitmask.RED, CombatIcon.MAGIC));
					//td.performAnimation(new Animation(1979));
					npc.setChargingAttack(false).getCombatBuilder().setAttackTimer(npc.getDefinition().getAttackSpeed() - 1);
					stop();
				}
			});
		} else {
			npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 1, 2, CombatType.MAGIC, true));
			npc.performAnimation(new Animation(8525));
			victim.performGraphic(new Graphic(graphics[Misc.getRandom(graphics.length - 1)]));
			victim.dealDamage(new Hit(Misc.getRandom(500), Hitmask.RED, CombatIcon.MAGIC));
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		
		return 5;
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
