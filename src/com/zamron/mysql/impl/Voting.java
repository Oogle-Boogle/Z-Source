/*package com.platinum.mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.platinum.world.World;
import com.platinum.world.content.dialogue.DialogueManager;
import com.platinum.world.entity.impl.player.Player;


public class Voting implements Runnable {

	public static final String HOST = "160.153.129.227";
	public static final String USER = "Fvoting2021";
	public static final String PASS = "Mitchell12";
	public static final String DATABASE = "Fvoting123";

	private Player player;
	private Connection conn;
	private Statement stmt;

	public Voting(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			if (!connect(HOST, DATABASE, USER, PASS)) {
				return;
			}

			String name = player.getUsername().replace(" ", "_");
			ResultSet rs = executeQuery("SELECT * FROM votes WHERE username='"+name+"' AND claimed=0 AND voted_on != -1");
			boolean claimedAnything = false;
			while (rs.next()) {
				String ipAddress = rs.getString("ip_address");
				int siteId = rs.getInt("site_id");

				//item that play gets when voting, vote point and a ticket. You can change below.
				player.getInventory().add(19670, 1);
				claimedAnything = true;

				//System.out.println("[FoxVote] Vote claimed by "+name+". (sid: "+siteId+", ip: "+ipAddress+")");

				rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
				rs.updateRow();
			}
			if(claimedAnything) {
				World.sendFilteredMessage("<col=089915><shad=1>" + player.getUsername()
				+ "  Thank you for the supporting the server by voting!");
				player.sendMessage("<col=008FB2>Your Voting reward has been added to your inventory.");
			} else {
				DialogueManager.sendStatement(player, "No votes have been found.");
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean connect(String host, String database, String user, String pass) {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
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
        } catch(Exception e) {
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