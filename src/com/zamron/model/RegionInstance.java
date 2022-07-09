package com.zamron.model;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.world.World;
import com.zamron.world.content.minigames.impl.Barrows;
import com.zamron.world.content.raids.addons.RaidChest;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * Handles a custom region instance for a player
 * 
 * @author Gabriel
 */
public class RegionInstance {

	public enum RegionInstanceType {
		BARROWS, GRAVEYARD, FIGHT_CAVE, WARRIORS_GUILD, NOMAD, RECIPE_FOR_DISASTER, CONSTRUCTION_HOUSE, RAID,
		CONSTRUCTION_DUNGEON, KEY, ZULRAHS_SHRINE, ZULRAH, WARMONGER, SAGITTARE, KEY_ROOM_1, KEY_ROOM_2, KEY_ROOM_3,
		CUSTOM_BOSS, BOSS_TIER_ARENA, INSTANCE_ARENA;
	}

	private Player owner;
	private RegionInstanceType type;
	private CopyOnWriteArrayList<NPC> npcsList;
	private CopyOnWriteArrayList<Player> playersList;
	private CopyOnWriteArrayList<GameObject> objectList;
	private CopyOnWriteArrayList<RaidChest> raidChestList;

	public RegionInstance(Player p, RegionInstanceType type) {
		this.owner = p;
		this.type = type;
		this.npcsList = new CopyOnWriteArrayList<NPC>();
		this.playersList = new CopyOnWriteArrayList<Player>();
		this.objectList = new CopyOnWriteArrayList<GameObject>();
		this.raidChestList = new CopyOnWriteArrayList<RaidChest>();
	}

	public void destruct() {
		for (NPC n : npcsList) {
			if (n != null && n.getConstitution() > 0 && World.getNpcs().get(n.getIndex()) != null
					&& n.getSpawnedFor() != null && n.getSpawnedFor().getIndex() == owner.getIndex() && !n.isDying()) {
				if (n.getId() >= 4278 && n.getId() <= 4284) {
					owner.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(false);
				}
				if (n.getId() >= 2024 && n.getId() <= 2034)
					Barrows.killBarrowsNpc(owner, n, false);
				World.deregister(n);
			}
		}
		npcsList.clear();
		owner.setRegionInstance(null);
	}

	public void destruct(NPC npc) {

		if (npc != null && npc.getConstitution() > 0 && World.getNpcs().get(npc.getIndex()) != null
				&& npc.getSpawnedFor() != null && npc.getSpawnedFor().getIndex() == owner.getIndex()
				&& !npc.isDying()) {
			if (npc.getId() >= 2024 && npc.getId() <= 2034)
				Barrows.killBarrowsNpc(owner, npc, false);

			World.deregister(npc);
		}
		npcsList.clear();
		for (RaidChest chest : raidChestList) {
			World.deregister(chest);
		}
		raidChestList.clear();
		owner.setRegionInstance(null);
	}

	public void add(Character c) {
		if (type == RegionInstanceType.CONSTRUCTION_HOUSE || type == RegionInstanceType.RAID) {
			if (c.isPlayer()) {
				playersList.add((Player) c);
			} else if (c.isNpc()) {
				npcsList.add((NPC) c);
			}

			if (c.getRegionInstance() == null || c.getRegionInstance() != this) {
				c.setRegionInstance(this);
			}
		}
	}

	public void remove(Character c) {
		if (type == RegionInstanceType.CONSTRUCTION_HOUSE || type == RegionInstanceType.RAID) {
			if (c.isPlayer()) {
				playersList.remove((Player) c);
				if (owner == ((Player) c)) {
					destruct();
				}
			} else if (c.isNpc()) {
				npcsList.remove((NPC) c);
			}

			if (c.getRegionInstance() != null && c.getRegionInstance() == this) {
				c.setRegionInstance(null);
			}
		}
	}

	public void addObject(GameObject o) {
		if (type == RegionInstanceType.CONSTRUCTION_HOUSE || type == RegionInstanceType.RAID) {
			objectList.add(o);
			if (o.getRegionInstance() == null || o.getRegionInstance() != this) {
				o.setRegionInstance(this);
			}
		}
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public RegionInstanceType getType() {
		return type;
	}

	public CopyOnWriteArrayList<NPC> getNpcsList() {
		return npcsList;
	}

	public CopyOnWriteArrayList<Player> getPlayersList() {
		return playersList;
	}

	@Override
	public boolean equals(Object other) {
		return (RegionInstanceType) other == type;
	}
}
