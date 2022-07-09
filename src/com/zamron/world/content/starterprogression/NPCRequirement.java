package com.zamron.world.content.starterprogression;

public class NPCRequirement {
	private final int id;
	private final int amount;

	public NPCRequirement(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}
}
