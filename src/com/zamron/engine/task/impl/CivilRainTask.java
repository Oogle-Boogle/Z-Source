package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.model.Graphic;
import com.zamron.world.entity.impl.player.Player;

public class CivilRainTask extends Task {

	final Player player;
	
	
	public CivilRainTask(Player player) {
		super(2, player, true);
		this.player = player;
	}
	
	
	@Override
	protected void execute() {
		
		if(player.getEquipment().contains(3321)) {
			player.performGraphic(new Graphic(1265));
		}
		
	}

}
