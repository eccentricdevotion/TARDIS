/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleon;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything
 * desired. The owner can program the circuit to make it assume a specific
 * shape. If no appearance is specified, the TARDIS automatically choses its own
 * shape. When a TARDIS materialises in a new location, within the first
 * nanosecond of landing, its chameleon circuit analyses the surrounding area,
 * calculates a twelve-dimensional data map of all objects within a
 * thousand-mile radius and then determines which outer shell will best blend in
 * with the environment. According to the Eleventh Doctor, the TARDIS would
 * perform these functions, but then disguise itself as a 1960s era police box
 * anyway.
 *
 * @author eccentric_nz
 */
public class ChameleonInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;
    private final Adaption adapt;
    private final ChameleonPreset preset;
    private final String model;
    private final ItemStack on;
    private final ItemStack off;

    public ChameleonInventory(TARDIS plugin, Adaption adapt, ChameleonPreset preset, String model) {
        this.plugin = plugin;
        this.adapt = adapt;
        this.preset = preset;
        this.model = model;
        on = ItemStack.of(Material.LIME_WOOL, 1);
        off = ItemStack.of(Material.LIGHT_GRAY_CARPET, 1);
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Chameleon Circuit", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        // Apply now
        ItemStack apply = ItemStack.of(GUIChameleon.BUTTON_APPLY.material(), 1);
        ItemMeta now = apply.getItemMeta();
        now.displayName(Component.text(plugin.getChameleonGuis().getString("APPLY")));
        List<Component> nowLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("APPLY_LORE")) {
            nowLore.add(Component.text(s));
        }
        now.lore(nowLore);
        apply.setItemMeta(now);
        // Disabled
        ItemStack dis = ItemStack.of(GUIChameleon.BUTTON_CHAMELEON.material(), 1);
        ItemMeta abled = dis.getItemMeta();
        abled.displayName(Component.text("Chameleon Circuit"));
        List<Component> ioLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("DISABLED_LORE")) {
            ioLore.add(Component.text(s));
        }
        abled.lore(ioLore);
        dis.setItemMeta(abled);
        // Adaptive
        ItemStack adap = ItemStack.of(GUIChameleon.BUTTON_ADAPT.material(), 1);
        ItemMeta tive = adap.getItemMeta();
        tive.displayName(Component.text(plugin.getChameleonGuis().getString("ADAPT")));
        List<Component> tiveLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("ADAPT_LORE")) {
            tiveLore.add(Component.text(s));
        }
        tive.lore(tiveLore);
        adap.setItemMeta(tive);
        // Invisible
        ItemStack invis;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            invis = ItemStack.of(GUIChameleon.BUTTON_INVISIBLE.material(), 1);
            ItemMeta ible = invis.getItemMeta();
            ible.displayName(Component.text(plugin.getChameleonGuis().getString("INVISIBLE")));
            List<Component> ilore = new ArrayList<>();
            for (String s : plugin.getChameleonGuis().getStringList("INVISIBLE_LORE")) {
                ilore.add(Component.text(s));
            }
            if (plugin.getConfig().getBoolean("circuits.damage")) {
                ilore.add(Component.text(plugin.getLanguage().getString("INVISIBILITY_LORE_1")));
                ilore.add(Component.text(plugin.getLanguage().getString("INVISIBILITY_LORE_2")));
            }
            ible.lore(ilore);
            invis.setItemMeta(ible);
        } else {
            invis = null;
        }
        // Shorted out
        ItemStack shor = ItemStack.of(GUIChameleon.BUTTON_SHORT.material(), 1);
        ItemMeta tout = shor.getItemMeta();
        tout.displayName(Component.text(plugin.getChameleonGuis().getString("SHORT")));
        List<Component> toutLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("SHORT_LORE")) {
            toutLore.add(Component.text(s));
        }
        tout.lore(toutLore);
        shor.setItemMeta(tout);
        // construction GUI
        ItemStack cons = ItemStack.of(GUIChameleon.BUTTON_CONSTRUCT.material(), 1);
        ItemMeta truct = cons.getItemMeta();
        truct.displayName(Component.text(plugin.getChameleonGuis().getString("CONSTRUCT")));
        List<Component> tructLore = new ArrayList<>();
        for (String s : plugin.getChameleonGuis().getStringList("CONSTRUCT_LORE")) {
            tructLore.add(Component.text(s));
        }
        truct.lore(tructLore);
        cons.setItemMeta(truct);
        // lock current preset
        ItemStack lock = null;
        if (adapt.equals(Adaption.BIOME)) {
            lock = ItemStack.of(GUIChameleon.BUTTON_LOCK.material(), 1);
            ItemMeta circuit = lock.getItemMeta();
            circuit.displayName(Component.text(plugin.getChameleonGuis().getString("LOCK")));
            List<Component> circuitLore = new ArrayList<>();
            for (String s : plugin.getChameleonGuis().getStringList("LOCK_LORE")) {
                circuitLore.add(Component.text(s));
            }
            circuit.lore(circuitLore);
            lock.setItemMeta(circuit);
        }
        // Disabled radio button
        boolean isFactoryOff = preset.equals(ChameleonPreset.FACTORY) && adapt.equals(Adaption.OFF);
        ItemStack fac = isFactoryOff ? on.clone() : off.clone();
        ItemMeta tory = fac.getItemMeta();
        Component donoff = isFactoryOff ? Component.text(plugin.getLanguage().getString("DISABLED", "Disabled"), NamedTextColor.RED) : Component.text(plugin.getLanguage().getString("SET_ON", "ON"), NamedTextColor.GREEN);
        tory.displayName(donoff);
        fac.setItemMeta(tory);
        // Adaptive radio button
        ItemStack biome = (adapt.equals(Adaption.OFF)) ? off.clone() : on.clone();
        ItemMeta block = biome.getItemMeta();
        block.displayName(Component.text(adapt.toString(), adapt.getColour()));
        biome.setItemMeta(block);
        // Invisible radio button
        ItemStack not;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            not = (preset.equals(ChameleonPreset.INVISIBLE)) ? on.clone() : off.clone();
            ItemMeta blue = not.getItemMeta();
            Component ionoff = (preset.equals(ChameleonPreset.INVISIBLE))
                    ? Component.text(plugin.getLanguage().getString("SET_ON", "ON"), NamedTextColor.GREEN)
                    : Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"), NamedTextColor.RED);
            blue.displayName(ionoff);
            not.setItemMeta(blue);
        } else {
            not = null;
        }
        // Shorted out radio button
        boolean isNotFactoryInvisibleOrConstruct = !preset.equals(ChameleonPreset.INVISIBLE) && !preset.equals(ChameleonPreset.FACTORY) && !preset.equals(ChameleonPreset.CONSTRUCT);
        ItemStack pre = isNotFactoryInvisibleOrConstruct ? on.clone() : off.clone();
        ItemMeta set = pre.getItemMeta();
        Component shorted;
        if (isNotFactoryInvisibleOrConstruct) {
            shorted = Component.text((preset == ChameleonPreset.ITEM) ? model : preset.toString(), NamedTextColor.GREEN);
        } else {
            shorted = Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"), NamedTextColor.RED);
        }
        set.displayName(shorted);
        pre.setItemMeta(set);
        // Construct radio button
        ItemStack bui = (preset.equals(ChameleonPreset.CONSTRUCT)) ? on.clone() : off.clone();
        ItemMeta lder = bui.getItemMeta();
        Component conoff = (preset.equals(ChameleonPreset.CONSTRUCT)) ? Component.text(plugin.getLanguage().getString("SET_ON", "ON"), NamedTextColor.GREEN) : Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"), NamedTextColor.RED);
        lder.displayName(conoff);
        bui.setItemMeta(lder);
        // Cancel / close
        ItemStack close = ItemStack.of(GUIChameleon.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);

        return new ItemStack[]{
                apply, null, null, lock, null, null, null, null, null,
                null, null, dis, adap, invis, shor, cons, null, null,
                null, null, fac, biome, not, pre, bui, null, close
        };
    }
}
