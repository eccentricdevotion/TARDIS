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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus. The machine would turn anyone inside
 * decades younger, but the process contained one side effect: genes that evolution rejected and left dormant would be
 * unlocked, transforming the human into a giant skeletal scorpion-like beast that fed off the lifeforce of living
 * creatures.
 *
 * @author eccentric_nz
 */
class TARDISLazarusPageTwoInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final List<Material> disguises = new ArrayList<>();
    private final Inventory inventory;

    TARDISLazarusPageTwoInventory(TARDIS plugin) {
        this.plugin = plugin;
        disguises.add(Material.SPIDER_SPAWN_EGG);
        disguises.add(Material.TRADER_LLAMA_SPAWN_EGG);
        disguises.add(Material.WANDERING_TRADER_SPAWN_EGG);
        disguises.add(Material.WOLF_SPAWN_EGG);
        disguises.add(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
        // hostile
        disguises.add(Material.BLAZE_SPAWN_EGG);
        disguises.add(Material.BOGGED_SPAWN_EGG);
        disguises.add(Material.BREEZE_SPAWN_EGG);
        disguises.add(Material.CREAKING_SPAWN_EGG);
        disguises.add(Material.CREEPER_SPAWN_EGG);
        disguises.add(Material.DROWNED_SPAWN_EGG);
        disguises.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
        disguises.add(Material.ENDERMITE_SPAWN_EGG);
        disguises.add(Material.EVOKER_SPAWN_EGG);
        disguises.add(Material.GHAST_SPAWN_EGG);
        disguises.add(Material.GUARDIAN_SPAWN_EGG);
        disguises.add(Material.HOGLIN_SPAWN_EGG);
        disguises.add(Material.HUSK_SPAWN_EGG);
        disguises.add(Material.MAGMA_CUBE_SPAWN_EGG);
        disguises.add(Material.PHANTOM_SPAWN_EGG);
        disguises.add(Material.PIGLIN_BRUTE_SPAWN_EGG);
        disguises.add(Material.PILLAGER_SPAWN_EGG);
        disguises.add(Material.RAVAGER_SPAWN_EGG);
        disguises.add(Material.SHULKER_SPAWN_EGG);
        disguises.add(Material.SILVERFISH_SPAWN_EGG);
        disguises.add(Material.SKELETON_SPAWN_EGG);
        disguises.add(Material.SLIME_SPAWN_EGG);
        disguises.add(Material.STRAY_SPAWN_EGG);
        disguises.add(Material.VEX_SPAWN_EGG);
        disguises.add(Material.VINDICATOR_SPAWN_EGG);
        disguises.add(Material.WARDEN_SPAWN_EGG);
        disguises.add(Material.WITCH_SPAWN_EGG);
        disguises.add(Material.WITHER_SKELETON_SPAWN_EGG);
        disguises.add(Material.ZOGLIN_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        // boss
        disguises.add(Material.ENDER_DRAGON_SPAWN_EGG);
        disguises.add(Material.WITHER_SPAWN_EGG);
        // unused
        disguises.add(Material.ZOMBIE_HORSE_SPAWN_EGG);

        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Genetic Manipulator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // hostile & boss & unused
        for (Material m : disguises) {
            ItemStack egg = ItemStack.of(m, 1);
            ItemMeta me = egg.getItemMeta();
            me.displayName(Component.text(m.toString().replace("_SPAWN_EGG", "")));
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // put illusioner
        ItemStack ill = ItemStack.of(Material.BOW, 1);
        ItemMeta usi = ill.getItemMeta();
        usi.displayName(Component.text("ILLUSIONER"));
        ill.setItemMeta(usi);
        stacks[i] = ill;
        i++;
        // put herobrine
        ItemStack hero = ItemStack.of(Material.PLAYER_HEAD, 1);
        ItemMeta brine = hero.getItemMeta();
        brine.displayName(Component.text("HEROBRINE"));
        hero.setItemMeta(brine);
        stacks[i] = hero;
        // page one
        ItemStack page = ItemStack.of(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page.getItemMeta();
        one.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_1", "Go to page 1")));
        page.setItemMeta(one);
        stacks[42] = page;
        // add skins
        ItemStack down = ItemStack.of(GUIGeneticManipulator.BUTTON_SKINS.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.displayName(Component.text("TARDIS Television"));
        down.setItemMeta(load);
        stacks[GUIGeneticManipulator.BUTTON_SKINS.slot()] = down;
        // TARDISWeepingAngels monsters
        ItemStack weep = ItemStack.of(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        ItemMeta ing = weep.getItemMeta();
        ing.displayName(Component.text("TARDIS Monsters"));
        weep.setItemMeta(ing);
        stacks[GUIGeneticManipulator.BUTTON_TWA.slot()] = weep;
        // add options
        ItemStack the = ItemStack.of(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta master = the.getItemMeta();
        master.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MASTER", "The Master's reverse polarity button")));
        master.lore(List.of(Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"))));
        CustomModelDataComponent component = master.getCustomModelDataComponent();
        component.setFloats(List.of(152f));
        master.setCustomModelDataComponent(component);
        the.setItemMeta(master);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = the;
        // adult
        ItemStack adult = ItemStack.of(GUIGeneticManipulator.BUTTON_AGE.material(), 1);
        ItemMeta baby = adult.getItemMeta();
        baby.displayName(Component.text(plugin.getLanguage().getString("BUTTON_AGE", "Age")));
        baby.lore(List.of(Component.text("ADULT")));
        adult.setItemMeta(baby);
        stacks[GUIGeneticManipulator.BUTTON_AGE.slot()] = adult;
        // type
        ItemStack typ = ItemStack.of(GUIGeneticManipulator.BUTTON_TYPE.material(), 1);
        ItemMeta col = typ.getItemMeta();
        col.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TYPE", "Type/Colour")));
        col.lore(List.of(Component.text("WHITE")));
        typ.setItemMeta(col);
        stacks[GUIGeneticManipulator.BUTTON_TYPE.slot()] = typ;
        // tamed etc
        ItemStack tamed = ItemStack.of(GUIGeneticManipulator.BUTTON_OPTS.material(), 1);
        ItemMeta tf = tamed.getItemMeta();
        tf.displayName(Component.text(plugin.getLanguage().getString("BUTTON_OPTS", "Disguise Options")));
        List<Component> opts = new ArrayList<>();
        for (String o : plugin.getLanguage().getString("BUTTON_OPTS_LIST").split("/")) {
            opts.add(Component.text(o).decorate(TextDecoration.ITALIC));
        }
        opts.add(Component.text("FALSE", NamedTextColor.RED));
        tf.lore(opts);
        tamed.setItemMeta(tf);
        stacks[GUIGeneticManipulator.BUTTON_OPTS.slot()] = tamed;
        // add buttons
        ItemStack rem = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = ItemStack.of(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DNA", "Modify my genetic material")));
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;

        return stacks;
    }
}
