package com.zamron.world.content.groupironman;

import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.NameUtils;
import com.zamron.world.World;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerLoading;
import com.zamron.world.entity.impl.player.PlayerSaving;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class GroupIronmanGroup {

    private Player owner;

    public Player getOwner() {
        return owner;
    }

    private List<String> members = new CopyOnWriteArrayList<>();

    public List<String> getMembers() {
        return members;
    }

    private String ownerName;


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        if(owner != null) {
            ownerName = owner.getUsername();
        }
    }

    public GroupIronmanGroup(Player owner) {
        this.owner = owner;
        members.add(owner.getUsername());
        ownerName = owner.getUsername();
        groups.put(ownerName, this);
        save();
    }

    public GroupIronmanGroup(Player owner, List<String> members) {
        this.owner = owner;
        this.members = new CopyOnWriteArrayList<>(members);
        save();
    }

    private static final Path path = Paths.get("./data/saves/groupironman/");

    private static final Map<String, GroupIronmanGroup> groups = new LinkedHashMap<>();

    public static Map<String, GroupIronmanGroup> getGroups() {
        return groups;
    }

    private CircularFifoQueue<String> logs = new CircularFifoQueue<>(100);

    public void setLogs(CircularFifoQueue<String> logs) {
        this.logs = logs;
    }

    private final List<String> pendingInvites = new ArrayList<>();

    public List<String> getPendingInvites() {
        return pendingInvites;
    }

    private Map<Integer, Integer> bank = new LinkedHashMap<>();

    public Map<Integer, Integer> getBank() {
        return bank;
    }

    public void setBank(Map<Integer, Integer> bank) {
        this.bank = bank;
    }

    private int points;
    private int level;
    private long experience;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoint(int count) {
        points += count;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementLevel(int count) {
        level += count;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void addExperience(long count) {
        experience += count;
    }

    public void addItem(Player player, int id, int amount) {
        if (!player.getInventory().contains(id, amount)) {
            amount = player.getInventory().getAmount(id);
        }

        player.getInventory().delete(id, amount);

        if (ItemDefinition.forId(id).isNoted()){
            id = Item.getUnNoted(id);
        }

        if (bank.containsKey(id)) {
            long newAmount = (long) bank.get(id) + (long) amount;
            //System.out.println("New amount = " + newAmount);
            if(newAmount > Integer.MAX_VALUE) {
                return;
            }
            bank.put(id, (int) newAmount);
        } else {
            bank.put(id, amount);
        }
        player.getInventory().refreshItems();
        updateBank();

    }

    public void removeItem(Player player, int id, int amount) {
        if (bank.containsKey(id) && bank.get(id) < amount) {
          amount = bank.get(id);
        }
        if (bank.containsKey(id) && bank.get(id) >= amount) {
            int amountInBank = bank.get(id);
            int invAmount = player.getInventory().getAmount(id);
            long total = (long) amountInBank + (long) invAmount;
            if(total > Integer.MAX_VALUE) {
                return;
            }
            if (bank.get(id) >= amountInBank) {
                int remainder = amountInBank - amount;
                //    //System.out.println("remainder = " + remainder);
                if (remainder == 0) {
                    bank.remove(id);
                } else {
                    bank.put(id, bank.get(id) - amount);
                }
                if (Item.getNoted(id) > 1 && player.withdrawAsNote()){
                    id = Item.getNoted(id);
                }
                player.getInventory().add(id, amount);
                player.getInventory().refreshItems();
            }
            updateBank();
        }
    }

    public void removeItem(Player player, int id) {
        if (bank.containsKey(id)) {
            int amountInBank = bank.get(id);
            int invAmount = player.getInventory().getAmount(id);
            long total = (long) amountInBank + (long) invAmount;
            if(total > Integer.MAX_VALUE) {
                return;
            }
            if (bank.get(id) >= amountInBank) {
                bank.remove(id);
                if (Item.getNoted(id) > 1 && player.withdrawAsNote()){
                    id = Item.getNoted(id);
                }
                player.getInventory().add(id, amountInBank);
                player.getInventory().refreshItems();
            }
            updateBank();
        }
    }

    private static final int BANK_STARTING_POINT = 19307;

    public void openBank(Player player) {
        player.getPacketSender().sendInterface(BANK_STARTING_POINT);
        if (player.withdrawAsNote()) {
            player.getPacketSender().sendToggle(888, 1);
        } else {
            player.getPacketSender().sendToggle(888, 0);
        }
        updateBank();
    }

    private void updateBank() {
        for (String memberName : members) {
            Player member = World.getPlayerByName(memberName);
            if (member == null) {
                continue;
            }

            int index = 0;
            Item[] items = new Item[bank.size()];
            //System.out.println(bank.entrySet());
            for (Map.Entry<Integer, Integer> item : bank.entrySet()) {
                items[index++] = new Item(item.getKey(), item.getValue());
            }
            if (member.getInterfaceId() == BANK_STARTING_POINT) {
                member.getPacketSender().resetItemsOnInterface(BANK_STARTING_POINT + 6, 180);
                member.getPacketSender().sendItemArrayOnInterface(BANK_STARTING_POINT + 6, items);
                member.getPacketSender().sendInterface(BANK_STARTING_POINT);
                member.getPacketSender().sendInterfaceSet(BANK_STARTING_POINT, 3321);
                member.getPacketSender().sendItemContainer(member.getInventory(), 3322);
            }
        }

        save();

    }

    public static void loadGroups() {
        try {
            Files.walk(path).filter(Files::isRegularFile).forEach(file -> {
                try {

                    //System.out.println(path.toAbsolutePath() + " | " + file.toAbsolutePath());
                    List<String> lines = Files.readAllLines(file);
                    List<String> members = new ArrayList<>();
                    Map<Integer, Integer> bank = new LinkedHashMap<>();
                    CircularFifoQueue<String> logs = new CircularFifoQueue<>();
                    int points = 0;
                    int level = 0;
                    long experience = 0;
                    int state = -1;
                    for (String line : lines) {

                        if (line.startsWith("POINTS: ")) {
                            points = Integer.parseInt(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("LEVEL: ")) {
                            level = Integer.parseInt(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("EXPERIENCE: ")) {
                            experience = Long.parseLong(line.split(": ")[1]);
                            continue;
                        }

                        if (line.startsWith("MEMBERS {")) {
                            ////System.out.println("state = 1");
                            state = 1;
                            continue;
                        } else if (line.startsWith("BANK {")) {
                            state = 2;
                            ////System.out.println("State = 2");
                            continue;
                        } else if (line.startsWith("LOGS {")) {
                            ////System.out.println("State = 3");
                            state = 3;
                            continue;
                        }

                        if (line.endsWith("}")) {
                            state = -1;
                            continue;
                        }

                        
                        switch (state) {
                        case 1:
                        	members.add(line);
                        	break;
                        	
                        case 2:
                        	String[] data = line.split(", ");
                            bank.put(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
                        	break;
                            
                        case 3:
                        	logs.add(line);
                        	break;
                        }
                    }

                    String owner = members.get(0);
                    GroupIronmanGroup group = new GroupIronmanGroup(null, members);
                    group.setBank(bank);
                    group.setLogs(logs);
                    group.setPoints(points);
                    group.setLevel(level);
                    group.setExperience(experience);
                    groups.put(owner, group);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLog(String message) {
        logs.add(message);

        owner.getGroupIronman().updateInterface();
        save();
    }

    public CircularFifoQueue<String> getLogs() {
        return logs;
    }

    public void invite(Player other) {
        if (members.size() == 4) {
            owner.sendMessage("Your group already has the maximum amount of members(4)");
            return;
        }
        if (members.contains(other.getUsername())) {
            owner.sendMessage(other.getUsername() + " is already on the group");
            return;
        }

        if (other.getGameMode() != GameMode.GROUP_IRONMAN) {
            owner.sendMessage(other.getUsername() + " isn't a GIM.");
            owner.sendMessage("Try inviting a group ironman player to your group.");
            return;
        }

        if (other.getGroupIronmanGroup() != null) {
            owner.sendMessage(other.getUsername() + " is already in a group");
            return;
        }
        owner.sendMessage("Invitation sent to " + other.getUsername());

        other.setGroupIronmanGroupInvitation(this);
        pendingInvites.add(other.getUsername());
        other.sendMessage(owner.getUsername() + " invited you to their group ironman group");
        other.sendMessage("Type ::accept to accept the invitation");

        owner.getGroupIronman().updateInterface(this);

    }

    public void handleInvitation(Player other, boolean accepted) {
        other.setGroupIronmanGroupInvitation(null);
        if (accepted) {
            other.setGroupIronmanGroup(this);
            other.setGroupOwnerName(owner.getUsername());
        }

        members.add(other.getUsername());
        pendingInvites.remove(other.getUsername());

        other.sendMessage("Group invitation accepted");
        addLog(other.getUsername() + " has joined the group");
        owner.getGroupIronman().updateInterface(this);
        other.save();
        save();

    }

    public void kick(String username) {
        if (!members.contains(username)) {
            owner.sendMessage(username + " is not on your group.");
            owner.sendMessage("Make sure you're using case sensitive characters for the usernames.");
            return;
        }

        Player kickedPlayer = World.getPlayerByName(username);
        if (kickedPlayer != null) {
            kickedPlayer.setGroupOwnerName("");
            kickedPlayer.setGroupIronmanGroup(null);
            kickedPlayer.sendMessage("You have been kicked from your ironman group");
        } else {
            Player player = new Player(null);

            player.setUsername(username);
            player.setLongUsername(NameUtils.stringToLong(username));

            PlayerLoading.getResult(player, true);

            player.setGroupOwnerName("");

            PlayerSaving.save(player);
        }

        members.remove(username);

        owner.getGroupIronman().updateInterface(this);

        save();
    }


    public void save() {
        try {
            if (ownerName == null) {
                return;
            }
            Path path = Paths.get(GroupIronmanGroup.path + "/" + ownerName + ".txt");
            List<String> result = new ArrayList<>();
            result.add("MEMBERS {");
            result.addAll(members);
            result.add("}");
            result.add("BANK {");
            bank.forEach((id, amount) -> {
                result.add(id + ", " + amount);
            });
            result.add("}");
            result.add("LOGS {");
            result.addAll(logs);
            result.add("}");
            result.add("POINTS: " + points);
            result.add("LEVEL: " + level);
            result.add("EXPERIENCE: " + experience);
            Files.write(path, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
