package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;

public class ShapelessPageBuilder extends PageBuilder {

    private final TARDIS plugin;

    public ShapelessPageBuilder(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public boolean compile() {
        // all shapeless recipes
        for (String key : plugin.getIncomposita().getShapelessRecipes().keySet()) {
            plugin.debug(key);
            String data = formatShapeless(key);
            if (!data.isEmpty()) {
                save(TARDISStringUtils.toDashedLowercase(key), data);
            }
        }
        return true;
    }

    private String formatShapeless(String item) {
        String crafting = TARDISStringUtils.toLowercaseDashed(item);
        // get ingredients
        String easyIngredients;
        // get recipe arrays
        String easyTable;
        easyIngredients = getIngredients(item);
        easyTable = getShapelessTable(item);
        String desc = String.format("The %s is used to ", item);
        try {
            String info = TARDISStringUtils.toEnumUppercase(item) + "_INFO";
            TARDISDescription description = TARDISDescription.valueOf(info);
            desc = description.getDesc();
        } catch (IllegalArgumentException ignored) {
        }
        String SHAPELESS = """
                ---
                layout: default
                title: %s
                ---

                import Recipe from "@site/src/components/Recipe";

                %s
                ===================

                %s

                ## Crafting
                            
                `/trecipe %s`

                | Ingredients | Crafting recipe |
                | ----------- | --------------- |
                | %s | <Recipe icons={%s} /> |
                """;
        // itemName, itemName, description, recipeCommand, List.of(spacedIngredientName, scoredIngredientName), List.of(scoredIngredientName...)
        return String.format(SHAPELESS, item, item, desc, crafting, easyIngredients, easyTable);
    }

    private String getIngredients(String item) {
        StringBuilder ingredientBuilder = new StringBuilder();
        String prefix = "";
        for (RecipeChoice choice : plugin.getIncomposita().getShapelessRecipes().get(item).getChoiceList()) {
            String link = getLink(choice);
            ingredientBuilder.append(prefix);
            prefix = "<br/>";
            ingredientBuilder.append(link);
        }
        return ingredientBuilder.toString();
    }

    private String getShapelessTable(String item) {
        StringBuilder tableBuilder = new StringBuilder("[");
        // get ingredient
        String dashed = "";
        List<RecipeChoice> shapeless = plugin.getIncomposita().getShapelessRecipes().get(item).getChoiceList();
        for (RecipeChoice choice : shapeless) {
            if (choice instanceof RecipeChoice.MaterialChoice mat) {
                dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(mat.getChoices().getFirst().toString()));
            }
            if (choice instanceof RecipeChoice.ExactChoice exact) {
                ItemStack is = exact.getItemStack();
                switch (is.getType()) {
                    case POTION -> {
                        PotionMeta pm = (PotionMeta) is.getItemMeta();
                        String potion = pm.getBasePotionType().name();
                        dashed = TARDISStringUtils.toLowercaseDashed("Potion of " + TARDISStringUtils.capitalise(potion));
                    }
                    case ENCHANTED_BOOK -> {
                        BookMeta bm = (BookMeta) is.getItemMeta();
                        String enchant = bm.getEnchants().keySet().stream().findFirst().toString();
                        String cap = TARDISStringUtils.capitalise(enchant);
                        dashed = TARDISStringUtils.toLowercaseDashed("Enchanted Book of " + cap);
                    }
                    default -> {
                        ItemMeta im = is.getItemMeta();
                        dashed = TARDISStringUtils.toLowercaseDashed(im.getDisplayName());
                    }
                }
            }
            tableBuilder.append("'").append(dashed).append("'").append(",");
        }
        for (int j = shapeless.size(); j < 9; j++) {
            tableBuilder.append("'air',");
        }
        // get result
        String result = TARDISStringUtils.toLowercaseDashed(item);
        tableBuilder.append("'").append(result).append("'").append("]");
        return tableBuilder.toString();
    }
}
