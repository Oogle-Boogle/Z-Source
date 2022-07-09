package com.zamron.world.content.roulette;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class RouletteDeposit extends Input {

	@Override
	public void handleSyntax(Player player, String text) {
		try {
		//text = text.replaceAll("k", "000").replaceAll("m", "000000").replaceAll("b", "000000000");
			if(text.contains("k")) {
				text = text.replaceAll("k", "000");
				//System.out.println("replaced k with 000: - text is now: " + text);
			} else if(text.contains("m")) {
				text = text.replaceAll("m", "000000");
				//System.out.println("replaced m with 000000: - text is now: " + text);
			} else if(text.contains("b")) {
				text = text.replaceAll("b", "000000000");
				//System.out.println("replaced b with 000000000: - text is now: " + text);
			}
		int amount = Integer.parseInt(text);
		player.getRoulette().deposit(amount);
		
	} catch (NumberFormatException e) {
		player.sendMessage("Make sure what u entered is a number");
		e.printStackTrace();
	}
	}
	
	
}
