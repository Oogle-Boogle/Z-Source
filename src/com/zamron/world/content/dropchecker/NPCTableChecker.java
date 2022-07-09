package com.zamron.world.content.dropchecker;

import java.util.HashMap;

public class NPCTableChecker {


    HashMap<Integer, String> NPCID = new HashMap<>();

    /**
     * Creates a new NPCDropTableChecker instance.
     */
    private NPCTableChecker() {
        initialize();
    }

    /**
     * initializes the program.
     */
    private void initialize() {

        NPCID.put(420, "Joker");

    }



}
