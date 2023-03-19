package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TARDISAllInOneConfigConverter {

    private final TARDIS plugin;
    private final String basePath;

    public TARDISAllInOneConfigConverter(TARDIS plugin) {
        this.plugin = plugin;
        this.basePath = this.plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator;
    }

    public boolean transferConfigs() {
        try {
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
                flat.save(new File(flatPath));
            }
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
                vortex.save(new File(vortexPath));
            }
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
                monsters.save(new File(monstersPath));
            }
            // Shop
            String tsPath = basePath + "TARDISShop" + File.separator + "config.yml";
            File tsFile = new File(tsPath);
            if (tsFile.exists()) {
                FileConfiguration ts = YamlConfiguration.loadConfiguration(tsFile);
                FileConfiguration shop = plugin.getShopConfig();
                for (String key : shop.getKeys(true)) {
                    shop.set(key, ts.get(key));
                }
                String shopPath = plugin.getDataFolder() + File.separator + "shop.yml";
                shop.save(new File(shopPath));
            }
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not transfer config options from legacy plugin folders! " + e.getMessage());
        }
        return false;
    }
}
