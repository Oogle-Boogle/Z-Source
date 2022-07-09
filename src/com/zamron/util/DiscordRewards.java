package com.zamron.util;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.world.entity.impl.player.Player;

public final class DiscordRewards {

	public static CopyOnWriteArrayList<String> discordRewardNames;

	private static final String PATH = "./data/discordRewards.ser";
	
	private static boolean edited;

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists()) 
			try {
				discordRewardNames = (CopyOnWriteArrayList<String>) SerializableFilesManager.loadSerializedFile(file);
				return;
			
			} catch (Throwable e) {
				e.printStackTrace();
			}
		save();
		discordRewardNames = new CopyOnWriteArrayList<String>();
		System.err.println("Finished loading Discord Rewards List");
	}

	public static void save() {
		if (!edited)
			return;
		try {
			SerializableFilesManager.storeSerializableClass(discordRewardNames, new File(PATH));
			edited = false;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static boolean isOnList(String cpuId) {
		return discordRewardNames.contains(cpuId);
	}

	public static void addPlayer(Player player) {
		discordRewardNames.add(player.getSerialNumber());
		edited = true;
		save();
	}

	public static void remove(Player player) {
		discordRewardNames.remove(player.getSerialNumber());
		edited = true;
		save();
	}
	
	public static boolean remove(String serial) {
		boolean result = discordRewardNames.remove(serial);
		if (result) 
			edited = true;
		return result;
	}

	public static CopyOnWriteArrayList<String> getList() {
		return discordRewardNames;
	}
	
	/*public static void giveReward(Player player) {
		
		if (isOnList(player.getSerialNumber()))
			return;
		
		boolean bank = false;
		
		addPlayer(player);
		
		if (player.getInventory().getFreeSlots() > 0) 
			player.getInventory().add(new Item(6199, 1));
		 else {
			bank = true;
			player.getBank().add(new Item(6199, 1));
		}
		player.sendMessage("You have been rewarded 1x Mystery Box for joining the discord!");
		player.sendMessage("Your reward hass been added to your "+(bank ? "Bank" : "Inventory")+".");
	}*/

}
