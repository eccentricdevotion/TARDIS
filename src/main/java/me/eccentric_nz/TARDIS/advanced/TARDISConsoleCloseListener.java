/*
 * Copyright (C) 2025 eccentric_nz
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

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISRandomiserCircuit;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeUtilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
public class TARDISConsoleCloseListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<>();

    public TARDISConsoleCloseListener(TARDIS plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISAdvancedConsoleInventory)) {
            return;
        }
        Player player = ((Player) event.getPlayer());
        // get the TARDIS the player is in
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return;
        }
        int id = rst.getTardis_id();
        InventoryView view = event.getView();
        // loop through inventory contents and remove any items that are not disks or circuits
        for (int i = 0; i < 18; i++) {
            ItemStack is = view.getItem(i);
            if (is != null) {
                Material mat = is.getType();
                if (!onlythese.contains(mat)) {
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), is);
                    view.setItem(i, ItemStack.of(Material.AIR));
                }
            }
        }
        Inventory inv = event.getInventory();
        // remember what was placed in the console
        saveCurrentConsole(inv, player.getUniqueId().toString());
        if (plugin.getConfig().getBoolean("difficulty.circuits")) {
            // check circuits
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // if no materialisation circuit exit
            if (!tcc.hasMaterialisation() && (inv.contains(Material.MUSIC_DISC_CAT) || inv.contains(Material.MUSIC_DISC_BLOCKS) || inv.contains(Material.MUSIC_DISC_CHIRP) || inv.contains(Material.MUSIC_DISC_WAIT))) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "MAT_MISSING");
                return;
            }
        }
        // get TARDIS's current location
        ResultSetCurrentFromId rs = new ResultSetCurrentFromId(plugin, id);
        if (!rs.resultSet()) {
            new TARDISEmergencyRelocation(plugin).relocate(id, player);
            return;
        }
        Current current = rs.getCurrent();
        // loop through remaining inventory items and process the disks
        for (int i = 0; i < 18; i++) {
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
                    List<Component> lore = is.getItemMeta().lore();
                    if (lore == null) {
                        return;
                    }
                    String first = ComponentUtils.stripColour(lore.getFirst());
                    if (!first.equals("Blank")) {
                        TravelType travelType = TravelType.SAVE;
                        switch (mat) {
                            case MUSIC_DISC_BLOCKS -> { // area
                                // check the current location is not in this area already
                                if (!plugin.getTardisArea().areaCheckInExile(first, current.location())) {
                                    continue;
                                }
                                // get a parking spot in this area
                                HashMap<String, Object> wherea = new HashMap<>();
                                wherea.put("area_name", first);
                                ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                                if (!rsa.resultSet()) {
                                    plugin.getMessenger().sendColouredCommand(player, "AREA_NOT_FOUND", "/tardis list areas", plugin);
                                    continue;
                                }
                                if ((!TARDISPermission.hasPermission(player, "tardis.area." + first) && !TARDISPermission.hasPermission(player, "tardis.area.*")) || (!player.isPermissionSet("tardis.area." + first) && !player.isPermissionSet("tardis.area.*"))) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_AREA_PERM", first);
                                    continue;
                                }
                                Location l;
                                if (rsa.getArea().grid()) {
                                    l = plugin.getTardisArea().getNextSpot(first);
                                } else {
                                    l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
                                }
                                if (l == null) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MORE_SPOTS");
                                    continue;
                                }
                                set_next.put("world", l.getWorld().getName());
                                set_next.put("x", l.getBlockX());
                                set_next.put("y", l.getBlockY());
                                set_next.put("z", l.getBlockZ());
                                set_next.put("submarine", 0);
                                // should be setting direction of TARDIS
                                if (!rsa.getArea().direction().isEmpty()) {
                                    set_next.put("direction", rsa.getArea().direction());
                                } else {
                                    set_next.put("direction", current.direction().toString());
                                }
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_APPROVED", first);
                                travelType = TravelType.AREA;
                                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
                            }
                            case MUSIC_DISC_CAT -> { // biome
                                // find a biome location
                                if (!TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_BIOME");
                                    continue;
                                }
                                if (current.location().getBlock().getBiome().getKey().value().toUpperCase(Locale.ROOT).equals(first)) {
                                    continue;
                                }
                                Biome biome;
                                try {
                                    biome = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(new NamespacedKey("minecraft", first.toLowerCase(Locale.ROOT)));
                                } catch (IllegalArgumentException iae) {
                                    String upper = first.toUpperCase(Locale.ROOT);
                                    // may have a pre-1.9 biome disk do old biome lookup...
                                    if (TardisOldBiomeLookup.OLD_BIOME_LOOKUP.containsKey(upper)) {
                                        biome = TardisOldBiomeLookup.OLD_BIOME_LOOKUP.get(upper);
                                    } else {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_VALID");
                                        continue;
                                    }
                                }
                                plugin.getMessenger().sendStatus(player, "BIOME_SEARCH");
                                Location nsob = BiomeUtilities.searchBiome(current.location().getWorld(), biome, current.location());
                                if (nsob == null) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_FOUND");
                                    continue;
                                } else {
                                    if (!plugin.getPluginRespect().getRespect(nsob, new Parameters(player, Flag.getDefaultFlags()))) {
                                        continue;
                                    }
                                    World bw = nsob.getWorld();
                                    // check location
                                    while (!bw.getChunkAt(nsob).isLoaded()) {
                                        bw.getChunkAt(nsob).load();
                                    }
                                    int[] start_loc = TARDISTimeTravel.getStartLocation(nsob, current.direction());
                                    int tmp_y = nsob.getBlockY();
                                    for (int up = 0; up < 10; up++) {
                                        int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), current.direction());
                                        if (count == 0) {
                                            nsob.setY(tmp_y + up);
                                            break;
                                        }
                                    }
                                    set_next.put("world", nsob.getWorld().getName());
                                    set_next.put("x", nsob.getBlockX());
                                    set_next.put("y", nsob.getBlockY());
                                    set_next.put("z", nsob.getBlockZ());
                                    set_next.put("direction", current.direction().toString());
                                    set_next.put("submarine", 0);
                                    plugin.getMessenger().send(player, "BIOME_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                    travelType = TravelType.BIOME;
                                }
                            }
                            case MUSIC_DISC_WAIT -> { // player
                                // get the player's location
                                if (TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
                                    if (player.getName().equalsIgnoreCase(first)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_SELF");
                                        continue;
                                    }
                                    // get the player
                                    Player to = plugin.getServer().getPlayer(first);
                                    if (to == null) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ONLINE");
                                        continue;
                                    }
                                    UUID toUUID = to.getUniqueId();
                                    // check the to player's DND status
                                    ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, toUUID.toString());
                                    if (rspp.resultSet() && rspp.isDND()) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DND", first);
                                        continue;
                                    }
                                    TARDISRescue to_player = new TARDISRescue(plugin);
                                    to_player.rescue(player, toUUID, id, current.direction(), false, false);
                                    travelType = TravelType.PLAYER;
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_PLAYER");
                                    continue;
                                }
                            }
                            case MUSIC_DISC_MALL -> { // preset
                                if (!ignore) {
                                    // apply the preset
                                    set_tardis.put("chameleon_preset", first);
                                }
                            }
                            case MUSIC_DISC_CHIRP -> { // save
                                if (TARDISPermission.hasPermission(player, "tardis.save")) {
                                    String world = ComponentUtils.stripColour(lore.get(1));
                                    int x = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(2)));
                                    int y = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(3)));
                                    int z = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(4)));
                                    if (current.location().getWorld().getName().equals(world) && current.location().getBlockX() == x && current.location().getBlockZ() == z) {
                                        continue;
                                    }
                                    // read the lore from the disk
                                    set_next.put("world", world);
                                    set_next.put("x", x);
                                    set_next.put("y", y);
                                    set_next.put("z", z);
                                    set_next.put("direction", lore.get(6));
                                    boolean sub = Boolean.parseBoolean(ComponentUtils.stripColour(lore.get(7)));
                                    set_next.put("submarine", (sub) ? 1 : 0);
                                    String five = ComponentUtils.stripColour(lore.get(5));
                                    if (five.startsWith("ITEM")) {
                                        String[] split = five.split(":");
                                        if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(split[1])) {
                                            set_tardis.put("chameleon_preset", five);
                                            // set chameleon adaption to OFF
                                            set_tardis.put("adapti_on", 0);
                                        } else {
                                            plugin.debug("Invalid PRESET value: " + five);
                                        }
                                    } else {
                                        try {
                                            ChameleonPreset.valueOf(five);
                                            set_tardis.put("chameleon_preset", five);
                                            // set chameleon adaption to OFF
                                            set_tardis.put("adapti_on", 0);
                                        } catch (IllegalArgumentException e) {
                                            plugin.debug("Invalid PRESET value: " + five);
                                        }
                                    }
                                    plugin.getMessenger().send(player, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                    travelType = TravelType.SAVE;
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_SAVE");
                                    continue;
                                }
                            }
                            default -> {
                            }
                        }
                        if (!set_next.isEmpty()) {
                            // update next
                            where_next.put("tardis_id", id);
                            plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
                        }
                        if (!set_tardis.isEmpty()) {
                            // update tardis
                            where_tardis.put("tardis_id", id);
                            plugin.getQueryFactory().doUpdate("tardis", set_tardis, where_tardis);
                        }
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.SAVE, id));
                        }
                        if (!plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                            plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                            DamageUtility.run(plugin, DiskCircuit.MEMORY, id, player);
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ADV_BLANK");
                    }
                } else if (mat.equals(Material.MUSIC_DISC_STRAD) && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && ComponentUtils.endsWith(is.getItemMeta().displayName(), "Blank Storage Disk")) {
                    // Blank Disk - get a random location
                    Location l = new TARDISRandomiserCircuit(plugin).getRandomlocation(player, current.direction());
                    if (l == null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PROTECTED");
                        return;
                    }
                    HashMap<String, Object> set_next = new HashMap<>();
                    HashMap<String, Object> where_next = new HashMap<>();
                    set_next.put("world", l.getWorld().getName());
                    set_next.put("x", l.getBlockX());
                    set_next.put("y", l.getBlockY());
                    set_next.put("z", l.getBlockZ());
                    set_next.put("direction", current.direction().toString());
                    boolean sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                    set_next.put("submarine", (sub) ? 1 : 0);
                    plugin.getTrackerKeeper().getSubmarine().remove(id);
                    where_next.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("random_circuit"), TravelType.RANDOM));
                    plugin.getTrackerKeeper().getHasRandomised().add(id);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "RANDOMISER");
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
                    }
                    // damage the circuit if configured
                    DamageUtility.run(plugin, DiskCircuit.RANDOMISER, id, player);
                }
            }
        }
    }

    private void saveCurrentConsole(Inventory inv, String uuid) {
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put("console", serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        plugin.getQueryFactory().doSyncUpdate("storage", set, where);
    }
}
