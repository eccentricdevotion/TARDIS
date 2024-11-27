/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

public enum RecipeItem {
    // shaped recipes start here
    ACID_BATTERY(NetherBrick.ACID_BATTERY.getKey(), RecipeCategory.PLANETS),
    ARTRON_CAPACITOR(Bucket.ARTRON_CAPACITOR.getKey(), RecipeCategory.ITEMS),
    ARTRON_CAPACITOR_STORAGE(GrayShulkerBox.EYE_STORAGE.getKey(), RecipeCategory.ITEMS),
    ARTRON_STORAGE_CELL(Bucket.ARTRON_BATTERY.getKey(), RecipeCategory.ITEMS),
    AUTHORISED_CONTROL_DISK(MusicDisc.CONTROL_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BIO_SCANNER_CIRCUIT(GlowstoneDust.BIO.getKey(), RecipeCategory.SONIC_CIRCUITS),
    BLANK_STORAGE_DISK(MusicDisc.BLANK_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BRUSH_CIRCUIT(GlowstoneDust.BRUSH.getKey(), RecipeCategory.SONIC_CIRCUITS),
    CONSOLE_LAMP(null, RecipeCategory.CUSTOM_BLOCKS),
    CONSOLE_LAMP_SWITCH(Lever.CONSOLE_LAMP_0.getKey(), RecipeCategory.MISC),
    CONVERSION_CIRCUIT(GlowstoneDust.CONVERSION.getKey(), RecipeCategory.SONIC_CIRCUITS),
    CUSTARD_CREAM(Food.CUSTARD_CREAM.getKey(), RecipeCategory.FOOD),
    DIAMOND_DISRUPTOR_CIRCUIT(GlowstoneDust.DIAMOND.getKey(), RecipeCategory.SONIC_CIRCUITS),
    ELIXIR_OF_LIFE(GoldIngot.ELIXIR_OF_LIFE.getKey(),RecipeCategory.ACCESSORIES),
    EMERALD_ENVIRONMENT_CIRCUIT(GlowstoneDust.EMERALD.getKey(), RecipeCategory.SONIC_CIRCUITS),
    EXTERIOR_LAMP_LEVEL_SWITCH(Lever.LAMP_0.getKey(), RecipeCategory.MISC),
    FISH_FINGER(Food.FISH_FINGER.getKey(), RecipeCategory.FOOD),
    FOB_WATCH(Clock.FOB_WATCH.getKey(), RecipeCategory.ACCESSORIES),
    HANDLES(BirchButton.HANDLES_OFF.getKey(), RecipeCategory.ACCESSORIES),
    IGNITE_CIRCUIT(GlowstoneDust.IGNITE.getKey(), RecipeCategory.SONIC_CIRCUITS),
    INTERIOR_LIGHT_LEVEL_SWITCH(Lever.LIGHT_0.getKey(), RecipeCategory.MISC),
    JAMMY_DODGER(Food.JAMMY_DODGER.getKey(), RecipeCategory.FOOD),
    KNOCKBACK_CIRCUIT(GlowstoneDust.KNOCKBACK.getKey(), RecipeCategory.SONIC_CIRCUITS),
    MONITOR_FRAME(Glass.MONITOR_FRAME_LEFT.getKey(), RecipeCategory.MISC),
    PAINTER_CIRCUIT(GlowstoneDust.PAINTER.getKey(), RecipeCategory.SONIC_CIRCUITS),
    PAPER_BAG(Food.PAPER_BAG.getKey(), RecipeCategory.FOOD),
    PERCEPTION_CIRCUIT(GlowstoneDust.PERCEPTION.getKey(), RecipeCategory.ITEM_CIRCUITS),
    PERCEPTION_FILTER(GoldNugget.PERCEPTION_FILTER.getKey(), RecipeCategory.ITEMS),
    PICKUP_ARROWS_CIRCUIT(GlowstoneDust.PICKUP.getKey(), RecipeCategory.SONIC_CIRCUITS),
    REDSTONE_ACTIVATOR_CIRCUIT(GlowstoneDust.REDSTONE.getKey(), RecipeCategory.SONIC_CIRCUITS),
    RIFT_CIRCUIT(GlowstoneDust.RIFT.getKey(), RecipeCategory.PLANETS),
    RIFT_MANIPULATOR(null, RecipeCategory.PLANETS),
    RUST_PLAGUE_SWORD(IronSword.RUST_PLAGUE_SWORD.getKey(), RecipeCategory.PLANETS),
    SERVER_ADMIN_CIRCUIT(GlowstoneDust.ADMIN.getKey(), RecipeCategory.SONIC_CIRCUITS),
    SONIC_DOCK(FlowerPot.SONIC_DOCK.getKey(), RecipeCategory.SONIC_CIRCUITS),
    SONIC_GENERATOR(FlowerPot.SONIC_GENERATOR.getKey(), RecipeCategory.ITEM_CIRCUITS),
    SONIC_OSCILLATOR(GlowstoneDust.SONIC.getKey(), RecipeCategory.ITEM_CIRCUITS),
    SONIC_SCREWDRIVER(BlazeRod.ELEVENTH.getKey(), RecipeCategory.ITEMS),
    STATTENHEIM_REMOTE(Flint.STATTENHEIM_REMOTE.getKey(), RecipeCategory.ITEMS),
    TARDIS_ARS_CIRCUIT(GlowstoneDust.ARS.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_ARTRON_FURNACE(Furnace.ARTRON_FURNACE.getKey(), RecipeCategory.ITEMS),
    TARDIS_BIOME_READER(Brick.BIOME_READER.getKey(), RecipeCategory.ITEMS),
    TARDIS_CHAMELEON_CIRCUIT(GlowstoneDust.CHAMELEON.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_COMMUNICATOR(LeatherHelmet.COMMUNICATOR.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_INPUT_CIRCUIT(GlowstoneDust.INPUT.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_INVISIBILITY_CIRCUIT(GlowstoneDust.INVISIBILITY.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_KEY(GoldNugget.BRASS_YALE.getKey(), RecipeCategory.ITEMS),
    TARDIS_LOCATOR(Compass.LOCATOR.getKey(), RecipeCategory.ITEMS),
    TARDIS_LOCATOR_CIRCUIT(GlowstoneDust.LOCATOR.getKey(), RecipeCategory.ITEM_CIRCUITS),
    TARDIS_MATERIALISATION_CIRCUIT(GlowstoneDust.MATERIALISATION.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MEMORY_CIRCUIT(GlowstoneDust.MEMORY.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MONITOR(Map.MONITOR.getKey(), RecipeCategory.MISC),
    TARDIS_RANDOMISER_CIRCUIT(GlowstoneDust.RANDOM.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_REMOTE_KEY(GoldNugget.REMOTE.getKey(), RecipeCategory.ITEMS),
    TARDIS_SCANNER_CIRCUIT(GlowstoneDust.SCANNER.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_SPACE_HELMET(Glass.HELMET.getKey(), RecipeCategory.ACCESSORIES),
    TARDIS_STATTENHEIM_CIRCUIT(GlowstoneDust.STATTENHEIM.getKey(), RecipeCategory.ITEM_CIRCUITS),
    TARDIS_TELEPATHIC_CIRCUIT(GlowstoneDust.TELEPATHIC.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_TELEVISION(BrownStainedGlass.TV.getKey(), RecipeCategory.ITEMS),
    TARDIS_TEMPORAL_CIRCUIT(GlowstoneDust.TEMPORAL.getKey(), RecipeCategory.CONSOLE_CIRCUITS),
    // rotors
    TIME_ROTOR_CONSOLE(LightGrayDye.TIME_ROTOR_CONSOLE_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_RUSTIC(LightGrayDye.TIME_ROTOR_RUSTIC_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_DELTA(LightGrayDye.TIME_ROTOR_DELTA_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_EARLY(LightGrayDye.TIME_ROTOR_EARLY_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ENGINE(LightGrayDye.ENGINE_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_ENGINE(LightGrayDye.ENGINE_ROTOR_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_HOSPITAL(LightGrayDye.HOSPITAL_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_TENTH(LightGrayDye.TIME_ROTOR_TENNANT_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_ELEVENTH(LightGrayDye.TIME_ROTOR_ELEVENTH_OFF.getKey(), RecipeCategory.ROTORS),
    TIME_ROTOR_TWELFTH(LightGrayDye.TIME_ROTOR_TWELFTH_OFF.getKey(), RecipeCategory.ROTORS),
    // consoles
    LIGHT_GRAY_CONSOLE(ConsoleBlock.CONSOLE_LIGHT_GRAY.getKey(), RecipeCategory.CONSOLES),
    GRAY_CONSOLE(ConsoleBlock.CONSOLE_GRAY.getKey(), RecipeCategory.CONSOLES),
    WHITE_CONSOLE(ConsoleBlock.CONSOLE_WHITE.getKey(), RecipeCategory.CONSOLES),
    BLACK_CONSOLE(ConsoleBlock.CONSOLE_BLACK.getKey(), RecipeCategory.CONSOLES),
    RED_CONSOLE(ConsoleBlock.CONSOLE_RED.getKey(), RecipeCategory.CONSOLES),
    ORANGE_CONSOLE(ConsoleBlock.CONSOLE_ORANGE.getKey(), RecipeCategory.CONSOLES),
    YELLOW_CONSOLE(ConsoleBlock.CONSOLE_YELLOW.getKey(), RecipeCategory.CONSOLES),
    LIME_CONSOLE(ConsoleBlock.CONSOLE_LIME.getKey(), RecipeCategory.CONSOLES),
    GREEN_CONSOLE(ConsoleBlock.CONSOLE_GREEN.getKey(), RecipeCategory.CONSOLES),
    CYAN_CONSOLE(ConsoleBlock.CONSOLE_CYAN.getKey(), RecipeCategory.CONSOLES),
    LIGHT_BLUE_CONSOLE(ConsoleBlock.CONSOLE_LIGHT_BLUE.getKey(), RecipeCategory.CONSOLES),
    BLUE_CONSOLE(ConsoleBlock.CONSOLE_BLUE.getKey(), RecipeCategory.CONSOLES),
    PURPLE_CONSOLE(ConsoleBlock.CONSOLE_PURPLE.getKey(), RecipeCategory.CONSOLES),
    MAGENTA_CONSOLE(ConsoleBlock.CONSOLE_MAGENTA.getKey(), RecipeCategory.CONSOLES),
    PINK_CONSOLE(ConsoleBlock.CONSOLE_PINK.getKey(), RecipeCategory.CONSOLES),
    BROWN_CONSOLE(ConsoleBlock.CONSOLE_BROWN.getKey(), RecipeCategory.CONSOLES),
    RUSTIC_CONSOLE(ConsoleBlock.CONSOLE_RUSTIC.getKey(), RecipeCategory.CONSOLES),
    // bow ties
    WHITE_BOW_TIE(LeatherHelmet.BOWTIE_WHITE.getKey()),
    ORANGE_BOW_TIE(LeatherHelmet.BOWTIE_ORANGE.getKey()),
    MAGENTA_BOW_TIE(LeatherHelmet.BOWTIE_MAGENTA.getKey()),
    LIGHT_BLUE_BOW_TIE(LeatherHelmet.BOWTIE_LIGHT_BLUE.getKey()),
    YELLOW_BOW_TIE(LeatherHelmet.BOWTIE_YELLOW.getKey()),
    LIME_BOW_TIE(LeatherHelmet.BOWTIE_LIME.getKey()),
    PINK_BOW_TIE(LeatherHelmet.BOWTIE_PINK.getKey()),
    GREY_BOW_TIE(LeatherHelmet.BOWTIE_GRAY.getKey()),
    LIGHT_GREY_BOW_TIE(LeatherHelmet.BOWTIE_LIGHT_GRAY.getKey()),
    CYAN_BOW_TIE(LeatherHelmet.BOWTIE_CYAN.getKey()),
    PURPLE_BOW_TIE(LeatherHelmet.BOWTIE_PURPLE.getKey()),
    BLUE_BOW_TIE(LeatherHelmet.BOWTIE_BLUE.getKey()),
    BROWN_BOW_TIE(LeatherHelmet.BOWTIE_BROWN.getKey()),
    GREEN_BOW_TIE(LeatherHelmet.BOWTIE_GREEN.getKey()),
    RED_BOW_TIE(LeatherHelmet.BOWTIE_RED.getKey(), RecipeCategory.ACCESSORIES),
    BLACK_BOW_TIE(LeatherHelmet.BOWTIE_BLACK.getKey()),
    THREE_D_GLASSES(LeatherHelmet.THREE_D_GLASSES.getKey(), RecipeCategory.ACCESSORIES),
    // module recipes
    VORTEX_MANIPULATOR(Clock.VORTEX_MANIPULATOR.getKey(), RecipeCategory.ACCESSORIES),
    SONIC_BLASTER(GoldenHoe.BLASTER.getKey(), RecipeCategory.ACCESSORIES),
    BLASTER_BATTERY(Bucket.BLASTER_BATTERY.getKey(), RecipeCategory.MISC),
    LANDING_PAD(null, RecipeCategory.ACCESSORIES),
    JUDOON_AMMUNITION(Arrow.JUDOON_AMMO.getKey(), RecipeCategory.MISC),
    K9(Bone.K9.getKey(), RecipeCategory.MISC),
    // custom block recipes start here
    UNTEMPERED_SCHISM(AncientDebris.UNTEMPERED_SCHISM_BLOCK.getKey(), RecipeCategory.ITEMS),
    GROW(SeedBlock.GROW.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    BLUE_BOX(Wool.BLUE_BOX.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    COG(Wool.COG.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    HEXAGON(Wool.HEXAGON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL(Wool.ROUNDEL.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL_OFFSET(Wool.ROUNDEL_OFFSET.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    THE_MOMENT(Wool.THE_MOMENT.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_BULB(RedstoneLamp.BULB_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC(SeaLantern.CLASSIC_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC_OFFSET(SeaLantern.CLASSIC_OFFSET_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TENTH(RedstoneLamp.TENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_ELEVENTH(RedstoneLamp.ELEVENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TWELFTH(SeaLantern.TWELFTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_THIRTEENTH(SeaLantern.THIRTEENTH_ON.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    DOOR(IronDoor.TARDIS_DOOR_0.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    BONE_DOOR(BirchDoor.BONE_DOOR.getKey(), RecipeCategory.CUSTOM_BLOCKS),
    CLASSIC_DOOR(CherryDoor.CLASSIC_DOOR.getKey(), RecipeCategory.CUSTOM_BLOCKS),
//    HANDBRAKE(1001, RecipeCategory.CUSTOM_BLOCKS),
//    THROTTLE(1001, RecipeCategory.CUSTOM_BLOCKS),
    // unshaped recipes start here
    BIOME_STORAGE_DISK(MusicDisc.BIOME_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    BOWL_OF_CUSTARD(Food.BOWL_OF_CUSTARD.getKey(), RecipeCategory.FOOD),
    PLAYER_STORAGE_DISK(MusicDisc.PLAYER_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    PRESET_STORAGE_DISK(MusicDisc.PRESET_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    SAVE_STORAGE_DISK(MusicDisc.SAVE_DISK.getKey(), RecipeCategory.STORAGE_DISKS),
    TARDIS_SCHEMATIC_WAND(Bone.WAND.getKey(), RecipeCategory.MISC),
    // jelly babies
    VANILLA_JELLY_BABY(MelonSlice.JELLY_BABY_WHITE.getKey()),
    ORANGE_JELLY_BABY(MelonSlice.JELLY_BABY_ORANGE.getKey(), RecipeCategory.FOOD),
    WATERMELON_JELLY_BABY(MelonSlice.JELLY_BABY_MAGENTA.getKey()),
    BUBBLEGUM_JELLY_BABY(MelonSlice.JELLY_BABY_LIGHT_BLUE.getKey()),
    LEMON_JELLY_BABY(MelonSlice.JELLY_BABY_YELLOW.getKey()),
    LIME_JELLY_BABY(MelonSlice.JELLY_BABY_LIME.getKey()),
    STRAWBERRY_JELLY_BABY(MelonSlice.JELLY_BABY_PINK.getKey()),
    EARL_GREY_JELLY_BABY(MelonSlice.JELLY_BABY_GRAY.getKey()),
    VODKA_JELLY_BABY(MelonSlice.JELLY_BABY_LIGHT_GRAY.getKey()),
    ISLAND_PUNCH_JELLY_BABY(MelonSlice.JELLY_BABY_CYAN.getKey()),
    GRAPE_JELLY_BABY(MelonSlice.JELLY_BABY_PURPLE.getKey()),
    BLUEBERRY_JELLY_BABY(MelonSlice.JELLY_BABY_BLUE.getKey()),
    CAPPUCCINO_JELLY_BABY(MelonSlice.JELLY_BABY_BROWN.getKey()),
    APPLE_JELLY_BABY(MelonSlice.JELLY_BABY_GREEN.getKey()),
    RASPBERRY_JELLY_BABY(MelonSlice.JELLY_BABY_RED.getKey()),
    LICORICE_JELLY_BABY(MelonSlice.JELLY_BABY_BLACK.getKey()),
    // sonic upgrades
    ADMIN_UPGRADE(GlowstoneDust.ADMIN.getKey(), RecipeCategory.SONIC_UPGRADES),
    BIO_SCANNER_UPGRADE(GlowstoneDust.BIO.getKey(), RecipeCategory.SONIC_UPGRADES),
    BRUSH_UPGRADE(GlowstoneDust.BRUSH.getKey(), RecipeCategory.SONIC_UPGRADES),
    REDSTONE_UPGRADE(GlowstoneDust.REDSTONE.getKey(), RecipeCategory.SONIC_UPGRADES),
    DIAMOND_UPGRADE(GlowstoneDust.DIAMOND.getKey(), RecipeCategory.SONIC_UPGRADES),
    EMERALD_UPGRADE(GlowstoneDust.EMERALD.getKey(), RecipeCategory.SONIC_UPGRADES),
    PAINTER_UPGRADE(GlowstoneDust.PAINTER.getKey(), RecipeCategory.SONIC_UPGRADES),
    IGNITE_UPGRADE(GlowstoneDust.IGNITE.getKey(), RecipeCategory.SONIC_UPGRADES),
    PICKUP_ARROWS_UPGRADE(GlowstoneDust.PICKUP.getKey(), RecipeCategory.SONIC_UPGRADES),
    KNOCKBACK_UPGRADE(GlowstoneDust.KNOCKBACK.getKey(), RecipeCategory.SONIC_UPGRADES),
    CONVERSION_UPGRADE(GlowstoneDust.CONVERSION.getKey(), RecipeCategory.SONIC_UPGRADES),
    // planet items
    ACID_BUCKET(WaterBucket.ACID_BUCKET.getKey(), RecipeCategory.UNCRAFTABLE),
    RUST_BUCKET(LavaBucket.RUST_BUCKET.getKey(), RecipeCategory.UNCRAFTABLE),
    // chemistry
    ATOMIC_ELEMENTS(ChemistryItem.CREATIVE.getKey(), RecipeCategory.CHEMISTRY),
    CHEMICAL_COMPOUNDS(ChemistryItem.COMPOUND.getKey(), RecipeCategory.CHEMISTRY),
    LAB_TABLE(ChemistryItem.LAB.getKey(), RecipeCategory.CHEMISTRY),
    PRODUCT_CRAFTING(ChemistryItem.PRODUCT.getKey(), RecipeCategory.CHEMISTRY),
    MATERIAL_REDUCER(ChemistryItem.REDUCER.getKey(), RecipeCategory.CHEMISTRY),
    ELEMENT_CONSTRUCTOR(ChemistryItem.CONSTRUCTOR.getKey(), RecipeCategory.CHEMISTRY),
    BLUE_LAMP(RedstoneLamp.BLUE_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    GREEN_LAMP(RedstoneLamp.GREEN_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    PURPLE_LAMP(RedstoneLamp.PURPLE_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    RED_LAMP(RedstoneLamp.RED_LAMP.getKey(), RecipeCategory.CHEMISTRY),
    HEAT_BLOCK(ChemistryItem.HEAT_BLOCK.getKey(), RecipeCategory.CHEMISTRY),
    BALLOON(Cornflower.RED_BALLOON.getKey(), RecipeCategory.CHEMISTRY),
    BLEACH(ChemistryItem.BLEACH.getKey(), RecipeCategory.CHEMISTRY),
    GLOW_STICK(GlowStick.ORANGE_GLOW_STICK.getKey(), RecipeCategory.CHEMISTRY),
    ICE_BOMB(ChemistryItem.ICE_BOMB.getKey(), RecipeCategory.CHEMISTRY),
    SPARKLER(EndRod.SPARKLER_PURPLE.getKey(), RecipeCategory.CHEMISTRY),
    SUPER_FERTILISER(ChemistryItem.SUPER_FERTILISER.getKey(), RecipeCategory.CHEMISTRY),
    // microscope
    COMPUTER_MONITOR(ChemistryItem.COMPUTER_MONITOR.getKey(), RecipeCategory.MICROSCOPE),
    ELECTRON_MICROSCOPE(ChemistryItem.ELECTRON_MICROSCOPE.getKey(), RecipeCategory.MICROSCOPE),
    FILING_CABINET(ChemistryItem.FILING_CABINET_OPEN.getKey(), RecipeCategory.MICROSCOPE),
    MICROSCOPE(ChemistryItem.MICROSCOPE.getKey(), RecipeCategory.MICROSCOPE),
    SLIDE_RACK(ChemistryItem.SLIDE_RACK.getKey(), RecipeCategory.MICROSCOPE),
    TELESCOPE(ChemistryItem.TELESCOPE.getKey(), RecipeCategory.MICROSCOPE),
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
