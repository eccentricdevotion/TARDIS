package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.DoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Nautilus;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class NautilusEjector {

    public void transport(TARDIS plugin, Nautilus inner_nautilus, int id, Player player) {
        // get exit location
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("tardis_id", id);
        whered.put("door_type", 1);
        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
        if (rsd.resultSet() && rsd.isLocked()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
            return;
        }
        // get the exit location
        TARDISDoorLocation dl = DoorListener.getDoor(0, id);
        Location l = dl.getL();
        // set the entity's direction as you would for a player when exiting
        switch (dl.getD()) {
            case NORTH -> {
                l.setZ(l.getZ() + 2.5f);
                l.setYaw(0.0f);
            }
            case WEST -> {
                l.setX(l.getX() + 2.5f);
                l.setYaw(270.0f);
            }
            case SOUTH -> {
                l.setZ(l.getZ() - 2.5f);
                l.setYaw(180.0f);
            }
            default -> {
                l.setX(l.getX() - 2.5f);
                l.setYaw(90.0f);
            }
        }
        World world = l.getWorld();
        // check there is a water block for the nautilus to be teleported to
        boolean water = false;
        while (!world.getChunkAt(l).isLoaded()) {
            world.getChunkAt(l).load();
        }
        for (BlockFace face : plugin.getGeneralKeeper().getSurrounding()) {
            if (l.getBlock().getRelative(BlockFace.DOWN).getRelative(face).getType() == Material.WATER) {
                water = true;
                break;
            }
        }
        if (!water) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NAUTILUS_WATER");
            return;
        }
        // eject player
        if (inner_nautilus.eject()) {
            Nautilus outer_nautilus = (Nautilus) l.getWorld().spawnEntity(l, EntityType.NAUTILUS);
            // age / baby
            outer_nautilus.setTicksLived(inner_nautilus.getTicksLived());
            if (!inner_nautilus.isAdult()) {
                outer_nautilus.setBaby();
            }
            // tamed / owner
            outer_nautilus.setTamed(inner_nautilus.isTamed());
            outer_nautilus.setOwner(inner_nautilus.getOwner());
            // saddled / armour
            outer_nautilus.getInventory().setSaddle(inner_nautilus.getInventory().getSaddle());
            outer_nautilus.getInventory().setArmor(inner_nautilus.getInventory().getArmor());
            // name
            Component nautilus_name = inner_nautilus.customName();
            if (nautilus_name != null) {
                outer_nautilus.customName(nautilus_name);
            }
            inner_nautilus.remove();
            // teleport player and remove from travellers table
            plugin.getGeneralKeeper().getDoorListener().movePlayer(player, l, true, player.getWorld(), false, 0, true, false);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doDelete("travellers", where);
            // set player as passenger
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> outer_nautilus.addPassenger(player), 10L);
        }
    }
}
