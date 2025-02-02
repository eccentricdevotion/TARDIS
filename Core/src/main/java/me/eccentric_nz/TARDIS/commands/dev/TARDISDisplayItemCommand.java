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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISUpdateBlocksCommand;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.ShopItem;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayItemCommand {

    private final TARDIS plugin;

    public TARDISDisplayItemCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean display(Player player, String[] args) {
        // spawn display entities
        Block block = player.getTargetBlock(null, 8);
        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "add" -> {
                if (args.length < 3) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return true;
                }
                Material material;
                ItemDisplay.ItemDisplayTransform transform = ItemDisplay.ItemDisplayTransform.GROUND;
                try {
                    material = Material.valueOf(args[2]);
                    if (args.length > 3) {
                        transform = ItemDisplay.ItemDisplayTransform.valueOf(args[3]);
                    }
                } catch (IllegalArgumentException e) {
                    material = Material.DIAMOND_AXE;
                }
                ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
                ItemStack is = new ItemStack(material);
                if (args.length > 4) {
                    try {
                        ShopItem shopItem = ShopItem.valueOf(args[4].toUpperCase(Locale.ROOT));
                        ItemMeta im = is.getItemMeta();
                        im.setItemModel(shopItem.getModel());
                        is.setItemMeta(im);
                    } catch (IllegalArgumentException ignored) { }
                }
                display.setItemStack(is);
                display.setItemDisplayTransform(transform);
                display.setBillboard(Display.Billboard.VERTICAL);
                display.setInvulnerable(true);
                if (args.length > 3 && args[4].equalsIgnoreCase("true")) {
                    TextDisplay text = (TextDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.75d, 0.5d), EntityType.TEXT_DISPLAY);
                    text.setAlignment(TextDisplay.TextAlignment.CENTER);
                    text.setText(TARDISStringUtils.capitalise(material.toString()) + ", Cost: 25.00");
                    text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                    text.setBillboard(Display.Billboard.VERTICAL);
                }
            }
            case "animate" -> {
                if (player.getPassengers().isEmpty()) {
                    ItemStack box = new ItemStack(Material.BLUE_DYE, 1);
                    ItemMeta im = box.getItemMeta();
                    im.setItemModel(new NamespacedKey(plugin, "police_box/flying/blue"));
                    box.setItemMeta(im);
                    ItemDisplay display = VehicleUtility.getItemDisplay(player, box, 1.75f);
                    int period = 40;
                    plugin.getTrackerKeeper().setAnimateTask(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, period), 5, period));
                } else {
                    for (Entity e : player.getPassengers()) {
                        e.eject();
                        e.remove();
                    }
                    plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getAnimateTask());
                }
            }
            case "remove" -> {
                BoundingBox box = new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 2.5, block.getZ() + 1);
                for (Entity e : block.getWorld().getNearbyEntities(box)) {
                    if (e instanceof ItemDisplay || e instanceof TextDisplay || e instanceof Interaction) {
                        e.remove();
                    }
                    // set the block to air
                    block.setType(Material.AIR);
                }
            }
            case "place" -> {
                // tdev displayitem place [block]
                if (args.length < 3) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return true;
                }
                TARDISDisplayItem tdi = TARDISDisplayItem.getBY_NAME().get(args[2]);
                if (tdi != null) {
                    ItemStack is = new ItemStack(tdi.getMaterial());
                    ItemMeta im = is.getItemMeta();
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                    im.setItemModel(tdi.getCustomModel());
                    im.setDisplayName(TARDISStringUtils.capitalise(args[2]));
                    is.setItemMeta(im);
                    Block up = block.getRelative(BlockFace.UP);
                    if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || tdi.isLight()) {
                        // also set an interaction entity
                        Interaction interaction = (Interaction) block.getWorld().spawnEntity(up.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
                        interaction.setResponsive(true);
                        interaction.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                        interaction.setPersistent(true);
                        if (tdi.isLight()) {
                            Levelled light = TARDISConstants.LIGHT;
                            int level = (tdi.isLit()) ? 15 : 0;
                            light.setLevel(level);
                            up.setBlockData(light);
                        }
                        if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) {
                            // set size
                            interaction.setInteractionHeight(2.0f);
                            interaction.setInteractionWidth(1.0f);
                        }
                    } else {
                        up.setType((tdi == TARDISDisplayItem.ARTRON_FURNACE) ? Material.FURNACE : Material.BARRIER);
                    }
                    double ay = (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) ? 0.0d : 0.5d;
                    ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
                    display.setItemStack(is);
                    display.setPersistent(true);
                    display.setInvulnerable(true);
                    if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || tdi == TARDISDisplayItem.UNTEMPERED_SCHISM) {
                        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
                    }
                    if (tdi.getMaterial() == Material.AMETHYST_SHARD) {
                        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
                    }
                    if (tdi == TARDISDisplayItem.ARTRON_FURNACE) {
                        display.setBrightness(new Display.Brightness(15, 15));
                    }
                }
            }
            case "break" -> {
                if (block.getType().equals(Material.BARRIER)) {
                    for (Entity e : block.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d))) {
                        if (e instanceof ItemDisplay display) {
                            ItemStack is = display.getItemStack();
                            block.getWorld().dropItemNaturally(block.getLocation(), is);
                            e.remove();
                        }
                        if (e instanceof Interaction) {
                            e.remove();
                        }
                    }
                    block.setType(Material.AIR);
                }
            }
            case "convert" -> {
                return new TARDISUpdateBlocksCommand(plugin).convert(player);
            }
            case "chunk" -> {
                Chunk chunk = player.getLocation().getChunk();
                for (Entity entity : chunk.getEntities()) {
                    if (entity instanceof ItemDisplay || entity instanceof Interaction || entity instanceof TextDisplay) {
                        entity.remove();
                    }
                }
                return true;
            }
            case "console" -> {
                if (args.length < 3) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return true;
                }
                String colour = args[2].toLowerCase(Locale.ROOT);
                if (TARDISConstants.COLOURS.contains(colour)) {
                    plugin.getMessenger().message(player, "Must be a valid concrete powder colour!");
                    return true;
                }
                // get TARDIS id
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                String uuid = player.getUniqueId().toString();
                if (rs.fromUUID(uuid)) {
                    new ConsoleBuilder(plugin).create(block, colour, rs.getTardisId(), uuid);
                }
                return true;
            }
            default -> {
                return false;
            }
        }
        return true;
    }


    private boolean isConsole(String str) {
        return Consoles.getBY_MATERIALS().containsKey(str);
    }
}
