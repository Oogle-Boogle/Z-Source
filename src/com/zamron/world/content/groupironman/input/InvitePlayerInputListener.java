package com.zamron.world.content.groupironman.input;

import com.zamron.model.input.Input;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class InvitePlayerInputListener extends Input {

    @Override
    public void handleSyntax(Player player, String username) {
        Player target = World.getPlayerByName(username);
        if (target == null) {
            player.sendMessage(username + " is offline");
            return;
        }
        player.getGroupIronmanGroup().invite(target);

    }
}
