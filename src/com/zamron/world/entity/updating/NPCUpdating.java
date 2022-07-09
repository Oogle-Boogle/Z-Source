package com.zamron.world.entity.updating;

import java.util.Iterator;

import com.zamron.model.Direction;
import com.zamron.model.Flag;
import com.zamron.model.Position;
import com.zamron.model.UpdateFlag;
import com.zamron.net.packet.ByteOrder;
import com.zamron.net.packet.PacketBuilder;
import com.zamron.net.packet.ValueType;
import com.zamron.net.packet.Packet.PacketType;
import com.zamron.net.packet.PacketBuilder.AccessType;
import com.zamron.world.World;
import com.zamron.world.entity.Entity;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * Represents a player's npc updating task, which loops through all local npcs
 * and updates their masks according to their current attributes.
 * 
 * @author Relex lawl
 */

public class NPCUpdating {

	/**
	 * Handles the actual npc updating for the associated player.
	 * 
	 * @return The NPCUpdating instance.
	 */
	public static void update(Player player) {
		try {
			PacketBuilder update = new PacketBuilder();
			PacketBuilder packet = new PacketBuilder(65, PacketType.SHORT);
			packet.initializeAccess(AccessType.BIT);
			packet.putBits(8, player.getLocalNpcs().size());
			for (Iterator<NPC> npcIterator = player.getLocalNpcs().iterator(); npcIterator.hasNext();) {
				NPC npc = npcIterator.next();
				if (World.getNpcs().get(npc.getIndex()) != null && npc.isVisible()
						&& player.getPosition().isWithinDistance(npc.getPosition()) && !npc.isNeedsPlacement()) {
					updateMovement(npc, packet);
					if (npc.getUpdateFlag().isUpdateRequired()) {
						appendUpdates(npc, update);
					}
				} else {
					npcIterator.remove();
					packet.putBits(1, 1);
					packet.putBits(2, 3);
				}
			}
			for (NPC npc : World.getNpcs()) {
				if (player.getLocalNpcs().size() >= 79) // Originally 255
					break;
				if (npc == null || player.getLocalNpcs().contains(npc) || !npc.isVisible() || npc.isNeedsPlacement())
					continue;
				if (npc.getPosition().isWithinDistance(player.getPosition())) {
					player.getLocalNpcs().add(npc);
					addNPC(player, npc, packet);
					if (npc.getUpdateFlag().isUpdateRequired()) {
						appendUpdates(npc, update);
					}
				}
			}
			if (update.buffer().writerIndex() > 0) {
				packet.putBits(14, 16383);
				packet.initializeAccess(AccessType.BYTE);
				packet.writeBuffer(update.buffer());
			} else {
				packet.initializeAccess(AccessType.BYTE);
			}
			if(!player.isMiniMe)
			player.getSession().queueMessage(packet);
		} catch (Throwable e) {
			e.printStackTrace();// let me know if it happens again
		}
	}

	public static void updateFacing(Player player, NPC npc) {
		if (npc.getMovementCoordinator().getCoordinator().isCoordinate() || npc.getCombatBuilder().isBeingAttacked()
				|| npc.getMovementQueue().isMoving())
			return;
		final Position position = npc.getPositionToFace();
		int x = position == null ? 0 : position.getX();
		int y = position == null ? 0 : position.getY();
		player.getSession().queueMessage(new PacketBuilder(88).putShort(x * 2 + 1).putShort(y * 2 + 1)
				.putShort(npc.getIndex(), ByteOrder.LITTLE));
	}

	/**
	 * Adds an npc to the associated player's client.
	 * 
	 * @param npc
	 *            The npc to add.
	 * @param builder
	 *            The packet builder to write information on.
	 * @return The NPCUpdating instance.
	 */
	private static void addNPC(Player player, NPC npc, PacketBuilder builder) {
		builder.putBits(14, npc.getIndex());
		builder.putBits(5, npc.getPosition().getY() - player.getPosition().getY());
		builder.putBits(5, npc.getPosition().getX() - player.getPosition().getX());
		builder.putBits(1, 0);
		builder.putBits(18, npc.getId());
		builder.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
	}

	/**
	 * Updates the npc's movement queue.
	 * 
	 * @param npc
	 *            The npc who's movement is updated.
	 * @param builder
	 *            The packet builder to write information on.
	 * @return The NPCUpdating instance.
	 */
	private static void updateMovement(NPC npc, PacketBuilder out) {
		if (npc.getSecondaryDirection().toInteger() == -1) {
			if (npc.getPrimaryDirection().toInteger() == -1) {
				if (npc.getUpdateFlag().isUpdateRequired()) {
					out.putBits(1, 1);
					out.putBits(2, 0);
				} else {
					out.putBits(1, 0);
				}
			} else {
				out.putBits(1, 1);
				out.putBits(2, 1);
				out.putBits(3, npc.getPrimaryDirection().toInteger());
				out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
			}
		} else {
			out.putBits(1, 1);
			out.putBits(2, 2);
			out.putBits(3, npc.getPrimaryDirection().toInteger());
			out.putBits(3, npc.getSecondaryDirection().toInteger());
			out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		}
	}

	/**
	 * Appends a mask update for {@code npc}.
	 * 
	 * @param npc
	 *            The npc to update masks for.
	 * @param builder
	 *            The packet builder to write information on.
	 * @return The NPCUpdating instance.
	 */
	private static void appendUpdates(NPC npc, PacketBuilder block) {
		int mask = 0;
		UpdateFlag flag = npc.getUpdateFlag();
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			mask |= 0x10;
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			mask |= 0x80;
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			mask |= 0x8;
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			mask |= 0x20;
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			mask |= 0x1;
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			mask |= 0x40;
		}
		if (flag.flagged(Flag.TRANSFORM) && npc.getTransformationId() != -1) {
			mask |= 0x2;
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			mask |= 0x4;
		}
		block.put(mask);
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			updateAnimation(block, npc);
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			updateSingleHit(block, npc);
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			updateGraphics(block, npc);
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			Entity entity = npc.getInteractingEntity();
			block.putShort(entity == null ? -1 : entity.getIndex() + (entity instanceof Player ? 32768 : 0));
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			block.putString(npc.getForcedChat());
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			updateDoubleHit(block, npc);
		}
		if (flag.flagged(Flag.TRANSFORM) && npc.getTransformationId() != -1) {
			block.putShort(npc.getTransformationId(), ValueType.A, ByteOrder.LITTLE);
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			final Position position = npc.getPositionToFace();
			int x = position == null ? 0 : position.getX();
			int y = position == null ? 0 : position.getY();
			block.putShort(x * 2 + 1, ByteOrder.LITTLE);
			block.putShort(y * 2 + 1, ByteOrder.LITTLE);
		}
	}

	/**
	 * Updates {@code npc}'s current animation and displays it for all local
	 * players.
	 * 
	 * @param builder
	 *            The packet builder to write information on.
	 * @param npc
	 *            The npc to update animation for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateAnimation(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
		builder.put(npc.getAnimation().getDelay());
	}

	/**
	 * Updates {@code npc}'s current graphics and displays it for all local players.
	 * 
	 * @param builder
	 *            The packet builder to write information on.
	 * @param npc
	 *            The npc to update graphics for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateGraphics(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getGraphic().getId());
		builder.putInt(((npc.getGraphic().getHeight().ordinal() * 50) << 16) + (npc.getGraphic().getDelay() & 0xffff));
	}

	/**
	 * Updates the npc's single hit.
	 * 
	 * @param builder
	 *            The packet builder to write information on.
	 * @param npc
	 *            The npc to update the single hit for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateSingleHit(PacketBuilder builder, NPC npc) {
		builder.putInt(npc.getPrimaryHit().getDamage());
		builder.put(npc.getPrimaryHit().getHitmask().ordinal(), ValueType.C);
		builder.put(npc.getPrimaryHit().getCombatIcon().ordinal() - 1);
		builder.putInt(npc.getConstitution());
		builder.putInt(npc.getDefaultConstitution());
	}

	/**
	 * Updates the npc's double hit.
	 * 
	 * @param builder
	 *            The packet builder to write information on.
	 * @param npc
	 *            The npc to update the double hit for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateDoubleHit(PacketBuilder builder, NPC npc) {
		builder.putInt(npc.getSecondaryHit().getDamage());
		builder.put(npc.getSecondaryHit().getHitmask().ordinal(), ValueType.S);
		builder.put(npc.getSecondaryHit().getCombatIcon().ordinal() - 1);
		builder.putInt(npc.getConstitution());
		builder.putInt(npc.getDefaultConstitution());
	}

	/**
	 * Resets all the npc's flags that should be reset after a tick's update
	 * 
	 * @param npc
	 *            The npc to reset flags for.
	 */
	public static void resetFlags(NPC npc) {
		npc.getUpdateFlag().reset();
		npc.setTeleporting(false).setForcedChat("");
		npc.setNeedsPlacement(false);
		npc.setPrimaryDirection(Direction.NONE);
		npc.setSecondaryDirection(Direction.NONE);
	}
}
