package com.zamron.world.content;

import java.util.ArrayList;
import java.util.List;

import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Zenyte
 */
public class StaffList {

	public static List<String> staff = new ArrayList<>();

	public static void handleButton(Player player, int id) {

		int index = (id - 32410);
		if (index > staff.size() - 1) {
			return;
		}
		String name = getName(staff.get(index));
		Player p = World.getPlayerByName(name);
		if (p == null) {
			player.getPacketSender().sendMessage(name + " is currently offline.");
			return;
		}
		sendHelpRequest(player, p);
	}

	public static void clearList(Player player) {
		for (int i = 0; i < 40; i++) {
			player.getPacketSender().sendString(32410 + i, "");
		}
	}

	public static String getName(String name) {
		String[] split = name.split(" ");
		return split[1].substring(split[1].lastIndexOf('@') + 1);
	}

	public static void sendGlobalHelpRequest(Player player) {
		for (String name : staff) {
			Player p = World.getPlayerByName(getName(name));
			if (p == null) {
				continue;
			}
			if (p == player) {
				p.getPacketSender().sendMessage("You have sent a help request to the staff.");
				continue;
			}
			p.getPacketSender().sendMessage(player.getUsername() + " is asking for staff attention!");
		}
	}

	public static void sendHelpRequest(Player player, Player staff) {
		if (player == staff) {
			staff.getPacketSender().sendMessage("You cant ask help to yourself.");
		return;
		}
		if (System.currentTimeMillis() - player.lastHelpRequest < 6000) {
			player.getPacketSender().sendMessage("You need to wait a few seconds before doing this again.");
			return;
		}
		player.getPacketSender().sendMessage("You have asked " + staff.getUsername() + " for help.");
		staff.getPacketSender().sendMessage("@red@" + player.getUsername() + " asks for your help!");
		player.lastHelpRequest = System.currentTimeMillis();
	}

	public static void login(Player player) {
		if (staff.contains(getPrefix(player) + " @red@" + player.getUsername())) {
			staff.remove(getPrefix(player) + " @red@" + player.getUsername());
		}
		staff.add(getPrefix(player) + " @gre@" + player.getUsername());
	}

	public static void logout(Player player) {
		if (staff.contains(getPrefix(player) + " @gre@" + player.getUsername())) {
			staff.remove(getPrefix(player) + " @gre@" + player.getUsername());
		}
		staff.add(getPrefix(player) + " @red@" + player.getUsername());
	}

	public static void updateInterface(Player player) {
		clearList(player);
		for (int i = 0; i < staff.size(); i++) {
			String name = staff.get(i);
			player.getPacketSender().sendString(32410 + i, name);
		}
	}

	public static String getPrefix(Player player) {
		return "<img=" + player.getRights().ordinal() + ">";
	}

}
