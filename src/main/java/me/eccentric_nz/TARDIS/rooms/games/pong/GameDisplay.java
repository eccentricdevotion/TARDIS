package me.eccentric_nz.TARDIS.rooms.games.pong;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;

public class GameDisplay {

    public static String create(Location location) {
        StringBuilder builder = new StringBuilder();
        for (char[] line : Lines.CANVAS) {
            String joined = new String(line); // char[] line = joined.toCharArray()
            location.add(0, -0.125d, 0);
            TextDisplay display = spawn(location, Component.text(joined));
            builder.append(display.getUniqueId()).append(":");
        }
        location.add(0, -0.125d, 0);
        TextDisplay display = spawn(location, Component.text("Score", NamedTextColor.GOLD));
        builder.append(display.getUniqueId());
        return builder.toString();
    }

    private static TextDisplay spawn(Location location, Component component) {
        TextDisplay display = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        display.setBackgroundColor(Color.BLACK);
        display.setLineWidth(300);
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.text(component);
        display.setPersistent(true);
        return display;
    }
}
