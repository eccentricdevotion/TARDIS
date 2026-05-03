package me.eccentric_nz.TARDIS.rooms.games;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
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

public class GamesInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public GamesInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 9, Component.text("TARDIS Gaming Computer", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Gaming Computer Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[9];
        // 0 info
        ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click the disk for"),
                Component.text("the game you want to play."),
                Component.text("Before playing Tetris, you"),
                Component.text("can set the start level,"),
                Component.text("and must have no item"),
                Component.text("in your first hotbar slot.")
        )));
        items[0] = info;
        // 1 connect four
        ItemStack connect = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
        connect.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Connect 4"));
        connect.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .hideTooltip(true)
                .build());
        items[1] = connect;
        // pong & tetris require tardis_zero_room world
        boolean zero = plugin.getConfig().getBoolean("allow.zero_room");
        if (zero) {
            // 2 pong
            ItemStack pong = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
            pong.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Pong"));
            pong.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                    .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                    .hideTooltip(true)
                    .build());
            items[2] = pong;
        }
        // 3 stone magma ice
        ItemStack stonemagmaice = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
        stonemagmaice.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Stone Magma Ice"));
        stonemagmaice.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .hideTooltip(true)
                .build());
        items[3] = stonemagmaice;
        // 4 tic tac toe
        ItemStack tictactoe = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
        tictactoe.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Tic Tac Toe"));
        tictactoe.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .hideTooltip(true)
                .build());
        items[4] = tictactoe;
        if (zero) {
            // 5 tetris
            ItemStack tetris = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
            tetris.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Tetris"));
            tetris.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                    .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                    .hideTooltip(true)
                    .build());
            items[5] = tetris;
            // 6 tetris start level = up tp 30
            ItemStack level = ItemStack.of(Material.FIREWORK_ROCKET, 1);
            level.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Start level"));
            level.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("0")).build());
            level.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                    .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                    .hideTooltip(true)
                    .build());
            items[6] = level;
        }
        // 8 close
        items[8] = GUIItemFactory.close();
        return items;
    }
}
