package com.zamron.net.packet.impl;

import com.zamron.model.PlayerRights;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class SetDropSimulationAmount extends Input {

	@Override
	public void handleSyntax(Player player, String text) {

		boolean isNumber = text.matches("[0-9]+");

		if (!isNumber) {
			player.sendMessage("What you entered isn't a number.");
			return;
		}

		int amount = Integer.parseInt(text);

		if (amount > 25000 && player.getRights() != PlayerRights.DEVELOPER) {
			player.sendMessage("@red@Cannot simulate over 25000 at once");
			return;
		}

		player.getDropSimulator().amount = amount;
		String npcName = NpcDefinition.forId(player.getDropSimulator().npcId).getName();
		player.sendMessage(
				"@blu@Drop simulation amount has been set to @red@" + amount + "@blu@ for npc: @red@" + npcName);
	}

}
