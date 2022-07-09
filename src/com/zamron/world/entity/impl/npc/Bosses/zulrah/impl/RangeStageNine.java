package com.zamron.world.entity.impl.npc.Bosses.zulrah.impl;

import java.util.Arrays;

import com.zamron.event.CycleEventContainer;
import com.zamron.event.CycleEventHandler;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.DangerousEntity;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.DangerousLocation;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.SpawnDangerousEntity;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahLocation;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahStage;
import com.zamron.world.entity.impl.player.Player;


public class RangeStageNine extends ZulrahStage {
	
	private int finishedAttack;

	public RangeStageNine(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDying()
				|| player == null || player.isDying() || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		int ticks = container.getTotalTicks();
		zulrah.getNpc().getCombatBuilder().attack(player);
		if (zulrah.getNpc().totalAttacks >= 20 && finishedAttack == 0) {
			finishedAttack = ticks;
			zulrah.getNpc().getCombatBuilder().setAttackTimer(20);
			//zulrah.getNpc().setFacePlayer(false);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.SOUTH_EAST, DangerousLocation.SOUTH_WEST), DangerousEntity.MINION_NPC), 1);
		}
		if (finishedAttack > 0) {
			if (ticks - finishedAttack == 2) {
				CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
						DangerousLocation.values()), DangerousEntity.TOXIC_SMOKE, 20), 1);
			} else if (ticks - finishedAttack == 18) {
				//zulrah.getNpc().setFacePlayer(false);
				zulrah.getNpc().totalAttacks = 0;
				zulrah.changeStage(10, CombatType.MELEE, ZulrahLocation.NORTH);
				container.stop();
			}
		}
	}
}
