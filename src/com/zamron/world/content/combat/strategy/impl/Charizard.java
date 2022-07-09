package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class Charizard implements CombatStrategy {

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
        NPC dragon = (NPC)entity;
        if(dragon.isChargingAttack() || dragon.getConstitution() <= 0) {
            dragon.getCombatBuilder().setAttackTimer(4);
            return true;
        }
        if(Locations.goodDistance(dragon.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            dragon.performAnimation(new Animation(dragon.getDefinition().getAttackAnimation()));
            dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 1, CombatType.MELEE, true));
        } else {
            dragon.setChargingAttack(true);
            dragon.performAnimation(new Animation(getAnimation(dragon.getId())));
            dragon.getCombatBuilder().setContainer(new CombatContainer(dragon, victim, 1, 3, CombatType.DRAGON_FIRE, true));
            TaskManager.submit(new Task(1, dragon, false) {
                int tick = 0;
                @Override
                public void execute() {
                    if(tick == 1 && dragon.getId() == 2843 || dragon.getId() == 174) {
                        new Projectile(dragon, victim, 393 + Misc.getRandom(3), 44, 3, 43, 43, 0).sendProjectile();
                    } else
                    if(tick == 2) {
                        victim.performGraphic(new Graphic(5));
                    } else if(tick == 3) {
                        victim.performGraphic(new Graphic(5));
                        dragon.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
                        stop();
                    }
                    tick++;
                }
            });

        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 8836; //Attk animation
        return anim;
    }


    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
