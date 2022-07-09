package com.zamron.world.content.multi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.zamron.GameSettings;
import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Position;
import com.zamron.model.definitions.NPCDrops;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatBuilder;
import com.zamron.world.content.combat.CombatFactory;
//import com.platinum.world.content.multi.impl.Chicken;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

/**
 * Created by Stan van der Bend on 16/12/2017.
 * project: runeworld-game-server
 * package: runeworld.world.entity.combat.strategy.global
 */
public abstract class MultiBossHandler extends NPC{

    private final static List<MultiBoss> MULTI_BOSSES = new ArrayList<>();

    public static void init(){
        //register(new Dragonix());
        //register(new Chicken());
      
    }


    public MultiBossHandler(int id) {
        super(id, new Position(2400, 3123, 0));
    }

    static void register(MultiBoss multiBoss){
        MULTI_BOSSES.add(multiBoss);

        if(multiBoss.getRespawn()) {
            final long millisTillRespawn = TimeUnit.MINUTES.toMillis(multiBoss.minutesTillRespawn());
        final int cyclesTillRespawn = Math.toIntExact(millisTillRespawn / GameSettings.GAME_PROCESSING_CYCLE_RATE);

        System.out.println("A "+multiBoss.getDefinition().getName()+" will spawn in "+cyclesTillRespawn+" cycles.");
       

        TaskManager.submit(new Task(cyclesTillRespawn, false) {
            @Override
            protected void execute() {
                multiBoss.spawn();
                multiBoss.setRespawn(false);
                stop();
            }
        });
        }
    }
    static void deRegister(MultiBoss multiBoss){
    	System.out.println("Deregistered global "+multiBoss.getDefinition().getName());
        MULTI_BOSSES.remove(multiBoss);
        World.deregister(multiBoss);
    }

    public static void onDeath(MultiBoss npc) {
        handleDrop(npc);
        deRegister(npc);
        register(npc.reincarnate());
    }
    private static void handleDrop(MultiBoss npc) {
    	npc.getCombatBuilder().getDamageMap().size();

        if(npc.getCombatBuilder().getDamageMap().size() == 0)
            return;

        final Map<Player, Integer> killers = new HashMap<>();

        for(Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {
        	
            if(entry == null)
                continue;

            final long timeout = entry.getValue().getStopwatch().elapsed();
            
            if(timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT)
                continue;

            final Player player = entry.getKey();
          
            if(player.getConstitution() <= 0 || !player.isRegistered())
                continue;

            killers.put(player, entry.getValue().getDamage());
        }

        npc.getCombatBuilder().getDamageMap().clear();

        List<Map.Entry<Player, Integer>> result = sortEntries(killers);
        int count = 0;

        for(Map.Entry<Player, Integer> entry : result) {

            final Player killer = entry.getKey();

            NPCDrops.dropItems(killer, npc);

            if(++count >= npc.maximumDrops())
                break;
        }
    }



    private static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {
        final List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }

    public static List<MultiBoss> getBosses(){
    	return MULTI_BOSSES;
    }
}
