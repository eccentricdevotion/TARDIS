/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISParticles;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISDematerialisationPreset implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final PRESET preset;
    private int task;
    private int i;
    private final BlockData cham_id;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private BlockData the_colour;
    private BlockData stain_colour;

    /**
     * Runnable method to dematerialise the TARDIS Police Box. Tries to mimic the transparency of dematerialisation by
     * building the Police Box first with GLASS, then STAINED_GLASS, then the normal preset wall block.
     *
     * @param plugin  instance of the TARDIS plugin
     * @param dd      the DestroyData
     * @param preset  the Chameleon preset currently in use by the TARDIS
     * @param cham_id the chameleon block id for the police box
     * @param loops   the number of loops to run
     */
    TARDISDematerialisationPreset(TARDIS plugin, DestroyData dd, PRESET preset, BlockData cham_id, int loops) {
        this.plugin = plugin;
        this.dd = dd;
        this.loops = loops;
        this.preset = preset;
        i = 0;
        this.cham_id = cham_id;
        if (this.preset.equals(PRESET.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, dd.getTardisID(), "blueprintData", dd.getDirection()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "stainData", dd.getDirection()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "glassData", dd.getDirection()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, dd.getDirection());
            stained_column = plugin.getPresets().getStained(preset, dd.getDirection());
            glass_column = plugin.getPresets().getGlass(preset, dd.getDirection());
        }
    }

    @Override
    public void run() {
        BlockData[][] datas;
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
                    datas = stained_column.getBlockData();
                    break;
                case 1: // glass
                    datas = glass_column.getBlockData();
                    break;
                default: // preset
                    datas = column.getBlockData();
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
                        TARDISBlockSetters.setBlock(world, flowerx, flowery, flowerz, Material.AIR);
                        break;
                    case CAKE:
                        plugin.getPresetDestroyer().destroyLamp(dd.getLocation(), preset);
                        break;
                    case JUNK_MODE:
                        plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection(), preset);
                        plugin.getPresetDestroyer().destroyHandbrake(dd.getLocation(), dd.getDirection());
                        break;
                    case SWAMP:
                        plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection(), preset);
                        break;
                    case JAIL:
                    case TOPSYTURVEY:
                        // destroy door
                        plugin.getPresetDestroyer().destroyDoor(dd.getTardisID());
                    default:
                        break;
                }
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    HashMap<String, Object> wherep = new HashMap<>();
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
                getColours(dd.getTardisID(), preset);
            } else if (preset.equals(PRESET.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                // animate particles
                plugin.getUtils().getJunkTravellers(dd.getLocation()).forEach((e) -> {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        Location effectsLoc = dd.getLocation().clone().add(0.5d, 0, 0.5d);
                        TARDISParticles.sendVortexParticles(effectsLoc, p);
                    }
                });
            } else {
                // just change the walls
                int xx, zz;
                for (int n = 0; n < 9; n++) {
                    BlockData[] coldatas = datas[n];
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
                            if (rail.getType().equals(Material.RAIL) || rail.getType().equals(Material.POWERED_RAIL)) {
                                change = false;
                            }
                        }
                        Material mat = coldatas[yy].getMaterial();
                        switch (mat) {
                            case GRASS_BLOCK:
                            case DIRT:
                                BlockData subi = (preset.equals(PRESET.SUBMERGED)) ? cham_id : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
                                break;
                            case WHITE_WOOL:
                                BlockData chad = (preset.equals(PRESET.FLOWER)) ? the_colour : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chad);
                                break;
                            case LIME_WOOL:
                                BlockData chaw = (preset.equals(PRESET.FLOWER)) ? the_colour : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
                                break;
                            case ALLIUM:
                            case AZURE_BLUET:
                            case BLUE_ORCHID:
                            case DEAD_BUSH:
                            case FERN:
                            case LARGE_FERN:
                            case LILAC:
                            case ORANGE_TULIP:
                            case OXEYE_DAISY:
                            case PEONY:
                            case PINK_TULIP:
                            case POPPY:
                            case RED_TULIP:
                            case ROSE_BUSH:
                            case SUNFLOWER:
                            case TALL_GRASS:
                            case WHITE_TULIP:
                                break;
                            case TORCH: // lamps, glowstone and torches
                            case GLOWSTONE:
                            case REDSTONE_LAMP:
                                BlockData light = (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) ? dd.getLamp().createBlockData() : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, light);
                                break;
                            case ACACIA_DOOR: // wood, iron & trap doors
                            case BIRCH_DOOR:
                            case DARK_OAK_DOOR:
                            case IRON_DOOR:
                            case JUNGLE_DOOR:
                            case OAK_DOOR:
                            case SPRUCE_DOOR:
                            case ACACIA_TRAPDOOR:
                            case BIRCH_TRAPDOOR:
                            case DARK_OAK_TRAPDOOR:
                            case JUNGLE_TRAPDOOR:
                            case OAK_TRAPDOOR:
                            case SPRUCE_TRAPDOOR:
                            case ACACIA_WALL_SIGN:
                            case BIRCH_WALL_SIGN:
                            case DARK_OAK_WALL_SIGN:
                            case JUNGLE_WALL_SIGN:
                            case OAK_WALL_SIGN:
                            case SPRUCE_WALL_SIGN:
                                if (preset.equals(PRESET.SWAMP) || preset.equals(PRESET.TOPSYTURVEY) || preset.equals(PRESET.JAIL)) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
                                }
                                break;
                            case WHITE_STAINED_GLASS:
                                BlockData chaf = (preset.equals(PRESET.FLOWER)) ? stain_colour : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaf);
                                break;
                            case LIME_STAINED_GLASS:
                                BlockData chap = (preset.equals(PRESET.PARTY)) ? stain_colour : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chap);
                                break;
                            case LIGHT_GRAY_STAINED_GLASS:
                                BlockData cham = (preset.equals(PRESET.FACTORY)) ? plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(cham_id.getMaterial()).createBlockData() : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, cham);
                                break;
                            case LIGHT_GRAY_TERRACOTTA:
                                BlockData chai = (preset.equals(PRESET.FACTORY)) ? cham_id : coldatas[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
                                break;
                            default: // everything else
                                if (change) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, coldatas[yy]);
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
                getJunkTravellers().forEach((e) -> {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        Location relativeLoc = getRelativeLocation(p);
                        p.teleport(relativeLoc);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.teleport(relativeLoc), 2L);
                    }
                });
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

    private List<Entity> getJunkTravellers() {
        // spawn an entity
        Entity orb = dd.getLocation().getWorld().spawnEntity(dd.getLocation(), EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(1.0, 1.0, 1.0);
        orb.remove();
        return ents;
    }

    private void getColours(int id, PRESET p) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            try {
                Block b = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock();
                if (p.equals(PRESET.FLOWER)) {
                    the_colour = b.getRelative(BlockFace.UP, 3).getBlockData();
                    String[] split = the_colour.getMaterial().toString().toLowerCase().split("_");
                    String colour = (split.length > 2) ? split[0] + "_" + split[1] : split[0];
                    stain_colour = plugin.getServer().createBlockData("minecraft:" + colour + "_stained_glass");
                    return;
                } else {
                    for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
                        if (Tag.WOOL.isTagged(b.getRelative(f).getType())) {
                            the_colour = b.getRelative(f).getBlockData();
                            String[] split = the_colour.getMaterial().toString().toLowerCase().split("_");
                            String colour = (split.length > 2) ? split[0] + "_" + split[1] : split[0];
                            stain_colour = plugin.getServer().createBlockData("minecraft:" + colour + "_stained_glass");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                the_colour = Material.BLUE_WOOL.createBlockData();
                stain_colour = Material.BLUE_STAINED_GLASS.createBlockData();
                return;
            }
        }
        the_colour = Material.BLUE_WOOL.createBlockData();
        stain_colour = Material.BLUE_STAINED_GLASS.createBlockData();
    }

    public void setTask(int task) {
        this.task = task;
    }
}
