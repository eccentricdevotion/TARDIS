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
package me.eccentric_nz.tardischunkgenerator.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftItemFrame;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
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
            ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(fake, fake.getDirection().ordinal(), fake.blockPosition());
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
