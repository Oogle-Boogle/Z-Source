package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.*;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.effect.CombatPoisonEffect;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Sephstrat implements CombatStrategy {

    private static final Graphic seph_healing_graphic = new Graphic(444);

    private static void attackAll(Character entity, Character victim) {
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
        NPC seph = (NPC)entity;
        Animation attackAnim = new Animation(422);
        @SuppressWarnings("unused")
        Graphic graphic1 = seph.getGraphic();

        if(seph.getConstitution() <= 0) {
            return true;
        }
        if 	(Misc.random(1000) <= 320 && seph.getConstitution() <= 6000000){
            seph.performGraphic(seph_healing_graphic);
            seph.forceChat("Not so easy now! Bring it!");
            victim.dealDamage(new Hit(150, Hitmask.DARK_RED, CombatIcon.NONE));
            seph.setConstitution(seph.getConstitution() + Misc.getRandom(5000000));
        }

        if (Misc.random(3) == 1) {
            Player target = (Player)victim;
            for (Player t : Misc.getCombinedPlayerList(target)) {
                if(t == null || t.getLocation() != Locations.Location.Seph)
                    continue;
                if (Locations.goodDistance(t.getPosition(), seph.getPosition(), 2)) {
                    seph.getCombatBuilder().setVictim(t);
                    new CombatHitTask(seph.getCombatBuilder(), new CombatContainer(seph, t, 2, CombatType.MELEE, true)).handleAttack();
                    CombatFactory.poisonEntity(t, CombatPoisonEffect.PoisonType.BOSS);
                }
            }
        } else {
            seph.performAnimation(attackAnim);
            seph.getCombatBuilder().setContainer(new CombatContainer(seph, victim, 1, 1, CombatType.MELEE, true));
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
}
