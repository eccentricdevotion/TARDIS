/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRegionFlagCommand {

    private final TARDIS plugin;
    private final List<String> which = Arrays.asList("entry", "exit");

    public TARDISRegionFlagCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleEntryExit(CommandSender sender, String[] args) {
        String flag = args[1].toLowerCase();
        if (!which.contains(flag)) {
            TARDISMessage.message(sender, "You need to specify which flag type you want to change to - entry or exit.");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            TARDISMessage.message(sender, "This command only works if you are using a default world for TARDISes.");
            return true;
        }
        String world_name = plugin.getConfig().getString("creation.default_world_name");
        // get all regions for the default world
        List<String> world_regions = plugin.getWorldGuardUtils().getTARDISRegions(plugin.getServer().getWorld(world_name));
        for (String region_id : world_regions) {
            if (flag.endsWith("entry")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world_name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " entry -w " + world_name + " -g nonmembers deny");
            } else {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " entry -w " + world_name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world_name + " -g everyone deny");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " chest-access -w " + world_name);
            }
        }
        return true;
    }
}
