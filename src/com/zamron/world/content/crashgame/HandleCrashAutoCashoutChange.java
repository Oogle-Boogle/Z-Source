package com.zamron.world.content.crashgame;


import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class HandleCrashAutoCashoutChange extends Input{
	
	@Override
	public void handleSyntax(Player player, String text) {
		try
		{
			double mult = Double.parseDouble(text);
			player.setCrashAutoCashout(mult);
			player.getPacketSender().sendString(62011, mult+"x");
		}catch(NumberFormatException e)
		{
			player.sendMessage("Unable to change Auto Cashout Multiplier.");
			player.sendMessage("Please ensure you use the format X.XX to set it.");
		}
	}

}
