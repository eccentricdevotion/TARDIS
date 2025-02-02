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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public class CustomPageBuilder extends PageBuilder {

    private final TARDIS plugin;

    public CustomPageBuilder(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public boolean compile() {
        // custom display item blocks
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            if (tdi.getCraftMaterial() != null) {
                plugin.debug(tdi.toString());
                String data = formatCustom(tdi);
                if (!data.isEmpty()) {
                    save(TARDISStringUtils.toDashedLowercase(tdi.toString()), data);
                }
            }
        }
        return true;
    }

    private String formatCustom(TARDISDisplayItem tdi) {
        String item = TARDISStringUtils.capitalise(tdi.toString());
        String mat = TARDISStringUtils.toDashedLowercase(tdi.getCraftMaterial().toString());
        String cap = TARDISStringUtils.capitalise(tdi.getCraftMaterial().toString());
        String easyIngredients = "[" + cap + "](" + cap.replaceAll(" ", "_") + ")<br/>[Glass Pane](https://minecraft.wiki/w/Glass_Pane)";
        String hardIngredients = "[" + cap + "](" + cap.replaceAll(" ", "_") + ")<br/>[Glass ](https://minecraft.wiki/w/Glass)";
        String crafting = TARDISStringUtils.toDashedLowercase(tdi.toString());
        String easyTable = "['glass-pane','glass-pane','glass-pane','glass-pane','" + mat + "','glass-pane','glass-pane','glass-pane','glass-pane','" + crafting + "']";
        String hardTable = "['glass','glass','glass','glass','" + mat + "','glass','glass','glass','glass','" + crafting + "']";
        String PAGE = """
                ---
                layout: default
                title: %s
                ---

                import Recipe from "@site/src/components/Recipe";

                %s
                ===================

                A %s custom block.

                ## Crafting
                            
                `/trecipe %s`

                | Ingredients | Crafting recipe | Difficulty |
                | ----------- | --------------- | ---------- |
                | %s | <Recipe icons={%s} /> | easy |
                | %s | <Recipe icons={%s} /> | hard |
                                
                """;
        // itemName, itemName, itemName, List.of(spacedIngredientName, scoredIngredientName), List.of(scoredIngredientName...)
        return String.format(PAGE, item, item, item, crafting, easyIngredients, easyTable, hardIngredients, hardTable);
    }
}
