package com.zamron.world.content.dialogue;

import java.nio.file.Path;

import com.zamron.model.input.Input;
import com.zamron.world.content.LotterySystem;
import com.zamron.world.entity.impl.player.Player;

public class EnterLotteryTicketAmount extends Input {

	@Override
	public void handleSyntax(Player player, String text) {

		int amount = Integer.parseInt(text);
		if(amount > 100) {
			player.sendMessage("100 can be bought max at once");
			return;
		}
		if(!player.getInventory().contains(10835, amount)) {
			player.sendMessage("You don't have enough to buy " + amount + " tickets");
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		String username = player.getUsername();
		Path path = LotterySystem.LOTTERY_DATA;
		player.getInventory().delete(10835, amount);
		for (int i = 0; i < amount; i++) {
			LotterySystem.addUser(username, path);
		}
	}
}
