/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayItemUtils {

    /**
     * Get a TARDISDisplayItem from a display entity
     *
     * @param display the display entity to get the data from
     * @return The TARDISDisplayItem that matches this entity isn't one
     */
    public static TARDISDisplayItem get(ItemDisplay display) {
        ItemStack is = display.getItemStack();
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            if (im.hasCustomModelData()) {
                return TARDISDisplayItem.getByMaterialAndData(is.getType(), im.getCustomModelData());
            }
        }
        return null;
    }

    /**
     * Get an item display entity from a block. Used for custom TARDIS blocks
     * and lights.
     *
     * @param block the block to use as the search location
     * @return The Item Display entity at the block's location or null if there
     * isn't one
     */
    public static ItemDisplay get(Block block) {
        for (Entity e : block.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                return display;
            }
        }
        return null;
    }

    /**
     * Get an item display entity from a light block. Used for custom TARDIS
     * lights. Light blocks don't have a bounding box, so we need to create our
     * own from the block's location.
     *
     * @param block the light block to use as the search location
     * @return The Item Display entity at the block's location or null if there
     * isn't one
     */
    public static ItemDisplay getFromLight(Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();
        BoundingBox box = new BoundingBox(x, y, z, x + 1, y + 1, z + 1);
        for (Entity e : block.getWorld().getNearbyEntities(box.expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                return display;
            }
        }
        return null;
    }

    /**
     * Get an interaction entity from a block. Used for custom TARDIS blocks and
     * lights.
     *
     * @param block the block to use as the search location
     */
    public static void remove(Block block) {
        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(0.5d, 0.5d, 0.5d), 0.55d, 0.55d, 0.55d,
                (d) -> d.getType() == EntityType.INTERACTION || d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof Interaction || e instanceof ItemDisplay) {
                e.remove();
            }
        }
    }

    /**
     * Get an item display entity from an Interaction entity. Used for Chemistry
     * lamp blocks
     *
     * @param interaction the Interaction entity to use as the search location
     * @return The Item Display entity at the Interaction location or null if
     * there isn't one
     */
    public static ItemDisplay get(Interaction interaction) {
        for (Entity e : interaction.getWorld().getNearbyEntities(interaction.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                return display;
            }
        }
        return null;
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi the TARDISDisplayItem to determine the ItemStack to display
     * @param world the world to spawn the entity in
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public static void set(TARDISDisplayItem tdi, World world, int x, int y, int z) {
        // spawn an item display entity
        ItemStack is = new ItemStack(tdi.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tdi.getDisplayName());
        im.setCustomModelData(tdi.getCustomModelData());
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
        is.setItemMeta(im);
        Location l = new Location(world, x + 0.5d, y + 0.5d, z + 0.5d);
        ItemDisplay display = (ItemDisplay) world.spawnEntity(l, EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setPersistent(true);
        display.setInvulnerable(true);
        display.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi the TARDISDisplayItem to determine the ItemStack to display
     * @param block the block location to spawn the entity at
     */
    public static void set(TARDISDisplayItem tdi, Block block) {
        // spawn an item display entity
        if (tdi.isLight()) {
            Levelled light = TARDISConstants.LIGHT;
            light.setLevel(tdi.isLit() ? 15 : 0);
            block.setBlockData(light);
            // also set an interaction entity
            Interaction interaction = (Interaction) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
            interaction.setResponsive(true);
            interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
            interaction.setPersistent(true);
        } else if (tdi != TARDISDisplayItem.ARTRON_FURNACE && tdi != TARDISDisplayItem.SONIC_GENERATOR) {
            block.setBlockData(TARDISConstants.BARRIER);
        }
        ItemStack is = new ItemStack(tdi.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tdi.getDisplayName());
        if (tdi.getCustomModelData() != -1) {
            im.setCustomModelData(tdi.getCustomModelData());
        }
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setPersistent(true);
        display.setInvulnerable(true);
        display.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
        if (tdi == TARDISDisplayItem.ARTRON_FURNACE) {
            display.setBrightness(new Display.Brightness(15, 15));
        }
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi the TARDISDisplayItem to determine the ItemStack to display
     * @param block the block location to spawn the entity at
     * @param im the ItemMeta to set on the display ItemStack
     */
    public static void setSeed(TARDISDisplayItem tdi, Block block, ItemMeta im) {
        block.setBlockData(TARDISConstants.BARRIER);
        ItemStack is = new ItemStack(tdi.getMaterial(), 1);
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setPersistent(true);
        display.setInvulnerable(true);
    }

    /**
     * Spawn an Interaction entity
     *
     * @param location the location to spawn the entity at
     * @param cmd the custom model data to set for the custom block key
     * associated with the entity
     */
    public static void set(Location location, int cmd) {
        // spawn an interaction entity
        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location.clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
        interaction.setResponsive(true);
        interaction.setPersistent(true);
        interaction.setInvulnerable(true);
    }

    /**
     * Remove item display entities in a chunk
     *
     * @param chunk the chunk to search for entities
     */
    public static void removeDisplaysInChunk(Chunk chunk, int lower, int upper) {
        for (Entity entity : chunk.getEntities()) {
            if ((entity instanceof ItemDisplay || entity instanceof Interaction)
                    && entity.getPersistentDataContainer().has(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                int y = entity.getLocation().getBlockY();
                if (y >= lower && y <= upper) {
                    entity.remove();
                }
            }
        }
    }
}
