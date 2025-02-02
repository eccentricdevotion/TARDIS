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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.List;

public class TARDISBiomeCommand {

    public boolean reset(CommandSender sender) {
        if (sender instanceof Player player) {
            Chunk chunk = player.getLocation().getChunk();
            int cx = chunk.getX() * 16;
            int cz = chunk.getZ() * 16;
            for (int x = 0; x <= 15; x++) {
                for (int z = 0; z <= 15; z++) {
                    for (int y = 64; y < 81; y++) {
                        chunk.getWorld().setBiome(cx + x, y, cz + z, org.bukkit.block.Biome.THE_VOID);
                    }
                }
            }
            ServerLevel w = ((CraftWorld) chunk.getWorld()).getHandle();
            CraftChunk craftChunk = (CraftChunk) chunk;
            w.getChunkSource().chunkMap.resendBiomesForChunks(List.of(craftChunk.getHandle(ChunkStatus.BIOMES)));
        }
        return true;
    }

    public boolean getName(CommandSender sender) {
        if (sender instanceof Player player) {
            String biome = TARDISStringUtils.capitalise(player.getLocation().getBlock().getBiome().getKey().getKey());
            TARDIS.plugin.getMessenger().message(player, "Biome: " + biome);
        }
        return true;
    }
}
