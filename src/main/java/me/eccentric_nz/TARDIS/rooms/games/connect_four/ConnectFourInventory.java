package me.eccentric_nz.TARDIS.rooms.games.connect_four;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors.Letters;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConnectFourInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ConnectFourInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Connect 4", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[54];
        // 0 info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        ItemMeta io = info.getItemMeta();
        io.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        io.lore(List.of(
            Component.text("Click a slot at the top of a column to drop a block."),
            Component.text("The TARDIS will choose shortly after."),
            Component.text("First to connect four blocks wins!"),
            Component.text("Click the reset button to play again.")
        ));
        info.setItemMeta(io);
        items[8] = info;
        // define grid
        ItemStack hole = ItemStack.of(Material.BLUE_CONCRETE_POWDER);
        ItemMeta holeMeta = hole.getItemMeta();
        holeMeta.setData(DataComponentTypes.CUSTOM_NAME, Component.text(" "));
        hole.setItemMeta(holeMeta);
        for (int j = 0; j < 53; j += 9) {
            for (int k = 0; k < 7; k++) {
                int slot = j + k;
                items[slot] = hole;
            }
        }
        // 17 game result
        ItemStack game = ItemStack.of(Material.TARGET, 1);
        ItemMeta result = game.getItemMeta();
        result.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Game result"));
        game.setItemMeta(result);
        items[17] = game;
        // 26 playing
        ItemStack banner = Letters.P(DyeColor.ORANGE, DyeColor.WHITE);
        ItemMeta im = banner.getItemMeta();
        im.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Playing"));
        banner.setItemMeta(im);
        items[26] = banner;
        // 44 reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Reset game"));
        reset.setItemMeta(cobble);
        items[44] = reset;
        // 53 close
        items[53] = GUIItemFactory.close();;
        return items;
    }
}
