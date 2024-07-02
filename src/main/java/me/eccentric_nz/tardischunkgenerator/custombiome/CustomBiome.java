package me.eccentric_nz.tardischunkgenerator.custombiome;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class CustomBiome {

    public static void addCustomBiome(CustomBiomeData data) {
        // get the key for the biome this custom biome is based on - minecraft:xxxx
        ResourceKey<Biome> minecraftKey = ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(data.getMinecraftName()));
        // create a key for the custom biome - tardis:xxxx
        ResourceKey<Biome> customKey = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("tardis", data.getCustomName()));
        // get the biome registry
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) BiomeHelper.getRegistry();
        // get the minecraft biome
        Biome minecraftbiome = registrywritable.get(minecraftKey);
        // build the custom biome
        Biome.BiomeBuilder newBiome = new Biome.BiomeBuilder();
        // copy some minecraft biome settings to the custom biome
        newBiome.downfall(minecraftbiome.climateSettings.downfall());
        MobSpawnSettings biomeSettingMobs = minecraftbiome.getMobSettings();
        newBiome.mobSpawnSettings(biomeSettingMobs);
        BiomeGenerationSettings biomeSettingGen = minecraftbiome.getGenerationSettings();
        newBiome.generationSettings(biomeSettingGen);
        // set custom settings
        newBiome.temperature(data.getTemperature());
        newBiome.downfall(data.getDownfall());
        newBiome.temperatureAdjustment(data.isFrozen() ? Biome.TemperatureModifier.NONE : Biome.TemperatureModifier.FROZEN);
        // set the custom biome colours
        BiomeSpecialEffects.Builder newFog = new BiomeSpecialEffects.Builder();
        newFog.grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE);
        newFog.fogColor(data.getFogColour());
        newFog.waterColor(data.getWaterColour());
        newFog.waterFogColor(data.getWaterFogColour());
        newFog.skyColor(data.getSkyColour());
        newFog.foliageColorOverride(data.getFoliageColour());
        newFog.grassColorOverride(data.getGrassColour());
        // add ambient particles
        if (data.getParticle() != null) {
            newFog.ambientParticle(new AmbientParticleSettings(data.getParticle(), data.getAmbience()));
        }
        newBiome.specialEffects(newFog.build());
        Biome biome = newBiome.build();
        // put the biome into the TARDIS biome map
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
            registrywritable.register(customKey, biome, RegistrationInfo.BUILT_IN);
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

