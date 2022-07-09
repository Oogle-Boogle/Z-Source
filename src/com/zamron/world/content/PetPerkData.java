package com.zamron.world.content;

import java.util.Arrays;

public class PetPerkData {
	
	private static final Object[][] PERK_DATA = new Object[][] {
		//npcId,
		{2322, 0, 1.1, 1.2, true, 1.3, 1.1}, //Chucky
		{230, 0, 1.2, 1.0, true, 1.25, 1.2},//Yoda
		{6304, 0, 2.0, 2.0, true, 2.0, 2.0},//Abbadon
		{5960, 0, 1.0, 1.0, true, 2.0, 2.0},////Rainbow Eeve
		{9945, 0, 1.5, 1.5, true, 2.0, 2.0}, //Assassin
		{744, 0, 1.5, 1.5, true, 1.25, 2.0},//Godzilla
		{1060, 0, 1.5, 1.5, true, 1.25, 2.0},//Broly
		{642, 0, 1.5, 1.5, true, 1.25, 2.0},//Flareon
		{644, 0, 1.5, 1.5, true, 1.25, 2.0},//Vapereon
		{811, 0, 1.5, 1.5, true, 1.25, 2.0},//Antman
		{1785, 0, 5.0, 1.8, true, 2.0, 5.0},//Bugatti
		{810, 0, 1.5, 1.5, true, 1.25, 2.0},//Mewtwo
			{815, 0, 1.5, 1.5, true, 1.25, 2.0},//Zorbak
			{816, 0, 1.5, 1.5, true, 1.25, 2.0},//Stoned toad
			{755, 0, 1.5, 1.5, true, 3.0, 2.0},//Cookie monster
	};
	
	public static boolean hasPerks(int petId) {
		return Arrays.stream(PERK_DATA).anyMatch(x -> (int)x[0] == petId);
	}
	
	public static int getDropRate(int petId) {
		return (int) Arrays.stream(PERK_DATA).map(bonus -> bonus[1]).filter(bonus -> (int)bonus != 0 && hasPerks(petId)).findFirst().orElse(0);
	}
	
	public static double getXpBonus(int petId) {
		return (double) Arrays.stream(PERK_DATA).map(bonus -> bonus[2]).filter(bonus -> (double)bonus != 0 && hasPerks(petId)).findFirst().orElse(0);
	}
	
	public static double getDamageBonus(int petId) {
		return (double) Arrays.stream(PERK_DATA).map(bonus -> bonus[3]).filter(bonus -> (double)bonus != 0 && hasPerks(petId)).findFirst().orElse(0);
	}
	
	public static boolean hasLootEffect(int petId) {
		return (boolean) Arrays.stream(PERK_DATA).map(bonus -> bonus[4]).filter(bonus -> (boolean)bonus != false && hasPerks(petId)).findFirst().orElse(false);
	}
	
	public static double getPrayDrainRate(int petId) {
		return (double) Arrays.stream(PERK_DATA).map(bonus -> bonus[5]).filter(bonus -> (double)bonus != 0 && hasPerks(petId)).findFirst().orElse(0);
	}
	
	public static double getHpDrainRate(int petId) {
		return (double) Arrays.stream(PERK_DATA).map(bonus -> bonus[6]).filter(bonus -> (double)bonus != 0 && hasPerks(petId)).findFirst().orElse(0);
	}
	
}
