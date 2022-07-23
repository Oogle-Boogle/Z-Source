package com.zamron.world.content;

import java.util.stream.DoubleStream;

import com.zamron.model.Item;
import com.zamron.model.Prayerbook;
import com.zamron.model.container.impl.Equipment;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.world.content.combat.prayer.CurseHandler;
import com.zamron.world.entity.impl.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class BonusManager {

    public static void update(Player player) {
        double[] bonuses = new double[18];
        for (Item item : player.getEquipment().getItems()) {
            // lmao they don't check if item is invalid here so it make decent amount of allocatins for no reason
            ItemDefinition definition = ItemDefinition.forId(item.getId());
            for (int i = 0; i < definition.getBonus().length; i++) {
                bonuses[i] += definition.getBonus()[i];
            }
        }
        Bonus[] values = Bonus.values;
        for (int i = 0; i < values.length; i++) {
            Bonus bonus = values[i];
            if (i <= 4) {
                player.getBonusManager().attackBonus[i] = hasOpStats(player) ? 1000 : bonuses[i];
            } else if (i <= 13) {
                int index = i - 5;
                player.getBonusManager().defenceBonus[index] = hasOpStats(player) ? 1000 : bonuses[i];
                if (player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613
                        || player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283
                        && !bonus.magic) {
                    if (player.getDfsCharges() > 0) {
                        player.getBonusManager().defenceBonus[index] += player.getDfsCharges();
                        bonuses[i] += player.getDfsCharges();
                    }
                }
            } else if (i <= 17) {
                int index = i - 14;
                player.getBonusManager().otherBonus[index] = hasOpStats(player) ? 1000 : bonuses[i];
            }
            player.getPacketSender().sendString(bonus.id, bonus.string + ": " + bonuses[i]);
        }
    }

    private static boolean hasOpStats(Player player) {
        return player.lastOpPotion > System.currentTimeMillis() && inLocation(player);
    }

    private static boolean inLocation(Player player) {
        return player.inFFA || player.getWildernessLevel() > 0 || player.getDueling().inDuelWith > 0 || player.inCustomFFA;
    }

    public double[] getAttackBonus() {
        return attackBonus;
    }

    public double[] getDefenceBonus() {
        return defenceBonus;
    }

    public double[] getOtherBonus() {
        return otherBonus;
    }

    public double[] attackBonus = new double[5];

    public double[] defenceBonus = new double[9];

    public double[] otherBonus = new double[4];

    public enum Bonus {
        STAB_OFFENSE(1675, "Stab"),
        SLASH_OFFENSE(1676, "Slash"),
        CRUSH_OFFENSE(1677, "Crush"),
        MAGIC_OFFENSE(1678, "Magic", true),
        RANGE_OFFENSE(1679, "Range"),

        STAB_DEFENCE(1680, "Stab"),
        SLASH_DEFENCE(1681, "Slash"),
        CRUSH_DEFENCE(1682, "Crush"),
        MAGIC_DEFENCE(1683, "Magic", true),
        RANGE_DEFENCE(1684, "Range"),

        SUMMONING(16522, "Summoning"),

        ABSORB_MELEE(16523, "Absorb Melee"),
        ABSORB_MAGIC(16524, "Absorb Magic", true),
        ABSORB_RANGED(16525, "Absorb Ranged"),

        STRENGTH(1686, "Strength"),
        RANGED_STRENGTH(15119, "Ranged Strength"),

        PRAYER(1687, "Prayer"),

        MAGIC_DAMAGE(15120, "Magic Damage", true);

        private final int id;
        private final String string;
        private final String idString;

        private final boolean magic;

        Bonus(int id, String string, boolean magic) {
            this.id = id;
            this.string = string;
            this.idString = Integer.toString(id);
            this.magic = magic;
        }

        Bonus(int id, String string) {
            this(id, string, false);
        }

        public static final Bonus[] values = values();

        private static final Int2ObjectMap<Bonus> idToBonus = new Int2ObjectOpenHashMap<>(values.length);

        static {
            for (Bonus bonus : values) {
                idToBonus.put(bonus.id, bonus);
            }
        }

        public static Bonus forID(int id) {
            return idToBonus.get(id);
        }

    }

    public static final int ATTACK_STAB = 0;

    public static final int ATTACK_SLASH = 1;

    public static final int ATTACK_CRUSH = 2;

    public static final int ATTACK_MAGIC = 3;

    public static final int ATTACK_RANGE = 4;

    public static final int DEFENCE_STAB = 0;

    public static final int DEFENCE_SLASH = 1;

    public static final int DEFENCE_CRUSH = 2;

    public static final int DEFENCE_MAGIC = 3;

    public static final int DEFENCE_RANGE = 4;

    public static final int DEFENCE_SUMMONING = 5;

    public static final int ABSORB_MELEE = 6;

    public static final int ABSORB_MAGIC = 7;

    public static final int ABSORB_RANGED = 8;

    public static final int BONUS_STRENGTH = 0;

    public static final int RANGED_STRENGTH = 1;

    public static final int BONUS_PRAYER = 2;

    public static final int MAGIC_DAMAGE = 3;

    public static boolean hasStats(int id) {
        return DoubleStream.of(ItemDefinition.forId(id).getBonus()).sum() != 0 && ItemDefinition.forId(id).getBonus()[14] >= 70 || DoubleStream.of(ItemDefinition.forId(id).getBonus()).sum() != 0 && ItemDefinition.forId(id).getBonus()[4] >= 70 || DoubleStream.of(ItemDefinition.forId(id).getBonus()).sum() != 0 && ItemDefinition.forId(id).getBonus()[3] >= 70;
    }

    /**
     * CURSES
     **/

    public static void sendCurseBonuses(final Player p) {
        if (p.getPrayerbook() == Prayerbook.CURSES) {
            sendAttackBonus(p);
            sendDefenceBonus(p);
            sendStrengthBonus(p);
            sendRangedBonus(p);
            sendMagicBonus(p);
        }
    }

    public static void sendAttackBonus(Player p) {
        double boost = p.getLeechedBonuses()[0];
        int bonus = 0;
        if (p.getCurseActive()[CurseHandler.LEECH_ATTACK]) {
            bonus = 5;
        } else if (p.getCurseActive()[CurseHandler.TURMOIL])
            bonus = 30;
        bonus += boost;
        if (bonus < -25)
            bonus = -25;
        p.getPacketSender().sendString(690, "" + getColor(bonus) + "" + bonus + "%");
    }

    public static void sendRangedBonus(Player p) {
        double boost = p.getLeechedBonuses()[4];
        int bonus = 0;
        if (p.getCurseActive()[CurseHandler.LEECH_RANGED])
            bonus = 5;
        else if (p.getCurseActive()[CurseHandler.TURMOIL])
            bonus = 30;
        bonus += boost;
        if (bonus < -25)
            bonus = -25;
        p.getPacketSender().sendString(693, "" + getColor(bonus) + "" + bonus + "%");
    }

    public static void sendMagicBonus(Player p) {
        double boost = p.getLeechedBonuses()[6];
        int bonus = 0;
        if (p.getCurseActive()[CurseHandler.LEECH_MAGIC])
            bonus = 5;
        else if (p.getCurseActive()[CurseHandler.TURMOIL])
            bonus = 30;
        bonus += boost;
        if (bonus < -25)
            bonus = -25;
        p.getPacketSender().sendString(694, "" + getColor(bonus) + "" + bonus + "%");
    }

    public static void sendDefenceBonus(Player p) {
        double boost = p.getLeechedBonuses()[1];
        int bonus = 0;
        if (p.getCurseActive()[CurseHandler.LEECH_DEFENCE])
            bonus = 5;
        else if (p.getCurseActive()[CurseHandler.TURMOIL])
            bonus = 15;
        bonus += boost;
        if (bonus < -25)
            bonus = -25;
        p.getPacketSender().sendString(692, "" + getColor(bonus) + "" + bonus + "%");
    }

    public static void sendStrengthBonus(Player p) {
        double boost = p.getLeechedBonuses()[2];
        int bonus = 0;
        if (p.getCurseActive()[CurseHandler.LEECH_STRENGTH])
            bonus = 5;
        else if (p.getCurseActive()[CurseHandler.TURMOIL])
            bonus = 23;
        bonus += boost;
        if (bonus < -25)
            bonus = -25;
        p.getPacketSender().sendString(691, "" + getColor(bonus) + "" + bonus + "%");
    }

    public static String getColor(int i) {
        if (i > 0)
            return "@gre@+";
        if (i < 0)
            return "@red@";
        return "";
    }
}
