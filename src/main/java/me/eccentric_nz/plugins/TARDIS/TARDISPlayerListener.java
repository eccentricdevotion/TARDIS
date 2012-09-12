package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
    TARDISdatabase service = TARDISdatabase.getInstance();

    public TARDISPlayerListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        int savedx = 0, savedy = 0, savedz = 0, cx = 0, cz = 0;

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
                if (blockData == 8 && blockType == Material.IRON_DOOR_BLOCK) {
                    by = (by - 1);
                }
                String blockLocStr = bw + ":" + bx + ":" + by + ":" + bz;
                plugin.trackPlayers.remove(playerNameStr);
                try {
                    int id;
                    String queryBlockUpdate = "";
                    Connection connection = service.getConnection();
                    Statement statement = connection.createStatement();
                    String queryTARDIS = "SELECT tardis_id FROM tardis WHERE owner = '" + playerNameStr + "'";
                    ResultSet rs = statement.executeQuery(queryTARDIS);
                    if (rs != null && rs.next()) {
                        id = rs.getInt("tardis_id");
                        rs.close();
                        if (blockName.equalsIgnoreCase("door") && blockType == Material.IRON_DOOR_BLOCK) {
                            queryBlockUpdate = "UPDATE doors SET door_location = '" + blockLocStr + "' WHERE door_type = 1 AND tardis_id = " + id;
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
                        statement.executeUpdate(queryBlockUpdate);
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " The position of the TARDIS " + blockName + " was updated successfully.");
                    } else {
                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RED + " There was a problem updating the position of the TARDIS " + blockName + "!");
                    }
                } catch (SQLException e) {
                    System.err.println(Constants.MY_PLUGIN_NAME + " Create table error: " + e);
                }

            } else {
                Action action = event.getAction();
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    ItemStack stack = player.getItemInHand();
                    Material material = stack.getType();
                    // get key material from config
                    Material key = Material.getMaterial(plugin.config.getString("key"));
                    // only proceed if they are clicking an iron door with a TARDIS key!
                    if (blockType == Material.IRON_DOOR_BLOCK) {
                        if (material == key) {
                            if (block != null) {
                                if (player.hasPermission("TARDIS.enter")) {
                                    //allowFlying.put(playerNameStr, player.getAllowFlight());
                                    //isFlying.put(playerNameStr, player.isFlying());
                                    Location block_loc = block.getLocation();
                                    String bw = block_loc.getWorld().getName();
                                    int bx = block_loc.getBlockX();
                                    int by = block_loc.getBlockY();
                                    int bz = block_loc.getBlockZ();
                                    byte doorData = block.getData();
                                    if (doorData == 8) {
                                        by = (by - 1);
                                    }
                                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                                    try {
                                        Connection connection = service.getConnection();
                                        Statement statement = connection.createStatement();
                                        String queryTardis = "SELECT tardis.*, doors.door_type FROM tardis, doors WHERE doors.door_location = '" + doorloc + "' AND doors.tardis_id = tardis.tardis_id";
                                        ResultSet rs = statement.executeQuery(queryTardis);
                                        if ((rs != null && rs.next())) {
                                            int id = rs.getInt("tardis_id");
                                            int doortype = rs.getInt("door_type");
                                            String d = rs.getString("direction");
                                            String chunkstr = rs.getString("chunk");
                                            String tl = rs.getString("owner");
                                            String companions = rs.getString("companions");
                                            String save = rs.getString("save");
                                            String cl = rs.getString("current");
                                            float yaw = player.getLocation().getYaw();
                                            float pitch = player.getLocation().getPitch();
                                            // get last known BLUEBOX location
                                            if (doortype == 1) {
                                                // player is in the TARDIS
                                                // get location from database
                                                final Location exitTardis = Constants.getLocationFromDB(save, yaw, pitch);
                                                // make location safe ie. outside of the bluebox
                                                double ex = exitTardis.getX();
                                                double ez = exitTardis.getZ();
                                                double ey = exitTardis.getY();
                                                switch (Constants.COMPASS.valueOf(d)) {
                                                    case NORTH:
                                                        exitTardis.setZ(ez + 2);
                                                        break;
                                                    case EAST:
                                                        exitTardis.setX(ex - 2);
                                                        break;
                                                    case SOUTH:
                                                        exitTardis.setZ(ez - 2);
                                                        break;
                                                    case WEST:
                                                        exitTardis.setX(ex + 2);
                                                        break;
                                                }
                                                exitTardis.setY(ey + .25);
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
                                                    builder.buildOuterTARDIS(id, newl, Constants.COMPASS.valueOf(d));
                                                }
                                                // exit TARDIS!
                                                tt(player, exitTardis);
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
                                                            //System.out.println(queryTraveller);
                                                            ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                                            if (timelordIsIn != null && timelordIsIn.next()) {
                                                                // is the player in the comapnion list
                                                                String[] companionData = companions.split(":");
                                                                for (String c : companionData) {
                                                                    String lc_name = c.toLowerCase();
                                                                    if (lc_name.equalsIgnoreCase(playerNameStr)) {
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
                                                    Location tmp_loc = null;
                                                    //String chunkstr = rs.getString("chunk");
                                                    String[] split = chunkstr.split(":");
                                                    World cw = plugin.getServer().getWorld(split[0]);
                                                    try {
                                                        cx = Integer.parseInt(split[1]);
                                                        cz = Integer.parseInt(split[2]);
                                                    } catch (NumberFormatException nfe) {
                                                        System.err.println(Constants.MY_PLUGIN_NAME + " Could not convert to number!");
                                                    }
                                                    Chunk chunk = cw.getChunkAt(cx, cz);
                                                    switch (Constants.COMPASS.valueOf(d)) {
                                                        case NORTH:
                                                            tmp_loc = chunk.getBlock(9, 19, 13).getLocation();
                                                            break;
                                                        case EAST:
                                                            tmp_loc = chunk.getBlock(3, 19, 9).getLocation();
                                                            break;
                                                        case SOUTH:
                                                            tmp_loc = chunk.getBlock(6, 19, 3).getLocation();
                                                            break;
                                                        case WEST:
                                                            tmp_loc = chunk.getBlock(13, 19, 6).getLocation();
                                                            break;
                                                    }
                                                    // enter TARDIS!
                                                    cw.getChunkAt(tmp_loc).load();
                                                    tmp_loc.setPitch(pitch);
                                                    tmp_loc.setYaw(yaw);
                                                    final Location tardis_loc = tmp_loc;
                                                    tt(player, tardis_loc);
                                                    String queryTravellerUpdate = "INSERT INTO travellers (tardis_id, player) VALUES (" + id + ", '" + playerNameStr + "')";
                                                    statement.executeUpdate(queryTravellerUpdate);
                                                    // update current TARDIS location
                                                    String queryLocUpdate = "UPDATE tardis SET current = '" + save + "' WHERE tardis_id = " + id;
                                                    statement.executeUpdate(queryLocUpdate);
                                                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                        SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_hum.mp3", false, tardis_loc, 9, 25);
                                                    }
                                                } else {
                                                    if (TLOnline == false) {
                                                        player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NOT_OWNER);
                                                    }
                                                }
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.GRAY + Constants.MY_PLUGIN_NAME + ChatColor.RESET + " " + Constants.NO_TARDIS);
                                        }
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
                            if (rs != null && rs.next()) {
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

                                // check if player is travelling
                                String queryTraveller = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + tl + "'";
                                ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                if (timelordIsIn != null && timelordIsIn.next()) {
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
                                    //player.sendMessage("0:" + r0_data + ", 1:" + r1_data + ", 2:" + r2_data + ", 3:" + r3_data);
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
                                        if (rsCom != null && rsCom.next()) {
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
                }
            }
        }
    }

    private void tt(Player p, Location l) {
        final Player thePlayer = p;
        final Location theLocation = l;

        if ((thePlayer.getAllowFlight()) && (!thePlayer.isFlying())) {
            //thePlayer.sendMessage("Is not flying");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setAllowFlight(true);
                    thePlayer.setFlying(true);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    //thePlayer.teleport(theLocation);
                    thePlayer.setAllowFlight(true);
                    thePlayer.setFlying(false);
                }
            }, 10L);
        }
        if ((thePlayer.getAllowFlight()) && (thePlayer.isFlying())) {
            //thePlayer.sendMessage("Is flying already");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.setAllowFlight(true);
                    thePlayer.setFlying(true);
                }
            }, 10L);
        }
        if (!thePlayer.getAllowFlight()) {
            //thePlayer.sendMessage("Not allowed to fly");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setAllowFlight(true);
                    thePlayer.setFlying(true);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    //thePlayer.teleport(theLocation);
                    thePlayer.setFlying(false);
                    thePlayer.setAllowFlight(false);
                }
            }, 10L);
        }
    }
}
