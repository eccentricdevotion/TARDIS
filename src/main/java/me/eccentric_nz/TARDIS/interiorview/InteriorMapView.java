package me.eccentric_nz.TARDIS.interiorview;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class InteriorMapView {

    public static boolean getInteriorSnapshot(Location location) {
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
        MapView mapView = Bukkit.createMap(location.getWorld());
        mapView.setTrackingPosition(false);
        for (MapRenderer renderer : mapView.getRenderers()) {
            mapView.removeRenderer(renderer);
        }
        InteriorRenderer renderer = new InteriorRenderer(location);
        mapView.addRenderer(renderer);
        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);
        // set map in Item frame
        return true;
    }
}
