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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISItemFrameSetter {

    public static void curate(JsonObject json, Location start, int id) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int px = rel.get("x").getAsInt();
        int py = rel.get("y").getAsInt();
        int pz = rel.get("z").getAsInt();
        BlockFace facing = BlockFace.valueOf(json.get("facing").getAsString());
        Location l = new Location(start.getWorld(), start.getBlockX() + px, start.getBlockY() + py, start.getBlockZ() + pz);
        ItemFrame frame = (ItemFrame) start.getWorld().spawnEntity(l, (json.get("glowing").getAsBoolean()) ? EntityType.GLOW_ITEM_FRAME : EntityType.ITEM_FRAME);
        frame.setFacingDirection(facing, true);
        int cmd = 1;
        if (json.has("item")) {
            try {
                ItemStack is = new ItemStack(Material.valueOf(json.get("item").getAsString()));
                ItemMeta im = is.getItemMeta();
                if (json.has("cmd")) {
                    cmd = json.get("cmd").getAsInt();
                    im.setCustomModelData(cmd);
                }
                if (json.has("name")) {
                    im.setDisplayName(json.get("name").getAsString());
                }
                if (json.has("lore")) {
                    List<String> lore = new ArrayList<>();
                    for (JsonElement element : json.get("lore").getAsJsonArray()) {
                        lore.add(element.getAsString());
                    }
                    im.setLore(lore);
                }
                if (json.has("banner")) {
                    JsonObject banner = json.get("banner").getAsJsonObject();
                    DyeColor baseColour = DyeColor.valueOf(banner.get("base_colour").getAsString());
                    JsonArray patterns = banner.get("patterns").getAsJsonArray();
                    List<Pattern> plist = new ArrayList<>();
                    for (int j = 0; j < patterns.size(); j++) {
                        JsonObject jo = patterns.get(j).getAsJsonObject();
                        PatternType pt = PatternType.valueOf(jo.get("pattern").getAsString());
                        DyeColor dc = DyeColor.valueOf(jo.get("pattern_colour").getAsString());
                        Pattern p = new Pattern(dc, pt);
                        plist.add(p);
                    }
                    BlockStateMeta bsm = (BlockStateMeta) im;
                    Banner b = (Banner) bsm.getBlockState();
                    b.setBaseColor(baseColour);
                    b.setPatterns(plist);
                    bsm.setBlockState(b);
                }
                is.setItemMeta(im);
                frame.setItem(is, false);
            } catch (IllegalArgumentException e) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not create item stack for schematic item frame!");
            }
        }
        if (json.has("rotor") && id != -1) {
            frame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
            // update rotor record
            TARDISTimeRotor.updateRotorRecord(id, frame.getUniqueId().toString());
        }
        frame.setFixed(json.get("fixed").getAsBoolean());
        frame.setVisible(json.get("visible").getAsBoolean());
        Rotation rotation = Rotation.valueOf(json.get("rotation").getAsString());
        frame.setRotation(rotation);
    }

    public static ItemFrame getItemFrameFromLocation(Location location) {
        BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1);
        for (Entity e : location.getWorld().getNearbyEntities(box, (e) -> e.getType() == EntityType.ITEM_FRAME)) {
            if (e instanceof ItemFrame frame) {
                return frame;
            }
        }
        return null;
    }
}
