package com.zamron.world.content.raids;

import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;

public class Raid3 extends InstancedRaid {

	public Raid3(Position defaultSpawn, String raidName, OldRaidParty oldRaidParty) {
		super(defaultSpawn, raidName, oldRaidParty);
	}

	/*
	 * Change npc positions / default spawns where players suppose to spawn each stage
	 * Then you'll have to change chest location in instancedraid
	 * Nothing else has to be changed like the height etc. InstancedRaid class handles it
	 */

	@Override
	public void configureNpcs() {
		RaidNpc zulrah1 = new RaidNpc(2042, new Position(2268, 3074));
		zulrah1.setStageRequiredtoAttack(0);
		addNpc(zulrah1);
		RaidNpc zulrah2 = new RaidNpc(2043, new Position(2268, 3074));
		zulrah2.setStageRequiredtoAttack(1);
		addNpc(zulrah2);
		RaidNpc zulrah3 = new RaidNpc(2044, new Position(2268, 3074));
		zulrah3.setStageRequiredtoAttack(2);
		addNpc(zulrah3);
		
		for(NPC n : getNpcs()) {
			n.setDefaultConstitution((1200000));
			n.setConstitution((12000000));
			n.setAttackbonus(n.getAttackbonus() * 5);
			n.setChargingAttack(true);
			n.setPoisonDamage(25);
		}
	}
	
	@Override
	public void nextLevel() {
		if(stage == 2) {
			victory();
			return;
		}
		stage++;
		RaidNpc raidNpc = (RaidNpc) getNpcs().get(stage).setChargingAttack(false);
		getRaidParty().sendMessageToMembers("@or2@Attack the " + getNpcs().get(stage).getDefinition().getName() + "!", true);
		switch(stage) {
		case 0: 
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2269, 3069);
			break;
		case 1:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2269, 3069);
			break;
		case 2:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2269, 3069);
			break;
		}
		teleportAll();
	}
}
