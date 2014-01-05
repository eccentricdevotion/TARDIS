/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDiskWriterCommand {

    private final TARDIS plugin;
    private final List<String> disks = new ArrayList<String>();

    public TARDISDiskWriterCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.disks.add("Save Storage Disk");
        this.disks.add("Player Storage Disk");
        this.disks.add("Biome Storage Disk");
        this.disks.add("Preset Storage Disk");
    }

    public boolean writeSave(Player player, String[] args) {
        ItemStack is = player.getItemInHand();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (is.getItemMeta().getDisplayName().equals("Save Storage Disk")) {
                List<String> lore = im.getLore();
                if (!lore.get(0).equals("Blank")) {
                    player.sendMessage(plugin.pluginName + "You can only write to a blank disk!");
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    player.sendMessage(plugin.pluginName + "That doesn't appear to be a valid save name (it may be too long or contain spaces).");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS);
                    return false;
                } else {
                    int id = rs.getTardis_id();
                    PRESET preset = rs.getPreset();
                    // check has unique name
                    HashMap<String, Object> wherename = new HashMap<String, Object>();
                    wherename.put("tardis_id", id);
                    wherename.put("dest_name", args[1]);
                    wherename.put("type", 0);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                    if (rsd.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You already have a save with that name!");
                        return true;
                    }
                    // get current destination
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", rs.getTardis_id());
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                        return true;
                    }
                    lore.set(0, args[1]);
                    lore.set(1, rsc.getWorld().getName());
                    lore.set(2, "" + rsc.getX());
                    lore.set(3, "" + rsc.getY());
                    lore.set(4, "" + rsc.getZ());
                    lore.set(5, preset.toString());
                    lore.set(6, rsc.getDirection().toString());
                    lore.set(7, (rsc.isSubmarine()) ? "true" : "false");
                    im.setLore(lore);
                    is.setItemMeta(im);
                    player.sendMessage(plugin.pluginName + "Location successfully saved to disk!");
                    return true;
                }
            }
        }
        return true;
    }

    public boolean writePlayer(Player player, String[] args) {
        ItemStack is = player.getItemInHand();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (is.getItemMeta().getDisplayName().equals("Player Storage Disk")) {
                List<String> lore = im.getLore();
                if (!lore.get(0).equals("Blank")) {
                    player.sendMessage(plugin.pluginName + "You can only write to a blank disk!");
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(plugin.pluginName + "Too few command arguments!");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    player.sendMessage(plugin.pluginName + "That doesn't appear to be a valid username");
                    return false;
                }
                if (player.getName().equalsIgnoreCase(args[1])) {
                    player.sendMessage(plugin.pluginName + "You cannot save yourself to disk!");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS);
                    return false;
                } else {
                    lore.set(0, args[1]);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    player.sendMessage(plugin.pluginName + "Player successfully saved to disk!");
                    return true;
                }
            }
        }
        return true;
    }

    public boolean eraseDisk(Player player) {
        ItemStack is = player.getItemInHand();
        if (is.hasItemMeta() && disks.contains(is.getItemMeta().getDisplayName())) {
            ItemMeta im = is.getItemMeta();
            List<String> lore = Arrays.asList(new String[]{"Blank"});
            im.setLore(lore);
            is.setItemMeta(im);
            player.sendMessage(plugin.pluginName + "Disk erased successfully!");
            return true;
        } else {
            player.sendMessage(plugin.pluginName + "You must be holding a Storage Disk in your hand!");
            return true;
        }
    }
}
