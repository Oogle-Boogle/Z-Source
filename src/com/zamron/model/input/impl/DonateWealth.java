package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.WellOfWealth;
import com.zamron.world.entity.impl.player.Player;

public class DonateWealth extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		WellOfWealth.donate(player, amount);
	}

}
