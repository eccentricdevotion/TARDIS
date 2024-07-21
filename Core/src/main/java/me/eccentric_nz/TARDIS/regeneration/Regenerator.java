package me.eccentric_nz.TARDIS.regeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.skins.DoctorSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Sometimes it's like playing pin the tail on the donkey during an earthquake. If you come out the other side with the
 * right number of eyes, that counts as a win. Missy
 */
public class Regenerator {

    public void dev(TARDIS plugin, Player player, String[] args) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta im = totem.getItemMeta();
        int cmd;
        Skin skin;
        int random = TARDISConstants.RANDOM.nextInt(DoctorSkins.DOCTORS.size());
        if (args.length == 1) {
            cmd = random + 1001;
            skin = DoctorSkins.DOCTORS.get(random);
        } else {
            cmd = TARDISNumberParsers.parseInt(args[1]) + 1000;
            skin = DoctorSkins.DOCTORS.get(cmd - 1);
        }
        if (cmd != 999) {
            // hide player
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.canSee(player)) {
                    p.hidePlayer(plugin, player);
                }
            }
            im.setCustomModelData(cmd);
            totem.setItemMeta(im);
            // get rotation
            float yaw = Location.normalizeYaw(player.getLocation().getYaw());
            // spawn a display entity
            ItemDisplay display = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation().clone().add(0, 0.725, 0), EntityType.ITEM_DISPLAY);
            display.setItemStack(totem);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            display.setRotation(yaw, 0);
            // set player skin
            plugin.getSkinChanger().set(player, skin);
            // make player smaller
            player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.5d);
            // make player invisible
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 1, false, false, false));
            // prevent player from moving
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()-> display.addPassenger(player), 2L);
            // start regeneration particles
            RegenerationEmitter emitter = new RegenerationEmitter(plugin, player, player.getLocation(), yaw);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, 20L);
            emitter.setTaskID(task);
        }
    }
}
