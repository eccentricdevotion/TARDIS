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
package me.eccentric_nz.TARDIS.chameleon.shell;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISShellRoomConstructor {

    public static final int[] orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
    public static final int[] orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
    private final TARDIS plugin;
    private final String GLASS = addQuotes(TARDISConstants.GLASS.getAsString());
    private Block sign;

    public TARDISShellRoomConstructor(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static String addQuotes(String s) {
        return "\"" + s + "\"";
    }

    public void createShell(Player player, int id, Block block, int cid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Location block_loc = block.getLocation();
            World w = block_loc.getWorld();
            int fx = block_loc.getBlockX() + 2;
            int fy = block_loc.getBlockY() + 1;
            int fz = block_loc.getBlockZ() - 1;
            // do a check to see if there are actually any blocks there
            // must be at least one block and one door
            boolean hasBlock = false;
            boolean hasDoor = false;
            boolean hasPrecious = false;
            for (int c = 0; c < 10; c++) {
                for (int y = fy; y < fy + 4; y++) {
                    Block fb = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                    if (!fb.getType().isAir()) {
                        if (TARDISMaterials.doors.contains(fb.getType())) {
                            hasDoor = true;
                        } else {
                            if (!plugin.getConfig().getBoolean("allow.all_blocks")
                                    && TARDISMaterials.precious.contains(fb.getType()) || Tag.WOOL_CARPETS.isTagged(fb.getType())) {
                                hasPrecious = true;
                            }
                            hasBlock = true;
                        }
                    }
                }
            }
            if (!hasBlock || !hasDoor) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SHELL_MIN_BLOCKS");
                return;
            }
            if (hasPrecious) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_NOT_CUSTOM");
                return;
            }
//            Tardis tardis = rs.getTardis();
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_SCAN");
            StringBuilder sb_blue_data = new StringBuilder("[");
            StringBuilder sb_stain_data = new StringBuilder("[");
            StringBuilder sb_glass_data = new StringBuilder("[");
            for (int c = 0; c < 10; c++) {
                sb_blue_data.append("[");
                sb_stain_data.append("[");
                sb_glass_data.append("[");
                for (int y = fy; y < (fy + 4); y++) {
                    Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                    Material material = b.getType();
                    BlockData data = b.getBlockData();
                    String dataStr = addQuotes(data.getAsString());
                    if (Tag.WALL_SIGNS.isTagged(material)) {
                        sign = b;
                    }
                    if (y == (fy + 3)) {
                        sb_blue_data.append(addQuotes(data.getAsString()));
                        if (TARDISMaterials.not_glass.contains(material)) {
                            sb_stain_data.append(dataStr);
                            sb_glass_data.append(dataStr);
                        } else {
                            Material colour = TARDISStainedGlassLookup.stainedGlassFromMaterial(w, material);
                            sb_stain_data.append(addQuotes(colour.createBlockData().getAsString()));
                            sb_glass_data.append(GLASS);
                        }
                    } else {
                        sb_blue_data.append(addQuotes(data.getAsString())).append(",");
                        if (TARDISMaterials.not_glass.contains(material)) {
                            sb_stain_data.append(dataStr).append(",");
                            sb_glass_data.append(dataStr).append(",");
                        } else {
                            Material colour = TARDISStainedGlassLookup.stainedGlassFromMaterial(w, material);
                            sb_stain_data.append(addQuotes(colour.createBlockData().getAsString())).append(",");
                            sb_glass_data.append(GLASS).append(",");
                        }
                    }
                }
                if (c == 9) {
                    sb_blue_data.append("]");
                    sb_stain_data.append("]");
                    sb_glass_data.append("]");
                } else {
                    sb_blue_data.append("],");
                    sb_stain_data.append("],");
                    sb_glass_data.append("],");
                }
            }
            sb_blue_data.append("]");
            sb_stain_data.append("]");
            sb_glass_data.append("]");
            String jsonBlue = sb_blue_data.toString();
            String jsonStain = sb_stain_data.toString();
            String jsonGlass = sb_glass_data.toString();
            // save or update the chameleon construct
            HashMap<String, Object> set = new HashMap<>();
            set.put("blueprintData", jsonBlue);
            set.put("stainData", jsonStain);
            set.put("glassData", jsonGlass);
            // read the sign
            if (sign != null) {
                Sign s = (Sign) sign.getState();
                SignSide front = s.getSide(Side.FRONT);
                String s1 = (ComponentUtils.stripColour(front.line(0)).contains("&PLAYER")) ? player.getName() + "'s" : ComponentUtils.stripColour(front.line(0));
                String s2 = (ComponentUtils.stripColour(front.line(1)).contains("&PLAYER")) ? player.getName() + "'s" : ComponentUtils.stripColour(front.line(1));
                String s3 = (ComponentUtils.stripColour(front.line(2)).contains("&PLAYER")) ? player.getName() + "'s" : ComponentUtils.stripColour(front.line(2));
                String s4 = (ComponentUtils.stripColour(front.line(3)).contains("&PLAYER")) ? player.getName() + "'s" : ComponentUtils.stripColour(front.line(3));
                set.put("line1", s1);
                set.put("line2", s2);
                set.put("line3", s3);
                set.put("line4", s4);
            }
            set.put("active", 1);
            set.put("tardis_id", id);
            if (cid != -1) {
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("chameleon_id", cid);
                plugin.getQueryFactory().doUpdate("chameleon", set, wherec);
            } else {
                plugin.getQueryFactory().doInsert("chameleon", set);
            }
            buildConstruct(rs.getTardis().getPreset().toString(), id, player);
        }
    }

    private void buildConstruct(String preset, int id, Player player) {
        // take their artron energy
        HashMap<String, Object> wheree = new HashMap<>();
        wheree.put("tardis_id", id);
        plugin.getQueryFactory().alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("shell") * -1, wheree, player);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_CONSTRUCTED");
        // update tardis table
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("chameleon_preset", "CONSTRUCT");
        sett.put("chameleon_demat", preset);
        sett.put("adapti_on", 0);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
        // update chameleon sign
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        whereh.put("type", 31);
        ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
        if (rsc.resultSet()) {
            for (HashMap<String, String> map : rsc.getData()) {
                TARDISStaticUtils.setSign(map.get("location"), 3, "CONSTRUCT", player);
            }
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", Control.FRAME.getId());
        ResultSetControls rsf = new ResultSetControls(plugin, where, false);
        if (rsf.resultSet()) {
            new TARDISChameleonFrame().updateChameleonFrame(ChameleonPreset.CONSTRUCT, rsf.getLocation());
        }
        plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Construct", plugin);
        // rebuild
        player.performCommand("tardis rebuild");
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getChameleonUses();
            new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
        }
    }
}
