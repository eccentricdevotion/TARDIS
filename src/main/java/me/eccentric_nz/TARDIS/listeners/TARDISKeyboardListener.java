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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Now, if the trachoid crystal contrafibulations are in synchronic resonance
 * with the referential difference index, then this should take us right to the
 * heart of the trouble. And they don't make sentences like that anymore.
 *
 * @author eccentric_nz
 */
public class TARDISKeyboardListener implements Listener {

    private final TARDIS plugin;

    public TARDISKeyboardListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            return;
        }
        Block against = event.getBlockAgainst();
        // is it a TARDIS keyboard sign
        String loc_str = against.getLocation().toString();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("type", 7);
        where.put("location", loc_str);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            if (plugin.getPM().isPluginEnabled("ProtocolLib")) {
                event.setCancelled(true);
            }
            TARDISCircuitChecker tcc = null;
            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                tcc = new TARDISCircuitChecker(plugin, rsc.getTardis_id());
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasInput()) {
                TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + MESSAGE.INPUT_MISSING.getText());
                return;
            }
            Sign keyboard = (Sign) against.getState();
            // track this sign
            plugin.getTrackerKeeper().getSign().put(block.getLocation().toString(), keyboard);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        String loc = event.getBlock().getLocation().toString();
        if (!plugin.getTrackerKeeper().getSign().containsKey(loc)) {
            return;
        }
        Sign keyboard = plugin.getTrackerKeeper().getSign().get(loc);
        Player p = event.getPlayer();
        if (!plugin.getPM().isPluginEnabled("ProtocolLib")) {
            int i = 0;
            for (String l : event.getLines()) {
                keyboard.setLine(i, l);
                i++;
            }
            keyboard.update();
            plugin.getTrackerKeeper().getSign().remove(loc);
            // cancel the edit and give the sign back to the player
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemStack itemInHand = p.getItemInHand();
                if ((itemInHand == null) || (itemInHand.getType() == Material.AIR)) {
                    p.setItemInHand(new ItemStack(Material.SIGN, 1));
                } else {
                    itemInHand.setAmount(itemInHand.getAmount() + 1);
                }
            }
        }
        // process the lines...
        // player?
        if (plugin.getServer().getPlayer(event.getLine(0)) != null) {
            // set location player
            p.performCommand("tardistravel " + event.getLine(0));
            plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel " + event.getLine(0));
            return;
        }
        // location?
        if (plugin.getServer().getWorld(event.getLine(0)) != null) {
            // set location to coords
            String command = event.getLine(0) + " " + event.getLine(1) + " " + event.getLine(2) + " " + event.getLine(3);
            p.performCommand("tardistravel " + command);
            plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel " + command);
            return;
        }
        // home?
        if (event.getLine(0).equalsIgnoreCase("home")) {
            p.performCommand("tardistravel home");
            plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel home");
            return;
        }
        // biome ?
        try {
            String upper = event.getLine(0).toUpperCase(Locale.ENGLISH);
            Biome biome = Biome.valueOf(upper);
            if (!upper.equals("HELL") && !upper.equals("SKY")) {
                p.performCommand("tardistravel biome " + upper);
                plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel biome " + upper);
                return;
            }
        } catch (IllegalArgumentException iae) {
            plugin.debug(MESSAGE.BIOME_NOT_VALID.getText());
        }
        // dest?
        HashMap<String, Object> whered = new HashMap<String, Object>();
        whered.put("dest_name", event.getLine(0));
        ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
        if (rsd.resultSet()) {
            p.performCommand("tardistravel dest " + event.getLine(0));
            plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel dest " + event.getLine(0));
            return;
        }
        // area?
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("area_name", event.getLine(0));
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
        if (rsa.resultSet()) {
            p.performCommand("tardistravel area " + event.getLine(0));
            plugin.getConsole().sendMessage(p.getName() + " issued server command: /tardistravel area " + event.getLine(0));
            return;
        }
        TARDISMessage.send(p, plugin.getPluginName() + "Keyboard not responding, press any key to continue.");
    }
}
