package com.zamron.mysql;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author Gabriel Hannason
 */
public class MySQLDatabase {

	public boolean active;
	public int connectionAttempts;
	public String host;
	public int port;
	public String database;
	public String username;
	public String password;
	private Connection connection;
	
	public MySQLDatabase(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.active = true;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void createConnection() {
		try {
			setConnection(DriverManager.getConnection("jdbc:mysql://"+ host + ":" + port+ "/" + database, username, password));
			connectionAttempts = 0;
		} catch (Exception e) {
			System.out.println("Unable to create connection to database "+database+"!");
			connectionAttempts++;
			e.printStackTrace();
		}
	}

	public void restart() {
		this.connectionAttempts = 0;
		this.active = true;
	}
}