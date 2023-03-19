package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TARDISAllInOneConfigConverter {

    private final TARDIS plugin;
    private final String basePath;

    public TARDISAllInOneConfigConverter(TARDIS plugin) {
        this.plugin = plugin;
        this.basePath = this.plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator;
    }

    public boolean transferConfig(MODULE module) {
        try {
            switch (module) {
                case HELPER -> {
                    // TCG flat world
                    String tcgPath = basePath + "TARDISChunkGenerator" + File.separator + "config.yml";
                    File tcgFile = new File(tcgPath);
                    if (tcgFile.exists()) {
                        FileConfiguration tcg = YamlConfiguration.loadConfiguration(tcgFile);
                        FileConfiguration flat = plugin.getGeneratorConfig();
                        for (String key : flat.getKeys(false)) {
                            flat.set(key, tcg.get(key));
                        }
                        String flatPath = plugin.getDataFolder() + File.separator + "flat_world.yml";
                        File hf = new File(flatPath);
                        flat.save(hf);
                        plugin.getGeneratorConfig().load(hf);
                    }
                }
                case VORTEX_MANIPULATOR -> {
                    // Vortex manipulator
                    String vmPath = basePath + "TARDISVortexManipulator" + File.separator + "config.yml";
                    File vmFile = new File(vmPath);
                    if (vmFile.exists()) {
                        FileConfiguration vm = YamlConfiguration.loadConfiguration(vmFile);
                        FileConfiguration vortex = plugin.getVortexConfig();
                        for (String key : vortex.getKeys(true)) {
                            vortex.set(key, vm.get(key));
                        }
                        String vortexPath = plugin.getDataFolder() + File.separator + "vortex_manipulator.yml";
                        File vf = new File(vortexPath);
                        vortex.save(vf);
                        plugin.getVortexConfig().load(vf);
                    }
                }
                case DYNMAP -> {
                    // Weeping angels
                    String twaPath = basePath + "TARDISWeepingAngels" + File.separator + "config.yml";
                    File twaFile = new File(twaPath);
                    if (twaFile.exists()) {
                        FileConfiguration twa = YamlConfiguration.loadConfiguration(twaFile);
                        FileConfiguration monsters = plugin.getMonstersConfig();
                        for (String key : monsters.getKeys(true)) {
                            monsters.set(key, twa.get(key));
                        }
                        String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
                        File mf = new File(monstersPath);
                        monsters.save(mf);
                        plugin.getMonstersConfig().load(mf);
                    }
                }
                case SHOP -> {
                    // Shop config
                    String tsPath = basePath + "TARDISShop" + File.separator + "config.yml";
                    File tsFile = new File(tsPath);
                    if (tsFile.exists()) {
                        FileConfiguration ts = YamlConfiguration.loadConfiguration(tsFile);
                        FileConfiguration shop = plugin.getShopConfig();
                        for (String key : shop.getKeys(true)) {
                            shop.set(key, ts.get(key));
                        }
                        String shopPath = plugin.getDataFolder() + File.separator + "shop.yml";
                        File sf = new File(shopPath);
                        shop.save(sf);
                        plugin.getShopConfig().load(sf);
                    }
                    // Shop items
                    String tsiPath = basePath + "TARDISShop" + File.separator + "items.yml";
                    File tsiFile = new File(tsiPath);
                    if (tsiFile.exists()) {
                        FileConfiguration tsi = YamlConfiguration.loadConfiguration(tsiFile);
                        FileConfiguration items = plugin.getItemsConfig();
                        for (String key : items.getKeys(true)) {
                            items.set(key, tsi.get(key));
                        }
                        String itemPath = plugin.getDataFolder() + File.separator + "items.yml";
                        File itf = new File(itemPath);
                        items.save(itf);
                        plugin.getItemsConfig().load(itf);
                    }
                }
                default -> { }
            }
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.WARNING, "Could not transfer config options from " + module + " legacy plugin folder! " + e.getMessage());
        }
        return false;
    }
}
