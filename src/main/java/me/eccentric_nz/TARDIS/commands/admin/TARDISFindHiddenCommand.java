package me.eccentric_nz.TARDIS.commands.admin;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Hidden;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFindHidden;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

public class TARDISFindHiddenCommand {

    public boolean search(TARDIS plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Location location = player.getLocation();
            int radius = 16;
            if (args.length > 1) {
                int parsed = TARDISNumberParsers.parseInt(args[1]);
                if (parsed > 0) {
                    radius = parsed;
                }
            }
            ResultSetFindHidden rsfh = new ResultSetFindHidden(plugin, radius);
            List<Hidden> data = rsfh.search(location, radius);
            if (data.size() > 0) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HIDDEN_FOUND");
                int i = 1;
                for (Hidden h : data) {
                    plugin.getMessenger().message(player, i
                            + ". X=" + h.getX()
                            + ", Y=" + h.getY()
                            + ", Z=" + h.getZ()
                            + ", owned by " + h.getOwner()
                            + ", " + h.getStatus());
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HIDDEN_NONE");
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
        }
        return true;
    }
}
