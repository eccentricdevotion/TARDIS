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
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TARDISEPSDisguiser {

    private final Player player;
    private final Location location;
    private ServerPlayer npc;

    public TARDISEPSDisguiser(Player player, Location location) {
        this.player = player;
        this.location = location;
        disguiseNPC();
    }

    public static void disguiseToPlayer(Player player, World world) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        for (Map.Entry<Integer, UUID> map : TARDISDisguiseTracker.DISGUISED_NPCS.entrySet()) {
            Entity tracked = nmsWorld.getEntity(map.getKey());
            if (tracked != null && tracked.level().getWorld() == world) {
                ServerPlayer entityPlayer = ((CraftPlayer) Bukkit.getOfflinePlayer(map.getValue())).getHandle();
                ServerPlayer npc = new ServerPlayer(server, nmsWorld, entityPlayer.getGameProfile(), ClientInformation.createDefault());
                // set location
                setEntityLocation(npc, new Location(world, tracked.getX(), tracked.getY(), tracked.getZ()));
                // send packets
                ClientboundPlayerInfoUpdatePacket packetPlayOutPlayerInfo = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
                ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(npc, 0, npc.blockPosition());
                ClientboundRotateHeadPacket packetPlayOutEntityHeadRotation = new ClientboundRotateHeadPacket(npc, (byte) npc.getYRot());
                ClientboundPlayerLookAtPacket packetPlayOutEntityLook = new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, npc.blockPosition().getX(), npc.blockPosition().getY(), npc.blockPosition().getZ());
                ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
                connection.send(packetPlayOutPlayerInfo);
                connection.send(packetPlayOutNamedEntitySpawn);
                connection.send(packetPlayOutEntityHeadRotation);
                connection.send(packetPlayOutEntityLook);
            }
        }
    }

    private static float fixYaw(float yaw) {
        return yaw * 256.0f / 360.0f;
    }

    private static void setEntityLocation(Entity entity, Location location) {
        entity.setPos(location.getX(), location.getY(), location.getZ());
        float fixed = fixYaw(location.getYaw());
        entity.setYHeadRot(fixed);
        entity.setYBodyRot(fixed);
        entity.setYRot(fixed);
        entity.setXRot(location.getPitch());
    }

    public static void removeNPC(int id, World world) {
        TARDISDisguiseTracker.DISGUISED_NPCS.remove(id);
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(id);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (world == p.getWorld()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                connection.send(packetPlayOutEntityDestroy);
            }
        }
    }

    public void disguiseNPC() {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // set skin
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        npc = new ServerPlayer(server, world, entityPlayer.getGameProfile(), ClientInformation.createDefault());
        // set location
        setEntityLocation(npc, location);
        // get Player equipment
        ItemStack feet = CraftItemStack.asNMSCopy(player.getInventory().getBoots());
        ItemStack legs = CraftItemStack.asNMSCopy(player.getInventory().getLeggings());
        ItemStack chest = CraftItemStack.asNMSCopy(player.getInventory().getChestplate());
        ItemStack head = CraftItemStack.asNMSCopy(player.getInventory().getHelmet());
        ItemStack mainHand = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        ItemStack offHand = CraftItemStack.asNMSCopy(player.getInventory().getItemInOffHand());
        // set NPC equipment
        npc.setItemSlot(EquipmentSlot.FEET, feet);
        npc.setItemSlot(EquipmentSlot.LEGS, legs);
        npc.setItemSlot(EquipmentSlot.CHEST, chest);
        npc.setItemSlot(EquipmentSlot.HEAD, head);
        npc.setItemSlot(EquipmentSlot.MAINHAND, mainHand);
        npc.setItemSlot(EquipmentSlot.OFFHAND, offHand);
    }

    public int showToAll() {
        TARDISDisguiseTracker.DISGUISED_NPCS.put(npc.getId(), player.getUniqueId());
        ServerEntity npcServerEntity = new ServerEntity(npc.serverLevel(), npc, 0, false, packet -> {
        }, Set.of());
        ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(npc, npcServerEntity);
        ClientboundRotateHeadPacket packetPlayOutEntityHeadRotation = new ClientboundRotateHeadPacket(npc, (byte) npc.getYRot());
        ClientboundPlayerLookAtPacket packetPlayOutEntityLook = new ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor.FEET, npc.blockPosition().getX(), npc.blockPosition().getY(), npc.blockPosition().getZ());
        ClientboundPlayerInfoUpdatePacket packetPlayOutPlayerInfo = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() == location.getWorld()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;
                connection.send(packetPlayOutNamedEntitySpawn);
                connection.send(packetPlayOutEntityHeadRotation);
                connection.send(packetPlayOutEntityLook);
                connection.send(packetPlayOutPlayerInfo);
            }
        }
        return npc.getId();
    }
}
