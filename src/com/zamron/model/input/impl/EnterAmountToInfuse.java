package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.skill.impl.summoning.PouchMaking;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountToInfuse extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getInterfaceId() != 63471) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		PouchMaking.infusePouches(player, amount);
	}

}
