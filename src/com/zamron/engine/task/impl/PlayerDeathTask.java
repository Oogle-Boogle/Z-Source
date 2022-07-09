package com.zamron.engine.task.impl;

import java.util.ArrayList;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zamron.GameSettings;

import com.zamron.engine.task.Task;
import com.zamron.model.Animation;
import com.zamron.model.Flag;
import com.zamron.model.GameMode;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.Locations.Location;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.CustomFreeForAll;
import com.zamron.world.content.ItemsKeptOnDeath;
import com.zamron.world.content.event.SpecialEvents;
import com.zamron.world.content.minigames.impl.FreeForAll;
import com.zamron.world.content.minigames.impl.LastManStanding;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * Represents a player's death task, through which the process of dying is handled,
 * the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

	/**
	 * The PlayerDeathTask constructor.
	 * @param player	The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	private Player player;
	private int ticks = 5;
	private boolean dropItems = true;
	Position oldPosition;
	Location loc;
	ArrayList<Item> itemsToKeep = null; 
	NPC death;

	@Override
	public void execute() {
		if(player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				player.getMovementQueue().setLockMovement(true).reset();
				player.endCustomBossRoom();
				player.endKeyRoom(false);
				break;
			case 3:
				player.performAnimation(new Animation(0x900));
				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				//this.death = getDeathNpc(player);
				break;
			case 1:
					if(player.inLMS) {
						Player lmsKiller = player.getCombatBuilder().getKiller(true);
						int amount = lmsKiller.getLMSKillStreak() >= 3 ? 5 : 3; 
						lmsKiller.incrementLMSKillStreak(2);
						lmsKiller.getInventory().add(19104, amount);
						lmsKiller.sendMessage("@blu@Your Last Man Standing killstreak is now: @red@ " + lmsKiller.getLMSKillStreak());
						if(amount >= 3) {
							lmsKiller.sendMessage("@blu@Because you had 3+ KC you got 2 extra keys!");
				}
						}
						
						if (player.inLMS && player.getLMSLifes() > 1) {
							player.restart();
							player.moveTo(LastManStanding.lmsRespawn);
							player.decrementLMSLifes(1);
							lmsDeath(player);
							player.getInventory().add(19104, 3);
							player.resetLMSKillStreak();
							player.sendMessage("@red@Lives left: @blu@" + player.getLMSLifes());
							stop();
							return;
							
						}
					this.oldPosition = player.getPosition().copy();
					this.loc = player.getLocation();
					
					if (player.inLMS && player.getLMSLifes() <= 1) { // add check for if IS NOT IN WILDERNESS
						LastManStanding.leaveGame(player);
						player.restart();
					}
				if (player.inFFA) {
					Player killer = player.getCombatBuilder().getKiller(true);
					FreeForAll.leaveGame(player);
					killer.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, killer.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
					killer.setSpecialPercentage(100);
					killer.getInventory().add(391, 5);
				}
				
				if(player.inCustomFFA) {
					Player killer = player.getCombatBuilder().getKiller(true);
					CustomFreeForAll.leaveGame(player);
					
					killer.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, killer.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
					killer.setSpecialPercentage(100);
					killer.getInventory().add(391, 5);
					
					player.getSkillManager().getSkills().level = player.oldSkillLevels;
					player.getSkillManager().getSkills().experience = player.oldSkillXP;
					player.getSkillManager().getSkills().maxLevel = player.oldSkillMaxLevels;
					CustomFreeForAll.updateSkills(player);
					
					if(CustomFreeForAll.redTeam.contains(player.getUsername())) {
						CustomFreeForAll.redTeam.remove(player.getUsername());
					} else if(CustomFreeForAll.blueTeam.contains(player.getUsername())) {
						CustomFreeForAll.blueTeam.remove(player.getUsername());
					}
				}
				
				if(loc != Location.DUNGEONEERING && !player.inFFA && loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA && loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT && loc != Location.SOULWARS && loc != Location.FIGHT_PITS && loc != Location.FIGHT_PITS_WAIT_ROOM && loc != Location.FIGHT_CAVES && loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD) {
					Player killer = player.getCombatBuilder().getKiller(true);
					if(player.getUsername().equalsIgnoreCase("") || player.getRights().equals(PlayerRights.OWNER) || player.getRights().equals(PlayerRights.DEVELOPER))
						dropItems = false;
					if(loc == Location.WILDERNESS) {
						if(killer != null && (killer.getRights().equals(PlayerRights.OWNER) || killer.getRights().equals(PlayerRights.DEVELOPER)))
							dropItems = false;
					}
					if(loc != Location.WILDERNESS) {
						dropItems = false;
					}
					if(killer != null) {
						if(killer.getRights().equals(PlayerRights.OWNER) || killer.getRights().equals(PlayerRights.DEVELOPER)) {
							dropItems = false;
						}
					}
					boolean spawnItems = loc != Location.NOMAD && !(loc == Location.GODWARS_DUNGEON && player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
					if(dropItems) {
						itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
						final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
						playerItems.addAll(player.getInventory().getValidItems());
						playerItems.addAll(player.getEquipment().getValidItems());
						final Position position = player.getPosition();
						for (Item item : playerItems) {
							if(!item.tradeable() || itemsToKeep.contains(item)) {
								if(!itemsToKeep.contains(item)) {
									itemsToKeep.add(item);
								}
								continue;
							}
							if(spawnItems) {
								if(item != null && item.getId() > 0 && item.getAmount() > 0) {
									GroundItemManager.spawnGroundItem((killer != null && killer.getGameMode() == GameMode.NORMAL ? killer : player), new GroundItem(item, position, killer != null ? killer.getUsername() : player.getUsername(), player.getHostAddress(), false, 150, true, 150));
								}
							}
						}
						if (killer != null) {
							killer.getPlayerKillingAttributes().add(player);
							killer.getPlayerKillingAttributes()
									.setPlayerKills(killer.getPlayerKillingAttributes().getPlayerKills()
											+ SpecialEvents.getDay() == SpecialEvents.TUESDAY ? 2 : 1);
							player.getPlayerKillingAttributes()
									.setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
							player.getPlayerKillingAttributes().setPlayerKillStreak(0);
							player.getPointsHandler().refreshPanel();
						}
						player.getInventory().resetItems().refreshItems();
						player.getEquipment().resetItems().refreshItems();
					}
				} else
					dropItems = false;
				player.getPacketSender().sendInterfaceRemoval();
				player.setEntityInteraction(null);
				player.getMovementQueue().setFollowCharacter(null);
				player.getCombatBuilder().cooldown(false);
				player.setTeleporting(false);
				player.setWalkToTask(null);
				player.getSkillManager().stopSkilling();
				break;
			case 0:
				if(dropItems) {
					if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						//GameMode.set(player, player.getGameMode(), true);
					} else { 
						if(itemsToKeep != null) {
							for(Item it : itemsToKeep) {
								player.getInventory().add(it.getId(), 1);
							}
							itemsToKeep.clear();
						}
					}
				}
				if(death != null) {
					World.deregister(death);
					System.out.println("Death isnt null");
				}
				player.restart();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				loc.onDeath(player);
				if (loc != Location.DUNGEONEERING && loc != Location.BOSS_TIER_LOCATION) {
					if (player.getPosition().equals(oldPosition))
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				}
				player = null;
				oldPosition = null;
				//stop();
				break;
			}
			ticks--;
		} catch(Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if(player != null) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
			}	
		}
	}
	
	public void lmsDeath(Player player) {
		player.getInventory().deleteAll().refreshItems();
		player.getEquipment().deleteAll().refreshItems();
	}

	public static NPC getDeathNpc(Player player) {
		NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
		World.register(death);
		death.setEntityInteraction(player);
		death.performAnimation(new Animation(401));
		death.forceChat(randomDeath(player.getUsername()));
		return death;
	}
	

	public static String randomDeath(String name) {
		switch (RandomUtility.getRandom(8)) {
		case 0:
			return "There is no escape, " + Misc.formatText(name)
					+ "...";
		case 1:
			return "Muahahahaha!";
		case 2:
			return "You belong to me!";
		case 3:
			return "Beware mortals, " + Misc.formatText(name)
					+ " travels with me!";
		case 4:
			return "Your time here is over, " + Misc.formatText(name)
					+ "!";
		case 5:
			return "Now is the time you die, " + Misc.formatText(name)
					+ "!";
		case 6:
			return "I claim " + Misc.formatText(name) + " as my own!";
		case 7:
			return "" + Misc.formatText(name) + " is mine!";
		case 8:
			return "Let me escort you back to Edgeville, "
					+ Misc.formatText(name) + "!";
		case 9:
			return "I have come for you, " + Misc.formatText(name)
					+ "!";
		}
		return "";
	}

}