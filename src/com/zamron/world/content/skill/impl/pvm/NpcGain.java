package com.zamron.world.content.skill.impl.pvm;

import com.zamron.model.Skill;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class NpcGain {
	public static int BossesXP = 50;
	public static int NormalXP = 20;
	public static int RaidReward = 1000;
	public static int DropReward = 250;
	public static int WBReward = 750;

	public static void GainBossesXP (Player player) {
		/*player.sendMessage("@red@You received 50 PVM exp from killing this monster");*/
		player.getSkillManager().addExperience(Skill.PVM, BossesXP);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void GainNormalXP (Player player) {
	/*	player.sendMessage("@blu@You received 20 PVM exp from killing this monster");*/
		player.getSkillManager().addExperience(Skill.PVM, NormalXP);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void GainEasyChestXP (Player player) {
		int easyxpamount = 50;
		if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 45) {
	/*		player.sendMessage("@yel@You just received PVM XP for opening this chest! (X2)");*/
			player.getSkillManager().addExperience(Skill.PVM, easyxpamount * 2);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 65) {
/*			player.sendMessage("@blu@You just received PVM XP for opening this chest! (X3)");*/
			player.getSkillManager().addExperience(Skill.PVM, easyxpamount * 3);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 80) {
/*			player.sendMessage("@gre@You just received PVM XP for opening this chest! (X4)");*/
			player.getSkillManager().addExperience(Skill.PVM, easyxpamount * 4);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else {
			player.getSkillManager().addExperience(Skill.PVM, easyxpamount * 1);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
			/*player.sendMessage("@red@You just received PVM XP for opening this chest! (X1)");*/
		}
	}
	public static void GainMediumChestXP (Player player) {
		int mediumxpamount = 250;
		if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 45) {
			/*player.sendMessage("@yel@You just received PVM XP for opening this chest! (X2)");*/
			player.getSkillManager().addExperience(Skill.PVM, mediumxpamount  * 2);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 65) {
			/*player.sendMessage("@blu@You just received PVM XP for opening this chest! (X3)");*/
			player.getSkillManager().addExperience(Skill.PVM, mediumxpamount * 3);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else if (player.getSkillManager().getMaxLevel(Skill.PVM) >= 80) {
			/*player.sendMessage("@gre@You just received PVM XP for opening this chest! (X4)");*/
			player.getSkillManager().addExperience(Skill.PVM, mediumxpamount * 4);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
		}
		else {
			player.getSkillManager().addExperience(Skill.PVM, mediumxpamount * 1);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getCurrentLevel(Skill.PVM));
			/*player.sendMessage("@red@You just received PVM XP for opening this chest! (X1)");*/
		}
	}
	public static void RaidNPCNormalXP (Player player, NPC npc) {
			/*player.sendMessage("@red@You received a bonus PVM exp from killing this Raid monster");*/
			player.getSkillManager().addExperience(Skill.PVM, BossesXP / 2);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void RaidNPCBossXP (Player player, NPC npc) {
			/*player.sendMessage("@red@You received a bonus PVM exp from killing this Raid monster");*/
			player.getSkillManager().addExperience(Skill.PVM, NormalXP / 2);
			player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void RaidComplete (Player player) {
		int level = player.getSkillManager().getCurrentLevel(Skill.PVM);
		/*player.sendMessage("@red@You received PVM exp for completing the Raids!");*/
		player.getSkillManager().addExperience(Skill.PVM, RaidReward * level);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void RaidFailed (Player player) {
		/*player.sendMessage("@red@You received PVM exp for failing the Raids!");*/
		player.getSkillManager().addExperience(Skill.PVM, RaidReward / 2);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void RareDropXP (Player player) {
		/*player.sendMessage("@red@You received bonus PVM exp from getting an announced drop");*/
		player.getSkillManager().addExperience(Skill.PVM, DropReward);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
	public static void WorldBossXP (Player player) {
		/*player.sendMessage("@red@You received bonus PVM exp from slaying an World Boss!");*/
		player.getSkillManager().addExperience(Skill.PVM, WBReward);
		player.getSkillManager().setCurrentLevel(Skill.PVM, player.getSkillManager().getMaxLevel(Skill.PVM));
	}
}

