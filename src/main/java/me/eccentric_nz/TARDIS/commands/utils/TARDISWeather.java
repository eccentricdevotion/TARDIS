package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.enumeration.WEATHER;
import org.bukkit.World;

public class TARDISWeather {

    public static void setRain(World world) {
        world.setStorm(true);
    }

    public static void setThunder(World world) {
        world.setStorm(true);
        world.setThundering(true);
    }

    public static void setclear(World world) {
        world.setThundering(false);
        world.setStorm(false);
    }

    public static void setWeather(World world, WEATHER weather) {
        switch (weather) {
            case RAIN:
                setRain(world);
                break;
            case THUNDER:
                setThunder(world);
                break;
            default:
                setclear(world);
                break;
        }
    }
}
