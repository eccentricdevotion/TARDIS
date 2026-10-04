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
import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class ArmourBuilder {

    private final TARDIS plugin;
    private final File wikiDir;

    public ArmourBuilder(TARDIS plugin) {
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
        // make JSON files for all monster armour
        for (ArmourVariant s : ArmourVariant.values()) {
            if (!s.getKey().getKey().contains("monster")) {
                String m = s.toString();
                if (m.contains("CYBERMAN") || m.contains("LORD")) {
                    m = "CYBERMAN";
                }
                if (m.contains("OOD")) {
                    m = "OOD";
                }
                if (m.contains("DROID")) {
                    m = "CLOCKWORK_DROID";
                }
                if (m.equals("SILENCE")) {
                    m = "SILENT";
                }
                Monster monster = Monster.valueOf(m);
                save(monster.getMaterial(), s.getKey().getKey(), monster.getEntityType().getKey().getKey());
            }
        }
    }

    public void save(Material material, String name, String entity) {
        String filename = material.toString().toLowerCase(Locale.ROOT) + ".json";
        String chestAndLeg = """
                 {
                   "bedrock_identifier": "tardis:item/monster/%s_chestplate",
                   "bedrock_options": {
                     "icon": "tardis.item_monster_monster_chestplate"
                   },
                   "components": {
                     "minecraft:equippable": {
                       "slot": "chest",
                       "asset_id": "tardis:%s",
                       "allowed_entities": [
                         "minecraft:%s",
                         "minecraft:player"
                       ],
                       "damage_on_hurt": false
                     }
                   },
                   "model": "tardis:monster_chestplate",
                   "type": "definition"
                 },
                 {
                   "bedrock_identifier": "tardis:item/monster/%s_leggings",
                   "bedrock_options": {
                     "icon": "tardis.item_monster_monster_leggings"
                   },
                   "components": {
                     "minecraft:equippable": {
                       "slot": "legs",
                       "asset_id": "tardis:%s",
                       "allowed_entities": [
                         "minecraft:%s",
                         "minecraft:player"
                       ],
                       "damage_on_hurt": false
                     }
                   },
                   "model": "tardis:monster_leggings",
                   "type": "definition"
                 }
                """;
        String chestplateFile = String.format("tardis.item_monster_%s_chestplate.json", name);
        String chestplate = """
                {
                  "format_version": "1.21.0",
                  "minecraft:attachable": {
                    "description": {
                      "identifier": "tardis:item/monster/%s_chestplate",
                      "materials": {
                        "enchanted": "armor_enchanted",
                        "default": "armor"
                      },
                      "textures": {
                        "default": "textures/entity/equipment/humanoid/%s",
                        "enchanted": "textures/misc/enchanted_actor_glint"
                      },
                      "geometry": {
                        "default": "geometry.player.armor.chestplate"
                      },
                      "scripts": {
                        "parent_setup": "variable.chest_layer_visible = 0.0;"
                      },
                      "render_controllers": [
                        "controller.render.armor"
                      ]
                    }
                  }
                }
                """;
        String leggingsFile = String.format("tardis.item_monster_%s_leggings.json", name);
        String leggings = """
                {
                  "format_version": "1.21.0",
                  "minecraft:attachable": {
                    "description": {
                      "identifier": "tardis:item/monster/%s_leggings",
                      "materials": {
                        "enchanted": "armor_enchanted",
                        "default": "armor"
                      },
                      "textures": {
                        "default": "textures/entity/equipment/humanoid_leggings/%s",
                        "enchanted": "textures/misc/enchanted_actor_glint"
                      },
                      "geometry": {
                        "default": "geometry.player.armor.leggings"
                      },
                      "scripts": {
                        "parent_setup": "variable.leg_layer_visible = 0.0;"
                      },
                      "render_controllers": [
                        "controller.render.armor"
                      ]
                    }
                  }
                }
                """;
        String contents = String.format(chestAndLeg, name, name, entity, name, name, entity);
        String chestplateContents = String.format(chestplate, name, name);
        String leggingsContents = String.format(leggings, name, name);
        File file = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + filename);
        File cfile = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + chestplateFile);
        File lfile = new File(plugin.getDataFolder() + File.separator + "wiki" + File.separator + leggingsFile);
        // save to file
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(contents);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cfile, true))) {
                bw.write(chestplateContents);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(lfile, true))) {
                bw.write(leggingsContents);
            }
        } catch (IOException e) {
            plugin.debug("Could not create and write to " + filename + "! " + e.getMessage());
        }
    }
}
