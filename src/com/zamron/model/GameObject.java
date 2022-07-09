package com.zamron.model;

import com.zamron.model.definitions.GameObjectDefinition;
import com.zamron.world.World;
import com.zamron.world.entity.Entity;
import com.zamron.world.entity.impl.player.Player;

/**
 * This file manages a game object entity on the globe.
 * 
 * @author Relex lawl / iRageQuit2012
 *
 */

public class GameObject extends Entity {

	private int ticksRemaining;

	public void removeTicks() {
		ticksRemaining--;
	}

	public int getTicksRemaining() {
		return ticksRemaining;
	}

	public GameObject(int id, Position position, int face, int type, int ticksRemaining) {
		super(position);
		this.id = id;
		this.face = face;
		this.type = type;
		this.ticksRemaining = ticksRemaining;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id       The new object's id.
	 * @param position The new object's position on the globe.
	 */
	public GameObject(int id, Position position) {
		super(position);
		this.id = id;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id       The new object's id.
	 * @param position The new object's position on the globe.
	 * @param type     The new object's type.
	 */
	public GameObject(int id, Position position, int type) {
		super(position);
		this.id = id;
		this.type = type;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * 
	 * @param id       The new object's id.
	 * @param position The new object's position on the globe.
	 * @param type     The new object's type.
	 * @param face     The new object's facing position.
	 */
	public GameObject(int id, Position position, int type, int face) {
		super(position);
		this.id = id;
		this.type = type;
		this.face = face;
	}

	public GameObject(int id, Position position, int type, String face) {
		super(position);
		this.id = id;
		this.type = type;
		switch (face) {
			case "NORTH":
			case "north":
			case "North":
				this.face = -1;
				break;
			case "EAST":
			case "east":
			case "East":
				this.face = -2;
				break;
			case "SOUTH":
			case "south":
			case "South":
				this.face = -3;
				break;
			case "WEST":
			case "west":
			case "West":
				this.face = 0;
				break;
		}
	}

	/**
	 * The object's id.
	 */
	private int id;

	/**
	 * Gets the object's id.
	 * 
	 * @return id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The object's type (default=10).
	 */
	private int type = 10;

	/**
	 * Gets the object's type.
	 * 
	 * @return type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the object's type.
	 * 
	 * @param type New type value to assign.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * The object's current direction to face.
	 */
	private int face;

	/**
	 * Gets the object's current face direction.
	 * 
	 * @return face.
	 */
	public int getFace() {
		return face;
	}

	/**
	 * Sets the object's face direction.
	 * 
	 * @param face Face value to which object will face.
	 */
	public void setFace(int face) {
		this.face = face;
	}

	/**
	 * Value that handles the object's 'Picks'
	 */
	private int picked;

	/**
	 * Value that handles the object's 'Cuts'
	 */
	private int cut;

	/**
	 * Gets the object's definition.
	 * 
	 * @return definition.
	 */
	public GameObjectDefinition getDefinition() {
		return GameObjectDefinition.forId(id);
	}

	@Override
	public void performAnimation(Animation animation) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendObjectAnimation(this, animation);
		}
	}

	@Override
	public void performGraphic(Graphic graphic) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendGraphic(graphic, getPosition());
		}
	}

	@Override
	public int getSize() {
		GameObjectDefinition definition = getDefinition();
		if (definition == null)
			return 1;

		switch (id) {
		case 38660:
		case 11434:
		case 410:
		case 2320:
			return 2;
		case 2282:
			return 5;
		case 1767:
			return 9;
		}

		if (definition.getSizeX() == 1)
			return definition.getSizeY();
		else if (definition.getSizeX() > 1 && definition.getSizeY() == 1)
			return definition.getSizeX();
		else
			return definition.getSizeY() + definition.getSizeX();

	}

	public int getCutAmount() {
		return this.cut;
	}

	public void setCutAmount(int amountcut) {
		this.cut = amountcut;
	}

	public void incrementCutAmount() {
		this.cut++;
	}

	public int getPickAmount() {
		return this.picked;
	}

	public void setPickAmount(int amount) {
		this.picked = amount;
	}

	public void incrementPickAmount() {
		this.picked++;
	}

	RegionInstance regionInstance;

	public void setRegionInstance(RegionInstance r) {
		regionInstance = r;
	}

	public Object getRegionInstance() {
		return regionInstance;
	}

}
