package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class ColourType {

    public static final HashMap<Material, NamespacedKey> LOOKUP = new HashMap<>();
    public static final HashMap<NamespacedKey, String> COLOURS = new HashMap<>();

    static {
        LOOKUP.put(Material.LIGHT_GRAY_CONCRETE_POWDER, LightGrayConcrete.CONSOLE_LIGHT_GRAY.getKey());
        LOOKUP.put(Material.GRAY_CONCRETE_POWDER, GrayConcrete.CONSOLE_GRAY.getKey());
        LOOKUP.put(Material.BLACK_CONCRETE_POWDER, BlackConcrete.CONSOLE_BLACK.getKey());
        LOOKUP.put(Material.WHITE_CONCRETE_POWDER, WhiteConcrete.CONSOLE_WHITE.getKey());
        LOOKUP.put(Material.RED_CONCRETE_POWDER, RedConcrete.CONSOLE_RED.getKey());
        LOOKUP.put(Material.ORANGE_CONCRETE_POWDER, OrangeConcrete.CONSOLE_ORANGE.getKey());
        LOOKUP.put(Material.YELLOW_CONCRETE_POWDER, YellowConcrete.CONSOLE_YELLOW.getKey());
        LOOKUP.put(Material.LIME_CONCRETE_POWDER, LimeConcrete.CONSOLE_LIME.getKey());
        LOOKUP.put(Material.GREEN_CONCRETE_POWDER, GreenConcrete.CONSOLE_GREEN.getKey());
        LOOKUP.put(Material.CYAN_CONCRETE_POWDER, CyanConcrete.CONSOLE_CYAN.getKey());
        LOOKUP.put(Material.LIGHT_BLUE_CONCRETE_POWDER, LightBlueConcrete.CONSOLE_LIGHT_BLUE.getKey());
        LOOKUP.put(Material.BLUE_CONCRETE_POWDER, BlueConcrete.CONSOLE_BLUE.getKey());
        LOOKUP.put(Material.PURPLE_CONCRETE_POWDER, PurpleConcrete.CONSOLE_PURPLE.getKey());
        LOOKUP.put(Material.MAGENTA_CONCRETE_POWDER, MagentaConcrete.CONSOLE_MAGENTA.getKey());
        LOOKUP.put(Material.PINK_CONCRETE_POWDER, PinkConcrete.CONSOLE_PINK.getKey());
        LOOKUP.put(Material.BROWN_CONCRETE_POWDER, BrownConcrete.CONSOLE_BROWN.getKey());
        COLOURS.put(LightGrayConcrete.CONSOLE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(GrayConcrete.CONSOLE_GRAY.getKey(), "GRAY");
        COLOURS.put(BlackConcrete.CONSOLE_BLACK.getKey(), "BLACK");
        COLOURS.put(WhiteConcrete.CONSOLE_WHITE.getKey(), "WHITE");
        COLOURS.put(RedConcrete.CONSOLE_RED.getKey(), "RED");
        COLOURS.put(OrangeConcrete.CONSOLE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(YellowConcrete.CONSOLE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(LimeConcrete.CONSOLE_LIME.getKey(), "LIME");
        COLOURS.put(GreenConcrete.CONSOLE_GREEN.getKey(), "GREEN");
        COLOURS.put(CyanConcrete.CONSOLE_CYAN.getKey(), "CYAN");
        COLOURS.put(LightBlueConcrete.CONSOLE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(BlueConcrete.CONSOLE_BLUE.getKey(), "BLUE");
        COLOURS.put(PurpleConcrete.CONSOLE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(MagentaConcrete.CONSOLE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(PinkConcrete.CONSOLE_PINK.getKey(), "PINK");
        COLOURS.put(BrownConcrete.CONSOLE_BROWN.getKey(), "BROWN");
    }
}
