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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.HashSet;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayItemUtils {

    /**
     * Get a TARDISDisplayItem from a display entity
     *
     * @param display the display entity to get the data from
     * @return The TARDISDisplayItem that matches this entity or null if there isn't one
     */
    public static TARDISDisplayItem get(ItemDisplay display) {
        ItemStack is = display.getItemStack();
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            if (im.hasItemModel()) {
                if (Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(is.getType())) {
                    return TARDISDisplayItem.CUSTOM_DOOR;
                } else {
                    return TARDISDisplayItem.getByModel(im.getItemModel());
                }
            }
        }
        return null;
    }

    /**
     * Get an item display entity from a block. Used for custom TARDIS blocks and lights.
     *
     * @param block the block to use as the search location
     * @return The Item Display entity at the block's location or null if there isn't one
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
     * Get all item display entities from a block. Used for TARDIS variable lights.
     *
     * @param block the block to use as the search location
     * @return All the Item Display entities at the block's location or an empty set if there aren't any
     */
    public static Set<ItemDisplay> getAll(Block block) {
        Set<ItemDisplay> displays = new HashSet<>();
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();
        BoundingBox box = new BoundingBox(x, y, z, x + 1, y + 1, z + 1);
        for (Entity e : block.getWorld().getNearbyEntities(box.expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                displays.add(display);
            }
        }
        return displays;
    }

    /**
     * Get all item display entities from a block. Used for TARDIS variable lights.
     *
     * @param interaction the Interaction to use as the search location
     * @return All the Item Display entities at the interaction's location or an empty set if there aren't any
     */
    public static Set<ItemDisplay> getAll(Interaction interaction) {
        Set<ItemDisplay> displays = new HashSet<>();
        for (Entity e : interaction.getWorld().getNearbyEntities(interaction.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                displays.add(display);
            }
        }
        return displays;
    }

    /**
     * Get an item display entity from a block. Used for custom TARDIS lights and double door. Light blocks don't have a
     * bounding box, so we need to create our own from the block's location.
     *
     * @param block the block to use as the search location
     * @return The Item Display entity at the block's location or null if there isn't one
     */
    public static ItemDisplay getFromBoundingBox(Block block) {
        while (!block.getChunk().isLoaded()) {
            block.getChunk().load();
        }
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
     * Removes interaction, item display and item frame entities from a block.
     *
     * @param block the block to use as the search location
     */
    public static void remove(Block block) {
        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation().add(0.5d, 0.5d, 0.5d), 0.55d, 0.55d, 0.55d, (d) -> d.getType() == EntityType.INTERACTION || d.getType() == EntityType.ITEM_DISPLAY || d.getType() == EntityType.TEXT_DISPLAY || d.getType() == EntityType.ITEM_FRAME || d.getType() == EntityType.GLOW_ITEM_FRAME)) {
            if (e instanceof Interaction || e instanceof ItemDisplay || e instanceof TextDisplay || e instanceof ItemFrame) {
                e.remove();
            }
        }
    }

    /**
     * Get an item display entity from an Interaction entity. Used for Chemistry lamp blocks
     *
     * @param interaction the Interaction entity to use as the search location
     * @return The Item Display entity at the Interaction location or null if there isn't one
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
     * Get an item display entity from an Interaction entity. Used for console sonic docks.
     *
     * @param interaction the Interaction entity to use as the search location
     * @return The Item Display entity at the Interaction location or null if there isn't one
     */
    public static ItemDisplay getSonic(Interaction interaction) {
        for (Entity e : interaction.getWorld().getNearbyEntities(interaction.getBoundingBox().expand(0.75d), (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
            if (e instanceof ItemDisplay display) {
                ItemStack is = display.getItemStack();
                if (TARDISStaticUtils.isSonic(is)) {
                    return display;
                }
            }
        }
        return null;
    }

    /**
     * Get a text display entity from an Interaction entity. Used for console sonic docks.
     *
     * @param interaction the Interaction entity to use as the search location
     * @return The Text Display entity at the Interaction location or null if there isn't one
     */
    public static TextDisplay getText(Interaction interaction) {
        for (Entity e : interaction.getWorld().getNearbyEntities(interaction.getBoundingBox().expand(0.1d), (d) -> d.getType() == EntityType.TEXT_DISPLAY)) {
            if (e instanceof TextDisplay display) {
                return display;
            }
        }
        return null;
    }

    public static Interaction getInteraction(Location location) {
        while (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.5d, 3d, 1.5d, (d) -> d.getType() == EntityType.INTERACTION)) {
            if (e instanceof Interaction interaction) {
                return interaction;
            }
        }
        return null;
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi   the TARDISDisplayItem to determine the ItemStack to display
     * @param world the world to spawn the entity in
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param z     the z coordinate
     */
    public static void set(TARDISDisplayItem tdi, World world, int x, int y, int z) {
        // spawn an item display entity
        ItemStack is = new ItemStack(tdi.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + tdi.getDisplayName());
//        im.setItemModel(tdi.getCustomModel());
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
        is.setItemMeta(im);
        Location l = new Location(world, x + 0.5d, y + 0.5d, z + 0.5d);
        ItemDisplay display = (ItemDisplay) world.spawnEntity(l, EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setPersistent(true);
        display.setInvulnerable(true);
        display.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi   the TARDISDisplayItem to determine the ItemStack to display
     * @param block the block location to spawn the entity at
     */
    public static ItemDisplay set(TARDISDisplayItem tdi, Block block, int id) {
        // spawn an item display entity
        if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || tdi.isLight()) {
            // also set an interaction entity
            Interaction interaction = (Interaction) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
            interaction.setResponsive(true);
            interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
            interaction.setPersistent(true);
            if (tdi == TARDISDisplayItem.DOOR) {
                // set size
                interaction.setInteractionHeight(2.0f);
                interaction.setInteractionWidth(1.0f);
                interaction.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
            }
            if (tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) {
                // set size
                interaction.setInteractionHeight(3.0f);
                interaction.setInteractionWidth(1.0f);
                interaction.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
            }
            if (tdi.isLight()) {
                // set a light block
                Levelled light = TARDISConstants.LIGHT;
                light.setLevel(tdi.isLit() ? 15 : 0);
                block.setBlockData(light);
            }
        } else if (tdi != TARDISDisplayItem.ARTRON_FURNACE && tdi != TARDISDisplayItem.SONIC_GENERATOR) {
            block.setBlockData(TARDISConstants.BARRIER);
        }
        Material material = (tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) ? tdi.getCraftMaterial() : tdi.getMaterial();
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + tdi.getDisplayName());
//        if (tdi.getCustomModel() != null) {
//            im.setItemModel(tdi.getCustomModel());
//        }
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
        is.setItemMeta(im);
        double ay = (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) ? 0.0d : 0.5d;
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || tdi == TARDISDisplayItem.UNTEMPERED_SCHISM) {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        }
        display.setPersistent(true);
        display.setInvulnerable(true);
        display.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
        if (tdi == TARDISDisplayItem.ARTRON_FURNACE) {
            display.setBrightness(new Display.Brightness(15, 15));
        }
        return display;
    }

    /**
     * Spawn an Item Display entity
     *
     * @param tdi   the TARDISDisplayItem to determine the ItemStack to display
     * @param block the block location to spawn the entity at
     * @param im    the ItemMeta to set on the display ItemStack
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
     * @param key      the custom model data to set for the custom block key associated with the entity
     */
    public static void set(Location location, String key, boolean isDoor) {
        // spawn an interaction entity
        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location.clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, key);
        interaction.setResponsive(true);
        interaction.setPersistent(true);
        interaction.setInvulnerable(true);
        if (isDoor) {
            // set size
            interaction.setInteractionHeight(2.0f);
            interaction.setInteractionWidth(1.0f);
        }
    }

    /**
     * Spawn an Interaction entity for a modelled TARDIS exterior
     *
     * @param stand the armour stand to add the interaction entity to
     * @param id    the tardis id to set for the key associated with the entity
     */
    public static void setInteraction(ArmorStand stand, int id) {
        Location location = stand.getLocation();
        // spawn an interaction entity
        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getStandUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), stand.getUniqueId());
        interaction.setResponsive(true);
        interaction.setPersistent(true);
        interaction.setInvulnerable(true);
        // set size
        interaction.setInteractionHeight(2.5f);
        interaction.setInteractionWidth(1.5f);
    }

    /**
     * Spawn an Interaction entity for a block TARDIS exterior
     *
     * @param block the block to spawn the interaction entity at
     * @param id    the tardis id to set for the key associated with the entity
     */
    public static void setInteraction(Block block, int id) {
        Location location = block.getLocation().clone().add(0.5d, 0, 0.5d);
        // spawn an interaction entity
        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
        interaction.setResponsive(true);
        interaction.setPersistent(true);
        interaction.setInvulnerable(true);
        // set size
        interaction.setInteractionHeight(4.0f);
        interaction.setInteractionWidth(3.0f);
    }

    /**
     * Remove item display, interaction &amp; item frame entities in a chunk
     *
     * @param chunk the chunk to search for entities
     * @param lower a lower y value to limit which entities will be removed
     * @param upper an upper y value to limit which entities will be removed
     */
    public static void removeDisplaysInChunk(Chunk chunk, int lower, int upper) {
        for (Entity entity : chunk.getEntities()) {
            // TARDIS item display and interaction entities + item frames
            if (((entity instanceof ItemDisplay || entity instanceof Interaction) && entity.getPersistentDataContainer().has(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) || entity instanceof ItemFrame) {
                int y = entity.getLocation().getBlockY();
                if (y >= lower && y <= upper) {
                    entity.remove();
                }
            }
        }
    }
}
