package com.zamron.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;


import com.zamron.engine.task.TaskManager;

import com.zamron.util.Stopwatch;
import com.zamron.world.World;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.grandexchange.GrandExchangeOffers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oogle
 */
public final class GameEngine implements Runnable {

	private final ScheduledExecutorService logicService = GameEngine.createLogicService();

	private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

	private static final int TIME = 10; //10 minutes
	private static Stopwatch timer = new Stopwatch().reset();

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

		/**
		 *
		 * Remove this if its causing issues
		 *
		 */
		if(timer.elapsed(TIME)) {
			timer.reset();
			System.gc();
		}

		long sequenceFinishNanos = System.nanoTime();
		long sequenceRunTime = sequenceFinishNanos - sequenceStartNanos;
		long runTimeInMillis = TimeUnit.NANOSECONDS.toMillis(sequenceRunTime);

		if (runTimeInMillis >= 600L) {
			log.warn("World sequence ran too slow! Actual runtime was {}ms ({}ns) - should be under 600ms!",
					runTimeInMillis, sequenceRunTime);
		}
	}

	public void submit(Runnable t) {
		try {
			logicService.execute(t);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static ScheduledExecutorService createLogicService() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.setRejectedExecutionHandler(new CallerRunsPolicy());
		executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
		executor.setKeepAliveTime(45, TimeUnit.SECONDS);
		executor.allowCoreThreadTimeOut(true);
		return Executors.unconfigurableScheduledExecutorService(executor);
	}
}