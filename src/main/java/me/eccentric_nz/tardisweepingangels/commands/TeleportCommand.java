package me.eccentric_nz.tardisweepingangels.commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

public class TeleportCommand {

    private final TARDIS plugin;

    public TeleportCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean add(CommandSender sender, String[] args) {
        String message = "Added a weeping angels' teleport location successfully.";
        String a = args[1].toLowerCase();
        if (a.equals("true") || a.equals("false")) {
            message = "Set 'angels.teleport_to_location' to " + args[1];
            plugin.getMonstersConfig().set("angels.teleport_to_location", Boolean.valueOf(a));
        } else if (sender instanceof Player player) {
            Location location = player.getLocation();
            String tpLoc = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
                    + location.getBlockZ();
            List<String> list;
            if (a.equals("replace")) {
                list = Arrays.asList(tpLoc);
            } else {
                list = plugin.getMonstersConfig().getStringList("angels.teleport_locations");
                list.add(tpLoc);
            }
            plugin.getMonstersConfig().set("angels.teleport_locations", list);
        }
        try {
            String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            plugin.getMonstersConfig().save(new File(monstersPath));
            plugin.getMessenger().message(sender, TardisModule.MONSTERS,
                    message);
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
        return true;
    }
}
