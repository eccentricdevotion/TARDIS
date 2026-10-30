package me.eccentric_nz.TARDIS.discovery;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

public class LocatorMap {

    public org.bukkit.inventory.ItemStack create(Location location) {
        ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
        ItemStack map = MapItem.create(
                level,
                location.getBlockX() - 256,
                location.getBlockZ() - 256,
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
        org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(map);
        item.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Locator Map"));
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        return item;
    }
}
