package com.zamron.world.content.skill.impl.agility;

import com.zamron.model.GameObject;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.skill.impl.scavenging.ScavengeGain;
import com.zamron.world.entity.impl.player.Player;

public class Agility {

	public static boolean handleObject(Player p, GameObject object) {
		if(object.getId() == 2309) {
			if(p.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
				p.getPacketSender().sendMessage("You need an Agility level of at least 55 to enter this course.");
				return true;
			}
		}
		ObstacleData agilityObject = ObstacleData.forId(object.getId());
		if(agilityObject != null) {
			if(p.isCrossingObstacle())
				return true;
			p.setPositionToFace(object.getPosition());
			p.setResetPosition(p.getPosition());
			p.setCrossingObstacle(true);
			//boolean wasRunning = p.getAttributes().isRunning();
			//if(agilityObject.mustWalk()) {
				//p.getAttributes().setRunning(false);
			//	p.getPacketSender().sendRunStatus();
			//}
			int chance = RandomUtility.random(100);
			if (chance >= 85 && chance <= 90) {
			ScavengeGain.Agility(p);
			}
			if(Misc.getRandom(7000) == 3) {
				p.getInventory().add(13325, 1);
				World.sendMessageNonDiscord("@blu@<img=12>[Skilling Pets] "+p.getUsername()+" has received the Giant Squirrel pet!");
				p.getPacketSender().sendMessage("@red@You have received a skilling pet!");
			}
			agilityObject.cross(p);
			Achievements.finishAchievement(p, AchievementData.CLIMB_AN_AGILITY_OBSTACLE);
			Achievements.doProgress(p, AchievementData.CLIMB_50_AGILITY_OBSTACLES);
		}
		return false;
	}

	public static boolean passedAllObstacles(Player player) {
		for(boolean crossedObstacle : player.getCrossedObstacles()) {
			if(!crossedObstacle)
				return false;
		}
		return true;
	}

	public static void resetProgress(Player player) {
		for(int i = 0; i < player.getCrossedObstacles().length; i++)
			player.setCrossedObstacle(i, false);
	}
	
	public static boolean isSucessive(Player player) {
		return Misc.getRandom(player.getSkillManager().getCurrentLevel(Skill.AGILITY) / 2) > 1;
	}
	
	public static void addExperience(Player player, int experience) {
		boolean agile = player.getEquipment().get(Equipment.BODY_SLOT).getId() == 14936 && player.getEquipment().get(Equipment.LEG_SLOT).getId() == 14938;
		player.getSkillManager().addExperience(Skill.AGILITY, agile ? (experience *= 1.5) : experience);
	}
}
