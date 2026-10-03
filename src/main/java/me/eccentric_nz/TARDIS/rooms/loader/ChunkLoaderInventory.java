package me.eccentric_nz.TARDIS.rooms.loader;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChunkLoaderInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ChunkLoaderInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Chunk Loader", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System Map.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // direction pad
        // up
        ItemStack pad_up = ItemStack.of(GUIMap.BUTTON_UP.material(), 1);
        pad_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        // down
        ItemStack pad_down = ItemStack.of(GUIMap.BUTTON_DOWN.material(), 1);
        pad_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        // left
        ItemStack pad_left = ItemStack.of(GUIMap.BUTTON_LEFT.material(), 1);
        pad_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        // right
        ItemStack pad_right = ItemStack.of(GUIMap.BUTTON_RIGHT.material(), 1);
        pad_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        // level selected
        ItemStack level_sel = ItemStack.of(GUIMap.BUTTON_LEVEL.material(), 1);
        level_sel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        // level top
        ItemStack level_top = ItemStack.of(GUIMap.BUTTON_LEVEL_T.material(), 1);
        level_top.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        // level top
        ItemStack level_bot = ItemStack.of(GUIMap.BUTTON_LEVEL_B.material(), 1);
        level_bot.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        // stone
        ItemStack black = ItemStack.of(GUIMap.BUTTON_MAP_ON.material(), 1);
        black.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        // load map
        ItemStack loader = ItemStack.of(GUIMap.BUTTON_MAP.material(), 1);
        loader.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        // close
        ItemStack close = GUIItemFactory.close();
        // process
        ItemStack process = ItemStack.of(GUIArs.BUTTON_RECON.material(), 1);
        process.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_PROCESS", "Add tickets")));
        process.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.text("for selected chunks."))));
        // clear
        ItemStack clear = ItemStack.of(GUIArs.BUTTON_JETT.material(), 1);
        clear.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CLEAR", "Clear chunk tickets")));

        return new ItemStack[]{
                null, pad_up, null, null, black, black, black, black, black,
                pad_left, loader, pad_right, null, black, black, black, black, black,
                null, pad_down, null, null, black, black, black, black, black,
                level_bot, level_sel, level_top, null, black, black, black, black, black,
                null, null, null, null, black, black, black, black, black,
                close, null, process, null, clear, null, null, null, null
        };
    }
}
