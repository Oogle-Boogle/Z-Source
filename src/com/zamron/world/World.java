package com.zamron.world;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.net.PlayerSession;
import com.zamron.net.SessionState;
import com.zamron.util.Misc;
import com.zamron.util.TextUtility;
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
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscArrayQueue;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author Gabriel Hannason Thanks to lare96 for help with parallel updating
 * system
 */
public final class World {

    public static long LAST_SHOP_RELOAD;

    public static final int MAX_PLAYERS = 2000;
    public static final int MAX_NPCS = 15000; // was 5000

    /**
     * All of the registered players.
     */
    private static final CharacterList<Player> players = new CharacterList<>(MAX_PLAYERS);

    private static final LongSet usernamesOnline = new LongOpenHashSet(MAX_PLAYERS);

    /**
     * All of the registered NPCs.
     */
    private static final CharacterList<NPC> npcs = new CharacterList<>(MAX_NPCS);

    /**
     * Used to block the game thread until updating has completed.
     */
    private static final Phaser synchronizer = new Phaser(1);

    /**
     * A thread pool that will update players in parallel.
     */
    private static final ExecutorService updateExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new ThreadFactoryBuilder().setNameFormat("UpdateThread")
                    .setPriority(Thread.MAX_PRIORITY)
                    .build());

    private static final UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
    private static final UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();

    /**
     * The queue of {@link Player}s waiting to be logged in.
     **/
    private static final MessagePassingQueue<Player> logins = new MpscArrayQueue<>(MAX_PLAYERS);

    private static final MessagePassingQueue.Consumer<Player> loginConsumer = player -> {
        try {
            PlayerHandler.handleLogin(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * The queue of {@link Player}s waiting to be logged out.
     **/
    private static final MessagePassingQueue<Player> logouts = new MpscArrayQueue<>(MAX_PLAYERS);

    private static final MessagePassingQueue.Consumer<Player> logoutConsumer =
            player -> PlayerHandler.handleLogout(player, false, true);

    public static boolean queueLogin(Player player) {
        return logins.offer(player);
    }

    public static boolean queueLogout(Player player) {
        if (player == null) return false;
        PlayerSession session = player.getSession();
        if (session == null) return false;
        switch (session.getState()) {
            case LOGGED_OUT:
            case LOGGING_OUT:
                return false;
            default:
                return logouts.offer(player);
        }
    }

    /**
     * The queue of {@link Player}s waiting to be given their vote reward.
     **/
    private static final Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

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
        return op.orElse(null);
    }

    public static Player getPlayerByIndex(int username) {
        Optional<Player> op = players.search(p -> p != null && p.getIndex() == username);
        return op.orElse(null);
    }

    public static Player getPlayerByLong(long encodedName) {
        Optional<Player> op = players.search(p -> p != null && p.getLongUsername() == encodedName);
        return op.orElse(null);
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
                players.forEach(StaffList::updateInterface);
                stop();
            }
        });
    }

    public static void savePlayers() {
        players.forEach(Player::save);
        players.forEach(MiniMeData::save);
    }

    public static CharacterList<Player> getPlayers() {
        return players;
    }

    public static boolean addPlayerUsername(long usernameLong) {
        return usernamesOnline.add(usernameLong);
    }

    public static boolean addPlayerUsername(Player player) {
        return addPlayerUsername(player.getLongUsername());
    }

    public static boolean removePlayerUsername(long usernameLong) {
        return usernamesOnline.remove(usernameLong);
    }

    public static boolean removePlayerUsername(Player player) {
        return removePlayerUsername(player.getLongUsername());
    }

    public static CharacterList<NPC> getNpcs() {
        return npcs;
    }

    public static void sequence() {
        // Handle queued logins.
        logins.drain(loginConsumer, GameSettings.LOGIN_THRESHOLD);

        // Handle queued logouts.
        logouts.drain(logoutConsumer, GameSettings.LOGOUT_THRESHOLD);

        //DailyNPCTask.sequence(null); // TODO
        DailyNPCTask.dailyResetTask();
        FightPit.sequence();
        // Cows.sequence();
        Reminders.sequence();
        // Cows.spawnMainNPCs();
        PestControl.sequence();
        ShootingStar.sequence();
        EvilTrees.sequence();
        TriviaBot.sequence();
        // ShopRestocking.sequence();
        //FreeForAll.sequence();
        //CustomFreeForAll.sequence();
        //LastManStanding.sequence();
        GlobalPerks.getInstance().tick();
        // First we construct the update sequences.
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
        DailyNPCTask.dailyResetTask();
    }

}
