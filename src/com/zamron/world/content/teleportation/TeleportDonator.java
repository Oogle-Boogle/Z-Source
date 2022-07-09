package com.zamron.world.content.teleportation;

public class TeleportDonator extends Teleporting {
	public static enum Donator {
		TELEPORT_1(new String[] {"Edgeville", "Dungeon"}, new int[] {3097, 9870, 0}),
		TELEPORT_2(new String[] {"Chaos", "Tunnels"}, new int[] {3185, 5471, 0}),
		TELEPORT_3(new String[] {"Brimhaven", "Dungeon"}, new int[] {2713, 9564, 0}),
		TELEPORT_4(new String[] {"Taverley", "Dungeon"}, new int[] {2884, 9797, 0}),
		TELEPORT_5(new String[] {"Strykewyrm", "Cavern"}, new int[] {2731, 5095, 0}),
		TELEPORT_6(new String[] {"Ancient", "Cavern"}, new int[] {1745, 5325, 0}),
		TELEPORT_7(new String[] {"Metal", "Dragons"}, new int[] {2711, 9464, 0}),
		TELEPORT_8(new String[] {"Ape Atoll", "Dungeon"}, new int[] {2804, 9146, 0}),
		TELEPORT_9(new String[] {"Slayer", "Tower"}, new int[] {3429, 3538, 0}),
		TELEPORT_10(new String[] {"Fremmenik", "Slayer Dung"}, new int[] {2805, 10001, 0}),
		TELEPORT_11(new String[] {" ", " "}, new int[] {2804, 9146, 10}),
		TELEPORT_12(new String[] {" ", " "}, new int[] {3429, 3538, 10});

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
		private Donator(final String[] teleportName, final int[] teleportCoordinates) {
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
