package me.eccentric_nz.TARDIS.commands.utils;

import java.util.ArrayList;
import java.util.List;

public class Arguments {

    private final List<String> arguments = new ArrayList<>();

    /**
     * Adds an argument
     *
     * @param argument Argument to add
     */
    public void addArgument(String argument) {
        arguments.add(argument);
    }

    /**
     * Gets an immutable (safe) copy of all the arguments
     *
     * @return Immutable copy of arguments
     */
    public List<String> getArguments() {
        return new ArrayList<>(arguments);
    }
}
