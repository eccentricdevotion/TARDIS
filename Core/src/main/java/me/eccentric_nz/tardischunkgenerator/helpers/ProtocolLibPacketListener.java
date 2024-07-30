package me.eccentric_nz.tardischunkgenerator.helpers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProtocolLibPacketListener {

    public void enable(TARDIS plugin) {
        plugin.debug("Using ProtocolLib for steer packet listening...");
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                Player player = e.getPlayer();
                Entity stand = player.getVehicle();
                if (stand != null && stand.getType() == EntityType.ARMOR_STAND) {
                    Entity chicken = stand.getVehicle();
                    if (chicken != null) {
                        float sideways = e.getPacket().getFloat().read(0);
                        float forward = e.getPacket().getFloat().read(1);
                        if (!e.getPacket().getBooleans().getValues().get(1)) { // shift key not pressed
                            // don't move if the chicken is on the ground
                            if (chicken.isOnGround()) {
                                chicken.setVelocity(new Vector(0, 0, 0));
                            } else {
                                Location playerLocation = player.getLocation();
                                float yaw = playerLocation.getYaw();
                                float pitch = playerLocation.getPitch();
                                chicken.setRotation(yaw, pitch);
                                stand.setRotation(yaw, pitch);
                                double radians = Math.toRadians(yaw);
                                double x = -forward * Math.sin(radians) + sideways * Math.cos(radians);
                                double z = forward * Math.cos(radians) + sideways * Math.sin(radians);
                                Vector velocity = (new Vector(x, 0.0D, z)).normalize().multiply(0.5D);
                                velocity.setY(chicken.getVelocity().getY());
                                if (!Double.isFinite(velocity.getX())) {
                                    velocity.setX(0);
                                }
                                if (!Double.isFinite(velocity.getZ())) {
                                    velocity.setZ(0);
                                }
                                if (!e.getPacket().getBooleans().getValues().get(0)) { // space bar not pressed
                                    if (pitch < 0) {
                                        // go up
                                        double up = Math.abs(pitch / 100.0d);
                                        velocity.setY(up);
                                    } else {
                                        double down = -Math.abs(pitch / 100.0d);
                                        velocity.setY(down);
                                    }
                                } else {
                                    velocity.setY(0);
                                }
                                velocity.checkFinite();
                                chicken.setVelocity(velocity);
                            }
                        } else {
                            chicken.setVelocity(new Vector(0, 0, 0));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                                // kill chicken
                                chicken.removePassenger(stand);
                                chicken.remove();
                                ArmorStand as = (ArmorStand) stand;
                                TARDIS.plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(player.getUniqueId());
                                // teleport player back to the TARDIS interior
                                new TARDISExteriorFlight(TARDIS.plugin).stopFlying(player, as);
                            });
                        }
                    }
                }
            }
        });
    }
}
