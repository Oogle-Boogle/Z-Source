/*package com.platinum.world.content;

import com.platinum.model.Animation;
import com.platinum.model.GameObject;
import com.platinum.model.Item;
import com.platinum.util.Misc;
import com.platinum.util.RandomUtility;
import com.platinum.world.content.skill.impl.pvm.NpcGain;
import com.platinum.world.entity.impl.player.Player;

public class CrystalChest {

	public static void handleChest(final Player p, final GameObject chest) {
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(!p.getInventory().contains(989)) {
			p.getPacketSender().sendMessage("This chest can only be opened with a Starter Key.");
			return;
		}
		p.performAnimation(new Animation(827));
		p.getInventory().delete(989, 1);
		p.getPacketSender().sendMessage("You open the chest..");
		Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
		for(Item item : loot) {
		p.getInventory().add(item);
		}
		p.getInventory().add(10835, 1 + RandomUtility.RANDOM.nextInt(10));
		NpcGain.GainEasyChestXP(p);
				
					//CustomObjects.objectRespawnTask(p, new GameObject(173 , chest.getPosition().copy(), 10, 0), chest, 10);
				
	}

	private static final Item[][] itemRewards =  {
			
			{new Item(18967,1)},
			{new Item(18967,1)},
			{new Item(19024,1)},
			{new Item(19024,1)},
			{new Item(19025,1)},
			{new Item(19025,1)},
			{new Item(19026,1)},
			{new Item(19026,1)},
			{new Item(19027,1)},
			{new Item(19027,1)},
			{new Item(19043,1)},
			{new Item(19043,1)},
			{new Item(19044,1)},
			{new Item(19044,1)},
			{new Item(10835,1)},
			{new Item(10835,1)},
			{new Item(10835,1)},
			{new Item(10835,3)},
			{new Item(10835,3)},
			{new Item(19864,30)},
			{new Item(19864,30)},
			{new Item(19864,30)},
			{new Item(19864,200)},
			{new Item(19864,200)},
			{new Item(19864,200)},
			{new Item(15373,1)},
			{new Item(15373,1)},
			{new Item(902,1)},
			{new Item(903, 1)},
			{new Item(904, 1)},
			{new Item(905, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			{new Item(2577, 1)},
			{new Item(2577, 1)},
			{new Item(2577, 1)},
			{new Item(902,1)},
			{new Item(903, 1)},
			{new Item(904, 1)},
			{new Item(905, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			{new Item(902,1)},
			{new Item(903, 1)},
			{new Item(904, 1)},
			{new Item(905, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			{new Item(902,1)},
			{new Item(903, 1)},
			{new Item(904, 1)},
			{new Item(905, 1)},
			{new Item(3082, 1)},
			{new Item(3082, 1)},
			
			{new Item(20016, 1)},
			{new Item(20017, 1)},
			{new Item(20018, 1)},
			{new Item(20021, 1)},
			{new Item(20022, 1)},
			{new Item(18910, 1)},
			{new Item(10720, 1)},
			{new Item(14006, 1)},
			{new Item(20016, 1)},
			{new Item(20017, 1)},
			{new Item(20018, 1)},
			{new Item(20021, 1)},
			{new Item(20022, 1)},
			{new Item(18910, 1)},
			{new Item(10720, 1)},
			{new Item(14006, 1)},
			{new Item(6041, 1)},
			{new Item(6041, 1)},
			
			{new Item(17911, 1)},
			{new Item(17908, 1)},
			{new Item(17909, 1)},
			{new Item(11732, 1)},
			{new Item(5161, 1)},
			{new Item(5157, 1)},
			{new Item(5160, 1)},
			{new Item(19138, 1)},
			{new Item(19139, 1)},
			{new Item(3662, 1)},
			{new Item(14453, 1)},
			{new Item(14455, 1)},
			{new Item(14457, 1)},
			{new Item(14453, 1)},
			{new Item(14455, 1)},
			{new Item(14457, 1)},
			
		};
	
}*/
