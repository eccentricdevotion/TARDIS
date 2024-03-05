package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class PageBuilder {

    private final TARDIS plugin;

    public PageBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void save(String filename, String contents) {
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

    public String getLink(RecipeChoice choice) {
        String ingredient;
        String MINECRAFT = "[%s](https://minecraft.wiki/w/%s)";
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
                    String WIKI = "[%s](/recipes/%s/%s)";
                    link = String.format(WIKI, folder, dn, TARDISStringUtils.toLowercaseDashed(dn));
                }
            }
        }
        return link;
    }
}
