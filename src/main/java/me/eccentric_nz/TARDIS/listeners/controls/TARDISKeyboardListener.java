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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Locale;

/**
 * Now, if the trachoid crystal contrafibulations are in synchronic resonance with the referential difference index,
 * then this should take us right to the heart of the trouble. And they don't make sentences like that anymore.
 *
 * @author eccentric_nz
 */
public class TARDISKeyboardListener implements Listener {

    private final TARDIS plugin;

    public TARDISKeyboardListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyboardInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b == null || !Tag.SIGNS.isTagged(b.getType())) {
            return;
        }
        Player player = event.getPlayer();
        Sign sign = (Sign) b.getState();
        if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase("[TARDIS Wiki]")) {
            plugin.getMessenger().sendSign(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        String loc = event.getBlock().getLocation().toString();
        // is it a keyboard sign?
        HashMap<String, Object> wherel = new HashMap<>();
        wherel.put("type", 7);
        wherel.put("location", loc);
        ResultSetControls rsc = new ResultSetControls(plugin, wherel, false);
        if (!rsc.resultSet()) {
            return;
        }
        Player p = event.getPlayer();
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(p, false)) {
            tcc = new TARDISCircuitChecker(plugin, rsc.getTardis_id());
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasInput()) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "INPUT_MISSING");
            return;
        }
        // process the lines...
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (!rs.resultSet()) {
            return;
        }
        String firstLine = event.getLine(0);
        if (firstLine == null) {
            return;
        }
        // wiki?
        if (firstLine.equalsIgnoreCase("wiki")) {
            plugin.getMessenger().sendSign(p);
            return;
        }
        int id = rs.getTardis_id();
        // player?
        if (plugin.getServer().getPlayer(firstLine) != null) {
            // set location player
            p.performCommand("tardistravel " + firstLine);
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel " + firstLine);
            return;
        }
        // location?
        if (TARDISAliasResolver.getWorldFromAlias(firstLine) != null) {
            // set location to coords
            String command = firstLine + " " + event.getLine(1) + " " + event.getLine(2) + " " + event.getLine(3);
            p.performCommand("tardistravel " + command);
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel " + command);
            return;
        }
        // home?
        if (firstLine.equalsIgnoreCase("home")) {
            // check not already at home location
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
            if (rsh.resultSet()) {
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherec);
                if (rscl.resultSet()) {
                    if (currentIsNotHome(rsh, rscl)) {
                        p.performCommand("tardistravel home");
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel home");
                    } else {
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "HOME_ALREADY");
                    }
                } else {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                }
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "HOME_NOT_FOUND");
            }
            return;
        }
        if (firstLine.equalsIgnoreCase("cave") && TARDISPermission.hasPermission(p, "tardis.timetravel.cave")) {
            p.performCommand("tardistravel cave");
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel cave");
            return;
        }
        if (firstLine.equalsIgnoreCase("village") && plugin.getConfig().getBoolean("allow.village_travel") && TARDISPermission.hasPermission(p, "tardis.timetravel.village")) {
            p.performCommand("tardistravel village");
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel village");
            return;
        }
        // biome ?
        try {
            String upper = firstLine.toUpperCase(Locale.ENGLISH);
            Biome.valueOf(upper);
            if (!upper.equals("HELL") && !upper.equals("SKY") && !upper.equals("VOID")) {
                p.performCommand("tardistravel biome " + upper);
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel biome " + upper);
                return;
            }
        } catch (IllegalArgumentException iae) {
            plugin.debug(plugin.getLanguage().getString("BIOME_NOT_VALID"));
        }
        // dest?
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("dest_name", firstLine);
        ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
        if (rsd.resultSet()) {
            p.performCommand("tardistravel dest " + firstLine);
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel dest " + firstLine);
            return;
        }
        // area?
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("area_name", firstLine);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        if (rsa.resultSet()) {
            p.performCommand("tardistravel area " + firstLine);
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, p.getName() + " issued server command: /tardistravel area " + firstLine);
            return;
        }
        plugin.getMessenger().send(p, TardisModule.TARDIS, "KEYBOARD_ERROR");
    }

    private boolean currentIsNotHome(ResultSetHomeLocation rsh, ResultSetCurrentLocation rsc) {
        if (rsh.getWorld() != rsc.getWorld()) {
            return true;
        }
        if (rsh.getY() != rsc.getY()) {
            return true;
        }
        if (rsh.getX() != rsc.getX()) {
            return true;
        }
        return rsh.getZ() != rsc.getZ();
    }
}
