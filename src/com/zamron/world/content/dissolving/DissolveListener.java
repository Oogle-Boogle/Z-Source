
package com.zamron.world.content.dissolving;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class DissolveListener {

	private Player player;

	public DissolveListener(Player player) {
		this.player = player;
	}

	public void dissolve() {
		Arrays.stream(DissolveData.values()).forEach(val -> {
			if (val.getRequired().getId() == player.getDissolveSelection().getId()) {
				if (getRestrictions(val)) {
					player.getInventory().delete(val.getRequired());
					player.getInventory().delete(10835, val.getBagsRequired());
					TimerTask task = new TimerTask() {
						int tick = 0;

						@Override
						public void run() {
							if (tick == 0) {
								player.getPacketSender().sendMessage("You try to dissolve....");
							} else if (tick == 3) {
								boolean success = random.nextInt(100) <= val.getChance() ? true : false;
								if (success) {
									World.sendMessageNonDiscord("<img=12><col=FF0000><shad=200>" + player.getUsername() + " @gre@Successfully<col=FF0000> dissolved their " + val.getReward().getDefinition().getName() + " !");
									player.getInventory().add(val.getReward());
								} else {
									player.getPacketSender().sendMessage("You @red@failed@bla@ your dissolve, better luck next time!");
								}
								cancel();
							}
							tick++;
						}

					};

					Timer timer = new Timer();
					timer.schedule(task, 500, 500);
					;
				}
			} else {
			}
		});
	}
	
	private boolean getRestrictions(DissolveData val) {
		if (!player.getInventory().contains(val.getRequired().getId()) || !player.getInventory().contains(10835, val.getBagsRequired())) {
			player.getPacketSender().sendMessage("You do not have the required items!");
			return false;
		}
		return true;
	}

	private Random random = new Random();
}