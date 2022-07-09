package com.zamron.tools;

import java.io.File;

/**
 * Deletes all of the files that DropBox has copied while syncing.
 * 
 * @author Andys1814.
 */
public class ConflictedCopyRemover {

	/**
	 * The main method which will run upon execution of this class.
	 * 
	 * @param args
	 *            The arguments cast on runtime.
	 */
	public static void main(String[] args) {
		clean(new File("./"));
	}

	/**
	 * Cleans the specified director of all the conflicted copies.
	 * 
	 * @param dir
	 *            The directory file to clean.
	 */
	private static void clean(File dir) {
		int count = 0;
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				clean(f);
				continue;
			}
			if (f.getName().contains("conflicted ")) {
				if (f.delete()) {
					//System.out.println("Removed " + f.getName() + "!");
					count++;
				}
			}
			//System.out.println("Removed " + count + " total conflicted copies within this project.");
		}
	}

}
