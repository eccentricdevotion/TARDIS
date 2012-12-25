package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class TARDISBuilderOuter {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    Statement statement;

    public TARDISBuilderOuter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void buildOuter(int id, Location l, TARDISConstants.COMPASS d, boolean c, Player p, boolean rebuild) {
        int plusx, minusx, x, plusz, minusz, z, wall_block = 35;
        byte sd = 0, norm = 0, grey = 8, blue = 11, chameleonData = 11;
        if (c) {
            Block chameleonBlock;
            // chameleon circuit is on - get block under TARDIS
            if (l.getBlock().getType() == Material.SNOW) {
                chameleonBlock = l.getBlock();
            } else {
                chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
            }
            int chameleonType = chameleonBlock.getTypeId();
            // determine wall_block
            if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) chameleonType)) {
                wall_block = chameleonType;
                chameleonData = chameleonBlock.getData();
            }
            if (TARDISConstants.CHAMELEON_BLOCKS_BAD.contains((Integer) chameleonType)) {
                p.sendMessage(plugin.pluginName + " Bummer, the TARDIS could not engage the Chameleon Circuit!");
            }
            if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) chameleonType)) {
                wall_block = TARDISConstants.swapId(chameleonType);
                switch (chameleonType) {
                    case 22:
                        chameleonData = 11;
                        break;
                    case 41:
                        chameleonData = 4;
                        break;
                    case 42:
                        chameleonData = 8;
                        break;
                    case 57:
                        chameleonData = 3;
                        break;
                    case 133:
                        chameleonData = 5;
                        break;
                    case 134:
                        chameleonData = 1;
                        break;
                    case 135:
                        chameleonData = 2;
                        break;
                    case 136:
                        chameleonData = 3;
                        break;
                    default:
                        chameleonData = chameleonBlock.getData();
                        break;
                }
            }

            if (TARDISConstants.CHAMELEON_BLOCKS_NEXT.contains((Integer) chameleonType)) {
                List<BlockFace> surrounding = Arrays.asList(new BlockFace[]{BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST});
                // try the surrounding blocks
                for (BlockFace bf : surrounding) {
                    Block surroundblock = chameleonBlock.getRelative(bf);
                    int eid = surroundblock.getTypeId();
                    if (TARDISConstants.CHAMELEON_BLOCKS_VALID.contains((Integer) eid)) {
                        wall_block = eid;
                        chameleonData = surroundblock.getData();
                        break;
                    }
                    if (TARDISConstants.CHAMELEON_BLOCKS_CHANGE.contains((Integer) eid)) {
                        wall_block = TARDISConstants.swapId(eid);
                        switch (eid) {
                            case 134:
                                chameleonData = 1;
                                break;
                            case 135:
                                chameleonData = 2;
                                break;
                            case 136:
                                chameleonData = 3;
                                break;
                            default:
                                chameleonData = chameleonBlock.getData();
                                break;
                        }
                        break;
                    }
                }
            }
        }
        byte mds = chameleonData, mdw = chameleonData, mdn = chameleonData, mde = chameleonData, bds = chameleonData, bdw = chameleonData, bdn = chameleonData, bde = chameleonData;
        final World world;
        // expand placed blocks to a police box
        double lowX = l.getX();
        double lowY = l.getY();
        double lowZ = l.getZ();
        l.setX(lowX + 0.5);
        l.setY(lowY + 2);
        l.setZ(lowZ + 0.5);
        // get relative locations
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        final int y = l.getBlockY();
        final int plusy = (l.getBlockY() + 1), minusy = (l.getBlockY() - 1), down2y = (l.getBlockY() - 2), down3y = (l.getBlockY() - 3);
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        world = l.getWorld();
        int south = wall_block, west = wall_block, north = wall_block, east = wall_block, signx = 0, signz = 0;
        String doorloc = "";

        try {
            Connection connection = service.getConnection();
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO blocks (tardis_id, location) VALUES (?,?)");
            // also remember the block under the door
            String underdoor;

            // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
            switch (d) {
                case SOUTH:
                    //if (yaw >= 315 || yaw < 45)
                    plugin.utils.setBlockCheck(world, x, down3y, minusz, 35, grey, id); // door is here if player facing south
                    ps.setString(2, world.getBlockAt(x, down3y, minusz).getLocation().toString());
                    doorloc = world.getName() + ":" + x + ":" + down2y + ":" + minusz;
                    sd = 2;
                    signx = x;
                    signz = (minusz - 1);
                    south = 71;
                    mds = 8;
                    bds = 1;
                    break;
                case EAST:
                    //if (yaw >= 225 && yaw < 315)
                    plugin.utils.setBlockCheck(world, minusx, down3y, z, 35, grey, id); // door is here if player facing east
                    ps.setString(2, world.getBlockAt(minusx, down3y, z).getLocation().toString());
                    doorloc = world.getName() + ":" + minusx + ":" + down2y + ":" + z;
                    sd = 4;
                    signx = (minusx - 1);
                    signz = z;
                    east = 71;
                    mde = 8;
                    bde = 0;
                    break;
                case NORTH:
                    //if (yaw >= 135 && yaw < 225)
                    plugin.utils.setBlockCheck(world, x, down3y, plusz, 35, grey, id); // door is here if player facing north
                    ps.setString(2, world.getBlockAt(x, down3y, plusz).getLocation().toString());
                    doorloc = world.getName() + ":" + x + ":" + down2y + ":" + plusz;
                    sd = 3;
                    signx = x;
                    signz = (plusz + 1);
                    north = 71;
                    mdn = 8;
                    bdn = 3;
                    break;
                case WEST:
                    //if (yaw >= 45 && yaw < 135)
                    plugin.utils.setBlockCheck(world, plusx, down3y, z, 35, grey, id); // door is here if player facing west
                    ps.setString(2, world.getBlockAt(plusx, down3y, z).getLocation().toString());
                    doorloc = world.getName() + ":" + plusx + ":" + down2y + ":" + z;
                    sd = 5;
                    signx = (plusx + 1);
                    signz = z;
                    west = 71;
                    mdw = 8;
                    bdw = 2;
                    break;
            }
            ps.setInt(1, id);
            ps.executeUpdate();
            // should insert the door when tardis is first made, and then update location there after!
            String queryInsertOrUpdate = "SELECT door_id FROM doors WHERE door_type = 0 AND tardis_id = " + id;
            ResultSet rs = statement.executeQuery(queryInsertOrUpdate);
            String queryDoor;
            if (rs.next()) {
                queryDoor = "UPDATE doors SET door_location = '" + doorloc + "' WHERE door_id = " + rs.getInt("door_id");
            } else {
                queryDoor = "INSERT INTO doors (tardis_id, door_type, door_location) VALUES (" + id + ", 0, '" + doorloc + "')";
            }
            rs.close();
            statement.executeUpdate(queryDoor);

            // bottom layer corners
            plugin.utils.setBlockAndRemember(world, plusx, down2y, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, down2y, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, down2y, minusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, plusx, down2y, minusz, wall_block, chameleonData, id, rebuild);
            // middle layer corners
            plugin.utils.setBlockAndRemember(world, plusx, minusy, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, minusy, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, minusy, minusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, plusx, minusy, minusz, wall_block, chameleonData, id, rebuild);
            // top layer
            switch (wall_block) {
                case 18:
                    plugin.utils.setBlockAndRemember(world, x, y, z, 17, chameleonData, id, rebuild);
                    break;
                case 46:
                    plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 14, id, rebuild);
                    break;
                case 79:
                    plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 3, id, rebuild);
                    break;
                case 89:
                    plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 4, id, rebuild);
                    break;
                default:
                    plugin.utils.setBlockAndRemember(world, x, y, z, wall_block, chameleonData, id, rebuild);
                    break;
            }
            plugin.utils.setBlockAndRemember(world, plusx, y, z, wall_block, chameleonData, id, rebuild); // east
            plugin.utils.setBlockAndRemember(world, plusx, y, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, y, plusz, wall_block, chameleonData, id, rebuild); // south
            plugin.utils.setBlockAndRemember(world, minusx, y, plusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, y, z, wall_block, chameleonData, id, rebuild); // west
            plugin.utils.setBlockAndRemember(world, minusx, y, minusz, wall_block, chameleonData, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, y, minusz, wall_block, chameleonData, id, rebuild); // north
            plugin.utils.setBlockAndRemember(world, plusx, y, minusz, wall_block, chameleonData, id, rebuild);
            // set sign
            plugin.utils.setBlock(world, signx, y, signz, 68, sd);
            Sign s = (Sign) world.getBlockAt(signx, y, signz).getState();
            if (plugin.getConfig().getBoolean("name_tardis")) {
                String queryGetOwner = "SELECT owner FROM tardis WHERE tardis_id = '" + id + "'";
                ResultSet rsOwner = statement.executeQuery(queryGetOwner);
                String owner = rsOwner.getString("owner");
                if (owner.length() >= 15) {
                    s.setLine(0, owner.substring(0, 13) + "'s");
                } else {
                    s.setLine(0, owner + "'s");
                }
            }
            s.setLine(1, ChatColor.WHITE + "POLICE");
            s.setLine(2, ChatColor.WHITE + "BOX");
            s.update();
            // put torch on top
            if (wall_block == 79) {
                plugin.utils.setBlockAndRemember(world, x, plusy, z, 76, (byte) 5, id, rebuild);
            } else {
                plugin.utils.setBlockAndRemember(world, x, plusy, z, 50, (byte) 5, id, rebuild);
            }
            // remove the IRON & LAPIS blocks
            plugin.utils.setBlock(world, x, minusy, z, 0, norm);
            plugin.utils.setBlock(world, x, down2y, z, 0, norm);
            // bottom layer with door bottom
            plugin.utils.setBlockAndRemember(world, plusx, down2y, z, west, bdw, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, down2y, plusz, north, bdn, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, down2y, z, east, bde, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, down2y, minusz, south, bds, id, rebuild);
            // middle layer with door top
            plugin.utils.setBlockAndRemember(world, plusx, minusy, z, west, mdw, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, minusy, plusz, north, mdn, id, rebuild);
            plugin.utils.setBlockAndRemember(world, minusx, minusy, z, east, mde, id, rebuild);
            plugin.utils.setBlockAndRemember(world, x, minusy, minusz, south, mds, id, rebuild);
            // add platform if configured and necessary
            if (plugin.getConfig().getBoolean("platform")) {
                // check if user has platform pref
                String queryGetPlatform = "SELECT platform_on FROM player_prefs WHERE player = '" + p.getName() + "'";
                ResultSet rsPlatform = statement.executeQuery(queryGetPlatform);
                boolean userPlatform;
                if (rsPlatform.next()) {
                    userPlatform = rsPlatform.getBoolean("platform_on");
                } else {
                    userPlatform = true;
                }
                rsPlatform.close();
                if (userPlatform) {
                    List<Block> platform_blocks = null;
                    switch (d) {
                        case SOUTH:
                            platform_blocks = Arrays.asList(world.getBlockAt(x - 1, down3y, minusz - 1), world.getBlockAt(x, down3y, minusz - 1), world.getBlockAt(x + 1, down3y, minusz - 1), world.getBlockAt(x - 1, down3y, minusz - 2), world.getBlockAt(x, down3y, minusz - 2), world.getBlockAt(x + 1, down3y, minusz - 2));
                            break;
                        case EAST:
                            platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, down3y, z - 1), world.getBlockAt(minusx - 1, down3y, z), world.getBlockAt(minusx - 1, down3y, z + 1), world.getBlockAt(minusx - 2, down3y, z - 1), world.getBlockAt(minusx - 2, down3y, z), world.getBlockAt(minusx - 2, down3y, z + 1));
                            break;
                        case NORTH:
                            platform_blocks = Arrays.asList(world.getBlockAt(x + 1, down3y, plusz + 1), world.getBlockAt(x, down3y, plusz + 1), world.getBlockAt(x - 1, down3y, plusz + 1), world.getBlockAt(x + 1, down3y, plusz + 2), world.getBlockAt(x, down3y, plusz + 2), world.getBlockAt(x - 1, down3y, plusz + 2));
                            break;
                        case WEST:
                            platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, down3y, z + 1), world.getBlockAt(plusx + 1, down3y, z), world.getBlockAt(plusx + 1, down3y, z - 1), world.getBlockAt(plusx + 2, down3y, z + 1), world.getBlockAt(plusx + 2, down3y, z), world.getBlockAt(plusx + 2, down3y, z - 1));
                            break;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Block pb : platform_blocks) {
                        Material mat = pb.getType();
                        if (mat == Material.AIR || mat == Material.STATIONARY_WATER || mat == Material.WATER || mat == Material.VINE || mat == Material.RED_MUSHROOM || mat == Material.BROWN_MUSHROOM || mat == Material.LONG_GRASS || mat == Material.SAPLING || mat == Material.DEAD_BUSH || mat == Material.RED_ROSE || mat == Material.YELLOW_FLOWER || mat == Material.SNOW) {
                            plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), 35, grey, id, rebuild);
                            String p_tmp = world.getName() + ":" + pb.getX() + ":" + pb.getY() + ":" + pb.getZ() + ":" + mat.toString();
                            sb.append(p_tmp).append("~");
                        }
                    }
                    String recall = sb.toString();
                    String platform_recall = "";
                    if (recall.length() > 0) {
                        platform_recall = recall.substring(0, recall.length() - 1);
                    }
                    String queryPlatform = "UPDATE tardis SET platform = '" + platform_recall + "' WHERE tardis_id = " + id;
                    statement.executeUpdate(queryPlatform);
                }
            }
            statement.close();
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + " Door Insert Error: " + e);
        }
    }
}