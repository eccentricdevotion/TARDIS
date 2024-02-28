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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
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
public class MakeSmithing {

    private final TARDIS plugin;
    private final String newLine = System.lineSeparator();

    private final String JAVA = """
            package me.eccentric_nz.TARDIS.recipes.smithing;
                        
            import me.eccentric_nz.TARDIS.TARDIS;
            import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
            import org.bukkit.Material;
            import org.bukkit.NamespacedKey;
            import org.bukkit.inventory.ItemStack;
            import org.bukkit.inventory.RecipeChoice;
            import org.bukkit.inventory.SmithingRecipe;
            import org.bukkit.inventory.SmithingTransformRecipe;
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
                    // result
                    ItemStack result = new ItemStack(Material.%s, 1);
                    // template
                    RecipeChoice template = new RecipeChoice.MaterialChoice(Material.REDSTONE);
                    // base material to upgrade
                    RecipeChoice base = new RecipeChoice.MaterialChoice(Material.%s);
                    // addition
                    ItemStack isa = new ItemStack(Material.%s, 1);
                    ItemMeta ima = isa.getItemMeta();
                    ima.setDisplayName("%s");
                    ima.setCustomModelData(RecipeItem.%s.getCustomModelData());
                    isa.setItemMeta(ima);
                    RecipeChoice addition = new RecipeChoice.ExactChoice(isa);
                    NamespacedKey key = new NamespacedKey(plugin, "%s");
                    SmithingRecipe r = new SmithingTransformRecipe(key, result, template, base, addition);
                    plugin.getServer().addRecipe(r);
                }
            }
            """;


    public MakeSmithing(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void saveSmithingRecipes() {
        Set<String> smithing = plugin.getRecipesConfig().getConfigurationSection("smithing").getKeys(false);
        smithing.forEach(this::saveRecipeFile);
    }

    private void saveRecipeFile(String s) {
        /*
        smithing:
          Admin Repair:
            base: BLAZE_ROD
            addition: GLOWSTONE_DUST=Server Admin Circuit
            result: BLAZE_ROD
         */
        String result = plugin.getRecipesConfig().getString("smithing." + s + ".result");
        String key = s.replace(" ", "_").toLowerCase(Locale.ENGLISH);
        try {
            // get base
            String base = plugin.getRecipesConfig().getString("smithing." + s + ".base");
            // get addition
            String[] additionData = plugin.getRecipesConfig().getString("smithing." + s + ".addition").split("=");
            String addition = additionData[0];
            // addition display name
            String dn = additionData[1];
            // addition CMD name
            String cmd = TARDISStringUtils.toEnumUppercase(dn);
            // config
            StringBuilder configBuilder = new StringBuilder();
            for (String c : plugin.getRecipesConfig().getConfigurationSection("smithing." + s).getKeys(true)) {
                Object section = plugin.getRecipesConfig().get("smithing." + s + "." + c);
                if (!(section instanceof MemorySection)) {
                    configBuilder.append(c).append(":").append(plugin.getRecipesConfig().getString("smithing." + s + "." + c)).append(newLine);
                }
            }
            String className = s.replaceAll(" ", "").replaceAll("-", "");
            String data = String.format(JAVA, configBuilder, className, className, result, base, addition, dn, cmd, key);
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
