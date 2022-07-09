package com.zamron.net.packet;

public class SantaHatCustomization {
	
}
/*package com.platinum.net.packet;

import java.util.Arrays;

import com.platinum.model.Flag;
import com.platinum.world.entity.impl.player.Player;

public class SantaHatCustomization implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int itemId = packet.readUnsignedShort();
		int size = packet.readUnsignedByte();

		//System.out.println("Worked");
		//System.out.println(itemId);

		switch (itemId) {

		case 1050:

			int[] colors = new int[size];

			//System.out.println("Size: " + size);

			for (int i = 0; i < size; i++) {
				colors[i] = packet.readInt();
				//System.out.println("Colors set(loop): " + colors[i]);
			}

			itemId = player.getCurrentHat();

			if (!player.getInventory().contains(itemId)) {
				return;
			}

			if (itemId == 1050) {
				player.setSantaColors(colors);
				//System.out.println("Colors set to: " + Arrays.toString(colors));

			}

			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendInterfaceRemoval();
			// player.setCurrentHat(-1);

			break;
		}

	}

}*/
