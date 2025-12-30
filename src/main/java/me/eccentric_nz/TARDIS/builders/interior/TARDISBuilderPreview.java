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
package me.eccentric_nz.TARDIS.builders.interior;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.utility.FractalFence;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.skins.MannequinSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderPreview implements Runnable {

    private final TARDIS plugin;
    private final Schematic schm;
    private final World world;
    private final int tips;
    private final HashMap<Block, BlockData> postBedBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postCarpetBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDripstoneBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLanternBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLichenBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSculkVeinBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final List<Block> fractalBlocks = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> postLightBlocks = new ArrayList<>();
    private final HashMap<Block, TARDISBannerData> postBannerBlocks = new HashMap<>();
    private Block postBedrock = null;
    private int task, level = 0, row = 0, startx, starty, startz, resetx, resetz, h, w, d, j = 2;
    private JsonArray arr;
    private JsonObject obj;
    private Location location;
    private boolean running = false;
    private Location ender = null;
    private int counter = 0;
    private double div = 1.0d;

    /**
     * Builds the inside of the TARDIS.
     *
     * @param plugin an instance of the main TARDIS plugin class
     * @param schm   the name of the schematic file to use can be ANCIENT, ARS, BIGGER, BONE, BUDGET, CAVE, COPPER,
     *               CORAL, CURSED, CUSTOM, DELTA, DELUXE, DIVISION, EIGHTH, ELEVENTH, ENDER, FACTORY, FIFTEENTH, FUGITIVE,
     *               HOSPITAL, MASTER, MECHANICAL, ORIGINAL, PLANK, PYRAMID, REDSTONE, ROTOR, RUSTIC, SIDRAT, STEAMPUNK,
     *               THIRTEENTH, TOM, TWELFTH, WAR, WEATHERED, WOOD, LEGACY_BIGGER, LEGACY_DELUXE, LEGACY_ELEVENTH,
     *               LEGACY_REDSTONE or a CUSTOM name.
     * @param world  the world where the TARDIS is to be built.
     */
    public TARDISBuilderPreview(TARDIS plugin, Schematic schm, World world) {
        this.plugin = plugin;
        this.schm = schm;
        this.world = world;
        this.tips = schm.getPreview();
    }

    @Override
    public void run() {
        if (!running) {
            starty = schm.getStartY();
            // get JSON
            obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                h = dimensions.get("height").getAsInt() - 1;
                w = dimensions.get("width").getAsInt();
                d = dimensions.get("length").getAsInt() - 1;
                div = (h + 1.0d) * w * (d + 1.0d);
                // calculate startx, starty, startz
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(tips);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
                // get the correct chunk for ARS
                Location cl = new Location(world, startx, starty, startz);
                Chunk c = world.getChunkAt(cl);
                while (!c.isLoaded()) {
                    c.load(true);
                }
                location = new Location(world, startx, starty, startz);
                // get input array
                arr = obj.get("input").getAsJsonArray();
                running = true;
            }
        }
        if (level == h && row == w - 1) {
            // put on the door, redstone torches, signs, beds, and the repeaters
            postBedBlocks.forEach(Block::setBlockData);
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postTorchBlocks.forEach(Block::setBlockData);
            postLanternBlocks.forEach(Block::setBlockData);
            postRepeaterBlocks.forEach(Block::setBlockData);
            postStickyPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
            postDripstoneBlocks.forEach(Block::setBlockData);
            postLichenBlocks.forEach(Block::setBlockData);
            postSculkVeinBlocks.forEach(Block::setBlockData);
            TARDISSignSetter.setSigns(postSignBlocks, plugin, -1);
            for (Map.Entry<Block, BlockData> carpet : postCarpetBlocks.entrySet()) {
                Block pcb = carpet.getKey();
                pcb.setBlockData(carpet.getValue());
            }
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.POWER);
            }
            postLightBlocks.forEach((block) -> {
                if (block.getType().isAir()) {
                    Levelled levelled = TARDISConstants.LIGHT;
                    levelled.setLevel(15);
                    block.setBlockData(levelled);
                }
            });
            if (schm.getPermission().equals("cave")) {
                iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                iceBlocks.clear();
            }
            for (int f = 0; f < fractalBlocks.size(); f++) {
                FractalFence.grow(fractalBlocks.get(f), f);
            }
            TARDISBannerSetter.setBanners(postBannerBlocks);
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.END_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            // mannequins
            if (obj.has("mannequins")) {
                JsonArray mannequins = obj.get("mannequins").getAsJsonArray();
                for (int i = 0; i < mannequins.size(); i++) {
                    JsonObject mannequin = mannequins.get(i).getAsJsonObject();
                    JsonObject rel = mannequin.get("rel_location").getAsJsonObject();
                    int mx = rel.get("x").getAsInt();
                    int my = rel.get("y").getAsInt();
                    int mz = rel.get("z").getAsInt();
                    Location ml = new Location(world, resetx + mx + 0.5d, starty + my, resetz + mz + 0.5d);
                    Mannequin m = (Mannequin) world.spawnEntity(ml, EntityType.MANNEQUIN);
                    m.setRotation(mannequin.get("rotation").getAsFloat(), 0);
                    m.setBodyYaw(mannequin.get("yaw").getAsFloat());
                    String which = mannequin.get("type").getAsString();
                    m.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.STRING, which);
                    Skin skin = MannequinSkins.getByName.getOrDefault(which, MannequinSkins.ROMAN);
                    m.setProfile(ResolvableProfile.resolvableProfile().name("").uuid(UUID.randomUUID()).addProperty(new ProfileProperty("textures", skin.value(), skin.signature())).build());
                    m.setSilent(true);
                    m.setAI(false);
                    m.setImmovable(true);
                    if (mannequin.has("hand")) {
                        m.setMainHand(mannequin.get("hand").getAsString().equals("left") ? MainHand.LEFT : MainHand.RIGHT);
                        m.getEquipment().setItemInMainHand(ItemStack.of(mannequin.get("item").getAsString().equals("IRON_SWORD") ? Material.IRON_SWORD : Material.IRON_SPEAR));
                    }
                }
            }
            if (obj.has("paintings")) {
                JsonArray paintings = (JsonArray) obj.get("paintings");
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
                        pl = TARDISPainting.calculatePosition(which.split(":")[1], facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    } else {
                        art = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT).get(new NamespacedKey("minecraft", which.toLowerCase(Locale.ROOT)));
                        pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, starty + py, resetz + pz));
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
                        plugin.debug("Invalid painting location!" + pl);
                    }
                }
            }
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                for (int i = 0; i < frames.size(); i++) {
                    TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), location, -1);
                }
            }
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                for (int i = 0; i < displays.size(); i++) {
                    TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), location, tips);
                }
            }
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
        }
        JsonArray floor = arr.get(level).getAsJsonArray();
        JsonArray r = (JsonArray) floor.get(row);
        // paste a column
        for (int col = 0; col <= d; col++) {
            counter++;
            JsonObject c = r.get(col).getAsJsonObject();
            int x = startx + row;
            int y = starty + level;
            int z = startz + col;
            BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
            Material type = data.getMaterial();
            if (type.equals(Material.LIGHT_GRAY_CONCRETE) && (schm.getPermission().equals("bone") || schm.getPermission().equals("rustic"))) {
                // get the block
                Block block = new Location(world, x, y, z).getBlock();
                // build a console
                String ct = (schm.getPermission().equals("bone")) ? "console_light_gray" : "console_rustic";
                new ConsoleBuilder(plugin).create(block, ct, -1, UUID.randomUUID().toString());
            }
            if (type.equals(Material.NOTE_BLOCK)) {
                // set block data to BARRIER
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, world, x, y, z);
            }
            if (type.equals(Material.ORANGE_WOOL)) {
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.HEXAGON, world, x, y, z);
            }
            if (type.equals(Material.BLUE_WOOL)) {
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.BLUE_BOX, world, x, y, z);
            }
            if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && schm.getPermission().equals("delta")) {
                fractalBlocks.add(world.getBlockAt(x, y, z));
            }
            if (level == 0 && type.equals(Material.PINK_STAINED_GLASS) && schm.getPermission().equals("division")) {
                postLightBlocks.add(world.getBlockAt(x, y - 1, z));
            }
            if (type.equals(Material.DEEPSLATE_REDSTONE_ORE) && (schm.getPermission().equals("division") || schm.getPermission().equals("hospital"))) {
                // replace with gray concrete
                data = schm.getPermission().equals("division") ? Material.GRAY_CONCRETE.createBlockData() : Material.LIGHT_GRAY_CONCRETE.createBlockData();
            }
            if (type.equals(Material.WHITE_STAINED_GLASS) && schm.getPermission().equals("war")) {
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ROUNDEL, world, x, y, z);
            }
            if (type.equals(Material.WHITE_TERRACOTTA) && schm.getPermission().equals("war")) {
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
            }
            if (type.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
                    // remember spawn location for this console preview
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("tardis_id", tips);
                    set.put("name", schm.getPermission());
                    set.put("world", world.getName());
                    set.put("x", x + 0.5d);
                    set.put("y", y);
                    set.put("z", (z + 1));
                    plugin.getQueryFactory().doInsert("transmats", set);
                }
            }
            if (type.equals(Material.CAKE) && schm.getPermission().equals("sidrat")) {
                // set block data to correct LEVER
                BlockData blockData = Material.LEVER.createBlockData();
                Switch lever = (Switch) blockData;
                lever.setAttachedFace(FaceAttachable.AttachedFace.WALL);
                lever.setFacing(BlockFace.WEST);
                data = lever;
            }
            if (type.equals(Material.JUKEBOX)) {
                // set block data to correct BARRIER + Item Display
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
            }
            if (type.equals(Material.COMMAND_BLOCK) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                if (type.equals(Material.COMMAND_BLOCK)) {
                    data = switch (schm.getPermission()) {
                        case "ender" -> Material.END_STONE_BRICKS.createBlockData();
                        case "delta", "cursed" -> Material.BLACKSTONE.createBlockData();
                        case "ancient", "bone", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                        case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                        case "sidrat" -> Material.RED_CONCRETE.createBlockData();
                        default -> Material.STONE_BRICKS.createBlockData();
                    };
                }
            }
            if (type.equals(Material.BEACON) && schm.getPermission().equals("ender")) {
                /*
                 * get the ender crystal location
                 */
                ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
            }
            if (type.equals(Material.ICE) && schm.getPermission().equals("cave")) {
                iceBlocks.add(world.getBlockAt(x, y, z));
            } else if (Tag.BEDS.isTagged(type)) {
                postBedBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.IRON_DOOR)) { // doors
                // if it's the door, don't set it just remember its block then do it at the end
                postDoorBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.REDSTONE_TORCH) || type.equals(Material.REDSTONE_WALL_TORCH)) {
                postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.TORCH) || type.equals(Material.WALL_TORCH) || type.equals(Material.SOUL_TORCH) || type.equals(Material.SOUL_WALL_TORCH)) {
                postTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.STICKY_PISTON)) {
                postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON)) {
                postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON_HEAD)) {
                postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LEVER)) {
                postLeverBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (Tag.ALL_SIGNS.isTagged(type)) {
                postSignBlocks.put(world.getBlockAt(x, y, z), c);
            } else if (type.equals(Material.POINTED_DRIPSTONE)) {
                postDripstoneBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.GLOW_LICHEN)) {
                postLichenBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.SCULK_VEIN)) {
                postSculkVeinBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LANTERN) || type.equals(Material.SOUL_LANTERN)) {
                postLanternBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (TARDISStaticUtils.isBanner(type)) {
                JsonObject state = c.has("banner") ? c.get("banner").getAsJsonObject() : null;
                if (state != null) {
                    TARDISBannerData tbd = new TARDISBannerData(data, state);
                    postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
                }
            } else if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
                if (c.has("head")) {
                    JsonObject head = c.get("head").getAsJsonObject();
                    if (head.has("uuid")) {
                        try {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            TARDISHeadSetter.textureSkull(plugin, uuid, head, world.getBlockAt(x, y, z));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            } else if (type.equals(Material.DECORATED_POT)) {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (c.has("pot")) {
                        JsonObject pot = c.get("pot").getAsJsonObject();
                        TARDISPotSetter.decorate(plugin, pot, world.getBlockAt(x, y, z));
                    }
                }, 1L);
            } else if (TARDISStaticUtils.isInfested(type)) {
                // legacy monster egg stone for controls
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
            } else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
                if (j < 6) {
                    data = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) data;
                    switch (j) {
                        case 2 -> {
                            directional.setFacing(BlockFace.WEST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                        }
                        case 3 -> {
                            directional.setFacing(BlockFace.NORTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                        }
                        case 4 -> {
                            directional.setFacing(BlockFace.SOUTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                        }
                        default -> {
                            directional.setFacing(BlockFace.EAST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                        }
                    }
                    j++;
                }
            } else if (type.equals(Material.SPONGE)) {
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
            } else if (type.equals(Material.BEDROCK)) {
                // remember bedrock location to block off the beacon light
                postBedrock = world.getBlockAt(x, y, z);
            } else if (type.equals(Material.BROWN_MUSHROOM) && schm.getPermission().equals("master")) {
                // spawn locations for two villagers
                TARDISBlockSetters.setBlock(world, x, y, z, Material.VOID_AIR);
                plugin.setTardisSpawn(true);
                world.spawnEntity(new Location(world, x + 0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
            } else if (type.equals(Material.BLACK_CARPET) && schm.getPermission().equals("master")) {
                postCarpetBlocks.put(world.getBlockAt(x, y, z), data);
            } else {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
            }
            if (col == d && row < w) {
                row++;
            }
            if (col == d && row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
