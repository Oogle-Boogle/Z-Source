package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.content.dropchecker.NPCDropTableChecker;
import com.zamron.world.entity.impl.player.Player;

public class EnterSyntaxToNpcSearchFor extends Input {
	@Override
	public void handleSyntax(Player player, String syntax) {
			
			NPCDropTableChecker.getSingleton().searchForNPC(player, syntax);
	}
}
