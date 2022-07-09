package com.zamron.world.content;

import java.util.Arrays;
import java.util.List;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.PoisonImmunityTask;
import com.zamron.model.*;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Stopwatch;
import com.zamron.world.World;
import com.zamron.world.content.aoesystem.AOESystem;
import com.zamron.world.content.combat.weapon.CombatSpecial;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Flub
 * @Edited Oogleboogle
 */

public class InstanceSystem {

	public InstanceSystem(Player player) {
		this.player = player;
	}

	private Player player;

	private Stopwatch instanceTimer = new Stopwatch();

	/** Currency used to enter the instance **/
	public static int INSTANCE_TOKEN_ID = 11179;

	/** List to store NPC ID's **/
	private static List<Integer> npcs = Arrays.asList(607, 610, 609, 1069, 11, 3154, 33, 1684, 5957, 5958, 5959, 185, 6311);

	/** Sets the total cost of tokens based on rank **/
	public static int COST_TO_ENTER(Player player) {
		int modifiedCost;
		switch (player.getRights()) {
			case DONATOR:
				modifiedCost = 20;
				break;
			case SUPER_DONATOR:
				modifiedCost = 15;
				break;
			case EXTREME_DONATOR:
				modifiedCost = 10;
				break;
			case LEGENDARY_DONATOR:
				modifiedCost = 7;
				break;
			case UBER_DONATOR:
				modifiedCost = 5;
				break;
			case DELUXE_DONATOR:
				modifiedCost = 3;
				break;
			case SUPPORT:
			case MODERATOR:
				modifiedCost = 2;
				break;
			case ADMINISTRATOR:
			case OWNER:
			case DEVELOPER:
				modifiedCost = 1;
				break;
			default: //Applies to anyone not listed above
				modifiedCost = 25;
		}
		player.getPacketSender().sendMessage("Your price to enter is " + modifiedCost + " tokens");
		return modifiedCost;
	}

	/** Opens the interface and sends the data **/
	public static void open(Player player) {

		int startId = 58720;

		for (int i = 0; i < npcs.size(); i++) {
			player.getPacketSender().sendString(startId, NpcDefinition.forId(npcs.get(i)).getName());
			startId++;
		}

		player.getPacketSender().sendInterface(58705);
	}

	/** Deals with clicks, selecting the relevant NPC and sending the new data **/
	public boolean handleClick(int id) {

		if (!(id >= -6816 && id <= -6804)) {
			return false;
		}
		int index = -1;

		if (id >= -6816) {
			index = 6816 + id;
		}
		final int npcId = npcs.get(index);
		NpcDefinition def = NpcDefinition.forId(npcs.get(index));

		player.getPacketSender().sendString(58716, "Npc killcount: @gre@" + player.getNpcKillCount(npcId))
				.sendString(58717, "Npc hitpoints: @gre@" + def.getHitpoints())
				.sendString(58718, "Npc level: @gre@" + def.getCombatLevel()).sendString(58926, def.getName());
		player.getPacketSender().sendNpcOnInterface(58927, npcId, 0);

		sendDrops(npcId);

		this.npcId = npcId;

		return true;
	}


	/** Sends the drops to the interface **/
	private void sendDrops(int npcId) {
		player.getPacketSender().resetItemsOnInterface(58936, 100);
		try {
			NPCDrops drops = NPCDrops.forId(npcId);
			if (drops == null) {
				//System.out.println("Was null");
				return;
			}
			for (int i = 0; i < drops.getDropList().length; i++) {

				player.getPacketSender().sendItemOnInterface(58936, drops.getDropList()[i].getId(), i,
						drops.getDropList()[i].getItem().getAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** used to set the initial NPC the player sees upon opening.
	 * This is the first entry in the npcs array **/
	private int npcId = npcs.get(0);

	/** Used to store NPCs for use later **/
	@Getter
	private NPC[] npcsToSpawn;

	/** Gets the required amount of NPC's to spawn and applies
	 * minimum and maximum parameters
	 */
	public int getSpawnAmount() {
		if (spawnAmount < 1)
			spawnAmount = 1;

		if (spawnAmount > 8)
			spawnAmount = 8;

		return spawnAmount;
	}

	/** Stores the amount to spawn. Defualt value of 1 **/
	@Setter
	private int spawnAmount = 1;

	/** Stores the amount of times the player removed their AOE Weapon. **/
	private int unequippedAoeWeapon;

	/** Creates a task to make the NPC's in the instance attack the player **/
	public static void aggroNpcs(Player player) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				player.getRegionInstance().getNpcsList().forEach(npc -> npc.getCombatBuilder().attack(player));
				this.stop();
			}
		});
	}

	/** Stars the instance, handles all code relating to initial spawn. **/
	public void startInstance() {
		unequippedAoeWeapon = 0;
		int currentTokens = player.getInventory().getAmount(INSTANCE_TOKEN_ID);
		int costToEnter = COST_TO_ENTER(player);

		if (currentTokens >= costToEnter && AOESystem.hasAoeWeapon(player)){
			player.getInventory().delete(INSTANCE_TOKEN_ID, costToEnter);
		} else if (currentTokens <= costToEnter) {
			int missingTokens = costToEnter - currentTokens;
			player.getPacketSender().sendMessage("You need @red@" + missingTokens + " @bla@more"+ (missingTokens <= 1 ? " token " : " tokens ") + "to enter.");
			return;
		} else 	if (!AOESystem.hasAoeWeapon(player)) {
			player.getPacketSender().sendMessage("You must equip an AOE weapon to use the Instance Arena!");
			return;
		}

		instanceTimer.reset();

		player.getPacketSender().sendInterfaceReset().sendInterfaceRemoval();

		if (player.getRegionInstance() != null) {
			player.getRegionInstance().destruct();
		}

		player.setRegionInstance(new RegionInstance(player, RegionInstance.RegionInstanceType.INSTANCE_ARENA));

		int height = player.getIndex() * 4;

		int x = 2526; //Base number for X Coord
		int y = 3671; //Base number for Y Coord

		Position firstPosition = new Position(x, y, height);
		Position secondPosition = new Position(x+4, y, height);
		Position thirdPosition = new Position(x+8, y, height);
		Position fourthPosition = new Position(x+12, y, height);
		Position fifthPosition = new Position(x, y+4, height);
		Position sixthPosition = new Position(x+4, y+4, height);
		Position seventhPosition = new Position(x+8, y+4, height);
		Position eighthPosition = new Position(x+12, y+4, height);

		switch (spawnAmount) {
			case 1:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition)};
				break;
			case 2:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition)};
				break;
			case 3:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition)};
				break;
			case 4:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition),
						new NPC(this.npcId, fourthPosition)};
				break;
			case 5:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition),
						new NPC(this.npcId, fourthPosition),
						new NPC(this.npcId, fifthPosition)};
				break;
			case 6:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition),
						new NPC(this.npcId, fourthPosition),
						new NPC(this.npcId, fifthPosition),
						new NPC(this.npcId, sixthPosition)};
				break;
			case 7:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition),
						new NPC(this.npcId, fourthPosition),
						new NPC(this.npcId, fifthPosition),
						new NPC(this.npcId, sixthPosition),
						new NPC(this.npcId, seventhPosition)};
				break;
			case 8:
				npcsToSpawn = new NPC[]{
						new NPC(this.npcId, firstPosition),
						new NPC(this.npcId, secondPosition),
						new NPC(this.npcId, thirdPosition),
						new NPC(this.npcId, fourthPosition),
						new NPC(this.npcId, fifthPosition),
						new NPC(this.npcId, sixthPosition),
						new NPC(this.npcId, seventhPosition),
						new NPC(this.npcId, eighthPosition)};
				break;
		}

		for (NPC npc : npcsToSpawn) {
			World.register(npc);
			npc.setInstancedNPC(true);
			npc.forceChat("Get ready " + player.getUsername() + "!");
			npc.setSpawnedFor(player);
			npc.performGraphic(new Graphic(453));
			npc.getCombatBuilder().attack(player);
			player.getRegionInstance().getNpcsList().add(npc);
		}

		aggroNpcs(player);

		player.moveTo(new Position(2524, 3671, height));

	}

	/** Handles deaths in the instance areana. **/
	public void respawn() {
		try {

			if (instanceTimer.elapsed() > 3600000) { //If the timer reaches 1 hour.
				player.getPacketSender().sendMessage("1 hour is up! Moving you home..");
				destructInstance(player);
				return;
			}

			if (unequippedAoeWeapon >= 2) { // If the player has removed their AOE weapon twice.
				player.getPacketSender().sendMessage("You have removed your AOE weapon for a second time and have been removed.");
				destructInstance(player);
				return;
			}

			if (!AOESystem.hasAoeWeapon(player)) { // First time removing the AOE weapon.
				player.getPacketSender().sendMessage("You must equip your AOE weapon within 10 seconds or be teleported home!");

				unequippedAoeWeapon++;

				TaskManager.submit(new Task(10, player, false) {
					@Override
					public void execute() {
						if (!AOESystem.hasAoeWeapon(player)) {
							destructInstance(player);
						}
						respawn();
						this.stop();
					}
				});
				return;
			}

			if (!player.getRegionInstance().getType().equals(RegionInstance.RegionInstanceType.INSTANCE_ARENA)  //Check if the player is still in an instance before respawning the NPC.
					|| !player.getLocation().equals(Locations.Location.INSTANCE_ARENA)) { //Check if the player is in the Instance Arena Location
				return;
			}

			for (NPC npc : npcsToSpawn) {
				World.register(npc);
				npc.setInstancedNPC(true);
				npc.forceChat(player.getUsername() + ", I'm coming for you!");
				npc.performGraphic(new Graphic(453));
				npc.setSpawnedFor(player);
				player.getRegionInstance().getNpcsList().add(npc);
			}
			aggroNpcs(player); //Makes all NPC's get pissed off and chase the player.
		} catch (Exception e) {
			//System.out.println("Caught an exception : " + e.getMessage());
		}
	}

	/** Restores the players HP when leaving the area. **/
	public static void restoreHP(Player player) {
		if (player.getRights().isMember() || player.getRights().isSeniorStaff()) {
			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
			}
		}
	}

	/** Restores the players skills when leaving the area. **/
	public static void restoreStats(Player player) {
		if (player.getRights().isMember() || player.getRights().isSeniorStaff()) {
			if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
			}
		}
	}

	/** Restores the players special attack when leaving the area. **/
	public static void restoreSpec(Player player) {
		if (player.getRights().isMember() || player.getRights().isSeniorStaff()) {
			player.setSpecialPercentage(100);
			CombatSpecial.updateBar(player);
			player.performGraphic(new Graphic(1302));
		}
	}

	/** Runs the previous restore methods in one method purely for vanity. **/
	public static void restore(Player player) {
		restoreHP(player);
		restoreSpec(player);
		restoreStats(player);
	}

	/** Properly handles the destruction of the instance. Removes NPCs and deals with the player in the proper manner **/
	public static void destructInstance(final Player player) {
		if ((player.getLocation() != Locations.Location.INSTANCE_ARENA) || (!player.getRegionInstance().equals(RegionInstance.RegionInstanceType.INSTANCE_ARENA)) || (player.getRegionInstance().getNpcsList().isEmpty())) {
		//System.out.println("Nothing to do to destruct the instance?");
		} else {
			player.moveTo(GameSettings.DEFAULT_POSITION);
			//System.out.println("Destroying Arena for " + player.getUsername());
			player.getRegionInstance().getNpcsList().forEach(npc -> npc.removeInstancedNpcs(Locations.Location.INSTANCE_ARENA, player.getPosition().getZ()));
			player.getRegionInstance().getNpcsList().forEach(npc -> World.deregister(npc));
			player.getRegionInstance().destruct();
			restore(player);
			PoisonImmunityTask.makeImmune(player, 0);
		}
	}


}
