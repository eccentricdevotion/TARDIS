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
package me.eccentric_nz.TARDIS.console;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupiedScreen;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreen;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
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
        ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text();
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            builder.color(NamedTextColor.DARK_PURPLE)
                    .append(Component.text("Drifting\n"))
                    .append(Component.text("in the\n"))
                    .append(Component.text("time\n"))
                    .append(Component.text("vortex..."));
            textDisplay.text(builder.build());
        } else if (coords) {
            rss.locationAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + resultSetConsole.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.isEmpty()) {
                        worldName = plugin.getMVHelper().getAlias(worldName);
                    }
                    builder.append(Component.text(worldName))
                            .append(Component.newline())
                            .append(Component.text(resultSetConsole.getX(), NamedTextColor.WHITE))
                            .append(Component.newline())
                            .append(Component.text(resultSetConsole.getY(), NamedTextColor.WHITE))
                            .append(Component.newline())
                            .append(Component.text(resultSetConsole.getZ(), NamedTextColor.WHITE));
                    textDisplay.text(builder.build());
                }
            });
        } else {
            // get the artron data
            rss.artronAsync((hasResult, resultSetConsole) -> {
                if (hasResult) {
                    builder.append(Component.text(plugin.getLanguage().getString("ARTRON_DISPLAY", "Artron Energy"), NamedTextColor.WHITE))
                            .append(Component.newline())
                            .append(Component.text(resultSetConsole.getArtronLevel(), NamedTextColor.AQUA))
                            .append(Component.newline())
                            .append(Component.text(plugin.getLanguage().getString("CHAM_DISPLAY", "Exterior"), NamedTextColor.WHITE))
                            .append(Component.newline());
                    Component preset;
                    if (resultSetConsole.getPreset().startsWith("POLICE_BOX_")) {
                        NamedTextColor colour = TARDISStaticUtils.policeBoxToNamedTextColor(resultSetConsole.getPreset());
                        preset = Component.text("POLICE_BOX", colour);
                    } else {
                        preset = Component.text(resultSetConsole.getPreset().replace("ITEM:", ""), NamedTextColor.BLUE);
                    }
                    builder.append(preset);
                    textDisplay.text(builder.build());
                }
            });
        }
    }
}
