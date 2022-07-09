package com.zamron.net.packet.impl;

import com.zamron.model.input.Input;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.entity.impl.player.Player;

public class ServerPerkContributionInput extends Input {
    @Override
    public void handleAmount(Player player, int amount) {
    	GlobalPerks.getInstance().contribute(player, amount);
    }
}
