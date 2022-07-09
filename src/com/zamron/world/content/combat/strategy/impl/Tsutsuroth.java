package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Locations.Location;
import com.zamron.model.Projectile;
import com.zamron.model.Skill;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Tsutsuroth implements CombatStrategy {

	private static final Animation anim1 = new Animation(6947);
	private static final Graphic graphic1 = new Graphic(1211, GraphicHeight.MIDDLE);
	private static final Graphic graphic2 = new Graphic(390);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer() && ((Player)victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC tsutsuroth = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(tsutsuroth.isChargingAttack()) {
			return true;
		}
		Player target = (Player)victim;
		CombatType style = Misc.getRandom(8) >= 6 && Locations.goodDistance(tsutsuroth.getPosition(), victim.getPosition(), 2) ? CombatType.MELEE : CombatType.MAGIC;
		if(style == CombatType.MELEE) {
			tsutsuroth.performAnimation(new Animation(6945));
			tsutsuroth.getCombatBuilder().setContainer(new CombatContainer(tsutsuroth, victim, 1, 1, CombatType.MELEE, true));
			int specialAttack = Misc.getRandom(4);
			if (specialAttack == 2) {
				int amountToDrain = Misc.getRandom(400);
				target.getPacketSender().sendMessage("K'ril Tsutsaroth slams through your defence and steals some Prayer points..");
				if(amountToDrain > target.getSkillManager().getCurrentLevel(Skill.PRAYER)) {
					amountToDrain = target.getSkillManager().getCurrentLevel(Skill.PRAYER);
				}
				target.getSkillManager().setCurrentLevel(Skill.PRAYER, target.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				if(target.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					target.getPacketSender().sendMessage("You have run out of Prayer points!");
				}
			}
		} else {
			tsutsuroth.performAnimation(anim1);
			tsutsuroth.setChargingAttack(true);
			TaskManager.submit(new Task(2, target, false) {
				int tick = 0;
				@Override
				public void execute() {
					switch(tick) {
					case 0:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if(t == null || t.getLocation() != Location.GODWARS_DUNGEON || t.isTeleporting())
								continue;
							if(t.getPosition().distanceToPoint(tsutsuroth.getPosition().getX(), tsutsuroth.getPosition().getY()) > 20)
								continue;
							new Projectile(tsutsuroth, target, graphic1.getId(), 44, 3, 43, 43, 0).sendProjectile();
						}
						break;
					case 2:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if(t == null || t.getLocation() != Location.GODWARS_DUNGEON)
								continue;
							target.performGraphic(graphic2);
							tsutsuroth.getCombatBuilder().setVictim(t);
							new CombatHitTask(tsutsuroth.getCombatBuilder(), new CombatContainer(tsutsuroth, t, 1, CombatType.MAGIC, true)).handleAttack();
						}
						tsutsuroth.setChargingAttack(false);
						stop();
						break;
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
