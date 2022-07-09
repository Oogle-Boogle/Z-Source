package com.zamron.world.content.roulette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class Roulette {
	
	public enum BetType {
		
		RED,
		BLACK,
		GREEN;
	}
	
	private List<BetType> currentBets = new ArrayList<>();
	
	public List<BetType> getCurrentBets() {
		return currentBets;
	}
	
	public void addBetType(BetType type) {
		currentBets.add(type);
	}
	
	public void removeBetType(BetType type) {
		currentBets.remove(type);
	}
	
	public void handleBetType(BetType type) {
		if(currentBets.contains(type)) {
			removeBetType(type);
		} else {
			addBetType(type);
		}
	}

	private final Player player;

	public Roulette(Player player) {
		this.player = player;
	}
	
	public static int winningNumber = -1;

	private final int MAX_ROULETTE_BET = 1000_000_000;

	private final List<Integer> greenNumbers = Arrays.asList(16, 30, 44, 58, 72, 86, 100, 114, 128, 142);
	
	public boolean spinning = false;
	
	public void startGame() {
		
		if(spinning) {
			player.sendMessage("@red@Please wait till your previous spin is finished.");
			return;
		}
		
		if(player.getRouletteBet() == 0) {
			player.sendMessage("@red@You haven't set a bet yet.");
			return;
		}
		
		if(player.getRouletteBalance() < player.getRouletteBet() * currentBets.size()) {
			player.sendMessage("@red@Cannot place bet because u dont have enough deposited :/");
			return;
		}
		if(currentBets.isEmpty()) {
			player.sendMessage("You haven't bet on anything yet.");
			return;
		}
		player.decrementRouletteBalance(player.getRouletteBet() * currentBets.size());
		player.getPacketSender().sendString(23584, "@or2@Current Balance: @yel@" + Misc.formatNumber(player.getRouletteBalance()));
		winningNumber = RandomUtility.inclusiveRandom(16, 142);
		spinning = true;
		player.getPacketSender().sendRouletteNumber(winningNumber);
	}

	public void getWinner(int number) {
		spinning = false;
		if (greenNumbers.contains(number)) {
			if(player.getRoulette().currentBets.contains(BetType.GREEN)) {
				player.sendMessage("Congrats u bet on green and won!");
				player.incrementRouletteBalance(player.getRouletteBet() * 14);
			}
		} else if (number % 2 == 0) {
			if(player.getRoulette().currentBets.contains(BetType.RED)) {
				player.sendMessage("Congrats u bet on red and won!");
				player.incrementRouletteBalance(player.getRouletteBet() * 2);
			}
		} else {
			if(player.getRoulette().currentBets.contains(BetType.BLACK)) {
				player.sendMessage("Congrats u bet on black and won!");
				player.incrementRouletteBalance(player.getRouletteBet() * 2);
			}
		}
		
		//player.getPacketSender().sendString(23584, "@or2@Current Balance: @yel@" + player.getRouletteBalance());
		player.getPacketSender().sendString(23584, "@or2@Current Balance: @yel@" + Misc.formatNumber(player.getRouletteBalance()));

	}

	public void deposit(int amount) {
		if (player.getInventory().getAmount(995) >= amount) {
			player.getInventory().delete(995, amount);
			player.incrementRouletteBalance(amount);
			player.sendMessage("Sucessfully deposited: " + Misc.formatAmount(amount)); // TODO format
			player.getPacketSender().sendString(23584, "@or2@Current Balance: @yel@" + Misc.formatNumber(player.getRouletteBalance()));
		} else {
			player.sendMessage("@red@Can't deposit, what you don't have :/");
		}
	}

	public void withdraw(int amount) {
		if (player.getRouletteBalance() >= amount) {
			player.decrementRouletteBalance(amount);
			player.getInventory().add(995, amount);
			player.sendMessage("Successfully withdrawed: " + Misc.formatAmount(amount));
			player.getPacketSender().sendString(23584, "@or2@Current Balance: @yel@" + Misc.formatNumber(player.getRouletteBalance()));
		} else {
			player.sendMessage("@red@Can't withdraw, what you don't have :/");
		}
	}

	public void setBet(long amount) {
		if (amount > MAX_ROULETTE_BET) {
			player.sendMessage("Error - Max roulette bet is: " + MAX_ROULETTE_BET + " coins"); // TODO format
			return;
		}
			if(player.getRouletteBalance() >= amount) {
			player.sendMessage("Successfully set roulette bet to: " + Misc.formatAmount(amount)); // TODO format
			player.setRouletteBet(amount);
			player.getPacketSender().sendString(23583, "@or2@Current bet amount: @yel@" + Misc.formatNumber(player.getRouletteBet()));
			return;
		} else {
			player.sendMessage("Can't bet what you don't have :/");
	}
	}

}
