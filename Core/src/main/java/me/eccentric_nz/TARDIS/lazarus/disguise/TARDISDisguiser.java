/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class TARDISDisguiser {

    private static final boolean nameVisible = false;
    private final Player player;
    private Object[] options;
    private EntityType entityType;
    private Entity entity;

    public TARDISDisguiser(Player player) {
        this.player = player;
    }

    public TARDISDisguiser(EntityType entityType, Player player) {
        this.entityType = entityType;
        this.player = player;
        options = null;
        createDisguise();
    }

    public TARDISDisguiser(EntityType entityType, Player player, Object[] options) {
        this.entityType = entityType;
        this.player = player;
        this.options = options;
        createDisguise();
    }

    public static void disguiseToPlayer(Player to, org.bukkit.World world) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == world) {
                if (TARDISDisguiseTracker.DISGUISED_AS_PLAYER.contains(p.getUniqueId())) {
                    TARDISPlayerDisguiser.disguiseToPlayer(p, to);
                }
                if (TARDISDisguiseTracker.DISGUISED_AS_MOB.containsKey(p.getUniqueId())) {
                    TARDISDisguise disguise = TARDISDisguiseTracker.DISGUISED_AS_MOB.get(p.getUniqueId());
                    Entity mob = TARDISDisguise.createMobDisguise(disguise, world);
                    if (mob != null) {
                        // set location
                        setEntityLocationIdAndName(mob, p.getLocation(), p);
                        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(p.getEntityId());
                        ServerLevel level = ((CraftWorld) world).getHandle();
                        level.addFreshEntity(mob, CreatureSpawnEvent.SpawnReason.CUSTOM);
                        ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket(mob, 0, mob.blockPosition());
                        ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
                        ServerGamePacketListenerImpl connection = ((CraftPlayer) to).getHandle().connection;
                        connection.send(packetPlayOutEntityDestroy);
                        connection.send(packetPlayOutSpawnLivingEntity);
                        connection.send(packetPlayOutEntityMetadata);
                    }
                }
            }
        }
    }

    public static void redisguise(Player player, org.bukkit.World world) {
        TARDISDisguise disguise = TARDISDisguiseTracker.DISGUISED_AS_MOB.get(player.getUniqueId());
        Entity mob = TARDISDisguise.createMobDisguise(disguise, world);
        if (mob != null) {
            // set location
            setEntityLocationIdAndName(mob, player.getLocation(), player);
            TARDISDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TARDISDisguise(disguise.entityType(), disguise.options()));
            ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
            ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket(mob, 0, mob.blockPosition());
            ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != player && player.getWorld() == p.getWorld()) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                    connection.send(packetPlayOutEntityDestroy);
                    connection.send(packetPlayOutSpawnLivingEntity);
                    connection.send(packetPlayOutEntityMetadata);
                }
            }
        }
    }

    private static void setEntityLocationIdAndName(Entity entity, Location location, Player player) {
        entity.setPos(location.getX(), location.getY(), location.getZ());
        entity.setId(player.getEntityId());
        if (TARDISDisguiser.nameVisible) {
            entity.setCustomName(Component.literal(player.getDisplayName()));
            entity.setCustomNameVisible(true);
        }
        entity.setYRot(fixYaw(location.getYaw()));
        entity.setXRot(location.getPitch());
        Mob insentient = (Mob) entity;
        insentient.setNoAi(true);
    }

    private static float fixYaw(float yaw) {
        return yaw * 256.0F / 360.0F;
    }

    private void createDisguise() {
        if (entityType != null) {
            Location location = player.getLocation();
            TARDISDisguise disguise = new TARDISDisguise(entityType, options);
            entity = TARDISDisguise.createMobDisguise(disguise, location.getWorld());
            if (entity != null) {
                setEntityLocationIdAndName(entity, location, player);
            }
        }
    }

    public void removeDisguise() {
        if (TARDISDisguiseTracker.DISGUISED_AS_PLAYER.contains(player.getUniqueId())) {
            new TARDISPlayerDisguiser(player, player.getUniqueId());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(player.getUniqueId()), 5L);
        } else {
            TARDISDisguiseTracker.DISGUISED_AS_MOB.remove(player.getUniqueId());
            ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
            ServerPlayer cp = ((CraftPlayer) player).getHandle();
            ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(cp, 0, cp.blockPosition());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != player && player.getWorld() == p.getWorld()) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                    connection.send(packetPlayOutEntityDestroy);
                    connection.send(packetPlayOutNamedEntitySpawn);
                }
            }
        }
    }

    public void disguiseToAll() {
        TARDISDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TARDISDisguise(entityType, options));
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket(entity, 0, entity.blockPosition());
        ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData().getNonDefaultValues());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player && player.getWorld() == p.getWorld()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                connection.send(packetPlayOutEntityDestroy);
                connection.send(packetPlayOutSpawnLivingEntity);
                connection.send(packetPlayOutEntityMetadata);
            }
        }
    }
}
