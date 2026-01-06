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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsole;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupied;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

/**
 * @author eccentric_nz
 */
public class ControlRunnable implements Runnable {

    private final TARDIS plugin;
    private int modulo = 0;

    public ControlRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // get occupied TARDISes
        ResultSetOccupied rso = new ResultSetOccupied(plugin);
        rso.resultSetAsync(resultSetOccupied -> {
            for (int id : rso.getData()) {
                ResultSetConsole rsc = new ResultSetConsole(plugin, id);
                if (modulo % 2 == 0) {
                    // get the location data
                    rsc.locationAsync((hasResult, resultSetConsole) -> {
                        if (hasResult) {
                            if (Tag.ALL_SIGNS.isTagged(resultSetConsole.getSign().getType())) {
                                // get the sign
                                Sign sign = (Sign) resultSetConsole.getSign().getState();
                                SignSide front = sign.getSide(Side.FRONT);
                                // update the data
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    front.line(0, Component.text("Drifting", NamedTextColor.DARK_PURPLE));
                                    front.line(1, Component.text("in the", NamedTextColor.DARK_PURPLE));
                                    front.line(2, Component.text("time", NamedTextColor.DARK_PURPLE));
                                    front.line(3, Component.text("vortex...", NamedTextColor.DARK_PURPLE));
                                } else {
                                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + resultSetConsole.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.isEmpty()) {
                                        worldName = plugin.getMVHelper().getAlias(worldName);
                                    }
                                    front.line(0, Component.text(worldName, NamedTextColor.DARK_PURPLE));
                                    front.line(1, Component.text(resultSetConsole.getX(), NamedTextColor.BLACK));
                                    front.line(2, Component.text(resultSetConsole.getY(), NamedTextColor.BLACK));
                                    front.line(3, Component.text(resultSetConsole.getZ(), NamedTextColor.BLACK));
                                }
                                sign.update();
                            }
                        }
                    });
                } else {
                    // get the artron data
                    rsc.artronAsync((hasResult, resultSetConsole) -> {
                        if (hasResult) {
                            if (Tag.ALL_SIGNS.isTagged(resultSetConsole.getSign().getType())) {
                                // get the sign
                                Sign sign = (Sign) resultSetConsole.getSign().getState();
                                SignSide front = sign.getSide(Side.FRONT);
                                // update the data
                                front.line(0, Component.text(plugin.getLanguage().getString("ARTRON_DISPLAY", "Artron Energy"), NamedTextColor.BLACK));
                                front.line(1, Component.text(resultSetConsole.getArtronLevel(), NamedTextColor.AQUA));
                                front.line(2, Component.text(plugin.getLanguage().getString("CHAM_DISPLAY", "Exterior"), NamedTextColor.BLACK));
                                Component preset;
                                if (resultSetConsole.getPreset().startsWith("POLICE_BOX_")) {
                                    NamedTextColor colour = TARDISStaticUtils.policeBoxToNamedTextColor(resultSetConsole.getPreset());
                                    preset = Component.text("POLICE_BOX", colour);
                                } else {
                                    preset = Component.text(resultSetConsole.getPreset().replace("ITEM:", ""), NamedTextColor.BLUE);
                                }
                                front.line(3, preset);
                                sign.update();
                            }
                        }
                    });
                }
            }
        });
        modulo++;
        if (modulo == 2) {
            modulo = 0;
        }
    }
}
