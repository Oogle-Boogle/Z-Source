package com.zamron.world.entity.impl.npc.Bosses;

import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;


public class SingleInstancedSagittare extends SingleInstancedArea {
	
	public SingleInstancedSagittare(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Sagittare sagittare = player.getSagittareEvent();
		if (sagittare.getNpc() != null) {
			World.deregister(sagittare.getNpc());
		}
	}

}
