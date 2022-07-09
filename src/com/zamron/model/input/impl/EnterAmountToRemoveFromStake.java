package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromStake extends EnterAmount {

	public EnterAmountToRemoveFromStake(int item) {
		super(item);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if((Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) && getItem() > 0) {
			player.getDueling().removeStakedItem(getItem(), amount);
		} else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
