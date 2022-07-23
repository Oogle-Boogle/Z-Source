package com.zamron.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

public class FrameUpdater {

    /**
     * System to optimize sendFrame126 performance.
     *
     * @author MikeRSPS
     * UltimateScape
     * http://ultimatescape2.com
     */
    private final Int2ObjectMap<Frame126> interfaceTextMap = new Int2ObjectOpenHashMap<>();

    public static class Frame126 {
        public int id;
        public String currentState;

        public Frame126(String s, int id) {
            this.currentState = s;
            this.id = id;
        }

    }

    public boolean shouldUpdate(String text, int id) {
        if (text.equalsIgnoreCase("[CLOSEMENU]") || id == 0 || id == 27000 || id == 27001 || id == 27002 || id == 1 || id == 57025 || id == 57028 || id == 5385)
            return true;
        if (!interfaceTextMap.containsKey(id)) {
            interfaceTextMap.put(id, new Frame126(text, id));
        } else {
            Frame126 t = interfaceTextMap.get(id);
            if (text.equals(t.currentState)) {
                return false;
            }
            t.currentState = text;
        }
        return true;
    }
}
