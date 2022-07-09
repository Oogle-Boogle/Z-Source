package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
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

public class OlmMinionsCombat implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		
		return null;
	}
	int tick = 25;
	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		
		NPC npc = (NPC) entity;
		
		TaskManager.submit(new Task(1, npc, false) {
			@Override
			protected void execute() {
				tick -= 1;
				if(tick == 24) {
					npc.performAnimation(new Animation(12252));
					victim.dealDamage(new Hit(Misc.getRandom(250), Hitmask.RED, CombatIcon.MELEE));
					
				}
				
				if(tick == 18) {
					npc.performAnimation(new Animation(12252));
					victim.dealDamage(new Hit(Misc.getRandom(250), Hitmask.RED, CombatIcon.MELEE));
				}
				
				if(tick == 11) {
					npc.performAnimation(new Animation(12252));
					victim.dealDamage(new Hit(Misc.getRandom(250), Hitmask.RED, CombatIcon.MELEE));
					npc.forceChat("Prepare for my special attack!");
				}
				
				if(tick == 2) {
					npc.performGraphic(new Graphic(2994));
					victim.dealDamage(new Hit(Misc.getRandom(850), Hitmask.RED, CombatIcon.MELEE));
					npc.forceChat("That was it! I can hit up to 85 with that attack, so be careful.");
				}
				if(tick == 1) {
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
