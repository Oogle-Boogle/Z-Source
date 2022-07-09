package com.zamron.world.content.skill.impl.farming;

import java.util.Calendar;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Animation;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.Achievements;
import com.zamron.world.content.Achievements.AchievementData;
import com.zamron.world.entity.impl.player.Player;

public class Plant {

	public int patch;
	public int plant;
	public int minute;
	public int hour;
	public int day;
	public int year;
	public byte stage = 0;
	public byte disease = -1;
	public byte watered = 0;

	private boolean dead = false;

	public byte harvested = 0;
	boolean harvesting = false;

	public Plant(int patchId, int plantId) {
		patch = patchId;
		plant = plantId;
	}

	public boolean doDisease() {
		return false;
	}

	public boolean doWater() {
		return false;
	}

	public void water(Player player, int item) {
		if (item == 5332) {
			return;
		}

		if(player.getClickDelay().elapsed(2000)) {
			if (isWatered()) {
				player.getPacketSender().sendMessage("Your plants have already been watered.");
				return;
			}

			if (item == 5331) {
				player.getPacketSender().sendMessage("Your watering can is empty.");
				return;
			}

			player.getPacketSender().sendMessage("You water the plant.");
			player.getFarming().nextWateringCan(item);
			player.performAnimation(new Animation(2293));
			watered = -1;
			doConfig(player);
			player.getClickDelay().reset();
		}
	}

	public void setTime() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(11);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

	public void click(Player player, int option) {
		if (option == 1) {
			if (dead)
				player.getPacketSender().sendMessage("Oh dear, your plants have died!");
			else if (isDiseased())
				player.getPacketSender().sendMessage("Your plants are diseased!");
			else if (stage == Plants.values()[plant].stages)
				harvest(player);
			else {
				String s = "Your plants are healthy";
				if(!isWatered())
					s += " but need some water to survive";
				else
					s += " and are currently growing";
				s+= ".";
				player.getPacketSender().sendMessage(s);
			}
		} else if ((option == 2) && (stage == Plants.values()[plant].stages))
			player.getPacketSender().sendMessage("Your plants are healthy and ready to harvest.");
	}

	public void harvest(final Player player) {
		if(harvesting)
			return;
		if (player.getInventory().contains(FarmingPatches.values()[patch].harvestItem)) {
			final Plant instance = this;
			player.performAnimation(new Animation(FarmingPatches.values()[patch].harvestAnimation));
			harvesting = true;
			TaskManager.submit(new Task(1, player, true) {
				@Override
				public void execute() {
					if(player.getMovementQueue().isMoving()) {
						stop();
						return;
					}
					if(player.getInventory().getFreeSlots() == 0) {
						player.getInventory().full();
						stop();
						return;
					}
					player.performAnimation(new Animation(FarmingPatches.values()[patch].harvestAnimation));
					Item add = null;
					int id = Plants.values()[plant].harvest;
					add = ItemDefinition.forId(id).isNoted() ? new Item(id-1, 1) : new Item(id, 1);
					
					if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 7409) {
						player.getInventory().add(add.getId(), add.getAmount() * 2);
						player.getPA().sendMessage("The magic secateurs grants you 2x herbs");
					} else {
					player.getInventory().add(add.getId(), add.getAmount());

					}
				
					
					String name = ItemDefinition.forId(Plants.values()[plant].harvest).getName();
					if(name.endsWith("s"))
						name = name.substring(0, name.length() - 1);
					player.getPacketSender().sendMessage("You harvest " + Misc.anOrA(name) + " " + name + ".");
					player.getSkillManager().addExperience(Skill.FARMING, (int) Plants.values()[plant].harvestExperience);
					Achievements.finishAchievement(player, AchievementData.HARVEST_A_CROP);
					if(Misc.getRandom(3500) == 3) {
						player.getInventory().add(13323, 1);
						World.sendMessageNonDiscord("@blu@<img=12>[Skilling Pets] "+player.getUsername()+" has received the Tangleroot pet!");
						player.getPacketSender().sendMessage("@red@You have received a skilling pet!");
					} 
					if (harvested == 3 && player.getInventory().contains(18336) && Misc.getRandom(4) == 0) {
						player.getPacketSender().sendMessage("You receive a seed back from your Scroll of life.");
						player.getInventory().add(Plants.values()[plant].seed, 1);
					}
					if(id == 219) {
						Achievements.doProgress(player, AchievementData.HARVEST_10_TORSTOLS);
						Achievements.doProgress(player, AchievementData.HARVEST_1000_TORSTOLS);
					}
					harvested++;
					if (harvested >= 3 && Misc.getRandom(4) <= 1) {
						player.getFarming().remove(instance);
						stop();
						return;
					}
				}

				@Override
				public void stop() {
					harvesting = false;
					setEventRunning(false);
					player.performAnimation(new Animation(65535));
				}
			});
		} else {
			String name = ItemDefinition.forId(FarmingPatches.values()[patch].harvestItem).getName();
			player.getPacketSender().sendMessage("You need " + Misc.anOrA(name) + " " + name + " to harvest these plants.");
		}
	}

	public boolean useItemOnPlant(final Player player, int item) {
		if (item == 952) {
			player.performAnimation(new Animation(830));
			player.getFarming().remove(this);
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {
					player.getPacketSender().sendMessage("You remove your plants from the plot.");
					player.performAnimation(new Animation(65535));
					stop();
				}
			});
			return true;
		}
		if (item == 6036) {
			if (dead) {
				player.getPacketSender().sendMessage("Your plant is dead!");
			} else if (isDiseased()) {
				player.getPacketSender().sendMessage("You cure the plant.");
				player.performAnimation(new Animation(2288));
				player.getInventory().delete(6036, 1);
				disease = -1;
				doConfig(player);
			} else {
				player.getPacketSender().sendMessage("Your plant does not need this.");
			}

			return true;
		}
		if ((item >= 5331) && (item <= 5340)) {
			water(player, item);
			return true;
		}

		return false;
	}

	public void process(Player player) {
		if (dead || stage >= Plants.values()[plant].stages) {
			return;
		}
		int elapsed = Misc.getMinutesElapsed(minute, hour, day, year) * 6;
		int grow = Plants.values()[plant].minutes;
		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				/*if (isDiseased()) {
				/*} else 
				if (!isWatered()) {
					player.getPacketSender().sendMessage("You need to water your plant.");*/
				if(isWatered()) {
					stage++;
					player.getFarming().doConfig();
					if (stage >= Plants.values()[plant].stages) {
						player.getPacketSender().sendMessage("<img=12> <shad=996699>A seed which you planted has finished growing!");
						return;
					}
				}

			}
			setTime();
		}
	}

	public void doConfig(Player player) {
		player.getFarming().doConfig();
	}

	public int getConfig() {
		if ((Plants.values()[plant].type == SeedType.ALLOTMENT) && (stage == 0) && (isWatered())) {
			return (Plants.values()[plant].healthy + stage + 64) * FarmingPatches.values()[patch].mod;
		}
		return (Plants.values()[plant].healthy + stage) * FarmingPatches.values()[patch].mod;
	}

	public FarmingPatches getPatch() {
		return FarmingPatches.values()[patch];
	}

	public boolean isDiseased() {
		return disease > -1;
	}

	public boolean isWatered() {
		return watered == -1;
	}
}
