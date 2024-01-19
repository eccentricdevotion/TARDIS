/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * A control sphere is a device created by the Great Intelligence to control its Robot Yetis. It is a glass sphere that
 * is fitted into the chest of a Yeti and serves as the brain. It emits a beeping noise.
 *
 * @author eccentric_nz
 */
public class TARDISTerminalListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, ResultSetCurrentLocation> terminalUsers = new HashMap<>();
    private final HashMap<UUID, String> terminalDestination = new HashMap<>();
    private final HashMap<UUID, Integer> terminalStep = new HashMap<>();
    private final HashMap<UUID, Integer> terminalIDs = new HashMap<>();
    private final HashMap<UUID, Integer> terminalWorlds = new HashMap<>();
    private final HashMap<UUID, Boolean> terminalSub = new HashMap<>();

    public TARDISTerminalListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onDestTerminalClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Destination Terminal")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        // get the TARDIS the player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return;
        }
        switch (slot) {
            case 1 -> terminalStep.put(uuid, 10);
            case 3 -> terminalStep.put(uuid, 25);
            case 5 -> terminalStep.put(uuid, 50);
            case 7 -> terminalStep.put(uuid, 100);
            case 9 -> setSlots(view, 10, 16, false, "X", true, uuid);
            case 17 -> setSlots(view, 10, 16, true, "X", true, uuid);
            case 18 -> setSlots(view, 19, 25, false, "Z", true, uuid);
            case 26 -> setSlots(view, 19, 25, true, "Z", true, uuid);
            case 27 -> setSlots(view, 28, 34, false, "Multiplier", false, uuid);
            case 35 -> setSlots(view, 28, 34, true, "Multiplier", false, uuid);
            case 36 -> {
                // current world
                terminalWorlds.put(uuid, terminalWorlds.containsKey(uuid) ? terminalWorlds.get(uuid) + 1 : 0);
                setCurrent(view, player, 36);
            }
            case 38 -> {
                // normal
                terminalWorlds.put(uuid, terminalWorlds.containsKey(uuid) ? terminalWorlds.get(uuid) + 1 : 0);
                setCurrent(view, player, 38);
            }
            case 40 -> {
                // nether
                terminalWorlds.put(uuid, terminalWorlds.containsKey(uuid) ? terminalWorlds.get(uuid) + 1 : 0);
                setCurrent(view, player, 40);
            }
            case 42 -> {
                // the end
                terminalWorlds.put(uuid, terminalWorlds.containsKey(uuid) ? terminalWorlds.get(uuid) + 1 : 0);
                setCurrent(view, player, 42);
            }
            case 44 ->
                // submarine
                    toggleSubmarine(view, player);
            case 46 ->
                // check destination
                    checkSettings(view, player);
            case 49 -> {
                // set destination
                if (terminalDestination.containsKey(uuid)) {
                    HashMap<String, Object> set = new HashMap<>();
                    String[] data = terminalDestination.get(uuid).split(":");
                    String ww = (!plugin.getPlanetsConfig().getBoolean("planets." + data[0] + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getWorld(data[0]).getName() : data[0];
                    set.put("world", ww);
                    set.put("x", data[1]);
                    set.put("y", data[2]);
                    set.put("z", data[3]);
                    set.put("direction", terminalUsers.get(uuid).getDirection().toString());
                    set.put("submarine", (terminalSub.containsKey(uuid)) ? 1 : 0);
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", terminalIDs.get(uuid));
                    plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                    plugin.getTrackerKeeper().getHasDestination().put(terminalIDs.get(uuid), new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.TERMINAL));
                    plugin.getTrackerKeeper().getRescue().remove(terminalIDs.get(uuid));
                    close(player);
                    plugin.getMessenger().send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(terminalIDs.get(uuid)));
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(terminalIDs.get(uuid))) {
                        new TARDISLand(plugin, terminalIDs.get(uuid), player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.TERMINAL, terminalIDs.get(uuid)));
                    }
                    // damage the circuit if configured
                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.input") > 0) {
                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, terminalIDs.get(uuid));
                        tcc.getCircuits();
                        // decrement uses
                        int uses_left = tcc.getInputUses();
                        new TARDISCircuitDamager(plugin, DiskCircuit.INPUT, uses_left, terminalIDs.get(uuid), player).damage();
                    }
                } else {
                    // set lore
                    ItemStack is = view.getItem(49);
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = Collections.singletonList("No valid destination has been set!");
                    im.setLore(lore);
                    is.setItemMeta(im);
                }
            }
            case 52 -> close(player);
            default -> {
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpenTerminal(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Player player && view.getTitle().equals(ChatColor.DARK_RED + "Destination Terminal")) {
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wheret);
                if (rsc.resultSet()) {
                    terminalUsers.put(uuid, rsc);
                    terminalIDs.put(uuid, id);
                } else {
                    // emergency TARDIS relocation
                    new TARDISEmergencyRelocation(plugin).relocate(id, player);
                    close(player);
                    return;
                }
            }
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                String sub = (rsp.isSubmarineOn()) ? "true" : "false";
                ItemStack is = inv.getItem(44);
                ItemMeta im = is.getItemMeta();
                im.setLore(Collections.singletonList(sub));
                is.setItemMeta(im);
            }
        }
    }

    private int getSlot(InventoryView view, int min, int max) {
        for (int i = min; i <= max; i++) {
            if (view.getItem(i) != null) {
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

    private List<String> getLoreValue(int max, int slot, boolean signed, UUID uuid) {
        int step = terminalStep.getOrDefault(uuid, 50);
        int val = max - slot;
        String str = switch (val) {
            case 0 -> (signed) ? "+" + (3 * step) : "x" + 7;
            case 1 -> (signed) ? "+" + (2 * step) : "x" + 6;
            case 2 -> (signed) ? "+" + step : "x" + 5;
            case 4 -> (signed) ? "-" + step : "x" + 3;
            case 5 -> (signed) ? "-" + (2 * step) : "x" + 2;
            case 6 -> (signed) ? "-" + (3 * step) : "x" + 1;
            default -> (signed) ? "0" : "x" + 4;
        };
        return Collections.singletonList(str);
    }

    private int getValue(int max, int slot, boolean signed, UUID uuid) {
        int step = terminalStep.getOrDefault(uuid, 50);
        int val = max - slot;
        int intval = switch (val) {
            case 0 -> (signed) ? (3 * step) : 7;
            case 1 -> (signed) ? (2 * step) : 6;
            case 2 -> (signed) ? step : 5;
            case 4 -> (signed) ? -step : 3;
            case 5 -> (signed) ? -(2 * step) : 2;
            case 6 -> (signed) ? -(3 * step) : 1;
            default -> (signed) ? 0 : 4;
        };
        return intval;
    }

    private void setSlots(InventoryView view, int min, int max, boolean pos, String row, boolean signed, UUID uuid) {
        int affected_slot = getSlot(view, min, max);
        int new_slot = getNewSlot(affected_slot, min, max, pos);
        view.setItem(affected_slot, null);
        ItemStack is = switch (row) {
            case "X" -> new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
            case "Z" -> new ItemStack(Material.YELLOW_WOOL, 1);
            default -> new ItemStack(Material.PURPLE_WOOL, 1);
        };
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(row);
        List<String> lore = getLoreValue(max, new_slot, signed, uuid);
        im.setLore(lore);
        is.setItemMeta(im);
        view.setItem(new_slot, is);
    }

    private void setCurrent(InventoryView view, Player p, int slot) {
        String current = terminalUsers.get(p.getUniqueId()).getWorld().getName();
        if (!plugin.getPlanetsConfig().getBoolean("planets." + current + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
            current = plugin.getMVHelper().getAlias(current);
        } else {
            current = TARDISAliasResolver.getWorldAlias(current);
        }
        int[] slots = new int[]{36, 38, 40, 42};
        for (int i : slots) {
            List<String> lore = null;
            ItemStack is = view.getItem(i);
            ItemMeta im = is.getItemMeta();
            if (i == slot) {
                switch (slot) {
                    case 38 ->
                        // get a normal world
                            lore = Collections.singletonList(getWorld("NORMAL", current, p));
                    case 40 -> {
                        // get a nether world
                        if (plugin.getConfig().getBoolean("travel.nether") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
                            lore = Collections.singletonList(getWorld("NETHER", current, p));
                        } else {
                            lore = Collections.singletonList(getWorld(plugin.getConfig().getString("travel.terminal.nether"), current, p));
                        }
                    }
                    case 42 -> {
                        // get an end world
                        if (plugin.getConfig().getBoolean("travel.the_end") || !plugin.getConfig().getBoolean("travel.terminal.redefine")) {
                            lore = Collections.singletonList(getWorld("THE_END", current, p));
                        } else {
                            lore = Collections.singletonList(getWorld(plugin.getConfig().getString("travel.terminal.the_end"), current, p));
                        }
                    }
                    default -> lore = Collections.singletonList(current);
                }
            }
            im.setLore(lore);
            is.setItemMeta(im);
        }
    }

    private void toggleSubmarine(InventoryView view, Player p) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
        if (rsp.resultSet()) {
            String bool = (rsp.isSubmarineOn()) ? "false" : "true";
            ItemStack is = view.getItem(44);
            ItemMeta im = is.getItemMeta();
            im.setLore(Collections.singletonList(bool));
            is.setItemMeta(im);
            int tf = (rsp.isSubmarineOn()) ? 0 : 1;
            HashMap<String, Object> set = new HashMap<>();
            set.put("submarine_on", tf);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("pp_id", rsp.getPp_id());
            plugin.getQueryFactory().doUpdate("player_prefs", set, wheret);
        }
    }

    private String getWorld(String e, String this_world, Player p) {
        List<String> allowedWorlds = new ArrayList<>();
        String world;
        Set<String> worldlist = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
        worldlist.forEach((o) -> {
            World ww = TARDISAliasResolver.getWorldFromAlias(o);
            if (ww != null) {
                String env = ww.getEnvironment().toString();
                if (e.equalsIgnoreCase(env)) {
                    if (plugin.getConfig().getBoolean("travel.include_default_world") || !plugin.getConfig().getBoolean("creation.default_world")) {
                        if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
                            allowedWorlds.add(o);
                        }
                    } else if (!o.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
                            allowedWorlds.add(o);
                        }
                    }
                }
                // remove the world the Police Box is in
                if (this_world != null && (allowedWorlds.size() > 1 || !plugin.getPlanetsConfig().getBoolean("planets." + this_world + ".time_travel"))) {
                    allowedWorlds.remove(this_world);
                }
                // remove the world if the player doesn't have permission
                if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(p, "tardis.travel." + o)) {
                    allowedWorlds.remove(this_world);
                }
            }
        });
        // next world in list
        if (!allowedWorlds.isEmpty()) {
            int rw = terminalWorlds.get(p.getUniqueId());
            if (rw > allowedWorlds.size() - 1) {
                rw = 0;
                terminalWorlds.put(p.getUniqueId(), 0);
            }
            world = allowedWorlds.get(rw);
        } else {
            // if all else fails return the current world
            world = this_world;
        }
        return (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(world) : TARDISAliasResolver.getWorldAlias(world);
    }

    private void checkSettings(InventoryView view, Player p) {
        UUID uuid = p.getUniqueId();
        // get x, z, m settings
        int slotm = getValue(34, getSlot(view, 28, 34), false, uuid) * plugin.getConfig().getInt("travel.terminal_step");
        int slotx = getValue(16, getSlot(view, 10, 16), true, uuid) * slotm;
        int slotz = getValue(25, getSlot(view, 19, 25), true, uuid) * slotm;
        List<String> lore = new ArrayList<>();
        COMPASS d = terminalUsers.get(uuid).getDirection();
        // what kind of world is it?
        Environment e;
        int[] slots = new int[]{36, 38, 40, 42};
        boolean found = false;
        for (int i : slots) {
            if (view.getItem(i).getItemMeta().hasLore()) {
                String world = view.getItem(i).getItemMeta().getLore().get(0);
                if (!world.equals("No permission")) {
                    found = true;
                    World w = (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getWorld(world) : TARDISAliasResolver.getWorldFromAlias(world);
                    e = w.getEnvironment();
                    if (plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".false_nether")) {
                        e = Environment.NETHER;
                    }
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    if (world.equals(terminalUsers.get(uuid).getWorld().getName())) {
                        // add current co-ords
                        slotx += terminalUsers.get(uuid).getX();
                        slotz += terminalUsers.get(uuid).getZ();
                    }
                    String loc_str = world + ":" + slotx + ":" + slotz;
                    switch (e) {
                        case THE_END -> {
                            int endy = TARDISStaticLocationGetters.getHighestYin3x3(w, slotx, slotz);
                            if (endy > 40 && Math.abs(slotx) > 9 && Math.abs(slotz) > 9) {
                                Location loc = new Location(w, slotx, 0, slotz);
                                int[] estart = TARDISTimeTravel.getStartLocation(loc, d);
                                int esafe = TARDISTimeTravel.safeLocation(estart[0], endy, estart[2], estart[1], estart[3], w, d);
                                if (esafe == 0) {
                                    String save = world + ":" + slotx + ":" + endy + ":" + slotz;
                                    if (plugin.getPluginRespect().getRespect(new Location(w, slotx, endy, slotz), new Parameters(p, Flag.getNoMessageFlags()))) {
                                        terminalDestination.put(uuid, save);
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
                        }
                        case NETHER -> {
                            if (tt.safeNether(w, slotx, slotz, d, p)) {
                                String save = world + ":" + slotx + ":" + plugin.getUtils().getHighestNetherBlock(w, slotx, slotz) + ":" + slotz;
                                terminalDestination.put(uuid, save);
                                lore.add(save);
                                lore.add("is a valid destination!");
                            } else {
                                lore.add(loc_str);
                                lore.add("is not safe!");
                            }
                        }
                        default -> {
                            Location loc = new Location(w, slotx, 0, slotz);
                            int[] start = TARDISTimeTravel.getStartLocation(loc, d);
                            int starty = TARDISStaticLocationGetters.getHighestYin3x3(w, slotx, slotz);
                            // allow room for under door block
                            if (starty <= 0) {
                                starty = 1;
                            }
                            int safe;
                            // check submarine
                            ItemMeta subim = view.getItem(44).getItemMeta();
                            loc.setY(starty);
                            if (subim.hasLore() && subim.getLore().get(0).equals("true") && TARDISStaticUtils.isOceanBiome(loc.getBlock().getBiome())) {
                                Location subloc = tt.submarine(loc.getBlock(), d);
                                if (subloc != null) {
                                    safe = 0;
                                    starty = subloc.getBlockY();
                                    terminalSub.put(uuid, true);
                                } else {
                                    safe = 1;
                                }
                            } else {
                                safe = TARDISTimeTravel.safeLocation(start[0], starty, start[2], start[1], start[3], w, d);
                            }
                            if (safe == 0) {
                                String save = world + ":" + slotx + ":" + starty + ":" + slotz;
                                if (plugin.getPluginRespect().getRespect(new Location(w, slotx, starty, slotz), new Parameters(p, Flag.getNoMessageFlags()))) {
                                    terminalDestination.put(uuid, save);
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
                        }
                    }
                }
            }
        }
        if (!found) {
            lore.add("You need to select a world!");
        }
        ItemStack is = view.getItem(46);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    private void close(Player p) {
        UUID uuid = p.getUniqueId();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            terminalUsers.remove(uuid);
            terminalStep.remove(uuid);
            terminalDestination.remove(uuid);
            terminalSub.remove(uuid);
            terminalWorlds.remove(uuid);
            p.closeInventory();
        }, 1L);
    }
}
