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
        for (DiskCircuit diskCircuit : DiskCircuit.values()) {
            if (!onlyThese.contains(diskCircuit.getMaterial())) {
                onlyThese.add(diskCircuit.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        String inventoryTitle = inventoryView.getTitle();
        if (inventoryTitle.equals(ChatColor.DARK_RED + "TARDIS Console")) {
            Player player = ((Player) event.getPlayer());
            // get the TARDIS the player is in
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers resultSetTravellers = new ResultSetTravellers(plugin, wheret, false);
            if (resultSetTravellers.resultSet()) {
                int id = resultSetTravellers.getTardisId();
                // loop through inventory contents and remove any items that are not disks or circuits
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = inventoryView.getItem(i);
                    if (itemStack != null) {
                        Material material = itemStack.getType();
                        if (!onlyThese.contains(material)) {
                            Objects.requireNonNull(player.getLocation().getWorld()).dropItemNaturally(player.getLocation(), itemStack);
                            inventoryView.setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
                Inventory inventory = event.getInventory();
                // remember what was placed in the console
                saveCurrentConsole(inventory, player.getUniqueId().toString());
                if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                    // check circuits
                    TardisCircuitChecker tardisCircuitChecker = new TardisCircuitChecker(plugin, id);
                    tardisCircuitChecker.getCircuits();
                    // if no materialisation circuit exit
                    if (!tardisCircuitChecker.hasMaterialisation() && (inventory.contains(Material.MUSIC_DISC_CAT) || inventory.contains(Material.MUSIC_DISC_BLOCKS) || inventory.contains(Material.MUSIC_DISC_CHIRP) || inventory.contains(Material.MUSIC_DISC_WAIT))) {
                        TardisMessage.send(player, "MAT_MISSING");
                        return;
                    }
                }
                // get TARDIS's current location
                HashMap<String, Object> whereCurrentLocation = new HashMap<>();
                whereCurrentLocation.put("tardis_id", id);
                ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(plugin, whereCurrentLocation);
                if (!resultSetCurrentLocation.resultSet()) {
                    new TardisEmergencyRelocation(plugin).relocate(id, player);
                    return;
                }
                Location currentLocation = new Location(resultSetCurrentLocation.getWorld(), resultSetCurrentLocation.getX(), resultSetCurrentLocation.getY(), resultSetCurrentLocation.getZ());
                // loop through remaining inventory items and process the disks
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = inventoryView.getItem(i);
                    if (itemStack != null) {
                        Material material = itemStack.getType();
                        if (!material.equals(Material.GLOWSTONE_DUST) && itemStack.hasItemMeta()) {
                            HashMap<String, Object> setNext = new HashMap<>();
                            HashMap<String, Object> setTardis = new HashMap<>();
                            HashMap<String, Object> whereNext = new HashMap<>();
                            HashMap<String, Object> whereTardis = new HashMap<>();
                            // process any disks
                            List<String> lore = Objects.requireNonNull(itemStack.getItemMeta()).getLore();
                            if (lore != null) {
                                String first = lore.get(0);
                                if (!first.equals("Blank")) {
                                    switch (material) {
                                        case MUSIC_DISC_BLOCKS: // area
                                            // check the current location is not in this area already
                                            if (!plugin.getTardisArea().areaCheckInExile(first, currentLocation)) {
                                                continue;
                                            }
                                            // get a parking spot in this area
                                            HashMap<String, Object> whereArea = new HashMap<>();
                                            whereArea.put("area_name", first);
                                            ResultSetAreas resultSetAreas = new ResultSetAreas(plugin, whereArea, false, false);
                                            if (!resultSetAreas.resultSet()) {
                                                TardisMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                                continue;
                                            }
                                            if ((!TardisPermission.hasPermission(player, "tardis.area." + first) && !TardisPermission.hasPermission(player, "tardis.area.*")) || (!player.isPermissionSet("tardis.area." + first) && !player.isPermissionSet("tardis.area.*"))) {
                                                TardisMessage.send(player, "TRAVEL_NO_AREA_PERM", first);
                                                continue;
                                            }
                                            Location location = plugin.getTardisArea().getNextSpot(resultSetAreas.getArea().getAreaName());
                                            if (location == null) {
                                                TardisMessage.send(player, "NO_MORE_SPOTS");
                                                continue;
                                            }
                                            setNext.put("world", Objects.requireNonNull(location.getWorld()).getName());
                                            setNext.put("x", location.getBlockX());
                                            setNext.put("y", location.getBlockY());
                                            setNext.put("z", location.getBlockZ());
                                            setNext.put("submarine", 0);
                                            // should be setting direction of TARDIS
                                            if (!resultSetAreas.getArea().getDirection().isEmpty()) {
                                                setNext.put("direction", resultSetAreas.getArea().getDirection());
                                            } else {
                                                setNext.put("direction", resultSetCurrentLocation.getDirection().toString());
                                            }
                                            TardisMessage.send(player, "TRAVEL_APPROVED", first);
                                            plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                                            break;
                                        case MUSIC_DISC_CAT: // biome
                                            // find a biome location
                                            if (!TardisPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                                TardisMessage.send(player, "TRAVEL_NO_PERM_BIOME");
                                                continue;
                                            }
                                            if (TardisStaticUtils.getBiomeAt(currentLocation).name().equals(first)) {
                                                continue;
                                            }
                                            Biome biome;
                                            try {
                                                biome = Biome.valueOf(first);
                                            } catch (IllegalArgumentException illegalArgumentException) {
                                                // may have a pre-1.9 biome disk do old biome lookup...
                                                if (TardisOldBiomeLookup.OLD_BIOME_LOOKUP.containsKey(first)) {
                                                    biome = TardisOldBiomeLookup.OLD_BIOME_LOOKUP.get(first);
                                                } else {
                                                    TardisMessage.send(player, "BIOME_NOT_VALID");
                                                    continue;
                                                }
                                            }
                                            TardisMessage.send(player, "BIOME_SEARCH");
                                            // TODO What is this variable? (n) "search" (o) "biome"?
                                            Location nsob = plugin.getGeneralKeeper().getTardisTravelCommand().searchBiome(player, id, biome, resultSetCurrentLocation.getWorld(), resultSetCurrentLocation.getX(), resultSetCurrentLocation.getZ());
                                            if (nsob == null) {
                                                TardisMessage.send(player, "BIOME_NOT_FOUND");
                                                continue;
                                            } else {
                                                if (!plugin.getPluginRespect().getRespect(nsob, new Parameters(player, Flag.getDefaultFlags()))) {
                                                    continue;
                                                }
                                                World bw = nsob.getWorld(); // TODO Rename.
                                                // check location
                                                while (true) {
                                                    assert bw != null;
                                                    if (bw.getChunkAt(nsob).isLoaded()) {
                                                        break;
                                                    }
                                                    bw.getChunkAt(nsob).load();
                                                }
                                                int[] startLocation = TardisTimeTravel.getStartLocation(nsob, resultSetCurrentLocation.getDirection());
                                                int tempY = nsob.getBlockY();
                                                for (int up = 0; up < 10; up++) {
                                                    int count = TardisTimeTravel.safeLocation(startLocation[0], tempY + up, startLocation[2], startLocation[1], startLocation[3], nsob.getWorld(), resultSetCurrentLocation.getDirection());
                                                    if (count == 0) {
                                                        nsob.setY(tempY + up);
                                                        break;
                                                    }
                                                }
                                                setNext.put("world", nsob.getWorld().getName());
                                                setNext.put("x", nsob.getBlockX());
                                                setNext.put("y", nsob.getBlockY());
                                                setNext.put("z", nsob.getBlockZ());
                                                setNext.put("direction", resultSetCurrentLocation.getDirection().toString());
                                                setNext.put("submarine", 0);
                                                TardisMessage.send(player, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                            }
                                            break;
                                        case MUSIC_DISC_WAIT: // player
                                            // get the player's location
                                            if (TardisPermission.hasPermission(player, "tardis.timetravel.player")) {
                                                if (player.getName().equalsIgnoreCase(first)) {
                                                    TardisMessage.send(player, "TRAVEL_NO_SELF");
                                                    continue;
                                                }
                                                // get the player
                                                Player to = plugin.getServer().getPlayer(first);
                                                if (to == null) {
                                                    TardisMessage.send(player, "NOT_ONLINE");
                                                    continue;
                                                }
                                                UUID toUuid = to.getUniqueId();
                                                // check the to player's DND status
                                                ResultSetPlayerPrefs resultSetPlayerPrefs = new ResultSetPlayerPrefs(plugin, toUuid.toString());
                                                if (resultSetPlayerPrefs.resultSet() && resultSetPlayerPrefs.isDndOn()) {
                                                    TardisMessage.send(player, "DND", first);
                                                    continue;
                                                }
                                                TardisRescue to_player = new TardisRescue(plugin);
                                                to_player.rescue(player, toUuid, id, resultSetCurrentLocation.getDirection(), false, false);
                                            } else {
                                                TardisMessage.send(player, "NO_PERM_PLAYER");
                                                continue;
                                            }
                                            break;
                                        case MUSIC_DISC_MALL: // preset
                                            // apply the preset
                                            setTardis.put("chameleon_preset", first);
                                            break;
                                        case MUSIC_DISC_CHIRP: // save
                                            if (TardisPermission.hasPermission(player, "tardis.save")) {
                                                String world = lore.get(1);
                                                int x = TardisNumberParsers.parseInt(lore.get(2));
                                                int y = TardisNumberParsers.parseInt(lore.get(3));
                                                int z = TardisNumberParsers.parseInt(lore.get(4));
                                                if (Objects.requireNonNull(currentLocation.getWorld()).getName().equals(world) && currentLocation.getBlockX() == x && currentLocation.getBlockZ() == z) {
                                                    continue;
                                                }
                                                // read the lore from the disk
                                                setNext.put("world", world);
                                                setNext.put("x", x);
                                                setNext.put("y", y);
                                                setNext.put("z", z);
                                                setNext.put("direction", lore.get(6));
                                                boolean submarine = Boolean.parseBoolean(lore.get(7));
                                                setNext.put("submarine", (submarine) ? 1 : 0);
                                                try {
                                                    Preset.valueOf(lore.get(5));
                                                    setTardis.put("chameleon_preset", lore.get(5));
                                                    // set chameleon adaption to OFF
                                                    setTardis.put("adapti_on", 0);
                                                } catch (IllegalArgumentException illegalArgumentException) {
                                                    plugin.debug("Invalid PRESET value: " + lore.get(5));
                                                }
                                                TardisMessage.send(player, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                            } else {
                                                TardisMessage.send(player, "TRAVEL_NO_PERM_SAVE");
                                                continue;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    if (setNext.size() > 0) {
                                        // update next
                                        whereNext.put("tardis_id", id);
                                        plugin.getQueryFactory().doSyncUpdate("next", setNext, whereNext);
                                        plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
                                    }
                                    if (setTardis.size() > 0) {
                                        // update tardis
                                        whereTardis.put("tardis_id", id);
                                        plugin.getQueryFactory().doUpdate("tardis", setTardis, whereTardis);
                                    }
                                    plugin.getTrackerKeeper().getRescue().remove(id);
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        new TardisLand(plugin, id, player).exitVortex();
                                    }
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.memory") > 0 && !plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                        plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                                        TardisCircuitChecker tardisCircuitChecker = new TardisCircuitChecker(plugin, id);
                                        tardisCircuitChecker.getCircuits();
                                        // decrement uses
                                        int usesLeft = tardisCircuitChecker.getMemoryUses();
                                        new TardisCircuitDamager(plugin, DiskCircuit.MEMORY, usesLeft, id, player).damage();
                                    }
                                } else {
                                    TardisMessage.send(player, "ADV_BLANK");
                                }
                            }
                        } else if (material.equals(Material.GLOWSTONE_DUST) && itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("TARDIS Randomiser Circuit")) {
                            // Randomiser Circuit
                            Location location = new TardisRandomiserCircuit(plugin).getRandomlocation(player, resultSetCurrentLocation.getDirection());
                            if (location != null) {
                                HashMap<String, Object> setNext = new HashMap<>();
                                HashMap<String, Object> whereNext = new HashMap<>();
                                setNext.put("world", Objects.requireNonNull(location.getWorld()).getName());
                                setNext.put("x", location.getBlockX());
                                setNext.put("y", location.getBlockY());
                                setNext.put("z", location.getBlockZ());
                                setNext.put("direction", resultSetCurrentLocation.getDirection().toString());
                                boolean submarine = plugin.getTrackerKeeper().getSubmarine().contains(id);
                                setNext.put("submarine", (submarine) ? 1 : 0);
                                plugin.getTrackerKeeper().getSubmarine().remove(id);
                                whereNext.put("tardis_id", id);
                                plugin.getQueryFactory().doSyncUpdate("next", setNext, whereNext);
                                plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("random_circuit"));
                                plugin.getTrackerKeeper().getHasRandomised().add(id);
                                TardisMessage.send(player, "RANDOMISER");
                                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    new TardisLand(plugin, id, player).exitVortex();
                                }
                                if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.randomiser") > 0) {
                                    TardisCircuitChecker tardisCircuitChecker = new TardisCircuitChecker(plugin, id);
                                    tardisCircuitChecker.getCircuits();
                                    // decrement uses
                                    int usesLeft = tardisCircuitChecker.getRandomiserUses();
                                    new TardisCircuitDamager(plugin, DiskCircuit.RANDOMISER, usesLeft, id, player).damage();
                                }
                            } else {
                                TardisMessage.send(player, "PROTECTED");
                            }
                        }
                    }
                }
            } else {
                TardisMessage.send(player, "NOT_IN_TARDIS");
            }
        }
    }

    private void saveCurrentConsole(Inventory inventory, String uuid) {
        String serialized = TardisInventorySerializer.itemStacksToString(inventory.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put("console", serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        plugin.getQueryFactory().doSyncUpdate("storage", set, where);
    }
}
