package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.*;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RoomsUtility {

    public static void fakeBlock(JsonObject json, LimitedRegion region, int sx, int sz) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int x = rel.get("x").getAsInt() + sx;
        int y = rel.get("y").getAsInt() + 64;
        int z = rel.get("z").getAsInt() + sz;
        int model = -1;
        if (json.has("stack")) {
            JsonObject stack = json.get("stack").getAsJsonObject();
            if (stack.has("cmd")) {
                model = stack.get("cmd").getAsInt();
            }
            Material material = Material.valueOf(stack.get("type").getAsString());
            TARDISDisplayItem tdi = TARDISDisplayItem.getByMaterialAndData(material, model);
            if (tdi != null) {
                set(region, x + 0.5d, y + 0.5d, z + 0.5d, tdi.getMaterial(), tdi.getCustomModel(), false);
            } else {
                set(region, x + 0.5d, y + 0.25d, z + 0.5d, material, model, true);
            }
        }
    }

    private static void set(LimitedRegion region, double x, double y, double z, Material material, int model, boolean inRoom) {
        Location location = new Location(null, x, y, z);
        ItemDisplay display = (ItemDisplay) region.spawnEntity(location, EntityType.ITEM_DISPLAY);
        ItemStack is = new ItemStack(material);
        if (model != -1) {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(model);
            is.setItemMeta(im);
        }
        display.setItemStack(is);
        if (inRoom) {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
            display.setBillboard(Display.Billboard.VERTICAL);
        }
        display.setInvulnerable(true);
    }

    public static void curate(JsonObject json, LimitedRegion region, int sx, int sz) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int x = rel.get("x").getAsInt() + sx;
        int y = rel.get("y").getAsInt() + 64;
        int z = rel.get("z").getAsInt() + sz;
        BlockFace facing = BlockFace.valueOf(json.get("facing").getAsString());
        Location l = new Location(null, x, y, z);
        ItemFrame frame = (ItemFrame) region.spawnEntity(l, (json.get("glowing").getAsBoolean()) ? EntityType.GLOW_ITEM_FRAME : EntityType.ITEM_FRAME);
        frame.setFacingDirection(facing, true);
        int cmd = 1;
        if (json.has("item")) {
            try {
                ItemStack is = new ItemStack(Material.valueOf(json.get("item").getAsString()));
                ItemMeta im = is.getItemMeta();
                if (json.has("cmd")) {
                    cmd = json.get("cmd").getAsInt();
                    im.setCustomModelData(cmd);
                }
                if (json.has("name")) {
                    im.setDisplayName(json.get("name").getAsString());
                }
                if (json.has("lore")) {
                    List<String> lore = new ArrayList<>();
                    for (JsonElement element : json.get("lore").getAsJsonArray()) {
                        lore.add(element.getAsString());
                    }
                    im.setLore(lore);
                }
                if (json.has("banner")) {
                    JsonObject banner = json.get("banner").getAsJsonObject();
                    DyeColor baseColour = DyeColor.valueOf(banner.get("base_colour").getAsString());
                    JsonArray patterns = banner.get("patterns").getAsJsonArray();
                    List<Pattern> plist = new ArrayList<>();
                    for (int j = 0; j < patterns.size(); j++) {
                        JsonObject jo = patterns.get(j).getAsJsonObject();
                        PatternType pt = PatternType.valueOf(jo.get("pattern").getAsString());
                        DyeColor dc = DyeColor.valueOf(jo.get("pattern_colour").getAsString());
                        Pattern p = new Pattern(dc, pt);
                        plist.add(p);
                    }
                    BlockStateMeta bsm = (BlockStateMeta) im;
                    Banner b = (Banner) bsm.getBlockState();
                    b.setBaseColor(baseColour);
                    b.setPatterns(plist);
                    bsm.setBlockState(b);
                }
                is.setItemMeta(im);
                frame.setItem(is, false);
            } catch (IllegalArgumentException e) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not create item stack for schematic item frame!");
            }
        }
        frame.setFixed(json.get("fixed").getAsBoolean());
        frame.setVisible(json.get("visible").getAsBoolean());
        Rotation rotation = Rotation.valueOf(json.get("rotation").getAsString());
        frame.setRotation(rotation);
    }

    public static void hang(JsonObject painting, LimitedRegion region, int sx, int sz) {
        // place paintings
        JsonObject rel = painting.get("rel_location").getAsJsonObject();
        int px = rel.get("x").getAsInt() + sx;
        int py = rel.get("y").getAsInt() + 64;
        int pz = rel.get("z").getAsInt() + sz;
        BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
        Location pl;
        String which = painting.get("art").getAsString();
        Art art = null;
        if (which.contains(":")) {
            // custom datapack painting
            pl = TARDISPainting.calculatePosition(which.split(":")[1], facing, new Location(null, px, py, pz));
        } else {
            art = Art.valueOf(which);
            pl = TARDISPainting.calculatePosition(art, facing, new Location(null, px, py, pz));
        }
        try {
            Painting ent = (Painting) region.spawnEntity(pl, EntityType.PAINTING);
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

    public static void flag(JsonObject json, Banner banner) {
        List<Pattern> plist = new ArrayList<>();
        JsonArray patterns = json.get("patterns").getAsJsonArray();
        for (int j = 0; j < patterns.size(); j++) {
            JsonObject jo = patterns.get(j).getAsJsonObject();
            PatternType pt = PatternType.valueOf(jo.get("pattern").getAsString());
            DyeColor dc = DyeColor.valueOf(jo.get("pattern_colour").getAsString());
            Pattern p = new Pattern(dc, pt);
            plist.add(p);
        }
        banner.setPatterns(plist);
        banner.update();
    }
}
