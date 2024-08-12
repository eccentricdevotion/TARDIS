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
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;

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
        ServerEntity serverEntity = new ServerEntity(serverPlayer.serverLevel(), serverPlayer, 0, false, packet -> {
        }, Set.of());
        // spawn packets
        this.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this));
        this.connection.send(new ClientboundAddEntityPacket(this, serverEntity));
        this.connection.send(new ClientboundRotateHeadPacket(this, (byte) ((this.yRotO * 256f) / 360f)));
        this.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(uuid)));
    }
}
