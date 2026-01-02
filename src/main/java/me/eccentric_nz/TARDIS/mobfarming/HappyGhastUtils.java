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
package me.eccentric_nz.TARDIS.mobfarming;

import io.papermc.paper.entity.Leashable;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHappy;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class HappyGhastUtils {

    private static final HashMap<String, Color> COLOURS = new HashMap<>();
    public static NamespacedKey AGE = new NamespacedKey(TARDIS.plugin, "happy_ghast_age");
    public static NamespacedKey BABY = new NamespacedKey(TARDIS.plugin, "happy_ghast_baby");
    public static NamespacedKey HARNESS = new NamespacedKey(TARDIS.plugin, "happy_ghast_harness");
    public static NamespacedKey HEALTH = new NamespacedKey(TARDIS.plugin, "happy_ghast_health");
    public static NamespacedKey HOME = new NamespacedKey(TARDIS.plugin, "happy_ghast_home");
    public static NamespacedKey NAME = new NamespacedKey(TARDIS.plugin, "happy_ghast_name");
    public static NamespacedKey ID = new NamespacedKey(TARDIS.plugin, "happy_ghast_id");
    public static NamespacedKey SLOT = new NamespacedKey(TARDIS.plugin, "happy_ghast_slot");

    static {
        COLOURS.put("WHITE_DYE", Color.WHITE);
        COLOURS.put("ORANGE_DYE", Color.ORANGE);
        COLOURS.put("MAGENTA_DYE", Color.fromRGB(13061821));
        COLOURS.put("LIGHT_BLUE_DYE", Color.fromRGB(3847130));
        COLOURS.put("YELLOW_DYE", Color.YELLOW);
        COLOURS.put("LIME_DYE", Color.LIME);
        COLOURS.put("PINK_DYE", Color.fromRGB(15961002));
        COLOURS.put("GRAY_DYE", Color.GRAY);
        COLOURS.put("LIGHT_GRAY_DYE", Color.fromRGB(10329495));
        COLOURS.put("CYAN_DYE", Color.fromRGB(1481884));
        COLOURS.put("PURPLE_DYE", Color.PURPLE);
        COLOURS.put("BLUE_DYE", Color.BLUE);
        COLOURS.put("BROWN_DYE", Color.fromRGB(8606770));
        COLOURS.put("GREEN_DYE", Color.GREEN);
        COLOURS.put("RED_DYE", Color.RED);
        COLOURS.put("BLACK_DYE", Color.BLACK);
    }

    public static Leashable getLeashed(Entity happy) {
        Location gl = happy.getLocation().clone().add(0, -4, 0);
        UUID hg = happy.getUniqueId();
        for (Entity h : gl.getNearbyEntities(4, 4, 4)) {
            if (h instanceof Leashable leashable) {
                try {
                    Entity holder = leashable.getLeashHolder();
                    if (holder instanceof HappyGhast ghast && hg.equals(ghast.getUniqueId())) {
                        return leashable;
                    }
                } catch (IllegalStateException ignored) {
                }
            }
        }
        return null;
    }

    public static void setLeashed(Location fence, TARDISHappyGhast happy, BlockFace face) {
        LeashHitch leashHitch = fence.getWorld().spawn(fence, LeashHitch.class);
        leashHitch.setFacingDirection(face);
        Slime slime = fence.getWorld().spawn(fence.getBlock().getRelative(face).getLocation().add(0.5, 0.5, 0.5), Slime.class);
        slime.setSize(0);
        PersistentDataContainer pdc = slime.getPersistentDataContainer();
        pdc.set(ID, PersistentDataType.INTEGER, happy.getTardis_id());
        pdc.set(SLOT, PersistentDataType.INTEGER, happy.getSlotIndex());
        String harness = "";
        if (happy.getHarness().getType().toString().endsWith("HARNESS")) {
            String h = happy.getHarness().getType().toString();
            harness = h.replace("HARNESS", "DYE");
            pdc.set(HARNESS, PersistentDataType.STRING, h);
        }
        pdc.set(BABY, PersistentDataType.BOOLEAN, happy.isBaby());
        if (happy.getHome() != null) {
            pdc.set(HOME, PersistentDataType.STRING, happy.getHome().toString());
        }
        pdc.set(HEALTH, PersistentDataType.DOUBLE, happy.getHealth());
        pdc.set(AGE, PersistentDataType.INTEGER, happy.getAge());
        String name = happy.getName();
        if (name != null && !name.isEmpty()) {
            pdc.set(NAME, PersistentDataType.STRING, name);
        }
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        ItemDisplay display = fence.getWorld().spawn(fence.getBlock().getRelative(BlockFace.NORTH).getLocation(), ItemDisplay.class);
        ItemStack dried = ItemStack.of(Material.DRIED_GHAST);
        display.setItemStack(dried);
        slime.addPassenger(display);
        if (!harness.isEmpty()) {
            TextDisplay text = fence.getWorld().spawn(fence.getBlock().getRelative(BlockFace.NORTH).getLocation(), TextDisplay.class);
            text.setBackgroundColor(COLOURS.get(harness));
            text.text(Component.text("    "));
            text.setBillboard(Display.Billboard.VERTICAL);
            slime.addPassenger(text);
        }
        TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> slime.setLeashHolder(leashHitch), 2L);
    }

    public static boolean isDockFree(Location dock) {
        for (Entity e : dock.getWorld().getNearbyEntities(dock, 8, 8, 8)) {
            if (e instanceof HappyGhast) {
                return false;
            }
        }
        return true;
    }

    public static int getFreeSlotCount(TARDIS plugin, int id) {
        ResultSetHappy rs = new ResultSetHappy(plugin);
        if (rs.fromId(id)) {
            return rs.getFreeSlots();
        }
        return 0;
    }

    public static int nextFreeSlot(TARDIS plugin, int id) {
        ResultSetHappy rs = new ResultSetHappy(plugin);
        if (rs.fromId(id)) {
            return rs.getAvailableIndex();
        }
        return -1;
    }

    public static void setSlotOccupied(TARDIS plugin, int slot, int id) {
        ResultSetHappy rs = new ResultSetHappy(plugin);
        if (rs.fromId(id)) {
            String[] slots = rs.getSlots().split(",");
            slots[slot] = "1";
            HashMap<String, Object> set = new HashMap<>();
            set.put("slots", String.join(",", slots));
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("happy", set, where);
        }
    }

    public static void setSlotUnoccupied(TARDIS plugin, int slot, int id) {
        ResultSetHappy rs = new ResultSetHappy(plugin);
        if (rs.fromId(id)) {
            String[] slots = rs.getSlots().split(",");
            slots[slot] = "0";
            HashMap<String, Object> set = new HashMap<>();
            set.put("slots", String.join(",", slots));
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("happy", set, where);
        }
    }
}
