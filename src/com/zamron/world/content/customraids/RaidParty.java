package com.zamron.world.content.customraids;

import com.zamron.model.Position;
import com.zamron.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public class RaidParty {

    private final Player owner;

    private final List<Player> members;

    public RaidParty(Player owner) {
        this.owner = owner;
        members = new ArrayList<>();
        members.add(owner);
    }

    private final int STARTING_POINT = 27680;

    private RaidDifficulty raidDifficulty = RaidDifficulty.EASY;

    public RaidDifficulty getRaidDifficulty() {
        return raidDifficulty;
    }

    public void setRaidDifficulty(RaidDifficulty raidDifficulty) {
        this.raidDifficulty = raidDifficulty;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isOwner(Player player) {
        return player.getUsername().equalsIgnoreCase(owner.getUsername());
    }

    public List<Player> getMembers() {
        return members;
    }

    private final Position RAID_LOBBY = new Position(3037, 5233, 0);

    public void displayMembers(Player player) {
        members.forEach(member -> {
            player.sendMessage("Member name: " + member.getUsername());
        });
    }

    public void join(Player player) {
        if (player.getRaidParty() != null) {
            player.sendMessage("You are already in a raid party");
            return;
        }
        if (player.getUsername().equalsIgnoreCase(owner.getUsername())) {
            player.sendMessage("You can't join your own party...");
            return;
        }

        //System.out.println(owner.getBannedRaidMembers() + " | " + player.getUsername());
        if (owner.getBannedRaidMembers().contains(player.getUsername().toLowerCase())) {
            player.sendMessage("You are banned from " + owner.getUsername() + "'s raid party.");
            return;
        }
        if (owner.isInRaid()) {
            player.sendMessage("You can't join this raids party while " +owner.getUsername() + " is raiding.");
            return;
        }
        player.setRaidParty(this);
        members.add(player);
        owner.sendMessage(player.getUsername() + " has joined your raid party");
        player.sendMessage("You have joined " + owner.getUsername() + "'s raid party");
        members.forEach(this::updateInterface);
    }

    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public void removeParty() {
        members.forEach(member -> {
            if (member.getRaidParty().isOwner(member)) {
                member.sendMessage("You have deleted your raid party");
            } else {
                member.sendMessage(owner.getUsername() + " has deleted the raid party");
            }
            member.setRaidParty(null);
            clearNames(member);
            //System.out.println("Member = " + member.getUsername());
        });
        members.clear();
    }

    public void delete(Player player) {
        if (!isOwner(player)) {
            return;
        }
        members.forEach(member -> {
            if (member.getRaidParty().isOwner(member)) {
                member.sendMessage("You have deleted your raid party");
            } else {
                member.sendMessage(owner.getUsername() + " has deleted the raid party");
            }
            member.setRaidParty(null);
            clearNames(member);
            //System.out.println("Member = " + member.getUsername());
        });
        members.clear();
    }

    public void leave(Player player) {
        if(isOwner(player)) {
            //System.out.println("Was owner");
            delete(player);
            return;
        }
        owner.sendMessage(player.getUsername() + " has left your raid party");
        player.sendMessage("You have left " + owner.getUsername() + "'s raid party");
        player.setRaidParty(null);
        members.remove(player);
        clearNames(player);
        members.forEach(this::updateInterface);
    }

    public void deathLeave(Player player) {
        owner.sendMessage(player.getUsername() + " has died and left your party.");
        player.sendMessage("You died and automatically left " + owner.getUsername() + "'s raid party.");
        player.setRaidParty(null);
        members.remove(player);
        clearNames(player);
        members.forEach(this::updateInterface);
        player.moveTo(RAID_LOBBY);
        delete(player);
    }

    public void updateInterface(Player player) {
        clearNames(player);
        for (int i = 0; i < members.size(); i++) {
            Player member = members.get(i);
            player.getPacketSender().sendString(STARTING_POINT + 32 + i, member.getUsername());
        }
    }

    private void clearNames(Player player) {
        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(STARTING_POINT + 32 + i, "");
        }
    }
}
