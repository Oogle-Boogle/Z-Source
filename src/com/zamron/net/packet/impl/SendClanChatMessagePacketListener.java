package com.zamron.net.packet.impl;

import org.jboss.netty.buffer.ChannelBuffer;

import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.world.content.PlayerPunishment;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.entity.impl.player.Player;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		/** Get method for the channel buffer. **/
		ChannelBuffer opcode = packet.getBuffer();
		/** Gets requested bytes from the buffer client > server **/
		int size = opcode.readableBytes();
		/** Check to flood **/
		if (size < 1 || size > 125) {
			System.err.println("blocked packet from sending from clan chat. Requested size="+size);
			return;
		}
		
		String clanMessage = packet.readString();
		/** Checks for null, invalid messages **/
		if(clanMessage == null || clanMessage.length() < 1 || clanMessage.length() > 125)
			return;
		
		if(PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		ClanChatManager.sendMessage(player, Misc.filterMessage(player, clanMessage));
	}

}