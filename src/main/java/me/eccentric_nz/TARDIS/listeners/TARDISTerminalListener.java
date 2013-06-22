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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTerminalListener implements Listener {

    private final TARDIS plugin;
    private HashMap<String, String> terminalUsers = new HashMap<String, String>();
    private HashMap<String, String> terminalDestination = new HashMap<String, String>();
    private HashMap<String, TARDISConstants.COMPASS> terminalDirection = new HashMap<String, TARDISConstants.COMPASS>();
    private HashMap<String, Integer> terminalStep = new HashMap<String, Integer>();
    private HashMap<String, Integer> terminalIDs = new HashMap<String, Integer>();

    public TARDISTerminalListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Destination Terminal")) {
            final Player player = (Player) event.getWhoClicked();
            String playerNameStr = player.getName();
            // get the TARDIS the player is in
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", playerNameStr);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int slot = event.getRawSlot();
                switch (slot) {
                    case 1:
                        terminalStep.put(playerNameStr, 10);
                        break;
                    case 3:
                        terminalStep.put(playerNameStr, 25);
                        break;
                    case 5:
                        terminalStep.put(playerNameStr, 50);
                        break;
                    case 7:
                        terminalStep.put(playerNameStr, 100);
                        break;
                    case 9:
                        setSlots(inv, 10, 16, false, (byte) 3, "X", true, playerNameStr);
                        break;
                    case 17:
                        setSlots(inv, 10, 16, true, (byte) 3, "X", true, playerNameStr);
                        break;
                    case 18:
                        setSlots(inv, 19, 25, false, (byte) 4, "Z", true, playerNameStr);
                        break;
                    case 26:
                        setSlots(inv, 19, 25, true, (byte) 4, "Z", true, playerNameStr);
                        break;
                    case 27:
                        setSlots(inv, 28, 34, false, (byte) 10, "Multiplier", false, playerNameStr);
                        break;
                    case 35:
                        setSlots(inv, 28, 34, true, (byte) 10, "Multiplier", false, playerNameStr);
                        break;
                    case 37:
                        setCurrent(inv, playerNameStr, 37);
                        break;
                    case 39:
                        setCurrent(inv, playerNameStr, 39);
                        break;
                    case 41:
                        setCurrent(inv, playerNameStr, 41);
                        break;
                    case 43:
                        setCurrent(inv, playerNameStr, 43);
                        break;
                    case 45:
                        checkSettings(inv, player);
                        break;
                    case 49:
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("save", terminalDestination.get(playerNameStr));
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", terminalIDs.get(playerNameStr));
                        new QueryFactory(plugin).doUpdate("tardis", set, wheret);
                        plugin.tardisHasDestination.put(terminalIDs.get(playerNameStr), plugin.getArtronConfig().getInt("random"));
                        if (plugin.trackRescue.containsKey(terminalIDs.get(playerNameStr))) {
                            plugin.trackRescue.remove(terminalIDs.get(playerNameStr));
                        }
                        close(player);
                        player.sendMessage(plugin.pluginName + "Destination set. Please release the handbrake!");
                        break;
                    case 53:
                        close(player);
                        break;
                    default:
                        plugin.debug("Not used");
                        break;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpenTerminal(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Player && inv.getName().equals("§4Destination Terminal")) {
            String name = ((Player) holder).getName();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", name);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                if (rs.resultSet()) {
                    terminalUsers.put(name, rs.getCurrent());
                    terminalDirection.put(name, rs.getDirection());
                    terminalIDs.put(name, id);
                }
            }
        }
    }

    private int getSlot(Inventory inv, int min, int max) {
        for (int i = min; i <= max; i++) {
            if (inv.getItem(i) != null) {
                return i;
            }
        }
        return min;
    }

    private int getNewSlot(int slot, int min, int max, boolean pos) {
        if (pos) {
            return (slot < max) ? slot + 1 : max;
        } else {
            return (slot > min) ? slot - 1 : min;
        }
    }

    private List<String> getLoreValue(int max, int slot, boolean signed, String name) {
        int step = (terminalStep.containsKey(name)) ? terminalStep.get(name) : 50;
        int val = max - slot;
        String str;
        switch (val) {
            case 0:
                str = (signed) ? "+" + (3 * step) : "x" + 7;
                break;
            case 1:
                str = (signed) ? "+" + (2 * step) : "x" + 6;
                break;
            case 2:
                str = (signed) ? "+" + step : "x" + 5;
                break;
            case 4:
                str = (signed) ? "-" + step : "x" + 3;
                break;
            case 5:
                str = (signed) ? "-" + (2 * step) : "x" + 2;
                break;
            case 6:
                str = (signed) ? "-" + (3 * step) : "x" + 1;
                break;
            default:
                str = (signed) ? "0" : "x" + 4;
                break;
        }
        return Arrays.asList(new String[]{str});
    }

    private int getValue(int max, int slot, boolean signed, String name) {
        int step = (terminalStep.containsKey(name)) ? terminalStep.get(name) : 50;
        int val = max - slot;
        int intval;
        switch (val) {
            case 0:
                intval = (signed) ? (3 * step) : 7;
                break;
            case 1:
                intval = (signed) ? (2 * step) : 6;
                break;
            case 2:
                intval = (signed) ? step : 5;
                break;
            case 4:
                intval = (signed) ? -step : 3;
                break;
            case 5:
                intval = (signed) ? -(2 * step) : 2;
                break;
            case 6:
                intval = (signed) ? -(3 * step) : 1;
                break;
            default:
                intval = (signed) ? 0 : 4;
                break;
        }
        return intval;
    }

    private void setSlots(Inventory inv, int min, int max, boolean pos, byte data, String row, boolean signed, String name) {
        int affected_slot = getSlot(inv, min, max);
        int new_slot = getNewSlot(affected_slot, min, max, pos);
        inv.setItem(affected_slot, null);
        ItemStack is = new ItemStack(35, 1, data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(row);
        List<String> lore = getLoreValue(max, new_slot, signed, name);
        im.setLore(lore);
        is.setItemMeta(im);
        inv.setItem(new_slot, is);
    }

    private void setCurrent(Inventory inv, String name, int slot) {
        String[] current = terminalUsers.get(name).split(":");
        int[] slots = new int[]{37, 39, 41, 43};
        for (int i : slots) {
            List<String> lore = null;
            ItemStack is = inv.getItem(i);
            ItemMeta im = is.getItemMeta();
            if (i == slot) {
                switch (slot) {
                    case 39:
                        // get a normal world
                        lore = Arrays.asList(new String[]{getWorld("NORMAL", current[0])});
                        break;
                    case 41:
                        // get a nether world
                        lore = Arrays.asList(new String[]{getWorld("NETHER", current[0])});
                        break;
                    case 43:
                        // get an end world
                        lore = Arrays.asList(new String[]{getWorld("THE_END", current[0])});
                        break;
                    default:
                        lore = Arrays.asList(new String[]{current[0]});
                        break;
                }
            }
            im.setLore(lore);
            is.setItemMeta(im);
        }
    }

    private String getWorld(String e, String this_world) {
        List<String> allowedWorlds = new ArrayList<String>();
        String world = "";
        Set<String> worldlist = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
        for (String o : worldlist) {
            World ww = plugin.getServer().getWorld(o);
            if (ww != null) {
                String env = ww.getEnvironment().toString();
                if (e.equalsIgnoreCase(env)) {
                    if (plugin.getConfig().getBoolean("include_default_world") || !plugin.getConfig().getBoolean("default_world")) {
                        if (plugin.getConfig().getBoolean("worlds." + o)) {
                            allowedWorlds.add(o);
                        }
                    } else {
                        if (!o.equals(plugin.getConfig().getString("default_world_name"))) {
                            if (plugin.getConfig().getBoolean("worlds." + o)) {
                                allowedWorlds.add(o);
                            }
                        }
                    }
                }
                // remove the world the Police Box is in
                if (this_world != null && allowedWorlds.size() > 1 && allowedWorlds.contains(this_world)) {
                    allowedWorlds.remove(this_world);
                }
            }
        }
        // random world
        Random rand = new Random();
        int rw = rand.nextInt(allowedWorlds.size());
        int i = 0;
        for (String w : allowedWorlds) {
            if (i == rw) {
                world = w;
            }
            i += 1;
        }
        return world;
    }

    private void checkSettings(Inventory inv, Player p) {
        String name = p.getName();
        // get x, z, m settings
        int slotm = getValue(34, getSlot(inv, 28, 34), false, name) * plugin.getConfig().getInt("terminal_step");
        int slotx = getValue(16, getSlot(inv, 10, 16), true, name) * slotm;
        int slotz = getValue(25, getSlot(inv, 19, 25), true, name) * slotm;
        String str = "";
        String[] current = terminalUsers.get(name).split(":");
        TARDISConstants.COMPASS d = terminalDirection.get(name);
        // what kind of world is it?
        Environment e;
        int[] slots = new int[]{37, 39, 41, 43};
        boolean found = false;
        for (int i : slots) {
            if (inv.getItem(i).getItemMeta().hasLore()) {
                found = true;
                String world = inv.getItem(i).getItemMeta().getLore().get(0);
                World w = plugin.getServer().getWorld(world);
                e = w.getEnvironment();
                TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                if (world.equals(current[0])) {
                    // add current co-ords
                    slotx += plugin.utils.parseNum(current[1]);
                    slotz += plugin.utils.parseNum(current[3]);
                }
                String loc_str = world + ":" + slotx + ":" + slotz;
                switch (e) {
                    case THE_END:
                        int endy = w.getHighestBlockYAt(slotx, slotz);
                        plugin.debug(endy);
                        if (endy > 40) {
                            Location loc = new Location(w, slotx, 0, slotz);
                            int[] estart = tt.getStartLocation(loc, d);
                            int esafe = tt.safeLocation(estart[0], endy, estart[2], estart[1], estart[3], w, d);
                            if (esafe == 0) {
                                String save = world + ":" + slotx + ":" + endy + ":" + slotz;
                                terminalDestination.put(name, save);
                                str = save + " is a valid destination!";
                            } else {
                                str = loc_str + " is not safe!";
                            }
                        } else {
                            str = loc_str + " is not safe!";
                        }
                        break;
                    case NETHER:
                        if (tt.safeNether(w, slotx, slotz, d, p)) {
                            String save = world + ":" + slotx + ":" + w.getHighestBlockYAt(slotx, slotz) + ":" + slotz;
                            terminalDestination.put(name, save);
                            str = save + " is a valid destination!";
                        } else {
                            str = loc_str + " is not safe!";
                        }
                        break;
                    default:
                        Location loc = new Location(w, slotx, 0, slotz);
                        int[] start = tt.getStartLocation(loc, d);
                        int starty = w.getHighestBlockYAt(slotx, slotz);
                        int safe = tt.safeLocation(start[0], starty, start[2], start[1], start[3], w, d);
                        if (safe == 0) {
                            String save = world + ":" + slotx + ":" + starty + ":" + slotz;
                            terminalDestination.put(name, save);
                            str = save + " is a valid destination!";
                        } else {
                            str = loc_str + " is not safe!";
                        }
                        break;
                }
            }
        }
        if (!found) {
            str = "You need to select a world!";
        }
        ItemStack is = inv.getItem(45);
        ItemMeta im = is.getItemMeta();
        List<String> lore = Arrays.asList(new String[]{str});
        im.setLore(lore);
        is.setItemMeta(im);
    }

    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (terminalUsers.containsKey(n)) {
                    terminalUsers.remove(n);
                }
                if (terminalDirection.containsKey(n)) {
                    terminalDirection.remove(n);
                }
                if (terminalStep.containsKey(n)) {
                    terminalStep.remove(n);
                }
                if (terminalDestination.containsKey(n)) {
                    terminalDestination.remove(n);
                }
                p.closeInventory();
            }
        }, 1L);
    }
}
