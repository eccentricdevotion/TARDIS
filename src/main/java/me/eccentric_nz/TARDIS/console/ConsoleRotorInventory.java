package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConsoleRotorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ConsoleRotorInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("3D Consoles", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] consoles = new ItemStack[54];
        // info
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Instructions"));
        info_im.lore(List.of(
                Component.text("Put your Sonic Screwdriver"),
                Component.text("in the bottom left most slot"),
                Component.text("and then click on the"),
                Component.text("Sonic of your choice.")
        ));
        info.setItemMeta(info_im);
        consoles[0] = info;
        // rotors
        int r = 9;
        for (Rotor rotor: Rotor.byCustomModel.values()) {
            ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(rotor.offModel());
            is.setItemMeta(im);
            consoles[r] = is;
            r++;
            if (r > 17) {
                break;
            }
        }
        // prev next
        // scroll left
        ItemStack scroll_left = ItemStack.of(GUIArs.BUTTON_SCROLL_L.material(), 1);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_L", "Scroll left")));
        scroll_left.setItemMeta(nim);
        consoles[18] = scroll_left;
        // scroll right
        ItemStack scroll_right = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_R", "Scroll right")));
        scroll_right.setItemMeta(pim);
        consoles[26] = scroll_right;
        // consoles
        int c = 27;
        for (DyeColor color : DyeColor.values()) {

            c++;
            if (c > 35) {
                break;
            }
        }
        // scroll left
        consoles[36] = scroll_left;
        // scroll right
        consoles[44] = scroll_right;
        // save
        ItemStack save = ItemStack.of(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        ItemMeta se = save.getItemMeta();
        se.displayName(Component.text(plugin.getChameleonGuis().getString("SAVE", "Save construct")));
        save.setItemMeta(se);
        consoles[49] = save;
        // close
        ItemStack close = ItemStack.of(GUICompanion.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        consoles[GUICompanion.BUTTON_CLOSE.slot()] = close;

        return consoles;
    }
}
