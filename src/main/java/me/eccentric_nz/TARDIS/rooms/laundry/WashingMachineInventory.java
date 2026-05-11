package me.eccentric_nz.TARDIS.rooms.laundry;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WashingMachineInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public WashingMachineInventory(TARDIS plugin) {
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
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Place items you want to launder"),
                Component.text("in the first row, then click"),
                Component.text("'Wash' to remove armour trims"),
                Component.text("or 'Bleach' to remove dye colours.")
        )));
        items[18] = info;
        // 21 wash trims
        ItemStack wash = ItemStack.of(Material.CAULDRON, 1);
        wash.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Wash"));
        items[21] = wash;
        // 23 remove dye
        ItemStack remove = ItemStack.of(Material.CAULDRON, 1);
        remove.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Bleach"));
        items[23] = remove;
        // 26 close
        items[26] = GUIItemFactory.close();
        return items;
    }
}
