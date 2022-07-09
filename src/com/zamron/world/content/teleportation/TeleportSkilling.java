package com.zamron.world.content.teleportation;

/**
 * Teleport class for Skilling.
 * @author Tyler
 *
 */

public class TeleportSkilling extends Teleporting {
	
	public static enum Skilling {
		TELEPORT_1(new String[] {"Green", "Dragons"}, new int[] {3351, 3680, 0}),
		TELEPORT_2(new String[] {"Zombie", "Graveyard"}, new int[] {3166, 3682, 0}),
		TELEPORT_3(new String[] {"Greater", "Demons"}, new int[] {3288, 3886, 0}),
		TELEPORT_4(new String[] {"Wilderness", "Castle"}, new int[] {3005, 3631, 0}),
		TELEPORT_5(new String[] {"West", "Dragons"}, new int[] {2980, 3599, 0}),
		TELEPORT_6(new String[] {"East", "Dragons"}, new int[] {3339, 3667, 0}),
		TELEPORT_7(new String[] {"Chaos", "Altar"}, new int[] {3239, 3619, 0}),
		TELEPORT_8(new String[] {"Rune", "Rocks"}, new int[] {3061, 3886, 0}),
		TELEPORT_9(new String[] {"Revenant", "Town"}, new int[] {3651, 3486, 0}),
		TELEPORT_10(new String[] {"Rogues'", "Castle"}, new int[] {3286, 3922, 0}),
		TELEPORT_11(new String[] {"Ice", "Plateau"}, new int[] {2953, 3901, 0}),
		TELEPORT_12(new String[] {"Safe Pvp", "Arena"}, new int[] {2815, 5511, 0});
		
		/**
		 * Initializing the teleport names.
		 */
		private String[] teleportName;
		/**
		 * Initializing the teleport coordinates.
		 */
		private int[] teleportCoordinates;
		
		/**
		 * Constructing the enumerator.
		 * @param teleportName
		 * 			The name of the teleport.
		 * @param teleportName2
		 * 			The secondary name of the teleport.
		 * @param teleportCoordinates
		 * 			The coordinates of the teleport.
		 */
		private Skilling(final String[] teleportName, final int[] teleportCoordinates) {
			this.teleportName = teleportName;
			this.teleportCoordinates = teleportCoordinates;
		}
		/**
		 * Setting the teleport name.
		 * @return
		 */
		public String[] getTeleportName() {
			return teleportName;
		}
		/**
		 * Setting the teleport coordinates.
		 * @return
		 */
		public int[] getCoordinates() {
			return teleportCoordinates;
		}
	}

}
