package com.zamron.world.content;

import java.util.ArrayList;

import com.zamron.model.Item;
import com.zamron.world.entity.impl.player.Player;

public class Dissolving {

	private Player player;

	public Dissolving(Player player) {
		this.player = player;
	}

	static int anim = 11904;
	static int orb = 5022;

	enum DissolvingData {

		// T1 ZONE
		T1_HELM(3313, new Item[] { new Item(orb, 50) }, 150, anim);
		/*T1_BODY(1115, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_LEGS(1067, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_STAFF(5157, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_WEAPON(21030, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_BOW(20695, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_BOOTS(21012, new Item[] { new Item(orb, 50) }, 150, anim),
		T1_GLOVES(21011, new Item[] { new Item(orb, 50) }, 150, anim),

		// T2 ZONE
		T2_HELM(3313, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_BODY(14732, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_LEGS(14734, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_STAFF(14377, new Item[] { new Item(orb, 50) }, 150, anim),
		T2WEAPON(14377, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_BOW(14377, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_BOOTS(10865, new Item[] { new Item(orb, 50) }, 150, anim),
		T2_GLOVES(2864, new Item[] { new Item(orb, 50) }, 150, anim);*/

		DissolvingData(int id, Item[] rewards, int experience, int animation) {
			this.id = id;
			this.rewards = rewards;
			this.experience = experience;
			this.animation = animation;
			// this.progressions = progressions;
		}

		private int id, experience, animation;
		private Item[] rewards;
		// private int[][] progressions;

		public int getId() {
			return id;
		}

		public int getExperience() {
			return experience;
		}

		public int getAnimation() {
			return animation;
		}

		public Item[] getRewards() {
			return rewards;
		}
		// public int[][] getProgressions() {
		// return progressions;
		// }

	}

	public ArrayList<Item> getDissolvableItemsInInv() {
		ArrayList<Item> dissolvableItems = new ArrayList<Item>();
		for (Item item : player.getInventory().getItems()) {
			if (isDissolvable(item.getId()))
				dissolvableItems.add(item);
		}
		return dissolvableItems;
	}

	public int getTotalOrbAmount(ArrayList<Item> allItems) {
		int totalOrbAmount = 0;
		for (Item item : allItems) {
			for (DissolvingData data : DissolvingData.values()) {
				if (data.getId() == item.getId())
					totalOrbAmount += (data.getRewards()[0].getAmount() * item.getAmount());
			}
		}
		return totalOrbAmount;
	}

	private boolean isDissolvable(int id) {
		for (DissolvingData data : DissolvingData.values()) {
			if (data.getId() == id)
				return true;
		}
		return false;
	}
}

/*
 * private void handleProgressions(int[][] progressions) { for(int[] progression
 * : progressions) {
 * player.getProgressionManager().getProgressionTask(progression[0]).
 * incrementProgress(progression[1], progression[2]); } }
 * 
 * }
 */
