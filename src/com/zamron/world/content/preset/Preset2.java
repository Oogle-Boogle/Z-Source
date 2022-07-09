package com.zamron.world.content.preset;

import com.zamron.GameSettings;
import com.zamron.model.Flag;
import com.zamron.model.Item;
import com.zamron.model.MagicSpellbook;
import com.zamron.model.Prayerbook;
import com.zamron.model.container.impl.Bank;
import com.zamron.model.container.impl.Equipment;
import com.zamron.net.packet.impl.EquipPacketListener;
import com.zamron.world.content.combat.magic.Autocasting;
import com.zamron.world.entity.impl.player.Player;

public class Preset2 {

	public static void load(Player p) {

		if (!p.getClickDelay().elapsed(5000)) {
			p.getPacketSender().sendMessage("You must wait 5 seconds to load another preset.");
			return;
		}
		if (p.presetItems2 == null) {
			return;
		}
		if (!Player.preset2) {
			p.getPacketSender().sendMessage("You don't have a loadout in preset 2.");
			return;
		}
		p.getPacketSender().sendMessage("You have loaded Preset 2.");

		p.getClickDelay().reset();

		p.getSkillManager().stopSkilling();
		Bank.depositItems(p, p.getEquipment(), true);
		Bank.depositItems(p, p.getInventory(), true);

		if (p.getMageBook2() == 3) {
			p.setSpellbook(MagicSpellbook.ANCIENT);
		} else if (p.getMageBook2() == 2) {
			p.setSpellbook(MagicSpellbook.LUNAR);
		} else if (p.getMageBook2() == 1) {
			p.setSpellbook(MagicSpellbook.NORMAL);
		}
		if (p.getPrayerBook2() == 1) {
			p.setPrayerbook(Prayerbook.NORMAL);
		} else if (p.getPrayerBook2() == 2) {
			p.setPrayerbook(Prayerbook.CURSES);
		}

		p.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, p.getSpellbook().getInterfaceId());
		Autocasting.resetAutocast(p, true);

		for (int j = 0; j < 56; j += 2) {
			Item inv = new Item(p.presetItems2[j], p.presetItems2[j + 1]);
			int tab = Bank.getTabForItem(p, p.presetItems2[j]);
			if (p.getBank(tab).contains(inv.getId(), inv.getAmount())) {
				p.getBank(tab).delete(p.presetItems2[j], p.presetItems2[j + 1]);
				p.getInventory().add(p.presetItems2[j], p.presetItems2[j + 1]);
			} else {
				p.getPacketSender().sendMessage("You are missing " + inv.getDefinition().getName());
			}

		}
		Item head = new Item(p.presetGear2[0], p.presetGear2[1]);
		Item cape = new Item(p.presetGear2[2], p.presetGear2[3]);
		Item amulet = new Item(p.presetGear2[4], p.presetGear2[5]);
		Item weapon = new Item(p.presetGear2[6], p.presetGear2[7]);
		Item body = new Item(p.presetGear2[8], p.presetGear2[9]);
		Item shield = new Item(p.presetGear2[10], p.presetGear2[11]);
		Item leg = new Item(p.presetGear2[14], p.presetGear2[15]);
		Item hands = new Item(p.presetGear2[18], p.presetGear2[19]);
		Item feet = new Item(p.presetGear2[20], p.presetGear2[21]);
		Item ring = new Item(p.presetGear2[24], p.presetGear2[25]);
		Item amunnution = new Item(p.presetGear2[26], p.presetGear2[27]);

		if ((p.getBank(Bank.getTabForItem(p, amunnution.getId())).contains(amunnution.getId(),
				amunnution.getAmount()))) {
			p.getEquipment().set(Equipment.AMMUNITION_SLOT, amunnution);
			p.getBank(Bank.getTabForItem(p, amunnution.getId())).delete(amunnution);

		} else {
			p.getPacketSender().sendMessage("You are missing " + amunnution.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, head.getId())).contains(head.getId(), head.getAmount()))) {
			p.getEquipment().set(Equipment.HEAD_SLOT, head);
			p.getBank(Bank.getTabForItem(p, head.getId())).delete(head);

		} else {
			p.getPacketSender().sendMessage("You are missing " + head.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, cape.getId())).contains(cape.getId(), cape.getAmount()))) {
			p.getEquipment().set(Equipment.CAPE_SLOT, cape);
			p.getBank(Bank.getTabForItem(p, cape.getId())).delete(cape);
		} else {
			p.getPacketSender().sendMessage("You are missing " + cape.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, amulet.getId())).contains(amulet.getId(), amulet.getAmount()))) {
			p.getEquipment().set(Equipment.AMULET_SLOT, amulet);
			p.getBank(Bank.getTabForItem(p, amulet.getId())).delete(amulet);
		} else {
			p.getPacketSender().sendMessage("You are missing " + amulet.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, weapon.getId())).contains(weapon.getId(), weapon.getAmount()))) {
			p.getEquipment().set(Equipment.WEAPON_SLOT, weapon);
			p.getBank(Bank.getTabForItem(p, weapon.getId())).delete(weapon);
		} else {
			p.getPacketSender().sendMessage("You are missing " + weapon.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, body.getId())).contains(body.getId(), body.getAmount()))) {
			p.getEquipment().set(Equipment.BODY_SLOT, body);
			p.getBank(Bank.getTabForItem(p, body.getId())).delete(body);
		} else {
			p.getPacketSender().sendMessage("You are missing " + body.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, shield.getId())).contains(shield.getId(), shield.getAmount()))) {
			p.getEquipment().set(Equipment.SHIELD_SLOT, shield);
			p.getBank(Bank.getTabForItem(p, shield.getId())).delete(shield);
		} else {
			p.getPacketSender().sendMessage("You are missing " + shield.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, leg.getId())).contains(leg.getId(), leg.getAmount()))) {
			p.getEquipment().set(Equipment.LEG_SLOT, leg);
			p.getBank(Bank.getTabForItem(p, leg.getId())).delete(leg);
		} else {
			p.getPacketSender().sendMessage("You are missing " + leg.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, hands.getId())).contains(hands.getId(), hands.getAmount()))) {
			p.getEquipment().set(Equipment.HANDS_SLOT, hands);
			p.getBank(Bank.getTabForItem(p, hands.getId())).delete(hands);
		} else {
			p.getPacketSender().sendMessage("You are missing " + hands.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, feet.getId())).contains(feet.getId(), feet.getAmount()))) {
			p.getEquipment().set(Equipment.FEET_SLOT, feet);
			p.getBank(Bank.getTabForItem(p, feet.getId())).delete(feet);
		} else {
			p.getPacketSender().sendMessage("You are missing " + feet.getDefinition().getName());
		}
		if ((p.getBank(Bank.getTabForItem(p, ring.getId())).contains(ring.getId(), ring.getAmount()))) {
			p.getEquipment().set(Equipment.RING_SLOT, ring);
			p.getBank(Bank.getTabForItem(p, ring.getId())).delete(ring);
		} else {
			p.getPacketSender().sendMessage("You are missing " + ring.getDefinition().getName());
		}

		if (!Player.bankPreset) {
			Presets.start(p);
		}
		Player.bankPreset = false;

		refresh(p);
		p.getClickDelay().reset();

	}

	public static void savingInv(Player p) {
		if (p.getSpellbook() == MagicSpellbook.ANCIENT) {
			p.setMageBook2(3);
		} else if (p.getSpellbook() == MagicSpellbook.LUNAR) {
			p.setMageBook2(2);
		} else if (p.getSpellbook() == MagicSpellbook.NORMAL) {
			p.setMageBook2(1);
		}
		if (p.getPrayerbook() == Prayerbook.NORMAL) {
			p.setPrayerBook2(1);
		} else if (p.getPrayerbook() == Prayerbook.CURSES) {
			p.setPrayerBook2(2);
		}

		Item inv0 = p.getInventory().forSlot(0);
		Item inv1 = p.getInventory().forSlot(1);
		Item inv2 = p.getInventory().forSlot(2);
		Item inv3 = p.getInventory().forSlot(3);
		Item inv4 = p.getInventory().forSlot(4);
		Item inv5 = p.getInventory().forSlot(5);
		Item inv6 = p.getInventory().forSlot(6);
		Item inv7 = p.getInventory().forSlot(7);
		Item inv8 = p.getInventory().forSlot(8);
		Item inv9 = p.getInventory().forSlot(9);
		Item inv10 = p.getInventory().forSlot(10);
		Item inv11 = p.getInventory().forSlot(11);
		Item inv12 = p.getInventory().forSlot(12);
		Item inv13 = p.getInventory().forSlot(13);
		Item inv14 = p.getInventory().forSlot(14);
		Item inv15 = p.getInventory().forSlot(15);
		Item inv16 = p.getInventory().forSlot(16);
		Item inv17 = p.getInventory().forSlot(17);
		Item inv18 = p.getInventory().forSlot(18);
		Item inv19 = p.getInventory().forSlot(19);
		Item inv20 = p.getInventory().forSlot(20);
		Item inv21 = p.getInventory().forSlot(21);
		Item inv22 = p.getInventory().forSlot(22);
		Item inv23 = p.getInventory().forSlot(23);
		Item inv24 = p.getInventory().forSlot(24);
		Item inv25 = p.getInventory().forSlot(25);
		Item inv26 = p.getInventory().forSlot(26);
		Item inv27 = p.getInventory().forSlot(27);

		p.presetItems2 = new int[] { inv0.getId(), inv0.getAmount(), inv1.getId(), inv1.getAmount(), inv2.getId(),
				inv2.getAmount(), inv3.getId(), inv3.getAmount(), inv4.getId(), inv4.getAmount(), inv5.getId(),
				inv5.getAmount(), inv6.getId(), inv6.getAmount(), inv7.getId(), inv7.getAmount(), inv8.getId(),
				inv8.getAmount(), inv9.getId(), inv9.getAmount(), inv10.getId(), inv10.getAmount(), inv11.getId(),
				inv11.getAmount(), inv12.getId(), inv12.getAmount(), inv13.getId(), inv13.getAmount(), inv14.getId(),
				inv14.getAmount(), inv15.getId(), inv15.getAmount(), inv16.getId(), inv16.getAmount(), inv17.getId(),
				inv17.getAmount(), inv18.getId(), inv18.getAmount(), inv19.getId(), inv19.getAmount(), inv20.getId(),
				inv20.getAmount(), inv21.getId(), inv21.getAmount(), inv22.getId(), inv22.getAmount(), inv23.getId(),
				inv23.getAmount(), inv24.getId(), inv24.getAmount(), inv25.getId(), inv25.getAmount(), inv26.getId(),
				inv26.getAmount(), inv27.getId(), inv27.getAmount() };
		Player.preset2 = true;
	}

	public static void savingGear(Player p) {

		Item inv0 = p.getEquipment().forSlot(0);
		Item inv1 = p.getEquipment().forSlot(1);
		Item inv2 = p.getEquipment().forSlot(2);
		Item inv3 = p.getEquipment().forSlot(3);
		Item inv4 = p.getEquipment().forSlot(4);
		Item inv5 = p.getEquipment().forSlot(5);
		Item inv6 = p.getEquipment().forSlot(6);
		Item inv7 = p.getEquipment().forSlot(7);
		Item inv8 = p.getEquipment().forSlot(8);
		Item inv9 = p.getEquipment().forSlot(9);
		Item inv10 = p.getEquipment().forSlot(10);
		Item inv11 = p.getEquipment().forSlot(11);
		Item inv12 = p.getEquipment().forSlot(12);
		Item inv13 = p.getEquipment().forSlot(13);

		p.presetGear2 = new int[] { inv0.getId(), inv0.getAmount(), inv1.getId(), inv1.getAmount(), inv2.getId(),
				inv2.getAmount(), inv3.getId(), inv3.getAmount(), inv4.getId(), inv4.getAmount(), inv5.getId(),
				inv5.getAmount(), inv6.getId(), inv6.getAmount(), inv7.getId(), inv7.getAmount(), inv8.getId(),
				inv8.getAmount(), inv9.getId(), inv9.getAmount(), inv10.getId(), inv10.getAmount(), inv11.getId(),
				inv11.getAmount(), inv12.getId(), inv12.getAmount(), inv13.getId(), inv13.getAmount() };
		Player.preset2 = true;
	}

	public static void refresh(Player p) {

		p.getEquipment().refreshItems();
		p.getUpdateFlag().flag(Flag.APPEARANCE);
		EquipPacketListener.resetWeapon(p);
		//BonusManager.update(p);
	}

}
