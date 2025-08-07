package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TARDISFixStorageCommand {

    private final TARDIS plugin;

    public TARDISFixStorageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean convertStacks() {
        try {
            for (Storage storage : Storage.values()) {
                ItemStack[] stacks = TARDISSerializeInventory.itemStacksFromString(storage.getEmpty());
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
                // rewrite serialised string
                String out = TARDISSerializeInventory.itemStacksToString(stacks);
                // write the string to file
                String file = plugin.getDataFolder() + File.separator + "storage_" + storage + ".txt";
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                    bw.write(out);
                }
            }
        } catch (IOException ignored) {
        }
        return true;
    }
}
