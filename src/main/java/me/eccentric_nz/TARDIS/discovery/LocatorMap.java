package me.eccentric_nz.TARDIS.discovery;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLocator;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

public class LocatorMap {

    private final TARDIS plugin;
    private final int cost;

    public LocatorMap(TARDIS plugin) {
        this.plugin = plugin;
        this.cost = plugin.getConfig().getInt("creation.discovery.map_cost");
    }

    /**
     * Gets a random temple location from the database.
     *
     * @param world The world to get the location from.
     */
    public Location getLocation(World world) {
        ResultSetLocator rsl = new ResultSetLocator(plugin);
        if (rsl.fromWorld(world)) {
            return rsl.getLocations().getFirst();
        }
        return null;
    }

    /**
     * Gets the closest (unclaimed) temple location to the player from the database.
     *
     * @param world  The world to get the location from.
     * @param chunkX The players current x chunk coordinate
     * @param chunkZ The players current z chunk coordinate
     * @return the closest location or null if none was found.
     */
    public Location getLocation(World world, int chunkX, int chunkZ) {
        ResultSetLocator rsl = new ResultSetLocator(plugin);
        return rsl.getClosestStructureLocation(world, chunkX, chunkZ);
    }

    public ItemStack create(Location location) {
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        net.minecraft.world.item.ItemStack map = MapItem.create(
                level,
                location.getBlockX() - 128,
                location.getBlockZ() - 128,
                (byte) 2,   // scale
                true,
                true
        );
        // add red X marker
        MapItemSavedData.addTargetDecoration(
                map,
                new BlockPos(
                        location.getBlockX(),
                        level.getHeight(
                                Heightmap.Types.WORLD_SURFACE,
                                location.getBlockX(),
                                location.getBlockZ()
                        ),
                        location.getBlockZ()
                ),
                "+",
                MapDecorationTypes.RED_X
        );
        ItemStack item = CraftItemStack.asBukkitCopy(map);
        item.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Locator Map"));
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        return item;
    }

    public MerchantRecipe fromMap(ItemStack is) {
        MerchantRecipe recipe = new MerchantRecipe(is, 1);
        recipe.setIngredients(List.of(ItemStack.of(Material.COMPASS), ItemStack.of(Material.LAPIS_LAZULI, cost)));
        return recipe;
    }
}
