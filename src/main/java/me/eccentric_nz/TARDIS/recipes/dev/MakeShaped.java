/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.recipes.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.MemorySection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class MakeShaped {

    private final TARDIS plugin;
    private final String newLine = System.lineSeparator();

    private final String JAVA = """
            package me.eccentric_nz.TARDIS.recipes.shaped;
            
            import me.eccentric_nz.TARDIS.TARDIS;
            import me.eccentric_nz.TARDIS.enumeration.Difficulty;
            import org.bukkit.Material;
            import org.bukkit.NamespacedKey;
            import org.bukkit.inventory.ItemStack;
            import org.bukkit.inventory.ShapedRecipe;
            import org.bukkit.inventory.meta.ItemMeta;

            import java.util.Arrays;
            
            /*
            %s
            */
                     
            public class %sRecipe {
            
                private final TARDIS plugin;
            
                public %sRecipe(TARDIS plugin) {
                    this.plugin = plugin;
                }
            
                public void addRecipes() {
                    ItemStack is = new ItemStack(Material.%s, %s);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("%s");
                    im.setCustomModelData(%s);
                    String lore = plugin.getRecipesConfig().getString("shaped.%s.lore");
                    if (!lore.isEmpty()) {
                        im.setLore(Arrays.asList(lore.split("~")));
                    }
                    is.setItemMeta(im);
                    NamespacedKey key = new NamespacedKey(plugin, "%s");
                    ShapedRecipe r = new ShapedRecipe(key, is);
                    if (plugin.getDifficulty() == Difficulty.HARD) {
                        r.shape("%s", "%s", "%s");
                        %s
                    } else {
                        r.shape("%s", "%s", "%s");
                        %s
                    }
                    plugin.getServer().addRecipe(r);
                    plugin.getFigura().getShapedRecipes().put("%s", r);
                }
            }
            """;

    public MakeShaped(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void saveShapedRecipes() {
        Set<String> shaped = plugin.getRecipesConfig().getConfigurationSection("shaped").getKeys(false);
        shaped.forEach(this::saveRecipeFile);
    }

    private void saveRecipeFile(String s) {
        /*
         * shape: A-A,BBB,CDC
         * ingredients: A: STONE B: GRASS_BLOCK C: BIRCH_PLANKS D: DIAMOND_BLOCK=Special Name
         * result: DIAMOND_SWORD
         * amount: 1
         * lore: "The vorpal blade~goes snicker-snack!"
         */
        String result = plugin.getRecipesConfig().getString("shaped." + s + ".result");
        String key = s.replace(" ", "_").toLowerCase(Locale.ENGLISH);
        // get shape
        try {
            String[] hardShapeTmp = plugin.getRecipesConfig().getString("shaped." + s + ".hard_shape").split(",");
            String[] hardShape = new String[3];
            hardShape[0] = "   ";
            hardShape[1] = "   ";
            hardShape[2] = "   ";
            for (int i = 0; i < hardShapeTmp.length; i++) {
                hardShape[i] = hardShapeTmp[i].replaceAll("-", " ");
            }
            String[] easyShapeTmp = plugin.getRecipesConfig().getString("shaped." + s + ".easy_shape").split(",");
            String[] easyShape = new String[3];
            easyShape[0] = "   ";
            easyShape[1] = "   ";
            easyShape[2] = "   ";
            for (int i = 0; i < easyShapeTmp.length; i++) {
                easyShape[i] = easyShapeTmp[i].replaceAll("-", " ");
            }
            Set<String> easyIngredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + s + ".easy_ingredients").getKeys(false);
            Set<String> hardIngredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + s + ".hard_ingredients").getKeys(false);
            StringBuilder easyBuilder = new StringBuilder();
            StringBuilder hardBuilder = new StringBuilder();
            easyIngredients.forEach((g) -> {
                String i = plugin.getRecipesConfig().getString("shaped." + s + ".easy_ingredients." + g);
                // r.setIngredient('W', Material.WARPED_BUTTON);
                easyBuilder.append("r.setIngredient('").append(g).append("', Material.").append(i).append(");").append(newLine).append("            ");
            });
            hardIngredients.forEach((g) -> {
                String i = plugin.getRecipesConfig().getString("shaped." + s + ".hard_ingredients." + g);
                // r.setIngredient('W', Material.WARPED_BUTTON);
                hardBuilder.append("r.setIngredient('").append(g).append("', Material.").append(i).append(");").append(newLine).append("            ");
            });
            StringBuilder configBuilder = new StringBuilder();
            for (String c : plugin.getRecipesConfig().getConfigurationSection("shaped." + s).getKeys(true)) {
                Object section = plugin.getRecipesConfig().get("shaped." + s + "." + c);
                if (!(section instanceof MemorySection)) {
                    configBuilder.append(c).append(":").append(plugin.getRecipesConfig().getString("shaped." + s + "." + c)).append(newLine);
                }
            }
            String className = s.replaceAll(" ", "").replaceAll("-", "").replace("3", "Three");
            int amount = plugin.getRecipesConfig().getInt("shaped." + s + ".amount");
            String cmd = RecipeItem.getByName(s).getCustomModelData() + "";
            String data = String.format(JAVA,
                    configBuilder,
                    className,
                    className,
                    result,
                    amount,
                    s,
                    cmd,
                    s,
                    key,
                    hardShape[0], hardShape[1], hardShape[2],
                    hardBuilder,
                    easyShape[0], easyShape[1], easyShape[2],
                    easyBuilder,
                    s
            );
            save(className + "Recipe", data);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, s + " recipe failed! Check the recipe config file!");
        }
    }

    private void save(String filename, String contents) {
        File javaDir = new File(plugin.getDataFolder() + File.separator + "java");
        if (!javaDir.exists()) {
            boolean result = javaDir.mkdir();
            if (result && javaDir.setWritable(true) && javaDir.setExecutable(true)) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Created java directory.");
            }
        }
        File file = new File(plugin.getDataFolder() + File.separator + "java" + File.separator + filename + ".java");
        // save to file
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                bw.write(contents);
            }
        } catch (IOException e) {
            plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
        }
    }
}
