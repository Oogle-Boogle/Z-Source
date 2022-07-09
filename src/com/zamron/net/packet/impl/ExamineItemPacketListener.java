package com.zamron.net.packet.impl;

import com.zamron.model.Skill;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.net.packet.Packet;
import com.zamron.net.packet.PacketListener;
import com.zamron.util.Misc;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.entity.impl.player.Player;

public class ExamineItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readShort();
		if(item == OldRaidParty.MAGICAL_ORB) {
			if(player.getInventory().contains(item)) {
				if(player.getOldRaidParty() != null) {
					player.sendMessage("@blu@Raid Party Members:");
					for(Player member : player.getOldRaidParty().getMembers()) {
						player.sendMessage("->" + member.getUsername());
					}
				}
				return;
			} else {
				player.sendMessage("Place this in your inventory and examine to show your guild party members.");
			}
		}
		
		if(item == 10835 || item == 18201) {
			player.getPacketSender().sendMessage("Mhmm... Shining coins...");
			return;
		}
		ItemDefinition itemDef = ItemDefinition.forId(item);
		if(itemDef != null) {
			player.getPacketSender().sendMessage(itemDef.getDescription());
			for (Skill skill : Skill.values()) {
				if (itemDef.getRequirement()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
					player.getPacketSender().sendMessage("@red@WARNING: You need " + new StringBuilder().append(skill.getName().startsWith("a") || skill.getName().startsWith("e") || skill.getName().startsWith("i") || skill.getName().startsWith("o") || skill.getName().startsWith("u") ? "an " : "a ").toString() + Misc.formatText(skill.getName()) + " level of at least " + itemDef.getRequirement()[skill.ordinal()] + " to wear this.");
				}
			}
		}
	}

}
