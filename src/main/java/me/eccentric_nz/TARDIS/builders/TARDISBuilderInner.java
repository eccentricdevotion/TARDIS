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

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted
 * resources to malfunctioning controls to a simple inability to arrive at the
 * proper time or location. While the Doctor did not build the TARDIS from
 * scratch, he has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInner {

    private final TARDIS plugin;
    List<Block> lampblocks = new ArrayList<Block>();

    public TARDISBuilderInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be DEFAULT, BIGGER,
     * ELEVENTH, REDSTONE, COAL or DELUXE.
     * @param world the world where the TARDIS is to be built.
     * @param dbID the unique key of the record for this TARDIS in the database.
     * @param p an instance of the player who owns the TARDIS.
     * @param middle_id the material type ID determined from the middle block in
     * the TARDIS creation stack, this material determines the makeup of the
     * TARDIS walls.
     * @param middle_data the data bit associated with the middle_id parameter.
     */
    public void buildInner(TARDISConstants.SCHEMATIC schm, World world, int dbID, Player p, int middle_id, byte middle_data) {
        String[][][] s;
        short[] d;
        switch (schm) {
            // TARDIS schematics supplied by ewized http://dev.bukkit.org/profiles/ewized/
            case BIGGER:
                s = plugin.biggerschematic;
                d = plugin.biggerdimensions;
                break;
            case DELUXE:
                s = plugin.deluxeschematic;
                d = plugin.deluxedimensions;
                break;
            case ELEVENTH:
                s = plugin.eleventhschematic;
                d = plugin.eleventhdimensions;
                break;
            case REDSTONE:
                s = plugin.redstoneschematic;
                d = plugin.redstonedimensions;
                break;
            case STEAMPUNK:
                s = plugin.steampunkschematic;
                d = plugin.steampunkdimensions
                break;
            default:
                s = plugin.budgetschematic;
                d = plugin.budgetdimensions;
                break;
        }
        short h = d[0];
        short w = d[1];
        short l = d[2];
        int level, row, col, id, x, z, startx, starty = 15, startz, resetx, resetz, cx, cy, cz, rid, multiplier = 1, j = 2;
        byte data;
        short damage = 0;
        String tmp, replacedBlocks;
        HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
        Block postSaveBlock = null;
        // calculate startx, starty, startz
        int gsl[] = plugin.utils.getStartLocation(dbID);
        startx = gsl[0];
        resetx = gsl[1];
        startz = gsl[2];
        resetz = gsl[3];
        x = gsl[4];
        z = gsl[5];
        Location wg1 = new Location(world, startx, starty, startz);
        // need to set TARDIS space to air first otherwise torches may be placed askew
        // also getting and storing block ids for bonus chest if configured
        StringBuilder sb = new StringBuilder();
        List<Chunk> chunkList = new ArrayList<Chunk>();
        boolean own_world = plugin.getConfig().getBoolean("create_worlds");
        boolean bonus_chest = plugin.getConfig().getBoolean("bonus_chest");
        boolean schematicHasChest = false;
        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < l; col++) {
                    Location replaceLoc = new Location(world, startx, starty, startz);
                    // get list of used chunks
                    Chunk thisChunk = world.getChunkAt(replaceLoc);
                    if (!chunkList.contains(thisChunk)) {
                        chunkList.add(thisChunk);
                    }
                    if (bonus_chest && !own_world) {
                        // get block at location
                        int replacedMaterialId = replaceLoc.getBlock().getTypeId();
                        if (replacedMaterialId != 8 && replacedMaterialId != 9 && replacedMaterialId != 10 && replacedMaterialId != 11) {
                            sb.append(replacedMaterialId).append(":");
                        }
                    }
                    if (!own_world) {
                        plugin.utils.setBlock(world, startx, starty, startz, 0, (byte) 0);
                    }
                    startx += x;
                }
                startx = resetx;
                startz += z;
            }
            startz = resetz;
            starty += 1;
        }
        Location wg2 = new Location(world, startx + (w - 1), starty, startz + (l - 1));
        // update chunks list in DB
        QueryFactory qf = new QueryFactory(plugin);
        for (Chunk c : chunkList) {
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("tardis_id", dbID);
            set.put("world", world.getName());
            set.put("x", c.getX());
            set.put("z", c.getZ());
            qf.doInsert("chunks", set);
        }
        // reset start positions and do over
        startx = resetx;
        starty = 15;
        startz = resetz;

        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < l; col++) {
                    tmp = s[level][row][col];
                    if (!tmp.equals("-")) {
                        if (tmp.contains(":")) {
                            String[] iddata = tmp.split(":");
                            id = plugin.utils.parseNum(iddata[0]);
                            data = Byte.parseByte(iddata[1]);
                            if (id == 54) { // chest
                                schematicHasChest = true;
                                // remember the location of this chest - if create_worlds is true make it the condenser chest
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                String chest = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                String which = (own_world) ? "condenser" : "chest";
                                setc.put(which, chest);
                                wherec.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setc, wherec);
                            }
                            if (id == 77) { // stone button
                                // remember the location of this button
                                String button = plugin.utils.makeLocationStr(world, startx, starty, startz);
                                qf.insertControl(dbID, 1, button, 0);
                            }
                            if (id == 93) { // remember the location of this redstone repeater
                                // save repeater location
                                String repeater = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                qf.insertControl(dbID, j, repeater, 0);
                                j++;
                            }
                            if (id == 71 && data < (byte) 8) { // iron door bottom
                                HashMap<String, Object> setd = new HashMap<String, Object>();
                                String doorloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setd.put("tardis_id", dbID);
                                setd.put("door_type", 1);
                                setd.put("door_location", doorloc);
                                setd.put("door_direction", "SOUTH");
                                qf.doInsert("doors", setd);
                                // if create_worlds is true, set the world spawn
                                if (own_world) {
                                    if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                                        Plugin mvplugin = plugin.pm.getPlugin("Multiverse-Core");
                                        if (mvplugin instanceof MultiverseCore) {
                                            MultiverseCore mvc = (MultiverseCore) mvplugin;
                                            MultiverseWorld foundWorld = mvc.getMVWorldManager().getMVWorld(world.getName());
                                            Location spawn = new Location(world, (startx + 0.5), starty, (startz + 1.5), 0, 0);
                                            foundWorld.setSpawnLocation(spawn);
                                        }
                                    } else {
                                        world.setSpawnLocation(startx, starty, (startz + 1));
                                    }
                                }
                            }
                            if (id == 68) { // chameleon circuit sign
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                String chameleonloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setc.put("chameleon", chameleonloc);
                                setc.put("chamele_on", 0);
                                wherec.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setc, wherec);
                            }
                            if (id == 52) { // scanner button
                                /*
                                 * mob spawner will be converted to the correct id by
                                 * setBlock(), but remember it for the scanner.
                                 */
                                HashMap<String, Object> setscan = new HashMap<String, Object>();
                                HashMap<String, Object> wherescan = new HashMap<String, Object>();
                                String scanloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setscan.put("scanner", scanloc);
                                wherescan.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setscan, wherescan);
                            }
                            if (id == 97) { // silverfish stone -> save sign
                                HashMap<String, Object> setss = new HashMap<String, Object>();
                                HashMap<String, Object> wheress = new HashMap<String, Object>();
                                String ssloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setss.put("save_sign", ssloc);
                                wheress.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setss, wheress);
                            }
                            if (id == 137 || id == -119) {
                                /*
                                 * command block - remember it to spawn the creeper on.
                                 */
                                HashMap<String, Object> setcreep = new HashMap<String, Object>();
                                HashMap<String, Object> wherecreep = new HashMap<String, Object>();
                                String creeploc = world.getName() + ":" + (startx + 0.5) + ":" + starty + ":" + (startz + 0.5);
                                setcreep.put("creeper", creeploc);
                                wherecreep.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setcreep, wherecreep);
                            }
                            if (id == 92) {
                                /*
                                 * This block will be converted to a lever by
                                 * setBlock(), but remember it so we can use it as the handbrake!
                                 */
                                String handbrakeloc = plugin.utils.makeLocationStr(world, startx, starty, startz);
                                qf.insertControl(dbID, 0, handbrakeloc, 0);
                            }
                            if (id == 143 || id == -113) {
                                /*
                                 * wood button will be coverted to the correct id by
                                 * setBlock(), but remember it for the Artron Energy Capacitor.
                                 */
                                String woodbuttonloc = plugin.utils.makeLocationStr(world, startx, starty, startz);
                                qf.insertControl(dbID, 6, woodbuttonloc, 0);
                            }
                            if (id == 7) {
                                // remember bedrock location to block off the beacon light
                                HashMap<String, Object> setbeac = new HashMap<String, Object>();
                                HashMap<String, Object> wherebeac = new HashMap<String, Object>();
                                String bedrocloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setbeac.put("beacon", bedrocloc);
                                wherebeac.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setbeac, wherebeac);
                            }
                            if (id == 124) {
                                // remember lamp blocks
                                Block lamp = world.getBlockAt(startx, starty, startz);
                                lampblocks.add(lamp);
                                if (plugin.getConfig().getInt("malfunction") > 0) {
                                    // remember lamp block locations for malfunction
                                    HashMap<String, Object> setlb = new HashMap<String, Object>();
                                    String lloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                    setlb.put("tardis_id", dbID);
                                    setlb.put("location", lloc);
                                    qf.doInsert("lamps", setlb);
                                }
                            }
                            if (id == 35 && data == 1) {
                                switch (middle_id) {
                                    case 22:
                                        break;
                                    default:
                                        id = middle_id;
                                        data = middle_data;
                                }
                            }
                        } else {
                            id = plugin.utils.parseNum(tmp);
                            data = 0;
                        }
                        // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                        if (id == 41 || id == 42 || id == 57 || id == 133 || id == -123 || id == 138 || id == -118 || id == 152 || id == -104) {
                            HashMap<String, Object> setpb = new HashMap<String, Object>();
                            String loc = plugin.utils.makeLocationStr(world, startx, starty, startz);
                            setpb.put("tardis_id", dbID);
                            setpb.put("location", loc);
                            setpb.put("police_box", 0);
                            qf.doInsert("blocks", setpb);
                            plugin.protectBlockMap.put(loc, dbID);
                        }
                        // if it's the door, don't set it just remember its block then do it at the end
                        if (id == 71) {
                            postDoorBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 76) {
                            postTorchBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 68) {
                            postSignBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 97) {
                            postSaveBlock = world.getBlockAt(startx, starty, startz);
                        } else if (id == 19) {
                            int swap;
                            if (world.getWorldType().equals(WorldType.FLAT) || own_world || world.getName().equals("TARDIS_TimeVortex")) {
                                swap = 0;
                            } else {
                                swap = 1;
                            }
                            plugin.utils.setBlock(world, startx, starty, startz, swap, data);
                        } else {
                            plugin.utils.setBlock(world, startx, starty, startz, id, data);
                        }
                    }
                    startx += x;
                }
                startx = resetx;
                startz += z;
            }
            startz = resetz;
            starty += 1;
        }
        // put on the door and the redstone torches
        for (Map.Entry<Block, Byte> entry : postDoorBlocks.entrySet()) {
            Block pdb = entry.getKey();
            byte pddata = entry.getValue();
            pdb.setTypeIdAndData(71, pddata, true);
        }
        for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
            Block ptb = entry.getKey();
            byte ptdata = entry.getValue();
            ptb.setTypeIdAndData(76, ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            final Block psb = entry.getKey();
            byte psdata = entry.getValue();
            psb.setTypeIdAndData(68, psdata, true);
            if (psb.getType().equals(Material.WALL_SIGN)) {
                Sign cs = (Sign) psb.getState();
                cs.setLine(0, "Chameleon");
                cs.setLine(1, "Circuit");
                cs.setLine(3, ChatColor.RED + "OFF");
                cs.update();
            }
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", dbID);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            if (postSaveBlock != null) {
                postSaveBlock.setTypeIdAndData(68, (byte) 3, true);
                if (postSaveBlock.getType().equals(Material.WALL_SIGN)) {
                    Sign ss = (Sign) postSaveBlock.getState();
                    ss.setLine(0, "TARDIS");
                    ss.setLine(1, "Saved");
                    ss.setLine(2, "Locations");
                    ss.setLine(3, "");
                    ss.update();
                }
            }
            if (schematicHasChest && bonus_chest && !own_world) {
                // get rid of last ":" and assign ids to an array
                String rb = sb.toString();
                replacedBlocks = rb.substring(0, rb.length() - 1);
                String[] replaceddata = replacedBlocks.split(":");
                // get saved chest location
                String saved_chestloc = rs.getChest();
                String[] cdata = saved_chestloc.split(":");
                World cw = plugin.getServer().getWorld(cdata[0]);
                cx = plugin.utils.parseNum(cdata[1]);
                cy = plugin.utils.parseNum(cdata[2]);
                cz = plugin.utils.parseNum(cdata[3]);
                Location chest_loc = new Location(cw, cx, cy, cz);
                Block the_chest = chest_loc.getBlock();
                if (the_chest.getType() == Material.CHEST) {
                    Chest chest = (Chest) the_chest.getState();
                    // get chest inventory
                    Inventory chestInv = chest.getInventory();
                    // convert non-smeltable ores to items
                    for (String i : replaceddata) {
                        rid = plugin.utils.parseNum(i);
                        switch (rid) {
                            case 1: // stone to cobblestone
                                rid = 4;
                                break;
                            case 16: // coal ore to coal
                                rid = 263;
                                break;
                            case 21: // lapis ore to lapis dye
                                rid = 351;
                                multiplier = 4;
                                damage = 4;
                                break;
                            case 56: // diamond ore to diamonds
                                rid = 264;
                                break;
                            case 73: // redstone ore to redstone dust
                                rid = 331;
                                multiplier = 4;
                                break;
                            case 129: // emerald ore to emerald
                                rid = 388;
                                break;
                            default:
                                break;
                        }
                        // add items to chest
                        chestInv.addItem(new ItemStack(rid, multiplier, damage));
                        multiplier = 1; // reset multiplier
                        damage = 0; // reset damage
                    }
                }
            }
        } else {
            plugin.console.sendMessage(plugin.pluginName + "Could not find chest location in DB!");
        }
        for (Block lamp : lampblocks) {
            lamp.setType(Material.REDSTONE_LAMP_ON);
        }
        lampblocks.clear();
        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
            plugin.wgchk.addWGProtection(p, wg1, wg2);
        }
    }
}
