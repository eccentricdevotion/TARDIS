package me.eccentric_nz.tardischunkgenerator.helpers;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class WeepingAngels {
    /**
     * Get TARDISWeepingAngelsAPI.
     *
     * @return the TARDISWeepingAngelsAPI
     */
    public static TARDISWeepingAngelsAPI getAPI() {
        Plugin p = Bukkit.getPluginManager().getPlugin("TARDISWeepingAngels");
        TARDISWeepingAngels twa = (TARDISWeepingAngels) p;
        return twa.getWeepingAngelsAPI();
    }

    public static boolean isDalek(Skeleton skeleton) {
        return skeleton.getPersistentDataContainer().has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER);
    }
}
