package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIConfiguration;
import me.eccentric_nz.TARDIS.custommodels.GUIKeyPreferences;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Features;
import me.eccentric_nz.TARDIS.custommodels.keys.MireVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SlitheenVariant;
import me.eccentric_nz.TARDIS.skins.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.util.Locale;

public class ComponentCommand {

    private final TARDIS plugin;
    private Gson gson;
    private String template = """
            {
              "type": "minecraft:select",
              "property": "component",
              "component": "minecraft:custom_name",
              "fallback": {
                "type": "minecraft:model",
                "model": "minecraft:block/%s"
              },
              "cases": [
                {
                  "when": {
                    "text": "",
                    "extra": [
                        "%s"
                    ]
                  },
                  "model": {
                    "type": "minecraft:model",
                    "model": "tardis:item/gui/room/%s"
                  }
                }
              ]
            }
            """;

    public ComponentCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void writeARS() {
        try {
            File dir = new File(plugin.getDataFolder() + File.separator + "component_models");
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    dir.setWritable(true);
                    dir.setExecutable(true);
                }
            }
            for (TARDISARS ars : TARDISARS.values()) {
                if (!ars.getMaterial().isEmpty()) {
                    String material = ars.getMaterial().toLowerCase(Locale.ROOT);
                    String json = String.format(template, material, ars.getDescriptiveName(), ars.toString().toLowerCase(Locale.ROOT));
                    File file = new File(plugin.getDataFolder() + File.separator + "component_models" + File.separator + material + ".json");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    bw.write(json);
                    bw.close();
                }
            }
            plugin.debug("Component model writing complete.");
        } catch (IOException e) {
            plugin.debug(e.getMessage());
        }
    }

    public void writeKey() {
        JsonObject base = new JsonObject();
        base.addProperty("type", "minecraft:select");
        base.addProperty("property", "component");
        base.addProperty("component", "minecraft:custom_model_data");
        JsonObject fallback = new JsonObject();
        fallback.addProperty("type", "minecraft:model");
        fallback.addProperty("model", "minecraft:item/gold_nugget");
        base.add("fallback", fallback);
        /*
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:component",
    "component": "minecraft:custom_model_data",
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/gold_nugget"
    },
    "cases": [
      {
        "when": [
          {
            "floats": [
              101
            ]
          }
        ],
        "model": {
          "type": "minecraft:model",
          "model": "tardis:item/key/brass_plain"
        }
      }...

      ]
  }
}
         */
        JsonArray cases = new JsonArray();
        int i = 1;
        for (GUIKeyPreferences kk : GUIKeyPreferences.values()) {
            if (kk.getSlot() < 18) {
                String lowercaseName = kk.toString().toLowerCase(Locale.ROOT);
                JsonObject case_cmd = getCase(100 + i, lowercaseName, "key");
                cases.add(case_cmd);
                i++;
            }
        }
        base.add("cases", cases);
        // write the file
        String output = plugin.getDataFolder() + File.separator + "processed" + File.separator + "gold_nugget.json";
        File file = new File(output);
        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(base, writer);
        } catch (IOException e) {
            plugin.debug("File write error for gold_nugget");
            plugin.debug(e.getMessage());
        }
    }

    public void writeRepeater() {
        JsonObject base = new JsonObject();
        base.addProperty("type", "minecraft:select");
        base.addProperty("property", "component");
        base.addProperty("component", "minecraft:custom_model_data");
        JsonObject fallback = new JsonObject();
        fallback.addProperty("type", "minecraft:model");
        fallback.addProperty("model", "minecraft:item/repeater");
        base.add("fallback", fallback);
        /*
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:component",
    "component": "minecraft:custom_model_data",
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/repeater"
    },
    "cases": [
      {
        "when": [
          {
            "floats": [
              101
            ]
          }
        ],
        "model": {
          "type": "minecraft:model",
          "model": "tardis:item/gui/repeater/police_box_off"
        }
      },
      {
        "when": {
          "floats": [
            201
          ]
        },
        "model": {
          "type": "minecraft:model",
          "model": "tardis:item/gui/repeater/police_box_on"
        }
      }...

      ]
  }
}
         */
        JsonArray cases = new JsonArray();
        int i = 1;
        for (GUIPlayerPreferences pp : GUIPlayerPreferences.values()) {
            if (pp.getMaterial() == Material.REPEATER) {
                String lowercaseName = pp.toString().toLowerCase(Locale.ROOT);
                JsonObject case_off = getCase(100 + i, lowercaseName + "_off", "gui/repeater");
                cases.add(case_off);
                JsonObject case_on = getCase(200 + i, lowercaseName + "_on", "gui/repeater");
                cases.add(case_on);
                i++;
            }
        }
        for (GUIConfiguration cc : GUIConfiguration.values()) {
            if (cc.getMaterial() == Material.REPEATER) {
                String lowercaseName = cc.toString().toLowerCase(Locale.ROOT);
                JsonObject case_off = getCase(100 + i, lowercaseName + "_off", "gui/repeater");
                cases.add(case_off);
                JsonObject case_on = getCase(200 + i, lowercaseName + "_on", "gui/repeater");
                cases.add(case_on);
                i++;
            }
        }
        base.add("cases", cases);
        // write the file
        String output = plugin.getDataFolder() + File.separator + "processed" + File.separator + "repeater.json";
        File file = new File(output);
        try (FileWriter writer = new FileWriter(file)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(base, writer);
        } catch (IOException e) {
            plugin.debug("File write error for repeater");
            plugin.debug(e.getMessage());
        }
    }

    private JsonObject getCase(int i, String name, String path) {
        JsonObject object = new JsonObject();
        JsonObject when = new JsonObject();
        JsonArray floats = new JsonArray();
        floats.add(i);
        when.add("floats", floats);
        object.add("when", when);
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", "tardis:item/" + path + "/" + name);
        object.add("model", model);
        return object;
    }

    public void writeExtra(CommandSender sender) {
        NamespacedKey key;
        String material = "leather";
        for (Skin skin : CharacterSkins.CHARACTERS) {
            material = "leather";
            switch (skin.name()) {
                case "Bannakaffalatta" -> {
                    material = "nether_wart";
                    key = Features.BANNAKAFFALATTA_SPIKES.getKey();
                }
                case "Brigadier Lethbridge-Stewart" -> key = Features.BRIGADIER_LETHBRIDGE_STEWART_HAT.getKey();
                case "Dalek Sec" -> {
                    material = "mangrove_propagule";
                    key = Features.DALEK_SEC_TENTACLES.getKey();
                }
                case "Hath" -> {
                    material = "pufferfish";
                    key = Features.HATH_FEATURES.getKey();
                }
                case "Impossible Astronaut" -> {
                    material = "orange_stained_glass_pane";
                    key = Features.IMPOSSIBLE_ASTRONAUT_PACK.getKey();
                }
                case "Jenny Flint" -> {
                    // 17 off-hand katana
                    writeFile(sender, "leather", Features.JENNY_FLINT_KATANA.getKey().getKey(), "Katana");
                    continue;
                }
                case "Judoon" -> {
                    material = "yellow_dye";
                    key = Features.JUDOON_SNOUT.getKey();
                }
                case "Ood" -> {
                    material = "rotten_flesh";
                    key = Features.OOD_FEATURES.getKey();
                }
                case "Strax" -> {
                    material = "potato";
                    key = Features.STRAX_EARS.getKey();
                }
                default -> {
                    continue;
                }
            }
            // write file
            if (key != null) {
                writeFile(plugin.getConsole(), material, key.getKey(), skin.name());
            } else {
                plugin.debug("null key for " + skin.name());
            }
        }
        for (Skin skin : CompanionSkins.COMPANIONS) {
            material = "leather";
            switch (skin.name()) {
                case "Ace" -> key = Features.ACE_PONYTAIL.getKey();
                case "Jo Grant" -> key = Features.JO_GRANT_HAIR.getKey();
                case "Martha Jones" -> key = Features.MARTHA_JONES_HAIR.getKey();
                case "Tegan" -> key = Features.TEGAN_HAT.getKey();
                default -> {
                    continue;
                }
            }
            // write file
            if (key != null) {
                writeFile(plugin.getConsole(), material, key.getKey(), skin.name());
            } else {
                plugin.debug("null key for " + skin.name());
            }
        }
        for (Skin skin : CyberSkins.VARIANTS) {
            material = "leather";
            switch (skin.name()) {
                case "Cyberman" -> {
                    material = "iron_ingot";
                    key = Features.CYBERMAN_FEATURES.getKey();
                    // + 7 weapon
                    writeFile(sender, material, CybermanVariant.CYBER_WEAPON.getKey().getKey(), "Cyber Weapon");
                }
                case "Wooden Cyberman" -> {
                    material = "spruce_button";
                    key = Features.WOOD_CYBERMAN_FEATURES.getKey();
                    // + weapon
                    writeFile(sender, material, CybermanVariant.WOOD_CYBER_WEAPON.getKey().getKey(), "Wood Cyber Weapon");
                }
                case "Black Cyberman" -> {
                    material = "iron_ingot";
                    key = Features.BLACK_CYBERMAN_FEATURES.getKey();
                }
                case "Invasion Cyberman" -> {
                    material = "iron_ingot";
                    key = Features.CYBERMAN_INVASION_FEATURES.getKey();
                    // + arm decor
                    writeFile(sender, material, CybermanVariant.CYBERMAN_INVASION_ARM.getKey().getKey(), "Cyber Arm");
                }
                case "Rise of the Cyberman", "Cyber Lord", "Moonbase Cyberman" -> {
                    material = "iron_ingot";
                    key = switch (skin.name()) {
                        case "Rise of the Cyberman" -> Features.CYBERMAN_RISE_FEATURES.getKey();
                        case "Moonbase Cyberman" -> Features.CYBERMAN_MOONBASE_FEATURES.getKey();
                        default -> Features.CYBER_LORD_FEATURES.getKey();
                    };
                    // + arm decor
                    writeFile(sender, material, CybermanVariant.CYBERMAN_INVASION_ARM.getKey().getKey(), "Cyber Arm");
                }
                case "Tenth Planet Cyberman" -> {
                    material = "iron_ingot";
                    key = Features.CYBERMAN_TENTH_PLANET_FEATURES.getKey();
                }
                case "Earthshock Cyberman" -> {
                    material = "iron_ingot";
                    key = Features.CYBERMAN_EARTHSHOCK_FEATURES.getKey();
                }
                case "Cybershade" -> key = Features.CYBERSHADE_EARS.getKey();
                default -> {
                    continue;
                }
            }
            // write file
            if (key != null) {
                writeFile(plugin.getConsole(), material, key.getKey(), skin.name());
            } else {
                plugin.debug("null key for " + skin.name());
            }
        }
//        for (Skin skin : DoctorSkins.DOCTORS) {
//            switch (skin.name()) {
//                default -> {
//                    continue;
//                }
//            }
//            // write file
//            writeFile(plugin.getConsole(), material, key.getKey(), skin.name());
//        }
        for (Skin skin : MonsterSkins.MONSTERS) {
            material = "leather";
            switch (skin.name()) {
                case "Angel of Liberty" -> {
                    key = Features.ANGEL_OF_LIBERTY_CROWN.getKey();
                    // + 5 torch
                    writeFile(sender, "torch", Features.ANGEL_OF_LIBERTY_TORCH.getKey().getKey(), "Liberty Torch");
                }
                case "Empty Child" -> {
                    material = "sugar";
                    key = Features.EMPTY_CHILD_MASK.getKey();
                }
                case "Ice Warrior" -> {
                    material = "snowball";
                    key = Features.ICE_WARRIOR_CREST.getKey();
                }
                case "Mire" -> {
                    material = "netherite_scrap";
                    key = Features.MIRE_HELMET.getKey();
                    // + 7, 8 left, right arms
                    writeFile(sender, "netherite_scrap", MireVariant.MIRE_LEFT_ARM.getKey().getKey(), skin.name() + " Left Arm");
                    writeFile(sender, "netherite_scrap", MireVariant.MIRE_RIGHT_ARM.getKey().getKey(), skin.name() + " Right Arm");
                }
                case "Omega" -> key = Features.OMEGA_FRILL.getKey();
                case "Racnoss" -> key = Features.RACNOSS_FEATURES.getKey();
                case "Scarecrow" -> {
                    material = "wheat";
                    key = Features.SCARECROW_EARS.getKey();
                }
                case "Sea Devil" -> {
                    material = "kelp";
                    key = Features.SEA_DEVIL_EARS.getKey();
                }
                case "Silence" -> {
                    material = "end_stone";
                    key = Features.SILENCE_SIDE_HEAD.getKey();
                }
                case "Silurian" -> {
                    material = "feather";
                    key = Features.SILURIAN_CREST.getKey();
                }
                case "Slitheen" -> {
                    material = "turtle_egg";
                    key = Features.SLITHEEN_HEAD.getKey();
                    // + 7, 8 left, right claws
                    writeFile(sender, "turtle_egg", SlitheenVariant.SLITHEEN_CLAW_LEFT.getKey().getKey(), skin.name() + " Left Claw");
                    writeFile(sender, "turtle_egg", SlitheenVariant.SLITHEEN_CLAW_RIGHT.getKey().getKey(), skin.name() + " Right Claw");
                }
                case "Sontaran" -> {
                    material = "potato";
                    key = Features.SONTARAN_EARS.getKey();
                }
                case "Sutekh" -> key = Features.SUTEKH_FEATURES.getKey();
                case "Sycorax" -> key = Features.SYCORAX_CAPE.getKey();
                case "The Beast" -> key = Features.THE_BEAST_HORNS.getKey();
                case "Vampire of Venice" -> {
                    material = "cod";
                    key = Features.VAMPIRE_OF_VENICE_FAN.getKey();
                }
                case "Weeping Angel" -> {
                    material = "brick";
                    key = Features.WEEPING_ANGEL_WINGS.getKey();
                }
                case "Zygon" -> {
                    material = "painting";
                    key = Features.ZYGON_CREST.getKey();
                }
                default -> {
                    continue;
                }
            }
            // write file
            if (key != null) {
                writeFile(plugin.getConsole(), material, key.getKey(), skin.name());
            } else {
                plugin.debug("null key for " + skin.name());
            }
        }
    }

    private void writeFile(CommandSender sender, String material, String key, String name) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        String filename = material + "_" + key;
        // make json - something like:
        JsonObject select = new JsonObject();
        select.addProperty("type", "minecraft:select");
        select.addProperty("property", "component");
        select.addProperty("component", "minecraft:custom_name");
        JsonObject fallback = new JsonObject();
        fallback.addProperty("type", "minecraft:model");
        fallback.addProperty("model", "minecraft:item/" + material);
        select.add("fallback", fallback);
        JsonArray cases = new JsonArray();
        JsonObject matcher = new JsonObject();
        JsonObject when = new JsonObject();
        when.addProperty("text", "");
        JsonArray extra = new JsonArray();
        extra.add(name);
        when.add("extra", extra);
        matcher.add("when", when);
        // get the model
        JsonObject model;
        try {
            String path = plugin.getDataFolder() + File.separator + "component" + File.separator + key + ".json";
            sender.sendMessage(path);
            File child = new File(path);
            JsonReader reader = new JsonReader(new FileReader(child));
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            model = root.get("model").getAsJsonObject();
        } catch (FileNotFoundException e) {
            // guess the model
            model = new JsonObject();
            model.addProperty("type", "minecraft:model");
            // get the model
            model.addProperty("model", "tardis:item/" + key);
        }
        matcher.add("model", model);
        cases.add(matcher);
        select.add("cases", cases);
        // write the file
        String output = plugin.getDataFolder() + File.separator + "component" + File.separator + "processed" + File.separator + filename + ".json";
        File file = new File(output);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(select, writer);
        } catch (IOException e) {
            sender.sendMessage("File write error for " + filename);
            sender.sendMessage(e.getMessage());
        }
    }

}
