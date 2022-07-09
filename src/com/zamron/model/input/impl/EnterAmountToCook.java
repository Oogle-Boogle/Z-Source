package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.skill.impl.cooking.Cooking;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountToCook extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getSelectedSkillingItem() > 0)
			Cooking.cook(player, player.getSelectedSkillingItem(), amount);
	}

}
