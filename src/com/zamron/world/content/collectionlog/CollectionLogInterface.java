package com.zamron.world.content.collectionlog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.model.definitions.NPCDrops.DropChance;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.npc.NPC;
import com.zamron.world.entity.impl.player.Player;
import lombok.var;


public class CollectionLogInterface {
    private List<Integer> currentlyViewing = new ArrayList<>();

    public CollectionLogInterface(Player player) {
        this.player = player;
    }

    public void open() {
        initialiseCurrentlyViewing();
        sendBossNames();
        sendNpcData(0);
        player.getPA().sendInterface(30360);
    }

    private void sendBossNames() {
        int[] startingLine = new int[]{30560};
        currentlyViewing.forEach(entry -> {
            player.getPA().sendString(startingLine[0]++, "" + NpcDefinition.forId(entry).getName());
        });
    }

    private void initialiseCurrentlyViewing() {
        currentlyViewing.clear();
        for (int entry : NPC_LIST) {
            currentlyViewing.add(entry);
        }
    }

    public void search(String name) {
        initialiseCurrentlyViewing();
        var tempList = new ArrayList<Integer>();
        for (int data : currentlyViewing) {
            if (Objects.nonNull(NpcDefinition.forId(data))) {
                if (!NpcDefinition.forId(data).getName().toLowerCase().contains(name.toLowerCase()))
                    tempList.add(data);
            } else {
                tempList.add(data);
            }
        }
        currentlyViewing.removeAll(tempList);

        for (int i = 0; i < 100; i++) {
            player.getPacketSender().sendString(30560 + i, "");
        }
        sendBossNames();
    }

    private boolean hasObtainedItem(int npc, int item) {
        return player.getCollectionLogData()
                .stream()
                .filter(data -> data.getNpcId() == npc && data.getItem() == item)
                .findFirst()
                .isPresent();
    }

    private void sendNpcData(int index) {
        var definition = NpcDefinition.forId(currentlyViewing.get(index));
        player.getPacketSender().sendNpcOnInterface(30367, definition.getId());

        player.getPacketSender().resetItemsOnInterface(30375, 20);
        player.getPacketSender().sendString(30369, "Killcount: " +
                Misc.insertCommasToNumber(String.valueOf(player.getNpcKillCount(definition
                        .getId()))));


        var drops = NPCDrops.forId(definition.getId());
        var slot = 0;
        for (var npcDrop : drops.getDropList()) { //smaller and equal too means very common, and always,
            if (npcDrop.getChance().ordinal() < DropChance.LEGENDARY.ordinal()) {
                continue; //now this is like this bc there is no drops, 1 sec
            }
            if (hasObtainedItem(definition.getId(), npcDrop.getId())) {
                var item = player.getCollectionLogData()
                        .stream()
                        .filter(data -> data.getNpcId() == definition.getId() && data.getItem() == npcDrop
                                .getId())
                        .findFirst()
                        .get();
                player.getPacketSender()
                        .sendItemOnInterface(30375, item.getItem(), slot++, item.getAmount());
            } else {
                player.getPacketSender().sendItemOnInterface(30375, npcDrop.getId(), slot++, 0);
            }
        }
    }

    public boolean handleButton(int buttonId) {
        if (!(buttonId >= 30560 && buttonId <= 30760)) {
            return false;
        }
        int index = -30560 + buttonId;
        if (currentlyViewing.size() > index) {
            sendNpcData(index);
        }
        return true;
    }

    private final int[] NPC_LIST = new int[]{17, 422, 3263, 15, 224, 1999, 9932, 9994, 9993, 16, 9277, 9273, 9903, 8133,
            8493, 9247, 172, 9203, 9935, 169, 170, 219, 12239, 3154, 5957, 1684, 185, 6311, 5959, 5958,};
    private final Player player;
}