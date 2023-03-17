package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import org.bukkit.ChatColor;
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

public class TVMCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommand(TARDIS plugin) {
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
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "CMD_PLAYER");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
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
                        Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Vortex Manipulator");
                        vmg.setContents(gui);
                        player.openInventory(vmg);
                        return true;
                    }
                }
                if (args.length > 0 && args[0].equalsIgnoreCase("go")) {
                    if (args.length < 2) {
                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_NAME");
                        return true;
                    }
                    // check save exists
                    TVMResultSetWarpByName rsw = new TVMResultSetWarpByName(plugin, uuid, args[1]);
                    if (!rsw.resultSet()) {
                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NO_SAVE");
                        return true;
                    }
                    Location l = rsw.getWarp();
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_STANDBY_TO", args[1]);
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
                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", required);
                        return true;
                    }
                    TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid, -required);
                    return true;
                }

                Parameters params = new Parameters(player, Flag.getAPIFlags());
                int required;
                List<String> worlds = new ArrayList<>();
                Location l;
                switch (args.length) {
                    case 1, 2, 3 -> {
                        // check world is an actual world
                        if (plugin.getServer().getWorld(args[0]) == null) {
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                            return true;
                        }
                        // check world is enabled for travel
                        if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
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
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                                return true;
                            }
                            // check world is enabled for travel
                            if (!containsIgnoreCase(args[0], plugin.getTardisAPI().getWorlds())) {
                                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
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
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_COORDS");
                            return true;
                        }
                        l = new Location(w, x, y, z);
                        // check block has space for player
                        if (!l.getBlock().getType().equals(Material.AIR)) {
                            TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_ADJUST");
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
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", actual);
                    return true;
                }
                if (l != null) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_STANDY");
                    while (!l.getChunk().isLoaded()) {
                        l.getChunk().load();
                    }
                    TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid, -actual);
                } else {
                    //close(player);
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PARAMETERS");
                }
                // do stuff
                return true;
            } else {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_HAND");
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
