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
package me.eccentric_nz.TARDIS.schematic.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SchematicSave {

    public static JsonObject getBannerJson(BlockState b) {
        JsonObject state = new JsonObject();
        Banner banner = (Banner) b;
        state.addProperty("base_colour", banner.getBaseColor().toString());
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
        return state;
    }

    public boolean act(TARDIS plugin, Player player, String which) {
        UUID uuid = player.getUniqueId();
        // check they have selected start and end blocks
        if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_START");
            return true;
        }
        if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NO_END");
            return true;
        }
        // get the world
        World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
        String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
        if (!w.getName().equals(chk_w)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_WORLD");
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_SQUARE");
            return true;
        }
        if (width % 16 != 0 && !which.equals("zero") && !which.equals("junk") && !which.contains("dalek")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_MULTIPLE");
            return true;
        }
        JsonArray paintings = new JsonArray();
        JsonArray armourStands = new JsonArray();
        JsonArray itemFrames = new JsonArray();
        JsonArray itemDisplays = new JsonArray();
        JsonArray interactions = new JsonArray();
        JsonArray mannequins = new JsonArray();
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
                    BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1).expand(0.1d);
                    for (Entity entity : b.getLocation().getWorld().getNearbyEntities(box)) {
                        Location entityLocation = entity.getLocation();
                        if (entity instanceof Mannequin mannequin) {
                            if (!entities.contains(entity)) {
                                JsonObject soldier = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", entityLocation.getBlockX() - minx);
                                loc.addProperty("y", entityLocation.getBlockY() - miny);
                                loc.addProperty("z", entityLocation.getBlockZ() - minz);
                                soldier.add("rel_location", loc);
                                soldier.addProperty("rotation", mannequin.getYaw());
                                soldier.addProperty("yaw", mannequin.getBodyYaw());
                                String type = mannequin.getPersistentDataContainer().getOrDefault(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.STRING, "roman");
                                soldier.addProperty("type", type);
                                if (!mannequin.getEquipment().getItemInMainHand().getType().isAir()) {
                                    soldier.addProperty("hand", mannequin.getMainHand() == MainHand.LEFT ? "left" : "right");
                                    soldier.addProperty("item", mannequin.getEquipment().getItemInMainHand().getType().toString());
                                }
                                mannequins.add(soldier);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof ArmorStand stand) {
                            if (!entities.contains(entity)) {
                                JsonObject as = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", entityLocation.getBlockX() - minx);
                                loc.addProperty("y", entityLocation.getBlockY() - miny);
                                loc.addProperty("z", entityLocation.getBlockZ() - minz);
                                as.add("rel_location", loc);
                                as.addProperty("facing", stand.getFacing().toString());
                                as.addProperty("invisible", stand.isVisible());
                                JsonObject head = new JsonObject();
                                ItemStack helmet = stand.getEquipment().getHelmet();
                                if (helmet != null) {
                                    ItemMeta im = helmet.getItemMeta();
                                    if (im.hasItemModel()) {
                                        head.addProperty("model", im.getItemModel().toString());
                                        head.addProperty("material", helmet.getType().toString());
                                        as.add("head", head);
                                    }
                                }
                                armourStands.add(as);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof Painting art) {
                            if (!entities.contains(entity)) {
                                JsonObject painting = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", entityLocation.getBlockX() - minx);
                                loc.addProperty("y", entityLocation.getBlockY() - miny);
                                loc.addProperty("z", entityLocation.getBlockZ() - minz);
                                painting.add("rel_location", loc);
                                try {
                                    painting.addProperty("art", art.getArt().toString());
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    // custom datapack painting
                                    painting.addProperty("art", DataPackPainting.getCustomVariant(art));
                                }
                                painting.addProperty("facing", art.getFacing().toString());
                                paintings.add(painting);
                                entities.add(entity);
                            }
                        }
                        if (entity instanceof ItemFrame f) {
                            if (!entities.contains(entity)) {
                                JsonObject frame = new JsonObject();
                                JsonObject loc = new JsonObject();
                                loc.addProperty("x", entityLocation.getBlockX() - minx);
                                loc.addProperty("y", entityLocation.getBlockY() - miny);
                                loc.addProperty("z", entityLocation.getBlockZ() - minz);
                                frame.add("rel_location", loc);
                                frame.addProperty("facing", f.getFacing().toString());
                                ItemStack item = f.getItem();
                                if (item != null) {
                                    Material type = item.getType();
                                    frame.addProperty("item", type.toString());
                                    if (item.hasItemMeta()) {
                                        ItemMeta im = item.getItemMeta();
                                        if (im.hasItemModel()) {
                                            frame.addProperty("cmd", im.getItemModel().getKey());
                                        }
                                        if (im.hasDisplayName()) {
                                            frame.addProperty("name", ComponentUtils.stripColour(im.displayName()));
                                        }
                                        if (im.hasLore()) {
                                            JsonArray lore = new JsonArray();
                                            for (Component component : im.lore()) {
                                                lore.add(ComponentUtils.stripColour(component));
                                            }
                                            frame.add("lore", lore);
                                        }
                                        if ((Tag.ITEMS_BANNERS.isTagged(type) || type == Material.SHIELD) && im instanceof BlockStateMeta bsm) {
                                            JsonObject state = SchematicSave.getBannerJson(bsm.getBlockState());
                                            frame.add("banner", state);
                                        }
                                    }
                                }
                                if (f.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                                    frame.addProperty("rotor", true);
                                }
                                if (f.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.INTEGER)) {
                                    frame.addProperty("microscope", true);
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
                                loc.addProperty("x", entityLocation.getBlockX() - minx);
                                loc.addProperty("y", entityLocation.getBlockY() - miny);
                                loc.addProperty("z", entityLocation.getBlockZ() - minz);
                                item.add("rel_location", loc);
                                JsonObject stack = new JsonObject();
                                Material material = display.getItemStack().getType();
                                NamespacedKey model = null;
                                if (display.getItemStack().hasItemMeta()) {
                                    ItemMeta im = display.getItemStack().getItemMeta();
                                    String pdckey = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.STRING);
                                    model = new NamespacedKey(plugin, pdckey);
                                    stack.addProperty("cmd", model.getKey());
                                }
                                stack.addProperty("type", material.toString());
                                TARDISDisplayItem tdi = TARDISDisplayItemRegistry.getByModel(model);
                                if (tdi != null) {
                                    stack.addProperty("light", tdi.isLight());
                                    stack.addProperty("lit", tdi.isLit());
                                    if (tdi.isClosedDoor()) {
                                        stack.addProperty("door", true);
                                    }
                                }
                                item.add("stack", stack);
                                itemDisplays.add(item);
                                entities.add(entity);
                            }
                        }
                    }
                    String blockData = b.getBlockData().getAsString();
                    obj.addProperty("data", blockData);
                    // banners
                    if (TARDISStaticUtils.isBanner(b.getType())) {
                        JsonObject state = getBannerJson(b.getState());
                        obj.add("banner", state);
                    }
                    // player heads
                    if (b.getType().equals(Material.PLAYER_HEAD) || b.getType().equals(Material.PLAYER_WALL_HEAD)) {
                        JsonObject head = new JsonObject();
                        Skull skull = (Skull) b.getState();
                        if (skull.getPlayerProfile() != null) {
                            head.addProperty("uuid", skull.getPlayerProfile().getUniqueId().toString());
                            head.addProperty("texture", skull.getPlayerProfile().getTextures().getSkin().toString());
                        }
                        obj.add("head", head);
                    }
                    // decorated pots
                    if (b.getType().equals(Material.DECORATED_POT)) {
                        JsonObject pot = new JsonObject();
                        DecoratedPot decorated = (DecoratedPot) b.getState();
                        for (Map.Entry<DecoratedPot.Side, Material> entry : decorated.getSherds().entrySet()) {
                            pot.addProperty(entry.getKey().toString(), entry.getValue().toString());
                        }
                        if (b.getBlockData() instanceof org.bukkit.block.data.type.DecoratedPot dp) {
                            pot.addProperty("cracked", dp.isCracked());
                        }
                        obj.add("pot", pot);
                    }
                    // signs
                    if (Tag.ALL_SIGNS.isTagged(b.getType())) {
                        JsonObject text = new JsonObject();
                        Sign sign = (Sign) b.getState();
                        SignSide front = sign.getSide(Side.FRONT);
                        if (!front.lines().isEmpty()) {
                            text.addProperty("line0", ComponentUtils.stripColour(front.line(0)));
                            text.addProperty("line1", ComponentUtils.stripColour(front.line(1)));
                            text.addProperty("line2", ComponentUtils.stripColour(front.line(2)));
                            text.addProperty("line3", ComponentUtils.stripColour(front.line(3)));
                            text.addProperty("glowing", front.isGlowingText());
                            text.addProperty("colour", front.getColor().toString());
                            text.addProperty("editable", sign.isWaxed());
                            JsonObject side = new JsonObject();
                            SignSide back = sign.getSide(Side.BACK);
                            side.addProperty("line0", ComponentUtils.stripColour(back.line(0)));
                            side.addProperty("line1", ComponentUtils.stripColour(back.line(1)));
                            side.addProperty("line2", ComponentUtils.stripColour(back.line(2)));
                            side.addProperty("line3", ComponentUtils.stripColour(back.line(3)));
                            side.addProperty("glowing", back.isGlowingText());
                            side.addProperty("colour", back.getColor().toString());
                            text.add("back", side);
                            obj.add("sign", text);
                        }
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
        if (!mannequins.isEmpty()) {
            schematic.add("mannequins", mannequins);
        }
        if (!armourStands.isEmpty()) {
            schematic.add("armour_stands", armourStands);
        }
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
        String output = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".json";
        File file = new File(output);
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file), 16 * 1024)) {
                bw.write(schematic.toString());
            }
            TARDISSchematicGZip.zip(output, plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".tschm");
            file.delete();
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_SAVED", which);
        } catch (IOException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_ERROR");
        }
        return true;
    }
}
