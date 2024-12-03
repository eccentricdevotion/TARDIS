package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FlightVisibility {

    private final TARDIS plugin;

    public FlightVisibility(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void hide(ArmorStand stand, Player player) {
        ItemDisplay display = (ItemDisplay) player.getPassengers().getFirst();
        // get the item stack and save it
        ItemStack head = display.getItemStack();
        plugin.getTrackerKeeper().getHiddenFlight().put(player.getUniqueId(), head);
        // stop animation
        FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
        plugin.getServer().getScheduler().cancelTask(frd.getAnimation());
        // set item display to invisible to hide police box
        player.eject();
        display.remove();
        if (stand.getCustomName() != null) {
            // hide the display name
            stand.setCustomNameVisible(false);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
    }

    public void show(Player player) {
        FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
        UUID uuid = frd.getStand();
        Entity as = plugin.getServer().getEntity(uuid);
        if (as instanceof ArmorStand stand) {
            if (stand.getCustomName() != null) {
                // set name visible
                stand.setCustomNameVisible(true);
            }
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            ItemStack box = plugin.getTrackerKeeper().getHiddenFlight().get(player.getUniqueId());
            ItemDisplay display = VehicleUtility.getItemDisplay(player, box, switch (box.getType()) {
                case ENDER_PEARL -> 1.5f;
                case GRAY_STAINED_GLASS_PANE -> 1.66f;
                default -> 1.75f;
            });
            // restart animation
            int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, 40), 5L, 40L);
            // save flight data
            plugin.getTrackerKeeper().getFlyingReturnLocation().put(player.getUniqueId(), new FlightReturnData(frd.getId(), frd.getLocation(), frd.getSound(), animation, stand.getUniqueId(), display.getUniqueId()));
            // remove tracker
            plugin.getTrackerKeeper().getHiddenFlight().remove(player.getUniqueId());
        }

    }
}
