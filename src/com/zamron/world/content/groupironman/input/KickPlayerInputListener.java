package com.zamron.world.content.groupironman.input;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class KickPlayerInputListener extends Input {

    @Override
    public void handleSyntax(Player player, String username) {
        player.getGroupIronmanGroup().kick(username);
    }
}

