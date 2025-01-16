package me.eccentric_nz.TARDIS.camera;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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
                    NamespacedKey viewing = switch (is.getType()) {
                        case BLACK_DYE -> ChameleonVariant.BLACK_CAMERA.getKey();
                        case RED_DYE -> ChameleonVariant.RED_CAMERA.getKey();
                        case BROWN_DYE -> ChameleonVariant.BROWN_CAMERA.getKey();
                        case GREEN_DYE -> ChameleonVariant.GREEN_CAMERA.getKey();
                        case PURPLE_DYE -> ChameleonVariant.PURPLE_CAMERA.getKey();
                        case CYAN_DYE -> ChameleonVariant.CYAN_CAMERA.getKey();
                        case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_CAMERA.getKey();
                        case GRAY_DYE -> ChameleonVariant.GRAY_CAMERA.getKey();
                        case PINK_DYE -> ChameleonVariant.PINK_CAMERA.getKey();
                        case LIME_DYE -> ChameleonVariant.LIME_CAMERA.getKey();
                        case YELLOW_DYE -> ChameleonVariant.YELLOW_CAMERA.getKey();
                        case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_CAMERA.getKey();
                        case MAGENTA_DYE -> ChameleonVariant.MAGENTA_CAMERA.getKey();
                        case ORANGE_DYE -> ChameleonVariant.ORANGE_CAMERA.getKey();
                        case WHITE_DYE -> ChameleonVariant.WHITE_CAMERA.getKey();
                        case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_CAMERA.getKey();
                        case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_CAMERA.getKey();
                        case WOLF_SPAWN_EGG -> ChameleonVariant.BAD_WOLF_CAMERA.getKey();
                        default -> null; // don't change for PANDORICA, WEEPING_ANGEL or CUSTOM
                    };
                    if (viewing != null) {
                        im.setItemModel(viewing);
                    }
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
            NamespacedKey notviewing = switch (is.getType()) {
                case BLACK_DYE -> ChameleonVariant.BLACK_CLOSED.getKey();
                case RED_DYE -> ChameleonVariant.RED_CLOSED.getKey();
                case BROWN_DYE -> ChameleonVariant.BROWN_CLOSED.getKey();
                case GREEN_DYE -> ChameleonVariant.GREEN_CLOSED.getKey();
                case BLUE_DYE -> ChameleonVariant.BLUE_CLOSED.getKey();
                case PURPLE_DYE -> ChameleonVariant.PURPLE_CLOSED.getKey();
                case CYAN_DYE -> ChameleonVariant.CYAN_CLOSED.getKey();
                case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_CLOSED.getKey();
                case GRAY_DYE -> ChameleonVariant.GRAY_CLOSED.getKey();
                case PINK_DYE -> ChameleonVariant.PINK_CLOSED.getKey();
                case LIME_DYE -> ChameleonVariant.LIME_CLOSED.getKey();
                case YELLOW_DYE -> ChameleonVariant.YELLOW_CLOSED.getKey();
                case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_CLOSED.getKey();
                case MAGENTA_DYE -> ChameleonVariant.MAGENTA_CLOSED.getKey();
                case ORANGE_DYE -> ChameleonVariant.ORANGE_CLOSED.getKey();
                case WHITE_DYE -> ChameleonVariant.WHITE_CLOSED.getKey();
                case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_CLOSED.getKey();
                case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_CLOSED.getKey();
                case WOLF_SPAWN_EGG -> ChameleonVariant.BAD_WOLF_CLOSED.getKey();
                default -> null; // don't change for PANDORICA, WEEPING_ANGEL or CUSTOM
            };
            if (notviewing != null) {
                im.setItemModel(notviewing);
            }
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
