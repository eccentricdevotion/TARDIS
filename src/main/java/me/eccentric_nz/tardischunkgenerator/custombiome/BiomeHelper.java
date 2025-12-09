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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.List;
import java.util.Locale;

public class BiomeHelper {

    public static Registry<Biome> getRegistry() {
        DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
        return dedicatedServer.registryAccess().lookup(Registries.BIOME).get();
    }

    public static void refreshChunk(Chunk chunk) {
        CraftChunk craftChunk = (CraftChunk) chunk;
        ServerLevel level = craftChunk.getCraftWorld().getHandle();
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(craftChunk.getHandle(ChunkStatus.BIOMES)));
    }

    /**
     * Set a cube to a custom biome - the bounds or the cube are the chunk borders and 16 blocks from startY.
     *
     * @param newBiomeName the name of the custom biome to set (such as tardis:skaro_lakes)
     * @param chunk        the chunk to set the biome for
     */
    public void setCustomBiome(String newBiomeName, Chunk chunk, int startY) {
        WritableRegistry<Biome> registryWritable = (WritableRegistry<Biome>) getRegistry();
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("tardis", newBiomeName.toLowerCase(Locale.ROOT)));
        Biome base = registryWritable.getValueOrThrow(key);
        if (base == null) {
            if (newBiomeName.contains(":")) {
                ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(newBiomeName.split(":")[0].toLowerCase(Locale.ROOT), newBiomeName.split(":")[1].toLowerCase(Locale.ROOT)));
                base = registryWritable.getValueOrThrow(newKey);
                if (base == null) {
                    return;
                }
            } else {
                return;
            }
        }
        Holder<Biome> biomeHolder = registryWritable.wrapAsHolder(base);
        Level w = ((CraftWorld) chunk.getWorld()).getHandle();
        int cx = chunk.getX() * 16;
        int cz = chunk.getZ() * 16;
        for (int x = 0; x <= 15; x++) {
            for (int z = 0; z <= 15; z++) {
                for (int y = startY; y < startY + 16; y++) {
                    setCustomBiome(cx + x, y, cz + z, w, biomeHolder);
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
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) getRegistry();
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace(newBiomeName.toLowerCase(Locale.ROOT)));
        base = registrywritable.getValueOrThrow(key);
        if (base == null) {
            if (newBiomeName.contains(":")) {
                ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(newBiomeName.split(":")[0].toLowerCase(Locale.ROOT), newBiomeName.split(":")[1].toLowerCase(Locale.ROOT)));
                base = registrywritable.getValueOrThrow(newKey);
                if (base == null) {
                    return false;
                }
            } else {
                return false;
            }
        }
        setCustomBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), ((CraftWorld) location.getWorld()).getHandle(), registrywritable.wrapAsHolder(base));
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
}
