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

import me.eccentric_nz.tardischunkgenerator.TardisHelperPlugin;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TardisDisguiser {

    private static final boolean NAME_VISIBLE = false;
    private final Player player;
    private Object[] options;
    private EntityType entityType;
    private Entity entity;

    public TardisDisguiser(Player player) {
        this.player = player;
    }

    public TardisDisguiser(EntityType entityType, Player player) {
        this.entityType = entityType;
        this.player = player;
        options = null;
        createDisguise();
    }

    public TardisDisguiser(EntityType entityType, Player player, Object[] options) {
        this.entityType = entityType;
        this.player = player;
        this.options = options;
        createDisguise();
    }

    /**
     * Disguises a player as another entity.
     *
     * @param entityType the entity type to disguise as
     * @param player     the player to disguise
     */
    public static void disguise(EntityType entityType, Player player) {
        new TardisDisguiser(entityType, player).disguiseToAll();
    }

    /**
     * Disguises a player as another entity.
     *
     * @param entityType the entity type to disguise as
     * @param player     the player to disguise
     * @param options    an array of entity options
     */
    public static void disguise(EntityType entityType, Player player, Object[] options) {
        new TardisDisguiser(entityType, player, options).disguiseToAll();
    }

    /**
     * Disguises a player as a randomly named Chameleon Arch player.
     *
     * @param player the player to disguise
     * @param name   the random name for the disguise
     */
    public static void disguise(Player player, String name) {
        new TardisChameleonArchDisguiser(player).changeSkin(name);
    }

    /**
     * Disguises a player as another player.
     *
     * @param player the player to disguise
     * @param uuid   the UUID of the player to disguise as
     */
    public static void disguise(Player player, UUID uuid) {
        new TardisPlayerDisguiser(player, uuid).disguiseToAll();
    }

    /**
     * Undisguises a player.
     *
     * @param player the player to undisguise
     */
    public static void undisguise(Player player) {
        new TardisDisguiser(player).removeDisguise();
    }

    /**
     * Undisguises a Chameleon arched player.
     *
     * @param player the player to undisguise
     */
    public static void reset(Player player) {
        new TardisChameleonArchDisguiser(player).resetSkin();
    }

    public static void disguiseToPlayer(Player to, org.bukkit.World world) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getWorld() == world) {
                if (TardisDisguiseTracker.DISGUISED_AS_PLAYER.contains(player.getUniqueId())) {
                    TardisPlayerDisguiser.disguiseToPlayer(player, to, TardisHelperPlugin.getTardisHelper());
                }
                if (TardisDisguiseTracker.DISGUISED_AS_MOB.containsKey(player.getUniqueId())) {
                    TardisDisguise disguise = TardisDisguiseTracker.DISGUISED_AS_MOB.get(player.getUniqueId());
                    Entity entity = TardisDisguise.createMobDisguise(disguise, world);
                    if (entity != null) {
                        // set location
                        setEntityLocationIdAndName(entity, player.getLocation(), player);
                        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(player.getEntityId());
                        PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
                        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
                        PlayerConnection connection = ((CraftPlayer) to).getHandle().playerConnection;
                        connection.sendPacket(packetPlayOutEntityDestroy);
                        connection.sendPacket(packetPlayOutSpawnEntityLiving);
                        connection.sendPacket(packetPlayOutEntityMetadata);
                    }
                }
            }
        }
    }

    public static void redisguise(Player player, org.bukkit.World world) {
        TardisDisguise disguise = TardisDisguiseTracker.DISGUISED_AS_MOB.get(player.getUniqueId());
        Entity entity = TardisDisguise.createMobDisguise(disguise, world);
        if (entity != null) {
            // set location
            setEntityLocationIdAndName(entity, player.getLocation(), player);
            TardisDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TardisDisguise(disguise.getEntityType(), disguise.getOptions()));
            PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(player.getEntityId());
            PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
            PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1 != player && player.getWorld() == player1.getWorld()) {
                    PlayerConnection connection = ((CraftPlayer) player1).getHandle().playerConnection;
                    connection.sendPacket(packetPlayOutEntityDestroy);
                    connection.sendPacket(packetPlayOutSpawnEntityLiving);
                    connection.sendPacket(packetPlayOutEntityMetadata);
                }
            }
        }
    }

    private static void setEntityLocationIdAndName(Entity entity, Location location, Player player) {
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        entity.e(player.getEntityId());
        if (TardisDisguiser.NAME_VISIBLE) {
            entity.setCustomName(new ChatMessage(player.getDisplayName()));
            entity.setCustomNameVisible(true);
        }
        entity.yaw = fixYaw(location.getYaw());
        entity.pitch = location.getPitch();
        EntityInsentient insentient = (EntityInsentient) entity;
        insentient.setNoAI(true);
    }

    private static float fixYaw(float yaw) {
        return yaw * 256.0F / 360.0F;
    }

    private void createDisguise() {
        if (entityType != null) {
            Location location = player.getLocation();
            TardisDisguise disguise = new TardisDisguise(entityType, options);
            entity = TardisDisguise.createMobDisguise(disguise, location.getWorld());
            if (entity != null) {
                setEntityLocationIdAndName(entity, location, player);
            }
        }
    }

    public void removeDisguise() {
        if (TardisDisguiseTracker.DISGUISED_AS_PLAYER.contains(player.getUniqueId())) {
            new TardisPlayerDisguiser(player, player.getUniqueId()).disguiseToAll();
            TardisDisguiseTracker.DISGUISED_AS_PLAYER.remove(player.getUniqueId());
        } else {
            TardisDisguiseTracker.DISGUISED_AS_MOB.remove(player.getUniqueId());
            PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(player.getEntityId());
            PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player != this.player && this.player.getWorld() == player.getWorld()) {
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(packetPlayOutEntityDestroy);
                    connection.sendPacket(packetPlayOutNamedEntitySpawn);
                }
            }
        }
    }

    public void disguiseToAll() {
        TardisDisguiseTracker.DISGUISED_AS_MOB.put(player.getUniqueId(), new TardisDisguise(entityType, options));
        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(player.getEntityId());
        PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != this.player && this.player.getWorld() == player.getWorld()) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(packetPlayOutEntityDestroy);
                connection.sendPacket(packetPlayOutSpawnEntityLiving);
                connection.sendPacket(packetPlayOutEntityMetadata);
            }
        }
    }
}
