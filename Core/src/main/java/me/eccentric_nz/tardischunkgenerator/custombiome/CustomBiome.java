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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

public class CustomBiome {

    public static void addCustomBiome(CustomBiomeData data) {
        // get the key for the biome this custom biome is based on - minecraft:xxxx
        ResourceKey<Biome> minecraftKey = ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(data.getMinecraftName()));
        // create a key for the custom biome - tardis:xxxx
        ResourceKey<Biome> customKey = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("tardis", data.getCustomName()));
        // get the biome registry
        WritableRegistry<Biome> registrywritable = (WritableRegistry<Biome>) BiomeHelper.getRegistry();
        // get the minecraft biome
        Biome minecraftbiome = registrywritable.getValueOrThrow(minecraftKey);
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

        try {
            // put the biome into the TARDIS biome map
            TARDISHelper.biomeMap.put(data.getCustomName(), biome);
            // inject into the biome registry
            // unfreeze Biome Registry
            Field frozen = MappedRegistry.class.getDeclaredField("l");
            frozen.setAccessible(true);
            frozen.set(registrywritable, false);
            // inject unregisteredIntrusiveHolders with a new map to allow intrusive holders
            Field unregisteredIntrusiveHolders = MappedRegistry.class.getDeclaredField("m");
            unregisteredIntrusiveHolders.setAccessible(true);
            unregisteredIntrusiveHolders.set(registrywritable, new IdentityHashMap<>());
            registrywritable.createIntrusiveHolder(biome);
            Holder<Biome> holder = registrywritable.register(customKey, biome, RegistrationInfo.BUILT_IN);

            // Biomes also have TagKeys (see minecraft.tags.BiomeTags)
            // clone the minecraft biome's tag keys
            Set<TagKey<Biome>> tags = new HashSet<>();
            Holder<Biome> minecraftHolder = Holder.direct(minecraftbiome);
            minecraftHolder.tags().forEach(tags::add);
            // Holder.Reference.bindTags
            Method bindTags = Holder.Reference.class.getDeclaredMethod("a", Collection.class);
            bindTags.setAccessible(true);
            bindTags.invoke(holder, tags);

            // make unregisteredIntrusiveHolders null again to remove potential for undefined behaviour
            unregisteredIntrusiveHolders.set(registrywritable, null);
            // refreeze biome registry
            frozen.setAccessible(true);
            frozen.set(registrywritable, true);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException ignored) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not unlock biome registry!");
        }
    }
}
