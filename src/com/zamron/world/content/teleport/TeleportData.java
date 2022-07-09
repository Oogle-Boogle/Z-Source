package com.zamron.world.content.teleport;

import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.model.definitions.NPCDrops;
import org.apache.commons.lang3.text.WordUtils;

public enum TeleportData {

//    GIANT_MOLE(TeleportCategory.GLOBAL, new Position(1761, 5186, 0), 3340, 2000, new Item(5073), new Item(20558, 100)),
//    CORPOREAL_BEAST(TeleportCategory.SKILLING, new Position(3197, 3673, 0), itemsFromIds(13734, 13754, 13752, 13746, 13750, 13748, 12001)),

//starter
	STARTER("Starter Zone", TeleportCategory.MONSTERS, new Position(2660, 3045, 0), 4455),



	//Monster
	DEFENDERS("Abbadon", TeleportCategory.BOSSES, new Position(2516, 5173, 0), 9994),



	//Hardened
	CORPOREAL_BEAST("Progression Island", TeleportCategory.MINIGAMES, new Position(3795, 3543,0), 8133),


	//expert
	NEX("Demonic Mystery", TeleportCategory.GLOBAL, new Position(3702, 2971,0), 3154),


	//Zone
	BOX_ZONE("JOKER(Range)", TeleportCategory.ULTRA_BOSSES, new Position(2139, 5100, 0), 197),

//	PYRAMID_HEAD("Pyramid Head(Melee)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
//	OPTIMUS_PRIME("Optimus Prime(Ranged)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
//	DARK_MAGICIAN("Dark Magician(Mage)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
//	DOOMSDAY("Doomsday(Melee)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
//	SASUKE("Sasuke(Ranged)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
//	BROLY("Broly(Mage)", TeleportCategory.ULTRA_BOSSES, new Position(2449, 2848, 0), 9003),
////	MAGMA("Magma Satanic Devil(Melee)", TeleportCategory.ULTRA_BOSSES, new Position(2529, 5850, 0), 10141),
    ;

    public final static TeleportData[] values = TeleportData.values();

    private final String name;
    private final TeleportCategory category;
    private final int npcId;
    private final int npcZoom;
    private final Position position;
	private Item[] items;

	TeleportData(String name, TeleportCategory category, Position position, int npcId, int npcZoom) {
        this.name = name == null ? WordUtils.capitalize(this.name().toLowerCase().replaceAll("_", " ")) : name;
        this.category = category;
        this.npcId = npcId;
        this.npcZoom = npcZoom;
        this.position = position;
    }

    TeleportData(String name, TeleportCategory category, Position position) {
        this(name, category, position, -1, 1400);
    }

    TeleportData(TeleportCategory category, Position position) {
        this(null, category, position);
    }

    TeleportData(TeleportCategory category, Position position, int npcId) {
        this(null, category, position, npcId, 1400);
    }

	TeleportData(String name, TeleportCategory category, Position position, int npcId) {
		this(name, category, position, npcId, 1400);
	}

    TeleportData(TeleportCategory category, Position position, int npcId, int npcZoom) {
        this(null, category, position, npcId, npcZoom);
    }

	public static void main(String[] args) {
		for(TeleportData data : values) {
			System.out.println("list.add(" + data.getPosition().getRegionId() + ");");
		}
	}

	public static void loadDrops()
	{
		for (TeleportData data : values) {
			NPCDrops npcDrops = NPCDrops.forId(data.npcId);
			if (npcDrops == null)
				continue;
			NPCDrops.NpcDropItem[] dropItems = npcDrops.getDropList();
			if (dropItems == null || dropItems.length == 0)
				continue;
			Item[] items = new Item[dropItems.length];
			for (int i = 0; i < dropItems.length; i++) {
				items[i] = new Item(dropItems[i].getId());
			}
			data.items = items;
		}
	}

	public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public TeleportCategory getCategory() {
        return category;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getNpcZoom() {
        return npcZoom;
    }

    public Item[] getItems() {
        return items;
    }

    private static Item[] itemsFromIds(int... ids) {
        if (ids.length == 0)
            return null;
        Item[] items = new Item[ids.length];
        for (int i = 0; i < ids.length; i++) {
            items[i] = new Item(ids[i]);
        }
        return items;
    }
}