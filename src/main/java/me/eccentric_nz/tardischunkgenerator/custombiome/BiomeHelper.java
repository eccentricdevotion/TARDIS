package me.eccentric_nz.tardischunkgenerator.custombiome;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;

import java.util.List;

public class BiomeHelper {

    DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();

    /**
     * Set a chunk to a custom biome
     *
     * @param newBiomeName the name of the custom biome to set (such as tardis:skaro_lakes)
     * @param chunk        the chunk to set the biome for
     */
    public void setCustomBiome(String newBiomeName, Chunk chunk) {
        WritableRegistry<Biome> registryWritable = (WritableRegistry<Biome>) dedicatedServer.registryAccess().registry(Registries.BIOME).get();
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, new ResourceLocation(newBiomeName.toLowerCase()));
        Biome base = registryWritable.get(key);
        if (base == null) {
            if (newBiomeName.contains(":")) {
                ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, new ResourceLocation(newBiomeName.split(":")[0].toLowerCase(), newBiomeName.split(":")[1].toLowerCase()));
                base = registryWritable.get(newKey);
                if (base == null) {
                    return;
                }
            } else {
                return;
            }
        }
        Holder<Biome> biomeHolder = Holder.direct(base);
        Level w = ((CraftWorld) chunk.getWorld()).getHandle();
        for (int x = 0; x <= 15; x++) {
            for (int z = 0; z <= 15; z++) {
                for (int y = 0; y <= chunk.getWorld().getMaxHeight(); y++) {
                    setCustomBiome(chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z, w, biomeHolder);
                }
            }
        }
        refreshChunk(chunk);
    }

    /**
     * Set a location to a custom biome
     *
     * @param newBiomeName the name of the custom biome to set (such as tardis:skaro_lakes)
     * @param location     the location to set the biome for
     * @return true if the biome was set
     */
    public boolean setCustomBiome(String newBiomeName, Location location) {
        Biome base;
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) dedicatedServer.registryAccess().registry(Registries.BIOME).get();
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, new ResourceLocation(newBiomeName.toLowerCase()));
        base = registrywritable.get(key);
        if (base == null) {
            if (newBiomeName.contains(":")) {
                ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, new ResourceLocation(newBiomeName.split(":")[0].toLowerCase(), newBiomeName.split(":")[1].toLowerCase()));
                base = registrywritable.get(newKey);
                if (base == null) {
                    return false;
                }
            } else {
                return false;
            }
        }
        setCustomBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), ((CraftWorld) location.getWorld()).getHandle(), Holder.direct(base));
        refreshChunk(location.getChunk());
        return true;
    }

    private void setCustomBiome(int x, int y, int z, Level w, Holder<Biome> bb) {
        BlockPos pos = new BlockPos(x, 0, z);
        if (w.isLoaded(pos)) {
            ChunkAccess chunk = w.getChunk(pos);
            if (chunk != null) {
                chunk.setBiome(x >> 2, y >> 2, z >> 2, bb);
            }
        }
    }

    private void refreshChunk(Chunk chunk) {
        CraftChunk craftChunk = (CraftChunk) chunk;
        ServerLevel level = craftChunk.getCraftWorld().getHandle();
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(craftChunk.getHandle(ChunkStatus.BIOMES)));
    }
}
