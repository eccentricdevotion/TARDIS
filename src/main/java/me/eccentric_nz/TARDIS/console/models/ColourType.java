package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodels.keys.ConsolePart;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsoleVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class ColourType {

    public static final HashMap<Material, NamespacedKey> LOOKUP = new HashMap<>();
    public static final HashMap<NamespacedKey, String> COLOURS = new HashMap<>();

    static {
        LOOKUP.put(Material.LIGHT_GRAY_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIGHT_GRAY.getKey());
        LOOKUP.put(Material.GRAY_CONCRETE_POWDER, ConsoleVariant.CONSOLE_GRAY.getKey());
        LOOKUP.put(Material.BLACK_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BLACK.getKey());
        LOOKUP.put(Material.WHITE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_WHITE.getKey());
        LOOKUP.put(Material.RED_CONCRETE_POWDER, ConsoleVariant.CONSOLE_RED.getKey());
        LOOKUP.put(Material.ORANGE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_ORANGE.getKey());
        LOOKUP.put(Material.YELLOW_CONCRETE_POWDER, ConsoleVariant.CONSOLE_YELLOW.getKey());
        LOOKUP.put(Material.LIME_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIME.getKey());
        LOOKUP.put(Material.GREEN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_GREEN.getKey());
        LOOKUP.put(Material.CYAN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_CYAN.getKey());
        LOOKUP.put(Material.LIGHT_BLUE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIGHT_BLUE.getKey());
        LOOKUP.put(Material.BLUE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BLUE.getKey());
        LOOKUP.put(Material.PURPLE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_PURPLE.getKey());
        LOOKUP.put(Material.MAGENTA_CONCRETE_POWDER, ConsoleVariant.CONSOLE_MAGENTA.getKey());
        LOOKUP.put(Material.PINK_CONCRETE_POWDER, ConsoleVariant.CONSOLE_PINK.getKey());
        LOOKUP.put(Material.BROWN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BROWN.getKey());
        COLOURS.put(ConsolePart.CONSOLE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_RUSTIC.getKey(), "RUSTIC");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_RUSTIC.getKey(), "RUSTIC");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_RUSTIC.getKey(), "RUSTIC");
    }
}
