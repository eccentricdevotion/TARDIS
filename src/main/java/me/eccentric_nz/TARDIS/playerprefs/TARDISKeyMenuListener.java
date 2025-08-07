/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.*;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISKeyMenuListener extends TARDISMenuListener {

    public static final TreeMap<Material, NamedTextColor> COLOUR_LOOKUP = new TreeMap<>();
    public static final HashMap<NamedTextColor, Material> REVERSE_LOOKUP = new HashMap<>();
    private final Material material;

    public TARDISKeyMenuListener(TARDIS plugin) {
        super(plugin);
        COLOUR_LOOKUP.put(Material.WHITE_WOOL, NamedTextColor.WHITE);
        COLOUR_LOOKUP.put(Material.MAGENTA_WOOL, NamedTextColor.LIGHT_PURPLE);
        COLOUR_LOOKUP.put(Material.PINK_WOOL, NamedTextColor.RED);
        COLOUR_LOOKUP.put(Material.ORANGE_WOOL, NamedTextColor.GOLD);
        COLOUR_LOOKUP.put(Material.YELLOW_WOOL, NamedTextColor.YELLOW);
        COLOUR_LOOKUP.put(Material.LIME_WOOL, NamedTextColor.GREEN);
        COLOUR_LOOKUP.put(Material.CYAN_WOOL, NamedTextColor.AQUA);
        COLOUR_LOOKUP.put(Material.LIGHT_BLUE_WOOL, NamedTextColor.BLUE);
        COLOUR_LOOKUP.put(Material.PURPLE_WOOL, NamedTextColor.DARK_PURPLE);
        COLOUR_LOOKUP.put(Material.RED_WOOL, NamedTextColor.DARK_RED);
        COLOUR_LOOKUP.put(Material.GREEN_WOOL, NamedTextColor.DARK_GREEN);
        COLOUR_LOOKUP.put(Material.BROWN_WOOL, NamedTextColor.DARK_AQUA);
        COLOUR_LOOKUP.put(Material.BLUE_WOOL, NamedTextColor.DARK_BLUE);
        COLOUR_LOOKUP.put(Material.LIGHT_GRAY_WOOL, NamedTextColor.GRAY);
        COLOUR_LOOKUP.put(Material.GRAY_WOOL, NamedTextColor.DARK_GRAY);
        COLOUR_LOOKUP.put(Material.BLACK_WOOL, NamedTextColor.BLACK);
        for (Map.Entry<Material, NamedTextColor> map : COLOUR_LOOKUP.entrySet()) {
            REVERSE_LOOKUP.put(map.getValue(), map.getKey());
        }
        Material keyMaterial;
        try {
            keyMaterial = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            keyMaterial = Material.GOLD_NUGGET;
        }
        this.material = keyMaterial;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISKeyMenuInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                TARDIS.plugin.debug("TARDISKeyMenuListener");
                event.setCancelled(true);
            }
            return;
        }
        InventoryView view = event.getView();
        switch (slot) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16 -> {
                event.setCancelled(true);
                // set display name of key in slot 18
                ItemStack key = view.getItem(18);
                if (key == null || !key.getType().equals(material) || !key.hasItemMeta()) {
                    return;
                }
                // get display name of selected key
                ItemStack choice = view.getItem(slot);
                ItemMeta choiceMeta = choice.getItemMeta();
                ItemMeta keyMeta = key.getItemMeta();
                CustomModelDataComponent component = choiceMeta.getCustomModelDataComponent();
                CustomModelDataComponent transfer = keyMeta.getCustomModelDataComponent();
                transfer.setFloats(component.getFloats());
                keyMeta.setCustomModelDataComponent(transfer);
                // personalise
                keyMeta.getPersistentDataContainer().set(TARDIS.plugin.getTimeLordUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                // set lore
                List<Component> lore;
                if (keyMeta.hasLore()) {
                    lore = keyMeta.lore();
                } else {
                    lore = new ArrayList<>();
                }
                Component belongs = Component.text("This key belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC);
                if (lore != null && !lore.contains(belongs)) {
                    lore.add(belongs);
                    lore.add(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                    keyMeta.lore(lore);
                }
                key.setItemMeta(keyMeta);
            }
            case 18 -> {
                // get item on cursor
                ItemStack cursor = event.getCursor();
                if (!cursor.getType().equals(Material.BLAZE_ROD) || !cursor.hasItemMeta()) {
                    return;
                }
                ItemMeta meta = cursor.getItemMeta();
                if (!meta.hasDisplayName()) {
                    return;
                }
                // set wool colour from display name of placed key
                NamedTextColor color = TARDISStaticUtils.getColor(meta.displayName());
                Material wool = TARDISKeyMenuListener.REVERSE_LOOKUP.getOrDefault(color, Material.WHITE_WOOL);
                view.setItem(19, ItemStack.of(wool));
            }
            case 19 -> {
                event.setCancelled(true);
                // set display name colour of key in slot 18
                ItemStack key = view.getItem(18);
                if (key == null || !key.getType().equals(material) || !key.hasItemMeta()) {
                    return;
                }
                // get current colour of wool
                ItemStack choice = view.getItem(19);
                Material wool = getNextWool(choice.getType());
                // set wool colour to next in line
                view.setItem(19, ItemStack.of(wool));
                // set key display name colour
                NamedTextColor display = COLOUR_LOOKUP.get(wool);
                ItemMeta key_im = key.getItemMeta();
                key_im.displayName(Component.text("TARDIS Key", display).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                key.setItemMeta(key_im);
            }
            case 26 -> {
                // close
                event.setCancelled(true);
                close(player);
            }
            default -> event.setCancelled(true);
        }
    }

    private Material getNextWool(Material current) {
        Material index = COLOUR_LOOKUP.higherKey(current);
        return (index != null) ? index : COLOUR_LOOKUP.firstKey();
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyMenuClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISKeyMenuInventory)) {
            return;
        }
        InventoryView view = event.getView();
        ItemStack key = view.getItem(18);
        if (key != null) {
            Player p = (Player) event.getPlayer();
            Location loc = p.getLocation();
            loc.getWorld().dropItemNaturally(loc, key);
            view.setItem(18, ItemStack.of(Material.AIR));
        }
    }
}
