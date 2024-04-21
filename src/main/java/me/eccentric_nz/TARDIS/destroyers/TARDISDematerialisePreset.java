/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.chameleon.construct.TARDISConstructColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which enabled it to dematerialise from normal
 * space into the Time Vortex and rematerialise back from it.
 *
 * @author eccentric_nz
 */
class TARDISDematerialisePreset implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final ChameleonPreset preset;
    private final BlockData cham_id;
    private final TARDISChameleonColumn column;
    private final TARDISChameleonColumn stained_column;
    private final TARDISChameleonColumn glass_column;
    private int task;
    private int i;
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
     */
    TARDISDematerialisePreset(TARDIS plugin, DestroyData dd, ChameleonPreset preset, BlockData cham_id) {
        this.plugin = plugin;
        this.dd = dd;
        loops = dd.getThrottle().getLoops();
        this.preset = preset;
        i = 0;
        this.cham_id = cham_id;
        if (this.preset.equals(ChameleonPreset.CONSTRUCT)) {
            column = new TARDISConstructColumn(plugin, dd.getTardisID(), "blueprintData", dd.getDirection().forPreset()).getColumn();
            stained_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "stainData", dd.getDirection().forPreset()).getColumn();
            glass_column = new TARDISConstructColumn(plugin, dd.getTardisID(), "glassData", dd.getDirection().forPreset()).getColumn();
        } else {
            column = plugin.getPresets().getColumn(preset, dd.getDirection().forPreset());
            stained_column = plugin.getPresets().getStained(preset, dd.getDirection().forPreset());
            glass_column = plugin.getPresets().getGlass(preset, dd.getDirection().forPreset());
        }
    }

    @Override
    public void run() {
        if (column == null || stained_column == null || glass_column == null) {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            plugin.getMessenger().sendColouredCommand(dd.getPlayer().getPlayer(), "INVALID_CONSTRUCT", "/tardistravel stop", plugin);
            // remove trackers
            plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(dd.getTardisID()));
            plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(dd.getTardisID()));
            return;
        }
        BlockData[][] data;
        // get relative locations
        int x = dd.getLocation().getBlockX(), plusx = dd.getLocation().getBlockX() + 1, minusx = dd.getLocation().getBlockX() - 1;
        int y;
        if (preset.equals(ChameleonPreset.SUBMERGED)) {
            y = dd.getLocation().getBlockY() - 1;
        } else {
            y = dd.getLocation().getBlockY();
        }
        int z = dd.getLocation().getBlockZ(), plusz = dd.getLocation().getBlockZ() + 1, minusz = dd.getLocation().getBlockZ() - 1;
        World world = dd.getLocation().getWorld();
        if (i < loops) {
            i++;
            // expand placed blocks to a police box
            data = switch (i % 3) {
                case 2 -> stained_column.getBlockData(); // stained
                case 1 -> glass_column.getBlockData(); // glass
                default -> column.getBlockData(); // preset
            };
            // first run - play sound
            if (i == 1) {
                switch (preset) {
                    case GRAVESTONE -> {
                        // remove flower
                        int flowerx;
                        int flowery = (dd.getLocation().getBlockY() + 1);
                        int flowerz;
                        switch (dd.getDirection().forPreset()) {
                            case NORTH -> {
                                flowerx = dd.getLocation().getBlockX();
                                flowerz = dd.getLocation().getBlockZ() + 1;
                            }
                            case WEST -> {
                                flowerx = dd.getLocation().getBlockX() + 1;
                                flowerz = dd.getLocation().getBlockZ();
                            }
                            case SOUTH -> {
                                flowerx = dd.getLocation().getBlockX();
                                flowerz = dd.getLocation().getBlockZ() - 1;
                            }
                            default -> {
                                flowerx = dd.getLocation().getBlockX() - 1;
                                flowerz = dd.getLocation().getBlockZ();
                            }
                        }
                        TARDISBlockSetters.setBlock(world, flowerx, flowery, flowerz, Material.AIR);
                    }
                    case CAKE -> plugin.getPresetDestroyer().destroyLamp(dd.getLocation(), preset);
                    case JUNK_MODE -> {
                        plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection().forPreset(), preset);
                        plugin.getPresetDestroyer().destroyHandbrake(dd.getLocation(), dd.getDirection().forPreset());
                    }
                    case SWAMP -> plugin.getPresetDestroyer().destroySign(dd.getLocation(), dd.getDirection().forPreset(), preset);
                    case JAIL, TOPSYTURVEY -> plugin.getPresetDestroyer().destroyDoor(dd.getTardisID());
                    case MESA -> {
                        // destroy door
                        plugin.getPresetDestroyer().destroyDoor(dd.getTardisID());
                        // remove dead bushes
                        int deadx;
                        int bushx;
                        int bushy = (dd.getLocation().getBlockY() + 3);
                        int deadz;
                        int bushz;
                        switch (dd.getDirection().forPreset()) {
                            case NORTH -> {
                                deadx = dd.getLocation().getBlockX() + 1;
                                deadz = dd.getLocation().getBlockZ() + 1;
                                bushx = dd.getLocation().getBlockX() - 1;
                                bushz = dd.getLocation().getBlockZ();
                            }
                            case WEST -> {
                                deadx = dd.getLocation().getBlockX() + 1;
                                deadz = dd.getLocation().getBlockZ() - 1;
                                bushx = dd.getLocation().getBlockX();
                                bushz = dd.getLocation().getBlockZ() + 1;
                            }
                            case SOUTH -> {
                                deadx = dd.getLocation().getBlockX() - 1;
                                deadz = dd.getLocation().getBlockZ() - 1;
                                bushx = dd.getLocation().getBlockX() + 1;
                                bushz = dd.getLocation().getBlockZ();
                            }
                            default -> {
                                deadx = dd.getLocation().getBlockX() - 1;
                                deadz = dd.getLocation().getBlockZ() + 1;
                                bushx = dd.getLocation().getBlockX();
                                bushz = dd.getLocation().getBlockZ() - 1;
                            }
                        }
                        TARDISBlockSetters.setBlock(world, deadx, bushy, deadz, Material.AIR);
                        TARDISBlockSetters.setBlock(world, bushx, bushy, bushz, Material.AIR);
                    }
                    case PUNKED -> plugin.getPresetDestroyer().destroyPistons(dd.getLocation());
                    default -> { }
                }
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, dd.getPlayer().getUniqueId().toString());
                    boolean minecart = false;
                    SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                    }
                    if (!minecart) {
                        String sound;
                        if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                            sound = "junk_takeoff";
                        } else {
                            sound = switch (spaceTimeThrottle) {
                                case WARP, RAPID, FASTER -> "tardis_takeoff_" + spaceTimeThrottle.toString().toLowerCase();
                                default -> "tardis_takeoff"; // NORMAL
                            };
                        }
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
                getColours(dd.getTardisID(), preset);
            } else if (preset.equals(ChameleonPreset.JUNK_MODE) && plugin.getConfig().getBoolean("junk.particles")) {
                // animate particles
                plugin.getUtils().getJunkTravellers(dd.getLocation()).forEach((e) -> {
                    if (e instanceof Player p) {
                        Location effectsLoc = dd.getLocation().clone().add(0.5d, 0, 0.5d);
                        TARDISParticles.sendVortexParticles(effectsLoc, p);
                    }
                });
            } else {
                if (i % 3 == 1 && preset.equals(ChameleonPreset.PUNKED)) {
                    plugin.getPresetDestroyer().destroyPistons(dd.getLocation());
                }
                // just change the walls
                int xx, zz;
                for (int n = 0; n < 9; n++) {
                    BlockData[] colData = data[n];
                    switch (n) {
                        case 0 -> {
                            xx = minusx;
                            zz = minusz;
                        }
                        case 1 -> {
                            xx = x;
                            zz = minusz;
                        }
                        case 2 -> {
                            xx = plusx;
                            zz = minusz;
                        }
                        case 3 -> {
                            xx = plusx;
                            zz = z;
                        }
                        case 4 -> {
                            xx = plusx;
                            zz = plusz;
                        }
                        case 5 -> {
                            xx = x;
                            zz = plusz;
                        }
                        case 6 -> {
                            xx = minusx;
                            zz = plusz;
                        }
                        case 7 -> {
                            xx = minusx;
                            zz = z;
                        }
                        default -> {
                            xx = x;
                            zz = z;
                        }
                    }
                    for (int yy = 3; yy >= 0; yy--) {
                        Material mat = colData[yy].getMaterial();
                        switch (mat) {
                            case GRASS_BLOCK, DIRT -> {
                                BlockData subi = (preset.equals(ChameleonPreset.SUBMERGED)) ? cham_id : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, subi);
                            }
                            case WHITE_WOOL, LIME_WOOL -> {
                                BlockData chaw = (preset.equals(ChameleonPreset.FLOWER)) ? the_colour : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaw);
                            }
                            case ACACIA_SAPLING, ALLIUM, AZURE_BLUET, BAMBOO_SAPLING, BEETROOTS, BIRCH_SAPLING, BLUE_ORCHID, CARROTS, CORNFLOWER, CRIMSON_FUNGUS, CRIMSON_ROOTS, DANDELION, DARK_OAK_SAPLING, DEAD_BUSH, FERN, SHORT_GRASS, JUNGLE_SAPLING, LARGE_FERN, LILAC, LILY_OF_THE_VALLEY, OAK_SAPLING, ORANGE_TULIP, OXEYE_DAISY, PEONY, PINK_TULIP, POPPY, POTATOES, RED_TULIP, ROSE_BUSH, SPRUCE_SAPLING, SUGAR_CANE, SUNFLOWER, SWEET_BERRY_BUSH, TALL_GRASS, WARPED_FUNGUS, WARPED_ROOTS, WHEAT, WHITE_TULIP, WITHER_ROSE -> {
                            }
                            // lamps, glowstone and torches
                            case TORCH, GLOWSTONE, REDSTONE_LAMP -> {
                                BlockData light;
                                if (dd.isSubmarine() && mat.equals(Material.TORCH)) {
                                    light = Material.GLOWSTONE.createBlockData();
                                } else {
                                    light = colData[yy];
                                }
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, light);
                            }
                            // wood, iron & trap doors
                            case IRON_DOOR, ACACIA_DOOR, ACACIA_TRAPDOOR, ACACIA_WALL_SIGN, BAMBOO_DOOR, BAMBOO_TRAPDOOR, BAMBOO_WALL_SIGN, BIRCH_DOOR, BIRCH_TRAPDOOR, BIRCH_WALL_SIGN, CHERRY_DOOR, CHERRY_TRAPDOOR, CHERRY_WALL_SIGN, CRIMSON_DOOR, CRIMSON_TRAPDOOR, CRIMSON_WALL_SIGN, DARK_OAK_DOOR, DARK_OAK_TRAPDOOR, DARK_OAK_WALL_SIGN, JUNGLE_DOOR, JUNGLE_TRAPDOOR, JUNGLE_WALL_SIGN, MANGROVE_DOOR, MANGROVE_TRAPDOOR, MANGROVE_WALL_SIGN, OAK_DOOR, OAK_TRAPDOOR, OAK_WALL_SIGN, SPRUCE_DOOR, SPRUCE_TRAPDOOR, SPRUCE_WALL_SIGN, WARPED_DOOR, WARPED_TRAPDOOR, WARPED_WALL_SIGN -> {
                                if (preset.equals(ChameleonPreset.SWAMP) || preset.equals(ChameleonPreset.TOPSYTURVEY) || preset.equals(ChameleonPreset.JAIL)) {
                                    TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, Material.AIR);
                                }
                            }
                            case WHITE_STAINED_GLASS -> {
                                BlockData chaf = (preset.equals(ChameleonPreset.FLOWER)) ? stain_colour : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chaf);
                            }
                            case LIME_STAINED_GLASS -> {
                                BlockData chap = (preset.equals(ChameleonPreset.PARTY)) ? stain_colour : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chap);
                            }
                            case LIGHT_GRAY_STAINED_GLASS -> {
                                BlockData cham = (preset.equals(ChameleonPreset.FACTORY)) ? TARDISStainedGlassLookup.stainedGlassFromMaterial(world, cham_id.getMaterial()).createBlockData() : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, cham);
                            }
                            case LIGHT_GRAY_TERRACOTTA -> {
                                BlockData chai = (preset.equals(ChameleonPreset.FACTORY)) ? cham_id : colData[yy];
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, chai);
                            }
                            default -> {
                                // everything else
                                TARDISBlockSetters.setBlock(world, xx, (y + yy), zz, colData[yy]);
                                if (preset == ChameleonPreset.JUNK_MODE && (mat == Material.ORANGE_WOOL || mat == Material.ORANGE_STAINED_GLASS || mat == Material.GLASS)) {
                                    // remove item display entities
                                    TARDISDisplayItemUtils.remove(world.getBlockAt(xx, (y + yy), zz));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, dd.isHide(), preset);
            if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                // teleport player(s) to exit (tmd.getFromToLocation())
                getJunkTravellers().forEach((e) -> {
                    if (e instanceof Player p) {
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

    private void getColours(int id, ChameleonPreset p) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 0);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            try {
                Block b = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock();
                if (p.equals(ChameleonPreset.FLOWER)) {
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
