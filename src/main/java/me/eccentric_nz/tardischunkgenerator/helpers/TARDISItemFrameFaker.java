package me.eccentric_nz.tardischunkgenerator.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TARDISItemFrameFaker {

    public static int cast(org.bukkit.entity.ItemFrame frame, Player player, Vector location) {
        int id = -1;
        if (player != null && player.isOnline()) {
            ItemFrame real = ((CraftItemFrame) frame).getHandle();
            Level world = ((CraftWorld) player.getWorld()).getHandle();
            ItemFrame fake = new ItemFrame(world, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), Direction.UP);
            fake.setDirection(Direction.UP);
            fake.setItem(real.getItem());
            fake.setRotation(real.getRotation());
            fake.setInvisible(true);
            fake.fixed = true;
            id = fake.getId();
            ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(fake, fake.getDirection().ordinal());
            ClientboundSetEntityDataPacket entityDataPacket = new ClientboundSetEntityDataPacket(id, real.getEntityData().getNonDefaultValues());
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(addEntityPacket);
            connection.send(entityDataPacket);
        }
        return id;
    }

    public static void remove(int id, Player player) {
        if (player != null && player.isOnline()) {
            ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(id);
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(removeEntitiesPacket);
        }
    }
}
