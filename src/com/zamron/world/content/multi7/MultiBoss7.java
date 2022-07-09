package com.zamron.world.content.multi7;

import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.content.multi.MultiBoss;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.npc.NPCMovementCoordinator;
import com.zamron.world.entity.impl.player.Player;

/**
 * Created by Stan van der Bend on 16/12/2017.
 * project: runeworld-game-server
 * package: runeworld.world.entity.combat.strategy
 */
public abstract class MultiBoss7 extends NPC {

    private boolean respawn = true;

    public MultiBoss7(int id) {
        super(id, new Position(2736, 3473, 0));
    }


    int cyclesOutOfCombat = 0;

    @Override
    public void sequence(){
        super.sequence();
    }

    public abstract MultiBoss7 reincarnate();

    /**
     * Handles any additional behaviour upon the spawning of this {@link MultiBoss}.
     */
    public void spawn(){        
        this.setAttackDistance(12);
        this.getDefinition().setAggressive(false);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawnTime(-1);
        setLocation(Locations.Location.getLocation(this));
        World.register(this);
    }

    /**
     * Handles the drop after this {@link MultiBoss} has been killed for the top {@link MultiBoss#maximumDrops()} players.
     *
     * @param player    a rewarded {@link Player}.
     */
    protected abstract void handleDrop(Player player);

    /**
     * The potential {@link SpawnLocation}s this {@link MultiBoss} can spawn at.
     *
     * @return all potential {@link SpawnLocation}s.
     */
    protected abstract SpawnLocation[] spawnLocations();

    /**
     * The amount of time it takes for this {@link MultiBoss} to respawn after it has de-spawned.
     *
     * @return respawn time in minutes.
     */
    protected abstract int minutesTillRespawn();

    /**
     * The amount of time after which this {@link MultiBoss} de-spawns due to lack of combat engagement.
     *
     * @return de-spawn time in minutes.
     */
    protected abstract int minutesTillDespawn();


    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public boolean getRespawn() {
       return this.respawn;
    }
    /**
     *The maximum amount of players that can receive a drop based on their damage.
     *
     * @return the maximum amount of drops.
     */
    public abstract int maximumDrops();

    private NPCMovementCoordinator movementCoordinator = new NPCMovementCoordinator(this);

    public NPCMovementCoordinator getMovementCoordinator() {
        return movementCoordinator;
    }

}
