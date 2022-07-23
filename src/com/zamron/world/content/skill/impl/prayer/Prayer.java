package com.zamron.world.content.skill.impl.prayer;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.StarterTasks;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.content.StarterTasks.StarterTaskData;
import com.zamron.world.entity.impl.player.Player;

/**
 * The prayer skill is based upon burying the corpses of enemies. Obtaining a higher level means
 * more prayer abilities being unlocked, which help out in combat.
 * 
 * @author Gabriel Hannason
 */

public class Prayer {
	
	public static boolean isBone(int bone) {
		return BonesData.forId(bone) != null;
	}
	
	public static void buryBone(final Player player, final int itemId) {
		final BonesData currentBone = BonesData.forId(itemId);
		if(currentBone == null)
			return;
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You dig a hole in the ground..");
		Sounds.sendSound(player, Sound.BURY_BONE);
		final Item bone = new Item(itemId);
		player.getInventory().delete(bone);
		TaskManager.submit(new Task(3, player, false) {
			@Override
			public void execute() {
				player.getPacketSender().sendMessage("..and bury the "+bone.getDefinition().getName()+".");
				player.getSkillManager().addExperience(Skill.PRAYER, currentBone.getBuryingXP());
				if(currentBone == BonesData.BIG_BONES)
					Achievements.finishAchievement(player, AchievementData.BURY_A_BIG_BONE);
				else if(currentBone == BonesData.FROSTDRAGON_BONES) {
					Achievements.doProgress(player, AchievementData.BURY_25_FROST_DRAGON_BONES);
					Achievements.doProgress(player, AchievementData.BURY_500_FROST_DRAGON_BONES);
					StarterTasks.doProgress(player, StarterTaskData.BURY_50_FROST_DRAGON_BONES);
					
				}
				stop();				
			}
		});
	}
}
