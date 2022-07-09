package com.zamron.world.content;



import com.zamron.util.Stopwatch;
import com.zamron.world.entity.impl.player.Player;

public class DPSOverlay {

	private Player player;

	public DPSOverlay(Player player) { // un-necessary constructor for player, but don't delete, for future use.
		this.player = player;
	}

	private long damageDone = 0;

	private Stopwatch timer = new Stopwatch();

	public long getDamageDone() {
		return damageDone;
	}

	public void incrementDamageDone(int damage) {
		damageDone += damage;
	}

	public void resetDamageDone() {
		damageDone = 0;
	}

	public void resetTimer() {
		timer.reset();
	}
	
	public long getDPS() {
		////System.out.println("Damage done: " + damageDone + " | Timer: " + timer.elapsed() + " | DPS: " + (1000 * damageDone / timer.elapsed()));
		return 1000 * damageDone / timer.elapsed(); // brb sec
	}

}
