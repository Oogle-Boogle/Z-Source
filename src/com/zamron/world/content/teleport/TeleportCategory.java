package com.zamron.world.content.teleport;


import java.util.HashMap;
import java.util.Map;

public enum TeleportCategory {
    MONSTERS(0),
    BOSSES(1),
    MINIGAMES(2),
    GLOBAL(3),
    ULTRA_BOSSES(4),
    SKILLING(5),
    ;

    private static final Map<Integer, TeleportCategory> map = new HashMap<>();
    static {
        for (TeleportCategory category : values()) {
            map.put(category.index, category);
        }
    }

    final int index;

    TeleportCategory(int index) {
        this.index = index;
    }

    public static TeleportCategory forIndex(int index) {
        return map.get(index);
    }
}
