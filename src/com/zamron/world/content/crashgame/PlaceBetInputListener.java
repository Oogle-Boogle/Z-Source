package com.zamron.world.content.crashgame;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class PlaceBetInputListener extends Input {

	@Override
	public void handleSyntax(Player player, String text) {
		try {
			int bet = Integer.parseInt(text);
			if (bet <= player.getDepositedAmount()) {
				if (bet >= 0) {
					player.setBetAmount(bet);
					player.getPacketSender().sendString(62010, bet + "1b coins");
				} else {
					player.sendMessage("You can't bet negative or 0 money!");
				}
			} else
				player.sendMessage("You can't bet more than you have deposited!");
		} catch (NumberFormatException e) {
			player.sendMessage("Please ensure what you enter is a number.");
		}
	}

}
