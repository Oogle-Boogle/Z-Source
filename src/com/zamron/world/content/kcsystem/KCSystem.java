package com.zamron.world.content.kcsystem;

import com.zamron.model.Locations.Location;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public final class KCSystem {

    private final Player player;

    public KCSystem(Player player) {
        this.player = player;
    }

    public boolean meetsRequirements(int npcID, boolean sendMessage) {
        KillCountNPCs data = KillCountNPCs.forID(npcID);
        if (data == null) return true;
        if (Location.INSTANCE_ARENA.equals(player.getLocation())) {
            return true; //Remove KC in the instance arena
        }
//        if (player.isInRaid()) return true;
        boolean met = true;
        for (KillRequirement requirement : data.getKillRequirements()) {
            if (requirement.meetsRequirement(player)) continue;
            if (!sendMessage) return false;

            met = false;

            int id = requirement.getId();
            String name = NpcDefinition.forId(id).getName();
            int amount = requirement.getRequiredKills();
            int killedAmount = player.getNpcKillCount(id);
            player.sendMessage("Requirements needed: @red@" + amount + " @blu@of @red@" + name + "@blu@ - Killed: @red@" + killedAmount);
        }
        return met;
    }

    public boolean meetsRequirements(NPC npc, boolean sendMessage) {
        return meetsRequirements(npc.getId(), sendMessage);
    }

}
