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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

/**
 *
 * @author macgeek
 */
public class TARDISHeadSetter {

    public static void textureSkull(TARDIS plugin, UUID uuid, JsonObject head, Block block) {
        try {
            PlayerProfile profile = plugin.getServer().createPlayerProfile(uuid);
            PlayerTextures textures = profile.getTextures();
            URL url = new URL(head.get("texture").getAsString());
            textures.setSkin(url);
            profile.setTextures(textures);
            Skull skull = (Skull) block.getState();
            skull.setOwnerProfile(profile);
            skull.update();
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.WARNING, "Could not create schematic texture URL for player head!");
        }
    }
}
