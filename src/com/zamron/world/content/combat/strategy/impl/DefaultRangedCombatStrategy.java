package com.zamron.world.content.combat.strategy.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Flag;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.GroundItem;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.Projectile;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.model.definitions.WeaponInterfaces;
import com.zamron.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.CombatContainer.CombatHit;
import com.zamron.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zamron.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.zamron.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zamron.world.content.combat.range.CombatRangedAmmo.RangedWeaponType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.content.combat.weapon.FightStyle;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.content.minigames.impl.Dueling.DuelRule;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import com.google.common.collect.Multiset.Entry;

/**
 * The default combat strategy assigned to an {@link Character} during a ranged
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultRangedCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {

		// We do not need to check npcs.
		if (entity.isNpc()) {
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		// If we are using a crystal bow then we don't need to check for ammo.
		if (CombatFactory.crystalBow(player)) {
			return true;
		}

		if (Dueling.checkRule(player, DuelRule.NO_RANGED)) {
			player.getPacketSender().sendMessage("Ranged-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset(true);
			return false;
		}

		// Check the ammo before proceeding.
		if (!checkAmmo(player)) {
			if (player.isSpecialActivated()) {
				player.setSpecialActivated(false);
				player.getPacketSender().sendSpriteChange(41006, 945);
				CombatSpecial.updateBar(player);
			}
			return false;
		}
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public CombatContainer attack(Character entity, Character victim) {
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;

			AmmunitionData ammo = AmmunitionData.KRATONITE_ARROW;

			switch (npc.getId()) {
			case 688:
				ammo = AmmunitionData.BATHEUS_ARROW;
				break;
			case 27:
				ammo = AmmunitionData.MARMAROS_ARROW;
				break;
			case 2028:
				ammo = AmmunitionData.BOLT_RACK;
				break;
			case 6220:
			case 6256:
			case 6276:
				ammo = AmmunitionData.FRACTITIE_ARROW;
				break;
			case 6225:
				ammo = AmmunitionData.STEEL_JAVELIN;
				break;
			case 6252:
				ammo = AmmunitionData.FRACTITIE_ARROW;
				break;
			}

			entity.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));

			entity.performGraphic(new Graphic(ammo.getStartGfxId(),
					ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(npc, victim, ammo, false);

			return new CombatContainer(entity, victim, 1, CombatType.RANGED, true);
		}

		Player player = (Player) entity;
		final boolean dBow = CombatFactory.darkBow(player);
		final boolean zBow = CombatFactory.zaryteBow(player);

		player.setFireAmmo(0);

		startAnimation(player);
		AmmunitionData ammo = RangedWeaponData.getAmmunitionData(player);

		if (!player.isSpecialActivated()) {
			if (!CombatFactory.crystalBow(player)) {
				decrementAmmo(player, victim.getPosition());
				if (dBow || player.getRangedWeaponData() == RangedWeaponData.MAGIC_SHORTBOW
						&& player.isSpecialActivated() && player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW) {
					decrementAmmo(player, victim.getPosition());
				}
			}
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926)
				new Graphic(ammo.getStartGfxId(), GraphicHeight.LOW);
			player.performGraphic(new Graphic(ammo.getStartGfxId(), ammo.getStartGfxId() == 2138 ? GraphicHeight.LOW
					: ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(player, victim, ammo, dBow);
		}

		CombatContainer container = new CombatContainer(entity, victim, dBow ? 2 : 1, CombatType.RANGED, true);

		/** CROSSBOW BOLTS EFFECT **/
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition() != null && player.getEquipment()
				.get(Equipment.WEAPON_SLOT).getDefinition().getName().toLowerCase().contains("crossbow")) {
			if (Misc.getRandom(12) >= 10) {
				container.setModifiedDamage(getModifiedDamage(player, victim, container));
			}
		}

		return container;
	}

	public static void fireProjectile(Character e, Character victim, final AmmunitionData ammo, boolean dBow) {
		TaskManager.submit(new Task(1, e.getCombatBuilder(), false) { // TODO
																		// FIX
																		// THIS
																		// PROJECTILE,
																		// SHOULDNT
																		// BE A
																		// TASK
			@Override
			protected void execute() {
				new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed(), ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				if (dBow) {
					new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 32,
							ammo.getProjectileSpeed(), ammo.getStartHeight() - 2, ammo.getEndHeight(), 0)
									.sendProjectile();
				}

				stop();
			}
		});
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {

		// The default distance for all npcs using ranged is 6.
		if (entity.isNpc()) {
			return 6;
		}

		// Create the player instance.

		Player player = (Player) entity;

		int distance = 5;
		if (player.getRangedWeaponData() != null)
			distance = player.getRangedWeaponData().getType().getDistanceRequired();

		return distance + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}

	/**
	 * Starts the performAnimation for the argued {@link Player} in the current
	 * combat hook.
	 * 
	 * @param player the player to start the performAnimation for.
	 */
	private void startAnimation(Player player) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
			player.performAnimation(new Animation(2075));
		} else if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926) {
			player.performAnimation(new Animation(5061));
		} else {
			player.performAnimation(new Animation(WeaponAnimations.getAttackAnimation(player)));
		}
	}

	/**
	 * Checks the ammo to make sure the argued {@link Player} has the right type and
	 * amount before attacking.
	 * 
	 * @param player the player's ammo to check.
	 * @return <code>true</code> if the player has the right ammo,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAmmo(Player player) {
		RangedWeaponData data = player.getRangedWeaponData();
		if (data.getType() == RangedWeaponType.THROW || data.getType() == RangedWeaponType.BLOWPIPE)
			return true;
		Item ammunition = player.getEquipment().getItems()[data.getType() == RangedWeaponType.THROW
				? Equipment.WEAPON_SLOT
				: Equipment.AMMUNITION_SLOT];
		boolean darkBow = data.getType() == RangedWeaponType.DARK_BOW && ammunition.getAmount() < 2
				|| data == RangedWeaponData.MAGIC_SHORTBOW && player.isSpecialActivated()
						&& player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW && ammunition.getAmount() < 2;
		if (ammunition.getId() == -1 || ammunition.getAmount() < 1 || darkBow) {
			player.getPacketSender().sendMessage(darkBow ? "You need at least 2 arrows to fire this bow."
					: "You don't have any ammunition to fire.");
			player.getCombatBuilder().reset(true);
			return false;
		}
		boolean properEquipment = false;
		for (AmmunitionData ammo : data.getAmmunitionData()) {
			for (int i : ammo.getItemIds()) {
				if (i == ammunition.getId()) {
					properEquipment = true;
					break;
				}
			}
		}
		if (!properEquipment) {
			String ammoName = ammunition.getDefinition().getName(),
					weaponName = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName(),
					add = !ammoName.endsWith("s") && !ammoName.endsWith("(e)") ? "s" : "";
			player.getPacketSender().sendMessage("You can not use " + ammoName + "" + add + " with "
					+ Misc.anOrA(weaponName) + " " + weaponName + ".");
			player.getCombatBuilder().reset(true);
			return false;
		}

		return true;
	}

	/**
	 * Decrements the amount ammo the {@link Player} currently has equipped.
	 * 
	 * @param player the player to decrement ammo for.
	 */
	public static void decrementAmmo(Player player, Position pos) {
        /** Bools to store equipment values **/
        final boolean avas = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10499;
        final boolean comp = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 14022;
        final boolean max = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 14019;
        final boolean vet = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 14021;
		final boolean col = player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 19886;
		final boolean coli = player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 19106;
		final boolean col5 = player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 15454;
		final boolean aura = player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 19156;
        final boolean dBow = CombatFactory.darkBow(player);

        // Determine which slot we are decrementing ammo from.
        int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.MINIGUN || player.getWeapon() == WeaponInterface.LONGBOW
                || player.getWeapon() == WeaponInterface.CROSSBOW ? Equipment.AMMUNITION_SLOT : Equipment.WEAPON_SLOT;

        // Set the ammo we are currently using.
        player.setFireAmmo(player.getEquipment().get(slot).getId());

        if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12926) { //Blowpipe
            if (!player.getBlowpipeLoading().getContents().isEmpty()) {
                for (Entry<Integer> dart : player.getBlowpipeLoading().getContents().entrySet()) {
                    player.getBlowpipeLoading().getContents().remove(dart.getElement());
                    return;
                }
            } else {
                player.getPacketSender().sendMessage("You have run out of ammunition!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

		if ((avas || comp || max || vet) && Misc.getRandom(4) == 1) { //Save arrow method 50%
			return;
		}

		if ((coli || col5 || aura)) {
			return;
		}
        if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12927) { //Blowpipe
            if (!player.getBlowpipeLoading().getContents().isEmpty()) {
                for (Entry<Integer> dart : player.getBlowpipeLoading().getContents().entrySet()) {
                    player.getBlowpipeLoading().getContents().remove(dart.getElement());
                    return;
                }
            } else {
                player.getPacketSender().sendMessage("You have run out of ammunition!");
                player.getCombatBuilder().reset(true);
                return;
            }
        }

        // If we are at 0 ammo remove the item from the equipment completely.
        if (player.getEquipment().get(slot).getAmount() == 0) {
            player.getPacketSender().sendMessage("You have run out of ammunition!");
            player.getEquipment().set(slot, new Item(-1));
            player.getCombatBuilder().reset(true);

            if (slot == Equipment.WEAPON_SLOT) {
                WeaponInterfaces.assign(player, new Item(-1));
            }
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        int distance = player.getPosition().getDistance(pos);


        if (distance > 2) //If we're further than 2 spaces, set a cap at 2
            distance = 2;


        if (dBow || player.isSpecialActivated() && player.getCombatSpecial() != null
                && player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW || player.getCombatSpecial() == CombatSpecial.MAGIC_LONGBOW) {

            player.getEquipment().set(slot, new Item(player.getFireAmmo(), player.getEquipment().get(slot).getAmount()-2)); //Removes the ammo

            TaskManager.submit(new Task(distance, player, false) {

                @Override
                protected void execute() {
                    GroundItemManager.spawnGroundItem(player,
                            new GroundItem(new Item(player.getFireAmmo(), 2), pos, player.getUsername(),
                                    false, 120, true, 120));

                    stop();
                }
            });

        } else {
            player.getEquipment().set(slot, new Item(player.getFireAmmo(), player.getEquipment().get(slot).getAmount()-1)); //Removes the ammo
            TaskManager.submit(new Task(distance, player, false) {

                @Override
                protected void execute() {
                    GroundItemManager.spawnGroundItem(player,
                            new GroundItem(new Item(player.getFireAmmo(), 1), pos, player.getUsername(),
                                    false, 120, true, 120));

                    stop();
                }
            });

        }
		if ((avas || comp || max || vet) && Misc.getRandom(1) == 1) { //Save arrow method 50%
			System.out.println("Arrow saved");
			return;
		}

        // Refresh the equipment interface.
        player.getEquipment().refreshItems();
        ;

	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	private static final int getModifiedDamage(Player player, Character target, CombatContainer container) {
		if (container == null || container.getHits().length < 1)
			return 0;
		CombatHit hit = container.getHits()[0];
		if (!hit.isAccurate())
			return 0;
		int damage = container.getHits()[0].getHit().getDamage();
		int ammo = player.getFireAmmo();
		if (ammo == -1) {
			return damage;
		}
		double multiplier = 1;
		Player pTarget = target.isPlayer() ? ((Player) target) : null;
		switch (ammo) {
		case 9236: // Lucky Lightning
			target.performGraphic(new Graphic(749));
			multiplier = 1.3;
			break;
		case 9237: // Earth's Fury
			target.performGraphic(new Graphic(755));
			multiplier = 1.05;
			break;
		case 9238: // Sea Curse
			target.performGraphic(new Graphic(750));
			multiplier = 1.1;
			break;
		case 9239: // Down To Earth
			target.performGraphic(new Graphic(757));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.MAGIC,
						pTarget.getSkillManager().getCurrentLevel(Skill.MAGIC) - 3);
				pTarget.getPacketSender().sendMessage("Your Magic level has been reduced.");
			}
			break;
		case 9240: // Clear Mind
			target.performGraphic(new Graphic(751));
			if (pTarget != null) {
				pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) - 40);
				if (pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
					pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
				}
				pTarget.getPacketSender().sendMessage("Your Prayer level has been leeched.");
				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) + 40);
				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
						.getMaxLevel(Skill.PRAYER)) {
					player.getSkillManager().setCurrentLevel(Skill.PRAYER,
							player.getSkillManager().getMaxLevel(Skill.PRAYER));
				} else {
					player.getPacketSender()
							.sendMessage("Your enchanced bolts leech some Prayer points from your opponent..");
				}
			}
			break;
		case 9241: // Magical Posion
			target.performGraphic(new Graphic(752));
			CombatFactory.poisonEntity(target, PoisonType.MILD);
			break;
		case 9242:
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)
					- player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / 200 < 10) {
				break;
			}
			int priceDamage = (int) (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) * 0.08);
			if (priceDamage < 0) {
				return damage;
			}
			int dmg2 = (int) ((int) ((int) target.getConstitution() * 0.065) > 1000 ? 650 + Misc.getRandom(50)
					: ((int) target.getConstitution() * 0.065));
			if (dmg2 <= 0) {
				return damage;
			}
			target.performGraphic(new Graphic(754));
			player.dealDamage(new Hit(priceDamage, Hitmask.RED, CombatIcon.RANGED));
			return dmg2;
		case 9243:
			target.performGraphic(new Graphic(758, GraphicHeight.MIDDLE));
			multiplier = 1.15;
			break;
		case 9244:
			target.performGraphic(new Graphic(756));
			if (pTarget != null && (pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283
					|| pTarget.getFireImmunity() > 0)) {
				return damage;
			}
			if (damage < 300 && Misc.getRandom(3) <= 1) {
				damage = 300 + Misc.getRandom(150);
			}
			multiplier = 1.33;
			break;
		case 9245:
			target.performGraphic(new Graphic(753));
			multiplier = 1.26;
			int heal = (int) (damage * 0.25) + 10;
			player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
					player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + heal);
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= 1120) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 1120);
			}
			player.getSkillManager().updateSkill(Skill.CONSTITUTION);
			if (damage < 250 && Misc.getRandom(3) <= 1) {
				damage += 150 + Misc.getRandom(80);
			}
			break;
		}
		return (int) (damage * multiplier);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
