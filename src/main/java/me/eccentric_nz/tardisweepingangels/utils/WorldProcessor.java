/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class WorldProcessor implements Runnable {

    private final TARDIS plugin;
    private final FileConfiguration config;

    public WorldProcessor(TARDIS plugin) {
        this.plugin = plugin;
        config = this.plugin.getMonstersConfig();
    }

    public static String sanitiseName(String name) {
        return name.replaceAll("\\.", "_");
    }

    @Override
    public void run() {
        int i = 0;
        if (!config.contains("config_version", true)) {
            // back up the old config
            File oldFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
            File newFile = new File(plugin.getDataFolder() + File.separator + "config_" + System.currentTimeMillis() + ".yml");
            FileUtil.copy(oldFile, newFile);
            // clear old world settings
            plugin.getMonstersConfig().set("angels.worlds", null);
            plugin.getMonstersConfig().set("cybermen.worlds", null);
            plugin.getMonstersConfig().set("daleks.worlds", null);
            plugin.getMonstersConfig().set("empty_child.worlds", null);
            plugin.getMonstersConfig().set("ice_warriors.worlds", null);
            plugin.getMonstersConfig().set("silurians.worlds", null);
            plugin.getMonstersConfig().set("sontarans.worlds", null);
            plugin.getMonstersConfig().set("vashta_nerada.worlds", null);
            plugin.getMonstersConfig().set("zygons.worlds", null);
            i++;
        }
        // add new world settings
        for (World w : plugin.getServer().getWorlds()) {
            String n = sanitiseName(w.getName());
            // set TARDIS worlds, nether and end worlds to zero by default
            int m = (config.contains("spawn_rate.default_max", true)) ? config.getInt("spawn_rate.default_max") : 0;
            if (!config.contains("angels.worlds." + n, true)) {
                plugin.getMonstersConfig().set("angels.worlds." + n, m);
                i++;
            }
            if (!config.contains("cybermen.worlds." + n, true)) {
                plugin.getMonstersConfig().set("cybermen.worlds." + n, m);
                i++;
            }
            if (!config.contains("daleks.worlds." + n, true)) {
                plugin.getMonstersConfig().set("daleks.worlds." + n, m);
                i++;
            }
            if (!config.contains("empty_child.worlds." + n, true)) {
                plugin.getMonstersConfig().set("empty_child.worlds." + n, m);
                i++;
            }
            if (!config.contains("hath.worlds." + n, true)) {
                plugin.getMonstersConfig().set("hath.worlds." + n, m);
                i++;
            }
            if (!config.contains("headless_monks.worlds." + n, true)) {
                plugin.getMonstersConfig().set("headless_monks.worlds." + n, m);
                i++;
            }
            if (!config.contains("ice_warriors.worlds." + n, true)) {
                plugin.getMonstersConfig().set("ice_warriors.worlds." + n, m);
                i++;
            }
            if (!config.contains("judoon.worlds." + n, true) || (config.contains("judoon.worlds." + n, true) && "true".equals(config.getString("judoon.worlds." + n)))) {
                plugin.getMonstersConfig().set("judoon.worlds." + n, m);
                i++;
            }
            if (!config.contains("k9.worlds." + n, true)) {
                plugin.getMonstersConfig().set("k9.worlds." + n, true);
                i++;
            }
            if (!config.contains("ood.worlds." + n, true) || (config.contains("ood.worlds." + n, true) && config.getInt("ood.worlds." + n) == 20)) {
                plugin.getMonstersConfig().set("ood.worlds." + n, true);
                i++;
            }
            if (!config.contains("racnoss.worlds." + n, true) && w.getEnvironment() == Environment.NETHER) {
                plugin.getMonstersConfig().set("racnoss.worlds." + n, m);
                i++;
            }
            if (!config.contains("sea_devils.worlds." + n, true)) {
                plugin.getMonstersConfig().set("sea_devils.worlds." + n, m);
                i++;
            }
            if (!config.contains("silent.worlds." + n, true)) {
                plugin.getMonstersConfig().set("silent.worlds." + n, m);
                i++;
            }
            if (!config.contains("silurians.worlds." + n, true)) {
                plugin.getMonstersConfig().set("silurians.worlds." + n, m);
                i++;
            }
            if (!config.contains("slitheen.worlds." + n, true)) {
                plugin.getMonstersConfig().set("slitheen.worlds." + n, m);
                i++;
            }
            if (!config.contains("sontarans.worlds." + n, true)) {
                plugin.getMonstersConfig().set("sontarans.worlds." + n, m);
                i++;
            }
            if (!config.contains("the_mire.worlds." + n, true)) {
                plugin.getMonstersConfig().set("the_mire.worlds." + n, m);
                i++;
            }
            if (!config.contains("toclafane.worlds." + n, true)) {
                plugin.getMonstersConfig().set("toclafane.worlds." + n, m);
                i++;
            }
            if (!config.contains("vashta_nerada.worlds." + n, true)) {
                plugin.getMonstersConfig().set("vashta_nerada.worlds." + n, m);
                i++;
            }
            if (!config.contains("zygons.worlds." + n, true)) {
                plugin.getMonstersConfig().set("zygons.worlds." + n, m);
                i++;
            }
        }
        if (i > 0) {
            try {
                String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
                config.save(new File(monstersPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.MONSTERS, "Updated monsters.yml");
            } catch (IOException io) {
                plugin.debug("Could not save monsters.yml, " + io.getMessage());
            }
        }
    }
}
