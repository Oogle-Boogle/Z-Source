package com.zamron.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.Locations.Location;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.zamron.world.entity.impl.player.Player;


public class Cows {
	
	public static int cowsKilled;
	public static boolean canStay;
	public static NPC cowkiller;
	public static NPC cowkiller2;
	public static NPC cowkiller3;


	private static CopyOnWriteArrayList<NPC> npcList = new CopyOnWriteArrayList<NPC>();

	public static void checkCoins(final Player player) {
		
	}
	
	public static void giveItems (final Player player) {
		
		player.getInventory().add(1277,1);
		player.getInventory().add(1171,1);
	}
	
	public static void teleportIn(final Player player) {
		for(Item t : player.getEquipment().getItems()) {
			if(t != null && t.getId() > 0 && t.getId() != 995) {
				player.getPacketSender().sendMessage("You can not bring any equipment to the cow event");
				return;
			}
		}
		for(Item t : player.getInventory().getItems()) {
			if(t != null && t.getId() > 0 && t.getId() != 995) {
				player.getPacketSender().sendMessage("You can only bring coins to the cow event");
				return;
			}
		}
		if (player.getInventory().contains(995)) {
			
			
		TeleportHandler.teleportPlayer(player, new Position(3260, 3272), player.getSpellbook().getTeleportType());
		//System.out.print("location:" + player.getLocation());
		
		giveItems(player);
		coinRemoval(player);

		player.getPacketSender().sendMessage("Your coins will begin to diminish soon..");
		} else {
            player.getPacketSender().sendMessage("You must have coins in your inventory to teleport to the cow event!");

		}
	}
	
	public static String[] KILLER_CHAT = {
			"We must not fail, slay them all!", 
			"There shall be no cows left! Diminish them!", 
			"We must work as an army. Together, we must defeat all of the cows!", 
			"I'm sick of seeing these cows. Kill them all!", 
			"Put some muscle to it. One hit these fat animals!",
			"Slay them for loot. Let us retrieve the rares!",
			"I've been slaying these cows since 2006!",
			"Cook their meat. Bury their bones. Loot their rares!",
			"Come on Arlania players. My sister can hit harder than you noobs!",
			"Kill them faster! Clear them out of here once and for all!"


		};
	public static void spawnMainNPCs() {
	
		int cowkillerhealth = 3000;
		cowkiller3 = spawnPCNPC(5210, new Position(3259,3264), cowkillerhealth); //cowkillernpc
		cowkiller2 = spawnPCNPC(5210, new Position(3259,3279), cowkillerhealth); //cowkillernpc
		cowkiller = spawnPCNPC(5210, new Position(3255,3289), cowkillerhealth); //cowkillernpc

		cowkiller.getMovementCoordinator().setCoordinator(new Coordinator(true, 10));
		npcList.add(cowkiller);
		cowkiller2.getMovementCoordinator().setCoordinator(new Coordinator(true, 10));
		npcList.add(cowkiller2);
		cowkiller3.getMovementCoordinator().setCoordinator(new Coordinator(true, 10));
		npcList.add(cowkiller3);
		
	}
	
	
	public static void sequence () {
	processNpcs();
		
	}
	
	
	public static NPC spawnPCNPC(int id, Position pos, int constitution) {
		NPC np = new NPC(id, pos);
		np.setConstitution(constitution);
		np.setDefaultConstitution(constitution);
		//set coordinator or alking policy
		World.register(np);
		return np;
	}
	
	public static void processNpcs() {
		
		if(cowkiller != null && cowkiller.getConstitution() > 0 && Misc.getRandom(15) == 10) {
			cowkiller.forceChat(KILLER_CHAT[Misc.getRandom(KILLER_CHAT.length - 1)]);
			cowkiller2.forceChat(KILLER_CHAT[Misc.getRandom(KILLER_CHAT.length - 1)]);

			cowkiller3.forceChat(KILLER_CHAT[Misc.getRandom(KILLER_CHAT.length - 1)]);

		}
		
	}
	
	public static void coinRemoval(final Player player) {
		TaskManager.submit(new Task(100, player, false) {
			@Override
			public void execute() {
				
				if(player.getLocation() == Location.COWS) {
					//System.out.print("is in cowfield");
				}
				if(player.getLocation() != Location.COWS) {
					System.out.print("is not in field, stopping thread");
					this.stop();
					return;
				}
				//check playtimes to base cash removal?
				if(player.getInventory().getAmount(995) > 414999) {

					player.getInventory().delete(995, 415000);
					player.performGraphic(new Graphic(1368));
					player.getPacketSender().sendMessage("Some of your coins diminish....");
				} else {
					player.getCombatBuilder().cooldown(true);
					player.getMovementQueue().reset();
					player.moveTo(new Position(3094, 3503, 0));
					player.getPacketSender().sendMessage("You have run out of coins and teleport out!");
					this.stop();
				}
			}
		});
	}


}
