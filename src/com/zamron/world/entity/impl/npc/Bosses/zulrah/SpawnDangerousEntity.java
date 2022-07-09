package com.zamron.world.entity.impl.npc.Bosses.zulrah;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.zamron.event.CycleEvent;
import com.zamron.event.CycleEventContainer;
import com.zamron.model.Animation;
import com.zamron.model.Direction;
import com.zamron.model.Flag;
import com.zamron.model.GameObject;
import com.zamron.model.Position;
import com.zamron.model.Locations.Location;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class SpawnDangerousEntity extends CycleEvent {
	
	private Zulrah zulrah;
	
	private Player player;
	
	private Queue<DangerousLocation> locations = new LinkedList<>();
	
	private DangerousEntity entity;
	
	private int duration;
	
	public SpawnDangerousEntity(Zulrah zulrah, Player player, List<DangerousLocation> locations, DangerousEntity entity) {
		this.player = player;
		this.zulrah = zulrah;
		this.entity = entity;
		this.locations.addAll(locations);
	}
	
	public SpawnDangerousEntity(Zulrah zulrah, Player player, List<DangerousLocation> locations, DangerousEntity entity, int duration) {
		this(zulrah, player, locations, entity);
		this.duration = 30;
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (player == null || zulrah == null || zulrah.getNpc() == null
				 || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		//zulrah.getNpc().setFacePlayer(false);
		int ticks = container.getTotalTicks();
		DangerousLocation location = locations.peek();
		if (location == null) {
			container.stop();
			//zulrah.getNpc().setPositionToFace(player.getPosition());
			return;
		}
		if (ticks == 4 || ticks == 8 || ticks == 12 || ticks == 16) {
			for (Point point : location.getPoints()) {
				if (entity.equals(DangerousEntity.TOXIC_SMOKE)) {
					Position smoke = new Position(point.x, point.y, zulrah.getHeight());
					if (ticks == 4 || ticks == 12) {
						if(player.getLocation() == Location.ZULRAH_SHRINE){
						Position smoke1 = new Position(player.getPosition().getX(), player.getPosition().getY(), zulrah.getHeight());
						GameObject smokeObject1 = new GameObject(11700, smoke1, 0, 10, duration);
						CustomObjects.globalObjectRemovalTask(smokeObject1, duration);
						}
					}
					zulrah.getNpc().setPositionToFace(smoke);
					zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
					GameObject smokeObject = new GameObject(11700, smoke, 0, 10, duration);
					CustomObjects.globalObjectRemovalTask(smokeObject, duration);
					//World.register(smokeObject);
					//World.register(smokeObject1);
					/*GameEngine.getGlobalObjects().add(new GlobalObject(11700, point.x, point.y, zulrah.getInstancedZulrah().getHeight(),
							0, 10, duration, -1));
					GameEngine.getGlobalObjects().add(new GlobalObject(11700, player.getPosition().getX(), player.getPosition().getZ(), zulrah.getInstancedZulrah().getHeight(),
							0, 10, duration, -1));*/
				} else if (entity.equals(DangerousEntity.MINION_NPC)) {
					Position snakeling = new Position(point.x, point.y, player.getPosition().getZ());
					zulrah.getNpc().setPositionToFace(snakeling);
					zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
					NPC snakeling1 = new NPC(Zulrah.SNAKELING, snakeling);
					NPC.removalTask(snakeling1, 40);
					snakeling1.setSpawnedFor(player);
					zulrah.getNpc().getCombatBuilder().attack(player);
					snakeling1.getCombatBuilder().attack(player);
					player.getRegionInstance().getNpcsList().add(snakeling1);
					//Server.npcHandler.spawnNpc(player, Zulrah.SNAKELING, point.x, point.y, zulrah.getInstancedZulrah().getHeight(), 0, 1, 11, 100, 50, true, false);
				}
			}
			locations.remove();
		}
		if (ticks == 2 || ticks == 6 || ticks == 10 || ticks == 14) {
			//Position p2f = new Position(location.getPoints()[0].x, location.getPoints()[0].y);
			//zulrah.getNpc().setPositionToFace(p2f);
			zulrah.getNpc().setDirection(Direction.NORTH);
			//zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
			int npcX = zulrah.getNpc().getPosition().getX();
			int npcY = zulrah.getNpc().getPosition().getY();
			for (Point point : location.getPoints()) {
				int targetY = (npcX - (int) point.getX()) * -1;
				int targetX = (npcY - (int) point.getY()) * -1;
				int projectile = entity.equals(DangerousEntity.TOXIC_SMOKE) ? 1044 : 1047;
				Position projectileNpc = new Position(npcX, npcY, player.getPosition().getZ());
				Position projectileTarget = new Position(targetX, targetY, player.getPosition().getZ());
				player.getPacketSender().sendProjectile(projectileNpc, projectileTarget, 50, 85, projectile, 70, 0, -1, 65);
				//player.getPA().createPlayersProjectile(npcX, npcY, targetX, targetY, 50, 85, projectile, 70, 0, -1, 65);
			}
			zulrah.getNpc().performAnimation(new Animation(5069));
			
			//zulrah.getNpc().getUpdateFlag();
		}
	}
}