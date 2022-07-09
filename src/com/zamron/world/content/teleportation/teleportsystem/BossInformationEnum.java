package com.zamron.world.content.teleportation.teleportsystem;

import com.zamron.model.Position;

public enum BossInformationEnum {

	KBD(-28705, 50, "King Black Dragon", new String[] {"Are You Ready To Face", "A @gre@ Fire Breathing Dragon?", "bring @gre@ anti fires", "Or a @gre@ Dragonfire Shield!"}, new Position(3005, 3850, 0)),
	VETION(-28704, 2006, "Vetion",new String[] {"The armored skeleton", "this level 454 boss has", "255 hp and attacks with", "and magic. The max hit is 45"}, new Position(3177, 3780, 0)),
	SKOTIZO(-28703, 7286, "Skotizo",new String[] {"This level 204 boss", "has 225 hp and attacks", "with range and melee. The max", "hit of this boss is 15"}, new Position(3257, 2772, 0)),
	SIRE(-28702, 5886, "Abyssal Sire",new String[] {"The abyssal demon boss", "this level 350 boss attacks", "you with melee and has 450 hp", "The boss has a max hit of 66"}, new Position(2980, 4830, 0)),
	KALPHITEQUEEN(-28701, 1158, "Kalphite Queen",new String[] {"The nightmare of every", "sailor, this level 291 boss", "has 255 hp and attacks with", "magic, his max hit is 32"}, new Position(3096, 3495, 0)),//
	CERBERUS(-28700, 1999, "Cerberus (instanced)",new String[] {"The guardian of hell!", "This level 318 boss has", "600 hp and attacks you with all", "3 styles, his max hit is 28"}, new Position(2872, 9847, 0)),
	BARRELCHEST(-28699, 5666, "Barrelchest",new String[] {"The boss who is known from", "the quest Great Brain Robbery.", "This level 190 boss has 134 hp", "The boss uses melee", "and has a max hit of 60"}, new Position(3263, 3676, 0)),
	CALLISTO(-28698, 2009, "Callisto",new String[] {"The big bear who wanders", "around the south of the Demonic", "Ruins. This level 470 boss attacks", "you with melee and has 255 hp.", "The boss uses melee and", "has a max hit of 60"}, new Position(3315, 3829, 0)),
	CHAOSELEMENTAL(-28697, 3200, "Chaos Elemental",new String[] {"The cloud with tentacles", "This level 305 boss has 250", "hp and attacks you with all", "styles. The boss has a max hit of 28"}, new Position(3265, 3926, 0)),
	CORP(-28696, 8133, "Corporeal Beast",new String[] {"The most challenging boss", "This level 785 boss has 2000", "hp and attacks you with melee", "and magic. The melee max hit is", "51 and magic max hit is 65"}, new Position(2976, 4384, 0)),
	NEX(-28695, 13447, "Nex",new String[] {"This insane mage resides", "west of the lava maze", "This level 202 boss has 225 hp", "and attack you with magic.", "He also has a special attack!"}, new Position(2990, 3850, 0)),
	GODWARS(-28694, 6260, "Godwars",new String[] {"", "", "", "", ""}, new Position(1, 1, 1)),
	LIZARDSHAMAN(-28693, 6766, "Lizardman Shaman", new String[] {"That's a big lizard", "This level 150 boss has", "150 hp and attacks with melee", "and magic. the max his is 50",}, new Position(1495, 3700, 0)),
	VENENATIS(-28692, 2000, "Venenatis", new String[] {"The biggest, most poisonous", "This level 464 boss has", "225 hp and attacks with melee", "and magic. the max his is 50",}, new Position(3352, 3730, 0)),
	BORK(-28691, 7134, "Bork", new String[] {"Mutant Monster", "This level 267 boss has", "8,670 hp and attacks with melee", "and magic. the max hit is 510",}, new Position(3090, 5530, 0));
	
	public int buttonId, npcId;
	public String bossName;
	public String[] information;
	public Position pos;

	BossInformationEnum(int buttonId, int npcId, String bossName, String[] information, Position pos) {
		this.buttonId = buttonId;
		this.npcId = npcId;
		this.bossName = bossName;
		this.information = information;
		this.pos = pos;
	}
	
	public int getButtonId() {
		return buttonId;
	}

	public String getBossName() {
		return bossName;
	}
	
	public String[] getInformation() {
		return information;
	}
	
	public Position getPos() {
		return pos;
	}
}