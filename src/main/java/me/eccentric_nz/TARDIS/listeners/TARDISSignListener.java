package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TARDISSignListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        int cx = 0, cy = 0, cz = 0;

        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack stack = player.getItemInHand();
                // only proceed if they are clicking a sign!
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
                                    player.sendMessage(plugin.pluginName + " Exit location set");
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
                        plugin.console.sendMessage(plugin.pluginName + " Get TARDIS from Sign Error: " + e);
                    }
                }
            }
        }
    }

    public void reOrder(List<String> list, String current) {
        int i = list.size();
        while (i-- > 0 && !list.get(0).equals(current)) {
            list.add(list.remove(0));
        }
    }
}
