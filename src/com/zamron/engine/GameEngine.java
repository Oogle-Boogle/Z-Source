package com.zamron.engine;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zamron.GameServer;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.world.World;
import com.zamron.world.content.WellOfGoodwill;
import com.zamron.world.content.WellOfWealth;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.grandexchange.GrandExchangeOffers;
import com.zamron.world.content.minimes.MiniMeData;
import com.zamron.world.content.minimes.MiniMeFunctions;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.zamron.GameSettings.ENGINE_PROCESSING_CYCLE_RATE;

/**
 * @author Oogle
 */

public final class GameEngine implements Runnable {

    public static int seconds, timer, display;

    private final ExecutorService taskService = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("TaskServiceThread")
                    .build());

    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    @Override
    public void run() {
        long sequenceStartNanos = System.nanoTime();

        try {
            TaskManager.sequence();
            World.sequence();
        } catch (Throwable ex) {
            log.error("Error whilst running world update sequence.", ex);
            // Save everything to avoid losses if the exception is fatal.
            World.savePlayers();
            GrandExchangeOffers.save();
            ClanChatManager.save();
        }

        long sequenceFinishNanos = System.nanoTime();
        long sequenceRunTime = sequenceFinishNanos - sequenceStartNanos;
        long runTimeInMillis = TimeUnit.NANOSECONDS.toMillis(sequenceRunTime);

        if (runTimeInMillis > ENGINE_PROCESSING_CYCLE_RATE) {
            log.warn("World sequence ran too slow! Actual runtime was {}ms ({}ns) - should be under {}ms!",
                    runTimeInMillis, sequenceRunTime, ENGINE_PROCESSING_CYCLE_RATE);
        }
    }
//        seconds++;
//        timer = 800; //800
//        display--;
//        if (seconds == 10800000) { //10800000
//            GameServer.setUpdating(true);
//            GameServer.isUpdating();
//            System.out.println("Server is now restarting (Hit the 3 hour mark).");
//            World.sendMessageNonDiscord("@red@<img=12>[SERVER]<img=12>Please ensure you ::save your progress before the update.");
//            for (Player players : World.getPlayers()) {
//                if (players == null) {
//                    continue;
//                }
//                players.getPacketSender().sendSystemUpdate(timer);
//            }
//            TaskManager.submit(new Task(timer) {
//                @Override
//                protected void execute() {
//                    World.savePlayers();
//                    for (Player player : World.getPlayers()) {
//                        if (player != null) {
//                            World.deregister(player);
//                        }
//                    }
//                    WellOfGoodwill.save();
//                    WellOfWealth.save();
//                    GrandExchangeOffers.save();
//                    ClanChatManager.save();
//                    GlobalPerks.getInstance().save();
//                    GameServer.getLogger().info("Restarting server CMD....");
//                    System.gc();
//                    stop();
//                    System.exit(0);
//                }
//            });
//        }
//    }

    public void submit(Runnable t) {
        try {
            taskService.execute(t);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
