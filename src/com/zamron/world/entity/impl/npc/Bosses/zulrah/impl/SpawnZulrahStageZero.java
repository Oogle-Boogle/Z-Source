package com.zamron.world.entity.impl.npc.Bosses.zulrah.impl;

import com.zamron.event.CycleEventContainer;
import com.zamron.model.Animation;
import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahLocation;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.ZulrahStage;
import com.zamron.world.entity.impl.player.Player;



public class SpawnZulrahStageZero extends ZulrahStage {

	public SpawnZulrahStageZero(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || player == null || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		int cycle = container.getTotalTicks();
		if (cycle == 8) {
			/*Position zulrahMap = new Position(2268, 3069, zulrah.getHeight());
			//player.getPacketSender().sendMessage("@red@Welcome to Zulrah's shrine"/*, -1, 4*///);
			//player.moveTo(zulrahMap);
		}
		if (cycle == 13) {
			int zulrahId = 2043;
			Position position = new Position(2268, 3074, zulrah.getHeight());
			NPC npcSpawn = new NPC(zulrahId, position).setSpawnedFor(player);
			World.register(npcSpawn);
			player.getRegionInstance().getNpcsList().add(npcSpawn);
			zulrah.setNpc(npcSpawn);
			if (zulrah.getNpc() == null) {
				player.getPacketSender().sendMessage("Something seems to have gone wrong! Please contact a staff member of Saradomin.");
				container.stop();
				return;
			}
			//npcSpawn.setPositionToFace(null);
			//setFacePlayer(false);
			//npcSpawn.setSpawnedFor(player);
			npcSpawn.performAnimation(new Animation(5073));
			zulrah.getNpc().getUpdateFlag();
			player.ZULRAH_CLICKS = 0;
		}
		if (cycle == 18) {
			zulrah.changeStage(1, CombatType.RANGED, ZulrahLocation.NORTH);
			container.stop();
		}
	}

}