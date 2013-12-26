/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISExteriorRenderer {

    private final TARDIS plugin;
    List<Integer> plat_blocks = Arrays.asList(new Integer[]{0, 6, 9, 8, 31, 32, 37, 38, 39, 40, 78, 106, 3019, 3020});

    public TARDISExteriorRenderer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void render(String interior, Location exterior, boolean next, int id, Player p, TARDISConstants.COMPASS d, long time) {
        // TODO only render if the destination has changed
        // construct a string for comparison
        World ew = exterior.getWorld();
        int epbx = exterior.getBlockX();
        int epby = exterior.getBlockY();
        int epbz = exterior.getBlockZ();
        String isRendered = ew.getName() + ":" + epbx + ":" + epby + ":" + epbz;
        String[] idata = interior.split(":");
        World iw = plugin.getServer().getWorld(idata[0]);
        int ipbx = plugin.utils.parseNum(idata[1]);
        int ipby = plugin.utils.parseNum(idata[2]) + 2;
        int ipbz = plugin.utils.parseNum(idata[3]);
        Location location = new Location(iw, ipbx, ipby, ipbz);
        if (plugin.trackRenderer.containsKey(id) && plugin.trackRenderer.get(id).equals(isRendered)) {
            p.sendMessage(plugin.pluginName + "Destination unchanged, no rendering needed, stand by for transmat...");
        } else {
            p.sendMessage(plugin.pluginName + "Starting exterior rendering, please wait...");
            int isx, isy, isz, esx, esy, esz, xx = 0, yy = 0, zz = 0;
            // get interior coords
            isx = ipbx - 6;
            isy = ipby - 1;
            isz = ipbz - 6;
            // get exterior coords
            esx = epbx - 6;
            esy = epby - 1;
            esz = epbz - 6;
            ew.getChunkAt(exterior).load();
            // loop through exterior blocks and mirror them in the interior
            for (int y = esy; y < (esy + 8); y++) {
                for (int x = esx; x < (esx + 13); x++) {
                    for (int z = esz; z < (esz + 13); z++) {
                        Block eb = ew.getBlockAt(x, y, z);
                        Block ib = iw.getBlockAt(isx + xx, isy + yy, isz + zz);
                        switch (eb.getTypeId()) {
                            case 8:
                            case 9:
                                ib.setTypeIdAndData(95, (byte) 3, true);
                                break;
                            case 10:
                            case 11:
                                ib.setTypeIdAndData(35, (byte) 1, true);
                                break;
                            default:
                                ib.setTypeIdAndData(eb.getTypeId(), eb.getData(), true);
                        }
                        zz++;
                    }
                    zz = 0;
                    xx++;
                }
                xx = 0;
                yy++;
            }
            // if this is the 'next' location, render a glass TARDIS
            if (next) {
                // get relative locations
                int x = location.getBlockX();
                int plusx = (location.getBlockX() + 1);
                int minusx = (location.getBlockX() - 1);
                int y = location.getBlockY();
                int z = (location.getBlockZ());
                int plusz = (location.getBlockZ() + 1);
                int minusz = (location.getBlockZ() - 1);
                TARDISChameleonColumn column = plugin.presets.getGlass(TARDISConstants.PRESET.OLD, d);
                addPlatform(location, d, p.getName(), id);
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
                        plugin.utils.setBlock(iw, px, (y + py), pz, colids[py], coldatas[py]);
                    }
                }
            }
            // change the grey/blue wool to blue/grey to reflect time of day
            byte to = (time > 0 && time < 12500) ? (byte) 3 : 15;
            byte from = (time > 0 && time < 12500) ? (byte) 15 : 3;
            for (int x = isx; x < isx + 13; x++) {
                for (int z = isz; z < (isz + 13); z++) {
                    if (iw.getBlockAt(x, isy + 8, z).getData() == from) {
                        iw.getBlockAt(x, isy + 8, z).setData(to);
                    }
                }
            }
            for (int x1 = isx - 1; x1 < isx + 14; x1++) {
                for (int y1 = isy + 2; y1 < isy + 8; y1++) {
                    if (iw.getBlockAt(x1, y1, isz - 1).getData() == from) {
                        iw.getBlockAt(x1, y1, isz - 1).setData(to);
                    }
                    if (iw.getBlockAt(x1, y1, isz + 13).getData() == from) {
                        iw.getBlockAt(x1, y1, isz + 13).setData(to);
                    }
                }
            }
            // build second and fourth walls
            for (int z2 = isz - 1; z2 < isz + 14; z2++) {
                for (int y2 = isy + 2; y2 < isy + 8; y2++) {
                    if (iw.getBlockAt(isx - 1, y2, z2).getData() == from) {
                        iw.getBlockAt(isx - 1, y2, z2).setData(to);
                    }
                    if (iw.getBlockAt(isx + 13, y2, z2).getData() == from) {
                        iw.getBlockAt(isx + 13, y2, z2).setData(to);
                    }
                }
            }
            plugin.trackRenderer.put(id, isRendered);
            p.sendMessage(plugin.pluginName + "Rendering complete, stand by for transmat...");
        }
        // tp the player inside the room
        plugin.trackTransmat.add(p.getName());
        transmat(p, d, location);
    }

    private void transmat(Player player, TARDISConstants.COMPASS d, Location loc) {
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

    private void addPlatform(Location l, TARDISConstants.COMPASS d, String p, int id) {
        int plusx, minusx, x, y, plusz, minusz, z;
        int platform_id = plugin.getConfig().getInt("police_box.platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("police_box.platform_data");
        // add platform if configured and necessary
        World world = l.getWorld();
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        y = (l.getBlockY() - 1);
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        if (plugin.getConfig().getBoolean("travel.platform")) {
            // check if user has platform pref
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p);
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.isPlatform_on();
            } else {
                userPlatform = true;
            }
            if (userPlatform) {
                List<Block> platform_blocks;
                switch (d) {
                    case SOUTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x - 1, y, minusz - 1), world.getBlockAt(x, y, minusz - 1), world.getBlockAt(x + 1, y, minusz - 1), world.getBlockAt(x - 1, y, minusz - 2), world.getBlockAt(x, y, minusz - 2), world.getBlockAt(x + 1, y, minusz - 2));
                        break;
                    case EAST:
                        platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, y, z - 1), world.getBlockAt(minusx - 1, y, z), world.getBlockAt(minusx - 1, y, z + 1), world.getBlockAt(minusx - 2, y, z - 1), world.getBlockAt(minusx - 2, y, z), world.getBlockAt(minusx - 2, y, z + 1));
                        break;
                    case NORTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x + 1, y, plusz + 1), world.getBlockAt(x, y, plusz + 1), world.getBlockAt(x - 1, y, plusz + 1), world.getBlockAt(x + 1, y, plusz + 2), world.getBlockAt(x, y, plusz + 2), world.getBlockAt(x - 1, y, plusz + 2));
                        break;
                    default:
                        platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, y, z + 1), world.getBlockAt(plusx + 1, y, z), world.getBlockAt(plusx + 1, y, z - 1), world.getBlockAt(plusx + 2, y, z + 1), world.getBlockAt(plusx + 2, y, z), world.getBlockAt(plusx + 2, y, z - 1));
                        break;
                }
                for (Block pb : platform_blocks) {
                    int matint = pb.getTypeId();
                    if (plat_blocks.contains(matint)) {
                        plugin.utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data);
                    }
                }
            }
        }
    }
}
