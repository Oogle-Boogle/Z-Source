package com.zamron.world.content.kcsystem;

import com.zamron.world.entity.impl.player.Player;

public final class KillRequirement {

    private final int id;
    private final int requiredKills;

    public KillRequirement(int id, int requiredKills) {
        this.id = id;
        this.requiredKills = requiredKills;
    }

    public int getId() {
        return id;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public boolean meetsRequirement(Player player) {
        return player.getNpcKillCount(id) >= requiredKills;
    }

    public static KillRequirement killRequirement(int id, int requiredKills) {
        return new KillRequirement(id, requiredKills);
    }

}
