package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.protection.TARDISLWCChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class TARDISSonicDock {

    private final TARDIS plugin;

    public TARDISSonicDock(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void dock(int id, Block block, Player player, ItemStack sonic) {
        // check for existing display item
        if (TARDISDisplayItemUtils.get(block) != null) {
            return;
        }
        // get sonic uuid
        UUID uuid = sonic.getItemMeta().getPersistentDataContainer().get(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID());
        // set item display
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(sonic);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        display.setBillboard(Display.Billboard.FIXED);
        display.setInvulnerable(true);
        // remove item from hand
        player.getInventory().setItemInMainHand(null);
        if (uuid != null) {
            // get last scan coordinates
            ResultSetSonicLocation rssc = new ResultSetSonicLocation(plugin, uuid);
            if (rssc.resultset()) {
                // check destination
                Location dest = rssc.getLocation();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                    return;
                }
                if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                    return;
                }
                if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && dest.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                    return;
                }
                if (!plugin.getPluginRespect().getRespect(dest, new Parameters(player, Flag.getDefaultFlags()))) {
                    return;
                }
                if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    String areaPerm = plugin.getTardisArea().getExileArea(player);
                    if (plugin.getTardisArea().areaCheckInExile(areaPerm, dest)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE_NO_TRAVEL");
                        return;
                    }
                }
                if (plugin.getTardisArea().isInExistingArea(dest)) {
                    plugin.getMessenger().sendColouredCommand(player, "AREA_NO_SONIC", "/tardistravel area [area name]", plugin);
                    return;
                }
                // check the world is not excluded
                String world = dest.getWorld().getName();
                if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_WORLD");
                    return;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                        return;
                    }
                    TARDISCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                        tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                        return;
                    }
                    boolean hidden = tardis.isHidden();
                    // get TARDIS's current location
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        hidden = true;
                    }
                    COMPASS d = rsc.getDirection();
                    COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int count;
                    int[] start_loc = TARDISTimeTravel.getStartLocation(dest, player_d);
                    // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS player_d)
                    count = TARDISTimeTravel.safeLocation(start_loc[0], dest.getBlockY(), start_loc[2], start_loc[1], start_loc[3], dest.getWorld(), player_d);
                    Block under = dest.getBlock().getRelative(BlockFace.DOWN);
                    if (plugin.getPM().isPluginEnabled("BlockLocker") && (BlockLockerAPIv2.isProtected(dest.getBlock()) || BlockLockerAPIv2.isProtected(under))) {
                        count = 1;
                    }
                    if (plugin.getPM().isPluginEnabled("LWC") && new TARDISLWCChecker().isBlockProtected(dest.getBlock(), under, player)) {
                        count = 1;
                    }
                    if (count > 0) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "WOULD_GRIEF_BLOCKS");
                        return;
                    }
                    SpaceTimeThrottle spaceTimeThrottle = new ResultSetThrottle(plugin).getSpeed(uuid.toString());
                    int ch = Math.round(plugin.getArtronConfig().getInt("comehere") * spaceTimeThrottle.getArtronMultiplier());
                    if (tardis.getArtron_level() < ch) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                        return;
                    }
                    // set next location
                    HashMap<String, Object> tid = new HashMap<>();
                    tid.put("tardis_id", id);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", dest.getWorld().getName());
                    set.put("x", dest.getBlockX());
                    set.put("y", dest.getBlockY());
                    set.put("z", dest.getBlockZ());
                    set.put("submarine", 0);
                    plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                    plugin.getMessenger().send(player, "LOC_SAVED", true);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.RANDOM));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    // get player prefs
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                    if (rsp.resultSet() && rsp.isAutoHandbrakeOn()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_DEST");
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDBRAKE_OFF");
                        // get handbrake location
                        HashMap<String, Object> whereh = new HashMap<>();
                        whereh.put("tardis_id", id);
                        whereh.put("type", 0);
                        whereh.put("secondary", 0);
                        ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
                        if (rsh.resultSet()) {
                            // check if door is open
                            if (isDoorOpen(id)) {
                                plugin.getMessenger().sendStatus(player, "DOOR_CLOSE");
                                // track handbrake clicked for takeoff when door closed
                                plugin.getTrackerKeeper().getHasClickedHandbrake().add(id);
                                // give them 30 seconds to close the door
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id)), 600L);
                                return;
                            }
                            Location handbrake_loc = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                            // TODO start time rotor & fix timings & handbrake to exit message
                            new TARDISTakeoff(plugin).run(id, handbrake_loc.getBlock(), handbrake_loc, player, rsp.isBeaconOn(), tardis.getBeacon(), rsp.isTravelbarOn(), spaceTimeThrottle);
                        } else {
                            plugin.debug("no handbrake");
                        }
                    } else {
                        // need to release the handbrake
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("comehere"), TravelType.SONIC_DOCK));
                        plugin.getMessenger().send(player, "SONIC_DEST", true);
                    }
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOCK_NOT_SCANNED");
        }
    }

    public void undock(Block block, Player player) {
        // check for existing display item
        ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
        if (display == null) {
            return;
        }
        // get the itemstack
        ItemStack sonic = display.getItemStack();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.getInventory().setItemInMainHand(sonic);
        } else {
            player.getInventory().addItem(sonic);
        }
        display.remove();
    }

    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block door = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock();
            return TARDISStaticUtils.isDoorOpen(door);
        }
        return false;
    }
}
