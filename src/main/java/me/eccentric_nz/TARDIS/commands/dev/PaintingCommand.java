package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class PaintingCommand {

    private final TARDIS plugin;

    public PaintingCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean getLocation(Player player) {
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.PAINTING);
        if (result == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_STAND");
            return true;
        }
        Painting painting = (Painting) result.getHitEntity();
        if (painting != null) {
            Location sponge = new Location(painting.getWorld(), 60, 3, 103);
            Art art = painting.getArt();
            plugin.debug("Art: " + art.assetId().asString());
            Location location = painting.getLocation();
            plugin.debug(String.format("Location: %s, %s, %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            plugin.debug(String.format("Sponge: %s, %s, %s", sponge.getBlockX() - location.getBlockX(), sponge.getBlockY() - location.getBlockY(), sponge.getBlockZ() - location.getBlockZ()));
            plugin.debug(String.format("Relative: %s, %s, %s", location.getBlockX() - sponge.getBlockX(), location.getBlockY() - sponge.getBlockY(), location.getBlockZ() - sponge.getBlockZ()));
            BlockFace face = painting.getFacing();
            plugin.debug("Facing: " + face);
        }
        return true;
    }
}
