package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TVListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TVListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onTelevisionMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Television")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 36) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 1 -> {
                // doctors
                ItemStack[] doctors = new TVDoctorsInventory().getMenu();
                Inventory doctorinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Doctor Skins");
                doctorinv.setContents(doctors);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(doctorinv), 2L);
            }
            case 3 -> {
                // companions
                ItemStack[] companions = new TVCompanionsInventory().getMenu();
                Inventory companioninv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Companion Skins");
                companioninv.setContents(companions);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(companioninv), 2L);
            }
            case 5 -> {
                // characters
                ItemStack[] characters = new TVCharactersInventory().getMenu();
                Inventory characterinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Character Skins");
                characterinv.setContents(characters);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(characterinv), 2L);
            }
            case 7 -> {
                // monsters
                ItemStack[] monsters = new TVMonstersInventory().getMenu();
                Inventory monsterinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Monster Skins");
                monsterinv.setContents(monsters);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(monsterinv), 2L);
            }
            case 31 -> {
                UUID uuid = player.getUniqueId();
                // remove skin
                plugin.getSkinChanger().remove(player);
                Skin skin = SkinUtils.SKINNED.get(uuid);
                SkinUtils.removeExtras(player, skin);
                SkinUtils.SKINNED.remove(uuid);
            }
            default -> close(player); // close
        }
    }
}
