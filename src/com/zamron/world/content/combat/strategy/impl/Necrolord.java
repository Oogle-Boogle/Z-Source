package com.zamron.world.content.combat.strategy.impl;

import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.magic.CombatSpells;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

/**
 * @author adam trinity // 8bytes/ adamm__ skype: live:nrpker7839
 */


public class Necrolord implements CombatStrategy {

 @Override
 public boolean canAttack(Character entity, Character victim) {
  return true;
 }

 @Override
 public boolean customContainerAttack(Character entity, Character victim) {
  return false;
 }

 @Override
 public CombatContainer attack(Character entity, Character victim) {
  NPC cobra = (NPC)entity;
  int att = Misc.getRandom(3);
  if(att == 1) {
   cobra.prepareSpell(CombatSpells.BLOOD_BARRAGE.getSpell(), victim);
  }
  if(att == 2) {
			cobra.prepareSpell(CombatSpells.ICE_BARRAGE.getSpell(), victim);
  }
  if(att == 3) {
   cobra.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
  }
  return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
 }
 @Override
 public int attackDelay(Character entity) {
  return entity.getAttackSpeed();
 }

 @Override
 public int attackDistance(Character entity) {
  return 15;
 }

 @Override
 public CombatType getCombatType() {
  return CombatType.MAGIC;
 }
}