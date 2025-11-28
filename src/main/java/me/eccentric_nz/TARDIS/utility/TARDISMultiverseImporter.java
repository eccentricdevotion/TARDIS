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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.SpawnCategory;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.AllowedPortalType;
import org.mvplugins.multiverse.core.world.MultiverseWorld;

import java.io.File;
import java.io.IOException;

public class TARDISMultiverseImporter {

    private final TARDIS plugin;
    MultiverseCoreApi mvc = MultiverseCoreApi.get();
    private final CommandSender sender;

    public TARDISMultiverseImporter(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    /**
     * Transfer Multiverse world values to planets.yml
     */
    public void transfer() {
        int i = 0;
        for (MultiverseWorld mvw : mvc.getWorldManager().getWorlds()) {
            // only import if the world doesn't have an entry in planets.yml
            if (!plugin.getPlanetsConfig().contains("planets." + mvw.getName())) {
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".enabled", mvw.isAutoLoad());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".resource-pack", "default");
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamemode", mvw.getGameMode().toString());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".time_travel", false);
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".environment", mvw.getEnvironment().toString());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".generator", mvw.getGenerator());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".spawn_chunk_radius", mvw.isKeepSpawnInMemory() ? 2 : 0);
                if (!mvw.getEntitySpawnConfig().getSpawnCategoryConfig(SpawnCategory.ANIMAL).isSpawn() || !mvw.getEntitySpawnConfig().getSpawnCategoryConfig(SpawnCategory.MONSTER).isSpawn()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.spawn_mobs", false);
                }
                if (!mvw.isAllowWeather()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.advance_weather", false);
                }
                if (!mvw.getPvp()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.pvp", false);
                }
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".allow_portals", mvw.getPortalForm() != AllowedPortalType.NONE);
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".alias", mvw.getAlias());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".helmic_regulator_order", -1);
                String icon;
                switch (mvw.getEnvironment()) {
                    case NETHER -> icon = "NETHERRACK";
                    case THE_END -> icon = "END_STONE";
                    default -> icon = "STONE";
                }
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".icon", icon);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_TRANSFER", mvw.getName());
                i++;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_EXISTS", mvw.getName());
            }
        }
        if (i > 0) {
            // save the config
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                plugin.getPlanetsConfig().save(new File(planetsPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to planets.yml");
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_IMPORT", "" + i);
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }
}
