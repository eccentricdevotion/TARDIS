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
package me.eccentric_nz.tardischemistry.product;

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public enum Product {

    Blue_Lamp("-,Cerium Chloride,-|-,REDSTONE_LAMP,-|-,-,-", Material.REDSTONE_LAMP, LampVariant.BLUE_LAMP.getKey()),
    Green_Lamp("-,Tungsten Chloride,-|-,REDSTONE_LAMP,-|-,-,-", Material.REDSTONE_LAMP, LampVariant.GREEN_LAMP.getKey()),
    Purple_Lamp("-,Potassium Chloride,-|-,REDSTONE_LAMP,-|-,-,-", Material.REDSTONE_LAMP, LampVariant.PURPLE_LAMP.getKey()),
    Red_Lamp("-,Mercuric Chloride,-|-,REDSTONE_LAMP,-|-,-,-", Material.REDSTONE_LAMP, LampVariant.RED_LAMP.getKey()),
    White_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,WHITE_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.WHITE_STAINED_GLASS_PANE, GlowStickVariant.WHITE_GLOW_STICK.getKey()),
    Orange_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,ORANGE_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.ORANGE_STAINED_GLASS_PANE, GlowStickVariant.ORANGE_GLOW_STICK.getKey()),
    Magenta_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,MAGENTA_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.MAGENTA_STAINED_GLASS_PANE, GlowStickVariant.MAGENTA_GLOW_STICK.getKey()),
    Light_Blue_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,LIGHT_BLUE_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.LIGHT_BLUE_STAINED_GLASS_PANE, GlowStickVariant.LIGHT_BLUE_GLOW_STICK.getKey()),
    Yellow_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,YELLOW_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.YELLOW_STAINED_GLASS_PANE, GlowStickVariant.YELLOW_GLOW_STICK.getKey()),
    Lime_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,LIME_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.LIME_STAINED_GLASS_PANE, GlowStickVariant.LIME_GLOW_STICK.getKey()),
    Pink_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,PINK_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.PINK_STAINED_GLASS_PANE, GlowStickVariant.PINK_GLOW_STICK.getKey()),
    Light_Gray_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,GRAY_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.LIGHT_GRAY_STAINED_GLASS_PANE, GlowStickVariant.LIGHT_GRAY_GLOW_STICK.getKey()),
    Cyan_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,CYAN_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.CYAN_STAINED_GLASS_PANE, GlowStickVariant.CYAN_GLOW_STICK.getKey()),
    Purple_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,PURPLE_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.PURPLE_STAINED_GLASS_PANE, GlowStickVariant.PURPLE_GLOW_STICK.getKey()),
    Blue_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,BLUE_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.BLUE_STAINED_GLASS_PANE, GlowStickVariant.BLUE_GLOW_STICK.getKey()),
    Brown_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,BROWN_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.BROWN_STAINED_GLASS_PANE, GlowStickVariant.BROWN_GLOW_STICK.getKey()),
    Green_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,GREEN_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.GREEN_STAINED_GLASS_PANE, GlowStickVariant.GREEN_GLOW_STICK.getKey()),
    Red_Glow_Stick("Polyethylene,Luminol,Polyethylene|Polyethylene,RED_DYE,Polyethylene|Polyethylene,Hydrogen Peroxide,Polyethylene", Material.RED_STAINED_GLASS_PANE, GlowStickVariant.RED_GLOW_STICK.getKey()),
    White_Balloon("Latex,WHITE_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.WHITE_BALLOON.getKey()),
    Orange_Balloon("Latex,ORANGE_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.ORANGE_BALLOON.getKey()),
    Magenta_Balloon("Latex,MAGENTA_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.MAGENTA_BALLOON.getKey()),
    Light_Blue_Balloon("Latex,LIGHT_BLUE_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.LIGHT_BLUE_BALLOON.getKey()),
    Yellow_Balloon("Latex,YELLOW_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.YELLOW_BALLOON.getKey()),
    Lime_Balloon("Latex,LIME_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.LIME_BALLOON.getKey()),
    Pink_Balloon("Latex,PINK_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.PINK_BALLOON.getKey()),
    Gray_Balloon("Latex,GRAY_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.GRAY_BALLOON.getKey()),
    Light_Gray_Balloon("Latex,LIGHT_GRAY_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.LIGHT_GRAY_BALLOON.getKey()),
    Cyan_Balloon("Latex,CYAN_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.CYAN_BALLOON.getKey()),
    Purple_Balloon("Latex,PURPLE_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.PURPLE_BALLOON.getKey()),
    Blue_Balloon("Latex,BLUE_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.BLUE_BALLOON.getKey()),
    Brown_Balloon("Latex,BROWN_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.BROWN_BALLOON.getKey()),
    Green_Balloon("Latex,GREEN_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.GREEN_BALLOON.getKey()),
    Red_Balloon("Latex,RED_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.RED_BALLOON.getKey()),
    Black_Balloon("Latex,BLACK_DYE,Latex|Latex,Helium,Latex|Latex,STRING,Latex", Material.CORNFLOWER, Balloon.BLACK_BALLOON.getKey()),
    Orange_Sparkler("-,Calcium Chloride,-|-,Magnesium,-|-,STICK,-", Material.END_ROD, Sparkler.SPARKLER_ORANGE.getKey()),
    Blue_Sparkler("-,Cerium Chloride,-|-,Magnesium,-|-,STICK,-", Material.END_ROD, Sparkler.SPARKLER_BLUE.getKey()),
    Green_Sparkler("-,Tungsten Chloride,-|-,Magnesium,-|-,STICK,-", Material.END_ROD, Sparkler.SPARKLER_GREEN.getKey()),
    Purple_Sparkler("-,Potassium Chloride,-|-,Magnesium,-|-,STICK,-", Material.END_ROD, Sparkler.SPARKLER_PURPLE.getKey()),
    Red_Sparkler("-,Mercuric Chloride,-|-,Magnesium,-|-,STICK,-", Material.END_ROD, Sparkler.SPARKLER_RED.getKey());

    private static final HashMap<String, Product> byName = new HashMap<>();

    static {
        for (Product p : values()) {
            byName.put(p.getName(), p);
        }
    }

    private final String recipe;
    private final Material material;
    private final NamespacedKey model;

    Product(String recipe, Material material, NamespacedKey model) {
        this.recipe = recipe;
        this.material = material;
        this.model = model;
    }

    public static HashMap<String, Product> getByName() {
        return byName;
    }

    public String getRecipe() {
        return recipe;
    }

    public Material getMaterial() {
        return material;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public String getName() {
        return this.toString().replace("_", " ");
    }

    public NamespacedKey getActive() {
        switch (this) {
            case Blue_Lamp -> { return LanternVariant.BLUE_LAMP_ON.getKey(); }
            case Green_Lamp -> { return LanternVariant.GREEN_LAMP_ON.getKey(); }
            case Purple_Lamp -> { return LanternVariant.PURPLE_LAMP_ON.getKey(); }
            case Red_Lamp -> { return LanternVariant.RED_LAMP_ON.getKey(); }
            case White_Glow_Stick -> { return GlowStickVariant.WHITE_GLOW_STICK_ACTIVE.getKey(); }
            case Orange_Glow_Stick -> { return GlowStickVariant.ORANGE_GLOW_STICK_ACTIVE.getKey(); }
            case Magenta_Glow_Stick -> { return GlowStickVariant.MAGENTA_GLOW_STICK_ACTIVE.getKey(); }
            case Light_Blue_Glow_Stick -> { return  GlowStickVariant.LIGHT_BLUE_GLOW_STICK_ACTIVE.getKey(); }
            case Yellow_Glow_Stick -> { return GlowStickVariant.YELLOW_GLOW_STICK_ACTIVE.getKey(); }
            case Lime_Glow_Stick -> { return GlowStickVariant.LIME_GLOW_STICK_ACTIVE.getKey(); }
            case Pink_Glow_Stick -> { return GlowStickVariant.PINK_GLOW_STICK_ACTIVE.getKey(); }
            case Light_Gray_Glow_Stick -> { return GlowStickVariant.LIGHT_GRAY_GLOW_STICK_ACTIVE.getKey(); }
            case Cyan_Glow_Stick -> { return GlowStickVariant.CYAN_GLOW_STICK_ACTIVE.getKey(); }
            case Purple_Glow_Stick -> { return GlowStickVariant.PURPLE_GLOW_STICK_ACTIVE.getKey(); }
            case Blue_Glow_Stick -> { return GlowStickVariant.BLUE_GLOW_STICK_ACTIVE.getKey(); }
            case Brown_Glow_Stick -> { return GlowStickVariant.BROWN_GLOW_STICK_ACTIVE.getKey(); }
            case Green_Glow_Stick -> { return GlowStickVariant.GREEN_GLOW_STICK_ACTIVE.getKey(); }
            case Red_Glow_Stick -> { return GlowStickVariant.RED_GLOW_STICK_ACTIVE.getKey(); }
            case Orange_Sparkler -> { return Sparkler.SPARKLER_ORANGE.getKey(); }
            case Blue_Sparkler -> { return Sparkler.SPARKLER_BLUE.getKey(); }
            case Green_Sparkler -> { return Sparkler.SPARKLER_GREEN.getKey(); }
            case Purple_Sparkler -> { return Sparkler.SPARKLER_PURPLE.getKey(); }
            case Red_Sparkler -> { return Sparkler.SPARKLER_RED.getKey(); }
            default -> {return null;}
        }
    }
}
