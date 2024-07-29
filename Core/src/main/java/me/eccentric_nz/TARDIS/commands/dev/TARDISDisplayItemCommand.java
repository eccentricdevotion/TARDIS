/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockRoomConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;

import java.util.HashMap;

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
        switch (args[1].toLowerCase()) {
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
                if (args.length > 3 && TARDISNumberParsers.isSimpleNumber(args[4])) {
                    int cmd = TARDISNumberParsers.parseInt(args[4]);
                    ItemMeta im = is.getItemMeta();
                    im.setCustomModelData(cmd);
                    is.setItemMeta(im);
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
            case "block" -> {
                ItemDisplay blockDisplay = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.0d, 0.5d), EntityType.ITEM_DISPLAY);
                ItemStack door = new ItemStack(Material.IRON_DOOR);
                ItemMeta im = door.getItemMeta();
                int cmd = (args.length == 3) ? TARDISNumberParsers.parseInt(args[2]) : 1;
                im.setCustomModelData(10000 + cmd);
                door.setItemMeta(im);
                blockDisplay.setItemStack(door);
                blockDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
                // also set an interaction entity
                Interaction interaction = (Interaction) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.0d, 0.5d), EntityType.INTERACTION);
                interaction.setResponsive(true);
                interaction.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 10000 + cmd);
                interaction.setPersistent(true);
                interaction.setInteractionHeight(2.0f);
                interaction.setInteractionWidth(1.0f);
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
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
                    im.setCustomModelData(tdi.getCustomModelData());
                    im.setDisplayName(TARDISStringUtils.capitalise(args[2]));
                    is.setItemMeta(im);
                    Block up = block.getRelative(BlockFace.UP);
                    if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR || tdi.isLight()) {
                        // also set an interaction entity
                        Interaction interaction = (Interaction) block.getWorld().spawnEntity(up.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
                        interaction.setResponsive(true);
                        interaction.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
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
                    if (tdi == TARDISDisplayItem.DOOR || tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) {
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
                TARDISDisplayBlockConverter converter = new TARDISDisplayBlockConverter(plugin, player);
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, converter, 5, 1);
                converter.setTaskId(taskId);
                // also find any rooms and convert the mushroom blocks there
                // get players tardis_id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(player.getUniqueId().toString())) {
                    int id = rst.getTardisId();
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, where);
                    if (rsa.resultSet()) {
                        String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                        Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
                        for (int l = 0; l < 3; l++) {
                            for (int row = 0; row < 9; row++) {
                                for (int col = 0; col < 9; col++) {
                                    if (!json[l][row][col].equalsIgnoreCase("STONE") && !isConsole(json[l][row][col])) {
                                        // get ARS slot
                                        TARDISARSSlot slot = new TARDISARSSlot();
                                        slot.setChunk(c);
                                        slot.setY(l);
                                        slot.setX(row);
                                        slot.setZ(col);
                                        TARDISDisplayBlockRoomConverter roomConverter = new TARDISDisplayBlockRoomConverter(plugin, player, slot);
                                        int roomTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, roomConverter, 5, 1);
                                        roomConverter.setTaskId(roomTaskId);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
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
                int colour = TARDISNumberParsers.parseInt(args[2]);
                if (colour < 1 || colour > 17) {
                    plugin.getMessenger().message(player, "Number must be between 1-17!");
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
