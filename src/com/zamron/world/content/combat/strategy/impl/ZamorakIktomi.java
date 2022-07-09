package com.zamron.world.content.combat.strategy.impl;

import com.zamron.util.Misc;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Graphic;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Locations;
import com.zamron.model.Locations.Location;
import com.zamron.model.Projectile;
import com.zamron.model.Skill;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class ZamorakIktomi implements CombatStrategy {
	
	/** @author Lord Lewis **/

	private static final Animation single_poison_attack = new Animation(5327);
	private static final Graphic iktomi_healing_graphic = new Graphic(444);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC Iktomi = (NPC)entity;
		if(Iktomi.isChargingAttack() || Iktomi.getConstitution() <= 0) {
			return true;
		}
		
		if(Iktomi.getConstitution() <= 1200 && !Iktomi.hasHealed()) {
			Iktomi.performGraphic(iktomi_healing_graphic);
			Iktomi.forceChat("Zamorak replenish me my Master!");
			victim.forceChat("Aaaaaaaaah!");
			victim.dealDamage(new Hit(150, Hitmask.RED, CombatIcon.NONE));
			Iktomi.setConstitution(Iktomi.getConstitution() + Misc.getRandom(7000));
			Iktomi.setHealed(true);
		}
		
		Player target = (Player)victim;
		boolean crucio = false;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			
			if (Locations.goodDistance(t.getPosition(), Iktomi.getPosition(), 1)) {
				crucio = true;
				Iktomi.getCombatBuilder().setVictim(t);
				new CombatHitTask(Iktomi.getCombatBuilder(), new CombatContainer(Iktomi, t, 1, CombatType.MAGIC, true)).handleAttack();
			}
		}
		if (crucio) {
			Iktomi.performAnimation(single_poison_attack);
			//Iktomi.performGraphic(attack_graphic);
		}

		int attackStyle = Misc.getRandom(3);
		if (attackStyle == 0 || attackStyle == 1) { // poison blast
			int distanceX = target.getPosition().getX() - Iktomi.getPosition().getX();
			int distanceY = target.getPosition().getY() - Iktomi.getPosition().getY();
			if (distanceX > 4 || distanceX < -1 || distanceY > 4 || distanceY < -1)
				attackStyle = 2;
			else {

				Iktomi.performAnimation(new Animation(attackStyle == 0 ? 5327 : 5327));
				if(target.getLocation() == Location.GODWARS_DUNGEON)
					Iktomi.getCombatBuilder().setContainer(new CombatContainer(Iktomi, target, 1, 1, CombatType.MAGIC, true));
				return true;
			}
		} else if (attackStyle == 2) { // Single Poison Blast
			
			Iktomi.performAnimation(single_poison_attack);
			Iktomi.getCombatBuilder().setContainer(new CombatContainer(Iktomi, target, 1, 2, CombatType.MAGIC, true));
			new Projectile(Iktomi, target, 551, 44, 3, 63, 43, 0).sendProjectile();
			 target.performGraphic(new Graphic(267));
		} else if (attackStyle == 3) { // Skill Drain Attack
			Iktomi.performAnimation(single_poison_attack);
				Iktomi.getCombatBuilder().setContainer(new CombatContainer(Iktomi, target, 1, 2, CombatType.MAGIC, true));
			new Projectile(Iktomi, target, 551, 44, 3, 63, 43, 0).sendProjectile();
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					int skill = (2);
					Skill skillT = Skill.forId(skill);
					Player player = (Player) target;
					int lvl = player.getSkillManager().getCurrentLevel(skillT);
					lvl -= 4 + Misc.getRandom(3);
					 target.performGraphic(new Graphic(267));
					player.getSkillManager().setCurrentLevel(skillT, player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ?  1 : lvl);
					target.getPacketSender().sendMessage("@red@Iktori has drained your strength!");
					stop();
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
		return 20;
	}
	
	

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}