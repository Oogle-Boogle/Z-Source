package com.zamron.model.input.impl;

import com.zamron.model.input.Input;
import com.zamron.util.NameUtils;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeUsername extends Input {

    /**
     * @Author Oogle
     * DOES NOT WORK ATM
     */

    int Scroll = 18343;

    boolean characterName = new File("../data/def/saves/characters/").isFile();
    Path characterFiles = Paths.get("..data/def/saves/characters/");

        //@Override
        /**public void handleSyntax(Player player, String syntax) {
            player.getPacketSender().sendInterfaceRemoval();
            if(syntax == null || syntax.length() <= 2 || syntax.length() > 12 || !NameUtils.isValidName(syntax)) {
                player.getPacketSender().sendMessage("That username is invalid please try another username.");
                return;
            }
            if (characterName) {
                player.getPacketSender().sendMessage("That username is already taken. Try another username.");
                System.out.println("Reached here");
            }
            if (!characterName) {
                try {
                    System.out.println("Renamed username to " +syntax);
                    player.getInventory().delete(Scroll, 1);
                    World.deregister(player);

                    player.setUsername(syntax);
                    player.getUsername().replace(player.getUsername(), syntax);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //return;
            }
        }**/
    }
