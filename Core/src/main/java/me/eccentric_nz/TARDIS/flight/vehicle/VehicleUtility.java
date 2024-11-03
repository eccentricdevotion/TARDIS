package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisModel;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleUtility {

    public static boolean isNotFlightReady(Location location) {
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.5d, 1.5d, 1.5d, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
            if (e instanceof ArmorStand as) {
                TARDIS.plugin.debug("Found armour stand");
                if (((CraftArmorStand) as).getHandle() instanceof TARDISArmourStand) {
                    TARDIS.plugin.debug("Found TARDISArmourStand");
                    return false;
                } else {
                    convertStand(as);
                    return false;
                }
            }
        }
        return true;
    }

    public static TARDISArmourStand spawnStand(Location location) {
        // spawn a custom armour stand at a block location
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        TARDISArmourStand entity = new TARDISArmourStand(net.minecraft.world.entity.EntityType.ARMOR_STAND, world);
        entity.setPosRaw(location.getX() + 0.5d, location.getY(), location.getZ() + 0.5d);
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity;
    }

    public static void convertStand(Location location, ChameleonPreset preset, BuildData data) {
        // get the existing stand
        ArmorStand old = null;
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.5d, 1.5d, 1.5d, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
            if (e instanceof ArmorStand a) {
                old = a;
            }
        }
        if (old != null) {
            // set helmet
            TARDISBuilderUtility.setPoliceBoxHelmet(TARDIS.plugin, preset, data, old);
            convertStand(old);
        }
    }

    public static void convertStand(ArmorStand old) {
        Location location = old.getLocation();
        // spawn a custom armour stand at the location
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        TARDISArmourStand entity = new TARDISArmourStand(net.minecraft.world.entity.EntityType.ARMOR_STAND, world);
        entity.setPosRaw(location.getX(), location.getY(), location.getZ());
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        ArmorStand stand = (ArmorStand) entity.getBukkitEntity();
        ItemStack is = old.getEquipment().getHelmet();
        stand.getEquipment().setHelmet(is, true);
        old.remove();
    }
}
