package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations;
import com.zamron.model.Projectile;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;

public class BrolyCombat implements CombatStrategy {

    private static final Animation anim = new Animation(12063);//melee attack
    private static final Animation anim2 = new Animation(12062);//mage attack
    private static final Animation anim3 = new Animation(12061);// mage attack
    // no listen, Basically, adam trinity gave me this like 9 bosses or smthing, cachesided,

    private static final Graphic gfx1 = new Graphic(1901, 3, GraphicHeight.MIDDLE);
    private static final Graphic gfx2 = new Graphic(1901);

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
        NPC skele = (NPC) entity;
        if (victim.getConstitution() <= 0) {
            return true;
        }
        if (skele.isChargingAttack()) {
            return true;
        }
        if (Locations.goodDistance(skele.getPosition().copy(), victim.getPosition().copy(), 1)
                && Misc.getRandom(6) <= 4) {
            skele.performAnimation(anim);
            skele.performGraphic(gfx1);
            skele.getCombatBuilder().setContainer(new CombatContainer(skele, victim, 1, 2, CombatType.MELEE, true));
        } else if (Misc.getRandom(10) <= 7) {
            skele.performAnimation(anim2);
            skele.setChargingAttack(true);
            skele.getCombatBuilder().setContainer(new CombatContainer(skele, victim, 1, 2, CombatType.MAGIC, true));
            TaskManager.submit(new Task(1, skele, false) {
                @Override
                protected void execute() {
                    new Projectile(skele, victim, 1901, 44, 3, 43, 31, 0).sendProjectile();
                    skele.setChargingAttack(false).getCombatBuilder()
                            .setAttackTimer(skele.getDefinition().getAttackSpeed() - 1);
                    stop();
                }
            });
        } else {
            skele.performAnimation(anim3);
            victim.performGraphic(gfx2);
            skele.getCombatBuilder().setContainer(new CombatContainer(skele, victim, 1, 2, CombatType.MAGIC, true));
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

}

/*
 * private static int animID; private static int health; private static int
 * attack; private static int defence; private static String forceChat; public
 * static NPC skeletalHorror;
 *
 *
 *
 * public static int getAnimID() { return animID; }
 *
 * public static int getAttack() { return attack; }
 *
 * public static int geskeleefence() { return defence; }
 *
 * public static String getForceChat() { return forceChat; }
 *
 * public static int getHealth() { return health; }
 *
 *
 *
 * public Animation Stage1Attack = new Animation(12063); public Animation
 * Stage2Attack = new Animation(12062); public Animation Stage3Attack = new
 * Animation(12061);
 *
 * public boolean canAttack(Character entity, Character victim) { return true; }
 *
 * public CombatContainer attack(Character entity, Character victim) { return
 * null; }
 *
 * public static void spawn() { skeletalHorror = new NPC(9176, new Position(0,
 * 0));
 *
 *
 * World.register(skeletalHorror); } public static void despawn(boolean nex) {
 *
 *
 * if(skeletalHorror != null && skeletalHorror.isRegistered())
 * World.deregister(skeletalHorror); }
 *
 *
 * public boolean customContainerAttack(Character entity, Character victim) {
 * NPC monster = (NPC) entity;
 * if(Locations.goodDistance(monster.getPosition().copy(),
 * victim.getPosition().copy(), 1)){ if (monster.isChargingAttack() ||
 * monster.getConstitution() <= 0) { return true; }
 * switch(Misc.inclusiveRandom(1, 4)) {
 *
 * case 1: if(skeletalHorror.getConstitution() <= 25000) {
 * monster.performAnimation(Stage1Attack);
 * monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim,
 * 1, 0, CombatType.MELEE, true)); } break;
 *
 * case 4: if(skeletalHorror.getConstitution() <= 5000) {
 *
 * monster.performAnimation(Stage3Attack);
 * monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim,
 * 2, 1, CombatType.MAGIC, true)); final Player p = (Player) victim;
 * p.getPacketSender().sendGlobalGraphic(new Graphic(1897),
 * victim.getPosition().copy()); }
 *
 * break; } return true; } return false; }
 *
 * public int attackDelay(Character entity) { return entity.getAttackSpeed(); }
 *
 * public int attackDistance(Character entity) { return 1; }
 *
 * public CombatType getCombatType() { return CombatType.MELEE; }
 *
 *
 *
 *
 * private int gfxID;
 *
 * public int getGfxID() { return gfxID; }
 *
 *
 *
 * public void setGfxID(int gfxID) { this.gfxID = gfxID; }
 *
 * public void setHealth(int health) { this.health = health; }
 *
 *
 * }
 */
