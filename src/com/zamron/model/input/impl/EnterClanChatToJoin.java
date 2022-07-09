package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
