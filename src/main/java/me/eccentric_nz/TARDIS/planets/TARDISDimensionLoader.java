package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

import java.lang.reflect.Field;
import java.util.Optional;

public class TARDISDimensionLoader {

    /**
     * Programmatically constructs the custom dimension on-demand.
     * Extracts the DimensionType from the datapack and hot-swaps
     * it into the live level instance.
     */
    public static World loadWorld(TARDIS plugin, String name) {

        DedicatedServer nmsServer = ((CraftServer) plugin.getServer()).getServer();
        Identifier worldLocation = Identifier.fromNamespaceAndPath("minecraft", name);
        Identifier typeLocation = Identifier.fromNamespaceAndPath("tardis", name.replace("tardis_", ""));
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, worldLocation);
        ResourceKey<DimensionType> dimensionTypeKey = ResourceKey.create(Registries.DIMENSION_TYPE, typeLocation);
        // return if the dimension level instance is already loaded in memory
        ServerLevel existingLevel = nmsServer.getLevel(levelKey);
        if (existingLevel != null) {
            return existingLevel.getWorld();
        }
        // get the custom DimensionType from datapack
        RegistryAccess.Frozen registryAccess = nmsServer.registryAccess();
        Optional<Holder.Reference<DimensionType>> dimTypeHolderOpt = registryAccess.lookup(Registries.DIMENSION_TYPE).flatMap(lookup -> lookup.get(dimensionTypeKey));
        if (dimTypeHolderOpt.isEmpty()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_SEVERE, "Cannot load world! Missing datapack dimension type: " + typeLocation);
            return null;
        }
        Holder.Reference<DimensionType> customDimensionTypeHolder = dimTypeHolderOpt.get();
        // initialize the world using Paper's standard creator logic
        WorldCreator creator = new TARDISDimension().getWorldCreator(TARDIS.plugin, name);
        World bukkitWorld = plugin.getServer().createWorld(creator);
        if (bukkitWorld == null) {
            return null;
        }
        // hot-swap the underlying dimensionType holder reference on the live NMS Level instance
        // this links the custom timelines, clocks and sky colours
        try {
            ServerLevel nmsLevel = ((CraftWorld) bukkitWorld).getHandle();
            // get the DimensionType Holder registration tracking field on the ServerLevel/Level base class - 'dimensionTypeRegistration'
            Field dimensionTypeField = null;
            Class<?> currentClass = nmsLevel.getClass();
            // traverse up the class hierarchy to capture the field defined in net.minecraft.world.level.Level
            while (currentClass != null) {
                try {
                    dimensionTypeField = currentClass.getDeclaredField("dimensionTypeRegistration");
                    break;
                } catch (NoSuchFieldException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }
            if (dimensionTypeField != null) {
                dimensionTypeField.setAccessible(true);
                // overwrite the overworld template holder reference with the TARDIS dimension type
                dimensionTypeField.set(nmsLevel, customDimensionTypeHolder);
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Hot-swapped custom dimension type holder for " + name);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("An error occurred during planetary dimension type injection!" + e.getMessage());
        }
        return bukkitWorld;
    }
}
