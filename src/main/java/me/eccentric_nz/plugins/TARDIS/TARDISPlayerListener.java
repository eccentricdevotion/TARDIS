package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TARDISPlayerListener implements Listener {

    private TARDIS plugin;
    float[][] adjustYaw = new float[4][4];
    TARDISDatabase service = TARDISDatabase.getInstance();

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
                    String home;
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet rs = service.getTardis(playerNameStr, "tardis_id, home");
                    if (rs.next()) {
                        id = rs.getInt("tardis_id");
                        home = rs.getString("home");
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
                            s.setLine(3, ChatColor.RED + "OFF");
                            s.update();
                        }
                        if (blockName.equalsIgnoreCase("save-sign") && (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST)) {
                            queryBlockUpdate = "UPDATE tardis SET save_sign = '" + blockLocStr + "' WHERE tardis_id = " + id;
                            // add text to sign
                            String[] coords = home.split(":");
                            Sign s = (Sign) block.getState();
                            s.setLine(0, "Saves");
                            s.setLine(1, "Home");
                            s.setLine(2, coords[0]);
                            s.setLine(3, coords[1] + "," + coords[2] + "," + coords[3]);
                            s.update();
                        }
                        statement.executeUpdate(queryBlockUpdate);
                        player.sendMessage(Constants.MY_PLUGIN_NAME + " The position of the TARDIS " + blockName + " was updated successfully.");
                    } else {
                        player.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " There was a problem updating the position of the TARDIS " + blockName + "!");
                    }
                } catch (SQLException e) {
                    plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Update TARDIS blocks error: " + e);
                }
            } else if (plugin.trackName.containsKey(playerNameStr) && !plugin.trackBlock.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String locStr = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockZ();
                    plugin.trackBlock.put(playerNameStr, locStr);
                    player.sendMessage(Constants.MY_PLUGIN_NAME + " You have 60 seconds to select the area end block - use the " + ChatColor.GREEN + "/TARDIS admin area end" + ChatColor.RESET + " command.");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.trackName.remove(playerNameStr);
                            plugin.trackBlock.remove(playerNameStr);
                        }
                    }, 1200L);
                } else {
                    player.sendMessage(Constants.MY_PLUGIN_NAME + " That block is inside an already defined area! Try somewhere else.");
                }
            } else if (plugin.trackBlock.containsKey(playerNameStr) && plugin.trackEnd.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String[] firstblock = plugin.trackBlock.get(playerNameStr).split(":");
                    if (!block_loc.getWorld().getName().equals(firstblock[0])) {
                        player.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " Area start and end blocks must be in the same world! Try again");
                    }
                    int minx, minz, maxx, maxz;
                    if (plugin.utils.parseNum(firstblock[1]) < block_loc.getBlockX()) {
                        minx = plugin.utils.parseNum(firstblock[1]);
                        maxx = block_loc.getBlockX();
                    } else {
                        minx = block_loc.getBlockX();
                        maxx = plugin.utils.parseNum(firstblock[1]);
                    }
                    if (plugin.utils.parseNum(firstblock[2]) < block_loc.getBlockZ()) {
                        minz = plugin.utils.parseNum(firstblock[2]);
                        maxz = block_loc.getBlockZ();
                    } else {
                        minz = block_loc.getBlockZ();
                        maxz = plugin.utils.parseNum(firstblock[2]);
                    }
                    String n = plugin.trackName.get(playerNameStr);
                    try {
                        Connection connection = service.getConnection();
                        PreparedStatement psArea = connection.prepareStatement("INSERT INTO areas (area_name, world, minx, minz, maxx, maxz) VALUES (?,?,?,?,?,?)");
                        psArea.setString(1, n);
                        psArea.setString(2, firstblock[0]);
                        psArea.setInt(3, minx);
                        psArea.setInt(4, minz);
                        psArea.setInt(5, maxx);
                        psArea.setInt(6, maxz);
                        psArea.executeUpdate();
                        player.sendMessage(Constants.MY_PLUGIN_NAME + " The area [" + plugin.trackName.get(playerNameStr) + "] was saved successfully");
                        plugin.trackName.remove(playerNameStr);
                        plugin.trackBlock.remove(playerNameStr);
                        plugin.trackEnd.remove(playerNameStr);
                        psArea.close();
                    } catch (SQLException e) {
                        plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Area save error: " + e);
                    }
                } else {
                    player.sendMessage(Constants.MY_PLUGIN_NAME + " That block is inside an already defined area! Try somewhere else.");
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
                                if (player.hasPermission("tardis.enter")) {
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
                                            String tl = rs.getString("owner");
                                            String save = rs.getString("save");
                                            String cl = rs.getString("current");
                                            boolean cham = rs.getBoolean("chamele_on");
                                            float yaw = player.getLocation().getYaw();
                                            float pitch = player.getLocation().getPitch();
                                            String companions = rs.getString("companions");
                                            boolean compswasnull = false;
                                            if (rs.wasNull()) {
                                                compswasnull = true;
                                            }
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
// need some sort of check here to sort out who has exited
                                                // count number of travellers - if 1 less than number counted at start
                                                // then build and remember else just exit?
                                                int count = 1;
                                                String queryCount = "SELECT COUNT (*) AS count FROM travellers WHERE tardis_id = " + id;
                                                ResultSet rsCount = statement.executeQuery(queryCount);
                                                if (rsCount.next()) {
                                                    count = rsCount.getInt("count");
                                                }
                                                if (!save.equals(cl) && (count == plugin.trackTravellers.get(id))) {
                                                    Location l = Constants.getLocationFromDB(cl, 0, 0);
                                                    newl = Constants.getLocationFromDB(save, 0, 0);
                                                    // remove torch
                                                    plugin.destroyer.destroyTorch(l);
                                                    // remove sign
                                                    plugin.destroyer.destroySign(l, Constants.COMPASS.valueOf(d));
                                                    // remove blue box
                                                    plugin.destroyer.destroyBlueBox(l, Constants.COMPASS.valueOf(d), id, false);
                                                }
                                                // try preloading destination chunk
                                                while (!exitWorld.getChunkAt(exitTardis).isLoaded()) {
                                                    exitWorld.getChunkAt(exitTardis).load();
                                                }
                                                // rebuild blue box
                                                if (newl != null && (count == plugin.trackTravellers.get(id))) {
                                                    plugin.builder.buildOuterTARDIS(id, newl, Constants.COMPASS.valueOf(d), cham, player, false);
                                                }
                                                // exit TARDIS!
                                                movePlayer(player, exitTardis, true, playerWorld, userQuotes);
                                                // remove player from traveller table
                                                String queryTraveller = "DELETE FROM travellers WHERE player = '" + playerNameStr + "'";
                                                statement.executeUpdate(queryTraveller);
                                            } else {
                                                boolean chkCompanion = false;
                                                if (!playerNameStr.equals(tl)) {
                                                    if (plugin.getServer().getPlayer(tl) != null) {
                                                        if (!compswasnull && !companions.equals("")) {
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
                                                                player.sendMessage(Constants.MY_PLUGIN_NAME + " " + Constants.TIMELORD_NOT_IN);
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage(Constants.MY_PLUGIN_NAME + " " + Constants.TIMELORD_OFFLINE);
                                                    }
                                                }
                                                if (playerNameStr.equals(tl) || chkCompanion == true || player.hasPermission("tardis.skeletonkey")) {
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
                                                            plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Could not convert to number!");
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
                                                        movePlayer(player, tardis_loc, false, playerWorld, userQuotes);
                                                        String queryTravellerUpdate = "INSERT INTO travellers (tardis_id, player) VALUES (" + id + ", '" + playerNameStr + "')";
                                                        statement.executeUpdate(queryTravellerUpdate);
                                                        // update current TARDIS location
                                                        String queryLocUpdate = "UPDATE tardis SET current = '" + save + "' WHERE tardis_id = " + id;
                                                        statement.executeUpdate(queryLocUpdate);
                                                        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_hum.mp3", false, tardis_loc, 9, 25);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (SQLException e) {
                                        plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Get TARDIS from Door Error: " + e);
                                    }
                                } else {
                                    player.sendMessage(Constants.MY_PLUGIN_NAME + " " + Constants.NO_PERMS_MESSAGE);
                                }
                            } else {
                                plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Could not get block");
                            }
                        } else {
                            Block blockAbove = block.getRelative(BlockFace.UP);
                            Material baType = blockAbove.getType();
                            byte baData = blockAbove.getData();
                            if (baType == Material.WOOL && (baData == 1 || baData == 11)) {
                                player.sendMessage(Constants.MY_PLUGIN_NAME + " " + Constants.WRONG_MATERIAL + Constants.TARDIS_KEY + ". You have a " + material + " in your hand!");
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

                                // check if player is travelling
                                String queryTraveller = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + tl + "'";
                                ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                if (timelordIsIn.next()) {
                                    // how many travellers are in the TARDIS?
                                    plugin.utils.updateTravellerCount(id);
                                    if (player.hasPermission("tardis.exile")) {
                                        // get the exile area
                                        String permArea = plugin.ta.getExileArea(player);
                                        player.sendMessage(Constants.MY_PLUGIN_NAME + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                                        Location l = plugin.ta.getNextSpot(permArea);
                                        if (l == null) {
                                            player.sendMessage(Constants.MY_PLUGIN_NAME + " All available parking spots are taken in this area!");
                                        }
                                        String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                                        String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                        statement.executeUpdate(querySave);
                                        player.sendMessage(Constants.MY_PLUGIN_NAME + " Your TARDIS was approved for parking in [" + permArea + "]!");
                                    } else {
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
                                        String environment = "NORMAL";
                                        if (r0_data <= 3) { // first position
                                            if (plugin.config.getBoolean("nether") == false && plugin.config.getBoolean("the_end") == false) {
                                                environment = "NORMAL";
                                            } else if (plugin.config.getBoolean("nether") == false || plugin.config.getBoolean("the_end") == false) {
                                                if (plugin.config.getBoolean("nether") == false) {
                                                    environment = (player.hasPermission("tardis.end")) ? "NORMAL:THE_END" : "NORMAL";
                                                }
                                                if (plugin.config.getBoolean("the_end") == false) {
                                                    environment = (player.hasPermission("tardis.nether")) ? "NORMAL:NETHER" : "NORMAL";
                                                }
                                            } else {
                                                if (player.hasPermission("tardis.end") && player.hasPermission("tardis.nether")) {
                                                    environment = "NORMAL:NETHER:THE_END";
                                                }
                                                if (!player.hasPermission("tardis.end") && player.hasPermission("tardis.nether")) {
                                                    environment = "NORMAL:NETHER";
                                                }
                                                if (player.hasPermission("tardis.end") && !player.hasPermission("tardis.nether")) {
                                                    environment = "NORMAL:THE_END";
                                                }
                                            }
                                        }
                                        if (r0_data >= 4 && r0_data <= 7) { // second position
                                            environment = "NORMAL";
                                        }
                                        if (r0_data >= 8 && r0_data <= 11) { // third position
                                            if (plugin.config.getBoolean("nether") == true && player.hasPermission("tardis.nether")) {
                                                environment = "NETHER";
                                            } else {
                                                String message = (player.hasPermission("tardis.nether")) ? " The ancient, dusty senators of Gallifrey have disabled time travel to the Nether" : " You do not have permission to time travel to the Nether";
                                                player.sendMessage(Constants.MY_PLUGIN_NAME + message);
                                            }
                                        }
                                        if (r0_data >= 12 && r0_data <= 15) { // last position
                                            if (plugin.config.getBoolean("the_end") == true && player.hasPermission("tardis.end")) {
                                                environment = "THE_END";
                                            } else {
                                                String message = (player.hasPermission("tardis.end")) ? " The ancient, dusty senators of Gallifrey have disabled time travel to The End" : " You do not have permission to time travel to The End";
                                                player.sendMessage(Constants.MY_PLUGIN_NAME + message);
                                            }
                                        }
                                        // create a random destination
                                        TARDISTimetravel tt = new TARDISTimetravel(plugin);
                                        Location rand = tt.randomDestination(player, player.getWorld(), r1_data, r2_data, r3_data, dir, environment);
                                        String d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                                        String dchat = rand.getWorld().getName() + " at x: " + rand.getBlockX() + " y: " + rand.getBlockY() + " z: " + rand.getBlockZ();
                                        String queryCompanions = "SELECT owner, companions FROM tardis WHERE tardis_id = " + id;
                                        ResultSet rsCom = statement.executeQuery(queryCompanions);
                                        boolean isTL = true;
                                        if (rsCom.next()) {
                                            String comps = rsCom.getString("companions");
                                            if (comps != null && !comps.equals("") && !comps.equals("[Null]")) {
                                                String[] companions = comps.split(":");
                                                for (String c : companions) {
                                                    // are they online - AND are they travelling - need check here for travelling!
                                                    if (plugin.getServer().getPlayer(c) != null) {
                                                        plugin.getServer().getPlayer(c).sendMessage(Constants.MY_PLUGIN_NAME + " Destination: " + dchat);
                                                    }
                                                    if (c.equalsIgnoreCase(player.getName())) {
                                                        isTL = false;
                                                    }
                                                }
                                            }
                                        }
                                        if (isTL == true) {
                                            player.sendMessage(Constants.MY_PLUGIN_NAME + " Destination: " + dchat);
                                        } else {
                                            if (plugin.getServer().getPlayer(rs.getString("owner")) != null) {
                                                plugin.getServer().getPlayer(rs.getString("owner")).sendMessage(Constants.MY_PLUGIN_NAME + " Destination: " + dchat);
                                            }
                                        }
                                        String querySave = "UPDATE tardis SET save = '" + d + "' WHERE tardis_id = " + id;
                                        statement.executeUpdate(querySave);
                                        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled() && playSound == true) {
                                            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_takeoff.mp3", false, b, 9, 75);
                                        }
                                    }
                                }
                                timelordIsIn.close();
                            }
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Get TARDIS from Button Error: " + e);
                        }
                    }
                    if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                        // get clicked block location
                        Location b = block.getLocation();
                        Sign s = (Sign) block.getState();
                        String line1 = s.getLine(0);
                        String queryTardis;
                        String bw = b.getWorld().getName();
                        int bx = b.getBlockX();
                        int by = b.getBlockY();
                        int bz = b.getBlockZ();
                        String signloc = bw + ":" + bx + ":" + by + ":" + bz;
                        if (line1.equals("Chameleon")) {
                            queryTardis = "SELECT * FROM tardis WHERE chameleon = '" + signloc + "'";
                        } else {
                            queryTardis = "SELECT tardis.home, destinations.* FROM tardis, destinations WHERE tardis.save_sign = '" + signloc + "' AND tardis.tardis_id = destinations.tardis_id";
                        }
                        // get tardis from saved button location
                        try {
                            Connection connection = service.getConnection();
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery(queryTardis);
                            if (rs.isBeforeFirst()) {
                                int id = rs.getInt("tardis_id");
                                if (line1.equals("Chameleon")) {
                                    String queryChameleon;
                                    if (rs.getBoolean("chamele_on")) {
                                        queryChameleon = "UPDATE tardis SET chamele_on = 0 WHERE tardis_id = " + id;
                                        s.setLine(3, ChatColor.RED + "OFF");
                                        s.update();
                                    } else {
                                        queryChameleon = "UPDATE tardis SET chamele_on = 1 WHERE tardis_id = " + id;
                                        s.setLine(3, ChatColor.GREEN + "ON");
                                        s.update();
                                    }
                                    statement.executeUpdate(queryChameleon);
                                } else {
                                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
                                        // set destination to currently displayed save
                                        String queryDest;
                                        if (s.getLine(1).equals("Home")) {
                                            queryDest = "UPDATE tardis SET save = home WHERE tardis_id = " + id;
                                        } else {
                                            // get location from sign
                                            String[] coords = s.getLine(3).split(",");
                                            queryDest = "UPDATE tardis SET save = '" + s.getLine(2) + ":" + coords[0] + ":" + coords[1] + ":" + coords[2] + "' WHERE tardis_id = " + id;
                                        }
                                        statement.executeUpdate(queryDest);
                                        plugin.utils.updateTravellerCount(id);
                                        player.sendMessage(Constants.MY_PLUGIN_NAME + " Exit location set");
                                    }
                                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {

                                        List<String> dests = new ArrayList<String>();
                                        String home = "";
                                        // cycle through saves
                                        while (rs.next()) {
                                            if (home.equals("")) {
                                                home = "Home:" + rs.getString("home");
                                            }
                                            dests.add(rs.getString("dest_name") + ":" + rs.getString("world") + ":" + rs.getString("x") + ":" + rs.getString("y") + ":" + rs.getString("z"));
                                        }
                                        dests.add(home);
                                        String[] display;
                                        if (plugin.trackDest.containsKey(player.getName())) {
                                            reOrder(dests, plugin.trackDest.get(player.getName()));
                                            plugin.trackDest.put(player.getName(), dests.get(1));
                                            display = dests.get(1).split(":");
                                        } else {
                                            display = dests.get(dests.size() - 1).split(":");
                                            plugin.trackDest.put(player.getName(), dests.get(dests.size() - 1));
                                        }
                                        s.setLine(1, display[0]);
                                        s.setLine(2, display[1]);
                                        s.setLine(3, display[2] + "," + display[3] + "," + display[4]);
                                        s.update();
                                    }
                                }
                            }
                            rs.close();
                            statement.close();
                        } catch (SQLException e) {
                            plugin.console.sendMessage(Constants.MY_PLUGIN_NAME + " Get TARDIS from Sign Error: " + e);
                        }
                    }
                }
            }
        }
    }
    Random r = new Random();

    @SuppressWarnings("deprecation")
    private void movePlayer(Player p, Location l, final boolean exit, final World from, boolean q) {

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
        }, 5L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                thePlayer.teleport(theLocation);
                if (thePlayer.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    thePlayer.setAllowFlight(true);
                }
                if (quotes) {
                    thePlayer.sendMessage(Constants.MY_PLUGIN_NAME + " " + plugin.quote.get(i));
                }
                if (exit == true) {
                    Inventory inv = thePlayer.getInventory();
                    Material m = Material.valueOf(Constants.TARDIS_KEY);
                    if (!inv.contains(m) && plugin.config.getBoolean("give_key") == true) {
                        ItemStack is = new ItemStack(m, 1);
                        TARDISItemRenamer ir = new TARDISItemRenamer(is);
                        ir.setName("Sonic Screwdriver", true);
                        inv.addItem(is);
                        thePlayer.updateInventory();
                        thePlayer.sendMessage(Constants.MY_PLUGIN_NAME + " Don't forget your TARDIS key!");
                    }
                }
            }
        }, 10L);
    }

    public void reOrder(List<String> list, String current) {
        int i = list.size();
        while (i-- > 0 && !list.get(0).equals(current)) {
            list.add(list.remove(0));
        }
    }
}
