package me.eccentric_nz.TARDIS.flight;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FlightVisibility {

    private final TARDIS plugin;

    public FlightVisibility(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void hide(ArmorStand stand, Player player) {
        // get the item stack and save it
        ItemStack head = stand.getEquipment().getHelmet();
        plugin.getTrackerKeeper().getHiddenFlight().put(player.getUniqueId(), head);
        // stop animation
        FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
        plugin.getServer().getScheduler().cancelTask(frd.getAnimation());
        // send packet to player to hide police box
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getEntityId(), List.of(new Pair<>(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.EMPTY)));
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(equipmentPacket);
        if (stand.getCustomName() != null) {
            // hide the display name
            stand.setCustomNameVisible(false);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
    }

    public void show(Player player) {
        FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
        UUID uuid = frd.getChicken();
        if (uuid != null) {
            Entity chicken = plugin.getServer().getEntity(uuid);
            if (chicken != null && !chicken.getPassengers().isEmpty()) {
                Entity as = chicken.getPassengers().get(0);
                if (as instanceof ArmorStand stand) {
                    // restore the original item stack
                    net.minecraft.world.item.ItemStack head = CraftItemStack.asNMSCopy(plugin.getTrackerKeeper().getHiddenFlight().get(player.getUniqueId()));
                    ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getEntityId(), List.of(new Pair<>(EquipmentSlot.HEAD, head)));
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
                    connection.send(equipmentPacket);
                    if (stand.getCustomName() != null) {
                        // set name visible
                        stand.setCustomNameVisible(true);
                    }
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    // get the TARDIS
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", frd.getId());
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        // restart animation
                        int animation = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new FlyingAnimation(plugin, stand, player, tardis.getPreset().equals(ChameleonPreset.PANDORICA)), 5L, 3L);
                        // save flight data
                        plugin.getTrackerKeeper().getFlyingReturnLocation().put(player.getUniqueId(), new FlightReturnData(frd.getId(), frd.getLocation(), frd.getSound(), animation, chicken.getUniqueId(), stand.getUniqueId()));
                        // remove tracker
                        plugin.getTrackerKeeper().getHiddenFlight().remove(player.getUniqueId());
                    }
                }
            }
        }
    }
}
