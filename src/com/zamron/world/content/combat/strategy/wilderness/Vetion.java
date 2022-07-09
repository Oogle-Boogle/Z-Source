package com.zamron.world.content.combat.strategy.wilderness;

import java.util.List;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Vetion implements CombatStrategy {

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
		NPC vetion = (NPC)entity;
		if(vetion.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(Locations.goodDistance(vetion.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) <= 3) {
			vetion.performAnimation(new Animation(5487));
			vetion.getCombatBuilder().setContainer(new CombatContainer(vetion, victim, 1, 1, CombatType.MELEE, true));
		} else {
			vetion.setChargingAttack(true);
			vetion.performAnimation(new Animation(vetion.getDefinition().getAttackAnimation()));
		
			final Position start = victim.getPosition().copy();
			final Position second = new Position(start.getX() + 2, start.getY() + Misc.getRandom(2));
			final Position last = new Position (start.getX() - 2, start.getY() - Misc.getRandom(2));
			
			final Player p = (Player)victim;
			final List<Player> list = Misc.getCombinedPlayerList(p);
			
			TaskManager.submit(new Task(1, vetion, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0) {
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), start);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), second);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), last);
					} else if(tick == 3) {
						for(Player t : list) {
							if(t == null)
								continue;
							if(t.getPosition().equals(start) || t.getPosition().equals(second) || t.getPosition().equals(last)) {
								new CombatHitTask(vetion.getCombatBuilder(), new CombatContainer(vetion, t, 3, CombatType.MAGIC, true)).handleAttack();
							}
						}
						vetion.setChargingAttack(false);
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
