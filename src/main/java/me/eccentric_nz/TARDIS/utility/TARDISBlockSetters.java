/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Switch;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISBlockSetters {

    private final TARDIS plugin;

    public TARDISBlockSetters(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets a block to the specified block data.
     *
     * @param l the location of the block.
     * @param d the block data to set the block to.
     */
    public static void setBlock(Location l, String d) {
        Block b = l.getBlock();
        if (b != null) {
            BlockData blockData = Bukkit.createBlockData(d);
            switch (blockData.getMaterial()) {
                case CAKE:
                    // cake -> handbrake
                    blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    blockData = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) blockData;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(blockData);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified block data.
     *
     * @param l  the location of the block.
     * @param bd the block data to set the block to.
     */
    public static void setBlock(Location l, BlockData bd) {
        Block b = l.getBlock();
        BlockData blockData;
        if (b != null) {
            switch (bd.getMaterial()) {
                case CAKE:
                    // cake -> handbrake
                    blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    blockData = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) blockData;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(bd);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified block data.
     *
     * @param l        the location of the block.
     * @param material the block type to set the block to.
     */
    public static void setBlock(Location l, Material material) {
        Block b = l.getBlock();
        BlockData blockData = material.createBlockData();
        if (b != null) {
            switch (material) {
                case CAKE:
                    // cake -> handbrake
                    blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    blockData = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) blockData;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(blockData, true);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified typeId and data.
     *
     * @param w    the world the block is in.
     * @param x    the x co-ordinate of the block.
     * @param y    the y co-ordinate of the block.
     * @param z    the z co-ordinate of the block.
     * @param data the block data to apply to the block.
     */
    public static void setBlock(World w, int x, int y, int z, BlockData data) {
        Block b = w.getBlockAt(x, y, z);
        if (b != null) {
            switch (data.getMaterial()) {
                case CAKE:
                    // cake -> handbrake
                    BlockData blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    BlockData oak = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) oak;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(data);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified block data.
     *
     * @param w    the world the block is in.
     * @param x    the x co-ordinate of the block.
     * @param y    the y co-ordinate of the block.
     * @param z    the z co-ordinate of the block.
     * @param data the block data to apply to the block.
     */
    public static void setBlock(World w, int x, int y, int z, String data) {
        Block b = w.getBlockAt(x, y, z);
        if (b != null) {
            BlockData blockData = Bukkit.createBlockData(data);
            switch (blockData.getMaterial()) {
                case CAKE:
                    // cake -> handbrake
                    blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    blockData = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) blockData;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(blockData);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified Material.
     *
     * @param w        the world the block is in.
     * @param x        the x co-ordinate of the block.
     * @param y        the y co-ordinate of the block.
     * @param z        the z co-ordinate of the block.
     * @param material the Material to apply to the block.
     */
    public static void setBlock(World w, int x, int y, int z, Material material) {
        Block b = w.getBlockAt(x, y, z);
        BlockData blockData = material.createBlockData();
        if (b != null) {
            switch (material) {
                case CAKE:
                    // cake -> handbrake
                    blockData = Material.LEVER.createBlockData();
                    Switch lever = (Switch) blockData;
                    lever.setFace(Switch.Face.FLOOR);
                    lever.setFacing(BlockFace.SOUTH);
                    b.setBlockData(lever);
                    break;
                case SPAWNER:
                    // mob spawner -> scanner button
                    blockData = Material.OAK_BUTTON.createBlockData();
                    Switch button = (Switch) blockData;
                    button.setFace(Switch.Face.WALL);
                    button.setFacing(BlockFace.SOUTH);
                    b.setBlockData(button);
                    break;
                default:
                    b.setBlockData(blockData);
                    break;
            }
        }
    }

    /**
     * Sets a block to the specified type and data and remembers its location and block data.
     *
     * @param w    the world the block is in.
     * @param x    the x co-ordinate of the block.
     * @param y    the y co-ordinate of the block.
     * @param z    the z co-ordinate of the block.
     * @param data the block data to apply to the block.
     * @param id   the TARDIS this block belongs to.
     */
    public static void setBlockAndRemember(World w, int x, int y, int z, BlockData data, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(TARDIS.plugin);
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", l);
        set.put("data", b.getBlockData().getAsString());
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
        // set the block
        b.setBlockData(data);
    }

    /**
     * Sets a block to the specified type and data and remembers its location and block data.
     *
     * @param w    the world the block is in.
     * @param x    the x co-ordinate of the block.
     * @param y    the y co-ordinate of the block.
     * @param z    the z co-ordinate of the block.
     * @param data the block data to apply to the block.
     * @param id   the TARDIS this block belongs to.
     */
    public static void setBlockAndRemember(World w, int x, int y, int z, String data, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(TARDIS.plugin);
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", l);
        set.put("data", b.getBlockData().getAsString());
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
        // set the block
        BlockData blockData = Bukkit.createBlockData(data);
        b.setBlockData(blockData);
    }

    /**
     * Sets a block to the specified type and data and remembers its location and block data.
     *
     * @param w        the world the block is in.
     * @param x        the x co-ordinate of the block.
     * @param y        the y co-ordinate of the block.
     * @param z        the z co-ordinate of the block.
     * @param material the material to set the block.
     * @param id       the TARDIS this block belongs to.
     */
    public static void setBlockAndRemember(World w, int x, int y, int z, Material material, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(TARDIS.plugin);
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", l);
        set.put("data", b.getBlockData().getAsString());
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
        // set the block
        b.setBlockData(material.createBlockData());
    }

    /**
     * Sets a block to the specified type and data and remembers its location and block data.
     *
     * @param b    the block to set and remember
     * @param m    the typeId to set the block to
     * @param id   the TARDIS this block belongs to
     * @param type the police_box type (0 = interior, 1 = police box, 2 = beacon up block)
     */
    public static void setBlockAndRemember(Block b, Material m, int id, int type) {
        // save the block location so that we can restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", l);
        BlockData data = b.getBlockData();
        set.put("data", data.getAsString());
        set.put("police_box", type);
        new QueryFactory(TARDIS.plugin).doInsert("blocks", set);
        // set the block
        b.setBlockData(m.createBlockData(), true);
    }

    /**
     * Sets the block under the TARDIS Police Box door to the specified typeId and data and remembers the block for
     * replacement later on.
     *
     * @param w      the world the block is in.
     * @param x      the x coordinate of the block.
     * @param y      the y coordinate of the block.
     * @param z      the z coordinate of the block.
     * @param id     the TARDIS this block belongs to.
     * @param portal whether a chest can be in the portal block location
     */
    public static void setUnderDoorBlock(World w, int x, int y, int z, int id, boolean portal) {
        // List of blocks that a door cannot be placed on
        List<String> ids = TARDIS.plugin.getBlocksConfig().getStringList("under_door_blocks");
        if (portal) {
            ids.remove("CHEST");
        }
        Block b = w.getBlockAt(x, y, z);
        String blockData = b.getBlockData().getAsString();
        if (ids.contains(b.getBlockData().getMaterial().toString())) {
            // remember replaced block location and BlockData so we can restore it later
            String l = b.getLocation().toString();
            QueryFactory qf = new QueryFactory(TARDIS.plugin);
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("location", l);
            set.put("data", blockData);
            set.put("police_box", 1);
            qf.doInsert("blocks", set);
            TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
            // set the block
            b.setBlockData(Material.BARRIER.createBlockData());
        }
    }
}
