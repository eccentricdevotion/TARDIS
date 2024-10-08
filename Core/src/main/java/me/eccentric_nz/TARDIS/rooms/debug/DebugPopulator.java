package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodeldata.GUIKeyPreferences;
import me.eccentric_nz.TARDIS.custommodeldata.GUISonicPreferences;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.rotors.Rotor;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DebugPopulator {

    private final TARDIS plugin;
    private final World world;
    private final int rx = -1552;
    private final int rz = -9744;

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
                setItemFromMaterial(location, item.getMaterial(), item.getCustomModelData(), item.getDisplayName());
                // loop x z 24 x 24 blocks with empty blocks between
                x -= 2;
                if (x < -24) {
                    x = -2;
                    z += 2;
                }
            }
        }
        int[] data = new int[]{20001962, 20001963, 20001964, 20001965, 20001966, 20001967, 20001973, 20001974, 20001975, 20001976, 20001977, 20001978, 20001980, 20001981, 20001983};
        String[] names = new String[]{"Telepathic", "Stattenheim", "Materialisation", "Locator", "Chameleon", "Sonic", "ARS", "Temporal", "Memory", "Input", "Scanner", "Perception", "Random", "Invisibility", "Rift"};
        int c = 0;
        for (String damaged : names) {
            Location location = new Location(world, rx + x, 65, rz + z);
            setItemFromMaterial(location, Material.GLOWSTONE_DUST, data[c], "Damaged " + damaged + " Circuit");
            c++;
            x -= 2;
            if (x < -24) {
                x = -2;
                z += 2;
            }
        }
        // rust/acid buckets, area disk
        Material[] materials = new Material[]{Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.MUSIC_DISC_BLOCKS};
        int[] cmd = new int[]{1, 1, 10000001};
        String[] misc = new String[]{"Acid Bucket", "Rust Bucket", "Area Storage Disk"};
        int b = 0;
        for (String m : misc) {
            Location location = new Location(world, rx + x, 65, rz + z);
            setItemFromMaterial(location, materials[b], cmd[b], m);
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
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            // 122 blocks - surgery room x-ray
            if (tdi != TARDISDisplayItem.NONE && tdi != TARDISDisplayItem.PANDORICA && tdi != TARDISDisplayItem.UNTEMPERED_SCHISM && tdi.getCustomModelData() != -1 && !tdi.toString().contains("DOOR")) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set display item at location
                TARDISDisplayItemUtils.set(tdi, world, rx + x, 65, rz + z);
                //labels
                addLabel(location, tdi.getDisplayName());
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
        for (GUISonicPreferences sonic : GUISonicPreferences.values()) {
            if (sonic.getMaterial() == Material.BLAZE_ROD && sonic != GUISonicPreferences.COLOUR) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set block at location
                setItemFromMaterial(location, sonic.getMaterial(), sonic.getCustomModelData(), sonic.getName());
                x += 2;
                if (x > 48) {
                    x = 28;
                    z -= 2;
                }
            }
        }
        for (GUIKeyPreferences key : GUIKeyPreferences.values()) {
            if (key.getSlot() < 17) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set block at location
                setItemFromMaterial(location, key.getMaterial(), key.getCustomModelData(), key.getName());
                x += 2;
                if (x > 48) {
                    x = 28;
                    z -= 2;
                }
            }
        }
    }

    public void monsters() {
        int x = 3;
        int z = -3;
        for (Monster monster : Monster.values()) {
            if (monster != Monster.FLYER) {
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
                if (monster == Monster.HEADLESS_MONK) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set helmet to sword version
                    setHelmet(as, 405);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.MIRE || monster == Monster.SLITHEEN) {
                    // set no helmet!
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set helmet to sword version
                    setHelmet(as, 5);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.CLOCKWORK_DROID) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set female
                    setHelmet(as, 7);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.DALEK) {
                    for (int c = 0; c < 17; c++) {
                        Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                        ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                        new ArmourStandEquipment().setStandEquipment(as, monster, false);
                        // set helmet to sword version
                        setHelmet(as, 10000005 + c);
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
                        setHelmet(as, c);
                        x += 3;
                        if (x > 24) {
                            x = 3;
                            z -= 3;
                        }
                    }
                }
            }
        }
    }

    private void setHelmet(ArmorStand as, int cmd) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            EntityEquipment ee = as.getEquipment();
            ItemStack head = ee.getHelmet();
            ItemMeta meta = head.getItemMeta();
            meta.setCustomModelData(cmd);
            head.setItemMeta(meta);
            ee.setHelmet(head);
        }, 2L);
    }

    public void chameleon() {
        // all 4 states need 3x3 with empty blocks between
        // ~ 22
        int x = -4;
        int r = 0;
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.usesArmourStand() && preset != ChameleonPreset.ITEM) {
                int cmd = 1001;
                for (int z = -4 - r; z > -17 - r; z -= 4) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    EntityEquipment ee = as.getEquipment();
                    Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, -1, true);
                    ItemStack head = new ItemStack(dye, 1);
                    ItemMeta meta = head.getItemMeta();
                    meta.setCustomModelData(cmd);
                    head.setItemMeta(meta);
                    ee.setHelmet(head);
                    as.setInvisible(true);
                    cmd++;
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
            int cmd = 1001;
            for (int z = -4 - r; z > -17 - r; z -= 4) {
                Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                EntityEquipment ee = as.getEquipment();
                Material material = Material.valueOf(plugin.getCustomModelConfig().getString("models." + c + ".item"));
                ItemStack head = new ItemStack(material, 1);
                ItemMeta meta = head.getItemMeta();
                meta.setCustomModelData(cmd);
                head.setItemMeta(meta);
                ee.setHelmet(head);
                as.setInvisible(true);
                cmd++;
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
        for (Rotor rotor : Rotor.byCustomModelData.values()) {
            Location location = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
            // set item frame
            ItemFrame frame = (ItemFrame) world.spawnEntity(location, EntityType.ITEM_FRAME);
            frame.setFacingDirection(BlockFace.UP, true);
            // set item
            ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE);
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(rotor.getOffModelData());
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
        }
    }

    public void doors() {
        // all states
        int x = -27;
        int z = 2;
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            if (tdi.toString().contains("DOOR") && tdi != TARDISDisplayItem.CUSTOM_DOOR) {
                // set display item at location
                Location location = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
                ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
                Material material = (tdi.toString().contains("OPEN")) ? tdi.getMaterial() : tdi.getCraftMaterial();
                ItemStack is = new ItemStack(material);
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(tdi.getCustomModelData());
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
            // closed state
            Location closed = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
            ItemDisplay c = (ItemDisplay) world.spawnEntity(closed, EntityType.ITEM_DISPLAY);
            Material material = d.getMaterial();
            ItemStack is = new ItemStack(material);
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000);
            is.setItemMeta(im);
            c.setItemStack(is);
            x -= 3;
            if (x < -48) {
                x = -27;
                z += 3;
            }
            Location open = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
            ItemDisplay o = (ItemDisplay) world.spawnEntity(open, EntityType.ITEM_DISPLAY);
            ItemStack ois = new ItemStack(material);
            ItemMeta oim = is.getItemMeta();
            oim.setCustomModelData(10000 + d.getFrames()[d.getFrames().length - 1]);
            ois.setItemMeta(oim);
            o.setItemStack(ois);
            x -= 3;
            if (x < -48) {
                x = -27;
                z += 3;
            }
            // open state
            if (d.hasExtra()) {
                Location extra = new Location(world, rx + x + 0.5d, 65.5d, rz + z + 0.5d);
                ItemDisplay e = (ItemDisplay) world.spawnEntity(extra, EntityType.ITEM_DISPLAY);
                ItemStack eis = new ItemStack(material);
                ItemMeta eim = is.getItemMeta();
                eim.setCustomModelData(10000 + d.getFrames().length);
                eis.setItemMeta(eim);
                e.setItemStack(eis);
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
        for (int i = 1; i < 18; i++) {
            Block block = world.getBlockAt(rx + x, 65, rz + z);
            block.setType(Material.WHITE_CONCRETE);
            new ConsoleBuilder(plugin).create(block, i, -1, UUID.randomUUID().toString());
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
            setItemFromMaterial(location, gui.material(), gui.customModelData(), gui.name());
            // loop x z 48 x 24 blocks with empty blocks between
            x -= 2;
            if (x < -48) {
                x = -2;
                z += 2;
            }
        }
    }

    private void setItemFromMaterial(Location location, Material material, int cmd, String name) {
        location.getBlock().setType(Material.WHITE_CONCRETE);
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(cmd);
        im.setDisplayName(name);
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setInvulnerable(true);
        // label
        addLabel(location, name);
    }

    private void setItemFromStack(Location location, ItemStack is, String name) {
        location.getBlock().setType(Material.WHITE_CONCRETE);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setInvulnerable(true);
        // label
        addLabel(location, name);
    }

    private void addLabel(Location location, String name) {
        TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
        text.setAlignment(TextDisplay.TextAlignment.CENTER);
        text.setText(name);
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
            ItemStack is = new ItemStack(le.material);
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000);
            is.setItemMeta(im);
            frame.setItem(is);
            // lock
            frame.setFixed(true);
            // invisible
            frame.setVisible(false);
            x += 3;
            if (x > 24) {
                x = 3;
                z += 3;
            }
        }
        List<Material> slides = List.of(Material.GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS);
        String[] names = new String[]{"Slide", "Scope View", "Screen"};
        int s = 0;
        for (Material material : slides) {
            Location location = new Location(world, rx + x, 65, rz + z);
            // set block at location
            setItemFromMaterial(location, material, 9999, names[s]);
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
            setItemFromStack(location, chemical, compound.getName());
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
            setItemFromStack(location, chemical, lab.getName());
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
            setItemFromStack(location, chemical, product.getName());
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
            setItemFromStack(location, chemical, element.toString());
            x += 2;
            if (x > 24) {
                x = 3;
                z += 2;
            }
        }
    }

    public void regeneration() {
        int x = 3;
        int z = -27;
        // regeneration items
        ItemStack elixir = ElixirOfLife.create();
        Location loc = new Location(world, rx + x, 65, rz + z);
        // set block at location
        setItemFromStack(loc, elixir, "Elixir of Life");
        x += 5;
        Block block = world.getBlockAt(rx + x, 65, rz + z);
        TARDISDisplayItemUtils.set(TARDISDisplayItem.UNTEMPERED_SCHISM, block, -1);
        x += 5;
        // regeneration poses
        for (int p = 1001; p < 1017; p++) {
            Location location = new Location(world, rx + x + 0.5d, 65.725, rz + z + 0.5d);
            // create the regeneration item model
            ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
            ItemMeta im = totem.getItemMeta();
            im.setCustomModelData(p);
            totem.setItemMeta(im);
            // spawn a display entity
            ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
            display.setItemStack(totem);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
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
            setItemFromMaterial(location, gui.material(), gui.customModelData(), gui.name());
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
            setItemFromMaterial(location, gui.material(), gui.customModelData(), gui.name());
            x += 2;
            if (x > 48) {
                x = 27;
                z += 2;
            }
        }
    }
}
