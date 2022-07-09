package com.zamron.world.content.partyroom;

import java.util.ArrayList;

import com.zamron.model.Position;
import com.zamron.world.clip.region.RegionClipping;
import com.zamron.world.entity.impl.player.Player;

public class PartyRoomBoundary {

    /**
     * The start of the boundary
     */
    private Position a;

    /**
     * The finish of the boundary
     */
    private Position b;

    /**
     * Represents a boundary
     *
     * @param a the a position
     * @param b the b position
     */
    public PartyRoomBoundary(Position a, Position b) {
        this.setA(a);
        this.setB(b);
    }

    /**
     * Gets the a
     *
     * @return the a
     */
    public Position getA() {
        return a;
    }

    /**
     * Sets the a
     *
     * @param a the a
     */
    public void setA(Position a) {
        this.a = a;
    }

    /**
     * Gets the b
     *
     * @return the b
     */
    public Position getB() {
        return b;
    }

    /**
     * Sets the b
     *
     * @param b the b
     */
    public void setB(Position b) {
        this.b = b;
    }

    /**
     * Checking if player is inside boundary
     *
     * @param  player the player
     * @param  x      the extra x
     * @param  y      the extra y
     * @return        inside
     */
    public boolean inside(Player player, int x, int y) {
        return (player.getPosition().getX() >= a.getX() - x && player.getPosition().getX() <= b.getX() + x
            && player.getPosition().getY() >= a.getY() - y && player.getPosition().getY() <= b.getY() + y)
            && player.getPosition().getZ() >= a.getZ() && player.getPosition().getZ() <= b.getZ();
    }

    /**
     * Checking if player is inside boundary
     *
     * @param  player the player
     * @param  radius the radius
     * @return        inside
     */
    public boolean inside(Player player, int radius) {
        return inside(player, radius, radius);
    }

    /**
     * Checking if player is inside boundary
     *
     * @param  player the player
     * @return        inside
     */
    public boolean inside(Player player) {
        return inside(player, 0);
    }

    /**
     * Whether inside a selection of boundaries
     *
     * @param  player   the player
     * @param  boundary the list of boundaries
     * @return          inside
     */
    public static boolean inside(Player player, ArrayList<PartyRoomBoundary> boundary) {
        for (PartyRoomBoundary b : boundary) {
            if (b.inside(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adding clipping to a boundary
     */
    public void addClipping() {
        for (int x = a.getX(); x <= b.getX(); x++) {
            for (int y = a.getY(); y <= b.getY(); y++) {
                RegionClipping.addClipping(x, y, a.getZ(), RegionClipping.BLOCKED_TILE);
            }
        }
    }

    /**
     * Removing clipping from a boundary
     */
    public void removeClipping() {
        for (int x = a.getX(); x <= b.getX(); x++) {
            for (int y = a.getY(); y <= b.getY(); y++) {
                RegionClipping.removeClipping(x, y, a.getZ(), 0x000000);
            }
        }
    }

    /**
     * Gets all the positions within a boundary
     * 
     * @return the positions
     */
    public ArrayList<Position> getAllPositionsWithinBoundary() {
        ArrayList<Position> positions = new ArrayList<Position>();
        for (int x = a.getX(); x <= b.getX(); x++) {
            for (int y = a.getY(); y <= b.getY(); y++) {
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }

}
