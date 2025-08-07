package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TARDISShowSeedRecipeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;
    private final String type;
    private final Material material;

    public TARDISShowSeedRecipeInventory(TARDIS plugin, String type, Material material) {
        this.plugin = plugin;
        this.type = type;
        this.material = material;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS " + type + " seed recipe", NamedTextColor.DARK_RED));
        this.inventory.setContents(getRecipeItems());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getRecipeItems() {
        ItemStack[] stacks = new ItemStack[27];
        // redstone torch
        ItemStack red = ItemStack.of(Material.REDSTONE_TORCH, 1);
        // lapis block
        ItemStack lapis = ItemStack.of(Material.LAPIS_BLOCK, 1);
        // interior wall
        ItemStack in_wall = ItemStack.of(Material.ORANGE_WOOL, 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.displayName(Component.text("Interior walls"));
        in_meta.lore(List.of(Component.text("Any valid Wall/Floor block")));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = ItemStack.of(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.displayName(Component.text("Interior floors"));
        fl_meta.lore(List.of(Component.text("Any valid Wall/Floor block")));
        in_floor.setItemMeta(fl_meta);
        // seed block
        ItemStack block = ItemStack.of(material, 1);
        // tardis type
        Schematic schm = Consoles.getBY_NAMES().get(type);
        ItemStack tardis;
        NamespacedKey model = TARDISDisplayItem.CUSTOM.getCustomModel();
        if (schm.isCustom()) {
            tardis = ItemStack.of(schm.getSeedMaterial(), 1);
        } else {
            try {
                TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(type);
                tardis = ItemStack.of(tdi.getMaterial(), 1);
                model = tdi.getCustomModel();
            } catch (IllegalArgumentException e) {
                tardis = ItemStack.of(TARDISDisplayItem.CUSTOM.getMaterial(), 1);
            }
        }
        ItemMeta seed = tardis.getItemMeta();
        seed.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
        // set display name
        seed.displayName(ComponentUtils.toGold("TARDIS Seed Block"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(type));
        lore.add(Component.text("Walls: ORANGE_WOOL"));
        lore.add(Component.text("Floors: LIGHT_GRAY_WOOL"));
        lore.add(Component.text("Chameleon: FACTORY"));
        seed.lore(lore);
        tardis.setItemMeta(seed);
        stacks[0] = red;
        stacks[9] = lapis;
        stacks[11] = in_wall;
        stacks[17] = tardis;
        stacks[18] = block;
        stacks[20] = in_floor;
        return stacks;
    }
}
