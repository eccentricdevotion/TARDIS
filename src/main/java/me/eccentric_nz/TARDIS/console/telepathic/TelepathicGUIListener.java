package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TelepathicGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TelepathicGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onTelepathicMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (!name.equals(ChatColor.DARK_RED + "TARDIS Telepathic Circuit")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        ItemStack choice = view.getItem(slot);
        switch (slot) {
            // toggle telepathy on/off
            case 0 -> {
                ItemMeta im = choice.getItemMeta();
                int b = (im.hasLore() && im.getLore().get(0).endsWith("ON")) ? 0 : 1;
                // update database
                HashMap<String, Object> set = new HashMap<>();
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                set.put("telepathy_on", b);
                plugin.getQueryFactory().doUpdate("player_prefs", set, where);
                // set item model
                int cmd = im.getCustomModelData();
                im.setCustomModelData((cmd > 100) ? cmd - 100 : cmd + 100);
                choice.setItemMeta(im);
                plugin.getMessenger().announceRepeater(player, "Telepathic Circuit " + (b == 1 ? "ON" : "OFF"));
                close(player);
            }
            // cave finder
            case 2 -> {
                if (choice != null) {
                    player.performCommand("tardistravel cave");
                    close(player);
                }
            }
            // structure finder
            case 4 -> {
                if (choice != null) {
                    if (!plugin.getUtils().inTARDISWorld(player)) {
                        return;
                    }
                    TARDISTelepathicStructure tts = new TARDISTelepathicStructure(plugin);
                    ItemStack[] gui = tts.getButtons();
                    Inventory structure = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Telepathic Structure Finder");
                    structure.setContents(gui);
                    player.openInventory(structure);
                }
            }
            // biome finder
            case 6 -> {
                if (choice != null) {
                    // get id of TARDIS player is in
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
                    if (rs.resultSet()) {
                        TARDISTelepathicBiome ttb = new TARDISTelepathicBiome(plugin, rs.getTardis_id());
                        ItemStack[] gui = ttb.getButtons();
                        Inventory biome = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Telepathic Biome Finder");
                        biome.setContents(gui);
                        player.openInventory(biome);
                    }
                }
            }
            // close
            case 8 -> close(player);
            // do nothing
            default -> {
            }
        }
    }
}
