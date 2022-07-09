package com.zamron.world.content.customraids;


import com.zamron.model.Item;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.World;

import static sun.audio.AudioPlayer.player;

public enum RaidDifficulty {


    /** Random number generator in CustomRaid rolls a number between 0->100.
     * If you want a 5% chance to win a rare, you'll need to set the 'rareChance' to 95'.
     * This means the random number needs to be 95 or more to win. This is equivalent to 5%.
     */


	EASY(3042, 5212, "Easy raid", new Item[] {new Item(10835, 500), new Item(15334, 1), new Item(18346, 1)},  //Common Rewards
            new Item[]{new Item(1499), new Item(3973), new Item(926 ,1), new Item(3985), new Item(20693), new Item(15220, 1),
            new Item(20694), new Item(2749, 1), new Item(2750, 1), new Item(2751, 1), new Item(2752, 1),
            new Item(2753, 1), new Item(2754, 1), new Item(2755, 1),
            new Item(19721, 1), new Item(19722, 1), new Item(19723, 1), new Item(19724, 1), new Item(19725, 1),
                    new Item(19734, 1), new Item(19736, 1), new Item(3951, 1), new Item(19720, 1),
                    new Item(4770, 1), new Item(4771, 1), new Item(4772, 1), new Item(5131, 1), new Item(6733, 1),
                    new Item(6583, 1), new Item(15653, 1), new Item(5132, 1), new Item(19935, 1)
            },85, "@gre@Easy raid"), //Rare Rewards

    MEDIUM(3042, 5212, "Medium raid", new Item[] {new Item(1499), new Item(3973), new Item(926 ,1), new Item(3985), new Item(19776), new Item(20693), new Item(15220, 1),
            new Item(20694), new Item(2749, 1), new Item(2750, 1), new Item(2751, 1), new Item(2752, 1),
            new Item(2753, 1), new Item(2754, 1), new Item(2755, 1), new Item(19776),
            new Item(19721, 1), new Item(19722, 1), new Item(19723, 1), new Item(19724, 1), new Item(19725, 1),
            new Item(19734, 1), new Item(19736, 1), new Item(3951, 1), new Item(19720, 1),
            new Item(4770, 1), new Item(4771, 1), new Item(4772, 1), new Item(5131, 1), new Item(6733, 1),
            new Item(6583, 1), new Item(15653, 1),new Item(10835, 500), new Item(10835, 1000), new Item(10835, 2500), new Item(15334, 1)}, //Common Rewards
            //Commons ^, Rares V
            new Item[]{new Item(4800),new Item(4801),new Item(4802),new Item(5079),new Item(15012),new Item(3951), new Item(5133,1),
                    new Item(3316),new Item(3931),new Item(3959),new Item(3960),new Item(5186),new Item(5187),new Item(6584),
                    new Item(14559),new Item(18750),new Item(18751),new Item(5131),new Item(4770),new Item(4772),new Item(5209),
                    new Item(923),new Item(3994),new Item(3995),new Item(3996), new Item(20054, 1),
                    new Item(20695), new Item(6583, 1), new Item(12601, 1), new Item(3908, 1), new Item(3909, 1),
                    new Item(3910, 1), new Item(19004, 1), new Item(19935, 1), new Item(19936, 1)
                    },90, "@red@Medium raid"), //Rare Rewards 15% chance

    HARD(3042, 5212, "Hard raid", new Item[] {new Item(10835, 5000), new Item(20260, 10),new Item(14559),new Item(18750),new Item(18751),new Item(5131),new Item(4770),new Item(4772),new Item(5209),
            new Item(923),new Item(3994),new Item(3995),new Item(3996), new Item(20054, 1), new Item(20695), new Item(6583, 1), new Item(12601, 1), new Item(3908, 1), new Item(3909, 1),
            new Item(12845, 3), new Item(12846, 3), new Item(12847, 3)},//Common Rewards


            //Rare rewards
            new Item[]{ new Item(19159),new Item(19160),new Item(19161), new Item(19163),new Item(19164),new Item(19166),
                    new Item(19469), new Item(20427), new Item(20431), new Item(20700), new Item(3820, 1), new Item(3821, 1),
                    new Item(3822, 1), new Item(9104, 1), new Item(19936, 1), new Item(19937, 1), new Item(19938, 1),

                    new Item(18391, 1), new Item(12848, 1), new Item(12845, 100), new Item(12846, 100), new Item(12847, 100),
                    new Item(8644, 1), new Item(8654, 1), new Item(8655, 1), new Item(8656, 1), new Item(3458, 1), new Item(12426, 1)

            },95, "@red@Hard raid"); //Rare Rewards 10% chance to win.

    RaidDifficulty(int x, int y, String description, Item[] commonRewards, Item[] rareRewards, int rareChance, String name) {
        this.x = x;
        this.y = y;
        this.description = description;
        this.commonRewards = commonRewards;
        this.rareRewards = rareRewards;
        this.rareChance = rareChance;
        this.name = name;
    }

    private final int x;
    private final int y;
    private final String description;
    private final Item[] commonRewards;
    private final Item[] rareRewards;
    private final int rareChance;
    private final String name;

    public static final RaidDifficulty[] DIFFICULTIES = values();


    public String getDescription() {
        return description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Item[] getCommonRewards() {
        return commonRewards;
    }

    public Item[] getRareRewards() {
        return rareRewards;
    }

    public int getRareChance() {
        return rareChance;
    }

    public String getName() {
        return name;
    }
}