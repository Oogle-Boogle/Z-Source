package com.zamron.world.content.minigames.impl.gungame;

import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import java.util.Random;

/**
 * Created by Ruben Salomons on 1-10-2017.
 */
public class GunGame {

    private static final int LOCATION_X  = 2090;
    private static final int LOCATION_Y = 4430;

    private static final Position HOME = new Position(3087, 3495);

    @SuppressWarnings("unused")
	private static int HEIGHT = 2;

    private static NPC npc = null;

    public static void join(Player p) {
        if(p.getMinigameAttributes().getGunGameAttributes().isStarted())
            return;

        p.getPacketSender().sendInterfaceRemoval();
        p.moveTo(new Position(LOCATION_X, LOCATION_Y, p.getIndex() * 4));
        p.getMinigameAttributes().getGunGameAttributes().setStarted(true);
        p.getMinigameAttributes().getGunGameAttributes().setWave(1);
        DialogueManager.start(p, 170);
    }

    public static void leave(Player p) {
        if(!p.getMinigameAttributes().getGunGameAttributes().isStarted()) {
            return;
        }
        p.getCombatBuilder().reset(true);
        if(p.getRegionInstance() != null)
            p.getRegionInstance().destruct();
        p.restart();
        p.getMinigameAttributes().getGunGameAttributes().setStarted(false);
        p.getMinigameAttributes().getGunGameAttributes().setWave(1);
    }

    public static void start(Player p) {
        int wave = p.getMinigameAttributes().getGunGameAttributes().getWave();
        GungameData data = GungameData.getForWave(wave);

        if(p.getMinigameAttributes().getGunGameAttributes().getWaveSpawn() == wave) {
            return;
        }

        if(wave >= 6) {
            giveReward(p);
            p.getInventory().delete(1548, 1);
            return;
        }

        if(data.getDropId() != -1 && !p.getInventory().contains(data.getDropId())) {
            DialogueManager.start(p, 171);
            return;
        }

        if(data.getDropId() != 1) {
            p.getInventory().delete(data.getDropId(), 1);
        }

        p.moveTo(new Position(LOCATION_X, LOCATION_Y, p.getIndex() * 4));
        npc = new NPC(GungameData.getForWave(wave).getNpcId(), new Position(p.getPosition().getX(), p.getPosition().getY() + 1, p.getPosition().getZ()));
        World.register(npc);
        npc.setFindNewTarget(true);
        npc.setSpawnedFor(p);
        p.getMinigameAttributes().getGunGameAttributes().setWaveSpawn(wave);

    }

    @SuppressWarnings("unused")
	public static void handleDeath(Player p, int npcId) {
        if(!p.getMinigameAttributes().getGunGameAttributes().isStarted()) {
            return;
        }

        int wave = p.getMinigameAttributes().getGunGameAttributes().getWave();
        GungameData data = GungameData.getForWave(wave);
        if(wave == 6) {
            return;
        }
        p.getMinigameAttributes().getGunGameAttributes().setWave(wave + 1);
        GroundItemManager.spawnGroundItem(p, new GroundItem(new Item(GungameData.getForWave(wave + 1).getDropId()), p.getPosition(), "GunGame", true, -1, true, 1));
        npc.setVisible(false);
        World.deregister(npc);
    }

    private static final int[] rewards = {995, 123, 2912, 210, 11730, 15372, 15371, 15370, 15369, 6199, 20012, 3105, 2550,};

    public static void giveReward(Player p) {
        int random = new Random().nextInt(rewards.length);
        p.getInventory().add(new Item(rewards[random]));
        leave(p);
        p.moveTo(HOME);
    }
}
