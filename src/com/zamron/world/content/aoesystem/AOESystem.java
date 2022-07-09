package com.zamron.world.content.aoesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.zamron.model.CombatIcon;
import com.zamron.model.container.impl.Equipment;
import com.zamron.world.entity.impl.player.Player;

public class AOESystem {

	public static List<AOEWeaponData> weaponData = new ArrayList<>();

	public void parseData() { // look this parses the data from the file, thats why it was never working, forgot that cuz its midnight and im tired:

		Path filePath = Paths.get("data", "" +
				"AOEWeapons.txt");

		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> {
				String[] wepData = line.split(" ");
				////System.out.println(Arrays.toString(wepData));
				weaponData.add(new AOEWeaponData(parseInt(wepData[0]), parseInt(wepData[1]), parseInt(wepData[2]),
						parseInt(wepData[3]), getIcon(wepData[4])));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public AOEWeaponData getAOEData(int id) {

		int index = -1;

		for (int i = 0; i < weaponData.size(); i++) {
			if (weaponData.get(i).getId() == id) {
				index = i;
				break;
			}
		}

		return index > -1 ? weaponData.get(index) : null;
	}

	public static boolean hasAoeWeapon(Player player){
		for (int i = 0; i < weaponData.size(); i++) {
			if (weaponData.get(i).getId() == player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
				return true;
			}
		}
		return false;
	}

	private CombatIcon getIcon(String str) {
		CombatIcon icon = null;

		if (str.equalsIgnoreCase("Range")) {
			icon = CombatIcon.RANGED;
		} else if (str.equalsIgnoreCase("Melee")) {
			icon = CombatIcon.MELEE;
		} else {
			icon = CombatIcon.MAGIC;
		}

		return icon;
	}

	private int parseInt(String str) {
		return Integer.parseInt(str);
	}

	private static AOESystem SINGLETON = null;

	public static AOESystem getSingleton() {
		if (SINGLETON == null) {
			SINGLETON = new AOESystem();
		}
		return SINGLETON;
	}

}
