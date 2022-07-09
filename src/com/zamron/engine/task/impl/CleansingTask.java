package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.util.QuickUtils;
import com.zamron.world.entity.impl.player.Player;

public class CleansingTask extends Task {

	final Player player;

	public CleansingTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (player.getCleansingTime() <= 100) {
			player.sendMessage("Your cleansing scroll effect has ended.");
			player.setDoubleDropsActive(false);
			player.setCleansingTime(0);
			stop();
			return;
		}
		player.decrementCleansingTime(100);
		if (player.getCleansingTime() % 1500 == 0) {
			player.sendMessage("@blu@Cleansing Time left:@red@ " + (int) QuickUtils.tickToMin(player.getCleansingTime())
					+ QuickUtils.getCleansingPrefix(player));
		}
	}

}
