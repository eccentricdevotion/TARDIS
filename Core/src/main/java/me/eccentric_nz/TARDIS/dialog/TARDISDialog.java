package me.eccentric_nz.TARDIS.dialog;

import com.google.gson.*;
import me.eccentric_nz.TARDIS.info.TISCategory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.MultiActionDialog;
import net.md_5.bungee.api.dialog.NoticeDialog;
import net.md_5.bungee.api.dialog.action.ActionButton;
import net.md_5.bungee.api.dialog.action.CustomClickAction;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import net.md_5.bungee.api.dialog.input.TextInput;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TARDISDialog {

    public Dialog createCategoryDialog() {
        DialogBase base = new DialogBase(new ComponentBuilder("TARDIS Information System").build())
                .body(List.of(new PlainMessageBody(new ComponentBuilder("Choose a category below:").build())));
        List<ActionButton> actions = new ArrayList<>();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        for (TISCategory category : TISCategory.values()) {
            JsonPrimitive arr = JsonParser.parseString(gson.toJson("\"" + category.getName() + "\"")).getAsJsonPrimitive();
            CustomClickAction action = new CustomClickAction("categoryform");
            action.additions(arr);
            ActionButton button = new ActionButton(new ComponentBuilder(category.getName()).build(), new ComponentBuilder(category.getLore().replace("~", "\n")).build(), 150, action);
            actions.add(button);
        }
        return new MultiActionDialog(base, actions, 2, null);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Dialog notice = new NoticeDialog(new DialogBase(new ComponentBuilder("Hello").color(ChatColor.RED).build()));
        player.showDialog(notice);
        notice = new NoticeDialog(
                new DialogBase(new ComponentBuilder("Hello").color(ChatColor.RED).build())
                        .inputs(
                                Arrays.asList(
                                        new TextInput("first", new ComponentBuilder("First").build()),
                                        new TextInput("second", new ComponentBuilder("Second").build())
                                )
                        ))
                .action(new ActionButton(new ComponentBuilder("Submit Button").build(), new CustomClickAction("customform")));
        player.sendMessage(new ComponentBuilder("click me").event(new ShowDialogClickEvent(notice)).build().toString());
    }
}
