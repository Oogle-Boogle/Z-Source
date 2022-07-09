package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class ChangeInstanceAmount extends Input {

	
	public void handleSyntax(Player player, String text) {
		
		boolean isNumber = text.matches("[0-9]+");
		
		if(!isNumber) {
			player.sendMessage("Enter a valid number.");
			return;
		}
		
		player.getInstanceSystem().setSpawnAmount(Integer.parseInt(text));

		if (Integer.parseInt(text) > 8)
			player.sendMessage("@red@The maximum number of NPC's is 8");
		
		player.sendMessage("@blu@Spawn amount has been set to: @red@" + player.getInstanceSystem().getSpawnAmount());
	}
	
}
