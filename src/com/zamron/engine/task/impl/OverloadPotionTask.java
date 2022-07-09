package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.model.Animation;
import com.zamron.model.CombatIcon;
import com.zamron.model.Hit;
import com.zamron.model.Hitmask;
import com.zamron.model.PlayerRights;
import com.zamron.model.Skill;
import com.zamron.model.Locations.Location;
import com.zamron.world.content.Consumables;
import com.zamron.world.entity.impl.player.Player;

public class OverloadPotionTask extends Task {

	public OverloadPotionTask(Player player) {
		super(1, player, true);
		this.player = player;
	}
	
	final Player player;

	@Override
	public void execute() {
		if(player == null || !player.isRegistered()) {
			stop();
			return;
		}
		int timer = player.getOverloadPotionTimer();
		if(timer == 600 || timer == 598 || timer == 596 || timer == 594 || timer == 592) {
			player.performAnimation(new Animation(3170));
			player.dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
		}
		if (timer == 600 || timer == 570 || timer == 540 || timer == 510 || timer == 480 || timer == 450 || timer == 420 || timer == 390 || timer == 360 || timer == 330 || timer == 300 || timer == 270 || timer == 240 || timer == 210 || timer == 180 || timer == 150 || timer == 120 || timer == 90 || timer == 60 || timer == 30) {
			Consumables.overloadIncrease(player, Skill.ATTACK, 0.27);
			Consumables.overloadIncrease(player, Skill.STRENGTH, 0.27);
			Consumables.overloadIncrease(player, Skill.DEFENCE, 0.27);
			Consumables.overloadIncrease(player, Skill.RANGED, 0.235);
			player.getSkillManager().setCurrentLevel(Skill.MAGIC, player.getSkillManager().getMaxLevel(Skill.MAGIC) + 7);
		}
		
		player.setOverloadPotionTimer(timer - 1);
		if(player.getOverloadPotionTimer() == 20) 
			player.getPacketSender().sendMessage("Your Overload's effect is about to run out.");
		if(player.getOverloadPotionTimer() <= 0 || player.getLocation() == Location.DUEL_ARENA || player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.WILDERNESS && player.getRights() != PlayerRights.DEVELOPER) {
			player.getPacketSender().sendMessage("Your Overload's effect has run out.");
			player.setConstitution(player.getConstitution() + 500);
			if(player.getConstitution() > 990) {
				player.setConstitution(990);
			}
			for(int i = 0; i < 7; i++) {
				if(i == 3 || i == 5)
					continue;
				player.getSkillManager().setCurrentLevel(Skill.forId(i), player.getSkillManager().getMaxLevel(i));
			}
			player.setOverloadPotionTimer(0);
			stop();
		}
	}
}
