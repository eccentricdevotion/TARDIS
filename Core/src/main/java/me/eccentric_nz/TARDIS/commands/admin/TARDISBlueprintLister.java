package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TARDISBlueprintLister {

    private final TARDIS plugin;

    public TARDISBlueprintLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender, String type) {
        String message = "%s -> %s";
        List<String> messages = new ArrayList<>();
        switch (type.toUpperCase(Locale.ROOT)) {
            case "BASE" -> {
                for (BlueprintBase base : BlueprintBase.values()) {
                    messages.add(String.format(message, base.toString(), base.getPermission()));
                }
            }
            case "BLASTER" -> {
                for (BlueprintBlaster blaster : BlueprintBlaster.values()) {
                    messages.add(String.format(message, blaster.toString(), blaster.getPermission()));
                }
            }
            case "CONSOLE" -> {
                for (BlueprintConsole console : BlueprintConsole.values()) {
                    messages.add(String.format(message, console.toString(), console.getPermission()));
                }
            }
            case "FEATURE" -> {
                for (BlueprintFeature feature : BlueprintFeature.values()) {
                    messages.add(String.format(message, feature.toString(), feature.getPermission()));
                }
            }
            case "PRESET" -> {
                for (BlueprintPreset preset : BlueprintPreset.values()) {
                    messages.add(String.format(message, preset.toString(), preset.getPermission()));
                }
            }
            case "ROOM" -> {
                for (BlueprintRoom room : BlueprintRoom.values()) {
                    messages.add(String.format(message, room.toString(), room.getPermission()));
                }
            }
            case "SHOP" -> {
                for (BlueprintShop shop : BlueprintShop.values()) {
                    messages.add(String.format(message, shop.toString(), shop.getPermission()));
                }
            }
            case "SONIC" -> {
                for (BlueprintSonic sonic : BlueprintSonic.values()) {
                    messages.add(String.format(message, sonic.toString(), sonic.getPermission()));
                }
            }
            case "TRAVEL" -> {
                for (BlueprintTravel travel : BlueprintTravel.values()) {
                    messages.add(String.format(message, travel.toString(), travel.getPermission()));
                }
            }
            case "VORTEX_MANIPULATOR" -> {
                for (BlueprintVortexManipulator manipulator : BlueprintVortexManipulator.values()) {
                    messages.add(String.format(message, manipulator.toString(), manipulator.getPermission()));
                }
            }
            case "WEEPING_ANGELS" -> {
                for (BlueprintWeepingAngels angel : BlueprintWeepingAngels.values()) {
                    messages.add(String.format(message, angel.toString(), angel.getPermission()));
                }
            }
            default -> {
                // do nothing
            }
        }
        if (!messages.isEmpty()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BLUEPRINTS_LIST", type.toUpperCase(Locale.ROOT));
            for (String s : messages) {
                sender.sendMessage(s);
            }
        }
    }
}
