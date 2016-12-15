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
package me.eccentric_nz.TARDIS.destroyers;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISJunkParticles;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISDematerialisationPreset implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final PRESET preset;
    public int task;
    private int i;
    private final int cham_id;
    private final byte cham_data;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private byte the_colour;

    /**
     * Runnable method to dematerialise the TARDIS Police Box. Tries to mimic
     * the transparency of dematerialisation by building the Police Box first
     * with GLASS, then STAINED_GLASS, then the normal preset wall block.
     *
     * @param plugin instance of the TARDIS plugin
     * @param dd the DestroyData
     * @param preset the Chameleon preset currently in use by the TARDIS
     * @param cham_id the chameleon block id for the police box
     * @param cham_data the chameleon block data for the police box
     * @param loops the number of loops to run
     */
    public TARDISDematerialisationPreset(TARDIS plugin, DestroyData dd, PRESET preset, int cham_id, byte cham_data, int loops) {
        this.plugin = plugin;
        this.dd = dd;
        this.loops = loops;
        this.preset = preset;
        this.i = 0;
        this.cham_id = cham_id;
        this.cham_data = cham_data;
        if (this.preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, dd.getTardisID(), "blueprint", dd.getDirection()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "stain", dd.getDirection()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "glass", dd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, dd.getDirection());
            stained_column = plugin.getPresets().getStained(preset, dd.getDirection());
            glass_column = plugin.getPresets().getGlass(preset, dd.getDirection());
        }
    }

    @Override
    public void run() {
        int[][] ids;
        byte[][] datas;
        // get relative locations
        int x = dd.getLocation().getBlockX(), plusx = dd.getLocation().getBlockX() + 1, minusx = dd.getLocation().getBlockX() - 1;
        int y;
        if (preset.equals(PRESET.SUBMERGED)) {
            y = dd.getLocation().getBlockY() - 1;
        } else {
            y = dd.getLocation().getBlockY();
        }
        int z = dd.getLocation().getBlockZ(), plusz = dd.getLocation().getBlockZ() + 1, minusz = dd.getLocation().getBlockZ() - 1;
        World world = dd.getLocation().getWorld();
        if (i < loops) {
            i++;
            // expand placed blocks to a police box
            switch (i % 3) {
                case 2: // stained
                    ids = stained_column.getId();
                    datas = stained_column.getData();
                    break;
                case 1: // glass
                    ids = glass_column.getId();
                    datas = glass_column.getData();
                    break;
                default: // preset
                    ids = column.getId();
                    datas = column.getData();
                    break;
            }
            // first run - play sound
            if (i == 1) {
                switch (preset) {
                    case GRAVESTONE:
                        // remove flower
                        int flowerx;
                        int flowery = (dd.getLocation().getBlockY() + 1);
                        int flowerz;
                        switch (dd.getDirection()) {
                            case NORTH:
                                flowerx = dd.getLocation().getBlockX();
                                flowerz = dd.getLocation().getBlockZ() + 1;
                                break;
                            case WEST:
                                flowerx = dd.getLocation().getBlockX() + 1;
                                flowerz = dd.getLocation().getBlockZ();
                                break;
                            case SOUTH:
                                flowerx = dd.getLocation().getBlockX();
                                flowerz = dd.getLocation().getBlockZ() - 1;
                                break;
                            default:
                                flowerx = dd.getLocation().getBlockX() - 1;
                                flowerz = dd.getLocation().getBlockZ();
                                break;
                        }
                        TARDISBlockSetters.setBlock(world, flowerx, flowery, flowerz, 0, (byte) 0);
                        break;
                    case CAKE:
                        plugin.getPresetDestroyer().destroyLamp(dd.getLocation(), preset);
                        break;
                    case JUNK_MODE:
                        plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection(), preset);
                        plugin.getPresetDestroyer().destroyHandbrake(dd.getLocation(), dd.getDirection());
                        break;
                    default:
                        break;
                }
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("uuid", dd.getPlayer().getUniqueId().toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                    boolean minecart = false;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                    }
                    if (!minecart) {
                        String sound = (preset.equals(PRESET.JUNK_MODE)) ? "junk_takeoff" : "tardis_takeoff";
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
                the_colour = getWoolColour(dd.getTardisID(), preset);
            } else if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                // animate particles
                for (Entity e : plugin.getUtils().getJunkTravellers(dd.getLocation())) {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        Location effectsLoc = dd.getLocation().clone().add(0.5d, 0, 0.5d);
                        TARDISJunkParticles.sendVortexParticles(effectsLoc, p);
                    }
                }
            } else {
                // just change the walls
                int xx, zz;
                for (int n = 0; n < 9; n++) {
                    int[] colids = ids[n];
                    byte[] coldatas = datas[n];
                    switch (n) {
                        case 0:
                            xx = minusx;
                            zz = minusz;
                            break;
                        case 1:
                            xx = x;
                            zz = minusz;
                            break;
                        case 2:
                            xx = plusx;
                            zz = minusz;
                            break;
                        case 3:
                            xx = plusx;
                            zz = z;
                            break;
                        case 4:
                            xx = plusx;
                            zz = plusz;
                            break;
                        case 5:
                            xx = x;
                            zz = plusz;
                            break;
                        case 6:
                            xx = minusx;
                            zz = plusz;
                            break;
                        case 7:
                            xx = minusx;
                            zz = z;
                            break;
                        default:
                            xx = x;
                            zz = z;
                            break;
                    }
                    for (int yy = 0; yy < 4; yy++) {
                        boolean change = true;
                        if (yy == 0 && n == 9) {
                            Block rail = world.getBlockAt(xx, y, zz);
                            if (rail.getType().equals(Material.RAILS) || rail.getType().equals(Material.POWERED_RAIL)) {
                                change = false;
                            }
                        }
                        switch (colids[yy]) {
                            case 2:
                            case 3:
                                int subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : colids[yy];
                                byte subd = (preset.equals(PRESET.SUBMERGED)) ? cham_data : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi, subd);
                                break;
                            case 35: // wool
                                byte chad = coldatas[yy];
                                if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                    chad = the_colour;
                                }
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], chad);
                                break;
                            case 31:
                            case 32:
                            case 37:
                            case 38:
                            case 175:
                                break;
                            case 50: // lamps, glowstone and torches
                            case 89:
                            case 124:
                                Material light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? dd.getLamp() : Material.getMaterial(colids[yy]);
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, light, coldatas[yy]);
                                break;
                            case 64:
                            case 68: // except the sign and doors
                            case 71:
                            case 193:
                            case 194:
                            case 195:
                            case 196:
                            case 197:
                                break;
                            case 95:
                                if (coldatas[yy] == -1) {
                                    if (preset.equals(PRESET.PARTY) || (preset.equals(PRESET.FLOWER) && coldatas[yy] == 0)) {
                                        chad = the_colour;
                                    } else {
                                        // if it was a wool / stained glass / stained clay block get the data from that
                                        int[] finalids = column.getId()[n];
                                        byte[] finaldatas = column.getData()[n];
                                        if (finalids[yy] == 35 || finalids[yy] == 95 || finalids[yy] == 159 || finalids[yy] == 160 || finalids[yy] == 171) {
                                            if (preset.equals(PRESET.FACTORY)) {
                                                chad = cham_data;
                                            } else {
                                                chad = finaldatas[yy];
                                            }
                                        } else {
                                            chad = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id);
                                        }
                                    }
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, 95, chad);
                                } else {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                            case 159:
                                int chai = (preset.equals(PRESET.FACTORY)) ? cham_id : colids[yy];
                                byte chaf = (preset.equals(PRESET.FACTORY)) ? cham_data : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai, chaf);
                                break;
                            default: // everything else
                                if (change) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                                }
                                break;
                        }
                    }
                }
            }
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            new TARDISDeinstaPreset(plugin).instaDestroyPreset(dd, false, preset);
            if (preset.equals(PRESET.JUNK_MODE)) {
                // teleport player(s) to exit (tmd.getFromToLocation())
                for (Entity e : getJunkTravellers(1.0d)) {
                    if (e instanceof Player) {
                        final Player p = (Player) e;
                        final Location relativeLoc = getRelativeLocation(p);
                        p.teleport(relativeLoc);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                p.teleport(relativeLoc);
                            }
                        }, 2L);
                    }
                }
            }
        }
    }

    private Location getRelativeLocation(Player p) {
        Location playerLoc = p.getLocation();
        double x = dd.getFromToLocation().getX() + (playerLoc.getX() - dd.getLocation().getX());
        double y = dd.getFromToLocation().getY() + (playerLoc.getY() - dd.getLocation().getY()) + 1.1d;
        double z = dd.getFromToLocation().getZ() + (playerLoc.getZ() - dd.getLocation().getZ());
        Location l = new Location(dd.getFromToLocation().getWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
        while (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        return l;
    }

    private List<Entity> getJunkTravellers(double d) {
        // spawn an entity
        Entity orb = dd.getLocation().getWorld().spawnEntity(dd.getLocation(), EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(d, d, d);
        orb.remove();
        return ents;
    }

    @SuppressWarnings("deprecation")
    private byte getWoolColour(int id, PRESET p) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block b = TARDISLocationGetters.getLocationFromDB(rs.getDoor_location(), 0.0F, 0.0F).getBlock();
            if (p.equals(PRESET.FLOWER)) {
                return b.getRelative(BlockFace.UP, 3).getData();
            } else {
                for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
                    if (b.getRelative(f).getType().equals(Material.WOOL)) {
                        return b.getRelative(f).getData();
                    }
                }
            }
        }
        return (byte) 0;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
