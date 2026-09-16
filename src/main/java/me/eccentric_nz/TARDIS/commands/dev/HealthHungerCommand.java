package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HealthHungerCommand {

    private final TARDIS plugin;

    public HealthHungerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void set(Player player) {
        player.setHealth(0.5);
        player.setFoodLevel(0);
        player.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(5000, 1));
    }
}
