package com.zamron.model.definitions;

import com.zamron.model.container.impl.Equipment;
import com.zamron.world.entity.impl.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This file manages every item definition, which includes
 * their name, description, value, skill requirements, etc.
 *
 * @author relex lawl
 */

public class ItemDefinition {

    /**
     * The directory in which item definitions are found.
     */
    private static final String FILE_DIRECTORY = "./data/def/txt/items.txt";

    /**
     * The max amount of items that will be loaded.
     */
    private static final int MAX_AMOUNT_OF_ITEMS = 22694;

    /**
     * ItemDefinition array containing all items' definition values.
     */
    private static ItemDefinition[] definitions = new ItemDefinition[MAX_AMOUNT_OF_ITEMS];

    /**
     * Loading all item definitions
     */
    public static void init() {
        ItemDefinition definition = definitions[0];
        try {
            File file = new File(FILE_DIRECTORY);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("inish")) {
                    definitions[definition.id] = definition;
                    continue;
                }
                String[] args = line.split(": ");
                if (args.length <= 1)
                    continue;
                String token = args[0], value = args[1];
                if (line.contains("Bonus[")) {
                    String[] other = line.split("]");
                    int index = Integer.valueOf(line.substring(6, other[0].length()));
                    double bonus = Double.valueOf(value);
                    definition.bonus[index] = bonus;
                    continue;
                }
                if (line.contains("Requirement[")) {
                    String[] other = line.split("]");
                    int index = Integer.valueOf(line.substring(12, other[0].length()));
                    int requirement = Integer.valueOf(value);
                    definition.requirement[index] = requirement;
                    continue;
                }
                switch (token.toLowerCase()) {
                    case "item id":
                        int id = Integer.valueOf(value);
                        definition = new ItemDefinition();
                        definition.id = id;
                        break;
                    case "name":
                        if (value == null)
                            continue;
                        definition.name = value;
                        break;
                    case "examine":
                        definition.description = value;
                        break;
                    case "value":
                        int price = Integer.valueOf(value);
                        definition.value = price;
                        break;
                    case "stackable":
                        definition.stackable = Boolean.valueOf(value);
                        break;
                    case "noted":
                        definition.noted = Boolean.valueOf(value);
                        break;
                    case "double-handed":
                        definition.isTwoHanded = Boolean.valueOf(value);
                        break;
                    case "equipment type":
                        definition.equipmentType = EquipmentType.valueOf(value);
                        break;
                    case "is weapon":
                        definition.weapon = Boolean.valueOf(value);
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ItemDefinition[] getDefinitions() {
        return definitions;
    }

    /**
     * Gets the item definition correspondent to the id.
     *
     * @param id The id of the item to fetch definition for.
     * @return definitions[id].
     */

    public static ItemDefinition forId(int id) {
        return (id < 0 || id > definitions.length || definitions[id] == null) ? new ItemDefinition() : definitions[id];
    }

    public static void whatDrops(Player player, String name) {

        int itemID = getItemId(name);

        if (itemID == -1) {
            player.getPacketSender().sendMessage("Item not found!");
            return;
        }

        ItemDefinition itemDef = ItemDefinition.forId(itemID);

        List<Integer> previouslyListed = new ArrayList<>();

        try {
            for (NPCDrops npcDrops : NPCDrops.getDrops().values()) {
                if (npcDrops != null) {
                    for (NPCDrops.NpcDropItem item : npcDrops.getDropList()) {
                        if (item != null && item.getId() == itemID) {
                            for (int npcId : npcDrops.getNpcIds()) {
                                if (npcId == -1)
                                    return;
                                NpcDefinition npcDef = NpcDefinition.forId(npcId);
                                if (npcDef != null && !npcDef.getName().equalsIgnoreCase("null") && !previouslyListed.contains(npcId)) {
                                    previouslyListed.add(npcId);
                                    player.getPacketSender().sendMessage(itemDef.getName()
                                            + " are dropped by "
                                            + npcDef.getName()
                                            + "! Noted: "
                                            + (itemDef.isNoted()
                                            ? "True" : "False"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (previouslyListed.isEmpty()) {
            player.getPacketSender().sendMessage("Unable to find an NPC that drops: "
                    + itemDef.getName());
        }
        previouslyListed.clear();
    }

    /**
     * Gets the max amount of items that will be loaded
     * in Niobe.
     *
     * @return The maximum amount of item definitions loaded.
     */
    public static int getMaxAmountOfItems() {
        return MAX_AMOUNT_OF_ITEMS;
    }

    /**
     * The id of the item.
     */
    private int id = 0;

    /**
     * Gets the item's id.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * The name of the item.
     */
    private String name = "None";

    /**
     * Gets the item's name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * The item's description.
     */
    private String description = "Null";

    /**
     * Gets the item's description.
     *
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Flag to check if item is stackable.
     */
    private boolean stackable;

    /**
     * Checks if the item is stackable.
     *
     * @return stackable.
     */
    public boolean isStackable() {
        if (noted)
            return true;
        return stackable;
    }

    /**
     * The item's shop value.
     */
    private int value;

    /**
     * Gets the item's shop value.
     *
     * @return value.
     */
    public int getValue() {
        return isNoted() ? ItemDefinition.forId(getId() - 1).value : value;
    }

    /**
     * Gets the item's equipment slot index.
     *
     * @return equipmentSlot.
     */
    public int getEquipmentSlot() {
        return equipmentType.slot;
    }

    /**
     * Flag that checks if item is noted.
     */
    private boolean noted;

    /**
     * Checks if item is noted.
     *
     * @return noted.
     */
    public boolean isNoted() {
        return noted;
    }

    private boolean isTwoHanded;

    /**
     * Checks if item is two-handed
     */
    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    private boolean weapon;

    public boolean isWeapon() {
        return weapon;
    }

    private EquipmentType equipmentType = EquipmentType.WEAPON;

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    /**
     * Checks if item is full body.
     */
    public boolean isFullBody() {
        return equipmentType.equals(EquipmentType.PLATEBODY);
    }

    /**
     * Checks if item is full helm.
     */
    public boolean isFullHelm() {
        return equipmentType.equals(EquipmentType.FULL_HELMET);
    }

    private double[] bonus = new double[18];

    public double[] getBonus() {
        return bonus;
    }

    private int[] requirement = new int[27];

    public int[] getRequirement() {
        return requirement;
    }

    public enum EquipmentType {
        HAT(Equipment.HEAD_SLOT),
        CAPE(Equipment.CAPE_SLOT),
        SHIELD(Equipment.SHIELD_SLOT),
        GLOVES(Equipment.HANDS_SLOT),
        BOOTS(Equipment.FEET_SLOT),
        AMULET(Equipment.AMULET_SLOT),
        RING(Equipment.RING_SLOT),
        ARROWS(Equipment.AMMUNITION_SLOT),
        FULL_MASK(Equipment.HEAD_SLOT),
        FULL_HELMET(Equipment.HEAD_SLOT),
        BODY(Equipment.BODY_SLOT),
        PLATEBODY(Equipment.BODY_SLOT),
        LEGS(Equipment.LEG_SLOT),
        WEAPON(Equipment.WEAPON_SLOT);
        //WING(Equipment.WINGS_SLOT),
        //AURA(Equipment.AURA_SLOT);

        private EquipmentType(int slot) {
            this.slot = slot;
        }

        private int slot;
    }

    @Override
    public String toString() {
        return "[ItemDefinition(" + id + ")] - Name: " + name + "; equipment slot: " + getEquipmentSlot() + "; value: "
                + value + "; stackable ? " + Boolean.toString(stackable) + "; noted ? " + Boolean.toString(noted) + "; 2h ? " + isTwoHanded;
    }

    public int getUnnotedId() {

        int unNoted = id - 1;

        switch (id) {

        }

        if (ItemDefinition.forId(unNoted).getName().equals(ItemDefinition.forId(id).getName())) {
            return unNoted;
        }
        return id;
    }

    public static int getItemId(String itemName) {
        for (int i = 0; i < MAX_AMOUNT_OF_ITEMS; i++) {
            if (definitions[i] != null) {
                if (definitions[i].getName().equalsIgnoreCase(itemName)) {
                    return definitions[i].getId();
                }
            }
        }
        return -1;
    }

}
