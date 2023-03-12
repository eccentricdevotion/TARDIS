/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardischunkgenerator.disguise;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
                        ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket((LivingEntity) mob);
                        ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
                        Connection connection = ((CraftPlayer) to).getHandle().connection.connection;
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
            TARDISDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TARDISDisguise(disguise.getEntityType(), disguise.getOptions()));
            ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
            ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket((LivingEntity) mob);
            ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(mob.getId(), mob.getEntityData().getNonDefaultValues());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != player && player.getWorld() == p.getWorld()) {
                    Connection connection = ((CraftPlayer) p).getHandle().connection.connection;
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
            entity.setCustomName(MutableComponent.create(new LiteralContents(player.getDisplayName())));
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
            new TARDISPlayerDisguiser(player, player.getUniqueId()).disguiseToAll();
            TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(player.getUniqueId());
        } else {
            TARDISDisguiseTracker.DISGUISED_AS_MOB.remove(player.getUniqueId());
            ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
            ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(((CraftPlayer) player).getHandle());
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != player && player.getWorld() == p.getWorld()) {
                    Connection connection = ((CraftPlayer) p).getHandle().connection.connection;
                    connection.send(packetPlayOutEntityDestroy);
                    connection.send(packetPlayOutNamedEntitySpawn);
                }
            }
        }
    }

    public void disguiseToAll() {
        TARDISDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TARDISDisguise(entityType, options));
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        ClientboundAddEntityPacket packetPlayOutSpawnLivingEntity = new ClientboundAddEntityPacket((LivingEntity) entity);
        ClientboundSetEntityDataPacket packetPlayOutEntityMetadata = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData().getNonDefaultValues());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player && player.getWorld() == p.getWorld()) {
                Connection connection = ((CraftPlayer) p).getHandle().connection.connection;
                connection.send(packetPlayOutEntityDestroy);
                connection.send(packetPlayOutSpawnLivingEntity);
                connection.send(packetPlayOutEntityMetadata);
            }
        }
    }
}
