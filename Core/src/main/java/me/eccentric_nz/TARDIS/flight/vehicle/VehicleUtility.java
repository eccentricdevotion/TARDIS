package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

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
            ArmorStand stand = convertStand(old);
            // get interaction entity at the location
            Interaction interaction = TARDISDisplayItemUtils.getInteraction(location);
            if (interaction != null) {
                interaction.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.INTEGER, data.getTardisID());
                interaction.getPersistentDataContainer().set(TARDIS.plugin.getStandUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), stand.getUniqueId());
            }
        }
    }

    public static ArmorStand convertStand(ArmorStand old) {
        Location location = old.getLocation();
        // spawn a custom armour stand at the location
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        TARDISArmourStand entity = new TARDISArmourStand(net.minecraft.world.entity.EntityType.ARMOR_STAND, world);
        entity.setPosRaw(location.getBlockX() + 0.5d, location.getY(), location.getBlockZ() + 0.5d);
        world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        ArmorStand stand = (ArmorStand) entity.getBukkitEntity();
        ItemStack is = old.getEquipment().getHelmet();
        stand.getEquipment().setHelmet(is, true);
        stand.setRotation(location.getYaw(), 0.0f);
        old.remove();
        return stand;
    }

    public static ItemDisplay getItemDisplay(Player player, ItemStack box, float scale) {
        ItemDisplay display = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation().add(0, 1.5, 0), EntityType.ITEM_DISPLAY);
        display.setItemStack(box);
        player.addPassenger(display);
        Vector3f size = new Vector3f(scale, scale, scale);
        Vector3f position = new Vector3f(0, -1, 0);
        // set initial scale and position
        Transformation initial = new Transformation(
                position,
                TARDISConstants.AXIS_ANGLE_ZERO,
                size,
                TARDISConstants.AXIS_ANGLE_ZERO
        );
        display.setTransformation(initial);
        return display;
    }
}
