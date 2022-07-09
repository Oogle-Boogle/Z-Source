package com.zamron.world.entity;

import com.zamron.engine.task.TaskManager;
import com.zamron.model.GameObject;
import com.zamron.net.PlayerSession;
import com.zamron.net.SessionState;
import com.zamron.world.World;
import com.zamron.world.clip.region.RegionClipping;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.raids.addons.RaidChest;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class EntityHandler {

	public static void register(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			PlayerSession session = player.getSession();
			if(player.isMiniMe) {
				//System.out.println("Registered bot: " + player.getUsername());
				World.getPlayers().add(player);
				return;
			}
			if (session.getState() == SessionState.LOGGING_IN && !World.getLoginQueue().contains(player)) {
				World.getLoginQueue().add(player);
			}
		}
		
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			World.getNpcs().add(npc);
		} else if (entity.isGameObject()) {
			if (entity instanceof RaidChest) {
				RaidChest raidChest = (RaidChest) entity;
				CustomObjects.spawnGlobalObjectWithinDistance(raidChest);
			} else {
				GameObject gameObject = (GameObject) entity;
				RegionClipping.addObject(gameObject);
				CustomObjects.spawnGlobalObjectWithinDistance(gameObject);
			}
		}
	}

	public static void deregister(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player)entity;
			World.getPlayers().remove(player);
		} else if(entity.isNpc()) {
			NPC npc = (NPC)entity;
			TaskManager.cancelTasks(npc.getCombatBuilder());
			TaskManager.cancelTasks(entity);
			World.getNpcs().remove(npc);
		} else if (entity.isGameObject()) {
			if (entity instanceof RaidChest) {
				RaidChest raidChest = (RaidChest) entity;
				CustomObjects.deleteGlobalObjectWithinDistance(raidChest);
			} else {
				GameObject gameObject = (GameObject) entity;
				RegionClipping.removeObject(gameObject);
				CustomObjects.deleteGlobalObjectWithinDistance(gameObject);
			}
		}
	}
}
