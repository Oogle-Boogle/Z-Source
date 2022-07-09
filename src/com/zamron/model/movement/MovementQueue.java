package com.zamron.model.movement;

import java.util.ArrayDeque;
import java.util.Deque;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Direction;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Locations.Location;
import com.zamron.model.movement.path.PathGenerator;
import com.zamron.world.clip.region.RegionClipping;
import com.zamron.world.content.EnergyHandler;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.content.combat.CombatType;
import com.zamron.world.entity.impl.Character;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * A queue of {@link Direction}s which a {@link Character} will follow.
 * 
 * @author Graham Edgecombe Edited by Gabbe
 */
public final class MovementQueue {

	/**
	 * Represents a single point in the queue.
	 * 
	 * @author Graham Edgecombe
	 */
	private static final class Point {

		/**
		 * The point's position.
		 */
		private final Position position;

		/**
		 * The direction to walk to this point.
		 */
		private final Direction direction;

		/**
		 * Creates a point.
		 * 
		 * @param position
		 *            The position.
		 * @param direction
		 *            The direction.
		 */
		public Point(Position position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction + ", position=" + position + "]";
		}

	}

	/**
	 * The maximum size of the queue. If any additional steps are added, they are
	 * discarded.
	 */
	private static final int MAXIMUM_SIZE = 100;

	/**
	 * The character whose walking queue this is.
	 */
	private final Character character;

	/**
	 * The queue of directions.
	 */
	private final Deque<Point> points = new ArrayDeque<Point>();

	/**
	 * The following task
	 */
	private Character followCharacter;

	/**
	 * Creates a walking queue for the specified character.
	 * 
	 * @param character
	 *            The character.
	 */
	public MovementQueue(Character character) {
		this.character = character;
	}

	/**
	 * Sets a character to follow
	 */
	public void setFollowCharacter(Character followCharacter) {
		this.followCharacter = followCharacter;
	}

	public Character getFollowCharacter() {
		return followCharacter;
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and client
	 * position by looking at the previous queue.
	 * 
	 * @param clientConnectionPosition
	 *            The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 *         {@code false} if not.
	 */
	public boolean addFirstStep(Position clientConnectionPosition) {
		reset();
		addStep(clientConnectionPosition);
		return true;
	}

	//Click to teleport
	private int firstStepX;
	private int firstStepY;

	/**
	 * Adds a step to walk to the queue.
	 * 
	 * @param x
	 *            X to walk to
	 * @param y
	 *            Y to walk to
	 * @param clipped
	 *            Can the step walk through objects?
	 */
	public void walkStep(int x, int y) {
		Position position = character.getPosition().copy();
		position.setX(position.getX() + x);
		position.setY(position.getY() + y);
		addStep(position);
	}

	/**
	 * Adds a step.
	 * 
	 * @param x
	 *            The x coordinate of this step.
	 * @param y
	 *            The y coordinate of this step.
	 * @param heightLevel
	 * @param flag
	 */
	private void addStep(int x, int y, int heightLevel) {
		if (character.getMovementQueue().isLockMovement() || character.isFrozen()) {
			return;
		}

		if (points.size() >= MAXIMUM_SIZE)
			return;

		final Point last = getLast();
		final int deltaX = x - last.position.getX();
		final int deltaY = y - last.position.getY();
		final Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE)
			points.add(new Point(new Position(x, y, heightLevel), direction));
	}

	/**
	 * Adds a step to the queue.
	 * 
	 * @param step
	 *            The step to add.
	 * @oaram flag
	 */
	public void addStep(Position step) {
		if (character.isFrozen() || lockMovement)
			return;
		final Point last = getLast();
		final int x = step.getX();
		final int y = step.getY();
		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();
		final int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0)
				deltaX--;
			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0)
				deltaY--;
			addStep(x - deltaX, y - deltaY, step.getZ());
		}
	}

	public boolean canWalk(int deltaX, int deltaY) {
		final Position to = new Position(character.getPosition().getX() + deltaX,
				character.getPosition().getY() + deltaY, character.getPosition().getZ());
		if (character.getPosition().getZ() == -1 && to.getZ() == -1 && character.isNpc()
				&& !((NPC) character).isSummoningNpc() || character.getLocation() == Location.RECIPE_FOR_DISASTER)
			return true;
		return canWalk(character.getPosition(), to, character.getSize());
	}

	public static boolean canWalk(Position from, Position to, int size) {
		return RegionClipping.canMove(from, to, size, size);
	}

	/*
	 * public boolean checkBarricade(int x, int y) { Position position =
	 * character.getPosition(); if(character.isPlayer()) {
	 * if(Locations.inSoulWars((Player)character)) {
	 * if(SoulWars.checkBarricade(position.getX() + x, position.getY()+ y,
	 * position.getZ())) { ((Player)character).getPacketSender().sendMessage(
	 * "The path is blocked by a Barricade."); reset(true); return true; } } }
	 * return false; }
	 */

	/**
	 * Gets the last point.
	 * 
	 * @return The last point.
	 */
	private Point getLast() {
		final Point last = points.peekLast();
		if (last == null)
			return new Point(character.getPosition(), Direction.NONE);
		return last;
	}

	/**
	 * @return true if the character is moving.
	 */
	public boolean isMoving() {
		return !points.isEmpty();
	}

	/**
	 * Called every 600ms, updates the queue.
	 */
	public void sequence() {

		boolean movement = !lockMovement && !character.isFrozen();
		boolean movementOccured = false;
		Position previousPosition = null;

		if (followCharacter != null) {
			handleFollowing();
		}

		if (movement) {
			Point walkPoint = null;
			Point runPoint = null;

			walkPoint = points.poll();
			if (isRunToggled()) {
				runPoint = points.poll();
			}

			if (character.isNeedsPlacement()) {
				reset();
				return;
			}

			if (walkPoint != null && walkPoint.direction != Direction.NONE) {
				if (followCharacter != null) {
					if (walkPoint.equals(followCharacter.getPosition())) {
						return;
					} else {
						if (!followCharacter.getMovementQueue().isRunToggled()) {
							if (character.getPosition().isWithinDistance(followCharacter.getPosition(), 2)) {
								runPoint = null;
							}
						}
					}
				}

				if (!character.isPlayer() && !character.getCombatBuilder().isAttacking()) {
					if (((NPC) character).isSummoningNpc() && !((NPC) character).summoningCombat()) {
						if (!canWalk(character.getPosition(), walkPoint.position, character.getSize())) {
							return;
						}
					}
				}

				previousPosition = character.getPosition();
				character.setPosition(walkPoint.position);
				character.setPrimaryDirection(walkPoint.direction);
				character.setLastDirection(walkPoint.direction);
				movementOccured = true;
			}
			if (runPoint != null && runPoint.direction != Direction.NONE) {
				if (followCharacter != null) {
					if (walkPoint.equals(followCharacter.getPosition())) {
						return;
					}
				}
				previousPosition = character.getPosition();
				character.setPosition(runPoint.position);
				character.setSecondaryDirection(runPoint.direction);
				character.setLastDirection(runPoint.direction);
				movementOccured = true;
			}
		}

		if (character.isPlayer()) {
			Player p = (Player) character;
			if (movementOccured) {
				handleRegionChange();
				p.setPreviousPosition(previousPosition);
			}
			Locations.process(p);
			EnergyHandler.processPlayerEnergy(p);
		}
	}

	public boolean isMovementDone() {
		return points.size() == 0;
	}

	public void handleRegionChange() {
		final int diffX = character.getPosition().getX() - character.getLastKnownRegion().getRegionX() * 8;
		final int diffY = character.getPosition().getY() - character.getLastKnownRegion().getRegionY() * 8;
		boolean regionChanged = false;
		if (diffX < 16)
			regionChanged = true;
		else if (diffX >= 88)
			regionChanged = true;
		if (diffY < 16)
			regionChanged = true;
		else if (diffY >= 88)
			regionChanged = true;
		if (regionChanged) {
			((Player) character).getPacketSender().sendMapRegion();
		}
	}

	public void handleFollowing() {

		// Check if we can still follow the leader.
		if (followCharacter == null || followCharacter.getConstitution() <= 0 || !followCharacter.isRegistered()
				|| character.getConstitution() <= 0 || !character.isRegistered()) {
			character.setEntityInteraction(null);
			followCharacter = null;
			return;
		}

		if (!Location.ignoreFollowDistance(character)) {
			boolean summNpc = followCharacter.isPlayer() && character.isNpc() && ((NPC) character).isSummoningNpc();
			if (!character.getPosition().isWithinDistance(followCharacter.getPosition(), summNpc ? 10 : 20)) {
				character.setEntityInteraction(null);
				if (summNpc) {
					((Player) followCharacter).getSummoning().moveFollower(true);
				}
				return;
			}
		}

		// Update entity interaction
		character.setEntityInteraction(followCharacter);

		// Block if our movement is locked.
		if (character.getMovementQueue().isLockMovement() || character.isFrozen()) {
			return;
		}

		// If we are on the same position as the leader then move
		// away.
		if (character.getPosition().equals(followCharacter.getPosition())) {
			character.getMovementQueue().reset();
			if (followCharacter.getMovementQueue().isMovementDone()) {
				MovementQueue.stepAway(character);
			}
			return;
		}

		if (character.getCombatBuilder().getStrategy() == null) {
			character.getCombatBuilder().determineStrategy();
		}

		// Are we following in combat?
		boolean combatFollow = character.getCombatBuilder().isAttacking();

		// The amount of distance between us and the other character.
		int distance = character.getPosition().getDistance(followCharacter.getPosition());

		// Reset movement if we're in character size range.
		if (distance >= 50) {
			character.setEntityInteraction(null);
			followCharacter = null;
			reset();
			return;
		}

		if (combatFollow) {
			if (character.getSize() == 1 && character.getCombatBuilder().getStrategy().getCombatType() == CombatType.MELEE) {
				if (character.getPosition().equals(followCharacter.getPosition())
						|| PathFinder.isInDiagonalBlock(character, followCharacter)) {
					if (isMovementDone() && followCharacter.getMovementQueue().isMovementDone()) {
						PathFinder.solveDiagonalBlock(character, followCharacter);
						return;
					}
				}
			}

			if (CombatFactory.checkAttackDistance(character.getCombatBuilder())) {
				// reset();
				return;
			}
		}

		Position destination = null;

		// Dancing
		if (!combatFollow) {
			if (character.isPlayer() && followCharacter.isPlayer()) {
				Player p = (Player) followCharacter;
				if (p.getPreviousPosition() != null) {
					destination = p.getPreviousPosition();
					p.setPreviousPosition(null);
				}
			}
		}

		if (destination == null) {
			destination = combatFollow ? PathGenerator.getCombatPath(character, followCharacter)
					: PathGenerator.getBasicPath(character, followCharacter);
		}

		// Summoning following
		/*
		 * if (character.isNpc()) { NPC npc = (NPC)character; if (npc.isSummoningNpc()
		 * && followCharacter.getLocation() == Location.BANK) { reset(); return; } }
		 */

		if (destination != null) {
			PathFinder.findPath(character, destination.getX(), destination.getY(), false, 16, 16);
		}
	}

	/**
	 * Stops the movement.
	 */
	public MovementQueue reset() {
		points.clear();
		if (character.isPlayer())
			((Player) character).setWalkToTask(null);
		return this;
	}

	/**
	 * Gets the size of the queue.
	 * 
	 * @return The size of the queue.
	 */
	public int size() {
		return points.size();
	}

	/**
	 * The force movement array index values.
	 */
	public static final int FIRST_MOVEMENT_X = 0, FIRST_MOVEMENT_Y = 1, SECOND_MOVEMENT_X = 2, SECOND_MOVEMENT_Y = 3,
			MOVEMENT_SPEED = 4, MOVEMENT_REVERSE_SPEED = 5, MOVEMENT_DIRECTION = 6;

	/**
	 * Steps away from a Gamecharacter
	 * 
	 * @param character
	 *            The gamecharacter to step away from
	 */
	public static void stepAway(Character character) {
		if (character.getMovementQueue().canWalk(-1, 0))
			character.getMovementQueue().walkStep(-1, 0);
		else if (character.getMovementQueue().canWalk(1, 0))
			character.getMovementQueue().walkStep(1, 0);
		else if (character.getMovementQueue().canWalk(0, -1))
			character.getMovementQueue().walkStep(0, -1);
		else if (character.getMovementQueue().canWalk(0, 1))
			character.getMovementQueue().walkStep(0, 1);
	}

	public void freeze(int delay) {
		if (character.isFrozen())
			return;
		character.setFreezeDelay(delay);
		if (character.isPlayer()) {
			((Player) character).getPacketSender().sendMessage("You have been frozen!");
		}
		reset();
		TaskManager.submit(new Task(2, character, true) {
			@Override
			protected void execute() {
				if (!character.isRegistered() || character.getConstitution() <= 0) {
					stop();
					return;
				}
				if (character.decrementAndGetFreezeDelay() == 0) {
					stop();
				}
			}
		});
	}

	public static int getMove(int x, int p2, int size) {
		if ((x - p2) == 0) {
			return 0;
		} else if ((x - p2) < 0) {
			return size;
		} else if ((x - p2) > 0) {
			return -size;
		}
		return 0;
	}

	/** If this entity's movement is locked. */
	private boolean lockMovement;

	/**
	 * Gets whether or not this entity is 'frozen'.
	 * 
	 * @return true if this entity cannot move.
	 */
	public boolean isLockMovement() {
		return lockMovement;
	}

	/**
	 * Sets if this entity can move or not.
	 * 
	 * @param lockMovement
	 *            true if this entity cannot move.
	 */
	public MovementQueue setLockMovement(boolean lockMovement) {
		this.lockMovement = lockMovement;
		return this;
	}

	public boolean isRunToggled() {
		return character.isPlayer() && ((Player) character).isRunning() && !((Player) character).isCrossingObstacle();
	}
}