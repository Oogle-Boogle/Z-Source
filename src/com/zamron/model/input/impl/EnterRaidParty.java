package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class EnterRaidParty extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("@red@Invalid syntax entered.");
			return;
		}
		Player raidLeader = World.getPlayerByName(syntax);
		if(raidLeader == player) {
			player.sendMessage("@red@You cannot join yourself.");
			return;
		}
		if (raidLeader == null) {
			player.sendMessage("@red@That player is not online.");
			return;
		}
		if (raidLeader.getOldRaidParty() == null) {
			player.sendMessage("@red@That player does not have a raid party to join.");
			return;
		}
		if (raidLeader.getOldRaidParty().getLeader() != raidLeader) {
			player.sendMessage("@red@That player is not the leader of the raid party.");
			return;
		}
		if(raidLeader.getOldRaidParty().getMembers().size() == 10) {
			player.sendMessage("@red@" + raidLeader.getUsername() + "'s party is currently full.");
			return;
		}
		if(!player.getRaidPartyInvites().contains(raidLeader.getOldRaidParty()) && raidLeader.getOldRaidParty().getIsRaiding(raidLeader)) {
			player.sendMessage("@red@That player is currently raiding. Please try again later.");
		}
		if (player.getRaidPartyInvites().contains(raidLeader.getOldRaidParty())) {
			player.sendMessage("@red@You already requested to join " + raidLeader.getUsername() + "'s raid party.");
			return;
		}
		if (raidLeader.getRelations().isFriendWith(player.getUsername())) {
			raidLeader.sendMessage("@blu@" + player.getUsername() + " has requested to join your raid party.");
			player.addPendingRaidParty(raidLeader.getOldRaidParty());
		} else {
			player.sendMessage("@red@" + raidLeader.getUsername() + " must be friends with you to request an invite.");
			return;
		}
	}
}
