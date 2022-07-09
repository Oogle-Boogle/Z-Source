package com.zamron.world.content.combat.bossminigame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/**
 /@Author Flub
 */
@Getter
@RequiredArgsConstructor
public enum EquipmentSetups {

    //Default used in case of errors
    DEFAULT(4151, 20072, 10828, 4720, 4722, 1704, 6570, 7462, 11732, -1),

    //First - 0
    FROST_DRAGON(4587, 1540, 1163,1127, 1079, 1725,6568, 7462, 13462, -1),
    BLACK_DRAGON(1333, 1540, 1161 ,1123, 1073, 1725,6568, 7462, 13462, -1),
    KBD_WAVE_ONE(4587, 1540, 10828 ,9674, 9676, 6585,6568, 7462, 13462, -1),
    //Second -1
    TORMENTED_DEMON(4710, 3840, 4708, 4712, 4714, 1725, 2414, 4095, 4097, -1), //needs ancient magebook
    CHAOS_ELEMENTAL(4151, 8850, 12282,9674, 9676, 1731,6568, 7462, 3105, -1),
    DAGANNOTH_PRIME(861, -1, 10828,2503, 2497, 15126,10498, 2491, 2577, -1),
    //Third -2
    DAGANNOTH_SUPREME(1333,20072, 10828, 4720, 4722, 1725, 6570, 7460, 4131, -1),
    BARRELCHEST(4710, 3840, 4708, 4712, 4714, 1725, 2414, 4095, 4097, -1), //needs normal mage book
    CERBERUS(13045, -1, 8921, 10551, 4087, 1704, 15345, 13006, 11732, -1),
    //Fourth -3
    VENENATIS(4587, 1540, 10828, 9674, 9676, 6585, 6568, 7462, 13462, -1),
    DAGANNOTH_REX(12284, 13736, 4089, 4091, 4093, 1704, 2412, 7462, 4097, -1), //ancient
    SKOTIZO(21053, 20072, 10828, 10551, 4759, 1704, 6570, 7460, 4127, -1),
    //Fifth -4
    CRAZY_LVL2_MAN(18894,18901, 10828, 13887, 13893, 6585, 6570, 7462, 11732, -1),
    SCORPIA(21053, 20072, 10828, 10551, 4759, 1704, 6570, 7460, 4127, -1),
    BORK(4151, 20072, 10828, 4720, 4722, 1704, 6570, 7462, 11732, -1);

    private final int weapon;
    private final int shield;
    private final int helm;
    private final int body;
    private final int legs;
    private final int neck;
    private final int cape;
    private final int hands;
    private final int feet;
    private final int ring;

}
