package com.zamron.world.content.pos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.zamron.model.definitions.ItemDefinition;
import com.zamron.net.packet.PacketBuilder;
import com.zamron.net.packet.Packet.PacketType;
import com.zamron.util.Misc;
import com.zamron.util.NameUtils;
import com.zamron.world.World;
import com.zamron.world.content.PlayerLogs;
import com.zamron.world.entity.impl.player.Player;
import com.zamron.world.entity.impl.player.PlayerLoading;
import com.zamron.world.entity.impl.player.PlayerSaving;

/**
 * A class representing a single player owned shop. In this
 * we hold and manage all the items that are added or sold
 * using an instance of this class. A single instance of this
 * class shows a single player owned shop in the manager class.
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class PlayerOwnedShop {

	/**
	 * The total capacity of items a shop can contain.
	 */
	public static final int SHOP_CAPACITY = 32;
	
	/**
	 * A collection of all the items in this player owned
	 * shop. If an item slot is empty this is represented as
	 * {@code null}, else as an {@link Item} instance.
	 */
	private Item[] items = new Item[SHOP_CAPACITY];
	
	/**
	 * A reference to the player owning this shop. We use this
	 * reference to notify the shop owner of certain events.
	 */
	private Player owner;
	
	/**
	 * The name of the player owning this player owned shop.
	 */
	private String username;
	
	public void open(Player player) {
		player.getPacketSender().sendString(32610, "Player Owned Shop - "+ownerName());
		player.getPacketSender().sendString(32611, "Search");
		player.getPlayerOwnedShopManager().updateFilter("");
		resetItems(player);
		refresh(player, false);
	}
	
	public void refresh(Player player, boolean myShop) {
		
		for(int i = 0; i < items.length; i++) {
			
			Item item = items[i];
			
			PacketBuilder out = new PacketBuilder(34, PacketType.SHORT);
			
			out.putShort(myShop ? 33621 : 32621);
			out.put(i);
			out.putShort(item == null ? 0 : item.getId() + 1);
			
			if(item != null && item.getAmount() > 254) {
				out.put(255);
				out.putInt(item.getAmount());
			} else {
				out.put(item == null ? 0 : item.getAmount());
			}
			
			player.getSession().queueMessage(out);
			
		}
		
	}
	
	public void add(int id, int amount) {
		
		ItemDefinition definition = ItemDefinition.forId(id);
		int price = 0;
		
		if(definition != null) {
			price = definition.getValue();
		}
		
		PlayerLogs.log(owner.getUsername(), "Added x" + amount + " "+ definition.getName() + " to POS " + " for "+ price);
		add(id, amount, price);
		
	}
	
	public void add(int id, int amount, int price) {
		add(new Item(id, amount, price));
		refreshAll();
		save();
	}
	
	public void add(Item item) {
		
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null && items[i].getId() == item.getId()) {
				items[i].setAmount(items[i].getAmount() + item.getAmount());
				return;
			}
		}
		
		int index = freeSlot();
		
		if(index != -1) {
			if(items[index] == null) {
				items[index] = item;
			}
		}
		
	}
	
	public int remove(int slot, int amount) {
		
		Item item = getItem(slot);
		int removed = -1;
		
		if(item != null) {
			if(amount >= item.getAmount()) {
				items[slot] = null;
				shift();
				removed = item.getAmount();
			} else {
				item.setAmount(item.getAmount() - amount);
				removed = amount;
			}
			save();
			refreshAll();
		}
		
		return removed;
		
	}
	
	public void shift() {
		
		List<Item> temp = new ArrayList<>();
		
		for(Item item : items) {
			if(item != null) {
				temp.add(item);
			}
		}
		
		items = temp.toArray(new Item[SHOP_CAPACITY]);
		
	}
	
	public int freeSlot() {
		for(int i = 0; i < items.length; i++){
			if(items[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public Item getItem(int slot) {
		return items[slot];
	}
	
	public int getAmount(int id) {
		for(int i = 0; i < items.length; i++){
			if(items[i] != null && items[i].getId() == id) {
				return items[i].getAmount();
			}
		}
		return 0;
	}
	
	public boolean contains(int id) {
		for(int i = 0; i < items.length; i++){
			if(items[i] != null && items[i].getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(String name) {
		
		if(name == null) {
			return false;
		}
		
		for(int i = 0; i < items.length; i++){
			if(items[i] != null && items[i].getDefinition() != null && items[i].getDefinition().getName() != null && items[i].getDefinition().getName().toLowerCase().contains(name.toLowerCase())) {
				return true;
			}
		}
		return false;
		
	}
	
	public int size() {
		int size = 0;
		for(int i = 0; i < items.length; i++){
			if(items[i] != null) {
				size++;
			}
		}
		return size;
	}
	
	public void refreshAll() {
		for(Player player : World.getPlayers()) {
			if(player != null && player.getPlayerOwnedShopManager().getCurrent() == this) {
				refresh(player, player.getPlayerOwnedShopManager().getMyShop() == this);
			}
		}
	}
	
	public static void resetItems(Player player) {
		
		for(int i = 0; i < SHOP_CAPACITY; i++) {
			
			PacketBuilder out = new PacketBuilder(34, PacketType.SHORT);
			
			out.putShort(32621);
			out.put(i);
			out.putShort(0);
			out.put(0);
			
			player.getSession().queueMessage(out);
			
		}
		
	}
	
	/**
	 * Access the online or offline player instance of the
	 * owner of this player owned shop. If the owner is indeed
	 * offline his/her details will be loaded from the saved
	 * serialized character file and reinterpreted to actual
	 * OOP objects to access the instance within the JVM.
	 * @param force If the instance is forced to be online.
	 * @return The player instance to the owner.
	 */
	public Player accessOwner(boolean force) {
		
		if(owner == null && !force) {
			
			owner = World.getPlayerByName(username);
			
			Path path = Paths.get("./data/saves/characters/", username + ".json");
			File file = path.toFile();
			
			if(owner == null && file.exists()) {
				
				Player player = new Player(null);
				
				player.setUsername(username);
				player.setLongUsername(NameUtils.stringToLong(username));
				
				PlayerLoading.getResult(player, true);
				
				return player;
				
			}
			
		}
		
		return owner;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String ownerName() {
		
		Player owner = accessOwner(false);
		
		if(owner != null) {
			return owner.getUsername();
		}
		
		return username;
		
	}
	
	/**
	 * 
	 * @param amount
	 */
	public void addMoney(long amount) {
		
		Player owner = accessOwner(false);
		
		if(owner != null) {
			
			owner.getPlayerOwnedShopManager().addEarnings(amount);
			
			//owner.getItems().sendItemToAnyTabOffline(5022, amount, owner.isActive());
			
			/*if(owner.getAbsX() <= 0 || owner.getAbsY() <= 0) {
				owner.setAbsX(owner.teleportToX);
				owner.setAbsY(owner.teleportToY);
			}*/
			
			if(!owner.isActive()) {
				owner.setShopUpdated(true);
			} else {
				String formatPrice = Misc.sendCashToString(amount);
				owner.sendMessage("<col=FF0000>You have earned "+formatPrice+" B from your shop. Make sure to claim it.</col>");
			}
			
			PlayerLogs.log(owner.getUsername(), "Added " + Misc.sendCashToString(amount) + " to money pouch");
			PlayerSaving.save(owner);
			
		}
		
	}
	
	public void save() {
		
		Path path = Paths.get(PlayerOwnedShopManager.DIRECTORY + File.separator, getUsername() + ".txt");
		
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path.getParent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			for(Item item : getItems()) {
				if(item != null) {
					writer.write(item.getId()+" - "+item.getAmount()+" - "+item.getPrice());
					writer.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Get a reference to a collection of all the items in
	 * this player owned shop. If an item slot is empty it
	 * will be shown as {@code null}, else as a {@link Item}
	 * instance.
	 * @return The array of items in this player owned shop.
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * Set a new array of items to represent the collection
	 * of all items in this player owned shop. If an item
	 * slot is empty it must be shown as {@code null}, else
	 * as a {@link Item} instance.
	 * @param items The new array of items for this shop.
	 */
	public void setItems(Item[] items) {
		this.items = items;
	}

	/**
	 * Get the reference to the player instance of the owner
	 * of this shop. It is important to notice that with this
	 * reference the player instance can refer to an offline
	 * player. If you would like to gain access to the player
	 * owning this shop while this player is online or offline
	 * use the {@link #accessOwner(boolean)} method instead.
	 * @return A reference to the player owning this shop.
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static class Item {
		
		private int id;
		
		private int amount;
		
		private int price;

		public Item(int id) {
			this(id, 1);
		}
		
		public Item(int id, int amount) {
			this.id = id;
			this.amount = amount;
		}
		
		public Item(int id, int amount, int price) {
			this(id, amount);
			this.price = price;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		public ItemDefinition getDefinition() {
			return ItemDefinition.forId(id);
		}

	}

	
}