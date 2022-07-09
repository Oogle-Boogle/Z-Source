package com.zamron.world.content.combat.strategy.impl;

import com.google.common.graph.Graph;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatHitTask;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 *
 * @Author Oogleboogle
 * Zamron
 */

public class Zorbak implements CombatStrategy {

    private static int animID;
    private static int health;
    private static int attack;
    private static int defence;

    public static NPC cB;

    public static int getAnimID() {
        return animID;
    }

    public static int getAttack() {
        return attack;
    }

    public static int getDefence() {
        return defence;
    }

    public static int getHealth() {
        return health;
    }

    private static final Animation attack_anim = new Animation(2078);// but low anim ids
    private static final Animation Stage2 = new Animation(10493);

    // private static final Graphic gfx2= new Graphic(1213);
    private static final Graphic gfx2 = new Graphic(1212, 3, GraphicHeight.LOW); //Fire
    private static final Graphic gfx3 = new Graphic(606, 3, GraphicHeight.LOW);

    public static String[] messages = { "Mi sangas pro la vundoj de inferaj trancxoj!", "La Diablo estas vivanta ene de mia korpo!"

    };

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC cB = (NPC) entity;
        Player target = (Player) victim;
        if (cB.isChargingAttack() || cB.getConstitution() <= 0) {
            return true;
        }
        boolean melee = Misc.getRandom(3) == 1 && Locations.goodDistance(target.getPosition(), cB.getPosition(), 1);
        if (melee) {
            cB.getCombatBuilder().setVictim(target);
            cB.performAnimation(new Animation(69));
            cB.performGraphic(gfx3);
            new CombatHitTask(cB.getCombatBuilder(), new CombatContainer(cB, target, 1, CombatType.MELEE, true))
                    .handleAttack();
        }
        cB.performAnimation(Stage2);
        cB.performGraphic(gfx2);
        TaskManager.submit(new Task(1, victim, false) {
            int tick = 0;

            @Override
            public void execute() {
                switch (tick) {
                    case 1:

                        int random = Misc.getRandom(100);

                        if (random >= 45) {
                            if (random >= 45 && Misc.getRandom(10) == 4) {
                                cB.forceChat(messages[Misc.getRandom(messages.length - 1)]);
                            }
                            if (cB.getConstitution() <= 0) {
                                cB.forceChat("Meh heh heh");
                                return;
                            }
                            cB.forceChat("Mi sangas pro la vundoj de inferaj trancxoj!");
                            new Projectile(cB, target, 606, 44, 3, 43, 43, 0).sendProjectile();
                        } else {
                            cB.performAnimation(attack_anim);
                        }
                        break;
                    case 2:
                        new CombatHitTask(cB.getCombatBuilder(),
                                new CombatContainer(cB, target, 1, CombatType.MAGIC, false)).handleAttack();
                        stop();
                        break;
                }
                tick++;
            }

        });
        return true;
    }

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer();
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 4;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
