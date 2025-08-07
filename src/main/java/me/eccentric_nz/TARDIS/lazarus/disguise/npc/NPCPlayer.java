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

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;

public class NPCPlayer extends ServerPlayer {

    public NPCPlayer(MinecraftServer minecraftserver, ServerLevel world, GameProfile gameprofile, ClientInformation clientinformation, Location location) {
        super(minecraftserver, world, gameprofile, clientinformation);
        setPos(location.getX(), location.getY(), location.getZ());
        setXRot(Math.clamp(location.getPitch(), -90.0F, 90.0F) % 360.0F);
        setYRot(location.getYaw() % 360);
        setYHeadRot(location.getYaw() % 360);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        ServerEntity serverEntity = new ServerEntity(serverPlayer.level(), serverPlayer, 0, false, packet -> {
        }, (packet, list) -> {}, Set.of());
        // spawn packets
        this.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this));
        this.connection.send(new ClientboundAddEntityPacket(this, serverEntity));
        this.connection.send(new ClientboundRotateHeadPacket(this, (byte) ((this.yRotO * 256f) / 360f)));
        this.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(uuid)));
    }
}
