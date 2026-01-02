/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.handles;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesTeleportCommand;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.doors.inner.*;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeMode;
import me.eccentric_nz.TARDIS.travel.TARDISRandomiserCircuit;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeUtilities;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Programming is a process used by Cybermen to control humans. To program a
 * human, the person has to be dead. A control is installed in the person,
 * powered by electricity, turning the person into an agent of the Cybermen.
 * Control over programmed humans can be shorted out by another signal, but that
 * kills whatever might be left of the person.
 *
 * @author eccentric_nz
 */
public class TARDISHandlesProcessor {

    private final TARDIS plugin;
    private final Program program;
    private final Player player;
    private final int pid;

    public TARDISHandlesProcessor(TARDIS plugin, Program program, Player player, int pid) {
        this.plugin = plugin;
        this.program = program;
        this.player = player;
        this.pid = pid;
    }

    public void processDisk() {
        String event = "";
        for (ItemStack is : program.getInventory()) {
            if (is != null) {
                TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(is.getItemMeta().displayName());
                switch (thb) {
                    case ARTRON, DEATH, DEMATERIALISE, ENTER, EXIT, HADS, LOG_OUT, MATERIALISE, SIEGE_OFF, SIEGE_ON ->
                            event = thb.toString();
                    default -> {
                    }
                }
            }
        }
        if (!event.isEmpty()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("parsed", event);
            HashMap<String, Object> where = new HashMap<>();
            where.put("program_id", pid);
            plugin.getQueryFactory().doUpdate("programs", set, where);
            plugin.getMessenger().handlesSend(player, "HANDLES_RUNNING");
        } else {
            // TODO check conditions
            processCommand(0);
            plugin.getMessenger().handlesSend(player, "HANDLES_EXECUTE");
        }
    }

    void processCommand(int pos) {
        for (int i = pos; i < 36; i++) {
            ItemStack is = program.getInventory()[i];
            if (is != null) {
                TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(is.getItemMeta().displayName());
                TARDISHandlesBlock next = getNext(i + 1);
                if (next != null) {
                    UUID uuid = player.getUniqueId();
                    // get TARDIS
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", uuid.toString());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        int id = tardis.getTardisId();
                        switch (thb) {
                            case DOOR -> {
                                switch (next) {
                                    case CLOSE -> {
                                        ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                                        if (rsp.fromID(id)) {
                                            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                            // close inner
                                            if (innerDisplayDoor.display()) {
                                                new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid, true);
                                            } else {
                                                new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid);
                                            }
                                            boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                                            // close outer
                                            if (outerDisplayDoor) {
                                                new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, uuid);
                                            } else if (rsp.getPreset().hasDoor()) {
                                                new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, uuid);
                                            }
                                        }
                                    }
                                    case OPEN -> {
                                        ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                                        if (rsp.fromID(id)) {
                                            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                                            // open inner
                                            if (innerDisplayDoor.display()) {
                                                new InnerDisplayDoorOpener(plugin).open(innerDisplayDoor.block(), id, true);
                                            } else {
                                                new InnerMinecraftDoorOpener(plugin).open(innerDisplayDoor.block(), id);
                                            }
                                            boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                                            // open outer
                                            if (outerDisplayDoor) {
                                                new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, uuid);
                                            } else if (rsp.getPreset().hasDoor()) {
                                                new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, uuid);
                                            }
                                        }
                                    }
                                    case LOCK, UNLOCK -> {
                                        HashMap<String, Object> whered = new HashMap<>();
                                        whered.put("tardis_id", id);
                                        whered.put("door_type", 0);
                                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                                        if (rsd.resultSet()) {
                                            if ((next.equals(TARDISHandlesBlock.LOCK) && !rsd.isLocked()) || (next.equals(TARDISHandlesBlock.UNLOCK) && rsd.isLocked())) {
                                                String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                                                int locked = (rsd.isLocked()) ? 0 : 1;
                                                HashMap<String, Object> setl = new HashMap<>();
                                                setl.put("locked", locked);
                                                HashMap<String, Object> wherel = new HashMap<>();
                                                wherel.put("tardis_id", id);
                                                // always lock / unlock both doors
                                                plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                                                plugin.getMessenger().handlesSend(player, "DOOR_LOCK", message);
                                            }
                                        }
                                    }
                                    default -> {
                                    }
                                }
                            }
                            case LIGHTS -> {
                                boolean onoff = next.equals(TARDISHandlesBlock.ON);
                                if ((onoff && !tardis.isLightsOn()) || (!onoff && tardis.isLightsOn())) {
                                    new TARDISLampToggler(plugin).flickSwitch(id, uuid, onoff, tardis.getSchematic().getLights());
                                }
                            }
                            case POWER -> {
                                switch (next) {
                                    case ON -> {
                                        if (!tardis.isPoweredOn()) {
                                            if (plugin.getConfig().getBoolean("allow.power_down")) {
                                                new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton();
                                            }
                                        }
                                    }
                                    case OFF -> {
                                        if (tardis.isPoweredOn()) {
                                            if (plugin.getConfig().getBoolean("allow.power_down")) {
                                                new TARDISPowerButton(plugin, id, player, tardis.getPreset(), true, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton();
                                            }
                                        }
                                    }
                                    case SHOW -> plugin.getMessenger().sendArtron(player, id, 0);
                                    case REDSTONE -> {
                                        // press the Handles button
                                        HashMap<String, Object> whereh = new HashMap<>();
                                        whereh.put("tardis_id", id);
                                        whereh.put("type", 26);
                                        ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
                                        if (rsh.resultSet()) {
                                            Location handles = TARDISStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                                            Block block = handles.getBlock();
                                            Powerable button = (Powerable) block.getBlockData();
                                            if (!button.isPowered()) {
                                                button.setPowered(true);
                                            }
                                            block.setBlockData(button, true);
                                        }
                                    }
                                    default -> {
                                    }
                                }
                            }
                            case SIEGE -> {
                                if ((next.equals(TARDISHandlesBlock.ON) && !tardis.isSiegeOn()) || (next.equals(TARDISHandlesBlock.OFF) && tardis.isSiegeOn())) {
                                    new TARDISSiegeMode(plugin).toggleViaSwitch(id, player);
                                }
                            }
                            case TRAVEL -> {
                                // get tardis artron level
                                ResultSetTardisArtron rsArtron = new ResultSetTardisArtron(plugin);
                                if (!rsArtron.fromID(id)) {
                                    continue;
                                }
                                int level = rsArtron.getArtronLevel();
                                int travel = plugin.getArtronConfig().getInt("travel");
                                if (level < travel) {
                                    plugin.getMessenger().handlesSend(player, "NOT_ENOUGH_ENERGY");
                                    continue;
                                }
                                ItemStack after = program.getInventory()[i + 1];
                                List<Component> lore = after.getItemMeta().lore();
                                if (lore != null) {
                                    String first = ComponentUtils.stripColour(lore.getFirst());
                                    // get current location
                                    ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                                    if (rsc.resultSet()) {
                                        Current current = rsc.getCurrent();
                                        Location goto_loc = null;
                                        COMPASS direction = current.direction();
                                        COMPASS nextDirection = current.direction();
                                        int x = current.location().getBlockX();
                                        int y = current.location().getBlockY();
                                        int z = current.location().getBlockZ();
                                        boolean sub = false;
                                        TravelType travelType = TravelType.SAVE;
                                        switch (next) {
                                            case RANDOM -> {
                                                Location random = new TARDISRandomiserCircuit(plugin).getRandomlocation(player, direction);
                                                if (random != null) {
                                                    plugin.getTrackerKeeper().getHasRandomised().add(id);
                                                    plugin.getMessenger().handlesSend(player, "RANDOMISER");
                                                    goto_loc = random;
                                                    sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                                                }
                                                travelType = TravelType.RANDOM;
                                            }
                                            case X -> {
                                                // if X comes after travel then we'll look for Y and Z
                                                ItemStack coordX = program.getInventory()[i + 2];
                                                TARDISHandlesBlock coordBlockX = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordX.getItemMeta().displayName()));
                                                x = getNumber(coordBlockX, i + 2);
                                                // find Y
                                                int fy = find(TARDISHandlesBlock.Y, i + 3);
                                                if (fy > 0) {
                                                    ItemStack coordY = program.getInventory()[fy];
                                                    TARDISHandlesBlock coordBlockY = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordY.getItemMeta().displayName()));
                                                    y = getNumber(coordBlockY, fy);
                                                }
                                                // find Z
                                                int fz = find(TARDISHandlesBlock.Z, i + 3);
                                                if (fz > 0) {
                                                    ItemStack coordZ = program.getInventory()[fz];
                                                    TARDISHandlesBlock coordBlockZ = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordZ.getItemMeta().displayName()));
                                                    z = getNumber(coordBlockZ, fz);
                                                }
                                                goto_loc = new Location(current.location().getWorld(), x, y, z);
                                                travelType = TravelType.COORDINATES;
                                            }
                                            case Y -> {
                                                // if Y comes after travel then X use current coords, and we'll look for Z
                                                ItemStack coordY = program.getInventory()[i + 2];
                                                TARDISHandlesBlock coordBlockY = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordY.getItemMeta().displayName()));
                                                y = getNumber(coordBlockY, i + 2);
                                                // find Z
                                                int fyz = find(TARDISHandlesBlock.Z, i + 3);
                                                if (fyz > 0) {
                                                    ItemStack coordZ = program.getInventory()[fyz];
                                                    TARDISHandlesBlock coordBlockZ = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordZ.getItemMeta().displayName()));
                                                    z = getNumber(coordBlockZ, fyz);
                                                }
                                                goto_loc = new Location(current.location().getWorld(), x, y, z);
                                                travelType = TravelType.RELATIVE_COORDINATES;
                                            }
                                            case Z -> {
                                                // if Z comes after travel then X and Y will use current coords
                                                ItemStack coordZ = program.getInventory()[i + 2];
                                                TARDISHandlesBlock coordBlockZ = TARDISHandlesBlock.valueOf(ComponentUtils.stripColour(coordZ.getItemMeta().displayName()));
                                                z = getNumber(coordBlockZ, i + 2);
                                                goto_loc = new Location(current.location().getWorld(), x, y, z);
                                                travelType = TravelType.RELATIVE_COORDINATES;
                                            }
                                            case HOME -> {
                                                // get home location
                                                HashMap<String, Object> wherehl = new HashMap<>();
                                                wherehl.put("tardis_id", id);
                                                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                                if (!rsh.resultSet()) {
                                                    plugin.getMessenger().handlesSend(player, "HOME_NOT_FOUND");
                                                    continue;
                                                }
                                                plugin.getMessenger().handlesSend(player, "TRAVEL_LOADED", "Home");
                                                goto_loc = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                                                nextDirection = rsh.getDirection();
                                                sub = rsh.isSubmarine();
                                                if (!rsh.getPreset().isEmpty()) {
                                                    // set the chameleon preset
                                                    HashMap<String, Object> wherep = new HashMap<>();
                                                    wherep.put("tardis_id", id);
                                                    HashMap<String, Object> setp = new HashMap<>();
                                                    setp.put("chameleon_preset", rsh.getPreset());
                                                    // set chameleon adaption to OFF
                                                    setp.put("adapti_on", 0);
                                                    plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
                                                }
                                                travelType = TravelType.HOME;
                                            }
                                            case RECHARGER -> {
                                                Location recharger = getRecharger(current.location().getWorld(), player);
                                                if (recharger != null) {
                                                    plugin.getMessenger().handlesSend(player, "RECHARGER_FOUND");
                                                    goto_loc = recharger;
                                                } else {
                                                    plugin.getMessenger().handlesSend(player, "NO_MORE_SPOTS");
                                                    continue;
                                                }
                                                travelType = TravelType.RECHARGER;
                                            }
                                            case AREA_DISK -> {
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
                                                    plugin.getMessenger().handlesSend(player, "TRAVEL_NO_AREA_PERM", first);
                                                    continue;
                                                }
                                                Location l;
                                                if (rsa.getArea().grid()) {
                                                    l = plugin.getTardisArea().getNextSpot(rsa.getArea().areaName());
                                                } else {
                                                    l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
                                                }
                                                if (l == null) {
                                                    plugin.getMessenger().handlesSend(player, "NO_MORE_SPOTS");
                                                    continue;
                                                }
                                                plugin.getMessenger().handlesSend(player, "TRAVEL_APPROVED", first);
                                                goto_loc = l;
                                                travelType = TravelType.AREA;
                                            }
                                            case BIOME_DISK -> {
                                                // find a biome location
                                                if (!TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                                    plugin.getMessenger().handlesSend(player, "TRAVEL_NO_PERM_BIOME");
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
                                                        plugin.getMessenger().handlesSend(player, "BIOME_NOT_VALID");
                                                        continue;
                                                    }
                                                }
                                                plugin.getMessenger().handlesSend(player, "BIOME_SEARCH");
                                                Location nsob = BiomeUtilities.searchBiome(current.location().getWorld(), biome, current.location());
                                                if (nsob == null) {
                                                    plugin.getMessenger().handlesSend(player, "BIOME_NOT_FOUND");
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
                                                    int[] start_loc = TARDISTimeTravel.getStartLocation(nsob, direction);
                                                    int tmp_y = nsob.getBlockY();
                                                    for (int up = 0; up < 10; up++) {
                                                        int count = TARDISTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), direction);
                                                        if (count == 0) {
                                                            nsob.setY(tmp_y + up);
                                                            break;
                                                        }
                                                    }
                                                    plugin.getMessenger().handlesSend(player, "BIOME_SET");
                                                    goto_loc = nsob;
                                                }
                                                travelType = TravelType.BIOME;
                                            }
                                            case PLAYER_DISK -> {
                                                // get the player's location
                                                if (TARDISPermission.hasPermission(player, "tardis.timetravel.player")) {
                                                    if (player.getName().equalsIgnoreCase(first)) {
                                                        plugin.getMessenger().handlesSend(player, "TRAVEL_NO_SELF");
                                                        continue;
                                                    }
                                                    // get the player
                                                    Player to = plugin.getServer().getPlayer(first);
                                                    if (to == null) {
                                                        plugin.getMessenger().handlesSend(player, "NOT_ONLINE");
                                                        continue;
                                                    }
                                                    UUID toUUID = to.getUniqueId();
                                                    // check the to player's DND status
                                                    ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, toUUID.toString());
                                                    if (rspp.resultSet() && rspp.isDND()) {
                                                        plugin.getMessenger().handlesSend(player, "DND", first);
                                                        continue;
                                                    }
                                                    Location player_loc = to.getLocation();
                                                    if (plugin.getTardisArea().isInExistingArea(player_loc)) {
                                                        plugin.getMessenger().sendColouredCommand(player, "PLAYER_IN_AREA", "/tardistravel area [area name]", plugin);
                                                        continue;
                                                    }
                                                    if (!plugin.getPluginRespect().getRespect(player_loc, new Parameters(player, Flag.getDefaultFlags()))) {
                                                        continue;
                                                    }
                                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + player_loc.getWorld().getName() + ".time_travel")) {
                                                        plugin.getMessenger().handlesSend(player, "NO_WORLD_TRAVEL");
                                                        continue;
                                                    }
                                                    World w = player_loc.getWorld();
                                                    int[] start_loc = TARDISTimeTravel.getStartLocation(player_loc, direction);
                                                    int count = TARDISTimeTravel.safeLocation(start_loc[0] - 3, player_loc.getBlockY(), start_loc[2], start_loc[1] - 3, start_loc[3], w, direction);
                                                    if (count > 0) {
                                                        plugin.getMessenger().handlesSend(player, "RESCUE_NOT_SAFE");
                                                        continue;
                                                    }
                                                    goto_loc = player_loc;
                                                } else {
                                                    plugin.getMessenger().handlesSend(player, "NO_PERM_PLAYER");
                                                    continue;
                                                }
                                                travelType = TravelType.PLAYER;
                                            }
                                            case SAVE_DISK -> {
                                                if (TARDISPermission.hasPermission(player, "tardis.save")) {
                                                    int sx = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(2)));
                                                    int sy = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(3)));
                                                    int sz = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(4)));
                                                    if (current.location().getWorld().getName().equals(lore.get(1)) && current.location().getBlockX() == sx && current.location().getBlockZ() == sz) {
                                                        continue;
                                                    }
                                                    plugin.getMessenger().handlesSend(player, "LOC_SET");
                                                    goto_loc = new Location(TARDISAliasResolver.getWorldFromAlias(ComponentUtils.stripColour(lore.get(1))), sx, sy, sz);
                                                    nextDirection = COMPASS.valueOf(ComponentUtils.stripColour(lore.get(6)));
                                                    sub = Boolean.parseBoolean(ComponentUtils.stripColour(lore.get(7)));
                                                } else {
                                                    plugin.getMessenger().handlesSend(player, "TRAVEL_NO_PERM_SAVE");
                                                    continue;
                                                }
                                            }
                                            default -> {
                                            }
                                        }
                                        if (goto_loc != null) {
                                            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, travelType));
                                            plugin.getTrackerKeeper().getRescue().remove(id);
                                            if (!plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                                plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                                                DamageUtility.run(plugin, DiskCircuit.MEMORY, id, player);
                                            }
                                            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                                // destroy police box
                                                DestroyData dd = new DestroyData();
                                                dd.setDirection(direction);
                                                dd.setLocation(current.location());
                                                dd.setPlayer(player);
                                                dd.setHide(false);
                                                dd.setOutside(false);
                                                dd.setSubmarine(current.submarine());
                                                dd.setTardisID(id);
                                                dd.setThrottle(SpaceTimeThrottle.NORMAL);
                                                // set handbrake off
                                                HashMap<String, Object> set = new HashMap<>();
                                                set.put("handbrake_on", 0);
                                                HashMap<String, Object> tid = new HashMap<>();
                                                tid.put("tardis_id", id);
                                                if (!tardis.isHidden()) {
                                                    plugin.getPresetDestroyer().destroyPreset(dd);
                                                    plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisID());
                                                    plugin.getTrackerKeeper().getInVortex().add(id);
                                                    // play tardis_takeoff sfx
                                                    TARDISSounds.playTARDISSound(current.location(), "tardis_takeoff");
                                                } else {
                                                    plugin.getPresetDestroyer().removeBlockProtection(id);
                                                    set.put("hidden", 0);
                                                }
                                                plugin.getQueryFactory().doUpdate("tardis", set, tid);
                                            }
                                            BuildData bd = new BuildData(uuid.toString());
                                            bd.setDirection(nextDirection);
                                            bd.setLocation(goto_loc);
                                            bd.setMalfunction(false);
                                            bd.setPlayer(player);
                                            bd.setRebuild(false);
                                            bd.setOutside(false);
                                            bd.setSubmarine(sub);
                                            bd.setTardisID(id);
                                            bd.setThrottle(SpaceTimeThrottle.NORMAL);
                                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                                // rebuild police box - needs to be a delay
                                                plugin.getPresetBuilder().buildPreset(bd);
                                                plugin.getTrackerKeeper().getInVortex().add(id);
                                                // play tardis_land sfx
                                                TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land");
                                                // set handbrake on
                                                HashMap<String, Object> seth = new HashMap<>();
                                                seth.put("handbrake_on", 1);
                                                HashMap<String, Object> wheret = new HashMap<>();
                                                wheret.put("tardis_id", id);
                                                plugin.getQueryFactory().doUpdate("tardis", seth, wheret);
                                            }, 500L);
                                            // set current
                                            HashMap<String, Object> setc = new HashMap<>();
                                            setc.put("world", goto_loc.getWorld().getName());
                                            setc.put("x", goto_loc.getBlockX());
                                            setc.put("y", goto_loc.getBlockY());
                                            setc.put("z", goto_loc.getBlockZ());
                                            setc.put("direction", nextDirection.toString());
                                            setc.put("submarine", (sub) ? 1 : 0);
                                            HashMap<String, Object> wherec = new HashMap<>();
                                            wherec.put("tardis_id", id);
                                            plugin.getQueryFactory().doUpdate("current", setc, wherec);
                                            // set back
                                            HashMap<String, Object> setb = new HashMap<>();
                                            setb.put("world", current.location().getWorld().getName());
                                            setb.put("x", current.location().getBlockX());
                                            setb.put("y", current.location().getBlockY());
                                            setb.put("z", current.location().getBlockZ());
                                            setb.put("direction", direction.toString());
                                            setb.put("submarine", (current.submarine()) ? 1 : 0);
                                            HashMap<String, Object> whereb = new HashMap<>();
                                            whereb.put("tardis_id", id);
                                            plugin.getQueryFactory().doUpdate("back", setb, whereb);
                                            // take energy
                                            HashMap<String, Object> wherea = new HashMap<>();
                                            wherea.put("tardis_id", id);
                                            plugin.getQueryFactory().alterEnergyLevel("tardis", -travel, wherea, player);
                                        }
                                    }
                                }
                            }
                            case HIDE -> player.performCommand("tardis hide");
                            case REBUILD -> player.performCommand("tardis rebuild");
                            case SCAN ->
                                    plugin.getServer().dispatchCommand(plugin.getConsole(), "handles scan " + uuid + " " + id);
                            case COMEHERE -> new TARDISHandlesTeleportCommand(plugin).beamMeUp(player);
                            case TAKE_OFF -> {
                                // player must be in TARDIS
                                if (plugin.getUtils().inTARDISWorld(player.getLocation())) {
                                    plugin.getServer().dispatchCommand(plugin.getConsole(), "handles takeoff " + uuid + " " + id);
                                }
                            }
                            case LAND -> plugin.getServer().dispatchCommand(plugin.getConsole(),
                                    "handles land " + uuid + " " + id);
                            default -> {
                            }
                        }
                    }
                }
            }
        }
    }

    void processArtronCommand(int pos) {
        boolean first = true;
        boolean process;
        TARDISHandlesBlock comparison = null;
        int level;
        for (int i = pos; i < 36; i++) {
            ItemStack is = program.getInventory()[i];
            if (is != null) {
                TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(is.getItemMeta().displayName()));
                switch (thb) {
                    case LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, EQUALS ->
                            comparison = thb; // operator
                    case ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO -> {
                        // find all sequential number blocks
                        if (first) {
                            level = getNumber(thb, i);
                            // get the current Artron level
                            ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                            if (rs.fromUUID(player.getUniqueId().toString())) {
                                process = switch (comparison) {
                                    case LESS_THAN -> (level < rs.getArtronLevel());
                                    case LESS_THAN_EQUAL -> (level <= rs.getArtronLevel());
                                    case GREATER_THAN -> (level > rs.getArtronLevel());
                                    case GREATER_THAN_EQUAL -> (level >= rs.getArtronLevel());
                                    default -> (level == rs.getArtronLevel()); // EQUALS
                                };
                                if (!process) {
                                    return;
                                }
                            }
                            first = false;
                        }
                    }
                    case DO -> processCommand(i);
                    default -> {
                    }
                }
            }
        }
    }

    private TARDISHandlesBlock getNext(int i) {
        if (i > 35) {
            return null;
        }
        ItemStack num = program.getInventory()[i];
        if (num != null) {
            return TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(num.getItemMeta().displayName()));
        }
        return null;
    }

    private Location getRecharger(World world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("world", world.getName());
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        if (rsa.resultSet()) {
            String area = rsa.getArea().areaName();
            if (!TARDISPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            if (rsa.getArea().grid()) {
                l = plugin.getTardisArea().getNextSpot(area);
            } else {
                l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
            }
        }
        return l;
    }

    private int getNumber(TARDISHandlesBlock thb, int i) {
        String tmp = thb.getDisplayName();
        int n = 1;
        ItemStack num = program.getInventory()[i + n];
        while (num != null) {
            TARDISHandlesBlock numBlock = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(num.getItemMeta().displayName()));
            if (numBlock.getCategory().equals(TARDISHandlesCategory.NUMBER)) {
                tmp += numBlock.getDisplayName();
            } else {
                break;
            }
            n++;
            num = program.getInventory()[i + n];
        }
        return TARDISNumberParsers.parseInt(tmp);
    }

    private int find(TARDISHandlesBlock thb, int i) {
        if (i > 35) {
            return -1;
        }
        for (int n = i; n < 34; n++) {
            ItemStack yOrZ = program.getInventory()[n];
            if (yOrZ != null) {
                TARDISHandlesBlock block = TARDISHandlesBlock.BY_NAME.get(ComponentUtils.stripColour(yOrZ.getItemMeta().displayName()));
                if (block == thb) {
                    return n + 1;
                }
            }
        }
        return -1;
    }
}
