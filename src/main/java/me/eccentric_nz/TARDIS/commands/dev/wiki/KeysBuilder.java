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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;

public class KeysBuilder {

    private final TARDIS plugin;
    private final File wikiDir;

    public KeysBuilder(TARDIS plugin) {
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
        File file = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + "gold_nugget.json");
        // make cases for all tardis key items
        for (KeyVariant k : KeyVariant.values()) {
            ItemStack is = ItemStack.of(Material.GOLD_NUGGET, 1);
            save(is, k, file);
        }
    }

    public void save(ItemStack is, KeyVariant k, File file) {
        try {
            String name = k.toString().toLowerCase(Locale.ROOT);
            String template = """
                                {
                                    "when": "%s",
                                    "model": {
                                        "type": "minecraft:model",
                                        "model": "tardis:item/key/%s"
                                    }
                                },
                    """;
            String contents = String.format(template, name, name);
            // save to file
            try {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                    bw.write(contents);
                }
            } catch (IOException e) {
                plugin.debug("Could not create and write to key file! " + e.getMessage());
            }
        } catch (NoSuchElementException | NullPointerException e) {
            plugin.debug("No custom model string data for " + is.getType());
        }
    }
}
