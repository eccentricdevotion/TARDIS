/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISShellRoomConstructor {

    private final TARDIS plugin;
    private final int[] orderx;
    private final int[] orderz;
    private Block sign;

    public TARDISShellRoomConstructor(TARDIS plugin) {
        this.plugin = plugin;
        this.orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
        this.orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
    }

    public void createShell(Player player, int id, Block block) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            Location block_loc = block.getLocation();
            World w = block_loc.getWorld();
            int fx = block_loc.getBlockX() + 2;
            int fy = block_loc.getBlockY() + 1;
            int fz = block_loc.getBlockZ() - 1;
            TARDISMessage.send(player, "PRESET_SCAN");
            StringBuilder sb_id = new StringBuilder("[");
            StringBuilder sb_data = new StringBuilder("[");
            // TODO get all block data as need to keep sign...
            StringBuilder sb_stain_mat = new StringBuilder("[");
            StringBuilder sb_glass_id = new StringBuilder("[");
            for (int c = 0; c < 10; c++) {
                sb_id.append("[");
                sb_data.append("[");
                sb_stain_mat.append("[");
                sb_glass_id.append("[");
                for (int y = fy; y < (fy + 4); y++) {
                    Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                    Material material = b.getType();
                    String matStr = material.toString();
                    if (material.equals(Material.SPONGE)) {
                        matStr = "AIR"; // convert sponge to air
                    }
                    if (material.equals(Material.WALL_SIGN)) {
                        sign = b;
                    }
                    BlockData data = b.getBlockData();
                    if (y == (fy + 3)) {
                        sb_id.append(matStr);
                        sb_data.append(data.getAsString());
                        if (TARDISMaterials.not_glass.contains(material)) {
                            sb_stain_mat.append(matStr);
                            sb_glass_id.append(matStr);
                        } else {
                            String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                            sb_stain_mat.append(colour);
                            sb_glass_id.append("GLASS");
                        }
                    } else {
                        sb_id.append(matStr).append(",");
                        sb_data.append(data.getAsString()).append(",");
                        if (TARDISMaterials.not_glass.contains(material)) {
                            sb_stain_mat.append(matStr).append(",");
                            sb_glass_id.append(matStr).append(",");
                        } else {
                            String colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).toString();
                            sb_stain_mat.append(colour).append(",");
                            sb_glass_id.append("GLASS,");
                        }
                    }
                }
                if (c == 9) {
                    sb_id.append("]");
                    sb_data.append("]");
                    sb_stain_mat.append("]");
                    sb_glass_id.append("]");
                } else {
                    sb_id.append("],");
                    sb_data.append("],");
                    sb_stain_mat.append("],");
                    sb_glass_id.append("],");
                }
            }
            sb_id.append("]");
            sb_data.append("]");
            sb_stain_mat.append("]");
            sb_glass_id.append("]");
            String ids = sb_id.toString();
            String datas = sb_data.toString();
            String stain_ids = sb_stain_mat.toString();
            String glass_ids = sb_glass_id.toString();
            String jsonBlue = new JSONArray(ids).toString();
            String jsonData = new JSONArray(datas).toString();
            String jsonStain = new JSONArray(stain_ids).toString();
            String jsonGlass = new JSONArray(glass_ids).toString();
            // save chameleon construct
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<>();
            set.put("blueprintID", jsonBlue);
            set.put("blueprintData", jsonData);
            set.put("stainID", jsonStain);
            set.put("glassID", jsonGlass);
            // read the sign
            if (sign != null) {
                Sign s = (Sign) sign;
                String s1 = (s.getLine(0).contains("&PLAYER")) ? player.getName() : s.getLine(0);
                String s2 = (s.getLine(1).contains("&PLAYER")) ? player.getName() : s.getLine(1);
                String s3 = (s.getLine(2).contains("&PLAYER")) ? player.getName() : s.getLine(2);
                String s4 = (s.getLine(3).contains("&PLAYER")) ? player.getName() : s.getLine(3);
                set.put("line1", s1);
                set.put("line2", s2);
                set.put("line3", s3);
                set.put("line4", s4);
            }
            if (rsc.resultSet()) {
                // update
                HashMap<String, Object> whereu = new HashMap<>();
                whereu.put("tardis_id", id);
                qf.doUpdate("chameleon", set, whereu);
            } else {
                // insert
                set.put("tardis_id", id);
                qf.doInsert("chameleon", set);
            }
            buildConstruct(tardis.getPreset().toString(), id, qf, tardis.getChameleon(), player);
        }
    }

    private void buildConstruct(String preset, int id, QueryFactory qf, String location, Player player) {
        // take their artron energy
        HashMap<String, Object> wheree = new HashMap<>();
        wheree.put("tardis_id", id);
        qf.alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("shell") * -1, wheree, player);
        TARDISMessage.send(player, "PRESET_CONSTRUCTED");
        // update tardis table
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("chameleon_preset", "CONSTRUCT");
        sett.put("chameleon_demat", preset);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        qf.doUpdate("tardis", sett, wheret);
        // update chameleon sign
        TARDISStaticUtils.setSign(location, 3, "CONSTRUCT", player);
        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Construct");
        // rebuild
        player.performCommand("tardis rebuild");
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getChameleonUses();
            new TARDISCircuitDamager(plugin, DISK_CIRCUIT.CHAMELEON, uses_left, id, player).damage();
        }
    }
}
