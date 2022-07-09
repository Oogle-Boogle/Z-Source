package com.zamron.tools;

import java.io.File;
import java.io.IOException;

/**
 * Handles functionality to create a backup of the player files.
 *
 * @author Andys1814.
 */
public final class PlayerBackup {

	/**
	 * Represents the directory for which this class will backup the contents
	 * of.
	 */
	private static final File FROM_DIRECTORY = new File("data/saves/characters");

	private static final File TO_DIRECTORY = new File("data/backups");

	@SuppressWarnings("unused")
	private static void runBackup() {
		if (!FROM_DIRECTORY.exists()) {
			//System.out.println("Source file not found.");
		}
		
		if (!TO_DIRECTORY.exists()) {
			try {
				TO_DIRECTORY.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {

	}

}
