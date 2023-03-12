package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TVMCommand implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommand(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vm")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
                plugin.getServer().dispatchCommand(sender, "vmh");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                player.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                String uuid = player.getUniqueId().toString();
                if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
                    // get tachyon level
                    TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
                    if (rs.resultSet()) {
                        // open gui
                        ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
                        Inventory vmg = plugin.getServer().createInventory(player, 54, "§4Vortex Manipulator");
                        vmg.setContents(gui);
                        player.openInventory(vmg);
                        return true;
                    }
                }
                if (args.length > 0 && args[0].equalsIgnoreCase("go")) {
                    if (args.length < 2) {
                        player.sendMessage(plugin.getPluginName() + "You need to specify a save name!");
                        return true;
                    }
                    // check save exists
                    TVMResultSetWarpByName rsw = new TVMResultSetWarpByName(plugin, uuid, args[1]);
                    if (!rsw.resultSet()) {
                        player.sendMessage(plugin.getPluginName() + "Save does not exist!");
                        return true;
                    }
                    Location l = rsw.getWarp();
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel to " + args[1] + "...");
                    while (!l.getChunk().isLoaded()) {
                        l.getChunk().load();
                    }
                    List<Player> players = new ArrayList<>();
                    players.add(player);
                    if (plugin.getConfig().getBoolean("allow.multiple")) {
                        for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                            if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                                players.add((Player) e);
                            }
                        }
                    }
                    int required = plugin.getConfig().getInt("tachyon_use.saved") * players.size();
                    if (!TVMUtils.checkTachyonLevel(uuid, required)) {
                        player.sendMessage(plugin.getPluginName() + "You need at least " + required + " tachyons to travel!");
                        return true;
                    }
                    TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid, -required);
                    return true;
                }

                Parameters params = new Parameters(player, TVMUtils.getProtectionFlags());
                int required;
                List<String> worlds = new ArrayList<>();
                Location l;
                switch (args.length) {
                    case 1, 2, 3 -> {
                        // check world is an actual world
                        if (plugin.getServer().getWorld(args[0]) == null) {
                            player.sendMessage(plugin.getPluginName() + "World does not exist!");
                            return true;
                        }
                        // check world is enabled for travel
                        if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                            player.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                            return true;
                        }
                        required = plugin.getConfig().getInt("tachyon_use.travel.world");
                        // only world specified (or incomplete setting)
                        worlds.add(args[0]);
                        l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
                    }
                    case 4 -> {
                        required = plugin.getConfig().getInt("tachyon_use.travel.coords");
                        // world, x, y, z specified
                        World w;
                        if (args[0].contains("~")) {
                            // relative location
                            w = player.getLocation().getWorld();
                        } else {
                            w = plugin.getServer().getWorld(args[0]);
                            if (w == null) {
                                player.sendMessage(plugin.getPluginName() + "World does not exist!");
                                return true;
                            }
                            // check world is enabled for travel
                            if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                                player.sendMessage(plugin.getPluginName() + "You cannot travel to this world using the Vortex Manipulator!");
                                return true;
                            }
                        }
                        double x;
                        double y;
                        double z;
                        try {
                            if (args[1].startsWith("~")) {
                                // get players current location
                                Location tl = player.getLocation();
                                double tx = tl.getX();
                                double ty = tl.getY();
                                double tz = tl.getZ();
                                // strip off the initial "~" and add to current position
                                x = tx + Double.parseDouble(args[1].substring(1));
                                y = ty + Double.parseDouble(args[2].substring(1));
                                z = tz + Double.parseDouble(args[3].substring(1));
                            } else {
                                x = Double.parseDouble(args[1]);
                                y = Double.parseDouble(args[2]);
                                z = Double.parseDouble(args[3]);
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage(plugin.getPluginName() + "Could not parse coordinates!");
                            return true;
                        }
                        l = new Location(w, x, y, z);
                        // check block has space for player
                        if (!l.getBlock().getType().equals(Material.AIR)) {
                            player.sendMessage(plugin.getPluginName() + "Destination block is not AIR! Adjusting...");
                            // get highest block at these coords
                            int highest = l.getWorld().getHighestBlockYAt(l);
                            l.setY(highest);
                        }
                    }
                    default -> {
                        required = plugin.getConfig().getInt("tachyon_use.travel.random");
                        // random
                        l = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
                    }
                }
                List<Player> players = new ArrayList<>();
                players.add(player);
                if (plugin.getConfig().getBoolean("allow.multiple")) {
                    for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                        if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                            players.add((Player) e);
                        }
                    }
                }
                int actual = required * players.size();
                if (!TVMUtils.checkTachyonLevel(uuid, actual)) {
                    player.sendMessage(plugin.getPluginName() + "You need at least " + actual + " tachyons to travel!");
                    return true;
                }
                if (l != null) {
                    player.sendMessage(plugin.getPluginName() + "Standby for Vortex travel...");
                    while (!l.getChunk().isLoaded()) {
                        l.getChunk().load();
                    }
                    TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid, -actual);
                } else {
                    //close(player);
                    player.sendMessage(plugin.getPluginName() + "No location could be found within those parameters.");
                }
                // do stuff
                return true;
            } else {
                player.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }

    public boolean containsIgnoreCase(String str, List<String> list) {
        for (String s : list) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
