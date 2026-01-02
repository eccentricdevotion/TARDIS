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

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.desktop.TARDISRandomArchiveName;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class HeadSetter {

    public static void textureSkull(TARDIS plugin, UUID uuid, JsonObject head, Block block) {
        try {
            String name;
            if (head.has("name")) {
                name = StringUtils.defaultIfBlank(
                        head.get("name").getAsString(),
                        TARDISStringUtils.toLowercaseDashed(TARDISRandomArchiveName.getRandomName()).substring(0, 15)
                );
            } else {
                name = TARDISStringUtils.toLowercaseDashed(TARDISRandomArchiveName.getRandomName()).substring(0, 15);
            }
            PlayerProfile profile = plugin.getServer().createProfile(uuid, name);
            PlayerTextures textures = profile.getTextures();
            URL url = URI.create(head.get("texture").getAsString()).toURL();
            textures.setSkin(url);
            profile.setTextures(textures);
            Skull skull = (Skull) block.getState();
            if (profile.isComplete()) {
                skull.setPlayerProfile(profile);
            } else {
                TARDIS.plugin.debug("Head texture could not be set due to incomplete player profile!");
            }
            skull.update();
        } catch (MalformedURLException ex) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "Could not create schematic texture URL for player head!");
        }
    }
}
