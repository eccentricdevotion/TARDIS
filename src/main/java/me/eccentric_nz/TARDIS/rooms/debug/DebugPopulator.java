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
package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.GUIKeyPreferences;
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SonicScrewdriver;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.compound.CompoundBuilder;
import me.eccentric_nz.tardischemistry.element.Element;
import me.eccentric_nz.tardischemistry.element.ElementBuilder;
import me.eccentric_nz.tardischemistry.lab.Lab;
import me.eccentric_nz.tardischemistry.lab.LabBuilder;
import me.eccentric_nz.tardischemistry.microscope.LabEquipment;
import me.eccentric_nz.tardischemistry.product.Product;
import me.eccentric_nz.tardischemistry.product.ProductBuilder;
import me.eccentric_nz.tardisregeneration.ElixirOfLife;
import me.eccentric_nz.tardisshop.ShopItem;
import me.eccentric_nz.tardisshop.ShopItemRecipe;
import me.eccentric_nz.tardisweepingangels.equip.ArmourStandEquipment;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.util.Transformation;

import java.util.*;

public class DebugPopulator {

    private final TARDIS plugin;
    private final World world;
    private final int rx = -1552;
    private final int rz = -9744;
    private final List<ItemStack> stacks = new ArrayList<>();

    public DebugPopulator(TARDIS plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public void createBase(boolean clear) {
        // TIPS slot -50
        TARDISTIPSData tipsData = new TARDISInteriorPostioning(plugin).getTIPSData(-50);
        int x = tipsData.getCentreX();
        int z = tipsData.getCentreZ();
        plugin.debug("Debug Preview spawn => x" + x + ", y65, z" + z);
        if (clear) {
            // clear all entities at the location
            Location location = new Location(world, x, 65, z);
            for (Entity entity : world.getNearbyEntities(location, 100, 10, 100)) {
                if (!(entity instanceof Player)) {
                    entity.remove();
                }
            }
        } else {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", -50);
            set.put("name", "debug_preview");
            set.put("world", world.getName());
            set.put("x", x + 0.5d);
            set.put("y", 65);
            set.put("z", z + 0.5d);
            plugin.getQueryFactory().doInsert("transmats", set);
        }
        for (int r = -100; r < 100; r++) {
            for (int c = -100; c < 100; c++) {
                world.getBlockAt(x + r, 64, z + c).setType(Material.BLACK_CONCRETE);
                world.getBlockAt(x + r, 65, z + c).setType(Material.AIR);
                if (r % 2 == 0 && c % 2 == 0) {
                    world.getBlockAt(x + r, 70, z + c).setBlockData(TARDISConstants.LIGHT_DIV);
                }
            }
        }
    }

    public void items() {
        int x = -2;
        int z = 2;
        for (ShopItem item : ShopItem.values()) {
            // 78 shaped, 20 shapeless
            if (item.getRecipeType() == ShopItemRecipe.SHAPED || item.getRecipeType() == ShopItemRecipe.SHAPELESS) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set block at location
                setItemFromMaterial(location, item.getMaterial(), item.getModel(), Component.text(item.getDisplayName()));
                // loop x z 24 x 24 blocks with empty blocks between
                x -= 2;
                if (x < -24) {
                    x = -2;
                    z += 2;
                }
            }
        }
        NamespacedKey[] data = new NamespacedKey[]{CircuitVariant.TELEPATHIC_DAMAGED.getKey(), CircuitVariant.STATTENHEIM_DAMAGED.getKey(), CircuitVariant.MATERIALISATION_DAMAGED.getKey(),
                CircuitVariant.LOCATOR_DAMAGED.getKey(), CircuitVariant.CHAMELEON_DAMAGED.getKey(), CircuitVariant.SONIC_DAMAGED.getKey(), CircuitVariant.ARS.getKey(), CircuitVariant.TEMPORAL_DAMAGED.getKey(),
                CircuitVariant.MEMORY_DAMAGED.getKey(), CircuitVariant.INPUT.getKey(), CircuitVariant.SCANNER_DAMAGED.getKey(), CircuitVariant.PERCEPTION_DAMAGED.getKey(), CircuitVariant.RANDOM_DAMAGED.getKey(),
                CircuitVariant.INVISIBILITY_DAMAGED.getKey(), CircuitVariant.RIFT_DAMAGED.getKey()};
        String[] names = new String[]{"Telepathic", "Stattenheim", "Materialisation", "Locator", "Chameleon", "Sonic", "ARS", "Temporal", "Memory", "Input", "Scanner", "Perception", "Random", "Invisibility", "Rift"};
        int c = 0;
        for (String damaged : names) {
            Location location = new Location(world, rx + x, 65, rz + z);
            setItemFromMaterial(location, Material.GLOWSTONE_DUST, data[c], Component.text("Damaged " + damaged + " Circuit"));
            c++;
            x -= 2;
            if (x < -24) {
                x = -2;
                z += 2;
            }
        }
        // rust/acid buckets, area disk
        Material[] materials = new Material[]{Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.MUSIC_DISC_BLOCKS};
        NamespacedKey[] cmd = new NamespacedKey[]{Whoniverse.ACID_BUCKET.getKey(), Whoniverse.RUST_BUCKET.getKey(), DiskVariant.AREA_DISK.getKey()};
        String[] misc = new String[]{"Acid Bucket", "Rust Bucket", "Area Storage Disk"};
        int b = 0;
        for (String m : misc) {
            Location location = new Location(world, rx + x, 65, rz + z);
            setItemFromMaterial(location, materials[b], cmd[b], Component.text(m));
            b++;
            x -= 2;
            if (x < -24) {
                x = -2;
                z += 2;
            }
        }
    }

    public void blocks() {
        int x = 2;
        int z = 2;
        for (TARDISDisplayItem tdi : TARDISDisplayItemRegistry.values()) {
            // 122 blocks - surgery room x-ray
            if (tdi != TARDISBlockDisplayItem.NONE && tdi != TARDISBlockDisplayItem.PANDORICA && tdi != TARDISBlockDisplayItem.UNTEMPERED_SCHISM && tdi.getCustomModel() != null && !tdi.toString().contains("DOOR")) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set display item at location
                // and remember item stack for chest population
                stacks.add(TARDISDisplayItemUtils.setAndReturnStack(tdi, world, rx + x, 65, rz + z));
                //labels
                addLabel(location, Component.text(tdi.getDisplayName()));
                // loop x z - spaced over 24 x 24 with empty blocks between
                x += 2;
                if (x > 24) {
                    x = 2;
                    z += 2;
                }
            }
        }
    }

    public void sonicAndKeys() {
        // all sonics and keys
        int x = 28;
        int z = -3;
        // all sonics both on and off states
        for (SonicScrewdriver ss : SonicScrewdriver.values()) {
            for (int i = 0; i < 2; i++) {
                Location location = new Location(world, rx + x, 65, rz + z);
                setItemFromMaterial(location, i == 0 ? ss.getModel() : ss.getActive(), Component.text(ss.getName()));
                x += 2;
                if (x > 48) {
                    x = 28;
                    z -= 2;
                }
            }
        }
        // all keys
        for (GUIKeyPreferences key : GUIKeyPreferences.values()) {
            if (key.getSlot() < 17) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set block at location
                setItemFromMaterial(location, key.getMaterial(), key.getModel(), Component.text(key.getName()));
                x += 2;
                if (x > 48) {
                    x = 28;
                    z -= 2;
                }
            }
        }
    }

    public void monsters() {
        if (!plugin.getConfig().getBoolean("modules.weeping_angels")) {
            return;
        }
        int x = 3;
        int z = -3;
        for (Monster monster : Monster.values()) {
            if (monster != Monster.DALEK) {
                Location location = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
                // equip armour stands
                new ArmourStandEquipment().setStandEquipment(stand, monster, (monster == Monster.EMPTY_CHILD));
                x += 3;
                if (x > 24) {
                    x = 3;
                    z -= 3;
                }
            }
            if (monster == Monster.CYBERMAN) {
                for (int c = 0; c < 9; c++) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // add all colours
                    switch (c) {
                        case 0 -> setHelmet(as, CybermanVariant.CYBERMAN_RISE_STATIC.getKey());
                        case 1 -> setHelmet(as, CybermanVariant.CYBER_LORD_STATIC.getKey());
                        case 2 -> setHelmet(as, CybermanVariant.BLACK_CYBERMAN_STATIC.getKey());
                        case 3 -> setHelmet(as, CybermanVariant.CYBERMAN_EARTHSHOCK_STATIC.getKey());
                        case 4 -> setHelmet(as, CybermanVariant.CYBERMAN_INVASION_STATIC.getKey());
                        case 5 -> setHelmet(as, CybermanVariant.CYBERMAN_MOONBASE_STATIC.getKey());
                        case 6 -> setHelmet(as, CybermanVariant.CYBERMAN_TENTH_PLANET_STATIC.getKey());
                        case 7 -> setHelmet(as, CybermanVariant.WOOD_CYBERMAN_STATIC.getKey());
                    }
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
            }
            if (monster == Monster.MIRE || monster == Monster.JUDOON || monster == Monster.SLITHEEN || monster == Monster.HEADLESS_MONK || monster == Monster.CLOCKWORK_DROID || monster == Monster.SILENT) {
                Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                new ArmourStandEquipment().setStandEquipment(as, monster, false);
                // set helmet to alternate version
                switch (monster) {
                    case CLOCKWORK_DROID -> setHelmet(as, DroidVariant.CLOCKWORK_DROID_FEMALE_STATIC.getKey());
                    case HEADLESS_MONK -> setHelmet(as, MonkVariant.HEADLESS_MONK_ALTERNATE.getKey());
                    case JUDOON -> setHelmet(as, JudoonVariant.JUDOON_GUARD.getKey());
                    case MIRE -> setHelmet(as, MireVariant.THE_MIRE_HELMETLESS.getKey());
                    case SILENT -> setHelmet(as, SilentVariant.SILENT_BEAMING.getKey());
                    case SLITHEEN -> setHelmet(as, SlitheenVariant.SLITHEEN_SUIT.getKey());
                }
                x += 3;
                if (x > 24) {
                    x = 3;
                    z -= 3;
                }
            }
            if (monster == Monster.DALEK) {
                for (int c = 0; c < 16; c++) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // add all colours
                    switch (c) {
                        case 0 -> setHelmet(as, DalekVariant.DALEK_WHITE.getKey());
                        case 1 -> setHelmet(as, DalekVariant.DALEK_ORANGE.getKey());
                        case 2 -> setHelmet(as, DalekVariant.DALEK_MAGENTA.getKey());
                        case 3 -> setHelmet(as, DalekVariant.DALEK_LIGHT_BLUE.getKey());
                        case 4 -> setHelmet(as, DalekVariant.DALEK_YELLOW.getKey());
                        case 5 -> setHelmet(as, DalekVariant.DALEK_LIME.getKey());
                        case 6 -> setHelmet(as, DalekVariant.DALEK_PINK.getKey());
                        case 7 -> setHelmet(as, DalekVariant.DALEK_GRAY.getKey());
                        case 8 -> setHelmet(as, DalekVariant.DALEK_LIGHT_GRAY.getKey());
                        case 9 -> setHelmet(as, DalekVariant.DALEK_CYAN.getKey());
                        case 10 -> setHelmet(as, DalekVariant.DALEK_PURPLE.getKey());
                        case 11 -> setHelmet(as, DalekVariant.DALEK_BLUE.getKey());
                        case 12 -> setHelmet(as, DalekVariant.DALEK_BROWN.getKey());
                        case 13 -> setHelmet(as, DalekVariant.DALEK_GREEN.getKey());
                        case 14 -> setHelmet(as, DalekVariant.DALEK_RED.getKey());
                        case 15 -> setHelmet(as, DalekVariant.DALEK_BLACK.getKey());
                    }
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
            }
            if (monster == Monster.OOD) {
                for (int c = 411; c < 436; c += 6) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set colour and red eye variants
                    switch (c) {
                        case 411 -> setHelmet(as, OodVariant.OOD_REDEYE_BLACK_STATIC.getKey());
                        case 417 -> setHelmet(as, OodVariant.OOD_BLUE_STATIC.getKey());
                        case 423 -> setHelmet(as, OodVariant.OOD_REDEYE_BLUE_STATIC.getKey());
                        case 429 -> setHelmet(as, OodVariant.OOD_BROWN_STATIC.getKey());
                        case 435 -> setHelmet(as, OodVariant.OOD_REDEYE_BROWN_STATIC.getKey());
                    }
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
            }
        }
    }

    private void setHelmet(ArmorStand as, NamespacedKey key) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            EntityEquipment ee = as.getEquipment();
            ItemStack head = ee.getHelmet();
            ItemMeta meta = head.getItemMeta();
            meta.setItemModel(key);
            head.setItemMeta(meta);
            ee.setHelmet(head);
            // remember item stack for chest population
            stacks.add(head);
        }, 2L);
    }

    public void chameleon() {
        // all 4 states need 3x3 with empty blocks between
        // ~ 22
        int x = -4;
        int r = 0;
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.usesArmourStand() && preset != ChameleonPreset.ITEM) {
                for (int z = -4 - r; z > -17 - r; z -= 4) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    EntityEquipment ee = as.getEquipment();
                    Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, -1, true);
                    ItemStack head = ItemStack.of(dye, 1);
                    ItemMeta meta = head.getItemMeta();
                    switch (z) {
                        case -4, -20 -> meta.setItemModel(preset.getClosed());
                        case -8, -24 -> meta.setItemModel(preset.getOpen());
                        case -12, -28 -> meta.setItemModel(preset.getStained());
                        // -16 & -32
                        default -> meta.setItemModel(preset.getGlass());
                    }
                    head.setItemMeta(meta);
                    ee.setHelmet(head);
                    as.setInvisible(true);
                    // remember item stack for chest population
                    stacks.add(head);
                }
                x -= 4;
                if (x < -49) {
                    x = -4;
                    r += 16;
                }
            }
        }
        for (String c : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            // custom models
            for (int z = -4 - r; z > -17 - r; z -= 4) {
                Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                EntityEquipment ee = as.getEquipment();
                Material material = Material.valueOf(plugin.getCustomModelConfig().getString("models." + c + ".item"));
                ItemStack head = ItemStack.of(material, 1);
                ItemMeta meta = head.getItemMeta();
                NamespacedKey key;
                switch (z) {
                    case -4, -20 -> key = new NamespacedKey(plugin, TARDISStringUtils.toUnderscoredLowercase(c) + "_closed");
                    case -8, -24 -> key = new NamespacedKey(plugin, TARDISStringUtils.toUnderscoredLowercase(c) + "_open");
                    case -12, -28 -> key = new NamespacedKey(plugin, TARDISStringUtils.toUnderscoredLowercase(c) + "_stained");
                    // -16 & -32
                    default -> key = new NamespacedKey(plugin, TARDISStringUtils.toUnderscoredLowercase(c) + "_glass");
                }
                meta.setItemModel(key);
                head.setItemMeta(meta);
                ee.setHelmet(head);
                as.setInvisible(true);
                // remember item stack for chest population
                stacks.add(head);
            }
            x -= 4;
            if (x < -49) {
                x = -4;
                r += 16;
            }
        }
    }

    public void rotors() {
        int x = 28;
        int z = 2;
        for (Rotor rotor : Rotor.byCustomModel.values()) {
            Location location = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
            // set item frame
            ItemFrame frame = (ItemFrame) world.spawnEntity(location, EntityType.ITEM_FRAME);
            frame.setFacingDirection(BlockFace.UP, true);
            // set item
            ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(rotor.offModel());
            is.setItemMeta(im);
            frame.setItem(is);
            // lock
            frame.setFixed(true);
            // invisible
            frame.setVisible(false);
            x += 4;
            if (x > 50) {
                x = 28;
                z += 4;
            }
            // remember item stack for chest population
            stacks.add(is);
        }
    }

    public void doors() {
        // all states
        int x = -27;
        int z = 2;
        for (TARDISDisplayItem tdi : TARDISDisplayItemRegistry.values()) {
            if (tdi.toString().contains("DOOR") && tdi != TARDISBlockDisplayItem.CUSTOM_DOOR) {
                // set display item at location
                Location location = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
                ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
                Material material = (tdi.toString().contains("OPEN")) ? tdi.getMaterial() : tdi.getCraftMaterial();
                ItemStack is = ItemStack.of(material);
                ItemMeta im = is.getItemMeta();
                im.setItemModel(tdi.getCustomModel());
                is.setItemMeta(im);
                display.setItemStack(is);
                // loop x z - spaced over 24 x 24 with empty blocks between
                x -= 3;
                if (x < -50) {
                    x = -27;
                    z += 3;
                }
            }
        }
        // custom doors
        for (String door : plugin.getCustomDoorsConfig().getKeys(false)) {
            Door d = Door.byName.get("DOOR_" + door.toUpperCase(Locale.ROOT));
            String key = TARDISStringUtils.toUnderscoredLowercase(door);
            plugin.debug(key);
            // closed state
            Location closed = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
            ItemDisplay c = (ItemDisplay) world.spawnEntity(closed, EntityType.ITEM_DISPLAY);
            Material material = d.getMaterial();
            ItemStack is = ItemStack.of(material);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(new NamespacedKey(plugin, key + "_closed"));
            is.setItemMeta(im);
            // remember item stack for chest population
            stacks.add(is);
            c.setItemStack(is);
            x -= 3;
            if (x < -48) {
                x = -27;
                z += 3;
            }
            // open state
            Location open = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
            ItemDisplay o = (ItemDisplay) world.spawnEntity(open, EntityType.ITEM_DISPLAY);
            ItemStack ois = ItemStack.of(material);
            ItemMeta oim = is.getItemMeta();
            im.setItemModel(new NamespacedKey(plugin, key + "_open"));
            ois.setItemMeta(oim);
            o.setItemStack(ois);
            // remember item stack for chest population
            stacks.add(ois);
            x -= 3;
            if (x < -48) {
                x = -27;
                z += 3;
            }
            // extra state
            if (d.hasExtra()) {
                Location extra = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
                ItemDisplay e = (ItemDisplay) world.spawnEntity(extra, EntityType.ITEM_DISPLAY);
                ItemStack eis = ItemStack.of(material);
                ItemMeta eim = is.getItemMeta();
                eim.setItemModel(new NamespacedKey(plugin, key + "_extra"));
                eis.setItemMeta(eim);
                e.setItemStack(eis);
                // remember item stack for chest population
                stacks.add(eis);
                x -= 3;
                if (x < -48) {
                    x = -27;
                    z += 3;
                }
            }
        }
    }

    public void consoles() {
        int x = -52;
        int z = 4;
        for (String colour : TARDISConstants.COLOURS) {
            Block block = world.getBlockAt(rx + x, 65, rz + z);
            block.setType(Material.WHITE_CONCRETE);
            new ConsoleBuilder(plugin).create(block, colour, -1, UUID.randomUUID().toString());
            x -= 6;
            if (x < -76) {
                x = -52;
                z += 6;
            }
        }
    }

    public void gui() {
        int x = -2;
        int z = 27;
        for (GuiPreview gui : DebugGUI.ICONS) {
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromMaterial(location, gui.material(), gui.model(), Component.text(gui.name()));
            // loop x z 48 x 24 blocks with empty blocks between
            x -= 2;
            if (x < -48) {
                x = -2;
                z += 2;
            }
        }
    }

    private void setItemFromMaterial(Location location, Material material, NamespacedKey model, Component name) {
        location.getBlock().setType(Material.WHITE_CONCRETE);
        ItemStack is = ItemStack.of(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setItemModel(model);
        im.displayName(name);
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setInvulnerable(true);
        // label
        addLabel(location, name);
        // remember item stack for chest population
        stacks.add(is);
    }

    private void setItemFromMaterial(Location location, List<Float> floats, Component name) {
        location.getBlock().setType(Material.WHITE_CONCRETE);
        ItemStack is = ItemStack.of(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        CustomModelDataComponent cmd = im.getCustomModelDataComponent();
        cmd.setFloats(floats);
        im.setCustomModelDataComponent(cmd);
        im.displayName(name);
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setInvulnerable(true);
        // label
        addLabel(location, name);
        // remember item stack for chest population
        stacks.add(is);
    }

    private void setItemFromStack(Location location, ItemStack is, Component name) {
        // remember item stack for chest population
        stacks.add(is);
        location.getBlock().setType(Material.WHITE_CONCRETE);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setInvulnerable(true);
        // label
        addLabel(location, name);
    }

    private void addLabel(Location location, Component name) {
        TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
        text.setAlignment(TextDisplay.TextAlignment.CENTER);
        text.text(name);
        text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
        text.setBillboard(Display.Billboard.VERTICAL);
    }

    public void chemistry() {
        int x = 3;
        int z = 27;
        for (LabEquipment le : LabEquipment.values()) {
            Location location = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
            // set item frame
            ItemFrame frame = (ItemFrame) world.spawnEntity(location, EntityType.ITEM_FRAME);
            frame.setFacingDirection(BlockFace.UP, true);
            // set item
            ItemStack is = ItemStack.of(le.getMaterial());
            ItemMeta im = is.getItemMeta();
            im.setItemModel(le.getModel());
            is.setItemMeta(im);
            frame.setItem(is);
            // lock
            frame.setFixed(true);
            // invisible
            frame.setVisible(false);
            // remember item stack for chest population
            stacks.add(is);
            x += 3;
            if (x > 24) {
                x = 3;
                z += 3;
            }
        }
        List<Material> slides = List.of(Material.GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS);
        String[] names = new String[]{"Slide", "Scope View", "Screen"};
        NamespacedKey[] keys = new NamespacedKey[]{ChemistryEquipment.GLASS_SLIDE.getKey(), ChemistryEquipment.FOLDER.getKey(), ChemistryEquipment.SCREEN.getKey()};
        int s = 0;
        for (Material material : slides) {
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromMaterial(location, material, keys[s], Component.text(names[s]));
            s++;
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
        for (Compound compound : Compound.values()) {
            ItemStack chemical = CompoundBuilder.getCompound(compound);
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromStack(location, chemical, Component.text(compound.getName()));
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
        for (Lab lab : Lab.values()) {
            ItemStack chemical = LabBuilder.getLabProduct(lab);
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromStack(location, chemical, Component.text(lab.getName()));
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
        for (Product product : Product.values()) {
            ItemStack chemical = ProductBuilder.getProduct(product);
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromStack(location, chemical, Component.text(product.getName()));
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
        for (Element element : Element.values()) {
            ItemStack chemical = ElementBuilder.getElement(element);
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromStack(location, chemical, Component.text(element.toString()));
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
    }

    public void regeneration() {
        int x = 3;
        int z = -30;
        // regeneration items
        ItemStack elixir = ElixirOfLife.create();
        Location loc = new Location(world, rx + x, 65, rz + z);
        // set block at location
        setItemFromStack(loc, elixir, Component.text("Elixir of Life"));
        x += 5;
        Block block = world.getBlockAt(rx + x, 65, rz + z);
        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.UNTEMPERED_SCHISM, block, -1);
        x += 5;
        // regeneration poses
        for (RegenerationVariant t : RegenerationVariant.values()) {
            Location location = new Location(world, rx + x + 0.5d, 65.725, rz + z + 0.5d);
            // create the regeneration item model
            ItemStack totem = ItemStack.of(Material.TOTEM_OF_UNDYING, 1);
            ItemMeta im = totem.getItemMeta();
            im.setItemModel(t.getKey());
            totem.setItemMeta(im);
            // spawn a display entity
            ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
            display.setItemStack(totem);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            // remember item stack for chest population
            stacks.add(totem);
            x += 4;
            if (x > 24) {
                x = 3;
                z -= 3;
            }
        }
    }

    public void handles() {
        int x = 27;
        int z = -27;
        for (GuiPreview gui : DebugHandles.ICONS) {
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromMaterial(location, gui.material(), gui.model(), Component.text(gui.name()));
            x += 2;
            if (x > 48) {
                x = 27;
                z -= 2;
            }
        }
    }

    public void lazarus() {
        int x = 27;
        int z = 27;
        for (GuiPreview gui : DebugLazarus.ICONS) {
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromMaterial(location, gui.material(), gui.model(), Component.text(gui.name()));
            x += 2;
            if (x > 48) {
                x = 27;
                z += 2;
            }
        }
    }

    public void setChests() {
        // fill chests with every TARDIS item
        Location location = new Location(world, rx + 52, 65, rz);
        int chests = (stacks.size() / 27) + 1;
        // place some chests
        for (int i = 0; i < chests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        for (ItemStack is : stacks) {
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            chest.getBlockInventory().addItem(is);
            count++;
        }
    }
}
