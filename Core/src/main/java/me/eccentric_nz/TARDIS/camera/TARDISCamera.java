package me.eccentric_nz.TARDIS.camera;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.GrayStainedGlassPane;
import me.eccentric_nz.TARDIS.custommodeldata.keys.LeatherHorseArmor;
import me.eccentric_nz.TARDIS.custommodeldata.keys.PoliceBox;
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
                        case BLACK_DYE -> PoliceBox.BLACK_FLYING.getKey();
                        case RED_DYE -> PoliceBox.RED_FLYING.getKey();
                        case BROWN_DYE -> PoliceBox.BROWN_FLYING.getKey();
                        case GREEN_DYE -> PoliceBox.GREEN_FLYING.getKey();
                        case PURPLE_DYE -> PoliceBox.PURPLE_FLYING.getKey();
                        case CYAN_DYE -> PoliceBox.CYAN_FLYING.getKey();
                        case LIGHT_GRAY_DYE -> PoliceBox.LIGHT_GRAY_FLYING.getKey();
                        case GRAY_DYE -> PoliceBox.GRAY_FLYING.getKey();
                        case PINK_DYE -> PoliceBox.PINK_FLYING.getKey();
                        case LIME_DYE -> PoliceBox.LIME_FLYING.getKey();
                        case YELLOW_DYE -> PoliceBox.YELLOW_FLYING.getKey();
                        case LIGHT_BLUE_DYE -> PoliceBox.LIGHT_BLUE_FLYING.getKey();
                        case MAGENTA_DYE -> PoliceBox.MAGENTA_FLYING.getKey();
                        case ORANGE_DYE -> PoliceBox.ORANGE_FLYING.getKey();
                        case WHITE_DYE -> PoliceBox.WHITE_FLYING.getKey();
                        case CYAN_STAINED_GLASS_PANE -> PoliceBox.TENNANT_FLYING.getKey();
                        case LEATHER_HORSE_ARMOR -> LeatherHorseArmor.TINTED_FLYING_0.getKey();
                        case WOLF_SPAWN_EGG -> PoliceBox.BAD_WOLF_FLYING.getKey();
                        case ENDER_PEARL -> PoliceBox.PANDORICA_FLYING.getKey();
                        default -> null; // don't change for WEEPING_ANGEL or CUSTOM
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
                case BLACK_DYE -> PoliceBox.BLACK.getKey();
                case RED_DYE -> PoliceBox.RED.getKey();
                case BROWN_DYE -> PoliceBox.BROWN.getKey();
                case GREEN_DYE -> PoliceBox.GREEN.getKey();
                case BLUE_DYE -> PoliceBox.BLUE.getKey();
                case PURPLE_DYE -> PoliceBox.PURPLE.getKey();
                case CYAN_DYE -> PoliceBox.CYAN.getKey();
                case LIGHT_GRAY_DYE -> PoliceBox.LIGHT_GRAY.getKey();
                case GRAY_DYE -> PoliceBox.GRAY.getKey();
                case PINK_DYE -> PoliceBox.PINK.getKey();
                case LIME_DYE -> PoliceBox.LIME.getKey();
                case YELLOW_DYE -> PoliceBox.YELLOW.getKey();
                case LIGHT_BLUE_DYE -> PoliceBox.LIGHT_BLUE.getKey();
                case MAGENTA_DYE -> PoliceBox.MAGENTA.getKey();
                case ORANGE_DYE -> PoliceBox.ORANGE.getKey();
                case WHITE_DYE -> PoliceBox.WHITE.getKey();
                case CYAN_STAINED_GLASS_PANE -> PoliceBox.TENNANT.getKey();
                case LEATHER_HORSE_ARMOR -> LeatherHorseArmor.TARDIS_TINTED.getKey();
                case WOLF_SPAWN_EGG -> PoliceBox.BAD_WOLF_CLOSED.getKey();
                case ENDER_PEARL -> PoliceBox.PANDORICA.getKey();
                case GRAY_STAINED_GLASS_PANE -> GrayStainedGlassPane.WEEPING_ANGEL.getKey();
                default -> null; // don't change for WEEPING_ANGEL or CUSTOM
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
