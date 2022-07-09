package com.zamron.world.content.collectionlog;

import com.zamron.model.input.Input;
import com.zamron.world.entity.impl.player.Player;

public class SearchForCollectionNpc extends Input {
    public void handleSyntax(Player player, String msg) {
    	player.getCollectionLog().search(msg);
    }
}
