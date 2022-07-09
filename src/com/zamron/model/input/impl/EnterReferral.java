package com.zamron.model.input.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class EnterReferral extends Input {
	public static final String[] rewardableUsers = new String[] {"", "", "", ""};
	public static final String[] temporaryCodes = new String[] {"Oogle","Retro"};
	public static final String[] merkCode = new String[] {"",""};
    @Override
    public void handleSyntax(Player player, String syntax) {
        player.hasReferral = true;
    referralResponse(player, syntax);
    
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("data/saves/referrals/referral_data.txt", true));
            w.write(player.getUsername() + " - " + syntax + " - " + player.getHostAddress());
            w.newLine();
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
    }
    }
	
        public static void referralResponse(Player player, String username) {
        	if(Arrays.stream(rewardableUsers).anyMatch(username::equalsIgnoreCase)) {
        	        player.getInventory().addItem(19121, 1);
        	        player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
            } else if(Arrays.stream(temporaryCodes).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(15373, 1);
            	player.getInventory().add(16432, 200);
            	player.getInventory().add(2572, 1);
                player.getInventory().add(3459, 1);
                player.getInventory().add(6199, 2);
                player.getInventory().addItem(19121, 1);
            	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
            	
            } else if(Arrays.stream(merkCode).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(3082, 1);
            	player.getInventory().add(15373, 1);
                player.getInventory().add(16432, 200);
            	player.getInventory().add(2572, 1);
            	player.getInventory().add(537, 100);
            	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
            }
        }
}