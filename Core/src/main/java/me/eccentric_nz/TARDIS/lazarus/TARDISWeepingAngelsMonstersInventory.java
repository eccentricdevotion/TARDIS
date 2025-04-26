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
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class TARDISWeepingAngelsMonstersInventory {

    private final ItemStack[] monsters;
    private final TARDIS plugin;

    public TARDISWeepingAngelsMonstersInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.monsters = getItemStack();
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        for (Monster monster : Monster.values()) {
            ItemStack mon = new ItemStack(monster.getMaterial(), 1);
            ItemMeta ster = mon.getItemMeta();
            ster.setDisplayName(monster.getName());
//            NamespacedKey model = null;
//            switch (monster) {
//                case CLOCKWORK_DROID -> model = DroidVariant.BUTTON_CLOCKWORK_DROID.getKey();
//                case CYBERMAN -> model = CybermanVariant.BUTTON_CYBERMAN.getKey();
//                case DALEK -> model = DalekVariant.BUTTON_DALEK.getKey();
//                case DALEK_SEC -> model = DalekSecVariant.BUTTON_DALEK_SEC.getKey();
//                case DAVROS -> model = DavrosVariant.BUTTON_DAVROS.getKey();
//                case EMPTY_CHILD -> model = EmptyChildVariant.BUTTON_EMPTY_CHILD.getKey();
//                case HATH -> model = HathVariant.BUTTON_HATH.getKey();
//                case HEADLESS_MONK -> model = MonkVariant.BUTTON_HEADLESS_MONK.getKey();
//                case ICE_WARRIOR -> model = IceWarriorVariant.BUTTON_ICE_WARRIOR.getKey();
//                case JUDOON -> model = JudoonVariant.BUTTON_JUDOON.getKey();
//                case K9 -> model = K9Variant.BUTTON_K9.getKey();
//                case MIRE -> model = MireVariant.BUTTON_THE_MIRE.getKey();
//                case OOD -> model = OodVariant.BUTTON_OOD.getKey();
//                case OSSIFIED -> model = OssifiedVariant.BUTTON_OSSIFIED.getKey();
//                case RACNOSS -> model = RacnossVariant.BUTTON_RACNOSS.getKey();
//                case SCARECROW -> model = ScarecrowVariant.BUTTON_SCARECROW.getKey();
//                case SEA_DEVIL -> model = SeaDevilVariant.BUTTON_SEA_DEVIL.getKey();
//                case SILENT -> model = SilentVariant.BUTTON_SILENT.getKey();
//                case SILURIAN -> model = SilurianVariant.BUTTON_SILURIAN.getKey();
//                case SLITHEEN -> model = SlitheenVariant.BUTTON_SLITHEEN.getKey();
//                case SONTARAN -> model = SontaranVariant.BUTTON_SONTARAN.getKey();
//                case STRAX -> model = StraxVariant.BUTTON_STRAX.getKey();
//                case SYCORAX -> model = SycoraxVariant.BUTTON_SYCORAX.getKey();
//                case TOCLAFANE -> model = ToclafaneVariant.BUTTON_TOCLAFANE.getKey();
//                case VASHTA_NERADA -> model = VashtaNeradaVariant.BUTTON_VASHTA_NERADA.getKey();
//                case WEEPING_ANGEL -> model = WeepingAngelVariant.BUTTON_WEEPING_ANGEL.getKey();
//                case ZYGON -> model = ZygonVariant.BUTTON_ZYGON.getKey();
//            }
            mon.setItemMeta(ster);
            stacks[i] = mon;
            i++;
        }
        // page one
        ItemStack page1 = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page1.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        page1.setItemMeta(one);
        stacks[42] = page1;
        // page two
        ItemStack page2 = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page2.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        page2.setItemMeta(two);
        stacks[43] = page2;
        // add skins
        ItemStack down = new ItemStack(GUIGeneticManipulator.BUTTON_SKINS.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("TARDIS Television");
        down.setItemMeta(load);
        stacks[44] = down;
        // master
        ItemStack the = new ItemStack(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(List.of(plugin.getLanguage().getString("SET_OFF")));
        CustomModelDataComponent component = master.getCustomModelDataComponent();
        component.setFloats(List.of(152f));
        master.setCustomModelDataComponent(component);
        the.setItemMeta(master);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = the;
        // add buttons
        ItemStack rem = new ItemStack(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = new ItemStack(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = new ItemStack(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;

        return stacks;
    }

    public ItemStack[] getMonsters() {
        return monsters;
    }
}
