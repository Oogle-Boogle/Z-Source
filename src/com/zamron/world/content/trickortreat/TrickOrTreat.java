package com.zamron.world.content.trickortreat;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.model.Flag;
import com.zamron.model.GameObject;
import com.zamron.model.Item;
import com.zamron.model.Skill;
import com.zamron.model.container.impl.Inventory;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.util.Misc;
import com.zamron.world.World;
import com.zamron.world.content.CustomObjects;
import com.zamron.world.content.dialogue.Dialogue;
import com.zamron.world.content.dialogue.DialogueExpression;
import com.zamron.world.content.dialogue.DialogueManager;
import com.zamron.world.content.dialogue.DialogueType;
import com.zamron.world.content.discord.DiscordMessenger;
import com.zamron.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public class TrickOrTreat {

    //Todo add calendar check
    public static TrickOrTreatData.LocationData currentLocation = null;

    public static void pickNextLocation() {
        World.getPlayers().forEach(p -> p.setKnockedDoor(false));
        World.getPlayers().forEach(p -> p.getPacketSender().sendEntityHintRemoval(true));
        if (currentLocation != null) {
            GameObject door = new GameObject(currentLocation.doorID, currentLocation.doorPos, 0, currentLocation.doorDirection);
            World.deregister(currentLocation.npc);
            CustomObjects.deleteGlobalObject(door);
        }
        //currentLocation = Misc.randomEnum(TrickOrTreatData.LocationData.class);
        currentLocation = TrickOrTreatData.LocationData.FALADOR;
        GameObject door = new GameObject(currentLocation.doorID, currentLocation.doorPos, 0, currentLocation.doorDirection);
        CustomObjects.spawnGlobalObject(door);
        World.register(currentLocation.npc);
        World.sendMessageNonDiscord("@blu@[Trick Or Treat] "
                +currentLocation.npc.getDefinition().getName()
                +" has spawned! @red@Clue: @blu@" + currentLocation.clue + ". @red@::tot");
        DiscordMessenger.sendInGameMessage("[Trick Or Treat] "
                + currentLocation.npc.getDefinition().getName()
                + " just spawned! Clue: " + currentLocation.clue + ". ::tot");
    }

    public static void knockDoor(Player player, int doorID) {
        if (player.isKnockedDoor()) {
            player.sendMessage("You can't knock again! Wait until the next global message!");
            return;
        }
        if (player.getPosition().getDistance(currentLocation.doorPos) <= 1 && doorID == currentLocation.doorID) {
            player.setKnockedDoor(true);
            player.forceChat("Trick Or Treat!");
            KnockResponse response = Misc.randomEnum(KnockResponse.class);
            currentLocation.npc.forceChat(response.response);

            Dialogue dialogue = new Dialogue() {
                @Override
                public DialogueType type() {
                    return DialogueType.NPC_STATEMENT;
                }

                @Override
                public DialogueExpression animation() {
                    return response.expression;
                }

                @Override
                public String[] dialogue() {
                    return new String[]{response.response};
                }

                @Override
                public int npcId() {
                    return currentLocation.npc.getId();
                }
            };
            DialogueManager.start(player, dialogue);
            if (response.treat) {
                currentLocation.npc.getMovementQueue().addStep(currentLocation.walkToDoor);
                currentLocation.npc.setPositionToFace(currentLocation.doorPos);
                treat(player);
            } else {
                trick(player);
            }
        }
    }

    public static void treat(Player player) {

        Inventory invent = player.getInventory();

        Item sweets = new Item(TrickOrTreatData.sweets, Misc.random(100));

        if (invent.getFreeSlots() >= 1 || invent.contains(TrickOrTreatData.sweets)) {
            invent.add(sweets);
            player.getPacketSender().sendMessage("You've received " + sweets.getAmount() + " Sweets!");
        } else {
            player.getBank(player.getCurrentBankTab()).add(sweets);
            player.getPacketSender().sendMessage(sweets.getAmount() + " were sent to your bank!");
        }

        int random = Misc.random(5000);

        Item treat = null;

        if (random <= 4000) {
            treat = shitTreat();
        }

        if (random <= 600) {
            treat = mediumTreat();
        }

        if (random <= 50) {
            treat = rareTreat();
            World.sendMessageDiscord("[Trick Or Treat Rare] " + player.getUsername() + " received a " + treat.getDefinition().getName());
        }

        if (random <= 1) {
            treat = superRareTreat();
            World.sendMessageDiscord("[Trick Or Treat Super Rare] " + player.getUsername() + " received a " + treat.getDefinition().getName());
        }

        if (treat != null) {

            if (invent.getFreeSlots() >= 1 || (invent.contains(treat.getId()) && treat.getDefinition().isStackable())) {
                player.getPacketSender().sendMessage("You've received a " + treat.getDefinition().getName() + "!");
                invent.add(treat);
            } else {
                player.getPacketSender().sendMessage("A " + treat.getDefinition().getName() + " was sent to your bank!");
                player.getBank(player.getCurrentBankTab()).add(treat);
            }
        }
    }

    public static Item shitTreat() {
        return Misc.randomItem(TrickOrTreatData.shitTreats);
    }

    public static Item mediumTreat() {
        return Misc.randomItem(TrickOrTreatData.mediumTreats);
    }

    public static Item rareTreat() {
        return Misc.randomItem(TrickOrTreatData.rareTreats);
    }

    public static Item superRareTreat() {
        return Misc.randomItem(TrickOrTreatData.superRareTreats);
    }

    public static void trick(Player player) {

        Item trick = Misc.randomItem(TrickOrTreatData.tricks);

        Inventory invent = player.getInventory();
        
        int random = Misc.random(3);
        
        switch (random) {
            case 0:
                transformPlayer(player);
                break;
            case 1:
                if (invent.getFreeSlots() >= 1)
                    invent.add(trick);
                currentLocation.npc.forceChat("Enjoy your " + trick.getDefinition().getName() + " loser!");
                break;
            case 2:
                for (Skill skill : Skill.values) {
                    player.getSkillManager().setCurrentLevel(skill, 1);
                }
                currentLocation.npc.forceChat("Haha! Enjoy being a noob again!");
                player.sendMessage("Uh oh! Your stats were set to 1! They'll take a while to regenerate!");
                break;
            default:
                if (invent.getFreeSlots() >= 1)
                    invent.add(trick);
                currentLocation.npc.forceChat("Enjoy your " + trick.getDefinition().getName() + " loser!");
                break;
        }
    }

    public static void transformPlayer(Player player) {

        List<Integer> npcs = new ArrayList<>();

        for (int npc : TrickOrTreatData.transformableNpcIDs) {
            npcs.add(npc);
        }

        int chosenNPC = Misc.randomElement(npcs);

        player.setNpcTransformationId(chosenNPC);
        player.getUpdateFlag().flag(Flag.APPEARANCE);

        String npcName = NpcDefinition.forId(chosenNPC).getName();

        currentLocation.npc.forceChat("Have fun as a " + npcName + "!");
        player.sendMessage("@red@You've been transformed into: " + npcName);


        TaskManager.submit(new Task(Misc.random(150, 400), false) {

            @Override
            public void execute() {
                player.setNpcTransformationId(-1);
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        });
    }


    public void handleTimer() {

        if (System.currentTimeMillis() > TrickOrTreatData.timeUntilSpawn) {
            pickNextLocation();
            TrickOrTreatData.resetTimer();
        }
    }


}
