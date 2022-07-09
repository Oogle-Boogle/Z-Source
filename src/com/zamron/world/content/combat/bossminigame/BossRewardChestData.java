package com.zamron.world.content.combat.bossminigame;

import com.zamron.model.Item;
import lombok.Data;

/**
 *
 * @Author Flub
 * Edited and fixed by Oogleboogle
 */
@Data
public class BossRewardChestData {

    /** Rewards Are Stored Here **/
    public static final Item[] SHIT_REWARDS = {
            new Item(10835, 100),
            new Item(10835, 200),
            new Item(10835, 50),
            new Item(10835, 75),
            new Item(10835, 150),
            new Item(15334, 1)
            };

    public static final Item[] MEDIUM_REWARDS = {
            new Item(19140, 1),
            new Item(10835, 1000),
            new Item(10835, 5000),
            new Item(10835, 6500),
            new Item(10835, 12500),
            new Item(10835, 15000),
            new Item(10835, 20000)
    };

    public static final Item[] RARE_REWARDS = {
            new Item(19141, 1),
            new Item(13591, 1),
            new Item(13591, 2),
            new Item(11179, 1),
            new Item(11179, 2),
            new Item(11179, 5),
            new Item(11179, 10)

    };

}
