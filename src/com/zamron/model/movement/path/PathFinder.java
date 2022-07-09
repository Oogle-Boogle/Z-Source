package com.zamron.model.movement.path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zamron.model.Position;
import com.zamron.world.clip.region.RegionClipping;
import com.zamron.world.entity.impl.Character;

public class PathFinder {

	private static final int DEFAULT_PATH_LENGTH = 4000;
	
	public static boolean accessable(int x, int y, int z, int destX, int destY) {
		Position p = new Position(x, y, z);
		if (destX == p.getLocalX() && destY == p.getLocalY()) {
			return false;
		}

		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];

		List<Integer> tileQueueX = new ArrayList<Integer>(10000);
		List<Integer> tileQueueY = new ArrayList<Integer>(10000);

		int curX = p.getLocalX();
		int curY = p.getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 1;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);

		final int regionX = p.getRegionX() << 3;
		final int regionY = p.getRegionY() << 3;

		destX = destX - regionX;
		destY = destY - regionY;

		while (tail != tileQueueX.size()
				&& tileQueueX.size() < DEFAULT_PATH_LENGTH) {

			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);

			int curAbsX = regionX + curX;
			int curAbsY = regionY + curY;

			if (curX == destX && curY == destY) {
				return true;
			}

			tail = (tail + 1) % DEFAULT_PATH_LENGTH;

			int thisCost = cost[curX][curY] + 1 + 1;

			if (curY > 0 && via[curX][curY - 1] == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}

			if (curX > 0 && via[curX - 1][curY] == 0
					&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, z) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}

			if (curY < 104 - 1 && via[curX][curY + 1] == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && via[curX + 1][curY] == 0
					&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, z) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}

			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
					&& (RegionClipping.getClipping(curAbsX - 1, curAbsY - 1, z) & 0x128010e) == 0
					&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, z) & 0x1280108) == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}

			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
					&& (RegionClipping.getClipping(curAbsX - 1, curAbsY + 1, z) & 0x1280138) == 0
					&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, z) & 0x1280108) == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
					&& (RegionClipping.getClipping(curAbsX + 1, curAbsY - 1, z) & 0x1280183) == 0
					&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, z) & 0x1280180) == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}

			if (curX < 104 - 1 && curY < 104 - 1
					&& via[curX + 1][curY + 1] == 0
					&& (RegionClipping.getClipping(curAbsX + 1, curAbsY + 1, z) & 0x12801e0) == 0
					&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, z) & 0x1280180) == 0
					&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}

		return false;
	}
	
	public static void findPath(Character gc, int destX, int destY, boolean moveNear,
			int xLength, int yLength) {
		try {
			if (destX == gc.getPosition().getLocalX() && destY == gc.getPosition().getLocalY() && !moveNear) {
				return;
			}
			
			
			final int height = gc.getPosition().getZ() % 4;
			destX = destX - 8 * gc.getPosition().getRegionX();
			destY = destY - 8 * gc.getPosition().getRegionY();
			final int[][] via = new int[104][104];
			final int[][] cost = new int[104][104];
			final LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
			final LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
			for (int xx = 0; xx < 104; xx++)
				for (int yy = 0; yy < 104; yy++)
					cost[xx][yy] = 99999999;
			int curX = gc.getPosition().getLocalX();
			int curY = gc.getPosition().getLocalY();
			if (curX > via.length - 1 || curY > via[curX].length - 1)
				return;
			if (curX < via.length && curY < via[0].length)
				via[curX][curY] = 99;
			if (curX < cost.length && curY < cost[0].length)
				cost[curX][curY] = 0;
			@SuppressWarnings("unused")
			final int head = 0;
			int tail = 0;
			tileQueueX.add(curX);
			tileQueueY.add(curY);
			boolean foundPath = false;
			while (tail != tileQueueX.size() && tileQueueX.size() < DEFAULT_PATH_LENGTH) {
				curX = tileQueueX.get(tail);
				curY = tileQueueY.get(tail);
				final int curAbsX = gc.getPosition().getRegionX() * 8 + curX;
				final int curAbsY = gc.getPosition().getRegionY() * 8 + curY;
				if (curX == destX && curY == destY) {
					foundPath = true;
					break;
				}
				tail = (tail + 1) % DEFAULT_PATH_LENGTH;

				if (cost.length < curX || cost[curX].length < curY)
					return;
				final int thisCost = cost[curX][curY] + 1;

				if (curY > 0
						&& via[curX][curY - 1] == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
					tileQueueX.add(curX);
					tileQueueY.add(curY - 1);
					via[curX][curY - 1] = 1;
					cost[curX][curY - 1] = thisCost;
				}
				if (curX > 0
						&& via[curX - 1][curY] == 0
						&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0) {
					tileQueueX.add(curX - 1);
					tileQueueY.add(curY);
					via[curX - 1][curY] = 2;
					cost[curX - 1][curY] = thisCost;
				}
				if (curY < 104 - 1
						&& via[curX][curY + 1] == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
					tileQueueX.add(curX);
					tileQueueY.add(curY + 1);
					via[curX][curY + 1] = 4;
					cost[curX][curY + 1] = thisCost;
				}
				if (curX < 104 - 1
						&& via[curX + 1][curY] == 0
						&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0) {
					tileQueueX.add(curX + 1);
					tileQueueY.add(curY);
					via[curX + 1][curY] = 8;
					cost[curX + 1][curY] = thisCost;
				}
				if (curX > 0
						&& curY > 0
						&& via[curX - 1][curY - 1] == 0
						&& (RegionClipping.getClipping(curAbsX - 1, curAbsY - 1, height) & 0x128010e) == 0
						&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
					tileQueueX.add(curX - 1);
					tileQueueY.add(curY - 1);
					via[curX - 1][curY - 1] = 3;
					cost[curX - 1][curY - 1] = thisCost;
				}
				if (curX > 0
						&& curY < 104 - 1
						&& via[curX - 1][curY + 1] == 0
						&& (RegionClipping.getClipping(curAbsX - 1, curAbsY + 1, height) & 0x1280138) == 0
						&& (RegionClipping.getClipping(curAbsX - 1, curAbsY, height) & 0x1280108) == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
					tileQueueX.add(curX - 1);
					tileQueueY.add(curY + 1);
					via[curX - 1][curY + 1] = 6;
					cost[curX - 1][curY + 1] = thisCost;
				}
				if (curX < 104 - 1
						&& curY > 0
						&& via[curX + 1][curY - 1] == 0
						&& (RegionClipping.getClipping(curAbsX + 1, curAbsY - 1, height) & 0x1280183) == 0
						&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY - 1, height) & 0x1280102) == 0) {
					tileQueueX.add(curX + 1);
					tileQueueY.add(curY - 1);
					via[curX + 1][curY - 1] = 9;
					cost[curX + 1][curY - 1] = thisCost;
				}
				if (curX < 104 - 1
						&& curY < 104 - 1
						&& via[curX + 1][curY + 1] == 0
						&& (RegionClipping.getClipping(curAbsX + 1, curAbsY + 1, height) & 0x12801e0) == 0
						&& (RegionClipping.getClipping(curAbsX + 1, curAbsY, height) & 0x1280180) == 0
						&& (RegionClipping.getClipping(curAbsX, curAbsY + 1, height) & 0x1280120) == 0) {
					tileQueueX.add(curX + 1);
					tileQueueY.add(curY + 1);
					via[curX + 1][curY + 1] = 12;
					cost[curX + 1][curY + 1] = thisCost;
				}
			}
			if (!foundPath)
				if (moveNear) {
					int i_223_ = 1000;
					int thisCost = 100;
					final int i_225_ = 10;
					for (int x = destX - i_225_; x <= destX + i_225_; x++)
						for (int y = destY - i_225_; y <= destY + i_225_; y++)
							if (x >= 0 && y >= 0 && x < 104 && y < 104
							&& cost[x][y] < 100) {
								int i_228_ = 0;
								if (x < destX)
									i_228_ = destX - x;
								else if (x > destX + xLength - 1)
									i_228_ = x - (destX + xLength - 1);
								int i_229_ = 0;
								if (y < destY)
									i_229_ = destY - y;
								else if (y > destY + yLength - 1)
									i_229_ = y - (destY + yLength - 1);
								final int i_230_ = i_228_ * i_228_ + i_229_
										* i_229_;
								if (i_230_ < i_223_ || i_230_ == i_223_
										&& cost[x][y] < thisCost) {
									i_223_ = i_230_;
									thisCost = cost[x][y];
									curX = x;
									curY = y;
								}
							}
					if (i_223_ == 1000)
						return;
				} else {
					return;
				}
			tail = 0;
			tileQueueX.set(tail, curX);
			tileQueueY.set(tail++, curY);
			int l5;
			for (int j5 = l5 = via[curX][curY]; curX != gc.getPosition().getLocalX()
					|| curY != gc.getPosition().getLocalY(); j5 = via[curX][curY]) {
				if (j5 != l5) {
					l5 = j5;
					tileQueueX.set(tail, curX);
					tileQueueY.set(tail++, curY);
				}
				if ((j5 & 2) != 0)
					curX++;
				else if ((j5 & 8) != 0)
					curX--;
				if ((j5 & 1) != 0)
					curY++;
				else if ((j5 & 4) != 0)
					curY--;
			}
			final int size = tail--;
			int pathX = gc.getPosition().getRegionX() * 8 + tileQueueX.get(tail);
			int pathY = gc.getPosition().getRegionY() * 8 + tileQueueY.get(tail);
			gc.getMovementQueue().addFirstStep(new Position(pathX, pathY, gc.getPosition().getZ()));
			for (int i = 1; i < size; i++) {
				tail--;
				pathX = gc.getPosition().getRegionX() * 8 + tileQueueX.get(tail);
				pathY = gc.getPosition().getRegionY() * 8 + tileQueueY.get(tail);
				gc.getMovementQueue().addStep(new Position(pathX, pathY, gc.getPosition().getZ()));
			}
		} catch(Exception e) {
			e.printStackTrace();
			//System.out.println("Error finding route, destx: "+destX+", destY: "+destY+". Reseted queue.");
			gc.getMovementQueue().setFollowCharacter(null);
			gc.getMovementQueue().reset();
		}
	}

	public static void solveDiagonalBlock(Character attacker, Character target) {
		int aX = attacker.getPosition().getX();
		int aY = attacker.getPosition().getY();

		int vX = target.getPosition().getX();
		int vY = target.getPosition().getY();

		int stepX = 0;
		int stepY = 0;

		if(aX > vX) {
			stepX = 1;
		} else if(vX > aX) {
			stepX = -1;
		} else if(aY > vY) {
			stepY = 1;
		} else if(vY > aY) {
			stepY = -1;
		} else if(aX == vX && aY == vY) {
			stepY = -1;
		}

		findPath(attacker, target.getPosition().getX() + stepX, target.getPosition().getY() + stepY, false, 8, 8);
	}
}
