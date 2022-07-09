package com.zamron.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatBuilder.CombatDamageCache;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class TheMay extends NPC {
	public static Item[] COMMONLOOT = { new Item(10835, 25), new Item(2572, 1), new Item(19138, 1), new Item(19139, 1), new Item(5130, 1), new Item(6199, 1),new Item(19132, 1),new Item(19131, 1),new Item(19133, 1),new Item(19722, 1),new Item(19721, 1),
			new Item(19468, 1), new Item(10835, 25),new Item(15418, 1), new Item(19723, 2),
			new Item(18940, 1), new Item(10835, 25),new Item(18941, 1), new Item(18942, 2),
			new Item(4772, 1), new Item(4771, 1),new Item(4770, 1) };

	public static Item[] RARELOOT = { new Item(7630, 1) ,new Item(5131, 1),new Item(4770, 1),new Item(4771, 1),new Item(4772, 1),new Item(5131, 1),new Item(15012,1), new Item(4799, 1),new Item(4800, 1), new Item(4801, 1), new Item(5131, 1), new Item(3988, 1), new Item(10835, 100), new Item(3973, 1)};

	public static Item[] SUPERRARELOOT = { new Item(16429, 1), new Item(19886, 1), };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 2005;
	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final TheMayLocation[] LOCATIONS = { new TheMayLocation(2848, 3034, 0, " (::may)") };

	/**
	 * 
	 */
	private static TheMay current;

	/**
	 * 
	 * @param position
	 */
	public TheMay(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(4500, false) { // 4500

			
			
			@Override
			public void execute() {
				spawn();
			}

		});

	}

	/**
	 * 
	 */
	public static void spawn() {

		if (getCurrent() != null) {
			return;
		}

		TheMayLocation location = Misc.randomElement(LOCATIONS);
		TheMay instance = new TheMay(location.copy());

		// //System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");

		World.sendMessageNonDiscord("<shad=1><img=12>@red@ Skeleton May has Respawned" + location.getLocation() + "!");
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {

		setCurrent(null);

		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Integer> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());

		}


		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Integer>> result = sortEntries(killers);
		int count = 0;

		for (Entry<Player, Integer> entry : result) {

			Player killer = entry.getKey();
			int damage = entry.getValue();

			handleDrop(npc, killer, damage);
			NpcGain.WorldBossXP(killer);

			if (++count >= 4) {
				break;
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(100);
		Item common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		Item common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		Item rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		Item superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));

		if (chance >= 98) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessageNonDiscord(
					"<img=12><col=FF0000>" + player.getUsername() + " received " + itemMessage + " from Trainer May");
			DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from Trainer May!");
			return;
		}

		if (chance >= 70) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessageNonDiscord(
					"<img=12><col=FF0000>" + player.getUsername() + " received " + itemMessage + " from Trainer May");
			DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from Trainer May!");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			return;
		}

	}

	/**
	 * 
	 * @param npc
	 * @param player
	 * @param damage
	 */
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {

			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}

		});

		return sortedEntries;

	}

	/**
	 * 
	 * @return
	 */
	public static TheMay getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(TheMay current) {
		TheMay.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class TheMayLocation extends Position {

		/**
		 * 
		 */
		private String location;

		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 * @param location
		 */
		public TheMayLocation(int x, int y, int z, String location) {
			super(x, y, z);
			setLocation(location);
		}

		/**
		 * 
		 * @return
		 */

		String getLocation() {
			return location;
		}

		/**
		 * 
		 * @param location
		 */
		public void setLocation(String location) {
			this.location = location;
		}

	}

}