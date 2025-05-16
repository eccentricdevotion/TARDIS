package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.*;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class ResourcePackConverterCommand {

    private final TARDIS plugin;
    private Gson gson;

    public ResourcePackConverterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    private static String uppercaseFirst(String s) {
        if (s.equalsIgnoreCase("ii") || s.equalsIgnoreCase("iii") || s.equalsIgnoreCase("iv")) {
            return s.toUpperCase(Locale.ROOT);
        }
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT).replace("tardis", "TARDIS");
    }

    public boolean process(CommandSender sender, String[] args) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        if (args.length > 2) {
            // get folder
            File[] directoryListing = new File(plugin.getDataFolder() + File.separator + "component").listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.isDirectory() || !child.getName().endsWith(".json")) {
                        continue;
                    }
                    // do something with child
                    try {
                        // get the json something like
                        /*
                        {
                          "model": {
                            "type": "minecraft:model",
                            "model": "tardis:item/tardis/3d_glasses"
                          }
                        }
                         */
                        JsonReader reader = new JsonReader(new FileReader(child));
                        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                        JsonObject model = root.get("model").getAsJsonObject();
                        // convert it to select component by custom name
                        /*
                        {
                            "model": {
                                "type": "minecraft:select",
                                "property": "component",
                                "component": "custom_name",
                                "fallback": {
                                    "type": "minecraft:model",
                                    "model": "minecraft:item/leather_helmet"
                                },
                                "cases": [
                                    {
                                        "when":{
                                           "text":"",
                                           "extra":[
                                              {
                                                 "bold":false,
                                                 "color":"white",
                                                 "italic":false,
                                                 "obfuscated":false,
                                                 "strikethrough":false,
                                                 "text":"3-D Glasses",
                                                 "underlined":false
                                              }
                                           ]
                                        },
                                        "model":{
                                           "type":"minecraft:model",
                                           "model":"tardis:item/tardis/3d_glasses"
                                        }
                                    }
                                ]
                            }
                        }
                         */
                        JsonObject select = new JsonObject();
                        select.addProperty("type", "minecraft:select");
                        select.addProperty("property", "component");
                        select.addProperty("component", "minecraft:custom_name");
                        JsonObject fallback = new JsonObject();
                        fallback.addProperty("type", "minecraft:model");
                        // get the material this item is based on
                        String name;
                        if (hasEntry(child.getName())) {
                            name = getEntry(child.getName());
                        } else {
                            name = convertFilename(child.getName());
                        }
                        String material = getMaterial(name);
                        fallback.addProperty("model", "minecraft:item/" + material);
                        select.add("fallback", fallback);
                        JsonArray cases = new JsonArray();
                        JsonObject matcher = new JsonObject();
                        JsonObject when = new JsonObject();
                        when.addProperty("text", "");
                        JsonArray extra = new JsonArray();
                        JsonObject conditions = new JsonObject();
                        conditions.addProperty("bold", false);
                        conditions.addProperty("color", "white");
                        conditions.addProperty("italic", false);
                        conditions.addProperty("obfuscated", false);
                        conditions.addProperty("strikethrough", false);
                        conditions.addProperty("text", name);
                        conditions.addProperty("underlined", false);
                        extra.add(conditions);
                        when.add("extra", extra);
                        matcher.add("when", when);
                        matcher.add("model", model);
                        cases.add(matcher);
                        select.add("cases", cases);
                        // write the file
                        String output = plugin.getDataFolder() + File.separator + "component" + File.separator + "processed" + File.separator + material + "_" + child.getName();
                        File file = new File(output);
                        try (FileWriter writer = new FileWriter(file)) {
                            gson.toJson(select, writer);
                        } catch (IOException e) {
                            sender.sendMessage("File write error for " + child.getName());
                            sender.sendMessage(e.getMessage());
                        }
                    } catch (FileNotFoundException e) {
                        sender.sendMessage("File read error for " + child.getName());
                    }
                }
            }
        } else {
            for (Map.Entry<String, ShapedRecipe> entry : plugin.getTardisAPI().getShapedRecipes().entrySet()) {
                ShapedRecipe sr = entry.getValue();
                ItemStack result = sr.getResult();
                writeFile(sender, result);
            }
            for (Map.Entry<String, ShapelessRecipe> entry : plugin.getTardisAPI().getShapelessRecipes().entrySet()) {
                ShapelessRecipe sr = entry.getValue();
                ItemStack result = sr.getResult();
                writeFile(sender, result);
            }
            for (Field field : GUIArchive.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIChameleonPoliceBoxes.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUIFarming field : GUIFarming.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (GUISavedPrograms field : GUISavedPrograms.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (Field field : GUITransmat.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIArea.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIChameleonPresets.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIGeneticManipulator.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUISaves.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIUpgrade.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIArs.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIChameleonTemplate.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIInteriorSounds.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUISonicActivator.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIWallFloor.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIArtronStorage.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIChemistry.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUIKeyPreferences field : GUIKeyPreferences.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (GUISonicConfigurator field : GUISonicConfigurator.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (Field field : GUIWeather.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUIAutonomous field : GUIAutonomous.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (Field field : GUICompanion.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUILights.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUISonicGenerator field : GUISonicGenerator.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (Field field : GUIChameleon.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIMap.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUISonicPreferences field : GUISonicPreferences.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
            for (Field field : GUIChameleonConstructor.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIControlCentre.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIParticle.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUITelevision.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (Field field : GUIChameleonHelp.class.getDeclaredFields()) {
                writeFromField(sender, field);
            }
            for (GUITemporalLocator field : GUITemporalLocator.values()) {
                if (field.getModel() != null) {
                    writeShortFile(sender, field.getMaterial().toString().toLowerCase(Locale.ROOT), field.getModel().getKey());
                }
            }
        }
        sender.sendMessage("Pack conversion complete!");
        return true;
    }

    private void writeFromField(CommandSender sender, Field field) {
        field.setAccessible(true);
        try {
            GUIData data = (GUIData) field.get(this);
            String material = data.material().toString().toLowerCase(Locale.ROOT);
            String key = data.key().getKey();
            writeShortFile(sender, material, key);
        } catch (IllegalAccessException e) {
            sender.sendMessage(field.getName());
        }
    }

    private void writeFile(CommandSender sender, ItemStack result) {
        ItemMeta im = result.getItemMeta();
        String name = ChatColor.stripColor(im.getDisplayName());
        String material = result.getType().toString().toLowerCase(Locale.ROOT);
        String lowercaseName = name.toLowerCase(Locale.ROOT).replace(" ", "_");
        String filename = material + "_" + lowercaseName;
        // make json - something like:
        /*
        {
           "model":{
              "type":"minecraft:select",
              "property":"component",
              "component":"minecraft:custom_name",
              "fallback":{
                 "type":"minecraft:model",
                 "model":"minecraft:item/flint"
              },
              "cases":[
                 {
                    "when":{
                       "text":"",
                       "extra":[
                          {
                             "bold":false,
                             "color":"white",
                             "italic":false,
                             "obfuscated":false,
                             "strikethrough":false,
                             "text":"Stattenheim Remote",
                             "underlined":false
                          }
                       ]
                    },
                    "model":{
                       "type":"minecraft:model",
                       "model":"tardis:item/tardis/stattenheim_remote"
                    }
                 }
              ]
           }
        }
        */
        JsonObject select = new JsonObject();
        select.addProperty("type", "minecraft:select");
        select.addProperty("property", "component");
        select.addProperty("component", "minecraft:custom_name");
        JsonObject fallback = new JsonObject();
        fallback.addProperty("type", "minecraft:model");
        fallback.addProperty("model", "minecraft:" + (result.getType().isBlock() ? "block" : "item") + "/" + material);
        select.add("fallback", fallback);
        JsonArray cases = new JsonArray();
        JsonObject matcher = new JsonObject();
        JsonObject when = new JsonObject();
        when.addProperty("text", "");
        JsonArray extra = new JsonArray();
        JsonObject conditions = new JsonObject();
        conditions.addProperty("bold", false);
        conditions.addProperty("color", "white");
        conditions.addProperty("italic", false);
        conditions.addProperty("obfuscated", false);
        conditions.addProperty("strikethrough", false);
        conditions.addProperty("text", name);
        conditions.addProperty("underlined", false);
        extra.add(conditions);
        when.add("extra", extra);
        matcher.add("when", when);
        // get the model
        NamespacedKey nsk = im.getItemModel();
        String key;
        if (nsk != null) {
            key = nsk.getKey() + ".json";
        } else {
            key = lowercaseName + ".json";
        }
        File child = new File(plugin.getDataFolder() + File.separator + key);
        JsonObject model;
        try {
            JsonReader reader = new JsonReader(new FileReader(child));
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            model = root.get("model").getAsJsonObject();
        } catch (FileNotFoundException e) {
            // guess the model
            model = new JsonObject();
            model.addProperty("type", "minecraft:model");
            // get the model
            model.addProperty("model", "tardis:item/" + lowercaseName);
        }
        matcher.add("model", model);
        cases.add(matcher);
        select.add("cases", cases);
        // write the file
        String output = plugin.getDataFolder() + File.separator + "processed" + File.separator + filename + ".json";
        File file = new File(output);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(select, writer);
        } catch (IOException e) {
            sender.sendMessage("File write error for " + filename);
            sender.sendMessage(e.getMessage());
        }
    }

    private void writeFile(CommandSender sender, String material, String key) {
        String filename = material + "_" + key;
        // make json - something like:
        /*
        {
           "model":{
              "type":"minecraft:select",
              "property":"component",
              "component":"minecraft:custom_name",
              "fallback":{
                 "type":"minecraft:model",
                 "model":"minecraft:item/flint"
              },
              "cases":[
                 {
                    "when":{
                       "text":"",
                       "extra":[
                          {
                             "bold":false,
                             "color":"white",
                             "italic":false,
                             "obfuscated":false,
                             "strikethrough":false,
                             "text":"Stattenheim Remote",
                             "underlined":false
                          }
                       ]
                    },
                    "model":{
                       "type":"minecraft:model",
                       "model":"tardis:item/tardis/stattenheim_remote"
                    }
                 }
              ]
           }
        }
        */
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
        JsonObject conditions = new JsonObject();
        conditions.addProperty("bold", false);
        conditions.addProperty("color", "white");
        conditions.addProperty("italic", false);
        conditions.addProperty("obfuscated", false);
        conditions.addProperty("strikethrough", false);
        String name;
        if (hasEntry(key)) {
            name = getEntry(key);
        } else {
            name = convertFilename(key);
        }
        conditions.addProperty("text", name);
        conditions.addProperty("underlined", false);
        extra.add(conditions);
        when.add("extra", extra);
        matcher.add("when", when);
        // get the model
        JsonObject model;
        try {
            String path = plugin.getDataFolder() + File.separator + key + ".json";
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
        String output = plugin.getDataFolder() + File.separator + "processed" + File.separator + filename + ".json";
        File file = new File(output);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(select, writer);
        } catch (IOException e) {
            sender.sendMessage("File write error for " + filename);
            sender.sendMessage(e.getMessage());
        }
    }

    private void writeShortFile(CommandSender sender, String material, String key) {
        String filename = material + "_" + key;
        String name;
        if (hasEntry(key)) {
            name = getEntry(key);
        } else {
            name = convertFilename(key);
        }
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

    private String convertFilename(String s) {
        String[] parts = s.split("\\.");
        String[] split = parts[0].replace("button_", "").replace("switch_", "").split("_");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
        }
        return builder.toString().trim();
    }

    public String getMaterial(String name) {
        plugin.debug(name);
        if (plugin.getFigura().getShapedRecipes().containsKey(name)) {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(name);
            return recipe.getResult().getType().toString().toLowerCase(Locale.ROOT);
        }
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(name)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(name);
            return recipe.getResult().getType().toString().toLowerCase(Locale.ROOT);
        }
        return "bowl";
    }

    public boolean hasEntry(String s) {
        String[] parts = s.split("\\.");
        String upper = parts[0].toUpperCase(Locale.ROOT);
        return plugin.getLanguage().getString(upper) != null;
    }

    public String getEntry(String s) {
        String[] parts = s.split("\\.");
        String upper = parts[0].toUpperCase(Locale.ROOT);
        return plugin.getLanguage().getString(upper);
    }
}
