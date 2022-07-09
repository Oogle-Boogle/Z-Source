package com.zamron.world.content.skill.impl.smithing;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Sounds;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.Sounds.Sound;
import com.zamron.world.entity.impl.player.Player;

public class Smelting {

	public static void openInterface(Player player) {
		player.getSkillManager().stopSkilling();
		for (int j = 0; j < SmithingData.SMELT_BARS.length; j++)
			player.getPacketSender().sendInterfaceModel(SmithingData.SMELT_FRAME[j], SmithingData.SMELT_BARS[j], 150);
		player.getPacketSender().sendChatboxInterface(2400);
	}

	public static void smeltBar(final Player player, final int barId, final int amount) {
		player.getSkillManager().stopSkilling();
		player.getPacketSender().sendInterfaceRemoval();
		if(!SmithingData.canSmelt(player, barId))
			return;
		player.performAnimation(new Animation(896));
		player.setCurrentTask(new Task(3, player, true) {
			int amountMade = 0;
			@Override
			public void execute() {
				if(!SmithingData.canSmelt(player, barId)) {
					stop();
					return;
				}
				player.setPositionToFace(new Position(3022, 9742, 0));
				player.performAnimation(new Animation(896));
				handleBarCreation(barId, player);
				amountMade++;
				if(amountMade >= amount)
					stop();
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}

	public static void handleBarCreation(int barId, Player player) {
		if(player.getOres()[0] > 0) {
			player.getInventory().delete(player.getOres()[0], 1);
			if(player.getOres()[1] > 0 && player.getOres()[1] != 453) {
				player.getInventory().delete(player.getOres()[1], 1);
			} else if(player.getOres()[1] == 453) {
				player.getInventory().delete(player.getOres()[1], SmithingData.getCoalAmount(barId));
			}
			if(barId != 2351) { //Iron bar - 50% successrate
				player.getInventory().add(barId, 1);
				player.getSkillManager().addExperience(Skill.SMITHING, getExperience(barId));
				if(barId == 2363) {
					Achievements.doProgress(player, AchievementData.SMELT_25_RUNE_BARS);
					Achievements.doProgress(player, AchievementData.SMELT_1000_RUNE_BARS);
				}
			} else if(SmithingData.ironOreSuccess(player)) {
				Achievements.finishAchievement(player, AchievementData.SMELT_AN_IRON_BAR);
				player.getInventory().add(barId, 1);
				player.getSkillManager().addExperience(Skill.SMITHING, getExperience(barId));
			} else
				player.getPacketSender().sendMessage("The Iron ore burns too quickly and you're unable to make an Iron bar.");
			Sounds.sendSound(player, Sound.SMELT_ITEM);
		}
	}
	
	

	public static int getExperience(int barId) {
		switch(barId) {
		case 2349: // Bronze bar
			return 50;
		case 2351: // Iron bar
			return 180;
		case 2353: // Steel bar
			return 250;
		case 2355: // Silver bar
		case 2357: // Gold bar
			return 350;
		case 2359: // Mithril bar
			return 500;
		case 2361: // Adamant bar
			return 700;
		case 2363: // Runite bar
			return 1000;
		}
		return 0;
	}
}
