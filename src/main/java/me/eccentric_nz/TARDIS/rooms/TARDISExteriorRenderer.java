/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISEntityTracker;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISExteriorRenderer {

    private final TARDIS plugin;

    public TARDISExteriorRenderer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void render(String interior, Location exterior, int id, Player p, COMPASS d, long time, String biome) {
        // construct a string for comparison
        World ew = exterior.getWorld();
        int epbx = exterior.getBlockX();
        int epby = exterior.getBlockY();
        int epbz = exterior.getBlockZ();
        String isRendered = ew.getName() + ":" + epbx + ":" + epby + ":" + epbz;
        String[] idata = interior.split(":");
        World iw = plugin.getServer().getWorld(idata[0]);
        int ipbx = TARDISNumberParsers.parseInt(idata[1]);
        int ipby = TARDISNumberParsers.parseInt(idata[2]) + 2;
        int ipbz = TARDISNumberParsers.parseInt(idata[3]);
        Location location = new Location(iw, ipbx, ipby, ipbz);
        if (plugin.getTrackerKeeper().getRenderer().containsKey(id) && plugin.getTrackerKeeper().getRenderer().get(id).equals(isRendered)) {
            TARDISMessage.send(p, "DEST_NO_CHANGE");
        } else {
            TARDISMessage.send(p, "RENDER_START");
            int isx, isy, isz, esx, esy, esz, xx = 0, yy = 0, zz = 0;
            // get interior coords
            isx = ipbx - 6;
            isy = ipby - 1;
            isz = ipbz - 6;
            // get exterior coords
            esx = epbx - 6;
            esy = epby - 1;
            esz = epbz - 6;
            // get preset bounds
            int bwx = epbx - 1;
            int bex = epbx + 1;
            int buy = epby + 3;
            int bnz = epbz - 1;
            int bsz = epbz + 1;
            ew.getChunkAt(exterior).load();
            // loop through exterior blocks and mirror them in the interior
            for (int y = esy; y < (esy + 8); y++) {
                for (int x = esx; x < (esx + 13); x++) {
                    for (int z = esz; z < (esz + 13); z++) {
                        // don't do preset blocks - they'l be set to glass later
                        if (!(y >= epby && y <= buy && x >= bwx && x <= bex && z >= bnz && z <= bsz)) {
                            Block eb = ew.getBlockAt(x, y, z);
                            Block ib = iw.getBlockAt(isx + xx, isy + yy, isz + zz);
                            switch (eb.getType()) {
                                case WATER:
                                    ib.setBlockData(Material.LIGHT_BLUE_STAINED_GLASS.createBlockData(), true);
                                    break;
                                case LAVA:
                                    ib.setBlockData(Material.ORANGE_WOOL.createBlockData(), true);
                                    break;
                                default:
                                    ib.setBlockData(eb.getBlockData(), true);
                            }
                        }
                        zz++;
                    }
                    zz = 0;
                    xx++;
                }
                xx = 0;
                yy++;
            }
            // render a glass TARDIS
            // get relative locations
            int x = location.getBlockX();
            int plusx = (location.getBlockX() + 1);
            int minusx = (location.getBlockX() - 1);
            int y = location.getBlockY();
            int z = (location.getBlockZ());
            int plusz = (location.getBlockZ() + 1);
            int minusz = (location.getBlockZ() - 1);
            TARDISChameleonColumn column = plugin.getPresets().getGlass(PRESET.RENDER, d);
            int px, pz;
            BlockData[][] data = column.getBlockData();
            for (int i = 0; i < 9; i++) {
                BlockData[] coldatas = data[i];
                switch (i) {
                    case 0:
                        px = minusx;
                        pz = minusz;
                        break;
                    case 1:
                        px = x;
                        pz = minusz;
                        break;
                    case 2:
                        px = plusx;
                        pz = minusz;
                        break;
                    case 3:
                        px = plusx;
                        pz = z;
                        break;
                    case 4:
                        px = plusx;
                        pz = plusz;
                        break;
                    case 5:
                        px = x;
                        pz = plusz;
                        break;
                    case 6:
                        px = minusx;
                        pz = plusz;
                        break;
                    case 7:
                        px = minusx;
                        pz = z;
                        break;
                    default:
                        px = x;
                        pz = z;
                        break;
                }
                for (int py = 0; py < 4; py++) {
                    TARDISBlockSetters.setBlock(iw, px, (y + py), pz, coldatas[py]);
                }
            }
            // change the black/blue/green wool to blue/black/ to reflect time of day and environment
            BlockData sky;
            BlockData base;
            BlockData stone;
            switch (biome) {
                case "THE_END":
                    sky = TARDISConstants.BLACK;
                    base = Material.END_STONE.createBlockData();
                    stone = Material.OBSIDIAN.createBlockData();
                    break;
                case "NETHER_WASTES":
                case "SOUL_SAND_VALLEY":
                case "CRIMSON_FOREST":
                case "WARPED_FOREST":
                case "BASALT_DELTAS":
                    sky = TARDISConstants.BLACK;
                    base = Material.NETHERRACK.createBlockData();
                    stone = Material.NETHER_QUARTZ_ORE.createBlockData();
                    break;
                default:
                    sky = (time > 12500) ? TARDISConstants.BLACK : Material.LIGHT_BLUE_WOOL.createBlockData();
                    base = Material.DIRT.createBlockData();
                    stone = Material.STONE.createBlockData();
                    break;
            }
            int endx = isx + 13;
            int topy = isy + 8;
            int endz = isz + 13;
            // change the ceiling
            for (int cx = isx; cx < isx + 13; cx++) {
                for (int cz = isz; cz < (isz + 13); cz++) {
                    Block skyBlock = iw.getBlockAt(cx, topy, cz);
                    if (Tag.WOOL.isTagged(skyBlock.getType())) {
                        skyBlock.setBlockData(sky);
                    }
                }
            }
            // change the first and third walls
            for (int x1 = isx - 1; x1 <= endx; x1++) {
                for (int y1 = isy; y1 < topy; y1++) {
                    Block first = iw.getBlockAt(x1, y1, isz - 1);
                    switch (first.getType()) {
                        case LIGHT_BLUE_WOOL:
                        case BLACK_WOOL:
                            first.setBlockData(sky);
                            break;
                        case DIRT:
                        case END_STONE:
                        case NETHERRACK:
                            first.setBlockData(base);
                            break;
                        default:
                            first.setBlockData(stone);
                            break;
                    }
                    Block third = iw.getBlockAt(x1, y1, endz);
                    switch (third.getType()) {
                        case LIGHT_BLUE_WOOL:
                        case BLACK_WOOL:
                            third.setBlockData(sky);
                            break;
                        case DIRT:
                        case END_STONE:
                        case NETHERRACK:
                            third.setBlockData(base);
                            break;
                        default:
                            third.setBlockData(stone);
                            break;
                    }
                }
            }
            // build second and fourth walls
            for (int z2 = isz - 1; z2 <= endz; z2++) {
                for (int y2 = isy; y2 < topy; y2++) {
                    Block second = iw.getBlockAt(isx - 1, y2, z2);
                    switch (second.getType()) {
                        case LIGHT_BLUE_WOOL:
                        case BLACK_WOOL:
                            second.setBlockData(sky);
                            break;
                        case DIRT:
                        case END_STONE:
                        case NETHERRACK:
                            second.setBlockData(base);
                            break;
                        default:
                            second.setBlockData(stone);
                            break;
                    }
                    Block fourth = iw.getBlockAt(endx, y2, z2);
                    switch (fourth.getType()) {
                        case LIGHT_BLUE_WOOL:
                        case BLACK_WOOL:
                            fourth.setBlockData(sky);
                            break;
                        case DIRT:
                        case END_STONE:
                        case NETHERRACK:
                            fourth.setBlockData(base);
                            break;
                        default:
                            fourth.setBlockData(stone);
                            break;
                    }
                }
            }
            plugin.getTrackerKeeper().getRenderer().put(id, isRendered);
            TARDISMessage.send(p, "RENDER_DONE");
            // remove dropped items
            for (Entity e : location.getChunk().getEntities()) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
        }
        // if enabled add static entities
        if (plugin.getConfig().getBoolean("preferences.render_entities")) {
            new TARDISEntityTracker(plugin).addNPCs(exterior, location, p.getUniqueId());
        }
        // charge artron energy for the render
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        plugin.getQueryFactory().alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("render"), where, p);
        // tp the player inside the room
        plugin.getTrackerKeeper().getRenderRoomOccupants().add(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            transmat(p, d, location);
            p.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            TARDISMessage.send(p, "RENDER_EXIT");
        }, 10L);
    }

    public void transmat(Player player, COMPASS d, Location loc) {
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        // make location safe ie. outside of the bluebox
        double ex = loc.getX();
        double ez = loc.getZ();
        switch (d) {
            case NORTH:
                loc.setX(ex + 0.5);
                loc.setZ(ez + 2.5);
                break;
            case EAST:
                loc.setX(ex - 1.5);
                loc.setZ(ez + 0.5);
                break;
            case SOUTH:
                loc.setX(ex + 0.5);
                loc.setZ(ez - 1.5);
                break;
            case WEST:
                loc.setX(ex + 2.5);
                loc.setZ(ez + 0.5);
                break;
        }
        player.teleport(loc);
    }
}
