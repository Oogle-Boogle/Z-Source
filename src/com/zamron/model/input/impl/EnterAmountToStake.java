package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.minigames.impl.Dueling;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountToStake extends EnterAmount {

	public EnterAmountToStake(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if((Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) && getItem() > 0 && getSlot() >= 0 && getSlot() < 28)
			player.getDueling().stakeItem(getItem(), amount, getSlot());
		else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
