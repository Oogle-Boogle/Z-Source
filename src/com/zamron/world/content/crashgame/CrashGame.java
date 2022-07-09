package com.zamron.world.content.crashgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class CrashGame {

	private static double mult;
	private static boolean isActive;
	private static ArrayList<Player> currentPlayers = new ArrayList<>();

	public static void addPlayer(Player player) {
		if (!currentPlayers.contains(player))
			currentPlayers.add(player);
		player.getPacketSender().sendString(62007, "LOCKED IN");
		updatePreGame();
	}

	public static double getMultiplier() {
		return mult;
	}

	public static boolean checkIfPlaying(Player player) {
		if (currentPlayers.contains(player))
			return true;
		return false;
	}

	public static boolean getActive() {
		return isActive;
	}

	public static void init() {
		new Thread(() -> {
			run();
		}).start();
	}

	private static void updatePreGame() {
		String playerArea = "";
		for (Player player : currentPlayers) {
			playerArea += "@gre@" + player.getUsername() + " | " + (int) (player.getCrashGameBet() / 1000) + "k\\n";
		}
		for (Player player : World.getPlayers())
			if (player != null)
				player.getPacketSender().sendString(62018, playerArea);
	}

	private static void updateDuringGame() {
		String playerStillIn = "";
		String playerCashedOut = "";
		for (Player player : currentPlayers) {
			if (player.getCashedOutMult() == 0 && isActive)
				playerStillIn += "@yel@" + player.getUsername() + " | " + (int) (player.getCrashGameBet() / 1000)
						+ "k\\n";
			else if (player.getCashedOutMult() > 0)
				playerCashedOut += "@gre@" + player.getUsername() + " | " + (int) (player.getCrashGameBet() / 1000)
						+ "k | " + player.getCashedOutMult() + "x\\n";
			else if (player.getCashedOutMult() == 0 && !isActive)
				playerCashedOut += "@red@" + player.getUsername() + " | " + (int) (player.getCrashGameBet() / 1000)
						+ "k\\n";
		}
		playerStillIn += playerCashedOut;
		for (Player player : World.getPlayers())
			if (player != null)
				player.getPacketSender().sendString(62018, playerStillIn);
	}

	public static void startClimb() {
		mult = 1.0;
		boolean bust = false;
		isActive = true;
		int bustType = RandomUtility.random(20);
		for (Player player : currentPlayers)
			if (player.getCashedOutMult() == 0.0)
				player.getPacketSender().sendString(62007, "Click here to pull out!");
		for (Player player : World.getPlayers())
			if (player != null)
				player.getPacketSender().sendString(62017, "");
		while (!bust) {
			updateDuringGame();
			bust = didWeBust(bustType);
			if (!bust) {
				doAutoCashOut();
				mult += 0.01;
				for (Player player : World.getPlayers())
					if (player != null) {
							player.getPacketSender().sendCrashMultiplier(String.format("@gre@%.2fx", mult));
						//player.getPacketSender().sendString(62016, String.format("@gre@%.2fx", mult));
					}
				try {
					Thread.sleep((long) Math.floor(20000 / (mult * 100)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		isActive = false;
		updateDuringGame();
		calculateWinnings();
		currentPlayers = new ArrayList<>();
		for (Player player : World.getPlayers())
			if (player != null) {
				player.getPacketSender().sendCrashMultiplier(String.format("@red@Busted at %.2fx", mult));
				writeData(String.format("Busted at %.2fx ", mult));
				//player.getPacketSender().sendString(62016, String.format("@red@Busted at %.2fx", mult));
				player.getPacketSender().sendString(62007, "PLACE BET");
			}
		startTimer();
	}
	
	public static void writeData(String data) {
		
		Path path = Paths.get("");
		
		try {
			Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void startTimer() {
		for (int i = 10; i >= 0; i--) {
			if (i == 5)
				updatePreGame();
			for (Player player : World.getPlayers())
				if (player != null)
					player.getPacketSender().sendString(62017, i + " seconds until next round");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		startClimb();
	}

	private static void calculateWinnings() {
		for (Player player : World.getPlayers())
			if (player != null)
				if (checkIfPlaying(player))
					if (player.getCashedOutMult() > 0) {
						player.addToCrashBalance((long) (player.getCrashGameBet() * player.getCashedOutMult()
								- player.getCrashGameBet()));
						if (player.getCrashGameBet() > player.getCrashGameBalance())
							player.setCrashGameBet(0);
						player.setCashedOutMult(0.0);
						String bet;
						if (player.getCrashGameBet() > 0)
							bet = (player.getCrashGameBet() / 1000) + "k";
						else
							bet = "0";
						player.getPacketSender().sendString(62010, bet);
						player.getPacketSender().sendString(62013,
								String.format("%.0fk", Math.floor(player.getCrashGameBalance() / 1000)));
					} else {
						player.removeFromCrashBalance(player.getCrashGameBet());
						if (player.getCrashGameBet() > player.getCrashGameBalance())
							player.setCrashGameBet(0);
						player.setCashedOutMult(0.0);
						String bet;
						if (player.getCrashGameBet() > 0)
							bet = (player.getCrashGameBet() / 1000) + "k";
						else
							bet = "Use ::bet [amount] to place a bet.";
						player.getPacketSender().sendString(62010, bet);
						player.getPacketSender().sendString(62013,
								String.format("%.0fk", Math.floor(player.getCrashGameBalance() / 1000)));
					}
	}

	private static void doAutoCashOut() {
		for (Player player : currentPlayers)
			if (player.getCrashAutoCashout() > 0 && player.getCashedOutMult() == 0
					&& player.getCrashAutoCashout() < mult) {
				double mult2 = Double.parseDouble(String.format("%.2f", mult));
				player.getPacketSender().sendString(62007, "You pulled out at " + mult2 + "x");
				player.setCashedOutMult(mult2);
			}
	}

	public static void run() {
		//System.out.println("Starting Crash Game...");
		startTimer();
	}

	public static boolean didWeBust(int bustType) {
		
		int c = RandomUtility.random(10000);
		
		////System.out.println("Was called c: " + c);

		return c >= 9999;
		
		
		/*switch (bustType) {
		case 0:
			return c <= 20;
		case 1:
		case 2:
		case 9:
			return c <= 60;
		case 3:
		case 4:
		case 12:
			return c <= 100;
		case 5:
		case 10:
		case 14:
			return c <= 750;
		case 6:
		case 7:
		case 13:
			return c <= 350;
		case 8:
		case 11:
			return c <= 1;
		default:
			return c <= 175;
		}*/
	}

}
