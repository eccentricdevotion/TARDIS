/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonCommand {

    private final TARDIS plugin;

    public TARDISChameleonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean doChameleon(Player player, String[] args) {
        if (!plugin.getConfig().getBoolean("travel.chameleon")) {
            TARDISMessage.send(player, "CHAM_DISABLED");
            return false;
        }
        if (player.hasPermission("tardis.timetravel")) {
            if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off") && !args[1].equalsIgnoreCase("short") && !args[1].equalsIgnoreCase("reset"))) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            // get the players TARDIS id
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return false;
            }
            int id = rs.getTardis_id();
            TARDISCircuitChecker circ_chk = null;
            if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                circ_chk = new TARDISCircuitChecker(plugin, id);
                circ_chk.getCircuits();
            }
            if (circ_chk != null && !circ_chk.hasChameleon()) {
                TARDISMessage.send(player, "CHAM_MISSING");
                return true;
            }
            String chamStr = rs.getChameleon();
            if (chamStr.isEmpty()) {
                TARDISMessage.send(player, "CHAM_NOT_FOUND");
                return false;
            } else {
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> tid = new HashMap<String, Object>();
                HashMap<String, Object> set = new HashMap<String, Object>();
                tid.put("tardis_id", id);
                if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off")) {
                    int x, y, z;
                    String[] chamData = chamStr.split(":");
                    World w = plugin.getServer().getWorld(chamData[0]);
                    x = TARDISNumberParsers.parseInt(chamData[1]);
                    y = TARDISNumberParsers.parseInt(chamData[2]);
                    z = TARDISNumberParsers.parseInt(chamData[3]);
                    Block chamBlock = w.getBlockAt(x, y, z);
                    Material chamType = chamBlock.getType();
                    if (chamType == Material.WALL_SIGN || chamType == Material.SIGN_POST) {
                        Sign cs = (Sign) chamBlock.getState();
                        if (args[1].equalsIgnoreCase("on")) {
                            set.put("chamele_on", 1);
                            qf.doUpdate("tardis", set, tid);
                            TARDISMessage.send(player, "CHAM_ON");
                            cs.setLine(3, ChatColor.GREEN + plugin.getLanguage().getString("SET_ON"));
                        }
                        if (args[1].equalsIgnoreCase("off")) {
                            set.put("chamele_on", 0);
                            qf.doUpdate("tardis", set, tid);
                            TARDISMessage.send(player, "CHAM_OFF");
                            cs.setLine(3, ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
                        }
                        cs.update();
                    }
                }
                int dwid = plugin.getConfig().getInt("police_box.wall_id");
                int dwd = plugin.getConfig().getInt("police_box.wall_data");
                if (args[1].equalsIgnoreCase("short")) {
                    // get the block the player is targeting
                    Block target_block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
                    TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                    int[] b_data = tcc.getChameleonBlock(target_block, player, true);
                    int c_id = b_data[0], c_data = b_data[1];
                    set.put("chameleon_id", c_id);
                    set.put("chameleon_data", c_data);
                    qf.doUpdate("tardis", set, tid);
                    boolean bluewool = (c_id == dwid && c_data == (byte) dwd);
                    if (!bluewool) {
                        TARDISMessage.send(player, "CHAM_SET", target_block.getType().toString());
                    }
                }
                if (args[1].equalsIgnoreCase("reset")) {
                    set.put("chameleon_id", dwid);
                    set.put("chameleon_data", dwd);
                    qf.doUpdate("tardis", set, tid);
                    TARDISMessage.send(player, "CHAM_REPAIR");
                }
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
