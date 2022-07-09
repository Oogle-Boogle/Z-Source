package com.zamron.world.content.trickortreat;

import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.world.entity.impl.npc.NPC;
import lombok.AllArgsConstructor;

public class TrickOrTreatData {

    /**
     * Storing the data that can be selected
     **/
    @AllArgsConstructor
    public enum LocationData {

        /**
         * teleportPos = Where the player is teleported to
         * doorPos = Where the door object is spawned
         * doorID = The Object ID of the door that we're going to spawn
         * doorDirection = The 'face' of the door
         * walkToDoor = Where the NPC walks when giving a treat
         * npc = The NPC to spawn on the wave
         * clue = The global message sent to all players
         */

        BURTHORPE(new Position(2926, 3559), new Position(2921, 3571), 3, "South",  new Position(2921, 3572), new NPC(4905, new Position(2918, 3576)), "He'll bash your metal!"),
        CAMELOT(new Position(2735, 3483), new Position(2713, 3483),3, "North",  new Position(2713, 3482), new NPC(4239, new Position(2715, 3478)), "The Gordon Ramsey of Camelot!"),
        CANIFIS(new Position(3493,3483), new Position(3497, 3502),3, "South",  new Position(3497, 3503), new NPC(5923, new Position(3502, 3504)), "The best garments in Canifis!"),
        FALADOR(new Position(2965,3380), new Position(2958, 3384),11712, "South",  new Position(2958, 3385), new NPC(551, new Position(2958, 3388)), "Sells the worst goods in Falador!"),

        FRANKENSTEIN(new Position(3549, 3530), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla"),
        MORTTON(new Position(3488, 3283), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla"),
        RELLEKKA(new Position(2660, 3660), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla"),
        VARROCK(new Position(3213, 3425), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla"),
        YANILLE(new Position(2594, 3100), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla"),
        ZANARIS(new Position(2660, 3660), new Position(44, 3156),5244, "North",  new Position(44, 3156), new NPC(5923, new Position(2342, 2342)), "Near the Bla bla");

        public Position teleportPos;
        public Position doorPos;
        public int doorID;
        public String doorDirection;
        public Position walkToDoor;
        public NPC npc;
        public String clue;

    }

    public static long timeUntilSpawn = 0;

    public static final int spawnDelay = 600000;

    public static void resetTimer() {
        timeUntilSpawn = System.currentTimeMillis() + spawnDelay;
    }

    public static final int portalID = 38150;

    public static Item[] shitTreats = {new Item (123,1), new Item(123, 2)};;

    public static Item[] mediumTreats = {new Item (123,1), new Item(123, 2)};

    public static Item[] rareTreats = {new Item (123,1), new Item(123, 2)};

    public static Item[] superRareTreats = {new Item (123,1), new Item(123, 2)};

    public static Item[] tricks = {new Item(526, 1), new Item(592, 1)};

    public static int[] transformableNpcIDs = {4928, 6099, 5917, 3425, 1697, 2862};

    public static final int sweets = 4563;

}
