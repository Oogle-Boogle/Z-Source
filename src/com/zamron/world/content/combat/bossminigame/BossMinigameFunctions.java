package com.zamron.world.content.combat.bossminigame;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.PoisonImmunityTask;
import com.zamron.model.*;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.container.impl.Inventory;
import com.zamron.net.packet.impl.EquipPacketListener;
import com.zamron.world.World;
import com.zamron.world.content.BonusManager;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.content.skill.SkillManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import static com.zamron.model.Locations.Location.BOSS_TIER_LOCATION;
import static com.zamron.model.RegionInstance.RegionInstanceType.BOSS_TIER_ARENA;

/**
 /@Author Flub
 */
public class BossMinigameFunctions {

    public static final int STAIRS_ENTRY_ID = 22945;
    public static final int EXIT_CAVE_ID = 5858;
    public static final Position ENTRY_DOOR = new Position(2591, 3092);
    public static final Position ARENA_ENTRANCE = new Position(2807, 10105);
    public static final Position ARENA_CENTRE = new Position(2776, 10089);
    public static int DOOR_X = ENTRY_DOOR.getX();
    public static int DOOR_Y = ENTRY_DOOR.getY();
    public static int INSIDE_CAVE_X = 2807;
    public static int INSIDE_CAVE_Y = 10105;

    public static MagicSpellbook currentSpellBook = MagicSpellbook.NORMAL;
    public static Prayerbook currentPrayerBook = Prayerbook.NORMAL;


    public static boolean checkItems(Player player) {
        if (player.getInventory().getFreeSlots() != 28) {
            return false;
        }
        for (int i = 0; i < 14; i++) {
            if (player.getEquipment().get(i).getId() > 0)
                return false;
        }
        return true;
    }

    public static void handleDoor(Player player) {

        if (player.getSummoning().getFamiliar() != null){
            player.getPacketSender().sendMessage("DISMISS YOUR FAMILIAR BEFORE ENTERING");
            return;
        }

        currentSpellBook = player.getSpellbook();
        currentPrayerBook = player.getPrayerbook();
        saveOldStats(player);
        player.getPacketSender().sendInterfaceRemoval();

        if (!player.getClickDelay().elapsed(7000)) {
            player.forceChat("I SHOULD STOP SPAM CLICKING THE ENTRANCE!");
            return;
        }

        if (player.isShouldGiveBossReward()) {
            player.forceChat("I need to click the chest before moving onto the next wave.");
            return;
        }

        if (!checkItems(player)) {//This will not allow the player to enter the doors if they have items
            player.forceChat("I can't enter if I have items!");
            return;
        }


        player.forceChat("This feels dangerous...");
        player.getPacketSender().sendMessage("The ground begins to shake..");

        TaskManager.submit(new Task(4) {
            @Override
            protected void execute() {
                player.getPacketSender().sendInterfaceRemoval();
                player.performAnimation(new Animation(1746));
                if (player.getLocation() != BOSS_TIER_LOCATION) {
                    player.moveTo(new Position(INSIDE_CAVE_X, INSIDE_CAVE_Y, player.getIndex() * 4));
                    player.setRegionInstance(new RegionInstance(player, BOSS_TIER_ARENA));
                    BossMiniGame.StartBossMinigame(player);
                    if (player.getSummoning().getFamiliar() != null) {
                        player.getSummoning().unsummon(true, true);
                    }
                }
                removePotionEffects(player);
                stop();
            }
        });

        if (!player.isUsedBossTeleport()) {
            TaskManager.submit(new Task(9) {
                @Override
                protected void execute() {
                    player.forceChat("I think I should walk a little further..");
                    //player.getPacketSender().sendMessage("@red@Visit the wiki for the guide!");
                    player.setUsedBossTeleport(true);
                    stop();
                }
            });
        }
        player.getClickDelay().reset();

    }


    public static void removePotionEffects(Player player) {

        if (player.getFireImmunity()>1 || player.getFireDamageModifier() > 1 || player.getDragonFireImmunity() > 1) {
            player.getPacketSender().sendMessage("You were immune to fire.. Not anymore!");
            player.setDragonFireImmunity(-1);
            player.setFireDamageModifier(0);
            player.setFireImmunity(-1);
        }

        if (player.getOverloadPotionTimer() >= 1) {
            player.getPacketSender().sendMessage("You had an overload potion active.. Not anymore!");
            player.setOverloadPotionTimer(-1);
        }

        if (player.getPoisonImmunity() > 1) {
            player.getPacketSender().sendMessage("You were immune to poison.. Not anymore!");
            player.setPoisonImmunity(-1);
        }

        if (player.getPrayerRenewalPotionTimer() > 0) {
            player.getPacketSender().sendMessage("You had a prayer renewal potion active.. Not anymore!");
            player.setPrayerRenewalPotionTimer(-1);
        }


    }

    public static void despawnNpcs(Player player) {
        if (player.getLocation() == BOSS_TIER_LOCATION && !player.getRegionInstance().getNpcsList().isEmpty() && player.getRegionInstance() != null) {
            ////System.out.println("DESPAWNING NPC'S FOR " + player.getUsername());
            player.getRegionInstance().getNpcsList().forEach(npc -> npc.removeInstancedNpcs(BOSS_TIER_LOCATION, player.getPosition().getZ()));
            player.getRegionInstance().getNpcsList().forEach(npc -> World.deregister(npc));
        }
    }


    public static void handleExit(Player player) {
        if (player.getRegionInstance() != null) {
            destructBossTier(player);
        }
        restoreOldStats(player);
        player.setRegionInstance(null);
        player.getInventory().deleteAll();
        player.getEquipment().deleteAll();
        player.getEquipment().refreshItems();
        player.moveTo(ENTRY_DOOR);
        player.getUpdateFlag().flag(Flag.ANIMATION);
        Autocasting.resetAutocast(player, true);
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        player.setFreezeDelay(-1);
        player.setResetMovementQueue(true);
        BossMiniGame.changePrayerBook = false;
        BossMiniGame.changeSpellBook = false;
        player.setSpellbook(currentSpellBook);
        player.setPrayerbook(currentPrayerBook);
        player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
        player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
        PoisonImmunityTask.makeImmune(player, 0);
    }

    public static boolean shouldDespawnNPCs(Player player) {
        return player.getRegionInstance() != null && !player.getRegionInstance().getNpcsList().isEmpty() && player.getLocation() == BOSS_TIER_LOCATION;
    }

    public static boolean shouldDestroy(Player player) {
        return (player.getLocation() == BOSS_TIER_LOCATION) && (player.getRegionInstance().getType() == BOSS_TIER_ARENA);
    }

    public static void destructBossTier(final Player player) {

        if (shouldDestroy(player)) {
           // //System.out.println("DESTROYING INSTANCE FOR " + player.getUsername());

            int z = 0;
            Position nonInstance = new Position(DOOR_X, DOOR_Y, z);
            player.moveTo(nonInstance);//Moves player to height 0
            ////System.out.println("Sending " + player.getUsername() + " to height 0");
        } else {
            int z = 0;
            Position nonInstance = new Position(DOOR_X, DOOR_Y, z);
            player.moveTo(nonInstance);//Moves player to height 0
        }


        if (shouldDespawnNPCs(player)) {
            ////System.out.println("DESPAWNING NPC'S FOR " + player.getUsername());
            player.getRegionInstance().getNpcsList().forEach(npc -> npc.removeInstancedNpcs(BOSS_TIER_LOCATION, player.getPosition().getZ()));
            player.getRegionInstance().getNpcsList().forEach(npc -> World.deregister(npc));
        }
        player.setRegionInstance(null);
    }

    public static void updateSkills(Player player) {
        for (Skill skill : Skill.values())
            player.getSkillManager().updateSkill(skill);
    }

    public static void saveOldStats(Player player) {

        SkillManager.Skills currentSkills = player.getSkillManager().getSkills();
        player.bossGameLevels = currentSkills.level;
        player.bossGameSkillXP = currentSkills.experience;
        player.bossGameMaxLevels = currentSkills.maxLevel;
    }

    public static void restoreOldStats(Player player) {
        if (player.bossGameLevels != null && player.bossGameSkillXP != null && player.bossGameMaxLevels != null) {
            player.getSkillManager().getSkills().level = player.bossGameLevels;
            player.getSkillManager().getSkills().experience = player.bossGameSkillXP;
            player.getSkillManager().getSkills().maxLevel = player.bossGameMaxLevels;
        }
        player.getInventory().deleteAll();
        player.getEquipment().deleteAll();
        player.getUpdateFlag().flag(Flag.ANIMATION);
        player.getEquipment().refreshItems();
        updateSkills(player);
    }

    public static NPC chosenBoss(Player player) {

        int x = BossMinigameFunctions.ARENA_CENTRE.getX();
        int y = BossMinigameFunctions.ARENA_CENTRE.getY();
        int z = player.getPosition().getZ();

        NPC chosenBoss = new NPC(0, new Position(x, y, z)).setSpawnedFor(player);


        switch (player.getCurrentBossWave()) {
            case 0:
                chosenBoss = new NPC(RandomNPCData.randomFirstWaveID(), new Position(x, y, z)).setSpawnedFor(player);
                break;
            case 1:
                chosenBoss = new NPC(RandomNPCData.randomSecondWaveID(), new Position(x, y, z)).setSpawnedFor(player);
                break;
            case 2:
                chosenBoss = new NPC(RandomNPCData.randomThirdWaveID(), new Position(x, y, z)).setSpawnedFor(player);
                break;
            case 3:
                chosenBoss = new NPC(RandomNPCData.randomFourthWaveID(), new Position(x, y, z)).setSpawnedFor(player);
                break;
            case 4:
                chosenBoss = new NPC(RandomNPCData.randomFifthWaveID(), new Position(x, y, z)).setSpawnedFor(player);
                break;
        }
        return chosenBoss;
    }

    public static void setNewStats(Player player, int attack, int defence, int strength, int ranged, int magic, int constitution, int prayer) {
        player.getSkillManager().newSkillManager();
        updateSkills(player);
        player.getSkillManager().setMaxLevel(Skill.ATTACK, attack);
        player.getSkillManager().setMaxLevel(Skill.DEFENCE, defence);
        player.getSkillManager().setMaxLevel(Skill.STRENGTH, strength);
        player.getSkillManager().setMaxLevel(Skill.RANGED, ranged);
        player.getSkillManager().setMaxLevel(Skill.MAGIC, magic);
        player.getSkillManager().setMaxLevel(Skill.CONSTITUTION, constitution);
        player.getSkillManager().setMaxLevel(Skill.PRAYER, prayer);
        for (Skill skill : Skill.values()) {
            player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill));
            player.getSkillManager().setExperience(skill, SkillManager.getExperienceForLevel(player.getSkillManager().getMaxLevel(skill)));
        }
    }

    public static void setEquipment(Player player, int weapon, int shield, int helm, int body, int legs, int neck, int cape, int hands, int feet, int ring) {
        player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(weapon, 1));
        player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(shield, 1));
        player.getEquipment().set(Equipment.HEAD_SLOT, new Item(helm, 1));
        player.getEquipment().set(Equipment.BODY_SLOT, new Item(body, 1));
        player.getEquipment().set(Equipment.LEG_SLOT, new Item(legs, 1));
        player.getEquipment().set(Equipment.AMULET_SLOT, new Item(neck, 1));
        player.getEquipment().set(Equipment.CAPE_SLOT, new Item(cape, 1));
        player.getEquipment().set(Equipment.HANDS_SLOT, new Item(hands, 1));
        player.getEquipment().set(Equipment.FEET_SLOT, new Item(feet, 1));
        player.getEquipment().set(Equipment.RING_SLOT, new Item(ring, 1));
        player.getEquipment().set(Equipment.AMMUNITION_SLOT, new Item(-1, 1));
        player.getEquipment().refreshItems();
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        EquipPacketListener.resetWeapon(player);
        BonusManager.update(player);
    }

    public static void setInventory(Player player, Item[] items) {
        Inventory playersInventory = player.getInventory();
        playersInventory.addItems(items, true);
    }

    public static void resetProgress(Player player) {
        player.getPacketSender().sendMessage("Progress has been reset!");
        player.setCurrentBossWave(0);
        player.setShouldGiveBossReward(false);
    }
}
