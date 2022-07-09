package com.zamron.net.packet.impl;

import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.world.World;
import com.zamron.world.content.combat.magic.CombatSpell;
import com.zamron.world.content.combat.magic.CombatSpells;
import com.zamron.world.entity.impl.player.Player;

public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		
		if(playerIndex < 0 || playerIndex > World.getPlayers().capacity())
			return;
		
		int spellId = packet.readLEShort();
		
		if (spellId < 0) 
			return;

		Player attacked = World.getPlayers().get(playerIndex);

		if (attacked == null || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}

		CombatSpell spell = CombatSpells.getSpell(spellId);
		if(spell == null) {
			player.getMovementQueue().reset();
			return;
		}
		
		if(attacked.getConstitution() <= 0) {
			player.getMovementQueue().reset();
			return;
		}
		
		// Start combat!
		player.setPositionToFace(attacked.getPosition());
		player.getCombatBuilder().resetCooldown();
		player.setCastSpell(spell);
		player.getCombatBuilder().attack(attacked);
	}

}
