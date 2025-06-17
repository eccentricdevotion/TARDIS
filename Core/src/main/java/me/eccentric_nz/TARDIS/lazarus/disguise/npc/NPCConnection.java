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

import io.netty.channel.ChannelFutureListener;
import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

import java.lang.reflect.Field;
import java.net.SocketAddress;

public class NPCConnection extends Connection {

    public NPCConnection(PacketFlow flag) {
        super(flag);
        channel = new NPCChannel(null);
        address = new SocketAddress() {};
    }

    @Override
    public PacketFlow getReceiving() {
        return PacketFlow.SERVERBOUND;
    }

    @Override
    public void flushChannel() {
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(Packet<?> packet) {
    }

    @Override
    public void send(Packet<?> packet, ChannelFutureListener channelfuturelistener) {
    }

    @Override
    public void send(Packet<?> packet, ChannelFutureListener channelfuturelistener, boolean flag) {
    }

    @Override
    public <T extends PacketListener> void setupInboundProtocol(ProtocolInfo<T> protocolinfo, T t0) {

    }

    @Override
    public void setListenerForServerboundHandshake(PacketListener pl) {
        try {
            Field field = Connection.class.getDeclaredField("q");
            field.setAccessible(true);
            field.set(this, pl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            TARDIS.plugin.getLogger().warning("Could not set packetListener for NPCConnection: " + e.getMessage());
        }
        try {
            Field field = Connection.class.getDeclaredField("p");
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            TARDIS.plugin.getLogger().warning("Could not set disconnectListener for NPCConnection: " + e.getMessage());
        }
    }
}
