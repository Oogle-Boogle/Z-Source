package com.zamron.world.content.starterprogression;

import com.zamron.model.Item;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class StarterProgression {

	private final int STARTING_POINT = 42001;

	private static final Item[][] REWARDS = { 
			{ new Item(10835, 100), new Item(15373, 5), new Item(2572, 1) },
			{ new Item(10835, 200), new Item(15373, 5), new Item(774, 1) },
			{ new Item(10835, 300), new Item(15373, 5), new Item(3824, 2) },
			{ new Item(10835, 400), new Item(3459, 1), new Item(3082, 1) },
			{ new Item(10835, 500), new Item(3459, 1), new Item(14006, 1) },
			{ new Item(10835, 5000), new Item(14691, 1), new Item(3928, 1) },
			// indexed rewards, so first column = first task reward
	};

	private static Map<Integer, NPCRequirement> tasks = new HashMap<>();

	public static void loadTasks() {
		tasks.put(0, new NPCRequirement(4455, 10)); // task index, npc id, npc amount
		tasks.put(1, new NPCRequirement(4457, 25));
		tasks.put(2, new NPCRequirement(4459, 50));
		tasks.put(3, new NPCRequirement(4456, 75));
		tasks.put(4, new NPCRequirement(4462, 100));
		tasks.put(5, new NPCRequirement(4409, 150));

	}

	private final Player player;

	public StarterProgression(Player player) {
		this.player = player;
	}

	public void openInterface() {
		int index = player.getCurrentStarterProgression();
		if (index == -1) {
			return;
		}
		player.getPacketSender().sendWalkableInterface(STARTING_POINT, true);
		updateInterface();
	}

	public void closeInterface() {
		player.getPacketSender().sendWalkableInterface(STARTING_POINT, false);
	}

	private void updateInterface() {
		int index = player.getCurrentStarterProgression();
		if (index == -1) {
			return; 
		}

		NPCRequirement requirement = tasks.get(index);
		NpcDefinition npcDefinition = NpcDefinition.forId(requirement.getId());
		if (npcDefinition != null && npcDefinition.getName() != null) {
			String name = NpcDefinition.forId(requirement.getId()).getName();
			player.getPacketSender().sendString(STARTING_POINT + 3, name);
		} else {
			//System.out.println("Npcdefinition was null for npc id " + requirement.getId() + " | Task index = " + index);
			//System.out.println(npcDefinition);
			//System.out.println(npcDefinition.getName());
		}
		Item[] rewards = REWARDS[index];
		player.getPacketSender().sendItemArrayOnInterface(STARTING_POINT + 4, rewards);
		int kc = player.getNpcKillCount(requirement.getId());
		int required = requirement.getAmount();
		int percentage = getPercentage(kc, required);
		player.getPacketSender().updateProgressBar(STARTING_POINT + 5, percentage);
		player.getPacketSender().sendString(STARTING_POINT + 6, percentage + "% (" + kc + "/" + required + ")");
	}

	private int getPercentage(int n, int total) {
		float proportion = ((float) n) / ((float) total);
		return (int) (proportion * 100f);
	}

	public void handleKill(int npcId) {
		int index = player.getCurrentStarterProgression();
		if (index == -1) { // all finished
			return;
		}
		NPCRequirement requirement = tasks.get(index);
		if (requirement == null) {
			player.getPacketSender().sendWalkableInterface(STARTING_POINT, false); // should fix it.
			return;
		}
		if (requirement.getId() != npcId) {
			return;
		}
		if (player.getNpcKillCount(npcId) >= requirement.getAmount()) {
			player.setStarterProgressionCompleted();
			player.getInventory().addItemSet(REWARDS[index]);
			player.sendMessage("Task kill " + requirement.getAmount() + " " + NpcDefinition.forId(npcId).getName()
					+ "'s has been completed");
			if (player.getCurrentStarterProgression() == -1) {
				player.getPacketSender().sendWalkableInterface(STARTING_POINT, false);
			}
		}
		updateInterface();
	}

	public void handleKilledNPC(NPC npc) {
		// TODO Auto-generated method stub
		
	}
}