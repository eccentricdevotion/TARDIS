package me.eccentric_nz.TARDIS.rooms.laundry;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WashingMachineInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public WashingMachineInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS Washing Machine", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the ArchitecturalBlueprints Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[27];
        // 18 info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        io.lore(List.of(
                Component.text("Place items you want to launder"),
                Component.text("in the first row, then click"),
                Component.text("'Wash' to remove armour trims"),
                Component.text("or 'Bleach' to remove dye colours.")
        ));
        info.setItemMeta(io);
        items[18] = info;
        // 21 wash trims
        ItemStack wash = ItemStack.of(Material.CAULDRON, 1);
        ItemMeta trim = wash.getItemMeta();
        trim.displayName(Component.text("Wash"));
        wash.setItemMeta(trim);
        items[21] = wash;
        // 23 remove dye
        ItemStack remove = ItemStack.of(Material.CAULDRON, 1);
        ItemMeta dye = remove.getItemMeta();
        dye.displayName(Component.text("Bleach"));
        remove.setItemMeta(dye);
        items[23] = remove;
        // 26 close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        items[26] = close;
        return items;
    }
}
