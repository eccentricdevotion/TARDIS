package me.eccentric_nz.TARDIS.rooms.architectural;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class TreeBlueprints {

    static ItemStack[][] getBlueprints() {
        int rows = BlueprintRoom.values().length / 9 + 1;
        ItemStack[][] blueprints = new ItemStack[rows][9];
        int r = 0;
        int c = 0;
        for (BlueprintRoom blueprint : BlueprintRoom.values()) {
            if (blueprint.getPermission().contains(".room.")) {
                blueprints[r][c] = getArchitecturalDisk(blueprint);
                c++;
                if (c == 9) {
                    r++;
                    c = 0;
                }
            }
        }
        return blueprints;
    }

    public static ItemStack getArchitecturalDisk(BlueprintRoom room) {
        try {
            String perm = room.getPermission();
            if (perm != null) {
                ItemStack is = ItemStack.of(Material.MUSIC_DISC_MELLOHI, 1);
                is.editPersistentDataContainer(pdc -> {
                    pdc.set(TARDIS.plugin.getTimeLordUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), UUID.randomUUID());
                    pdc.set(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING, perm);
                });
                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Blueprint Disk"));
                is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(TARDISStringUtils.capitalise(room.toString()))).build());
                is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                        .addHiddenComponents(TARDISConstants.HIDE)
                        .build());
                return is;
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
