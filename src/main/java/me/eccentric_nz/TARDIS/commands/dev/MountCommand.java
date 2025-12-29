package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

public class MountCommand {

    private final TARDIS plugin;

    public MountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean test(Player player, boolean mount) {
        if (mount) {
            // get the armour stand the player is looking at
            Location observerPos = player.getEyeLocation();
            RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ARMOR_STAND);
            if (result == null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_STAND");
                return true;
            }
            ArmorStand as = (ArmorStand) result.getHitEntity();
            if (as != null) {
                ItemStack box = ItemStack.of(Material.BLUE_DYE, 1);
                ItemMeta im = box.getItemMeta();
                im.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
                box.setItemMeta(im);
                ItemDisplay display = VehicleUtility.getItemDisplay(player, box, 1.75f);
                int period = 40;
                plugin.getTrackerKeeper().setAnimateTask(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, period), 5, period));
                as.addPassenger(player);
            }
        } else {
            // unmount
            for (Entity e : player.getPassengers()) {
                e.eject();
                e.remove();
            }
            Entity vehicle = player.getVehicle();
            vehicle.eject();
            plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getAnimateTask());
        }
        return true;
    }
}
