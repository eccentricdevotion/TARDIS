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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsole;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOccupied;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;

/**
 * @author eccentric_nz
 */
public class TARDISControlRunnable implements Runnable {

    private final TARDIS plugin;
    private int modulo = 0;

    public TARDISControlRunnable(TARDIS plugin) {
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
                                    front.setLine(0, ChatColor.DARK_PURPLE + "Drifting");
                                    front.setLine(1, ChatColor.DARK_PURPLE + "in the");
                                    front.setLine(2, "time");
                                    front.setLine(3, "vortex...");
                                } else {
                                    String worldName = (resultSetConsole.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(resultSetConsole.getWorld()) : "";
                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + resultSetConsole.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldName.equals("")) {
                                        worldName = plugin.getMVHelper().getAlias(worldName);
                                    }
                                    front.setLine(0, ChatColor.DARK_PURPLE + worldName);
                                    front.setLine(1, ChatColor.BLACK + resultSetConsole.getX());
                                    front.setLine(2, ChatColor.BLACK + resultSetConsole.getY());
                                    front.setLine(3, ChatColor.BLACK + resultSetConsole.getZ());
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
                                front.setLine(0, ChatColor.BLACK + plugin.getLanguage().getString("ARTRON_DISPLAY"));
                                front.setLine(1, ChatColor.AQUA + resultSetConsole.getArtronLevel());
                                front.setLine(2, ChatColor.BLACK + plugin.getLanguage().getString("CHAM_DISPLAY"));
                                String preset = "";
                                if (resultSetConsole.getPreset().startsWith("POLICE_BOX_")) {
                                    ChatColor colour = policeBoxToChatColor(resultSetConsole.getPreset());
                                    preset = colour + "POLICE_BOX";
                                } else {
                                    preset = ChatColor.BLUE + resultSetConsole.getPreset().replace("ITEM:", "");
                                }
                                front.setLine(3, preset);
                                sign.update();
                            }
                        }
                    });
                }
            }
            modulo++;
            if (modulo == 2) {
                modulo = 0;
            }
        });
    }

    private ChatColor policeBoxToChatColor(String preset) {
        switch (preset) {
            case "POLICE_BOX_WHITE" -> {
                return ChatColor.WHITE;
            }
            case "POLICE_BOX_BROWN", "POLICE_BOX_ORANGE" -> {
                return ChatColor.GOLD;
            }
            case "POLICE_BOX_BLACK" -> {
                return ChatColor.BLACK;
            }
            case "POLICE_BOX_CYAN" -> {
                return ChatColor.DARK_AQUA;
            }
            case "POLICE_BOX_LIGHT_BLUE" -> {
                return ChatColor.BLUE;
            }
            case "POLICE_BOX_GRAY" -> {
                return ChatColor.DARK_GRAY;
            }
            case "POLICE_BOX_GREEN" -> {
                return ChatColor.DARK_GREEN;
            }
            case "POLICE_BOX_PURPLE" -> {
                return ChatColor.DARK_PURPLE;
            }
            case "POLICE_BOX_RED" -> {
                return ChatColor.DARK_RED;
            }
            case "POLICE_BOX_LIGHT_GRAY" -> {
                return ChatColor.GRAY;
            }
            case "POLICE_BOX_LIME" -> {
                return ChatColor.GREEN;
            }
            case "POLICE_BOX_PINK" -> {
                return ChatColor.LIGHT_PURPLE;
            }
            case "POLICE_BOX_MAGENTA" -> {
                return ChatColor.RED;
            }
            case "POLICE_BOX_YELLOW" -> {
                return ChatColor.YELLOW;
            }
            default -> {
                return ChatColor.DARK_BLUE;
            }
        }
    }
}
