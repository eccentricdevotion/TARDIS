package me.eccentric_nz.TARDIS.utility;

import java.util.HashMap;

/**
 * All the vanilla gamerules
 */
public class GameRuleConverter {

    public static final HashMap<String, String> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put("allowEnteringNetherUsingPortals", "allow_entering_nether_using_portals");
        OLD_TO_NEW.put("announceAdvancements", "show_advancement_messages");
        OLD_TO_NEW.put("blockExplosionDropDecay", "block_explosion_drop_decay");
        OLD_TO_NEW.put("commandBlocksEnabled", "command_blocks_work");
        OLD_TO_NEW.put("commandBlockOutput", "command_block_output");
        OLD_TO_NEW.put("commandModificationBlockLimit", "max_block_modifications");
        OLD_TO_NEW.put("disableElytraMovementCheck", "elytra_movement_check");
        OLD_TO_NEW.put("disablePlayerMovementCheck", "player_movement_check");
        OLD_TO_NEW.put("disableRaids", "raids");
        OLD_TO_NEW.put("doDaylightCycle", "advance_time");
        OLD_TO_NEW.put("doEntityDrops", "entity_drops");
        OLD_TO_NEW.put("doFireTick", "fire_spread_radius_around_player");
        OLD_TO_NEW.put("allowFireTicksAwayFromPlayer", "fire_spread_radius_around_player");
        OLD_TO_NEW.put("doImmediateRespawn", "immediate_respawn");
        OLD_TO_NEW.put("doInsomnia", "spawn_phantoms");
        OLD_TO_NEW.put("doLimitedCrafting", "limited_crafting");
        OLD_TO_NEW.put("doMobLoot", "mob_drops");
        OLD_TO_NEW.put("doMobSpawning", "spawn_mobs");
        OLD_TO_NEW.put("doPatrolSpawning", "spawn_patrols");
        OLD_TO_NEW.put("doTileDrops", "block_drops");
        OLD_TO_NEW.put("doTraderSpawning", "spawn_wandering_traders");
        OLD_TO_NEW.put("doVinesSpread", "spread_vines");
        OLD_TO_NEW.put("doWardenSpawning", "spawn_wardens");
        OLD_TO_NEW.put("doWeatherCycle", "advance_weather");
        OLD_TO_NEW.put("drowningDamage", "drowning_damage");
        OLD_TO_NEW.put("enderPearlsVanishOnDeath", "ender_pearls_vanish_on_death");
        OLD_TO_NEW.put("fallDamage", "fall_damage");
        OLD_TO_NEW.put("fireDamage", "fire_damage");
        OLD_TO_NEW.put("forgiveDeadPlayers", "forgive_dead_players");
        OLD_TO_NEW.put("freezeDamage", "freeze_damage");
        OLD_TO_NEW.put("globalSoundEvents", "global_sound_events");
        OLD_TO_NEW.put("keepInventory", "keep_inventory");
        OLD_TO_NEW.put("lavaSourceConversion", "lava_source_conversion");
        OLD_TO_NEW.put("locatorBar", "locator_bar");
        OLD_TO_NEW.put("logAdminCommands", "log_admin_commands");
        OLD_TO_NEW.put("maxCommandChainLength", "max_command_sequence_length");
        OLD_TO_NEW.put("maxCommandForkCount", "max_command_forks");
        OLD_TO_NEW.put("maxEntityCramming", "max_entity_cramming");
        OLD_TO_NEW.put("minecartMaxSpeed", "max_minecart_speed");
        OLD_TO_NEW.put("mobExplosionDropDecay", "mob_explosion_drop_decay");
        OLD_TO_NEW.put("mobGriefing", "mob_griefing");
        OLD_TO_NEW.put("naturalRegeneration", "natural_health_regeneration");
        OLD_TO_NEW.put("playersNetherPortalCreativeDelay", "players_nether_portal_creative_delay");
        OLD_TO_NEW.put("playersNetherPortalDefaultDelay", "players_nether_portal_default_delay");
        OLD_TO_NEW.put("playersSleepingPercentage", "players_sleeping_percentage");
        OLD_TO_NEW.put("projectilesCanBreakBlocks", "projectiles_can_break_blocks");
        OLD_TO_NEW.put("pvp", "pvp");
        OLD_TO_NEW.put("randomTickSpeed", "random_tick_speed");
        OLD_TO_NEW.put("reducedDebugInfo", "reduced_debug_info");
        OLD_TO_NEW.put("sendCommandFeedback", "send_command_feedback");
        OLD_TO_NEW.put("showDeathMessages", "show_death_messages");
        OLD_TO_NEW.put("snowAccumulationHeight", "max_snow_accumulation_height");
        OLD_TO_NEW.put("spawnerBlocksEnabled", "spawner_blocks_work");
        OLD_TO_NEW.put("spawnMonsters", "spawn_monsters");
        OLD_TO_NEW.put("spawnRadius", "respawn_radius");
        OLD_TO_NEW.put("spectatorsGenerateChunks", "spectators_generate_chunks");
        OLD_TO_NEW.put("tntExplodes", "tnt_explodes");
        OLD_TO_NEW.put("tntExplosionDropDecay", "tnt_explosion_drop_decay");
        OLD_TO_NEW.put("universalAnger", "universal_anger");
        OLD_TO_NEW.put("waterSourceConversion", "water_source_conversion");
    }
}
