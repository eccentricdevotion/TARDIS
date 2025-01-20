package me.eccentric_nz.TARDIS.utility;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Art;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Villager;

import java.util.List;
import java.util.Locale;

public class TARDISRegistryValues {

    public static List<Biome> BIOMES;
    public static List<Villager.Profession> VILLAGER_PROFESSIONS;
    public static List<Villager.Type> VILLAGER_TYPES;
    public static List<Cat.Type> CAT_TYPES;
    public static List<Frog.Variant> FROG_VARIANTS;

   static {
       Registry<Biome> biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
       biomeRegistry.forEach(BIOMES::add);
       Registry<Villager.Profession> professionRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_PROFESSION);
       professionRegistry.forEach(VILLAGER_PROFESSIONS::add);
       Registry<Villager.Type> typeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_TYPE);
       typeRegistry.forEach(VILLAGER_TYPES::add);
       Registry<Cat.Type> catRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.CAT_VARIANT);
       catRegistry.forEach(CAT_TYPES::add);
       Registry<Frog.Variant> frogRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.FROG_VARIANT);
       frogRegistry.forEach(FROG_VARIANTS::add);
   }

   public static Biome getBiome(String name) {
       Registry<Biome> biomeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
       Biome biome = biomeRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
       return biome;
   }

   public static Frog.Variant getFrogVariant(String name) {
       Registry<Frog.Variant> frogRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.FROG_VARIANT);
       Frog.Variant variant = frogRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
       return variant;
   }

   public static int getCatIndex(Cat.Type type) {
       return CAT_TYPES.indexOf(type);
   }

    public static Cat.Type getCatType(String name) {
       Registry<Cat.Type> catRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.CAT_VARIANT);
       Cat.Type type = catRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
       return type;
    }

    public static Villager.Profession getVillagerProfession(String name) {
       Registry<Villager.Profession> professionRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_PROFESSION);
       Villager.Profession profession = professionRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
       return profession;
    }

    public static Villager.Type getVillagerType(String name) {
       Registry<Villager.Type> typeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_TYPE);
       Villager.Type type = typeRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
       return type;
    }

    public static Art getPaintingVariant(String name) {
        Registry<Art> artRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT);
        Art art = artRegistry.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
        return art;
    }
}
