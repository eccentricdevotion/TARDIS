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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author macgeek
 */
public class TARDISInteractionSetter {

    public static void makeClickable(JsonObject json, Location start) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int px = rel.get("x").getAsInt();
        int py = rel.get("y").getAsInt();
        int pz = rel.get("z").getAsInt();
        Location l = new Location(start.getWorld(), start.getBlockX() + px, start.getBlockY() + py, start.getBlockZ() + pz);
        Interaction interaction = (Interaction) l.getWorld().spawnEntity(l.clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, -1);
        JsonObject bounds = json.get("bounds").getAsJsonObject();
        float height = bounds.get("height").getAsFloat();
        float width = bounds.get("width").getAsFloat();
        interaction.setInteractionHeight(height);
        interaction.setInteractionWidth(width);
        interaction.setResponsive(true);
        interaction.setPersistent(true);
        interaction.setInvulnerable(true);
    }
}
