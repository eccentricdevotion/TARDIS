package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class TARDISRecipeInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;
    private final RecipeCategory category;
    private final HashMap<String, String> recipeItems = new HashMap<>();

    public TARDISRecipeInventory(TARDIS plugin, RecipeCategory category) {
        recipeItems.put("ACID_BATTERY", "acid-battery");
        recipeItems.put("ARTRON_STORAGE_CELL", "cell");
        recipeItems.put("AUTHORISED_CONTROL_DISK", "control");
        recipeItems.put("BIO_SCANNER_CIRCUIT", "bio-circuit");
        recipeItems.put("BIOME_STORAGE_DISK", "biome-disk");
        recipeItems.put("BLANK_STORAGE_DISK", "blank");
        recipeItems.put("BLASTER_BATTERY", "battery");
        recipeItems.put("BOWL_OF_CUSTARD", "custard");
        recipeItems.put("DIAMOND_DISRUPTOR_CIRCUIT", "d-circuit");
        recipeItems.put("EMERALD_ENVIRONMENT_CIRCUIT", "e-circuit");
        recipeItems.put("FISH_FINGER", "fish-finger");
        recipeItems.put("FOB_WATCH", "watch");
        recipeItems.put("HANDLES", "handles");
        recipeItems.put("IGNITE_CIRCUIT", "ignite-circuit");
        recipeItems.put("JAMMY_DODGER", "jammy-dodger");
        recipeItems.put("KNOCKBACK_CIRCUIT", "k-circuit");
        recipeItems.put("LANDING_PAD", "pad");
        recipeItems.put("ORANGE_JELLY_BABY", "jelly-baby");
        recipeItems.put("PAINTER_CIRCUIT", "painter");
        recipeItems.put("PAPER_BAG", "paper-bag");
        recipeItems.put("PERCEPTION_CIRCUIT", "p-circuit");
        recipeItems.put("PERCEPTION_FILTER", "filter");
        recipeItems.put("PICKUP_ARROWS_CIRCUIT", "arrow-circuit");
        recipeItems.put("PLAYER_STORAGE_DISK", "player-disk");
        recipeItems.put("PRESET_STORAGE_DISK", "preset-disk");
        recipeItems.put("RED_BOW_TIE", "bow-tie");
        recipeItems.put("REDSTONE_ACTIVATOR_CIRCUIT", "r-circuit");
        recipeItems.put("RIFT_CIRCUIT", "rift-circuit");
        recipeItems.put("RIFT_MANIPULATOR", "rift-manipulator");
        recipeItems.put("RUST_PLAGUE_SWORD", "rust");
        recipeItems.put("SAVE_STORAGE_DISK", "save-disk");
        recipeItems.put("SERVER_ADMIN_CIRCUIT", "a-circuit");
        recipeItems.put("SONIC_BLASTER", "blaster");
        recipeItems.put("SONIC_GENERATOR", "generator");
        recipeItems.put("SONIC_OSCILLATOR", "oscillator");
        recipeItems.put("SONIC_SCREWDRIVER", "sonic");
        recipeItems.put("STATTENHEIM_REMOTE", "remote");
        recipeItems.put("TARDIS_ARS_CIRCUIT", "ars-circuit");
        recipeItems.put("TARDIS_ARTRON_FURNACE", "furnace");
        recipeItems.put("TARDIS_BIOME_READER", "reader");
        recipeItems.put("TARDIS_CHAMELEON_CIRCUIT", "c-circuit");
        recipeItems.put("TARDIS_COMMUNICATOR", "communicator");
        recipeItems.put("TARDIS_INPUT_CIRCUIT", "i-circuit");
        recipeItems.put("TARDIS_INVISIBILITY_CIRCUIT", "invisible");
        recipeItems.put("TARDIS_KEY", "key");
        recipeItems.put("TARDIS_KEYBOARD_EDITOR", "keyboard");
        recipeItems.put("TARDIS_LOCATOR_CIRCUIT", "l-circuit");
        recipeItems.put("TARDIS_LOCATOR", "locator");
        recipeItems.put("TARDIS_MATERIALISATION_CIRCUIT", "m-circuit");
        recipeItems.put("TARDIS_MEMORY_CIRCUIT", "memory-circuit");
        recipeItems.put("TARDIS_RANDOMISER_CIRCUIT", "randomiser-circuit");
        recipeItems.put("TARDIS_REMOTE_KEY", "r-key");
        recipeItems.put("TARDIS_SCANNER_CIRCUIT", "scanner-circuit");
        recipeItems.put("TARDIS_SCHEMATIC_WAND", "wand");
        recipeItems.put("TARDIS_STATTENHEIM_CIRCUIT", "s-circuit");
        recipeItems.put("TARDIS_TELEPATHIC_CIRCUIT", "telepathic");
        recipeItems.put("TARDIS_TEMPORAL_CIRCUIT", "t-circuit");
        recipeItems.put("THREE_D_GLASSES", "glasses");
        recipeItems.put("TIME_ROTOR_EARLY", "rotor_early");
        recipeItems.put("TIME_ROTOR_ELEVENTH", "rotor_eleventh");
        recipeItems.put("TIME_ROTOR_TENTH", "rotor_tenth");
        recipeItems.put("TIME_ROTOR_TWELFTH", "rotor_twelfth");
        recipeItems.put("VORTEX_MANIPULATOR", "vortex");
        recipeItems.put("ADMIN_UPGRADE", "a-upgrade");
        recipeItems.put("BIO_SCANNER_UPGRADE", "b-upgrade");
        recipeItems.put("DIAMOND_UPGRADE", "d-upgrade");
        recipeItems.put("EMERALD_UPGRADE", "e-upgrade");
        recipeItems.put("IGNITE_UPGRADE", "i-upgrade");
        recipeItems.put("KNOCKBACK_UPGRADE", "k-upgrade");
        recipeItems.put("PAINTER_UPGRADE", "p-upgrade");
        recipeItems.put("PICKUP_ARROWS_UPGRADE", "pu-upgrade");
        recipeItems.put("REDSTONE_UPGRADE", "r-upgrade");
        this.plugin = plugin;
        this.category = category;
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setCustomModelData(GUIChameleonPresets.BACK.getCustomModelData());
        back.setItemMeta(but);
        stack[0] = back;
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Click a button below", "to see the recipe", "for that item"));
        info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
        info.setItemMeta(info_im);
        stack[4] = info;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[8] = close;
        int i = 9;
        for (RecipeItem item : RecipeItem.values()) {
            if (item.getCategory() == category) {
                String arg = recipeItems.get(item.toString());
                String str = item.toRecipeString();
                if (arg != null) {
                    ItemStack result;
                    if (isShapeless(str)) {
                        ShapelessRecipe shapeless = plugin.getIncomposita().getShapelessRecipes().get(str);
                        result = shapeless.getResult();
                    } else {
                        ShapedRecipe shaped = plugin.getFigura().getShapedRecipes().get(str);
                        result = shaped.getResult();
                    }
                    ItemMeta im = result.getItemMeta();
                    im.setDisplayName(str);
                    im.setLore(Collections.singletonList("/trecipe " + arg));
                    im.setCustomModelData(item.getCustomModelData());
                    im.addItemFlags(ItemFlag.values());
                    result.setItemMeta(im);
                    stack[i] = result;
                    i++;
                }
            }
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }

    private boolean isShapeless(String s) {
        return !s.contains("Blank") && !s.contains("Authorised") && (s.contains("Jelly") || s.contains("Disk") || s.contains("Custard") || s.contains("Wand") || s.contains("Upgrade"));
    }
}
