/*package com.platinum.mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.platinum.world.World;
import com.platinum.world.entity.impl.player.Player;


public class Donation implements Runnable {

	public static final String HOST = "160.153.153.163"; // website ip address
	public static final String USER = "platinum_d_admin";
	public static final String PASS = "G=rtY&+#^Rm3";
	public static final String DATABASE = "platinum_donate";
															

	private Player player;
	private Connection conn;
	private Statement stmt;


	public Donation(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			player.lastDonationClaim = System.currentTimeMillis() + 30000;
			String name = player.getUsername().replace("_", " ");
			ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='" + name + "' AND status='Completed' AND claimed=0");

			while (rs.next()) {
				int item_number = rs.getInt("item_number");
				@SuppressWarnings("unused")
				double paid = rs.getDouble("amount");
				int quantity = rs.getInt("quantity");

				switch (item_number) {// add products according to their ID in
										// the ACP

				case 41: // example
					player.getInventory().add(19936, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;

				/*case 42: // example
					player.getInventory().add(19937, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;*/

				/*case 43: // example
					player.getInventory().add(19938, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 44: // example
					player.getInventory().add(12162, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 45: // example
					player.getInventory().add(12164, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 46: // example
					player.getInventory().add(6192, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 47: // example
					player.getInventory().add(19821, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 48: // example
					player.getInventory().add(3918, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 49: // example
					player.getInventory().add(4803, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 50: // example
					player.getInventory().add(5197, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 51: // example
					player.getInventory().add(19886, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 52: // example
					player.getInventory().add(15374, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 53: // example
					player.getInventory().add(15566, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 54: // example
					player.getInventory().add(5170, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 55: // example
					player.getInventory().add(773, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;
					
				case 56: // example
					player.getInventory().add(774, quantity);
					player.sendMessage("Thanks for donating!");
					World.sendFilteredMessage("[<img=12>][@red@Donation@bla@] @red@" + player.getUsername()
							+ "@bla@ has just donated Thanks for @red@Contributing@bla@!");
					break;



					default:
						player.sendMessage("No donations was found under your name.");
						break;
				}
				rs.updateInt("claimed", 1);
				rs.updateRow();
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			//System.out.println("Failing connecting to database!");
			return false;
		}
	}


	public void destroy() {
		try {
			conn.close();
			conn = null;
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int executeUpdate(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			int results = stmt.executeUpdate(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}


	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}*/
