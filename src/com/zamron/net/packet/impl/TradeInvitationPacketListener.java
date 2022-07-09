package com.zamron.net.packet.impl;

import com.zamron.engine.task.impl.WalkToTask;
import com.zamron.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zamron.model.Locations;
import com.zamron.model.Locations.Location;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player accepts a trade offer,
 * whether it's from the chat box or through the trading player menu.
 * 
 * @author relex lawl
 */

public class TradeInvitationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (Misc.getMinutesPlayed(player) <= 30) {
			player.sendMessage("You need to have played atleast 30 minutes to trade items.");
			return;
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.getCombatBuilder().cooldown(false);
		if (player.getConstitution() <= 0)
			return;
		if(player.isTeleporting())
			return;
		player.getSkillManager().stopSkilling();
		if(player.getLocation() == Location.FIGHT_PITS) {
			player.getPacketSender().sendMessage("You cannot trade other players here.");
			return;
		}
		int index = packet.getOpcode() == TRADE_OPCODE ? (packet.readShort() & 0xFF) : packet.readLEShort();
		if(index < 0 || index > World.getPlayers().capacity())
			return;
		Player target = World.getPlayers().get(index);
		
		////System.out.println("Index: " + index);
		////System.out.println("Name: " + target.getUsername());
		if (target == null || !Locations.goodDistance(player.getPosition(), target.getPosition(), 13)) 
			return;
		if(target.isMiniMe) {
			if (target.getMinimeOwner() != player)
			player.sendMessage("@red@You cannot trade with this mini-me!");
			return;
		}
		player.setWalkToTask(new WalkToTask(player, target.getPosition(), target.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				if(target.getIndex() != player.getIndex())
					player.getTrading().requestTrade(target);
			}
		}));
	}

	public static final int TRADE_OPCODE = 39;
	public static final int CHATBOX_TRADE_OPCODE = 139;
}
