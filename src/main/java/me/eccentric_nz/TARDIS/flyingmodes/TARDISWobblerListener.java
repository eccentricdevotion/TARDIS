package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class TARDISWobblerListener extends TARDISWobblerSlot implements Listener {

    private final TARDIS plugin;
    private final Map<String, TARDISWobblerRunnable> isWobbling;
    private final List<Integer> directions = Arrays.asList(new Integer[]{16, 24, 26, 34});

    public TARDISWobblerListener(TARDIS plugin) {
        this.plugin = plugin;
        this.isWobbling = new HashMap<String, TARDISWobblerRunnable>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onWobblerClose(InventoryCloseEvent event) {
        final Inventory inv = event.getInventory();
        if (inv.getTitle().equals("Wobbler")) {
            Player player = (Player) event.getPlayer();
            if (isWobbling.containsKey(player.getName())) {
                plugin.getServer().getScheduler().cancelTask(isWobbling.get(player.getName()).getTaskId());
                isWobbling.remove(player.getName());
                player.sendMessage(plugin.getPluginName() + "Wobbling aborted!");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWobblerOpen(InventoryOpenEvent event) {
        final Inventory inv = event.getInventory();
        if (inv.getTitle().equals("Wobbler")) {
            final Player player = (Player) event.getPlayer();
            final String name = player.getName();
            // start the runnable
            TARDISWobblerRunnable wr = new TARDISWobblerRunnable(inv);
            final int id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, wr, 5L, 20L);
            wr.setTaskId(id);
            isWobbling.put(name, wr);
            // schedule a delayed task to close the inventory
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getServer().getScheduler().cancelTask(id);
                    int final_slot = isWobbling.get(name).getSlot();
                    isWobbling.remove(name);
                    player.closeInventory();
                    player.sendMessage(plugin.getPluginName() + "The final slot was :" + final_slot);
                }
            }, 600L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWobblerClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = event.getWhoClicked().getName();
        if (inv.getTitle().equals("Wobbler") && isWobbling.containsKey(name)) {
            int slot = event.getRawSlot();
            int old_slot = isWobbling.get(name).getSlot();
            if (directions.contains(slot)) {
                switch (slot) {
                    case 16: // up
                        int up = upSlot(old_slot);
                        if (bounds.contains(up)) {
                            inv.setItem(old_slot, vortex);
                            inv.setItem(up, box);
                            isWobbling.get(name).setSlot(up);
                        }
                        break;
                    case 24: // left
                        int left = leftSlot(old_slot);
                        if (bounds.contains(left)) {
                            inv.setItem(old_slot, vortex);
                            inv.setItem(left, box);
                            isWobbling.get(name).setSlot(left);
                        }
                        break;
                    case 26: // right
                        int right = rightSlot(old_slot);
                        if (bounds.contains(right)) {
                            inv.setItem(old_slot, vortex);
                            inv.setItem(right, box);
                            isWobbling.get(name).setSlot(right);
                        }
                        break;
                    case 34: // down
                        int down = downSlot(old_slot);
                        if (bounds.contains(down)) {
                            inv.setItem(old_slot, vortex);
                            inv.setItem(down, box);
                            isWobbling.get(name).setSlot(down);
                        }
                        break;
                    default:
                        break;
                }
            }
            event.setCancelled(true);
        }
    }

    public Map<String, TARDISWobblerRunnable> getIsWobbling() {
        return isWobbling;
    }
}
