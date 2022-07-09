package com.zamron.world.content.skill.impl.thieving;

import com.zamron.model.Animation;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.Locations.Location;
import com.zamron.util.RandomUtility;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.entity.impl.player.Player;

public class Stalls {

	public static void stealFromStall(Player player, int lvlreq, int xp, int reward, String message) {
		if(player.getInventory().getFreeSlots() < 1) {
			player.getPacketSender().sendMessage("You need some more inventory space to do this.");
			return;
		}
		if (player.getCombatBuilder().isBeingAttacked()) {
			player.getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return;
		}
		if(!player.getClickDelay().elapsed(2000))
			return;
		if(player.getSkillManager().getMaxLevel(Skill.THIEVING) < lvlreq) {
			player.getPacketSender().sendMessage("You need a Thieving level of at least " + lvlreq + " to steal from this stall.");
			return;
		}
		if (player.getLocation() == Location.DONATOR_ZONE) {
		if (RandomUtility.RANDOM.nextInt(35) == 5) {
			TeleportHandler.teleportPlayer(player, new Position(2338, 9800), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("You were caught stealing and teleported away from the stall!");
			return;
		} 
	}
		player.performAnimation(new Animation(881));
		player.getPacketSender().sendMessage(message);
		player.getPacketSender().sendInterfaceRemoval();
		player.getSkillManager().addExperience(Skill.THIEVING, xp);
		player.getClickDelay().reset();
		if (player.getRights() == PlayerRights.DONATOR) {
			player.getInventory().add(10835, 1);

		}
		if (player.getRights() == PlayerRights.SUPER_DONATOR || player.getRights() == PlayerRights.SUPPORT) {
			player.getInventory().add(10835, 2);
		}
		if (player.getRights() == PlayerRights.EXTREME_DONATOR || player.getRights() == PlayerRights.MODERATOR) {
			player.getInventory().add(10835, 3);
		}
		if (player.getRights() == PlayerRights.LEGENDARY_DONATOR || player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.getInventory().add(10835, 3);
		}
		if (player.getRights().isHighDonator()) {
			player.getInventory().add(10835, 5);
		}
		
		if (player.getLocation() == Location.DONATOR_ZONE) {
			Item item = new Item(reward);
			int value = item.getDefinition().getValue();		
			player.getInventory().add(10835, value);

		} else {
			player.getInventory().add(reward, 1);

		}
		
		player.getSkillManager().stopSkilling();
		if(reward == 15009)
			Achievements.finishAchievement(player, AchievementData.STEAL_A_RING);
		else if(reward == 11998) {
			Achievements.doProgress(player, AchievementData.STEAL_140_SCIMITARS);
			Achievements.doProgress(player, AchievementData.STEAL_5000_SCIMITARS);
		}
	}
	

}
