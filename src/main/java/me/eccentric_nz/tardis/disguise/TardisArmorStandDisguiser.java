/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.disguise;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TardisArmorStandDisguiser {

    private final ArmorStand stand;
    private final Object[] options;
    private final EntityType entityType;
    private Entity entity;

    public TardisArmorStandDisguiser(ArmorStand armorStand, EntityType entityType, Object[] options) {
        this.stand = armorStand;
        this.entityType = entityType;
        this.options = options;
        createDisguise();
    }

    /**
     * Disguises an armour stand as another entity.
     *
     * @param armorStand the armour stand to disguise
     * @param entityType the entity type to disguise as
     * @param options    an array of entity options
     */
    public static void disguiseArmourStand(ArmorStand armorStand, EntityType entityType, Object[] options) {
        new TardisArmorStandDisguiser(armorStand, entityType, options).disguiseToAll();
    }

    /**
     * Undisguises an armour stand.
     *
     * @param armorStand the armour stand to undisguise
     */
    public static void undisguiseArmourStand(ArmorStand armorStand) {
        TardisArmorStandDisguiser.removeDisguise(armorStand);
    }

    public static void disguiseToPlayer(Player to, org.bukkit.World world) {
        for (Map.Entry<UUID, TardisDisguise> map : TardisDisguiseTracker.DISGUISED_ARMOR_STANDS.entrySet()) {
            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(map.getKey());
            if (armorStand != null && armorStand.getWorld() == world) {
                Entity entity = TardisDisguise.createMobDisguise(map.getValue(), world);
                if (entity != null) {
                    // set location
                    setEntityLocationIdAndName(entity, armorStand.getLocation(), armorStand);
                    PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(armorStand.getEntityId());
                    PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
                    PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
                    PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(entity.getId(), (byte) entity.getYRot(), (byte) entity.getXRot(), true);
                    PlayerConnection connection = ((CraftPlayer) to).getHandle().b; // b = playerConnection
                    connection.sendPacket(packetPlayOutEntityDestroy);
                    connection.sendPacket(packetPlayOutSpawnEntityLiving);
                    connection.sendPacket(packetPlayOutEntityMetadata);
                    connection.sendPacket(packetPlayOutEntityLook);
                }
            }
        }
    }

    public static void redisguise(ArmorStand armorStand, org.bukkit.World world) {
        TardisDisguise disguise = TardisDisguiseTracker.DISGUISED_AS_MOB.get(armorStand.getUniqueId());
        Entity entity = TardisDisguise.createMobDisguise(disguise, world);
        if (entity != null) {
            // set location
            setEntityLocationIdAndName(entity, armorStand.getLocation(), armorStand);
            TardisDisguiseTracker.DISGUISED_AS_MOB.put(armorStand.getUniqueId(), new TardisDisguise(disguise.getEntityType(), disguise.getOptions()));
            PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(armorStand.getEntityId());
            PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
            PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
            PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(entity.getId(), (byte) entity.getYRot(), (byte) entity.getXRot(), true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (armorStand.getWorld() == player.getWorld()) {
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().b; // b = playerConnection
                    connection.sendPacket(packetPlayOutEntityDestroy);
                    connection.sendPacket(packetPlayOutSpawnEntityLiving);
                    connection.sendPacket(packetPlayOutEntityMetadata);
                    connection.sendPacket(packetPlayOutEntityLook);
                }
            }
        }
    }

    private static void setEntityLocationIdAndName(Entity entity, Location location, ArmorStand armorStand) {
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        entity.e(armorStand.getEntityId());
        float fixed = fixYaw(location.getYaw());
        entity.setHeadRotation(fixed);
        entity.h(fixed);
        entity.setYRot(fixed);
        entity.setXRot(location.getPitch());
        EntityInsentient insentient = (EntityInsentient) entity;
        insentient.setNoAI(true);
    }

    private static float fixYaw(float yaw) {
        return yaw * 256.0F / 360.0F;
    }

    public static void removeDisguise(ArmorStand armorStand) {
        TardisDisguiseTracker.DISGUISED_ARMOR_STANDS.remove(armorStand.getUniqueId());
        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(armorStand.getEntityId());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (armorStand.getWorld() == player.getWorld()) {
                ((CraftPlayer) player).getHandle().b.sendPacket(packetPlayOutEntityDestroy); // b = playerConnection
            }
        }
    }

    private void createDisguise() {
        if (entityType != null) {
            Location location = stand.getLocation();
            TardisDisguise disguise = new TardisDisguise(entityType, options);
            entity = TardisDisguise.createMobDisguise(disguise, location.getWorld());
            if (entity != null) {
                setEntityLocationIdAndName(entity, location, stand);
            }
        }
    }

    public void disguiseToAll() {
        TardisDisguiseTracker.DISGUISED_AS_MOB.put(stand.getUniqueId(), new TardisDisguise(entityType, options));
        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(stand.getEntityId());
        PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
        PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(entity.getId(), (byte) entity.getYRot(), (byte) entity.getXRot(), true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (stand.getWorld() == player.getWorld()) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().b; // b = playerConnection
                connection.sendPacket(packetPlayOutEntityDestroy);
                connection.sendPacket(packetPlayOutSpawnEntityLiving);
                connection.sendPacket(packetPlayOutEntityMetadata);
                connection.sendPacket(packetPlayOutEntityLook);
            }
        }
    }
}
