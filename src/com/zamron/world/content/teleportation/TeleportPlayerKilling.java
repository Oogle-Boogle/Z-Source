package com.zamron.world.content.teleportation;

public class TeleportPlayerKilling extends Teleporting  {

	public static enum PlayerKilling {
		TELEPORT_1(new String[] {"Phoenix", "Cave"}, new int[] {2839, 9557, 0}),
		TELEPORT_2(new String[] {"Bandos", "Avatar"}, new int[] {2891, 4767, 0}),
		TELEPORT_3(new String[] {"Glacors", "Cave"}, new int[] {3050, 9573, 0}),
		TELEPORT_4(new String[] {"Nex", "Dungeon"}, new int[] {2903, 5203, 0}),
		TELEPORT_5(new String[] {"Scorpia", "(wilderness)"}, new int[] {3136, 3950, 0}),
		TELEPORT_6(new String[] {"Venenatis", "(wilderness)"}, new int[] {3350, 3734, 0}),
		TELEPORT_7(new String[] {"Cerberus", "Cave"}, new int[] {1240, 1243, 0}),
		TELEPORT_8(new String[] {"Skotizo", "Boss"}, new int[] {3378, 9816, 0}),
		TELEPORT_9(new String[] {"Abby Sire", "(wilderness)"}, new int[] {3333, 3864, 0}),
		
		TELEPORT_10(new String[] {"Therm", "Smoke Devil"}, new int[] {2388, 4692, 0}),
		TELEPORT_11(new String[] {"Naza", "(multi)"}, new int[] {3056, 9555, 0}),
		TELEPORT_12(new String[] {"", ""}, new int[] {3429, 3538, 10});
		
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
		private PlayerKilling(final String[] teleportName, final int[] teleportCoordinates) {
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
