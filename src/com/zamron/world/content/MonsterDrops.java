package com.zamron.world.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NPCDrops.DropChance;
import com.zamron.model.definitions.NPCDrops.WellChance;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

/**
 * @Author Levi Patton
 * @rune-server.org/members/AuguryPS
 * 
 * @author Tyrant (tyrant.inquiries@gmail.com) -> Fixes
 *
 */
public class MonsterDrops {
	/**
	 * A method that initializes and fills in the cache of the drops. What this
	 * does is go by every {@link NPCDrops} and attaches it to its relevant npc
	 * name so a player can view the drop table for that name.
	 */
	public static void initialize() {
		try {
			NPCDrops.getDrops().entrySet().stream().filter(Objects::nonNull)
					.filter($d -> $d.getKey() > 0 && NpcDefinition.forId($d.getKey()) != null).forEach($d -> {
						npcDrops.put(NpcDefinition.forId($d.getKey()).getName().toLowerCase(), $d.getValue());
//						//System.out.println("Added: " + $d.getValue().toString() + " " + $d.getKey());
					});
			////System.out.println("MonsterDrops has been initialized: size " + npcDrops.size());
		} catch (Exception i) {
			i.printStackTrace();
		}
	}

	public static Map<String, NPCDrops> npcDrops = new HashMap<>();

	public static void sendNpcDrop(Player player, int id, String name) {
		// NPCDrops drops = NPCDrops.forId(id);
		NPCDrops drops = npcDrops.get(name);
		// NPCDrops drops = NPCDrops.forId(NpcDefinition.forName(name).getId());
		if (drops == null) {
			player.sendMessage(
					"No drop table found for " + name + " " + id + " " + NpcDefinition.forName(name).getId());
			return;
		}
		for (int i = 29081; i < 29091; i++) {
			player.getPA().sendFrame126(i, "");
		}

		int line = 29081;

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}

			final DropChance dropChance = drops.getDropList()[i].getChance();
			final WellChance wellChance = drops.getDropList()[i].getWellChance();

			if (dropChance == DropChance.ALWAYS) {
				player.getPA().sendFrame126(line,
						ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"
								+ Misc.format(drops.getDropList()[i].getItem().getAmount()));
				line++;
			}
			if (wellChance == WellChance.ALWAYS) {
				player.getPA().sendFrame126(line,
						ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName() + " x"
								+ Misc.format(drops.getDropList()[i].getItem().getAmount()));
				line++;
			}
		}
		if (line == 29081) {
			player.getPA().sendFrame126(28901 + 4, "\\n\\n\\n\\n\\nNo 100% drops");
		}
		line++;

		ArrayList<String> added = new ArrayList<>();
		line = 28922;
		for (int i = 28922; i < 28962; i++) {
			player.getPA().sendFrame126(i, "");
		}

		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance.ordinal() > DropChance.ALWAYS.ordinal()
					&& dropChance.ordinal() <= DropChance.NOTTHATRARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line,
							itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
					;
					line++;
				}
			}
			final WellChance wellChance = drops.getDropList()[i].getWellChance();
			if (wellChance.ordinal() > WellChance.ALWAYS.ordinal()
					&& wellChance.ordinal() <= WellChance.NOTTHATRARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line,
							itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
					;
					line++;
				}
			}
		}
		player.getPA().sendFrame126(28901 + 5, "Regular Drops: " + added.size());
		added.clear();

		for (int i = 29002; i < 29042; i++) {
			player.getPA().sendFrame126(i, "");
		}
		line = 29002;
		for (int i = 0; i < drops.getDropList().length; i++) {
			if (drops.getDropList()[i].getItem().getId() <= 0
					|| drops.getDropList()[i].getItem().getId() > ItemDefinition.getMaxAmountOfItems()
					|| drops.getDropList()[i].getItem().getAmount() <= 0) {
				continue;
			}
			final DropChance dropChance = drops.getDropList()[i].getChance();
			if (dropChance.ordinal() >= DropChance.RARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line,
							itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
					;
					line++;
				}
			}
			final WellChance wellChance = drops.getDropList()[i].getWellChance();
			if (wellChance.ordinal() >= WellChance.RARE.ordinal()) {
				String itemName = ItemDefinition.forId(drops.getDropList()[i].getItem().getId()).getName();
				if (!added.contains(itemName)) {
					added.add(itemName);
					player.getPA().sendFrame126(line,
							itemName + " x" + Misc.format(drops.getDropList()[i].getItem().getAmount()));
					;
					line++;
				}
			}
		}
		player.getPA().sendFrame126(28901 + 6, "Rare Drops: " + added.size());

		player.getPA().sendFrame126(28901 + 7, "");
		player.getPA().sendFrame126(28901 + 3, Misc.formatPlayerName(name));
		player.getPA().sendInterface(28901);
	}
}
