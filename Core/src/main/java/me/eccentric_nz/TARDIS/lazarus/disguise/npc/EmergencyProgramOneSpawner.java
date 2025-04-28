/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus.disguise.npc;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import me.eccentric_nz.TARDIS.skins.ProfileChanger;
import me.eccentric_nz.TARDIS.skins.SkinFetcher;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.CraftServer;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EmergencyProgramOneSpawner {

    private final Player player;
    private final Location location;
    private int id = -1;

    public EmergencyProgramOneSpawner(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public int create() {
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, player.getUniqueId());
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
                    // set the skin to the player's
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    gameProfile.getProperties().removeAll("textures");
                    gameProfile.getProperties().put("textures", new Property("textures", value, signature));
                    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                    ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
                    ClientInformation info = ClientInformation.createDefault();
                    NPCPlayer npc = new NPCPlayer(server, world, gameProfile, ClientInformation.createDefault(), location);
                    npc.connection = new NPCPacketListener(server, new NPCConnection(PacketFlow.CLIENTBOUND), npc, new CommonListenerCookie(gameProfile, 0, info, false));
                    world.addNewPlayer(npc);
                    SynchedEntityData dataWatcher = npc.getEntityData();
                    dataWatcher.set(net.minecraft.world.entity.player.Player.DATA_HEALTH_ID, 20F); // set max life
                    dataWatcher.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 0xFF); // set second skin layer
                    id = npc.getId();
                    ProfileChanger.setPlayerProfile(npc.getBukkitEntity(), gameProfile);
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
                    npc.setInvulnerable(true);
                    showAll(npc, npc.getBukkitEntity().getLocation());
                }
            }
        });
        return id;
    }

    public void showAll(ServerPlayer npc, Location location) {
        TARDISDisguiseTracker.DISGUISED_NPCS.put(npc.getId(), player.getUniqueId());
        ServerEntity serverEntity = new ServerEntity(npc.serverLevel(), npc, 0, false, packet -> {
        }, (packet, list) -> {}, Set.of());
        List<Pair<EquipmentSlot, ItemStack>> equipment = List.of(
                new Pair<>(EquipmentSlot.MAINHAND, npc.getItemBySlot(EquipmentSlot.MAINHAND)),
                new Pair<>(EquipmentSlot.OFFHAND, npc.getItemBySlot(EquipmentSlot.OFFHAND)),
                new Pair<>(EquipmentSlot.HEAD, npc.getItemBySlot(EquipmentSlot.HEAD)),
                new Pair<>(EquipmentSlot.CHEST, npc.getItemBySlot(EquipmentSlot.CHEST)),
                new Pair<>(EquipmentSlot.LEGS, npc.getItemBySlot(EquipmentSlot.LEGS)),
                new Pair<>(EquipmentSlot.FEET, npc.getItemBySlot(EquipmentSlot.FEET))
        );
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(npc.getId(), equipment);
        ClientboundPlayerInfoUpdatePacket playerInfoAdd = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
        ClientboundAddEntityPacket namedEntitySpawn = new ClientboundAddEntityPacket(npc, serverEntity);
        ClientboundRotateHeadPacket headRotation = new ClientboundRotateHeadPacket(npc, (byte) ((location.getYaw() * 256f) / 360f));
        ClientboundPlayerInfoRemovePacket playerInfoRemove = new ClientboundPlayerInfoRemovePacket(List.of(npc.getUUID()));
        ClientboundSetEntityDataPacket dataPacket = null;
        List<SynchedEntityData.DataValue<?>> metas = npc.getEntityData().getNonDefaultValues();
        if (metas != null && !metas.isEmpty()) {
            dataPacket = new ClientboundSetEntityDataPacket(npc.getId(), metas);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == npc.getBukkitEntity().getWorld()) {
                ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
                connection.send(playerInfoAdd);
                connection.send(namedEntitySpawn);
                connection.send(headRotation);
                connection.send(playerInfoRemove);
                connection.send(equipmentPacket);
                if (dataPacket != null) {
                    connection.send(dataPacket);
                }
            }
        }
    }
}
