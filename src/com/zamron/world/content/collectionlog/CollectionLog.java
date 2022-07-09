package com.zamron.world.content.collectionlog;

import com.zamron.model.definitions.NPCDrops;
import com.zamron.model.definitions.NPCDrops.DropChance;
import com.zamron.model.definitions.NPCDrops.NpcDropItem;
import com.zamron.model.definitions.NpcDefinition;
import com.zamron.world.entity.impl.player.Player;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author Suic
 * @Since 18.03.2020
 */
@RequiredArgsConstructor
public class CollectionLog {
	private final Player player;
    
    /*
     * Contains NPC Id's that should work with the collection log You can change these as
     */		private List<NpcDefinition> npcs = new ArrayList<>();
    		private final int[] NPC_LIST = new int[] {17, 422, 3263, 15, 224, 1999, 9932, 9994, 9993, 16, 9277, 9273, 9903, 8133,
                    8493, 9247, 172, 9203, 9935, 169, 170, 219, 12239, 3154, 5957, 1684, 185, 6311, 5959, 5958};
    		private int textStart = 30560;
    
    /*
     * Handles first opening the interface
     */
    public void open(Player player) {
        player.getPacketSender().sendInterface(30360);
        initNpcList();
        textStart = 30560; 
        npcs.forEach(npc -> {
            player.getPacketSender().sendString(textStart++, "" +npc.getName());
        });
        
        sendNpcData(player, 0);
        
    }
    
    
    /*
     * Sends all collection log info too the interface
     */
    private void sendNpcData(Player player, int index) {
        NpcDefinition npc = npcs.get(index);
        int id = npc.getId();
        player.getPacketSender().sendNpcOnInterface(30367,id);
        player.getPacketSender().sendNpcOnInterface(id,30367);
        player.getPacketSender().resetItemsOnInterface(30375, 20);
        player.getPacketSender().sendString(30369, "Killcount: " + player.getNPCKILLCount());
       NPCDrops drops = NPCDrops.forId(id);
        int slot = 0;
        List<Integer> items = new ArrayList<>();
        for (NpcDropItem drop : drops.getDropList()) {
            int item = drop.getId();
            if (items.contains(item)) {
                continue;
            }

            if (drop.getChance().ordinal() > DropChance.RARE.ordinal()) { 
                player.getPacketSender().sendItemOnInterface(30375, item, slot++, getCollectedAmount(player, id, item));
                items.add(item);
            }
        }
        if (!items.isEmpty()) {
            player.getPacketSender().sendCollectedItems(getCollectedItems(player, id, items));
        }
    }

    /*
     * Useless
     */
    private int getCollectedAmount(Player player, int bossId, int itemId) {
        if (player.getCollectedItems().isEmpty() || player.getCollectedItems().get(bossId) == null || player.getCollectedItems().get(bossId).get(itemId) == null) {
            return 1;
        }
        return player.getCollectedItems().get(bossId).get(itemId);
    }
    
    /*
     * Useless
     */
    private List<Integer> getCollectedItems(Player player, int id, List<Integer> drops) {
        List<Integer> collectedItems = new ArrayList<>();
        if (player.getCollectedItems().get(id) == null) {
            return Collections.emptyList();
        }
        Map<Integer, Integer> currentCollections = player.getCollectedItems().get(id);
        for (int drop : drops) {
            if (currentCollections.containsKey(drop)) {
                collectedItems.add(drop);
            }
        }
        return collectedItems;
    }

    private void initNpcList() {
        npcs.clear();
        for (int npcId : NPC_LIST) {
        	npcs.add(NpcDefinition.forId(npcId));
        }
    }

    public void search(Player player, String name) {
        initNpcList();
        npcs.removeIf(def -> !def.getName().toLowerCase().contains(name.toLowerCase()));

        textStart = 30560;
        for(int i = 0; i < 100; i++) {
            player.getPacketSender().sendString(textStart + i, "");
        }
        npcs.forEach(npc -> {
            player.getPacketSender().sendString(textStart++,  npc.getName());
        });
    }

    public boolean handleButton(Player player, int buttonId) {
        if (!(buttonId >= 30560 && buttonId <= 30760)) {
            return false;
        }
        int index = -30560 + buttonId;
        if (npcs.size() > index) {
            sendNpcData(player, index);
        }
        return true;
    }
}
