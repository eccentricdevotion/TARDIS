/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBlockSetters {

    private final TARDIS plugin;

    public TARDISBlockSetters(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets a block to the specified typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     */
    @SuppressWarnings("deprecation")
    public static void setBlock(World w, int x, int y, int z, int m, byte d) {
        final Block b = w.getBlockAt(x, y, z);
        if (m < 0) {
            m += 256;
        }
        if (m == 92) { //cake -> handbrake
            m = 69;
            d = (byte) 5;
        }
        if (m == 52) { //mob spawner -> scanner button
            m = 143;
            d = (byte) 3;
        }
        if (b != null) {
            b.setTypeId(m);
            b.setData(d, true);
        }
    }

    /**
     * Sets a block to the specified typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the material to set the block to.
     * @param d the data bit to set the block to.
     */
    @SuppressWarnings("deprecation")
    public static void setBlock(World w, int x, int y, int z, Material m, byte d) {
        final Block b = w.getBlockAt(x, y, z);
        if (m.equals(Material.CAKE_BLOCK)) { //cake -> handbrake
            m = Material.LEVER;
            d = (byte) 5;
        }
        if (m.equals(Material.MOB_SPAWNER)) { //mob spawner -> scanner button
            m = Material.WOOD_BUTTON;
            d = (byte) 3;
        }
        if (b != null) {
            b.setType(m);
            b.setData(d, true);
        }
    }

    /**
     * Sets a block to the specified type and data and remembers its location,
     * typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     */
    @SuppressWarnings("deprecation")
    public void setBlockAndRemember(World w, int x, int y, int z, int m, byte d, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("tardis_id", id);
        set.put("location", l);
        int bid = b.getTypeId();
        byte data = b.getData();
        set.put("block", bid);
        set.put("data", data);
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
        // set the block
        b.setTypeId(m);
        b.setData(d, true);
    }

    /**
     * Sets a block to the specified type and data and remembers its location,
     * typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     */
    @SuppressWarnings("deprecation")
    public void setBlockAndRemember(World w, int x, int y, int z, Material m, byte d, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("tardis_id", id);
        set.put("location", l);
        String mat = b.getType().toString();
        byte data = b.getData();
        set.put("block", mat);
        set.put("data", data);
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
        // set the block
        b.setType(m);
        b.setData(d, true);
    }

    /**
     * Sets the block under the TARDIS Police Box door to the specified typeId
     * and data and remembers the block for replacement later on.
     *
     * @param w the world the block is in.
     * @param x the x coordinate of the block.
     * @param y the y coordinate of the block.
     * @param z the z coordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     * @param portal whether a chest can be in the portal block location
     */
    @SuppressWarnings("deprecation")
    public void setUnderDoorBlock(World w, int x, int y, int z, int m, byte d, int id, boolean portal) {
        // List of blocks that a door cannot be placed on
        List<Integer> ids = plugin.getBlocksConfig().getIntegerList("under_door_blocks");
        if (portal) {
            ids.remove(Integer.valueOf(54));
        }
        Block b = w.getBlockAt(x, y, z);
        int bid = b.getTypeId();
        if (ids.contains(bid)) {
            // remember replaced block location, TypeId and Data so we can restore it later
            String l = b.getLocation().toString();
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("tardis_id", id);
            set.put("location", l);
            set.put("block", bid);
            set.put("data", b.getData());
            set.put("police_box", 1);
            qf.doInsert("blocks", set);
            plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
            // set the block
            b.setTypeId(m);
            b.setData(d, true);
        }
    }
}
