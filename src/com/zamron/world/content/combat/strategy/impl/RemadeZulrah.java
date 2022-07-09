package com.zamron.world.content.combat.strategy.impl;

import java.util.Random;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class RemadeZulrah implements CombatStrategy {

	@SuppressWarnings("unused")
	private static final Graphic Graphic_Magic = new Graphic(79);
	@SuppressWarnings("unused")
	private static final Graphic Graphic_Range = new Graphic(78);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC zulrah = (NPC) entity;
		if (zulrah.isChargingAttack() || zulrah.getConstitution() <= 0) {
			return true;
		}
		CombatType style = Misc.getRandom(4) <= 2 ? CombatType.MAGIC : CombatType.RANGED;
		CombatType style1 = Misc.getRandom(4) <= 2 ? CombatType.RANGED : CombatType.MAGIC;
		if (style == null) {
			return false;
		}
		Player target = (Player) victim;
		int stage = target.getZulrahEvent().getStage();
		for (Player t : Misc.getCombinedPlayerList(target)) {
			if (t == null || t.isTeleporting())
				continue;
			if (t.getPosition().distanceToPoint(zulrah.getPosition().getX(), zulrah.getPosition().getY()) > 20)
				continue;
			TaskManager.submit(new Task(2, target, false) {
				@Override
				public void execute() {
					Player target = (Player) victim;
					for (Player t : Misc.getCombinedPlayerList(target)) {
						if (t == null)
							continue;
						zulrah.getCombatBuilder().setVictim(t);
						if (stage != 0)
							if (CombatAction.randomBetween(zulrah, target))
								stop();

					}
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
		return 15;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}

enum CombatAction {
	MAGIC {
		@Override
		void action(NPC zulrah, Player target) {
			zulrah.getCombatBuilder().setAttackTimer(5);
			zulrah.performAnimation(new Animation(zulrah.getDefinition().getAttackAnimation()));
			new Projectile(zulrah, target, new Graphic(1155).getId(), 44, 3, 43, 43, 0).sendProjectile();
			// zulrah.getCombatBuilder().setContainer(new CombatContainer(zulrah, victim, 1,
			// 1, CombatType.MAGIC, true));
			zulrah.getCombatBuilder().setContainer(new CombatContainer(zulrah, target, 1, 1, CombatType.MAGIC, true));
			zulrah.totalAttacks += 1;

		}
	},
	MELEE {
		@Override
		void action(NPC zulrah, Player target) {
			zulrah.getCombatBuilder().setAttackTimer(10);
			zulrah.performAnimation(new Animation(5806));// 5807
			TaskManager.submit(new Task(5, target, false) {

				@Override
				protected void execute() {
					zulrah.getCombatBuilder()
							.setContainer(new CombatContainer(zulrah, target, 1, 1, CombatType.MELEE, true));

					// new CombatHit(zulrah.getCombatBuilder(), new CombatContainer(zulrah, target,
					// 1, CombatType.MELEE, true)).handleAttack();
					zulrah.totalAttacks += 1;
					stop();
				}

			});

		}
	},
	RANGED {
		@Override
		void action(NPC zulrah, Player target) {
			zulrah.getCombatBuilder().setAttackTimer(4);
			zulrah.performAnimation(new Animation(zulrah.getDefinition().getAttackAnimation()));
			new Projectile(zulrah, target, new Graphic(1485).getId(), 44, 3, 43, 43, 0).sendProjectile();
			zulrah.getCombatBuilder().setContainer(new CombatContainer(zulrah, target, 1, 1, CombatType.RANGED, true));
			zulrah.totalAttacks += 1;

		}
	};
	abstract void action(NPC zulrah, Player target);

	public static boolean randomBetween(NPC zulrah, Player target) {
		Random rand = new Random();

		if (zulrah.getTransformationId() == 2042) {
			CombatAction mage[] = { CombatAction.MAGIC, CombatAction.RANGED };
			mage[Misc.random(rand.nextInt(1))].action(zulrah, target);
		} else if (zulrah.getTransformationId() == 2043) {
			CombatAction range[] = { CombatAction.MAGIC, CombatAction.RANGED, CombatAction.MELEE };
			range[rand.nextInt(2)].action(zulrah, target);
		} else if (zulrah.getTransformationId() == 2044) {
			CombatAction.MELEE.action(zulrah, target);
		}
		// //System.out.println("Npc id:"+zulrah.getTransformationId());
		return zulrah.setChargingAttack(false) != null;

	}
}
