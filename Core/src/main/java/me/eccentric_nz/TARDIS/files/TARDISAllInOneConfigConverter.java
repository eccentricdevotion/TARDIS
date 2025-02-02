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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TARDISAllInOneConfigConverter {

    private final TARDIS plugin;
    private final String basePath;

    public TARDISAllInOneConfigConverter(TARDIS plugin) {
        this.plugin = plugin;
        this.basePath = this.plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator;
    }

    public boolean transferConfig(TardisModule module) {
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
                    // vortex manipulator
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
                case MONSTERS -> {
                    // weeping angels
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
                    // shop config
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
                    // shop items
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
                case BLASTER -> {
                    // blaster config
                    String tbPath = basePath + "TARDISSonicBlaster" + File.separator + "config.yml";
                    File tbFile = new File(tbPath);
                    if (tbFile.exists()) {
                        FileConfiguration ts = YamlConfiguration.loadConfiguration(tbFile);
                        FileConfiguration blaster = plugin.getBlasterConfig();
                        blaster.set("max_blocks", ts.get("max_blocks"));
                        for (String key : blaster.getConfigurationSection("tachyon_use").getKeys(false)) {
                            blaster.set("tachyon_use." + key, ts.get("tachyon_use." + key));
                        }
                        //add recipes
                        String rbPath = basePath + "TARDISSonicBlaster" + File.separator + "recipes.yml";
                        File rbFile = new File(rbPath);
                        if (rbFile.exists()) {
                            FileConfiguration rs = YamlConfiguration.loadConfiguration(rbFile);
                            for (String key : blaster.getConfigurationSection("recipes").getKeys(true)) {
                                blaster.set("recipes." + key, rs.get(key));
                            }
                        }
                        String blasterPath = plugin.getDataFolder() + File.separator + "blaster.yml";
                        File bf = new File(blasterPath);
                        blaster.save(bf);
                        plugin.getBlasterConfig().load(bf);
                    }
                }
                default -> {
                }
            }
            return true;
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "Could not transfer config options from " + module + " legacy plugin folder! " + e.getMessage());
        }
        return false;
    }
}
