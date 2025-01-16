package me.eccentric_nz.TARDIS.lazarus.disguise.npc;

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
    public void send(Packet<?> packet, PacketSendListener genericfuturelistener) {
    }

    @Override
    public void send(Packet<?> packet, PacketSendListener genericfuturelistener, boolean flag) {
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
