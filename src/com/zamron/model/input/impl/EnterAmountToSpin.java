package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.skill.impl.crafting.Flax;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountToSpin extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		Flax.spinFlax(player, amount);
	}

}
