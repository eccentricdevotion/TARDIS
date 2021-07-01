/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.api.event.TardisTravelEvent;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisEmergencyRelocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.*;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisRandomiserCircuit;
import me.eccentric_nz.tardis.travel.TardisRescue;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import me.eccentric_nz.tardis.travel.TravelCostAndType;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisConsoleCloseListener implements Listener {

    private final TardisPlugin plugin;
    private final List<Material> onlyThese = new ArrayList<>();

    public TardisConsoleCloseListener(TardisPlugin plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (!onlyThese.contains(dc.getMaterial())) {
                onlyThese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String inv_name = view.getTitle();
        if (inv_name.equals(ChatColor.DARK_RED + "TARDIS Console")) {
            Player p = ((Player) event.getPlayer());
            // get the TARDIS the player is in
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", p.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                int id = rst.getTardisId();
                // loop through inventory contents and remove any items that are not disks or circuits
                for (int i = 0; i < 9; i++) {
                    ItemStack is = view.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!onlyThese.contains(mat)) {
                            Objects.requireNonNull(p.getLocation().getWorld()).dropItemNaturally(p.getLocation(), is);
                            view.setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
                Inventory inv = event.getInventory();
                // remember what was placed in the console
                saveCurrentConsole(inv, p.getUniqueId().toString());
                if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                    // check circuits
                    TardisCircuitChecker tcc = new TardisCircuitChecker(plugin, id);
                    tcc.getCircuits();
                    // if no materialisation circuit exit
                    if (!tcc.hasMaterialisation() && (inv.contains(Material.MUSIC_DISC_CAT) || inv.contains(Material.MUSIC_DISC_BLOCKS) || inv.contains(Material.MUSIC_DISC_CHIRP) || inv.contains(Material.MUSIC_DISC_WAIT))) {
                        TardisMessage.send(p, "MAT_MISSING");
                        return;
                    }
                }
                // get TARDIS's current location
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    new TardisEmergencyRelocation(plugin).relocate(id, p);
                    return;
                }
                Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                // loop through remaining inventory items and process the disks
                for (int i = 0; i < 9; i++) {
                    ItemStack is = view.getItem(i);
                    if (is != null) {
                        Material mat = is.getType();
                        if (!mat.equals(Material.GLOWSTONE_DUST) && is.hasItemMeta()) {
                            boolean ignore = false;
                            HashMap<String, Object> set_next = new HashMap<>();
                            HashMap<String, Object> set_tardis = new HashMap<>();
                            HashMap<String, Object> where_next = new HashMap<>();
                            HashMap<String, Object> where_tardis = new HashMap<>();
                            // process any disks
                            List<String> lore = Objects.requireNonNull(is.getItemMeta()).getLore();
                            if (lore != null) {
                                String first = lore.get(0);
                                if (!first.equals("Blank")) {
                                    TravelType travelType = TravelType.SAVE;
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
                                                TardisMessage.send(p, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                                continue;
                                            }
                                            if ((!TardisPermission.hasPermission(p, "tardis.area." + first) && !TardisPermission.hasPermission(p, "tardis.area.*")) || (!p.isPermissionSet("tardis.area." + first) && !p.isPermissionSet("tardis.area.*"))) {
                                                TardisMessage.send(p, "TRAVEL_NO_AREA_PERM", first);
                                                continue;
                                            }
                                            Location l = plugin.getTardisArea().getNextSpot(rsa.getArea().getAreaName());
                                            if (l == null) {
                                                TardisMessage.send(p, "NO_MORE_SPOTS");
                                                continue;
                                            }
                                            set_next.put("world", Objects.requireNonNull(l.getWorld()).getName());
                                            set_next.put("x", l.getBlockX());
                                            set_next.put("y", l.getBlockY());
                                            set_next.put("z", l.getBlockZ());
                                            set_next.put("submarine", 0);
                                            // should be setting direction of TARDIS
                                            if (!rsa.getArea().getDirection().isEmpty()) {
                                                set_next.put("direction", rsa.getArea().getDirection());
                                            } else {
                                                set_next.put("direction", rsc.getDirection().toString());
                                            }
                                            TardisMessage.send(p, "TRAVEL_APPROVED", first);
                                            travelType = TravelType.AREA;
                                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
                                            break;
                                        case MUSIC_DISC_CAT: // biome
                                            // find a biome location
                                            if (!TardisPermission.hasPermission(p, "tardis.timetravel.biome")) {
                                                TardisMessage.send(p, "TRAVEL_NO_PERM_BIOME");
                                                continue;
                                            }
                                            if (TardisStaticUtils.getBiomeAt(current).name().equals(first)) {
                                                continue;
                                            }
                                            Biome biome;
                                            try {
                                                biome = Biome.valueOf(first);
                                            } catch (IllegalArgumentException iae) {
                                                // may have a pre-1.9 biome disk do old biome lookup...
                                                if (TardisOldBiomeLookup.OLD_BIOME_LOOKUP.containsKey(first)) {
                                                    biome = TardisOldBiomeLookup.OLD_BIOME_LOOKUP.get(first);
                                                } else {
                                                    TardisMessage.send(p, "BIOME_NOT_VALID");
                                                    continue;
                                                }
                                            }
                                            TardisMessage.send(p, "BIOME_SEARCH");
                                            Location nsob = plugin.getGeneralKeeper().getTardisTravelCommand().searchBiome(p, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                            if (nsob == null) {
                                                TardisMessage.send(p, "BIOME_NOT_FOUND");
                                                continue;
                                            } else {
                                                if (!plugin.getPluginRespect().getRespect(nsob, new Parameters(p, Flag.getDefaultFlags()))) {
                                                    continue;
                                                }
                                                World bw = nsob.getWorld();
                                                // check location
                                                while (true) {
                                                    assert bw != null;
                                                    if (bw.getChunkAt(nsob).isLoaded()) break;
                                                    bw.getChunkAt(nsob).load();
                                                }
                                                int[] start_loc = TardisTimeTravel.getStartLocation(nsob, rsc.getDirection());
                                                int tmp_y = nsob.getBlockY();
                                                for (int up = 0; up < 10; up++) {
                                                    int count = TardisTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), rsc.getDirection());
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
                                                TardisMessage.send(p, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                                travelType = TravelType.BIOME;
                                            }
                                            break;
                                        case MUSIC_DISC_WAIT: // player
                                            // get the player's location
                                            if (TardisPermission.hasPermission(p, "tardis.timetravel.player")) {
                                                if (p.getName().equalsIgnoreCase(first)) {
                                                    TardisMessage.send(p, "TRAVEL_NO_SELF");
                                                    continue;
                                                }
                                                // get the player
                                                Player to = plugin.getServer().getPlayer(first);
                                                if (to == null) {
                                                    TardisMessage.send(p, "NOT_ONLINE");
                                                    continue;
                                                }
                                                UUID toUUID = to.getUniqueId();
                                                // check the to player's DND status
                                                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, toUUID.toString());
                                                if (rspp.resultSet() && rspp.isDndOn()) {
                                                    TardisMessage.send(p, "DND", first);
                                                    continue;
                                                }
                                                TardisRescue to_player = new TardisRescue(plugin);
                                                to_player.rescue(p, toUUID, id, rsc.getDirection(), false, false);
                                                travelType = TravelType.PLAYER;
                                            } else {
                                                TardisMessage.send(p, "NO_PERM_PLAYER");
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
                                            if (TardisPermission.hasPermission(p, "tardis.save")) {
                                                String world = lore.get(1);
                                                int x = TardisNumberParsers.parseInt(lore.get(2));
                                                int y = TardisNumberParsers.parseInt(lore.get(3));
                                                int z = TardisNumberParsers.parseInt(lore.get(4));
                                                if (Objects.requireNonNull(current.getWorld()).getName().equals(world) && current.getBlockX() == x && current.getBlockZ() == z) {
                                                    continue;
                                                }
                                                // read the lore from the disk
                                                set_next.put("world", world);
                                                set_next.put("x", x);
                                                set_next.put("y", y);
                                                set_next.put("z", z);
                                                set_next.put("direction", lore.get(6));
                                                boolean sub = Boolean.parseBoolean(lore.get(7));
                                                set_next.put("submarine", (sub) ? 1 : 0);
                                                try {
                                                    Preset.valueOf(lore.get(5));
                                                    set_tardis.put("chameleon_preset", lore.get(5));
                                                    // set chameleon adaption to OFF
                                                    set_tardis.put("adapti_on", 0);
                                                } catch (IllegalArgumentException e) {
                                                    plugin.debug("Invalid PRESET value: " + lore.get(5));
                                                }
                                                TardisMessage.send(p, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                                travelType = TravelType.SAVE;
                                            } else {
                                                TardisMessage.send(p, "TRAVEL_NO_PERM_SAVE");
                                                continue;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    if (set_next.size() > 0) {
                                        // update next
                                        where_next.put("tardis_id", id);
                                        plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
                                    }
                                    if (set_tardis.size() > 0) {
                                        // update tardis
                                        where_tardis.put("tardis_id", id);
                                        plugin.getQueryFactory().doUpdate("tardis", set_tardis, where_tardis);
                                    }
                                    plugin.getTrackerKeeper().getRescue().remove(id);
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        new TardisLand(plugin, id, p).exitVortex();
                                        plugin.getPluginManager().callEvent(new TardisTravelEvent(p, null, TravelType.SAVE, id));
                                    }
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.memory") > 0 && !plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                        plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                                        TardisCircuitChecker tcc = new TardisCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getMemoryUses();
                                        new TardisCircuitDamager(plugin, DiskCircuit.MEMORY, uses_left, id, p).damage();
                                    }
                                } else {
                                    TardisMessage.send(p, "ADV_BLANK");
                                }
                            }
                        } else if (mat.equals(Material.GLOWSTONE_DUST) && is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).hasDisplayName() && is.getItemMeta().getDisplayName().equals("TARDIS Randomiser Circuit")) {
                            // Randomiser Circuit
                            Location l = new TardisRandomiserCircuit(plugin).getRandomlocation(p, rsc.getDirection());
                            if (l != null) {
                                HashMap<String, Object> set_next = new HashMap<>();
                                HashMap<String, Object> where_next = new HashMap<>();
                                set_next.put("world", Objects.requireNonNull(l.getWorld()).getName());
                                set_next.put("x", l.getBlockX());
                                set_next.put("y", l.getBlockY());
                                set_next.put("z", l.getBlockZ());
                                set_next.put("direction", rsc.getDirection().toString());
                                boolean sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                                set_next.put("submarine", (sub) ? 1 : 0);
                                plugin.getTrackerKeeper().getSubmarine().remove(id);
                                where_next.put("tardis_id", id);
                                plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("random_circuit"), TravelType.RANDOM));
                                plugin.getTrackerKeeper().getHasRandomised().add(id);
                                TardisMessage.send(p, "RANDOMISER");
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    new TardisLand(plugin, id, p).exitVortex();
                                    plugin.getPluginManager().callEvent(new TardisTravelEvent(p, null, TravelType.RANDOM, id));
                                }
                                if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.randomiser") > 0) {
                                    TardisCircuitChecker tcc = new TardisCircuitChecker(plugin, id);
                                    tcc.getCircuits();
                                    // decrement uses
                                    int uses_left = tcc.getRandomiserUses();
                                    new TardisCircuitDamager(plugin, DiskCircuit.RANDOMISER, uses_left, id, p).damage();
                                }
                            } else {
                                TardisMessage.send(p, "PROTECTED");
                            }
                        }
                    }
                }
            } else {
                TardisMessage.send(p, "NOT_IN_TARDIS");
            }
        }
    }

    private void saveCurrentConsole(Inventory inv, String uuid) {
        String serialized = TardisInventorySerializer.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put("console", serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        plugin.getQueryFactory().doSyncUpdate("storage", set, where);
    }
}