package me.eccentric_nz.TARDIS.particles;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.List;

public class ParticleColour {

    public static List<ChatColor> colours = List.of(ChatColor.WHITE, ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.YELLOW);

    public static ChatColor fromColor(Color color) {
        if (color == Color.WHITE) { return ChatColor.WHITE; }
        if (color == Color.AQUA) { return ChatColor.AQUA; }
        if (color == Color.BLACK) { return ChatColor.BLACK; }
        if (color == Color.BLUE) { return ChatColor.BLUE; }
        if (color == Color.TEAL) { return ChatColor.DARK_AQUA; }
        if (color == Color.NAVY) { return ChatColor.DARK_BLUE; }
        if (color == Color.GRAY) { return ChatColor.DARK_GRAY; }
        if (color == Color.GREEN) { return ChatColor.DARK_GREEN; }
        if (color == Color.PURPLE) { return ChatColor.DARK_PURPLE; }
        if (color == Color.MAROON) { return ChatColor.DARK_RED;}
        if (color == Color.ORANGE) { return ChatColor.GOLD; }
        if (color == Color.SILVER) { return ChatColor.GRAY; }
        if (color == Color.LIME) { return ChatColor.GREEN; }
        if (color == Color.FUCHSIA) { return ChatColor.LIGHT_PURPLE; }
        if (color == Color.RED) { return ChatColor.RED; }
        if (color == Color.YELLOW) { return ChatColor.YELLOW; }
        return ChatColor.UNDERLINE;
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

    public static ChatColor fromString(String str) {
        String clean = ChatColor.stripColor(str);
        switch (clean) {
            case "White" -> { return ChatColor.WHITE; }
            case "Aqua" -> { return ChatColor.AQUA; }
            case "Black" -> { return ChatColor.BLACK; }
            case "Blue" -> { return ChatColor.BLUE; }
            case "Dark Aqua" -> { return ChatColor.DARK_AQUA; }
            case "Dark Blue" -> { return ChatColor.DARK_BLUE; }
            case "Dark Gray" -> { return ChatColor.DARK_GRAY; }
            case "Dark Green" -> { return ChatColor.DARK_GREEN; }
            case "Dark Purple" -> { return ChatColor.DARK_PURPLE; }
            case "Dark Red" -> { return ChatColor.DARK_RED;}
            case "Gold" -> { return ChatColor.GOLD; }
            case "Gray" -> { return ChatColor.GRAY; }
            case "Green" -> { return ChatColor.GREEN; }
            case "Light Purple" -> { return ChatColor.LIGHT_PURPLE; }
            case "Red" -> { return ChatColor.RED; }
            case "Yellow" -> { return ChatColor.YELLOW; }
            default -> { return ChatColor.UNDERLINE; }
        }
    }

    public static String toString(ChatColor chatColor) {
        switch (chatColor) {
            case WHITE -> { return "White"; }
            case AQUA -> { return "Aqua"; }
            case BLACK -> { return "Black"; }
            case BLUE -> { return "Blue"; }
            case DARK_AQUA -> { return "Dark Aqua"; }
            case DARK_BLUE -> { return "Dark Blue"; }
            case DARK_GRAY -> { return "Dark Gray"; }
            case DARK_GREEN -> { return "Dark Green"; }
            case DARK_PURPLE -> { return "Dark Purple"; }
            case DARK_RED -> { return "Dark Red";}
            case GOLD -> { return "Gold"; }
            case GRAY -> { return "Gray"; }
            case GREEN -> { return "Green"; }
            case LIGHT_PURPLE -> { return "Light Purple"; }
            case RED -> { return "Red"; }
            case YELLOW -> { return "Yellow"; }
            default -> { return "Underline"; }
        }
    }
}

