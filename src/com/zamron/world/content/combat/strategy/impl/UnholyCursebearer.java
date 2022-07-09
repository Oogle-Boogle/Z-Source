package com.zamron.world.content.combat.strategy.impl;

import java.util.List;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class UnholyCursebearer implements CombatStrategy {
	/*
	 * author @live:nrpker7839
	 */

	private static final Animation anim = new Animation(13169);

	private static final Graphic gfx1 = new Graphic(2441, 3, GraphicHeight.LOW);

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
		NPC uc = (NPC)entity;
			if(uc.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if(uc.isChargingAttack()) {
			return true;
		}
		if(Locations.goodDistance(uc.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
			uc.performAnimation(anim);
			uc.performGraphic(gfx1);
			uc.getCombatBuilder().setContainer(new CombatContainer(uc, victim, 1, 2, CombatType.MELEE, true));
		} else if(Misc.getRandom(10) <= 7) {
			uc.performAnimation(anim);
			uc.setChargingAttack(true);
			uc.getCombatBuilder().setContainer(new CombatContainer(uc, victim, 1, 5, CombatType.MAGIC, false));
			TaskManager.submit(new Task(1, uc, false) {
				@Override
				protected void execute() {
					new Projectile(uc, victim, 2441, 44, 1, 4, 4, 0).sendProjectile();
					uc.setChargingAttack(false);
					stop();
				}
			});
		} else {
			////System.out.println("Attacking now");
			uc.setChargingAttack(true);
			uc.performAnimation(new Animation(uc.getDefinition().getAttackAnimation()));
		
			final Position start = victim.getPosition().copy();
			final Position second = new Position(start.getX() + 2, start.getY() + Misc.getRandom(2));
			final Position last = new Position (start.getX() - 2, start.getY() - Misc.getRandom(2));
			
			
			final Player p = (Player)victim;
			final List<Player> list = Misc.getCombinedPlayerList(p);
			uc.getCombatBuilder().setContainer(new CombatContainer(uc, victim, 1, 5, CombatType.MAGIC, true));
			TaskManager.submit(new Task(1, uc, false) {
				int tick = 0;
				@Override
				public void execute() {
					if(tick == 0) {
						p.getPacketSender().sendGlobalGraphic(new Graphic(2440), start);
						p.getPacketSender().sendGlobalGraphic(new Graphic(2440), second);
						p.getPacketSender().sendGlobalGraphic(new Graphic(2440), last);
					} else if(tick == 3) {
						for(Player t : list) {
							if(t == null)
								continue;
						
						}
						uc.setChargingAttack(false);
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
		return 5;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
