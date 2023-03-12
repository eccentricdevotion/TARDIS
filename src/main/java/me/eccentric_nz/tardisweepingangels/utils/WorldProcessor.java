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
package me.eccentric_nz.tardisweepingangels.utils;

import java.io.File;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.FileUtil;

public class WorldProcessor implements Runnable {

    private final TARDISWeepingAngels plugin;
    private final FileConfiguration config;

    public WorldProcessor(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        config = this.plugin.getConfig();
    }

    @Override
    public void run() {
        if (!config.contains("config_version", true)) {
            // back up the old config
            File oldFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
            File newFile = new File(plugin.getDataFolder() + File.separator + "config_" + System.currentTimeMillis() + ".yml");
            FileUtil.copy(oldFile, newFile);
            // clear old world settings
            plugin.getConfig().set("angels.worlds", null);
            plugin.getConfig().set("cybermen.worlds", null);
            plugin.getConfig().set("daleks.worlds", null);
            plugin.getConfig().set("empty_child.worlds", null);
            plugin.getConfig().set("ice_warriors.worlds", null);
            plugin.getConfig().set("silurians.worlds", null);
            plugin.getConfig().set("sontarans.worlds", null);
            plugin.getConfig().set("vashta_nerada.worlds", null);
            plugin.getConfig().set("zygons.worlds", null);
        }
        // add new world settings
        plugin.getServer().getWorlds().forEach((w) -> {
            String n = sanitiseName(w.getName());
            // set TARDIS worlds, nether and end worlds to zero by default
            int m = (config.contains("spawn_rate.default_max", true)) ? config.getInt("spawn_rate.default_max") : 0;
            if (!config.contains("angels.worlds." + n, true)) {
                plugin.getConfig().set("angels.worlds." + n, m);
            }
            if (!config.contains("cybermen.worlds." + n, true)) {
                plugin.getConfig().set("cybermen.worlds." + n, m);
            }
            if (!config.contains("daleks.worlds." + n, true)) {
                plugin.getConfig().set("daleks.worlds." + n, m);
            }
            if (!config.contains("empty_child.worlds." + n, true)) {
                plugin.getConfig().set("empty_child.worlds." + n, m);
            }
            if (!config.contains("hath.worlds." + n, true)) {
                plugin.getConfig().set("hath.worlds." + n, m);
            }
            if (!config.contains("headless_monks.worlds." + n, true)) {
                plugin.getConfig().set("headless_monks.worlds." + n, m);
            }
            if (!config.contains("ice_warriors.worlds." + n, true)) {
                plugin.getConfig().set("ice_warriors.worlds." + n, m);
            }
            if (!config.contains("judoon.worlds." + n, true) || (config.contains("judoon.worlds." + n, true) && config.getString("judoon.worlds." + n).equals("true"))) {
                plugin.getConfig().set("judoon.worlds." + n, m);
            }
            if (!config.contains("k9.worlds." + n, true)) {
                plugin.getConfig().set("k9.worlds." + n, true);
            }
            if (!config.contains("ood.worlds." + n, true) || (config.contains("ood.worlds." + n, true) && config.getInt("ood.worlds." + n) == 20)) {
                plugin.getConfig().set("ood.worlds." + n, true);
            }
            if (!config.contains("racnoss.worlds." + n, true) && w.getEnvironment() == Environment.NETHER) {
                    plugin.getConfig().set("racnoss.worlds." + n, m);
            }
            if (!config.contains("sea_devils.worlds." + n, true)) {
                plugin.getConfig().set("sea_devils.worlds." + n, m);
            }
            if (!config.contains("silent.worlds." + n, true)) {
                plugin.getConfig().set("silent.worlds." + n, m);
            }
            if (!config.contains("silurians.worlds." + n, true)) {
                plugin.getConfig().set("silurians.worlds." + n, m);
            }
            if (!config.contains("slitheen.worlds." + n, true)) {
                plugin.getConfig().set("slitheen.worlds." + n, m);
            }
            if (!config.contains("sontarans.worlds." + n, true)) {
                plugin.getConfig().set("sontarans.worlds." + n, m);
            }
            if (!config.contains("the_mire.worlds." + n, true)) {
                plugin.getConfig().set("the_mire.worlds." + n, m);
            }
            if (!config.contains("toclafane.worlds." + n, true)) {
                plugin.getConfig().set("toclafane.worlds." + n, m);
            }
            if (!config.contains("vashta_nerada.worlds." + n, true)) {
                plugin.getConfig().set("vashta_nerada.worlds." + n, m);
            }
            if (!config.contains("zygons.worlds." + n, true)) {
                plugin.getConfig().set("zygons.worlds." + n, m);
            }
        });
        plugin.saveConfig();
    }

    public static String sanitiseName(String name) {
        return name.replaceAll("\\.", "_");
    }
}
