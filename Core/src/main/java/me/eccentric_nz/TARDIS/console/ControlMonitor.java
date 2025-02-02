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
package me.eccentric_nz.TARDIS.console;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupiedScreen;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreen;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.UUID;

public class ControlMonitor implements Runnable {

    private final TARDIS plugin;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.4f, 0.4f, 0.4f), TARDISConstants.AXIS_ANGLE_ZERO);
    private int modulo = 0;

    public ControlMonitor(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ResultSetOccupiedScreen rsos = new ResultSetOccupiedScreen(plugin);
        rsos.resultSetAsync(resultSetOccupied -> {
            for (Pair<Integer, UUID> pair : rsos.getData()) {
                update(pair.getFirst(), pair.getSecond(), modulo % 2 == 0);
            }
        });
        modulo++;
        if (modulo == 2) {
            modulo = 0;
        }
    }

    public void update(int id, UUID uuid, boolean coords) {
        Entity entity = plugin.getServer().getEntity(uuid);
        if (!(entity instanceof TextDisplay textDisplay)) {
            return;
        }
        textDisplay.setTransformation(transformation);
        textDisplay.setBillboard(Display.Billboard.FIXED);
        textDisplay.setBackgroundColor(Color.fromRGB(8, 10, 15));
        textDisplay.setSeeThrough(false);
        // get text
        ResultSetScreen rss = new ResultSetScreen(plugin, id);
        StringBuilder builder = new StringBuilder();
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            builder.append(ChatColor.DARK_PURPLE)
                    .append("Drifting\n")
                    .append("in the\n")
                    .append("time\n")
                    .append("vortex...");
            textDisplay.setText(builder.toString());
        } else if (coords) {
            rss.locationAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + resultSetConsole.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.isEmpty()) {
                        worldName = plugin.getMVHelper().getAlias(worldName);
                    }
                    builder.append(ChatColor.DARK_PURPLE)
                            .append(worldName)
                            .append("\n")
                            .append(ChatColor.WHITE)
                            .append(resultSetConsole.getX())
                            .append("\n")
                            .append(resultSetConsole.getY())
                            .append("\n")
                            .append(resultSetConsole.getZ());
                    textDisplay.setText(builder.toString());
                }
            });
        } else {
            // get the artron data
            rss.artronAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    builder.append(ChatColor.WHITE)
                            .append(plugin.getLanguage().getString("ARTRON_DISPLAY"))
                            .append("\n")
                            .append(ChatColor.AQUA)
                            .append(resultSetConsole.getArtronLevel())
                            .append("\n")
                            .append(ChatColor.WHITE)
                            .append(plugin.getLanguage().getString("CHAM_DISPLAY"))
                            .append("\n");
                    String preset;
                    if (resultSetConsole.getPreset().startsWith("POLICE_BOX_")) {
                        ChatColor colour = TARDISStaticUtils.policeBoxToChatColor(resultSetConsole.getPreset());
                        preset = colour + "POLICE_BOX";
                    } else {
                        preset = ChatColor.BLUE + resultSetConsole.getPreset().replace("ITEM:", "");
                    }
                    builder.append(preset);
                    textDisplay.setText(builder.toString());
                }
            });
        }
    }
}
