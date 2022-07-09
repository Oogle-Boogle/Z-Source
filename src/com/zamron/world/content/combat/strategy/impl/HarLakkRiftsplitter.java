package com.zamron.world.content.combat.strategy.impl;

import com.zamron.model.Animation;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatContainer;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.content.combat.strategy.CombatStrategy;
import com.zamron.world.content.combat.CombatBuilder.CombatDamageCache;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author live:nrpker7839
 * adam.trinity
 */

public class HarLakkRiftsplitter implements CombatStrategy {

	/**
	 * Animations
	 */
	public Animation meleeHit = new Animation(14380);
	public Animation hugeMelee = new Animation(14384);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC monster = (NPC) entity;
		if(Locations.goodDistance(monster.getPosition().copy(), victim.getPosition().copy(), 1)){
		if (monster.isChargingAttack() || monster.getConstitution() <= 0) {
			return true;
		}
		switch(Misc.inclusiveRandom(1, 4)) {
			/**
			 * Regular attack
			 */
			case 1:
				monster.performAnimation(meleeHit);
				monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 1, 0, CombatType.MELEE, true));
				break;
			/**
			 * Melee bonus attack
			 */
			case 4:
				monster.performAnimation(hugeMelee);
				monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 2, 1, CombatType.MELEE, true));
				break;
		}
		return true;
	}
		return false;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 1;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	/* handle drops */
	public static int[] COMMONLOOT = {4151, 6739, 4718, 15369, 15370, 18778, 11720, 11696 };
	public static int[] MEDIUMLOOT = {14484, 11694, 15241, 2577, 20012, 18349, 18351, 18353};
	public static int[] RARELOOT = {6199, 15372, 15373};
	public static int[] SUPERRARELOOT = {618, 625, 620, 619, 623};
	
	/**
	 * 
	 * @param npc
	 */
	public static void handleDrop(NPC npc) {


		if(npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Integer> killers = new HashMap<>();

		for(Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if(entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();
			
			if(timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();
			
			if(player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());
			player.sendMessage("damage cache +");
			
		}

		npc.getCombatBuilder().getDamageMap().clear();
		
		List<Entry<Player, Integer>>result = sortEntries(killers);
		int count = 0;
		
		for(Entry<Player, Integer> entry : result) {
			
			Player killer = entry.getKey();
			int damage = entry.getValue();
			
			handleDrop(npc, killer, damage);
			killer.sendMessage("handling drop");
			
			if(++count >= 5) {
				break;
			}
			
		}

	}
	
	private static void handleDrop(NPC npc, Player player, int damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
		player.sendMessage("giving loot");
	}
	
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(10);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		
		//player.getPacketSender().sendMessage("chance: "+chance);
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(995, 7500000 + Misc.getRandom(15000000)), pos, player.getUsername(), false, 150, true, 200));

		if(chance > 9){
			//super rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		if(chance > 7) {
			//rare
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
		if(chance > 4) {
			//medium
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
		if(chance >= 0){
			//common
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
	

		
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
	
}
