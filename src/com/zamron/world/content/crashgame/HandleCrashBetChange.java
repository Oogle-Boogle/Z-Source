package com.zamron.world.content.crashgame;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class HandleCrashBetChange extends Input{
	
	@Override
	public void handleSyntax(Player player, String text) {
		try {
			long bet = Long.parseLong(text);
			if(bet <= player.getCrashGameBalance())
			{
				if(bet >= 0)
				{
					player.setCrashGameBet(bet);
					player.getPacketSender().sendString(62010,(bet/1000) + "k");
				}else {
					player.sendMessage("You can't bet negative or 0 money!");
				}
			}
			else
				player.sendMessage("You can't bet more than you have deposited!");
		}catch(NumberFormatException e)
		{
			player.sendMessage("Please ensure what you enter is a number.");
		}
	}

}
