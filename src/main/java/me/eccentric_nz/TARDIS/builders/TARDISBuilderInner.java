package me.eccentric_nz.TARDIS.builders;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInner {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    Statement statement;

    public TARDISBuilderInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be DEFAULT, BIGGER
     * or DELUXE.
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
        short h, w, l;
        switch (schm) {
            case BIGGER:
                s = plugin.biggerschematic;
                h = plugin.biggerdimensions[0];
                w = plugin.biggerdimensions[1];
                l = plugin.biggerdimensions[2];
                break;
            // Deluxe TARDIS schematic supplied by ewized http://dev.bukkit.org/profiles/ewized/
            case DELUXE:
                s = plugin.deluxeschematic;
                h = plugin.deluxedimensions[0];
                w = plugin.deluxedimensions[1];
                l = plugin.deluxedimensions[2];
                break;
            default:
                s = plugin.budgetschematic;
                h = plugin.budgetdimensions[0];
                w = plugin.budgetdimensions[1];
                l = plugin.budgetdimensions[2];
                break;
        }
        int level, row, col, id, x, y, z, startx, starty = 15, startz, resetx, resetz, cx, cy, cz, rid, multiplier = 1, tx = 0, ty = 0, tz = 0, j = 0;
        byte data;
        short damage = 0;
        String tmp, replacedBlocks;
        HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
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
        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < l; col++) {
                    Location replaceLoc = new Location(world, startx, starty, startz);
                    // get list of used chunks
                    Chunk thisChunk = world.getChunkAt(replaceLoc);
                    if (!chunkList.contains(thisChunk)) {
                        chunkList.add(thisChunk);
                    }
                    if (plugin.getConfig().getBoolean("bonus_chest")) {
                        // get block at location
                        int replacedMaterialId = replaceLoc.getBlock().getTypeId();
                        if (replacedMaterialId != 8 && replacedMaterialId != 9 && replacedMaterialId != 10 && replacedMaterialId != 11) {
                            sb.append(replacedMaterialId).append(":");
                        }
                    }
                    plugin.utils.setBlock(world, startx, starty, startz, 0, (byte) 0);
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
                                // remember the location of this chest
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                String chest = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setc.put("chest", chest);
                                wherec.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setc, wherec);
                            }
                            if (id == 77) { // stone button
                                // remember the location of this button
                                HashMap<String, Object> setb = new HashMap<String, Object>();
                                HashMap<String, Object> whereb = new HashMap<String, Object>();
                                String button = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setb.put("button", button);
                                whereb.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setb, whereb);
                            }
                            if (id == 93) { // remember the location of this redstone repeater
                                // save repeater location
                                HashMap<String, Object> setr = new HashMap<String, Object>();
                                HashMap<String, Object> wherer = new HashMap<String, Object>();
                                String repeater = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                setr.put("button" + j, repeater);
                                wherer.put("tardis_id", dbID);
                                qf.doUpdate("tardis", setr, wherer);
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
                        // if its the door, don't set it just remember its block then do it at the end
                        if (id == 71) {
                            postDoorBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 76) {
                            postTorchBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 68) {
                            postSignBlocks.put(world.getBlockAt(startx, starty, startz), data);
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
            byte pddata = Byte.valueOf(entry.getValue());
            pdb.setTypeIdAndData(71, pddata, true);
        }
        for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
            Block ptb = entry.getKey();
            byte ptdata = Byte.valueOf(entry.getValue());
            ptb.setTypeIdAndData(76, ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            Block psb = entry.getKey();
            byte psdata = Byte.valueOf(entry.getValue());
            psb.setTypeIdAndData(68, psdata, true);
            Sign cs = (Sign) psb.getState();
            cs.setLine(0, "Chameleon");
            cs.setLine(1, "Circuit");
            cs.setLine(3, ChatColor.RED + "OFF");
            cs.update();
        }
        if (plugin.getConfig().getBoolean("bonus_chest")) {
            // get rid of last ":" and assign ids to an array
            String rb = sb.toString();
            replacedBlocks = rb.substring(0, rb.length() - 1);
            String[] replaceddata = replacedBlocks.split(":");
            // get saved chest location
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", dbID);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                String saved_chestloc = rs.getChest();
                String[] cdata = saved_chestloc.split(":");
                World cw = plugin.getServer().getWorld(cdata[0]);
                cx = plugin.utils.parseNum(cdata[1]);
                cy = plugin.utils.parseNum(cdata[2]);
                cz = plugin.utils.parseNum(cdata[3]);
                Location chest_loc = new Location(cw, cx, cy, cz);
                Block bonus_chest = chest_loc.getBlock();
                Chest chest = (Chest) bonus_chest.getState();
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
                    }
                    // add items to chest
                    chestInv.addItem(new ItemStack(rid, multiplier, damage));
                    multiplier = 1; // reset multiplier
                    damage = 0; // reset damage
                }
            } else {
                plugin.console.sendMessage(plugin.pluginName + " Could not find chest location in DB!");
            }
        }
        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
            plugin.wgchk.addWGProtection(p, wg1, wg2);
        }
    }
}