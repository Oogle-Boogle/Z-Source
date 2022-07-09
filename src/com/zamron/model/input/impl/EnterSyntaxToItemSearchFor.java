package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.content.ItemComparing;
import com.zamron.world.entity.impl.player.Player;

public class EnterSyntaxToItemSearchFor extends Input {
	
	@Override
	public void handleSyntax(Player player, String syntax) {
			
			ItemComparing.getSingleton().search(player, syntax);
	}

}
