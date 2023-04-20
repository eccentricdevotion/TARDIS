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
package me.eccentric_nz.TARDIS.schematic.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

public class SchematicSave {

    public boolean act(TARDIS plugin, Player player, String which) {
        UUID uuid = player.getUniqueId();
        // check they have selected start and end blocks
        if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
            TARDISMessage.send(player, "SCHM_NO_START");
            return true;
        }
        if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            TARDISMessage.send(player, "SCHM_NO_END");
            return true;
        }
        // get the world
        World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
        String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
        if (!w.getName().equals(chk_w)) {
            TARDISMessage.send(player, "SCHM_WORLD");
            return true;
        }
        // get the raw coords
        int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
        int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
        int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
        int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
        int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
        int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
        // get the min & max coords
        int minx = Math.min(sx, ex);
        int maxx = Math.max(sx, ex);
        int miny = Math.min(sy, ey);
        int maxy = Math.max(sy, ey);
        int minz = Math.min(sz, ez);
        int maxz = Math.max(sz, ez);
        // create a JSON object for relative position
        JsonObject relative = new JsonObject();
        int px = player.getLocation().getBlockX() - minx;
        int py = player.getLocation().getBlockY() - miny;
        int pz = player.getLocation().getBlockZ() - minz;
        relative.addProperty("x", px);
        relative.addProperty("y", py);
        relative.addProperty("z", pz);
        // create a JSON object for dimensions
        JsonObject dimensions = new JsonObject();
        int width = (maxx - minx) + 1;
        int height = (maxy - miny) + 1;
        int length = (maxz - minz) + 1;
        dimensions.addProperty("width", width);
        dimensions.addProperty("height", height);
        dimensions.addProperty("length", length);
        if (width != length) {
            TARDISMessage.send(player, "SCHM_SQUARE");
            return true;
        }
        if ((width % 16 != 0 || length % 16 != 0) && !which.equals("zero") && !which.equals("junk") && !which.contains("dalek")) {
            TARDISMessage.send(player, "SCHM_MULTIPLE");
            return true;
        }
        JsonArray paintings = new JsonArray();
        JsonArray itemFrames = new JsonArray();
        JsonArray itemDisplays = new JsonArray();
        JsonArray interactions = new JsonArray();
        List<Entity> entities = new ArrayList<>();
        // create JSON arrays for block data
        JsonArray levels = new JsonArray();
        // loop through the blocks inside this cube
        for (int l = miny; l <= maxy; l++) {
            JsonArray rows = new JsonArray();
            for (int r = minx; r <= maxx; r++) {
                JsonArray columns = new JsonArray();
                for (int c = minz; c <= maxz; c++) {
                    JsonObject obj = new JsonObject();
                    Block b = w.getBlockAt(r, l, c);
                    // check for entities
                    Location location = b.getLocation();
                    BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1);
                    for (Entity entity : b.getLocation().getWorld().getNearbyEntities(box)) {
                        Location eloc = entity.getLocation();
                        if (entity instanceof Painting art) {
                            if (!entities.contains(entity)) {
                                JsonObject painting = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", eloc.getBlockX() - minx);
                                loc.addProperty("y", eloc.getBlockY() - miny);
                                loc.addProperty("z", eloc.getBlockZ() - minz);
                                painting.add("rel_location", loc);
                                painting.addProperty("art", art.getArt().toString());
                                painting.addProperty("facing", art.getFacing().toString());
                                paintings.add(painting);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof ItemFrame f) {
                            if (!entities.contains(entity)) {
                                JsonObject frame = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", eloc.getBlockX() - minx);
                                loc.addProperty("y", eloc.getBlockY() - miny);
                                loc.addProperty("z", eloc.getBlockZ() - minz);
                                frame.add("rel_location", loc);
                                frame.addProperty("facing", f.getFacing().toString());
                                ItemStack item = f.getItem();
                                if (item != null) {
                                    frame.addProperty("item", f.getItem().getType().toString());
                                    if (item.hasItemMeta()) {
                                        ItemMeta im = item.getItemMeta();
                                        if (im.hasCustomModelData()) {
                                            frame.addProperty("cmd", im.getCustomModelData());
                                        }
                                        if (im.hasDisplayName()) {
                                            frame.addProperty("name", im.getDisplayName());
                                        }
                                        if (im.hasLore()) {
                                            JsonArray lore = new JsonArray();
                                            for (String s : im.getLore()) {
                                                lore.add(s);
                                            }
                                            frame.add("lore", lore);
                                        }
                                    }
                                }
                                if (f.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                                    frame.addProperty("rotor", true);
                                }
                                frame.addProperty("fixed", f.isFixed());
                                frame.addProperty("visible", f.isVisible());
                                frame.addProperty("rotation", f.getRotation().toString());
                                frame.addProperty("glowing", (f instanceof GlowItemFrame));
                                itemFrames.add(frame);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof ItemDisplay display) {
                            if (!entities.contains(entity)) {
                                JsonObject item = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", eloc.getBlockX() - minx);
                                loc.addProperty("y", eloc.getBlockY() - miny);
                                loc.addProperty("z", eloc.getBlockZ() - minz);
                                item.add("rel_location", loc);
                                JsonObject stack = new JsonObject();
                                Material material = display.getItemStack().getType();
                                int model = display.getItemStack().getItemMeta().getCustomModelData();
                                stack.addProperty("type", material.toString());
                                stack.addProperty("cmd", model);
                                TARDISDisplayItem tdi = TARDISDisplayItem.getByMaterialAndData(material, model);
                                if (tdi != null) {
                                    stack.addProperty("light", tdi.isLight());
                                    stack.addProperty("lit", tdi.isLit());
                                }
                                item.add("stack", stack);
                                itemDisplays.add(item);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof Interaction interaction) {
                            if (!entities.contains(entity)) {
                                JsonObject inter = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", eloc.getBlockX() - minx);
                                loc.addProperty("y", eloc.getBlockY() - miny);
                                loc.addProperty("z", eloc.getBlockZ() - minz);
                                inter.add("rel_location", loc);
                                JsonObject bounds = new JsonObject();
                                bounds.addProperty("height", interaction.getInteractionHeight());
                                bounds.addProperty("width", interaction.getInteractionWidth());
                                inter.add("bounds", bounds);
                                interactions.add(inter);
                                entities.add(entity);
                            }
                        }
                    }
                    String blockData = b.getBlockData().getAsString();
                    obj.addProperty("data", blockData);
                    // banners
                    if (TARDISStaticUtils.isBanner(b.getType())) {
                        JsonObject state = new JsonObject();
                        Banner banner = (Banner) b.getState();
                        JsonArray patterns = new JsonArray();
                        if (banner.numberOfPatterns() > 0) {
                            banner.getPatterns().forEach((p) -> {
                                JsonObject pattern = new JsonObject();
                                pattern.addProperty("pattern", p.getPattern().toString());
                                pattern.addProperty("pattern_colour", p.getColor().toString());
                                patterns.add(pattern);
                            });
                        }
                        state.add("patterns", patterns);
                        obj.add("banner", state);
                    }
                    // player heads
                    if (b.getType().equals(Material.PLAYER_HEAD) || b.getType().equals(Material.PLAYER_WALL_HEAD)) {
                        JsonObject head = new JsonObject();
                        Skull skull = (Skull) b.getState();
                        if (skull.getOwnerProfile() != null) {
                            head.addProperty("uuid", skull.getOwnerProfile().getUniqueId().toString());
                            head.addProperty("texture", skull.getOwnerProfile().getTextures().getSkin().toString());
                        }
                        obj.add("head", head);
                    }
                    columns.add(obj);
                }
                rows.add(columns);
            }
            levels.add(rows);
        }
        JsonObject schematic = new JsonObject();
        schematic.add("relative", relative);
        schematic.add("dimensions", dimensions);
        schematic.add("input", levels);
        if (paintings.size() > 0) {
            schematic.add("paintings", paintings);
        }
        if (itemFrames.size() > 0) {
            schematic.add("item_frames", itemFrames);
        }
        if (itemDisplays.size() > 0) {
            schematic.add("item_displays", itemDisplays);
        }
        if (interactions.size() > 0) {
            schematic.add("interactions", interactions);
        }
        String output = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".json";
        File file = new File(output);
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file), 16 * 1024)) {
                bw.write(schematic.toString());
            }
            TARDISSchematicGZip.zip(output, plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".tschm");
            file.delete();
            TARDISMessage.send(player, "SCHM_SAVED", which);
        } catch (IOException e) {
            TARDISMessage.send(player, "SCHM_ERROR");
        }
        return true;
    }
}
