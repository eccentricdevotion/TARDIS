package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.utils.TeamAdder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class TeamCommand {

    private final TARDIS plugin;

    public TeamCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean join(CommandSender sender) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
            return true;
        }
        // get the entity the player is targeting
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 8.0d);
        if (result == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_TEAM_LOOK");
            return true;
        }
        Entity entity = result.getHitEntity();
        if (entity != null) {
            // add entity to the team
            TeamAdder.joinTeam(entity);
            plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_TEAM");
        } else {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_TEAM_LOOK");
            return true;
        }
        return true;
    }
}
