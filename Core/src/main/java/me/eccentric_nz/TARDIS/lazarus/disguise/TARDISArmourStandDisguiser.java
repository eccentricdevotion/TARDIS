/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus.disguise;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TARDISArmourStandDisguiser {

    private final ArmorStand stand;
    private final Object[] options;
    private final EntityType entityType;
    private Entity entity;

    public TARDISArmourStandDisguiser(ArmorStand stand, EntityType entityType, Object[] options) {
        this.stand = stand;
        this.entityType = entityType;
        this.options = options;
        createDisguise();
    }

    public static void disguiseToPlayer(Player to, org.bukkit.World world) {
        for (Map.Entry<UUID, TARDISDisguise> map : TARDISDisguiseTracker.DISGUISED_ARMOR_STANDS.entrySet()) {
            ArmorStand stand = (ArmorStand) Bukkit.getEntity(map.getKey());
            if (stand != null && stand.getWorld() == world) {
                Entity mob = TARDISDisguise.createMobDisguise(map.getValue(), world);
                if (mob != null) {
                    // set location
                    setEntityLocationIdAndName(mob, stand.getLocation(), stand);
                    ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(stand.getEntityId());
                    ClientboundAddEntityPacket packetPlayOutSpawnEntityLiving = new ClientboundAddEntityPacket(mob, 0, mob.blockPosition());
                    ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
                    ClientboundPlayerLookAtPacket packetPlayOutEntityLook = new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, mob.blockPosition().getX(), mob.blockPosition().getY(), mob.blockPosition().getZ());
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) to).getHandle().connection;
                    connection.send(packetPlayOutEntityDestroy);
                    connection.send(packetPlayOutSpawnEntityLiving);
                    connection.send(packetPlayOutEntityMetadata);
                    connection.send(packetPlayOutEntityLook);
                }
            }
        }
    }

    public static void redisguise(ArmorStand stand, org.bukkit.World world) {
        TARDISDisguise disguise = TARDISDisguiseTracker.DISGUISED_AS_MOB.get(stand.getUniqueId());
        Entity mob = TARDISDisguise.createMobDisguise(disguise, world);
        if (mob != null) {
            // set location
            setEntityLocationIdAndName(mob, stand.getLocation(), stand);
            TARDISDisguiseTracker.DISGUISED_AS_MOB.put(stand.getUniqueId(), new TARDISDisguise(disguise.getEntityType(), disguise.getOptions()));
            ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(stand.getEntityId());
            ClientboundAddEntityPacket packetPlayOutSpawnEntityLiving = new ClientboundAddEntityPacket(mob, 0, mob.blockPosition());
            ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
            ClientboundPlayerLookAtPacket packetPlayOutEntityLook = new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, mob.blockPosition().getX(), mob.blockPosition().getY(), mob.blockPosition().getZ());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (stand.getWorld() == p.getWorld()) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                    connection.send(packetPlayOutEntityDestroy);
                    connection.send(packetPlayOutSpawnEntityLiving);
                    connection.send(packetPlayOutEntityMetadata);
                    connection.send(packetPlayOutEntityLook);
                }
            }
        }
    }

    private static void setEntityLocationIdAndName(Entity entity, Location location, ArmorStand stand) {
        entity.setPos(location.getX(), location.getY(), location.getZ());
        entity.setId(stand.getEntityId());
        float fixed = fixYaw(location.getYaw());
        entity.setYHeadRot(fixed);
        entity.setYBodyRot(fixed);
        entity.setYRot(fixed);
        entity.setXRot(location.getPitch());
        Mob insentient = (Mob) entity;
        insentient.setNoAi(true);
    }

    private static float fixYaw(float yaw) {
        return yaw * 256.0F / 360.0F;
    }

    public static void removeDisguise(ArmorStand stand) {
        TARDISDisguiseTracker.DISGUISED_ARMOR_STANDS.remove(stand.getUniqueId());
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(stand.getEntityId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (stand.getWorld() == p.getWorld()) {
                ((CraftPlayer) p).getHandle().connection.send(packetPlayOutEntityDestroy);
            }
        }
    }

    private void createDisguise() {
        if (entityType != null) {
            Location location = stand.getLocation();
            TARDISDisguise disguise = new TARDISDisguise(entityType, options);
            entity = TARDISDisguise.createMobDisguise(disguise, location.getWorld());
            if (entity != null) {
                setEntityLocationIdAndName(entity, location, stand);
            }
        }
    }

    public void disguiseToAll() {
        TARDISDisguiseTracker.DISGUISED_AS_MOB.put(stand.getUniqueId(), new TARDISDisguise(entityType, options));
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(stand.getEntityId());
        ClientboundAddEntityPacket packetPlayOutSpawnEntityLiving = new ClientboundAddEntityPacket(entity, 0, entity.blockPosition());
        ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData().getNonDefaultValues());
        ClientboundPlayerLookAtPacket packetPlayOutEntityLook = new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (stand.getWorld() == p.getWorld()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                connection.send(packetPlayOutEntityDestroy);
                connection.send(packetPlayOutSpawnEntityLiving);
                connection.send(packetPlayOutEntityMetadata);
                connection.send(packetPlayOutEntityLook);
            }
        }
    }
}
