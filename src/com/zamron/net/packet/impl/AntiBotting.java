package com.zamron.net.packet.impl;

import com.zamron.model.input.impl.AntiBottingInput;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

/*
 * @author Bas - ArlaniaPs
 * Class to stop cheating actions in game
 */

public class AntiBotting {
	
	public static String currentQuestion;
	@SuppressWarnings("unused")
	private static String currentAnswer;
	
	private static final String[][] QUESTION_DATA = {
			{"What is the name of this server", "Zamron"}
	};
	
	
	public static void checkBotting(Player player) {
		if(Misc.getRandom(1) == 1) {
		cancelCurrentActions(player);
		sendPrompt(player);
		}
	}
	
	public static void sendPrompt(Player player) {
		for (int i = 0; i < QUESTION_DATA.length; i++) {
			if (Misc.getRandom(QUESTION_DATA.length - 1) == i) {
				
				currentQuestion = QUESTION_DATA[i][0];
				currentAnswer = QUESTION_DATA[i][1];
				player.getPacketSender().sendEnterAmountPrompt(currentQuestion);
				player.setInputHandling(new AntiBottingInput());
				player.setPlayerLocked(true);

			}
		}

	}

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
	

}
