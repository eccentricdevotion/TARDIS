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
