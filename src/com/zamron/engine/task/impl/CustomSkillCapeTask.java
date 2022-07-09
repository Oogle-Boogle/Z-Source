package com.zamron.engine.task.impl;

import com.zamron.engine.task.Task;
import com.zamron.model.Animation;
import com.zamron.model.Flag;
import com.zamron.model.Graphic;
import com.zamron.model.Item;
import com.zamron.model.container.impl.Equipment;
import com.zamron.world.entity.impl.player.Player;

public class CustomSkillCapeTask extends Task {

	final Player player;

	int tick = 82;

	public static boolean taskActive;

	public CustomSkillCapeTask(Player player) {
		super(2, player, false);
		this.player = player;
	}

	@Override
	protected void execute() {
		taskActive = true;
		tick -= 2;
		
		if (tick == 80) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9792, 1));
			if (player.getEquipment().contains(9792)) {
				player.performAnimation(new Animation(4941));
				player.performGraphic(new Graphic(814));
			}
		}
		
		if (tick == 72) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9948, 1));
			if (player.getEquipment().contains(9948)) {
				player.performAnimation(new Animation(5158));
				player.performGraphic(new Graphic(907));
			}
		}
		
		if (tick == 60) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9750, 1));
			if (player.getEquipment().contains(9750)) {
				player.performAnimation(new Animation(4981));
				player.performGraphic(new Graphic(828));
			}
		}
		if (tick == 44) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9759, 1));
			if (player.getEquipment().contains(9759)) {
				player.performAnimation(new Animation(4979));
				player.performGraphic(new Graphic(2982));
			}
		}

		if (tick == 32) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9762, 1));
			if (player.getEquipment().contains(9762)) {
				player.performAnimation(new Animation(4939));
				player.performGraphic(new Graphic(813));
			}
		}

		if (tick == 24) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9771, 1));
			if (player.getEquipment().contains(9771)) {
				player.performAnimation(new Animation(4977));
				player.performGraphic(new Graphic(830));
			}
		}

		if (tick == 18) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9786, 1));
			if (player.getEquipment().contains(9786)) {
				player.performAnimation(new Animation(4967));
				player.performGraphic(new Graphic(1656));
			}
		}
		if (tick == 10) {
			player.getEquipment().set(Equipment.CAPE_SLOT, new Item(9765, 1));
			if (player.getEquipment().contains(9765)) {
				player.performAnimation(new Animation(4947));
				player.performGraphic(new Graphic(817));

			}
		}

		if (tick == 0) {
			player.getEquipment().resetItems();
			taskActive = false;
			player.sendMessage("@red@Task ended");
			stop();
		}
		//System.out.println(taskActive);
		player.getEquipment().refreshItems();
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

}
