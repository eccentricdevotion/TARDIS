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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISExteriorRenderer {

    private final TARDIS plugin;

    public TARDISExteriorRenderer(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void render(String interior, Location exterior, int id, final Player p, final COMPASS d, long time, Biome biome) {
        // construct a string for comparison
        World ew = exterior.getWorld();
        int epbx = exterior.getBlockX();
        int epby = exterior.getBlockY();
        int epbz = exterior.getBlockZ();
        String isRendered = ew.getName() + ":" + epbx + ":" + epby + ":" + epbz;
        String[] idata = interior.split(":");
        World iw = plugin.getServer().getWorld(idata[0]);
        int ipbx = plugin.getUtils().parseInt(idata[1]);
        int ipby = plugin.getUtils().parseInt(idata[2]) + 2;
        int ipbz = plugin.getUtils().parseInt(idata[3]);
        final Location location = new Location(iw, ipbx, ipby, ipbz);
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
                            switch (eb.getTypeId()) {
                                case 8:
                                case 9:
                                    ib.setType(Material.STAINED_GLASS);
                                    ib.setData((byte) 3, true);
                                    break;
                                case 10:
                                case 11:
                                    ib.setType(Material.WOOL);
                                    ib.setData((byte) 1, true);
                                    break;
                                default:
                                    ib.setTypeIdAndData(eb.getTypeId(), eb.getData(), true);
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
            int[][] ids = column.getId();
            byte[][] data = column.getData();
            for (int i = 0; i < 9; i++) {
                int[] colids = ids[i];
                byte[] coldatas = data[i];
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
                    plugin.getUtils().setBlock(iw, px, (y + py), pz, colids[py], coldatas[py]);
                }
            }
            // change the black/blue/green wool to blue/black/ to reflect time of day and environment
            byte sky;
            Material base;
            Material stone;
            switch (biome) {
                case SKY:
                    sky = 15;
                    base = Material.ENDER_STONE;
                    stone = Material.OBSIDIAN;
                    break;
                case HELL:
                    sky = 15;
                    base = Material.NETHERRACK;
                    stone = Material.QUARTZ_ORE;
                    break;
                default:
                    sky = (time > 12500) ? (byte) 15 : 3;
                    base = Material.DIRT;
                    stone = Material.STONE;
                    break;
            }
            int endx = isx + 13;
            int topy = isy + 8;
            int endz = isz + 13;
            // change the ceiling
            for (int cx = isx; cx < isx + 13; cx++) {
                for (int cz = isz; cz < (isz + 13); cz++) {
                    iw.getBlockAt(cx, topy, cz).setData(sky);
                }
            }
            // change the first and third walls
            for (int x1 = isx - 1; x1 <= endx; x1++) {
                for (int y1 = isy; y1 < topy; y1++) {
                    switch (iw.getBlockAt(x1, y1, isz - 1).getType()) {
                        case WOOL:
                            iw.getBlockAt(x1, y1, isz - 1).setData(sky);
                            break;
                        case DIRT:
                        case ENDER_STONE:
                        case NETHERRACK:
                            iw.getBlockAt(x1, y1, isz - 1).setType(base);
                            break;
                        default:
                            iw.getBlockAt(x1, y1, isz - 1).setType(stone);
                            break;
                    }
                    switch (iw.getBlockAt(x1, y1, endz).getType()) {
                        case WOOL:
                            iw.getBlockAt(x1, y1, endz).setData(sky);
                            break;
                        case DIRT:
                        case ENDER_STONE:
                        case NETHERRACK:
                            iw.getBlockAt(x1, y1, endz).setType(base);
                            break;
                        default:
                            iw.getBlockAt(x1, y1, endz).setType(stone);
                            break;
                    }
                }
            }
            // build second and fourth walls
            for (int z2 = isz - 1; z2 <= endz; z2++) {
                for (int y2 = isy; y2 < topy; y2++) {
                    switch (iw.getBlockAt(isx - 1, y2, z2).getType()) {
                        case WOOL:
                            iw.getBlockAt(isx - 1, y2, z2).setData(sky);
                            break;
                        case DIRT:
                        case ENDER_STONE:
                        case NETHERRACK:
                            iw.getBlockAt(isx - 1, y2, z2).setType(base);
                            break;
                        default:
                            iw.getBlockAt(isx - 1, y2, z2).setType(stone);
                            break;
                    }
                    switch (iw.getBlockAt(endx, y2, z2).getType()) {
                        case WOOL:
                            iw.getBlockAt(endx, y2, z2).setData(sky);
                            break;
                        case DIRT:
                        case ENDER_STONE:
                        case NETHERRACK:
                            iw.getBlockAt(endx, y2, z2).setType(base);
                            break;
                        default:
                            iw.getBlockAt(endx, y2, z2).setType(stone);
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
        // charge artron energy for the render
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        new QueryFactory(plugin).alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("render"), where, p);
        // tp the player inside the room
        plugin.getTrackerKeeper().getTransmat().add(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                transmat(p, d, location);
                p.playSound(location, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                TARDISMessage.send(p, "RENDER_EXIT");
            }
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
