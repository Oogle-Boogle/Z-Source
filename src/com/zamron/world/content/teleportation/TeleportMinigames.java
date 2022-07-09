package com.zamron.world.content.teleportation;

public class TeleportMinigames extends Teleporting {
	public static enum Minigames {
		TELEPORT_1(new String[] {"Barrows", "Minigame"}, new int[] {3565, 3313, 0}),
		TELEPORT_2(new String[] {"Fight", "Caves"}, new int[] {2445, 5177, 0}),
		TELEPORT_3(new String[] {"Fight", "Pits"}, new int[] {2399, 5177, 0}),
		TELEPORT_4(new String[] {"Pest", "Control"}, new int[] {2663, 2654, 0}),
		TELEPORT_5(new String[] {"Duel", "Arena"}, new int[] {3364, 3267, 0}),
		TELEPORT_6(new String[] {"Warrior's", "Guild"}, new int[] {2855, 3543, 0}),
		TELEPORT_7(new String[] {"Recipe For", "Disaster"}, new int[] {3080, 3498, 0}),
		TELEPORT_8(new String[] {"Nomad's", "Requeim"}, new int[] {1891, 3177, 0}),
		TELEPORT_9(new String[] {"Zombie", "Slaughter"}, new int[] {3503, 3563, 0}),
		TELEPORT_10(new String[] {" ", " "}, new int[] {2933, 9848, 10}),
		TELEPORT_11(new String[] {" ", " "}, new int[] {3235, 3295, 10}),
		TELEPORT_12(new String[] {" ", " "}, new int[] {2480, 5174, 10});

		
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
		private Minigames(final String[] teleportName, final int[] teleportCoordinates) {
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
