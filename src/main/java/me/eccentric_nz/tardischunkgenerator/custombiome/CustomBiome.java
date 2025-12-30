/*
 * Copyright (C) 2026 eccentric_nz
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
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

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
        ResourceKey<Biome> minecraftKey = ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace(data.minecraftName()));
        // create a key for the custom biome - tardis:xxxx
        ResourceKey<Biome> customKey = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("tardis", data.customName()));
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
        newBiome.temperature(data.temperature());
        newBiome.downfall(data.downfall());
        newBiome.temperatureAdjustment(data.frozen() ? Biome.TemperatureModifier.NONE : Biome.TemperatureModifier.FROZEN);
        // set the custom biome colours
        BiomeSpecialEffects.Builder specialEffects = new BiomeSpecialEffects.Builder();
        specialEffects.grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE);
        specialEffects.waterColor(data.waterColour());
        specialEffects.foliageColorOverride(data.foliageColour());
        specialEffects.grassColorOverride(data.grassColour());
        newBiome.specialEffects(specialEffects.build());
        // set sky colour
        newBiome.setAttribute(EnvironmentAttributes.SKY_COLOR, data.skyColour());
        // set fog colours
        newBiome.setAttribute(EnvironmentAttributes.FOG_COLOR, data.fogColour());
        newBiome.setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, data.waterFogColour());
        // add ambient particles
        if (data.particle() != null) {
            newBiome.setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(data.particle(), data.ambience()));
        }
        Biome biome = newBiome.build();

        try {
            // put the biome into the TARDIS biome map
            TARDISHelper.biomeMap.put(data.customName(), biome);
            // inject into the biome registry
            // unfreeze Biome Registry
            Field frozen = MappedRegistry.class.getDeclaredField("frozen");
            frozen.setAccessible(true);
            frozen.set(registrywritable, false);
            // inject unregisteredIntrusiveHolders with a new map to allow intrusive holders
            Field unregisteredIntrusiveHolders = MappedRegistry.class.getDeclaredField("unregisteredIntrusiveHolders");
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
            Method bindTags = Holder.Reference.class.getDeclaredMethod("bindTags", Collection.class);
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
