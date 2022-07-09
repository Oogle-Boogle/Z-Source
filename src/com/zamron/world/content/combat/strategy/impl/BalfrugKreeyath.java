package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class BalfrugKreeyath implements CombatStrategy {

	private static final Animation attack_anim = new Animation(69);//but low anim ids 
	private static final Graphic graphic1 = new Graphic(1212);
	@SuppressWarnings("unused")
	private static final Graphic graphic2 = new Graphic(1213);
	
	public static String[] messages = {
		"You will fear the demon powers!",
		"I can only think of destroying you!!"
		
	};
	
	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC cB = (NPC)entity;
		Player target = (Player)victim;
		if(cB.isChargingAttack() || cB.getConstitution() <= 0) {
			return true;
		}
		boolean melee = Misc.getRandom(3) == 1 && Locations.goodDistance(target.getPosition(), cB.getPosition(), 1);
		if(melee) {
			cB.getCombatBuilder().setVictim(target);
			cB.performAnimation(new Animation(69));
			new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, target, 1, CombatType.MELEE, true)).handleAttack();
		}
		cB.performAnimation(attack_anim);
		cB.performGraphic(graphic1);
		TaskManager.submit(new Task(1, victim, false) {
			int tick = 0;
			@Override
			public void execute() {
				switch(tick) {
				case 1:
					
					int random = Misc.getRandom(100);
					if (random >= 80) {
						if( random >= 80 && Misc.getRandom(10) == 4) {
							BalfrugKreeyath.forceChat(messages[Misc.getRandom(messages.length - 1)]);
						}
						cB.forceChat("You Shall Die!");
						new Projectile(cB, target, 1212, 44, 3, 43, 43, 0).sendProjectile();
					} else {
						new Projectile(cB, target, 1212, 44, 3, 43, 43, 0).sendProjectile();
					}
					break;
				case 2:
					new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, target, 1, CombatType.MAGIC, false)).handleAttack();
					stop();
					break;
				}
				tick++;
			}

		});
		return true;
	}

	protected static void forceChat(String string) {
		setForcedChat(messages);
		
	}

	private static void setForcedChat(String[] messages2) {
		//this.forcedChat = forcedChat;
		//return this;
	}
	

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
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
