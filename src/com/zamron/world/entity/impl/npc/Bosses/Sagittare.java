package com.zamron.world.entity.impl.npc.Bosses;

import com.zamron.model.Position;
import com.zamron.model.RegionInstance;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Sagittare {

	/**
	 * The player associated with this event
	 */
	private final Player player;

	private int height;

	/**
	 * The boundary location
	 */
	public static final Boundary BOUNDARY = new Boundary(2205, 3304, 2229, 3317);

	/**
	 * The npc
	 */
	private NPC npc;
	

	/**
	 * Creates a new event for the player
	 * 
	 * @param player
	 *            the player
	 */
	public Sagittare(Player player) {
		this.player = player;
	}

	public void initialize() {
		if (player.getRegionInstance() != null) {
			player.getRegionInstance().destruct();
		}
		this.height =  player.getIndex() * 4;
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.SAGITTARE));
		player.getPacketSender().sendInterfaceReset();
		Position pos = new Position(2214, 3310, height);
		NPC sagittare = new NPC(9766, pos).setSpawnedFor(player);
		World.register(sagittare);
		this.npc = sagittare;
		player.getRegionInstance().getNpcsList().add(sagittare);
		teleport();
	}

	private void teleport() {
		Position sagittareMap = new Position(2214, 3306, this.getHeight());
		player.getPacketSender().sendMessage("@red@Welcome to Sagittare!");
		player.moveTo(sagittareMap);
	}
	
	/**
	 * Stops the zulrah instance and concludes the events
	 */
	public void stop() {
		player.getRegionInstance().destruct();
	}

	/**
	 * The reference to zulrah, the npc
	 * 
	 * @return the reference to zulrah
	 */
	public NPC getNpc() {
		return npc;
	}

	public int getHeight() {
		return height;
	}
}
