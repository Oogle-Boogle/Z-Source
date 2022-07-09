package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.grandexchange.GrandExchange;
import com.zamron.world.entity.impl.player.Player;

public class EnterGeQuantity extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		GrandExchange.setQuantity(player, amount);
	}

}
