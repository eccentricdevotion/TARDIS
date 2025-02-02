/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database.data;

public class Destination {

    private final int dest_id;
    private final int tardis_id;
    private final String dest_name;
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final String direction;
    private final String preset;
    private final int type;
    private final boolean submarine;
    private final int slot;
    private final String icon;

    public Destination(int dest_id, int tardis_id, String dest_name, String world, int x, int y, int z, String direction, String preset, int type, boolean submarine, int slot, String icon) {
        this.dest_id = dest_id;
        this.tardis_id = tardis_id;
        this.dest_name = dest_name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.preset = preset;
        this.type = type;
        this.submarine = submarine;
        this.slot = slot;
        this.icon = icon;
    }

    public int getDest_id() {
        return dest_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getDest_name() {
        return dest_name;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getDirection() {
        return direction;
    }

    public String getPreset() {
        return preset;
    }

    public int getType() {
        return type;
    }

    public boolean isSubmarine() {
        return submarine;
    }

    public int getSlot() {
        return slot;
    }

    public String getIcon() {
        return icon;
    }
}
