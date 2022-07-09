package com.zamron.world.content;

import java.util.HashMap;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.raids.OldRaidParty;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.player.Player;

public class KeysEvent {
	
	//Item ids that will be dropped
	public static int pvmKey = 3455; //1543    3455 t4+ key
	public static int pvmKeyLow = 3459;
	
	//useless, just needed to write down object id
	public static int chest = 6420;

	public static int rareLoots[] = {18377,15418,19468,2572,16137,11076,18363,18380,18381,18382,18383,18384,18385,3077};

	public static int ultraLoots[] = {18380,18381,18382,18383,18384,18385,1499,4799,4800,4801,3951,15012};
	
	public static int amazingLoots[] = {1499,4799,4800,4801,3951,15012,3316,3931,3958,3959,3960,5187,14559};
	
	public static int commonLoots[] = {18940,18941,18942,18946,18934,19721,19722,19723,19734,19736,18377,15418,19468,2572,16137,11076,18363};
	/*
	 * Start methods below
	 */
	
	
	/*
	 * Grabs a random item from aray
	 */
	public static int getRandomItem(int[] array) {
		return array[Misc.getRandom(array.length - 1)];
	}

	
	/*
	 * Opening the chest with suspense, give reward
	 */
	public static void openChest(Player player) {
		if(!player.getClickDelay().elapsed(3000)) 
			return;
		if (player.getInventory().contains(1543)) {   
		
			TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (player.getInventory().contains(1543)) {  
					player.getInventory().delete(1543, 1);
					player.performAnimation(new Animation(827));
					player.getPacketSender().sendMessage("Opening Chest...");
					giveReward(player);
					NpcGain.GainMediumChestXP(player);
				} else {
					player.getPacketSender().sendMessage("You require a Medium Key to open this chest!");
				}
				this.stop();
			}
		});
      } else {
		  
    	  player.getPacketSender().sendMessage("You require a Medium Key to open this chest!");
    	  return;
      }
	 
	}
	
	public static void giveReward(Player player) {
		if (Misc.getRandom(40) == 5) { //Rare Item
			int rareDrops = getRandomItem(rareLoots);
			player.getInventory().add(rareDrops, 1);
			World.sendMessageNonDiscord("@or3@[Medium Key Chest]@bla@ "+player.getUsername()+ " has recieved a Rare from the chest!");
		} else if (Misc.getRandom(225) == 147) {//Ultra Rare items
			World.sendMessageNonDiscord("@or3@[Medium Key Chest]@bla@ "+player.getUsername()+ " has recieved an Ultra Rare from the chest!");
			int ultraDrops = getRandomItem(ultraLoots);
			player.getInventory().add(ultraDrops, 1);
		} else if (Misc.getRandom(400) == 388) {//Amazing items
			World.sendMessageNonDiscord("@or3@[Medium Key Chest]@bla@ "+player.getUsername()+ " has recieved a Legendary Reward from the chest!!");
			int amazingDrops = getRandomItem(amazingLoots);
			player.getInventory().add(amazingDrops, 1);
		} else {//Common items
			int commonDrops = getRandomItem(commonLoots);
			player.getInventory().add(commonDrops, 1);
		}
			
	}
	
	/*
	 * Handles Skotizo Boss
	 */
	public static void handleSkotizo(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a t4-7 key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Phoenix Boss
	 */
	public static void handlePhoenix(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Thermo Boss
	 */
	public static void handleThermo(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Slash Bash Boss
	 */
	public static void handleSlashBash(Player player, Position pos) {
		if (Misc.getRandom(100) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles KBD Boss
	 */
	public static void handleKBD(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Cerb Boss
	 */
	public static void handleCerb(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Bork Boss
	 */
	public static void handleBork(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Glacor Boss
	 */
	public static void handleGlacor(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Shaman Boss
	 */
	public static void handleShaman(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Green Dragon
	 */
	public static void handleGreenDragon(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Blue Dragon
	 */
	public static void handleBlueDragon(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles Abby Demon
	 */
	public static void handleAbbyDemon(Player player, Position pos) {
		if (Misc.getRandom(50) == 15) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKeyLow), pos, player.getUsername(), false, 150, true, 200));
			player.getPacketSender().sendMessage("@red@You have recieved a t1-3 key!");
		}
		if (Misc.getRandom(100) == 100) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pvmKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	/*
	 * Handles Raids NPC 1
	 */
	public static void handleRaidsNPC(Player player, Position pos, Position position) {
		OldRaidParty oldRaidParty = player.getOldRaidParty();
		if (oldRaidParty != null) {
			if (oldRaidParty.getIsRaiding(player) && oldRaidParty.getCurrentRaid() != null) {
				oldRaidParty.getCurrentRaid().nextLevel();
			}
		}
	}

	

	private static boolean roll(int number) {
		if (Misc.getRandom(150) == number)
			return true;
		else
			return false;
	}

	private static int[] itemRewards = new int[] { 22158, 22159, 22160, 22161, 22162, 22163, 12852 };

	/*
	 * Handles Raids NPC 5
	 */
	public static void handleRaidsNPCBoss(Player player, Position pos, Position position) {
		HashMap<Integer, Integer> rewards = new HashMap<Integer, Integer>();
		rewards.put(13591, 1);

		for (int i = 1; i <= 4; i++) { // 4 tries
			if (roll(5)) { // check if player landed on 5
				int reward = Misc.inclusiveRandom(0, itemRewards.length - 1);
				if (rewards.get(itemRewards[i]) != null) {
					int amount = rewards.get(reward) + 1;
					rewards.put(itemRewards[i], amount);
				} else {
					rewards.put(itemRewards[i], 1);
				}
			}
		}
		OldRaidParty oldRaidParty = player.getOldRaidParty();
		if (oldRaidParty != null) {
			if (oldRaidParty.getIsRaiding(player) && oldRaidParty.getCurrentRaid() != null) {
				oldRaidParty.succeededRaid(rewards);
			}
		}
	}


}
