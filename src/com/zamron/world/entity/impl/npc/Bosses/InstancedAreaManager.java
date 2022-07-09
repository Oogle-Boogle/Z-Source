package com.zamron.world.entity.impl.npc.Bosses;

import java.util.HashMap;
import java.util.Map;

import com.zamron.world.World;

public class InstancedAreaManager {
	
	/**
	 * A single instance of this class for global usage
	 */
	private static final InstancedAreaManager SINGLETON = new InstancedAreaManager();
	
	/**
	 * The maximum height of any one instance
	 */
	private static final int MAXIMUM_HEIGHT = 4 * 1024;
	
	/**
	 * A mapping of all {@InstancedArea} objects that are being operated on
	 * and are active.
	 */
	private Map<Integer, InstancedArea> active = new HashMap<>();
	
	/**
	 * A private empty {@link InstancedAreaManager} constructor exists to ensure that
	 * no other instance of this class can be created from outside this class.
	 */
	private InstancedAreaManager() { }
	
	/**
	 * Creates a new {@link SingleInstancedArea} object with the given params
	 * @param height
	 * 			the height of the new instance
	 * @param instance	
	 * 			the instance that will be added
	 * @return	
	 * 			null if no height can be found for this area, otherwise the new 
	 * 			{@link SingleInstancedArea} object will be returned.
	 */
	public void add(int height, InstancedArea instance) {
		active.put(height, instance);
	}
	
	/**
	 * Determines if the {@link InstancedArea} paramater exists within
	 * the mapping of active {@link InstancedArea} objects and can be 
	 * disposed of.
	 *  
	 * @param area	the instanced area 
	 * @return		true if the area exists in the mapping with the same height level
	 * 				and the same reference
	 */
	public boolean disposeOf(InstancedArea area) {
		int height = area.getHeight();
		if (!active.containsKey(height)) {
			return false;
		}
		InstancedArea found = active.get(height);
		if (!found.equals(area)) {
			return false;
		}
		active.remove(height);
		return true;
	}
	
	/**
	 * Retrieves an open height level by sifting through the mapping and attempting
	 * to retrieve the lowest height level.
	 * @return	
	 * 		the next lowest, open height level will be returned otherwise -1
	 * 		will be returned. When -1 is returned it signifies that there are
	 * 		no heights open from 0 to {@link MAXIMUM_HEIGHT}.
	 */
	public int getNextOpenHeight(Boundary boundary) {
		for (int height = 0; height < MAXIMUM_HEIGHT; height += 4) {
			if (active.containsKey(height)) {
				continue;
			}
			final int heightLevel = height;
			if (World.getPlayers().stream().anyMatch(p -> Boundary.isIn(p, boundary) && p.getPosition().getZ() == heightLevel)) {
				continue;
			}
			return height;
		}
		return -1;
	}
	
	/**
	 * Retrieves the single instance of this class
	 * @return	the single instance
	 */
	public static InstancedAreaManager getSingleton() {
		return SINGLETON;
	}

}
