package com.zamron.world.content;

import com.zamron.model.Animation;
import com.zamron.model.Skill;
import com.zamron.world.entity.impl.player.Player;

/**
 * Handles a player's run energy
 * @author Gabriel Hannason
 * Thanks to Russian for formula!
 */
public class EnergyHandler {

	public static void processPlayerEnergy(Player p) {
		if(p.isRunning() && p.getMovementQueue().isMoving()) {
			int energy = p.getRunEnergy();
			if (energy > 0) {
				p.setRunEnergy(energy);
				p.getPacketSender().sendRunEnergy(energy);
			} else {
				p.setRunning(false);
				p.getPacketSender().sendRunStatus();
				p.getPacketSender().sendRunEnergy(0);
			}
		}
		if (p.getRunEnergy() < 100) {
			if (System.currentTimeMillis() >= (restoreEnergyFormula(p) + p.getLastRunRecovery().getTime())) {
				int energy = p.getRunEnergy() + 1;
				p.setRunEnergy(energy);
				p.getPacketSender().sendRunEnergy(energy);
				p.getLastRunRecovery().reset();
			}
		}
	}
	
	public static void rest(Player player) {
		if(player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
			player.getPacketSender().sendMessage("You cannot do this right now.");
			return;
		}
		player.setResting(true);
		player.performAnimation(new Animation(11786));
		player.getPacketSender().sendMessage("You begin resting..");
	}
	
	public static double restoreEnergyFormula(Player p) {
		return 2260 - (p.getSkillManager().getCurrentLevel(Skill.AGILITY) * (p.isResting() ? 13000 : 10));
	}
}
