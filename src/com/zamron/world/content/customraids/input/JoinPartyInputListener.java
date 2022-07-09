package com.zamron.world.content.customraids.input;

import com.zamron.model.input.Input;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class JoinPartyInputListener extends Input {

    public void handleSyntax(Player player, String playerName) {
        Player target = World.getPlayerByName(playerName);
        if (target == null) {
            player.sendMessage(playerName + " is offline");
            return;
        }

        if (target.getRaidParty() == null) {
            player.sendMessage(playerName + " does not have a raid party setup");
            return;
        }

        target.getRaidParty().join(player);
    }
}
