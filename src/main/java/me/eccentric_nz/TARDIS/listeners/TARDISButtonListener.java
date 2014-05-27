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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The various systems of the console room are fairly well-understood. According
 * to one account, each of the six panels controls a discrete function. The
 * navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class TARDISButtonListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<Material>();
    private final List<Integer> onlythese = Arrays.asList(1, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20);
    public ItemStack[] items;
    private final ItemStack[] tars;
    private final ItemStack[] clocks;

    public TARDISButtonListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.WOOD_BUTTON);
        validBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
        validBlocks.add(Material.REDSTONE_COMPARATOR_ON);
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.WALL_SIGN);
        validBlocks.add(Material.NOTE_BLOCK);
        validBlocks.add(Material.JUKEBOX);
        this.items = new TARDISTerminalInventory(this.plugin).getTerminal();
        this.tars = new TARDISARSInventory(this.plugin).getTerminal();
        this.clocks = new TARDISTemporalLocatorInventory(this.plugin).getTerminal();
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the
     * button is clicked it will return a random destination based on the
     * settings of the four TARDIS console repeaters.
     *
     * @param event the player clicking a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onButtonInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a type of a button or a lever!
                if (validBlocks.contains(blockType)) {
                    // get clicked block location
                    String buttonloc = block.getLocation().toString();
                    // get tardis from saved button location
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("location", buttonloc);
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        int id = rsc.getTardis_id();
                        int type = rsc.getType();
                        if (!onlythese.contains(type)) {
                            return;
                        }
                        HashMap<String, Object> whereid = new HashMap<String, Object>();
                        whereid.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                        if (rs.resultSet()) {
                            // check they initialised
                            if (!rs.isTardis_init()) {
                                TARDISMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            int level = rs.getArtron_level();
                            boolean hb = rs.isHandbrake_on();
                            boolean set_dest = false;
                            String comps = rs.getCompanions();
                            UUID ownerUUID = rs.getUuid();
                            TARDISCircuitChecker tcc = null;
                            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                                tcc = new TARDISCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            int cost = 0;
                            switch (type) {
                                case 1: // random location button
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    cost = plugin.getArtronConfig().getInt("random");
                                    if (level < cost) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", rs.getTardis_id());
                                    ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rscl.resultSet()) {
                                        // emergency TARDIS relocation
                                        new TARDISEmergencyRelocation(plugin).relocate(id, player);
                                        return;
                                    }
                                    COMPASS dir = rscl.getDirection();
                                    Location cl = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                                    if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                                        // get the exile area
                                        String permArea = plugin.getTardisArea().getExileArea(player);
                                        TARDISMessage.send(player, "EXILE", permArea);
                                        Location l = plugin.getTardisArea().getNextSpot(permArea);
                                        if (l == null) {
                                            TARDISMessage.send(player, "NO_MORE_SPOTS");
                                        } else {
                                            set.put("world", l.getWorld().getName());
                                            set.put("x", l.getBlockX());
                                            set.put("y", l.getBlockY());
                                            set.put("z", l.getBlockZ());
                                            set.put("direction", dir.toString());
                                            set.put("submarine", 0);
                                            TARDISMessage.send(player, "TRAVEL_APPROVED", permArea);
                                            set_dest = true;
                                        }
                                    } else {
                                        ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, rsc.getSecondary());
                                        if (rsr.resultSet()) {
                                            String environment = "THIS";
                                            int nether_min = plugin.getArtronConfig().getInt("nether_min");
                                            int the_end_min = plugin.getArtronConfig().getInt("the_end_min");
                                            byte[] repeaters = rsr.getRepeaters();
                                            if (repeaters[0] <= 3) { // first position
                                                environment = "THIS";
                                            }
                                            if (repeaters[0] >= 4 && repeaters[0] <= 7) { // second position
                                                environment = "NORMAL";
                                            }
                                            if (repeaters[0] >= 8 && repeaters[0] <= 11) { // third position
                                                if (plugin.getConfig().getBoolean("travel.nether") && player.hasPermission("tardis.nether")) {
                                                    // check they have enough artron energy to travel to the NETHER
                                                    if (level < nether_min) {
                                                        environment = "NORMAL";
                                                        TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", nether_min), "Nether");
                                                    } else {
                                                        environment = "NETHER";
                                                    }
                                                } else {
                                                    String message = (player.hasPermission("tardis.nether")) ? "ANCIENT" : "NO_PERM_TRAVEL";
                                                    TARDISMessage.send(player, message, "Nether");
                                                }
                                            }
                                            if (repeaters[0] >= 12 && repeaters[0] <= 15) { // last position
                                                if (plugin.getConfig().getBoolean("travel.the_end") && player.hasPermission("tardis.end")) {
                                                    // check they have enough artron energy to travel to THE_END
                                                    if (level < the_end_min) {
                                                        environment = "NORMAL";
                                                        TARDISMessage.send(player, "NOT_ENOUGH_TRAVEL_ENERGY", String.format("%d", the_end_min), "End");
                                                    } else {
                                                        environment = "THE_END";
                                                    }
                                                } else {
                                                    String message = (player.hasPermission("tardis.end")) ? "ANCIENT" : "NO_PERM_TRAVEL";
                                                    TARDISMessage.send(player, message, "End");
                                                }
                                            }
                                            // create a random destination
                                            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                                            Location rand = tt.randomDestination(player, repeaters[1], repeaters[2], repeaters[3], dir, environment, rscl.getWorld(), false, cl);
                                            if (rand != null) {
                                                set.put("world", rand.getWorld().getName());
                                                set.put("x", rand.getBlockX());
                                                set.put("y", rand.getBlockY());
                                                set.put("z", rand.getBlockZ());
                                                set.put("direction", dir.toString());
                                                set.put("submarine", (plugin.getTrackerKeeper().getSubmarine().contains(id)) ? 1 : 0);
                                                set_dest = true;
                                                plugin.getTrackerKeeper().getSubmarine().remove(Integer.valueOf(id));
                                                String dchat = rand.getWorld().getName() + " at x: " + rand.getBlockX() + " y: " + rand.getBlockY() + " z: " + rand.getBlockZ();
                                                boolean isTL = true;
                                                if (comps != null && !comps.isEmpty()) {
                                                    String[] companions = comps.split(":");
                                                    for (String c : companions) {
                                                        // are they online - AND are they travelling
                                                        UUID cuuid = UUID.fromString(c);
                                                        if (plugin.getServer().getPlayer(cuuid) != null) {
                                                            // are they travelling
                                                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                                                            wherec.put("tardis_id", id);
                                                            wherec.put("uuid", c);
                                                            ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherec, false);
                                                            if (rsv.resultSet()) {
                                                                TARDISMessage.send(plugin.getServer().getPlayer(cuuid), "DEST", dchat);
                                                            }
                                                        }
                                                        if (c.equalsIgnoreCase(player.getName())) {
                                                            isTL = false;
                                                        }
                                                    }
                                                }
                                                if (isTL == true) {
                                                    TARDISMessage.send(player, "DEST", dchat);
                                                } else {
                                                    if (plugin.getServer().getPlayer(ownerUUID) != null) {
                                                        TARDISMessage.send(plugin.getServer().getPlayer(ownerUUID), "DEST", dchat);
                                                    }
                                                }
                                            } else {
                                                TARDISMessage.send(player, "PROTECTED");
                                            }
                                        }
                                    }
                                    break;
                                case 8: // fast return button
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    cost = plugin.getArtronConfig().getInt("travel");
                                    if (level < cost) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    HashMap<String, Object> wherebl = new HashMap<String, Object>();
                                    wherebl.put("tardis_id", rs.getTardis_id());
                                    ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, wherebl);
                                    if (rsb.resultSet()) {
                                        HashMap<String, Object> wherecu = new HashMap<String, Object>();
                                        wherecu.put("tardis_id", rs.getTardis_id());
                                        ResultSetCurrentLocation rscu = new ResultSetCurrentLocation(plugin, wherecu);
                                        if (rscu.resultSet()) {
                                            if (!compareCurrentToBack(rscu, rsb)) {
                                                set.put("world", rsb.getWorld().getName());
                                                set.put("x", rsb.getX());
                                                set.put("y", rsb.getY());
                                                set.put("z", rsb.getZ());
                                                set.put("direction", rsb.getDirection().toString());
                                                set.put("submarine", (rsb.isSubmarine()) ? 1 : 0);
                                                set_dest = true;
                                                TARDISMessage.send(player, "PREV_SET", true);
                                            } else {
                                                TARDISMessage.send(player, "TRAVEL_NO_BACK");
                                            }
                                        }
                                    } else {
                                        TARDISMessage.send(player, "PREV_NOT_FOUND");
                                    }
                                    break;
                                case 9: // terminal sign
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (level < plugin.getArtronConfig().getInt("travel")) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput()) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    Inventory aec = plugin.getServer().createInventory(player, 54, "§4Destination Terminal");
                                    aec.setContents(items);
                                    player.openInventory(aec);
                                    break;
                                case 10: // ARS sign
                                    if (!hb) {
                                        TARDISMessage.send(player, "ARS_NO_TRAVEL");
                                        return;
                                    }
                                    // check they have permission to grow rooms
                                    if (!player.hasPermission("tardis.ars")) {
                                        TARDISMessage.send(player, "NO_PERM_ROOMS");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(rs.getChunk())) {
                                        TARDISMessage.send(player, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasARS()) {
                                        TARDISMessage.send(player, "ARS_MISSING");
                                        return;
                                    }
                                    Inventory ars = plugin.getServer().createInventory(player, 54, "§4Architectural Reconfiguration");
                                    ars.setContents(tars);
                                    player.openInventory(ars);
                                    break;
                                case 11: // Temporal Locator sign
                                    if (tcc != null && !tcc.hasTemporal()) {
                                        TARDISMessage.send(player, "TEMP_MISSING");
                                        return;
                                    }
                                    if (player.hasPermission("tardis.temporal")) {
                                        Inventory tmpl = plugin.getServer().createInventory(player, 27, "§4Temporal Locator");
                                        tmpl.setContents(clocks);
                                        player.openInventory(tmpl);
                                    }
                                    break;
                                case 12: // Control room light switch
                                    new TARDISLampToggler(plugin).flickSwitch(id, player);
//                                    HashMap<String, Object> wherel = new HashMap<String, Object>();
//                                    wherel.put("tardis_id", id);
//                                    ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
//                                    List<Block> lamps = new ArrayList<Block>();
//                                    if (rsl.resultSet()) {
//                                        // get lamp locations
//                                        ArrayList<HashMap<String, String>> data = rsl.getData();
//                                        for (HashMap<String, String> map : data) {
//                                            Location loc = plugin.getUtils().getLocationFromDB(map.get("location"), 0.0F, 0.0F);
//                                            lamps.add(loc.getBlock());
//                                        }
//                                    }
//                                    HashMap<String, Object> wherepp = new HashMap<String, Object>();
//                                    wherepp.put("uuid", player.getUniqueId().toString());
//                                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
//                                    boolean use_wool = false;
//                                    if (rsp.resultSet()) {
//                                        use_wool = rsp.isWoolLightsOn();
//                                    }
//                                    for (Block b : lamps) {
//                                        if (b.getType().equals(Material.REDSTONE_LAMP_ON)) {
//                                            if (use_wool) {
//                                                b.setType(Material.WOOL);
//                                                b.setData((byte) 15);
//                                            } else {
//                                                b.setType(Material.SPONGE);
//                                            }
//                                        } else if (b.getType().equals(Material.SPONGE) || (b.getType().equals(Material.WOOL) && b.getData() == (byte) 15)) {
//                                            b.setType(Material.REDSTONE_LAMP_ON);
//                                        }
//                                    }
                                    break;
                                case 13: // TIS
                                    plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TARDISInfoMenu.TIS);
                                    player.sendMessage(ChatColor.GOLD + "-----------TARDIS Information System-----------");
                                    player.sendMessage(ChatColor.GOLD + "---*" + plugin.getLanguage().getString("TIS_INFO") + "*---");
                                    player.sendMessage("§6> TARDIS §fM§6anual");
                                    player.sendMessage("§6> §fI§6tems");
                                    player.sendMessage("§6> §fC§6omponents");
                                    player.sendMessage("§6> §fD§6isks");
                                    player.sendMessage("§6> C§fo§6mmands");
                                    player.sendMessage("§6> §fT§6ARDIS Types");
                                    player.sendMessage("§6> §fR§6ooms");
                                    player.sendMessage("§6> §fE§6xit");
                                    break;
                                case 14: // Disk Storage
                                    UUID playerUUID = player.getUniqueId();
                                    // only the time lord of this tardis
                                    if (!ownerUUID.equals(playerUUID)) {
                                        TARDISMessage.send(player, "NOT_OWNER");
                                        return;
                                    }
                                    // do they have a storage record?
                                    HashMap<String, Object> wherestore = new HashMap<String, Object>();
                                    wherestore.put("uuid", playerUUID);
                                    ResultSetDiskStorage rsstore = new ResultSetDiskStorage(plugin, wherestore);
                                    ItemStack[] stack = new ItemStack[54];
                                    if (rsstore.resultSet()) {
                                        try {
                                            if (!rsstore.getSavesOne().isEmpty()) {
                                                stack = TARDISSerializeInventory.itemStacksFromString(rsstore.getSavesOne());
                                            } else {
                                                stack = TARDISSerializeInventory.itemStacksFromString(STORAGE.SAVE_1.getEmpty());
                                            }
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
                                        }
                                    } else {
                                        try {
                                            stack = TARDISSerializeInventory.itemStacksFromString(STORAGE.SAVE_1.getEmpty());
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get default Storage Inventory: " + ex.getMessage());
                                        }
                                        // make a record
                                        HashMap<String, Object> setstore = new HashMap<String, Object>();
                                        setstore.put("uuid", player.getUniqueId().toString());
                                        setstore.put("tardis_id", id);
                                        qf.doInsert("storage", setstore);
                                    }
                                    Inventory inv = plugin.getServer().createInventory(player, 54, STORAGE.SAVE_1.getTitle());
                                    inv.setContents(stack);
                                    player.openInventory(inv);
                                    break;
                                case 16: // enter zero room
                                    int zero_amount = plugin.getArtronConfig().getInt("zero");
                                    if (level < zero_amount) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
                                        return;
                                    }
                                    final Location zero = plugin.getUtils().getLocationFromDB(rs.getZero(), 0.0F, 0.0F);
                                    if (zero != null) {
                                        TARDISMessage.send(player, "ZERO_READY");
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero);
                                            }
                                        }, 20L);
                                        plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
                                        HashMap<String, Object> wherez = new HashMap<String, Object>();
                                        wherez.put("tardis_id", id);
                                        qf.alterEnergyLevel("tardis", -zero_amount, wherez, player);
                                    } else {
                                        TARDISMessage.send(player, "NO_ZERO");
                                    }
                                    break;
                                case 17:
                                    // exit zero room
                                    plugin.getTrackerKeeper().getZeroRoomOccupants().remove(player.getUniqueId());
                                    plugin.getGeneralKeeper().getRendererListener().transmat(player);
                                    break;
                                case 20:
                                    // toggle black wool blocks behind door
                                    new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                default:
                                    break;
                            }
                            if (set_dest) {
                                HashMap<String, Object> wherel = new HashMap<String, Object>();
                                wherel.put("tardis_id", id);
                                qf.doUpdate("next", set, wherel);
                                plugin.getTrackerKeeper().getHasDestination().put(id, cost);
                                if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                                    plugin.getTrackerKeeper().getRescue().remove(id);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean compareCurrentToBack(ResultSetCurrentLocation c, ResultSetBackLocation b) {
        return (c.getWorld().equals(b.getWorld())
                && c.getX() == b.getX()
                && c.getY() == b.getY()
                && c.getZ() == b.getZ());
    }
}
