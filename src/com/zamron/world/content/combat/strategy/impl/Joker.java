package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Joker implements CombatStrategy {

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
        NPC Joker = (NPC)entity;
        if(Joker.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }
        if (Misc.getRandom(6) == 1) {
            Joker.forceChat("You must underestimate me?!");
        }
        if (Misc.getRandom(6) == 1) {
            Joker.forceChat("Do you know i'm the joker? Muhhaha! *cough*");
        }
        if (Misc.getRandom(6) == 1) {
            Joker.forceChat("Don't kill me!!!!");
        }
        if(Locations.goodDistance(Joker.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 1) {
            Joker.performAnimation(new Animation(Joker.getDefinition().getAttackAnimation()));
            Joker.getCombatBuilder().setContainer(new CombatContainer(Joker, victim, 1, 1, CombatType.MELEE, true));
        } else if(!Locations.goodDistance(Joker.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(2) == 1) {
            Joker.setChargingAttack(true);
            final Position pos = new Position(victim.getPosition().getX() - 2, victim.getPosition().getY());
            ((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(5327), pos);
            Joker.performAnimation(new Animation(77723));
            Joker.forceChat("You can't run from me!");
            TaskManager.submit(new Task(2) {
                @Override
                protected void execute() {
                    Joker.moveTo(pos);
                    Joker.performAnimation(new Animation(Joker.getDefinition().getAttackAnimation()));
                    Joker.performAnimation(new Animation(77723));
                    Joker.forceChat("Take that!");
                    Joker.getCombatBuilder().setContainer(new CombatContainer(Joker, victim, 1, 1, CombatType.MELEE, false));
                    Joker.setChargingAttack(false);
                    Joker.getCombatBuilder().setAttackTimer(0);
                    stop();
                }
            });
        } else {
            Joker.setChargingAttack(true);
            boolean barrage = Misc.getRandom(4) <= 2;
            Joker.performAnimation(new Animation(barrage ? 929 : 929));
            Joker.getCombatBuilder().setContainer(new CombatContainer(Joker, victim, 1, 1, CombatType.MAGIC, true));
            TaskManager.submit(new Task(1, Joker, false) {
                int tick = 0;
                @Override
                public void execute() {
                    if(tick == 0 && !barrage) {
                        new Projectile(Joker, victim, 2718, 44, 3, 43, 43, 0).sendProjectile();
                    } else if(tick == 1) {
                        if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
                            victim.getMovementQueue().freeze(15);
                            victim.performGraphic(new Graphic(369));
                        }
                        if(barrage && Misc.getRandom(6) <= 3) {
                            Joker.performAnimation(new Animation(7774));
                            for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
                                if(toAttack != null && Locations.goodDistance(Joker.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
                                    new CombatHitTask(Joker.getCombatBuilder(), new CombatContainer(Joker, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
                                    toAttack.performGraphic(new Graphic(1557));

                                }
                            }
                        }
                        Joker.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Joker) - 2);
                        stop();
                    }
                    tick++;
                }
            });
        }
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

    public static Object initialize() {
        // TODO Auto-generated method stub
        return null;
    }
}
