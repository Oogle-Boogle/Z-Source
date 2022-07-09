package com.zamron.world.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.zamron.util.RandomUtility;
import com.zamron.world.entity.impl.player.Player;

public final class StoreMBox {

	public enum StoreItems {

		PAPER_SACK(1, 18950),
		DEMONIC_CAPE(1, 18748),
		DEMONI_OLM_GLOVES(1, 18751),
		TAXBAGS(200, 10835),

		DRAGON_MINIGUN(1, 5131),
		CRIMSON_PERNIX_COWL(1, 4772),
		CRIMSON_PERNIX_BODY(1, 4771),
		CRIMSON_PERNIX_LEGS(1, 4770),
		RANGE_GLOVES(1, 18347),
		INFARTICO_CAPE(1, 5209),
		INFARTICO_BLADE(1, 923),
		COLLECTORS_AMULET(1, 19886),
		LORD_VALOR_HELMET(1, 3908),
		LORD_VALOR_BODY(1, 3910),
		LORD_VALOR_LEGS(1, 3908),
		LORD_VALOR_CAPE(1, 3907),
		LORD_VALORS_STAFF(1, 19720);


		StoreItems(int amount, int... ids) {
			this.amount = amount;
			this.ids = ids;
		}

		private int[] ids;
		private int amount;
	}

	Random random = ThreadLocalRandom.current();

	private final int BOX_ID = 3988;

	public final void getReward(final Player player) {

		if (canOpen(player)) {
			StoreItems[] items = StoreItems.values();

			StoreItems itemPicked = items[random.nextInt(items.length)];

			player.getInventory().delete(BOX_ID);
			player.getInventory().addItems(itemPicked.amount, itemPicked.ids);
			String itemName = capitalizeFirst(itemPicked.toString().replaceAll("_", " ").replaceAll("PERCENT", "%"));
			player.sendMessage("@blu@You open the box and get an@red@ " + itemName);
		} else {
			player.sendMessage("@red@You need atleast 3 free inventory slots to open this box");
		}
	}

	private final String capitalizeFirst(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	private final boolean canOpen(final Player player) {

		return player.getInventory().getFreeSlots() >= 3 && player.getInventory().contains(BOX_ID);

	}
}
