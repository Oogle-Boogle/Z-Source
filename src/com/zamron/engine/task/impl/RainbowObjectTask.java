package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.model.Graphic;
import com.zamron.world.entity.impl.player.Player;

public class RainbowObjectTask extends Task {

	private final Player player;
	private final int[] graphics = { 2969, 2970, 2971, 2972, 2973, 2974, 2975 };
	private int tick = 4, index = 0;

	public RainbowObjectTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (!hasRequiredItem()) {
			stop();
		} else if (tick % 4 == 0) {
			int graphic = graphics[index++ % graphics.length];
			player.performGraphic(new Graphic(graphic));

		}
		tick++;
	}

	public boolean hasRequiredItem() {
		return (player.getEquipment().contains(3991));
	}

}