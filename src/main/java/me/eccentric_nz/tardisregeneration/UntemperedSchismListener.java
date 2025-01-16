package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRegenerations;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UntemperedSchismListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, Location> untempered = new HashMap<>();
    private final Set<Vector> vectorsEW = new HashSet<>();
    private final Set<Vector> vectorsNS = new HashSet<>();

    public UntemperedSchismListener(TARDIS plugin) {
        this.plugin = plugin;
        vectorsEW.add(new Vector(0, 1, 0));
        vectorsEW.add(new Vector(0, 2, 0));
        vectorsEW.add(new Vector(-1, 0, 0));
        vectorsEW.add(new Vector(-1, 1, 0));
        vectorsEW.add(new Vector(-1, 2, 0));
        vectorsEW.add(new Vector(1, 0, 0));
        vectorsEW.add(new Vector(1, 1, 0));
        vectorsEW.add(new Vector(1, 2, 0));
        vectorsNS.add(new Vector(0, 1, 0));
        vectorsNS.add(new Vector(0, 2, 0));
        vectorsNS.add(new Vector(0, 0, -1));
        vectorsNS.add(new Vector(0, 1, -1));
        vectorsNS.add(new Vector(0, 2, -1));
        vectorsNS.add(new Vector(0, 0, 1));
        vectorsNS.add(new Vector(0, 1, 1));
        vectorsNS.add(new Vector(0, 2, 1));
    }

    @EventHandler
    public void onUntemperedSchismPlace(BlockPlaceEvent event) {
        ItemStack schism = event.getItemInHand();
        if (!UntemperedSchism.is(schism)) {
            return;
        }
        Block block = event.getBlock();
        Location location = block.getLocation();
        Player player = event.getPlayer();
        // check for room for 3x3 portal
        BlockFace facing = player.getFacing();
        if (!hasSpace(location, facing == BlockFace.EAST || facing == BlockFace.WEST ? vectorsEW : vectorsNS)) {
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "UNTEMPERED_SCHISM_SPACE");
            event.setCancelled(true);
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            block.setType(Material.AIR);
            // make a schism
            ItemDisplay display = TARDISDisplayItemUtils.set(TARDISDisplayItem.UNTEMPERED_SCHISM, block, -1);
            float yaw = DoorUtility.getLookAtYaw(player);
            // set display rotation
            display.setRotation(yaw, 0);
            untempered.put(player.getUniqueId(), location);
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "UNTEMPERED_SCHISM_WALK");
        }, 1L);
    }

    @EventHandler
    public void onEnterUntemperedSchism(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!untempered.containsKey(uuid)) {
            return;
        }
        // get block to
        Location to = event.getTo();
        if (!matchesLocation(to, untempered.get(uuid))) {
            return;
        }
        // check they have no regenerations left
        ResultSetRegenerations rsr = new ResultSetRegenerations(plugin);
        if (rsr.fromUUID(uuid.toString()) && rsr.getCount() > 0) {
            plugin.getMessenger().send(event.getPlayer(), TardisModule.REGENERATION, "UNTEMPERED_SCHISM_LEFT", rsr.getCount());
            // drop a block
            to.getWorld().dropItemNaturally(to, UntemperedSchism.create());
        } else {
            // explode untempered schism
            to.getWorld().createExplosion(to, 0, false, false);
            // reset regeneration count
            int reset = plugin.getRegenerationConfig().getInt("regenerations", 15);
            plugin.getQueryFactory().setRegenerationCount(uuid, reset);
            plugin.getMessenger().send(event.getPlayer(), TardisModule.REGENERATION, "UNTEMPERED_SCHISM_RESET", reset);
        }
        // remove item display, interaction and light
        Block block = to.getBlock();
        TARDISDisplayItemUtils.remove(block);
        block.setType(Material.AIR);
        untempered.remove(uuid);
    }

    private boolean matchesLocation(Location to, Location location) {
        return (to.getBlockX() == location.getBlockX() && to.getBlockY() == location.getBlockY() && to.getBlockZ() == location.getBlockZ());
    }

    private boolean hasSpace(Location location, Set<Vector> vectors) {
        for (Vector v : vectors) {
            location.add(v);
            if (!location.getBlock().getType().isAir()) {
                return false;
            }
            location.subtract(v);
        }
        return true;
    }
}
