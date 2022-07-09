package com.zamron.world.entity.impl.npc.Bosses.zulrah;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.zamron.event.CycleEvent;
import com.zamron.event.CycleEventContainer;
import com.zamron.event.CycleEventHandler;
import com.zamron.model.Animation;
import com.zamron.model.Flag;
import com.zamron.model.GameObject;
import com.zamron.model.Position;
import com.zamron.model.RegionInstance;
import com.zamron.model.RegionInstance.RegionInstanceType;
import com.zamron.util.Misc;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.Bosses.Boundary;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.CreateToxicStageOne;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MageStageEight;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MageStageFive;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MageStageThree;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MeleeStageSix;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MeleeStageTen;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.MeleeStageTwo;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.RangeStageEleven;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.RangeStageFour;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.RangeStageNine;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.RangeStageSeven;
import com.zamron.world.entity.impl.npc.Bosses.zulrah.impl.SpawnZulrahStageZero;
import com.zamron.world.entity.impl.player.Player;
import com.google.common.base.Stopwatch;

public class Zulrah {

	/**
	 * The minion snake npc id
	 */
	public static final int SNAKELING = 2045;

	/**
	 * The relative lock for this event
	 */
	private final Object EVENT_LOCK = new Object();

	/**
	 * The player associated with this event
	 */
	private final Player player;

	private int height;

	/**
	 * The boundary of zulrah's location
	 */
	public static final Boundary BOUNDARY = new Boundary(2248, 3059, 2283, 3084);

	/**
	 * The zulrah npc
	 */
	private NPC npc;

	/**
	 * The current stage of zulrah
	 */
	private int stage;

	/**
	 * Determines if the npc is transforming or not.
	 */
	private boolean transforming;

	/**
	 * The stopwatch for tracking when the zulrah npc fight starts.
	 */
	private Stopwatch stopwatch = Stopwatch.createUnstarted();

	/**
	 * A mapping of all the stages
	 */
	private Map<Integer, ZulrahStage> stages = new HashMap<>();

	/**
	 * Creates a new Zulrah event for the player
	 * 
	 * @param player
	 *            the player
	 */

	private boolean groupFlag = false;

	public Zulrah(Player player) {
		this.player = player;
		stages.put(0, new SpawnZulrahStageZero(this, player));
		stages.put(1, new CreateToxicStageOne(this, player));
		stages.put(2, new MeleeStageTwo(this, player));
		stages.put(3, new MageStageThree(this, player));
		stages.put(4, new RangeStageFour(this, player));
		stages.put(5, new MageStageFive(this, player));
		stages.put(6, new MeleeStageSix(this, player));
		stages.put(7, new RangeStageSeven(this, player));
		stages.put(8, new MageStageEight(this, player));
		stages.put(9, new RangeStageNine(this, player));
		stages.put(10, new MeleeStageTen(this, player));
		stages.put(11, new RangeStageEleven(this, player));
	}

	public void initialize() {
		if (player.getRegionInstance() != null) {
			player.getRegionInstance().destruct();
		}
		this.height =  player.getIndex() * 4;
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH));
		// player.getPacketSender().sendMessage("DEBUG: (instance height = " +
		// this.height + ")");
		stage = 0;
		stopwatch = Stopwatch.createStarted();
		player.getPacketSender().sendInterfaceReset();
		// 1, 5);
		// player.getPacketSender().sendMessage("Zulrah is in BETA Stage any
		// bugs please report on forums.");
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
		teleport();
	}

	private void teleport() {
		Position zulrahMap = new Position(2268, 3069, this.getHeight());
		player.getPacketSender().sendMessage("@red@Welcome to Zulrah's shrine"/*, -1, 4*/);
		player.moveTo(zulrahMap);
	}
	/**
	 * Determines if the player is standing in a toxic location
	 * 
	 * @return true of the player is in a toxic location
	 */
	public boolean isInToxicLocation() {
		//if(player.getLocation() == Location.ZULRAH_SHRINE){
			for(DangerousLocation locations : DangerousLocation.values()) {
				for(Point point : locations.getPoints()) {
					double distance = player.getPosition().distanceToPoint(point.x + 1, point.y + 1);
					Position venom = new Position(point.x, point.y, player.getPosition().getZ());
					@SuppressWarnings("unused")
					GameObject veneno = new GameObject(11700, venom);
					if(distance <= 1 && CustomObjects.objectExists(venom))
						return true;
				}
				

			//}
			
			//location.getPoints()[0].x, location.getPoints()[0].y)
			//if(player.getPosition().getDistance(other)
		/*for (int x = player.getPosition().getX() - 1; x < player.getPosition().getX() + 1; x++) {
			for (int y = player.getPosition().getY() - 1; y < player.getPosition().getY() + 1; y++) {
				Position venom = new Position(x, y, player.getPosition().getZ());
				GameObject veneno = new GameObject(11700, venom);
				if (CustomObjects.objectExists(venom)) {
					return true;
				}
			}
		}*/
		}
		return false;
	}

	/**
	 * Stops the zulrah instance and concludes the events
	 */
	public void stop() {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		if (stage < 1) {
			return;
		}
		if (stopwatch.isRunning())
			stopwatch.stop();
		if (groupFlag == false) {
			long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			long best = player.getBestZulrahTime();
			String duration = best < (60_000 * 60) ? Misc.toFormattedMS(time) : Misc.toFormattedHMS(time);
			player.getPacketSender().sendMessage("@red@Time Elapsed: " + duration + "</col> "
					+ (time < player.getBestZulrahTime() ? "(New personal best)" : "") + ".");
			if (time < player.getBestZulrahTime()) {
				player.setBestZulrahTime(time);
			}
		}

		player.getRegionInstance().destruct();
	}

	public void changeStage(int stage, CombatType combatType, ZulrahLocation location) {
		this.stage = stage;
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		CycleEventHandler.getSingleton().addEvent(EVENT_LOCK, stages.get(stage), 1);
		if (stage == 1) {
			return;
		}
		int type = combatType == CombatType.MELEE ? 2044 : combatType == CombatType.MAGIC ? 2042 : 2043;
		npc.performAnimation(new Animation(5072));
		npc.getCombatBuilder().setAttackTimer(8);
		transforming = true;
		player.getCombatBuilder().reset(true);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (container.getTotalTicks() == 2) {
					npc.setTransformationId(153);
					npc.getUpdateFlag().flag(Flag.TRANSFORM);
				} else if (container.getTotalTicks() == 3) {
					Position zulrahNew = new Position(location.getLocation().x, location.getLocation().y,
							player.getPosition().getZ());
					npc.moveTo(zulrahNew);
				} else if (container.getTotalTicks() == 4) {
					npc.setTransformationId(type);
					npc.performAnimation(new Animation(5073));
					npc.setPositionToFace(player.getPosition());
					npc.getUpdateFlag().flag(Flag.TRANSFORM);
					npc.getUpdateFlag().flag(Flag.FACE_POSITION);
					transforming = false;
					setNpc(npc);
					container.stop();
				}
			}

		}, 1);
	}



	/**
	 * The reference to zulrah, the npc
	 * 
	 * @return the reference to zulrah
	 */
	public NPC getNpc() {
		return npc;
	}

	/**
	 * The instance of the Zulrah {@link NPC}
	 * 
	 * @param npc
	 *            the zulrah npc
	 */
	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	/**
	 * The stage of the zulrah event
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Determines if the NPC is transforming or not
	 * 
	 * @return {@code true} if the npc is in a transformation stage
	 */
	public boolean isTransforming() {
		return transforming;
	}


	public int getHeight() {
		return height;
	}
}
