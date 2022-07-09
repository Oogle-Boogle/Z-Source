package com.zamron.model;

import com.zamron.model.container.impl.Bank;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.content.skill.impl.slayer.SlayerMaster;
import com.zamron.world.content.skill.impl.slayer.SlayerTasks;
import com.zamron.world.entity.impl.player.Player;

public enum GameMode {

	NORMAL, HARDCORE_IRONMAN, IRONMAN, GROUP_IRONMAN;

	public static void set(Player player, GameMode newMode, boolean death) {
		if (!death && !player.getClickDelay().elapsed(1000))
			return;
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();
		if (newMode != player.getGameMode() || death) {
			if (!player.newPlayer()) {
				player.getEquipment().resetItems().refreshItems();
				player.getInventory().resetItems().refreshItems();
				for (Bank bank : player.getBanks()) {
					bank.resetItems();
				}
				player.getSlayer().resetSlayerTask();
				player.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0)
						.setSlayerMaster(SlayerMaster.VANNAKA);
				// player.getSkillManager().newSkillManager();
				// for(Skill skill : Skill.values()) {
				// player.getSkillManager().updateSkill(skill);
				// }
				for (AchievementData d : AchievementData.values()) {
					player.getAchievementAttributes().setCompletion(d.ordinal(), false);
				}
				player.getMinigameAttributes().getRecipeForDisasterAttributes().reset();
				player.getMinigameAttributes().getNomadAttributes().reset();
				player.getKillsTracker().clear();
				player.getDropLog().clear();
				player.getPointsHandler().reset();
				PlayerPanel.refreshPanel(player);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		}
		player.setGameMode(newMode);
		player.getPacketSender().sendIronmanMode(newMode.ordinal());
		if (!death) {
			player.getPacketSender().sendMessage("")
					.sendMessage(
							"You've set your gamemode to " + newMode.name().toLowerCase().replaceAll("_", " ") + ".")
					.sendMessage("If you wish to change it, please talk to the town crier in Edgeville.");
		} else {
			player.getPacketSender().sendMessage("Your account progress has been reset.");
		}
		if(player.newPlayer()) {
			player.setPlayerLocked(true);
		} else {
			player.setPlayerLocked(false);
		}
	}
}
