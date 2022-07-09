package com.zamron.world.content.invansionminigame;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerLoading;

public class Testing {

	public static void main(String[] args) {

		Map<Player, Integer> kcMap = new HashMap<>();
		for (File file : new File("data/saves/characters/").listFiles()) {
			Player player = new Player(null);
			player.setUsername(file.getName().substring(0, file.getName().length() - 5));

			PlayerLoading.getResult(player, true);

			kcMap.put(player, RandomUtility.inclusiveRandom(0, 100));

		}

		final Map<Player, Integer> top3 = kcMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		for (Map.Entry<Player, Integer> map : top3.entrySet()) {
			//System.out.println("Key: " + map.getKey().getUsername() + " | Value: " + map.getValue());
		}

	}

}
