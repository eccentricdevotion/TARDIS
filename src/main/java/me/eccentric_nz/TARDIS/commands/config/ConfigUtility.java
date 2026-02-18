package me.eccentric_nz.TARDIS.commands.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigUtility {

    public final static HashMap<String, String> firstsStr = new HashMap<>() {
        {
            put("inventory_group", "creation");
            put("seed_block.easy", "creation");
            put("seed_block.normal", "creation");
            put("seed_block.hard", "creation");
        }
    };
    public final static HashMap<String, String> firstsBool = new HashMap<>() {
        {
            put("3d_doors", "allow");
            put("abandon", "");
            put("achievements", "allow");
            put("add_lights", "allow");
            put("add_server_link", "preferences");
            put("admin_bypass", "allow");
            put("all_blocks", "allow");
            put("allow_end_after_visit", "travel");
            put("allow_nether_after_visit", "travel");
            put("animal_spawners", "allow");
            put("animated_door", "police_box");
            put("any_key", "preferences");
            put("archive", "");
            put("autonomous", "allow");
            put("biome_reader", "difficulty");
            put("blueprints", "modules");
            put("chameleon", "travel");
            put("charge", "sonic");
            put("check_blocks_before_upgrade", "desktop");
            put("check_for_home", "creation");
            put("chemistry", "modules");
            put("circuits", "difficulty");
            put("create_worlds", "creation");
            put("create_worlds_with_perms", "creation");
            put("damage", "circuits");
            put("debug", "");
            put("default_world", "creation");
            put("disk_in_hand_for_write", "difficulty");
            put("disks", "difficulty");
            put("dynamic_lamps", "allow");
            put("emergency_npc", "allow");
            put("exile", "travel");
            put("external_gravity", "allow");
            put("furnace_particles", "artron_furnace");
            put("give_key", "travel");
            put("guardians", "allow");
            put("hads", "allow");
            put("handles", "allow");
            put("include_default_world", "travel");
            put("invisibility", "allow");
            put("keep_night", "creation");
            put("land_on_water", "travel");
            put("load_shells", "police_box");
            put("mapping", "modules");
            put("materialise", "police_box");
            put("mob_farming", "allow");
            put("name_tardis", "police_box");
            put("nerf_pistons.enabled", "preferences");
            put("nerf_pistons.only_tardis_worlds", "preferences");
            put("nether", "travel");
            put("no_coords", "preferences");
            put("no_creative_condense", "preferences");
            put("no_destination_malfunctions", "travel");
            put("no_enchanted_condense", "preferences");
            put("open_door_policy", "preferences");
            put("particles", "eye_of_harmony");
            put("per_world_perms", "travel");
            put("perception_filter", "allow");
            put("power_down", "allow");
            put("power_down_on_quit", "allow");
            put("previews", "desktop");
            put("reduce_count", "abandon");
            put("regeneration", "modules");
            put("render_entities", "preferences");
            put("respect_chunky_border", "preferences");
            put("respect_grief_prevention", "preferences");
            put("respect_worldborder", "preferences");
            put("return_room_seed", "growth");
            put("rooms_require_blocks", "growth");
            put("seed_block.crafting", "creation");
            put("seed_block.legacy", "creation");
            put("sfx", "allow");
            put("shop", "modules");
            put("sonic_blaster", "modules");
            put("spawn_eggs", "allow");
            put("spawn_random_monsters", "preferences");
            put("stattenheim_remote", "difficulty");
            put("strike_lightning", "preferences");
            put("switch_resource_packs", "");
            put("system_upgrades", "difficulty");
            put("tardis_locator", "difficulty");
            put("terminal.redefine", "travel");
            put("the_end", "travel");
            put("update.auto_update", "preferences");
            put("update.notify", "preferences");
            put("use_default_condensables", "preferences");
            put("use_nick", "police_box");
            put("use_worldguard", "preferences");
            put("view_interior", "police_box");
            put("view_interior_uses_console_size", "police_box");
            put("village_travel", "allow");
            put("vortex_manipulator", "modules");
            put("weather_set", "allow");
            put("weeping_angels", "modules");
            put("wg_flag_set", "allow");
            put("zero_room", "allow");
        }
    };
    public final static HashMap<String, String> firstsInt = new HashMap<>() {
        {
            put("ars_limit", "growth");
            put("block_change_percent", "desktop");
            put("border_radius", "creation");
            put("charge_interval", "sonic");
            put("charge_level", "sonic");
            put("conversion_radius", "sonic");
            put("count", "creation");
            put("delay_factor", "growth");
            put("force_field", "allow");
            put("freeze_cooldown", "sonic");
            put("grace_period", "travel");
            put("gravity_max_distance", "growth");
            put("gravity_max_velocity", "growth");
            put("hads_damage", "preferences");
            put("hads_distance", "preferences");
            put("heal_speed", "preferences");
            put("malfunction", "preferences");
            put("malfunction_end", "preferences");
            put("malfunction_nether", "preferences");
            put("min_time", "arch");
            put("random_attempts", "travel");
            put("random_circuit.x", "travel");
            put("random_circuit.z", "travel");
            put("room_speed", "growth");
            put("rooms_condenser_percent", "growth");
            put("sfx_volume", "preferences");
            put("terminal_step", "travel");
            put("timeout", "travel");
            put("timeout_height", "travel");
            put("tips_limit", "creation");
            put("tp_radius", "travel");
            put("update_period", "mapping");
            put("updates_per_tick", "mapping");
            put("usage", "sonic");
            put("uses.ars", "circuits");
            put("uses.chameleon", "circuits");
            put("uses.input", "circuits");
            put("uses.invisibility", "circuits");
            put("uses.materialisation", "circuits");
            put("uses.memory", "circuits");
            put("uses.randomiser", "circuits");
            put("uses.scanner", "circuits");
            put("uses.temporal", "circuits");
            put("wall_data", "police_box");
            put("wall_id", "police_box");
        }
    };
    public final static Set<String> firstsIntArtron = Set.of(
            "autonomous", "backdoor", "comehere", "creeper_recharge", "full_charge", "hide", "jettison",
            "lightning_recharge", "nether_min", "player", "random", "recharge_distance", "the_end_min", "travel");


    public static Set<String> combineLists() {
        Set<String> newList = new HashSet<>(
                firstsStr.size()
                        + firstsBool.size()
                        + firstsInt.size()
                        + firstsIntArtron.size()
        );
        newList.addAll(firstsStr.keySet());
        newList.addAll(firstsBool.keySet());
        newList.addAll(firstsInt.keySet());
        newList.addAll(firstsIntArtron);
        return newList;
    }
}
