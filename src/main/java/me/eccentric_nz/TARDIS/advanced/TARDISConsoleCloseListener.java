/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.enumeration.TARDISOldBiomeLookup;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISRandomiserCircuit;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleCloseListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<>();

    public TARDISConsoleCloseListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DISK_CIRCUIT dc : DISK_CIRCUIT.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        String inv_name = inv.getTitle();
        if (inv_name.equals("§4TARDIS Console")) {
            Player p = ((Player) event.getPlayer());
            // get the TARDIS the player is in
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", p.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                // loop through inventory contents and remove any items that are not disks or circuits
                for (int i = 0; i < 9; i++) {
                    ItemStack is = inv.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!onlythese.contains(mat)) {
                            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                            inv.setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
                // remember what was placed in the console
                saveCurrentConsole(inv, p.getUniqueId().toString());
                if (!plugin.getDifficulty().equals(DIFFICULTY.EASY)) {
                    // check circuits
                    TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                    // if no materialisation circuit exit
                    if (!tcc.hasMaterialisation() && (inv.contains(Material.MUSIC_DISC_CAT) || inv.contains(Material.MUSIC_DISC_BLOCKS) || inv.contains(Material.MUSIC_DISC_CHIRP) || inv.contains(Material.MUSIC_DISC_WAIT))) {
                        TARDISMessage.send(p, "MAT_MISSING");
                        return;
                    }
                }
                // get TARDIS's current location
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    new TARDISEmergencyRelocation(plugin).relocate(id, p);
                    return;
                }
                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                // loop through remaining inventory items and process the disks
                for (int i = 0; i < 9; i++) {
                    ItemStack is = inv.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!mat.equals(Material.MAP) && is.hasItemMeta()) {
                            boolean ignore = false;
                            HashMap<String, Object> set_next = new HashMap<>();
                            HashMap<String, Object> set_tardis = new HashMap<>();
                            HashMap<String, Object> where_next = new HashMap<>();
                            HashMap<String, Object> where_tardis = new HashMap<>();
                            // process any disks
                            List<String> lore = is.getItemMeta().getLore();
                            if (lore != null) {
                                String first = lore.get(0);
                                if (!first.equals("Blank")) {
                                    switch (mat) {
                                        case MUSIC_DISC_BLOCKS: // area
                                            // check the current location is not in this area already
                                            if (!plugin.getTardisArea().areaCheckInExile(first, current)) {
                                                continue;
                                            }
                                            // get a parking spot in this area
                                            HashMap<String, Object> wherea = new HashMap<>();
                                            wherea.put("area_name", first);
                                            ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                                            if (!rsa.resultSet()) {
                                                TARDISMessage.send(p, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                                continue;
                                            }
                                            if ((!p.hasPermission("tardis.area." + first) && !p.hasPermission("tardis.area.*")) || (!p.isPermissionSet("tardis.area." + first) && !p.isPermissionSet("tardis.area.*"))) {
                                                TARDISMessage.send(p, "TRAVEL_NO_AREA_PERM", first);
                                                continue;
                                            }
                                            Location l = plugin.getTardisArea().getNextSpot(rsa.getArea().getAreaName());
                                            if (l == null) {
                                                TARDISMessage.send(p, "NO_MORE_SPOTS");
                                                continue;
                                            }
                                            set_next.put("world", l.getWorld().getName());
                                            set_next.put("x", l.getBlockX());
                                            set_next.put("y", l.getBlockY());
                                            set_next.put("z", l.getBlockZ());
                                            set_next.put("submarine", 0);
                                            TARDISMessage.send(p, "TRAVEL_APPROVED", first);
                                            plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                                            break;
                                        case MUSIC_DISC_CAT: // biome
                                            // find a biome location
                                            if (!p.hasPermission("tardis.timetravel.biome")) {
                                                TARDISMessage.send(p, "TRAVEL_NO_PERM_BIOME");
                                                continue;
                                            }
                                            if (current.getBlock().getBiome().toString().equals(first)) {
                                                continue;
                                            }
                                            Biome biome;
                                            try {
                                                biome = Biome.valueOf(first);
                                            } catch (IllegalArgumentException iae) {
                                                // may have a pre-1.9 biome disk do old biome lookup...
                                                if (TARDISOldBiomeLookup.OLD_BIOME_LOOKUP.containsKey(first)) {
                                                    biome = TARDISOldBiomeLookup.OLD_BIOME_LOOKUP.get(first);
                                                } else {
                                                    TARDISMessage.send(p, "BIOME_NOT_VALID");
                                                    continue;
                                                }
                                            }
                                            TARDISMessage.send(p, "BIOME_SEARCH");
                                            Location nsob = plugin.getGeneralKeeper().getTardisTravelCommand().searchBiome(p, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                            if (nsob == null) {
                                                TARDISMessage.send(p, "BIOME_NOT_FOUND");
                                                continue;
                                            } else {
                                                if (!plugin.getPluginRespect().getRespect(nsob, new Parameters(p, FLAG.getDefaultFlags()))) {
                                                    continue;
                                                }
                                                World bw = nsob.getWorld();
                                                // check location
                                                while (!bw.getChunkAt(nsob).isLoaded()) {
                                                    bw.getChunkAt(nsob).load();
                                                }
                                                int[] start_loc = TARDISTimeTravel.getStartLocation(nsob, rsc.getDirection());
                                                int tmp_y = nsob.getBlockY();
                                                for (int up = 0; up < 10; up++) {
                                                    int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), rsc.getDirection());
                                                    if (count == 0) {
                                                        nsob.setY(tmp_y + up);
                                                        break;
                                                    }
                                                }
                                                set_next.put("world", nsob.getWorld().getName());
                                                set_next.put("x", nsob.getBlockX());
                                                set_next.put("y", nsob.getBlockY());
                                                set_next.put("z", nsob.getBlockZ());
                                                set_next.put("direction", rsc.getDirection().toString());
                                                set_next.put("submarine", 0);
                                                TARDISMessage.send(p, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                            }
                                            break;
                                        case MUSIC_DISC_WAIT: // player
                                            // get the player's location
                                            if (p.hasPermission("tardis.timetravel.player")) {
                                                if (p.getName().equalsIgnoreCase(first)) {
                                                    TARDISMessage.send(p, "TRAVEL_NO_SELF");
                                                    continue;
                                                }
                                                // get the player
                                                Player to = plugin.getServer().getPlayer(first);
                                                if (to == null) {
                                                    TARDISMessage.send(p, "NOT_ONLINE");
                                                    continue;
                                                }
                                                UUID toUUID = to.getUniqueId();
                                                // check the to player's DND status
                                                HashMap<String, Object> wherednd = new HashMap<>();
                                                wherednd.put("uuid", toUUID.toString());
                                                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, wherednd);
                                                if (rspp.resultSet() && rspp.isDND()) {
                                                    TARDISMessage.send(p, "DND", first);
                                                    continue;
                                                }
                                                TARDISRescue to_player = new TARDISRescue(plugin);
                                                to_player.rescue(p, toUUID, id, rsc.getDirection(), false, false);
                                            } else {
                                                TARDISMessage.send(p, "NO_PERM_PLAYER");
                                                continue;
                                            }
                                            break;
                                        case MUSIC_DISC_MALL: // preset
                                            if (!ignore) {
                                                // apply the preset
                                                set_tardis.put("chameleon_preset", first);
                                            }
                                            break;
                                        case MUSIC_DISC_CHIRP: // save
                                            if (p.hasPermission("tardis.save")) {
                                                String world = lore.get(1);
                                                int x = TARDISNumberParsers.parseInt(lore.get(2));
                                                int y = TARDISNumberParsers.parseInt(lore.get(3));
                                                int z = TARDISNumberParsers.parseInt(lore.get(4));
                                                if (current.getWorld().getName().equals(world) && current.getBlockX() == x && current.getBlockZ() == z) {
                                                    continue;
                                                }
                                                // read the lore from the disk
                                                set_next.put("world", world);
                                                set_next.put("x", x);
                                                set_next.put("y", y);
                                                set_next.put("z", z);
                                                set_next.put("direction", lore.get(6));
                                                boolean sub = Boolean.valueOf(lore.get(7));
                                                set_next.put("submarine", (sub) ? 1 : 0);
                                                set_tardis.put("chameleon_preset", lore.get(5));
                                                TARDISMessage.send(p, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                            } else {
                                                TARDISMessage.send(p, "TRAVEL_NO_PERM_SAVE");
                                                continue;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    QueryFactory qf = new QueryFactory(plugin);
                                    if (set_next.size() > 0) {
                                        // update next
                                        where_next.put("tardis_id", id);
                                        qf.doSyncUpdate("next", set_next, where_next);
                                        plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                                    }
                                    if (set_tardis.size() > 0) {
                                        // update tardis
                                        where_tardis.put("tardis_id", id);
                                        qf.doUpdate("tardis", set_tardis, where_tardis);
                                    }
                                    if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                        plugin.getTrackerKeeper().getRescue().remove(id);
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        new TARDISLand(plugin, id, p).exitVortex();
                                    }
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.memory") > 0 && !plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                        plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getMemoryUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.MEMORY, uses_left, id, p).damage();
                                    }
                                } else {
                                    TARDISMessage.send(p, "ADV_BLANK");
                                }
                            }
                        } else if (mat.equals(Material.MAP) && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("TARDIS Randomiser Circuit")) {
                            // Randomiser Circuit
                            Location l = new TARDISRandomiserCircuit(plugin).getRandomlocation(p, rsc.getDirection());
                            if (l != null) {
                                HashMap<String, Object> set_next = new HashMap<>();
                                HashMap<String, Object> where_next = new HashMap<>();
                                set_next.put("world", l.getWorld().getName());
                                set_next.put("x", l.getBlockX());
                                set_next.put("y", l.getBlockY());
                                set_next.put("z", l.getBlockZ());
                                set_next.put("direction", rsc.getDirection().toString());
                                boolean sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                                set_next.put("submarine", (sub) ? 1 : 0);
                                if (plugin.getTrackerKeeper().getSubmarine().contains(id)) {
                                    plugin.getTrackerKeeper().getSubmarine().remove(Integer.valueOf(id));
                                }
                                where_next.put("tardis_id", id);
                                new QueryFactory(plugin).doSyncUpdate("next", set_next, where_next);
                                plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("random_circuit"));
                                plugin.getTrackerKeeper().getHasRandomised().add(id);
                                TARDISMessage.send(p, "RANDOMISER");
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    new TARDISLand(plugin, id, p).exitVortex();
                                }
                                if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.randomiser") > 0) {
                                    TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                    tcc.getCircuits();
                                    // decrement uses
                                    int uses_left = tcc.getRandomiserUses();
                                    new TARDISCircuitDamager(plugin, DISK_CIRCUIT.RANDOMISER, uses_left, id, p).damage();
                                }
                            } else {
                                TARDISMessage.send(p, "PROTECTED");
                            }
                        }
                    }
                }
            } else {
                TARDISMessage.send(p, "NOT_IN_TARDIS");
            }
        }
    }

    private void saveCurrentConsole(Inventory inv, String uuid) {
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put("console", serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        new QueryFactory(plugin).doSyncUpdate("storage", set, where);
    }
}
