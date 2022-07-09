package com.zamron.model.input.impl;

import com.zamron.model.input.EnterAmount;
import com.zamron.world.content.skill.impl.firemaking.Firemaking;
import com.zamron.world.content.skill.impl.firemaking.Logdata;
import com.zamron.world.content.skill.impl.firemaking.Logdata.logData;
import com.zamron.world.entity.impl.player.Player;

public class EnterAmountOfLogsToAdd extends EnterAmount {

	
	@Override
	public void handleAmount(Player player, int amount) {
		Firemaking.lightFire(player, -1, true, amount);
		if(player.getInteractingObject() != null)
			player.setPositionToFace(player.getInteractingObject().getPosition());
	}
	
	public static void openInterface(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().stopSkilling();
		final logData lData = Logdata.getLogData(player, -1);
		if(lData == null) {
			player.getPacketSender().sendMessage("You do not have any logs to add to this fire.");
			return;
		}
		player.setInputHandling(new EnterAmountOfLogsToAdd());
		player.getPacketSender().sendEnterAmountPrompt("How many logs would you like to add to the fire?");
	}

}
