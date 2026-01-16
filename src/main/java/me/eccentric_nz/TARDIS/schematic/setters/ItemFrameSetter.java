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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rotors.TimeRotor;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
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
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class ItemFrameSetter {

    public static void curate(JsonArray frames, Location start, int id) {
        for (int i = 0; i < frames.size(); i++) {
            JsonObject json = frames.get(i).getAsJsonObject();
            JsonObject rel = json.get("rel_location").getAsJsonObject();
            int px = rel.get("x").getAsInt();
            int py = rel.get("y").getAsInt();
            int pz = rel.get("z").getAsInt();
            BlockFace facing = BlockFace.valueOf(json.get("facing").getAsString());
            Location l = new Location(start.getWorld(), start.getBlockX() + px, start.getBlockY() + py, start.getBlockZ() + pz);
            ItemFrame frame = (ItemFrame) start.getWorld().spawnEntity(l, (json.get("glowing").getAsBoolean()) ? EntityType.GLOW_ITEM_FRAME : EntityType.ITEM_FRAME);
            frame.setFacingDirection(facing, true);
            String cmd = "";
            if (json.has("item")) {
                ItemStack is = ItemStackSetter.build(
                        json.get("item") instanceof JsonPrimitive ?
                                json :
                                json.get("item").getAsJsonObject()
                );
                frame.setItem(is, false);
                if (json.has("rotation")) {
                    Rotation rotation = Rotation.valueOf(json.get("rotation").getAsString());
                    frame.setRotation(rotation);
                }
            }
            if (json.has("rotor") && id != -1) {
                frame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, cmd);
                // update rotor record
                TimeRotor.updateRotorRecord(id, frame.getUniqueId().toString());
            }
            // check whether it is Lab Equipment
            if (json.has("microscope")) {
                frame.getPersistentDataContainer().set(TARDIS.plugin.getMicroscopeKey(), PersistentDataType.INTEGER, 10000);
            }
            frame.setFixed(json.get("fixed").getAsBoolean());
            frame.setVisible(json.get("visible").getAsBoolean());
            Rotation rotation = Rotation.valueOf(json.get("rotation").getAsString());
            frame.setRotation(rotation);
        }
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
