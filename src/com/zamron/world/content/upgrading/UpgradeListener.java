
package com.zamron.world.content.upgrading;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.zamron.net.packet.impl.Upgrade;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class UpgradeListener {

	private Player player;

	public UpgradeListener(Player player) {

		this.player = player;
	}

	public void upgrade() {
		Arrays.stream(UpgradeData.values()).forEach(val -> {
			if (val.getRequired().getId() == player.getUpgradeSelection().getId()) {
				if (getRestrictions(val)) {
					player.getInventory().delete(val.getRequired());
					player.getInventory().delete(10835, val.getBagsRequired());
					TimerTask task = new TimerTask() {
						int tick = 0;

						@Override
						public void run()
						{
							if (tick == 0) 
							{
								player.getPacketSender().sendMessage("@red@You try to upgrade....");
							} else if (tick == 3) 
							{
								boolean success = random.nextInt(100) <= val.getChance() ? true : false;
								if (success) 
								{
									player.getPacketSender().sendMessage("@blu@You succesfully upgraded your item!");
									player.getInventory().add(val.getReward());
									//World.sendMessageNonDiscord("<img=12>@blu@" + player.getUsername() + " has just succeeded upgrading an Item!");
									World.sendMessageNonDiscord("<img=12>@blu@"+player.getUsername()+ " has just succeeded upgrading and received an " + val.getReward().getDefinition().getName()+ "!!");
								} else 
								{
									player.getPacketSender().sendMessage("@red@You failed to upgrade!");
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
	
	private boolean getRestrictions(UpgradeData data) {
		if (!player.getInventory().contains(data.getRequired().getId()) || !player.getInventory().contains(10835, data.getBagsRequired())) {
			player.getPacketSender().sendMessage("You do not have the required items!");
			return false;
		}
		return true;
	}

	private Random random = new Random();
}