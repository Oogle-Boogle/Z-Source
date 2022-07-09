package com.zamron.world.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.stream.Stream;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class DonationDeals {
	
	private static Player player;
	
	public DonationDeals(Player player) {
		this.player = player;
	}
	
	public static final int[][] items = new int[][] {
		{13997, 5},
		{5170, 1},
		{20427, 1},
	};
	
	public static long timeLeft = 86400000 - 1000; //86400000
	
	public long displayTimeLeft(long timeInMs) {
		
		long seconds = (int) Math.ceil((timeInMs / 1000));
		
		if(seconds < 0) {
			//System.out.println("Stopping as its less than 0");
			return 0;
		}
		
		LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
		String time = timeOfDay.toString();
		
	    player.getPacketSender().sendString(57267, "Promotions: (Expires in: @yel@" + time + ")");
	    //System.out.println(time);
	    
	    return seconds;
		
	}
	
	public void displayReward() {
		player.getPacketSender().sendInterface(57265);
		int slot = 0;
		for(int[] item : items) {
			player.getPacketSender().sendItemOnInterface(57276, item[0], slot, item[1]);
			slot++;
		}
	}
	
	public void reset() {
		
		player.setAmountDonatedToday(0);
		player.claimedFirst = false;
		player.claimedSecond = false;
		player.claimedThird = false;
	}
	
	public static void handleRewards() {
		int amount = player.getAmountDonatedToday();
		if(amount < 85) {
			return;
		}
		if(amount >= 85 && amount < 150 && !player.claimedFirst) {
			player.getInventory().add(items[0][0], items[0][1]);
			player.claimedFirst = true;
			player.sendMessage("Enjoy the extra reward for donating!");
		} else if(amount >= 150 && amount < 300 && !player.claimedSecond) {
			player.getInventory().add(items[1][0], items[1][1]);
			player.claimedSecond = true;
			player.sendMessage("Enjoy the extra reward for donating!");
		} else if(amount >= 300 && !player.claimedThird) {
			player.getInventory().add(items[2][0], items[2][1]);
			player.claimedThird = true;
			player.sendMessage("Enjoy the extra reward for donating!");
		}
	}
	
	private final Path FILE_PATH = Paths.get("./data/saves/DonationDealData.txt");
	
	private void writeLastResetTime() {
		
		final String lastResetTime = Long.toString(System.currentTimeMillis());
		try {
			Files.write(FILE_PATH, lastResetTime.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	long lastResetTime = -1;
	
	private long getLastResetTime() {
		try(Stream<String> lines = Files.lines(FILE_PATH).limit(1)) {
			
			lines.forEach(x -> {
				lastResetTime = Long.parseLong(x);
			});
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		return lastResetTime;
		
	}
	
	public boolean shouldReset() {
		return getLastResetTime() > player.lastDonation;
	}
	
	public void displayTime() {
		TaskManager.submit(new Task(1, player, true) {
			@Override
			protected void execute() {
				//System.out.println("Called this method");
				if(timeLeft > 0) {
		    	displayTimeLeft(timeLeft);
		    	timeLeft -= 600;
				} else {
					writeLastResetTime();
				}
			}
		});
	}
}
