package com.zamron.world.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public final class LotterySystem {

	public static final Path LOTTERY_DATA = Paths.get("data", "lottery_data.txt"); // size of this will very likely not
	// exceed over 500 lines
	public static final Path LOTTERY_WINNERS = Paths.get("data", "lottery_winners.txt"); // size of this will very
	// likely not exceed over
	// 5-20 lines.

	public static void addUser(final String username, final Path filePath) {
		writeUser(username, filePath);
	}

	private static void writeUser(final String username, final Path filePath) {
		List<String> userData = new ArrayList<>();
		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(userData::add);
			userData.add(username);
			Files.write(filePath, userData);

		} catch (IOException e) {
			e.printStackTrace(); // sec
		}

	}

	public static void pickWinner() { // nv m i already did, only need to set a timer., for now lets set to 2 or 3
										// minutes to test fast.
		try {
			List<String> lines = Files.readAllLines(LOTTERY_DATA);
			final int index = ThreadLocalRandom.current().nextInt(lines.size());
			String winner = lines.get(index);
			writeUser(winner + "," + getTotalPrizepool(), LOTTERY_WINNERS);
			World.sendMessageNonDiscord("<img=12>@blu@[LOTTERY SYSTEM] @red@" + winner + " @blu@Has won the lottery!");
			Files.write(LOTTERY_DATA, "".getBytes()); // Clear file
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void claimReward(Player player) {
		String name = player.getUsername();
		try {
			List<String> winners = Files.readAllLines(LOTTERY_WINNERS).stream().map(x -> x.substring(0, x.indexOf(",")))
					.collect(Collectors.toList());
			//System.out.println("Winners: " + Arrays.asList(winners.toArray()));
			if (winners.contains(name)) {
				player.sendMessage("Winner!");
				player.getInventory().add(10835, getWinnerPrizePool());
				winners.remove(name);
			} else {
				player.sendMessage("You don't have any pending winnings.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void claimReward(String username) {
		List<String> winnerList = new ArrayList<>();
		try (Stream<String> lines = Files.lines(LOTTERY_WINNERS)) {


			lines.forEach(line -> {
				String name = line.substring(0, line.indexOf(","));
				//System.out.println("Found name: " + name);
				if (!name.equalsIgnoreCase(username)) {
					winnerList.add(line);
				} else {
					// do logic
				}
			});
			Files.write(LOTTERY_WINNERS, winnerList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int prizePool = 0;

	private static int getWinnerPrizePool() {

		try (Stream<String> lines = Files.lines(LOTTERY_WINNERS)) {
			lines.forEach(x -> {
				String[] split = x.split(","); // split by ','
				prizePool = Integer.parseInt(split[1]);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prizePool;
	}

	public static int getCurrentTicketAmount() {
		int totalTickets = 0;
		try {
			totalTickets = Files.readAllLines(LOTTERY_DATA).size();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return totalTickets;
	}

	public static int getTotalPrizepool() {
		return (int) ((getCurrentTicketAmount() * 1000) * (0.8)); // total prize pool should be same, or should there be
																	// server fee? should be a server fee of like 80% of
																	// the amount only is in the pool kk

	}

}
