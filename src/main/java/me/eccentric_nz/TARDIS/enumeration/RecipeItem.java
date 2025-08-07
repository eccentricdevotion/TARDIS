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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum RecipeItem {
    // shaped recipes start here
    ACID_BATTERY(Whoniverse.ACID_BATTERY.getKey(), RecipeCategory.PLANETS),
    ARTRON_CAPACITOR(Whoniverse.ARTRON_CAPACITOR.getKey(), RecipeCategory.ITEMS),
    ARTRON_CAPACITOR_STORAGE(Whoniverse.EYE_STORAGE.getKey(), RecipeCategory.ITEMS),
    ARTRON_STORAGE_CELL(Whoniverse.ARTRON_BATTERY.getKey(), RecipeCategory.ITEMS),
    AUTHORISED_CONTROL_DISK(DiskVariant.CONTROL_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BIO_SCANNER_CIRCUIT(CircuitVariant.BIO.getKey(), RecipeCategory.SONIC_CIRCUITS),
    BLANK_STORAGE_DISK(DiskVariant.BLANK_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BRUSH_CIRCUIT(CircuitVariant.BRUSH.getKey(), RecipeCategory.SONIC_CIRCUITS),
    CONSOLE_LAMP(null, RecipeCategory.CUSTOM_BLOCKS),
    CONSOLE_LAMP_SWITCH(ModelledControl.CONSOLE_LAMP_0.getKey(), RecipeCategory.MISC),
    CONVERSION_CIRCUIT(CircuitVariant.CONVERSION.getKey(), RecipeCategory.SONIC_CIRCUITS),
    CUSTARD_CREAM(Food.CUSTARD_CREAM.getKey(), RecipeCategory.FOOD),
    DIAMOND_DISRUPTOR_CIRCUIT(CircuitVariant.DIAMOND.getKey(), RecipeCategory.SONIC_CIRCUITS),
    ELIXIR_OF_LIFE(Whoniverse.ELIXIR_OF_LIFE.getKey(),RecipeCategory.ACCESSORIES),
    EMERALD_ENVIRONMENT_CIRCUIT(CircuitVariant.EMERALD.getKey(), RecipeCategory.SONIC_CIRCUITS),
    EXTERIOR_LAMP_LEVEL_SWITCH(ModelledControl.LAMP_0.getKey(), RecipeCategory.MISC),
    FISH_FINGER(Food.FISH_FINGER.getKey(), RecipeCategory.FOOD),
    FOB_WATCH(Whoniverse.FOB_WATCH.getKey(), RecipeCategory.ACCESSORIES),
    HANDLES(Whoniverse.HANDLES_OFF.getKey(), RecipeCategory.ACCESSORIES),
    IGNITE_CIRCUIT(CircuitVariant.IGNITE.getKey(), RecipeCategory.SONIC_CIRCUITS),
    INTERIOR_LIGHT_LEVEL_SWITCH(ModelledControl.LIGHT_0.getKey(), RecipeCategory.MISC),
    JAMMY_DODGER(Food.JAMMY_DODGER.getKey(), RecipeCategory.FOOD),
    KNOCKBACK_CIRCUIT(CircuitVariant.KNOCKBACK.getKey(), RecipeCategory.SONIC_CIRCUITS),
    MONITOR_FRAME(ModelledControl.MONITOR_FRAME_LEFT.getKey(), RecipeCategory.MISC),
    PAINTER_CIRCUIT(CircuitVariant.PAINTER.getKey(), RecipeCategory.SONIC_CIRCUITS),
    PAPER_BAG(Food.PAPER_BAG.getKey(), RecipeCategory.FOOD),
    PERCEPTION_CIRCUIT(CircuitVariant.PERCEPTION.getKey(), RecipeCategory.ITEM_CIRCUITS),
    PERCEPTION_FILTER(KeyVariant.PERCEPTION_FILTER.getKey(), RecipeCategory.ITEMS),
    PICKUP_ARROWS_CIRCUIT(CircuitVariant.PICKUP.getKey(), RecipeCategory.SONIC_CIRCUITS),
    REDSTONE_ACTIVATOR_CIRCUIT(CircuitVariant.REDSTONE.getKey(), RecipeCategory.SONIC_CIRCUITS),
    RIFT_CIRCUIT(CircuitVariant.RIFT.getKey(), RecipeCategory.PLANETS),
    RIFT_MANIPULATOR(null, RecipeCategory.PLANETS),
    RUST_PLAGUE_SWORD(Whoniverse.RUST_PLAGUE_SWORD.getKey(), RecipeCategory.PLANETS),
    SERVER_ADMIN_CIRCUIT(CircuitVariant.ADMIN.getKey(), RecipeCategory.SONIC_CIRCUITS),
    SONIC_DOCK(SonicItem.SONIC_DOCK_OFF.getKey(), RecipeCategory.SONIC_CIRCUITS),
    SONIC_GENERATOR(SonicItem.SONIC_GENERATOR.getKey(), RecipeCategory.ITEM_CIRCUITS),
    SONIC_OSCILLATOR(CircuitVariant.SONIC.getKey(), RecipeCategory.ITEM_CIRCUITS),
    SONIC_SCREWDRIVER(SonicVariant.ELEVENTH.getKey(), RecipeCategory.ITEMS),
    STATTENHEIM_REMOTE(Whoniverse.STATTENHEIM_REMOTE.getKey(), RecipeCategory.ITEMS),
    TARDIS_ARS_CIRCUIT(CircuitVariant.ARS.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_ARTRON_FURNACE(Whoniverse.ARTRON_FURNACE.getKey(), RecipeCategory.ITEMS),
    TARDIS_BIOME_READER(Whoniverse.BIOME_READER.getKey(), RecipeCategory.ITEMS),
    TARDIS_CHAMELEON_CIRCUIT(CircuitVariant.CHAMELEON.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_COMMUNICATOR(Whoniverse.COMMUNICATOR.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_INPUT_CIRCUIT(CircuitVariant.INPUT.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_INVISIBILITY_CIRCUIT(CircuitVariant.INVISIBILITY.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_KEY(KeyVariant.BRASS_YALE.getKey(), RecipeCategory.ITEMS),
    TARDIS_LOCATOR(Whoniverse.LOCATOR.getKey(), RecipeCategory.ITEMS),
    TARDIS_LOCATOR_CIRCUIT(CircuitVariant.LOCATOR.getKey(), RecipeCategory.ITEM_CIRCUITS),
    TARDIS_MATERIALISATION_CIRCUIT(CircuitVariant.MATERIALISATION.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MEMORY_CIRCUIT(CircuitVariant.MEMORY.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MONITOR(ModelledControl.MONITOR.getKey(), RecipeCategory.MISC),
    TARDIS_RANDOMISER_CIRCUIT(CircuitVariant.RANDOM.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_REMOTE_KEY(KeyVariant.REMOTE.getKey(), RecipeCategory.ITEMS),
    TARDIS_SCANNER_CIRCUIT(CircuitVariant.SCANNER.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_SPACE_HELMET(Whoniverse.HELMET.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_SPACE_SUIT_CHESTPLATE(Whoniverse.SPACE_SUIT_CHESTPLATE.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_SPACE_SUIT_LEGGINGS(Whoniverse.SPACE_SUIT_LEGGINGS.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_STATTENHEIM_CIRCUIT(CircuitVariant.STATTENHEIM.getKey(), RecipeCategory.ITEM_CIRCUITS),
    TARDIS_TELEPATHIC_CIRCUIT(CircuitVariant.TELEPATHIC.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_TELEVISION(Whoniverse.TV.getKey(), RecipeCategory.ITEMS),
    TARDIS_TEMPORAL_CIRCUIT(CircuitVariant.TEMPORAL.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    // rotors
    TIME_ROTOR_CONSOLE(RotorVariant.TIME_ROTOR_CONSOLE_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_RUSTIC(RotorVariant.TIME_ROTOR_RUSTIC_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_DELTA(RotorVariant.TIME_ROTOR_DELTA_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_EARLY(RotorVariant.TIME_ROTOR_EARLY_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ENGINE(RotorVariant.ENGINE_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_ENGINE(RotorVariant.ENGINE_ROTOR_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_HOSPITAL(RotorVariant.HOSPITAL_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_TENTH(RotorVariant.TIME_ROTOR_TENNANT_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_ELEVENTH(RotorVariant.TIME_ROTOR_ELEVENTH_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_TWELFTH(RotorVariant.TIME_ROTOR_TWELFTH_OFF.getKey(), RecipeCategory.ROTORS),
    // consoles
    LIGHT_GRAY_CONSOLE(ConsoleVariant.CONSOLE_LIGHT_GRAY.getKey(), RecipeCategory.CONSOLES),
    GRAY_CONSOLE(ConsoleVariant.CONSOLE_GRAY.getKey(), RecipeCategory.CONSOLES),
    WHITE_CONSOLE(ConsoleVariant.CONSOLE_WHITE.getKey(), RecipeCategory.CONSOLES),
    BLACK_CONSOLE(ConsoleVariant.CONSOLE_BLACK.getKey(), RecipeCategory.CONSOLES),
    RED_CONSOLE(ConsoleVariant.CONSOLE_RED.getKey(), RecipeCategory.CONSOLES),
    ORANGE_CONSOLE(ConsoleVariant.CONSOLE_ORANGE.getKey(), RecipeCategory.CONSOLES),
    YELLOW_CONSOLE(ConsoleVariant.CONSOLE_YELLOW.getKey(), RecipeCategory.CONSOLES),
    LIME_CONSOLE(ConsoleVariant.CONSOLE_LIME.getKey(), RecipeCategory.CONSOLES),
    GREEN_CONSOLE(ConsoleVariant.CONSOLE_GREEN.getKey(), RecipeCategory.CONSOLES),
    CYAN_CONSOLE(ConsoleVariant.CONSOLE_CYAN.getKey(), RecipeCategory.CONSOLES),
    LIGHT_BLUE_CONSOLE(ConsoleVariant.CONSOLE_LIGHT_BLUE.getKey(), RecipeCategory.CONSOLES),
    BLUE_CONSOLE(ConsoleVariant.CONSOLE_BLUE.getKey(), RecipeCategory.CONSOLES),
    PURPLE_CONSOLE(ConsoleVariant.CONSOLE_PURPLE.getKey(), RecipeCategory.CONSOLES),
    MAGENTA_CONSOLE(ConsoleVariant.CONSOLE_MAGENTA.getKey(), RecipeCategory.CONSOLES),
    PINK_CONSOLE(ConsoleVariant.CONSOLE_PINK.getKey(), RecipeCategory.CONSOLES),
    BROWN_CONSOLE(ConsoleVariant.CONSOLE_BROWN.getKey(), RecipeCategory.CONSOLES),
    RUSTIC_CONSOLE(ConsoleVariant.CONSOLE_RUSTIC.getKey(), RecipeCategory.CONSOLES),
    // bow ties
    WHITE_BOW_TIE(BowTieVariant.BOWTIE_WHITE.getKey()),
    ORANGE_BOW_TIE(BowTieVariant.BOWTIE_ORANGE.getKey()),
    MAGENTA_BOW_TIE(BowTieVariant.BOWTIE_MAGENTA.getKey()),
    LIGHT_BLUE_BOW_TIE(BowTieVariant.BOWTIE_LIGHT_BLUE.getKey()),
    YELLOW_BOW_TIE(BowTieVariant.BOWTIE_YELLOW.getKey()),
    LIME_BOW_TIE(BowTieVariant.BOWTIE_LIME.getKey()),
    PINK_BOW_TIE(BowTieVariant.BOWTIE_PINK.getKey()),
    GREY_BOW_TIE(BowTieVariant.BOWTIE_GRAY.getKey()),
    LIGHT_GREY_BOW_TIE(BowTieVariant.BOWTIE_LIGHT_GRAY.getKey()),
    CYAN_BOW_TIE(BowTieVariant.BOWTIE_CYAN.getKey()),
    PURPLE_BOW_TIE(BowTieVariant.BOWTIE_PURPLE.getKey()),
    BLUE_BOW_TIE(BowTieVariant.BOWTIE_BLUE.getKey()),
    BROWN_BOW_TIE(BowTieVariant.BOWTIE_BROWN.getKey()),
    GREEN_BOW_TIE(BowTieVariant.BOWTIE_GREEN.getKey()),
    RED_BOW_TIE(BowTieVariant.BOWTIE_RED.getKey(), RecipeCategory.ACCESSORIES),
    BLACK_BOW_TIE(BowTieVariant.BOWTIE_BLACK.getKey()),
    THREE_D_GLASSES(Whoniverse.THREE_D_GLASSES.getKey(), RecipeCategory.ACCESSORIES),
    // module recipes
    VORTEX_MANIPULATOR(Whoniverse.VORTEX_MANIPULATOR.getKey(), RecipeCategory.ACCESSORIES),
    SONIC_BLASTER(Whoniverse.BLASTER.getKey(), RecipeCategory.ACCESSORIES),
    BLASTER_BATTERY(Whoniverse.BLASTER_BATTERY.getKey(), RecipeCategory.MISC),
    LANDING_PAD(null, RecipeCategory.ACCESSORIES),
    JUDOON_AMMUNITION(ArrowVariant.JUDOON_AMMO.getKey(), RecipeCategory.MISC),
    K9(K9Variant.K9.getKey(), RecipeCategory.MISC),
    // custom block recipes start here
    UNTEMPERED_SCHISM(Schism.UNTEMPERED_SCHISM_BLOCK.getKey(), RecipeCategory.ITEMS),
    GROW(SeedBlock.GROW.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    BLUE_BOX(Wool.BLUE_BOX.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    COG(Wool.COG.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    HEXAGON(Wool.HEXAGON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL(Wool.ROUNDEL.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL_OFFSET(Wool.ROUNDEL_OFFSET.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    THE_MOMENT(Wool.THE_MOMENT.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_BULB(LampVariant.BULB_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC(LanternVariant.CLASSIC_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC_OFFSET(LanternVariant.CLASSIC_OFFSET_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TENTH(LampVariant.TENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_ELEVENTH(LampVariant.ELEVENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TWELFTH(LanternVariant.TWELFTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_THIRTEENTH(LanternVariant.THIRTEENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    DOOR(TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    BONE_DOOR(BoneDoorVariant.BONE_DOOR_CLOSED.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    CLASSIC_DOOR(ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey(), RecipeCategory.CUSTOM_BLOCKS),
//    HANDBRAKE(1001, RecipeCategory.CUSTOM_BLOCKS),
//    THROTTLE(1001, RecipeCategory.CUSTOM_BLOCKS),
    // unshaped recipes start here
    BIOME_STORAGE_DISK(DiskVariant.BIOME_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BOWL_OF_CUSTARD(Food.BOWL_OF_CUSTARD.getKey(), RecipeCategory.FOOD),
    PLAYER_STORAGE_DISK(DiskVariant.PLAYER_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    PRESET_STORAGE_DISK(DiskVariant.PRESET_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    SAVE_STORAGE_DISK(DiskVariant.SAVE_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    TARDIS_SCHEMATIC_WAND(Whoniverse.WAND.getKey(), RecipeCategory.MISC),
    // jelly babies
    VANILLA_JELLY_BABY(JellyBabyVariant.JELLY_BABY_WHITE.getKey()),
    ORANGE_JELLY_BABY(JellyBabyVariant.JELLY_BABY_ORANGE.getKey(), RecipeCategory.FOOD),
    WATERMELON_JELLY_BABY(JellyBabyVariant.JELLY_BABY_MAGENTA.getKey()),
    BUBBLEGUM_JELLY_BABY(JellyBabyVariant.JELLY_BABY_LIGHT_BLUE.getKey()),
    LEMON_JELLY_BABY(JellyBabyVariant.JELLY_BABY_YELLOW.getKey()),
    LIME_JELLY_BABY(JellyBabyVariant.JELLY_BABY_LIME.getKey()),
    STRAWBERRY_JELLY_BABY(JellyBabyVariant.JELLY_BABY_PINK.getKey()),
    EARL_GREY_JELLY_BABY(JellyBabyVariant.JELLY_BABY_GRAY.getKey()),
    VODKA_JELLY_BABY(JellyBabyVariant.JELLY_BABY_LIGHT_GRAY.getKey()),
    ISLAND_PUNCH_JELLY_BABY(JellyBabyVariant.JELLY_BABY_CYAN.getKey()),
    GRAPE_JELLY_BABY(JellyBabyVariant.JELLY_BABY_PURPLE.getKey()),
    BLUEBERRY_JELLY_BABY(JellyBabyVariant.JELLY_BABY_BLUE.getKey()),
    CAPPUCCINO_JELLY_BABY(JellyBabyVariant.JELLY_BABY_BROWN.getKey()),
    APPLE_JELLY_BABY(JellyBabyVariant.JELLY_BABY_GREEN.getKey()),
    RASPBERRY_JELLY_BABY(JellyBabyVariant.JELLY_BABY_RED.getKey()),
    LICORICE_JELLY_BABY(JellyBabyVariant.JELLY_BABY_BLACK.getKey()),
    // sonic upgrades
    ADMIN_UPGRADE(CircuitVariant.ADMIN.getKey(), RecipeCategory.SONIC_UPGRADES),
    BIO_SCANNER_UPGRADE(CircuitVariant.BIO.getKey(), RecipeCategory.SONIC_UPGRADES),
    BRUSH_UPGRADE(CircuitVariant.BRUSH.getKey(), RecipeCategory.SONIC_UPGRADES),
    REDSTONE_UPGRADE(CircuitVariant.REDSTONE.getKey(), RecipeCategory.SONIC_UPGRADES),
    DIAMOND_UPGRADE(CircuitVariant.DIAMOND.getKey(), RecipeCategory.SONIC_UPGRADES),
    EMERALD_UPGRADE(CircuitVariant.EMERALD.getKey(), RecipeCategory.SONIC_UPGRADES),
    PAINTER_UPGRADE(CircuitVariant.PAINTER.getKey(), RecipeCategory.SONIC_UPGRADES),
    IGNITE_UPGRADE(CircuitVariant.IGNITE.getKey(), RecipeCategory.SONIC_UPGRADES),
    PICKUP_ARROWS_UPGRADE(CircuitVariant.PICKUP.getKey(), RecipeCategory.SONIC_UPGRADES),
    KNOCKBACK_UPGRADE(CircuitVariant.KNOCKBACK.getKey(), RecipeCategory.SONIC_UPGRADES),
    CONVERSION_UPGRADE(CircuitVariant.CONVERSION.getKey(), RecipeCategory.SONIC_UPGRADES),
    // planet items
    ACID_BUCKET(Whoniverse.ACID_BUCKET.getKey(), RecipeCategory.UNCRAFTABLE),
    RUST_BUCKET(Whoniverse.RUST_BUCKET.getKey(), RecipeCategory.UNCRAFTABLE),
    // chemistry
    ATOMIC_ELEMENTS(ChemistryEquipment.CREATIVE.getKey(), RecipeCategory.CHEMISTRY),
    CHEMICAL_COMPOUNDS(ChemistryEquipment.COMPOUND.getKey(), RecipeCategory.CHEMISTRY),
    LAB_TABLE(ChemistryEquipment.LAB.getKey(), RecipeCategory.CHEMISTRY),
    PRODUCT_CRAFTING(ChemistryEquipment.PRODUCT.getKey(), RecipeCategory.CHEMISTRY),
    MATERIAL_REDUCER(ChemistryEquipment.REDUCER.getKey(), RecipeCategory.CHEMISTRY),
    ELEMENT_CONSTRUCTOR(ChemistryEquipment.CONSTRUCTOR.getKey(), RecipeCategory.CHEMISTRY),
    BLUE_LAMP(LampVariant.BLUE_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    GREEN_LAMP(LampVariant.GREEN_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    PURPLE_LAMP(LampVariant.PURPLE_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    RED_LAMP(LampVariant.RED_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    HEAT_BLOCK(ChemistryEquipment.HEAT_BLOCK.getKey(), RecipeCategory.CHEMISTRY),
    BALLOON(Balloon.RED_BALLOON.getKey(), RecipeCategory.CHEMISTRY),
    BLEACH(ChemistryEquipment.BLEACH.getKey(), RecipeCategory.CHEMISTRY),
    GLOW_STICK(GlowStickVariant.ORANGE_GLOW_STICK.getKey(), RecipeCategory.CHEMISTRY),
    ICE_BOMB(ChemistryEquipment.ICE_BOMB.getKey(), RecipeCategory.CHEMISTRY),
    SPARKLER(Sparkler.SPARKLER_PURPLE.getKey(), RecipeCategory.CHEMISTRY),
    SUPER_FERTILISER(ChemistryEquipment.SUPER_FERTILISER.getKey(), RecipeCategory.CHEMISTRY),
    // microscope
    COMPUTER_MONITOR(ChemistryEquipment.COMPUTER_MONITOR.getKey(), RecipeCategory.MICROSCOPE),
    ELECTRON_MICROSCOPE(ChemistryEquipment.ELECTRON_MICROSCOPE.getKey(), RecipeCategory.MICROSCOPE),
    FILING_CABINET(ChemistryEquipment.FILING_CABINET_OPEN.getKey(), RecipeCategory.MICROSCOPE),
    MICROSCOPE(ChemistryEquipment.MICROSCOPE.getKey(), RecipeCategory.MICROSCOPE),
    SLIDE_RACK(ChemistryEquipment.SLIDE_RACK.getKey(), RecipeCategory.MICROSCOPE),
    TELESCOPE(ChemistryEquipment.TELESCOPE.getKey(), RecipeCategory.MICROSCOPE),
    // not found
    NOT_FOUND(null, RecipeCategory.UNCRAFTABLE);

    private final NamespacedKey model;
    private final RecipeCategory category;

    RecipeItem(NamespacedKey model, RecipeCategory category) {
        this.model = model;
        this.category = category;
    }

    RecipeItem(NamespacedKey model) {
        this.model = model;
        category = RecipeCategory.UNUSED;
    }

    public static RecipeItem getByName(String name) {
        String processed = TARDISStringUtils.toEnumUppercase(name);
        try {
            return RecipeItem.valueOf(processed);
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }
    }

    public NamespacedKey getModel() {
        return model;
    }

    public RecipeCategory getCategory() {
        return category;
    }

    public String toRecipeString() {
        return switch (this) {
            case THREE_D_GLASSES -> "3-D Glasses";
            case BIO_SCANNER_CIRCUIT -> "Bio-scanner Circuit";
            case BIO_SCANNER_UPGRADE -> "Bio-scanner Upgrade";
            case BOWL_OF_CUSTARD -> "Bowl of Custard";
            case ELIXIR_OF_LIFE -> "Elixir of Life";
            case TARDIS_ARS_CIRCUIT -> "TARDIS ARS Circuit";
            default -> TARDISStringUtils.capitalise(toString()).replace("Tardis", "TARDIS");
        };
    }

    public String toTabCompletionString() {
        String recipe = toRecipeString();
        if (this == THREE_D_GLASSES) {
            return "3-d-glasses";
        } else if (recipe.startsWith("TARDIS")) {
            return TARDISStringUtils.toLowercaseDashed(recipe).replace("tardis-", "");
        } else if (recipe.endsWith("Baby")) {
            return "jelly-baby";
        } else if (recipe.endsWith("Tie")) {
            return "bow-tie";
        } else {
            return TARDISStringUtils.toLowercaseDashed(recipe);
        }
    }
}
