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
import java.util.Locale;

public class TARDISMultiverseImporter {

    private final TARDIS plugin;
    private final CommandSender sender;
    private final MultiverseCoreApi mvc = MultiverseCoreApi.get();

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
            String w = mvw.getName().toLowerCase(Locale.ROOT);
            // only import if the world doesn't have an entry in planets.yml
            if (!plugin.getPlanetsConfig().contains("planets." + w)) {
                plugin.getPlanetsConfig().set("planets." + w + ".enabled", mvw.isAutoLoad());
                plugin.getPlanetsConfig().set("planets." + w + ".resource-pack", "default");
                plugin.getPlanetsConfig().set("planets." + w + ".gamemode", mvw.getGameMode().toString());
                plugin.getPlanetsConfig().set("planets." + w + ".time_travel", false);
                plugin.getPlanetsConfig().set("planets." + w + ".environment", mvw.getEnvironment().toString());
                plugin.getPlanetsConfig().set("planets." + w + ".generator", mvw.getGenerator());
                plugin.getPlanetsConfig().set("planets." + w + ".spawn_chunk_radius", mvw.isKeepSpawnInMemory() ? 2 : 0);
                if (!mvw.getEntitySpawnConfig().getSpawnCategoryConfig(SpawnCategory.ANIMAL).isSpawn() || !mvw.getEntitySpawnConfig().getSpawnCategoryConfig(SpawnCategory.MONSTER).isSpawn()) {
                    plugin.getPlanetsConfig().set("planets." + w + ".gamerules.spawn_mobs", false);
                }
                if (!mvw.isAllowWeather()) {
                    plugin.getPlanetsConfig().set("planets." + w + ".gamerules.advance_weather", false);
                }
                if (!mvw.getPvp()) {
                    plugin.getPlanetsConfig().set("planets." + w + ".gamerules.pvp", false);
                }
                plugin.getPlanetsConfig().set("planets." + w + ".allow_portals", mvw.getPortalForm() != AllowedPortalType.NONE);
                plugin.getPlanetsConfig().set("planets." + w + ".alias", mvw.getAlias());
                plugin.getPlanetsConfig().set("planets." + w + ".helmic_regulator_order", -1);
                String icon;
                switch (mvw.getEnvironment()) {
                    case NETHER -> icon = "NETHERRACK";
                    case THE_END -> icon = "END_STONE";
                    default -> icon = "STONE";
                }
                plugin.getPlanetsConfig().set("planets." + w + ".icon", icon);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_TRANSFER", w);
                i++;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_EXISTS", w);
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
