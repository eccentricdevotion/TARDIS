package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoneMagmaIceInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public StoneMagmaIceInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Stone Magma Ice", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[36];
        // 0 info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click the block to make your choice."),
                Component.text("The TARDIS will choose at the same time."),
                Component.text("STONE smashes ICE."),
                Component.text("ICE cools MAGMA."),
                Component.text("MAGMA melts STONE."),
                Component.text("Click the reset button to play again.")
        )));
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
        reset.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Reset game"));
        items[27] = reset;
        // 35 close
        items[35] = GUIItemFactory.close();
        return items;
    }
}
