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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.rooms.eye.Capacitor;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Sphere extends TARDISParticleRunnable {

    public final Set<Vector> coords = new HashSet<>();
    private final Location location;
    private final Capacitor capacitor;

    public Sphere(TARDIS plugin, UUID uuid, Location location, Capacitor capacitor) {
        super(plugin, uuid);
        this.location = location;
        this.capacitor = capacitor;
        init();
    }

    public void init() {
        for (double i = 0; i <= Math.PI; i += Math.PI / capacitor.getRings()) {
            double r = Math.sin(i);
            double y = capacitor.getRadius() * Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / capacitor.getRings()) {
                double x = capacitor.getRadius() * Math.cos(a) * r;
                double z = capacitor.getRadius() * Math.sin(a) * r;
                coords.add(new Vector(x, y, z));
            }
        }
        ItemDisplay display = TARDISDisplayItemUtils.get(location.getBlock());
        // check if there is a display there already
        if (display == null) {
            display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        }
        ItemStack is = ItemStack.of(Material.MAGMA_BLOCK);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("Sphere " + TARDISStringUtils.capitalise(capacitor.toString())));
        is.setItemMeta(im);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
    }

    @Override
    public void run() {
        // check for player in chunk
        if (playerInChunk(location)) {
            for (Vector v : coords) {
                location.add(v.getX(), v.getY(), v.getZ());
                spawnParticle(Particle.ENTITY_EFFECT, location, 3, 0, TARDISConstants.RANDOM.nextBoolean() ? Color.ORANGE : Color.YELLOW);
                location.subtract(v.getX(), v.getY(), v.getZ());
            }
        }
    }

    private boolean playerInChunk(Location location) {
        int r = location.getBlockY() % 16;
        int upperY = location.getBlockY() + (16 - r);
        int lowerY = location.getBlockY() - r;
        for (Entity e : location.getChunk().getEntities()) {
            if (e instanceof Player p) {
                int pl = p.getLocation().getBlockY();
                if (pl < upperY && pl > lowerY) {
                    return true;
                }
            }
        }
        return false;
    }
}
