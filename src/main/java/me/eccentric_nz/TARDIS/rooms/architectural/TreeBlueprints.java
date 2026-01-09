package me.eccentric_nz.TARDIS.rooms.architectural;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TreeBlueprints {

    static ItemStack[][] getBlueprints() {
        int rows = BlueprintRoom.PERMS.size() / 8 + 1;
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
                ItemMeta im = is.getItemMeta();
                PersistentDataContainer pdc = im.getPersistentDataContainer();
                pdc.set(TARDIS.plugin.getTimeLordUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), UUID.randomUUID());
                pdc.set(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING, perm);
                im.displayName(ComponentUtils.toWhite("TARDIS Blueprint Disk"));
                List<Component> lore = List.of(
                        Component.text(TARDISStringUtils.capitalise(room.toString()))
                );
                im.lore(lore);
                im.addItemFlags(ItemFlag.values());
                im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                is.setItemMeta(im);
                return is;
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
