package com.zamron.world.entity.impl.npc.Bosses.zulrah;

import com.zamron.event.CycleEvent;
import com.zamron.world.entity.impl.player.Player;


public abstract class ZulrahStage extends CycleEvent {
	
	protected Zulrah zulrah;
	
	protected Player player;
	
	public ZulrahStage(Zulrah zulrah, Player player) {
		this.zulrah = zulrah;
		this.player = player;
	}

}
