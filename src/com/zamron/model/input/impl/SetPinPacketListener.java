package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class SetPinPacketListener extends Input {
	@Override
	public void handleSyntax(Player player, String pin) {

		if (pin.length() < 6 && pin.length() > 0) {
			player.setSavedPin(pin);
			player.setSavedIp(player.getHostAddress());
			player.setHasPin(true);
			player.sendMessage("@blu@Your new pin is: @red@" + pin);

		}
	}
}
