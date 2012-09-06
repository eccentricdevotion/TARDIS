package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    private boolean playerIsFlying;
    private boolean playerCanFly;
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
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material blockType = block.getType();
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
                                                exitTardis.setZ(ez + 2.5);
                                                break;
                                            case EAST:
                                                exitTardis.setX(ex - 2.5);
                                                break;
                                            case SOUTH:
                                                exitTardis.setZ(ez - 2.5);
                                                break;
                                            case WEST:
                                                exitTardis.setX(ex + 2.5);
                                                break;
                                        }
                                        //exitTardis.setY(ey - 1.75);
                                        World exitWorld = exitTardis.getWorld();
                                        // destroy current TARDIS location
                                        Location newl = null;
                                        TARDISDestroyer destroyer = new TARDISDestroyer(plugin);
                                        if (!save.equals(cl)) {
                                            Location l = Constants.getLocationFromDB(cl, 0, 0);
                                            newl = Constants.getLocationFromDB(save, 0, 0);
                                            //double old_y = newl.getY();
                                            //newl.setY(old_y - 2);
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
                                                if (!companions.equals("") && companions != null) {
                                                    // is the timelord in the TARDIS?
                                                    String queryTraveller = "SELECT * FROM travellers WHERE tardis_id = " + id + " AND player = '" + tl + "' LIMIT 1";
                                                    ResultSet timelordIsIn = statement.executeQuery(queryTraveller);
                                                    if (timelordIsIn != null && timelordIsIn.next()) {
                                                        // is the player in the comapnion list
                                                        String[] companionData = companions.split(":");
                                                        for (String c : companionData) {
                                                            if (c.equals(playerNameStr)) {
                                                                chkCompanion = true;
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        player.sendMessage(Constants.TIMELORD_NOT_IN);
                                                        TLOnline = true;
                                                    }
                                                }
                                            } else {
                                                player.sendMessage(Constants.TIMELORD_OFFLINE);
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
                                                player.sendMessage(Constants.NOT_OWNER);
                                            }
                                        }
                                    }
                                } else {
                                    player.sendMessage(Constants.NO_TARDIS);
                                }
//                                statement.close();
                            } catch (SQLException e) {
                                System.err.println(Constants.MY_PLUGIN_NAME + " Get TARDIS from Door Error: " + e);
                            }
                        } else {
                            player.sendMessage(Constants.NO_PERMS_MESSAGE);
                        }
                    } else {
                        System.err.println(Constants.MY_PLUGIN_NAME + " Could not get block");
                    }
                } else {
                    player.sendMessage(Constants.WRONG_MATERIAL + Constants.TARDIS_KEY + ". You have a " + material + " in your hand!");
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
                                player.sendMessage("Home destination selected!");
                                // always teleport to home location
                                String querySave = "UPDATE tardis SET save = home WHERE tardis_id = " + id;
                                statement.executeUpdate(querySave);
                            }
                            if (r0_data >= 4 && r0_data <= 7 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // second position
                                if (!s1_str.equals("") && s1_str != null && !s1_str.equalsIgnoreCase("null")) {
                                    String[] s1_data = s1_str.split("~");
                                    String querySave = "UPDATE tardis SET save = '" + s1_data[1] + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    player.sendMessage("Destination 1 [" + s1_data[0] + "] selected!");
                                } else {
                                    player.sendMessage("There is no destination saved to slot 1!");
                                    playSound = false;
                                }
                            }
                            if (r0_data >= 8 && r0_data <= 11 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // third position
                                if (!s2_str.equals("") && s2_str != null && !s2_str.equalsIgnoreCase("null")) {
                                    String[] s2_data = s2_str.split("~");
                                    String querySave = "UPDATE tardis SET save = '" + s2_data[1] + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    player.sendMessage("Destination 2 [" + s2_data[0] + "] selected!");
                                } else {
                                    player.sendMessage("There is no destination saved to slot 2!");
                                    playSound = false;
                                }
                            }
                            if (r0_data >= 12 && r0_data <= 15 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // last position
                                if (!s3_str.equals("") && s3_str != null && !s3_str.equalsIgnoreCase("null")) {
                                    String[] s3_data = s3_str.split("~");
                                    String querySave = "UPDATE tardis SET save = '" + s3_data[1] + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    player.sendMessage("Destination 3 [" + s3_data[0] + "] selected!");
                                } else {
                                    player.sendMessage("There is no destination saved to slot 3!");
                                    playSound = false;
                                }
                            }
                            if (r1_data > 3 || r2_data > 3 || r3_data > 3) {
                                // create a random destination
                                TARDISTimetravel tt = new TARDISTimetravel(plugin);
                                Location rand = tt.randomDestination(player, player.getWorld(), r1_data, r2_data, r3_data, dir);
                                String d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                                player.sendMessage("Destination world: " + rand.getWorld().getName());
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

    private void tt(Player p, Location l) {
        final Player thePlayer = p;
        final Location theLocation = l;
        if ((thePlayer.getAllowFlight()) && (!thePlayer.isFlying())) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setFlying(true);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setFlying(false);
                }
            }, 10L);
        }
        if ((thePlayer.getAllowFlight()) && (thePlayer.isFlying())) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                }
            }, 10L);
        }
        if (!thePlayer.getAllowFlight()) {
            //thePlayer.setFlying(true);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setAllowFlight(true);
                    thePlayer.setFlying(true);
                }
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    thePlayer.teleport(theLocation);
                    thePlayer.setFlying(false);
                    thePlayer.setAllowFlight(false);
                }
            }, 10L);
        }
    }
}
