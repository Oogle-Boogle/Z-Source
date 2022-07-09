package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.model.GraphicHeight;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.player.Player;


public class TrioBosses {
	
	//Npc ids that will drop keys
	public static int skeletonId = 0;
	public static int zombieId = 0;
	public static int ghostId = 0;
	
	//Item ids that will be dropped
	public static int skeletonKey = 0;
	public static int ghostKey = 0;
	public static int zombieKey = 0;
	
	//useless, just needed to write down obejct id
	public static int chest = 2079;

	//arrays that hold the rare and common chest loots
	public static int rareLoots[] = {1413,9925,9924,10726,10724,10725,3647,15255,15257,15256,15268,15258};
	
	public static int commonLoots[] = {1959};
	
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
		if (player.getInventory().contains(605) && player.getInventory().contains(993) && player.getInventory().contains(1590)) {   
			player.getInventory().delete(605, 1);   
			player.getInventory().delete(993, 1);   
			player.getInventory().delete(1590, 1);   
			TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				player.performAnimation(new Animation(6387));
				player.getPacketSender().sendMessage("Opening Chest...Happy Halloween!");
				giveReward(player);
				this.stop();
			}
		});
      } else {
	/*
	 * Player does not have all the keys
	 */
    	  player.getPacketSender().sendMessage("You need all the keys to open the chest!");
    	  return;
      }
	 
	}
	
	/*
	 * Eating Pumpkins action (fun useless thing)
	 */
	public static void eatPumpkin(Player player) {
		player.getInventory().delete(1959, 1);
		player.performAnimation(new Animation(865));
		player.performGraphic(new Graphic(199, GraphicHeight.HIGH));
		//player.forceChat("Happy Halloween everyone from "+player.getUsername());
	}
	
	/*
	 * Handles teleporting into the event area
	 */
	public static void teleIn(Player player) {
		TeleportHandler.teleportPlayer(player, new Position(2573, 9866), player.getSpellbook().getTeleportType());
		player.getPacketSender().sendMessage("Good luck at the @or3@Halloween Event!");
	}
	
	/*
	 * Gives loot from chest
	 */
	public static void giveReward(Player player) {
			World.sendMessageNonDiscord("@or3@[Halloween Event]@bla@ "+player.getUsername()+ " has opened a chest!");
			if (Misc.getRandom(20) == 5) {
			/*
			 * Give a rare Loot
			 */
			int rareDrops = getRandomItem(rareLoots);
			player.getInventory().add(rareDrops, 1);
			World.sendMessageNonDiscord("@or3@[Halloween Event]@bla@ "+player.getUsername()+ " has recieved a rare holiday item!");

		} else {
			/*
			 * Give Common Loot
			 */
			
			int commonDrops = getRandomItem(commonLoots);
			player.getInventory().add(commonDrops, Misc.inclusiveRandom(1, 5));
			
		 }
			
	}
	
	/*
	 * Handles the skeleton npc drops
	 */
	public static void handleSkeleton(Player player, Position pos) {
		if (Misc.getRandom(6) == 2) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(skeletonKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}
	
	/*
	 * Handles the ghost npc drops
	 */
	public static void handleGhost(Player player, Position pos) {
		if (Misc.getRandom(6) == 2) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(ghostKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}

	/*
	 * Handles the ghost npc drops
	 */
	public static void handleZombie(Player player, Position pos) {
		if (Misc.getRandom(6) == 2) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(zombieKey), pos, player.getUsername(), false, 150, true, 200));
		    player.getPacketSender().sendMessage("@red@You have recieved a key!");
		}
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526), pos, player.getUsername(), false, 150, true, 200));
	}

}
