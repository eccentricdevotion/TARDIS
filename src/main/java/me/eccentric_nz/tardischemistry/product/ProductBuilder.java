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
package me.eccentric_nz.tardischemistry.product;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ProductBuilder {

    public static ItemStack getProduct(Product product) {
        ItemStack is = ItemStack.of(product.getMaterial(), 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(product.getName()));
        is.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes()
                .addModifier(
                Attribute.LUCK,
                new AttributeModifier(
                        product.getModel(),
                        0.0d,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.ANY
                ))
                .build()
        );
        is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .hideTooltip(true)
                .build());
        is.setData(DataComponentTypes.ITEM_MODEL, product.getModel());
        is.editPersistentDataContainer(pdc->pdc.set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, product.getModel().getKey()));
        return is;
    }
}
