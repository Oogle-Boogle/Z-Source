package com.zamron.model.input.impl;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.input.EnterAmount;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class BuyShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > 80000000)
			amount = 80000000;
		player.getPacketSender().sendInterfaceRemoval();
		int cost = ItemDefinition.forId(18016).getValue() * amount;
		long moneyAmount = player.getInventory().getAmount(10835);
		long canBeBought = moneyAmount / (ItemDefinition.forId(18016).getValue());
		if(canBeBought >= amount)
			canBeBought = amount;
		if(canBeBought == 0) {
			player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
			return;
		}
		cost = ItemDefinition.forId(18016).getValue() * (int) canBeBought;
		if(player.getInventory().getAmount(10835) < cost) {
			player.getPacketSender().sendMessage("You do not have enough money in your @red@inventory@bla@ to buy that amount.");
			return;
		}
		player.getInventory().delete(10835, (int) cost);
		player.getInventory().add(18016, (int) canBeBought);
		player.getPacketSender().sendMessage("You've bought "+canBeBought+" Spirit Shards for "+Misc.insertCommasToNumber(""+(int)cost)+" coins.");
	}

}
