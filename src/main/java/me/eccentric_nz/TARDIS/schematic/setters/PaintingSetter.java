package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class PaintingSetter {

    public static void setArt(JsonArray paintings, World world, int x, int y, int z) {
        for (int i = 0; i < paintings.size(); i++) {
            JsonObject painting = paintings.get(i).getAsJsonObject();
            JsonObject rel = painting.get("rel_location").getAsJsonObject();
            int px = rel.get("x").getAsInt();
            int py = rel.get("y").getAsInt();
            int pz = rel.get("z").getAsInt();
            BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
            String which = painting.get("art").getAsString();
            String[] split = which.split(":");
            Art art = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT).get(new NamespacedKey(split[0], split[1]));
            Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, x + px, y + py, z + pz));
            Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
            ent.setFacingDirection(facing, true);
            if (art != null) {
                ent.setArt(art, true);
            } else {
                DataPackPainting.setCustomVariant(ent, which);
            }
        }
    }
}
