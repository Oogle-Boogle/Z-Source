package com.zamron.world.content;

import com.zamron.engine.task.Task;
import com.zamron.world.entity.impl.player.Player;

public class TimelyReward1 extends Task {

	private Player player;
	
	public TimelyReward1(Player player) {
		super(100, player, true);
		this.player = player;
	}
	
	@Override
	protected void execute() {
		
		//System.out.println("executed it here");
		if(!player.hasRewardTimerActive1()) {
			//System.out.println("Wasn't active");
			player.getPacketSender().sendString(57202, "Claim via ::claimreward1");
			return;
		}
		
		if(player == null) {
			//System.out.println("Was null");
			return;
		}
		
		player.decrementRewardTimer1(1);
		if(player.getRewardTimer1() < 1) {
			player.getPacketSender().sendString(57202, "Claim via ::claimreward1");
		} else {
			player.getPacketSender().sendString(57202, "Time left: " + player.getRewardTimer1() + " mins");
		}
		
		
		
	}
	
	
	
}
