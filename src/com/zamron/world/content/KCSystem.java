package com.zamron.world.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zamron.model.Locations;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class KCSystem {

	private Player player;

	public KCSystem(Player player) {
		this.player = player;
	}

	public enum NpcData {

		LUCARIO(3263, new int[][]{{17, 50}}),
		HADES(15, new int[][]{{3263, 50}}),
		DEFENDERS(9994, new int[][]{{15, 100}}),
		GODZILLA(9932, new int[][]{{9994, 150}}),
		DEMONIC_OLM(224, new int[][]{{9932, 175}}),
		CERBERUS(1999, new int[][]{{224, 200}}),
		ZEUS(16, new int[][]{{1999, 225}}),
		INFARTICO(9993, new int[][]{{16, 260}}),
		LORDVALOR(9277, new int[][]{{9993, 350}}),
		STORM_BREAKER(33, new int[][]{{9277, 450}}),
		DZANTH(9273, new int[][]{{33, 550}}),
		KINGKONG(9903, new int[][]{{9273, 750}}),
		CORPBEAST(8133, new int[][]{{9903, 750}}),
		LUCID_WARRIORS(9247, new int[][]{{8133, 750}}),
		HULK(8493, new int[][]{{9247, 750}}),
		DARKBLUE_WIZARDS(9203, new int[][]{{8493, 750}}),
		HEATED_PYROS(172, new int[][]{{9203, 750}}),
		PURPLE_WYRM(9935, new int[][]{{172, 1000}}),
		TRINITY(170, new int[][]{{9935, 1000}}),
		CLOUD(169, new int[][]{{170, 1000}}),
		HERBAL_ROUGE(219, new int[][]{{169, 1000}}),
		EXODEN(12239, new int[][]{{219, 1000}}),
		SUPREME_NEX(3154, new int[][]{{12239, 1000}}),
		APOLLO_RANGER(1684, new int[][]{{3154, 550}}),
		NOXIOUS_TROLL(5957, new int[][]{{1684, 550}}),
		AZAZEL_BEAST(5958, new int[][]{{5957, 550}}),
		RAVANA(5959, new int[][]{{5958, 550}}),
		WARRIORS(185, new int[][]{{5959, 550}}),
		WARR(6311, new int[][]{{185, 550}}),
		SUPREME_BOX(192, new int[][]{{197, 1000}, {191, 1000}}),
		VADER(11, new int[][]{{1069, 250}}),
		EXTREME_BOX(191, new int[][]{{197, 1000}});

		NpcData(int npcId, int[][] killRequirements) {
			this.id = npcId;
			this.kcReqs = killRequirements;
		}

		private int id;
		private int[][] kcReqs;

	}

	public int[][] getData(int id) {

		for (NpcData data : NpcData.values()) {
			if (data.id == id) {
				return data.kcReqs;
			}
		}

		return null;
	}

	Map<Integer, Integer> kcMap = new HashMap<>();

	public boolean meetsRequirements(int[][] npcData) {
		if (player.getLocation() == Locations.Location.INSTANCE_ARENA) //Remove KC in the instance arena
			return true;
		if (npcData == null) {
			return true;
		}
		for (int[] data : npcData) {
			kcMap.put(data[0], data[1]);
		}
		boolean meetsReqs = kcMap.entrySet().stream()
				.allMatch(npc -> player.getNpcKillCount(npc.getKey()) >= npc.getValue());
		if (meetsReqs || player.isInRaid()) {
			return true;
		}
		return false;
	}

	//Need a break; to stop the loop

	public void sendRequirementsMessage() {

			List<String> messages = new ArrayList<>();
			for (Map.Entry<Integer, Integer> kcData : kcMap.entrySet()) {
				String name = NpcDefinition.forId(kcData.getKey()).getName();
				int amount = kcData.getValue();
				int killedAmount = player.getNpcKillCount(kcData.getKey());
				messages.add("Requirements needed: @red@" + amount + " @blu@of @red@" + name + "@blu@ - Killed: @red@" + killedAmount);
			}
			for (String reqs : messages) {
				player.sendMessage(reqs);
				kcMap.clear();
				break;
			}
		}
	}
