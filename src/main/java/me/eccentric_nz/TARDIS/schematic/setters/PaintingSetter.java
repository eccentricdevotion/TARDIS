package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

import java.util.Locale;

public class PaintingSetter {

    public static void setArt(JsonArray paintings, World world, int x, int y, int z) {
        for (int i = 0; i < paintings.size(); i++) {
            JsonObject painting = paintings.get(i).getAsJsonObject();
            JsonObject rel = painting.get("rel_location").getAsJsonObject();
            int px = rel.get("x").getAsInt();
            int py = rel.get("y").getAsInt();
            int pz = rel.get("z").getAsInt();
            BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
            Location pl;
            String which = painting.get("art").getAsString();
            Art art = null;
            if (which.contains(":")) {
                // custom datapack painting
                pl = TARDISPainting.calculatePosition(which.split(":")[1], facing, new Location(world, x + px, y + py, z + pz));
            } else {
                art = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT).get(new NamespacedKey("minecraft", which.toLowerCase(Locale.ROOT)));
                pl = TARDISPainting.calculatePosition(art, facing, new Location(world, x + px, y + py, z + pz));
            }
            try {
                Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                ent.teleport(pl);
                ent.setFacingDirection(facing, true);
                if (art != null) {
                    ent.setArt(art, true);
                } else {
                    DataPackPainting.setCustomVariant(ent, which);
                }
            } catch (IllegalArgumentException e) {
                TARDIS.plugin.debug("Invalid painting location!" + pl);
            }
        }
    }
}
