package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRegenerations;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
 * Sometimes it's like playing pin the tail on the donkey during an earthquake.
 * If you come out the other side with the right number of eyes, that counts as a win.
 * - Missy
 */
public class Regenerator {

    private static void display(TARDIS plugin, Player player, int cmd, Skin skin) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta im = totem.getItemMeta();
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
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> display.addPassenger(player), 2L);
        // start regeneration particles
        RegenerationEmitter emitter = new RegenerationEmitter(plugin, player, player.getLocation(), yaw);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, 20L);
        emitter.setTaskID(task);
    }

    public void dev(TARDIS plugin, Player player, String[] args) {
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
            display(plugin, player, cmd, skin);
        }
    }

    public void processPlayer(TARDIS plugin, Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.regenerate")) {
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "NO_PERM_REGENERATION");
            return;
        }
        ResultSetRegenerations rsr = new ResultSetRegenerations(plugin);
        if (rsr.fromUUID(player.getUniqueId().toString())) {
            // does the player have any regenerations left?
            if (rsr.getCount() <= 0) {
                plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_COUNT");
                return;
            }
            // does the player have enough time lord energy?
            if (plugin.getRegenerationConfig().getBoolean("regeneration.artron.consume") && rsr.getArtronLevel() < plugin.getRegenerationConfig().getInt("regeneration.artron.amount")) {
                plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_ARTRON");
                return;
            }
            // get which regeneration this is
            int which = (plugin.getRegenerationConfig().getInt("regeneration.regens") + 1) - rsr.getCount();
            // trigger regeneration
            Skin skin = DoctorSkins.DOCTORS.get(which - 1);
            display(plugin, player, which, skin);
            // reduce regen count
            int reduced = rsr.getCount() - 1;
            plugin.getQueryFactory().doRegenerationDecrement(player.getUniqueId(), reduced);
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_REMAINING", reduced);
        }
    }
}
