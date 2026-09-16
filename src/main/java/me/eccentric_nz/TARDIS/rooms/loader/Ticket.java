package me.eccentric_nz.TARDIS.rooms.loader;

import org.bukkit.World;

import java.util.UUID;

public record Ticket(int id, UUID uuid, World world, int x, int z) {
}
