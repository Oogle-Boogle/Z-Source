package com.zamron.util;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerHandler;

public final class MACBanL {

	public static CopyOnWriteArrayList<String> macList;

	private static final String PATH = "./data/bannedMACS.ser";
	
	private static boolean edited;

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists()) 
			try {
				macList = (CopyOnWriteArrayList<String>) SerializableFilesManager.loadSerializedFile(file);
				return;
			
			} catch (Throwable e) {
				e.printStackTrace();
			}
		save();
		macList = new CopyOnWriteArrayList<String>();
		System.err.println("Finished loading MACList");
	}

	public static void save() {
		if (!edited)
			return;
		try {
			SerializableFilesManager.storeSerializableClass(macList, new File(PATH));
			edited = false;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static boolean isBanned(String mac) {
		return macList.contains(mac);
	}

	public static void ban(Player player, boolean loggedIn) {
		macList.add(player.getMacAddress());
		if (loggedIn)
			PlayerHandler.handleLogout(player, false);
		edited = true;
		save();
	}

	public static void unban(Player player) {
		macList.remove(player.getMacAddress());
		edited = true;
		save();
	}
	
	public static boolean remove(String mac) {
		boolean result = macList.remove(mac);
		if (result) {
			edited = true;
		}
		return result;
	}

	public static CopyOnWriteArrayList<String> getList() {
		return macList;
	}

}
