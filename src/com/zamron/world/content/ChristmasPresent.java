package com.zamron.world.content;

import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class ChristmasPresent {


	public static void openBox (Player player) {
		player.getInventory().delete(6542, 1);
		
		if (RandomUtility.getRandom(10) == 5) {
			/*
			 * Landing on 5 and recieve reward
			 */
			player.getInventory().add(1050, 1);
			player.getPacketSender().sendMessage("Congratulations you recieved the santa hat");
		} else {
			/*
			 * Not landing on 5
			 */
			player.getInventory().add(10835, 100 + RandomUtility.getRandom(500));
			player.getPacketSender().sendMessage("Sorry, you didn't get the santa hat. Try again");
		}
	}



}
