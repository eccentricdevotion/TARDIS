package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TelepathicBiomeListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> scroll = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final ItemStack[][] biomes;
    private final int rows;

    public TelepathicBiomeListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        rows = EnvironmentBiomes.OVERWORLD.size() / 8 + 1;
        biomes = getBiomes();
    }

    @EventHandler
    public void onWallMenuOpen(InventoryOpenEvent event) {
        String name = event.getView().getTitle();
        if (name.equals(ChatColor.DARK_RED + "Telepathic Biome Finder")) {
            Player p = (Player) event.getPlayer();
            scroll.put(p.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBiomeMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (!name.equals(ChatColor.DARK_RED + "Telepathic Biome Finder")) {
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
            // scroll up
            case 8 -> {
                if (view.getItem(8) != null) {
                    if (!scrolling.contains(uuid)) {
                        scrolling.add(uuid);
                        scroll(view, scroll.get(uuid) + 1, true, uuid);
                    }
                }
            }
            // scroll down
            case 17 -> {
                if (view.getItem(17) != null) {
                    if (!scrolling.contains(uuid)) {
                        scrolling.add(uuid);
                        scroll(view, scroll.get(uuid) - 1, false, uuid);
                    }
                }
            }
            // close
            case 53 -> close(player);
            // run a command
            default -> {
                ItemStack choice = view.getItem(slot);
                if (choice != null) {
                    // get the biome
                    ItemMeta im = choice.getItemMeta();
                    String enumStr = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
                    player.performCommand("tardistravel biome " + enumStr);
                    close(player);
                }
            }
        }
    }

    private void scroll(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 5)) || (!up && row >= 0)) {
            scroll.put(uuid, row);
            setSlots(view, row, uuid);
        } else {
            scrolling.remove(uuid);
        }
    }

    private void setSlots(InventoryView view, int row, UUID uuid) {
        int slot = 0;
        for (int r = row; r < row + 6; r++) {
            for (int c = 0; c < 8; c++) {
                view.setItem(slot, biomes[r][c]);
                if (slot % 9 == 7) {
                    slot += 2;
                } else {
                    slot++;
                }
            }
        }
        scrolling.remove(uuid);
    }

    private ItemStack[][] getBiomes() {
        ItemStack[][] stacks = new ItemStack[rows][8];
        int r = 0;
        int c = 0;
        for (Biome biome : EnvironmentBiomes.OVERWORLD) {
            ItemStack is = new ItemStack(EnvironmentBiomes.BIOME_BLOCKS.get(biome), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(TARDISStringUtils.capitalise(biome.toString()));
            is.setItemMeta(im);
            stacks[r][c] = is;
            c++;
            if (c == 8) {
                r++;
                c = 0;
            }
        }
        return stacks;
    }
}
