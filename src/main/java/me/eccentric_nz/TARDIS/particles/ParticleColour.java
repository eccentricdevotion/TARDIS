package me.eccentric_nz.TARDIS.particles;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.List;

public class ParticleColour {

    public static List<NamedTextColor> colours = List.of(NamedTextColor.WHITE, NamedTextColor.AQUA, NamedTextColor.BLACK, NamedTextColor.BLUE, NamedTextColor.DARK_AQUA, NamedTextColor.DARK_BLUE, NamedTextColor.DARK_GRAY, NamedTextColor.DARK_GREEN, NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_RED, NamedTextColor.GOLD, NamedTextColor.GRAY, NamedTextColor.GREEN, NamedTextColor.LIGHT_PURPLE, NamedTextColor.RED, NamedTextColor.YELLOW);

    public static NamedTextColor fromColor(Color color) {
        if (color == Color.WHITE) { return NamedTextColor.WHITE; }
        if (color == Color.AQUA) { return NamedTextColor.AQUA; }
        if (color == Color.BLACK) { return NamedTextColor.BLACK; }
        if (color == Color.BLUE) { return NamedTextColor.BLUE; }
        if (color == Color.TEAL) { return NamedTextColor.DARK_AQUA; }
        if (color == Color.NAVY) { return NamedTextColor.DARK_BLUE; }
        if (color == Color.GRAY) { return NamedTextColor.DARK_GRAY; }
        if (color == Color.GREEN) { return NamedTextColor.DARK_GREEN; }
        if (color == Color.PURPLE) { return NamedTextColor.DARK_PURPLE; }
        if (color == Color.MAROON) { return NamedTextColor.DARK_RED;}
        if (color == Color.ORANGE) { return NamedTextColor.GOLD; }
        if (color == Color.SILVER) { return NamedTextColor.GRAY; }
        if (color == Color.LIME) { return NamedTextColor.GREEN; }
        if (color == Color.FUCHSIA) { return NamedTextColor.LIGHT_PURPLE; }
        if (color == Color.RED) { return NamedTextColor.RED; }
        if (color == Color.YELLOW) { return NamedTextColor.YELLOW; }
        return null;
    }

    public static Color fromDatabase(String colour) {
        switch (colour) {
            case "White" -> { return Color.WHITE; }
            case "Aqua" -> { return Color.AQUA; }
            case "Black" -> { return Color.BLACK; }
            case "Blue" -> { return Color.BLUE; }
            case "Dark Aqua" -> { return Color.TEAL; }
            case "Dark Blue" -> { return Color.NAVY; }
            case "Dark Gray" -> { return Color.GRAY; }
            case "Dark Green" -> { return Color.GREEN; }
            case "Dark Purple" -> { return Color.PURPLE; }
            case "Dark Red" -> { return Color.MAROON;}
            case "Gold" -> { return Color.ORANGE; }
            case "Gray" -> { return Color.SILVER; }
            case "Green" -> { return Color.LIME; }
            case "Light Purple" -> { return Color.FUCHSIA; }
            case "Red" -> { return Color.RED; }
            case "Yellow" -> { return Color.YELLOW; }
            default -> { return Color.OLIVE; }
        }
    }

    public static NamedTextColor fromString(String str) {
        switch (str) {
            case "White" -> { return NamedTextColor.WHITE; }
            case "Aqua" -> { return NamedTextColor.AQUA; }
            case "Black" -> { return NamedTextColor.BLACK; }
            case "Blue" -> { return NamedTextColor.BLUE; }
            case "Dark Aqua" -> { return NamedTextColor.DARK_AQUA; }
            case "Dark Blue" -> { return NamedTextColor.DARK_BLUE; }
            case "Dark Gray" -> { return NamedTextColor.DARK_GRAY; }
            case "Dark Green" -> { return NamedTextColor.DARK_GREEN; }
            case "Dark Purple" -> { return NamedTextColor.DARK_PURPLE; }
            case "Dark Red" -> { return NamedTextColor.DARK_RED;}
            case "Gold" -> { return NamedTextColor.GOLD; }
            case "Gray" -> { return NamedTextColor.GRAY; }
            case "Green" -> { return NamedTextColor.GREEN; }
            case "Light Purple" -> { return NamedTextColor.LIGHT_PURPLE; }
            case "Red" -> { return NamedTextColor.RED; }
            case "Yellow" -> { return NamedTextColor.YELLOW; }
            default -> { return null; }
        }
    }

    public static String toString(NamedTextColor textColor) {
        if (textColor == NamedTextColor.WHITE) { return "White"; }
        if (textColor == NamedTextColor.AQUA) { return "Aqua"; }
        if (textColor == NamedTextColor.BLACK) { return "Black"; }
        if (textColor == NamedTextColor.BLUE) { return "Blue"; }
        if (textColor == NamedTextColor.DARK_AQUA) { return "Dark Aqua"; }
        if (textColor == NamedTextColor.DARK_BLUE) { return "Dark Blue"; }
        if (textColor == NamedTextColor.DARK_GRAY) { return "Dark Gray"; }
        if (textColor == NamedTextColor.DARK_GREEN) { return "Dark Green"; }
        if (textColor == NamedTextColor.DARK_PURPLE) { return "Dark Purple"; }
        if (textColor == NamedTextColor.DARK_RED) { return "Dark Red";}
        if (textColor == NamedTextColor.GOLD) { return "Gold"; }
        if (textColor == NamedTextColor.GRAY) { return "Gray"; }
        if (textColor == NamedTextColor.GREEN) { return "Green"; }
        if (textColor == NamedTextColor.LIGHT_PURPLE) { return "Light Purple"; }
        if (textColor == NamedTextColor.RED) { return "Red"; }
        if (textColor == NamedTextColor.YELLOW) { return "Yellow"; }
        return null;
    }

    public static NamedTextColor fromChar(char c) {
        switch (c) {
            case '0'-> { return NamedTextColor.BLACK; }
            case '1'-> { return NamedTextColor.DARK_BLUE; }
            case '2'-> { return NamedTextColor.DARK_GREEN; }
            case '3'-> { return NamedTextColor.DARK_AQUA; }
            case '4'-> { return NamedTextColor.DARK_RED; }
            case '5'-> { return NamedTextColor.DARK_PURPLE; }
            case '6'-> { return NamedTextColor.GOLD; }
            case '7'-> { return NamedTextColor.GRAY; }
            case '8'-> { return NamedTextColor.DARK_GRAY; }
            case '9'-> { return NamedTextColor.BLUE; }
            case 'a'-> { return NamedTextColor.GREEN; }
            case 'b'-> { return NamedTextColor.AQUA; }
            case 'c'-> { return NamedTextColor.RED; }
            case 'd'-> { return NamedTextColor.LIGHT_PURPLE; }
            case 'e'-> { return NamedTextColor.YELLOW; }
            default -> { return NamedTextColor.WHITE; }
        }
    }
}

