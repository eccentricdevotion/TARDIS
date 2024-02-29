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
public class MakeShapeless {

    private final TARDIS plugin;
    private final String newLine = System.lineSeparator();

    private final String JAVA = """
            package me.eccentric_nz.TARDIS.recipes.shapeless;
                        
            import me.eccentric_nz.TARDIS.TARDIS;
            import me.eccentric_nz.TARDIS.enumeration.Difficulty;
            import org.bukkit.Material;
            import org.bukkit.NamespacedKey;
            import org.bukkit.inventory.ItemStack;
            import org.bukkit.inventory.ShapelessRecipe;
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
                    String lore = plugin.getRecipesConfig().getString("shapeless.%s.lore");
                    if (!lore.isEmpty()) {
                        im.setLore(Arrays.asList(lore.split("~")));
                    }
                    is.setItemMeta(im);
                    NamespacedKey key = new NamespacedKey(plugin, "%s");
                    ShapelessRecipe r = new ShapelessRecipe(key, is);
                    %s
                    plugin.getServer().addRecipe(r);
                    plugin.getIncomposita().getShapelessRecipes().put("%s", r);
                }
            }
            """;


    public MakeShapeless(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void saveShapelessRecipes() {
        Set<String> shapeless = plugin.getRecipesConfig().getConfigurationSection("shapeless").getKeys(false);
        shapeless.forEach(this::saveRecipeFile);
    }

    private void saveRecipeFile(String s) {
        /*
        shapeless:
          TARDIS Schematic Wand:
            recipe: BONE,REDSTONE
            result: BONE
            amount: 1
            lore: Right-click start~Left-click end
         */
        String result = plugin.getRecipesConfig().getString("shapeless." + s + ".result");
        String key = s.replace(" ", "_").toLowerCase(Locale.ENGLISH);
        // get shape
        try {
            String[] ingredients = plugin.getRecipesConfig().getString("shapeless." + s + ".recipe").split(",");
            StringBuilder builder = new StringBuilder();
            for (String i : ingredients) {
                // r.setIngredient('W', Material.WARPED_BUTTON);
                builder.append("r.addIngredient(Material.").append(i).append(");").append(newLine).append("            ");
            }
            StringBuilder configBuilder = new StringBuilder();
            for (String c : plugin.getRecipesConfig().getConfigurationSection("shapeless." + s).getKeys(true)) {
                Object section = plugin.getRecipesConfig().get("shapeless." + s + "." + c);
                if (!(section instanceof MemorySection)) {
                    configBuilder.append(c).append(":").append(plugin.getRecipesConfig().getString("shapeless." + s + "." + c)).append(newLine);
                }
            }
            String className = s.replaceAll(" ", "").replaceAll("-", "");
            int amount = plugin.getRecipesConfig().getInt("shapeless." + s + ".amount");
            String cmd = RecipeItem.getByName(s).getCustomModelData() + "";
            String data = String.format(JAVA, configBuilder, className, className, result, amount, s, cmd, s, key, builder, s);
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
