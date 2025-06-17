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
package me.eccentric_nz.TARDIS.skins;

import com.mojang.authlib.GameProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.event.player.PlayerHideEntityEvent;
import org.bukkit.event.player.PlayerShowEntityEvent;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ProfileChanger {

    public static void setPlayerProfile(CraftPlayer cp, GameProfile profile) {
        ServerPlayer self = cp.getHandle();
        if (!self.sentListPacket) {
            setGameProfile(self, profile);
            return;
        }
        List<ServerPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        // first unregister the player for all players with the old game profile
        for (ServerPlayer player : players) {
            CraftPlayer bukkitPlayer = player.getBukkitEntity();
            if (bukkitPlayer.canSee(cp)) {
                untrackAndHideEntity(bukkitPlayer, self);
            }
        }
        // set the game profile here, we should have unregistered the entity via iterating all player entities above
        setGameProfile(self, profile);
        // re-register the game profile for all players
        for (ServerPlayer player : players) {
            CraftPlayer bukkitPlayer = player.getBukkitEntity();
            if (bukkitPlayer.canSee(cp)) {
                trackAndShowEntity(bukkitPlayer, self, profile.getId());
            }
        }
        // refresh misc player things after sending game profile
        refreshPlayer(self, cp.getLocation());
    }

    private static void setGameProfile(ServerPlayer player, GameProfile profile) {
        try {
            // set GameProfile accessible
            Field gpField = net.minecraft.world.entity.player.Player.class.getDeclaredField("cV"); // cy = GameProfile
            gpField.setAccessible(true);
            gpField.set(player, profile);
            gpField.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException ignored) {
            TARDIS.plugin.debug("Failed to set GameProfile");
        }
    }

    private static void untrackAndHideEntity(CraftPlayer cp, ServerPlayer other) {
        Bukkit.getPluginManager().callEvent(new PlayerHideEntityEvent(cp, other.getBukkitEntity()));
        ServerPlayer sp = cp.getHandle();
        // remove this entity from the hidden player's EntityTrackerEntry
        ChunkMap tracker = ((ServerLevel) sp.level()).getChunkSource().chunkMap;
        ChunkMap.TrackedEntity entry = tracker.entityMap.get(other.getId());
        if (entry != null) {
            entry.removePlayer(sp);
        }
        // remove the hidden entity from this player user list, if they're on it
        if (other.sentListPacket) {
            sp.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(other.getUUID())));
        }
    }

    private static void trackAndShowEntity(CraftPlayer cp, ServerPlayer other, UUID uuidOverride) {
        ServerPlayer sp = cp.getHandle();
        ChunkMap tracker = ((ServerLevel) sp.level()).getChunkSource().chunkMap;
        // uuid override
        UUID original = null;
        if (uuidOverride != null) {
            original = other.getUUID();
            other.setUUID(uuidOverride);
        }
        sp.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(other)));
        if (original != null) {
            other.setUUID(original);
        }
        ChunkMap.TrackedEntity entry = tracker.entityMap.get(other.getId());
        if (entry != null && !entry.seenBy.contains(sp.connection)) {
            entry.updatePlayer(sp);
        }
        Bukkit.getServer().getPluginManager().callEvent(new PlayerShowEntityEvent(cp, other.getBukkitEntity()));
    }

    private static void refreshPlayer(ServerPlayer sp, Location loc) {
        // respawn the player then update their position and selected slot
        ServerLevel worldserver = sp.level();
        sp.connection.send(new ClientboundRespawnPacket(sp.createCommonSpawnInfo(worldserver), ClientboundRespawnPacket.KEEP_ALL_DATA));
        sp.onUpdateAbilities();
        sp.connection.send(new ClientboundPlayerPositionPacket(0, new PositionMoveRotation(new Vec3(loc.getX(), loc.getY(), loc.getZ()),new Vec3(loc.getX(), loc.getY(), loc.getZ()), loc.getYaw(), loc.getPitch()), Collections.emptySet()));
        PlayerList playerList = sp.server.getPlayerList();
        playerList.sendPlayerPermissionLevel(sp);
        playerList.sendLevelInfo(sp, worldserver);
        playerList.sendAllPlayerInfo(sp);
        // Resend their XP and effects because the respawn packet resets it
        sp.connection.send(new ClientboundSetExperiencePacket(sp.experienceProgress, sp.totalExperience, sp.experienceLevel));
        for (MobEffectInstance mobEffect : sp.getActiveEffects()) {
            sp.connection.send(new ClientboundUpdateMobEffectPacket(sp.getId(), mobEffect, false));
        }
    }
}
