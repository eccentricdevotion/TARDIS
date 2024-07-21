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
            return "world"; // minecraft / spigot default
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
