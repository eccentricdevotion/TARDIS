package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.desktop.TARDISWallListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class TARDISVariableLightBlocksListener extends TARDISWallListener {

    private final TARDIS plugin;

    public TARDISVariableLightBlocksListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onVariableLightBlockMenuOpen(InventoryOpenEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_RED + "Variable Light Blocks")) {
            Player player = (Player) event.getPlayer();
            scroll.put(player.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVariableLightBlockClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (!name.equals(ChatColor.DARK_RED + "Variable Light Blocks")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        switch (slot) {
            case 8 -> {
                // scroll up
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scroll(view, scroll.get(uuid) + 1, true, uuid);
                }
            }
            case 17 -> {
                // scroll down
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scroll(view, scroll.get(uuid) - 1, false, uuid);
                }
            }
            case 26 -> {
                // default wall
                String wall = getWallFloor(uuid, true);
                setVariableLightBlock(player, wall);
            }
            case 35 -> {
                // default floor
                String floor = getWallFloor(uuid, false);
                setVariableLightBlock(player, floor);
            }
            case 53 -> close(player); // close
            default -> {
                // get block type and data
                ItemStack choice = view.getItem(slot);
                setVariableLightBlock(player, choice.getType().toString());
            }
        }
    }

    private void setVariableLightBlock(Player player, String block) {
        // get player's TARDIS
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        UUID uuid = player.getUniqueId();
        if (rst.fromUUID(uuid.toString())) {
            int id = rst.getTardisId();
            // remember choice
            HashMap<String, Object> set = new HashMap<>();
            set.put("material", block);
            ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
            if (rslp.fromID(rst.getTardisId())) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("light_prefs", set, where);
            } else {
                set.put("tardis_id", id);
                plugin.getQueryFactory().doSyncInsert("light_prefs", set);
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // go back to Lights GUI
                ItemStack[] lightStacks = new TARDISLightsInventory(plugin, id, uuid).getGUI();
                Inventory lightGUI = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Lights");
                lightGUI.setContents(lightStacks);
                player.openInventory(lightGUI);
            }, 5L);
        }
    }
}
