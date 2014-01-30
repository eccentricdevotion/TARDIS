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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
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
        int ipbx = plugin.utils.parseInt(idata[1]);
        int ipby = plugin.utils.parseInt(idata[2]) + 2;
        int ipbz = plugin.utils.parseInt(idata[3]);
        final Location location = new Location(iw, ipbx, ipby, ipbz);
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
                                    ib.setTypeIdAndData(95, (byte) 3, true);
                                    break;
                                case 10:
                                case 11:
                                    ib.setTypeIdAndData(35, (byte) 1, true);
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
            TARDISChameleonColumn column = plugin.presets.getGlass(PRESET.RENDER, d);
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
            plugin.trackRenderer.put(id, isRendered);
            p.sendMessage(plugin.pluginName + "Rendering complete, stand by for transmat...");
        }
        // charge artron energy for the render
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        new QueryFactory(plugin).alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("render"), where, p);
        // tp the player inside the room
        plugin.trackTransmat.add(p.getName());
        plugin.getServer()
                .getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        transmat(p, d, location);
                        p.playSound(location, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        p.sendMessage(plugin.pluginName + "Right-click to exit.");
                    }
                },
                10L);
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

    @SuppressWarnings("deprecation")
    private void addPlatform(Location l, COMPASS d, String p, int id) {
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
                userPlatform = pp.isPlatformOn();
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
                    if (TARDISConstants.PLATFORM_BLOCKS.contains(matint)) {
                        plugin.utils.setBlock(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data);
                    }
                }
            }
        }
    }
}
