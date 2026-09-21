package me.eccentric_nz.TARDIS.custommodels;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GUIItemFactory {

    public static ItemStack close() {
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        close.setData(DataComponentTypes.CUSTOM_NAME, Component.text(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        return close;
    }
}
