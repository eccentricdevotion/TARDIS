package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TARDISPlayerListener implements Listener {

    private TARDIS plugin;
    float[][] adjustYaw = new float[4][4];
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISPlayerListener(TARDIS plugin) {
        this.plugin = plugin;
        // yaw adjustments if inner and outer door directions are different
        adjustYaw[0][0] = 0;
        adjustYaw[0][1] = -90;
        adjustYaw[0][2] = 180;
        adjustYaw[0][3] = 90;
        adjustYaw[1][0] = 90;
        adjustYaw[1][1] = 0;
        adjustYaw[1][2] = -90;
        adjustYaw[1][3] = 180;
        adjustYaw[2][0] = 180;
        adjustYaw[2][1] = 90;
        adjustYaw[2][2] = 0;
        adjustYaw[2][3] = -90;
        adjustYaw[3][0] = -90;
        adjustYaw[3][1] = 180;
        adjustYaw[3][2] = 90;
        adjustYaw[3][3] = 0;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        int savedx = 0, savedy = 0, savedz = 0, cx = 0, cy = 0, cz = 0;

        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (plugin.trackPlayers.containsKey(playerNameStr)) {
                String blockName = plugin.trackPlayers.get(playerNameStr);
                Location block_loc = block.getLocation();
                String bw = block_loc.getWorld().getName();
                int bx = block_loc.getBlockX();
                int by = block_loc.getBlockY();
                int bz = block_loc.getBlockZ();
                byte blockData = block.getData();
                if (blockData >= 8 && blockType == Material.IRON_DOOR_BLOCK) {
                    by = (by - 1);
                }
                String blockLocStr = bw + ":" + bx + ":" + by + ":" + bz;
                plugin.trackPlayers.remove(playerNameStr);
                try {
                    int id;
                    String queryBlockUpdate = "";
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rs = service.getTardis(playerNameStr, "tardis_id");
                    if (rs.next()) {
                        id = rs.getInt("tardis_id");
                        rs.close();
                        if (blockName.equalsIgnoreCase("door") && blockType == Material.IRON_DOOR_BLOCK) {
                            // get door data this should let us determine the direction
                            String d = "EAST";
                            switch (blockData) {
                                case 0:
                                    d = "EAST";
                                    break;
                                case 1:
                                    d = "SOUTH";
                                    break;
                                case 2:
                                    d = "WEST";
                                    break;
                                case 3:
                                    d = "NORTH";
                                    break;
                            }
                            queryBlockUpdate = "UPDATE doors SET door_location = '" + blockLocStr + "', door_direction = '" + d + "' WHERE door_type = 1 AND tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("button") && blockType == Material.STONE_BUTTON) {
                            queryBlockUpdate = "UPDATE tardis SET button = '" + blockLocStr + "' WHERE tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("save-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                            queryBlockUpdate = "UPDATE tardis SET repeater0 = '" + blockLocStr + "' WHERE tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("x-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                            queryBlockUpdate = "UPDATE tardis SET repeater1 = '" + blockLocStr + "' WHERE tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("z-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                            queryBlockUpdate = "UPDATE tardis SET repeater2 = '" + blockLocStr + "' WHERE tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("y-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                            queryBlockUpdate = "UPDATE tardis SET repeater3 = '" + blockLocStr + "' WHERE tardis_id = " + id;
                        }
                        if (blockName.equalsIgnoreCase("chameleon") && (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST)) {
                            queryBlockUpdate = "UPDATE tardis SET chameleon = '" + blockLocStr + "', chamele_on = 0 WHERE tardis_id = " + id;
                            // add text to sign
                            Sign s = (Sign) block.getState();
                            s.setLine(0, "Chameleon");
                            s.setLine(1, "Circuit");
                            s.setLine(3, "¤cOFF");
                            s.update();
                        }
                        statement.executeUpdate(queryBlockUpdate);
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The position of the TARDIS " + blockName + " was updated successfully.");
                    } else {
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " There was a problem updating the position of the TARDIS " + blockName + "!");
                    }
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + " Create table error: " + e);
                }
            } else if (plugin.trackName.containsKey(playerNameStr) && !plugin.trackBlock.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                String locStr = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockZ();
                plugin.trackBlock.put(playerNameStr, locStr);
                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " You have 60 seconds to select the area end block - use the " + ChatColor.GREEN + "/TARDIS admin area end" + ChatColor.RESET + " command.");
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.trackName.remove(playerNameStr);
                        if (plugin.trackGroup.containsKey(playerNameStr)) {
                            plugin.trackGroup.remove(playerNameStr);
                        }
                        plugin.trackFlag.remove(playerNameStr);
                        plugin.trackBlock.remove(playerNameStr);
                    }
                }, 1200L);
            } else if (plugin.trackBlock.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                String[] firstblock = plugin.trackBlock.get(playerNameStr).split(":");
                if (!block_loc.getWorld().getName().equals(firstblock[0])) {
                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " Area start and end blocks must be in the same world! Try again");
                }
                TARDISUtils utils = new TARDISUtils(plugin);
                int minx, minz, maxx, maxz;
                if (utils.parseNum(firstblock[1]) < block_loc.getBlockX()) {
                    minx = utils.parseNum(firstblock[1]);
                    maxx = block_loc.getBlockX();
                } else {
                    minx = block_loc.getBlockX();
                    maxx = utils.parseNum(firstblock[1]);
                }
                if (utils.parseNum(firstblock[2]) < block_loc.getBlockZ()) {
                    minz = utils.parseNum(firstblock[2]);
                    maxz = block_loc.getBlockZ();
                } else {
                    minz = block_loc.getBlockZ();
                    maxz = utils.parseNum(firstblock[2]);
                }
                String n = plugin.trackName.get(playerNameStr);
                int f = utils.parseNum(plugin.trackFlag.get(playerNameStr));
                String queryArea = "INSERT INTO areas (area_name, area_type, ";
                if (plugin.trackGroup.containsKey(playerNameStr)) {
                    queryArea += "area_group, world, minx, minz, maxx, maxz) VALUES ('" + n + "'," + f + ",'" + plugin.trackGroup.get(playerNameStr) + "'";
                } else {
                    queryArea += "world, minx, minz, maxx, maxz) VALUES ('" + n + "'," + f;
                }
                queryArea += ",'" + firstblock[0] + "'," + minx + "," + minz + "," + maxx + "," + maxz + ")";
                try {
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(queryArea);
                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The area [" + plugin.trackName.get(playerNameStr) + "] was saved successfully");
                    plugin.trackName.remove(playerNameStr);
                    if (plugin.trackGroup.containsKey(playerNameStr)) {
                        plugin.trackGroup.remove(playerNameStr);
                    }
                    plugin.trackFlag.remove(playerNameStr);
                    plugin.trackBlock.remove(playerNameStr);
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + " Create table error: " + e);
                }
            } else {
                Action action = event.getAction();
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    World playerWorld = player.getLocation().getWorld();
                    ItemStack stack = player.getItemInHand();
                    Material material = stack.getType();
                    // get key material from config
                    Material key = Material.getMaterial(plugin.config.getString("key"));
                    // only proceed if they are clicking an iron door with a TARDIS key!
                    if (blockType == Material.IRON_DOOR_BLOCK) {
                        if (material == key) {
                            if (block != null) {
                                if (player.hasPermission("TARDIS.enter")) {
                                    Location block_loc = block.getLocation();
                                    String bw = block_loc.getWorld().getName();
                                    int bx = block_loc.getBlockX();
                                    int by = block_loc.getBlockY();
                                    int bz = block_loc.getBlockZ();
                                    byte doorData = block.getData();
                                    if (doorData >= 8) {
                                        by = (by - 1);
                                    }
                                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                                    try {
                                        Connection connection = service.getConnection();
                                        Statement statement = connection.createStatement();
                                        String queryTardis = "SELECT tardis.*, doors.door_type, doors.door_direction FROM tardis, doors WHERE doors.door_location = '" + doorloc + "' AND doors.tardis_id = tardis.tardis_id";
                                        ResultSet rs = statement.executeQuery(queryTardis);
                                        if ((rs != null && rs.next())) {
                                            int id = rs.getInt("tardis_id");
                                            int doortype = rs.getInt("door_type");
                                            String d = rs.getString("direction");
                                            String dd = rs.getString("door_direction");
                                            String chunkstr = rs.getString("chunk");
                                            String tl = rs.getString("owner");
                                            String companions = rs.getString("companions");
                                            String save = rs.getString("save");
                                            String cl = rs.getString("current");
                                            boolean cham = rs.getBoolean("chamele_on");
                                            float yaw = player.getLocation().getYaw();
                                            float pitch = player.getLocation().getPitch();
                                            // get quotes player prefs
                                            String queryQuotes = "SELECT quotes_on FROM player_prefs WHERE player = '" + playerNameStr + "'";
                                            ResultSet rsQuotes = statement.executeQuery(queryQuotes);
                                            boolean userQuotes;
                                            if (rsQuotes.next()) {
                                                userQuotes = rsQuotes.getBoolean("quotes_on");
                                            } else {
                                                userQuotes = true;
                                            }
                                            if (doortype == 1) {
                                                // player is in the TARDIS
                                                // change the yaw if the door directions are different
                                                if (!dd.equals(d)) {
                                                    switch (Constants.COMPASS.valueOf(dd)) {
                                                        case NORTH:
                                                            yaw = yaw + adjustYaw[0][Constants.COMPASS.valueOf(d).ordinal()];
                                                            break;
                                                        case WEST:
                                                            yaw = yaw + adjustYaw[1][Constants.COMPASS.valueOf(d).ordinal()];
                                                            break;
                                                        case SOUTH:
                                                            yaw = yaw + adjustYaw[2][Constants.COMPASS.valueOf(d).ordinal()];
                                                            break;
                                                        case EAST:
                                                            yaw = yaw + adjustYaw[3][Constants.COMPASS.valueOf(d).ordinal()];
                                                            break;
                                                    }
                                                }
                                                // get location from database
                                                final Location exitTardis = Constants.getLocationFromDB(save, yaw, pitch);
                                                // make location safe ie. outside of the bluebox
                                                double ex = exitTardis.getX();
                                                double ez = exitTardis.getZ();
                                                double ey = exitTardis.getY();
                                                switch (Constants.COMPASS.valueOf(d)) {
                                                    case NORTH:
                                                        exitTardis.setX(ex + 0.5);
                                                        exitTardis.setZ(ez + 2.5);
                                                        break;
                                                    case EAST:
                                                        exitTardis.setX(ex - 1.5);
                                                        exitTardis.setZ(ez + 0.5);
                                                        break;
                                                    case SOUTH:
                                                        exitTardis.setX(ex + 0.5);
                                                        exitTardis.setZ(ez - 1.5);
                                                        break;
                                                    case WEST:
                                                        exitTardis.setX(ex + 2.5);
                                                        exitTardis.setZ(ez + 0.5);
                                                        break;
                                                }
                                                World exitWorld = exitTardis.getWorld();
                                                // destroy current TARDIS location
                                                Location newl = null;
                                                TARDISDestroyer destroyer = new TARDISDestroyer(plugin);
                                                if (!save.equals(cl)) {
                                                    Location l = Constants.getLocationFromDB(cl, 0, 0);
                                                    newl = Constants.getLocationFromDB(save, 0, 0);
                                                    // remove torch
                                                    destroyer.destroyTorch(l);
                                                    // remove sign
                                                    destroyer.destroySign(l, Constants.COMPASS.valueOf(d));
                                                    // remove blue box
                                                    destroyer.destroyBlueBox(l, Constants.COMPASS.valueOf(d), id);
                                                }
                                                // try preloading destination chunk
                                                while (!exitWorld.getChunkAt(exitTardis).isLoaded()) {
                                                    exitWorld.getChunkAt(exitTardis).load();
                                                }
                                                // rebuild blue box
                                                TARDISBuilder builder = new TARDISBuilder(plugin);
                                                if (newl != null) {
                                                    builder.buildOuterTARDIS(id, newl, Constants.COMPASS.valueOf(d), cham, player);
                                                }
                                                // exit TARDIS!
                                                tt(player, exitTardis, true, playerWorld, userQuotes);
                                                // remove player from traveller table
                                                String queryTraveller = "DELETE FROM travellers WHERE player = '" + playerNameStr + "'";
                                                statement.executeUpdate(queryTraveller);
                                            } else {
                                                boolean chkCompanion = false;
                                                boolean TLOnline = false;
                                                if (!playerNameStr.equals(tl)) {
                                                    if (plugin.getServer().getPlayer(tl) != null) {
                                                        if (companions != null && !companions.equals("") && !companions.equals("[Null]")) {
                                                            // is the timelord in the TARDIS?
                                                            String queryTraveller = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + tl + "' LIMIT 1";
                                                            ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                                            if (timelordIsIn != null && timelordIsIn.next()) {
                                                                // is the player in the comapnion list
                                                                String[] companionData = companions.split(":");
                                                                for (String c : companionData) {
                                                                    //String lc_name = c.toLowerCase();
                                                                    if (c.equalsIgnoreCase(playerNameStr)) {
                                                                        chkCompanion = true;
                                                                        break;
                                                                    }
                                                                }
                                                            } else {
                                                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.TIMELORD_NOT_IN);
                                                                TLOnline = true;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.TIMELORD_OFFLINE);
                                                        TLOnline = true;
                                                    }
                                                }
                                                if (playerNameStr.equals(tl) || chkCompanion == true) {
                                                    // get INNER TARDIS location
                                                    String queryInnerDoor = "SELECT * FROM doors WHERE door_type = 1 AND tardis_id = " + id;
                                                    ResultSet doorRS = statement.executeQuery(queryInnerDoor);
                                                    if (doorRS.next()) {
                                                        String innerD = doorRS.getString("door_direction");
                                                        String doorLocStr = doorRS.getString("door_location");
                                                        String[] split = doorLocStr.split(":");
                                                        World cw = plugin.getServer().getWorld(split[0]);
                                                        try {
                                                            cx = Integer.parseInt(split[1]);
                                                            cy = Integer.parseInt(split[2]);
                                                            cz = Integer.parseInt(split[3]);
                                                        } catch (NumberFormatException nfe) {
                                                            System.err.println(Constants.MY_PLUGIN_NAME + " Could not convert to number!");
                                                        }
                                                        Location tmp_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                                                        int getx = tmp_loc.getBlockX();
                                                        int getz = tmp_loc.getBlockZ();
                                                        String doorDirStr = doorRS.getString("door_direction");
                                                        switch (Constants.COMPASS.valueOf(doorDirStr)) {
                                                            case NORTH:
                                                                // z -ve
                                                                tmp_loc.setX(getx + 0.5);
                                                                tmp_loc.setZ(getz - 0.5);
                                                                break;
                                                            case EAST:
                                                                // x +ve
                                                                tmp_loc.setX(getx + 1.5);
                                                                tmp_loc.setZ(getz + 0.5);
                                                                break;
                                                            case SOUTH:
                                                                // z +ve
                                                                tmp_loc.setX(getx + 0.5);
                                                                tmp_loc.setZ(getz + 1.5);
                                                                break;
                                                            case WEST:
                                                                // x -ve
                                                                tmp_loc.setX(getx - 0.5);
                                                                tmp_loc.setZ(getz + 0.5);
                                                                break;
                                                        }
                                                        // enter TARDIS!
                                                        cw.getChunkAt(tmp_loc).load();
                                                        tmp_loc.setPitch(pitch);
                                                        // get inner door direction so we can adjust yaw if necessary
                                                        if (!innerD.equals(d)) {
                                                            switch (Constants.COMPASS.valueOf(d)) {
                                                                case NORTH:
                                                                    yaw = yaw + adjustYaw[0][Constants.COMPASS.valueOf(innerD).ordinal()];
                                                                    break;
                                                                case WEST:
                                                                    yaw = yaw + adjustYaw[1][Constants.COMPASS.valueOf(innerD).ordinal()];
                                                                    break;
                                                                case SOUTH:
                                                                    yaw = yaw + adjustYaw[2][Constants.COMPASS.valueOf(innerD).ordinal()];
                                                                    break;
                                                                case EAST:
                                                                    yaw = yaw + adjustYaw[3][Constants.COMPASS.valueOf(innerD).ordinal()];
                                                                    break;
                                                            }
                                                        }
                                                        tmp_loc.setYaw(yaw);
                                                        final Location tardis_loc = tmp_loc;
                                                        tt(player, tardis_loc, false, playerWorld, userQuotes);
                                                        String queryTravellerUpdate = "INSERT INTO travellers (tardis_id, player) VALUES (" + id + ", '" + playerNameStr + "')";
                                                        statement.executeUpdate(queryTravellerUpdate);
                                                        // update current TARDIS location
                                                        String queryLocUpdate = "UPDATE tardis SET current = '" + save + "' WHERE tardis_id = " + id;
                                                        statement.executeUpdate(queryLocUpdate);
                                                        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_hum.mp3", false, tardis_loc, 9, 25);
                                                        }
                                                    }
                                                } else {
                                                    if (TLOnline == false) {
                                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NOT_OWNER);
                                                    }
                                                }
                                            }
                                        } //else {
                                        //player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                        //}
                                    } catch (SQLException e) {
                                        System.err.println(Constants.MY_PLUGIN_NAME + " Get TARDIS from Door Error: " + e);
                                    }
                                } else {
                                    player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_PERMS_MESSAGE);
                                }
                            } else {
                                System.err.println(Constants.MY_PLUGIN_NAME + " Could not get block");
                            }
                        } else {
                            Block blockAbove = block.getRelative(BlockFace.UP);
                            Material baType = blockAbove.getType();
                            byte baData = blockAbove.getData();
                            if (baType == Material.WOOL && (baData == 1 || baData == 11)) {
                                player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.WRONG_MATERIAL + Constants.TARDIS_KEY + ". You have a " + material + " in your hand!");
                            }
                        }
                    }
                    if (blockType == Material.STONE_BUTTON) {
                        // get clicked block location
                        Location b = block.getLocation();
                        String bw = b.getWorld().getName();
                        int bx = b.getBlockX();
                        int by = b.getBlockY();
                        int bz = b.getBlockZ();
                        String buttonloc = bw + ":" + bx + ":" + by + ":" + bz;
                        // get tardis from saved button location
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryTardis = "SELECT * FROM tardis WHERE button = '" + buttonloc + "'";
                            ResultSet rs = statement.executeQuery(queryTardis);
                            if (rs.next()) {
                                int id = rs.getInt("tardis_id");
                                String tl = rs.getString("owner");
                                String r0_str = rs.getString("repeater0");
                                String r1_str = rs.getString("repeater1");
                                String r2_str = rs.getString("repeater2");
                                String r3_str = rs.getString("repeater3");
                                String dir = rs.getString("direction");
                                String s1_str = rs.getString("save1");
                                String s2_str = rs.getString("save2");
                                String s3_str = rs.getString("save3");
//                                System.out.println("[Boxfriend Debug] Repeater 0 location is: " + r0_str);
//                                System.out.println("[Boxfriend Debug] Repeater 1 location is: " + r1_str);
//                                System.out.println("[Boxfriend Debug] Repeater 2 location is: " + r2_str);
//                                System.out.println("[Boxfriend Debug] Repeater 3 location is: " + r3_str);

                                // check if player is travelling
                                String queryTraveller = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + tl + "'";
                                ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                if (timelordIsIn.next()) {
                                    // get repeater settings
                                    Location r0_loc = Constants.getLocationFromDB(r0_str, 0, 0);
                                    Block r0 = r0_loc.getBlock();
                                    byte r0_data = r0.getData();
                                    Location r1_loc = Constants.getLocationFromDB(r1_str, 0, 0);
                                    Block r1 = r1_loc.getBlock();
                                    byte r1_data = r1.getData();
                                    Location r2_loc = Constants.getLocationFromDB(r2_str, 0, 0);
                                    Block r2 = r2_loc.getBlock();
                                    byte r2_data = r2.getData();
                                    Location r3_loc = Constants.getLocationFromDB(r3_str, 0, 0);
                                    Block r3 = r3_loc.getBlock();
                                    byte r3_data = r3.getData();
                                    boolean playSound = true;
                                    if (r0_data <= 3 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // first position
                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Home destination selected!");
                                        // always teleport to home location
                                        String querySave = "UPDATE tardis SET save = home WHERE tardis_id = " + id;
                                        statement.executeUpdate(querySave);
                                    }
                                    if (r0_data >= 4 && r0_data <= 7 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // second position
                                        if (!s1_str.equals("") && s1_str != null && !s1_str.equalsIgnoreCase("null")) {
                                            String[] s1_data = s1_str.split("~");
                                            String querySave = "UPDATE tardis SET save = '" + s1_data[1] + "' WHERE tardis_id = " + id;
                                            statement.executeUpdate(querySave);
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination 1 [" + s1_data[0] + "] selected!");
                                        } else {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " There is no destination saved to slot 1!");
                                            playSound = false;
                                        }
                                    }
                                    if (r0_data >= 8 && r0_data <= 11 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // third position
                                        if (!s2_str.equals("") && s2_str != null && !s2_str.equalsIgnoreCase("null")) {
                                            String[] s2_data = s2_str.split("~");
                                            String querySave = "UPDATE tardis SET save = '" + s2_data[1] + "' WHERE tardis_id = " + id;
                                            statement.executeUpdate(querySave);
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination 2 [" + s2_data[0] + "] selected!");
                                        } else {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " There is no destination saved to slot 2!");
                                            playSound = false;
                                        }
                                    }
                                    if (r0_data >= 12 && r0_data <= 15 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // last position
                                        if (!s3_str.equals("") && s3_str != null && !s3_str.equalsIgnoreCase("null")) {
                                            String[] s3_data = s3_str.split("~");
                                            String querySave = "UPDATE tardis SET save = '" + s3_data[1] + "' WHERE tardis_id = " + id;
                                            statement.executeUpdate(querySave);
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination 3 [" + s3_data[0] + "] selected!");
                                        } else {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " There is no destination saved to slot 3!");
                                            playSound = false;
                                        }
                                    }
                                    if (r1_data > 3 || r2_data > 3 || r3_data > 3) {
                                        // create a random destination
                                        TARDISTimetravel tt = new TARDISTimetravel(plugin);
                                        Location rand = tt.randomDestination(player, player.getWorld(), r1_data, r2_data, r3_data, dir);
                                        String d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                                        String queryCompanions = "SELECT owner, companions FROM tardis WHERE tardis_id = " + id;
                                        ResultSet rsCom = statement.executeQuery(queryCompanions);
                                        boolean isTL = true;
                                        if (rsCom.next()) {
                                            String comps = rsCom.getString("companions");
                                            if (comps != null && !comps.equals("") && !comps.equals("[Null]")) {
                                                String[] companions = comps.split(":");
                                                for (String c : companions) {
                                                    if (plugin.getServer().getPlayer(c) != null) {
                                                        plugin.getServer().getPlayer(c).sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination world: " + rand.getWorld().getName());
                                                    }
                                                    if (c.equalsIgnoreCase(player.getName())) {
                                                        isTL = false;
                                                    }
                                                }
                                            }
                                        }
                                        if (isTL == true) {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination world: " + rand.getWorld().getName());
                                        } else {
                                            if (plugin.getServer().getPlayer(rs.getString("owner")) != null) {
                                                plugin.getServer().getPlayer(rs.getString("owner")).sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " Destination world: " + rand.getWorld().getName());
                                            }
                                        }
                                        String querySave = "UPDATE tardis SET save = '" + d + "' WHERE tardis_id = " + id;
                                        statement.executeUpdate(querySave);
                                    }
                                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled() && playSound == true) {
                                        SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_takeoff.mp3", false, b, 9, 75);
                                    }
                                }
                                timelordIsIn.close();
                            }
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Get TARDIS from Button Error: " + e);
                        }
                    }
                    if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                        // get clicked block location
                        Location b = block.getLocation();
                        String bw = b.getWorld().getName();
                        int bx = b.getBlockX();
                        int by = b.getBlockY();
                        int bz = b.getBlockZ();
                        String signloc = bw + ":" + bx + ":" + by + ":" + bz;
                        // get tardis from saved button location
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            String queryTardis = "SELECT * FROM tardis WHERE chameleon = '" + signloc + "'";
                            ResultSet rs = statement.executeQuery(queryTardis);
                            if (rs.next()) {
                                int id = rs.getInt("tardis_id");
                                String queryChameleon;
                                Sign s = (Sign) block.getState();
                                if (rs.getBoolean("chamele_on")) {
                                    queryChameleon = "UPDATE tardis SET chamele_on = 0 WHERE tardis_id = " + id;
                                    s.setLine(3, "¤cOFF");
                                    s.update();
                                } else {
                                    queryChameleon = "UPDATE tardis SET chamele_on = 1 WHERE tardis_id = " + id;
                                    s.setLine(3, "¤aON");
                                    s.update();
                                }
                                statement.executeUpdate(queryChameleon);
                            }
                        } catch (SQLException e) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Get TARDIS from Button Error: " + e);
                        }
                    }
                }
            }
        }
    }
    Random r = new Random();

    private void tt(Player p, Location l, boolean exit, final World from, boolean q) {

        final int i = r.nextInt(plugin.quotelen);
        final Player thePlayer = p;
        final Location theLocation = l;
        final World to = theLocation.getWorld();
        final boolean allowFlight = thePlayer.getAllowFlight();
        final boolean crossWorlds = from != to;
        final boolean quotes = q;

        // try loading chunk
        World world = l.getWorld();
        Chunk chunk = world.getChunkAt(l);
        if (!world.isChunkLoaded(chunk)) {
            world.loadChunk(chunk);
        }
        world.refreshChunk(chunk.getX(), chunk.getZ());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                thePlayer.teleport(theLocation);
            }
        }, 10L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                thePlayer.teleport(theLocation);
                if (thePlayer.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    thePlayer.setAllowFlight(true);
                }
                if (quotes) {
                    thePlayer.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + plugin.quote.get(i));
                }
            }
        }, 5L);
    }
}