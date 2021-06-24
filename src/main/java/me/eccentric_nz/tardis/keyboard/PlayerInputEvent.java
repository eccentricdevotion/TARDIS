/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.keyboard;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerInputEvent extends PlayerEvent {

    public static HandlerList handlerList = new HandlerList();

    public PlayerInputEvent(PacketPlayInUpdateSign packet, Player player) {
        super(player);
        // This is where your code goes
        updateSign(player, packet);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public void updateSign(Player player, PacketPlayInUpdateSign packet) {
        EntityPlayer entityPlayer = ((CraftPlayer) Objects.requireNonNull(player.getPlayer())).getHandle();
        entityPlayer.resetIdleTimer();
        WorldServer worldServer = entityPlayer.getWorldServer();
        BlockPosition blockPosition = packet.b();
        if (worldServer.isLoaded(blockPosition)) {
            IBlockData iBlockData = worldServer.getType(blockPosition);
            TileEntity tileEntity = worldServer.getTileEntity(blockPosition);
            if (!(tileEntity instanceof TileEntitySign tileEntitySign)) {
                return;
            }
            tileEntitySign.isEditable = true;
            String[] lines = packet.c();
            for (int i = 0; i < lines.length; ++i) {
                tileEntitySign.a(i, new ChatComponentText(lines[i]));
            }
            tileEntitySign.update();
            worldServer.notify(blockPosition, iBlockData, iBlockData, 3);
            SignChangeEvent event = new SignChangeEvent(player.getWorld().getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()), player, lines);
            Bukkit.getPluginManager().callEvent(event);
        }
    }
}
