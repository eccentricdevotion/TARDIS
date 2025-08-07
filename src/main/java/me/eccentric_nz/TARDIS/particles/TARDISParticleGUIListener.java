/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetThrottle;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISParticleGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISParticleGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onParticleClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISParticleInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (slot >= 0 && slot < 54) {
            InventoryView view = event.getView();
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                String display = ComponentUtils.stripColour(im.displayName());
                switch (slot) {
                    case 1, 2, 3, 4, 5, 6, 7 -> setShape(view, slot, display, uuid); // particle shape
                    case 10, 11, 12, 13, 14, 15, 16,
                         19, 20, 21, 22, 23, 24, 25, 27,
                         28, 29, 30, 31, 32, 33, 34,
                         37, 38, 39, 40, 41, 42, 43 -> setEffect(view, slot, display, uuid); // particle effect
                    case 17 -> cycleColour(view, uuid); // colour
                    case 35 -> cycleBlocks(view, uuid); // block
                    case 44 -> toggle(view, is, uuid); // set enabled/disabled
                    case 45 -> less(view, true, uuid); // less density
                    case 47 -> more(view, true, uuid); // more density
                    case 48 -> test(view, player, uuid); // test
                    case 49 -> less(view, false, uuid); // less speed
                    case 51 -> more(view, false, uuid); // more speed
                    case 53 -> close(player);
                    default -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private void setShape(InventoryView view, int slot, String display, UUID uuid) {
        for (int s = 1; s < 8; s++) {
            ItemStack is = view.getItem(s);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                ItemStack toSet = ItemStack.of(s == slot ? Material.LAPIS_ORE : Material.LAPIS_LAZULI);
                toSet.setItemMeta(im);
//                is.setType(s == slot ? Material.LAPIS_ORE : Material.LAPIS_LAZULI);
                view.setItem(s, toSet);
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("shape", display.toUpperCase(Locale.ROOT));
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
    }

    private void setEffect(InventoryView view, int slot, String display, UUID uuid) {
        for (int s = 10; s < 44; s++) {
            ItemStack is = view.getItem(s);
            if (is != null && s != GUIParticle.COLOUR.slot() && s != GUIParticle.BLOCK_INFO.slot() && s != GUIParticle.BLOCK.slot() && s != GUIParticle.TOGGLE.slot()) {
                ItemMeta im = is.getItemMeta();
                ItemStack toSet = ItemStack.of(s == slot ? Material.REDSTONE_ORE : Material.REDSTONE);
                toSet.setItemMeta(im);
                view.setItem(s, toSet);
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("effect", display.toUpperCase(Locale.ROOT));
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
    }

    private void cycleColour(InventoryView view, UUID uuid) {
        ItemStack is = view.getItem(GUIParticle.COLOUR.slot());
        ItemMeta im = is.getItemMeta();
        Component lore = im.lore().getFirst();
        NamedTextColor current = ParticleColour.fromString(lore);
        int index = ParticleColour.colours.indexOf(current) + 1;
        if (index > 15) {
            index = 0;
        }
        NamedTextColor next = ParticleColour.colours.get(index);
        String colour = ParticleColour.toString(next);
        im.lore(List.of(Component.text(colour, next)));
        is.setItemMeta(im);
        view.setItem(GUIParticle.COLOUR.slot(), is);
        HashMap<String, Object> set = new HashMap<>();
        set.put("colour", colour);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
    }

    private void cycleBlocks(InventoryView view, UUID uuid) {
        ItemStack is = view.getItem(GUIParticle.BLOCK.slot());
        ItemMeta im = is.getItemMeta();
        Component lore = im.lore().getFirst();
        int index = ParticleBlock.blocks.indexOf(lore.toString()) + 1;
        if (index > ParticleBlock.blocks.size() - 1) {
            index = 0;
        }
        Component block = Component.text(ParticleBlock.blocks.get(index));
        im.lore(List.of(block));
        is.setItemMeta(im);
        view.setItem(GUIParticle.BLOCK.slot(), is);
        HashMap<String, Object> set = new HashMap<>();
        set.put("block", block);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
    }

    private void toggle(InventoryView view, ItemStack is, UUID uuid) {
        ItemMeta im = is.getItemMeta();
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        boolean on = component.getFloats().getFirst() > 200;
        component.setFloats(on ? SwitchVariant.BUTTON_TOGGLE_OFF.getFloats() : SwitchVariant.BUTTON_TOGGLE_ON.getFloats());
        im.setCustomModelDataComponent(component);
        List<Component> lore = im.lore();
        lore.set(0, on ? Component.text(plugin.getLanguage().getString("SET_OFF", "OFF")) : Component.text(plugin.getLanguage().getString("SET_ON", "ON")));
        im.lore(lore);
        is.setItemMeta(im);
        view.setItem(GUIParticle.TOGGLE.slot(), is);
        HashMap<String, Object> set = new HashMap<>();
        set.put("particles_on", on ? 0 : 1);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
    }

    private void test(InventoryView view, Player player, UUID uuid) {
        if (plugin.getUtils().inTARDISWorld(player)) {
            // must be outside the TARDIS
            return;
        }
        // get players TARDIS id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(uuid.toString())) {
            // get TARDIS location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rst.getTardisId());
            if (rsc.resultSet()) {
                // get throttle setting
                ResultSetThrottle rs = new ResultSetThrottle(plugin);
                Throticle throticle = rs.getSpeedAndParticles(uuid.toString());
                // read current settings
                ParticleData data = getParticleData(view);
                // display particles
                Emitter emitter = new Emitter(plugin, uuid, rsc.getCurrent().location(), data, throticle.throttle().getFlightTime());
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, data.getShape().getPeriod());
                emitter.setTaskID(task);
                // close GUI
                close(player);
            }
        }
    }

    private ParticleData getParticleData(InventoryView view) {
        ParticleEffect effect = ParticleEffect.ASH;
        ParticleShape shape = ParticleShape.RANDOM;
        boolean b = false;
        try {
            for (int s = 10; s < 44; s++) {
                ItemStack eis = view.getItem(s);
                if (eis != null && eis.getType() == Material.REDSTONE_ORE) {
                    effect = ParticleEffect.valueOf(ComponentUtils.stripColour(eis.getItemMeta().displayName()).toUpperCase(Locale.ROOT));
                }
            }
            for (int s = 1; s < 8; s++) {
                ItemStack sis = view.getItem(s);
                if (sis != null && sis.getType() == Material.LAPIS_ORE) {
                    shape = ParticleShape.valueOf(ComponentUtils.stripColour(sis.getItemMeta().displayName()).toUpperCase(Locale.ROOT));
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
        ItemStack dis = view.getItem(GUIParticle.DENSITY.slot());
        String d = ComponentUtils.stripColour(dis.getItemMeta().lore().getFirst());
        int density = TARDISNumberParsers.parseInt(d);
        ItemStack spis = view.getItem(GUIParticle.SPEED.slot());
        String s = ComponentUtils.stripColour(spis.getItemMeta().lore().getFirst());
        ItemStack cis = view.getItem(GUIParticle.COLOUR.slot());
        String colour = ComponentUtils.stripColour(cis.getItemMeta().lore().getFirst());
        ItemStack bis = view.getItem(GUIParticle.BLOCK.slot());
        String block = ComponentUtils.stripColour(bis.getItemMeta().lore().getFirst());
        double speed = TARDISNumberParsers.parseInt(s) / 10.0d;
        return new ParticleData(effect, shape, density, speed, colour, block, b);
    }

    private void less(InventoryView view, boolean density, UUID uuid) {
        int min = density ? 8 : 0;
        int slot = density ? GUIParticle.DENSITY.slot() : GUIParticle.SPEED.slot();
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        List<Component> lore = im.lore();
        int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.getFirst()));
        level -= 1;
        if (level >= min) {
            lore.set(0, Component.text(level, NamedTextColor.AQUA));
            im.lore(lore);
            is.setItemMeta(im);
            view.setItem(slot, is);
            String field = density ? "density" : "speed";
            HashMap<String, Object> set = new HashMap<>();
            set.put(field, level);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
        }
    }

    private void more(InventoryView view, boolean density, UUID uuid) {
        int max = density ? 32 : 10;
        int slot = density ? GUIParticle.DENSITY.slot() : GUIParticle.SPEED.slot();
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        List<Component> lore = im.lore();
        int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.getFirst()));
        level += 1;
        if (level <= max) {
            lore.set(0, Component.text(level, NamedTextColor.AQUA));
            im.lore(lore);
            is.setItemMeta(im);
            view.setItem(slot, is);
            String f = density ? "density" : "speed";
            HashMap<String, Object> set = new HashMap<>();
            set.put(f, level);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncUpdate("particle_prefs", set, where);
        }
    }
}
