package me.eccentric_nz.TARDIS.brigadier;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;

import java.util.List;

public class BrigadierCommandRegister {

    private final ReloadableRegistrarEvent<Commands> commands;

    public BrigadierCommandRegister(ReloadableRegistrarEvent<Commands> commands) {
        this.commands = commands;
    }

    public void addAll() {
        commands.registrar().register(new AdminCommandNode().build(), List.of("tadmin"));
        commands.registrar().register(new SchematicCommandNode().build(), List.of("ts", "tschematic"));
    }
}
