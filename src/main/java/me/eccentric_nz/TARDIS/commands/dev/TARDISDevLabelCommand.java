package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.library.LibraryCatalogue;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISDevLabelCommand {

    private final TARDIS plugin;

    public TARDISDevLabelCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean catalog(CommandSender sender) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            // get start location
            if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "SCHM_NO_START");
                return true;
            }
            Location start = plugin.getTrackerKeeper().getStartLocation().get(uuid);
            new LibraryCatalogue().label(start);
        }
        return true;
    }
}
