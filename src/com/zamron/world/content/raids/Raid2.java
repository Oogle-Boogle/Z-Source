package com.zamron.world.content.raids;

import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.entity.impl.npc.NPC;

public class Raid2 extends InstancedRaid {

	public Raid2(Position defaultSpawn, String raidName, OldRaidParty oldRaidParty) {
		super(defaultSpawn, raidName, oldRaidParty);
	}

	/*
	 * Change npc positions / default spawns where players suppose to spawn each stage
	 * Then you'll have to change chest location in instancedraid
	 * Nothing else has to be changed like the height etc. InstancedRaid class handles it
	 */

	@Override //2411, 5113
	public void configureNpcs() {
		RaidNpc diglet = new RaidNpc(9277, new Position(3109, 3099));
		diglet.setStageRequiredtoAttack(0);
		addNpc(diglet);
		
		RaidNpc charmeleon = new RaidNpc(1999, new Position(2596, 3415));
		charmeleon.setStageRequiredtoAttack(1);
		addNpc(charmeleon);
		
		RaidNpc lucario = new RaidNpc(9903, new Position(2981, 3223));
		lucario.setStageRequiredtoAttack(2);
		addNpc(lucario);
		
		RaidNpc mewtwo = new RaidNpc(9247, new Position(1960, 4763));
		mewtwo.setStageRequiredtoAttack(3);
		addNpc(mewtwo);
		
		RaidNpc groudon = new RaidNpc(9203, new Position(2344, 4759));
		groudon.setStageRequiredtoAttack(4);
		addNpc(groudon);
		
		RaidNpc squirtle2 = new RaidNpc(9935, new Position(2469, 4767));
		squirtle2.setStageRequiredtoAttack(5);
		addNpc(squirtle2);
		
		RaidNpc charmeleon2 = new RaidNpc(169, new Position(2920, 9690));
		charmeleon2.setStageRequiredtoAttack(6);
		addNpc(charmeleon2);
		
		RaidNpc lucario2 = new RaidNpc(12239, new Position(2660, 4512));
		lucario2.setStageRequiredtoAttack(7);
		addNpc(lucario2);
		
		RaidNpc mewtwo2 = new RaidNpc(1684, new Position(2022, 4439));
		mewtwo2.setStageRequiredtoAttack(8);
		addNpc(mewtwo2);
		
		RaidNpc riachu = new RaidNpc(5958, new Position(2783, 3282));
		riachu.setStageRequiredtoAttack(9);
		addNpc(riachu);
		
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
		if(stage == 9) {
			victory();
			return;
		}
		stage++;
		getNpcs().get(stage).setChargingAttack(false);
		RaidNpc raidNpc = (RaidNpc) getNpcs().get(stage).setChargingAttack(false);
		getRaidParty().sendMessageToMembers("@or2@Attack the " + getNpcs().get(stage).getDefinition().getName() + "!", true);
		switch(stage) {
		case 0: //(2411, 5113)
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(3104, 3097);
			break;
		case 1:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2596, 3415);
			break;
		case 2: 
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2981, 3222);
			break;
		case 3:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(1958, 4755);
			break;
		case 4: 
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2336, 4759);
			break;
		case 5:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2471, 4762);
			break;
		case 6:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2914, 9682);
			break;
		case 7:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2663, 4520);
			break;
		case 8:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2013, 4439);
			break;
		case 9:
			this.getRegionInstance().add(raidNpc);
			World.register(raidNpc);
			setDefaultSpawn(2783, 3282);
			break;
		}
		teleportAll();
	}
}