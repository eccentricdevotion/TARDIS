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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

public class ItemsBuilder {

    private final TARDIS plugin;
    private final File wikiDir;
    private final String template = """
            {
                "model": {
                    "type": "minecraft:select",
                    "property": "minecraft:custom_model_data",
                    "index": 0,
                    "cases": [
                        {
                            "when": "%s",
                            "model": {
                                "type": "minecraft:model",
                                "model": "tardis:item/tardis/%s"
                            }
                        }
                    ]
                }
            }
            """;

    public ItemsBuilder(TARDIS plugin) {
        this.plugin = plugin;
        this.wikiDir = new File(plugin.getDataFolder() + File.separator + "wiki");
    }

    public void place(CommandSender sender) {
        if (!wikiDir.exists()) {
            boolean result = wikiDir.mkdir();
            if (result && wikiDir.setWritable(true) && wikiDir.setExecutable(true)) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Created wiki directory.");
            }
        }
        // make JSON files for all tardis recipe items
        for (ShapedRecipe s : plugin.getFigura().getShapedRecipes().values()) {
            save(s.getResult());
        }
        for (ShapelessRecipe s : plugin.getIncomposita().getShapelessRecipes().values()) {
            save(s.getResult());
        }
    }

    public void save(ItemStack is) {
        if (is.getType() != Material.GLOWSTONE_DUST) {
            return;
        }
        try {
            String name = is.getData(DataComponentTypes.CUSTOM_MODEL_DATA).strings().getFirst();
            String filename = is.getType().toString().toLowerCase(Locale.ROOT) + ".json";
            String contents = String.format(template, name, TARDISStringUtils.toScoredLowercase(name));
            File file = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + filename);
            // save to file
            try {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                    bw.write(contents);
                }
            } catch (IOException e) {
                plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
            }
        } catch (NoSuchElementException | NullPointerException e) {
            plugin.debug("No custom model string data for " + is.getType());
        }
    }
}
