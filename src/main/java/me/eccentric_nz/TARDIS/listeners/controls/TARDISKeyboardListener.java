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

import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISSign;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public static boolean isKeyboardEditor(ItemStack is) {
        if (is != null && is.getType().equals(Material.OAK_SIGN) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            return im.hasDisplayName() && im.getDisplayName().equals("TARDIS Keyboard Editor") && im.hasCustomModelData();
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyboardInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b != null && Tag.SIGNS.isTagged(b.getType())) {
            Player player = event.getPlayer();
            Sign sign = (Sign) b.getState();
            if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase("[TARDIS Wiki]")) {
                TARDISSign.sendSignLink(player);
                return;
            }
            String loc = event.getClickedBlock().getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("type", 7);
            where.put("location", loc);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                TARDISCircuitChecker tcc = null;
                if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                    tcc = new TARDISCircuitChecker(plugin, rs.getTardis_id());
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasInput()) {
                    TARDISMessage.send(player, "INPUT_MISSING");
                    return;
                }
                plugin.getTrackerKeeper().getSign().put(loc, sign);
                player.openSign(sign);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        String loc = event.getBlock().getLocation().toString();
        if (!plugin.getTrackerKeeper().getSign().containsKey(loc)) {
            return;
        }
        Player p = event.getPlayer();
        plugin.getTrackerKeeper().getSign().remove(loc);
        // process the lines...
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (rs.resultSet()) {
            // wiki?
            if (event.getLine(0).equalsIgnoreCase("wiki")) {
                TARDISSign.sendSignLink(p);
                return;
            }
            int id = rs.getTardis_id();
            // player?
            if (plugin.getServer().getPlayer(event.getLine(0)) != null) {
                // set location player
                p.performCommand("tardistravel " + event.getLine(0));
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel " + event.getLine(0));
                return;
            }
            // location?
            if (TARDISAliasResolver.getWorldFromAlias(event.getLine(0)) != null) {
                // set location to coords
                String command = event.getLine(0) + " " + event.getLine(1) + " " + event.getLine(2) + " " + event.getLine(3);
                p.performCommand("tardistravel " + command);
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel " + command);
                return;
            }
            // home?
            if (event.getLine(0).equalsIgnoreCase("home")) {
                // check not already at home location
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("tardis_id", id);
                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
                if (rsh.resultSet()) {
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                    if (rsc.resultSet()) {
                        if (currentIsNotHome(rsh, rsc)) {
                            p.performCommand("tardistravel home");
                            plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel home");
                        } else {
                            TARDISMessage.send(p, "HOME_ALREADY");
                        }
                    } else {
                        TARDISMessage.send(p, "CURRENT_NOT_FOUND");
                    }
                } else {
                    TARDISMessage.send(p, "HOME_NOT_FOUND");
                }
                return;
            }
            if (event.getLine(0).equalsIgnoreCase("cave") && TARDISPermission.hasPermission(p, "tardis.timetravel.cave")) {
                p.performCommand("tardistravel cave");
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel cave");
                return;
            }
            if (event.getLine(0).equalsIgnoreCase("village") && plugin.getConfig().getBoolean("allow.village_travel") && TARDISPermission.hasPermission(p, "tardis.timetravel.village")) {
                p.performCommand("tardistravel village");
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel village");
                return;
            }
            // biome ?
            try {
                String upper = event.getLine(0).toUpperCase(Locale.ENGLISH);
                Biome.valueOf(upper);
                if (!upper.equals("HELL") && !upper.equals("SKY") && !upper.equals("VOID")) {
                    p.performCommand("tardistravel biome " + upper);
                    plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel biome " + upper);
                    return;
                }
            } catch (IllegalArgumentException iae) {
                plugin.debug(plugin.getLanguage().getString("BIOME_NOT_VALID"));
            }
            // dest?
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", event.getLine(0));
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (rsd.resultSet()) {
                p.performCommand("tardistravel dest " + event.getLine(0));
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel dest " + event.getLine(0));
                return;
            }
            // area?
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("area_name", event.getLine(0));
            ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
            if (rsa.resultSet()) {
                p.performCommand("tardistravel area " + event.getLine(0));
                plugin.getLogger().log(Level.INFO, p.getName() + " issued server command: /tardistravel area " + event.getLine(0));
                return;
            }
        } else {
            plugin.debug("Player is not in a TARDIS!");
        }
        TARDISMessage.send(p, "KEYBOARD_ERROR");
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
