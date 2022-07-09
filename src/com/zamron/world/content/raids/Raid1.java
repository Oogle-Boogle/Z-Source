package com.zamron.world.content.raids;

import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;

public class Raid1 extends InstancedRaid {

	public Raid1(Position defaultSpawn, String raidName, OldRaidParty oldRaidParty) {
		super(defaultSpawn, raidName, oldRaidParty);
	}

	/*
	 * Change npc positions / default spawns where players suppose to spawn each stage
	 * Then you'll have to change chest location in instancedraid
	 * Nothing else has to be changed like the height etc. InstancedRaid class handles it
	 */

	@Override //2411, 5113
	public void configureNpcs() {
		RaidNpc diglet = new RaidNpc(3224, new Position(3036, 5217));
		diglet.setStageRequiredtoAttack(0);
		addNpc(diglet);
		
		RaidNpc charmeleon = new RaidNpc(3225, new Position(3036, 5087));
		charmeleon.setStageRequiredtoAttack(1);
		addNpc(charmeleon);
		
		RaidNpc lucario = new RaidNpc(3226, new Position(3036, 5023));
		lucario.setStageRequiredtoAttack(2);
		addNpc(lucario);
		
		RaidNpc mewtwo = new RaidNpc(177, new Position(3036, 4893));
		mewtwo.setStageRequiredtoAttack(3);
		addNpc(mewtwo);
		
		RaidNpc groudon = new RaidNpc(527, new Position(3036, 4700));
		groudon.setStageRequiredtoAttack(4);
		addNpc(groudon);
		for(NPC n : getNpcs()) {
			n.setDefaultConstitution((12000000));
			n.setConstitution((12000000));
			n.setAttackbonus(n.getAttackbonus() * 5);
			n.setChargingAttack(true);
			n.setPoisonDamage(25);
		}
	}
	
	@Override
	public void nextLevel() {
		if(stage == 4) {
			victory();
			return;
		}
		stage++;
		RaidNpc raidNpc = (RaidNpc) getNpcs().get(stage).setChargingAttack(false);
		getNpcs().get(stage).setChargingAttack(false);
		getRaidParty().sendMessageToMembers("@or2@Attack the " + getNpcs().get(stage).getDefinition().getName() + "!", true);
		switch(stage) {
		case 0: //(2411, 5113)
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3036, 5213);
			break;
		case 1:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3036, 5082);
			break;
		case 2: 
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3036, 5020);
			break;
		case 3:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3036, 4889);
			break;
		case 4: 
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3036, 4697);
			break;
		
		}
		teleportAll();
	}
}