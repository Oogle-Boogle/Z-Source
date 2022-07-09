package com.zamron.world.content.itemcombiner;


import com.zamron.model.Item;
import com.zamron.world.entity.impl.player.Player;

public class Combiner {

	private Player player;

	public Combiner(Player player) {
		this.player = player;
	}

	public void openInterface() {
		player.getPacketSender().sendInterface(19700);
		sendData();
	}

	private void sendData() {
		int count = 0;
		player.getPacketSender().slot = 0;
		for (CombinerData data : CombinerData.values()) {
			player.getPacketSender().sendItemOnInterface(19710, data.craftedItem.getId(), count,
					data.craftedItem.getAmount());
			player.getPacketSender().sendCombinerItemsOnInterface(19711, data.requirements);
			count++;
		}

	}

	public void craftItem(int index) {
		CombinerData itemToCraft = CombinerData.values()[index];
		Item[] reqs = itemToCraft.requirements;
		boolean firstMatches = player.getInventory().getAmount(reqs[0].getId()) >= reqs[0].getAmount();
		boolean secondMatches = player.getInventory().getAmount(reqs[1].getId()) >= reqs[1].getAmount();
		boolean thirdMatches = player.getInventory().getAmount(reqs[2].getId()) >= reqs[2].getAmount();
		boolean fourthMatches = player.getInventory().getAmount(reqs[3].getId()) >= reqs[3].getAmount();
		if (firstMatches && secondMatches && thirdMatches && fourthMatches) {
			player.getInventory().deleteItemSet(itemToCraft.requirements);
			player.getInventory().add(itemToCraft.craftedItem);
			player.sendMessage("Enjoy the new crafted item!");
		} else {
			player.sendMessage("U don't have the requirements in your inventory to craft this item.");
		}
	}

}
