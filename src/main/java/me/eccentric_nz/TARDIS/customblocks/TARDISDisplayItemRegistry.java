package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public final class TARDISDisplayItemRegistry {
    private static final HashMap<String, TARDISDisplayItem> BY_ID = new HashMap<>();

    private static final HashMap<String, TARDISDisplayItem> BY_NAME = new HashMap<>();
    private static final HashMap<NamespacedKey, TARDISDisplayItem> BY_MODEL = new HashMap<>();
    private static final HashMap<Integer, TARDISDisplayItem> BY_MUSHROOM_STEM = new HashMap<>() {
        {
            put(1, TARDISChemistryDisplayItem.BLUE_LAMP);
            put(2, TARDISChemistryDisplayItem.RED_LAMP);
            put(3, TARDISChemistryDisplayItem.PURPLE_LAMP);
            put(4, TARDISChemistryDisplayItem.GREEN_LAMP);
            put(10000001, TARDISChemistryDisplayItem.BLUE_LAMP_ON);
            put(10000002, TARDISChemistryDisplayItem.RED_LAMP_ON);
            put(10000003, TARDISChemistryDisplayItem.PURPLE_LAMP_ON);
            put(10000004, TARDISChemistryDisplayItem.GREEN_LAMP_ON);
            put(5, TARDISChemistryDisplayItem.HEAT_BLOCK);
            put(46, TARDISBlockDisplayItem.HEXAGON);
            put(47, TARDISBlockDisplayItem.ROUNDEL);
            put(48, TARDISBlockDisplayItem.ROUNDEL_OFFSET);
            put(49, TARDISBlockDisplayItem.COG);
            put(50, TARDISBlockDisplayItem.ADVANCED_CONSOLE);
            put(51, TARDISBlockDisplayItem.DISK_STORAGE);
            put(54, TARDISBlockDisplayItem.BLUE_BOX);
        }
    };

    private static final HashMap<Integer, TARDISDisplayItem> BY_RED_MUSHROOM = new HashMap<>() {
        {
            put(40, TARDISChemistryDisplayItem.CREATIVE);
            put(41, TARDISChemistryDisplayItem.COMPOUND);
            put(42, TARDISChemistryDisplayItem.REDUCER);
            put(43, TARDISChemistryDisplayItem.CONSTRUCTOR);
            put(44, TARDISChemistryDisplayItem.LAB);
            put(45, TARDISChemistryDisplayItem.PRODUCT);
        }
    };

    // Register TARDISDisplayItems for statically defined
    static {
        for (TARDISDisplayItem tdi : TARDISBlockDisplayItem.values()) {
            register(tdi);
        }
        for (TARDISDisplayItem tdi : TARDISChemistryDisplayItem.values()) {
            register(tdi);
        }
        for (TARDISDisplayItem tdi : TARDISLightDisplayItem.values()) {
            register(tdi);
        }
        for (TARDISDisplayItem tdi : TARDISSeedDisplayItem.values()) {
            register(tdi);
        }
    }


    public static void register(Class<? extends Enum<?>> enumClass) {
        for (Enum<?> constraint : enumClass.getEnumConstants()) {
            if (constraint instanceof TARDISDisplayItem displayItem) {
                register(displayItem);
            }
        }
    }

    public static void register(TARDISDisplayItem displayItem) {
        BY_ID.put(displayItem.getId(), displayItem);
        BY_NAME.put(displayItem.getName().toLowerCase(Locale.ROOT), displayItem);
        NamespacedKey customModel = displayItem.getCustomModel();
        if (customModel != null) {
            BY_MODEL.put(customModel, displayItem);
        }
    }

    public static TARDISDisplayItem getByModel(NamespacedKey key) {
        return BY_MODEL.get(key);
    }

    public static TARDISDisplayItem getByDisplayName(Component name) {
        for (TARDISDisplayItem tdi : BY_NAME.values()) {
            if (ComponentUtils.stripColour(name).equals(tdi.getDisplayName())) {
                return tdi;
            }
        }
        return null;
    }

    public static TARDISDisplayItem getByItemDisplay(ItemDisplay display) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        if (im.hasItemModel()) {
            return getByModel(im.getItemModel());
        }
        if (im.hasDisplayName()) {
            return getByDisplayName(im.displayName());
        }
        return null;
    }

    public static HashMap<String, TARDISDisplayItem> getBY_NAME() {
        return BY_NAME;
    }

    public static HashMap<Integer, TARDISDisplayItem> getBY_MUSHROOM_STEM() {
        return BY_MUSHROOM_STEM;
    }

    public static HashMap<Integer, TARDISDisplayItem> getBY_RED_MUSHROOM() {
        return BY_RED_MUSHROOM;
    }

    public static Collection<TARDISDisplayItem> values() {
        return BY_NAME.values();
    }

    public static TARDISDisplayItem valueOf(String name) {
        return BY_ID.get(name);
    }
}
