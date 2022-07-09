package com.zamron.world.content.scratchcard;

public enum ScratchCardData {

	//TODO THESE AND IMPLEMENT SCRATCH CARDS INGAME - AFK STORE ? BOSS POINT STORE? RANDOM DROP ?

	SANTA(1050), TIERKEY(3458), MARIOHEAD(17933), LUIGIHEAD(17932), BlueInfusionStone(12845), OVERLOADRING(19140),
	AURA(3309), SUPPLYCRATE(7630), ICY_GLAIVE(12426), TIERKEY2(3459), TIERKEY3(3455), ADZE(13661), INFERNALPICKAXE(5080);

	private int displayId;


	ScratchCardData(int displayId) {
		this.displayId = displayId;
	}

	public int getDisplayId() {
		return displayId;
	}

	public void setDisplayId(int displayId) {
		this.displayId = displayId;
	}

}