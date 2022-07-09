package com.zamron.world.content.raids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.zamron.GameSettings;
import com.zamron.model.Item;
import com.zamron.world.World;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.entity.impl.player.Player;

public class OldRaidParty {

	public static int MAGICAL_ORB = 6950;

	private Player raidLeader;
	private static ArrayList<Player> raidMembers;
	private static InstancedRaid currentRaid;
	private ArrayList<Player> defeatedMembers;
	private ConcurrentHashMap<Player, Boolean> raidingStatus;

	public OldRaidParty(Player raidLeader) {
		if (!raidLeader.getInventory().contains(MAGICAL_ORB) && !raidLeader.getBank().contains(MAGICAL_ORB)) {
			if (raidLeader.getInventory().getFreeSlots() > 0) {
				raidLeader.getInventory().add(MAGICAL_ORB, 1);
				raidLeader.sendMessage("@blu@<shad=10>A magical orb was added to your inventory.");
				raidLeader.sendMessage("@blu@<shad=10>->Use this magical orb on players to accept them into your raid party.");
			} else {
				if (raidLeader.getBank().getFreeSlots() > 0) {
					raidLeader.getInventory().add(MAGICAL_ORB, 1);
					raidLeader.sendMessage("@blu@<shad=10>A magical orb was added to your bank.");
					raidLeader
							.sendMessage("@blu@<shad=10>->Use the magical orb on players to accept them into your raid party.");
				}
			}
		}
		this.raidLeader = raidLeader;
		raidMembers = new ArrayList<Player>();
		defeatedMembers = new ArrayList<Player>();
		raidingStatus = new ConcurrentHashMap<Player, Boolean>();
		raidLeader.sendMessage("@blu@<shad=10>You have created a new raid party!");
		raidLeader.sendMessage("->@blu@<shad=10>You can invite a maximum of 9 players to your raid party.");
	}

	public void updateRaidingStatus(Player member, boolean value) {
		raidingStatus.put(member, value);
	}

	public boolean getIsRaiding(Player member) {
		if (raidingStatus.get(member) != null)
			return raidingStatus.get(member);
		else
			return false;
	}

	public void refresh() {
		defeatedMembers.clear();
		raidingStatus.clear();
		for (Player member : raidMembers) {
			raidingStatus.put(member, true);
		}
		raidingStatus.put(raidLeader, true);
	}

	public void leave(Player member) {
		if (member == raidLeader) {
			disband();
			return;
		}
		if (raidMembers.contains(member)) {
			raidMembers.remove(member);
			if (member.getRegionInstance() != null) {
				member.setRegionInstance(null);
				member.moveTo(GameSettings.DEFAULT_POSITION);
				if (raidingStatus.containsKey(member))
					raidingStatus.remove(member);
				sendMessageToMembers("@red@<shad=10>->" + member.getUsername() + " has left your raid group.", false);
			}
		}
	}

	public void failedRaid() {
		if (currentRaid != null) {
			// double check region instances
			for (Player member : raidMembers) {
				if (member != null) {
					if (member.getRegionInstance() != null) {
						member.setRegionInstance(null);
						member.moveTo(GameSettings.DEFAULT_POSITION);
						member.sendMessage("@red@<shad=10>Unfortunately, your raid group failed the raid...");
						NpcGain.RaidFailed(member);
					}
				} else {
					raidMembers.remove(member);
				}
			}
			currentRaid.destroyNpcs();
		}
	}

	private int tickets = 13591;

	public void succeededRaid(HashMap<Integer, Integer> rewards) {
		ArrayList<Player> rewardPoolMembers = new ArrayList<Player>(raidMembers);
		rewardPoolMembers.add(raidLeader);
		int ticketAmount = 1;

		ArrayList<Item> rewardPoolItems = new ArrayList<Item>();
		Iterator<Entry<Integer, Integer>> rewardIterator = rewards.entrySet().iterator();

		while (rewardIterator.hasNext()) {
			Entry<Integer, Integer> entry = rewardIterator.next();
			if (entry.getKey() != tickets) { // make sure we aren't putting tickets in reward pool
				for (int i = 1; i <= entry.getValue(); i++) {
					Item rewardItem = new Item(entry.getKey(), 1);
					rewardPoolItems.add(rewardItem);
				}
			} else {
				ticketAmount = entry.getValue();
			}
		}


		// double check region instances
		if (currentRaid != null) {
			for (Player member : raidMembers) {
				if (member != null) {
					if (member.getRegionInstance() != null) {
						member.setRegionInstance(null);
						member.moveTo(GameSettings.RAIDS_FINISHED_POSITION);
						member.getInventory().add(tickets, ticketAmount);
						member.sendMessage("<col=60148a><shad=10>Congratulations, your team has successfully completed the raid!");
						updateRaidingStatus(member, false);
						NpcGain.RaidComplete(member);
					}
				} else {
					raidMembers.remove(member);
				}
			}

			raidLeader.setRegionInstance(null);
			raidLeader.moveTo(GameSettings.RAIDS_FINISHED_POSITION);
			raidLeader.getInventory().add(tickets, ticketAmount);
			raidLeader.sendMessage("<col=60148a><shad=10>Congratulations, your team has successfully completed the raid!");
			updateRaidingStatus(raidLeader, false);
		}
		currentRaid = null;
	}

	// for raids with chests
	public void succeededRaid() {
		// double check region instances
		if (currentRaid != null) {
			for (Player member : raidMembers) {
				if (member != null) {
					if (member.getRegionInstance() != null) {
						member.setRegionInstance(null);
						member.moveTo(GameSettings.RAIDS_FINISHED_POSITION);
						member.sendMessage("<col=60148a><shad=200>Congratulations, your team has successfully completed the raid!Congratulations, your team has successfully completed the raid!");
						updateRaidingStatus(member, false);
						NpcGain.RaidComplete(member);
					}
				} else {
					raidMembers.remove(member);
				}
			}

			raidLeader.setRegionInstance(null);
			raidLeader.moveTo(GameSettings.RAIDS_FINISHED_POSITION);
			raidLeader.sendMessage("<col=60148a><shad=200>Congratulations, your team has successfully completed the raid!Congratulations, your team has successfully completed the raid!");
			updateRaidingStatus(raidLeader, false);
		}
		currentRaid = null;
	}
	
	public void addDefeatedMember(Player member) {
		member.sendMessage("Unfortunately, you did not complete the raid...");
		defeatedMembers.add(member);
		member.moveTo(GameSettings.DEFAULT_POSITION);
	}

	public boolean isDefeated(Player member) {
		return defeatedMembers.contains(member);
	}

	public void setRaid(InstancedRaid raid) {
		currentRaid = raid;
		sendMessageToMembers("<col=60148a><shad=200>Your current raid has been set to: " + raid.getName(), false);
		raidLeader.sendMessage("<col=60148a><shad=200>You have set your party's raid to: " + raid.getName());
	}

	public InstancedRaid getCurrentRaid() {
		return currentRaid;
	}

	public void addMember(Player recruit) {
		for (Player member : raidMembers) {
			if (member == null || !member.getSession().getChannel().isConnected()) {
				raidMembers.remove(member);
			}
		}
		if (raidMembers.size() >= 9) {
			raidLeader.sendMessage("@red@<shad=200>You can only add 9 additional players to your raid party.");
			return;
		}

		if (raidMembers.contains(recruit)) {
			raidLeader.sendMessage("@red@<shad=200>This player is already in your raid party.");
			return;
		}

		if (recruit.getOldRaidParty() != null) {
			raidLeader.sendMessage("@red@<shad=200>" + recruit.getUsername() + " must leave their current raid party first.");
			return;
		}

		recruit.setOldRaidParty(this);
		raidMembers.add(recruit);
		raidingStatus.put(recruit, false);

		sendMessageToMembers("@blu@<shad=200>" + recruit.getUsername() + " was added to your raid party!", false);

		raidLeader.sendMessage("@blu@<shad=200>You added " + recruit.getUsername() + " to your raid party!");
	}

	public void sendMessageToMembers(String message, boolean notifyLeader) {
		for (Player member : raidMembers) {
			member.sendMessage(message);
		}
		if (notifyLeader)
			raidLeader.sendMessage(message);
	}

	public Player getLeader() {
		return raidLeader;
	}

	public ArrayList<Player> getMembers() {
		return raidMembers;
	}

	// called if owner leaves
	public static void disband() {
		ArrayList<Player> oldList = new ArrayList<Player>();
		for (Player p : raidMembers) {
			oldList.add(p);
		}
		for (Player member : oldList) {
			if (member != null) {
				if (raidMembers.contains(member)) {
					raidMembers.remove(member);
					if (member.getRegionInstance() != null) {
						member.setRegionInstance(null);
						member.moveTo(GameSettings.DEFAULT_POSITION);
					}
				}
			}
		}
		if (currentRaid != null)
			currentRaid.destroyNpcs();

		for (Player p : World.getPlayers()) {
			if (p != null) {
				if (p.getRaidPartyInvites().size() > 0) {
					p.removeRaidPartyInvite(null);
				}
			}
		}
	}

	public void removeMember(Player member) {
		if (member == null)
			return;

		member.setOldRaidParty(null);

		if (raidMembers.contains(member))
			raidMembers.remove(member);

		if (defeatedMembers.contains(member))
			defeatedMembers.remove(member);

		if (raidingStatus.containsKey(member))
			raidingStatus.remove(member);

		sendMessageToMembers("@red@<shad=200>" + member.getUsername() + " was removed from your raid party.", false);

		raidLeader.sendMessage("@blu@<shad=200>You removed " + member.getUsername() + " from your raid party.");
	}
}
