/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.schematic.actions.SchematicSave;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class SchematicBuilder {

    private final TARDIS plugin;
    private final World w;
    private final int id, sx, ex, sy, ey, sz, ez;
    private final int[] controls = {0, 2, 3, 4, 5};
    private final HashMap<String, Material> mushroom_stem = new HashMap<>();
    private Location h;

    public SchematicBuilder(TARDIS plugin, int id, World w, int sx, int ex, int sy, int ey, int sz, int ez) {
        this.plugin = plugin;
        this.id = id;
        this.w = w;
        this.sx = sx;
        this.ex = ex;
        this.sy = sy;
        this.ey = ey;
        this.sz = sz;
        this.ez = ez;
        // orange hexagon
        mushroom_stem.put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=true,west=true]", Material.ORANGE_WOOL);
        // blue box
        mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=true,west=true]", Material.BLUE_WOOL);
        // white roundel
        mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=false]", Material.WHITE_STAINED_GLASS);
        // white roundel offset
        mushroom_stem.put("minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=true]", Material.WHITE_TERRACOTTA);
    }

    public ArchiveData build() {
        boolean ars = true;
        // get locations of controls first and compare their coords...
        HashMap<Integer, Location> map = new HashMap<>();
        for (int c : controls) {
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            whereh.put("type", c);
            ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
            if (rsc.resultSet()) {
                Location location = TARDISStaticLocationGetters.getLocationFromDB(rsc.getLocation());
                switch (c) {
                    // world repeater
                    // x repeater
                    // z repeater
                    case 2, 3, 4, 5 -> map.put(c, location); // distance multiplier
                    default ->
                            h = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation()); // handbrake
                }
            }
        }
        // also find the beacon location...
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        int bx = 0, by = 0, bz = 0, cx = 0, cy = 0, cz = 0;
        if (rs.resultSet()) {
            String beacon = rs.getTardis().getBeacon();
            if (!beacon.isEmpty()) {
                String[] split = beacon.split(":");
                bx = TARDISNumberParsers.parseInt(split[1]);
                by = TARDISNumberParsers.parseInt(split[2]);
                bz = TARDISNumberParsers.parseInt(split[3]);
            }
            // and the creeper location...
            String creeper = rs.getTardis().getCreeper();
            if (!creeper.isEmpty()) {
                String[] csplit = creeper.split(":");
                cx = TARDISNumberParsers.parseInt(csplit[1].substring(0, csplit[1].length() - 2));
                cy = TARDISNumberParsers.parseInt(csplit[2]);
                cz = TARDISNumberParsers.parseInt(csplit[3].substring(0, csplit[3].length() - 2));
            }
        }

        // get the min & max coords
        int minx = Math.min(sx, ex);
        int maxx = Math.max(sx, ex);
        int miny = Math.min(sy, ey);
        int maxy = Math.max(sy, ey);
        int minz = Math.min(sz, ez);
        int maxz = Math.max(sz, ez);
        // create a JSON object for relative position
        JsonObject relative = new JsonObject();
        relative.addProperty("x", maxx);
        relative.addProperty("y", miny);
        relative.addProperty("z", minz - 1);
        // create a JSON object for dimensions
        JsonObject dimensions = new JsonObject();
        int width = (maxx - minx) + 1;
        int height = (maxy - miny) + 1;
        int length = (maxz - minz) + 1;
        dimensions.addProperty("width", width);
        dimensions.addProperty("height", height);
        dimensions.addProperty("length", length);
        JsonArray paintings = new JsonArray();
        JsonArray itemFrames = new JsonArray();
        JsonArray itemDisplays = new JsonArray();
        JsonArray interactions = new JsonArray();
        List<Entity> entities = new ArrayList<>();
        // create JSON arrays for block data
        JsonArray levels = new JsonArray();
        int f = 2;
        int beacon = 0;
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
                        if (entity instanceof ItemFrame fr) {
                            if (!entities.contains(entity)) {
                                JsonObject frame = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", eloc.getBlockX() - minx);
                                loc.addProperty("y", eloc.getBlockY() - miny);
                                loc.addProperty("z", eloc.getBlockZ() - minz);
                                frame.add("rel_location", loc);
                                frame.addProperty("facing", fr.getFacing().toString());
                                ItemStack item = fr.getItem();
                                Material type = item.getType();
                                if (!type.isAir()) {
                                    frame.addProperty("item", type.toString());
                                    if (item.hasItemMeta()) {
                                        ItemMeta im = item.getItemMeta();
                                        if (im.hasItemModel()) {
                                            frame.addProperty("cmd", im.getItemModel().getKey());
                                        }
                                        if (im.hasDisplayName()) {
                                            JsonElement element = ComponentUtils.getJson(im.displayName());
                                            plugin.debug(element.toString());
                                            frame.add("name", element);
                                        }
                                        if (im.hasLore()) {
                                            JsonArray lore = new JsonArray();
                                            for (Component s : im.lore()) {
                                                lore.add(ComponentUtils.stripColour(s));
                                            }
                                            frame.add("lore", lore);
                                        }
                                        if ((Tag.ITEMS_BANNERS.isTagged(type) || type == Material.SHIELD) && im instanceof BlockStateMeta bsm) {
                                            JsonObject state = SchematicSave.getBannerJson(bsm.getBlockState());
                                            frame.add("banner", state);
                                        }
                                    }
                                }
                                frame.addProperty("fixed", fr.isFixed());
                                frame.addProperty("visible", fr.isVisible());
                                frame.addProperty("rotation", fr.getRotation().toString());
                                frame.addProperty("glowing", (fr instanceof GlowItemFrame));
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
                                ItemMeta im = display.getItemStack().getItemMeta();
                                stack.addProperty("type", material.toString());
                                if (im.hasItemModel()) {
                                    NamespacedKey model = im.getItemModel();
                                    stack.addProperty("cmd", model.getKey());
                                    TARDISDisplayItem tdi = TARDISDisplayItemRegistry.getByModel(model);
                                    if (tdi != null) {
                                        stack.addProperty("light", tdi.isLight());
                                        stack.addProperty("lit", tdi.isLit());
                                    }
                                }
                                // save custom name
                                if (im.hasDisplayName()) {
                                    JsonElement element = ComponentUtils.getJson(im.displayName());
                                    stack.add("name", element);
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
                    BlockData data = b.getBlockData();
                    Material m = data.getMaterial();
                    // set ARS block
                    if (ars && m.isAir()) {
                        data = Material.INFESTED_COBBLESTONE.createBlockData();
                        ars = false;
                    }
                    switch (m) {
                        case REPEATER -> {
                            // random location blocks
                            if (isControlBlock(map.get(f), w, r, l, c)) {
                                MultipleFacing mushroom = (MultipleFacing) Material.MUSHROOM_STEM.createBlockData();
                                mushroom.setFace(BlockFace.DOWN, true);
                                mushroom.setFace(BlockFace.EAST, true);
                                mushroom.setFace(BlockFace.NORTH, true);
                                mushroom.setFace(BlockFace.SOUTH, true);
                                mushroom.setFace(BlockFace.UP, true);
                                mushroom.setFace(BlockFace.WEST, true);
                                data = mushroom;
                                f++;
                            }
                        }
                        case LEVER -> {
                            // handbrake
                            if (isControlBlock(h, w, r, l, c)) {
                                data = Material.CAKE.createBlockData();
                            }
                        }
                        case MUSHROOM_STEM -> {
                            if (mushroom_stem.containsKey(data.getAsString())) {
                                data = mushroom_stem.get(data.getAsString()).createBlockData();
                            }
                        }
                        default -> {
                        }
                    }
                    if (bx != 0 && l == by && r == bx && c == bz) {
                        data = Material.BEDROCK.createBlockData();
                    }
                    if (cx != 0 && l == cy && r == cx && c == cz) {
                        data = (m.equals(Material.BEACON)) ? Material.BEACON.createBlockData() : Material.COMMAND_BLOCK.createBlockData();
                        beacon = (m.equals(Material.BEACON)) ? 1 : 0;
                    }
                    obj.addProperty("data", data.getAsString());
                    // banners
                    if (TARDISStaticUtils.isBanner(m)) {
                        JsonObject state = SchematicSave.getBannerJson(b.getState());
                        obj.add("banner", state);
                    }
                    // player heads
                    if (m.equals(Material.PLAYER_HEAD) || m.equals(Material.PLAYER_WALL_HEAD)) {
                        JsonObject head = new JsonObject();
                        Skull skull = (Skull) b.getState();
                        if (skull.getPlayerProfile() != null) {
                            String name = Objects.requireNonNullElse(skull.getPlayerProfile().getName(), "");
                            head.addProperty("uuid", skull.getPlayerProfile().getUniqueId().toString());
                            head.addProperty("name", name);
                            head.addProperty("texture", skull.getPlayerProfile().getTextures().getSkin().toString());
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
        if (!paintings.isEmpty()) {
            schematic.add("paintings", paintings);
        }
        if (!itemFrames.isEmpty()) {
            schematic.add("item_frames", itemFrames);
        }
        if (!itemDisplays.isEmpty()) {
            schematic.add("item_displays", itemDisplays);
        }
        if (!interactions.isEmpty()) {
            schematic.add("interactions", interactions);
        }
        return new ArchiveData(schematic, beacon);
    }

    private boolean isControlBlock(Location l, World w, int x, int y, int z) {
        Location n = new Location(w, x, y, z);
        return (n.equals(l));
    }

    public static class ArchiveData {

        private final JsonObject JSON;
        private final int beacon;

        ArchiveData(JsonObject JSON, int beacon) {
            this.JSON = JSON;
            this.beacon = beacon;
        }

        public JsonObject getJSON() {
            return JSON;
        }

        public int getBeacon() {
            return beacon;
        }
    }
}
