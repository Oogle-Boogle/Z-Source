package com.zamron.world.content;

import com.zamron.model.Position;

public class PlsHelpMe {

	public static void main(String[] args) {

		int startX = 2333;
		int startY = 3639;
		int offset = 3;

		Position[] positions = new Position[(int) Math.pow((2 * offset + 1), 2) - 1];
		//System.out.println(positions.length);

		int index = 0;

		for (int x = startX - offset; x <= startX + offset; x++) {

			for (int y = startY - offset; y <= startY + offset; y++) {

				if (x == 2333 && y == 3639) {
					continue;
				}
				positions[index] = new Position(x, y);

				index++;
			}
		}

		for (Position pos : positions) {
			//System.out.println("X: " + pos.getX() + " | Y: " + pos.getY());
		}

		//System.out.println("Index: " + index);

	}
}
