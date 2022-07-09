package com.zamron.world.content.teleportation;



public class TeleportTraining extends Teleporting {
	
	public static enum Training {
		TELEPORT_1(new String[] {"Rock", "Crabs"}, new int[] {2679, 3714, 0}),
		TELEPORT_2(new String[] {"Experiments", ""}, new int[] {3561, 9948, 0}),
		TELEPORT_3(new String[] {"Bandit", "Camp"}, new int[] {3169, 2982, 0}),
		
		TELEPORT_4(new String[] {"Yak", "Field"}, new int[] {3206, 3263, 0}),

		TELEPORT_5(new String[] {"Ghoul", "Field"}, new int[] {3420, 3510, 0}),
		
		TELEPORT_6(new String[] {"Armoured", "Zombies"}, new int[] {3086, 9672, 0}),
		TELEPORT_7(new String[] {"Dust", "Devils"}, new int[] {3277, 2964, 0}),
		TELEPORT_8(new String[] {"Monkey", "Skeletons"}, new int[] {2805, 9143, 0}),
		TELEPORT_9(new String[] {"Monkey", "Guards"}, new int[] {2793, 2773, 0}),
		
		TELEPORT_10(new String[] {"Chaos", "Druids"}, new int[] {2933, 9848, 0}),
		TELEPORT_11(new String[] {"Chicken", "Pen"}, new int[] {3235, 3295, 0}),
		TELEPORT_12(new String[] {"Tzhaar", "Minions"}, new int[] {2480, 5174, 0});
		
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
		private Training(final String[] teleportName, final int[] teleportCoordinates) {
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

