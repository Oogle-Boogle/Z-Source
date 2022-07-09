package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.model.Position;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.skill.impl.hunter.Hunter;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class NPCRespawnTask extends Task {

	private Player killer;

	public NPCRespawnTask(NPC npc, int respawn, Player killer) {
		super(respawn);
		this.npc = npc;
		this.killer = killer;
	}

	final NPC npc;

	@Override
	public void execute() {

		NPC npc_ = new NPC(npc.getId(), npc.getDefaultPosition());
		
		if (npc != null) {
			
			if (npc.isInstancedNPC()) {
				if (killer != null) {
					if (killer.getRegionInstance() == null) {
						stop();
						return;
					}
				}
			}
		}

		if (npc_.isKeyRoomNpc()) {
			
			if (killer == null) {
				stop();
				return;
			}

			if (killer != null) {
				if (killer.getKeyRoom() == null) {
					stop();
					return;
				}
				if (killer.getKeyRoom() != null) {

					if (killer.getKeyRoom().count >= 4) {
						stop();
						return;
					}
					killer.getKeyRoom().count++;
				}
			}
		}

		npc_.getMovementCoordinator().setCoordinator(npc.getMovementCoordinator().getCoordinator());

		if (npc_.getId() == 8022 || npc_.getId() == 8028) { // Desospan, respawn
															// at random
															// locations
			npc_.moveTo(new Position(2595 + RandomUtility.getRandom(12), 4772 + RandomUtility.getRandom(8)));
		} else if (npc_.getId() > 5070 && npc_.getId() < 5081) {
			Hunter.HUNTER_NPC_LIST.add(npc_);
		}
		World.register(npc_);
		stop();
	}

}
