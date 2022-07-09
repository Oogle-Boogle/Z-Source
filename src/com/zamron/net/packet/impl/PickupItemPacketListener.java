package com.zamron.net.packet.impl;

import com.zamron.engine.task.impl.WalkToTask;
import com.zamron.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.player.Player;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 * 
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(final Player player, Packet packet) {
		final int y = packet.readLEShort();
		final int itemId = packet.readShort();
		final int x = packet.readLEShort();
		if(player.isTeleporting())
			return;
		final Position position = new Position(x, y, player.getPosition().getZ());
		if(!player.getLastItemPickup().elapsed(500))
			return;
		if(player.getConstitution() <= 0 || player.isTeleporting())
			return;
		player.setWalkToTask(new WalkToTask(player, position, 1, new FinalizedMovementTask() {
			@Override
			public void execute() {
				if (Math.abs(player.getPosition().getX() - x) > 25 || Math.abs(player.getPosition().getY() - y) > 25) {
					player.getMovementQueue().reset();
					return;
				}
				boolean canPickup = player.getInventory().getFreeSlots() > 0 || (player.getInventory().getFreeSlots() == 0 && ItemDefinition.forId(itemId).isStackable() && player.getInventory().contains(itemId));
				if(!canPickup) {
					player.getInventory().full();
					return;
				}
				GroundItem gItem = GroundItemManager.getGroundItem(player, new Item(itemId), position);
				if(gItem != null) {
					if(player.getInventory().getAmount(gItem.getItem().getId()) + gItem.getItem().getAmount() > Integer.MAX_VALUE || player.getInventory().getAmount(gItem.getItem().getId()) + gItem.getItem().getAmount() <= 0) {
						player.getPacketSender().sendMessage("You cannot hold that amount of this item. Clear your inventory!");
						return;
					}
					GroundItemManager.pickupGroundItem(player, new Item(itemId), new Position(x, y, player.getPosition().getZ()));
				}
			}
		}));
	}
}
