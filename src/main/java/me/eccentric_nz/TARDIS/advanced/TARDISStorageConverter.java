package me.eccentric_nz.TARDIS.advanced;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.io.IOException;
import java.util.Map;

public class TARDISStorageConverter {

    public static ItemStack[] updateDisks(String serialized) {
        try {
            ItemStack[] stacks = TARDISSerializeInventory.itemStacksFromString(serialized);
            // convert stacks to component display names
            for (ItemStack is : stacks) {
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName()) {
                        Component component = im.displayName();
                        // strip color codes
                        String stripped = ComponentUtils.stripColour(component);
                        if (!component.children().isEmpty()) {
                            stripped = ComponentUtils.stripColour(component.children().getFirst());
                        }
                        im.displayName(Component.text(stripped));
                        if (is.getType() == Material.GLOWSTONE_DUST) {
                            CustomModelDataComponent cmd = im.getCustomModelDataComponent();
                            cmd.setFloats(CircuitVariant.GALLIFREY.getFloats());
                            im.setCustomModelDataComponent(cmd);
                        }
                        im.setItemModel(null);
                        im.addItemFlags(ItemFlag.values());
                        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                        is.setItemMeta(im);
                    }
                }
            }
            return stacks;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static ItemStack[] updateCircuits(String serialized) {
        try {
            ItemStack[] stacks = TARDISSerializeInventory.itemStacksFromString(serialized);
            // convert stacks to component display names
            for (ItemStack is : stacks) {
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName()) {
                        Component component = im.displayName();
                        // strip color codes
                        String stripped = ComponentUtils.stripColour(component);
                        if (!component.children().isEmpty()) {
                            stripped = ComponentUtils.stripColour(component.children().getFirst());
                        }
                        im.displayName(Component.text(stripped));
                        if (is.getType() == Material.GLOWSTONE_DUST) {
                            CustomModelDataComponent cmd = im.getCustomModelDataComponent();
                            cmd.setFloats(CircuitVariant.fromDisplayName(stripped).getFloats());
                            im.setCustomModelDataComponent(cmd);
                        }
                        im.setItemModel(null);
                        im.addItemFlags(ItemFlag.values());
                        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                        is.setItemMeta(im);
                    }
                }
            }
            return stacks;
        } catch (Exception ignored) {
        }
        return null;
    }
}
