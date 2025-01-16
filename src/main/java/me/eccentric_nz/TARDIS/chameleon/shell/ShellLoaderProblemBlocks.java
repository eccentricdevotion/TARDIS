package me.eccentric_nz.TARDIS.chameleon.shell;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Tag;

public class ShellLoaderProblemBlocks {

    public static final List<Material> DO_FIRST = new ArrayList<>();

    static {
        DO_FIRST.add(Material.LEVER);
        DO_FIRST.add(Material.REDSTONE_TORCH);
        DO_FIRST.add(Material.REDSTONE_WALL_TORCH);
        DO_FIRST.add(Material.TORCH);
        DO_FIRST.add(Material.WALL_TORCH);
        DO_FIRST.addAll(Tag.BUTTONS.getValues());
        DO_FIRST.addAll(Tag.DOORS.getValues());
        DO_FIRST.addAll(Tag.FLOWERS.getValues());
        DO_FIRST.addAll(Tag.TRAPDOORS.getValues());
        DO_FIRST.addAll(Tag.WALL_SIGNS.getValues());
    }
}
