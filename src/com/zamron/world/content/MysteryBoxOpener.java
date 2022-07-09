package com.zamron.world.content;

import java.util.HashMap;
import java.util.Map;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;

public class MysteryBoxOpener {

    private Player player;

    public MysteryBoxOpener(Player player) {
        this.player = player;
    }

    private int openBox = -1;

    public int getOpenBox() {
        return openBox;
    }
    public void setOpenBox(int boxId) {
        this.openBox = boxId;
    }

    private int[] common, uncommon, rare;

    public void display(int boxId, String name, int[] common, int[] uncommon, int[] rare) {
        //player.getPacketSender().sendInterface(48030);
        openBox = boxId;
        this.common = common;
        this.uncommon = uncommon;
        this.rare = rare;
        //updateInterface(boxId, name, common, uncommon, rare);
    }

    private void updateInterface(int boxId, String name, int[] common, int[] uncommon, int[] rare) {

        for(int i = 0; i < 3; i++) {
            player.getPacketSender().resetItemsOnInterface(48051 + i * 10,150);
        }
        player.getPacketSender().sendString(48035, name);
        player.getPacketSender().sendItemOnInterface(48045, boxId, 0, 1);

        for (int i = 0; i < common.length; i++) {
            player.getPacketSender().sendItemOnInterface(48051, common[i], i, 1);
        }

        for (int i = 0; i < uncommon.length; i++) {
            player.getPacketSender().sendItemOnInterface(48061, uncommon[i], i, 1);
        }

        for (int i = 0; i < rare.length; i++) {
            player.getPacketSender().sendItemOnInterface(48071, rare[i], i, 1);
        }

    }

    public void open(int boxId) {
        int reward = -1;
        int chance = RandomUtility.inclusiveRandom(0, 100);

        if (chance > 93) { // OH LOL oh lol
            reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
            /**World.sendMessageNonDiscord("<shad=bf0000>[Rare Reward]</shad>@bla@: "+player.getUsername().toString() +
                    " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(reward).getName() +
                    " </shad>@bla@from the @red@" + ItemDefinition.forId(boxId).getName() );**/
        } else if (chance > 65) {
            reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
        } else if (chance >= 0) {
            reward = common[RandomUtility.exclusiveRandom(0, common.length)];
        }
        //	player.getAchievementTracker().progress(AchievementData.OPEN_10_BOXES, 1);
        player.getInventory().delete(boxId, 1);
        player.getInventory().add(reward, 1);
    }

    public void openAll(int boxId) {
        int amount = player.getInventory().getAmount(boxId);
        Map<Integer, Integer> rewards = new HashMap<>();
        for (int i = 0; i < amount; i++) {
            int reward = -1;
            int chance = RandomUtility.inclusiveRandom(0, 100);

            if (chance > 95) { //90% of 100% = 10%
                reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
                /**World.sendMessageNonDiscord("<shad=bf0000>[Rare Reward]</shad>@bla@: "+player.getUsername().toString() +
                        " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(reward).getName() +
                        " </shad>@bla@from the @red@" + ItemDefinition.forId(boxId).getName() );**/
            } else if (chance > 65) { //65% of 100% = 35% (remainder)
                reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
            } else if (chance >= 0) { //0% of 100% = 55% (remainder)
                reward = common[RandomUtility.exclusiveRandom(0, common.length)]; // ye its correct.
            }

            rewards.merge(reward, 1, Integer::sum);

        }
        player.getInventory().delete(boxId, amount);
        boolean bank = amount <= player.getInventory().getFreeSlots();
        rewards.entrySet().forEach(r -> {
            if (bank) {
                player.getInventory().add(r.getKey(), r.getValue());
            } else {
                player.getBank(0).add(r.getKey(), r.getValue());
            }
        });
    }

}
