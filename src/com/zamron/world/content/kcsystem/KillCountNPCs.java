package com.zamron.world.content.kcsystem;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import static com.zamron.world.content.kcsystem.KillRequirement.killRequirement;

public enum KillCountNPCs {

    LUCARIO(3263, killRequirement(17, 50)),
    HADES(15, killRequirement(3263, 50)),
    DEFENDERS(9994, killRequirement(15, 100)),
    GODZILLA(9932, killRequirement(9994, 150)),
    DEMONIC_OLM(224, killRequirement(9932, 175)),
    CERBERUS(1999, killRequirement(224, 200)),
    ZEUS(16, killRequirement(1999, 225)),
    INFARTICO(9993, killRequirement(16, 260)),
    LORDVALOR(9277, killRequirement(9993, 350)),
    STORM_BREAKER(33, killRequirement(9277, 450)),
    DZANTH(9273, killRequirement(33, 550)),
    KINGKONG(9903, killRequirement(9273, 750)),
    CORPBEAST(8133, killRequirement(9903, 750)),
    LUCID_WARRIORS(9247, killRequirement(8133, 750)),
    HULK(8493, killRequirement(9247, 750)),
    DARKBLUE_WIZARDS(9203, killRequirement(8493, 750)),
    HEATED_PYROS(172, killRequirement(9203, 750)),
    PURPLE_WYRM(9935, killRequirement(172, 1000)),
    TRINITY(170, killRequirement(9935, 1000)),
    CLOUD(169, killRequirement(170, 1000)),
    HERBAL_ROUGE(219, killRequirement(169, 1000)),
    EXODEN(12239, killRequirement(219, 1000)),
    SUPREME_NEX(3154, killRequirement(12239, 1000)),
    APOLLO_RANGER(1684, killRequirement(3154, 550)),
    NOXIOUS_TROLL(5957, killRequirement(1684, 550)),
    AZAZEL_BEAST(5958, killRequirement(5957, 550)),
    RAVANA(5959, killRequirement(5958, 550)),
    WARRIORS(185, killRequirement(5959, 550)),
    WARR(6311, killRequirement(185, 550)),
    SUPREME_BOX(192, killRequirement(197, 1000), killRequirement(191, 1000)),
    VADER(11, killRequirement(1069, 250)),
    EXTREME_BOX(191, killRequirement(197, 1000));

    KillCountNPCs(int npcId, KillRequirement... killRequirements) {
        this.id = npcId;
        this.killRequirements = killRequirements;
    }

    private final int id;
    private final KillRequirement[] killRequirements;

    public int getId() {
        return id;
    }

    public KillRequirement[] getKillRequirements() {
        return killRequirements;
    }

    public static final KillCountNPCs[] values = values();

    private static final Int2ObjectMap<KillCountNPCs> idToData = new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (KillCountNPCs value : values) {
            idToData.put(value.id, value);
        }
    }

    public static KillCountNPCs forID(int id) {
        return idToData.get(id);
    }

}