package me.eccentric_nz.TARDIS.mobfarming;


import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class TARDISLeashListener implements Listener {

    private final TARDIS plugin;

    public TARDISLeashListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeashCut(PlayerUnleashEntityEvent event) {
        // check it is in the TARDIS world
        if (!plugin.getUtils().inTARDISWorld(event.getPlayer())) {
            return;
        }
        if (!(event.getEntity() instanceof Slime slime)) {
            return;
        }
        if (slime.hasAI()) {
            return;
        }
        if (slime.getPassengers().isEmpty()) {
            return;
        }
        if (!(slime.getPassengers().getFirst() instanceof ItemDisplay)) {
            return;
        }
        PersistentDataContainer pdc = slime.getPersistentDataContainer();
        if (!pdc.has(HappyGhastUtils.SLOT, PersistentDataType.INTEGER) || !pdc.has(HappyGhastUtils.ID, PersistentDataType.INTEGER)) {
            return;
        }
        event.setDropLeash(false);
        // get slot
        HappyGhastUtils.setSlotUnoccupied(plugin, pdc.get(HappyGhastUtils.SLOT, PersistentDataType.INTEGER), pdc.get(HappyGhastUtils.ID, PersistentDataType.INTEGER));
        // get the TARDIS the event occurred in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", event.getPlayer().getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            event.setCancelled(true);
        }
        int id = rst.getTardis_id();
        // get the exit location
        TARDISDoorLocation dl = TARDISDoorListener.getDoor(0, id);
        Location l = dl.getL();
        // set the entity's direction as you would for a player when exiting
        switch (dl.getD()) {
            case NORTH -> {
                l.setZ(l.getZ() + 4.5f);
                l.setYaw(0.0f);
            }
            case WEST -> {
                l.setX(l.getX() + 4.5f);
                l.setYaw(270.0f);
            }
            case SOUTH -> {
                l.setZ(l.getZ() - 4.5f);
                l.setYaw(180.0f);
            }
            default -> {
                l.setX(l.getX() - 4.5f);
                l.setYaw(90.0f);
            }
        }
        // read PDC and spawn ghast outside the TARDIS
        HappyGhast skies = (HappyGhast) l.getWorld().spawnEntity(l, EntityType.HAPPY_GHAST);
        skies.setAge(pdc.get(HappyGhastUtils.AGE, PersistentDataType.INTEGER));
        if (pdc.get(HappyGhastUtils.BABY, PersistentDataType.BOOLEAN)) {
            skies.setBaby();
        }
        if (pdc.has(HappyGhastUtils.HARNESS)) {
            String material = pdc.get(HappyGhastUtils.HARNESS, PersistentDataType.STRING);
            ItemStack harness = ItemStack.of(Material.valueOf(material));
            skies.getEquipment().setItem(EquipmentSlot.BODY, harness);
        }
        skies.setHealth(pdc.get(HappyGhastUtils.HEALTH, PersistentDataType.DOUBLE));
        if (pdc.has(HappyGhastUtils.NAME)) {
            String name = pdc.get(HappyGhastUtils.NAME, PersistentDataType.STRING);
            if (name != null && !name.isEmpty()) {
                skies.customName(Component.text(name));
            }
        }
        // remove the passengers
        for (Entity p : slime.getPassengers()) {
            slime.removePassenger(p);
            p.remove();
        }
        // remove the (dried ghast) slime
        slime.remove();
    }
}
