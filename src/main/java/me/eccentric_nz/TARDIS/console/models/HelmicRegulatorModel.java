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
package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HelmicRegulatorModel {

    public void setState(ItemDisplay display, int state) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 7 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_7.getKey());
            case 6 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_6.getKey());
            case 5 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_5.getKey());
            case 4 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_4.getKey());
            case 3 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_3.getKey());
            case 2 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_2.getKey());
            case 1 -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_1.getKey());
            default -> im.setItemModel(ModelledControl.HELMIC_REGULATOR_0.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
