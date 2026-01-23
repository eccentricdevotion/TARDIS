package me.eccentric_nz.TARDIS.rooms.games.tictactoe;

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
        // play grid
        int[] grid = new int[]{3, 4, 5, 12, 13, 14, 21, 22, 23};
        ItemStack wool = ItemStack.of(MatchState.NOT_STARTED.getSymbol());
        for (int g : grid) {
            items[g] = wool;
        }
        // player turn
        ItemStack player = ItemStack.of(Material.TARGET, 1);
        ItemMeta pim = player.getItemMeta();
        pim.displayName(Component.text("Player"));
        player.setItemMeta(pim);
        items[7] = player;
        // player indicator
        ItemStack turn = ItemStack.of(MatchState.PLAYER_TURN.getSymbol(), 1);
        ItemMeta indicator = turn.getItemMeta();
        indicator.displayName(Component.text("Player"));
        turn.setItemMeta(indicator);
        items[8] = turn;
        // tardis turn
        ItemStack tardis = ItemStack.of(Material.TARGET, 1);
        ItemMeta tim = tardis.getItemMeta();
        tim.displayName(Component.text("TARDIS"));
        tardis.setItemMeta(tim);
        items[16] = tardis;
        // 45 reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.displayName(Component.text("Reset game"));
        reset.setItemMeta(cobble);
        items[45] = reset;
        // 35 close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        items[53] = close;
        return items;
    }
}
