package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.content.Juggernaut;
import com.zamron.world.entity.impl.player.Player;

public class DonateTaxBagsInput extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {

		player.getPacketSender().sendInterfaceRemoval();
		

		String regex = "[0-9]+";

		if (!syntax.matches(regex)) {
			player.sendMessage("@red@Enter an amount not letters :/");
			return;
		}

		int amount = Integer.parseInt(syntax);

		if (amount > Juggernaut.amountNeeded) {
			player.sendMessage("@red@I can only take " + Juggernaut.amountNeeded + " More bags.");
			return;
		} else if (player.getInventory().contains(10835, amount)) {
			player.getInventory().delete(10835, amount);
			player.sendMessage("You have donated: " + amount + " 1b coin.");
			Juggernaut.amountNeeded -= amount;
			hasReached();
			return;
		} else {
			player.sendMessage("You don't have that many bags :/");
		}
	}

	public boolean hasReached() {
		if(Juggernaut.amountNeeded < 1) {
			Juggernaut.spawn();
			return true;
		} else {
			return false;
		}
	}

}
