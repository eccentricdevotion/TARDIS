package me.eccentric_nz.TARDIS.rooms.architectural;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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
    private final HashMap<UUID, Integer> scroll_start = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final int rows;
    private final ItemStack[][] blueprints;
    private List<Player> players;

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
                if (players.size() > 9) {
                    // scroll left
                    int startl;
                    int max = players.size() - 9;
                    if (scroll_start.containsKey(uuid)) {
                        startl = scroll_start.get(uuid) + 1;
                        if (startl >= max) {
                            startl = max;
                        }
                    } else {
                        startl = 1;
                    }
                    scroll_start.put(uuid, startl);
                    for (int i = 0; i < 9; i++) {
                        setPlayerSlot(view, (36 + i), players.get(startl + i));
                    }
                }
            }
            case 46 -> {
                if (players.size() > 9) {
                    // scroll right
                    int startr;
                    if (scroll_start.containsKey(uuid)) {
                        startr = scroll_start.get(uuid) - 1;
                        if (startr <= 0) {
                            startr = 0;
                        }
                    } else {
                        startr = 0;
                    }
                    scroll_start.put(uuid, startr);
                    for (int i = 0; i < 9; i++) {
                        setPlayerSlot(view, (36 + i), players.get(startr + i));
                    }
                }
            }
            case 49 -> {
                // give
                if (selected_slot.containsKey(uuid) && selected_player.containsKey(uuid)) {
                    ItemStack room = view.getItem(selected_slot.get(uuid));
                    ItemMeta rim = room.getItemMeta();
                    PersistentDataContainer pdc = rim.getPersistentDataContainer();
                    String perm = pdc.get(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING);
                    ItemStack head = view.getItem(selected_player.get(uuid));
                    UUID receiver = UUID.fromString(ComponentUtils.stripColour(head.getItemMeta().lore().getFirst()));
                    // check receiving player doesn't have this permission already
                    if (!TARDISPermission.hasPermission(receiver, perm)) {
                        // give a Blueprint disk to the player
                        ItemStack gift = room.clone();
                        ItemMeta gim = gift.getItemMeta();
                        gim.getPersistentDataContainer().set(TARDIS.plugin.getTimeLordUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), receiver);
                        List<Component> lore = gim.lore();
                        lore.add(Component.text("Valid only for"));
                        lore.add(Component.text(player.getName()));
                        gim.lore(lore);
                        gift.setItemMeta(gim);
                        Player recipient = plugin.getServer().getPlayer(receiver);
                        if (recipient != null && recipient.isOnline()) {
                            recipient.give(gift);
                            String r = ComponentUtils.stripColour(gim.displayName());
                            plugin.getMessenger().send(recipient, TardisModule.TARDIS, "GIVE_ITEM", player.getName(), "a " + r + " Blueprint Disk");
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BLUEPRINT_HAS");
                    }
                }
            }
            case 53 -> close(player);
            default -> {
                ItemStack disk = view.getItem(slot);
                // 0 - 27 // select blueprint
                if (disk.getType() == Material.MUSIC_DISC_MELLOHI) {
                    selected_slot.put(uuid, slot);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BLUEPRINT_NOT_GRANTED");
                }
            }
        }
    }

    private void setPlayerSlot(InventoryView view, int slot, Player player) {
        ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) head.getItemMeta();
        skull.setOwningPlayer(player);
        skull.displayName(Component.text(player.getName()));
        skull.lore(List.of(Component.text(player.getUniqueId().toString())));
        head.setItemMeta(skull);
        view.setItem(slot, head);
    }

    private void scrollDisks(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 2)) || (!up && row >= 0)) {
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
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    PersistentDataContainer pdc = im.getPersistentDataContainer();
                    String perm = pdc.get(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING);
                    if (!perms.contains(perm)) {
                        view.setItem(slot, is.withType(Material.MUSIC_DISC_RELIC));
                    } else {
                        view.setItem(slot, is);
                    }
                } else {
                    view.setItem(slot, null);
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
            players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
            players.remove(p);
        }
    }
}
