package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TVSkinListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<String> titles = List.of(ChatColor.DARK_RED + "Doctor Skins", ChatColor.DARK_RED + "Companion Skins", ChatColor.DARK_RED + "Character Skins", ChatColor.DARK_RED + "Monster Skins");

    public TVSkinListener(TARDIS plugin) {
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
    public void onSkinMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!titles.contains(title)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 25 -> {
                // back
                ItemStack[] items = new TVInventory().getMenu();
                Inventory tvinv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Television");
                tvinv.setContents(items);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(tvinv), 2L);
            }
            case 26 -> close(player); // close
            default -> {
                Skin skin = ArchSkins.ARI;
                String which = ChatColor.stripColor(title).split(" ")[0];
                switch (which) {
                    case "Doctor" -> skin = DoctorSkins.DOCTORS.get(slot);
                    case "Companion" -> skin = CompanionSkins.COMPANIONS.get(slot);
                    case "Character" -> skin = CharacterSkins.CHARACTERS.get(slot);
                    case "Monster" -> skin = MonsterSkins.MONSTERS.get(slot);
                }
                plugin.getSkinChanger().set(player, skin);
                close(player);
            }
        }
    }
}
