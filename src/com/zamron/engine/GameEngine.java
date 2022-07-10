package com.zamron.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;


import com.zamron.engine.task.TaskManager;
import com.zamron.event.CycleEventHandler;


import com.zamron.world.World;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.grandexchange.GrandExchangeOffers;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zamron.world.entity.impl.player.Player;

/**
 * @author lare96
 * @author Gabriel Hannason
 */
public final class GameEngine implements Runnable {

	private final ScheduledExecutorService logicService = GameEngine.createLogicService();

	// private static final int PROCESS_GAME_TICK = 2;

	// private EngineState engineState = EngineState.PACKET_PROCESSING;

	// private int engineTick = 0;

	@Override
	public void run() {
		try {
			long s = System.nanoTime();
			/*
			 * switch(engineState) { case PACKET_PROCESSING: World.getPlayers().forEach($it ->
			 * $it.getSession().handlePrioritizedMessageQueue()); break; case GAME_PROCESSING: TaskManager.sequence();
			 * World.sequence(); break; } engineState = next();
			 */

			TaskManager.sequence();
			World.sequence();
			CycleEventHandler.getSingleton().process();
			long e = (System.nanoTime() - s) / 1000000;

			/**
			 * Process incoming packets consecutively throughout the sleeping cycle *The key to instant switching of
			 * equipment
			 */
			if (e < 600) {
				if (e < 400) {
					for (int i = 0; i < 30; i++) {
						long sleep = (600 - e) / 30;
						Thread.sleep(sleep);
						subcycle();
					}
				} else {
					Thread.sleep(600 - e);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			World.savePlayers();
			GrandExchangeOffers.save();
			ClanChatManager.save();
		}
	}

	private static void subcycle() {
		for (Player p : World.getPlayers()) {
			if (p != null) {
				p.getSession().handleQueuedMessages();
			}
		}
	}

	/*
	 * private EngineState next() { if (engineTick == PROCESS_GAME_TICK) { engineTick = 0; return
	 * EngineState.GAME_PROCESSING; } engineTick++; return EngineState.PACKET_PROCESSING; } private enum EngineState {
	 * PACKET_PROCESSING, GAME_PROCESSING; }
	 */

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