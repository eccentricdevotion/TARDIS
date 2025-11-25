package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MountCommand {

    private final TARDIS plugin;

    public MountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean test(Player player, boolean mount) {
        if (mount) {
            // get the armour stand the player is looking at
            Location observerPos = player.getEyeLocation();
            TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
            TARDISVector3D observerStart = new TARDISVector3D(observerPos);
            TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
            ArmorStand as = null;
            // Get nearby entities
            for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
                // Bounding box of the given player
                TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
                TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
                TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                if (target.getType().equals(EntityType.ARMOR_STAND) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                    if (as == null || as.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                        as = (ArmorStand) target;
                    }
                }
            }
            if (as == null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_STAND");
                return true;
            }
            ItemStack box = ItemStack.of(Material.BLUE_DYE, 1);
            ItemMeta im = box.getItemMeta();
            im.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
            box.setItemMeta(im);
            ItemDisplay display = VehicleUtility.getItemDisplay(player, box, 1.75f);
            int period = 40;
            plugin.getTrackerKeeper().setAnimateTask(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, period), 5, period));
            as.addPassenger(player);
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
