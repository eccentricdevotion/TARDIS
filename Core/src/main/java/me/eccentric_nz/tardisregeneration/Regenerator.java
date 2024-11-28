package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.keys.TotemOfUndying;
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

import java.util.HashMap;

/**
 * Sometimes it's like playing pin the tail on the donkey during an earthquake. If you come out the other side with the
 * right number of eyes, that counts as a win. - Missy
 */
public class Regenerator {

    private static void display(TARDIS plugin, Player player, Skin skin) {
        // make player invulnerable
        player.setInvulnerable(true);
        // create the regeneration item model
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta im = totem.getItemMeta();
        // hide player
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.hidePlayer(plugin, player);
            }
        }
        im.setItemModel(TotemOfUndying.valueOf(skin.toString()).getKey());
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
        player.getAttribute(Attribute.SCALE).setBaseValue(0.5d);
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
        Skin skin;
        int random = TARDISConstants.RANDOM.nextInt(DoctorSkins.DOCTORS.size());
        if (args.length == 1) {
            skin = DoctorSkins.DOCTORS.get(random);
        } else {
            skin = DoctorSkins.DOCTORS.get(TARDISNumberParsers.parseInt(args[1]));
        }
        display(plugin, player, skin);
    }

    public void processPlayer(TARDIS plugin, Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.regenerate")) {
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "NO_PERM_REGENERATION");
            return;
        }
        String uuid = player.getUniqueId().toString();
        ResultSetRegenerations rsr = new ResultSetRegenerations(plugin);
        if (rsr.fromUUID(uuid)) {
            // is the player blocking regenerations?
            if (rsr.isRegenBlockOn()) {
                plugin.getMessenger().sendColouredCommand(player, "REGENERATION_BLOCKED", "/regeneration block off", plugin);
                return;
            }
            // does the player have any regenerations left?
            int playerRemaining = rsr.getCount();
            if (playerRemaining <= 0) {
                plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_COUNT");
                return;
            }
            // does the player have enough time lord energy?
            if (plugin.getRegenerationConfig().getBoolean("artron.consume")) {
                int cost = plugin.getRegenerationConfig().getInt("artron.amount");
                if (rsr.getArtronLevel() < cost) {
                    plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_ARTRON");
                    return;
                } else {
                    // take the energy
                    HashMap<String, Object> wheretl = new HashMap<>();
                    wheretl.put("uuid", uuid);
                    plugin.getQueryFactory().alterEnergyLevel("player_prefs", -cost, wheretl, player);
                }
            }
            int maxRegenerations = plugin.getRegenerationConfig().getInt("regenerations");
            int decrement = 1;
            // player regenerations default is 15, but configured max regenerations may be less e.g. 12
            if (playerRemaining > maxRegenerations) {
                decrement = (playerRemaining - maxRegenerations) + 1;
            }
            // get which regeneration this is
            int which = maxRegenerations - playerRemaining;
            if (which < 0 || which > 15) {
                which = 0;
            }
            // trigger regeneration
            Skin skin = DoctorSkins.DOCTORS.get(which);
            display(plugin, player, skin);
            // reduce regeneration count, taking into account disparity between configured and default
            int reduced = playerRemaining - decrement;
            plugin.getQueryFactory().setRegenerationCount(player.getUniqueId(), reduced);
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_REMAINING", reduced);
        }
    }
}
