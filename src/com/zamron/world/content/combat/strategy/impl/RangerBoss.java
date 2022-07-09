package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 *
 * @Author Oogleboogle
 * Zamron
 *
 */

public class RangerBoss implements CombatStrategy {

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
        NPC DarkRanger = (NPC)entity;
        if(victim.getConstitution() <= 0) {
            return true;
        }
        if(DarkRanger.isChargingAttack()) {
            return true;
        }
        final Position pos = new Position(victim.getPosition().getX(), victim.getPosition().getY());
        DarkRanger.setChargingAttack(true);
        DarkRanger.performAnimation(new Animation(426));
        final CombatType attkType = Misc.getRandom(5) <= 2 ? CombatType.RANGED : CombatType.RANGED;
        DarkRanger.getCombatBuilder().setContainer(new CombatContainer(DarkRanger, victim, 1, 6, attkType, Misc.getRandom(5) <= 1 ? false : true));
        TaskManager.submit(new Task(1, DarkRanger, false) {
            int tick = 0;
            @Override
            public void execute() {
                if(tick == 2) {
                    DarkRanger.performAnimation(new Animation((4973)));
                    ((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(832), pos);
                    new Projectile(DarkRanger, victim, (attkType == CombatType.RANGED ? 605 : 473), 44, 8, 43, 43, 0).sendProjectile();
                    DarkRanger.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
        return true;
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
