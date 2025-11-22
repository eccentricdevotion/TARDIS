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
package me.eccentric_nz.TARDIS.commands.tardis;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Lamp;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * The TARDIS scanner was the main method for the occupants of the vessel to
 * observe the outside environment. The appearance and specifications of the
 * scanner system varied significantly in the course of the Doctor's travels.
 *
 * @author eccentric_nz
 */
class TARDISLampsCommand {

    private final TARDIS plugin;
    private final List<String> subs = List.of("auto", "list", "set");

    TARDISLampsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    private boolean valid_material(String mat) {
        List<?> allowList = plugin.getLampsConfig().getList("lamp_blocks");
        if (allowList == null) {
            return false;
        }
        for (Object allow : allowList) {
            if (allow instanceof String) {
                if (((String) allow).equalsIgnoreCase(mat)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Updates TARDISes from pre-malfunction plugin versions so that the lamps
     * can flash.
     *
     * @param owner the Timelord of the TARDIS
     * @return true if the TARDIS has not been updated, otherwise false
     */
    boolean addLampBlocks(Player owner) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", owner.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            // check if they have already got lamp records
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, false);
            if (rsl.resultSet()) {
                plugin.getMessenger().send(owner, TardisModule.TARDIS, "LAMP_DELETE");
                HashMap<String, Object> wheredel = new HashMap<>();
                wheredel.put("tardis_id", id);
                plugin.getQueryFactory().doDelete("lamps", wheredel);
            }
            // get the TARDIS console chunk
            String[] tc = tardis.getChunk().split(":");
            int cx = TARDISNumberParsers.parseInt(tc[1]);
            int cz = TARDISNumberParsers.parseInt(tc[2]);
            World world = TARDISAliasResolver.getWorldFromAlias(tc[0]);
            Chunk startChunk = world.getChunkAt(cx, cz);
            int starty, endy;
            Schematic schm = tardis.getSchematic();
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                starty = schm.getStartY();
                endy = starty + h;
                String w = world.getName();
                // loop through the chunks
                for (Chunk chunk : TARDISChunkUtils.getConsoleChunks(startChunk, tardis.getSchematic())) {
                    // find the lamps in the chunks
                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;
                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = starty; yy < endy; yy++) {
                                Material mat = world.getBlockAt(xx, yy, zz).getType();
                                if (mat.equals(Material.LIGHT)) {
                                    String lamp = w + ":" + xx + ":" + yy + ":" + zz;
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("tardis_id", id);
                                    set.put("location", lamp);
                                    plugin.getQueryFactory().doInsert("lamps", set);
                                    plugin.getMessenger().send(owner, TardisModule.TARDIS, "LAMP_ADD", (xx + ":" + yy + ":" + zz));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            plugin.getMessenger().send(owner, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return false;
        }
    }

    boolean listLampBlocks(Player owner) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", owner.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
            if (rsl.resultSet()) {
                for (Lamp l : rsl.getData()) {
                    Block b = l.block();
                    plugin.getMessenger().send(owner, TardisModule.TARDIS, "LAMP_LIST", (b.getX() + ":" + b.getY() + ":" + b.getZ()));
                }
            }
        }
        return true;
    }

    boolean setLampBlock(Player player, String[] args) {
        if (args.length < 6) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_SET_USAGE");
            return false;
        }
        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(args[2]);
            y = Integer.parseInt(args[3]);
            z = Integer.parseInt(args[4]);
        } catch (NumberFormatException ignored) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_SET_USAGE");
            return false;
        }
        // First material override isn't optional
        String materialOn = null;
        materialOn = args[5];
        if (materialOn.equals("_")) {
            materialOn = "";
        } else {
            if (!valid_material(materialOn)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_BAD_MATERIAL");
                return false;
            }
        }
        // Get optional material override arguments
        String materialOff = null;
        if (args.length >= 7) {
            materialOff = args[6];
            if (materialOff.equals("_")) {
                materialOff = "";
            } else {
                if (!valid_material(materialOff)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_BAD_MATERIAL");
                    return false;
                }
            }
        }
        Float percentage = null;
        try {
            if (args.length >= 8) {
                percentage = Math.clamp(Float.parseFloat(args[7]), 0, 1);
            }
        } catch (NumberFormatException ignored) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_SET_USAGE");
            return false;
        }

        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String dimension = rs.getTardis().getChunk().split(":")[0];
            int id = tardis.getTardisId();
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            wherel.put("location", (dimension + ":" + x + ":" + y + ":" + z));

            HashMap<String, Object> set = new HashMap<>();
            set.put("material_on", materialOn);
            if (materialOff != null) {
                set.put("material_off", materialOff);
            }
            if (percentage != null) {
                set.put("percentage", percentage);
            }

            plugin.getQueryFactory().doUpdate("lamps", set, wherel);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LAMP_SET_SUCCESS", wherel.get("location"));
        }

        return true;
    }

    boolean zip(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.update")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }

        if (args.length < 2) {
            return false;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        if (!subs.contains(sub)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_VALID");
            return true;
        }

        return switch (sub) {
            case "auto" -> addLampBlocks(player);
            case "list" -> listLampBlocks(player);
            case "set" -> setLampBlock(player, args);
            default -> true;
        };
    }
}
