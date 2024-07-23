package me.eccentric_nz.TARDIS.camera;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class TARDISCamera {

    private final TARDIS plugin;

    public TARDISCamera(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void viewExterior(Player player, int id, boolean pandorica) {
        Location playerLocation = player.getLocation();
        TARDISCameraTracker.SPECTATING.put(player.getUniqueId(), new CameraLocation(playerLocation, id, playerLocation.getChunk().isForceLoaded()));
        TARDISCameraTracker.CAMERA_IN_USE.add(id);
        playerLocation.getChunk().addPluginChunkTicket(plugin);
        // get the TARDIS's current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.debug("No current location");
            return;
        }
        // teleport player to exterior
        Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        player.teleport(location);
        // get the armour stand
        for (Entity e : location.getWorld().getNearbyEntities(location, 1, 1, 1, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
            if (e instanceof ArmorStand stand) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    //set the model to the bigger flying one
                    EntityEquipment ee = stand.getEquipment();
                    ItemStack is = ee.getHelmet();
                    ItemMeta im = is.getItemMeta();
                    im.setCustomModelData((pandorica ? 1008 : 1005));
                    is.setItemMeta(im);
                    ee.setHelmet(is);
                    // hide player from themselves
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                    stand.addPassenger(player);
                }, 2L);
            }
        }
    }

    public void stopViewing(Player player, ArmorStand stand) {
        UUID uuid = player.getUniqueId();
        CameraLocation data = TARDISCameraTracker.SPECTATING.get(uuid);
        if (data != null) {
            // stop force loading chunk
            player.getLocation().getChunk().removePluginChunkTicket(plugin);
            // reset police box model
            EntityEquipment ee = stand.getEquipment();
            ItemStack is = ee.getHelmet();
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(1001);
            is.setItemMeta(im);
            ee.setHelmet(is);
            // teleport player to interior
            Location interior = data.getLocation();
            while (!interior.getChunk().isLoaded()) {
                interior.getChunk().load();
            }
            player.teleport(interior);
            // remove invisibility
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            // add player to travellers
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("tardis_id", data.getId());
            sett.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("travellers", sett);
            TARDISCameraTracker.SPECTATING.remove(uuid);
            TARDISCameraTracker.CAMERA_IN_USE.remove(data.getId());
            if (data.isForceLoaded()) {
                interior.getChunk().addPluginChunkTicket(plugin);
            } else {
                interior.getChunk().removePluginChunkTicket(plugin);
            }
        }
    }
}
