package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class CustomiseConsoleInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public CustomiseConsoleInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Customise Console", NamedTextColor.DARK_RED));
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
                Component.text("Choose your desired time rotor"),
                Component.text("Choose your desired console"),
                Component.text("Click on the Save button"),
                Component.text("to apply your choices.")
        ));
        info.setItemMeta(info_im);
        consoles[0] = info;
        // rotors
        int r = 9;
        for (Map.Entry<String,Rotor> rotor: Rotor.byName.entrySet()) {
            ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(rotor.getValue().offModel());
            im.displayName(Component.text(TARDISStringUtils.capitalise(rotor.getKey())));
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
        for (Map.Entry<Material, NamespacedKey> colour : ColourType.BY_MATERIAL.entrySet()) {
            // get colour name
            String key = colour.getKey().toString();
            String name = key.contains("CONCRETE") ? key.replace("_CONCRETE", "") : "RUSTIC";
            ItemStack is = ItemStack.of(colour.getKey(), 1);
            ItemMeta im = is.getItemMeta();
            String dn = TARDISStringUtils.capitalise(name) + " Console";
            im.displayName(ComponentUtils.toWhite(dn));
            is.setItemMeta(im);
            consoles[c] = is;
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
        ItemStack save = ItemStack.of(Material.BOWL, 1);
        ItemMeta se = save.getItemMeta();
        se.displayName(Component.text("Save"));
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
