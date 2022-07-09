package com.zamron.world.content.combat;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.aoesystem.AOEHandler;
import com.zamron.world.content.aoesystem.AOESystem;
import com.zamron.world.content.aoesystem.AOEWeaponData;
import com.zamron.world.content.combat.CombatContainer.CombatHit;
import com.zamron.world.content.combat.strategy.impl.Nex;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator.CoordinateState;
import com.zamron.world.entity.impl.player.Player;

import java.util.List;

/**
 * A {@link Task} implementation that deals a series of hits to an entity after
 * a delay.
 *
 * @author lare96
 */
public class CombatHitTask extends Task {

    /**
     * The attacker instance.
     */
    private Character attacker;

    /**
     * The victim instance.
     */
    private Character victim;

    /**
     * The attacker's combat builder attached to this task.
     */
    private CombatBuilder builder;

    /**
     * The attacker's combat container that will be used.
     */
    private CombatContainer container;

    /**
     * The total damage dealt during this hit.
     */
    private int damage;

    /**
     * Create a new {@link CombatHit}.
     *
     * @param builder the combat builder attached to this task.
     * @param container the combat hit that will be used.
     * @param delay the delay in ticks before the hit will be dealt.
     * @param initialRun if the task should be ran right away.
     */
    public CombatHitTask(CombatBuilder builder, CombatContainer container,
            int delay, boolean initialRun) {
        super(delay, builder.getCharacter(), initialRun);
        this.builder = builder;
        this.container = container;
        this.attacker = builder.getCharacter();
        this.victim = builder.getVictim();
    }

    public CombatHitTask(CombatBuilder builder, CombatContainer container) { //Instant attack, no task
        this.builder = builder;
        this.container = container;
        this.attacker = builder.getCharacter();
        this.victim = builder.getVictim();
    }

    @Override
    public void execute() {

        handleAttack();

        this.stop();
    }

    public void handleEntityInterface(Character attacker, Character victim, int damage) {
        if (attacker.isPlayer()) {
            Player p = (Player) attacker;
            if (victim.isPlayer()) {//plrs
                Player v = (Player) victim;
                if (v.isMiniMe) {
                    return;
                }
                String entityName = v.getUsername();
                p.getPacketSender().sendEntityInterface(entityName);
            } else if (victim.isNpc()) {//npcs
                NPC v = (NPC) victim;
                String entityName = v.getDefinition().getName();
                p.getPacketSender().sendEntityInterface(entityName);

            }
        }
    }


    public void handleAttack() {

        if (attacker.isPlayer() && !attacker.getAsPlayer().isMiniMe) {
            if (victim.isNpc()) {
                NPC vic = (NPC) victim;
                String name = vic.getDefinition().getName();
                attacker.getAsPlayer().getPacketSender().sendRichPresenceState("Fighting "+ Misc.formatPlayerName(name));
            }
            if (victim.isPlayer()){
                Player vic = (Player) victim;
                String name = vic.getUsername();
                attacker.getAsPlayer().getPacketSender().sendRichPresenceState("Fighting "+ Misc.formatPlayerName(name));
            }
        }


        if (attacker.getConstitution() <= 0 || !attacker.isRegistered()) {
            return;
        }

        if (victim.isPlayer()) {
            Player botTest = (Player) victim;
            if (botTest.isMiniMe)
                return;
        }

		// Do any hit modifications to the container here first.
        if (container.getModifiedDamage() > 0) {
            container.allHits(context -> {
                context.getHit().setDamage(container.getModifiedDamage());
                context.setAccurate(true);
            });
        }

		// Now we send the hitsplats if needed! We can't send the hitsplats
        // there are none to send, or if we're using magic and it splashed.
        if (container.getHits().length != 0 && container.getCombatType() != CombatType.MAGIC || container.isAccurate()) {

            /**
             * PRAYERS *
             */
            CombatFactory.applyPrayerProtection(container, builder);
            if(victim != null) {
            this.damage = container.getDamage();
            victim.getCombatBuilder().addDamage(attacker, damage);
            container.dealDamage();

            handleEntityInterface(attacker, victim, damage);
            } else {
            	System.err.println("Victim is null");
            }

            /**
             * MISC *
             */
            if (attacker.isPlayer()) {
                Player p = (Player) attacker;
                if (damage > 0) {
                    if (p.getLocation() == Location.PEST_CONTROL_GAME) {
                        p.getMinigameAttributes().getPestControlAttributes().incrementDamageDealt(damage);
                    } else if (p.getLocation() == Location.DUNGEONEERING) {
                        p.getMinigameAttributes().getDungeoneeringAttributes().incrementDamageDealt(damage);
                    }
                    /**
                     * ACHIEVEMENTS *
                     */
                    if (container.getCombatType() == CombatType.MELEE) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MELEE, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MELEE, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MELEE, damage);
                    } else if (container.getCombatType() == CombatType.RANGED) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_RANGED, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_RANGED, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_RANGED, damage);
                    } else if (container.getCombatType() == CombatType.MAGIC) {
                        Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MAGIC, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MAGIC, damage);
                        Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MAGIC, damage);
                    }
                    
					AOEWeaponData aoeData = AOESystem.getSingleton()
							.getAOEData(p.getEquipment().get(Equipment.WEAPON_SLOT).getId());

					if (aoeData != null && aoeData.getRadius() > 0) {
						AOEHandler.handleAttack(p, victim, aoeData.getMinDamage(), aoeData.getMaxDamage(),
								aoeData.getRadius(), aoeData.getIcon());
					}
					if (GameSettings.DOUBLE_DAMAGE_EVENT) {
						p.getDpsOverlay().incrementDamageDone(damage * 2);
					} else if (GameSettings.TRIPLE_DAMAGE_EVENT) {
						p.getDpsOverlay().incrementDamageDone(damage * 3);
					} else
						p.getDpsOverlay().incrementDamageDone(damage);
                    
                    if (victim.isPlayer()) {
                        Achievements.finishAchievement(p, AchievementData.FIGHT_ANOTHER_PLAYER);
                    }
                    Player bot = (World.getPlayerByName(p.getUsername() + "!"));
                    if (bot != null && bot.isMiniMe && bot.getMinimeOwner() == p) {
                        bot.getCombatBuilder().attack(victim);
                    }
                }
            } else {
                if (victim.isPlayer() && container.getCombatType() == CombatType.DRAGON_FIRE) {
                    Player p = (Player) victim;
                    if (Misc.getRandom(20) <= 15 && p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613) {
                        p.setPositionToFace(attacker.getPosition().copy());
                        CombatFactory.chargeDragonFireShield(p);
                    }
                    if (damage >= 160) {
                        ((Player) victim).getPacketSender().sendMessage("You are badly burnt by the dragon's fire!");

                    }
                }
            }
        }

        // Give experience based on the hits.
        CombatFactory.giveExperience(builder, container, damage);

        if (!container.isAccurate()) {
            if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
                victim.performGraphic(new Graphic(85, GraphicHeight.MIDDLE));
                attacker.getCurrentlyCasting().finishCast(attacker, victim, false, 0);
                attacker.setCurrentlyCasting(null);
            }
        } else if (container.isAccurate()) {

            CombatFactory.handleArmorEffects(attacker, victim, damage, container.getCombatType());
            CombatFactory.handlePrayerEffects(attacker, victim, damage, container.getCombatType());
            CombatFactory.handleSpellEffects(attacker, victim, damage, container.getCombatType());

            attacker.poisonVictim(victim, container.getCombatType());

            // Finish the magic spell with the correct end graphic.
            if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
                attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::performGraphic);
                attacker.getCurrentlyCasting().finishCast(attacker, victim, true, damage);
                attacker.setCurrentlyCasting(null);
            }
        }

        // Send the defensive animations.
        if (victim.getCombatBuilder().getAttackTimer() <= 2) {
            if (victim.isPlayer()) {
                victim.performAnimation(new Animation(WeaponAnimations.getBlockAnimation(((Player) victim))));
                if (((Player) victim).getInterfaceId() > 0) {
                    ((Player) victim).getPacketSender().sendInterfaceRemoval();
                }
            } else if (victim.isNpc()) {
                if (!(((NPC) victim).getId() >= 6142 && ((NPC) victim).getId() <= 6145)) {
                    victim.performAnimation(new Animation(((NPC) victim).getDefinition().getDefenceAnimation()));
                }
            }
        }

        // Fire the container's dynamic hit method.
        container.onHit(damage, container.isAccurate());

        // And finally auto-retaliate if needed.
        if (!victim.getCombatBuilder().isAttacking() || victim.getCombatBuilder().isCooldown() || victim.isNpc() && ((NPC) victim).findNewTarget()) {
            if (victim.isPlayer() && ((Player) victim).isAutoRetaliate() && !victim.getMovementQueue().isMoving() && ((Player) victim).getWalkToTask() == null) {
                victim.getCombatBuilder().setDidAutoRetaliate(true);
                victim.getCombatBuilder().attack(attacker);
            } else if (victim.isNpc()) {
                if (!(attacker.isNpc() && ((NPC) attacker).isSummoningNpc())) {
                    NPC npc = (NPC) victim;
                    if (npc.getMovementCoordinator().getCoordinateState() == CoordinateState.HOME && npc.getLocation() != Location.PEST_CONTROL_GAME) {
                        victim.getCombatBuilder().attack(attacker);
                        npc.setFindNewTarget(false);
                    }
                }
            }
        }

        if (attacker.isNpc() && victim.isPlayer()) {
            NPC npc = (NPC) attacker;
            Player p = (Player) victim;
            if (npc.switchesVictim() && Misc.getRandom(6) <= 1) {
                if (npc.getDefinition().isAggressive()) {
                    npc.setFindNewTarget(true);
                } else {
                    if (p.getLocalPlayers().size() >= 1) {
                        List<Player> list = p.getLocalPlayers();
                        Player c = list.get(Misc.getRandom(list.size() - 1));
                        npc.getCombatBuilder().attack(c);
                    }
                }
            }

            Sounds.sendSound(p, Sounds.getPlayerBlockSounds(p.getEquipment().get(Equipment.WEAPON_SLOT).getId()));
            /**
             * CUSTOM ON DAMAGE STUFF *
             */
            if (victim.isPlayer() && npc.getId() == 13447) {
                Nex.dealtDamage(((Player) victim), damage);
            }

        } else if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            /**
             * SKULLS *
             */
            if (player.getLocation() == Location.WILDERNESS && victim.isPlayer()) {
                if (!player.getCombatBuilder().isBeingAttacked() && !player.getCombatBuilder().didAutoRetaliate() || player.getCombatBuilder().isBeingAttacked() && player.getCombatBuilder().getLastAttacker() != victim && Location.inMulti(player)) {
                    CombatFactory.skullPlayer(player);
                }
            }

            player.setLastCombatType(container.getCombatType());

            Sounds.sendSound(player, Sounds.getPlayerAttackSound(player));

            /**
             * CUSTOM ON DAMAGE STUFF *
             */
            if (victim.isNpc() && ((NPC) victim).getId() == 13447) {
                Nex.takeDamage(player, damage);
            } else if (victim.isPlayer()) {
                Sounds.sendSound((Player) victim, Sounds.getPlayerBlockSounds(((Player) victim).getEquipment().get(Equipment.WEAPON_SLOT).getId()));
            }
        }
    }
}
