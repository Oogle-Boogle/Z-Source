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

public class Presets {

	public static void load(Player p) {

		if (!p.getClickDelay().elapsed(5000)) {
			p.getPacketSender().sendMessage("You must wait 5 seconds to load another preset.");
			return;
		}
		if (p.presetItems == null){
			return;
		}
		if (!Player.preset) {
			p.getPacketSender().sendMessage("You don't have a loadout in preset 1.");
			return;
		}
		p.getPacketSender().sendMessage("You have loaded Preset 1.");

		p.getClickDelay().reset();

		p.getSkillManager().stopSkilling();
		Bank.depositItems(p, p.getEquipment(), true);
		Bank.depositItems(p, p.getInventory(), true);

		if (p.getMageBook() == 3) {
			p.setSpellbook(MagicSpellbook.ANCIENT);
		} else if (p.getMageBook() == 2) {
			p.setSpellbook(MagicSpellbook.LUNAR);
		} else if (p.getMageBook() == 1) {
			p.setSpellbook(MagicSpellbook.NORMAL);
		}
		if (p.getPrayerBook() == 1) {
			p.setPrayerbook(Prayerbook.NORMAL);
		} else if (p.getPrayerBook() == 2) {
			p.setPrayerbook(Prayerbook.CURSES);
		}

		p.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, p.getSpellbook().getInterfaceId());
		Autocasting.resetAutocast(p, true);

		for (int j = 0; j < 56; j += 2) {
			Item inv = new Item(p.presetItems[j], p.presetItems[j + 1]);
			int tab = Bank.getTabForItem(p, p.presetItems[j]);
			if (p.getBank(tab).contains(inv.getId(), inv.getAmount())) {
				p.getBank(tab).delete(p.presetItems[j], p.presetItems[j + 1]);
				p.getInventory().add(p.presetItems[j], p.presetItems[j + 1]);
			} else {
				p.getPacketSender().sendMessage("You are missing " + inv.getDefinition().getName());
			}

		}
		Item head = new Item(p.presetGear[0], p.presetGear[1]);
		Item cape = new Item(p.presetGear[2], p.presetGear[3]);
		Item amulet = new Item(p.presetGear[4], p.presetGear[5]);
		Item weapon = new Item(p.presetGear[6], p.presetGear[7]);
		Item body = new Item(p.presetGear[8], p.presetGear[9]);
		Item shield = new Item(p.presetGear[10], p.presetGear[11]);
		Item leg = new Item(p.presetGear[14], p.presetGear[15]);
		Item hands = new Item(p.presetGear[18], p.presetGear[19]);
		Item feet = new Item(p.presetGear[20], p.presetGear[21]);
		Item ring = new Item(p.presetGear[24], p.presetGear[25]);
		Item amunnution = new Item(p.presetGear[26], p.presetGear[27]);

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
		start(p);
		}
		Player.bankPreset = false;

		refresh(p);
		p.getClickDelay().reset();

	}

	public static void refresh(Player p) {

		p.getEquipment().refreshItems();
		p.getUpdateFlag().flag(Flag.APPEARANCE);
		EquipPacketListener.resetWeapon(p);
	//	BonusManager.update(p);
	}

	public static void savingInv(Player p) {
		if (p.getSpellbook() == MagicSpellbook.ANCIENT) {
			p.setMageBook(3);
		} else if (p.getSpellbook() == MagicSpellbook.LUNAR) {
			p.setMageBook(2);
		} else if (p.getSpellbook() == MagicSpellbook.NORMAL) {
			p.setMageBook(1);
		}
		if (p.getPrayerbook() == Prayerbook.NORMAL) {
			p.setPrayerBook(1);
		} else if (p.getPrayerbook() == Prayerbook.CURSES) {
			p.setPrayerBook(2);
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


		p.presetItems = new int[] { inv0.getId(), inv0.getAmount(), inv1.getId(), inv1.getAmount(), inv2.getId(),
				inv2.getAmount(), inv3.getId(), inv3.getAmount(), inv4.getId(), inv4.getAmount(), inv5.getId(),
				inv5.getAmount(), inv6.getId(), inv6.getAmount(), inv7.getId(), inv7.getAmount(), inv8.getId(),
				inv8.getAmount(), inv9.getId(), inv9.getAmount(), inv10.getId(), inv10.getAmount(), inv11.getId(),
				inv11.getAmount(), inv12.getId(), inv12.getAmount(), inv13.getId(), inv13.getAmount(), inv14.getId(),
				inv14.getAmount(), inv15.getId(), inv15.getAmount(), inv16.getId(), inv16.getAmount(), inv17.getId(),
				inv17.getAmount(), inv18.getId(), inv18.getAmount(), inv19.getId(), inv19.getAmount(), inv20.getId(),
				inv20.getAmount(), inv21.getId(), inv21.getAmount(), inv22.getId(), inv22.getAmount(), inv23.getId(),
				inv23.getAmount(), inv24.getId(), inv24.getAmount(), inv25.getId(), inv25.getAmount(), inv26.getId(),
				inv26.getAmount(), inv27.getId(), inv27.getAmount() };
		
		Player.preset = true;		

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

		p.presetGear = new int[] { inv0.getId(), inv0.getAmount(), inv1.getId(), inv1.getAmount(), inv2.getId(),
				inv2.getAmount(), inv3.getId(), inv3.getAmount(), inv4.getId(), inv4.getAmount(), inv5.getId(),
				inv5.getAmount(), inv6.getId(), inv6.getAmount(), inv7.getId(), inv7.getAmount(), inv8.getId(),
				inv8.getAmount(), inv9.getId(), inv9.getAmount(), inv10.getId(), inv10.getAmount(), inv11.getId(),
				inv11.getAmount(), inv12.getId(), inv12.getAmount(), inv13.getId(), inv13.getAmount() };
		Player.preset = true;

	}

	public static void start(Player p) {

		Item inv0 = p.getInventory().forSlot(0);
		int item0 = inv0.getId();
		int amount0 = inv0.getAmount();

		Item inv1 = p.getInventory().forSlot(1);
		int item1 = inv1.getId();
		int amount1 = inv1.getAmount();

		Item inv2 = p.getInventory().forSlot(2);
		int item2 = inv2.getId();
		int amount2 = inv2.getAmount();

		Item inv3 = p.getInventory().forSlot(3);
		int item3 = inv3.getId();
		int amount3 = inv3.getAmount();

		Item inv4 = p.getInventory().forSlot(4);
		int item4 = inv4.getId();
		int amount4 = inv4.getAmount();

		Item inv5 = p.getInventory().forSlot(5);
		int item5 = inv5.getId();
		int amount5 = inv5.getAmount();

		Item inv6 = p.getInventory().forSlot(6);
		int item6 = inv6.getId();
		int amount6 = inv6.getAmount();

		Item inv7 = p.getInventory().forSlot(7);
		int item7 = inv7.getId();
		int amount7 = inv7.getAmount();

		Item inv8 = p.getInventory().forSlot(8);
		int item8 = inv8.getId();
		int amount8 = inv8.getAmount();

		Item inv9 = p.getInventory().forSlot(9);
		int item9 = inv9.getId();
		int amount9 = inv9.getAmount();

		Item inv10 = p.getInventory().forSlot(10);
		int item10 = inv10.getId();
		int amount10 = inv10.getAmount();

		Item inv11 = p.getInventory().forSlot(11);
		int item11 = inv11.getId();
		int amount11 = inv11.getAmount();

		Item inv12 = p.getInventory().forSlot(12);
		int item12 = inv12.getId();
		int amount12 = inv12.getAmount();

		Item inv13 = p.getInventory().forSlot(13);
		int item13 = inv13.getId();
		int amount13 = inv13.getAmount();

		Item inv14 = p.getInventory().forSlot(14);
		int item14 = inv14.getId();
		int amount14 = inv14.getAmount();

		Item inv15 = p.getInventory().forSlot(15);
		int item15 = inv15.getId();
		int amount15 = inv15.getAmount();

		Item inv16 = p.getInventory().forSlot(16);
		int item16 = inv16.getId();
		int amount16 = inv16.getAmount();

		Item inv17 = p.getInventory().forSlot(17);
		int item17 = inv17.getId();
		int amount17 = inv17.getAmount();

		Item inv18 = p.getInventory().forSlot(18);
		int item18 = inv18.getId();
		int amount18 = inv18.getAmount();

		Item inv19 = p.getInventory().forSlot(19);
		int item19 = inv19.getId();
		int amount19 = inv19.getAmount();

		Item inv20 = p.getInventory().forSlot(20);
		int item20 = inv20.getId();
		int amount20 = inv20.getAmount();

		Item inv21 = p.getInventory().forSlot(21);
		int item21 = inv21.getId();
		int amount21 = inv21.getAmount();

		Item inv22 = p.getInventory().forSlot(22);
		int item22 = inv22.getId();
		int amount22 = inv22.getAmount();

		Item inv23 = p.getInventory().forSlot(23);
		int item23 = inv23.getId();
		int amount23 = inv23.getAmount();

		Item inv24 = p.getInventory().forSlot(24);
		int item24 = inv24.getId();
		int amount24 = inv24.getAmount();

		Item inv25 = p.getInventory().forSlot(25);
		int item25 = inv25.getId();
		int amount25 = inv25.getAmount();

		Item inv26 = p.getInventory().forSlot(26);
		int item26 = inv26.getId();
		int amount26 = inv26.getAmount();

		Item inv27 = p.getInventory().forSlot(27);
		int item27 = inv27.getId();
		int amount27 = inv27.getAmount();

		Item equip = p.getEquipment().forSlot(0);
		int itemequip = equip.getId();
		int equipamount = equip.getAmount();

		Item equip1 = p.getEquipment().forSlot(1);
		int itemequip1 = equip1.getId();
		int equipamount1 = equip1.getAmount();

		Item equip2 = p.getEquipment().forSlot(2);
		int itemequip2 = equip2.getId();
		int equipamount2 = equip2.getAmount();

		Item equip3 = p.getEquipment().forSlot(3);
		int itemequip3 = equip3.getId();
		int equipamount3 = equip3.getAmount();

		Item equip4 = p.getEquipment().forSlot(4);
		int itemequip4 = equip4.getId();
		int equipamount4 = equip4.getAmount();

		Item equip5 = p.getEquipment().forSlot(5);
		int itemequip5 = equip5.getId();
		int equipamount5 = equip5.getAmount();

		Item equip13 = p.getEquipment().forSlot(13);
		int itemequip13 = equip13.getId();
		int equipamount13 = equip13.getAmount();

		Item equip7 = p.getEquipment().forSlot(7);
		int itemequip7 = equip7.getId();
		int equipamount7 = equip7.getAmount();

		Item equip12 = p.getEquipment().forSlot(10);
		int itemequip12 = equip12.getId();
		int equipamount12 = equip12.getAmount();

		Item equip9 = p.getEquipment().forSlot(12);
		int itemequip9 = equip9.getId();
		int equipamount9 = equip9.getAmount();

		Item equip10 = p.getEquipment().forSlot(9);
		int itemequip10 = equip10.getId();
		int equipamount10 = equip10.getAmount();

		p.getPacketSender().sendInterface(46500);
		p.getPacketSender().sendItemOnInterface(46545, item0, amount0);
		p.getPacketSender().sendItemOnInterface(46546, item1, amount1);
		p.getPacketSender().sendItemOnInterface(46547, item2, amount2);
		p.getPacketSender().sendItemOnInterface(46548, item3, amount3);

		p.getPacketSender().sendItemOnInterface(46549, item4, amount4);
		p.getPacketSender().sendItemOnInterface(46550, item5, amount5);
		p.getPacketSender().sendItemOnInterface(46551, item6, amount6);
		p.getPacketSender().sendItemOnInterface(46552, item7, amount7);

		p.getPacketSender().sendItemOnInterface(46553, item8, amount8);
		p.getPacketSender().sendItemOnInterface(46554, item9, amount9);
		p.getPacketSender().sendItemOnInterface(46555, item10, amount10);
		p.getPacketSender().sendItemOnInterface(46556, item11, amount11);

		p.getPacketSender().sendItemOnInterface(46557, item12, amount12);
		p.getPacketSender().sendItemOnInterface(46558, item13, amount13);
		p.getPacketSender().sendItemOnInterface(46559, item14, amount14);
		p.getPacketSender().sendItemOnInterface(46560, item15, amount15);

		p.getPacketSender().sendItemOnInterface(46561, item16, amount16);
		p.getPacketSender().sendItemOnInterface(46562, item17, amount17);
		p.getPacketSender().sendItemOnInterface(46563, item18, amount18);
		p.getPacketSender().sendItemOnInterface(46564, item19, amount19);

		p.getPacketSender().sendItemOnInterface(46565, item20, amount20);
		p.getPacketSender().sendItemOnInterface(46566, item21, amount21);
		p.getPacketSender().sendItemOnInterface(46567, item22, amount22);
		p.getPacketSender().sendItemOnInterface(46568, item23, amount23);

		p.getPacketSender().sendItemOnInterface(46569, item24, amount24);
		p.getPacketSender().sendItemOnInterface(46570, item25, amount25);
		p.getPacketSender().sendItemOnInterface(46571, item26, amount26);
		p.getPacketSender().sendItemOnInterface(46572, item27, amount27);

		p.getPacketSender().sendItemOnInterface(46573, itemequip, equipamount);
		p.getPacketSender().sendItemOnInterface(46574, itemequip1, equipamount1);
		p.getPacketSender().sendItemOnInterface(46575, itemequip2, equipamount2);
		p.getPacketSender().sendItemOnInterface(46576, itemequip13, equipamount13);

		p.getPacketSender().sendItemOnInterface(46577, itemequip3, equipamount3);
		p.getPacketSender().sendItemOnInterface(46578, itemequip4, equipamount4);
		p.getPacketSender().sendItemOnInterface(46579, itemequip5, equipamount5);
		p.getPacketSender().sendItemOnInterface(46580, itemequip7, equipamount7);

		p.getPacketSender().sendItemOnInterface(46581, itemequip10, equipamount10);
		p.getPacketSender().sendItemOnInterface(46583, itemequip9, equipamount9);
		p.getPacketSender().sendItemOnInterface(46582, itemequip12, equipamount12);

		if (p.getSpellbook() == MagicSpellbook.ANCIENT) {
			p.getPacketSender().sendString(46513, "Ancients");
		} else if (p.getSpellbook() == MagicSpellbook.LUNAR) {
			p.getPacketSender().sendString(46513, "Lunar");
		} else if (p.getSpellbook() == MagicSpellbook.NORMAL) {
			p.getPacketSender().sendString(46513, "Modern");
		}
		if (p.getPrayerbook() == Prayerbook.NORMAL) {
			p.getPacketSender().sendString(46514, "Normal");
		} else if (p.getPrayerbook() == Prayerbook.CURSES) {
			p.getPacketSender().sendString(46514, "Curses");
		}
	}

}
