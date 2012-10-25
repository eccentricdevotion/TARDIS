package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.Inventory;

public class TARDISDestroyer {

    private final TARDIS plugin;
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISDestroyer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void destroyTARDIS(Constants.SCHEMATIC schm, int id, World w, Constants.COMPASS d, int i) {
        short h, width, l;
        switch (schm) {
            case BIGGER:
            h = plugin.biggerdimensions[0];
            width = plugin.biggerdimensions[1];
            l = plugin.biggerdimensions[2];
            break;
            case DELUXE:
                h = plugin.deluxedimensions[0];
                width = plugin.deluxedimensions[1];
                l = plugin.deluxedimensions[2];
                break;
            default:
                h = plugin.budgetdimensions[0];
                width = plugin.budgetdimensions[1];
                l = plugin.budgetdimensions[2];
                break;
        }
        // inner TARDIS
        int level, row, col, x, y, z, startx, starty = (14+h), startz, resetx, resetz;
        // calculate startx, starty, startz
        TARDISUtils utils = new TARDISUtils(plugin);
        int gsl[] = utils.getStartLocation(id, d);
        startx = gsl[0];
        resetx = gsl[1];
        startz = gsl[2];
        resetz = gsl[3];
        x = gsl[4];
        z = gsl[5];
        for (level = 0; level < h; level++) {
            for (row = 0; row < width; row++) {
                for (col = 0; col < l; col++) {
                    // set the block to stone
                    Block b = w.getBlockAt(startx, starty, startz);
                    Material m = b.getType();
                    // if it's a chest clear the inventory first
                    if (m == Material.CHEST) {
                        Chest che = (Chest) b.getState();
                        Inventory inv = che.getBlockInventory();
                        inv.clear();
                    }
                    // if it's a furnace clear the inventory first
                    if (m == Material.FURNACE) {
                        Furnace fur = (Furnace) b.getState();
                        fur.getInventory().clear();
                    }
                    if (m != Material.CHEST) {
                        utils.setBlock(w, startx, starty, startz, i, (byte) 0);
                    }
                    switch (d) {
                        case NORTH:
                        case SOUTH:
                            startx += x;
                            break;
                        case EAST:
                        case WEST:
                            startz += z;
                            break;
                    }
                }
                switch (d) {
                    case NORTH:
                    case SOUTH:
                        startx = resetx;
                        startz += z;
                        break;
                    case EAST:
                    case WEST:
                        startz = resetz;
                        startx += x;
                        break;
                }
            }
            switch (d) {
                case NORTH:
                case SOUTH:
                    startz = resetz;
                    break;
                case EAST:
                case WEST:
                    startx = resetx;
                    break;
            }
            starty -= 1;
        }
    }

    public void destroyBlueBox(Location l, Constants.COMPASS d, int id) {
        World w = l.getWorld();
        int sbx = l.getBlockX() - 1;
        int rbx = sbx;
        int gbx = sbx;
        int sby = l.getBlockY();
        int sbywool = l.getBlockY() - 3;
        int sbz = l.getBlockZ() - 1;
        int rbz = sbz;
        int gbz = sbz;
        // remove blue wool and door
        TARDISUtils utils = new TARDISUtils(plugin);
        for (int yy = 0; yy < 3; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    utils.setBlock(w, sbx, sby, sbz, 0, (byte) 0);
                    sbx++;
                }
                sbx = rbx;
                sbz++;
            }
            sbz = rbz;
            sby++;
        }
        // replace the block under the door if there is one
        try {
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String queryReplaced = "SELECT replaced FROM tardis WHERE tardis_id = '" + id + "' LIMIT 1";
            ResultSet rs = statement.executeQuery(queryReplaced);
            if (rs.next()) {
                String replacedData = rs.getString("replaced");
                if (!replacedData.equals("") && replacedData != null) {
                    String[] parts = replacedData.split(":");
                    World rw = plugin.getServer().getWorld(parts[0]);
                    int rx = 0, ry = 0, rz = 0, rID = 0;
                    byte rb = 0;
                    try {
                        rx = Integer.valueOf(parts[1]);
                        ry = Integer.valueOf(parts[2]);
                        rz = Integer.valueOf(parts[3]);
                        rID = Integer.valueOf(parts[4]);
                        rb = Byte.valueOf(parts[5]);
                    } catch (NumberFormatException nfe) {
                        System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number!");
                    }
                    Block b = rw.getBlockAt(rx, ry, rz);
                    b.setTypeIdAndData(rID, rb, true);
                }
            }
            // finally forget the replaced block
            String queryForget = "UPDATE tardis SET replaced = '' WHERE tardis_id = " + id;
            statement.executeUpdate(queryForget);

            // get rid of platform is there is one
            if (plugin.config.getBoolean("platform") == Boolean.valueOf("true")) {
                String queryPlatform = "SELECT platform FROM tardis WHERE tardis_id = " + id;
                ResultSet prs = statement.executeQuery(queryPlatform);
                if (prs.next()) {
                    if (!prs.getString("platform").equals("[Null]") && !prs.getString("platform").equals("") && prs.getString("platform") != null) {
                        int px = 0, py = 0, pz = 0;
                        String[] str_blocks = prs.getString("platform").split("~");
                        for (String sb : str_blocks) {
                            String[] p_data = sb.split(":");
                            World pw = plugin.getServer().getWorld(p_data[0]);
                            Material mat = Material.valueOf(p_data[4]);
                            try {
                                px = Integer.valueOf(p_data[1]);
                                py = Integer.valueOf(p_data[2]);
                                pz = Integer.valueOf(p_data[3]);
                            } catch (NumberFormatException nfe) {
                                System.err.println(Constants.MY_PLUGIN_NAME + "Could not convert to number!");
                            }
                            Block pb = pw.getBlockAt(px, py, pz);
                            pb.setType(mat);
                        }
                    }
                    // forget the platform blocks
                    String queryEmptyP = "UPDATE tardis SET platform = '' WHERE tardis_id = " + id;
                    statement.executeUpdate(queryEmptyP);
                }
                prs.close();
            }
            // remove protected blocks from the blocks table
            String queryRemoveBlocks = "DELETE FROM blocks WHERE tardis_id = " + id;
            statement.executeUpdate(queryRemoveBlocks);
            statement.close();
        } catch (SQLException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " Save Replaced Block Error: " + e);
        }
    }

    public void destroySign(Location l, Constants.COMPASS d) {
        TARDISUtils utils = new TARDISUtils(plugin);
        World w = l.getWorld();
        int signx = 0, signz = 0;
        switch (d) {
            case EAST:
                signx = -2;
                signz = 0;
                break;
            case SOUTH:
                signx = 0;
                signz = -2;
                break;
            case WEST:
                signx = 2;
                signz = 0;
                break;
            case NORTH:
                signx = 0;
                signz = 2;
                break;
        }
        int signy = 2;
        utils.setBlock(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz, 0, (byte) 0);
    }

    public void destroyTorch(Location l) {
        TARDISUtils utils = new TARDISUtils(plugin);
        World w = l.getWorld();
        int tx = l.getBlockX();
        int ty = l.getBlockY() + 3;
        int tz = l.getBlockZ();
        utils.setBlock(w, tx, ty, tz, 0, (byte) 0);
    }
}
