package me.eccentric_nz.TARDIS.commands.tardis;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TardisUtility {

    public static Player check(TARDIS plugin, CommandSender sender, TardisCommand tc) {
        if (sender instanceof Player player) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return null;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardisId()) && tc.noSiege()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CMD");
                return null;
            }
            return player;
        }
        return null;
    }
}
