package me.eccentric_nz.TARDIS.rooms.laundry;

import com.destroystokyo.paper.MaterialTags;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WashingMachineListener extends TARDISMenuListener {

    private final Set<Material> DYEABLE = new HashSet<>();

    public WashingMachineListener(TARDIS plugin) {
        super(plugin);
        DYEABLE.addAll(MaterialTags.COLORABLE.getValues());
        DYEABLE.addAll(Tag.CONCRETE_POWDERS.getValues());
        DYEABLE.addAll(Tag.CANDLES.getValues());
        DYEABLE.addAll(Tag.TERRACOTTA.getValues());
        DYEABLE.addAll(Tag.ITEMS_BUNDLES.getValues());
        DYEABLE.addAll(Tag.ITEMS_HARNESSES.getValues());
        DYEABLE.addAll(Tag.ITEMS_CAULDRON_CAN_REMOVE_DYE.getValues()); // leather player / horse / wolf armour
    }

    @EventHandler
    public void onWashingMachineClick(InventoryClickEvent event) {
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
            default -> {
            }
        }
    }

    private void processTrims(InventoryView view) {
        for (int i = 0; i < 9; i++) {
            ItemStack armour = view.getItem(i);
            if (armour != null) {
                Material material = armour.getType();
                if (Tag.ITEMS_TRIMMABLE_ARMOR.isTagged(material)) {
                    if (armour.hasData(DataComponentTypes.TRIM)) {
                        ItemArmorTrim trim = armour.getData(DataComponentTypes.TRIM);
                        TrimPattern tp = trim.armorTrim().getPattern();
                        NamespacedKey key = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(tp);
                        ItemStack template = ItemStack.of(getTemplate(key));
                        view.setItem(i + 9, template);
                        armour.unsetData(DataComponentTypes.TRIM);
                        view.setItem(i, armour);
                    }
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
                    if (dyed.hasData(DataComponentTypes.DYED_COLOR)) {
                        dyed.resetData(DataComponentTypes.DYED_COLOR);
                        view.setItem(i, dyed);
                    } else {
                        ItemStack bleached = switch (dyed.getType()) {
                            case ORANGE_CARPET, MAGENTA_CARPET, LIGHT_BLUE_CARPET, LIGHT_GRAY_CARPET, YELLOW_CARPET,
                                 LIME_CARPET, PINK_CARPET, CYAN_CARPET, PURPLE_CARPET, BLUE_CARPET, BROWN_CARPET,
                                 GRAY_CARPET, GREEN_CARPET, RED_CARPET, BLACK_CARPET -> ItemStack.of(Material.WHITE_CARPET, dyed.getAmount());
                            case ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, LIGHT_GRAY_WOOL, YELLOW_WOOL, LIME_WOOL,
                                 PINK_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GRAY_WOOL, GREEN_WOOL,
                                 RED_WOOL, BLACK_WOOL -> ItemStack.of(Material.WHITE_WOOL, dyed.getAmount());
                            case ORANGE_WOOL_SLAB, MAGENTA_WOOL_SLAB, LIGHT_BLUE_WOOL_SLAB, LIGHT_GRAY_WOOL_SLAB,
                                 YELLOW_WOOL_SLAB, LIME_WOOL_SLAB, PINK_WOOL_SLAB, CYAN_WOOL_SLAB, PURPLE_WOOL_SLAB,
                                 BLUE_WOOL_SLAB, BROWN_WOOL_SLAB, GRAY_WOOL_SLAB, GREEN_WOOL_SLAB, RED_WOOL_SLAB,
                                 BLACK_WOOL_SLAB -> ItemStack.of(Material.WHITE_WOOL_SLAB, dyed.getAmount());
                            case ORANGE_WOOL_STAIRS, MAGENTA_WOOL_STAIRS, LIGHT_BLUE_WOOL_STAIRS,
                                 LIGHT_GRAY_WOOL_STAIRS, YELLOW_WOOL_STAIRS, LIME_WOOL_STAIRS, PINK_WOOL_STAIRS,
                                 CYAN_WOOL_STAIRS, PURPLE_WOOL_STAIRS, BLUE_WOOL_STAIRS, BROWN_WOOL_STAIRS,
                                 GRAY_WOOL_STAIRS, GREEN_WOOL_STAIRS, RED_WOOL_STAIRS, BLACK_WOOL_STAIRS -> ItemStack.of(Material.WHITE_WOOL_STAIRS, dyed.getAmount());
                            case ORANGE_CONCRETE, MAGENTA_CONCRETE, LIGHT_BLUE_CONCRETE, LIGHT_GRAY_CONCRETE,
                                 YELLOW_CONCRETE, LIME_CONCRETE, PINK_CONCRETE, CYAN_CONCRETE, PURPLE_CONCRETE,
                                 BLUE_CONCRETE, BROWN_CONCRETE, GRAY_CONCRETE, GREEN_CONCRETE, RED_CONCRETE,
                                 BLACK_CONCRETE -> ItemStack.of(Material.WHITE_CONCRETE, dyed.getAmount());
                            case ORANGE_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER,
                                 YELLOW_CONCRETE_POWDER, LIME_CONCRETE_POWDER, PINK_CONCRETE_POWDER, CYAN_CONCRETE_POWDER, PURPLE_CONCRETE_POWDER,
                                 BLUE_CONCRETE_POWDER, BROWN_CONCRETE_POWDER, GRAY_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, RED_CONCRETE_POWDER,
                                 BLACK_CONCRETE_POWDER -> ItemStack.of(TARDISConstants.RANDOM.nextBoolean() ? Material.SAND : Material.GRAVEL, dyed.getAmount());
                            case ORANGE_CONCRETE_SLAB, MAGENTA_CONCRETE_SLAB, LIGHT_BLUE_CONCRETE_SLAB,
                                 LIGHT_GRAY_CONCRETE_SLAB, YELLOW_CONCRETE_SLAB, LIME_CONCRETE_SLAB, PINK_CONCRETE_SLAB,
                                 CYAN_CONCRETE_SLAB, PURPLE_CONCRETE_SLAB, BLUE_CONCRETE_SLAB, BROWN_CONCRETE_SLAB,
                                 GRAY_CONCRETE_SLAB, GREEN_CONCRETE_SLAB, RED_CONCRETE_SLAB, BLACK_CONCRETE_SLAB -> ItemStack.of(Material.WHITE_CONCRETE_SLAB, dyed.getAmount());
                            case ORANGE_CONCRETE_STAIRS, MAGENTA_CONCRETE_STAIRS, LIGHT_BLUE_CONCRETE_STAIRS,
                                 LIGHT_GRAY_CONCRETE_STAIRS, YELLOW_CONCRETE_STAIRS, LIME_CONCRETE_STAIRS,
                                 PINK_CONCRETE_STAIRS, CYAN_CONCRETE_STAIRS, PURPLE_CONCRETE_STAIRS,
                                 BLUE_CONCRETE_STAIRS, BROWN_CONCRETE_STAIRS, GRAY_CONCRETE_STAIRS,
                                 GREEN_CONCRETE_STAIRS, RED_CONCRETE_STAIRS, BLACK_CONCRETE_STAIRS -> ItemStack.of(Material.WHITE_CONCRETE_STAIRS, dyed.getAmount());
                            case WHITE_STAINED_GLASS, ORANGE_STAINED_GLASS, MAGENTA_STAINED_GLASS,
                                 LIGHT_BLUE_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, YELLOW_STAINED_GLASS,
                                 LIME_STAINED_GLASS, PINK_STAINED_GLASS, CYAN_STAINED_GLASS, PURPLE_STAINED_GLASS,
                                 BLUE_STAINED_GLASS, BROWN_STAINED_GLASS, GRAY_STAINED_GLASS, GREEN_STAINED_GLASS,
                                 RED_STAINED_GLASS, BLACK_STAINED_GLASS -> ItemStack.of(Material.GLASS, dyed.getAmount());
                            case WHITE_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE,
                                 LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE,
                                 YELLOW_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE,
                                 CYAN_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE,
                                 BROWN_STAINED_GLASS_PANE, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE,
                                 RED_STAINED_GLASS_PANE, BLACK_STAINED_GLASS_PANE -> ItemStack.of(Material.GLASS_PANE, dyed.getAmount());
                            case WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX,
                                 LIGHT_GRAY_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX,
                                 CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX,
                                 GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX, BLACK_SHULKER_BOX -> ItemStack.of(Material.SHULKER_BOX, dyed.getAmount());
                            case ORANGE_BED, MAGENTA_BED, LIGHT_BLUE_BED, LIGHT_GRAY_BED, YELLOW_BED, LIME_BED,
                                 PINK_BED, CYAN_BED, PURPLE_BED, BLUE_BED, BROWN_BED, GRAY_BED, GREEN_BED, RED_BED,
                                 BLACK_BED -> ItemStack.of(Material.WHITE_BED, dyed.getAmount());
                            case WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA, LIGHT_BLUE_TERRACOTTA,
                                 LIGHT_GRAY_TERRACOTTA,
                                 YELLOW_TERRACOTTA, LIME_TERRACOTTA, PINK_TERRACOTTA, CYAN_TERRACOTTA,
                                 PURPLE_TERRACOTTA,
                                 BLUE_TERRACOTTA, BROWN_TERRACOTTA, GRAY_TERRACOTTA, GREEN_TERRACOTTA, RED_TERRACOTTA,
                                 BLACK_TERRACOTTA -> ItemStack.of(Material.TERRACOTTA, dyed.getAmount());
                            case WHITE_BUNDLE, ORANGE_BUNDLE, MAGENTA_BUNDLE, LIGHT_BLUE_BUNDLE, LIGHT_GRAY_BUNDLE,
                                 YELLOW_BUNDLE, LIME_BUNDLE, PINK_BUNDLE, CYAN_BUNDLE, PURPLE_BUNDLE,
                                 BLUE_BUNDLE, BROWN_BUNDLE, GRAY_BUNDLE, GREEN_BUNDLE, RED_BUNDLE,
                                 BLACK_BUNDLE -> ItemStack.of(Material.BUNDLE, dyed.getAmount());
                            case ORANGE_HARNESS, MAGENTA_HARNESS, LIGHT_BLUE_HARNESS, LIGHT_GRAY_HARNESS,
                                 YELLOW_HARNESS, LIME_HARNESS, PINK_HARNESS, CYAN_HARNESS, PURPLE_HARNESS,
                                 BLUE_HARNESS, BROWN_HARNESS, GRAY_HARNESS, GREEN_HARNESS, RED_HARNESS,
                                 BLACK_HARNESS -> ItemStack.of(Material.WHITE_HARNESS, dyed.getAmount());
                            case WHITE_CANDLE, ORANGE_CANDLE, MAGENTA_CANDLE, LIGHT_BLUE_CANDLE, LIGHT_GRAY_CANDLE,
                                 YELLOW_CANDLE, LIME_CANDLE, PINK_CANDLE, CYAN_CANDLE, PURPLE_CANDLE,
                                 BLUE_CANDLE, BROWN_CANDLE, GRAY_CANDLE, GREEN_CANDLE, RED_CANDLE,
                                 BLACK_CANDLE -> ItemStack.of(Material.CANDLE, dyed.getAmount());
                            default -> ItemStack.of(Material.WHITE_CUSHION, dyed.getAmount());
                        };
                        view.setItem(i, bleached);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWashingMachineClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof WashingMachineInventory)) {
            return;
        }
        Player player = ((Player) event.getPlayer());
        // drop any user placed items in the inventory
        InventoryView view = event.getView();
        for (int s = 0; s < 18; s++) {
            ItemStack userStack = view.getItem(s);
            if (userStack != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), userStack);
            }
        }
    }
}
