package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TARDISLightEmittingListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISLightEmittingListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLightEmittingBlockClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (!name.equals(NamedTextColor.DARK_RED + "Light Emitting Blocks")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 27) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        switch (slot) {
            case 24 -> {
                // back
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                // get TARDIS id
                if (rst.fromUUID(uuid.toString())) {
                    ItemStack[] lightStacks = new TARDISLightsInventory(plugin, rst.getTardisId(), uuid).getGUI();
                    Inventory lightGUI = plugin.getServer().createInventory(player, 54, NamedTextColor.DARK_RED + "TARDIS Lights");
                    lightGUI.setContents(lightStacks);
                    player.openInventory(lightGUI);
                }
            }
            case 26 -> close(player); // close
            default -> {
                // get block type and data
                ItemStack choice = view.getItem(slot);
                setEmittingLightBlock(player, choice.getType().toString());
            }
        }
    }

    private void setEmittingLightBlock(Player player, String block) {
        // remember block
        Sequences.CONVERTERS.put(player.getUniqueId(), block);
        // get player's TARDIS
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        UUID uuid = player.getUniqueId();
        if (rst.fromUUID(uuid.toString())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // go back to Lights GUI
                ItemStack[] lightStacks = new TARDISLightsInventory(plugin, rst.getTardisId(), uuid).getGUI();
                Inventory lightGUI = plugin.getServer().createInventory(player, 54, NamedTextColor.DARK_RED + "TARDIS Lights");
                lightGUI.setContents(lightStacks);
                player.openInventory(lightGUI);
            }, 5L);
        }
    }
}
