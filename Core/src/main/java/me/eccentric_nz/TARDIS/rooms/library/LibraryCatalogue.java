package me.eccentric_nz.TARDIS.rooms.library;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

public class LibraryCatalogue {

    public void label(Location start) {
        for (EnchantmentShelf shelf : EnchantmentShelf.values()) {
            Block block = start.clone().add(shelf.getPosition()).getBlock().getRelative(shelf.getFacing());
            Location card = block.getLocation();
            float yaw;
            switch (shelf.getFacing()) {
                case SOUTH -> {
                    card.add(0.5, 0.5, 0.1);
                    yaw = 0;
                }
                case EAST -> {
                    card.add(0.1, 0.5, 0.5);
                    yaw = -90;
                }
                case NORTH -> {
                    card.add(0.5, 0.5, 0.9);
                    yaw = 180;
                }
                // WEST
                default -> {
                    card.add(0.9, 0.5, 0.5);
                    yaw = 90;
                }
            }
            for (int l = 1; l <= shelf.getMaxLevel(); l++) {
                Location text = card.clone().add(0, l, 0);
                TextDisplay display = (TextDisplay) start.getWorld().spawnEntity(text, EntityType.TEXT_DISPLAY);
                display.setText(shelf.getMinecraftName() + roman(l));
                display.setAlignment(TextDisplay.TextAlignment.CENTER);
                display.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                display.setBillboard(Display.Billboard.FIXED);
                display.setRotation(yaw, 0);
            }
        }
    }

    private String roman(int i) {
        String numeral = "";
        switch (i) {
            case 5 -> numeral = " V";
            case 4 -> numeral = " IV";
            case 3 -> numeral = " III";
            case 2 -> numeral = " II";
            default -> numeral = " I";
        }
        return numeral;
    }
}
