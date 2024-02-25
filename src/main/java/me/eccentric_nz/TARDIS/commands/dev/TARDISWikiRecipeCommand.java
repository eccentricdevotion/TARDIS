package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class TARDISWikiRecipeCommand {

    private final String MINECRAFT = "[%s](https://minecraft.wiki/w/%s)";
    private final String WIKI = "[%s](/items/%s)";
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
            Set<String> shaped = plugin.getRecipesConfig().getConfigurationSection("shaped").getKeys(false);
            Set<String> shless = plugin.getRecipesConfig().getConfigurationSection("shapeless").getKeys(false);
            Location location = player.getLocation().add(0, 2, 0);
            int shapedChests = (shaped.size() / 27) + 1;
            int shlessChests = (shless.size() / 27) + 1;
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
            for (String s : shaped) {
                if (count == 27) {
                    // get next chest
                    chestNum++;
                    count = 0;
                    chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
                }
                is = plugin.getFigura().getShapedRecipes().get(s).getResult();
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
            for (String s : shless) {
                if (count == 27) {
                    // get next chest
                    chestNum++;
                    count = 0;
                    chest = (Chest) location.getBlock().getRelative(BlockFace.WEST, chestNum).getState();
                }
                is = plugin.getIncomposita().getShapelessRecipes().get(s).getResult();
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
            for (String key : plugin.getRecipesConfig().getConfigurationSection("shaped").getKeys(false)) {
                plugin.debug(key);
                data = format("shaped", key);
                if (!data.isEmpty()) {
                    save(TARDISStringUtils.toDashedLowercase(key), data);
                }
            }
        } else if (args[1].equalsIgnoreCase("shapeless")) {
            // all shapeless recipes
            for (String key : plugin.getRecipesConfig().getConfigurationSection("shapeless").getKeys(false)) {
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
            if (plugin.getRecipesConfig().contains("shaped." + item)) {
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
        if (plugin.getRecipesConfig().contains(section + "." + item)) {
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
            return String.format(PAGE, item, item, desc, easyIngredients, easyTable, hardIngredients, hardTable);
        }
        return "";
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
            plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
        }
    }

    private String getIngredients(String item, String difficulty, boolean shaped) {
        StringBuilder ingredientBuilder = new StringBuilder();
        Set<String> ingredients;
        if (shaped) {
            ingredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + item + "." + difficulty + "_ingredients").getKeys(false);
        } else {
            ingredients = Set.of(plugin.getRecipesConfig().getString("shapeless." + item + ".recipe").split(","));
        }
        String prefix = "";
        for (String g : ingredients) {
            String i;
            if (shaped) {
                i = plugin.getRecipesConfig().getString("shaped." + item + "." + difficulty + "_ingredients." + g);
            } else {
                i = g;
            }
            String ingredient;
            String link;
            if (i.contains("=")) {
                String[] choice = i.split("=");
                ingredient = choice[1];
                link = String.format(WIKI, ingredient, TARDISStringUtils.toLowercaseDashed(ingredient)) + newLine;
            } else if (i.contains(">")) {
                String[] choice = i.split(">");
                ingredient = "Potion of " + TARDISStringUtils.capitalise(choice[1]);
                link = String.format(MINECRAFT, ingredient, TARDISStringUtils.toLowercaseDashed(ingredient));
            } else if (i.contains("≈")) {
                String[] choice = i.split("≈");
                String cap = TARDISStringUtils.capitalise(choice[1]);
                ingredient = "Enchanted Book of " + cap;
                link = String.format(MINECRAFT, ingredient, cap.replaceAll(" ", "_"));
            } else {
                ingredient = TARDISStringUtils.capitalise(i);
                link = String.format(MINECRAFT, ingredient, ingredient.replaceAll(" ", "_"));
            }
            ingredientBuilder.append(prefix);
            prefix = "<br/>";
            ingredientBuilder.append(link);
        }
        return ingredientBuilder.toString();
    }

    private String getShapelessTable(String item) {
        StringBuilder tableBuilder = new StringBuilder("[");
        // get ingredient
        String dashed;
        String r = plugin.getRecipesConfig().getString("shapeless." + item + ".recipe");
        String[] items = r.split(",");
        for (String i : items) {
            if (i.contains("=")) {
                String[] choice = i.split("=");
                dashed = TARDISStringUtils.toLowercaseDashed(choice[1]);
            } else if (i.contains(">")) {
                String[] choice = i.split(">");
                dashed = TARDISStringUtils.toLowercaseDashed("Potion of " + TARDISStringUtils.capitalise(choice[1]));
            } else if (i.contains("≈")) {
                String[] choice = i.split("≈");
                dashed = TARDISStringUtils.toLowercaseDashed("Enchanted Book of " + TARDISStringUtils.capitalise(choice[1]));
            } else {
                dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(i));
            }
            tableBuilder.append("'").append(dashed).append("'").append(",");
        }
        for (int j = items.length; j < 9; j++) {
            tableBuilder.append("'air',");
        }
        // get result
        String result = TARDISStringUtils.toLowercaseDashed(item);
        tableBuilder.append("'").append(result).append("'").append("]");
        return tableBuilder.toString();
    }

    private String getShapedTable(String item, String difficulty) {
        StringBuilder tableBuilder = new StringBuilder("[");
        String shape = plugin.getRecipesConfig().getString("shaped." + item + "." + difficulty + "_shape");
        for (String c : shape.split("")) {
            switch (c) {
                case "," -> {
                    // do nothing
                }
                case "-" -> tableBuilder.append("'air',");
                default -> {
                    // get ingredient
                    String dashed;
                    String i = plugin.getRecipesConfig().getConfigurationSection("shaped." + item + "." + difficulty + "_ingredients").getString(c);
                    if (i.contains("=")) {
                        String[] choice = i.split("=");
                        dashed = TARDISStringUtils.toLowercaseDashed(choice[1]);
                    } else if (i.contains(">")) {
                        String[] choice = i.split(">");
                        dashed = TARDISStringUtils.toLowercaseDashed("Potion of " + TARDISStringUtils.capitalise(choice[1]));
                    } else if (i.contains("≈")) {
                        String[] choice = i.split("≈");
                        dashed = TARDISStringUtils.toLowercaseDashed("Enchanted Book of " + TARDISStringUtils.capitalise(choice[1]));
                    } else {
                        dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(i));
                    }
                    tableBuilder.append("'").append(dashed).append("'").append(",");
                }
            }
        }
        // get result
        String result = TARDISStringUtils.toLowercaseDashed(item);
        tableBuilder.append("'").append(result).append("'").append("]");
        return tableBuilder.toString();
    }
}
