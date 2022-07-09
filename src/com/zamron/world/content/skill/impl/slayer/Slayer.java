package com.zamron.world.content.skill.impl.slayer;

import com.zamron.model.Item;
import com.zamron.model.Locations;
import com.zamron.model.Position;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Shop;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Misc;
import com.zamron.util.RandomUtility;
import com.zamron.world.World;
import com.zamron.world.content.Emotes.Skillcape_Data;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.event.SpecialEvents;
import com.zamron.world.content.serverperks.GlobalPerks;
import com.zamron.world.content.transportation.TeleportHandler;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;

public class Slayer {

    private Player player;

    public Slayer(Player p) {
        this.player = p;
    }

    private SlayerTasks slayerTask = SlayerTasks.NO_TASK, lastTask = SlayerTasks.NO_TASK;
    private SlayerMaster slayerMaster = SlayerMaster.VANNAKA;
    private int amountToSlay, taskStreak;
    public static String duoPartner, duoInvitation;

    public void assignTask() {
        boolean hasTask = getSlayerTask() != SlayerTasks.NO_TASK && player.getSlayer()
                .getLastTask() != getSlayerTask();
        boolean duoSlayer = duoPartner != null;
        if (hasTask) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        int[] taskData = SlayerTasks.getNewTaskData(slayerMaster, player);
        int slayerTaskId = taskData[0], slayerTaskAmount = taskData[1];
        SlayerTasks taskToSet = SlayerTasks.forId(slayerTaskId);
        if (taskToSet == player.getSlayer()
                .getLastTask() || NpcDefinition.forId(taskToSet.getNpcId())
                .getSlayerLevel() > player.getSkillManager().getMaxLevel(Skill.SLAYER)) {
            assignTask();
            return;
        }
        player.getPacketSender().sendInterfaceRemoval();
        this.amountToSlay = slayerTaskAmount;
        this.slayerTask = taskToSet;
        DialogueManager.start(player, SlayerDialogues.receivedTask(player, getSlayerMaster(), getSlayerTask()));
        PlayerPanel.refreshPanel(player);
        if (duoSlayer) {
            Player duo = World.getPlayerByName(duoPartner);
            duo.getSlayer().setSlayerTask(taskToSet);
            duo.getSlayer().setAmountToSlay(slayerTaskAmount);
            duo.getPacketSender().sendInterfaceRemoval();
            DialogueManager.start(duo, SlayerDialogues.receivedTask(duo, slayerMaster, taskToSet));
            PlayerPanel.refreshPanel(duo);
        }
    }

    public void resetSlayerTask() {
        SlayerTasks task = getSlayerTask();
        if (task == SlayerTasks.NO_TASK)
            return;
        this.slayerTask = SlayerTasks.NO_TASK;
        this.amountToSlay = 0;
        this.taskStreak = 0;
        player.getPointsHandler()
                .setSlayerPoints(player.getPointsHandler().getSlayerPoints() - 10, false);
        PlayerPanel.refreshPanel(player);
        Player duo = duoPartner == null ? null : World.getPlayerByName(duoPartner);
        if (duo != null) {
            duo.getSlayer().setSlayerTask(SlayerTasks.NO_TASK).setAmountToSlay(0).setTaskStreak(0);
            duo.getPacketSender()
                    .sendMessage("Your partner exchanged 10 Slayer points to reset your team's Slayer task.");
            PlayerPanel.refreshPanel(duo);
            player.getPacketSender()
                    .sendMessage("You've successfully reset your team's Slayer task.");
        } else {
            player.getPacketSender().sendMessage("Your Slayer task has been reset.");
        }
    }

    public void killedNpc(NPC npc) {
        if (slayerTask != SlayerTasks.NO_TASK) {
            if (slayerTask.getNpcId() == npc.getId()) {
                handleSlayerTaskDeath(true);
                if (duoPartner != null) {
                    Player duo = World.getPlayerByName(duoPartner);
                    if (duo != null) {
                        if (checkDuoSlayer(player, false)) {
                            duo.getSlayer()
                                    .handleSlayerTaskDeath(Locations.goodDistance(player.getPosition(), duo
                                            .getPosition(), 20));
                        } else {
                            resetDuo(player, duo);
                        }
                    }
                }

            }
        }
    }

    public void handleSlayerTaskDeath(boolean giveXp) {
        int xp = slayerTask.getXP() + Misc.getRandom(slayerTask.getXP() / 5);
        if (amountToSlay > 1) {
            amountToSlay--;
        } else {
            player.getPacketSender()
                    .sendMessage("")
                    .sendMessage("You've completed your Slayer task! Return to a Slayer master for another one.");
            taskStreak++;
            //Achievements.finishAchievement(player, AchievementData.COMPLETE_A_SLAYER_TASK);
            if (slayerTask.getTaskMaster() == SlayerMaster.KURADEL) {
                //Achievements.finishAchievement(player, AchievementData.COMPLETE_A_HARD_SLAYER_TASK);
            } else if (slayerTask.getTaskMaster() == SlayerMaster.SUMONA) {
                //Achievements.finishAchievement(player, AchievementData.COMPLETE_AN_ELITE_SLAYER_TASK);
            }
            lastTask = slayerTask;
            slayerTask = SlayerTasks.NO_TASK;
            amountToSlay = 0;
            givePoints(slayerMaster);
        }

        if (giveXp) {
            player.getSkillManager().addExperience(Skill.SLAYER, doubleSlayerXP ? xp * 2 : xp);
        }

        PlayerPanel.refreshPanel(player);
    }
    @SuppressWarnings("incomplete-switch")
    public void givePoints(SlayerMaster master) {
        int pointsReceived = 4;
        switch (master) {
            case VANNAKA:
                pointsReceived += 2 + Misc.random(1);
                break;
            case DURADEL:
                pointsReceived += 4 + Misc.random(1);
                break;
            case KURADEL:
                pointsReceived += 6 + Misc.random(2);
                break;
            case SUMONA:
                pointsReceived += 8 + Misc.random(3);
                break;
            case BRAVEK:
                if (player.getBravekDifficulty() == null) {
                    pointsReceived += 100;
                } else {
                    switch (player.getBravekDifficulty()) {

                        case "easy":
                            pointsReceived = 5 + Misc.random(2);
                            break;
                        case "medium":
                            pointsReceived = 7 + Misc.random(5);
                            break;
                        case "hard":
                            pointsReceived = 15 + Misc.random(10);
                            break;
                    }
                }
                player.incrementBravekTasksCompleted(1);
                break;
        }
        if (Skillcape_Data.SLAYER.isWearingCape(player)) {
            pointsReceived += pointsReceived + 2;
        }

        if (player.getEquipment().contains(3949) || player.getEquipment().contains(936)) {
            pointsReceived *= 2;
            player.getPacketSender().sendMessage("You received double points for wearing slayer helm!");
        }

        if (GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.x2_SLAYER_POINTS) {
            pointsReceived *= 2;
        }

        int per5 = pointsReceived * 2;
        int per10 = pointsReceived * 3;
        //int per50 = pointsReceived * 5;
        double custom1 = pointsReceived * 1;
        double custom2 = pointsReceived * 2;
        double custom3 = pointsReceived * 3;


        //if (player.getEquipment().containsAny(5127, 5167)) {
        //pointsReceived = pointsReceived * 2;
        //}

        if (player.getBravekTasksCompleted() >= 100 && player.getBravekTasksCompleted() <= 249) {
            player.getPointsHandler().setSlayerPointsDouble(custom1, true);
        }
        if (player.getBravekTasksCompleted() >= 250 && player.getBravekTasksCompleted() <= 499) {
            player.getPointsHandler().setSlayerPointsDouble(custom2, true);
        }
        if (player.getBravekTasksCompleted() >= 500) {
            player.getPointsHandler().setSlayerPointsDouble(custom3, true);
        }

        int random = RandomUtility.exclusiveRandom(250);
        if (random == 250) {
            player.getInventory().add(19101, 1);
            player.sendMessage("Congratulations! A @red@Custom Slayer Helm@blu@Has been added to ur inventory!");
            World.sendMessageNonDiscord("<img=12>@blu@[SLAYER]<img=12>@red@ " + player.getUsername()
                    .toString() + " @blu@Has received a Custom Slayer Helmet");
        }

        if (SpecialEvents.getDay() == SpecialEvents.WEDNESDAY) {
            pointsReceived *= 2;
        }

        switch (player.getRights()) {
            case LEGENDARY_DONATOR:
            case UBER_DONATOR:
            case DELUXE_DONATOR:
                pointsReceived *= 2;
                break;
            case VIP_DONATOR:
                pointsReceived *= 3;
                break;
        }

        switch (player.getSecondaryPlayerRights()) {
            case LEGENDARY_DONATOR:
            case UBER_DONATOR:
            case DELUXE_DONATOR:
                pointsReceived *= 2;
                break;
        }
        if (player.getSlayer().getTaskStreak() == 5) {
            player.getPointsHandler().setSlayerPoints(per5, true);
            player.getPacketSender().sendMessage("You received " + per5 + " Slayer points.");
        } else if (player.getSlayer().getTaskStreak() == 10) {
            player.getPointsHandler().setSlayerPoints(per10, true);
            player.getPacketSender().sendMessage("You received " + per10 + " Slayer points.");
            player.getSlayer().setTaskStreak(0);
        } else if (player.getSlayer().getTaskStreak() >= 0 && player.getSlayer()
                .getTaskStreak() < 5 || player.getSlayer()
                .getTaskStreak() >= 6 && player.getSlayer().getTaskStreak() < 10) {
            player.getPointsHandler().setSlayerPoints(pointsReceived, true);
            player.getPacketSender()
                    .sendMessage("You received " + pointsReceived + " Slayer points.");
        }
        player.getPointsHandler().refreshPanel();
    }

    public boolean assignDuoSlayerTask() {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
            player.getPacketSender().sendMessage("You already have a Slayer task.");
            return false;
        }
        Player partner = World.getPlayerByName(duoPartner);
        if (partner == null) {
            player.getPacketSender().sendMessage("");
            player.getPacketSender()
                    .sendMessage("You can only get a new Slayer task when your duo partner is online.");
            return false;
        }
        if (partner.getSlayer().getDuoPartner() == null || !partner.getSlayer()
                .getDuoPartner()
                .equals(player.getUsername())) {
            resetDuo(player, null);
            return false;
        }
        if (partner.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
            player.getPacketSender().sendMessage("Your partner already has a Slayer task.");
            return false;
        }
        if (partner.getSlayer().getSlayerMaster() != player.getSlayer().getSlayerMaster()) {
            player.getPacketSender()
                    .sendMessage("You and your partner need to have the same Slayer master.");
            return false;
        }
        if (partner.getInterfaceId() > 0) {
            player.getPacketSender()
                    .sendMessage("Your partner must close all their open interfaces.");
            return false;
        }
        return true;
    }

    public static boolean checkDuoSlayer(Player p, boolean login) {
        if (p.getSlayer().getDuoPartner() == null) {
            return false;
        }
        Player partner = World.getPlayerByName(p.getSlayer().getDuoPartner());
        if (partner == null) {
            return false;
        }
        if (partner.getSlayer().getDuoPartner() == null || !partner.getSlayer()
                .getDuoPartner()
                .equals(p.getUsername())) {
            resetDuo(p, null);
            return false;
        }
        if (partner.getSlayer().getSlayerMaster() != p.getSlayer().getSlayerMaster()) {
            resetDuo(p, partner);
            return false;
        }
        if (login) {
            p.getSlayer().setSlayerTask(partner.getSlayer().getSlayerTask());
            p.getSlayer().setAmountToSlay(partner.getSlayer().getAmountToSlay());
        }
        return true;
    }

    public static void resetDuo(Player player, Player partner) {
        if (partner != null) {
            if (partner.getSlayer().getDuoPartner() != null && partner.getSlayer()
                    .getDuoPartner()
                    .equals(player.getUsername())) {
                partner.getSlayer().setDuoPartner(null);
                partner.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
                PlayerPanel.refreshPanel(partner);
            }
        }
        player.getSlayer().setDuoPartner(null);
        player.getPacketSender().sendMessage("Your Slayer duo team has been disbanded.");
        PlayerPanel.refreshPanel(player);
    }

    public void handleInvitation(boolean accept) {
        if (duoInvitation != null) {
            Player inviteOwner = World.getPlayerByName(duoInvitation);
            if (inviteOwner != null) {
                if (accept) {
                    if (duoPartner != null) {
                        player.getPacketSender()
                                .sendMessage("You already have a Slayer duo partner.");
                        inviteOwner.getPacketSender()
                                .sendMessage("" + player.getUsername() + " already has a Slayer duo partner.");
                        return;
                    }
                    inviteOwner.getPacketSender()
                            .sendMessage("" + player.getUsername() + " has joined your duo Slayer team.")
                            .sendMessage("Seek respective Slayer master for a task.");
                    inviteOwner.getSlayer().setDuoPartner(player.getUsername());
                    PlayerPanel.refreshPanel(inviteOwner);
                    player.getPacketSender()
                            .sendMessage("You have joined " + inviteOwner.getUsername() + "'s duo Slayer team.");
                    player.getSlayer().setDuoPartner(inviteOwner.getUsername());
                    PlayerPanel.refreshPanel(player);
                } else {
                    player.getPacketSender().sendMessage("You've declined the invitation.");
                    inviteOwner.getPacketSender()
                            .sendMessage("" + player.getUsername() + " has declined your invitation.");
                }
            } else
                player.getPacketSender().sendMessage("Failed to handle the invitation.");
        }
    }

    public void handleSlayerRingTP(int itemId) {
        if (!player.getClickDelay().elapsed(4500))
            return;
        if (player.getMovementQueue().isLockMovement())
            return;
        SlayerTasks task = getSlayerTask();
        if (task == SlayerTasks.NO_TASK)
            return;
        Position slayerTaskPos = new Position(task.getTaskPosition().getX(), task.getTaskPosition()
                .getY(), task.getTaskPosition().getZ());
        if (!TeleportHandler.checkReqs(player, slayerTaskPos))
            return;
        TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook()
                .getTeleportType());
        Item slayerRing = new Item(itemId);
        player.getInventory().delete(slayerRing);
        if (slayerRing.getId() < 13288)
            player.getInventory().add(slayerRing.getId() + 1, 1);
        else
            player.getPacketSender().sendMessage("Your Ring of Slaying crumbles to dust.");
    }

    public int getAmountToSlay() {
        return this.amountToSlay;
    }

    public Slayer setAmountToSlay(int amountToSlay) {
        this.amountToSlay = amountToSlay;
        return this;
    }

    public int getTaskStreak() {
        return this.taskStreak;
    }

    public Slayer setTaskStreak(int taskStreak) {
        this.taskStreak = taskStreak;
        return this;
    }

    public SlayerTasks getLastTask() {
        return this.lastTask;
    }

    public void setLastTask(SlayerTasks lastTask) {
        this.lastTask = lastTask;
    }

    public boolean doubleSlayerXP = false;

    public Slayer setDuoPartner(String duoPartner) {
        Slayer.duoPartner = duoPartner;
        return this;
    }

    public String getDuoPartner() {
        return duoPartner;
    }

    public SlayerTasks getSlayerTask() {
        return slayerTask;
    }

    public Slayer setSlayerTask(SlayerTasks slayerTask) {
        this.slayerTask = slayerTask;
        return this;
    }

    public SlayerMaster getSlayerMaster() {
        return slayerMaster;
    }

    public void setSlayerMaster(SlayerMaster master) {
        this.slayerMaster = master;
    }

    public void setDuoInvitation(String player) {
        Slayer.duoInvitation = player;
    }

    public static boolean handleRewardsInterface(Player player, int button) {
        if (player.getInterfaceId() == 36000) {
            switch (button) {
                case -29534:
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case -29522:
                    if (player.getPointsHandler().getSlayerPoints() < 10) {
                        player.getPacketSender().sendMessage("You do not have 10 Slayer points.");
                        return true;
                    }
                    player.getPointsHandler().refreshPanel();
                    player.getPointsHandler().setSlayerPoints(-10, true);
                    player.getSkillManager().addExperience(Skill.SLAYER, 10000);
                    player.getPacketSender()
                            .sendMessage("You've bought 10000 Slayer XP for 10 Slayer points.");
                    break;
                case -29519:
                    if (player.getPointsHandler().getSlayerPoints() < 300) {
                        player.getPacketSender().sendMessage("You do not have 300 Slayer points.");
                        return true;
                    }
                    if (player.getSlayer().doubleSlayerXP) {
                        player.getPacketSender().sendMessage("You already have this buff.");
                        return true;
                    }
                    player.getPointsHandler().setSlayerPoints(-300, true);
                    player.getSlayer().doubleSlayerXP = true;
                    player.getPointsHandler().refreshPanel();
                    player.getPacketSender()
                            .sendMessage("You will now permanently receive double Slayer experience.");
                    break;
                case -29531:
                    Shop.ShopManager.getShops().get(47).open(player);
                	//player.sendMessage("Slayer shop is currently being reworked");
                    break;
            }
            player.getPacketSender()
                    .sendString(36030, "Current Points:   " + player.getPointsHandler()
                            .getSlayerPoints());
            return true;
        }
        return false;
    }
}
