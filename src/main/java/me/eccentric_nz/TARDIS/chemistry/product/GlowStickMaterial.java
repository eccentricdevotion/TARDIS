package me.eccentric_nz.TARDIS.chemistry.product;

import org.bukkit.Material;

public class GlowStickMaterial {

    public static boolean isCorrectMaterial(Material material) {
        switch (material) {
            case WHITE_STAINED_GLASS_PANE:
            case ORANGE_STAINED_GLASS_PANE:
            case MAGENTA_STAINED_GLASS_PANE:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case YELLOW_STAINED_GLASS_PANE:
            case LIME_STAINED_GLASS_PANE:
            case PINK_STAINED_GLASS_PANE:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case BLUE_STAINED_GLASS_PANE:
            case PURPLE_STAINED_GLASS_PANE:
            case GREEN_STAINED_GLASS_PANE:
            case BROWN_STAINED_GLASS_PANE:
            case CYAN_STAINED_GLASS_PANE:
            case RED_STAINED_GLASS_PANE:
                return true;
            default:
                return false;
        }
    }
}
