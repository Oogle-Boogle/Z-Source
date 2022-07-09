package com.zamron.world;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.util.Misc;
import com.zamron.world.content.*;
import com.zamron.world.content.combat.DailyNPCTask;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.content.minigames.impl.FightPit;
import com.zamron.world.content.minigames.impl.FreeForAll;
import com.zamron.world.content.minigames.impl.LastManStanding;
import com.zamron.world.content.minigames.impl.PestControl;
import com.zamron.world.content.minimes.MiniMeData;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.entity.Entity;
import com.zamron.world.entity.EntityHandler;
import com.zamron.world.entity.impl.CharacterList;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerHandler;
import com.zamron.world.entity.updating.NpcUpdateSequence;
import com.zamron.world.entity.updating.PlayerUpdateSequence;
import com.zamron.world.entity.updating.UpdateSequence;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating
 *         system
 */
public class World {

	public static long LAST_SHOP_RELOAD;

	/** All of the registered players. */
	private static CharacterList<Player> players = new CharacterList<>(1000);

	/** All of the registered NPCs. */
	private static CharacterList<NPC> npcs = new CharacterList<>(8000); //was 5000

	/** Used to block the game thread until updating has completed. */
	private static Phaser synchronizer = new Phaser(1);

	/** A thread pool that will update players in parallel. */
	private static ExecutorService updateExecutor = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(),
			new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

	/** The queue of {@link Player}s waiting to be logged in. **/
	private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/** The queue of {@link Player}s waiting to be logged out. **/
	private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/** The queue of {@link Player}s waiting to be given their vote reward. **/
	private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

	public static boolean DOUBLE_DONATIONS;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void register(Entity entity) {
		EntityHandler.register(entity);
	}

	public static void deregister(Entity entity) {
		EntityHandler.deregister(entity);
	}

	public static Player getPlayerByName(String username) {
		Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByIndex(int username) {
		Optional<Player> op = players.search(p -> p != null && p.getIndex() == username);
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByLong(long encodedName) {
		Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
		return op.isPresent() ? op.get() : null;
	}

	public static void sendMessageDiscord(String message) {
		players.stream().filter(p -> p != null && (!p.isMiniMe)).forEach(p -> p.getPacketSender().sendMessage(message));
		if (message.contains("[New Player]")) {
			DiscordMessenger.sendNewPlayer(message);
		 } else {
			DiscordMessenger.sendInGameMessage(message);
		}
	}

	public static void sendMessageNonDiscord(String message) {
		players.stream().filter(p -> p != null && (!p.isMiniMe)).forEach(p -> p.getPacketSender().sendMessage(message));
	}

	public static void sendStaffMessage(String message) {
		players.stream().filter(p -> p != null && (p.getRights().isSeniorStaff())).forEach(p -> p.getPacketSender().sendMessage(message));
		DiscordMessenger.sendStaffMessage(message);
	}

	public static void sendStaffYell(String message) {
		players.stream().filter(p -> p != null && (p.getRights().isStaff())).forEach(p -> p.getPacketSender().sendMessage(message));
	}

	public static void updateServerTime() {
		//players.forEach(p -> p.getPacketSender().sendString(39173,
				//PlayerPanel.LINE_START + "@whi@Server Time: @cya@" + Misc.getCurrentServerTime()));
	}

	public static void register(Player c, NPC entity) {
		EntityHandler.register(entity);
		c.getRegionInstance().getNpcsList().add(entity);
	}

	public static void updatePlayersOnline() {
		// players.forEach(p-> p.getPacketSender().sendString(39173,
		// PlayerPanel.LINE_START + "@or1@Players Online: @yel@"+players.size()));
	
		players.forEach(
				p -> p.getPacketSender().sendString(34353, "@or2@Players Online: @gre@" + (int) (players.size()) + ""));
		players.forEach(
				p -> p.getPacketSender().sendString(26608, "@or2@Players Online: @gre@" + (int) (players.size()) + ""));
		players.forEach(
				p -> p.getPacketSender().sendString(57003, "Players:  @gre@" + (int) (World.getPlayers().size()) + ""));
		updateStaffList();
	}

	public static void updateStaffList() {
		TaskManager.submit(new Task(false) {
			@Override
			protected void execute() {
				players.forEach(p -> StaffList.updateInterface(p));
				stop();
			}
		});
	}

	public static void savePlayers() {
		players.forEach(p -> p.save());
		players.forEach(MiniMeData::save);
	}

	public static CharacterList<Player> getPlayers() {
		return players;
	}

	public static CharacterList<NPC> getNpcs() {
		return npcs;
	}

	public static void sequence() {
		// Handle queued logins.
		for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++) {
			Player player = logins.poll();
			if (player == null)
				break;
			PlayerHandler.handleLogin(player);
		}
		// Handle queued logouts.
		int amount = 0;
		Iterator<Player> $it = logouts.iterator();
		while ($it.hasNext()) {
			Player player = $it.next();
			if (player == null || amount >= GameSettings.LOGOUT_THRESHOLD)
				break;
			if (PlayerHandler.handleLogout(player, false)) {
				$it.remove();
				amount++;
			}
		}

		//DailyNPCTask.sequence(null); // TODO
		DailyNPCTask task = new DailyNPCTask();
		FightPit.sequence();
		// Cows.sequence();
		Reminders.sequence();
		// Cows.spawnMainNPCs();
		PestControl.sequence();
		ShootingStar.sequence();
		EvilTrees.sequence();
		TriviaBot.sequence();
		// ShopRestocking.sequence();
		FreeForAll.sequence();
		CustomFreeForAll.sequence();
		LastManStanding.sequence();
		GlobalPerks.getInstance().tick();
		// First we construct the update sequences.
		UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
		UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
		// Then we execute pre-updating code.
		players.forEach(playerUpdate::executePreUpdate);
		npcs.forEach(npcUpdate::executePreUpdate);
		// Then we execute parallelized updating code.
		synchronizer.bulkRegister(players.size());
		players.forEach(playerUpdate::executeUpdate);
		synchronizer.arriveAndAwaitAdvance();
		// Then we execute post-updating code.
		players.forEach(playerUpdate::executePostUpdate);
		npcs.forEach(npcUpdate::executePostUpdate);
		//DailyNPCTask.dailyResetTask();
		DailyNPCTask.dailyResetTask();
	}

	public static Queue<Player> getLoginQueue() {
		return logins;
	}

	public static Queue<Player> getLogoutQueue() {
		return logouts;
	}

	public static Queue<Player> getVoteRewardingQueue() {
		return voteRewards;
	}
}
