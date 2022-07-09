package com.zamron.world.content.raids.addons;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.zamron.model.GameObject;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.raids.InstancedRaid;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.entity.impl.player.Player;

public class RaidChest extends GameObject {

	private ConcurrentHashMap<Player, Boolean> claims = new ConcurrentHashMap<Player, Boolean>();
	private Item[] rewards;
	private int range;
	private double percent;
	private InstancedRaid raid;
	
	public RaidChest(int id, Position position) {
		super(id, position);
	}

	public InstancedRaid getRaid() {
		return raid;
	}
	
	public void configure(OldRaidParty party, Item[] rewards) {
		raid = party.getCurrentRaid();
		claims.put(party.getLeader(), false);
		for (Player p : party.getMembers()) {
			claims.put(p, false);
		}
		this.rewards = rewards;
	}

	public void configureWithPercent(OldRaidParty party, Item[] rewards, int range, double percent) {
		raid = party.getCurrentRaid();
		claims.put(party.getLeader(), false);
		this.range = range;
		this.percent = percent;
		for (Player p : party.getMembers()) {
			claims.put(p, false);
		}
		this.rewards = rewards;
	}

	public void claimRewardWithPercent(Player player) {
		if (!claims.get(player)) {
			claims.put(player, true);
			player.getInventory().add(new Item(13591, Misc.inclusiveRandom(1, 2)));
			for (Item i : rewards) {
				double random = Misc.getRandom(range);
				if (random >= percent) {
					//System.out.println("random(win): "+ random);
					player.getInventory().add(i.getId(), i.getAmount());
					player.sendMessage(
							"@or2@You got lucky and received x" + i.getAmount() + " " + i.getDefinition().getName() + "!");
				} else {
					//System.out.println("random(lose): "+ random);
				}
			}
			if (checkForLastPerson()) {
				World.deregister(this);
				player.getOldRaidParty().succeededRaid();
			}
		} else {
			player.sendMessage("You have already claimed your reward from this chest.");
			player.sendMessage("You will automatically be teleported home when the last person clicks the chest.");
		}
	}

	public void claimReward(Player player) {
		if (!claims.get(player)) { //If player has not claimed..
			player.sendMessage("Claiming reward.");
			claims.put(player, true);
			if (Misc.inclusiveRandom(0, 100) > 95) {
				Item reward = rewards[Misc.inclusiveRandom(rewards.length - 1)];
				player.getInventory().add(reward);
				player.getInventory().add(new Item(19864, Misc.inclusiveRandom(50, 100)));
				player.getOldRaidParty().sendMessageToMembers("@blu@" + player.getUsername() + " has received x"
						+ reward.getAmount() + " " + reward.getDefinition().getName() + " from the Raid 2 chest!",
						true);
			} else {
				player.sendMessage("You got unlucky and only received some cash and tickets..");
				player.getInventory().add(new Item(19864, Misc.inclusiveRandom(50, 100)));
			}
			if (checkForLastPerson()) {
				World.deregister(this);
				player.getOldRaidParty().succeededRaid();
			}
		} else {
			player.sendMessage("You have already claimed your reward from this chest.");
			player.sendMessage("You will automatically be teleported home when the last person clicks the chest.");
		}
	}

	private boolean checkForLastPerson() {
		for (Entry<Player, Boolean> entry : claims.entrySet()) {
			if (!entry.getValue())
				return false;
		}
		return true;
	}
}
