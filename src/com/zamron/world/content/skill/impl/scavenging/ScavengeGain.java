package com.zamron.world.content.skill.impl.scavenging;

import com.zamron.model.Skill;
import com.zamron.world.entity.impl.player.Player;

public class ScavengeGain {
	private static final int Chop = 100;
	private static final int Ores = 100;
	private static final int Runecrafting = 125;
	private static final int Agility = 125;
	private static final int Barrows = 150;
	private static final int Easy = 150;
	private static final int Medium = 300;
	private static final int Hard = 750;
	private static final int MysteryBox = 500;
	private static final int DropFeed = 125;

	public static void WC (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from Chopping logs!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Chop);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void Mining (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from Mining ores!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Ores);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void Runecraft (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from Runecrafting!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Runecrafting);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void Agility (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from Agility!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Agility);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void Barrows (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from Barrows!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Barrows);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void EasyBox (Player player) {
		if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 55) {
			player.sendMessage("You received an level-based x2 exp from the Easy Scavenging Box");
			player.getSkillManager().addExperience(Skill.SCAVENGING, Easy * 2);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 55) {
			player.getSkillManager().addExperience(Skill.SCAVENGING, Easy);
			player.sendMessage("@red@You received bonus Scavenge exp from the Easy Scavenging Box!");
		}
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void MediumBox (Player player) {
		if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 80) {
			player.sendMessage("You received an level-based x2 exp from the Medium Scavenging Box");
			player.getSkillManager().addExperience(Skill.SCAVENGING, Medium * 2);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 55) {
			player.getSkillManager().addExperience(Skill.SCAVENGING, Medium);
			player.sendMessage("@red@You received bonus Scavenge exp from the Medium Scavenging Box!");
		}
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void HardBox (Player player) {
		player.sendMessage("@red@You received bonus Scavenge exp from the Hard Scavenging Box!");
		player.getSkillManager().addExperience(Skill.SCAVENGING, Hard);
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void MysteryBox (Player player) {
		if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 80) {
			player.sendMessage("@red@Because of 80+ Scavenge you received x3 XP from this rewarding");
			player.getSkillManager().addExperience(Skill.SCAVENGING, MysteryBox * 3);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 55) {
			player.sendMessage("@red@Because of 55+ Scavenge you received x2 XP from this rewarding");
			player.getSkillManager().addExperience(Skill.SCAVENGING, MysteryBox * 2);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 55) {
			player.sendMessage("@red@You received bonus Scavenge exp from opening an Mystery Box!");
			player.getSkillManager().addExperience(Skill.SCAVENGING, MysteryBox);
		}
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
	public static void Collection (Player player) {
		if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 80) {
			player.sendMessage("@red@Because of 80+ Scavenge you received x3 XP from this Collection entry");
			player.getSkillManager().addExperience(Skill.SCAVENGING, DropFeed * 3);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) >= 55) {
			player.sendMessage("@red@Because of 55+ Scavenge you received x2 XP from this Collection entry");
			player.getSkillManager().addExperience(Skill.SCAVENGING, DropFeed * 2);
		}
		else if (player.getSkillManager().getCurrentLevel(Skill.SCAVENGING) <= 55) {
			player.sendMessage("@red@You received bonus Scavenge exp from this Collection entry!");
			player.getSkillManager().addExperience(Skill.SCAVENGING, DropFeed);
		}
		player.getSkillManager().setCurrentLevel(Skill.SCAVENGING, player.getSkillManager().getMaxLevel(Skill.SCAVENGING));
	}
}

