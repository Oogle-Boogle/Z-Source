package com.zamron.world.content.partyroom;

import com.zamron.model.Item;
import com.zamron.model.container.ItemContainer;
import com.zamron.model.container.StackType;
import com.zamron.util.Misc;

public class PartyRoomChestItemContainer extends ItemContainer {


    private static final int MAIN_CONTAINER_SIZE = 216;

    @Override
    public int capacity() {
        return MAIN_CONTAINER_SIZE;
    }

    @Override
    public StackType stackType() {
        return StackType.DEFAULT;
    }

    @Override
    public ItemContainer refreshItems() {
        return null;
    }

    @Override
    public ItemContainer full() {
        return null;
    }

    public Item getRandom() {
        Item[] item = getValidItemsArray();
        Item reward = item[Misc.random(item.length - 1)];
        int amount = 1;
        if (reward.getDefinition().isStackable()) {
            if (reward.getAmount() > 1) {
                amount = Misc.exclusiveRandom(1, reward.getAmount());
            }
        }
        return new Item(reward.getId(), amount);
    }
}
