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
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
 * A control sphere is a device created by the Great Intelligence to control its
 * Robot Yetis. It is a glass sphere that is fitted into the chest of a Yeti and
 * serves as the brain. It emits a beeping noise.
 *
 * @author eccentric_nz
 */
public class TARDISTerminalListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, ResultSetCurrentLocation> terminalUsers = new HashMap<String, ResultSetCurrentLocation>();
    private final HashMap<String, String> terminalDestination = new HashMap<String, String>();
    private final HashMap<String, Integer> terminalStep = new HashMap<String, Integer>();
    private final HashMap<String, Integer> terminalIDs = new HashMap<String, Integer>();
    private final HashMap<String, Boolean> terminalSub = new HashMap<String, Boolean>();

    public TARDISTerminalListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDestTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Destination Terminal")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                final Player player = (Player) event.getWhoClicked();
                String playerNameStr = player.getName();
                // get the TARDIS the player is in
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", playerNameStr);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                if (rst.resultSet()) {
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
                        case 36:
                            setCurrent(inv, player, 36);
                            break;
                        case 38:
                            setCurrent(inv, player, 38);
                            break;
                        case 40:
                            setCurrent(inv, player, 40);
                            break;
                        case 42:
                            setCurrent(inv, player, 42);
                            break;
                        case 44:
                            // submarine
                            toggleSubmarine(inv, player);
                            break;
                        case 46:
                            checkSettings(inv, player);
                            break;
                        case 49:
                            if (terminalDestination.containsKey(playerNameStr)) {
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                String[] data = terminalDestination.get(playerNameStr).split(":");
                                set.put("world", data[0]);
                                set.put("x", data[1]);
                                set.put("y", data[2]);
                                set.put("z", data[3]);
                                set.put("direction", terminalUsers.get(playerNameStr).getDirection().toString());
                                set.put("submarine", (terminalSub.containsKey(playerNameStr)) ? 1 : 0);
                                HashMap<String, Object> wheret = new HashMap<String, Object>();
                                wheret.put("tardis_id", terminalIDs.get(playerNameStr));
                                new QueryFactory(plugin).doUpdate("next", set, wheret);
                                plugin.tardisHasDestination.put(terminalIDs.get(playerNameStr), plugin.getArtronConfig().getInt("random"));
                                if (plugin.trackRescue.containsKey(terminalIDs.get(playerNameStr))) {
                                    plugin.trackRescue.remove(terminalIDs.get(playerNameStr));
                                }
                                close(player);
                                player.sendMessage(plugin.pluginName + "Destination set. Please release the handbrake!");
                            } else {
                                // set lore
                                ItemStack is = inv.getItem(49);
                                ItemMeta im = is.getItemMeta();
                                List<String> lore = Arrays.asList(new String[]{"No valid destination has been set!"});
                                im.setLore(lore);
                                is.setItemMeta(im);
                            }
                            break;
                        case 52:
                            close(player);
                            break;
                        default:
                            break;
                    }
                }
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
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wheret);
                if (rsc.resultSet()) {
                    terminalUsers.put(name, rsc);
                    terminalIDs.put(name, id);
                }
            }
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", name);
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                String sub = (rsp.isSubmarine_on()) ? "true" : "false";
                ItemStack is = inv.getItem(44);
                ItemMeta im = is.getItemMeta();
                im.setLore(Arrays.asList(new String[]{sub}));
                is.setItemMeta(im);
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

    private void setCurrent(Inventory inv, Player p, int slot) {
        String current = terminalUsers.get(p.getName()).getWorld().getName();
        int[] slots = new int[]{36, 38, 40, 42};
        for (int i : slots) {
            List<String> lore = null;
            ItemStack is = inv.getItem(i);
            ItemMeta im = is.getItemMeta();
            if (i == slot) {
                switch (slot) {
                    case 38:
                        // get a normal world
                        lore = Arrays.asList(new String[]{getWorld("NORMAL", current, p)});
                        break;
                    case 40:
                        // get a nether world
                        lore = Arrays.asList(new String[]{getWorld("NETHER", current, p)});
                        break;
                    case 42:
                        // get an end world
                        lore = Arrays.asList(new String[]{getWorld("THE_END", current, p)});
                        break;
                    default:
                        lore = Arrays.asList(new String[]{current});
                        break;
                }
            }
            im.setLore(lore);
            is.setItemMeta(im);
        }
    }

    private void toggleSubmarine(Inventory inv, Player p) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", p.getName());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        if (rsp.resultSet()) {
            String bool = (rsp.isSubmarine_on()) ? "false" : "true";
            ItemStack is = inv.getItem(44);
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(new String[]{bool}));
            is.setItemMeta(im);
            int tf = (rsp.isSubmarine_on()) ? 0 : 1;
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("submarine_on", tf);
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("pp_id", rsp.getPp_id());
            new QueryFactory(plugin).doUpdate("player_prefs", set, wheret);
        }
    }

    private String getWorld(String e, String this_world, Player p) {
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
                // remove the world if the player doesn't have permission
                if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("per_world_perms") && !p.hasPermission("tardis.travel." + o)) {
                    allowedWorlds.remove(this_world);
                }
            }
        }
        // random world
        if (allowedWorlds.size() > 0) {
            Random rand = new Random();
            int rw = rand.nextInt(allowedWorlds.size());
            int i = 0;
            for (String w : allowedWorlds) {
                if (i == rw) {
                    world = w;
                }
                i += 1;
            }
        } else {
            // if all else fails return the current world
            world = this_world;
        }
        return world;
    }

    private void checkSettings(Inventory inv, Player p) {
        String name = p.getName();
        // get x, z, m settings
        int slotm = getValue(34, getSlot(inv, 28, 34), false, name) * plugin.getConfig().getInt("terminal_step");
        int slotx = getValue(16, getSlot(inv, 10, 16), true, name) * slotm;
        int slotz = getValue(25, getSlot(inv, 19, 25), true, name) * slotm;
        List<String> lore = new ArrayList<String>();
        TARDISConstants.COMPASS d = terminalUsers.get(name).getDirection();
        // what kind of world is it?
        Environment e;
        int[] slots = new int[]{36, 38, 40, 42};
        boolean found = false;
        for (int i : slots) {
            if (inv.getItem(i).getItemMeta().hasLore()) {
                String world = inv.getItem(i).getItemMeta().getLore().get(0);
                if (!world.equals("No permission")) {
                    found = true;
                    World w = plugin.getServer().getWorld(world);
                    e = w.getEnvironment();
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                    if (world.equals(terminalUsers.get(name).getWorld().getName())) {
                        // add current co-ords
                        slotx += terminalUsers.get(name).getX();
                        slotz += terminalUsers.get(name).getZ();
                    }
                    String loc_str = world + ":" + slotx + ":" + slotz;
                    switch (e) {
                        case THE_END:
                            int endy = w.getHighestBlockYAt(slotx, slotz);
                            if (endy > 40) {
                                Location loc = new Location(w, slotx, 0, slotz);
                                int[] estart = tt.getStartLocation(loc, d);
                                int esafe = tt.safeLocation(estart[0], endy, estart[2], estart[1], estart[3], w, d);
                                if (esafe == 0) {
                                    String save = world + ":" + slotx + ":" + endy + ":" + slotz;
                                    if (respect.getRespect(p, new Location(w, slotx, endy, slotz), false)) {
                                        terminalDestination.put(name, save);
                                        lore.add(save);
                                        lore.add("is a valid destination!");
                                    } else {
                                        lore.add(save);
                                        lore.add("is a protected location.");
                                        lore.add("Try again!");
                                    }
                                } else {
                                    lore.add(loc_str);
                                    lore.add("is not safe!");
                                }
                            } else {
                                lore.add(loc_str);
                                lore.add("is not safe!");
                            }
                            break;
                        case NETHER:
                            if (tt.safeNether(w, slotx, slotz, d, p)) {
                                String save = world + ":" + slotx + ":" + getHighestNetherBlock(w, slotx, slotz) + ":" + slotz;
                                terminalDestination.put(name, save);
                                lore.add(save);
                                lore.add("is a valid destination!");
                            } else {
                                lore.add(loc_str);
                                lore.add("is not safe!");
                            }
                            break;
                        default:
                            Location loc = new Location(w, slotx, 0, slotz);
                            int[] start = tt.getStartLocation(loc, d);
                            int starty = w.getHighestBlockYAt(slotx, slotz);
                            // allow room for under door block / platform if necessary
                            if (starty <= 0) {
                                starty = 1;
                            }
                            int safe;
                            // check submarine
                            ItemMeta subim = inv.getItem(44).getItemMeta();
                            loc.setY(starty);
                            if (subim.hasLore() && subim.getLore().get(0).equals("true") && plugin.utils.isOceanBiome(loc.getBlock().getBiome())) {
                                Location subloc = tt.submarine(loc.getBlock(), d);
                                if (subloc != null) {
                                    safe = 0;
                                    starty = subloc.getBlockY();
                                    terminalSub.put(name, true);
                                    plugin.trackSubmarine.add(terminalIDs.get(name));
                                } else {
                                    safe = 1;
                                }
                            } else {
                                safe = tt.safeLocation(start[0], starty, start[2], start[1], start[3], w, d);
                                if (plugin.trackSubmarine.contains(Integer.valueOf(terminalIDs.get(name)))) {
                                    plugin.trackSubmarine.remove(Integer.valueOf(terminalIDs.get(name)));
                                }
                            }
                            if (safe == 0) {
                                String save = world + ":" + slotx + ":" + starty + ":" + slotz;
                                if (respect.getRespect(p, new Location(w, slotx, starty, slotz), false)) {
                                    terminalDestination.put(name, save);
                                    lore.add(save);
                                    lore.add("is a valid destination!");
                                } else {
                                    lore.add(save);
                                    lore.add("is a protected location.");
                                    lore.add("Try again!");
                                }
                            } else {
                                lore.add(loc_str);
                                lore.add("is not safe!");
                            }
                            break;
                    }
                }
            }
        }
        if (!found) {
            lore.add("You need to select a world!");
        }
        ItemStack is = inv.getItem(46);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    @SuppressWarnings("deprecation")
    private int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (startBlock.getTypeId() != 0) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getTypeId() == 0 && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        int id = startBlock.getTypeId();
        if ((id == 87 || id == 88 || id == 89 || id == 112 || id == 113 || id == 114) && air >= 4) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }

    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (terminalUsers.containsKey(n)) {
                    terminalUsers.remove(n);
                }
                if (terminalStep.containsKey(n)) {
                    terminalStep.remove(n);
                }
                if (terminalDestination.containsKey(n)) {
                    terminalDestination.remove(n);
                }
                if (terminalSub.containsKey(n)) {
                    terminalSub.remove(n);
                }
                p.closeInventory();
            }
        }, 1L);
    }
}
