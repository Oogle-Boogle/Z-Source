package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.*;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Death implements CombatStrategy {

	private static final Graphic death_healing_graphic = new Graphic(444);
	public static int JASON = 75;
	public static int FREDDY = 103;
	public static int MYERS = 1973;


	private static void attackAll(Character entity, Character victim) {


	}
	public static void despawn(boolean death) {
		if (death) {
			for (NPC npc : World.getNpcs())
				World.deregister(npc);
		}
	}


	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}


	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC Death = (NPC) entity;
		if (Death.isChargingAttack() || Death.getConstitution() <= 0) {
			return true;
		}
		if 	(Misc.random(1000) <= 320 && Death.getConstitution() <= 6000000){
			Death.performGraphic(death_healing_graphic);
			Death.forceChat("Power of the dark heal me!");
			victim.dealDamage(new Hit(150, Hitmask.DARK_RED, CombatIcon.NONE));
			Death.setConstitution(Death.getConstitution() + Misc.getRandom(5000000));
		}
		if (Death.getConstitution() <= 20000000 && !Death.hasHealed())
		{
			int z = victim.getPosition().getZ();
			int x = victim.getPosition().getX();
			int y = victim.getPosition().getY();

			NPC[] npcs = {
					new NPC(JASON, new Position(x + 1, y + 1, z)),
					new NPC(FREDDY, new Position(x - 1, y - 1, z)),
					new NPC(MYERS, new Position(x , y + 2, z))};

			for (NPC npc : npcs) {
				Death.forceChat("I brought some very dark souls with me!");
				npc.forceChat("You will perish!");
				World.register(npc);
				npc.setSpawnedFor(victim.getAsPlayer());
				npc.getCombatBuilder().attack(victim);
				Death.setHealed(true);
			}


		}
		NPC death = (NPC) entity;
		Animation attackAnim = new Animation(440);
		@SuppressWarnings("unused")
		Graphic graphic1 = death.getGraphic();

		if (death.getConstitution() <= 0) {
			return true;
		}

		if (Misc.random(3) == 1) {
			Player target = (Player) victim;
			for (Player t : Misc.getCombinedPlayerList(target)) {
				if (t == null || t.getLocation() != Locations.Location.DEATH)
					continue;
				if (Locations.goodDistance(t.getPosition(), death.getPosition(), 2)) {
					death.getCombatBuilder().setVictim(t);
					new CombatHitTask(death.getCombatBuilder(), new CombatContainer(death, t, 2, CombatType.MELEE, true)).handleAttack();
				}
			}
		} else {
			death.performAnimation(attackAnim);
			death.getCombatBuilder().setContainer(new CombatContainer(death, victim, 1, 1, CombatType.MELEE, true));
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 7;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	public static void handleDeath() {
		despawn(false);
	}
}

