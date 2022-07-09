package com.zamron.world.content.raids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.zamron.model.Position;
import com.zamron.model.RegionInstance;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public abstract class InstancedRaid {

	private String raidName;
	protected ArrayList<NPC> raidNpcs;
	protected int stage = -1;
	private OldRaidParty oldRaidParty;
	protected RegionInstance regionInstance;
	private Position defaultSpawn;
	private int height = 0;
	private HashMap<Integer, Integer> rewards = new HashMap<Integer, Integer>();

	public InstancedRaid(Position defaultSpawn, String raidName, OldRaidParty oldRaidParty) {
		this.height = oldRaidParty.getLeader().getIndex() * 4;
		this.defaultSpawn = new Position(0, 0, height);
		this.raidName = raidName;
		this.oldRaidParty = oldRaidParty;
	}

	public ArrayList<NPC> getNpcs() {
		return raidNpcs;
	}

	// destroys all npcs / instance
	public void destroyNpcs() {
		for (NPC n : raidNpcs) {
			if (World.getNpcs().contains(n)) {
				World.deregister(n);
			}
		}
	}

	public void setDefaultSpawn(int x, int y) {
		defaultSpawn.setX(x);
		defaultSpawn.setY(y);
	}

	public OldRaidParty getRaidParty() {
		return oldRaidParty;
	}

	public Position getDefaultPosition() {
		return defaultSpawn;
	}

	public RegionInstance getRegionInstance() {
		return regionInstance;
	}

	public ArrayList<NPC> getRaidNpcs() {
		return raidNpcs;
	}

	public int getStage() {
		return stage;
	}

	public String getName() {
		return raidName;
	}

	public void addNpc(NPC npc) {
		if (raidNpcs == null)
			raidNpcs = new ArrayList<NPC>();

		raidNpcs.add(npc);
	}

	public void spawnNpcs() {
		regionInstance = new RegionInstance(oldRaidParty.getLeader(), RegionInstanceType.RAID);
		oldRaidParty.getLeader().setRegionInstance(regionInstance);
		regionInstance.add(oldRaidParty.getLeader());
		for (Player member : oldRaidParty.getMembers()) {
			member.setRegionInstance(regionInstance);
			regionInstance.add(member);
		}
		if (this instanceof Raid3) {
			for (NPC n : raidNpcs) {
				n.getPosition().setZ(height);
			}
		} else {
			Iterator<NPC> spawnIterator = raidNpcs.iterator();
			while (spawnIterator.hasNext()) {
				NPC npc = spawnIterator.next();
				npc.getPosition().setZ(height);
				regionInstance.add(npc);
				World.register(npc);
			}
		}
		for (Player member : oldRaidParty.getMembers()) {
			if (member != null) {
				oldRaidParty.updateRaidingStatus(member, true);
			} else {
				oldRaidParty.getMembers().remove(member);
			}
		}
		oldRaidParty.updateRaidingStatus(oldRaidParty.getLeader(), true);
	}

	public abstract void configureNpcs();

	public abstract void nextLevel();

	public void teleportAll() {
		for (Player member : oldRaidParty.getMembers()) {
			if (member != null && !oldRaidParty.isDefeated(member)) {
				Position alteredPosition = defaultSpawn;
				if (this instanceof Raid1) {
					alteredPosition.add(Misc.inclusiveRandom(0, 0), 0);
				} else if (this instanceof Raid2) {
					alteredPosition.add(Misc.inclusiveRandom(0, 0), Misc.inclusiveRandom(0, 0));
				} else if (this instanceof Raid3) {
					alteredPosition.add(Misc.inclusiveRandom(0, 0), Misc.inclusiveRandom(0, 0));
				}

				member.moveTo(alteredPosition);
				if (stage > 0)
					member.sendMessage("->@blu@Your raid party has been teleported to the next raid boss!");
				else
					member.sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}

		if (oldRaidParty.getLeader() != null) {
			if (!oldRaidParty.isDefeated(oldRaidParty.getLeader())) {
				Position alteredPosition = defaultSpawn;
				alteredPosition.add(Misc.inclusiveRandom(-1, 1), 0);
				oldRaidParty.getLeader().moveTo(alteredPosition);
				if (stage > 0)
					oldRaidParty.getLeader()
							.sendMessage("->@blu@Your raid party has been teleported to the next raid boss!");
				else
					oldRaidParty.getLeader().sendMessage("->@blu@Your raid party has begun the " + raidName + " raid!");
			}
		}
	}

	private Player findNewRegionOwner() {
		for (Player member : oldRaidParty.getMembers()) {
			if (!oldRaidParty.isDefeated(member))
				return member;
		}
		return null;
	}

	public void defeat(Player member) {
		if (oldRaidParty.getLeader() == member) {
			// pass on region leadership
			Player newOwner = findNewRegionOwner();
			if (newOwner == null) {
				regionInstance.remove(member);
				oldRaidParty.failedRaid();
				oldRaidParty.updateRaidingStatus(member, false);
				return;
			} else {
				regionInstance.setOwner(findNewRegionOwner());
			}
		}
		regionInstance.remove(member);
		oldRaidParty.addDefeatedMember(member);
		oldRaidParty.updateRaidingStatus(member, false);
	}

	private void givePoints(int amount) {
		for(Player p : oldRaidParty.getMembers()) {
			p.getPointsHandler().setRaidPoints(p.getPointsHandler().getRaidPoints() + amount, false);
			p.sendMessage("You received " + amount + " points, you now have " + p.getPointsHandler().getRaidPoints() + "!");
		}
		oldRaidParty.getLeader().getPointsHandler().setRaidPoints(oldRaidParty.getLeader().getPointsHandler().getRaidPoints() + amount, false);
		oldRaidParty.getLeader().sendMessage("You received " + amount + " points, you now have " + oldRaidParty
				.getLeader().getPointsHandler().getRaidPoints() + "!");
	}
	
	public void victory() {
		// for raids without chests
		rewards.put(14471, 1);
		oldRaidParty.succeededRaid(rewards);
		givePoints(Misc.inclusiveRandom(100, 250));
	}

	public void respawn(Player player) {
		player.moveTo(defaultSpawn);
	}
}
