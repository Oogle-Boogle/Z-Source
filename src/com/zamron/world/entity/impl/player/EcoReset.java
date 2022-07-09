package com.zamron.world.entity.impl.player;

import java.io.File;

import com.zamron.model.container.impl.Bank;

/*
 * @Author Bas - Restoring Arlania w/ this file
 */

public class EcoReset {

	public static void main(String[] args) {

		/*
		 * Loading character files
		 */
		for (File file : new File("data/saves/characters/").listFiles()) {
			Player player = new Player(null);
			player.setUsername(file.getName().substring(0, file.getName().length()-5));
		
			PlayerLoading.getResult(player); //comment out line 78-81 in playerloading.java for this
			
			/*
			 * Money pouch, inventory, equipment, and dung bound items
			 */
			player.setMoneyInPouch(0);
			player.getInventory().resetItems();
			player.getEquipment().resetItems();
			player.getMinigameAttributes().getDungeoneeringAttributes().setBoundItems(new int[5]);
			
			/*
			 * Reset Bank
			 */
		for (Bank bank : player.getBanks()) {
				if (bank == null) {
					return;
				}
				bank.resetItems();
			}
			
			/*
			 * Clear pack yack / beast of burden
			 */
			if (player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().resetItems();
			}
			
			/*
			 * Reset the points
			 */
			//leave loyalty points
			player.getPointsHandler().setPkPoints(0, true);
			player.setBossPoints(0);
			player.getPointsHandler().setDonationPoints(0, true);
			player.getPointsHandler().setDungeoneeringTokens(0, true);
			player.getPointsHandler().setCustompestcontrolpoints(0, true);
			player.getPointsHandler().setPrestigePoints(0, true);
			player.getPointsHandler().setTriviaPoints(0, true);
			player.getPointsHandler().setSlayerPoints(0, true);
			player.getPointsHandler().setVotingPoints(0, true);
			
			/*
			 * Save File
			 */
			PlayerSaving.save(player);
			//System.out.println("Account Reset Successfully");
		}
	}
}
