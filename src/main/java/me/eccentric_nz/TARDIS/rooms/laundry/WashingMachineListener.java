package me.eccentric_nz.TARDIS.rooms.laundry;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WashingMachineListener extends TARDISMenuListener {

    private final Set<Material> DYEABLE = new HashSet<>();

    public WashingMachineListener(TARDIS plugin) {
        super(plugin);
        DYEABLE.addAll(Tag.WOOL.getValues());
        DYEABLE.addAll(Tag.WOOL_CARPETS.getValues());
        DYEABLE.addAll(Tag.ITEMS_DYEABLE.getValues()); // leather player / horse armour & wolf armour
    }

    @EventHandler
    public void onArchitecturalBlueprintClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof WashingMachineInventory)) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(slot > 17);
        Player player = (Player) event.getWhoClicked();
        InventoryView view = event.getView();
        switch (slot) {
            case 21 -> processTrims(view); // wash off trim patterns
            case 23 -> processDyed(view); // remove dye color from leather armour and wool / carpets
            case 26 -> close(player);
            default -> { }
        }
    }

    private void processTrims(InventoryView view) {
        for (int i = 0; i < 9; i++) {
            ItemStack armour = view.getItem(i);
            if (armour != null) {
                Material material = armour.getType();
                if (Tag.ITEMS_TRIMMABLE_ARMOR.isTagged(material)) {
                    ArmorMeta meta = (ArmorMeta) armour.getItemMeta();
                    ArmorTrim trim = meta.getTrim();
                    TrimPattern tp = trim.getPattern();
                    NamespacedKey key = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(tp);
                    ItemStack template = ItemStack.of(getTemplate(key));
                    view.setItem(i + 9, template);
                    meta.setTrim(null);
                    armour.setItemMeta(meta);
                    view.setItem(i, armour);
                }
            }
        }
    }

    private @NotNull Material getTemplate(NamespacedKey key) {
        String which = key.getKey().toUpperCase(Locale.ROOT);
        try {
            return Material.valueOf(which + "_ARMOR_TRIM_SMITHING_TEMPLATE");
        } catch (IllegalArgumentException e) {
            return Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE;
        }
    }

    private void processDyed(InventoryView view) {
        for (int i = 0; i < 9; i++) {
            ItemStack dyed = view.getItem(i);
            if (dyed != null) {
                Material material = dyed.getType();
                if (DYEABLE.contains(material)) {
                    ItemMeta im = dyed.getItemMeta();
                    if (im instanceof LeatherArmorMeta armorMeta) {
                        armorMeta.setColor(null);
                        dyed.setItemMeta(armorMeta);
                        view.setItem(i, dyed);
                    } else {
                        ItemStack bleached = switch (dyed.getType()) {
                            case ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, YELLOW_WOOL, LIME_WOOL,
                                 PINK_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GRAY_WOOL, GREEN_WOOL,
                                 RED_WOOL, BLACK_WOOL -> ItemStack.of(Material.WHITE_WOOL, dyed.getAmount());
                            default -> ItemStack.of(Material.WHITE_CARPET, dyed.getAmount());
                        };
                        view.setItem(i, bleached);
                    }
                }
            }
        }
    }
}
