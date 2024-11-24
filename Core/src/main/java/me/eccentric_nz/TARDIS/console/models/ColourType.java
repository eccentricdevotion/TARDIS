package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.ConsoleBlock;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class ColourType {

    public static final HashMap<Material, NamespacedKey> LOOKUP = new HashMap<>();
    public static final HashMap<NamespacedKey, String> COLOURS = new HashMap<>();

    static {
        LOOKUP.put(Material.LIGHT_GRAY_CONCRETE_POWDER, ConsoleBlock.CONSOLE_LIGHT_GRAY.getKey());
        LOOKUP.put(Material.GRAY_CONCRETE_POWDER, ConsoleBlock.CONSOLE_GRAY.getKey());
        LOOKUP.put(Material.BLACK_CONCRETE_POWDER, ConsoleBlock.CONSOLE_BLACK.getKey());
        LOOKUP.put(Material.WHITE_CONCRETE_POWDER, ConsoleBlock.CONSOLE_WHITE.getKey());
        LOOKUP.put(Material.RED_CONCRETE_POWDER, ConsoleBlock.CONSOLE_RED.getKey());
        LOOKUP.put(Material.ORANGE_CONCRETE_POWDER, ConsoleBlock.CONSOLE_ORANGE.getKey());
        LOOKUP.put(Material.YELLOW_CONCRETE_POWDER, ConsoleBlock.CONSOLE_YELLOW.getKey());
        LOOKUP.put(Material.LIME_CONCRETE_POWDER, ConsoleBlock.CONSOLE_LIME.getKey());
        LOOKUP.put(Material.GREEN_CONCRETE_POWDER, ConsoleBlock.CONSOLE_GREEN.getKey());
        LOOKUP.put(Material.CYAN_CONCRETE_POWDER, ConsoleBlock.CONSOLE_CYAN.getKey());
        LOOKUP.put(Material.LIGHT_BLUE_CONCRETE_POWDER, ConsoleBlock.CONSOLE_LIGHT_BLUE.getKey());
        LOOKUP.put(Material.BLUE_CONCRETE_POWDER, ConsoleBlock.CONSOLE_BLUE.getKey());
        LOOKUP.put(Material.PURPLE_CONCRETE_POWDER, ConsoleBlock.CONSOLE_PURPLE.getKey());
        LOOKUP.put(Material.MAGENTA_CONCRETE_POWDER, ConsoleBlock.CONSOLE_MAGENTA.getKey());
        LOOKUP.put(Material.PINK_CONCRETE_POWDER, ConsoleBlock.CONSOLE_PINK.getKey());
        LOOKUP.put(Material.BROWN_CONCRETE_POWDER, ConsoleBlock.CONSOLE_BROWN.getKey());
        COLOURS.put(ConsoleBlock.CONSOLE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsoleBlock.CONSOLE_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsoleBlock.CONSOLE_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsoleBlock.CONSOLE_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsoleBlock.CONSOLE_RED.getKey(), "RED");
        COLOURS.put(ConsoleBlock.CONSOLE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsoleBlock.CONSOLE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsoleBlock.CONSOLE_LIME.getKey(), "LIME");
        COLOURS.put(ConsoleBlock.CONSOLE_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsoleBlock.CONSOLE_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsoleBlock.CONSOLE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsoleBlock.CONSOLE_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsoleBlock.CONSOLE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsoleBlock.CONSOLE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsoleBlock.CONSOLE_PINK.getKey(), "PINK");
        COLOURS.put(ConsoleBlock.CONSOLE_BROWN.getKey(), "BROWN");
    }
}
