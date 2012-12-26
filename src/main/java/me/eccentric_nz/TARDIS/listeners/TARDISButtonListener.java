package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.travel.TARDISTimetravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

public class TARDISButtonListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISButtonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onButtonInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack stack = player.getItemInHand();
                // only proceed if they are clicking a stone button!
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
                                    player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                                    Location l = plugin.ta.getNextSpot(permArea);
                                    if (l == null) {
                                        player.sendMessage(plugin.pluginName + " All available parking spots are taken in this area!");
                                    }
                                    String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                                    String querySave = "UPDATE tardis SET save = '" + save_loc + "' WHERE tardis_id = " + id;
                                    statement.executeUpdate(querySave);
                                    player.sendMessage(plugin.pluginName + " Your TARDIS was approved for parking in [" + permArea + "]!");
                                } else {
                                    // get repeater settings
                                    Location r0_loc = TARDISConstants.getLocationFromDB(r0_str, 0, 0);
                                    Block r0 = r0_loc.getBlock();
                                    byte r0_data = r0.getData();
                                    Location r1_loc = TARDISConstants.getLocationFromDB(r1_str, 0, 0);
                                    Block r1 = r1_loc.getBlock();
                                    byte r1_data = r1.getData();
                                    Location r2_loc = TARDISConstants.getLocationFromDB(r2_str, 0, 0);
                                    Block r2 = r2_loc.getBlock();
                                    byte r2_data = r2.getData();
                                    Location r3_loc = TARDISConstants.getLocationFromDB(r3_str, 0, 0);
                                    Block r3 = r3_loc.getBlock();
                                    byte r3_data = r3.getData();
                                    boolean playSound = true;
                                    String environment = "NORMAL";
                                    if (r0_data <= 3) { // first position
                                        if (plugin.getConfig().getBoolean("nether") == false && plugin.getConfig().getBoolean("the_end") == false) {
                                            environment = "NORMAL";
                                        } else if (plugin.getConfig().getBoolean("nether") == false || plugin.getConfig().getBoolean("the_end") == false) {
                                            if (plugin.getConfig().getBoolean("nether") == false) {
                                                environment = (player.hasPermission("tardis.end")) ? "NORMAL:THE_END" : "NORMAL";
                                            }
                                            if (plugin.getConfig().getBoolean("the_end") == false) {
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
                                        if (plugin.getConfig().getBoolean("nether") == true && player.hasPermission("tardis.nether")) {
                                            environment = "NETHER";
                                        } else {
                                            String message = (player.hasPermission("tardis.nether")) ? " The ancient, dusty senators of Gallifrey have disabled time travel to the Nether" : " You do not have permission to time travel to the Nether";
                                            player.sendMessage(plugin.pluginName + message);
                                        }
                                    }
                                    if (r0_data >= 12 && r0_data <= 15) { // last position
                                        if (plugin.getConfig().getBoolean("the_end") == true && player.hasPermission("tardis.end")) {
                                            environment = "THE_END";
                                        } else {
                                            String message = (player.hasPermission("tardis.end")) ? " The ancient, dusty senators of Gallifrey have disabled time travel to The End" : " You do not have permission to time travel to The End";
                                            player.sendMessage(plugin.pluginName + message);
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
                                                    plugin.getServer().getPlayer(c).sendMessage(plugin.pluginName + " Destination: " + dchat);
                                                }
                                                if (c.equalsIgnoreCase(player.getName())) {
                                                    isTL = false;
                                                }
                                            }
                                        }
                                    }
                                    if (isTL == true) {
                                        player.sendMessage(plugin.pluginName + " Destination: " + dchat);
                                    } else {
                                        if (plugin.getServer().getPlayer(rs.getString("owner")) != null) {
                                            plugin.getServer().getPlayer(rs.getString("owner")).sendMessage(plugin.pluginName + " Destination: " + dchat);
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
                        plugin.console.sendMessage(plugin.pluginName + " Get TARDIS from Button Error: " + e);
                    }
                }
            }
        }
    }
}