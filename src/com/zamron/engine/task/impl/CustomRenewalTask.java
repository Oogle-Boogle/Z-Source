package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.world.entity.impl.player.Player;

public class CustomRenewalTask extends Task {
	
	final Player player;
	
	int timer = 3000;
	
	public CustomRenewalTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {
		
		timer -= 100;
		
		if (timer > 0) {
			player.setConstitution(990);
		}
		
	}
	
}
