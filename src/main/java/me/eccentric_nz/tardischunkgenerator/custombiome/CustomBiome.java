package me.eccentric_nz.tardischunkgenerator.custombiome;

import com.mojang.serialization.Lifecycle;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;

public class CustomBiome {

    public static void addCustomBiome(CustomBiomeData data) {
        DedicatedServer dedicatedServer = ((CraftServer) Bukkit.getServer()).getServer();
        ResourceKey<Biome> minecraftKey = ResourceKey.create(Registries.BIOME, new ResourceLocation("minecraft", data.getMinecraftName()));
        ResourceKey<Biome> customKey = ResourceKey.create(Registries.BIOME, new ResourceLocation("tardis", data.getCustomName()));
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) dedicatedServer.registryAccess().registryOrThrow(Registries.BIOME);
        Biome minecraftbiome = registrywritable.get(minecraftKey);
        Biome.BiomeBuilder newBiome = new Biome.BiomeBuilder();
        newBiome.downfall(minecraftbiome.climateSettings.downfall());
        MobSpawnSettings biomeSettingMobs = minecraftbiome.getMobSettings();
        newBiome.mobSpawnSettings(biomeSettingMobs);
        BiomeGenerationSettings biomeSettingGen = minecraftbiome.getGenerationSettings();
        newBiome.generationSettings(biomeSettingGen);
        newBiome.temperature(data.getTemperature());
        newBiome.downfall(data.getDownfall());
        newBiome.temperatureAdjustment(data.isFrozen() ? Biome.TemperatureModifier.NONE : Biome.TemperatureModifier.FROZEN);
        BiomeSpecialEffects.Builder newFog = new BiomeSpecialEffects.Builder();
        newFog.grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE);
        newFog.fogColor(data.getFogColour());
        newFog.waterColor(data.getWaterColour());
        newFog.waterFogColor(data.getWaterFogColour());
        newFog.skyColor(data.getSkyColour());
        newFog.foliageColorOverride(data.getFoliageColour());
        newFog.grassColorOverride(data.getGrassColour());
        newBiome.specialEffects(newFog.build());
        Biome biome = newBiome.build();
        TARDISHelper.biomeMap.put(data.getCustomName(), biome);
        // inject into the biome registry
        try {
            // unfreeze Biome Registry
            Field frozen = MappedRegistry.class.getDeclaredField("l");
            frozen.setAccessible(true);
            frozen.set(registrywritable, false);
            // inject unregisteredIntrusiveHolders with a new map to allow intrusive holders
            Field unregisteredIntrusiveHolders = MappedRegistry.class.getDeclaredField("m");
            unregisteredIntrusiveHolders.setAccessible(true);
            unregisteredIntrusiveHolders.set(registrywritable, new IdentityHashMap<>());
            registrywritable.createIntrusiveHolder(biome);
            registrywritable.register(customKey, biome, Lifecycle.stable());
            // make unregisteredIntrusiveHolders null again to remove potential for undefined behaviour
            unregisteredIntrusiveHolders.set(registrywritable, null);
            // refreeze biome registry
            frozen.setAccessible(true);
            frozen.set(registrywritable, true);

        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not unlock biome registry!");
        }
    }
}

