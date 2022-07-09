package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.container.impl.Equipment;
import com.zamron.util.RandomUtility;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.magic.CombatSpells;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.content.minigames.impl.Dueling.DuelRule;
import com.zamron.world.entity.Entity;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * The default combat strategy assigned to an {@link Entity} during a magic
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultMagicCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {

		// Npcs don't need to be checked.
		if (entity.isNpc()) {
			if(victim.isPlayer()) {
				Player p = (Player)victim;
				if(Nex.nexMinion(((NPC) entity).getId())) {
					if(!p.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
						return false;
					}
					return true;
				}
			}
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		if(Dueling.checkRule(player, DuelRule.NO_MAGIC)) {
			player.getPacketSender().sendMessage("Magic-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset(true);
			return false;
		}

		// We can't attack without a spell.
		if(player.getCastSpell() == null)
			player.setCastSpell(player.getAutocastSpell());

		int equipment = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
		if(equipment == 14006 || equipment == 13867 || equipment == 6483) {
			player.setCastSpell(CombatSpells.TIER1.getSpell());
		}
		if(equipment == 19468) {
			player.setCastSpell(CombatSpells.TIER3.getSpell());
		}
		if(equipment == 3951) {
			player.setCastSpell(CombatSpells.TIER4.getSpell());
		} 
		if(equipment == 19720) {
			player.setCastSpell(CombatSpells.TIER5.getSpell());
		} 
		if(equipment == 15653) {
			player.setCastSpell(CombatSpells.TIER6.getSpell());
		} 
		if(equipment == 15656) {
			player.setCastSpell(CombatSpells.TIER7.getSpell());
		}
		if(equipment == 5129) {
			player.setCastSpell(CombatSpells.Tier8.getSpell());
		}
		if(equipment == 19727 || equipment == 18891) {
			player.setCastSpell(CombatSpells.TIER9.getSpell());
		} 
		if(equipment == 13995 || equipment == 8664 || equipment == 8656) {
			player.setCastSpell(CombatSpells.DRAGONLAVA.getSpell());
		}
		if(equipment == 3920 ) {
			player.setCastSpell(CombatSpells.DEMI.getSpell());
		}
		if (player.getCastSpell() == null) {
			return false;
		}
		// Check the cast using the spell implementation.
		return player.getCastSpell().canCast(player, true);
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {

		if (entity.isPlayer()) {
			Player player = (Player) entity;
			int equipment = player.getEquipment().get(Equipment.WEAPON_SLOT).getId();
			player.prepareSpell(player.getCastSpell(), victim);
			if(player.isAutocast() && player.getAutocastSpell() != null)
				player.setCastSpell(player.getAutocastSpell());
			
			if(equipment == 14006 || equipment == 13867 || equipment == 6483) {
				player.setCastSpell(CombatSpells.TIER1.getSpell());
				player.setAutocast(true);
			}
			if(equipment == 19468) {
				player.setCastSpell(CombatSpells.TIER3.getSpell());
				player.setAutocast(true);
			}
			if(equipment == 3951) {
				player.setCastSpell(CombatSpells.TIER4.getSpell());
				player.setAutocast(true);
			} 
			if(equipment == 19720) {
				player.setCastSpell(CombatSpells.TIER5.getSpell());
				player.setAutocast(true);
			} 
			if(equipment == 15653) {
				player.setCastSpell(CombatSpells.TIER6.getSpell());
				player.setAutocast(true);
			} 
			if(equipment == 15656) {
				player.setCastSpell(CombatSpells.TIER7.getSpell());
				player.setAutocast(true);
			}
			if(equipment == 5129) {
				player.setCastSpell(CombatSpells.TIER7.getSpell());
				player.setAutocast(true);
			}
			if(equipment == 19727 || equipment == 18891) {
				player.setCastSpell(CombatSpells.TIER9.getSpell());
				player.setAutocast(true);
			} 
			if(equipment == 13995 || equipment == 8664 || equipment == 3282) {
				player.setCastSpell(CombatSpells.DRAGONLAVA.getSpell());
				player.setAutocast(true);
			} 
			
			player.setPreviousCastSpell(player.getCastSpell());
		} else if (entity.isNpc()) {
			NPC npc = (NPC) entity;

			switch (npc.getId()) {
			case 13:
			case 172:
			case 174:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] { CombatSpells.WEAKEN, CombatSpells.FIRE_STRIKE, CombatSpells.EARTH_STRIKE, CombatSpells.WATER_STRIKE }).getSpell(), victim);
				break;
			case 2025:
			case 1643:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.FIRE_WAVE, CombatSpells.EARTH_WAVE, CombatSpells.WATER_WAVE }).getSpell(), victim);
				break;
			case 3495:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.SMOKE_BLITZ, CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST}).getSpell(), victim);
				break;
			case 3496:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.BLOOD_BARRAGE, CombatSpells.BLOOD_BURST, CombatSpells.BLOOD_BLITZ, CombatSpells.BLOOD_RUSH}).getSpell(), victim);
				break;
			case 3491:
				npc.prepareSpell(RandomUtility.randomElement(new CombatSpells[] {CombatSpells.ICE_BARRAGE, CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST, CombatSpells.ICE_RUSH}).getSpell(), victim);
				break;
			case 13454:
				npc.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
				break;
			case 13453:
				npc.prepareSpell(CombatSpells.BLOOD_BURST.getSpell(), victim);
				break;
			case 13452:
				npc.prepareSpell(CombatSpells.BLOOD_BURST.getSpell(), victim);
				break;
			case 13451:
				npc.prepareSpell(CombatSpells.SHADOW_BURST.getSpell(), victim);
				break;
			case 2896:
				npc.prepareSpell(CombatSpells.WATER_STRIKE.getSpell(), victim);
				break;
			case 2882:
				npc.prepareSpell(CombatSpells.DAGANNOTH_PRIME.getSpell(), victim);
				break;
			case 6254:
				npc.prepareSpell(CombatSpells.WIND_WAVE.getSpell(), victim);
				break;
			case 6257:
				npc.prepareSpell(CombatSpells.WATER_WAVE.getSpell(), victim);
				break;
			case 6278:
				npc.prepareSpell(CombatSpells.SHADOW_RUSH.getSpell(), victim);
				break;
			case 6221:
				npc.prepareSpell(CombatSpells.FIRE_BLAST.getSpell(), victim);
				break;
			}

			if (npc.getCurrentlyCasting() == null)
				npc.prepareSpell(CombatSpells.WIND_STRIKE.getSpell(), victim);
		}

		if (entity.getCurrentlyCasting().maximumHit() == -1) {
			return new CombatContainer(entity, victim, CombatType.MAGIC, true);
		}

		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		int distance = 8;
		if(entity.isNpc()) {
			switch(((NPC) entity).getId()) {
			case 2896:
			case 13451:
			case 13452:
			case 13453:
			case 13454:
				distance = 40;
				break;
			}
		}
		return distance;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
