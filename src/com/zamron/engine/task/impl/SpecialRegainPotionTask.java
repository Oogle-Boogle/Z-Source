package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.entity.impl.player.Player;

public class SpecialRegainPotionTask extends Task {

	private Player player;
	int tick = 1000;

	public SpecialRegainPotionTask(Player player) {
		super(50, player, true);
		this.player = player;
	}

	@Override
	protected void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}
		tick -=50;
		if (tick % 50 == 0) {
			player.incrementSpecialPercentage(50);
			player.sendMessage("@red@Your special percentage has been incremented by 50%");
			player.sendMessage("@blu@Time left in ticks: @red@" + tick);
		}
		if(tick % 250 == 0) {
			player.setSpecialPercentage(100);
			player.sendMessage("@blu@Time left in ticks: @red@" + tick);
		}
		if(tick == 50) {
			player.sendMessage("@blu@Time left in ticks: @red@" + tick);
			player.setSpecialPercentage(500);
		}
		
		if(tick == 0) {
			player.sendMessage("@red@The potion effect has run out");
			stop();
		}
		CombatSpecial.updateBar(player);
	}
}