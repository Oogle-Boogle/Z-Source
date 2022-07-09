package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class InfernalWizardCombat implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		
		return null;
	}
	
	int tick = 15;

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		
		NPC npc = (NPC) entity;
		
		TaskManager.submit(new Task(1, npc, false) {
			@Override
			protected void execute() {
				tick -= 1;
				if(tick == 14) {
					npc.forceChat("Ya Ranen!");
				}
				
				if(tick == 13) {
					victim.performGraphic(new Graphic(1363));
				}
				
				if(tick == 12) {
					npc.forceChat("Zariajayu Vzryvchatku!");
				}
				
				if(tick == 8) {
					victim.performGraphic(new Graphic(1364));
					victim.dealTripleDamage(new Hit(Misc.getRandom(100), Hitmask.RED, CombatIcon.MAGIC), new Hit(Misc.getRandom(380), Hitmask.RED, CombatIcon.MAGIC), new Hit(Misc.getRandom(390), Hitmask.RED, CombatIcon.MAGIC));
				}
				if(tick == 3) {
					victim.performGraphic(new Graphic(1360));
					victim.dealDamage(new Hit(Misc.getRandom(100), Hitmask.RED, CombatIcon.MAGIC));
					tick = 15;
				}
				npc.setChargingAttack(false).getCombatBuilder().setAttackTimer(npc.getDefinition().getAttackSpeed() - 1);
				if(tick == 0 || victim.getConstitution() < 1) {
					stop();
				}
			}
		});
		
		
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		
		return 4;
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
