package com.zamron.world.content.combat.strategy.impl;

import com.zamron.util.Misc;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Skill;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.Character;

/** @author Lukafurlan **/

public class DireWolf implements CombatStrategy {

	public static int phase;
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
		Player player = (Player)victim;
		if(player.getConstitution() <= 0 || entity.getConstitution() <= 0) {
			return true;
		}

		if(phase == 0)
		{
			int rand = Misc.getRandom(10);
			int amountToDrain = Misc.getRandom(220);
			if(rand == 5) {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(new Animation(6579));
				player.performGraphic(new Graphic(377));
				player.getPacketSender().sendMessage("@red@You were hit by a powerful magic attack!");
			} else if(rand == 2) {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MAGIC, true)).handleAttack();
				entity.performAnimation(new Animation(6579));
				player.performGraphic(new Graphic(377));
				player.getPacketSender().sendMessage("@red@The Dire Wolf lets out two powerful spells at once!");
			} else if(rand == 4 || rand == 3){
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				player.getPacketSender().sendMessage("@blu@The Dire Wolf has drained some prayer points!");
				entity.performAnimation(new Animation(6579));
			} else {
				new CombatHitTask(entity.getCombatBuilder(), new CombatContainer(entity, player, 1, CombatType.MELEE, true)).handleAttack();
				entity.performAnimation(new Animation(6579));
			}
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 3;
	}


	

	@Override
	public CombatType getCombatType() {
		// TODO Auto-generated method stub
		return null;
	}

}