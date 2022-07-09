package com.zamron;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zamron.model.definitions.ItemDefinition;

public class DumpBonusesNick {
	public static void main(String[] args) {
		new DumpBonusesNick().run2();
	}

	public void run2() {
		try {
			ItemDefinition.init();
			DataOutputStream out = new DataOutputStream(new FileOutputStream("./ItemDefs.dat", false));

			out.writeShort(ItemDefinition.getMaxAmountOfItems());

			int totalEquipable = 0;
			for (ItemDefinition item : ItemDefinition.getDefinitions()) {
				if (item == null || (item.getEquipmentType() == ItemDefinition.EquipmentType.WEAPON && !item.isWeapon()))
					continue;
				totalEquipable++;
			}

			out.writeShort(totalEquipable);

			for (ItemDefinition item : ItemDefinition.getDefinitions()) {
				if (item == null || (item.getEquipmentType() == ItemDefinition.EquipmentType.WEAPON && !item.isWeapon()))
					continue;
				System.out.println("dumping " + item.getId());

				int[] bonuses = new int[14]; // 5 att, 5 def, 1 pray, 1 str, 1 ranged str, 1 magic dmg
				int index = 0;
				for (int i = 0; i < item.getBonus().length; i++) {
					if (i >= 10 && i <= 13) {
						continue;
					}
					int value = (int) item.getBonus()[i];
					bonuses[index] = value;
					index++;
				}

				out.writeShort(item.getId());
				for (int bonus : bonuses) {
					out.writeInt(bonus);
				}
			}
			out.close();
			System.out.println("Completed the dump");
			System.out.println(ItemDefinition.getMaxAmountOfItems());
			System.out.println(totalEquipable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
