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
package me.eccentric_nz.TARDIS.commands.dev;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockRoomConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

/**
 *
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
                ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
                display.setItemStack(new ItemStack(Material.DIAMOND_AXE));
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
                display.setBillboard(Display.Billboard.VERTICAL);
                display.setInvulnerable(true);
                TextDisplay text = (TextDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.75d, 0.5d), EntityType.TEXT_DISPLAY);
                text.setAlignment(TextDisplay.TextAlignment.CENTER);
                text.setText("TARDIS Axe, Cost: 25.00");
                text.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.25f, 0.25f, 0.25f), new AxisAngle4f()));
                text.setBillboard(Display.Billboard.VERTICAL);
            }
            case "remove" -> {
                BoundingBox box = new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 2.5, block.getZ() + 1);
                for (Entity e : block.getWorld().getNearbyEntities(box)) {
                    if (e instanceof ItemDisplay || e instanceof TextDisplay) {
                        e.remove();
                        plugin.debug("Removed - " + e.getType());
                    }
                }
            }
            case "place" -> {
                // tdev displayitem place [block]
                if (args.length < 3) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
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
                    if (tdi.isLight()) {
                        Light light = (Light) Material.LIGHT.createBlockData();
                        int level = (tdi.isLit()) ? 15 : 0;
                        light.setLevel(level);
                        up.setBlockData(light);
                        // also set an interaction entity
                        Interaction interaction = (Interaction) block.getWorld().spawnEntity(up.getLocation(), EntityType.INTERACTION);
                        interaction.setResponsive(true);
                        interaction.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, tdi.getCustomModelData());
                        interaction.setPersistent(true);
                        interaction.setInvulnerable(true);
                    } else {
                        up.setType(Material.BARRIER);
                    }
                    ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
                    display.setItemStack(is);
                    display.setPersistent(true);
                    display.setInvulnerable(true);
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
                // also find any rooms and convert the mushroom blocks there, TODO implement both as player command
                // get players tardis_id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(player.getUniqueId().toString())) {
                    int id = rst.getTardis_id();
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
