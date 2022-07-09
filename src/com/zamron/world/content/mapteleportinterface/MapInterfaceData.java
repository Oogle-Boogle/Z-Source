package com.zamron.world.content.mapteleportinterface;

import com.zamron.model.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MapInterfaceData {

    BANK(new Position(2336, 3688), 23502),
    ALTER(new Position(2334,3656), 23505),
    SLAYER(new Position(2325,3671), 23508),
    SKILLING_BOSS(new Position(2329,3701), 23520),
    WORLD_BOSSES(new Position(2336, 3671), 23511),
    AFK(new Position(2337,3716), 23523),
    DUNGEON(new Position(2319,3678), 23517),
    SHOPS(new Position(2352,3674), 23514),
    INSTANCE(new Position(2319, 3678), 23526);

    @Getter public Position POS;
    @Getter public int ButtonID;



}
