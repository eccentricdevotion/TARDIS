package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

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

public class NoughtsAndCrossesInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public NoughtsAndCrossesInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Tic Tac Toe", NamedTextColor.DARK_RED));
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
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click an empty slot to place your mark."),
                Component.text("The TARDIS will choose soon after."),
                Component.text("PLAYER is noughts."),
                Component.text("TARDIS is crosses."),
                Component.text("Click the reset button to play again.")
        )));
        items[0] = info;
        // play grid
        int[] grid = new int[]{3, 4, 5, 12, 13, 14, 21, 22, 23};
        ItemStack wool = ItemStack.of(MatchState.NOT_STARTED.getSymbol());
        wool.setData(DataComponentTypes.CUSTOM_NAME, Component.text(" "));
        for (int g : grid) {
            items[g] = wool;
        }
        // player turn
        ItemStack player = ItemStack.of(Material.TARGET, 1);
        player.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Player"));
        items[7] = player;
        // player indicator
        ItemStack turn = ItemStack.of(MatchState.PLAYER_TURN.getSymbol(), 1);
        turn.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Player"));
        items[8] = turn;
        // tardis turn
        ItemStack tardis = ItemStack.of(Material.TARGET, 1);
        tardis.setData(DataComponentTypes.CUSTOM_NAME, Component.text("TARDIS"));
        items[16] = tardis;
        // 45 reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        reset.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Reset game"));
        items[45] = reset;
        // 35 close
        items[53] = GUIItemFactory.close();
        return items;
    }
}
