package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.clock.ClockNetworkState;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DimensionTimeSyncListener implements Listener {

    private final TARDIS plugin;

    public DimensionTimeSyncListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World targetWorld = player.getWorld();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
            ServerLevel nmsLevel = nmsPlayer.level();
            // get game time
            long totalGameTime = nmsLevel.getGameTime();
            RegistryAccess registryAccess = nmsLevel.registryAccess();
            Identifier clockLocation = Identifier.fromNamespaceAndPath("tardis", targetWorld.getName().replace("tardis_", ""));
            var clockResourceKey = ResourceKey.create(Registries.WORLD_CLOCK, clockLocation);
            // setup variable to capture the Holder reference context
            AtomicReference<Holder<WorldClock>> resolvedClockHolder = new AtomicReference<>();
            registryAccess.lookup(Registries.WORLD_CLOCK).ifPresent(lookup -> {
                // get the world clock
                Optional<Holder.Reference<WorldClock>> holderOpt = lookup.get(clockResourceKey);
                holderOpt.ifPresent(worldClockReference -> {
                    resolvedClockHolder.set(holderOpt.get());
                    // get the ClockNetworkState
                    long dimensionDayTime = nmsLevel.getDefaultClockTime();
                    boolean standardCycleTicking = nmsLevel.getGameRules().get(GameRules.ADVANCE_TIME);
                    float timelineRate = standardCycleTicking ? 1.0F : 0.0F;
                    float partialTick = 0.0F;
                    ClockNetworkState clockNetworkState = new ClockNetworkState(dimensionDayTime, partialTick, timelineRate);
                    Map<Holder<WorldClock>, ClockNetworkState> clockUpdates = new HashMap<>();
                    clockUpdates.put(resolvedClockHolder.get(), clockNetworkState);
                    // construct and send time packet
                    ClientboundSetTimePacket timePacket = new ClientboundSetTimePacket(totalGameTime, clockUpdates);
                    nmsPlayer.connection.send(timePacket);
                });
            });
        }, 1L);
    }
}
