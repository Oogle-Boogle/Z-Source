package com.zamron.world.content.minimes;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.*;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.WeaponAnimations;
import com.zamron.util.NameUtils;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class MiniMeFunctions {

    public static void create(Player owner) {
        Player shadow = new Player(null).setUsername("Mini " + owner.getUsername() + "!");
        if (owner.getMinime() != null) {
            owner.sendMessage("@red@You call your mini-me!");
            MiniMeFunctions.startFollowing(owner);
            return;
        }
        shadow.moveTo(owner.getPosition());
        shadow.setLongUsername(NameUtils.stringToLong(shadow.getUsername()));
        shadow.isMiniMe = true;
        shadow.minimeOwner = owner;
        owner.setMinime(shadow);
        World.register(shadow);
        shadow.getSkillManager().setSkills(owner.getSkillManager().getSkills());
        owner.sendMessage("@blu@You call your mini-me!");
        shadow.getMovementQueue().setFollowCharacter(owner);
        CharacterAnimations originalPlayer = owner.getCharacterAnimations().clone(); // Makes it possible to store animations from the owner of the bot.
        Player MiniMe = owner.getMinime();
        MiniMeData.load(MiniMe);

        TaskManager.submit(new Task(2, owner.getMinime(), false) {
            @Override
            public void execute() {


                MiniMe.getEquipment().setItems(owner.getEquipment().getItems());
                MiniMe.setWeapon(owner.getWeapon());
                MiniMe.getUpdateFlag().flag(Flag.APPEARANCE);

                MiniMe.setCharacterAnimations(originalPlayer); //Fix shit animation

                if (owner.getCombatBuilder().isAttacking() || owner.getCombatBuilder().isBeingAttacked()) {
                    if (owner.getCombatBuilder().isAttacking()) {
                        MiniMe.getMovementQueue().setFollowCharacter(null);
                        MiniMe.setPositionToFace(owner.getCombatBuilder().getVictim().getPosition());
                        MiniMe.getCombatBuilder().attack(owner.getCombatBuilder().getVictim());
                    }
                } else {
                    if (MiniMe.getCombatBuilder().isAttacking())
                        MiniMe.getCombatBuilder().reset(true);

                    /*if (MiniMe.getMovementQueue().getFollowCharacter() != owner)
                        MiniMe.getMovementQueue().setFollowCharacter(owner);*/
                }

                if (!MiniMe.getLocalPlayers().contains(owner)) {
                    MiniMe.moveTo(owner.getPosition());
                    MiniMe.getMovementQueue().setFollowCharacter(owner);
                }

                if (MiniMe.checkPendingBotRemoval()) {
                    World.deregister(MiniMe);
                    owner.setMinime(null);
                    MiniMe.getMinimeOwner().sendMessage("@blu@Your mini-me vanishes.");
                    stop();
                    return;
                }

                if (!World.getPlayers().contains(owner) && World.getPlayers().contains(MiniMe)) {
                    World.deregister(MiniMe);
                    //System.out.println("stopped");
                    stop();
                }
            }
        });
    }

    public static void startFollowing(Player owner) {
        if (owner.getMinime() == null) {
            owner.sendMessage("You need to summon your mini me first.");
            return;
        }

        if (owner.getMinime().getMovementQueue().getFollowCharacter() == owner) {
            owner.sendMessage("Your minime is already following you.");
            return;
        }


        owner.getMinime().moveTo(owner.getPosition());
        owner.sendMessage("Your minime begins to follow you.");
        owner.getMinime().getMovementQueue().setFollowCharacter(owner);

    }

    public static void stopFollowing(Player owner) {

        if (owner.getMinime() == null) {
            owner.sendMessage("You need to summon your mini me first.");
            return;
        }

        owner.getMinime().getMovementQueue().setFollowCharacter(null);
        owner.getMinime().setEntityInteraction(null);
        owner.getMinime().getMovementQueue().reset();
        owner.sendMessage("Your minime stops following you.");

    }

    /**public static void handleItemOnPlayer(int itemID, int amount, Player owner) {

        Item item = new Item(itemID, amount);
        ItemDefinition itemDef = item.getDefinition();
        boolean isWeapon = itemDef.getEquipmentType().equals(ItemDefinition.EquipmentType.WEAPON) && itemDef.isWeapon() && !itemDef.isNoted();
        boolean otherItem = itemDef.isFullBody() || itemDef.isFullHelm() || itemDef.getEquipmentType() != ItemDefinition.EquipmentType.WEAPON;
        int equipmentSlot = itemDef.getEquipmentSlot();
        int freeMiniMeSlots = owner.getMinime().getInventory().getFreeSlots();

        for (Skill skill : Skill.values()) {
            if (skill == Skill.CONSTRUCTION)
                continue;

            if (itemDef.getRequirement()[skill.ordinal()] > owner.getMinime().getSkillManager().getMaxLevel(skill)) {
                owner.sendMessage("Your minime doesn't have the stats to equip this item.");
                return;
            }
        }

        if (owner.getInventory().getAmount(itemID) < amount) {
            return;
        }

        if (freeMiniMeSlots < 1) {
            owner.sendMessage("Your minime has no free slots left in their inventory.");
        } else if (isWeapon || otherItem) {
            if (itemDef.isTwoHanded() && freeMiniMeSlots > 2 && itemDef.isWeapon() && !itemDef.isNoted()) {
                Item weapon = owner.getMinime().getEquipment().forSlot(Equipment.WEAPON_SLOT);
                Item shield = owner.getMinime().getEquipment().forSlot(Equipment.SHIELD_SLOT);
                if (weapon != null) {
                    owner.getMinime().getEquipment().set(Equipment.WEAPON_SLOT, null);
                    owner.getMinime().getInventory().add(weapon);
                    owner.sendMessage("@blu@"+weapon.getAmount() + " x " + weapon.getDefinition().getName() + " was removed and added to your minime's inventory.");
                }
                if (shield != null) {
                    owner.getMinime().getEquipment().set(Equipment.SHIELD_SLOT, null);
                    owner.getMinime().getInventory().add(shield);
                    owner.sendMessage("@blu@"+shield.getAmount() + " x " + shield.getDefinition().getName() + " was removed and added to your minime's inventory.");

                }
                owner.getInventory().delete(itemID, amount);
                owner.getMinime().getEquipment().setItem(equipmentSlot, item);
                owner.getMinime().getUpdateFlag().flag(Flag.APPEARANCE);
                owner.sendMessage("@blu@Your minime equips the " + itemDef.getName());
                if (isWeapon)
                    WeaponAnimations.assign(owner.getMinime(), item);
            } else {
                owner.getInventory().delete(itemID, amount);
                owner.getMinime().getEquipment().setItem(equipmentSlot, item);
                owner.getMinime().getUpdateFlag().flag(Flag.APPEARANCE);
                owner.sendMessage("@blu@Your minime equips the " + itemDef.getName());
                if (isWeapon)
                    WeaponAnimations.assign(owner.getMinime(), item);
            }
        } else if (itemDef.isStackable() || itemDef.isNoted()) {
            owner.getInventory().delete(itemID, amount);
            owner.getMinime().getInventory().add(itemID, amount);
            owner.sendMessage("@red@Added @blu@" + amount + "@red@ x @blu@" + itemDef.getName() + " @red@ to your minime's inventory.@blu@ Total: " + owner.getMinime().getInventory().getAmount(itemID) + " Free Slots :"  + owner.getMinime().getInventory().getFreeSlots());
        } else if ((!itemDef.isStackable() || !itemDef.isNoted()) && freeMiniMeSlots >= amount) {
            owner.getInventory().delete(itemID, amount);
            owner.getMinime().getInventory().add(itemID, amount);
            owner.sendMessage("@red@Added @blu@" + amount + "@red@ x @blu@" + itemDef.getName() + " @red@ to your minime's inventory.@blu@ Total: " + owner.getMinime().getInventory().getAmount(itemID) + " Free Slots: " + owner.getMinime().getInventory().getFreeSlots());
        }
    }**/
}
