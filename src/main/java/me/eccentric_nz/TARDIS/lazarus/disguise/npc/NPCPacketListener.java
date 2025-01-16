package me.eccentric_nz.TARDIS.lazarus.disguise.npc;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class NPCPacketListener extends ServerGamePacketListenerImpl {

    public NPCPacketListener(MinecraftServer minecraftserver, Connection networkmanager, ServerPlayer entityplayer, CommonListenerCookie cookie) {
        super(minecraftserver, networkmanager, entityplayer, cookie);
    }

    @Override
    public void resumeFlushing() {
    }

    @Override
    public void send(Packet<?> packet) {
    }
}
