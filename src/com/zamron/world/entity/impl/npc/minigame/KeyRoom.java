package com.zamron.world.entity.impl.npc.minigame;

import com.zamron.model.*;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author Trey2k
 *
 *         Creation Date: Jul 4, 2018 - 8:35:19 PM
 */
public class KeyRoom {

	public int keyFloorType;

	private GameObject nextStage, rewardsChest;

	/**
	 * The player associated with this event
	 */
	private final Player player;

	private int height;
	
	public int count;

	public KeyRoom(Player player, int keyFloorType) {
		this.player = player;
		this.keyFloorType = keyFloorType;
		player.keyCount1 = player.savedKeyCount1;
		player.keyCount2 = player.savedKeyCount2;
		player.keyCount3 = player.savedKeyCount3;
		this.count = 0;
		initialize();
	}

	public void initialize() {
		if (player.getRegionInstance() != null) 
			player.getRegionInstance().destruct();
		removeFloorNPCs();
		this.height = player.getIndex() * 4;
		player.setRegionInstance(new RegionInstance(player, getInstanceType()));
		player.getPacketSender().sendInterfaceReset();
		teleport();
		spawnObjectsAndEntities();
	}

	private int getNpcIdForFloor() {
		if (keyFloorType == 2)
			return 1290;
		else if (keyFloorType == 3)
			return 742;
		return 1279;
	}

	private void spawnObjectsAndEntities() {

		int totalSpawned = 0;
		
		if (keyFloorType == 1) {
			nextStage = new GameObject(2466, new Position(2097, 4434, this.getHeight()), 0, 10, 10000);
			rewardsChest = new GameObject(12120, new Position(2095, 4434, this.getHeight()), 2, 10, 10000);
		} else if (keyFloorType == 2) {
			nextStage = new GameObject(2467, new Position(1948, 5014, this.getHeight()), 0, 10, 10000);
			rewardsChest = new GameObject(12120, new Position(1946, 5014, this.getHeight()), 2, 10, 10000);
		} else
			rewardsChest = new GameObject(12120, new Position(3104, 9520, this.getHeight()), 2, 10, 10000);
		
		if (nextStage != null)
		    CustomObjects.spawnGlobalObject(nextStage);
		if (rewardsChest != null)
		    CustomObjects.spawnGlobalObject(rewardsChest);
		
		while (totalSpawned < 4) {
			totalSpawned++;
			NPC npc = new NPC(getNpcIdForFloor(), getPosition(keyFloorType, totalSpawned));
					npc.getMovementCoordinator().setCoordinator(new Coordinator(true, 5));
			this.count++;
			World.register(npc);
		}

	}

	/**
	 * @param totalSpawned
	 * @return
	 */
	private Position getPosition(int floorId, int totalSpawned) {
		switch (floorId) {
		
		case 1:
			return new Position(Misc.random(2091, 2097), Misc.random(4424, 4431), this.getHeight());
		case 2:
			return new Position(Misc.random(1948, 1952), Misc.random(5012, 5020), this.getHeight());
		case 3:
			if (totalSpawned == 1)
				new Position(3101, 9514, this.getHeight());
			else if (totalSpawned == 2)
				new Position(3107, 9516, this.getHeight());
			else if (totalSpawned == 3)
				new Position(3101, 9521, this.getHeight());
			return new Position(3106, 9520, this.getHeight());
		}
		return null;
	}

	/**
	 * @return
	 */
	private RegionInstanceType getInstanceType() {
		if (keyFloorType == 2)
			return RegionInstanceType.KEY_ROOM_2;
		else if (keyFloorType == 3)
			return RegionInstanceType.KEY_ROOM_3;
		return RegionInstanceType.KEY_ROOM_1;
	}

	private Position getLocation() {
		if (keyFloorType == 2)
			return new Position(1951, 5016, this.getHeight());
		else if (keyFloorType == 3)
			return new Position(3105, 9518, this.getHeight());
		return new Position(2095, 4428, this.getHeight());
	}

	private void teleport() {
		String msg = "Your current Kill Count for floor " + keyFloorType + " is: <col=ff0000>";

		if (keyFloorType == 1)
			msg += player.keyCount1;
		else if (keyFloorType == 2)
			msg += player.keyCount2;
		else if (keyFloorType == 3)
			msg += player.keyCount3;

		player.getPacketSender().sendMessage(msg);
		player.moveTo(getLocation());
	}

	public void finishRoom(boolean fromUpdate) {
		removeFloorNPCs();
		player.savedKeyCount1 = fromUpdate ? player.keyCount1 : 0;
		player.savedKeyCount2 = fromUpdate ? player.keyCount2 : 0;
		player.savedKeyCount3 = fromUpdate ? player.keyCount3 : 0;
		if (player.getRegionInstance() != null)
		    player.getRegionInstance().destruct();
		player.setKeyRoom(null);
	}

	/**
	 * 
	 */
	private void removeFloorNPCs() {
		for (NPC npc : World.getNpcs()) {
			if (npc != null) {
				if (npc.isKeyRoomNpc() && npc.getPosition().getZ() == getHeight()) 
				    World.deregister(npc);
			}
		}
		
	}

	public int getHeight() {
		return height;
	}
	
	public enum KeyChance {
		
		BEGINNER(2832, 25), 
		DECENT(2834, 5),
		BEST(2836, 2.5);

		public int keyId;
		
		public double chance;
		
		KeyChance(int keyId, double chance) {
			this.keyId = keyId;
			this.chance = chance;
		}
	}
	
	public static KeyChance getData(int keyId) {
		for (KeyChance i : KeyChance.values()) {
			if (i.keyId == keyId)
				return i;
		}
		return null;
	}
	
	private static int[] BEGINNER = { 4151, 6199,3988,3912 };
	private static int[] DECENT = { 15373, 20002, 20001, 20000, 12926, 10835, 11605 };
	private static Item[] BEST = { new Item(15373, 1), new Item(20002, 1), new Item(20001, 1), new Item(20000, 1), new Item(12926, 1), new Item(5130, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(15373, 1), new Item(18768, 1), new Item(3072, 1), new Item(10835, 5), new Item(11605, 5) };
	
	public static void rollKey(Player player, int itemId) {
		if(!player.getInventory().contains(itemId))
            return;
		
		
		KeyChance data = getData(itemId);
		
		if (data == null) 
			return;
		
		Item rewardType = null;
		
		double percent = Math.random() * 100;
		
		player.getInventory().delete(itemId, 1);
		
		if (percent <= data.chance) {
		    if (itemId == 2836)
				rewardType = BEST[Misc.random(BEST.length - 1)];
			else if (itemId == 2834)
				rewardType = new Item(DECENT[Misc.random(DECENT.length - 1)], 1);
			else if (itemId == 2832) 
				rewardType = new Item(BEGINNER[Misc.random(BEGINNER.length - 1)], 1);
			
			
		} else {
			if (itemId == 2832) 
				rewardType = new Item(10835, 5);
			else if (itemId == 2834)
				rewardType = new Item(10835, 15);
			else if (itemId == 2836)
				rewardType = new Item(10835, 50);
		}
			
		boolean bank = false;
		
		if (rewardType != null) {
			
			final ItemDefinition def = ItemDefinition.getDefinitions()[rewardType.getId()];
			
			if (player.getInventory().getFreeSlots() > 0) 
				player.getInventory().add(rewardType);
			else {
				bank = true;
				player.getBank().add(rewardType);
			}
			player.sendMessage("Congratulations! you've recieved "+rewardType.getAmount()+"x "+def.getName()+"...");
			player.sendMessage(".. it has been added to your "+(bank ? "Bank" : "Inventory")+ ".");
		}
		
	}
	
	public static void handleObjectClick(Player player, GameObject object, int option) {
		
		if (option == 2) {
			displayInterface(player);
			return;
		}
		
		switch (object.getId()) {
		
		case 2466:
			
			if (player.keyCount1 >= 50) {
				player.setKeyRoom(new KeyRoom(player, 2));
				return;
			}
			player.sendMessage("You need a Kill Count of 50 on this floor to get into the next.");
			player.sendMessage("Your current Kill Count for Floor 1 is: <col=0000ff>"+player.keyCount1);
			return;
			
		case 2467:
            if (player.keyCount2 >= 250) {
            	player.setKeyRoom(new KeyRoom(player, 3));
				return;
			}
			player.sendMessage("You need a Kill Count of 250 on this floor to get into the next.");
			player.sendMessage("Your current Kill Count for Floor 2 is: <col=0000ff>"+player.keyCount2);
			return;
			
			
		case 12120:
			player.sendMessage("Use the key you have on this chest to unlock a reward!");
			return;
		}
	}
	
	private static void displayInterface(Player player) {
		for (int i = 8145; i <= 8195; i++) 
			player.getPacketSender().sendString(i, "");
		
		player.getPacketSender().sendString(8144, "@dre@Key Room Minigame");
		player.getPacketSender().sendString(8145, "");
		player.getPacketSender().sendString(8146, "Welcome to the Key Room.");
		player.getPacketSender().sendString(8147, "");
		player.getPacketSender().sendString(8148, "What is the Key Room?");
		player.getPacketSender().sendString(8149, "");
		player.getPacketSender().sendString(8150, "The Key Room is a Minigame based upon killing monsters");
		player.getPacketSender().sendString(8151, "which drop various Keys based upon your Floor Level.");
		player.getPacketSender().sendString(8150, "Everybody starts at Floor 1, you will need a Kill Count of 100");
		player.getPacketSender().sendString(8152, "in order to advance to the next Floor. (Floor 2)");
		player.getPacketSender().sendString(8153, "Then another 250 Kills to advance onto Floor 3.");
		player.getPacketSender().sendString(8154, "");
		player.getPacketSender().sendString(8155, "What are the different Types of Keys?");
		player.getPacketSender().sendString(8156, "");
		player.getPacketSender().sendString(8157, "Beginner Key (Floor 1)");
		player.getPacketSender().sendString(8158, "Decent Key (Floor 2)");
		player.getPacketSender().sendString(8159, "Best Key (Floor 3)");
		player.getPacketSender().sendString(8160, "");
		player.getPacketSender().sendString(8161, "Each key has it's own list of items you can get from the chest.");
		player.getPacketSender().sendString(8162, "Each key also has a % Chance of reciving the item or it");
		player.getPacketSender().sendString(8163, "will reward you with coins.");
		player.getPacketSender().sendString(8164, "");
		player.getPacketSender().sendString(8165, "50 Million Coins (Floor 1)");
		player.getPacketSender().sendString(8166, "250 Million Coins (Floor 2)");
		player.getPacketSender().sendString(8167, "500 Million Coins (Floor 3)");
		player.getPacketSender().sendString(8168, "");
		player.getPacketSender().sendString(8169, "Please note, when you logout or TP out, your Kill Count will reset!");
		player.getPacketSender().sendInterface(8134);
	}
}