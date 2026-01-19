package me.eccentric_nz.TARDIS.rooms.games;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

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
        ItemMeta io = info.getItemMeta();
        io.displayName(Component.text(plugin.getChameleonGuis().getString("INFO", "Info")));
        io.lore(List.of(
                Component.text("Click the disk for"),
                Component.text("the game you want to play."),
                Component.text("Before playing Tetris, you"),
                Component.text("can set the start level,"),
                Component.text("and must have no item"),
                Component.text("in your first hotbar slot.")
        ));
        info.setItemMeta(io);
        items[0] = info;
        // pong & tetris require TARDIS_Zero_Room world
        boolean zero = plugin.getConfig().getBoolean("allow.zero_room");
        if (zero) {
            // 3 pong
            ItemStack pong = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
            ItemMeta pim = pong.getItemMeta();
            pim.displayName(Component.text("Pong"));
            pim.addItemFlags(ItemFlag.values());
            pim.setAttributeModifiers(Multimaps.forMap(Map.of()));
            pong.setItemMeta(pim);
            items[3] = pong;
        }
        // 4 tic tac toe
        ItemStack tictactoe = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
        ItemMeta ncim = tictactoe.getItemMeta();
        ncim.displayName(Component.text("Tic Tac Toe"));
        ncim.addItemFlags(ItemFlag.values());
        ncim.setAttributeModifiers(Multimaps.forMap(Map.of()));
        tictactoe.setItemMeta(ncim);
        items[4] = tictactoe;
        if (zero) {
            // 5 tetris
            ItemStack tetris = ItemStack.of(Material.MUSIC_DISC_OTHERSIDE, 1);
            ItemMeta tim = tetris.getItemMeta();
            tim.displayName(Component.text("Tetris"));
            tim.addItemFlags(ItemFlag.values());
            tim.setAttributeModifiers(Multimaps.forMap(Map.of()));
            tetris.setItemMeta(tim);
            items[5] = tetris;
            // 6 tetris start level = up tp 30
            ItemStack level = ItemStack.of(Material.FIREWORK_ROCKET, 1);
            ItemMeta lim = level.getItemMeta();
            lim.displayName(Component.text("Start level"));
            lim.lore(List.of(Component.text("0")));
            lim.addItemFlags(ItemFlag.values());
            lim.setAttributeModifiers(Multimaps.forMap(Map.of()));
            level.setItemMeta(lim);
            items[6] = level;
        }
        // 8 close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        items[8] = close;
        return items;
    }
}
