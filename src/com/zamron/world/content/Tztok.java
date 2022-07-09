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

public class Tztok extends NPC {

	public static int[] COMMONLOOT = { 2572,19137,19138,19139,6041,5130,
			18865,19132,19131,19721,19132,19131,19133,18940,18941,18942,19721,19722,19723,18892,15418,19468};
	public static int[] RARELOOT = { 5131,4771,4772,4770,5131,4771,4772,4770, 4799 ,4800,4801,5079,3973,3951,15012
			,5131,4770,4771,4772,3988, 7630};
	public static int[] SUPERRARELOOT = {11978,19140,19886,6320,4058,4057,4056,4059,19890,12162, 19938, 19937, 19935, 19936 };

	/**
	 * 
	 */
	public static final int NPC_ID = 2745;

	/**
	 * add your maps to that folder open me your client.java in client
	 */
	public static final TztokLocation[] LOCATIONS = { new TztokLocation(2412, 4670, 0, "<col=0999ad> <img=12> ::wb ") };

	/**
	 * 
	 */
	private static Tztok current;

	/**
	 * 
	 * @param position
	 */
	public Tztok(Position position) {

		super(NPC_ID, position);
	}

	/**
	 * 
	 */
	public static void initialize() {

		TaskManager.submit(new Task( 10080, false) { // 6000

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

		TztokLocation location = Misc.randomElement(LOCATIONS);
		Tztok instance = new Tztok(location.copy());

		// //System.out.println(instance.getPosition());

		World.register(instance);
		setCurrent(instance);
		System.out.print("World boss spawned.");

		World.sendMessageDiscord("<img=12><col=bababa>[<col=0999ad><shad=200>WORLD BOSS<col=bababa>]<col=0999ad>The World Boss has Respawned at <col=00a745>" + location.getLocation() + "");
	}

	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {
		World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

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
			if (damage >= 10000) {
				handleDrop(npc, killer, damage);
			NpcGain.WorldBossXP(killer);

			if (++count >= 10) {
				break;
			}
			}

		}

	}

	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(300);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		@SuppressWarnings("unused")
		int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));

		if (chance >= 290) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessageNonDiscord(
					"<img=12><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=12>[ " + itemMessage + "<col=eaeaea>]<img=12><col=FF0000>from the World Boss!");
			DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from the World Boss!");
			return;
		}

		if (chance >= 185) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessageNonDiscord(
					"<img=12><col=FF0000>" + player.getUsername() + " received<img=12><col=eaeaea>[ " + itemMessage + "<col=eaeaea>]<img=12><col=FF0000> from the World Boss!");
			DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from the World Boss!");
			return;
		}
		if (chance >= 0) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
			String itemName = (new Item(common).getDefinition().getName());
			World.sendMessageNonDiscord(
					"<img=12><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=12>[<col=07b481> " + itemName + "<col=eaeaea>]<img=12><col=FF0000> from the World Boss!");
			DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemName + " from the World Boss!");
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
	public static Tztok getCurrent() {
		return current;
	}

	/**
	 * 
	 * @param current
	 */
	public static void setCurrent(Tztok current) {
		Tztok.current = current;
	}

	/**
	 * 
	 * @author Levi <levi.patton69 @ skype>
	 *
	 */
	public static class TztokLocation extends Position {

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
		public TztokLocation(int x, int y, int z, String location) {
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