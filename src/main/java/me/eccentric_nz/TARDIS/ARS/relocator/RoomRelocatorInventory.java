package me.eccentric_nz.TARDIS.ARS.relocator;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class RoomRelocatorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public RoomRelocatorInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Room Relocator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack(player));
    }
    /**
     * Constructs an inventory for the Room Relocator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack(Player player) {
        ItemStack[] is = new ItemStack[54];
        // direction pad up
        ItemStack pad_up = ItemStack.of(GUIArs.BUTTON_UP.material(), 1);
        pad_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        is[GUIArs.BUTTON_UP.slot()] = pad_up;
        // black wool
        ItemStack black = ItemStack.of(GUIArs.BUTTON_MAP_ON.material(), 1);
        black.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        for (int j = 0; j < 37; j += 9) {
            for (int k = 0; k < 5; k++) {
                int slot = 4 + j + k;
                is[slot] = black;
            }
        }
        // direction pad left
        ItemStack pad_left = ItemStack.of(GUIArs.BUTTON_LEFT.material(), 1);
        pad_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        is[GUIArs.BUTTON_LEFT.slot()] = pad_left;
        // load map
        ItemStack map = ItemStack.of(GUIArs.BUTTON_MAP.material(), 1);
        map.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        is[GUIArs.BUTTON_MAP.slot()] = map;
        // direction pad right
        ItemStack pad_right = ItemStack.of(GUIArs.BUTTON_RIGHT.material(), 1);
        pad_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        is[GUIArs.BUTTON_RIGHT.slot()] = pad_right;
        // reconfigure
        ItemStack s = ItemStack.of(GUIArs.BUTTON_RECON.material(), 1);
        s.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RECON", "Reconfigure!")));
        is[GUIArs.BUTTON_RECON.slot()] = s;
        // direction pad down
        ItemStack pad_down = ItemStack.of(GUIArs.BUTTON_DOWN.material(), 1);
        pad_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        is[GUIArs.BUTTON_DOWN.slot()] = pad_down;
        // level bottom
        ItemStack level_bot = ItemStack.of(GUIArs.BUTTON_LEVEL_B.material(), 1);
        level_bot.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        is[GUIArs.BUTTON_LEVEL_B.slot()] = level_bot;
        // level selected
        ItemStack level_sel = ItemStack.of(GUIArs.BUTTON_LEVEL.material(), 1);
        level_sel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        is[GUIArs.BUTTON_LEVEL.slot()] = level_sel;
        // level top
        ItemStack level_top = ItemStack.of(GUIArs.BUTTON_LEVEL_T.material(), 1);
        level_top.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        is[GUIArs.BUTTON_LEVEL_T.slot()] = level_top;
        // reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        reset.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Reset"));
        is[GUIArs.BUTTON_RESET.slot()] = reset;
        return is;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
