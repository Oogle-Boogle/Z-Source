package com.zamron.world.content.combat.bossminigame;

import com.zamron.GameSettings;
import com.zamron.model.Item;
import com.zamron.model.MagicSpellbook;
import com.zamron.model.Prayerbook;
import com.zamron.world.World;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.content.combat.prayer.PrayerHandler;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 /@Author Flub
 */
public class BossMiniGame {

    public static boolean changePrayerBook = false;
    public static boolean changeSpellBook = false;


    public static void StartBossMinigame(Player player) {

        /** DECLARATIONS **/

        NPC chosenBoss = BossMinigameFunctions.chosenBoss(player);
        //System.out.println("Chosen boss from StartBossMinigame is "+chosenBoss.getId());
        StatSetups stats = StatSetups.DEFAULT;
        EquipmentSetups gear = EquipmentSetups.DEFAULT;
        Item[] inventory = InventorySetups.DEFAULT;

        /** Adding ability to change Prayer and Magic books **/

        MagicSpellbook newSpellBook = MagicSpellbook.NORMAL;
        Prayerbook newPrayerBook = Prayerbook.NORMAL;


        /** SWITCHES TO MATCH STATS / INVENTORY WITH BOSS
         *
         * You Need to put a case for each ID that you list in RandomNPCData.java**/


        switch (chosenBoss.getId()) { // First Wave
            case 53:// Frost Dragon
                gear = EquipmentSetups.FROST_DRAGON;
                stats = StatSetups.FROST_DRAGON;
                inventory = InventorySetups.FROST_DRAGON;
                break;
            case 54:// Black Dragon <-- Wtf lol that should be the ID of the boss xD black dragon is a boss
                gear = EquipmentSetups.BLACK_DRAGON;
                stats = StatSetups.BLACK_DRAGON;
                inventory = InventorySetups.BLACK_DRAGON;
                break;
            case 50:// King Black Dragon (KBD) nope
                gear = EquipmentSetups.KBD_WAVE_ONE;
                stats = StatSetups.KBD_WAVE_ONE;
                inventory = InventorySetups.KBD_WAVE_ONE;
                break;
            case 2883:// Tormented Demon
                gear = EquipmentSetups.TORMENTED_DEMON;
                stats = StatSetups.TORMENTED_DEMON;
                inventory = InventorySetups.TORMENTED_DEMON;
                changeSpellBook = true;
                newSpellBook = MagicSpellbook.ANCIENT;
                break;
            case 3200:// Chaos Elemental
                gear = EquipmentSetups.CHAOS_ELEMENTAL;
                stats = StatSetups.CHAOS_ELEMENTAL;
                inventory = InventorySetups.CHAOS_ELEMENTAL;
                break;
            case 2882:// Dagganoth Prime
                gear = EquipmentSetups.DAGANNOTH_PRIME;
                stats = StatSetups.DAGANNOTH_PRIME;
                inventory = InventorySetups.DAGANNOTH_PRIME;
                break;
            case 2881:// Dagannoth Supreme
                gear = EquipmentSetups.DAGANNOTH_SUPREME;
                stats = StatSetups.DAGANNOTH_SUPREME;
                inventory = InventorySetups.DAGANNOTH_SUPREME;
                break;
            case 499:// Barrelchest
                gear = EquipmentSetups.BARRELCHEST;
                stats = StatSetups.BARREL_CHEST;
                inventory = InventorySetups.BARREL_CHEST;
                changeSpellBook = true;
                newSpellBook = MagicSpellbook.NORMAL;
                break;
            case 2001:// Scorpia
                gear = EquipmentSetups.SCORPIA;
                stats = StatSetups.SCORPIA;
                inventory = InventorySetups.SCORPIA;
                break;
            case 2000: //Ven
                gear = EquipmentSetups.VENENATIS;
                stats = StatSetups.VENENATIS;
                inventory = InventorySetups.VENENATIS;
                break;
            case 7553: //General
                gear = EquipmentSetups.BARRELCHEST;
                stats = StatSetups.BARREL_CHEST;
                inventory = InventorySetups.BARREL_CHEST;
                break;
            case 7134:
                gear = EquipmentSetups.BORK;
                stats = StatSetups.BORK;
                inventory = InventorySetups.BORK;
                break;
            case 7286:
                gear = EquipmentSetups.SKOTIZO;
                stats = StatSetups.SKOTIZO;
                inventory = InventorySetups.BORK;
                break;
            case 5666:
                gear = EquipmentSetups.BARRELCHEST;
                stats = StatSetups.BARREL_CHEST;
                inventory = InventorySetups.BARREL_CHEST;
                break;
        }

        /** Storing values to assign **/

        int[] gearID = {gear.getWeapon(), gear.getShield(), gear.getHelm(), gear.getBody(), gear.getLegs(), gear.getNeck(), gear.getCape(), gear.getHands(), gear.getFeet(), gear.getRing()};
        int[] statID = {stats.getAttack(), stats.getDefence(), stats.getStrength(), stats.getRanged(), stats.getMagic(), stats.getConstitution(), stats.getPrayer()};

        int wave = player.getCurrentBossWave();
        switch (wave) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                //System.out.println("We're at the wave selection phase and booleans are spell book change :"+changeSpellBook + " prayer change: "+changePrayerBook);
                player.getInventory().deleteAll();
                BossMinigameFunctions.setNewStats(player, statID[0], statID[1], statID[2], statID[3], statID[4], statID[5], statID[6]);
                BossMinigameFunctions.setEquipment(player, gearID[0], gearID[1], gearID[2], gearID[3], gearID[4], gearID[5], gearID[6], gearID[7], gearID[8], gearID[9]);
                BossMinigameFunctions.setInventory(player, inventory);
                //ItemEffect.refreshEffects(player);
                World.register(chosenBoss);
                player.getRegionInstance().getNpcsList().addIfAbsent(chosenBoss);
                player.forceChat("For wave "+ (wave+1) +" I'll be fighting " + chosenBoss.getDefinition().getName()+"!");

                PrayerHandler.deactivateAll(player);
                CurseHandler.deactivateAll(player);

                if (changeSpellBook) {
                    player.setSpellbook(newSpellBook);
                    player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
                }

                if (changePrayerBook) {
                    player.setPrayerbook(newPrayerBook);
                    player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId()).sendMessage("Your prayers have been changed...");
                }
                break;

            case 5: //When all waves are finished, we set the wave to 0
                player.setCurrentBossWave(0);
                break;

        }

        if (player.getSummoning().getFamiliar() != null) // Get Rid Of Familiars
            player.getSummoning().unsummon(true, true);

    }
}