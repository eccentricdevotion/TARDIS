package me.eccentric_nz.TARDIS.rooms.architectural;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArchitecturalBlueprintsListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selected_slot = new HashMap<>();
    private final HashMap<UUID, Integer> selected_player = new HashMap<>();
    private final HashMap<UUID, Integer> scroll = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final Integer rows;
    private final ItemStack[][] blueprints;

    public ArchitecturalBlueprintsListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        rows = BlueprintRoom.PERMS.size() / 9 + 1;
        blueprints = TreeBlueprints.getBlueprints();
    }

    @EventHandler
    public void onArchitecturalBlueprintClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ArchitecturalBlueprintsInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        InventoryView view = event.getView();
        switch (slot) {
            case 27 -> {
                // scroll up
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scrollDisks(view, scroll.get(uuid) + 1, true, uuid);
                }
            }
            case 28 -> {
                // scroll down
                if (!scrolling.contains(uuid)) {
                    scrolling.add(uuid);
                    scrollDisks(view, scroll.get(uuid) - 1, false, uuid);
                }
            }
            case 36, 37, 38, 39, 40, 41, 42, 43, 44 -> {
                ItemStack head = event.getView().getItem(slot);
                // select player
                if (head.getType() == Material.PLAYER_HEAD) {
                    selected_player.put(uuid, slot);
                }
            }
            case 45 -> {
                // scroll left
            }
            case 46 -> {
                // scroll right
            }
            case 49 -> {
                // give
                if (selected_slot.containsKey(uuid) && selected_player.containsKey(uuid)) {
                    // check receiving player doesn't have this permission already
                    // give a Blueprint disk to the player
                }
            }
            case 53 -> close(player);
            default -> selected_slot.put(uuid, slot); // 0 - 27 // select blueprint
        }
    }

    private void scrollDisks(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 3)) || (!up && row >= 0)) {
            scroll.put(uuid, row);
            setDiskSlots(view, row, uuid);
        } else {
            scrolling.remove(uuid);
        }
    }

    private void setDiskSlots(InventoryView view, int row, UUID uuid) {
        ResultSetBlueprint rsb = new ResultSetBlueprint(plugin);
        List<String> perms = rsb.getRoomBlueprints(uuid.toString());
        int slot = 0;
        for (int r = row; r < row + 3; r++) {
            for (int c = 0; c < 9; c++) {
                ItemStack is = blueprints[r][c];
                ItemMeta im = is.getItemMeta();
                PersistentDataContainer pdc = im.getPersistentDataContainer();
                String perm = pdc.get(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING);
                if (!perms.contains(perm)) {
                    view.setItem(slot, is.withType(Material.MUSIC_DISC_RELIC));
                } else {
                    view.setItem(slot, is);
                }
                slot++;
            }
        }
        scrolling.remove(uuid);
    }

    @EventHandler
    public void onBlueprintMenuOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder(false) instanceof ArchitecturalBlueprintsInventory) {
            Player p = (Player) event.getPlayer();
            scroll.put(p.getUniqueId(), 0);
        }
    }
}
