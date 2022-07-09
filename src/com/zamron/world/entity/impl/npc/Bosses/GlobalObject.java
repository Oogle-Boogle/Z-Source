package com.zamron.world.entity.impl.npc.Bosses;

import com.zamron.model.Position;
import com.zamron.world.entity.Entity;

public class GlobalObject extends Entity {
	
	private int id;
	
	private int x;
	
	private int y;
	
	private int height;
	
	private int face;
	
	private int ticksRemaining;
	
	private int restoreId;
	
	private int type;
	
	public GlobalObject(int id, Position position) {
		super(position);
		this.id = id;
	}
	
	public GlobalObject(Position position, int height, int face) {
		super(position);
		this.height = height;
		this.face = face;
	}
	
	public GlobalObject(int id, Position position, int face, int type) {
		super(position);
		this.id = id;
		this.face = face;
		this.type = type;
	}
	
	public GlobalObject(int id, Position position, int face, int type, int ticksRemaining) {
		super(position);
		this.id = id;
		this.face = face;
		this.type = type;
		this.ticksRemaining = ticksRemaining;
	}
	
	public GlobalObject(int id, Position position, int face, int type, int ticksRemaining, int restoreId) {
		super(position);
		this.id = id;
		this.face = face;
		this.type = type;
		this.ticksRemaining = ticksRemaining;
		this.restoreId = restoreId;
	}
	
	public void removeTick() {
		this.ticksRemaining--;
	}
	
	public int getObjectId() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getFace() {
		return face;
	}
	
	public int getTicksRemaining() {
		return ticksRemaining;
	}
	
	public int getRestoreId() {
		return restoreId;
	}
	
	public int getType() {
		return type;
	}
	
	public void updateType(int type) {
		this.id = type;
	}

}
