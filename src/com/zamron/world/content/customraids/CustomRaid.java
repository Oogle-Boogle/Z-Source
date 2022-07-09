package com.zamron.world.content.customraids;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.skill.impl.pvm.NpcGain;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

import java.security.SecureRandom;
import java.util.*;

public class CustomRaid {

    private final Player player;

    public CustomRaid(Player player) {
        this.player = player;
    }

    private final Map<RaidDifficulty, List<List<NPC>>> waves = new HashMap<>();

    private final Map<String, Long> damageMap = new HashMap<>();

    public Map<String, Long> getDamageMap() {
        return damageMap;
    }

    public void incrementDamage(String username, long damage) {
        damageMap.merge(username, damage, Long::sum);
        player.getRaidParty().getMembers().forEach(this::updateOverlay);
    }

    private final int STARTING_POINT = 27680;

    private final Random random = new SecureRandom();

    private final int OVERLAY_ID = 27800;

    private final Position INSIDE_RAID_ARENA = new Position(3037, 5212);

    private final Position RAID_LOBBY = new Position(3037, 5233, 0);


    public void open(Player player) {
        player.getPacketSender().sendInterface(27680);
        player.getPacketSender().sendInterfaceReset();
        Map<Integer, String> stringMap = new HashMap<>();
        for (int i = 0; i < RaidDifficulty.DIFFICULTIES.length; i++) {
            stringMap.put(STARTING_POINT + 49 + i, RaidDifficulty.DIFFICULTIES[i].getName());
        }
        stringMap.forEach((id, text) -> {
            player.getPacketSender().sendString(id, text);
        });
        updateOverlay(player);
    }

    private void updateOverlay(Player player) {
        List<Map.Entry<String, Long>> sortedDamageMap = sortEntries(damageMap);
        for (int i = 0; i < 5; i++) {
            player.getPacketSender().sendString(OVERLAY_ID + 2 + i, "");
        }
        int index = 0;
        for (Map.Entry<String, Long> entry : sortedDamageMap) {
            player.getPacketSender()
                    .sendString(OVERLAY_ID + 2 + index, "@red@" + entry.getKey() + ": " + Misc
                            .formatNumber(entry.getValue()));
            index++;
        }
    }

    private <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }

    private int currentWave = 0;
    private int count = 0;
    private int targetCount = 0;

    public void init(Player other) {
        if (!player.getRaidParty().isOwner(other)) {
            other.sendMessage("Only the party owner can start the raid");
            return;
        }
        RaidDifficulty raidDifficulty = player.getRaidParty().getRaidDifficulty();
        initNpcs();
        List<List<NPC>> npcs = waves.get(raidDifficulty);
        npcs.stream().flatMap(List::stream).forEach(npc -> {
            npc.setPosition(new Position(npc.getPosition().getX(), npc.getPosition()
                    .getY(), player.getIndex() * 200));
        });
        damageMap.clear();
        getDamageMap().clear();
        player.getRaidParty().getMembers().forEach(member -> {
            member.moveTo(raidDifficulty.getX(), raidDifficulty.getY(), player.getIndex() * 200);
            member.setInRaid(true);
            player.getRaidParty().getMembers().forEach(this::updateOverlay);
        });
        initWave();
    }

    private void initNpcs() {
        int height = player.getIndex() * 200;
        waves.clear();
        // this can be done in a much nicer way if u upgrade to java 9+
        // since java 9 has List.of()
        List<List<NPC>> easyRaidNpcs = new ArrayList<>();
        List<NPC> firstWaveList = new ArrayList<>();
        firstWaveList.add(new NPC(3224, new Position(3037, 5215, height)));
        easyRaidNpcs.add(firstWaveList);
        List<NPC> secondWaveList = new ArrayList<>();
        secondWaveList.add(new NPC(3225, new Position(3037, 5215, height)));
        easyRaidNpcs.add(secondWaveList); // Done
        List<NPC> thirdWaveList = new ArrayList<>();
        thirdWaveList.add(new NPC(3226, new Position(3037, 5215, height)));
        easyRaidNpcs.add(thirdWaveList); // Done
        List<NPC> fourthWaveList = new ArrayList<>();
        fourthWaveList.add(new NPC(177, new Position(3037, 5215, height)));
        easyRaidNpcs.add(fourthWaveList); // Done
        List<NPC> fifthWaveList = new ArrayList<>();
        fifthWaveList.add(new NPC(527, new Position(3037, 5215, height)));
        easyRaidNpcs.add(fifthWaveList); // Done

        waves.put(RaidDifficulty.EASY, easyRaidNpcs);


        List<List<NPC>> mediumRaidNpcs = new ArrayList<>();

        List<NPC> med1WaveList = new ArrayList<>();
        med1WaveList.add(new NPC(10126, new Position(3037, 5215, height)));

        med1WaveList.add(new NPC(10126, new Position(3037, 5209, height)));
        mediumRaidNpcs.add(med1WaveList);

        List<NPC> med2WaveList = new ArrayList<>();
        med2WaveList.add(new NPC(11751, new Position(3037, 5215, height)));

        med2WaveList.add(new NPC(11751, new Position(3037, 5209, height)));
        mediumRaidNpcs.add(med2WaveList); // Done

        List<NPC> med3WaveList = new ArrayList<>();
        med3WaveList.add(new NPC(9766, new Position(3037, 5215, height)));

        med3WaveList.add(new NPC(9766, new Position(3037, 5209, height)));
        mediumRaidNpcs.add(med3WaveList); // Done

        waves.put(RaidDifficulty.MEDIUM, mediumRaidNpcs);

        //Start of raids
        List<List<NPC>> hardRaidNpcs = new ArrayList<>();

        List<NPC> hard1WaveList = new ArrayList<>();
        hard1WaveList.add(new NPC(422, new Position(3037, 5215, height)));

        hard1WaveList.add(new NPC(422, new Position(3037, 5209, height)));
        hardRaidNpcs.add(hard1WaveList);

        List<NPC> hard2WaveList = new ArrayList<>();
        hard2WaveList.add(new NPC(111, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard2WaveList); // Done

        List<NPC> hard3WaveList = new ArrayList<>();
        hard3WaveList.add(new NPC(2862, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard3WaveList); // Done

        List<NPC> hard4WaveList = new ArrayList<>();
        hard4WaveList.add(new NPC(9912, new Position(3037, 5215, height)));

        hard4WaveList.add(new NPC(9912, new Position(3037, 5209, height)));
        hardRaidNpcs.add(hard4WaveList);

        List<NPC> hard5WaveList = new ArrayList<>();
        hard5WaveList.add(new NPC(6313, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard5WaveList); //9203

        List<NPC> hard6WaveList = new ArrayList<>();
        hard6WaveList.add(new NPC(110, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard6WaveList);

        List<NPC> hard7WaveList = new ArrayList<>();
        hard7WaveList.add(new NPC(171, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard7WaveList);

        List<NPC> hard8WaveList = new ArrayList<>();
        hard8WaveList.add(new NPC(12240, new Position(3037, 5215, height)));
        hardRaidNpcs.add(hard8WaveList);

        List<NPC> hard9WaveList = new ArrayList<>();
        hard9WaveList.add(new NPC(1684, new Position(3037, 5215, height)));
        hard9WaveList.add(new NPC(109, new Position(3037, 5209, height)));
        hardRaidNpcs.add(hard9WaveList);


        waves.put(RaidDifficulty.HARD, hardRaidNpcs);
    }


    private void initWave() {
        RaidDifficulty raidDifficulty = player.getRaidParty().getRaidDifficulty();
        if (currentWave >= waves.get(raidDifficulty).size()) {
            handleFinish(false);
            return;
        }
        initNpcs();
        List<NPC> npcs = waves.get(raidDifficulty).get(currentWave);
        count = 0;
        targetCount = npcs.size();
        if (currentWave == 0) {
            player.getRaidParty()
                    .getMembers()
                    .forEach(member -> {
                        member.sendMessage("Starting first wave in 5 seconds");
                        member.getPacketSender().sendInterfaceRemoval();
                    });
        } else {
            player.getRaidParty()
                    .getMembers()
                    .forEach(member -> member.sendMessage("Wave " + currentWave + " finished, next wave starting in 5 seconds"));
        }
        TaskManager.submit(new Task(10) {
            @Override
            protected void execute() {
                player.getRaidParty()
                        .getMembers()
                        .forEach(member -> {
                            member.getPacketSender().sendInterfaceRemoval();
                            member.getPacketSender().sendWalkableInterface(OVERLAY_ID, true);
                        });
                npcs.forEach(World::register);
                stop();
            }
        });
    }

    public void handleFinish(boolean onOwnerLeave) {
        if (!player.isInRaid() || player.getPosition().getZ() == 0){
            player.sendMessage("Raid has finished");
            player.moveTo(RAID_LOBBY);
            player.setInRaid(false);
            damageMap.remove(waves);
            return;
        }
        damageMap.remove(waves.size());
        //System.out.println("finished: " + onOwnerLeave);
        waves.values()
                .stream()
                .flatMap(List::stream)
                .flatMap(List::stream)
                .forEach(World::deregister);
        player.getRaidParty().getMembers().forEach(member -> {
            currentWave = 0;
            count = 0;
            targetCount = 0;
            member.sendMessage("Raid has finished");
            NpcGain.RaidComplete(member);
            System.out.println("Removed waves???");
            member.moveTo(RAID_LOBBY);
            member.setInRaid(false);
            member.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
            if (!onOwnerLeave) {
                member.getInventory()
                        .add(KEY_ID, highestDamager(member
                                .getUsername()) ? 1 : 1);
                handleReward(member);
            }
        });
        damageMap.clear();
    }

    public boolean highestDamager(String name) {
        if (damageMap.get(name) == null) {
            return false;
        }
        return damageMap.get(name)
                .equals(damageMap.values().stream().max(Comparator.naturalOrder()).orElse(-1L));
    }

    public void handleKill() {
        count++;
        if (count == targetCount) {
            currentWave++;
            initWave();
        }
    }

    public void handleDeath(Player player) {
        if (player.getRaidParty().isOwner(player)) {
            handleFinish(true);
        } else {
            //System.out.println("from " + damageMap + " removing " + player.getUsername());
            damageMap.remove(waves);
            currentWave = 0;
            System.out.println("Removed waves???");
            damageMap.remove(player.getUsername());
            player.setInRaid(false);
            player.getRaidParty().getMembers().forEach(this::updateOverlay);
            player.getRaidParty().deathLeave(player);
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
            player.setRaidParty(null);
            player.moveTo(RAID_LOBBY);
            NpcGain.RaidFailed(player);
        }
    }

    public void handleLogout(Player player) {
        if (player.getRaidParty().isOwner(player)) {
            handleFinish(true);
        } else {
            //System.out.println("from " + damageMap + " removing " + player.getUsername());
            damageMap.remove(player.getUsername());
            player.getRaidParty().getMembers().forEach(this::updateOverlay);
            player.getRaidParty().leave(player);
            player.moveTo(RAID_LOBBY);
            player.setInRaid(false);
            damageMap.remove(waves);
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
        }
    }


    private final int KEY_ID = 13591;

    public void handleReward(Player player) {
        if (!player.getInventory().contains(KEY_ID)) {
            player.sendMessage("You need a raid key to open this chest");
            return;
        }

        RaidDifficulty difficulty = player.getRaidParty().getRaidDifficulty();

        player.getPacketSender().resetItemsOnInterface(27832, 3);
        player.getPacketSender().sendInterface(27830);

        int amount = player.getInventory().getAmount(KEY_ID);


        double playersChanceToWin = difficulty.getRareChance(); //Setting the var 'changeToWinRare' to match the chance quoted in difficulty.
        int randomNumberRolled = Misc.random(0, 100); // A random number between 0 -> 100
        Item first; // Initialiser
        int itemID; // Int to store item ID

        for (int i = 0; i < amount; i++) {

            if (randomNumberRolled >= playersChanceToWin) { // If the player wins..
                first = difficulty.getRareRewards()[random.nextInt(difficulty.getRareRewards().length)];
                World.sendMessageDiscord("<img=12><col=bababa>[<col=0999ad><shad=200>RAIDS<col=bababa>] " +player.getUsername() + " has just received a " + first.getDefinition().getName() + " from raids!");
            } else {
                first = difficulty.getCommonRewards()[random.nextInt(difficulty.getCommonRewards().length)];
            }
            itemID = first.getId();
            player.getPacketSender().sendItemOnInterface(27832, itemID, 0, first.getAmount());
            player.getInventory().delete(KEY_ID, 1);
            player.getInventory().addItemSet(new Item[]{first});
        }
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
}
