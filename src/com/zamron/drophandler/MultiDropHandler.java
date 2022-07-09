package com.zamron.drophandler;

import com.zamron.model.Position;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public interface MultiDropHandler {

	
	public abstract void handleDrop(Player player, NPC npc, Position position);
	
	
	public abstract void clearDamageMap();
}
