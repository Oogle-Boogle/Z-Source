package com.zamron.world.content.scratchcard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zamron.model.Item;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class ScratchCard {

	private Player player;

	public ScratchCard(Player player) {
		this.player = player;
	}

	public void display() {

		if (inSession()) {
			return;
		}

		if (player.getInventory().contains(10600, 1)) {
			player.getInventory().delete(new Item(10600, 1));
			cleanInterface();
			player.getPacketSender().sendInterface(39300);
			COMBINATION.add(new ScratchCardCombination(new ScratchCardInstanced(-26218, getRandom()),
					new ScratchCardInstanced(-26215, getRandom()), new ScratchCardInstanced(-26209, getRandom())));
		}
	}

	/**
	 * On {@code display}
	 */
	private void cleanInterface() {

		setBonus(false);
		COMBINATION.clear();
		COMBINATION_COUNT.clear();
		player.getPacketSender().sendItemOnInterface(39330, -1, -1);
		player.getPacketSender().sendItemOnInterface(39331, -1, -1);
		player.getPacketSender().sendItemOnInterface(39332, -1, -1);
		player.getPacketSender().sendItemOnInterface(39333, -1, -1);		
		player.getPA().sendFrame126("Match 3 to Win!", 39303);
		player.getPA().sendFrame126("$", 39306);
		player.getPA().sendFrame126("$", 39316);
		player.getPA().sendFrame126("$", 39314);
	}

	/**
	 * Processes on button {@click}
	 */
	public boolean win = true;
	private void process() {
		if (COMBINATION_COUNT.size() == 3) {
			if ((COMBINATION_COUNT.get(0).getId() == COMBINATION_COUNT.get(1).getId())
					&& (COMBINATION_COUNT.get(0).getId() == COMBINATION_COUNT.get(2).getId() && win == true)) {
				player.getInventory().add(new Item(COMBINATION_COUNT.get(0).getId(), 1));
				player.getPA().sendFrame126("Congratulation's, you have won!", 39303);
				player.getPA().sendMessage("Congratulation's, you have won!");
			} else {
				player.getPA().sendFrame126("Bad luck, you have lost!", 39303);
				player.getPA().sendMessage("Bad luck, you have lost!");
			}
			getBonus();
		}
	}

	/**
	 * Reveals the clicked card
	 * 
	 * @param button
	 */
	public void reveal(int button) {
		if (button == -26212) {
			miscButton(button);
			return;
		}
		switch (button) {
		case -26218:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getFirst())) {
					player.getPA().sendMessage("You've already revealed the first card!");
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getFirst());
				player.getPA().sendFrame126("", 39306);
			//	player.getPacketSender().sendItemOnInterface(39330, COMBINATION.get(i).getFirst().getId(), 1);
				player.getPacketSender().sendItemOnInterface(39330, COMBINATION.get(i).getFirst().getId(), 1, 1);
			}
			break;
		case -26215:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getSecond())) {
					player.getPA().sendMessage("You've already revealed the second card!");
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getSecond());
				player.getPA().sendFrame126("", 39316);
				player.getPacketSender().sendItemOnInterface(39331, COMBINATION.get(i).getSecond().getId(), 1, 1);

			}
			break;
		case -26209:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getThird())) {
					player.getPA().sendMessage("You've already revealed the third card!");
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getThird());
				player.getPA().sendFrame126("", 39314);
				player.getPacketSender().sendItemOnInterface(39333, COMBINATION.get(i).getThird().getId(), 1, 1);

			}
			break;
		}
		process();
	}

	private void miscButton(int button) {
		switch (button) {
		case -26212:
			if (COMBINATION_COUNT.size() < 3 || !BONUS) {
				player.getPA().sendMessage("Please finish your scratch session!");
				return;
			}
			// player.interfaceManager.close();
			player.getPacketSender().closeAllWindows();
			break;
		}
	}

	/**
	 * Fetches a random bonus
	 */
	public void getBonus() {
		setBonus(true);
		int items[] = { 10835 };
		int BonusRewards[] = { 10835 };

		int item = items[random.nextInt(items.length)];
		int item1 = BonusRewards[random.nextInt(BonusRewards.length)];
			player.getInventory().add(new Item(item1, 8500));
			player.getPacketSender().sendItemOnInterface(39332, item1, 1, 8500);
		}

	/**
	 * Checks if player is in session
	 * 
	 * @return
	 */
	private boolean inSession() {
		if (player.getInterfaceId() == 39300) {
			player.getPA().sendMessage("You are already in a scratch session!");
			return true;
		}
		return false;
	}

	/**
	 * Fetches a random card
	 */
	private static Random random = new Random();

	private int getRandom() {
		return ScratchCardData.values()[random.nextInt(ScratchCardData.values().length)].getDisplayId();
	}

	/**
	 * Checks if the player has revealed the bonus prize
	 */
	private static boolean BONUS = false;

	public static void setBonus(boolean BONUS) {
		ScratchCard.BONUS = BONUS;
	}

	/**
	 * Stores the already used cards
	 */
	private static final List<ScratchCardInstanced> COMBINATION_COUNT = new ArrayList<>();

	/**
	 * Stores the randomly generated cards
	 */
	private static final List<ScratchCardCombination> COMBINATION = new ArrayList<>();
}