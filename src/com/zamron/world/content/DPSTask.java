package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class DPSTask extends Task {
	
	private Player player;
	
	public DPSTask(Player player) {
		super(1, false);
		this.player = player;
	}
	
	protected void execute() {

		player.getPacketSender().sendString(23999, "DPS: @gre@" + Misc.formatNumber(player.getDpsOverlay().getDPS()));
		
	}

}
