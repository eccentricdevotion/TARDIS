package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoneMagmaIceInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public StoneMagmaIceInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = TARDIS.plugin.getServer().createInventory(this, 36, Component.text("Stone Magma Ice", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[36];
        // 0 info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        io.lore(List.of(
            Component.text("Click the block to make your choice."),
            Component.text("The TARDIS will choose at the same time."),
            Component.text("STONE smashes ICE."),
            Component.text("ICE cools MAGMA."),
            Component.text("MAGMA melts STONE."),
            Component.text("Click the reset button to play again.")
        ));
        info.setItemMeta(io);
        items[0] = info;
        // 11 stone
        ItemStack stone = ItemStack.of(Material.STONE);
        items[11] = stone;
        // 13 magma
        ItemStack magma = ItemStack.of(Material.MAGMA_BLOCK);
        items[13] = magma;
        // 15 ice
        ItemStack ice = ItemStack.of(Material.ICE);
        items[15] = ice;
        // 27 reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.displayName(Component.text("Reset game"));
        reset.setItemMeta(cobble);
        items[27] = reset;
        // 35 close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        items[35] = close;
        return items;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
