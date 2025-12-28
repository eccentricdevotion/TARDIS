package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.enumeration.Room;
import org.bukkit.Sound;

import java.util.HashMap;

public class ARSSound {

    public static HashMap<Room, Sound> ROOM_SOUNDS = new HashMap<>() {
        {
            put(Room.ALLAY, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM);
            put(Room.APIARY, Sound.ENTITY_BEE_POLLINATE);
            put(Room.AQUARIUM, Sound.ENTITY_FISH_SWIM);
            put(Room.BIRDCAGE, Sound.ENTITY_PARROT_AMBIENT);
            put(Room.FARM, Sound.ENTITY_SHEEP_AMBIENT);
            put(Room.GEODE, Sound.ENTITY_AXOLOTL_IDLE_WATER);
            put(Room.HAPPY, Sound.ENTITY_HAPPY_GHAST_AMBIENT);
            put(Room.BAMBOO, Sound.ENTITY_PANDA_AMBIENT);
            put(Room.HUTCH, Sound.ENTITY_RABBIT_AMBIENT);
            put(Room.IGLOO, Sound.ENTITY_POLAR_BEAR_AMBIENT);
            put(Room.IISTUBIL, Sound.ENTITY_CAMEL_AMBIENT);
            put(Room.LAVA, Sound.ENTITY_STRIDER_AMBIENT);
            put(Room.MANGROVE, Sound.ENTITY_FROG_AMBIENT);
            put(Room.NAUTILUS, Sound.ENTITY_NAUTILUS_AMBIENT);
            put(Room.PEN, Sound.ENTITY_SNIFFER_IDLE);
            put(Room.POOL, Sound.ENTITY_GUARDIAN_AMBIENT);
            put(Room.STABLE, Sound.ENTITY_HORSE_AMBIENT);
            put(Room.STALL, Sound.ENTITY_LLAMA_AMBIENT);
            put(Room.VILLAGE, Sound.ENTITY_VILLAGER_AMBIENT);
        }
    };
}
