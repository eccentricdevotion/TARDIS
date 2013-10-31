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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISInstaPreset {

    private final TARDIS plugin;
    private final TARDISConstants.COMPASS d;
    private final Location location;
    private final int tid;
    private final String p;
    private final boolean mal;
    private final boolean sub;
    private Block sponge;
    private final TARDISConstants.PRESET preset;
    private TARDISChameleonColumn column;

    public TARDISInstaPreset(TARDIS plugin, Location location, TARDISConstants.PRESET preset, int tid, TARDISConstants.COMPASS d, String p, boolean mal, boolean sub) {
        this.plugin = plugin;
        this.d = d;
        this.location = location;
        this.preset = preset;
        this.tid = tid;
        this.p = p;
        this.mal = mal;
        this.sub = sub;
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        column = getColumn(preset, d);
        int plusx, minusx, x, plusz, y, undery, minusz, z, platform_id = plugin.getConfig().getInt("platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("platform_data");
        final World world;
        // get relative locations
        x = location.getBlockX();
        plusx = (location.getBlockX() + 1);
        minusx = (location.getBlockX() - 1);
        y = location.getBlockY();
        undery = (location.getBlockY() - 1);
        z = (location.getBlockZ());
        plusz = (location.getBlockZ() + 1);
        minusz = (location.getBlockZ() - 1);
        world = location.getWorld();
        int signx = 0, signz = 0;
        String doorloc = "";
        // rescue player?
        if (plugin.trackRescue.containsKey(tid)) {
            String name = plugin.trackRescue.get(tid);
            Player saved = plugin.getServer().getPlayer(name);
            if (saved != null) {
                TARDISDoorLocation idl = plugin.doorListener.getDoor(1, tid);
                Location l = idl.getL();
                plugin.doorListener.movePlayer(saved, l, false, world, false);
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", tid);
                set.put("player", name);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doInsert("travellers", set);
            }
            plugin.trackRescue.remove(tid);
        }
        // platform
        plugin.buildPB.addPlatform(location, false, d, p, tid);
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> ps = new HashMap<String, Object>();
        ps.put("tardis_id", tid);
        String loc = "";
        // get direction player is facing from yaw
        // place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                if (sub) {
                    plugin.utils.setBlockCheck(world, x, undery, minusz, 19, (byte) 0, tid, true); // door is here if player facing south
                    sponge = world.getBlockAt(x, undery, minusz);
                } else {
                    plugin.utils.setBlockCheck(world, x, undery, minusz, platform_id, platform_data, tid, false); // door is here if player facing south
                }
                loc = world.getBlockAt(x, undery, minusz).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + x + ":" + y + ":" + minusz;
                signx = x;
                signz = (minusz - 1);
                break;
            case EAST:
                //if (yaw >= 225 && yaw < 315)
                if (sub) {
                    plugin.utils.setBlockCheck(world, minusx, undery, z, 19, (byte) 0, tid, true); // door is here if player facing east
                    sponge = world.getBlockAt(minusx, undery, z);
                } else {
                    plugin.utils.setBlockCheck(world, minusx, undery, z, platform_id, platform_data, tid, false); // door is here if player facing east
                }
                loc = world.getBlockAt(minusx, undery, z).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + minusx + ":" + y + ":" + z;
                signx = (minusx - 1);
                signz = z;
                break;
            case NORTH:
                //if (yaw >= 135 && yaw < 225)
                if (sub) {
                    plugin.utils.setBlockCheck(world, x, undery, plusz, 19, (byte) 0, tid, true); // door is here if player facing north
                    sponge = world.getBlockAt(x, undery, plusz);
                } else {
                    plugin.utils.setBlockCheck(world, x, undery, plusz, platform_id, platform_data, tid, false); // door is here if player facing north
                }
                loc = world.getBlockAt(x, undery, plusz).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + x + ":" + y + ":" + plusz;
                signx = x;
                signz = (plusz + 1);
                break;
            case WEST:
                //if (yaw >= 45 && yaw < 135)
                if (sub) {
                    plugin.utils.setBlockCheck(world, plusx, undery, z, 19, (byte) 0, tid, true); // door is here if player facing west
                    sponge = world.getBlockAt(plusx, undery, z);
                } else {
                    plugin.utils.setBlockCheck(world, plusx, undery, z, platform_id, platform_data, tid, false); // door is here if player facing west
                }
                loc = world.getBlockAt(plusx, undery, z).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + plusx + ":" + y + ":" + z;
                signx = (plusx + 1);
                signz = z;
                break;
        }
        ps.put("police_box", 1);
        qf.doInsert("blocks", ps);
        if (!loc.isEmpty()) {
            plugin.protectBlockMap.put(loc, tid);
        }
        // should insert the door when tardis is first made, and then update location there after!
        HashMap<String, Object> whered = new HashMap<String, Object>();
        whered.put("door_type", 0);
        whered.put("tardis_id", tid);
        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<String, Object>();
        setd.put("door_location", doorloc);
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<String, Object>();
            whereid.put("door_id", rsd.getDoor_id());
            qf.doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", tid);
            setd.put("door_type", 0);
            setd.put("door_direction", d.toString());
            qf.doInsert("doors", setd);
        }
        int xx, zz;
        int[][] ids = column.getId();
        byte[][] data = column.getData();
        for (int i = 0; i < 10; i++) {
            int[] colids = ids[i];
            byte[] coldatas = data[i];
            switch (i) {
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
                case 8:
                    xx = x;
                    zz = z;
                    break;
                default:
                    xx = signx;
                    zz = signz;
                    break;
            }
            for (int yy = 0; yy < 4; yy++) {
                if (colids[yy] != 68) {
                    plugin.utils.setBlockAndRemember(world, xx, (y + yy), zz, colids[yy], coldatas[yy], tid);
                }
                if (yy == 2 && colids[yy] == 68) {
                    plugin.utils.setBlock(world, xx, (y + yy), zz, colids[yy], coldatas[yy]);
                    Block sign = world.getBlockAt(xx, (y + yy), zz);
                    if (sign.getType().equals(Material.WALL_SIGN)) {
                        Sign s = (Sign) sign.getState();
                        if (plugin.getConfig().getBoolean("name_tardis")) {
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            wheret.put("tardis_id", tid);
                            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                            if (rst.resultSet()) {
                                String owner = rst.getOwner();
                                if (owner.length() > 14) {
                                    s.setLine(0, owner.substring(0, 12) + "'s");
                                } else {
                                    s.setLine(0, owner + "'s");
                                }
                            }
                        }
                        s.setLine(1, ChatColor.WHITE + "POLICE");
                        s.setLine(2, ChatColor.WHITE + "BOX");
                        s.update();
                    }
                }
            }
        }
        if (sub && plugin.worldGuardOnServer) {
            plugin.wgutils.sponge(sponge, true);
        }
        // message travellers in tardis
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", tid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
        if (rst.resultSet()) {
            final List<String> travellers = rst.getData();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (String s : travellers) {
                        Player p = plugin.getServer().getPlayer(s);
                        if (p != null) {
                            String message = (mal) ? "There was a malfunction and the emergency handbrake was engaged! Scan location before exit!" : "LEFT-click the handbrake to exit!";
                            p.sendMessage(plugin.pluginName + message);
                        }
                    }
                }
            }, 30L);
        }
        plugin.tardisMaterialising.remove(Integer.valueOf(tid));
    }

    private TARDISChameleonColumn getColumn(TARDISConstants.PRESET p, TARDISConstants.COMPASS d) {
        switch (p) {
            case OLD:
                return plugin.presets.getPolice().get(d);
            case FACTORY:
                return plugin.presets.getFactory().get(d);
            case STONE:
                return plugin.presets.getColumn().get(d);
            case DESERT:
                return plugin.presets.getDesert().get(d);
            case JUNGLE:
                return plugin.presets.getJungle().get(d);
            case NETHER:
                return plugin.presets.getNether().get(d);
            case SWAMP:
                return plugin.presets.getSwamp().get(d);
            case PARTY:
                return plugin.presets.getTent().get(d);
            case VILLAGE:
                return plugin.presets.getVillage().get(d);
            case YELLOW:
                return plugin.presets.getYellowsub().get(d);
            default:
                return plugin.presets.getTaller().get(d);
        }
    }
}
