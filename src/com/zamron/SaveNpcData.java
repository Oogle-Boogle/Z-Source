package com.zamron;

import com.zamron.model.Direction;
import com.zamron.model.Position;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.google.gson.annotations.SerializedName;

public class SaveNpcData {
	
	@SerializedName("npc-id")
	private int npcId;
	private Position position;
	@SerializedName("walking-policy")
	private Coordinator coordinator;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + npcId;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaveNpcData other = (SaveNpcData) obj;
		if (npcId != other.npcId)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	public SaveNpcData(int npcId, Direction direction, Position position, Coordinator coordinator) {
		this.npcId = npcId;
		this.position = position;
		this.coordinator = coordinator;
	}
}
