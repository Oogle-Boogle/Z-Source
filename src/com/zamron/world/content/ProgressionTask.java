package com.zamron.world.content;

public class ProgressionTask {
	
	private String progressionName;
	private String[] taskNames;
	private int[] taskAmounts;

	public ProgressionTask(String progressionName, String[] names, int[] amounts) {
		this.progressionName = progressionName;
		this.taskNames = names;
		this.taskAmounts = amounts;
	}
	
	public String getProgressionName() {
		return progressionName;
	}

	public String getName(int index) {
		return taskNames[index];
	}

	public int getAmount(int index) {
		return taskAmounts[index];
	}

	private int[] progress = new int[3];

	public int getProgress(int index) {
		return progress[index];
	}

	public void setProgress(int index, int amount) {
		this.progress[index] = amount;
	}

	public void incrementProgress(int index, int amount) {
		this.progress[index] += amount;
	}

	public boolean isCompleted(int index) {
		return progress[index] >= getAmount(index);
	}
	
	public boolean hasCompleted = false;
	
	public boolean allCompleted() {
		return isCompleted(0) && isCompleted(1) && isCompleted(2);
	}

}
