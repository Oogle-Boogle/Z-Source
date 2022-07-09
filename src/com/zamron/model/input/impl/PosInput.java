package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class PosInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendClientRightClickRemoval();
		player.getPlayerOwnedShopManager().updateFilter(syntax);
		
	}
}
