package com.zamron.world.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.GroundItem;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.util.Misc;
import com.zamron.world.content.combat.CombatBuilder.CombatDamageCache;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class DailyNpc extends NPC {

    public static int[] COMMONLOOT = { 10835};
    public static int[] RARELOOT = { };
    public static int[] SUPERRARELOOT = { 13265,13266,19158,13270,13267,13268};

    /**
     *
     */
    public static final int NPC_ID = -1;

    /**
     * add your maps to that folder open me your client.java in client
     */
    public static final DailyNpcLocation[] LOCATIONS = { new DailyNpcLocation(2413, 4681, 0, " ( ::hween )") };

    /**
     *
     */
    private static DailyNpc current;

    /**
     *
     * @param position
     */
    public DailyNpc(Position position) {

        super(NPC_ID, position);
    }

    /**
     *
     */
    public static void initialize() {

        TaskManager.submit(new Task(30000000, false) { // 6000

            @Override
            public void execute() {
                spawn();
            }

        });

    }

    /**
     *
     */
    public static void spawn() {

        if (getCurrent() != null) {
            return;
        }

        DailyNpcLocation location = Misc.randomElement(LOCATIONS);
        DailyNpc instance = new DailyNpc(location.copy());

        // //System.out.println(instance.getPosition());

        World.register(instance);
        setCurrent(instance);
        // System.out.print("spawned.");

        World.sendMessageNonDiscord("<img=12><shad=200><col=000000>[<col=b96900>HALLOWEEN<col=000000>]<col=b96900>Killer Pumpkin has respawned!  " + location.getLocation() + "!");
    }

    /**
     *
     * @param npc
     */
    public static void handleDrop(NPC npc) {
        World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

        setCurrent(null);

        if (npc.getCombatBuilder().getDamageMap().size() == 0) {
            return;
        }

        Map<Player, Integer> killers = new HashMap<>();

        for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

            if (entry == null) {
                continue;
            }

            long timeout = entry.getValue().getStopwatch().elapsed();

            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            Player player = entry.getKey();

            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                continue;
            }

            killers.put(player, entry.getValue().getDamage());

        }

        npc.getCombatBuilder().getDamageMap().clear();

        List<Entry<Player, Integer>> result = sortEntries(killers);
        int count = 0;

        for (Entry<Player, Integer> entry : result) {

            Player killer = entry.getKey();
            int damage = entry.getValue();

            handleDrop(npc, killer, damage);

            if (++count >= 20) {
                break;
            }

        }

    }

    public static void giveLoot(Player player, NPC npc, Position pos) {
        int chance = Misc.getRandom(1000);
        int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
        @SuppressWarnings("unused")
        int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
        int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
        int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

        GroundItemManager.spawnGroundItem(player,
                new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));

        if (chance >= 990) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(superrare).getDefinition().getName());
            Misc.anOrA(itemName);
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() +"received<col=eaeaea>[ " + itemName + "<col=eaeaea>]<col=FF0000>from Sephiroth");
            return;
        }

        if (chance >= 699) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(rare).getDefinition().getName());
            Misc.anOrA(itemName);
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() + "received<col=eaeaea>[ " + itemName + "<col=eaeaea>]<col=FF0000>from Sephiroth");
            return;
        }
        if (chance >= 1) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(10835, 100), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(common).getDefinition().getName());
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() + "received<col=eaeaea>[ " + itemName + "<col=eaeaea>]<col=FF0000>from Sephiroth");
            return;
        }

    }

    /**
     *
     * @param npc
     * @param player
     * @param damage
     */
    private static void handleDrop(NPC npc, Player player, int damage) {
        Position pos = npc.getPosition();
        giveLoot(player, npc, pos);
    }

    /**
     *
     * @param map
     * @return
     */
    static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

        List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {

            @Override
            public int compare(Entry<K, V> e1, Entry<K, V> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }

        });

        return sortedEntries;

    }

    /**
     *
     * @return
     */
    public static DailyNpc getCurrent() {
        return current;
    }

    /**
     *
     * @param current
     */
    public static void setCurrent(DailyNpc current) {
        DailyNpc.current = current;
    }

    /**
     *
     * @author Levi <levi.patton69 @ skype>
     *
     */
    public static class DailyNpcLocation extends Position {

        /**
         *
         */
        private String location;

        /**
         *
         * @param x
         * @param y
         * @param z
         * @param location
         */
        public DailyNpcLocation(int x, int y, int z, String location) {
            super(x, y, z);
            setLocation(location);
        }

        /**
         *
         * @return
         */

        String getLocation() {
            return location;
        }

        /**
         *
         * @param location
         */
        public void setLocation(String location) {
            this.location = location;
        }

    }

}
