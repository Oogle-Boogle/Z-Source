package com.zamron.world.content.teleportation;

public class TeleportBosses extends Teleporting  {

	public static enum Bosses {
		TELEPORT_1(new String[] {"King Black", "Dragon"}, new int[] {2273, 4681, 0}),
		TELEPORT_2(new String[] {"Godwars", "Dungeon"}, new int[] {2871, 5319, 2}),
		TELEPORT_3(new String[] {"Kalphite", "Queen"}, new int[] {3488, 9516, 0}),
		TELEPORT_4(new String[] {"Slash", "Bash"}, new int[] {2547, 9447, 0}),
		TELEPORT_5(new String[] {"Frost Dragons", "(not wilderness)"}, new int[] {2802, 4790, 0}),
		TELEPORT_6(new String[] {"Dagannoth", "Kings"}, new int[] {1912, 4367, 0}),
		TELEPORT_7(new String[] {"Tormented", "Demons"}, new int[] {2540, 5774, 0}),
		TELEPORT_8(new String[] {"Chaos Ele", "(wilderness)"}, new int[] {3276, 3915, 0}),
		TELEPORT_9(new String[] {"Corporeal", "Beast"}, new int[] {2886, 4376, 0}),
		TELEPORT_10(new String[] {"AS", "Warrior"}, new int[] {2766, 2799, 0}),
		TELEPORT_11(new String[] {"Barrelchest", "Area"}, new int[] {2973, 9517, 3}),
		TELEPORT_12(new String[] {"Lizardman", "Shaman"}, new int[] {2718, 9811, 1});
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
		private Bosses(final String[] teleportName, final int[] teleportCoordinates) {
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
