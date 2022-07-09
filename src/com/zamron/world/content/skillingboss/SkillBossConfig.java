package com.zamron.world.content.skillingboss;

import com.zamron.model.Position;

public class SkillBossConfig {

    // NPC ID Of the boss
    public static final int npcID = 6306;
    // The amount of time we add to the timer after it runs out
    public static final long timeDelay = 300000; // 5 minutes
    // How often will the server update players on the current XP count?
    public static long xpUpdateTimer = System.currentTimeMillis() + timeDelay; // 5 minutes
    // The spawn location of the boss
    public static final Position spawnPos = new Position(2614, 3130, 0); //Home
    // The value that holds the global XP
    public static long serverXPCounter;
    // How much XP requred until the boss will spawn?
    public static long requiredServerXP = 500000000; //1000000000


    public static void resetTimer() {
        xpUpdateTimer = System.currentTimeMillis() + timeDelay;
    }


}
