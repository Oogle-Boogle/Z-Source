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
import com.zamron.util.Stopwatch;
import com.zamron.world.content.combat.CombatBuilder.CombatDamageCache;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.World;
import com.zamron.world.content.combat.CombatFactory;
import com.zamron.world.entity.impl.GroundItemManager;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class DarkRanger extends NPC {

    public static int[] COMMONLOOT = { 12708, 4770, 4771, 4772, 18782 };
    public static int[] RARELOOT = { 3914, 19886, 18347, 4781, 4782, 4783, 4785, 3957, 18392, };
    public static int[] SUPERRARELOOT = { 3077, 6312, 6309, 6308, 6307, 6311, 6310, 19938, 19937, 3990, 3991, 5081, 5125, 5133, 10905};

    /**
     *
     */
    public static final int NPC_ID = 299;

    public static int TIME = 8500;

    private static Stopwatch timer = new Stopwatch().reset();

    /**
     * add your maps to that folder open me your client.java in client
     */
    public static final DarkRangerLocations[] LOCATIONS = { new DarkRangerLocations(2212, 5100, 0, "<col=0999ad> <img=12> ::darkranger ") };

    /**
     *
     */
    private static DarkRanger current;

    /**
     *
     * @param position
     */
    public DarkRanger(Position position) {

        super(NPC_ID, position);
    }

    /**
     *
     */
    public static void initialize() {

        TaskManager.submit(new Task( 8500, false) { // 6000

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

        DarkRangerLocations location = Misc.randomElement(LOCATIONS);
        DarkRanger instance = new DarkRanger(location.copy());

        // //System.out.println(instance.getPosition());

        World.register(instance);
        setCurrent(instance);
        System.out.print("Dark Ranger has spawned..");

        World.sendMessageDiscord("<img=12><col=bababa><shad=10>[<col=0999ad>BOSS<col=bababa>]<col=0999ad>Dark Ranger <col=00a745>has <shad=10>respawned <img=12> ::DarkRanger");
    }

    public static void handleDrop(NPC npc) {
       // World.getPlayers().forEach(p -> p.getPacketSender().sendString(26707, "@or2@WildyWyrm: @gre@N/A"));

        setCurrent(null);

        timer.reset();

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
            if (damage >= 10000) {

                handleDrop(npc, killer, damage);
                NpcGain.WorldBossXP(killer);

                if (++count >= 10) {
                    break;
                }
            }
        }

    }
    /**
     *
     * @param npc
     */
    public static void giveLoot(Player player, NPC npc, Position pos) {
        int chance = Misc.getRandom(300);
        int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
        @SuppressWarnings("unused")
        int common1 = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
        int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
        int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

        GroundItemManager.spawnGroundItem(player,
                new GroundItem(new Item(10835, 5000), pos, player.getUsername(), false, 150, true, 200));
        GroundItemManager.spawnGroundItem(player,
                new GroundItem(new Item(11212, 1000), pos, player.getUsername(), false, 150, true, 200));

        if (chance >= 290) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(superrare).getDefinition().getName());
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=12>[ " + itemMessage + "<col=eaeaea>]<img=12><col=FF0000>from the Dark Ranger!");
            DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from the Dark Ranger!");
            return;
        }

        if (chance >= 185) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(rare).getDefinition().getName());
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() + " received<img=12><col=eaeaea>[ " + itemMessage + "<col=eaeaea>]<img=12><col=FF0000> from the Dark Ranger!");
            DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemMessage + " from the Dark Ranger!");
            return;
        }
        if (chance >= 0) {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(new Item(common, 1), pos, player.getUsername(), false, 150, true, 200));
            String itemName = (new Item(common).getDefinition().getName());
            World.sendMessageNonDiscord(
                    "<img=12><col=FF0000>" + player.getUsername() + " received<col=eaeaea><img=12>[<col=07b481> " + itemName + "<col=eaeaea>]<img=12><col=FF0000> from the Dark Ranger!");
            DiscordMessenger.sendRareDrop(player.getUsername(), " Just received " + itemName + " from the Dark Ranger!");
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
    public static DarkRanger getCurrent() {
        return current;
    }

    /**
     *
     * @param current
     */
    public static void setCurrent(DarkRanger current) {
        DarkRanger.current = current;
    }

    /**
     *
     * @author Levi <levi.patton69 @ skype>
     *
     */
    public static class DarkRangerLocations extends Position {

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
        public DarkRangerLocations(int x, int y, int z, String location) {
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