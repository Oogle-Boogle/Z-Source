package com.zamron.world.content;

import com.zamron.model.Animation;
import com.zamron.model.GameObject;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public class Loyaltychest {

	public static void handleChest(final Player p, final GameObject chest) {
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if (p.getPointsHandler().getLoyaltyPoints() < 20000) {
			p.getPacketSender().sendMessage("To open this chest you need 20k Loyalty Points");
		} else {
			p.performAnimation(new Animation(827));
			if (p.getRights() == PlayerRights.DONATOR) {
				if (Misc.getRandom(15) == 5) {
					p.getPacketSender().sendMessage("Merk Key (Easy) has been saved as a donator benefit");
				} else {
					p.getPointsHandler().setLoyaltyPoints(20000, false);
				}
			}
			if (p.getRights() == PlayerRights.SUPER_DONATOR || p.getRights() == PlayerRights.SUPPORT) {
				if (Misc.getRandom(12) == 5) {
					p.getPacketSender().sendMessage("Merk Key (Easy) has been saved as a donator benefit");
				} else {
					p.getPointsHandler().setLoyaltyPoints(20000, false);
				}
			}
			if (p.getRights() == PlayerRights.EXTREME_DONATOR || p.getRights() == PlayerRights.MODERATOR) {
				if (Misc.getRandom(9) == 5) {
					p.getPacketSender().sendMessage("Merk Key (Easy) has been saved as a donator benefit");
				} else {
					p.getPointsHandler().setLoyaltyPoints(20000, false);
				}
			}
			if (p.getRights() == PlayerRights.LEGENDARY_DONATOR  || p.getRights() == PlayerRights.ADMINISTRATOR) {
				if (Misc.getRandom(6) == 5) {
					p.getPacketSender().sendMessage("Merk Key (Easy) has been saved as a donator benefit");
				} else {
					p.getPointsHandler().setLoyaltyPoints(20000, false);
				}
			}
			if (p.getRights().isHighDonator() || p.getRights() == PlayerRights.DEVELOPER) {
				if (Misc.getRandom(3) == 2) {
					p.getPacketSender().sendMessage("Merk Key (Easy) has been saved as a donator benefit");
				} else {
					p.getPointsHandler().setLoyaltyPoints(20000, false);
				}
			}
			
			p.getPacketSender().sendMessage("You open the chest..");
		
			Item[] loot = itemRewards[Misc.getRandom(itemRewards.length - 1)];
			for(Item item : loot) {
				p.getInventory().add(item);
			}
			p.getInventory().add(995, 100000 + RandomUtility.RANDOM.nextInt(100000));
			//p.getPointsHandler().removeLoyaltyPoints(20000);
		}
		
				
	}

	private static final Item[][] itemRewards =  {
			{new Item(17911, 1)},
			{new Item(17908, 1)},
			{new Item(17909, 1)},
			{new Item(995, 100000000)},
			{new Item(6199, 1)},
			{new Item(14484, 1)},
			{new Item(11694, 1)},
			{new Item(6199, 1)},
			{new Item(15373, 1)},
			{new Item(2572, 1)},
			{new Item(3654, 1)},
			{new Item(3655, 1)},
			{new Item(3656, 1)},
			{new Item(3657, 1)},
			{new Item(3658, 1)},
			
		};
	
}
