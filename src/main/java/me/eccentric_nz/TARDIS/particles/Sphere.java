package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.rooms.eye.Capacitor;
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

    private final Location location;
    private final Capacitor capacitor;
    public Set<Vector> coords = new HashSet<>();

    public Sphere(TARDIS plugin, UUID uuid, Location location, Capacitor capacitor) {
        super(plugin, uuid);
        this.location = location;
        this.capacitor = capacitor;
        init();
    }

    public void init() {
        for (double i = 0; i <= Math.PI; i += Math.PI / capacitor.getRings()) {
            double r = Math.sin(i) * capacitor.getRadius();
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / capacitor.getDensity()) {
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                coords.add(new Vector(x, y, z));
            }
        }
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        ItemStack is = new ItemStack(Material.MAGMA_BLOCK);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(capacitor.getCustomModelData());
        is.setItemMeta(im);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
    }

    @Override
    public void run() {
        // check for player in chunk
        if (playerInChunk(location)) {
//            t += 0.25;
            for (Vector v : coords) {
                location.add(v.getX(), v.getY(), v.getZ());
                spawnParticle(Particle.ENTITY_EFFECT, location, 3, 0, TARDISConstants.RANDOM.nextBoolean() ? Color.ORANGE : Color.YELLOW);
                location.subtract(v.getX(), v.getY(), v.getZ());
            }
//            if (t > 6) {
//                cancel();
//            }
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
