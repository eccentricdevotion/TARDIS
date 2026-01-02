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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.BiomeSearchResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BiomeUtilities {

    public static String getLevelName() {
        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("level-name");
        } catch (IOException e) {
            return "world"; // minecraft / paper default
        }
    }

    public static void addBiomes(String basePath) {
        if (TARDIS.plugin.getPlanetsConfig().getBoolean("planets.gallifrey.enabled")) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HELPER, "Adding custom biomes for planet Gallifrey...");
            CustomBiome.addCustomBiome(TARDISBiomeData.BADLANDS);
        }
        if (TARDIS.plugin.getPlanetsConfig().getBoolean("planets.skaro.enabled")) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HELPER, "Adding custom biomes for planet Skaro...");
            CustomBiome.addCustomBiome(TARDISBiomeData.DESERT);
        }
    }

    public static Location searchBiome(World world, Biome biome, Location policeBox) {
        BiomeSearchResult searchResult = world.locateNearestBiome(policeBox, 6400, biome);
        return searchResult != null ? searchResult.getLocation() : null;
    }
}
