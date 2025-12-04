package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import me.eccentric_nz.tardisweepingangels.utils.TeamAdder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        Entity entity = null;
        // get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // bounding box of the given player
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (entity == null || entity.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    entity = target;
                }
            }
        }
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
