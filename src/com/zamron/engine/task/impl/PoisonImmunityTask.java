package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.world.entity.impl.player.Player;

public class PoisonImmunityTask extends Task {

	public PoisonImmunityTask(final Player p) {
		super(1, p, false);
		this.p = p;
	}
	
	final Player p;

	@Override
	public void execute() {
		if(p == null || !p.isRegistered()) {
			stop();
			return;
		}
		int currentImmunity = p.getPoisonImmunity();
		if(currentImmunity > 0) {
			p.setPoisonImmunity(currentImmunity-1);
		} else {
			p.setPoisonImmunity(0);
			p.getPacketSender().sendMessage("You are no longer immune to poison.");
			stop();
		}
	}
	
	public static void makeImmune(final Player p, int seconds) {
		int currentImmunity = p.getPoisonImmunity();
		boolean startEvent = currentImmunity == 0;
		p.setPoisonImmunity(currentImmunity+seconds);
		p.setPoisonDamage(0);
		if(!startEvent)
			return;
		TaskManager.submit(new PoisonImmunityTask(p));
	}
}
