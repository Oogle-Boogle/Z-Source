package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.world.entity.impl.player.Player;

public class HpPotionTask extends Task { // kool

	final Player player;

	int tick = 1000;

	public HpPotionTask(Player player) {
		super(2, player, false);
		this.player = player;
	}

	@Override
	protected void execute() {
		tick -= 2;
		if((tick >= 500) && player.getConstitution() <= 1100) {
			player.heal(200);
		}
		
		if((tick < 500 && tick >= 100 && player.getConstitution() <= 1200)) {
			player.heal(100);
		}
		
		if((tick < 100 && tick > 2) && player.getConstitution() <= 1250) {
			player.heal(50);
		}
		
		if((tick == 2) && player.getConstitution() <= 990) {
			player.heal(990);
			player.sendMessage("@red@Custom Health Flask has run out");
			stop();
		}
		
	}

}
