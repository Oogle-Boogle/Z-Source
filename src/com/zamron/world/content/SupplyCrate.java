package com.zamron.world.content;

import com.zamron.model.Animation;
import com.zamron.model.Graphic;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class SupplyCrate {

    /**
     *
     * @Author Retro
     * Zamron
     *
     */

    static int boxId = 7630;
    static int anim = 1894;

    public static void openBox(Player player) {
        int chance = RandomUtility.exclusiveRandom(50);
        player.setAnimation(new Animation(anim));
        player.performGraphic(new Graphic(678));
        player.getInventory().add(10835, 10000);
        player.getInventory().add(3144,1000);
        player.getInventory().add(3025,100);
        player.getInventory().add(19080,100);

        player.getInventory().delete(boxId); //deletes box from their inventory

        if (chance >= 40 && chance <= 45) {
            player.getInventory().add(5170,1);
            player.performAnimation(new Animation(1914));
            player.performGraphic(new Graphic(101));
            player.forceChat("Yay!");
            player.getPacketSender().sendMessage("Congratulations! You received a lucky item!");
        } else if (chance >= 46) {
            player.getInventory().add(5185, 1);
            player.performAnimation(new Animation(1914));
            player.performGraphic(new Graphic(101));
            player.forceChat("Yay!");
            player.getPacketSender().sendMessage("Congratulations! You received a lucky item!");
        }
    }
}
