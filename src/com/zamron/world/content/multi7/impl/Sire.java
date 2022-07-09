package com.zamron.world.content.multi7.impl;

import com.zamron.model.Position;
import com.zamron.world.content.multi7.SpawnLocation;
import com.zamron.world.content.multi7.MultiBoss7;
import com.zamron.world.entity.impl.player.Player;

/**
 * Created by Stan van der Bend on 16/12/2017.
 * project: runeworld-game-server
 * package: runeworld.world.entity.combat.strategy.global.impl
 */
public class Sire extends MultiBoss7 {

    private final static int NPC_ID = 175;
    private final static int KEY_ITEM_ID = 19933;

    private final static String RECEIVE_DROP_MESSAGE = "You received a key drop from Kid Buu.";

    public Sire() {
        super(NPC_ID);
    }

    public boolean respawn = true;

    @Override
    public MultiBoss7 reincarnate() {
        return new Sire();
    }

    @Override
    protected void handleDrop(Player player) {
        player.giveItem(KEY_ITEM_ID, 1);
        player.sendMessage(RECEIVE_DROP_MESSAGE);
    }


    public boolean getRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    @Override
    protected SpawnLocation[] spawnLocations() {
        return new SpawnLocation[]{
                new SpawnLocation(new Position(2909, 2725, 0), "")
        };
    }

    @Override
    protected int minutesTillRespawn() {
        return 1;
    }

    @Override
    protected int minutesTillDespawn() {
    	return 20;
    }

    @Override
    public int maximumDrops() {
        return 50;
    }
}
