package com.zamron.world.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {

	static List<Integer> ints = Arrays.asList(2984, 2981, 2981, 2981, 2984);
	/**
	 * Results for player1: [2986, 2986, 2982, 2985, 2984]
Results for player2: [2980, 2983, 2985, 2986, 2983]
	 * @param args
	 */

	public static void main(String[] args) {

		String pair = getResult(ints);

		//System.out.println("Total pairs: " + pair);
	}

	static int pairs = 0;
	static int triple = 0;
	static int quad = 0;
	static int five = 0;

	private static String getResult(List<Integer> ints) {
		String result = "none";

		Map<Integer, Integer> frequencies = new HashMap<>();

		for (Integer anInt : ints) {
			frequencies.merge(anInt, 1, Integer::sum);
		}

		int pairs = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 2).count();
		int triple = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 3).count();
		int quad = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 4).count();
		int five = (int) frequencies.entrySet().stream().filter(value -> value.getValue() == 5).count();

		if (five != 0)
			result = "Five of a kind!";
		else if (quad != 0)
			result = "Four of a kind!";
		else if (pairs == 1 && triple != 0)
			result = "Full house!";
		else if (pairs != 2 && triple != 0)
			result = "Three of a kind!";
		else if (pairs == 2)
			result = "Two pairs!";
		else if (pairs == 1)
			result = "One pair!";
		else
			result = "No pair";
		//System.out.println("List elements: " + ints);
		//System.out.println("List result(String): " + result);
		
		//System.out.println("Pairs: " + pairs);
		//System.out.println("Triples: " + triple);

		return result;
	}
}
