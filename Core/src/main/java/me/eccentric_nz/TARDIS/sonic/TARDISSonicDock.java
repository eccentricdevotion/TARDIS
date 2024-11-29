package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISSculkShrieker;
import me.eccentric_nz.TARDIS.console.interaction.SonicConsoleRecharge;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodeldata.keys.SonicItem;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.sonic.actions.SonicRecharge;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.Handbrake;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.protection.TARDISLWCChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class TARDISSonicDock {

    private final TARDIS plugin;

    public TARDISSonicDock(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static ItemFrame getItemFrame(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 48);
        ResultSetControls rsc = new ResultSetControls(TARDIS.plugin, where, false);
        if (rsc.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                BoundingBox box = new BoundingBox(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getBlockX() + 1, location.getBlockY() + 1, location.getBlockZ() + 1);
                for (Entity e : location.getWorld().getNearbyEntities(box, (e) -> e.getType() == EntityType.ITEM_FRAME)) {
                    if (e instanceof ItemFrame frame && frame.getItem().getType() == Material.FLOWER_POT) {
                        return frame;
                    }
                }
            }
        }
        return null;
    }

    public void dock(int id, Interaction interaction, Player player, ItemStack sonic) {
        // check for existing display item
        if (TARDISDisplayItemUtils.getSonic(interaction) != null) {
            return;
        }
        ItemDisplay display = doDocking(sonic, interaction.getLocation(), new Vector(0, 0.75d, 0.1d), player, id);
        display.setRotation(0.0f, 15.0f);
        // start charging
        if (plugin.getConfig().getBoolean("sonic.charge")) {
            long delay = plugin.getConfig().getLong("sonic.charge_level") / plugin.getConfig().getLong("sonic.charge_interval");
            SonicConsoleRecharge recharge = new SonicConsoleRecharge(plugin, display.getUniqueId(), interaction, id, player);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, recharge, 1L, delay);
            recharge.setTask(task);
        }
    }

    public void dock(int id, ItemFrame frame, Player player, ItemStack sonic) {
        Block block = frame.getLocation().getBlock();
        // check for existing display item
        if (TARDISDisplayItemUtils.get(block) != null) {
            return;
        }
        ItemDisplay display = doDocking(sonic, block.getLocation(), new Vector(0.5d, 0.5d, 0.5d), player, id);
        // change the dock model
        updateModel(frame, SonicItem.SONIC_DOCK_ON.getKey(), false);
        // start charging
        if (plugin.getConfig().getBoolean("sonic.charge")) {
            long delay = plugin.getConfig().getLong("sonic.charge_level") / plugin.getConfig().getLong("sonic.charge_interval");
            SonicRecharge recharge = new SonicRecharge(plugin, display.getUniqueId(), frame, id, player);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, recharge, 1L, delay);
            recharge.setTask(task);
        }
    }

    private ItemDisplay doDocking(ItemStack sonic, Location location, Vector vector, Player player, int id) {
        // remove enchantments if any
        sonic.removeEnchantment(Enchantment.UNBREAKING);
        // get sonic uuid
        UUID uuid = sonic.getItemMeta().getPersistentDataContainer().get(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID());
        // set item display
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(vector), EntityType.ITEM_DISPLAY);
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
                if (dest != null) {
                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                        return display;
                    }
                    if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                        plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                        return display;
                    }
                    if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                        return display;
                    }
                    if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && dest.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                        return display;
                    }
                    if (!plugin.getPluginRespect().getRespect(dest, new Parameters(player, Flag.getDefaultFlags()))) {
                        return display;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                        String areaPerm = plugin.getTardisArea().getExileArea(player);
                        if (plugin.getTardisArea().areaCheckInExile(areaPerm, dest)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE_NO_TRAVEL");
                            return display;
                        }
                    }
                    if (plugin.getTardisArea().isInExistingArea(dest)) {
                        plugin.getMessenger().sendColouredCommand(player, "AREA_NO_SONIC", "/tardistravel area [area name]", plugin);
                        return display;
                    }
                    // check the world is not excluded
                    String world = dest.getWorld().getName();
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_WORLD");
                        return display;
                    }
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return display;
                        }
                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true) && !tcc.hasMaterialisation()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                            return display;
                        }
                        // damage circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                            // decrement uses
                            int uses_left = tcc.getMaterialisationUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                        }
                        COMPASS player_direction = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                        int[] start_loc = TARDISTimeTravel.getStartLocation(dest, player_direction);
                        int count = TARDISTimeTravel.safeLocation(start_loc[0], dest.getBlockY(), start_loc[2], start_loc[1], start_loc[3], dest.getWorld(), player_direction);
                        Block under = dest.getBlock().getRelative(BlockFace.DOWN);
                        if (plugin.getPM().isPluginEnabled("BlockLocker") && (BlockLockerAPIv2.isProtected(dest.getBlock()) || BlockLockerAPIv2.isProtected(under))) {
                            count = 1;
                        }
                        if (plugin.getPM().isPluginEnabled("LWC") && new TARDISLWCChecker().isBlockProtected(dest.getBlock(), under, player)) {
                            count = 1;
                        }
                        if (count > 0) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "WOULD_GRIEF_BLOCKS");
                            return display;
                        }
                        Throticle throticle = new ResultSetThrottle(plugin).getSpeedAndParticles(player.getUniqueId().toString());
                        int ch = Math.round(plugin.getArtronConfig().getInt("comehere") * throticle.getThrottle().getArtronMultiplier());
                        if (tardis.getArtronLevel() < ch) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                            return display;
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
                        if (new Handbrake(plugin).isRelativityDifferentiated(id)) {
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
                                    return display;
                                }
                                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                                if (rsp.resultSet()) {
                                    Location handbrake_loc = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                                    // take off
                                    new TARDISTakeoff(plugin).run(id, handbrake_loc.getBlock(), handbrake_loc, player, rsp.isBeaconOn(), tardis.getBeacon(), rsp.isTravelbarOn(), throticle);
                                    // start time rotor?
                                    if (tardis.getRotor() != null) {
                                        if (tardis.getRotor() == TARDISConstants.UUID_ZERO) {
                                            // get sculk shrieker and set shreiking
                                            TARDISSculkShrieker.setRotor(id);
                                        } else {
                                            ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                                            if (itemFrame != null) {
                                                // get the rotor type
                                                Rotor rotor = Rotor.getByModel(TARDISTimeRotor.getRotorModel(itemFrame));
                                                TARDISTimeRotor.setRotor(rotor, itemFrame);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // need to release the handbrake
                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("comehere"), TravelType.SONIC_DOCK));
                            plugin.getMessenger().send(player, "SONIC_DEST", true);
                        }
                    }
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOCK_NOT_SCANNED");
        }
        return display;
    }

    public void undock(Interaction interaction, Player player) {
        // check for existing display item
        ItemDisplay display = TARDISDisplayItemUtils.getSonic(interaction);
        if (display == null) {
            return;
        }
        doUndock(display, player);
    }

    public void undock(ItemFrame frame, Player player) {
        // check for existing display item
        ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(frame.getLocation().getBlock());
        if (display == null) {
            return;
        }
        doUndock(display, player);
        // change the dock model
        updateModel(frame, SonicItem.SONIC_DOCK_OFF.getKey(), true);
    }

    private void doUndock(ItemDisplay display, Player player) {
        // get the itemstack
        ItemStack sonic = display.getItemStack();
        // set the charge level in lore
        SonicLore.setChargeLevel(sonic);
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            player.getInventory().setItemInMainHand(sonic);
        } else {
            player.getInventory().addItem(sonic);
        }
        display.remove();
    }

    private void updateModel(ItemFrame frame, NamespacedKey model, boolean setDisplay) {
        ItemStack dock = frame.getItem();
        ItemMeta im = dock.getItemMeta();
        im.setItemModel(model);
        if (setDisplay) {
            im.setDisplayName("Sonic Dock");
        }
        dock.setItemMeta(im);
        frame.setItem(dock);
        frame.setSilent(true);
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
