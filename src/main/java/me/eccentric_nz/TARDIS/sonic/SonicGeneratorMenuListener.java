/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.recipes.shaped.SonicScrewdriverRecipe;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class SonicGeneratorMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<String, Integer> costs;
    private final HashMap<String, String> fields;
    private Location location;

    public SonicGeneratorMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        costs = getCosts();
        fields = getFields();
    }

    private HashMap<String, Integer> getCosts() {
        double full = plugin.getArtronConfig().getDouble("full_charge") / 100.0d;
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Standard Sonic", (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full));
        map.put("Bio-scanner Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full));
        map.put("Redstone Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.redstone") * full));
        map.put("Diamond Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full));
        map.put("Emerald Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full));
        map.put("Painter Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full));
        map.put("Ignite Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full));
        map.put("Pickup Arrows Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full));
        map.put("Knockback Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full));
        map.put("Brush Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.brush") * full));
        map.put("Conversion Upgrade", (int) (plugin.getArtronConfig().getDouble("sonic_generator.conversion") * full));
        return map;
    }

    private HashMap<String, String> getFields() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Bio-scanner Upgrade", "bio");
        map.put("Redstone Upgrade", "redstone");
        map.put("Diamond Upgrade", "diamond");
        map.put("Emerald Upgrade", "emerald");
        map.put("Painter Upgrade", "painter");
        map.put("Ignite Upgrade", "ignite");
        map.put("Pickup Arrows Upgrade", "arrow");
        map.put("Knockback Upgrade", "knockback");
        map.put("Brush Upgrade", "brush");
        map.put("Conversion Upgrade", "conversion");
        return map;
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof SonicGeneratorInventory)) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        location = plugin.getTrackerKeeper().getSonicGenerators().get(p.getUniqueId());
        int slot = event.getRawSlot();
        ItemStack sonic;
        boolean slotWasNull = false;
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISSonicGeneratorMenuListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        InventoryView view = event.getView();
        switch (slot) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> {
                // set display name of sonic in slot 49
                sonic = view.getItem(49);
                if (sonic == null) {
                    sonic = ItemStack.of(Material.BLAZE_ROD, 1);
                    slotWasNull = true;
                }
                // get custom model data of selected sonic
                ItemStack choice = view.getItem(slot);
                sonic.copyDataFrom(choice, dataComponentType -> true);
                if (slotWasNull) {
                    view.setItem(49, sonic);
                    setCost(view, costs.get("Standard Sonic"));
                }
            }
            case 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 -> {
                ItemStack upgrade = view.getItem(slot);
                Component upgrade_name = upgrade.getData(DataComponentTypes.CUSTOM_NAME);
                sonic = view.getItem(49);
                if (sonic == null) {
                    sonic = ItemStack.of(Material.BLAZE_ROD, 1);
                    slotWasNull = true;
                }
                List<Component> lore;
                if (ComponentUtils.hasLore(sonic)) {
                    // get the current sonic's upgrades
                    lore = new ArrayList<>(sonic.getData(DataComponentTypes.LORE).lines());
                } else {
                    // otherwise this is the first upgrade
                    lore = new ArrayList<>();
                    lore.add(Component.text("Upgrades:"));
                }
                // if they don't already have the upgrade
                if (!lore.contains(upgrade_name)) {
                    lore.add(upgrade_name);
                    sonic.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                    setCost(view, getCost(view) + costs.get(ComponentUtils.stripColour(upgrade_name)));
                }
                if (slotWasNull) {
                    view.setItem(49, sonic);
                }
            }
            case 36 -> {
                // reset to standard
                sonic = view.getItem(49);
                if (sonic == null) {
                    sonic = ItemStack.of(Material.BLAZE_ROD, 1);
                    slotWasNull = true;
                }
                if (slotWasNull) {
                    sonic.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Sonic Screwdriver"));
                    view.setItem(49, sonic);
                } else {
                    // remove lore
                    sonic.resetData(DataComponentTypes.LORE);
                }
                List<Float> sonicModel = SonicScrewdriverRecipe.sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), SonicVariant.ELEVENTH.getFloats());
                sonic.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                        .addFloats(sonicModel)
                        .build());
                setCost(view, costs.get("Standard Sonic"));
            }
            case 43 -> {
                // save
                sonic = view.getItem(49);
                if (sonic != null) {
                    save(p, sonic, true);
                }
            }
            case 44 -> {
                // save & generate
                sonic = view.getItem(49);
                if (sonic != null) {
                    save(p, sonic, false);
                    generate(p, sonic, getCost(view));
                }
            }
            case 53 -> close(p); // close
            default -> {
            }
        }
    }

    private void save(Player p, ItemStack is, boolean close) {
        // process the sonic
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        where.put("activated", 1);
        List<Float> floats = is.getData(DataComponentTypes.CUSTOM_MODEL_DATA).floats();
        Float model = floats.getFirst();
        String split = SonicVariant.getByFloat(model).toString().toLowerCase(Locale.ROOT);
        set.put("model", split);
        if (ComponentUtils.hasLore(is)) {
            List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
            fields.forEach((key, value) -> set.put(value, (lore.contains(Component.text(key))) ? 1 : 0));
        } else {
            // has been reset to standard sonic
            fields.forEach((key, value) -> set.put(value, 0));
        }
        plugin.getQueryFactory().doUpdate("sonic", set, where);
        plugin.getTrackerKeeper().getSonicGenerators().remove(p.getUniqueId());
        if (close) {
            close(p);
        }
    }

    private void generate(Player p, ItemStack sonic, int cost) {
        // check they have enough Artron energy
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (rs.fromUUID(p.getUniqueId().toString())) {
            int level = rs.getArtronLevel();
            if (cost < level) {
                ItemStack is = sonic.clone();
                Location loc = location.clone().add(0.5d, 0.75d, 0.5d);
                Entity drop = location.getWorld().dropItem(loc, is);
                drop.setVelocity(new Vector(0, 0, 0));
                plugin.getTrackerKeeper().getSonicGenerators().remove(p.getUniqueId());
                // remove the Artron energy
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().alterEnergyLevel("tardis", -cost, where, p);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "UPGRADE_ABORT_ENERGY");
            }
        }
        close(p);
    }

    private int getCost(InventoryView view) {
        ItemStack is = view.getItem(45);
        String c = ComponentUtils.stripColour(is.getData(DataComponentTypes.LORE).lines().getFirst());
        return TARDISNumberParsers.parseInt(c);
    }

    private void setCost(InventoryView view, int cost) {
        ItemStack is = view.getItem(45);
        List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
        lore.set(0, Component.text(cost));
        is.lore(lore);
    }
}
