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



public class CreateToxicStageOne extends ZulrahStage {

	public CreateToxicStageOne(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDying()
				|| player == null || player.isDying() || player.getRegionInstance() == null) {
			container.stop();
			return;
		}
		zulrah.getNpc().getCombatBuilder().attack(player);
		if (container.getTotalTicks() == 1) {
			//zulrah.getNpc().setFacePlayer(false);
			zulrah.getNpc().getCombatBuilder().reset(true);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.values()), DangerousEntity.TOXIC_SMOKE, 20), 1);
		} else if (container.getTotalTicks() >= 19) {
			zulrah.getNpc().totalAttacks = 0;
			zulrah.getNpc().setPositionToFace(player.getPosition());
			zulrah.changeStage(2, CombatType.MELEE, ZulrahLocation.NORTH);
			container.stop();
		}
	}

}
