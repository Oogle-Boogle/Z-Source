package com.zamron.world.entity.impl.npc.Bosses.zulrah.impl;

import com.zamron.event.CycleEventContainer;
import com.zamron.model.Flag;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahLocation;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahStage;
import com.zamron.world.entity.impl.player.Player;



public class MeleeStageTen extends ZulrahStage {

	public MeleeStageTen(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDying()
				|| player == null || player.isDying() || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		zulrah.getNpc().getCombatBuilder().attack(player);
		if (zulrah.getNpc().totalAttacks > 1 && zulrah.getNpc().getCombatBuilder().getAttackTimer() == 9) {
			player.getZulrahEvent().changeStage(11, CombatType.RANGED, ZulrahLocation.NORTH);
			zulrah.getNpc().totalAttacks = 0;
			zulrah.getNpc().setPosition(player.getPosition());
			zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
			container.stop();
			return;
		}
	}
}
