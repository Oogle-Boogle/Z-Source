package com.zamron.world.content.minigames.impl.gungame;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben Salomons on 1-10-2017.
 */
public enum GungameData {
    ROOM1(87, -1, 1),
    ROOM2(21, 1544, 2),
    ROOM3(19, 1545, 3),
    ROOM4(110, 1546, 4),
    ROOM5(221, 1547, 5),
    BOSS_WAVE(119, 1548, 6);

    private final int npcId;
    private final int dropId;
    private final int wave;

    GungameData(int npcId, int dropId, int wave) {
        this.npcId = npcId;
        this.dropId = dropId;
        this.wave = wave;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getDropId() {
        return dropId;
    }

    public int getWave() {
        return wave;
    }

    static final Map<Integer, GungameData> waves = new HashMap<>();

    static {
        waves.put(1, ROOM1);
        waves.put(2, ROOM2);
        waves.put(3, ROOM3);
        waves.put(4, ROOM4);
        waves.put(5, ROOM5);
        waves.put(6, BOSS_WAVE);
    }

    public static GungameData getForWave(int wave) {
        return waves.get(wave);
    }
}
