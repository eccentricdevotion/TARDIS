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
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.advanced.TardisCircuitDamager;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.artron.TardisArtronIndicator;
import me.eccentric_nz.tardis.artron.TardisLampToggler;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.commands.handles.TardisHandlesTeleportCommand;
import me.eccentric_nz.tardis.control.TardisPowerButton;
import me.eccentric_nz.tardis.database.data.Program;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.*;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisDoorCloser;
import me.eccentric_nz.tardis.move.TardisDoorOpener;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.siegemode.TardisSiegeMode;
import me.eccentric_nz.tardis.travel.TardisRandomiserCircuit;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Programming is a process used by Cybermen to control humans. To program a human, the person has to be dead. A control
 * is installed in the person, powered by electricity, turning the person into an agent of the Cybermen. Control over
 * programmed humans can be shorted out by another signal, but that kills whatever might be left of the person.
 *
 * @author eccentric_nz
 */
public class TardisHandlesProcessor {

    private final TardisPlugin plugin;
    private final Program program;
    private final Player player;
    private final int pid;

    public TardisHandlesProcessor(TardisPlugin plugin, Program program, Player player, int pid) {
        this.plugin = plugin;
        this.program = program;
        this.player = player;
        this.pid = pid;
    }

    public void processDisk() {
        String event = "";
        for (ItemStack is : program.getInventory()) {
            if (is != null) {
                TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
                switch (thb) {
                    case ARTRON:
                    case DEATH:
                    case DEMATERIALISE:
                    case ENTER:
                    case EXIT:
                    case HADS:
                    case LOG_OUT:
                    case MATERIALISE:
                    case SIEGE_OFF:
                    case SIEGE_ON:
                        event = thb.toString();
                        break;
                    default:
                        break;
                }
            }
        }
        if (!event.isEmpty()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("parsed", event);
            HashMap<String, Object> where = new HashMap<>();
            where.put("program_id", pid);
            plugin.getQueryFactory().doUpdate("programs", set, where);
            TardisMessage.handlesSend(player, "HANDLES_RUNNING");
        } else {
            // TODO Check conditions
            processCommand(0);
            TardisMessage.handlesSend(player, "HANDLES_EXECUTE");
        }
    }

    void processCommand(int pos) {
        for (int i = pos; i < 36; i++) {
            ItemStack is = program.getInventory()[i];
            if (is != null) {
                TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
                TardisHandlesBlock next = getNext(i + 1);
                if (next != null) {
                    UUID uuid = player.getUniqueId();
                    // get TARDIS
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", uuid.toString());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        int id = tardis.getTardisId();
                        switch (thb) {
                            case DOOR:
                                switch (next) {
                                    case CLOSE -> new TardisDoorCloser(plugin, uuid, id).closeDoors();
                                    case OPEN -> new TardisDoorOpener(plugin, uuid, id).openDoors();
                                    case LOCK, UNLOCK -> {
                                        HashMap<String, Object> whered = new HashMap<>();
                                        whered.put("tardis_id", id);
                                        whered.put("door_type", 0);
                                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                                        if (rsd.resultSet()) {
                                            if ((next.equals(TardisHandlesBlock.LOCK) && !rsd.isLocked()) || (next.equals(TardisHandlesBlock.UNLOCK) && rsd.isLocked())) {
                                                String message = (rsd.isLocked()) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                                                int locked = (rsd.isLocked()) ? 0 : 1;
                                                HashMap<String, Object> setl = new HashMap<>();
                                                setl.put("locked", locked);
                                                HashMap<String, Object> wherel = new HashMap<>();
                                                wherel.put("tardis_id", id);
                                                // always lock / unlock both doors
                                                plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                                                TardisMessage.handlesSend(player, "DOOR_LOCK", message);
                                            }
                                        }
                                    }
                                }
                                break;
                            case LIGHTS:
                                boolean onoff = next.equals(TardisHandlesBlock.ON);
                                if ((onoff && !tardis.isLightsOn()) || (!onoff && tardis.isLightsOn())) {
                                    new TardisLampToggler(plugin).flickSwitch(id, uuid, onoff, tardis.getSchematic().hasLanterns());
                                }
                                break;
                            case POWER:
                                switch (next) {
                                    case ON:
                                        if (!tardis.isPowered()) {
                                            if (plugin.getConfig().getBoolean("allow.power_down")) {
                                                new TardisPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().hasLanterns()).clickButton();
                                            }
                                        }
                                        break;
                                    case OFF:
                                        if (tardis.isPowered()) {
                                            if (plugin.getConfig().getBoolean("allow.power_down")) {
                                                new TardisPowerButton(plugin, id, player, tardis.getPreset(), true, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().hasLanterns()).clickButton();
                                            }
                                        }
                                        break;
                                    case SHOW:
                                        new TardisArtronIndicator(plugin).showArtronLevel(player, id, 0);
                                        break;
                                    case REDSTONE:
                                        // press the Handles button
                                        HashMap<String, Object> whereh = new HashMap<>();
                                        whereh.put("tardis_id", id);
                                        whereh.put("type", 26);
                                        ResultSetControls rsh = new ResultSetControls(plugin, whereh, false);
                                        if (rsh.resultSet()) {
                                            Location handles = TardisStaticLocationGetters.getLocationFromBukkitString(rsh.getLocation());
                                            assert handles != null;
                                            Block block = handles.getBlock();
                                            Powerable button = (Powerable) block.getBlockData();
                                            if (!button.isPowered()) {
                                                button.setPowered(true);
                                            }
                                            block.setBlockData(button, true);
                                        }
                                        break;
                                }
                                break;
                            case SIEGE:
                                if ((next.equals(TardisHandlesBlock.ON) && !tardis.isSiegeOn()) || (next.equals(TardisHandlesBlock.OFF) && tardis.isSiegeOn())) {
                                    new TardisSiegeMode(plugin).toggleViaSwitch(id, player);
                                }
                                break;
                            case TRAVEL:
                                // get tardis artron level
                                ResultSetTardisArtron rsArtron = new ResultSetTardisArtron(plugin);
                                if (!rsArtron.fromID(id)) {
                                    continue;
                                }
                                int level = rsArtron.getArtronLevel();
                                int travel = plugin.getArtronConfig().getInt("travel");
                                if (level < travel) {
                                    TardisMessage.handlesSend(player, "NOT_ENOUGH_ENERGY");
                                    continue;
                                }
                                ItemStack after = program.getInventory()[i + 1];
                                List<String> lore = Objects.requireNonNull(after.getItemMeta()).getLore();
                                if (lore != null) {
                                    String first = lore.get(0);
                                    // get current location
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (rsc.resultSet()) {
                                        Location goto_loc = null;
                                        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                        CardinalDirection direction = rsc.getDirection();
                                        CardinalDirection nextDirection = rsc.getDirection();
                                        int x = rsc.getX();
                                        int y = rsc.getY();
                                        int z = rsc.getZ();
                                        boolean sub = false;
                                        switch (next) {
                                            case RANDOM:
                                                Location random = new TardisRandomiserCircuit(plugin).getRandomlocation(player, direction);
                                                if (random != null) {
                                                    plugin.getTrackerKeeper().getHasRandomised().add(id);
                                                    TardisMessage.handlesSend(player, "RANDOMISER");
                                                    goto_loc = random;
                                                    sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                                                }
                                                break;
                                            case X:
                                                // if X comes after travel then we'll look for Y and Z
                                                ItemStack coordX = program.getInventory()[i + 2];
                                                TardisHandlesBlock coordBlockX = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordX.getItemMeta()).getDisplayName());
                                                x = getNumber(coordBlockX, i + 2);
                                                // find Y
                                                int fy = find(TardisHandlesBlock.Y, i + 3);
                                                if (fy > 0) {
                                                    ItemStack coordY = program.getInventory()[fy];
                                                    TardisHandlesBlock coordBlockY = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordY.getItemMeta()).getDisplayName());
                                                    y = getNumber(coordBlockY, fy);
                                                }
                                                // find Z
                                                int fz = find(TardisHandlesBlock.Z, i + 3);
                                                if (fz > 0) {
                                                    ItemStack coordZ = program.getInventory()[fz];
                                                    TardisHandlesBlock coordBlockZ = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordZ.getItemMeta()).getDisplayName());
                                                    z = getNumber(coordBlockZ, fz);
                                                }
                                                goto_loc = new Location(rsc.getWorld(), x, y, z);
                                                break;
                                            case Y:
                                                // if Y comes after travel then X use current coords, and we'll look for Z
                                                ItemStack coordY = program.getInventory()[i + 2];
                                                TardisHandlesBlock coordBlockY = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordY.getItemMeta()).getDisplayName());
                                                y = getNumber(coordBlockY, i + 2);
                                                // find Z
                                                int fyz = find(TardisHandlesBlock.Z, i + 3);
                                                if (fyz > 0) {
                                                    ItemStack coordZ = program.getInventory()[fyz];
                                                    TardisHandlesBlock coordBlockZ = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordZ.getItemMeta()).getDisplayName());
                                                    z = getNumber(coordBlockZ, fyz);
                                                }
                                                goto_loc = new Location(rsc.getWorld(), x, y, z);
                                                break;
                                            case Z:
                                                // if Z comes after travel then X and Y will use current coords
                                                ItemStack coordZ = program.getInventory()[i + 2];
                                                TardisHandlesBlock coordBlockZ = TardisHandlesBlock.valueOf(Objects.requireNonNull(coordZ.getItemMeta()).getDisplayName());
                                                z = getNumber(coordBlockZ, i + 2);
                                                goto_loc = new Location(rsc.getWorld(), x, y, z);
                                                break;
                                            case HOME:
                                                // get home location
                                                HashMap<String, Object> wherehl = new HashMap<>();
                                                wherehl.put("tardis_id", id);
                                                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                                if (!rsh.resultSet()) {
                                                    TardisMessage.handlesSend(player, "HOME_NOT_FOUND");
                                                    continue;
                                                }
                                                TardisMessage.handlesSend(player, "TRAVEL_LOADED", "Home");
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
                                                break;
                                            case RECHARGER:
                                                Location recharger = getRecharger(rsc.getWorld(), player);
                                                if (recharger != null) {
                                                    TardisMessage.handlesSend(player, "RECHARGER_FOUND");
                                                    goto_loc = recharger;
                                                } else {
                                                    TardisMessage.handlesSend(player, "NO_MORE_SPOTS");
                                                    continue;
                                                }
                                                break;
                                            case AREA_DISK:
                                                // check the current location is not in this area already
                                                if (!plugin.getTardisArea().areaCheckInExile(first, current)) {
                                                    continue;
                                                }
                                                // get a parking spot in this area
                                                HashMap<String, Object> wherea = new HashMap<>();
                                                wherea.put("area_name", first);
                                                ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                                                if (!rsa.resultSet()) {
                                                    TardisMessage.handlesSend(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                                    continue;
                                                }
                                                if ((!TardisPermission.hasPermission(player, "tardis.area." + first) && !TardisPermission.hasPermission(player, "tardis.area.*")) || (!player.isPermissionSet("tardis.area." + first) && !player.isPermissionSet("tardis.area.*"))) {
                                                    TardisMessage.handlesSend(player, "TRAVEL_NO_AREA_PERM", first);
                                                    continue;
                                                }
                                                Location l = plugin.getTardisArea().getNextSpot(rsa.getArea().getAreaName());
                                                if (l == null) {
                                                    TardisMessage.handlesSend(player, "NO_MORE_SPOTS");
                                                    continue;
                                                }
                                                TardisMessage.handlesSend(player, "TRAVEL_APPROVED", first);
                                                goto_loc = l;
                                                break;
                                            case BIOME_DISK:
                                                // find a biome location
                                                if (!TardisPermission.hasPermission(player, "tardis.timetravel.biome")) {
                                                    TardisMessage.handlesSend(player, "TRAVEL_NO_PERM_BIOME");
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
                                                        TardisMessage.handlesSend(player, "BIOME_NOT_VALID");
                                                        continue;
                                                    }
                                                }
                                                TardisMessage.handlesSend(player, "BIOME_SEARCH");
                                                Location nsob = plugin.getGeneralKeeper().getTardisTravelCommand().searchBiome(player, id, biome, rsc.getWorld(), rsc.getX(), rsc.getZ());
                                                if (nsob == null) {
                                                    TardisMessage.handlesSend(player, "BIOME_NOT_FOUND");
                                                    continue;
                                                } else {
                                                    if (!plugin.getPluginRespect().getRespect(nsob, new Parameters(player, Flag.getDefaultFlags()))) {
                                                        continue;
                                                    }
                                                    World bw = nsob.getWorld();
                                                    // check location
                                                    while (true) {
                                                        assert bw != null;
                                                        if (bw.getChunkAt(nsob).isLoaded())
                                                            break;
                                                        bw.getChunkAt(nsob).load();
                                                    }
                                                    int[] start_loc = TardisTimeTravel.getStartLocation(nsob, direction);
                                                    int tmp_y = nsob.getBlockY();
                                                    for (int up = 0; up < 10; up++) {
                                                        int count = TardisTimeTravel.safeLocation(start_loc[0], tmp_y + up, start_loc[2], start_loc[1], start_loc[3], nsob.getWorld(), direction);
                                                        if (count == 0) {
                                                            nsob.setY(tmp_y + up);
                                                            break;
                                                        }
                                                    }
                                                    TardisMessage.handlesSend(player, "BIOME_SET");
                                                    goto_loc = nsob;
                                                }
                                                break;
                                            case PLAYER_DISK:
                                                // get the player's location
                                                if (TardisPermission.hasPermission(player, "tardis.timetravel.player")) {
                                                    if (player.getName().equalsIgnoreCase(first)) {
                                                        TardisMessage.handlesSend(player, "TRAVEL_NO_SELF");
                                                        continue;
                                                    }
                                                    // get the player
                                                    Player to = plugin.getServer().getPlayer(first);
                                                    if (to == null) {
                                                        TardisMessage.handlesSend(player, "NOT_ONLINE");
                                                        continue;
                                                    }
                                                    UUID toUUID = to.getUniqueId();
                                                    // check the to player's DND status
                                                    ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, toUUID.toString());
                                                    if (rspp.resultSet() && rspp.isDndOn()) {
                                                        TardisMessage.handlesSend(player, "DND", first);
                                                        continue;
                                                    }
                                                    Location player_loc = to.getLocation();
                                                    if (!plugin.getTardisArea().areaCheckInExisting(player_loc)) {
                                                        TardisMessage.handlesSend(player, "PLAYER_IN_AREA", ChatColor.AQUA + "/tardistravel area [area name]");
                                                        continue;
                                                    }
                                                    if (!plugin.getPluginRespect().getRespect(player_loc, new Parameters(player, Flag.getDefaultFlags()))) {
                                                        continue;
                                                    }
                                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + Objects.requireNonNull(player_loc.getWorld()).getName() + ".time_travel")) {
                                                        TardisMessage.handlesSend(player, "NO_WORLD_TRAVEL");
                                                        continue;
                                                    }
                                                    World w = player_loc.getWorld();
                                                    int[] start_loc = TardisTimeTravel.getStartLocation(player_loc, direction);
                                                    int count = TardisTimeTravel.safeLocation(start_loc[0] - 3, player_loc.getBlockY(), start_loc[2], start_loc[1] - 3, start_loc[3], w, direction);
                                                    if (count > 0) {
                                                        TardisMessage.handlesSend(player, "RESCUE_NOT_SAFE");
                                                        continue;
                                                    }
                                                    goto_loc = player_loc;
                                                } else {
                                                    TardisMessage.handlesSend(player, "NO_PERM_PLAYER");
                                                    continue;
                                                }
                                                break;
                                            case SAVE_DISK:
                                                if (TardisPermission.hasPermission(player, "tardis.save")) {
                                                    int sx = TardisNumberParsers.parseInt(lore.get(2));
                                                    int sy = TardisNumberParsers.parseInt(lore.get(3));
                                                    int sz = TardisNumberParsers.parseInt(lore.get(4));
                                                    if (Objects.requireNonNull(current.getWorld()).getName().equals(lore.get(1)) && current.getBlockX() == sx && current.getBlockZ() == sz) {
                                                        continue;
                                                    }
                                                    TardisMessage.handlesSend(player, "LOC_SET");
                                                    goto_loc = new Location(TardisAliasResolver.getWorldFromAlias(lore.get(1)), sx, sy, sz);
                                                    nextDirection = CardinalDirection.valueOf(lore.get(6));
                                                    sub = Boolean.parseBoolean(lore.get(7));
                                                } else {
                                                    TardisMessage.handlesSend(player, "TRAVEL_NO_PERM_SAVE");
                                                    continue;
                                                }
                                                break;
                                        }
                                        if (goto_loc != null) {
                                            plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                                            plugin.getTrackerKeeper().getRescue().remove(id);
                                            if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.memory") > 0 && !plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                                plugin.getTrackerKeeper().getHasNotClickedHandbrake().add(id);
                                                TardisCircuitChecker tcc = new TardisCircuitChecker(plugin, id);
                                                tcc.getCircuits();
                                                // decrement uses
                                                int uses_left = tcc.getMemoryUses();
                                                new TardisCircuitDamager(plugin, DiskCircuit.MEMORY, uses_left, id, player).damage();
                                            }
                                            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                                // destroy police box
                                                DestroyData dd = new DestroyData();
                                                dd.setDirection(direction);
                                                dd.setLocation(current);
                                                dd.setPlayer(player);
                                                dd.setHide(false);
                                                dd.setOutside(false);
                                                dd.setSubmarine(rsc.isSubmarine());
                                                dd.setTardisId(id);
                                                dd.setThrottle(SpaceTimeThrottle.NORMAL);
                                                // set handbrake off
                                                HashMap<String, Object> set = new HashMap<>();
                                                set.put("handbrake_on", 0);
                                                HashMap<String, Object> tid = new HashMap<>();
                                                tid.put("tardis_id", id);
                                                if (!tardis.isHidden()) {
                                                    plugin.getPresetDestroyer().destroyPreset(dd);
                                                    plugin.getTrackerKeeper().getDematerialising().add(dd.getTardisId());
                                                    plugin.getTrackerKeeper().getInVortex().add(id);
                                                    // play tardis_takeoff sfx
                                                    TardisSounds.playTardisSound(current, "tardis_takeoff");
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
                                            bd.setTardisId(id);
                                            bd.setThrottle(SpaceTimeThrottle.NORMAL);
                                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                                // rebuild police box - needs to be a delay
                                                plugin.getPresetBuilder().buildPreset(bd);
                                                plugin.getTrackerKeeper().getInVortex().add(id);
                                                // play tardis_land sfx
                                                TardisSounds.playTardisSound(bd.getLocation(), "tardis_land");
                                                // set handbrake on
                                                HashMap<String, Object> seth = new HashMap<>();
                                                seth.put("handbrake_on", 1);
                                                HashMap<String, Object> wheret = new HashMap<>();
                                                wheret.put("tardis_id", id);
                                                plugin.getQueryFactory().doUpdate("tardis", seth, wheret);
                                            }, 500L);
                                            // set current
                                            HashMap<String, Object> setc = new HashMap<>();
                                            setc.put("world", Objects.requireNonNull(goto_loc.getWorld()).getName());
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
                                            setb.put("world", rsc.getWorld().getName());
                                            setb.put("x", rsc.getX());
                                            setb.put("y", rsc.getY());
                                            setb.put("z", rsc.getZ());
                                            setb.put("direction", direction.toString());
                                            setb.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
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
                            case HIDE:
                                player.performCommand("tardis hide");
                                break;
                            case REBUILD:
                                player.performCommand("tardis rebuild");
                                break;
                            case SCAN:
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles scan " + uuid + " " + id);
                                break;
                            case COMEHERE:
                                new TardisHandlesTeleportCommand(plugin).beamMeUp(player);
                                break;
                            case TAKE_OFF:
                                // player must be in TARDIS
                                if (plugin.getUtils().inTardisWorld(player.getLocation())) {
                                    plugin.getServer().dispatchCommand(plugin.getConsole(), "handles takeoff " + uuid + " " + id);
                                }
                                break;
                            case LAND:
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "handles land " + uuid + " " + id);
                                break;
                        }
                    }
                }
            }
        }
    }

    void processArtronCommand(int pos) {
        boolean first = true;
        boolean process;
        TardisHandlesBlock comparison = null;
        int level;
        for (int i = pos; i < 36; i++) {
            ItemStack is = program.getInventory()[i];
            if (is != null) {
                TardisHandlesBlock thb = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(is.getItemMeta()).getDisplayName());
                switch (thb) {
                    case LESS_THAN:
                    case LESS_THAN_EQUAL:
                    case GREATER_THAN:
                    case GREATER_THAN_EQUAL:
                    case EQUALS:
                        // operator
                        comparison = thb;
                    case ONE:
                    case TWO:
                    case THREE:
                    case FOUR:
                    case FIVE:
                    case SIX:
                    case SEVEN:
                    case EIGHT:
                    case NINE:
                    case ZERO:
                        // find all sequential number blocks
                        if (first) {
                            level = getNumber(thb, i);
                            // get the current Artron level
                            ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                            if (rs.fromUUID(player.getUniqueId().toString())) {
                                assert comparison != null;
                                process = switch (comparison) {
                                    case LESS_THAN -> (level < rs.getArtronLevel());
                                    case LESS_THAN_EQUAL -> (level <= rs.getArtronLevel());
                                    case GREATER_THAN -> (level > rs.getArtronLevel());
                                    case GREATER_THAN_EQUAL -> (level >= rs.getArtronLevel());
                                    default -> // EQUALS
                                            (level == rs.getArtronLevel());
                                };
                                if (!process) {
                                    return;
                                }
                            }
                            first = false;
                        }
                    case DO:
                        processCommand(i);
                }
            }
        }
    }

    private TardisHandlesBlock getNext(int i) {
        if (i > 35) {
            return null;
        }
        ItemStack num = program.getInventory()[i];
        if (num != null) {
            return TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(num.getItemMeta()).getDisplayName());
        }
        return null;
    }

    private Location getRecharger(World world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("world", world.getName());
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
        if (rsa.resultSet()) {
            String area = rsa.getArea().getAreaName();
            if (!TardisPermission.hasPermission(player, "tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            l = plugin.getTardisArea().getNextSpot(area);
        }
        return l;
    }

    private int getNumber(TardisHandlesBlock thb, int i) {
        String tmp = thb.getDisplayName();
        int n = 1;
        ItemStack num = program.getInventory()[i + n];
        while (num != null) {
            TardisHandlesBlock numBlock = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(num.getItemMeta()).getDisplayName());
            if (numBlock.getCategory().equals(TardisHandlesCategory.NUMBER)) {
                tmp += numBlock.getDisplayName();
            } else {
                break;
            }
            n++;
            num = program.getInventory()[i + n];
        }
        return TardisNumberParsers.parseInt(tmp);
    }

    private int find(TardisHandlesBlock thb, int i) {
        if (i > 35) {
            return -1;
        }
        for (int n = i; n < 34; n++) {
            ItemStack yOrZ = program.getInventory()[n];
            if (yOrZ != null) {
                TardisHandlesBlock block = TardisHandlesBlock.BY_NAME.get(Objects.requireNonNull(yOrZ.getItemMeta()).getDisplayName());
                if (block == thb) {
                    return n + 1;
                }
            }
        }
        return -1;
    }
}
