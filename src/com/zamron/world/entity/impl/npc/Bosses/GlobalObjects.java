package com.zamron.world.entity.impl.npc.Bosses;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import com.zamron.world.entity.impl.player.Player;

public class GlobalObjects {
	
	/**
	 * A collection of all existing objects
	 */
	Queue<GlobalObject> objects = new LinkedList<>();
	
	/**
	 * A collection of all objects to be removed from the game
	 */
	Queue<GlobalObject> remove = new LinkedList<>();
	
	/**
	 * Adds a new global object to the game world
	 * @param object	the object being added
	 */
	public void add(GlobalObject object) {
		updateObject(object, object.getObjectId());
		objects.add(object);
	}
	
	/**
	 * Removes a global object from the world. If the object is present in the game,
	 * we find the reference to that object and add it to the remove list. 
	 * @param id		the identification value of the object
	 * @param x			the x location of the object
	 * @param y			the y location of the object
	 * @param height	the height of the object 
	 */
	public void remove(int id, int x, int y, int height) {
		Optional<GlobalObject> existing = objects.stream().filter(o -> o.getObjectId() == id && o.getX() == x 
				&& o.getY() == y && o.getHeight() == height).findFirst();
		if (!existing.isPresent()) {
			return;
		}
		remove(existing.get());
	}
	
	/**
	 * Attempts to remove any and all objects on a certain height that have the same object id.
	 * @param id		the id of the object
	 * @param height	the height the object must be on to be removed
	 */
	public void remove(int id, int height) {
		objects.stream().filter(o -> o.getObjectId() == id && o.getHeight() == height).forEach(this::remove);
	}
	
	/**
	 * Removes a global object from the world based on object reference
	 * @param object	the global object
	 */
	public void remove(GlobalObject object) {
		if (!objects.contains(object)) {
			return;
		}
		updateObject(object, -1);
		remove.add(object);
	}
	
	/**
	 * Determines if an object exists in the game world
	 * @param id		the identification value of the object
	 * @param x			the x location of the object
	 * @param y			the y location of the object
	 * @param height	the height location of the object
	 * @return			true if the object exists, otherwise false.
	 */
	public boolean exists(int id, int x, int y, int height) {
		return objects.stream().anyMatch(object -> object.getObjectId() == id && object.getX() == x && object.getY() == y
				&& object.getHeight() == height);
	}
	
	/**
	 * Determines if any object exists in the game world at the specified location
	 * @param x			the x location of the object
	 * @param y			the y location of the object
	 * @param height	the height location of the object
	 * @return			true if the object exists, otherwise false.
	 */
	public boolean anyExists(int x, int y, int height) {
		return objects.stream().anyMatch(object ->object.getX() == x && object.getY() == y && object.getHeight() == height);
	}
	
	/**
	 * Determines if an object exists in the game world
	 * @param id		the identification value of the object
	 * @param x			the x location of the object
	 * @param y			the y location of the object
	 * @return			true if the object exists, otherwise false.
	 */
	public boolean exists(int id, int x, int y) {
		return exists(id, x, y, 0);
	}
	
	public GlobalObject get(int id, int x, int y, int height) {
		Optional<GlobalObject> obj = objects.stream().filter(object -> object.getObjectId() == id && object.getX() == x
				&& object.getY() == y && object.getHeight() == height).findFirst();
		return obj.orElse(null);
		
	}
	
	/**
	 * All global objects have a unique value associated with them that is referred to as ticks remaining.
	 * Every six hundred milliseconds each object has their amount of ticks remaining reduced. Once an 
	 * object has zero ticks remaining the object is replaced with it's counterpart. If an object has a
	 * tick remaining value that is negative, the object is never removed unless indicated otherwise.
	 */
	public void pulse() {
		try {
			if (objects.size() == 0) {
				return;
			}
			Queue<GlobalObject> updated = new LinkedList<>();
			GlobalObject object = null;
			objects.removeAll(remove);
			remove.clear();
			while ((object = objects.poll()) != null) {
				if (object.getTicksRemaining() < 0) {
					updated.add(object);
					continue;
				}
				object.removeTick();
				if (object.getTicksRemaining() == 0) {
					updateObject(object, object.getRestoreId());
				} else {
					updated.add(object);
				}
			}
			objects.addAll(updated);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * Updates a single global object with a new object id in the game world for every player within a region.
	 * @param object	the new global object
	 * @param objectId	the new object id
	 */
	public void updateObject(final GlobalObject object, final int objectId) {
		//updateObject(object, objectId, false);
 	}
	
	
	/**
	 * Updates a single global object with a new object id in the game world for every player within a region.
	 * @param object	the new global object
	 * @param objectId	the new object id
	 */
	/*public void updateObject(final GlobalObject object, final int objectId, boolean update) {
		List<Player> players = World.getPlayers().stream().filter(Objects::nonNull).filter(player ->
			player.getPosition().distanceToPoint(object.getX(), object.getY()) <= 60 && player.getPosition().getZ() == object.getHeight()).collect(Collectors.toList());
		players.forEach(player -> player.getPA().object(objectId, object.getX(), object.getY(), object.getFace(), object.getType()));
		if(update) {
			object.updateType(objectId);
		}
 	}*/
	
	/**
	 * Updates all region objects for a specific player
	 * @param player	the player were updating all objects for
	 */
	/*public void updateRegionObjects(Player player) {
		objects.stream().filter(Objects::nonNull).filter(object -> player.distanceToPoint(
			object.getX(), object.getY()) <= 60 && object.getHeight() == player.heightLevel).forEach(object -> player.getPA().object(
				object.getObjectId(), object.getX(), object.getY(), object.getFace(), object.getType()));
		loadCustomObjects(player);
	}*/
	
	/**
	 * Used for spawning objects that cannot be inserted into the file
	 * @param player	the player
	 */
	/*private void loadCustomObjects(Player player) {
		player.getFarming().updateObjects();
	}*/
	
	/**
	 * Loads all object information from a simple text file
	 * @throws IOException	an exception likely to occur from file non-existence or during reading protocol
	 */
	/*public void loadGlobalObjectFile() throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("./Data/cfg/global-objects.cfg")))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				String[] data = line.split("\t");
				if (data.length != 6) {
					continue;
				}
				int id, x, y, height, face, type;
				try {
					id = Integer.parseInt(data[0]);
					x = Integer.parseInt(data[1]);
					y = Integer.parseInt(data[2]);
					height = Integer.parseInt(data[3]);
					face = Integer.parseInt(data[4]);
					type = Integer.parseInt(data[5]);
				} catch (NumberFormatException nfe) {
					//System.out.println("WARNING: Unable to load object from file.");
					continue;
				} catch (NullPointerException ne) {
					continue;
				}
				add(new GlobalObject(id, x, y, height, face, type, -1));
			}
		}
	}*/
	
	/**
	 * This is a convenience method that should only be referenced when
	 * testing game content on a private host. This should not be referenced
	 * during the active game. 
	 * @throws IOException 
	 */
	public void reloadObjectFile(Player player) throws IOException {
		objects.clear();
		//loadGlobalObjectFile();
		//updateRegionObjects(player);
	}

}
