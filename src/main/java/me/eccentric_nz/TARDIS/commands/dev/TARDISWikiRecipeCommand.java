package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TARDISWikiRecipeCommand {

    private final String MINECRAFT = "[%s](https://minecraft.wiki/w/%s)";
    private final String WIKI = "[%s](/recipes/%s/%s)";
    private final String newLine = System.lineSeparator();
    private final String PAGE = """
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

            | Ingredients | Crafting recipe | Difficulty |
            | ----------- | --------------- | ------------- |
            | %s | <Recipe icons={%s} /> | easy |
            | %s | <Recipe icons={%s} /> | hard |

            """;
    private final TARDIS plugin;

    public TARDISWikiRecipeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean write(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        if (args[1].equalsIgnoreCase("chest")) {
            if (!(sender instanceof Player player)) {
                return true;
            }
            // fill chests with every TARDIS item
            Location location = player.getLocation().add(0, 2, 0);
            int shapedChests = (plugin.getFigura().getShapedRecipes().size() / 27) + 1;
            int shlessChests = (plugin.getIncomposita().getShapelessRecipes().size() / 27) + 1;
            // place some chests
            for (int i = 0; i < shapedChests; i++) {
                location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
            }
            for (int i = 1; i <= shlessChests; i++) {
                location.getBlock().getRelative(BlockFace.WEST, i).setType(Material.CHEST);
            }
            int count = 0;
            int chestNum = 0;
            Chest chest = (Chest) location.getBlock().getState();
            ItemStack is;
            for (ShapedRecipe s : plugin.getFigura().getShapedRecipes().values()) {
                if (count == 27) {
                    // get next chest
                    chestNum++;
                    count = 0;
                    chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
                }
                is = s.getResult();
                is.setAmount(1);
                if (is.getItemMeta() instanceof Damageable damageable) {
                    damageable.setDamage(0);
                    damageable.setUnbreakable(true);
                    damageable.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                    is.setItemMeta(damageable);
                }
                chest.getBlockInventory().addItem(is);
                count++;
            }
            count = 0;
            chestNum = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.WEST).getState();
            for (ShapelessRecipe s : plugin.getIncomposita().getShapelessRecipes().values()) {
                if (count == 27) {
                    // get next chest
                    chestNum++;
                    count = 0;
                    chest = (Chest) location.getBlock().getRelative(BlockFace.WEST, chestNum).getState();
                }
                is = s.getResult();
                is.setAmount(1);
                if (is.getItemMeta() instanceof Damageable damageable) {
                    damageable.setDamage(0);
                    damageable.setUnbreakable(true);
                    damageable.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                    is.setItemMeta(damageable);
                }
                chest.getBlockInventory().addItem(is);
                count++;
            }
            return true;
        }
        String data;
        if (args[1].equalsIgnoreCase("shaped")) {
            // all shaped recipes
            for (String key : plugin.getFigura().getShapedRecipes().keySet()) {
                plugin.debug(key);
                data = format("shaped", key);
                if (!data.isEmpty()) {
                    save(TARDISStringUtils.toDashedLowercase(key), data);
                }
            }
        } else if (args[1].equalsIgnoreCase("shapeless")) {
            // all shapeless recipes
            for (String key : plugin.getIncomposita().getShapelessRecipes().keySet()) {
                plugin.debug(key);
                data = format("shapeless", key);
                if (!data.isEmpty()) {
                    save(TARDISStringUtils.toDashedLowercase(key), data);
                }
            }
        } else {
            // concat the args
            StringBuilder sb = new StringBuilder();
            String prefix = "";
            for (int i = 1; i < args.length; i++) {
                sb.append(prefix);
                prefix = " ";
                sb.append(args[i]);
            }
            String item = sb.toString();
            // specific recipe
            if (plugin.getFigura().getShapedRecipes().containsKey(item)) {
                data = format("shaped", item);
            } else {
                data = format("shapeless", item);
            }
            if (!data.isEmpty()) {
                save(TARDISStringUtils.toDashedLowercase(item), data);
            }
        }
        return true;
    }

    private String format(String section, String item) {
        String crafting = TARDISStringUtils.toLowercaseDashed(item);
        // get ingredients
        String easyIngredients;
        String hardIngredients;
        // get recipe arrays
        String easyTable;
        String hardTable;
        if (section.equals("shaped")) {
            easyIngredients = getIngredients(item, "easy", true);
            hardIngredients = getIngredients(item, "hard", true);
            easyTable = getShapedTable(item, "easy");
            hardTable = getShapedTable(item, "hard");
        } else {
            easyIngredients = getIngredients(item, "easy", false);
            hardIngredients = "";
            easyTable = getShapelessTable(item);
            hardTable = "";
        }
        String desc = String.format("The %s is used to ", item);
        try {
            String info = TARDISStringUtils.toEnumUppercase(item) + "_INFO";
            TARDISDescription description = TARDISDescription.valueOf(info);
            desc = description.getDesc();
        } catch (IllegalArgumentException ignored) {
        }
        // itemName, itemName, itemName, List.of(spacedIngredientName, scoredIngredientName), List.of(scoredIngredientName...) -> x2
        return String.format(PAGE, item, item, desc, crafting, easyIngredients, easyTable, hardIngredients, hardTable);
    }

    private void save(String filename, String contents) {
        File wikiDir = new File(plugin.getDataFolder() + File.separator + "wiki");
        if (!wikiDir.exists()) {
            boolean result = wikiDir.mkdir();
            if (result && wikiDir.setWritable(true) && wikiDir.setExecutable(true)) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Created wiki directory.");
            }
        }
        File file = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + TARDISStringUtils.toLowercaseDashed(filename) + ".mdx");
        // save to file
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                bw.write(contents);
            }
        } catch (IOException e) {
            plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
        }
    }

    private String getIngredients(String item, String difficulty, boolean shaped) {
        StringBuilder ingredientBuilder = new StringBuilder();
        String prefix = "";
        if (shaped) {
            for (Map.Entry<Character, RecipeChoice> map : plugin.getFigura().getShapedRecipes().get(item).getChoiceMap().entrySet()) {
                RecipeChoice choice = map.getValue();
                String link = getLink(choice, item);
                ingredientBuilder.append(prefix);
                prefix = "<br/>";
                ingredientBuilder.append(link);
            }
        } else {
            for (RecipeChoice choice : plugin.getIncomposita().getShapelessRecipes().get(item).getChoiceList()) {
                String link = getLink(choice, item);
                ingredientBuilder.append(prefix);
                prefix = "<br/>";
                ingredientBuilder.append(link);
            }
        }
        return ingredientBuilder.toString();
    }

    private String getLink(RecipeChoice choice, String item) {
        String ingredient;
        String link = MINECRAFT;
        if (choice instanceof RecipeChoice.MaterialChoice mat) {
            ingredient = TARDISStringUtils.capitalise(mat.getChoices().get(0).toString());
            // MINECRAFT = "[%s](https://minecraft.wiki/w/%s)";
            link = String.format(MINECRAFT, ingredient, ingredient.replaceAll(" ", "_"));
        }
        if (choice instanceof RecipeChoice.ExactChoice exact) {
            ItemStack is = exact.getItemStack();
            switch (is.getType()) {
                case POTION -> {
                    PotionMeta pm = (PotionMeta) is.getItemMeta();
                    String potion = pm.getBasePotionType().name();
                    ingredient = "Potion of " + TARDISStringUtils.capitalise(potion);
                    link = String.format(MINECRAFT, ingredient, ingredient.replaceAll(" ", "_"));
                }
                case ENCHANTED_BOOK -> {
                    BookMeta bm = (BookMeta) is.getItemMeta();
                    String enchant = bm.getEnchants().keySet().stream().findFirst().toString();
                    String cap = TARDISStringUtils.capitalise(enchant);
                    ingredient = "Enchanted Book of " + cap;
                    link = String.format(MINECRAFT, ingredient, ingredient.replaceAll(" ", "_"));
                }
                default -> {
                    ItemMeta im = is.getItemMeta();
                    String dn = im.getDisplayName();
                    RecipeItem recipeItem = RecipeItem.getByName(dn);
                    String folder = recipeItem.getCategory().toString().toLowerCase(Locale.ROOT);
                    link = String.format(WIKI, folder, dn, TARDISStringUtils.toLowercaseDashed(dn));
                }
            }
        }
        return link;
    }

    private String getShapelessTable(String item) {
        StringBuilder tableBuilder = new StringBuilder("[");
        // get ingredient
        String dashed = "";
        List<RecipeChoice> shapeless = plugin.getIncomposita().getShapelessRecipes().get(item).getChoiceList();
        for (RecipeChoice choice : shapeless) {
            if (choice instanceof RecipeChoice.MaterialChoice mat) {
                dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(mat.getChoices().get(0).toString()));
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

    private String getShapedTable(String item, String difficulty) {
        StringBuilder tableBuilder = new StringBuilder("[");
        ShapedRecipe shapedRecipe = plugin.getFigura().getShapedRecipes().get(item);
        Map<Character, RecipeChoice> choices = shapedRecipe.getChoiceMap();
        String[] shape = shapedRecipe.getShape();
        for (String row : shape) {
            for (int i = 0; i < 3; i++) {
                Character c = row.charAt(i);
                switch (c) {
                    case ',' -> {
                        // do nothing
                    }
                    case '-' -> tableBuilder.append("'air',");
                    default -> {
                        // get ingredient
                        String dashed = "";
                        RecipeChoice choice = choices.get(c);
                        if (choice instanceof RecipeChoice.MaterialChoice mat) {
                            dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(mat.getChoices().get(0).toString()));
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
                }
            }
        }
        // get result
        String result = TARDISStringUtils.toLowercaseDashed(item);
        tableBuilder.append("'").append(result).append("'").append("]");
        return tableBuilder.toString();
    }
}
