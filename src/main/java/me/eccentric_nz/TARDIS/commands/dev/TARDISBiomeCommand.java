package me.eccentric_nz.TARDIS.commands.dev;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
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
}
