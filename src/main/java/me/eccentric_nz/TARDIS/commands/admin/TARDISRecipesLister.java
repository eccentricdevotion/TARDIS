package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Map;

public class TARDISRecipesLister {

    private final TARDIS plugin;

    public TARDISRecipesLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void listRecipes(CommandSender sender) {
        for (Map.Entry<String, ShapedRecipe> shaped : plugin.getFigura().getShapedRecipes().entrySet()) {
            sender.sendMessage(TARDISStringUtils.toUnderscoredUppercase(shaped.getKey()) + "(\"" + shaped.getKey() + "\", Material." + shaped.getValue().getResult().getType().toString() + ", " + RecipeItem.getByName(shaped.getKey()).getCustomModelData() + "),");
        }
        for (Map.Entry<String, ShapelessRecipe> shapeless : plugin.getIncomposita().getShapelessRecipes().entrySet()) {
            sender.sendMessage(TARDISStringUtils.toUnderscoredUppercase(shapeless.getKey()) + "(\"" + shapeless.getKey() + "\", Material." + shapeless.getValue().getResult().getType().toString() + ", " + RecipeItem.getByName(shapeless.getKey()).getCustomModelData() + "),");
        }
        for (Map.Entry<Schematic, ShapedRecipe> seed : plugin.getOobstructionum().getSeedRecipes().entrySet()) {
            int model;
            String material;
            if (TARDISSeedModel.materialMap.containsKey(seed.getKey().getSeedMaterial())) {
                model = TARDISSeedModel.modelByMaterial(seed.getKey().getSeedMaterial());
                if (seed.getKey().getPermission().equals("rotor")) {
                    material = "MUSHROOM_STEM";
                } else {
                    material = "RED_MUSHROOM_BLOCK";
                }
            } else {
                model = 45;
                material = "MUSHROOM_STEM";
            }
            sender.sendMessage(seed.getKey().getPermission().toUpperCase() + "_SEED(\"" + seed.getKey().getPermission() + "\", Material." + material + ", " + model + "),");
        }
        if (plugin.checkTWA()) {
            for (Monster m : Monster.values()) {
                sender.sendMessage(m.toString() + "_HEAD(\"" + m.getName() + " Head\", Material." + m.getMaterial().toString() + ", " + m.getCustomModelData() + "),");
            }
        }
    }
}
