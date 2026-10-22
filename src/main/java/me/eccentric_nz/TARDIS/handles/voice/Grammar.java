package me.eccentric_nz.TARDIS.handles.voice;

import com.google.gson.Gson;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinationNames;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Grammar {

    public static String getJSON(UUID uuid) {
        List<String> words = new ArrayList<>();
        for (String k : TARDIS.plugin.getHandlesConfig().getConfigurationSection("core-commands").getKeys(true)) {
            if (k.contains(".")) {
                // split it, and only use the second half
                String[] split = k.split("\\.");
                words.add(split[1]);
            } else {
                words.add(k);
            }
        }
        words.add("a");
        words.add("about");
        words.add("after");
        words.add("all");
        words.add("also");
        words.add("an");
        words.add("and");
        words.add("any");
        words.add("as");
        words.add("at");
        words.add("back");
        words.add("be");
        words.add("because");
        words.add("build");
        words.add("but");
        words.add("by");
        words.add("can");
        words.add("come");
        words.add("could");
        words.add("create");
        words.add("day");
        words.add("destination");
        words.add("do");
        words.add("even");
        words.add("face");
        words.add("facing");
        words.add("find");
        words.add("first");
        words.add("for");
        words.add("from");
        words.add("get");
        words.add("give");
        words.add("go");
        words.add("good");
        words.add("handbrake");
        words.add("handles");
        words.add("have");
        words.add("he");
        words.add("her");
        words.add("hey handles");
        words.add("hey");
        words.add("him");
        words.add("his");
        words.add("how");
        words.add("i");
        words.add("if");
        words.add("in");
        words.add("into");
        words.add("is");
        words.add("it");
        words.add("its");
        words.add("just");
        words.add("know");
        words.add("like");
        words.add("location");
        words.add("look");
        words.add("make");
        words.add("me");
        words.add("most");
        words.add("my");
        words.add("new");
        words.add("no");
        words.add("not");
        words.add("now");
        words.add("of");
        words.add("off");
        words.add("on");
        words.add("on");
        words.add("one");
        words.add("only");
        words.add("or");
        words.add("other");
        words.add("our");
        words.add("out");
        words.add("over");
        words.add("park");
        words.add("people");
        words.add("place");
        words.add("point");
        words.add("remind");
        words.add("save");
        words.add("saved");
        words.add("say");
        words.add("see");
        words.add("she");
        words.add("so");
        words.add("some");
        words.add("take");
        words.add("tardis");
        words.add("than");
        words.add("that");
        words.add("the");
        words.add("their");
        words.add("them");
        words.add("then");
        words.add("there");
        words.add("these");
        words.add("they");
        words.add("think");
        words.add("this");
        words.add("time");
        words.add("to");
        words.add("two");
        words.add("up");
        words.add("us");
        words.add("use");
        words.add("want");
        words.add("way");
        words.add("we");
        words.add("well");
        words.add("what");
        words.add("when");
        words.add("which");
        words.add("who");
        words.add("will");
        words.add("with");
        words.add("work");
        words.add("would");
        words.add("year");
        words.add("you");
        words.add("your");
        // add biome names
        for (Biome biome : RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)) {
            String b = biome.key().value();
            words.add(b.replace("_", " "));
        }
        // add players save names
        ResultSetDestinationNames rsd = new ResultSetDestinationNames(TARDIS.plugin);
        if (rsd.fromUUID(uuid.toString())) {
            words.addAll(rsd.getNames());
        }
        // add tardis areas
        ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, null, true, true);
        if (rsa.resultSet()) {
            words.addAll(rsa.getNames());
        }
        return new Gson().toJson(words);
    }
}
