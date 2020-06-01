package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class TARDISScannerMap {

    private final TARDIS plugin;
    private final Location location;
    private final ItemFrame itemFrame;

    public TARDISScannerMap(TARDIS plugin, Location location, ItemFrame itemFrame) {
        this.plugin = plugin;
        this.location = location;
        this.itemFrame = itemFrame;
    }

    public void setMap() {
        World world = location.getWorld();
        MapView view = plugin.getServer().createMap(world);
        view.setCenterX(location.getBlockX());
        view.setCenterZ(location.getBlockZ());
        view.setScale(MapView.Scale.CLOSEST);
        view.setTrackingPosition(true);
        view.setLocked(true);
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1, (short) view.getId());
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setMapView(view);
        map.setItemMeta(meta);
        itemFrame.setItem(map);
        itemFrame.setRotation(Rotation.NONE);
        plugin.getTardisHelper().updateMap(world, view);
    }
}
