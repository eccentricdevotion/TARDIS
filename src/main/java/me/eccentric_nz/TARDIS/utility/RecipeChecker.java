package me.eccentric_nz.TARDIS.utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class RecipeChecker implements Runnable {

    @Override
    public void run() {
        ServerLevel world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        // check recipes
        try {
            Set<Map.Entry<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, RecipeHolder<?>>>> recipes = MinecraftServer.getServer().getRecipeManager().recipes.entrySet();
            for (Map.Entry<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, RecipeHolder<?>>> recipe : recipes) {
                Object2ObjectLinkedOpenHashMap<ResourceLocation, RecipeHolder<?>> map = recipe.getValue();
                for (Object2ObjectMap.Entry<ResourceLocation, RecipeHolder<?>> r : map.object2ObjectEntrySet()) {
                    Object obj = r.getValue().value();
                    if (obj instanceof net.minecraft.world.item.crafting.ShapedRecipe shaped) {
                        String key = r.getKey().getNamespace().toLowerCase();
                        String path = r.getKey().getPath();
                        if (key.startsWith("tardis")) {
                            Bukkit.getLogger().log(Level.INFO, path + " => " + shaped.getResultItem(world.registryAccess()).toString());
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.WARNING, "Skipping recipe...");
        }
    }
}
