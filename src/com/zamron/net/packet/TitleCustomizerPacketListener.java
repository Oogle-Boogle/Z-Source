package com.zamron.net.packet;

import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

public class TitleCustomizerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		//System.out.println("ITS A PACKET!!!!!!!!!!");
		//int length = packet.readUnsignedByte();
		
		
		String message = Misc.readString(packet.getBuffer());
		
		
		/** Gets requested bytes from the buffer client > server **/
		
		
		//System.out.println("String is: " + message);

	}

}
