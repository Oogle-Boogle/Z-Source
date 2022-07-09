package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.util.QuickUtils;
import com.zamron.world.entity.impl.player.Player;

public class PraiseTask extends Task {

	final Player player;

	public PraiseTask(Player player) {
		super(100, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {

		if (player.getPraiseTime() <= 100) {
			player.sendMessage("Your praise scroll effect has ended.");
			player.setDoubleRateActive(false);
			player.setPraiseTime(0);
			stop();
			return;
		}
		if (player.getPraiseTime() % 1500 == 0) {
			player.sendMessage("@blu@Praise Time left:@red@ " + (int) QuickUtils.tickToMin(player.getPraiseTime())
					+ QuickUtils.getPraisePrefix(player));
		}
		player.decrementPraiseTime(100);
	}

}
