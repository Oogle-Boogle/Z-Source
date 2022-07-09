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
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class HWeenBoss extends NPC {
	public static Item[] COMMONLOOT = { new Item(1959, 1), new Item(1959, 2),
			new Item(1959, 3) };

	public static Item[] RARELOOT = { new Item(1959,5), new Item(1959,7), new Item(1959,10) };

	public static Item[] SUPERRARELOOT = { new Item(1959,20),new Item(3647), new Item(1959,25), new Item(9925), new Item(13095), new Item(15255), new Item(15257), new Item(15256), new Item(15268), new Item(15258), new Item(1413), new Item(9924),
			new Item(10726), new Item(10724), new Item(10725) };
	
	/**
	 * 
	 */
	public static final int NPC_ID = 8548;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final HWeenBossLocation[] LOCATIONS = { new HWeenBossLocation(2401, 3236, 0, " ( ::hween )") };

	/**
	 * 
	 */
	private static HWeenBoss current;

	/**
	 * 
	 * @param position
	 */
	public HWeenBoss(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task(1500, false) { // 6000

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

		HWeenBossLocation location = Misc.randomElement(LOCATIONS);
		HWeenBoss instance = new HWeenBoss(location.copy());

		// //System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		// System.out.print("spawned.");

		World.sendMessageNonDiscord("<img=392><shad=200><col=000000>[<col=b96900>HALLOWEEN<col=000000>]<col=b96900>Killer Pumpkin has respawned " + location.getLocation() + "");
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

		if (chance >= 99) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(superrare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (superrare.getDefinition().getName());
			Misc.anOrA(itemName);
			World.sendMessageNonDiscord(
					"<img=392><col=000000>" + player.getUsername() + " received<col=b96900>[ " + itemName + "<col=b96900>]<col=000000> from the Killer Pumpkin!");
			return;
		}

		if (chance >= 93) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(rare, pos, player.getUsername(), false, 150, true, 200));
			String itemName = rare.getDefinition().getName();
			Misc.anOrA(itemName);
			World.sendMessageNonDiscord(
					"<img=392><col=000000>" + player.getUsername() + " received<col=b96900>[ " + itemName + "<col=b96900>]<col=000000> from the Killer Pumpkin!");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(common, pos, player.getUsername(), false, 150, true, 200));
			String itemName = (common.getDefinition().getName());
			World.sendMessageNonDiscord(
					"<img=392><col=000000>" + player.getUsername() + " received<col=b96900>[ " + itemName + "<col=b96900>]<col=000000> from the Killer Pumpkin!");
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
	public static HWeenBoss getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(HWeenBoss current) {
		HWeenBoss.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class HWeenBossLocation extends Position {

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
		public HWeenBossLocation(int x, int y, int z, String location) {
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
