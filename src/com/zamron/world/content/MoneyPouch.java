package com.zamron.world.content;

import com.zamron.model.Locations.Location;
import com.zamron.model.container.impl.Bank;
import com.zamron.model.definitions.ItemDefinition;
import com.zamron.util.Misc;
import com.zamron.world.entity.impl.player.Player;

import java.text.NumberFormat;

/**
 * Handles the money pouch
 * @author Goml
 * Perfected by Gabbe
 */
public class MoneyPouch {


	public static String formatNumber(long number) {
		return NumberFormat.getInstance().format(number);
	}
	
	public static void refresh(Player player) {
		player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
	}
	
	public static void removeTaxbags(Player player, long amount) {
		player.setMoneyInPouch(player.getMoneyInPouch() - amount);
		MoneyPouch.refresh(player);	
	}

	/**
	 * Deposits tax bags into the money pouch
	 * @param amount How many Taxbags to deposit
	 * @return true Returns true if transaction was successful
	 * @return false Returns false if transaction was unsuccessful
	 */
	public static boolean depositTaxBag(Player plr, int amount, boolean fromTaxBag, int itemID) {
		if(amount <= 0)
			return false;
		if(plr.getInterfaceId() > 0) {
			plr.getPacketSender().sendMessage("Please close the interface you have open before opening another one.");
			return false;
		}
		if(plr.getConstitution() <= 0) {
			plr.getPacketSender().sendMessage("You cannot do this while dying.");
			return false;
		}
		if(plr.getLocation() == Location.WILDERNESS) {
			plr.getPacketSender().sendMessage("You cannot do this here.");
			return false;
		}
		if ((itemID == 10835) && validateTaxBagQuantity(plr, amount, fromTaxBag)) {
			long addedMoney = (long)plr.getMoneyInPouch() + (long)amount;
			if (addedMoney > Long.MAX_VALUE) {
				long canStore = Long.MAX_VALUE - plr.getMoneyInPouch();
				if (!fromTaxBag)
				     plr.getInventory().delete(10835, (int)canStore);
				plr.setMoneyInPouch(plr.getMoneyInPouch() + canStore);
				plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
				plr.getPacketSender().sendMessage("You've added "+canStore + " 1b coins to your money pouch.");
				return true;
			} else {
				if (!fromTaxBag)
				    plr.getInventory().delete(10835, amount);
				plr.setMoneyInPouch(plr.getMoneyInPouch() + amount);
				plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
				plr.getPacketSender().sendMessage("You've added "+formatNumber(amount)+" 1b coins to your money pouch.");
				return true;
			}
		} else if ((itemID == 995) && validateAmount(plr, amount, fromTaxBag)) {
			int b = 1000000000;
			int billsToAdd = 0;
			if (amount < b) {
				billsToAdd = 0;
				plr.getPacketSender().sendMessage("You can only deposit in 1b lump sums.");
				return false;
			} else if (amount < b*2) {
				billsToAdd = 1;
			} else {
				billsToAdd = 2;
			}

			//System.out.println("Bills to add: "+billsToAdd);
				if (!fromTaxBag)
					plr.getInventory().delete(995, billsToAdd*b);
				plr.setMoneyInPouch(plr.getMoneyInPouch() + (billsToAdd));
				plr.getPacketSender().sendString(8135, ""+plr.getMoneyInPouch());
				plr.getPacketSender().sendMessage("You've added "+formatNumber(billsToAdd*b)+" GP to your money pouch.");
				return true;
			}
		 else {
			plr.getPacketSender().sendMessage("You do not seem to have "+formatNumber(amount)+" "+ ItemDefinition.forId(itemID).getName() + " in your inventory!");
			return false;
		}
	}

	public static void withdrawMoney(Player player, long amount) {
		//System.out.println("amount was(pouch): " + amount);
		if(amount <= 0)
			return;
		if(player.getMoneyInPouch() <= 0) {
			player.getPacketSender().sendMessage("Your money pouch is empty.");
			return;
		}
		
		if(amount > player.getMoneyInPouch())
			amount = player.getMoneyInPouch();
		
		if(amount < 1 && player.getInventory().getAmount(10835) < Integer.MAX_VALUE - amount) {
			if(player.getMoneyInPouch() >= amount) {
				player.setMoneyInPouch(player.getMoneyInPouch() - amount);
				player.getInventory().add(10835, (int)amount);
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
				player.sendMessage("You withdraw " + formatNumber(amount) + " 1b coins from your pouch.");
				return;
			}
		}
		
		if(amount >= 1 && player.getMoneyInPouch() >= amount) {
			
			long remainder = amount % 1;
			
			long totalInv = (remainder + player.getInventory().getAmount(10835));
			
			if(totalInv > Integer.MAX_VALUE) {
				player.sendMessage("@red@You cannot hold that much cash in ur inventory - add some of the cash to bank or pouch");
				return;
			}
			
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			long convertedAmount = (amount - remainder) / 1;
			player.getInventory().add(10835, (int)convertedAmount);
			
			player.getInventory().add(10835, (int)remainder);
			
			player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
			
			player.sendMessage("You withdraw " + Misc.formatNumber(amount) + " From your pouch");
			
		}
		
	}

	public static void toBank(Player player) {
		if(!player.isBanking() || player.getInterfaceId() != 5292)
			return;
		
		if(player.getMoneyInPouch() == 0) {
			player.getPacketSender().sendMessage("You money pouch is empty.");
			return;
		}
		
		int amount = player.getMoneyInPouchAsInt();
		int bankAmt = player.getBank(Bank.getTabForItem(player, 10835)).getAmount(10835);
		int totalAmount = bankAmt+amount;
		player.setCurrentBankTab(Bank.getTabForItem(player, 10835));
		if(player.getBank(player.getCurrentBankTab()).getFreeSlots() <= 0 && !player.getBank(player.getCurrentBankTab()).contains(10835)) {
			player.getPacketSender().sendMessage("Your bank is currently full.");
			return;
		}
		if(totalAmount > Integer.MAX_VALUE || totalAmount < 0) {
			int canWithdraw = (Integer.MAX_VALUE - player.getBank(Bank.getTabForItem(player, 10835)).getAmount(10835));
			if(canWithdraw <= 0) {
				player.getPacketSender().sendMessage("You cannot withdraw more money into your bank.");
				return;
			}
			player.setMoneyInPouch(player.getMoneyInPouch() - canWithdraw);
			player.getBank(Bank.getTabForItem(player, 10835)).add(10835, canWithdraw);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			player.getPacketSender().sendMessage("You could only withdraw "+canWithdraw+" 1b coins.");
		} else {
			player.getBank(player.getCurrentBankTab()).add(10835, amount);
			player.setMoneyInPouch(player.getMoneyInPouch() - amount);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
		}
	}

	/**
	 * Validates that the player has the Taxbags in their inventory
	 * @param amount The amount the player wishes to insert
	 * @return true Returns true if the player has the Taxbags in their inventory
	 * @return false Returns false if the player does not have the Taxbags in their inventory
	 */
	private static boolean validateTaxBagQuantity(Player plr, int amount, boolean fromPouch) {
		return plr.getInventory().getAmount(10835) >= amount || fromPouch;
	}

	/**
	 * Validates that the player has the coins in their inventory
	 *
	 * @param amount The amount the player wishes to insert
	 * @return false Returns false if the player does not have the coins in their inventory
	 */
	private static boolean validateAmount(Player plr, int amount, boolean fromPouch) {
		return plr.getInventory().getAmount(995) >= amount || fromPouch;
	}

}